package nucleon.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;

public class PropertiesConfig extends Config {

    private static final ObjectMapper MAPPER = new JavaPropsMapper();

    public PropertiesConfig(String configFile) {
        super(configFile, MAPPER);
    }
}
