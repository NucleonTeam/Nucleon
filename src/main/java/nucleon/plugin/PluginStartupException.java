package nucleon.plugin;

public class PluginStartupException extends PluginException {

    public PluginStartupException() {
        super();
    }

    public PluginStartupException(String message) {
        super(message);
    }

    public PluginStartupException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginStartupException(Throwable cause) {
        super(cause);
    }
}
