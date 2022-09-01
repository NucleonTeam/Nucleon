package nucleon.server;

public class Server {
    private static Server instance = null;

    public static Server getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Server instance not created");
        }
        return instance;
    }

    private long tick = -1;

    private boolean started = false;

    public Server() {
        instance = this;
    }

    public void startup() {
        if (started) {
            throw new IllegalStateException("Server instance already started");
        }
        started = true;
    }

    public void shutdown() {
        if (!started) {
            throw new IllegalStateException("Server instance not started");
        }
        started = false;
    }

    public long getTick() {
        return tick;
    }
}
