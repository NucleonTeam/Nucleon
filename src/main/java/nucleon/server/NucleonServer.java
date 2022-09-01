package nucleon.server;

import lombok.Getter;
import nucleon.network.Network;

public final class NucleonServer {

    @Getter private static NucleonServer instance = null;

    @Getter private final ServerSettings settings;

    public NucleonServer(ServerSettings settings) throws IllegalStateException {
        if (instance != null) throw new IllegalStateException("Server already initialized");
        instance = this;
        this.settings = settings;
    }

    public long getCurrentTick() {
        return 0; // todo
    }

    private void init() {
        // init: item, block, entity, world (generators), network
        var initializer = new ServerInitializer(
                Network.init(settings)
        );
        // load plugins. for each plugin run method onLoad(initializer)
        // block all initializers
    }

    public void start() {
        System.out.println("Starting Nucleon server...");
        init();
        // load levels
        // run network interface
        // for each plugin run method onEnable()
        // main loop
        onStop();
    }

    private void onStop() {
        // kick all players
        // stop network interface
        // unload all worlds
        // unload plugins
    }
}
