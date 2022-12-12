package nucleon.network.handler;

import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.data.PacketCompressionAlgorithm;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.*;
import nucleon.network.Network;
import nucleon.player.PlayerChainData;

public class PlayerLoginPacketHandler implements BedrockPacketHandler {

    private final BedrockServerSession session;

    public PlayerLoginPacketHandler(BedrockServerSession serverSession) {
        this.session = serverSession;
    }

    private void sendPlayStatusPacket(PlayStatusPacket.Status status) {
        var pk = new PlayStatusPacket();
        pk.setStatus(status);
        session.sendPacket(pk);
    }

    @Override
    public boolean handle(RequestNetworkSettingsPacket packet) {
        var protocol = packet.getProtocolVersion();
        if (protocol != Network.CODEC.getProtocolVersion()) {
            String disconnectMessage;
            if (protocol < Network.CODEC.getProtocolVersion()) {
                disconnectMessage = "disconnectionScreen.outdatedClient";
                sendPlayStatusPacket(PlayStatusPacket.Status.LOGIN_FAILED_CLIENT_OLD);
            } else {
                disconnectMessage = "disconnectionScreen.outdatedServer";
                sendPlayStatusPacket(PlayStatusPacket.Status.LOGIN_FAILED_SERVER_OLD);
            }

            var disconnectPacket = new DisconnectPacket();
            disconnectPacket.setKickMessage(disconnectMessage);
            session.sendPacket(packet);
            return false;
        }

        var pk = new NetworkSettingsPacket();
        pk.setCompressionAlgorithm(PacketCompressionAlgorithm.ZLIB);
        pk.setCompressionThreshold(1);

        session.setCompression(PacketCompressionAlgorithm.ZLIB);
        session.setPacketCodec(Network.CODEC);

        session.sendPacketImmediately(pk);
        return true;
    }

    @Override
    public boolean handle(LoginPacket packet) {
        var chainData = PlayerChainData.read(packet);

        session.setPacketHandler(new ResourcePackPacketHandler(session, chainData));

        sendPlayStatusPacket(PlayStatusPacket.Status.LOGIN_SUCCESS);
        return true;
    }
}
