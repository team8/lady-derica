package com.palyrobotics.frc2016.util;
import com.palyrobotics.frc2016.Constants;
import com.palyrobotics.frc2016.behavior.RobotSetpoints;
import com.palyrobotics.frc2016.subsystems.Drive;
import com.team254.lib.util.DriveSignal;

import edu.wpi.first.wpilibj.DriverStation;

public class ProportionalDriveHelper {

	private Drive drive;

	public ProportionalDriveHelper(Drive drive) {
		this.drive = drive;
	}

	public void pDrive(double throttle, double wheel, RobotSetpoints setpoints) {
		if (DriverStation.getInstance().isAutonomous()) {
			return;
		}

		if (drive.hasController()) {
			return;
		}

		if(setpoints.drive_routine_action != RobotSetpoints.DriveRoutineAction.NONE) {
			return;
		}

		double angularPower = wheel;
		double rightPwm = throttle;
		double leftPwm = throttle;
		leftPwm += angularPower;
		rightPwm -= angularPower;

		drive.setOpenLoop(new DriveSignal(Constants.kDriveScalar*leftPwm, Constants.kDriveScalar*rightPwm));
	}

}