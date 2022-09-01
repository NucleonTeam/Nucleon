package nucleon.plugin;

public abstract class NucleonPlugin implements Plugin {

    @Override
    public boolean isEnabled() {
        return false;
    }
}
