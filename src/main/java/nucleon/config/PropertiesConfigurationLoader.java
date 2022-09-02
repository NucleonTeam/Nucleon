package nucleon.config;

import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;
import org.spongepowered.configurate.loader.CommentHandler;
import org.spongepowered.configurate.loader.CommentHandlers;
import org.spongepowered.configurate.loader.ParsingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;

public final class PropertiesConfigurationLoader extends AbstractConfigurationLoader<BasicConfigurationNode> {

    public static Builder builder() { return new Builder(); }

    public static final class Builder extends AbstractConfigurationLoader.Builder<Builder, PropertiesConfigurationLoader> {

        Builder() { super(); }

        @Override
        public PropertiesConfigurationLoader build() { return new PropertiesConfigurationLoader(this); }
    }

    PropertiesConfigurationLoader(Builder builder) { super(builder, new CommentHandler[]{CommentHandlers.HASH}); }

    @Override
    protected void loadInternal(BasicConfigurationNode node, BufferedReader reader) throws ParsingException {
        Properties properties = new Properties();

        try {
            properties.load(reader);
        } catch (IOException e) {
            throw new ParsingException(node, 0, 0, null, null, e);
        }

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            Object path = entry.getKey();
            Object value = entry.getValue();
            node.node(path).raw(value);
        }
    }

    @Override
    protected void saveInternal(ConfigurationNode node, Writer writer) throws ConfigurateException {
        if (!node.isMap()) {
            throw new ConfigurateException("Node is not a map");
        }
        Map<Object, ? extends ConfigurationNode> nodeMap = node.childrenMap();
        Properties properties = new Properties(nodeMap.size());

        for (Map.Entry<Object, ? extends ConfigurationNode> entry : nodeMap.entrySet()) {
            Object path = entry.getKey();
            Object value = entry.getValue().raw();
            properties.put(path, value);
        }

        try {
            properties.store(writer, "Powered by Nucleon.");
        } catch (IOException e) {
            throw new ConfigurateException(node, e);
        }
    }

    @Override
    public BasicConfigurationNode createNode(ConfigurationOptions options) { return BasicConfigurationNode.root(options); }
}
