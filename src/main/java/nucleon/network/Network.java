package nucleon.network;

import lombok.Getter;
import nucleon.server.ServerInitializer;
import nucleon.server.ServerSettings;

public class Network {

    @Getter private static Network instance;

    @Getter private NetworkInterface networkInterface;

    private Network(ServerSettings settings) {
        networkInterface = new DefaultNetworkInterface(settings.getInetAddress());
    }

    public static Initializer init(ServerSettings settings) throws IllegalStateException {
        if (instance != null) throw new IllegalStateException("Module already initialized");
        instance = new Network(settings);
        return new Initializer(instance);
    }

    public static class Initializer extends ServerInitializer.Initializer<Network> {

        private Initializer(Network manager) {
            super(manager);
        }

        public void setNetworkInterface(NetworkInterface newInterface) {
            manager.networkInterface = newInterface;
        }
    }
}
