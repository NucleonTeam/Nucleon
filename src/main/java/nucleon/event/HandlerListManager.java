package nucleon.event;

import nucleon.util.Args;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public final class HandlerListManager {

    public static final HandlerListManager GLOBAL = new HandlerListManager();

    @SuppressWarnings("unchecked")
    private static Class<? extends Event> resolveNearestHandleableParent(Class<? extends Event> eventClass) {
        Class<?> superclass = eventClass.getSuperclass();

        while (superclass != Event.class) {
            Class<? extends Event> eventSuperclass = (Class<? extends Event>) superclass;
            if (isEventHandleable(eventSuperclass)) {
                return eventSuperclass;
            }
            superclass = eventSuperclass.getSuperclass();
        }

        return null;
    }

    private static boolean isEventHandleable(Class<? extends Event> eventClass) {
        return !Modifier.isAbstract(eventClass.getModifiers()) || eventClass.isAnnotationPresent(AllowHandle.class);
    }

    private final Map<Class<? extends Event>, HandlerList<?>> allLists = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T extends Event> HandlerList<T> acquireListFor(Class<? extends T> eventClass) {
        Args.notNull(eventClass, "Event class");

        if (allLists.containsKey(eventClass)) {
            return (HandlerList<T>) allLists.get(eventClass);
        }

        Class<? extends Event> superclass = resolveNearestHandleableParent(eventClass);
        HandlerList<? super T> parentQueue;
        if (superclass == null)
            parentQueue = null;
        else
            parentQueue = acquireListFor(superclass);
        HandlerList<T> list = new HandlerList<>(eventClass, parentQueue);
        allLists.put(eventClass, list);
        return list;
    }
}
