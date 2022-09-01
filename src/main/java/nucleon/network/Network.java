package nucleon.network;

import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

public class Network {
    private final Map<Class<? extends NetworkSource>, NetworkSource> sources = new HashMap<>();

    private boolean started = false;

    public void register(@NonNull NetworkSource source) {
        if (sources.containsKey(source.getClass())) {
            throw new IllegalArgumentException(String.format("Source `%s` already registered", source.getClass()));
        }
        // TODO: Call cancellable event
        sources.put(source.getClass(), source);
        if (started) {
            source.startup();
        }
    }

    public void unregister(Class<? extends NetworkSource> sourceClass) {
        if (!sources.containsKey(sourceClass)) {
            throw new IllegalArgumentException(String.format("Source `%s` not registered", sourceClass));
        }
        if (started) {
            sources.get(sourceClass).shutdown();
        }
        sources.remove(sourceClass);
    }

    public void startup() {
        if (started) {
            throw new IllegalStateException("Network already started");
        }
        started = true;
    }

    public void shutdown() {
        if (!started) {
            throw new IllegalStateException("Network is not started");
        }
        started = false;
    }
}
