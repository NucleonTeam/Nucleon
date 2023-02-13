package nucleon.nbt;

import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtMapBuilder;
import reactor.util.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class CompoundTag {

    private final String key;
    private final NbtMapBuilder map;

    public CompoundTag(@NonNull String key, @NonNull NbtMap map) {
        this.key = key;
        this.map = NbtMapBuilder.from(map);
    }

    public CompoundTag(@NonNull NbtMap map) {
        this("", map);
    }

    public CompoundTag() {
        this("", NbtMap.EMPTY);
    }

    public @NonNull NbtMap getNbtMap() {
        return map.build();
    }

    public @NonNull String getKey() {
        return key;
    }

    public <T> @NonNull CompoundTag set(@NonNull Tag<T, ?> key, @NonNull T value) {
        key.write(map, value);
        return this;
    }

    public <T> @NonNull T get(@NonNull Tag<T, ?> key) {
        return key.read(map);
    }

    public <T, V> @NonNull CompoundTag setList(@NonNull Tag<T, V> key, List<V> value) {
        map.putList(key.getKey(), key.getType(), value);
        return this;
    }

    public <T, V> @NonNull List<V> getList(@NonNull Tag<T, V> key) {
        return map.build().getList(key.getKey(), key.getType(), new ArrayList<>());
    }

    public @NonNull CompoundTag remove(@NonNull Tag<?, ?> key) {
        map.remove(key.getKey());
        return this;
    }

    public boolean exists(@NonNull Tag<?, ?> key) {
        return exists(key.getKey());
    }

    public boolean exists(@NonNull String key) {
        return map.containsKey(key);
    }

    public @NonNull Collection<String> keys() {
        return map.keySet();
    }
}
