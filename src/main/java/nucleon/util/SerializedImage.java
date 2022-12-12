package nucleon.util;

import it.unimi.dsi.fastutil.io.FastByteArrayOutputStream;
import lombok.Getter;
import nucleon.player.PlayerSkin;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Getter
public class SerializedImage {

    public static final SerializedImage EMPTY = new SerializedImage(0, 0, new byte[0]);

    private final int width;
    private final int height;
    private final byte[] data;

    private SerializedImage(int width, int height, byte[] data) {
        this.width = width;
        this.height = height;
        this.data = data;
    }

    public static SerializedImage create(int width, int height, byte[] data) {
        return new SerializedImage(width, height, data);
    }

    public static SerializedImage create(byte[] skinData) throws IllegalArgumentException {
        switch (skinData.length) {
            case PlayerSkin.SINGLE_SKIN_SIZE:
                return new SerializedImage(64, 32, skinData);

            case PlayerSkin.DOUBLE_SKIN_SIZE:
                return new SerializedImage(64, 64, skinData);

            case PlayerSkin.SKIN_128_64_SIZE:
                return new SerializedImage(128, 64, skinData);

            case PlayerSkin.SKIN_128_128_SIZE:
                return new SerializedImage(128, 128, skinData);
        }

        throw new IllegalArgumentException("Unknown legacy skin size");
    }

    public static SerializedImage create(BufferedImage image) throws IOException {
        try (var stream = new FastByteArrayOutputStream()) {
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    Color color = new Color(image.getRGB(x, y), true);
                    stream.write(color.getRed());
                    stream.write(color.getGreen());
                    stream.write(color.getBlue());
                    stream.write(color.getAlpha());
                }
            }
            image.flush();
            return new SerializedImage(image.getWidth(), image.getHeight(), stream.array);
        }
    }
}
