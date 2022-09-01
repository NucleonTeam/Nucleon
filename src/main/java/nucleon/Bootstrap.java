package nucleon;

import nucleon.server.NucleonServer;
import nucleon.server.ServerSettings;

public class Bootstrap {

    public static void main(String[] args) {
        var settings = new ServerSettings();
        var server = new NucleonServer(settings);
        server.start();
    }
}
