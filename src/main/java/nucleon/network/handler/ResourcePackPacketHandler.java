package nucleon.network.handler;

import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import nucleon.player.PlayerChainData;

public class ResourcePackPacketHandler implements BedrockPacketHandler {

    private final BedrockServerSession session;
    private final PlayerChainData chainData;

    public ResourcePackPacketHandler(BedrockServerSession session, PlayerChainData chainData) {
        this.session = session;
        this.chainData = chainData;
    }

}
