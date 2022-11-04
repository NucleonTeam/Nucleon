package nucleon.network;

import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.data.PacketCompressionAlgorithm;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.*;

public class PlayerPacketHandler implements BedrockPacketHandler {

    private final BedrockServerSession serverSession;

    public PlayerPacketHandler(BedrockServerSession serverSession) {
        this.serverSession = serverSession;
    }

    private void sendPlayStatusPacket(PlayStatusPacket.Status status) {
        var pk = new PlayStatusPacket();
        pk.setStatus(status);
        serverSession.sendPacket(pk);
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
            serverSession.sendPacket(packet);
            return false;
        }

        var pk = new NetworkSettingsPacket();
        pk.setCompressionAlgorithm(PacketCompressionAlgorithm.ZLIB);
        pk.setCompressionThreshold(1);

        serverSession.setCompression(PacketCompressionAlgorithm.ZLIB);
        serverSession.setPacketCodec(Network.CODEC);

        serverSession.sendPacketImmediately(pk);
        return true;
    }
}
