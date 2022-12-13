package nucleon.entity;

import com.nukkitx.math.vector.Vector3d;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.protocol.bedrock.data.entity.EntityDataMap;
import com.nukkitx.protocol.bedrock.packet.AddEntityPacket;
import com.nukkitx.protocol.bedrock.packet.RemoveEntityPacket;
import lombok.Getter;
import lombok.NonNull;
import nucleon.entity.metadata.EntityMetadata;
import nucleon.player.Player;
import nucleon.player.PlayerManager;
import nucleon.world.World;
import reactor.util.annotation.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public abstract class Entity {

    private static final AtomicLong freeId = new AtomicLong(1);

    @Getter private final long entityId = getFreeId();
    @Getter private boolean spawned = false;
    @Getter private boolean removed = false;
    @Getter private final EntityType entityType;
    protected World world = null;
    @Getter protected Vector3d position = Vector3d.ZERO;
    @Getter protected Vector3d motion = Vector3d.ZERO;
    @Getter protected Vector3f rotation = Vector3f.ZERO;
    protected final HashSet<UUID> viewers = new HashSet<>();
    protected final HashSet<Entity> viewing = new HashSet<>();
    @Getter private final EntityMetadata metadata;

    public Entity(EntityType entityType) {
        this.entityType = entityType;
        this.metadata = entityType.getMetadataSupplier().get();
    }

    public static long getFreeId() {
        return freeId.getAndIncrement();
    }

    public final void spawn(World world, Vector3d pos) {
        if (spawned) return;

        spawned = true;
        setWorld(world);
        this.position = pos;
        onSpawn();
    }

    public void respawn(World world, Vector3d spawnPos) {

    }

    protected void onSpawn() {

    }

    public void addViewer(Player player) {
        if (isViewer(player)) return;
        viewers.add(player.getId());
        player.viewing.add(this);

        if (player.isDisconnected() || player.isRemoved()) return;

        var pk = new AddEntityPacket();
        pk.setUniqueEntityId(entityId);
        pk.setRuntimeEntityId(entityId);
        pk.setPosition(position.toFloat());
        pk.setMotion(motion.toFloat());
        pk.setRotation(rotation);
        pk.setIdentifier(entityType.getIdentifier());
        pk.getMetadata().putAll(getMetadata().getDataMap());
        player.sendPacket(pk);
    }

    public void removeViewer(Player player) {
        viewers.remove(player.getId());
        player.viewing.remove(this);

        if (player.isDisconnected() || player.isRemoved()) return;

        var pk = new RemoveEntityPacket();
        pk.setUniqueEntityId(entityId);
        player.sendPacket(pk);
    }

    public final @NonNull Collection<Player> getViewers() {
        var playerManager = PlayerManager.getInstance();
        return viewers.stream().map(playerManager::getPlayerById).collect(Collectors.toSet());
    }

    public final boolean isViewer(Player player) {
        return viewers.contains(player.getId());
    }

    public final @Nullable World getWorld() {
        return world;
    }

    public void setPosition(@NonNull Vector3d newPosition) {
        position = newPosition;
    }

    public void setMotion(@NonNull Vector3d motion) {
        this.motion = motion;
    }

    public void setRotation(@NonNull Vector3f rotation) {
        this.rotation = rotation;
    }

    public void setRotation(float pitch, float yaw, float headYaw) {
        setRotation(Vector3f.from(pitch, yaw, headYaw));
    }

    public final float getPitch() {
        return rotation.getX();
    }

    public final void setPitch(float value) {
        setRotation(Vector3f.from(value, rotation.getY(), rotation.getZ()));
    }

    public final float getYaw() {
        return rotation.getY();
    }

    public final void setYaw(float value) {
        setRotation(Vector3f.from(rotation.getX(), value, rotation.getZ()));
    }

    public final float getHeadYaw() {
        return rotation.getZ();
    }

    public final void setHeadYaw(float value) {
        setRotation(Vector3f.from(rotation.getX(), rotation.getY(), value));
    }

    public synchronized void setWorld(@NonNull World world) {
        if (world.equals(this.world)) return;
        this.world = world;
        world.addEntity(this);
        if (this instanceof Player) world.addPlayer((Player) this);
    }

    public void teleport(@NonNull Vector3d pos) {
        setPosition(pos);
        //TODO
    }

    public void teleport(@NonNull Vector3d pos, @NonNull World world) {
        setWorld(world);
        teleport(pos);
    }

    public synchronized void remove() {
        if (removed) return;
        removed = true;

        if (world != null) {
            world.removeEntity(this);
            if (this instanceof Player) world.removePlayer((Player) this);
        }
        world = null;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(entityId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Entity)) return false;

        return entityId == ((Entity) obj).entityId;
    }
}
