package nucleon.event;

public abstract class Event {

    private static final int RECURSION_DEPTH_LIMIT = 50;

    private final ThreadLocal<Integer> recursionDepth = ThreadLocal.withInitial(() -> 0);

    private boolean cancelled = false;

    public final void call() {
        int recursionDepthValue = recursionDepth.get();

        if (recursionDepthValue >= RECURSION_DEPTH_LIMIT) {
            throw new EventException("");
        }

        try {
            recursionDepth.set(recursionDepthValue + 1);

            HandlerList<?> list = HandlerListManager.GLOBAL.acquireListFor(getClass());

            do {
                list.handleOrdered(this);
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

    public final boolean isCancelled() {
        return cancelled;
    }

    public final String name() {
        return getClass().getSimpleName();
    }
}
