package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;
import org.firstinspires.ftc.vision.opencv.ColorRange;
import org.firstinspires.ftc.vision.opencv.ImageRegion;

public class CameraSetupper {
    public static ColorBlobLocatorProcessor getProcessor(ImageRegion region) {
        return getProcessor(region, ColorRange.GREEN);
    }

    public static ColorBlobLocatorProcessor getProcessor(ImageRegion region, ColorRange range) {
        return new ColorBlobLocatorProcessor.Builder()
                .setTargetColorRange(range)         // use a predefined color match
                .setContourMode(ColorBlobLocatorProcessor.ContourMode.EXTERNAL_ONLY)    // exclude blobs inside blobs
                .setRoi(region)  // search central 1/4 of camera view
                .setDrawContours(true)                        // Show contours on the Stream Preview
                .setBlurSize(5)                               // Smooth the transitions between different colors in image
                .build();
    }
}
