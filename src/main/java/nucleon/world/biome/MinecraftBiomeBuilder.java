package nucleon.world.biome;

import lombok.Getter;
import nucleon.nbt.CompoundTag;
import nucleon.nbt.Tag;
import nucleon.util.Args;
import reactor.util.annotation.NonNull;

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

        return new CompoundTag()
                .set(Tag.String("name_hash"), biomeId)
                .set(Tag.Compound("minecraft:climate"), climate)
                .set(Tag.Compound("minecraft:overworld_generation_rules"), new CompoundTag())
                .set(Tag.Compound("minecraft:surface_parameters"), new CompoundTag())
                .set(Tag.Boolean("rain"), canRain)
                .set(Tag.Float("ash"), ashSporesQuantity)
                .set(Tag.Float("red_spores"), 0f)
                .set(Tag.Float("blue_spores"), 0f);
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
