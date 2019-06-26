package com.lisadevelopment.lisa.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtils {

    public static byte[] createColoredRectangle(int width, int height, Color color) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(color);
        graphics.fillRect(0, 0, width, height);
        graphics.dispose();
        ByteArrayOutputStream imageOutput = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", imageOutput);
        } catch (IOException e) {
            try {
                imageOutput.close();
            } catch (IOException ignore) {}
            imageOutput = null;
        }
        return imageOutput == null ? null : imageOutput.toByteArray();
    }
}
