package nucleon.event;

import nucleon.plugin.Plugin;
import nucleon.plugin.TestPlugin;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;

import static nucleon.event.EventPriority.*;

public class EventHandlingTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventHandlingTest.class);

    @Test
    public void doTest() {
        HandlerList<TestEvent> queue = HandlerListManager.GLOBAL.acquireListFor(TestEvent.class);

        Plugin plugin = TestPlugin.getInstance();

        StringBuilder resultBuilder = new StringBuilder();

        Collection<HandlerAware<TestEvent>> executors = Arrays.asList(
                new HandlerAware<>(
                        HIGHEST,
                        false,
                        event -> {
                            LOGGER.debug(() -> "Last");
                            resultBuilder.append("p");
                            event.cancel();
                        },
                        plugin
                ),
                new HandlerAware<>(
                        MONITOR,
                        false,
                        event -> {
                            LOGGER.debug(() -> "Post-last. Should not be reached");
                            resultBuilder.append("/");
                        },
                        plugin
                ),
                new HandlerAware<>(
                        LOWEST,
                        false,
                        event -> {
                            LOGGER.debug(() -> "First");
                            resultBuilder.append("p");
                        },
                        plugin
                ),
                new HandlerAware<>(
                        MONITOR,
                        true,
                        event -> {
                            LOGGER.debug(() -> "Post-last");
                            resultBuilder.append("a");
                        },
                        plugin
                ),
                new HandlerAware<>(
                        NORMAL,
                        false,
                        event -> {
                            LOGGER.debug(() -> "Normal");
                            resultBuilder.append("o");
                        },
                        plugin
                )
        );

        executors.forEach(queue::register);

        TestEvent event = new TestEvent("Hello, world!");
        event.call();

        String order = resultBuilder.toString();
        Assertions.assertEquals("popa", order, "Invalid order: " + order);
    }
}
