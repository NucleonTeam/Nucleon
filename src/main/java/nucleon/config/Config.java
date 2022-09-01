package nucleon.config;

import nucleon.config.provider.JsonConfigProvider;
import nucleon.config.provider.YamlConfigProvider;
import nucleon.util.Args;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;

import java.io.File;

public final class Config {

    public static final Provider YAML = new YamlConfigProvider();
    public static final Provider JSON = new JsonConfigProvider();

    private final File file;

    private final Provider provider;

    private ConfigurationNode rootNode;

    public Config(File file, Provider provider) {
        this.file = Args.notNull(file, "File");
        this.provider = Args.notNull(provider, "Provider");
    }

    public File getFile() {
        return file;
    }

    public Provider getProvider() {
        return provider;
    }

    public Config load() {
        try {
            rootNode = provider.load(file);
        } catch (ConfigurateException ex) {
            throw new ConfigException(ex);
        }
        return this;
    }

    public Config save() {
        try {
            provider.save(file, rootNode);
        } catch (ConfigurateException ex) {
            throw new ConfigException(ex);
        }
        return this;
    }

    public ConfigurationNode getRootNode() {
        return rootNode;
    }

    public interface Provider {

        ConfigurationNode load(File file) throws ConfigurateException;

        void save(File file, ConfigurationNode root) throws ConfigurateException;
    }
}
