package nucleon.network.compression;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.ReferenceCountUtil;

import java.util.List;

public class PacketDecompressor extends MessageToMessageDecoder<ByteBuf> {

    public static final String NAME = "packet-decompressor";

    private ZipCompressor compressor;

    @Override
    public void handlerAdded(ChannelHandlerContext context) {
        this.compressor = new ZipCompressor(false, true, 7);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext context) {
        this.compressor.close();
    }

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf input, List<Object> out) throws Exception {
        ByteBuf output = null;
        try {
            output = context.alloc().ioBuffer(input.readableBytes() << 2);

            this.compressor.process(input, output);
            while (output.isReadable()) {
                out.add(output.readRetainedSlice(this.readUnsignedVarInt(output)));
            }
        } finally {
            ReferenceCountUtil.safeRelease(output);
        }
    }

    private int readUnsignedVarInt(ByteBuf buffer) {
        int result = 0;
        for (int shift = 0; shift < 64; shift += 7) {
            var head = buffer.readByte();
            result |= (head & 0x7FL) << shift;
            if ((head & 0x80) == 0) {
                return result;
            }
        }

        throw new ArithmeticException("VarLong was too large");
    }
}
