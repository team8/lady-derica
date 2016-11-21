package com.palyrobotics.frc2016.input;

import com.palyrobotics.frc2016.subsystems.Drive.DriveGear;
import com.team254.lib.util.Pose;

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

	public RobotName name = RobotName.DERICA;
	
	// No sensors on the robot currently
	public DriveGear gear;

	// Collects drivetrain sensor data into {@link Pose}
	public Pose getDrivePose() {
		return new Pose(0, 0, 0, 0, 0, 0);
	}
}
