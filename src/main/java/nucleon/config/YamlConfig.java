package nucleon.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class YamlConfig extends Config {

    private static final ObjectMapper MAPPER = new YAMLMapper();

    public YamlConfig(String configFile) {
        super(configFile, MAPPER);
    }
}
