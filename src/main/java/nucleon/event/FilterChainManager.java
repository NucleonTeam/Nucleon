package nucleon.event;

import nucleon.util.Args;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public final class FilterChainManager {

    public static final FilterChainManager GLOBAL = new FilterChainManager();

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

    private final Map<Class<? extends Event>, FilterChain<?>> event2chainMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T extends Event> FilterChain<T> acquireChainFor(Class<? extends T> eventClass) {
        Args.notNull(eventClass, "Event class");

        if (event2chainMap.containsKey(eventClass)) {
            return (FilterChain<T>) event2chainMap.get(eventClass);
        }

        Class<? extends Event> superclass = resolveNearestHandleableParent(eventClass);
        FilterChain<? super T> parentChain;

        if (superclass == null) {
            parentChain = null;
        } else {
            parentChain = acquireChainFor(superclass);
        }

        FilterChain<T> list = new FilterChain<>(eventClass, parentChain);
        event2chainMap.put(eventClass, list);
        return list;
    }
}
