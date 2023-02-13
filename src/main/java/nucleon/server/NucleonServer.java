package nucleon.server;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import nucleon.network.Network;
import nucleon.player.PlayerManager;
import nucleon.registry.Registry;
import nucleon.world.Dimension;
import nucleon.world.NormalWorld;
import nucleon.world.WorldManager;

@Log4j2
public final class NucleonServer {

    @Getter private static NucleonServer instance = null;

    @Getter private final ServerSettings settings;
    @Getter private volatile boolean started;
    @Getter private boolean initialized = false;

    @Getter private final Network network;

    public NucleonServer(ServerSettings settings) throws IllegalStateException {
        if (instance != null) throw new IllegalStateException("Server already initialized");
        instance = this;
        this.settings = settings;
        network = new Network(settings.getInetAddress());
    }

    public long getCurrentTick() {
        return 0; // todo
    }

    private void init() {
        var completeInitialization = Registry.init(this);

        PlayerManager.init();
        WorldManager.init();

        // init: item, block, entity, world (generators), network
        // load plugins. for each plugin run method onLoad(initializer)

        initialized = true;
        completeInitialization.subscribe();
        log.info("Server was fully initialized");
    }

    public void start() {
        log.info("Starting Nucleon server...");
        init();

        WorldManager.defaultWorld = new NormalWorld(Dimension.OVERWORLD);

        // load levels
        network.start();

        started = true;
        // for each plugin run method onEnable()
        // main loop

        log.info("Server was started");
        while (started) {
            Thread.onSpinWait();
        }

        onStop();
    }

    private void onStop() {
        log.info("Stopping server...");

        // kick all players
        network.stop();
        // unload all worlds
        // unload plugins
    }
}
