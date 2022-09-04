package nucleon.network.compression;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;
import network.ycc.raknet.pipeline.FlushTickHandler;

import java.util.zip.DataFormatException;

public class PacketCompressor extends ChannelOutboundHandlerAdapter {

    public static final String NAME = "packet-compressor";

    private static final int POOL_BYTE_MAXIMUM = 128 * 1024;
    private static final int COMPONENT_MAXIMUM = 512;

    private ZipCompressor compressor;
    private CompositeByteBuf input, output;
    private boolean dirty;

    @Override
    public void handlerAdded(ChannelHandlerContext context) throws Exception {
        super.handlerAdded(context);

        this.compressor = new ZipCompressor(true, true, 7);

        var allocator = context.alloc();
        this.input = allocator.compositeDirectBuffer(COMPONENT_MAXIMUM);
        this.output = allocator.compositeDirectBuffer(COMPONENT_MAXIMUM);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext context) throws Exception {
        ReferenceCountUtil.safeRelease(this.output);
        ReferenceCountUtil.safeRelease(this.input);

        this.compressor.close();

        super.handlerRemoved(context);
    }

    @Override
    public void write(ChannelHandlerContext context, Object message, ChannelPromise promise) throws Exception {
        if (message instanceof ByteBuf) {
            var buffer = (ByteBuf) message;
            try {
                promise.trySuccess();
                if (buffer.isReadable()) {
                    if (POOL_BYTE_MAXIMUM < this.input.readableBytes()) {
                        this.flush0(context);
                    }

                    var headerBuffer = context.alloc().directBuffer(8, 8);

                    this.writeVarInt(buffer.readableBytes(), headerBuffer);

                    this.input.addComponent(true, headerBuffer);
                    this.input.addComponent(true, buffer.retain());

                    this.dirty = true;
                    if (POOL_BYTE_MAXIMUM < this.output.readableBytes()) {
                        this.flush0(context);
                    }

                    FlushTickHandler.checkFlushTick(context.channel());
                }
            } finally {
                ReferenceCountUtil.safeRelease(message);
            }
        } else {
            super.write(context, message, promise);
        }
    }

    @Override
    public void flush(ChannelHandlerContext context) throws Exception {
        if (this.dirty) {
            this.flush0(context);
        }

        super.flush(context);
    }

    private void flush0(ChannelHandlerContext context) throws DataFormatException {
        this.dirty = false;

        var allocator = context.alloc();
        var output = allocator.directBuffer(this.input.readableBytes() / 4 + 16);

        this.compressor.process(this.input, output);
        this.input.release();
        this.input = allocator.compositeDirectBuffer(COMPONENT_MAXIMUM);

        context.write(output).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

    private void writeVarInt(int value, ByteBuf buffer) {
        while (true) {
            if (0 == (value & ~0x7F)) {
                buffer.writeByte(value);
                return;
            } else {
                buffer.writeByte((byte) ((value & 0x7F) | 0x80));
                value >>>= 7;
            }
        }
    }
}
