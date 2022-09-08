package nucleon.server;

import lombok.Getter;
import nucleon.network.Network;

import java.util.UUID;

@Getter
public class NucleonServer {

    public static final long ID = UUID.randomUUID().getMostSignificantBits();

    @Getter
    private static NucleonServer instance;

    private final ServerSettings settings;

    private final Network network;

    private boolean enabled = true;

    public NucleonServer(ServerSettings settings) throws IllegalStateException {
        if (NucleonServer.instance != null) {
            throw new IllegalStateException("Server already initialized");
        }

        NucleonServer.instance = this;
        this.settings = settings;
        this.network = new Network(settings);
    }

    public long getCurrentTick() {
        return 0; // todo
    }

    private void init() {
        // init: item, block, entity, world (generators), network
        var initializer = new ServerInitializer();
        // load plugins. for each plugin run method onLoad(initializer)
        // block all initializers
    }

    public void start() {
        System.out.println("Starting Nucleon server...");
        this.init();
        // load levels
        this.network.start();

        // start: delete after creating the main loop
        try {
            Thread.sleep(1000);
        } catch (InterruptedException exception) {
            throw new RuntimeException(exception);
        }
        // end: delete after creating the main loop

        // for each plugin run method onEnable()

        while (enabled) { // main loop for keep server running
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

        }
        this.onStop();
    }

    private void onStop() {
        System.out.println("Stopping Nucleon server...");
        // kick all players

        System.out.println("Stopping network");
        this.network.close();
        // unload all worlds
        // unload plugins
    }

    public void stop() {
        enabled = false;
    }
}
