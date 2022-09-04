package nucleon.event;

import nucleon.util.Args;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class FilterChain<T extends Event> {

    private final Class<? extends T> eventClass;

    private final FilterChain<? super T> parent;

    private final List<FilterAware<T>> interceptors = new ArrayList<>();

    public FilterChain(Class<? extends T> eventClass, FilterChain<? super T> parent) {
        this.eventClass = Args.notNull(eventClass, "Event class");
        this.parent = parent;
    }

    public Class<? extends T> getEventClass() { return eventClass; }

    public FilterChain<? super T> getParent() { return parent; }

    public void register(FilterAware<T> handler) {
        Args.notNull(handler, "Handler");
        interceptors.add(handler);
        interceptors.sort(Comparator.comparing(FilterAware::getPriority));
    }

    public void execute(Event event) {
        Args.notNull(event, "Event");

        T castedEvent = eventClass.cast(event);

        for (FilterAware<T> handler : interceptors) {
            if (!event.isCancelled() || handler.isHandleCancelled()) {
                handler.getFilter().execute(castedEvent);
            }
        }
    }
}
