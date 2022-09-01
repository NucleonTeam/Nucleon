package nucleon.server;

public class ServerStartupError extends Error {

    public ServerStartupError() {
        super();
    }

    public ServerStartupError(String message) {
        super(message);
    }

    public ServerStartupError(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerStartupError(Throwable cause) {
        super(cause);
    }
}
