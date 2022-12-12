package nucleon.player;

import com.nukkitx.protocol.bedrock.BedrockServerSession;
import lombok.Getter;

import java.util.UUID;

public final class Player {

    @Getter private final UUID uuid = UUID.randomUUID();
    @Getter private final PlayerChainData chainData;
    private final BedrockServerSession session;

    public Player(PlayerChainData chainData, BedrockServerSession session) {
        this.chainData = chainData;
        this.session = session;
    }
}
