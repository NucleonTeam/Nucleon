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
}
