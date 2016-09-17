package edu.brown.cs.burlap.screen;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

import java.awt.image.BufferedImage;

/**
 * A utility class for converting between an OpenCV matrix of a Atari screen and a Buffered Image.
 *
 * @author Melrose Roderick.
 */
public class ScreenConverter {
    public static OpenCVFrameConverter.ToMat frameConverter = new OpenCVFrameConverter.ToMat();
    public static Java2DFrameConverter imageConverter = new Java2DFrameConverter();

    public static BufferedImage convert(opencv_core.Mat screen) {
        return imageConverter.convert(frameConverter.convert(screen));
    }
}
