package nucleon.block;

import com.nukkitx.math.vector.Vector3i;
import lombok.NonNull;

import java.util.function.BiPredicate;

public interface Block {

    int getId();

    short stateId();

    boolean isAir();

    boolean isLiquid();

    boolean isSolid();

    default boolean compare(@NonNull Block block, Comparator comparator) {
        return comparator.test(this, block);
    }

    default boolean compare(@NonNull Block block) {
        return compare(block, Comparator.ID);
    }

    interface Setter {

        void setBlock(int x, int y, int z, @NonNull Block block);

        default void setBlock(@NonNull Vector3i pos, @NonNull Block block) {
            setBlock(pos.getX(), pos.getY(), pos.getZ(), block);
        }
    }

    interface Getter {

        @NonNull Block getBlock(int x, int y, int z);

        default @NonNull Block getBlock(@NonNull Vector3i pos) {
            return getBlock(pos.getX(), pos.getY(), pos.getZ());
        }
    }
    @FunctionalInterface
    interface Comparator extends BiPredicate<Block, Block> {

        Comparator IDENTITY = (b1, b2) -> b1 == b2;

        Comparator ID = (b1, b2) -> b1.getId() == b2.getId();

        Comparator STATE = (b1, b2) -> b1.stateId() == b2.stateId();
    }

}
