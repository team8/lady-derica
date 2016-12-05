package com.palyrobotics.frc2016.util;

import com.palyrobotics.frc2016.subsystems.Drive.DriveGear;
import com.palyrobotics.frc2016.robot.team254.lib.util.Pose;

/**
 * Holds all hardware input, such as sensors. <br />
 * Can be simulated
 * @author Nihar
 *
 */
public class RobotState {
	public enum RobotName {
		TYR, DERICA
	}

	public enum GamePeriod {
		AUTO, TELEOP, DISABLED
	}
	public GamePeriod gamePeriod;

	public final RobotName name = RobotName.DERICA;
	
	// No sensors on the robot currently
	public DriveGear gear;

	// Collects drivetrain sensor data into {@link Pose}
	public Pose getDrivePose() {
		return new Pose(0, 0, 0, 0, 0, 0);
	}

	// DIO Values
	public double left_encoder;
	public double right_encoder;
	public double arm_potentiometer;
}
