package org.firstinspires.ftc.teamcode;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;
import org.firstinspires.ftc.vision.opencv.ImageRegion;
import org.openftc.easyopencv.OpenCvCamera;
import org.firstinspires.ftc.vision.VisionPortal;

import java.util.List;

@TeleOp(name="vision auto")
public class VisionAuto extends LinearOpMode {
    public static int minSize = 500;
    public static int tolerance = 1500;
    public static int maxPixels = 38_005;
    static double minServoPosition = 0.35;
    static double maxServoPosition = 0.65;
    public static double motorPower = -0.18;
    Servo steer;
    DcMotorSimple motor;
    OpenCvCamera camera;
    ColorBlobLocatorProcessor leftProcessor;
    ColorBlobLocatorProcessor rightProcessor;

    @Override
    public void runOpMode(){
        leftProcessor = CameraSetupper.getProcessor(ImageRegion.asUnityCenterCoordinates(-1, 1, 0, -1));

        rightProcessor = CameraSetupper.getProcessor(ImageRegion.asUnityCenterCoordinates(0, 1, 1, -1));

        VisionPortal portal = new VisionPortal.Builder()
                .addProcessor(leftProcessor)
                .addProcessor(rightProcessor)
                .setCameraResolution(new Size(320, 240))
                .setCamera(hardwareMap.get(WebcamName.class, "webcam"))
                .build();

        telemetry.setMsTransmissionInterval(50);   // Speed up telemetry updates, Just use for debugging.
        telemetry.setDisplayFormat(Telemetry.DisplayFormat.MONOSPACE);

        steer = hardwareMap.get(Servo.class, "servo");
        motor = hardwareMap.get(DcMotorSimple.class, "motor");


        steer.setDirection(Servo.Direction.REVERSE);

        waitForStart();

        double m = maxServoPosition - minServoPosition;


        int loops = 0;
        while (opModeIsActive()) {
            loops++;
            List<ColorBlobLocatorProcessor.Blob> leftBlobs = leftProcessor.getBlobs();
            ColorBlobLocatorProcessor.Util.filterByArea(minSize, maxPixels, leftBlobs);  // filter out very small blobs.
            int leftAmount = 0;
            for (ColorBlobLocatorProcessor.Blob blob : leftBlobs) {
                leftAmount += blob.getContourArea();
            }

            List<ColorBlobLocatorProcessor.Blob> rightBlobs = rightProcessor.getBlobs();
            ColorBlobLocatorProcessor.Util.filterByArea(minSize, maxPixels, rightBlobs);  // filter out very small blobs.
            int rightAmount = 0;
            for (ColorBlobLocatorProcessor.Blob blob : rightBlobs) {
                rightAmount += blob.getContourArea();
            }

            if ((isRoughlyEqual(maxPixels, leftAmount) && isRoughlyEqual(maxPixels, rightAmount))) {
                steer.setPosition(0.5);
                motor.setPower(0);
                telemetry.addData("status", "reached target");
            }
            else if (isNoticeablyBigger(leftAmount, rightAmount)) {
                double steerInput = (double)(rightAmount)/leftAmount;

                steer.setPosition(minServoPosition + ((m/2)*steerInput));
                motor.setPower(motorPower);
                telemetry.addData("status", "turning left");

            }
            else if (isNoticeablyBigger(rightAmount, leftAmount)) {
                double steerInput = (double)(leftAmount)/rightAmount;
                steerInput *= -1;
                steerInput += 1;

                steer.setPosition((minServoPosition + m/2) + ((m/2)*steerInput));
                motor.setPower(motorPower);
                telemetry.addData("status", "turning right");

            }
            else {
                steer.setPosition(0.5);
                motor.setPower(motorPower);
                telemetry.addData("status", "nothing found");

            }

            telemetry.addData("right amount", rightAmount);
            telemetry.addData("left amount", leftAmount);
            telemetry.addData("motor power", motor.getPower());
            telemetry.addData("loops per second", loops/getRuntime());
            telemetry.update();
            sleep(50);
        }
    }

    public static boolean isRoughlyEqual(int num1, int num2) {
        return Math.abs(num1 - num2) <= tolerance;
    }

    public static boolean isNoticeablyBigger(int num1, int num2) {
        return num1 - num2 > tolerance;
    }
}
