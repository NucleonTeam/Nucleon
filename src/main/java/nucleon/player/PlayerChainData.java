package nucleon.player;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.nukkitx.protocol.bedrock.packet.LoginPacket;
import lombok.Getter;
import lombok.NonNull;
import nucleon.util.SerializedImage;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class PlayerChainData {

    private String username;
    private UUID identity;
    private long clientRandomId;
    private PlayerSkin skin;

    public PlayerChainData(String chainData, String skinData) throws JsonSyntaxException {
        readChainData(chainData);
        readSkinData(skinData);
    }

    private void readChainData(String chainData) throws JsonSyntaxException, IllegalArgumentException {
        var root = JsonParser.parseString(chainData).getAsJsonObject();
        if (!root.has("chain")) throw new IllegalArgumentException("Invalid chain data");
        var chains = root.get("chain").getAsJsonArray();
        if (chains.isEmpty()) throw new IllegalArgumentException("Chain data is empty");

        chains.forEach(chain -> {
            var obj = decode(chain.getAsString());
            if (obj == null || !obj.has("extraData")) return;

            var extraData = obj.get("extraData").getAsJsonObject();
            if (extraData.has("displayName")) username = extraData.get("displayName").getAsString();
            if (extraData.has("identity")) identity = UUID.fromString(extraData.get("identity").getAsString());
        });
    }

    private void readSkinData(String skinData) throws JsonSyntaxException, IllegalArgumentException {
        var data = decode(skinData);
        if (data == null) throw new IllegalArgumentException("Invalid skin data");

        if (data.has("ClientRandomId")) clientRandomId = data.get("ClientRandomId").getAsLong();

        skin = new PlayerSkin();

        if (data.has("SkinId")) skin.setSkinId(data.get("SkinId").getAsString());
        if (data.has("PlayFabId")) skin.setPlayFabId(data.get("PlayFabId").getAsString());
        if (data.has("CapeId")) skin.setCapeId(data.get("CapeId").getAsString());
        if (data.has("PremiumSkin")) skin.setPremium(data.get("PremiumSkin").getAsBoolean());
        if (data.has("PersonaSkin")) skin.setPersona(data.get("PersonaSkin").getAsBoolean());
        if (data.has("CapeOnClassicSkin")) skin.setCapeOnClassic(data.get("CapeOnClassicSkin").getAsBoolean());
        if (data.has("SkinResourcePatch")) skin.setSkinResourcePatch(new String(Base64.getDecoder().decode(data.get("SkinResourcePatch").getAsString()), StandardCharsets.UTF_8));
        if (data.has("SkinGeometryData")) skin.setGeometryData(new String(Base64.getDecoder().decode(data.get("SkinGeometryData").getAsString()), StandardCharsets.UTF_8));
        if (data.has("SkinAnimationData")) skin.setAnimationData(new String(Base64.getDecoder().decode(data.get("SkinAnimationData").getAsString()), StandardCharsets.UTF_8));
        if (data.has("SkinColor")) skin.setSkinColor(data.get("SkinColor").getAsString());
        if (data.has("ArmSize")) skin.setArmSize(data.get("ArmSize").getAsString());

        skin.setSkinData(decodeImage(data, "Skin"));
        skin.setCapeData(decodeImage(data, "Cape"));

        if (data.has("AnimatedImageData")) {
            data.get("AnimatedImageData").getAsJsonArray().forEach(object -> {
                var obj = object.getAsJsonObject();

                skin.getAnimations().add(new PlayerSkin.Animation(
                        SerializedImage.create(
                                obj.get("ImageWidth").getAsInt(),
                                obj.get("ImageHeight").getAsInt(),
                                Base64.getDecoder().decode(obj.get("Image").getAsString())
                        ),
                        obj.get("Type").getAsInt(),
                        obj.get("Frames").getAsFloat(),
                        obj.get("AnimationExpression").getAsInt()
                ));
            });
        }

        if (data.has("PersonaPieces")) {
            data.get("PersonaPieces").getAsJsonArray().forEach(object -> {
                var obj = object.getAsJsonObject();

                skin.getPersonaPieces().add(new PlayerSkin.Piece(
                        obj.get("PieceId").getAsString(),
                        obj.get("PieceType").getAsString(),
                        obj.get("PackId").getAsString(),
                        obj.get("IsDefault").getAsBoolean(),
                        obj.get("ProductId").getAsString()
                ));
            });
        }

        if (data.has("PieceTintColors")) {
            data.get("PieceTintColors").getAsJsonArray().forEach(object -> {
                var obj = object.getAsJsonObject();

                skin.getTintColors().add(new PlayerSkin.PieceTint(
                        obj.get("PieceType").getAsString(),
                        obj.get("Colors").getAsJsonArray().asList().stream().map(JsonElement::getAsString).collect(Collectors.toList())
                ));
            });
        }
    }

    private JsonObject decode(String input) {
        String[] parts = input.split("\\.");
        if (parts.length < 2) return null;

        return JsonParser.parseString(new String(Base64.getDecoder().decode(parts[1]), StandardCharsets.UTF_8)).getAsJsonObject();
    }

    private static @NonNull SerializedImage decodeImage(JsonObject root, String name) {
        if (root.has(name + "Data")) {
            byte[] skinImage = Base64.getDecoder().decode(root.get(name + "Data").getAsString());

            if (root.has(name + "ImageHeight") && root.has(name + "ImageWidth")) {

                return SerializedImage.create(
                        root.get(name + "ImageWidth").getAsInt(),
                        root.get(name + "ImageHeight").getAsInt(),
                        skinImage
                );
            } else {
                return SerializedImage.create(skinImage);
            }
        }

        return SerializedImage.EMPTY;
    }

    public static @NonNull PlayerChainData read(@NonNull LoginPacket packet) throws IllegalArgumentException {
        try {
            return new PlayerChainData(packet.getChainData().toString(), packet.getSkinData().toString());
        } catch (JsonSyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
