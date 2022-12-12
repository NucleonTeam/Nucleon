package nucleon.player;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nukkitx.protocol.bedrock.packet.LoginPacket;
import lombok.Getter;
import lombok.NonNull;
import nucleon.util.SerializedImage;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class PlayerChainData {

    private String username;
    private UUID identity;
    private long clientRandomId;
    private PlayerSkin skin;
    private String xboxId;
    private boolean xboxAuthed;
    private String serverAddress;
    private String deviceModel;
    private DeviceOS deviceOS;
    private String deviceId;
    private String gameVersion;
    private int guiScale;
    private String languageCode;
    private int currentInputMode;
    private int defaultInputMode;
    private int uiProfile;
    private String capeData;
    private String identityPublicKey;

    public PlayerChainData(String chainData, String skinData) throws JsonSyntaxException {
        readChainData(chainData);
        readSkinData(skinData);
    }

    private void readChainData(String chainData) throws JsonSyntaxException, IllegalArgumentException {
        var root = JsonParser.parseString(chainData).getAsJsonObject();
        if (!root.has("chain")) throw new IllegalArgumentException("Invalid chain data");
        var chainsNode = root.get("chain").getAsJsonArray();
        if (chainsNode.isEmpty()) throw new IllegalArgumentException("Chain data is empty");

        var chains = chainsNode.asList().stream().map(JsonElement::getAsString).collect(Collectors.toList());
        chains.forEach(chain -> {
            var obj = decode(chain);

            if (obj == null) return;

            if (obj.has("identityPublicKey")) identityPublicKey = obj.get("identityPublicKey").getAsString();

            if (!obj.has("extraData")) return;

            var extraData = obj.get("extraData").getAsJsonObject();
            if (extraData.has("displayName")) username = extraData.get("displayName").getAsString();
            if (extraData.has("identity")) identity = UUID.fromString(extraData.get("identity").getAsString());
            if (extraData.has("XUID")) xboxId = extraData.get("XUID").getAsString();
        });

        try {
            xboxAuthed = Verifier.verify(chains);
        } catch (Exception ex) {
            xboxAuthed = false;
        }
    }

    private void readSkinData(String skinData) throws JsonSyntaxException, IllegalArgumentException {
        var data = decode(skinData);
        if (data == null) throw new IllegalArgumentException("Invalid skin data");

        if (data.has("ClientRandomId")) clientRandomId = data.get("ClientRandomId").getAsLong();
        if (data.has("ServerAddress")) serverAddress = data.get("ServerAddress").getAsString();
        if (data.has("DeviceModel")) deviceModel = data.get("DeviceModel").getAsString();
        if (data.has("DeviceOS")) deviceOS = DeviceOS.of(data.get("DeviceOS").getAsInt());
        if (data.has("DeviceId")) deviceId = data.get("DeviceId").getAsString();
        if (data.has("GameVersion")) gameVersion = data.get("GameVersion").getAsString();
        if (data.has("GuiScale")) guiScale = data.get("GuiScale").getAsInt();
        if (data.has("LanguageCode")) languageCode = data.get("LanguageCode").getAsString();
        if (data.has("CurrentInputMode")) currentInputMode = data.get("CurrentInputMode").getAsInt();
        if (data.has("DefaultInputMode")) defaultInputMode = data.get("DefaultInputMode").getAsInt();
        if (data.has("UIProfile")) uiProfile = data.get("UIProfile").getAsInt();
        if (data.has("CapeData")) capeData = data.get("CapeData").getAsString();

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

    @Getter
    public enum DeviceOS {
        UNKNOWN(-1, "Unknown"),
        ANDROID(1, "Android"),
        IOS(2, "IOS"),
        OSX(3, "OSX"),
        AMAZON(4, "Amazon"),
        GEAR_VR(5, "Gear VR"),
        HOLOLENS(6, "HoloLens"),
        WINDOWS_10(7, "Windows 10"),
        WINDOWS_32(8, "Windows 32"),
        DEDICATED(9, "Dedicated"),
        TV_OS(10, "TVOS"),
        PLAYSTATION(11, "PlayStation"),
        NINTENDO(12, "Nintendo"),
        XBOX(13, "Xbox"),
        WINDOWS_PHONE(14, "Windows Phone");

        private final int id;
        private final String name;

        DeviceOS(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public static DeviceOS of(int id) {
            return Arrays.stream(DeviceOS.values())
                    .filter(device -> device.getId() == id)
                    .findFirst()
                    .orElse(DeviceOS.UNKNOWN);
        }
    }

    public static class Verifier {

        private static final String MOJANG_PUBLIC_KEY_BASE64 = "MHYwEAYHKoZIzj0CAQYFK4EEACIDYgAE8ELkixyLcwlZryUQcu1TvPOmI2B7vX83ndnWRUaXm74wFfa5f/lwQNTfrLVHa2PmenpGI6JhIMUJaWZrjmMj90NoKNFSNBuKdm8rYiXsfaz3K36x/1U26HpG0ZxK/V1V";
        private static final PublicKey MOJANG_PUBLIC_KEY;

        static {
            try {
                MOJANG_PUBLIC_KEY = generateKey(MOJANG_PUBLIC_KEY_BASE64);
            } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                throw new AssertionError(e);
            }
        }

        private Verifier() {

        }

        private static ECPublicKey generateKey(String base64) throws NoSuchAlgorithmException, InvalidKeySpecException {
            return (ECPublicKey) KeyFactory.getInstance("EC").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(base64)));
        }

        public static boolean verify(List<String> chains) throws Exception {
            ECPublicKey lastKey = null;
            boolean mojangKeyVerified = false;
            Iterator<String> iterator = chains.iterator();
            while (iterator.hasNext()) {
                JWSObject jws = JWSObject.parse(iterator.next());

                URI x5u = jws.getHeader().getX509CertURL();
                if (x5u == null) return false;

                ECPublicKey expectedKey = generateKey(x5u.toString());
                // First key is self-signed
                if (lastKey == null) lastKey = expectedKey;
                else if (!lastKey.equals(expectedKey)) return false;

                if (!jws.verify(new ECDSAVerifier(lastKey))) return false;

                if (mojangKeyVerified) return !iterator.hasNext();
                if (lastKey.equals(MOJANG_PUBLIC_KEY)) mojangKeyVerified = true;

                Map<String, Object> payload = jws.getPayload().toJSONObject();
                Object base64key = payload.get("identityPublicKey");
                if (!(base64key instanceof String)) throw new RuntimeException("No key found");
                lastKey = generateKey((String) base64key);
            }
            return mojangKeyVerified;
        }
    }
}
