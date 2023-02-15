package nucleon.item;

import reactor.util.annotation.NonNull;

import java.util.concurrent.atomic.AtomicInteger;

public final class Material {

    private final static AtomicInteger freeId = new AtomicInteger(0);

    private final String identifier;
    private final short runtimeId = (short) (freeId.getAndIncrement() - Short.MIN_VALUE);
    private final int maxStackSize;

    private Material(String identifier, int maxStackSize) {
        this.identifier = identifier;
        this.maxStackSize = maxStackSize;
    }

    public @NonNull String getIdentifier() {
        return identifier;
    }

    public short getRuntimeId() {
        return runtimeId;
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }

    public static @NonNull Material create(@NonNull String identifier) {
        return create(identifier, 64);
    }

    public static @NonNull Material create(@NonNull String identifier, int maxStackSize) {
        return new Material(identifier, maxStackSize);
    }
}
