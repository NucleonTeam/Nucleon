package nucleon.registry;

import nucleon.server.NucleonServer;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

public final class Registry {

    private static Registry instance = null;

    private final NucleonServer server;
    private final Biomes biomes = new Biomes();
    private final Entities entities = new Entities();
    private final Items items = new Items();

    private Registry(@NonNull NucleonServer server) {
        this.server = server;
    }

    public static @NonNull Mono<Void> init(@NonNull NucleonServer server) throws IllegalStateException {
        if (instance != null) throw new IllegalStateException("Module already initialized");

        instance = new Registry(server);

        return Mono.fromRunnable(() -> {
            instance.biomes.completeInitialization();
            instance.entities.completeInitialization();
            instance.items.completeInitialization();
        });
    }

    static void checkForInitialization() throws IllegalStateException {
        if (instance == null) return;
        if (instance.server.isInitialized()){
            throw new IllegalAccessError("You can't register new content after server initialization");
        }
    }

    public static Biomes biomes() {
        if (instance == null) throw new IllegalStateException("Registry module is not initialized");
        return instance.biomes;
    }

    public static Entities entities() {
        if (instance == null) throw new IllegalStateException("Registry module is not initialized");
        return instance.entities;
    }

    public static Items items() {
        if (instance == null) throw new IllegalStateException("Registry module is not initialized");
        return instance.items;
    }
}
