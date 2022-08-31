package nucleon.bootstrap;

import nucleon.server.Server;
import nucleon.server.ServerSettings;

public class Bootstrap {

    public static void main(String[] args) {
        var settings = new ServerSettings();
        var server = new Server(settings);
        server.start();
    }
}
