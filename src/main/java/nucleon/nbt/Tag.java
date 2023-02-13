package nucleon.nbt;

import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtMapBuilder;
import com.nukkitx.nbt.NbtType;
import reactor.util.annotation.NonNull;

public final class Tag<T, V> {

    private final String key;
    private final Reader reader;
    private final Writer<T> writer;
    private final NbtType<V> type;

    private Tag(@NonNull String key, @NonNull Reader reader, @NonNull Writer<T> writer, @NonNull NbtType<V> type) {
        this.key = key;
        this.reader = reader;
        this.writer = writer;
        this.type = type;
    }

    public @NonNull String getKey() {
        return key;
    }

    public T read(@NonNull NbtMapBuilder map) {
        return (T) reader.read(map, key);
    }

    public void write(@NonNull NbtMapBuilder map, @NonNull T value) {
        writer.write(map, key, value);
    }

    public @NonNull NbtType<V> getType() {
        return type;
    }

    public static @NonNull Tag<String, String> String(@NonNull String key) {
        return new Tag<>(key, NbtMapBuilder::get, NbtMapBuilder::putString, NbtType.STRING);
    }

    public static @NonNull Tag<Long, Long> Long(@NonNull String key) {
        return new Tag<>(key, NbtMapBuilder::get, NbtMapBuilder::putLong, NbtType.LONG);
    }

    public static @NonNull Tag<Integer, Integer> Integer(@NonNull String key) {
        return new Tag<>(key, NbtMapBuilder::get, NbtMapBuilder::putInt, NbtType.INT);
    }

    public static @NonNull Tag<Short, Short> Short(@NonNull String key) {
        return new Tag<>(key, NbtMapBuilder::get, NbtMapBuilder::putShort, NbtType.SHORT);
    }

    public static @NonNull Tag<Byte, Byte> Byte(@NonNull String key) {
        return new Tag<>(key, NbtMapBuilder::get, NbtMapBuilder::putByte, NbtType.BYTE);
    }

    public static @NonNull Tag<Boolean, Byte> Boolean(@NonNull String key) {
        return new Tag<>(key, NbtMapBuilder::get, NbtMapBuilder::putBoolean, NbtType.BYTE);
    }

    public static @NonNull Tag<Float, Float> Float(@NonNull String key) {
        return new Tag<>(key, NbtMapBuilder::get, NbtMapBuilder::putFloat, NbtType.FLOAT);
    }

    public static @NonNull Tag<Double, Double> Double(@NonNull String key) {
        return new Tag<>(key, NbtMapBuilder::get, NbtMapBuilder::putDouble, NbtType.DOUBLE);
    }

    public static @NonNull Tag<CompoundTag, NbtMap> Compound(@NonNull String key) {
        return new Tag<>(key,
                (map, $key) -> new CompoundTag($key, (NbtMap) map.get($key)),
                (map, $key, value) -> map.putCompound($key, value.getNbtMap()),
                NbtType.COMPOUND);
    }

    private interface Reader {

        Object read(@NonNull NbtMapBuilder map, @NonNull String key);
    }

    private interface Writer<T> {

        void write(@NonNull NbtMapBuilder map, @NonNull String key, T value);
    }
}
