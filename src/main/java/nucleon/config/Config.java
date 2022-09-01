package nucleon.config;

import nucleon.util.Args;

import java.io.File;

public final class Config {

    public interface Provider {

    }

    private final File file;

    private final Provider provider;


    public Config(File file, Provider provider) {
        this.file = Args.notNull(file, "File");
        this.provider = Args.notNull(provider, "Provider");
    }

    public File getFile() {
        return file;
    }

    public Provider getProvider() {
        return provider;
    }

    public Config load() {
        return this;
    }

    public Config save() {
        return this;
    }

}
