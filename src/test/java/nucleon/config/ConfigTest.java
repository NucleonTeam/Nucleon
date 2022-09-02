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
    public void doTestJson() {
        var cfg = new Config(System.getProperty("user.dir") + "/test.json");

        cfg.load();

        String personName = "Dick";
        int personAge = 21;

        cfg.setNestedString("Person.Name", personName);
        cfg.setNestedInt("Person.Age", personAge);

        cfg.save();

        String cfgContent;

        try {
            cfgContent = Files.readString(cfg.getFile().toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        LOGGER.debug(() -> cfgContent);

        cfg.getFile().delete();

        String expected =
                "{" +
                    "\"Person\":{" +
                        "\"Name\":\"" + personName + "\"," +
                        "\"Age\":" + personAge +
                    "}" +
                "}";
        Assertions.assertEquals(expected.trim(), cfgContent.trim());
    }

    @Test
    public void doTestYaml() {
        var cfg = new Config(System.getProperty("user.dir") + "/test.yaml");

        cfg.load();

        String personName = "Dick";
        int personAge = 21;

        cfg.setNestedString("Person.Name", personName);
        cfg.setNestedInt("Person.Age", personAge);

        cfg.save();

        String cfgContent;

        try {
            cfgContent = Files.readString(cfg.getFile().toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        LOGGER.debug(() -> cfgContent);

        cfg.getFile().delete();

        String expected =
                "Person:\n" +
                "    Name: " + personName + "\n" +
                "    Age: " + personAge;
        Assertions.assertEquals(expected.trim(), cfgContent.trim());
    }

    @Test
    public void doTestProps() {
        var cfg = new Config(System.getProperty("user.dir") + "/test.props");

        cfg.load();

        String personName = "Dick";
        int personAge = 21;

        cfg.setNestedString("Person.Name", personName);
        cfg.setNestedInt("Person.Age", personAge);

        cfg.save();

        String cfgContent;

        try {
            cfgContent = Files.readString(cfg.getFile().toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        LOGGER.debug(   () -> cfgContent);

        cfg.getFile().delete();

        // todo
        //String expected = "Person={Name\\=" + personName + ", Age\\=" + personAge + "}";
        //Assertions.assertEquals(expected.trim(), cfgContent.trim());
    }

    @Test
    public void doTestEnum() {
        var cfg = new Config(System.getProperty("user.dir") + "/test.enum");

        cfg.load();

        String personName = "Dick";
        int personAge = 21;

        cfg.setString("0", personName);
        cfg.setInt("1", personAge);

        cfg.save();

        String cfgContent;

        try {
            cfgContent = Files.readString(cfg.getFile().toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        LOGGER.info(() -> cfgContent);

        cfg.getFile().delete();

        // todo

        //String expected = personName + System.lineSeparator() + personAge;
        //Assertions.assertEquals(expected.trim(), cfgContent.trim());
    }
}
