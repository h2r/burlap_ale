package edu.brown.cs.burlap.screen;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

import java.awt.image.BufferedImage;

/**
 * Created by maroderi on 6/23/16.
 */
public class ScreenConverter {
    protected OpenCVFrameConverter.ToMat frameConverter;
    protected Java2DFrameConverter imageConverter;

    public ScreenConverter() {
        frameConverter = new OpenCVFrameConverter.ToMat();
        imageConverter = new Java2DFrameConverter();
    }

    public BufferedImage convert(opencv_core.Mat screen) {
        return imageConverter.convert(frameConverter.convert(screen));
    }
}
