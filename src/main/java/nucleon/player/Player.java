package nucleon.player;

import com.nukkitx.network.util.DisconnectReason;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.data.inventory.ItemData;
import com.nukkitx.protocol.bedrock.packet.AddPlayerPacket;
import com.nukkitx.protocol.bedrock.packet.UpdateAttributesPacket;
import lombok.Getter;
import lombok.NonNull;
import nucleon.entity.EntityLiving;
import nucleon.entity.VanillaEntities;
import nucleon.event.player.PlayerDisconnectEvent;
import nucleon.event.player.PlayerLoginEvent;

import java.util.UUID;

public final class Player extends EntityLiving {

    @Getter private final UUID id = UUID.randomUUID();
    @Getter private final PlayerChainData chainData;
    private final BedrockServerSession session;
    @Getter private GameMode gameMode = GameMode.SURVIVAL;

    @Getter private boolean disconnected = false;

    public Player(PlayerChainData chainData, BedrockServerSession session) {
        super(VanillaEntities.HUMAN);

        this.chainData = chainData;
        this.session = session;

        onLogin();
    }

    public @NonNull String getName() {
        return chainData.getUsername();
    }

    private void onLogin() {
        PlayerManager.getInstance().addPlayer(this);
        session.addDisconnectHandler(this::onQuit);

        var loginEvent = new PlayerLoginEvent(this);
        loginEvent.call();
    }

    private void onQuit(DisconnectReason reason) {
        disconnected = true;
        PlayerManager.getInstance().removePlayer(this);

        var disconnectEvent = new PlayerDisconnectEvent(this, reason);
        disconnectEvent.call();
        remove();
    }

    public void sendPacket(BedrockPacket packet) {
        session.sendPacket(packet);
    }

    @Override
    public synchronized void remove() {
        if (!disconnected) session.disconnect();
        super.remove();

        viewing.forEach(entity -> entity.removeViewer(this));
    }

    public void sendAttributes() {
        var pk = new UpdateAttributesPacket();
        pk.setRuntimeEntityId(getEntityId());
        //TODO
        sendPacket(pk);
    }

    public void setGameMode(@NonNull GameMode gameMode) {
        this.gameMode = gameMode;

        //TODO: send packet
    }

    @Override
    public void addViewer(Player player) {
        if (isViewer(player)) return;
        viewers.add(player.getId());
        player.viewing.add(this);

        if (player.isDisconnected() || player.isRemoved()) return;

        var pk = new AddPlayerPacket();
        pk.setUuid(player.getId());
        pk.setRuntimeEntityId(getEntityId());
        pk.setUniqueEntityId(getEntityId());
        pk.setPosition(getPosition().toFloat());
        pk.setMotion(getMotion().toFloat());
        pk.setRotation(getRotation());
        pk.setDeviceId(getChainData().getDeviceId());
        pk.setUsername(getName());
        pk.setHand(ItemData.AIR);
        player.sendPacket(pk);
    }
}
