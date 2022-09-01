package nucleon.event.plugin;

import nucleon.plugin.Plugin;

public class PluginEnableEvent extends PluginEvent {

    public PluginEnableEvent(Plugin plugin) {
        super(plugin);
    }
}
