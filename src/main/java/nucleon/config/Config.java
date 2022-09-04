package nucleon.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

public abstract class Config {

    @JsonIgnore
    @Getter
    private final Path configFile;

    @JsonIgnore
    private final ObjectMapper mapper;

    public Config(String configFile, ObjectMapper mapper) {
        this.configFile = Path.of(configFile);
        this.mapper = mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    public void save() {
        try {
            mapper.writeValue(this.configFile.toFile(), this);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public <T extends Config> T load() {
        T config;
        try {
            config = mapper.readerForUpdating(this).readValue(this.configFile.toFile());
        } catch (FileNotFoundException exception) {
            this.save();
            config = this.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        return config;
    }
}
