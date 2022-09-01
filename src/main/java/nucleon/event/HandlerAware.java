package nucleon.event;

import nucleon.plugin.Plugin;
import nucleon.util.Args;

public final class HandlerAware<T extends Event> {

    private final EventPriority priority;

    private final boolean handleCancelled;

    private final EventHandler<T> handler;

    private final Plugin plugin;

    public HandlerAware(EventPriority priority, boolean handleCancelled, EventHandler<T> handler, Plugin plugin) {
        this.priority = Args.notNull(priority, "Priority");
        this.handleCancelled = handleCancelled;
        this.handler = Args.notNull(handler, "Handler");
        this.plugin = Args.notNull(plugin, "Plugin");
    }

    public EventPriority getPriority() { return priority; }

    public boolean isHandleCancelled() { return handleCancelled; }

    public EventHandler<T> getHandler() { return handler; }

    public Plugin getPlugin() { return plugin; }
}
