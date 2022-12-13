package nucleon.world;

import lombok.NonNull;
import nucleon.block.Block;
import org.apache.commons.lang3.NotImplementedException;

public class NormalWorld extends World {

    public NormalWorld(@NonNull Dimension dimension) {
        super(dimension);
    }

    @Override
    public void setBlock(int x, int y, int z, @NonNull Block block) {
        throw new NotImplementedException();
    }

    @Override
    public @NonNull Block getBlock(int x, int y, int z) {
        throw new NotImplementedException();
    }
}
