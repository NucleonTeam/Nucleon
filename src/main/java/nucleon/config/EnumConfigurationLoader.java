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

public final class EnumConfigurationLoader extends AbstractConfigurationLoader<BasicConfigurationNode> {

    public static Builder builder() { return new Builder(); }

    public static final class Builder extends AbstractConfigurationLoader.Builder<Builder, EnumConfigurationLoader> {

        Builder() { super(); }

        @Override
        public EnumConfigurationLoader build() { return new EnumConfigurationLoader(this); }
    }

    EnumConfigurationLoader(Builder builder) { super(builder, new CommentHandler[]{CommentHandlers.HASH}); }

    @Override
    protected void loadInternal(BasicConfigurationNode node, BufferedReader reader) throws ParsingException {
        int lineNum = 1;

        try {
            while (reader.ready()) {
                lineNum++;
                String value = reader.readLine();
                node.node().raw(value);
            }
        } catch (IOException e) {
            throw new ParsingException(node, lineNum, 0, null, null, e);
        }
    }

    @Override
    protected void saveInternal(ConfigurationNode node, Writer writer) throws ConfigurateException {
        for (ConfigurationNode nodeValue : node.childrenList()) {
            Object value = nodeValue.raw();
            if (value != null) {
                try {
                    writer.write(value.toString());
                    writer.write(System.lineSeparator());
                } catch (IOException e) {
                    throw new ConfigurateException(nodeValue, e);
                }
            }
        }
    }

    @Override
    public BasicConfigurationNode createNode(ConfigurationOptions options) { return BasicConfigurationNode.root(options); }
}
