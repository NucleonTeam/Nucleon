package nucleon.network.compression;

import io.netty.buffer.ByteBuf;

import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ZipCompressor implements AutoCloseable {

    private final byte[] buffer = new byte[8192];

    private final boolean compress;

    private final Deflater deflater;
    private final Inflater inflater;

    public ZipCompressor(boolean compress, boolean raw, int level) {
        this.compress = compress;
        if (compress) {
            this.deflater = new Deflater(level, raw);
            this.inflater = null;
        } else {
            this.deflater = null;
            this.inflater = new Inflater(raw);
        }
    }

    public void process(ByteBuf input, ByteBuf output) throws DataFormatException {
        byte[] inputData = new byte[input.readableBytes()];
        input.readBytes(inputData);
        if (this.compress && this.deflater != null) {
            this.deflater.setInput(inputData);
            this.deflater.finish();
            while (!this.deflater.finished()) {
                output.writeBytes(this.buffer, 0, this.deflater.deflate(this.buffer));
            }

            this.deflater.reset();
        } else {
            if (this.inflater != null) {
                this.inflater.setInput(inputData);
                while (!this.inflater.finished() && this.inflater.getTotalIn() < inputData.length) {
                    output.writeBytes(this.buffer, 0, this.inflater.inflate(this.buffer));
                }

                this.inflater.reset();
            }
        }
    }

    @Override
    public void close() {
        if (this.compress && this.deflater != null) {
            this.deflater.end();
        } else {
            if (this.inflater != null) {
                this.inflater.end();
            }
        }
    }
}
