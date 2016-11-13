package com.palyrobotics.frc2016.input;

import com.palyrobotics.frc2016.subsystems.Drive.DriveGear;

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
	public static RobotName name = RobotName.DERICA;
	
	// No sensors on the robot currently
	public DriveGear gear;
}
