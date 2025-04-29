package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Driving OpMode")
public class DrivingOpMode extends LinearOpMode {
    static double minServoPosition = 0.35;
    static double maxServoPosition = 0.65;

    static double maxMotorPower = 0.6;
    Servo steer;
    DcMotorSimple motor;
    @Override
    public void runOpMode() throws InterruptedException {
        steer = hardwareMap.get(Servo.class, "servo");
        motor = hardwareMap.get(DcMotorSimple.class, "motor");

        double m = (minServoPosition - maxServoPosition)/(-2);

        steer.setDirection(Servo.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {
            motor.setPower(gamepad1.right_stick_y*maxMotorPower);
            steer.setPosition((m*gamepad1.left_stick_x) + 0.5);
        }
    }
}
