package com.palyrobotics.frc2016.util;
import com.palyrobotics.frc2016.input.Commands;
import com.team254.lib.util.DriveSignal;

public class ProportionalDriveHelper {
	private DriveSignal signal = DriveSignal.NEUTRAL;

	public DriveSignal pDrive(Commands commands) {
		double throttle = -commands.leftStickInput.y;
		double wheel = commands.rightStickInput.x;

		double rightPwm = throttle - wheel;
		double leftPwm = throttle + wheel;

		signal.leftMotor = leftPwm;
		signal.rightMotor = rightPwm;
		return new DriveSignal(leftPwm, rightPwm);
	}

}