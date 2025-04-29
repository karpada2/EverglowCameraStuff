package org.firstinspires.ftc.teamcode;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;
import org.firstinspires.ftc.vision.opencv.ColorRange;
import org.firstinspires.ftc.vision.opencv.ImageRegion;

@TeleOp(name="vision auto")
public class VisionAuto extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotorSimple motor = hardwareMap.get(DcMotorSimple.class, "motor");
        Servo servo = hardwareMap.get(Servo.class, "servo");
        ColorBlobLocatorProcessor blobbyLeft = CameraSetupper.getProcessor(ImageRegion.asUnityCenterCoordinates(
                -1, 1, -0.25, -1));
        ColorBlobLocatorProcessor blobbyCenter = CameraSetupper.getProcessor(ImageRegion.asUnityCenterCoordinates(
                -0.25, 1, 0.25, -1));
        ColorBlobLocatorProcessor blobbyRight = CameraSetupper.getProcessor(ImageRegion.asUnityCenterCoordinates(
                0.25, 1, 1, -1));

        VisionPortal portal = new VisionPortal.Builder()
                .addProcessor(blobbyLeft)
                .addProcessor(blobbyCenter)
                .addProcessor(blobbyRight)
                .setCameraResolution(new Size(320, 240))
                .setCamera(hardwareMap.get(WebcamName.class, "webcam"))
                .build();

        waitForStart();

        while (opModeIsActive()) {
            
        }
    }
}
