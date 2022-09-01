package nucleon.config.provider;

import nucleon.config.Config;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;

import java.io.File;

public class JsonConfigProvider implements Config.Provider {

    @Override
    public ConfigurationNode load(File file) throws ConfigurateException {
        return GsonConfigurationLoader.builder()
                .path(file.toPath())
                .build()
                .load();
    }

    @Override
    public void save(File file, ConfigurationNode root) throws ConfigurateException {
        GsonConfigurationLoader.builder()
                .path(file.toPath())
                .build()
                .save(root);
    }
}
