package nucleon.event.plugin;

import nucleon.plugin.Plugin;

public class PluginDisableEvent extends PluginEvent {

    public PluginDisableEvent(Plugin plugin) {
        super(plugin);
    }
}
