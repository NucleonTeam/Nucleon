package nucleon.config;

import nucleon.util.Args;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public enum ConfigType {

    YAML("yml", "yaml") {
        @Override
        public ConfigurationLoader<?> buildLoader(File file) {
            return YamlConfigurationLoader.builder()
                    .file(file)
                    .nodeStyle(NodeStyle.FLOW)
                    .build();
        }
    },

    JSON("js", "json") {
        @Override
        public ConfigurationLoader<?> buildLoader(File file) {
            return GsonConfigurationLoader.builder()
                    .file(file)
                    .indent(0)
                    .build();
        }
    },

    PROPERTIES("props", "properties") {
        @Override
        public ConfigurationLoader<?> buildLoader(File file) {
            return PropertiesConfigurationLoader.builder()
                    .file(file)
                    .build();
        }
    },

    ENUM("enum", "enumeration", "list") {
        @Override
        public ConfigurationLoader<?> buildLoader(File file) {
            return EnumConfigurationLoader.builder()
                    .file(file)
                    .build();
        }
    },

    UNKNOWN {
        @Override
        public ConfigurationLoader<?> buildLoader(File file) {
            throw new UnsupportedOperationException("Unknown file extension: " + file.getName());
        }
    };

    private static final Map<String, ConfigType> ASSOCIATIONS = new HashMap<>();

    static {
        for (ConfigType type : values()) {
            for (String extension : type.extensions) {
                ASSOCIATIONS.put(extension, type);
            }
        }
    }

    public static ConfigType detect(File file) {
        Args.notNull(file, "File");
        String fileName = file.getName();
        return detect(fileName);
    }

    public static ConfigType detect(String fileName) {
        Args.notNull(fileName, "File name");
        int extensionStartIndex = fileName.lastIndexOf('.');
        if (extensionStartIndex < 0) {
            throw new IllegalArgumentException("File has no extension");
        }
        String fileExtension = fileName.substring(extensionStartIndex + 1);
        return ASSOCIATIONS.getOrDefault(fileExtension, UNKNOWN);
    }

    private final String[] extensions;

    ConfigType(String... extensions) {
        this.extensions = extensions;
    }

    public String[] extensions() { return extensions.clone(); }

    public abstract ConfigurationLoader<?> buildLoader(File file);
}
