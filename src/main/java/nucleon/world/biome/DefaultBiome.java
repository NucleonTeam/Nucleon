package nucleon.world.biome;

import com.nukkitx.nbt.NbtMap;
import reactor.util.annotation.NonNull;

public class DefaultBiome extends Biome {

    public final static String ID = "default_positron";

    public DefaultBiome() {
        super(ID);
    }

    @Override
    protected @NonNull NbtMap createNbtMap() {
        return MinecraftBiomeBuilder.create(getId())
                .buildCompoundTag().getNbtMap();
    }
}
