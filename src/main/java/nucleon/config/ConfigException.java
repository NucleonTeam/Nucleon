package nucleon.config;

public class ConfigException extends RuntimeException {

    public ConfigException(Exception exception) {
        super(exception);
    }
}
