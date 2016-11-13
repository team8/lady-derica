package com.palyrobotics.frc2016.util;
import com.palyrobotics.frc2016.input.Commands;
import com.palyrobotics.frc2016.subsystems.Drive;
import com.team254.lib.util.DriveSignal;

import edu.wpi.first.wpilibj.DriverStation;

public class ProportionalDriveHelper {

	private Drive drive;
	private DriveSignal signal = new DriveSignal(0, 0);

	public ProportionalDriveHelper(Drive drive) {
		this.drive = drive;
	}

	public void pDrive(Commands commands) {
		double throttle = -commands.leftStick.y;
		double wheel = commands.rightStick.x;
		if (DriverStation.getInstance().isAutonomous()) {
			return;
		}

		if (drive.hasController()) {
			return;
		}

		if(commands.routineRequest != Commands.RoutineRequest.NONE) {
			return;
		}

		double angularPower = wheel;
		double rightPwm = throttle;
		double leftPwm = throttle;
		leftPwm += angularPower;
		rightPwm -= angularPower;

		signal.leftMotor = leftPwm;
		signal.rightMotor = rightPwm;
		drive.setOpenLoop(new DriveSignal(leftPwm, rightPwm));
	}

}