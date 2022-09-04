package nucleon.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;

public class ConfigTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigTest.class);

    @Test
    public void doTestProperties() {
        TestProperties cfg = new TestProperties().load();

        LOGGER.info(cfg::toString);

        Assertions.assertEquals("den", cfg.name);
        Assertions.assertEquals(12, cfg.age);

        var name = "Jack";
        var age = 21;

        cfg.name = name;
        cfg.age = age;

        cfg.save();

        String cfgContent;

        try {
            cfgContent = Files.readString(cfg.getConfigFile());
        } catch (IOException exception) {
            exception.printStackTrace();
            return;
        }

        LOGGER.debug(() -> cfgContent);

        cfg.getConfigFile().toFile().delete();

        String expected = "name=" + name + "\nage=" + age;
        Assertions.assertEquals(expected.trim(), cfgContent.trim());
    }

    @Test
    public void doTestYaml() {
        TestYaml cfg = new TestYaml().load();

        LOGGER.info(cfg::toString);

        Assertions.assertEquals("den", cfg.name);
        Assertions.assertEquals(12, cfg.age);

        var name = "Jack";
        var age = 21;

        cfg.name = name;
        cfg.age = age;

        cfg.save();

        String cfgContent;

        try {
            cfgContent = Files.readString(cfg.getConfigFile());
        } catch (IOException exception) {
            exception.printStackTrace();
            return;
        }

        LOGGER.debug(() -> cfgContent);

        cfg.getConfigFile().toFile().delete();

        String expected = "---\n" + "name: \"" + name + "\"\n" + "age: " + age;
        Assertions.assertEquals(expected.trim(), cfgContent.trim());
    }

    public static class TestProperties extends PropertiesConfig {

        public String name = "den"; // Default value
        public int age = 12; // Default value

        public TestProperties() {
            super("test.properties");
        }
    }

    public static class TestYaml extends YamlConfig {

        public String name = "den"; // Default value
        public int age = 12; // Default value

        public TestYaml() {
            super("test.yml");
        }
    }
}
