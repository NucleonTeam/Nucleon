package nucleon.player;

import com.nukkitx.network.util.DisconnectReason;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

public final class Player {

    @Getter private final UUID id = UUID.randomUUID();
    @Getter private final PlayerChainData chainData;
    private final BedrockServerSession session;

    @Getter private boolean disconnected = false;

    public Player(PlayerChainData chainData, BedrockServerSession session) {
        this.chainData = chainData;
        this.session = session;

        onLogin();
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Player)) return false;

        return id.equals(((Player) obj).id);
    }

    public @NonNull String getName() {
        return chainData.getUsername();
    }

    private void onLogin() {
        PlayerManager.getInstance().addPlayer(this);
        session.addDisconnectHandler(this::onQuit);
        System.out.println("joined");
    }

    private void onQuit(DisconnectReason reason) {
        disconnected = true;
        PlayerManager.getInstance().removePlayer(this);
        System.out.println("quited");
    }
}
