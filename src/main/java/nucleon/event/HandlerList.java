package nucleon.event;

import nucleon.util.Args;

public final class HandlerList<T extends Event> {

    private class Entry {

        final HandlerAware<T> handler;

        Entry prev, next;

        Entry(HandlerAware<T> handler) {
            this.handler = handler;
        }
    }

    private final Class<? extends T> eventClass;

    private final HandlerList<? super T> parent;

    private Entry startEntry;

    public HandlerList(Class<? extends T> eventClass, HandlerList<? super T> parent) {
        this.eventClass = Args.notNull(eventClass, "Event class");
        this.parent = parent;
    }

    public Class<? extends T> getEventClass() { return eventClass; }

    public HandlerList<? super T> getParent() { return parent; }

    public void register(HandlerAware<T> handler) {
        Args.notNull(handler, "Handler");

        Entry newEntry = new Entry(handler);

        if (startEntry == null) {
            startEntry = newEntry;
            return;
        }

        Entry curEntry = startEntry;

        while (handler.getPriority().compareTo(curEntry.handler.getPriority()) >= 0) {
            if (curEntry.next == null) {
                curEntry.next = newEntry;
                newEntry.prev = curEntry;
                return;
            }
            curEntry = curEntry.next;
        }

        if (curEntry.prev == null) {
            startEntry = newEntry;
        } else {
            curEntry.prev.next = newEntry;
            newEntry.prev = curEntry.prev;
        }
        curEntry.prev = newEntry;
        newEntry.next = curEntry;
    }

    public void handleOrdered(Event event) {
        Args.notNull(event, "Event");

        T cast = eventClass.cast(event);

        for (Entry entry = startEntry; entry != null; entry = entry.next) {
            HandlerAware<T> handler = entry.handler;
            if (event.isCancelled() && !handler.isHandleCancelled()) {
                continue;
            }
            handler.getHandler().handle(cast);
        }
    }
}
