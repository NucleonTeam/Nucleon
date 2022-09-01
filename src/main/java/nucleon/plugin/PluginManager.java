package nucleon.plugin;

import nucleon.event.*;
import nucleon.util.Args;
import nucleon.util.Utils;

import java.lang.reflect.Method;

public final class PluginManager {

    public void registerEventListener(Listener listener, Plugin plugin) {
        Args.notNull(listener, "Listener");
        Args.notNull(plugin, "Plugin");

        if (!plugin.isEnabled()) {
            throw new PluginException("Attempt to register an event listener in a not enabled plugin");
        }

        Class<? extends Listener> listenerClass = listener.getClass();

        if (!Utils.isClassReflection(listenerClass)) {
            throw new PluginException("Attempt to register non-class as event listener");
        }

        for (Method method : listenerClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventHandler.class)) {
                registerEventHandlerMethod(listener, plugin, method);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void registerEventHandlerMethod(Listener listener, Plugin plugin, Method method) {
        if (!Utils.isConcreteMethod(method)) {
            throw new PluginException("");
        }

        if (method.getParameterCount() != 1) {
            throw new PluginException("");
        }

        Class<?> paramType = method.getParameterTypes()[0];

        if (Event.class.isAssignableFrom(paramType)) {
            throw new PluginException("");
        }

        EventHandler handler = method.getAnnotation(EventHandler.class);

        registerEventHandler(
                (Class<? extends Event>) paramType,
                handler.priority(),
                handler.handleCancelled(),
                new MethodEventHandler<>(method, listener),
                plugin);
    }

    public <T extends Event> void registerEventHandler(
            Class<T> eventClass,
            EventPriority priority,
            boolean handleCancelled,
            nucleon.event.EventHandler<T> handler,
            Plugin plugin) {
        Args.notNull(eventClass, "Event class");
        Args.notNull(priority, "Priority");
        Args.notNull(handler, "Handler");
        Args.notNull(plugin, "Plugin");

        if (!plugin.isEnabled()) {
            throw new IllegalArgumentException("Attempt to register an event handler in a not enabled plugin");
        }

        HandlerAware<T> aware = new HandlerAware<>(priority, handleCancelled, handler, plugin);
        HandlerListManager.GLOBAL.acquireListFor(eventClass).register(aware);
    }
}
