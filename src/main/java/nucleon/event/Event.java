package nucleon.event;

public abstract class Event {

    private static final int RECURSION_DEPTH_LIMIT = 64;

    private final ThreadLocal<Integer> recursionDepth = ThreadLocal.withInitial(() -> 0);

    private boolean cancelled = false;

    public final void call() {
        int recursionDepthValue = recursionDepth.get();

        if (recursionDepthValue >= RECURSION_DEPTH_LIMIT) {
            throw new EventException("Recursion depth exceeded the limit value (" + RECURSION_DEPTH_LIMIT + ")");
        }

        try {
            recursionDepth.set(recursionDepthValue + 1);

            FilterChain<?> list = FilterChainManager.GLOBAL.acquireChainFor(getClass());

            do {
                list.execute(this);
                list = list.getParent();
            } while (list != null);
        } finally {
            recursionDepth.set(recursionDepthValue);
        }
    }

    public final void cancel() {
        ensureThatCancellable();
        cancelled = true;
    }

    private void ensureThatCancellable() {
        if (!(this instanceof Cancellable)) {
            throw new IllegalStateException("Attempt to cancel non-cancellable event");
        }
    }

    public final boolean isCancelled() { return cancelled; }

    public final String name() { return getClass().getSimpleName(); }
}
