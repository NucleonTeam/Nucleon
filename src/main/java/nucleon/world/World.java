package nucleon.world;

import lombok.Getter;
import lombok.NonNull;
import nucleon.block.Block;
import nucleon.entity.Entity;
import nucleon.player.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class World implements Block.Getter, Block.Setter {

    @Getter private final UUID id = UUID.randomUUID();
    @Getter private final Dimension dimension;
    private final ConcurrentHashMap<UUID, Player> players = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Entity> entities = new ConcurrentHashMap<>();

    public World(@NonNull Dimension dimension) {
        this.dimension = dimension;
    }

    public @NonNull Collection<Player> getPlayers() {
        return new HashSet<>(players.values());
    }

    public void addPlayer(Player player) {
        players.put(player.getId(), player);
        player.setWorld(this);
    }

    public void removePlayer(Player player) {
        players.remove(player.getId());
    }

    public @NonNull Collection<Entity> getEntities() {
        return new HashSet<>(entities.values());
    }

    public void addEntity(Entity entity) {
        entities.put(entity.getEntityId(), entity);
        entity.setWorld(this);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity.getEntityId());
        entity.remove();
    }
}
