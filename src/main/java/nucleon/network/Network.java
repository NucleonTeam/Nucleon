package nucleon.network;

import nucleon.server.ServerSettings;

import java.net.InetSocketAddress;

public class Network {

    private final InetSocketAddress serverAddress;

    public Network(ServerSettings settings) {
        serverAddress = settings.getInetAddress();
    }

    public void start() {
        // initialize raknet server
        // run raknet server in separate thread
    }

    public void stop() {
        // stop raknet server: send stop signal + Thread#join()
    }
}
