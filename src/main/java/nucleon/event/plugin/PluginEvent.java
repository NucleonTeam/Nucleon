package nucleon.event.plugin;

import nucleon.event.Event;
import nucleon.plugin.Plugin;

public abstract class PluginEvent extends Event {

    private final Plugin plugin;

    protected PluginEvent(Plugin plugin) {
        this.plugin = Args.notNull(plugin, "Plugin");
    }

    public final Plugin getPlugin() { return plugin; }
}
