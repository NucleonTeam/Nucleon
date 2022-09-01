package nucleon.event;

import nucleon.util.Args;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class MethodEventHandler<T extends Event> implements EventHandler<T> {

    private final Method method;

    private final Listener listener;

    public MethodEventHandler(Method method, Listener listener) {
        this.method = Args.notNull(method, "Method");
        this.listener = Args.notNull(listener, "Listener");
    }

    @Override
    public void handle(T event) {
        Args.notNull(event, "Event");

        try {
            method.setAccessible(true);
            method.invoke(listener, event);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            throw new EventException("Could not execute the event [" + event.name() + "]", e.getTargetException());
        }
    }
}
