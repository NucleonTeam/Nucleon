package nucleon.config.provider;

import nucleon.config.Config;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;

public class YamlConfigProvider implements Config.Provider {

    @Override
    public ConfigurationNode load(File file) throws ConfigurateException {
        return YamlConfigurationLoader.builder()
                .path(file.toPath())
                .build()
                .load();
    }

    @Override
    public void save(File file, ConfigurationNode root) throws ConfigurateException {
        YamlConfigurationLoader.builder()
                .path(file.toPath())
                .build()
                .save(root);
    }
}
