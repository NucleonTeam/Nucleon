package nucleon.event;

import nucleon.plugin.NucleonPlugin;
import nucleon.util.Args;

public final class FilterAware<T extends Event> {

    private final EventPriority priority;

    private final boolean handleCancelled;

    private final EventFilter<T> filter;

    private final NucleonPlugin plugin;
    
    public FilterAware(EventPriority priority, boolean handleCancelled, EventFilter<T> filter, NucleonPlugin plugin) {
        this.priority = Args.notNull(priority, "Priority");
        this.handleCancelled = handleCancelled;
        this.filter = Args.notNull(filter, "Filter");
        this.plugin = Args.notNull(plugin, "Plugin");
    }

    public EventPriority getPriority() { return priority; }

    public boolean isHandleCancelled() { return handleCancelled; }

    public EventFilter<T> getFilter() { return filter; }

    public NucleonPlugin getPlugin() { return plugin; }
}
