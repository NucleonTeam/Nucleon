package nucleon.event;

public class TestEvent extends Event implements Cancellable {

    public final String testString;

    public TestEvent(String testString) {
        this.testString = testString;
    }
}
