package nucleon.world;

import lombok.Getter;
import lombok.NonNull;
import nucleon.block.Block;

import java.util.UUID;

public abstract class World implements Block.Getter, Block.Setter {

    @Getter private final UUID id = UUID.randomUUID();
    @Getter private final Dimension dimension;

    public World(@NonNull Dimension dimension) {
        this.dimension = dimension;
    }
}
