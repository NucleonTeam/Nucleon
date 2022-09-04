package nucleon.event;

public interface EventFilter<T extends Event> {

    void execute(T event);
}
