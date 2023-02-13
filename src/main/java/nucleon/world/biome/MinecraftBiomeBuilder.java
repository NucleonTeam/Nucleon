package nucleon.world.biome;

import lombok.Getter;
import nucleon.nbt.CompoundTag;
import nucleon.nbt.Tag;
import nucleon.util.Args;
import reactor.util.annotation.NonNull;

import java.util.ArrayList;

@Getter
public final class MinecraftBiomeBuilder {

    private final String biomeId;
    private boolean canRain = true;
    private float ashSporesQuantity = 0f;
    private float blueSporesQuantity = 0f;
    private float redSporesQuantity = 0f;

    private MinecraftBiomeBuilder(@NonNull String biomeId) {
        this.biomeId = biomeId;
    }

    public static @NonNull MinecraftBiomeBuilder create(@NonNull String biomeId) {
        return new MinecraftBiomeBuilder(biomeId);
    }

    public @NonNull CompoundTag buildCompoundTag() {
        var climate = new CompoundTag()
                .set(Tag.Float("ash"), 0f)
                .set(Tag.Float("red_spores"), 0f)
                .set(Tag.Float("blue_spores"), 0f)
                .set(Tag.Float("white_ash"), ashSporesQuantity)
                .set(Tag.Float("snow_accumulation_max"), 0.125f)
                .set(Tag.Float("snow_accumulation_min"), 0f);

        var consolidatedFeatures = new CompoundTag()
                .setList(Tag.Compound("features"), new ArrayList<>());

        var multiNoiseGenerationRules = new CompoundTag()
                .set(Tag.Float("target_altitude"), 0f)
                .set(Tag.Float("target_humidity"), 0f)
                .set(Tag.Float("target_temperature"), 0f)
                .set(Tag.Float("target_weirdness"), 0f)
                .set(Tag.Float("weight"), 0f);

        var overworldGenerationRules = new CompoundTag()
                .setList(Tag.Compound("generate_for_climates"), new ArrayList<>())
                .setList(Tag.Compound("hills_transformation"), new ArrayList<>())
                .setList(Tag.Compound("mutate_transformation"), new ArrayList<>());

        return new CompoundTag()
                .set(Tag.Float("ash"), 0f)
                .set(Tag.Float("blue_spores"), 0f)
                .set(Tag.Float("depth"), 0.125f)
                .set(Tag.Float("downfall"), 0.4f)
                .set(Tag.Float("height"), 0.05f)
                .set(Tag.Compound("minecraft:climate"), climate)
                .set(Tag.Compound("minecraft:consolidated_features"), consolidatedFeatures)
                .set(Tag.Compound("minecraft:multinoise_generation_rules"), multiNoiseGenerationRules)
                .set(Tag.Compound("minecraft:overworld_generation_rules"), overworldGenerationRules)
                .set(Tag.Compound("minecraft:surface_parameters"), new CompoundTag())
                .set(Tag.String("name_hash"), biomeId)
                .set(Tag.Boolean("rain"), canRain)
                .set(Tag.Float("red_spores"), 0f)
                .setList(Tag.String("tags"), new ArrayList<>())
                .set(Tag.Float("temperature"), 0.8f)
                .set(Tag.Float("waterColorA"), 0.65f)
                .set(Tag.Float("waterColorB"), 0.9607844f)
                .set(Tag.Float("waterColorG"), 0.6862745f)
                .set(Tag.Float("waterColorR"), 0.26666668f)
                .set(Tag.Float("waterTransparency"), 0.65f)
                .set(Tag.Float("white_ash"), ashSporesQuantity);
    }

    public @NonNull MinecraftBiomeBuilder setCanRain(boolean value) {
        canRain = value;
        return this;
    }

    public @NonNull MinecraftBiomeBuilder setAshSporesQuantity(float value) {
        Args.notNegative(value, "Value");
        ashSporesQuantity = value;
        return this;
    }

    public @NonNull MinecraftBiomeBuilder setRedSporesQuantity(float value) {
        Args.notNegative(value, "Value");
        redSporesQuantity = value;
        return this;
    }

    public @NonNull MinecraftBiomeBuilder setBlueSporesQuantity(float value) {
        Args.notNegative(value, "Value");
        blueSporesQuantity = value;
        return this;
    }
}
