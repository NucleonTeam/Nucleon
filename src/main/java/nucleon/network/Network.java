package nucleon.network;

public class Network {
    private boolean started = false;

    public void startup() {
        if (started) {
            throw new IllegalStateException("Network already started");
        }
        started = true;
    }

    public void shutdown() {
        if (!started) {
            throw new IllegalStateException("Network is not started");
        }
        started = false;
    }
}
