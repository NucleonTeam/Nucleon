package nucleon.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class PacketDecoder extends MessageToMessageDecoder<ByteBuf> {

    public static final String NAME = "packet-decoder";

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf input, List<Object> output) {
        // read packet data
    }
}
