package nucleon.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.unix.UnixChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.log4j.Log4j2;
import network.ycc.raknet.RakNet;
import network.ycc.raknet.pipeline.UserDataCodec;
import network.ycc.raknet.server.channel.RakNetServerChannel;
import nucleon.network.compression.PacketCompressor;
import nucleon.network.compression.PacketDecompressor;
import nucleon.network.handler.UnconnectedPingHandler;
import nucleon.network.packet.PacketDecoder;
import nucleon.network.packet.PacketHandler;
import nucleon.server.NucleonServer;
import nucleon.server.ServerSettings;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@Log4j2
public class Network extends Thread {

    private final InetSocketAddress serverAddress;
    private final EventLoopGroup workerGroup = new DefaultEventLoopGroup();
    private EventLoopGroup bossGroup;
    private ChannelFuture channelFuture;

    public Network(ServerSettings settings) {
        this.serverAddress = settings.getInetAddress();
    }

    @Override
    public void run() {
        try {
            this.bossGroup = Epoll.isAvailable() ? new EpollEventLoopGroup() : new NioEventLoopGroup();

            var bootstrap = new ServerBootstrap()
                    .group(this.bossGroup, this.workerGroup)
                    .channelFactory(() ->
                            new RakNetServerChannel(() ->
                                    Epoll.isAvailable() ? new EpollDatagramChannel() : new NioDatagramChannel()))
                    .option(RakNet.SERVER_ID, NucleonServer.ID);
            bootstrap.handler(new ChannelInitializer<>() {


                @SuppressWarnings("NullableProblems")
                @Override
                protected void initChannel(Channel channel) {
                    channel.pipeline().addLast(new UnconnectedPingHandler());
                }
            }).childHandler(new ChannelInitializer<>() {

                @Override
                protected void initChannel(Channel channel) {
                    var channelConfig = channel.config();
                    try {
                        channelConfig.setOption(ChannelOption.IP_TOS, 0x18);
                    } catch (ChannelException ignored) {/**/}

                    channelConfig.setAllocator(PooledByteBufAllocator.DEFAULT);

                    var rakNetConfig = (RakNet.Config) channelConfig;
                    rakNetConfig.setMaxQueuedBytes(8 * 1024 * 1024);

                    var pipeline = channel.pipeline();
                    pipeline.addFirst("timeout", new ReadTimeoutHandler(20, TimeUnit.SECONDS));
                    pipeline.addLast(UserDataCodec.NAME, new UserDataCodec(0xfe)); // 0xfe - batch packet id
                    pipeline.addLast(PacketCompressor.NAME, new PacketCompressor());
                    pipeline.addLast(PacketDecompressor.NAME, new PacketDecompressor());
                    pipeline.addLast(PacketDecoder.NAME, new PacketDecoder());
                    pipeline.addLast(PacketHandler.NAME, new PacketHandler());
                }
            });

            if (this.bossGroup.getClass().equals(EpollEventLoopGroup.class)) {
                bootstrap.option(UnixChannelOption.SO_REUSEPORT, true);
                bootstrap.option(EpollChannelOption.MAX_DATAGRAM_PAYLOAD_SIZE, 4 * 1024);
            } else {
                bootstrap.option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(4 * 1024, 64 * 1024, 256 * 1024));
            }

            this.channelFuture = bootstrap.bind(this.serverAddress);
            this.channelFuture.addListener($ -> log.info("Network started"));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void close() {
        try {
            this.interrupt();
            this.channelFuture.channel().closeFuture();
        } finally {
            this.workerGroup.shutdownGracefully();
            this.bossGroup.shutdownGracefully();
        }
    }
}
