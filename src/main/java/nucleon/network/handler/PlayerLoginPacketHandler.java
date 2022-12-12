package nucleon.network.handler;

import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.data.PacketCompressionAlgorithm;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.*;
import nucleon.event.player.PlayerPreLoginEvent;
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
        final PlayerChainData chainData;
        try {
            chainData = PlayerChainData.read(packet);
        } catch (IllegalArgumentException ex) {
            session.disconnect("Login failed");
            return false;
        }

        try {
            var event = new PlayerPreLoginEvent(chainData);
            event.call();

            switch (event.getResult()) {
                case REJECTED:
                    session.disconnect(event.getRejectReason());
                    return false;

                case ACCEPTED:
                    session.setPacketHandler(new ResourcePackPacketHandler(session, chainData));
                    sendPlayStatusPacket(PlayStatusPacket.Status.LOGIN_SUCCESS);
                    break;
            }

        } catch (Exception ex) {
            session.disconnect("Login error");
            return false;
        }
        return true;
    }
}
