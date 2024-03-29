package nucleon.server;

import lombok.Getter;
import nucleon.network.Network;

@Getter
public class ServerInitializer {

    // ItemRegistry.Initializer getItemInitializer()

    // BlockRegistry.Initializer getBlockInitializer()

    // EntityRegistry.Initializer getEntityInitializer()

    // WorldRegistry.Initializer getWorldInitializer()

    public abstract static class Initializer<T> {

        protected final T manager;

        public Initializer(T manager) {
            this.manager = manager;
        }
    }
}
