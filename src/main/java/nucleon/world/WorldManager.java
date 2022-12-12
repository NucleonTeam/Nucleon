package nucleon.world;

import lombok.NonNull;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class WorldManager {

    private static WorldManager instance = null;

    private final ConcurrentHashMap<UUID, World> worlds = new ConcurrentHashMap<>();

    private WorldManager() {

    }

    public static void init() {
        if (instance != null) throw new IllegalStateException("Module already initialized");
        instance = new WorldManager();
    }

    public static @NonNull WorldManager getInstance() {
        if (instance == null) throw new IllegalStateException("Module isn't initialized");
        return instance;
    }

    public void register(@NonNull World world) {
        worlds.put(world.getId(), world);
    }

    public void unregister(@NonNull World world) {
        worlds.remove(world.getId());
    }

    public @NonNull World getWorld(@NonNull UUID id) {
        return worlds.get(id);
    }
}
