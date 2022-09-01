package nucleon.scheduler;

public interface Task {

    void onRun();

    default void onCancel() {}
}
