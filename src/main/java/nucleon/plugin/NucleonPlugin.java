package nucleon.plugin;

import nucleon.event.Listener;

public abstract class NucleonPlugin {

    public boolean isEnabled() {
        return true;
    }

    public void registerListener(Listener listener) {
        // todo: Listener registration
    }
}
