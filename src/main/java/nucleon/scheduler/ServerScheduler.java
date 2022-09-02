package nucleon.scheduler;

import nucleon.server.NucleonServer;
import nucleon.util.Args;

import java.util.Comparator;
import java.util.PriorityQueue;

public class ServerScheduler implements Scheduler {

    private static final Comparator<TaskAware> COMPARATOR
            = Comparator.comparing(TaskAware::getNextRunTick).reversed();

    private final NucleonServer server;

    private final PriorityQueue<TaskAware> queue = new PriorityQueue<>(COMPARATOR);

    private long lastUpdateTick;

    public ServerScheduler(NucleonServer server) {
        this.server = Args.notNull(server, "Server");
    }

    @Override
    public TaskAware scheduleTask(Task task) {
        return registerTask(task, 0, 0);
    }

    @Override
    public TaskAware scheduleDelayedTask(Task task, long delay) {
        return registerTask(task, delay, 0);
    }

    @Override
    public TaskAware scheduleRepeatingTask(Task task, long period) {
        return registerTask(task, 0, period);
    }

    @Override
    public TaskAware scheduleDelayedRepeatingTask(Task task, long delay, long period) {
        return registerTask(task, delay, period);
    }

    private TaskAware registerTask(Task task, long delay, long period) {
        Args.notNegative(delay, "Delay");
        Args.notNegative(period, "Period");
        TaskAware aware = new TaskAware(task, delay, Math.max(period, 1));
        doScheduleTask(aware, false);
        return aware;
    }

    @Override
    public void processTick() {
        lastUpdateTick = server.getCurrentTick();
        while (!queue.isEmpty() && queue.peek().getNextRunTick() <= lastUpdateTick) {
            TaskAware task = queue.poll();

            if (task.isCancelled()) {
                continue;
            }

            task.getTask().onRun();

            if (task.isCancelled()) {
                continue;
            }
            if (task.isRepeating()) {
                doScheduleTask(task, true);
            }
        }
    }

    private void doScheduleTask(TaskAware task, boolean repeat) {
        if (repeat) {
            task.setNextRunTick(lastUpdateTick + task.getPeriod());
        } else {
            task.setNextRunTick(lastUpdateTick + task.getDelay());
        }
        queue.add(task);
    }
}
