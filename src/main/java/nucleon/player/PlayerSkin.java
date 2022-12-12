package nucleon.player;

import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import nucleon.util.SerializedImage;
import org.apache.commons.lang3.ArrayUtils;
import reactor.util.annotation.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Getter
@Setter
public class PlayerSkin {

    private static final int PIXEL_SIZE = 4;

    public static final int SINGLE_SKIN_SIZE = 64 * 32 * PIXEL_SIZE;
    public static final int DOUBLE_SKIN_SIZE = 64 * 64 * PIXEL_SIZE;
    public static final int SKIN_128_64_SIZE = 128 * 64 * PIXEL_SIZE;
    public static final int SKIN_128_128_SIZE = 128 * 128 * PIXEL_SIZE;

    public static final String GEOMETRY_CUSTOM = "{\"geometry\" : {\"default\" : \"" + "geometry.humanoid.custom" + "\"}}";

    private final String id = UUID.randomUUID().toString();
    private String skinId;
    private String playFabId = "";
    private String skinResourcePatch = GEOMETRY_CUSTOM;
    private SerializedImage skinData;
    private final List<Animation> animations = new ArrayList<>();
    private final List<Piece> personaPieces = new ArrayList<>();
    private final List<PieceTint> tintColors = new ArrayList<>();
    private SerializedImage capeData;
    private String geometryData;
    private String animationData;
    private boolean premium;
    private boolean persona;
    private boolean capeOnClassic;
    private boolean primaryUser = true;
    private String capeId;
    private String skinColor = "#0";
    private String armSize = "wide";
    private boolean trusted = true;
    private String geometryDataEngineVersion = "";

    public PlayerSkin() {

    }

    public boolean isValid() {
        return isValidSkin() && isValidResourcePatch();
    }

    private boolean isValidSkin() {
        return skinId != null && !skinId.trim().isEmpty() && skinId.length() < 100 &&
                skinData != null && skinData.getWidth() >= 64 && skinData.getHeight() >= 32 &&
                skinData.getData().length >= SINGLE_SKIN_SIZE &&
                (playFabId == null || playFabId.length() < 100) &&
                (capeId == null || capeId.length() < 100) &&
                (skinColor == null || skinColor.length() < 100) &&
                (armSize == null || armSize.length() < 100) &&
                (geometryDataEngineVersion == null || geometryDataEngineVersion.length() < 100);
    }

    private boolean isValidResourcePatch() {
        if (skinResourcePatch == null || skinResourcePatch.length() > 1000) return false;
        try {
            var geometry = JsonParser.parseString(skinResourcePatch).getAsJsonObject()
                    .get("geometry").getAsJsonObject();

            return geometry.has("default") && geometry.isJsonPrimitive();
        } catch (ClassCastException | NullPointerException e) {
            return false;
        }
    }

    public @NonNull SerializedImage getSkinData() {
        return skinData == null? SerializedImage.EMPTY : skinData;
    }

    public String getSkinId() {
        if (skinId == null) generateSkinId("Custom");
        return skinId;
    }

    public void setSkinId(@Nullable String skinId) {
        if (skinId == null || skinId.trim().isEmpty()) {
            return;
        }

        this.skinId = skinId;
    }

    public void generateSkinId(@NonNull String name) {
        byte[] data = ArrayUtils.addAll(getSkinData().getData(), getSkinResourcePatch().getBytes(StandardCharsets.UTF_8));
        this.skinId = UUID.nameUUIDFromBytes(data) + "." + name;
    }

    public void setSkinData(@NonNull SerializedImage skinData) {
        this.skinData = skinData;
    }

    public void setSkinResourcePatch(@Nullable String skinResourcePatch) {
        if (skinResourcePatch == null || skinResourcePatch.trim().isEmpty()) {
            skinResourcePatch = GEOMETRY_CUSTOM;
        }

        this.skinResourcePatch = skinResourcePatch;
    }

    public void setGeometryName(@Nullable String geometryName) {
        if (geometryName == null || geometryName.trim().isEmpty()) {
            skinResourcePatch = GEOMETRY_CUSTOM;
            return;
        }

        this.skinResourcePatch = "{\"geometry\" : {\"default\" : \"" + geometryName + "\"}}";
    }

    public @NonNull String getSkinResourcePatch() {
        return Objects.requireNonNullElse(this.skinResourcePatch, "");
    }

    public @NonNull SerializedImage getCapeData() {
        return capeData == null? SerializedImage.EMPTY : capeData;
    }

    public @NonNull String getCapeId() {
        return capeId == null? "" : capeId;
    }

    public void setCapeId(@Nullable String capeId) {
        if (capeId == null || capeId.trim().isEmpty()) {
            capeId = null;
        }

        this.capeId = capeId;
    }

    public void setCapeData(@NonNull SerializedImage capeData) {
        this.capeData = capeData;
    }

    public @NonNull String getGeometryData() {
        return geometryData == null? "" : geometryData;
    }

    public void setGeometryData(@NonNull String geometryData) {
        if (geometryData.equals(this.geometryData)) return;

        this.geometryData = geometryData;
    }

    public @NonNull String getAnimationData() {
        return animationData == null? "" : animationData;
    }

    public void setAnimationData(@NonNull String animationData) {
        if (animationData.equals(this.animationData)) return;

        this.animationData = animationData;
    }

    public String getPlayFabId() {
        if (persona && (playFabId == null || playFabId.isEmpty())) {
            try {
                playFabId = skinId.split("-")[5];
            } catch (Exception e) {
                playFabId = id.replace("-", "").substring(16);
            }
        }
        return playFabId;
    }

    @Getter
    public static class Animation {

        private final SerializedImage image;
        private final int type;
        private final float frames;
        private final int expression;

        public Animation(@NonNull SerializedImage image, int type, float frames, int expression) {
            this.image = image;
            this.type = type;
            this.frames = frames;
            this.expression = expression;
        }

        public @NonNull SerializedImage getImage() {
            return image;
        }
    }

    @Getter
    public static class Piece {

        private final String id;
        private final String type;
        private final String packId;
        private final boolean isDefault;
        private final String productId;

        public Piece(String id, String type, String packId, boolean isDefault, String productId) {
            this.id = id;
            this.type = type;
            this.packId = packId;
            this.isDefault = isDefault;
            this.productId = productId;
        }
    }

    @Getter
    public static class PieceTint {

        private final String pieceType;
        private final List<String> colors;

        public PieceTint(@NonNull String pieceType, @NonNull List<String> colors) {
            this.pieceType = pieceType;
            this.colors = Collections.unmodifiableList(colors);
        }
    }
}
