package nucleon.network.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.ReferenceCountUtil;
import network.ycc.raknet.RakNet;
import network.ycc.raknet.packet.UnconnectedPing;
import network.ycc.raknet.packet.UnconnectedPong;
import network.ycc.raknet.server.pipeline.UdpPacketHandler;
import nucleon.server.NucleonServer;

import java.net.InetSocketAddress;
import java.util.StringJoiner;

public class UnconnectedPingHandler extends UdpPacketHandler<UnconnectedPing> {

    private static final String INFO = new StringJoiner(";")
            .add("MCPE")
            .add("motd")
            .add("486") // protocol version
            .add("1.18.10")
            .add("2") // online players count
            .add("10") // max players count
            .add(Long.toString(NucleonServer.ID))
            .add("sub motd")
            .add("creative")
            .add("1").toString();

    public UnconnectedPingHandler() {
        super(UnconnectedPing.class);
    }

    @Override
    protected void handle(ChannelHandlerContext context, InetSocketAddress socketAddress, UnconnectedPing ping) {
        var channel = context.channel();
        var rakNetConfig = (RakNet.Config) channel.config();

        var pong = new UnconnectedPong();
        pong.setClientTime(ping.getClientTime());
        pong.setServerId(rakNetConfig.getServerId());
        pong.setMagic(rakNetConfig.getMagic());
        pong.setInfo(INFO);

        var buffer = context.alloc().directBuffer(pong.sizeHint());
        try {
            rakNetConfig.getCodec().encode(pong, buffer);
            for (int i = 0; i < 3; i++) {
                var future = channel.writeAndFlush(new DatagramPacket(buffer.retainedSlice(), socketAddress));
                future.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
            }
        } finally {
            ReferenceCountUtil.safeRelease(pong);
            buffer.release();
        }
    }
}
