package nucleon.world.biome;

import com.nukkitx.nbt.NbtMap;
import reactor.util.annotation.NonNull;

public abstract class Biome {

    private final String id;
    private final NbtMap nbtMap;

    public Biome(@NonNull String id) {
        this.id = id;
        this.nbtMap = createNbtMap();
    }

    protected abstract @NonNull NbtMap createNbtMap();

    public final @NonNull String getId() {
        return id;
    }

    public final @NonNull NbtMap getNbt() {
        return nbtMap;
    }
}
