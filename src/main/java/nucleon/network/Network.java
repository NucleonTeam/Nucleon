package nucleon.network;

import com.nukkitx.protocol.bedrock.*;
import com.nukkitx.protocol.bedrock.v553.Bedrock_v553;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletionException;

public class Network implements BedrockServerEventHandler {

    public static final BedrockPacketCodec CODEC = Bedrock_v553.V553_CODEC;

    private final BedrockServer bedrockServer;

    private final BedrockPong bedrockPong = new BedrockPong();

    public Network(InetSocketAddress bindAddress) {
        this.bedrockServer = new BedrockServer(bindAddress, Runtime.getRuntime().availableProcessors());
        this.bedrockServer.setHandler(this);
    }

    public void start() {
        try {
            this.bedrockServer.bind().join();
            System.out.println("Network started");
        } catch (CompletionException exception) {
            exception.printStackTrace();
        }
    }

    public void stop() {
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
        this.bedrockPong.setMotd("test motd");
        this.bedrockPong.setSubMotd("test sub-motd");
        this.bedrockPong.setPlayerCount(2);
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
        System.out.println("Session: " + serverSession);
        // session create
    }

    @Override
    public void onUnhandledDatagram(ChannelHandlerContext context, DatagramPacket packet) {
        BedrockServerEventHandler.super.onUnhandledDatagram(context, packet);
    }
}
