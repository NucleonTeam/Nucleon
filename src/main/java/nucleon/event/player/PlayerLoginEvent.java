package nucleon.event.player;

import com.nukkitx.math.vector.Vector3d;
import lombok.NonNull;
import nucleon.player.Player;
import nucleon.world.World;
import nucleon.world.WorldManager;
import reactor.util.annotation.Nullable;

public class PlayerLoginEvent extends PlayerEvent {

    private World spawningWorld = WorldManager.defaultWorld;
    private Vector3d spawnPosition = Vector3d.ZERO;

    public PlayerLoginEvent(Player player) {
        super(player);
    }

    public @Nullable World getSpawningWorld() {
        return spawningWorld;
    }

    public void setSpawningWorld(@NonNull World world) {
        spawningWorld = world;
    }

    public @NonNull Vector3d getSpawnPosition() {
        return spawnPosition;
    }

    public void setSpawnPosition(@NonNull Vector3d pos) {
        spawnPosition = pos;
    }
}
