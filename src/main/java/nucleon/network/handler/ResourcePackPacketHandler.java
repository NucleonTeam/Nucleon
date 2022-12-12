package nucleon.network.handler;

import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.ResourcePackChunkRequestPacket;
import com.nukkitx.protocol.bedrock.packet.ResourcePackClientResponsePacket;
import nucleon.player.Player;
import nucleon.player.PlayerChainData;

public class ResourcePackPacketHandler implements BedrockPacketHandler {

    private final BedrockServerSession session;
    private final PlayerChainData chainData;

    public ResourcePackPacketHandler(BedrockServerSession session, PlayerChainData chainData) {
        this.session = session;
        this.chainData = chainData;
    }

    @Override
    public boolean handle(ResourcePackClientResponsePacket packet) {
        //TODO: implement loading packs

        complete();
        return true;
    }

    @Override
    public boolean handle(ResourcePackChunkRequestPacket packet) {
        //TODO
        return true;
    }

    private void complete() {
        var player = new Player(chainData, session);
        var handler = new InGamePacketHandler(session, player);
        session.setPacketHandler(handler);
        handler.sendStartGamePacket();
    }
}
