package nucleon.scheduler;

import nucleon.util.Args;

public final class TaskAware {

    private final Task task;

    private final long delay;

    private final long period;

    private long nextRunTick;

    private boolean cancelled;

    public TaskAware(Task task, long delay, long period) {
        this.task = Args.notNull(task, "Task");
        this.delay = delay;
        this.period = period;
    }

    public Task getTask() { return task; }

    public boolean isDelayed() { return delay > 0; }

    public long getDelay() { return delay; }

    public boolean isRepeating() { return period > 0; }

    public long getPeriod() { return period; }

    public long getNextRunTick() { return nextRunTick; }

    public void setNextRunTick(long nextRunTick) { this.nextRunTick = nextRunTick; }

    public boolean isCancelled() { return cancelled; }

    public void cancel() {
        cancelled = true;
        task.onCancel();
    }
}
