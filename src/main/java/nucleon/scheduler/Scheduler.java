package nucleon.scheduler;

public interface Scheduler {

    TaskAware scheduleTask(Task task);

    TaskAware scheduleDelayedTask(Task task, long delay);

    TaskAware scheduleRepeatingTask(Task task, long period);

    TaskAware scheduleDelayedRepeatingTask(Task task, long delay, long period);

    void processTick();
}
