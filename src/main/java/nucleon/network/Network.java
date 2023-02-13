package nucleon.network;

import com.nukkitx.protocol.bedrock.*;
import com.nukkitx.protocol.bedrock.v560.Bedrock_v560;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import lombok.extern.log4j.Log4j2;
import nucleon.network.handler.PlayerLoginPacketHandler;
import reactor.util.annotation.NonNull;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletionException;

@Log4j2(topic = "network")
public class Network implements BedrockServerEventHandler {

    public static final BedrockPacketCodec CODEC = Bedrock_v560.V560_CODEC;

    private final BedrockServer bedrockServer;
    private final InetSocketAddress address;
    private final BedrockPong bedrockPong = new BedrockPong();

    public Network(InetSocketAddress bindAddress) {
        this.address = bindAddress;
        this.bedrockServer = new BedrockServer(bindAddress, Runtime.getRuntime().availableProcessors());
        this.bedrockServer.setHandler(this);
    }

    public void start() {
        try {
            this.bedrockServer.bind().join();
            log.info("Network started on {}:{}", address.getHostString(), address.getPort());
        } catch (CompletionException exception) {
            exception.printStackTrace();
        }
    }

    public void stop() {
        log.warn("Network was stopped");
        this.bedrockServer.close();
    }

    @Override
    public boolean onConnectionRequest(InetSocketAddress address) {
        return true;
    }

    @Override
    public BedrockPong onQuery(InetSocketAddress address) {
        this.bedrockPong.setEdition("MCPE");
        this.bedrockPong.setGameType("creative");
        this.bedrockPong.setMotd("Nucleon server");
        this.bedrockPong.setSubMotd("Test server");
        this.bedrockPong.setPlayerCount(0);
        this.bedrockPong.setMaximumPlayerCount(5);

        var bindPort = this.bedrockServer.getBindAddress().getPort();
        this.bedrockPong.setIpv4Port(bindPort);
        this.bedrockPong.setIpv6Port(bindPort);
        this.bedrockPong.setNintendoLimited(false);
        this.bedrockPong.setProtocolVersion(CODEC.getProtocolVersion());
        this.bedrockPong.setVersion(CODEC.getMinecraftVersion());

        return this.bedrockPong;
    }

    @Override
    public void onSessionCreation(BedrockServerSession serverSession) {
        var address = serverSession.getAddress();
        log.info("[{}:{}] connected to the server", address.getHostString(), address.getPort());
        serverSession.addDisconnectHandler((reason) -> {
            log.info("[{}:{}] disconnected({})", address.getHostString(), address.getPort(), reason.name());
        });
        serverSession.setPacketHandler(new PlayerLoginPacketHandler(serverSession));
    }

    @Override
    public void onUnhandledDatagram(ChannelHandlerContext context, DatagramPacket packet) {

    }

    public @NonNull InetSocketAddress getAddress() {
        return address;
    }
}
