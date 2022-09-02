package nucleon.config;

import nucleon.util.Args;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.ConfigurationLoader;

import java.io.File;

public final class Config {

    private final File file;

    private final ConfigurationLoader<?> loader;

    /** Root node */
    private transient volatile ConfigurationNode rn;

    public Config(String fileName) {
        this(new File(fileName));
    }

    public Config(File file) {
        this(file, ConfigType.detect(file).buildLoader(file));
    }

    public Config(String fileName, ConfigurationLoader<?> loader) {
        this(new File(fileName), loader);
    }

    public Config(File file, ConfigurationLoader<?> loader) {
        this.file = Args.notNull(file, "File");
        this.loader = Args.notNull(loader, "Loader");
    }

    public File getFile() { return file; }

    public ConfigurationLoader<?> getLoader() { return loader; }

    public Config load() {
        loadInternal();
        return this;
    }

    private synchronized void loadInternal() {
        try {
            rn = loader.load();
        } catch (ConfigurateException e) {
            throw new ConfigException("Couldn't load the file [" + file.getName() + "]", e);
        }
    }

    public Config save() {
        return save(false);
    }

    public Config save(boolean clear) {
        saveInternal(clear);
        return this;
    }

    private synchronized void saveInternal(boolean clear) {
        try {
            loader.save(rn);
        } catch (ConfigurateException e) {
            throw new ConfigException("Couldn't save the file [" + file.getName() + "]", e);
        }
        if (clear) {
            rn = null;
        }
    }

    public ConfigurationNode getRootNode() {
        ensureThatLoaded();
        return rn;
    }

    public int getInt(String key) {
        return access(key).getInt();
    }

    public int getIntOrDefault(String key, int defVal) {
        return access(key).getInt(defVal);
    }

    public long getLong(String key) {
        return access(key).getLong();
    }

    public long getLongOrDefault(String key, long defVal) {
        return access(key).getLong(defVal);
    }

    public boolean getBoolean(String key) {
        return access(key).getBoolean();
    }

    public boolean getBooleanOrDefault(String key, boolean defVal) {
        return access(key).getBoolean(defVal);
    }

    public String getString(String key) {
        return access(key).getString();
    }

    public String getStringOrDefault(String key, String defVal) {
        return access(key).getString(defVal);
    }

    public int getNestedInt(String path) {
        return nestedAccess(path).getInt();
    }

    public int getNestedIntOrDefault(String path, int defVal) {
        return nestedAccess(path).getInt(defVal);
    }

    public long getNestedLong(String path) {
        return nestedAccess(path).getLong();
    }

    public long getNestedLongOrDefault(String path, long defVal) {
        return nestedAccess(path).getLong(defVal);
    }

    public boolean getNestedBoolean(String path) {
        return nestedAccess(path).getBoolean();
    }

    public boolean getNestedBooleanOrDefault(String path, boolean defVal) {
        return nestedAccess(path).getBoolean(defVal);
    }

    public String getNestedString(String path) {
        return nestedAccess(path).getString();
    }

    public String getNestedStringOrDefault(String path, String defVal) {
        return nestedAccess(path).getString(defVal);
    }

    public void setInt(String key, int value) {
        access(key).raw(value);
    }

    public void setLong(String key, long value) {
        access(key).raw(value);
    }

    public void setBoolean(String key, boolean value) {
        access(key).raw(value);
    }

    public void setString(String key, String value) {
        access(key).raw(value);
    }

    public void setNestedInt(String path, int value) {
        nestedAccess(path).raw(value);
    }

    public void setNestedLong(String path, long value) {
        nestedAccess(path).raw(value);
    }

    public void setNestedBoolean(String path, boolean value) {
        nestedAccess(path).raw(value);
    }

    public void setNestedString(String path, String value) {
        nestedAccess(path).raw(value);
    }

    public boolean contains(String key) {
        Args.notBlank(key, "Key");
        ensureThatLoaded();
        return rn.hasChild(key);
    }

    public boolean containsNested(String path) {
        Args.notBlank(path, "Path");
        ensureThatLoaded();
        int delimiter = path.lastIndexOf('.');
        if (delimiter < 0) {
            return contains(path);
        }
        String leftPath = path.substring(0, delimiter);
        String lastKey = path.substring(delimiter + 1);
        return nestedAccess(leftPath).hasChild(lastKey);
    }

    public boolean remove(String key) {
        Args.notBlank(key, "Key");
        ensureThatLoaded();
        return rn.removeChild(key);
    }

    public boolean removeNested(String path) {
        Args.notBlank(path, "Path");
        ensureThatLoaded();
        int delimiter = path.lastIndexOf('.');
        if (delimiter < 0) {
            return remove(path);
        }
        String leftPath = path.substring(0, delimiter);
        String lastKey = path.substring(delimiter + 1);
        return nestedAccess(leftPath).removeChild(lastKey);
    }

    private ConfigurationNode nestedAccess(String path) {
        Args.notBlank(path, "Path");
        ensureThatLoaded();
        Object[] keys = path.split("\\.");
        return rn.node(keys);
    }

    private ConfigurationNode access(String key) {
        Args.notBlank(key, "Key");
        ensureThatLoaded();
        return rn.node(key);
    }

    private void ensureThatLoaded() {
        if (rn == null) {
            throw new ConfigException("Config wasn't loaded");
        }
    }
}
