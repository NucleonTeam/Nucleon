package nucleon.network.packet;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.ReadTimeoutException;
import network.ycc.raknet.packet.Packet;

import java.net.InetSocketAddress;

public class PacketHandler extends SimpleChannelInboundHandler<Packet> {

    public static final String NAME = "protocol-handler";

    @Override
    protected void channelRead0(ChannelHandlerContext context, Packet packet) {
        var playerAddress = (InetSocketAddress) context.channel().remoteAddress();

        // packet handle
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
        if (cause instanceof ReadTimeoutException) {
            var channel = context.channel();
            var playerAddress = (InetSocketAddress) channel.remoteAddress();

            // timeout

            channel.close();
        } else {
            super.exceptionCaught(context, cause);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext context) {
        var playerAddress = (InetSocketAddress) context.channel().remoteAddress();

        // player joined
    }

    @Override
    public void channelInactive(ChannelHandlerContext context) {
        try {
            var channel = context.channel();
            var playerAddress = (InetSocketAddress) channel.remoteAddress();

            // player disconnected

            channel.close();
            super.channelInactive(context);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
