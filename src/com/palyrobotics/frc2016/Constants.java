package com.palyrobotics.frc2016;

import com.palyrobotics.frc2016.Robot.RobotName;
import com.palyrobotics.lib.util.ConstantsBase;

public class Constants extends ConstantsBase {
	public static double kDriveSensitivity = .75;
	public static double kNegativeInertiaScalar = 5.0;

	// Manual control speed tuning
	public static double kManualIntakeSpeed = 1.0;
	public static double kManualExhaustSpeed = 1.0;

	// Autonomous intake speed
	public static double kAutoIntakeSpeed = 1.0;

	// DriveStraightController gains
	public static double kDriveMaxSpeedInchesPerSec = 120.0;
	public static double kDriveMaxAccelInchesPerSec2 = 107.0;
	public static double kDrivePositionKp = 0.7;
	public static double kDrivePositionKi = 0;
	public static double kDrivePositionKd = 0;
	public static double kDriveStraightKp = 3.0;
	public static double kDriveStraightKi = 0;
	public static double kDriveStraightKd = 0;
	public static double kDrivePositionKv = 0.008;
	public static double kDrivePositionKa = 0.0017;
	public static double kDriveOnTargetError = 0.75;
	public static double kDrivePathHeadingFollowKp = 0.01;

	// TurnInPlaceController gains
	public static double kTurnMaxSpeedRadsPerSec = 5.25;
	public static double kTurnMaxAccelRadsPerSec2 = 5.25;
	public static double kTurnKp = 3.0;
	public static double kTurnKi = 0.18;
	public static double kTurnKd = 0.23;
	public static double kTurnKv = 0.085;
	public static double kTurnKa = 0.075;
	public static double kTurnOnTargetError = 0.0225;

	// !!! End of editable Constants! !!!
	public static int kEndEditableArea = 0;

	// !!! Electrical constants (do not change at runtime, lol)
	/*
	 * TYR
	 */
	// Motors
	public static int kTyrLeftDriveMotorFrontDeviceID  = 1;
	public static int kTyrLeftDriveMotorBackDeviceID = 3;
	public static int kTyrLeftDriveMotor1PDP = 12; // Found using tyr code
	public static int kTyrLeftDriveMotor2PDP = 13;

	public static int kTyrRightDriveMotorFrontDeviceID = 2;
	public static int kTyrRightDriveMotorBackDeviceID = 4;
	public static int kTyrRightDriveMotor1PDP = 14; // Found using tyr code
	public static int kTyrRightDriveMotor2PDP = 15;
	// DIO Encoders
	public static int kTyrLeftDriveEncoderDIOA = 1;
	public static int kTyrLeftDriveEncoderDIOB = 0;
	public static int kTyrRightDriveEncoderDIOA = 2;
	public static int kTyrRightDriveEncoderDIOB = 3;

	//Intake
	public static int kTyrLeftIntakeMotorPWM = 1;
	public static int kTyrLeftIntakeMotorPDP = 7;
	public static int kTyrIntakeMotorPWM = 8;
	public static int kTyrRightIntakeMotorPDP = 8;
	
	// Solenoids (shooter, latch, grabber)
	public static int kShooterSolenoidPortExtend = 5;
	public static int kShooterSolenoidPortRetract = 2;
	public static int kLatchSolenoidPortExtend = 1;
	public static int kLatchSolenoidPortRetract = 6;
	public static int kGrabberSolenoidPortExtend = 0;
	public static int kGrabberSolenoidPortRetract = 7;


	/*
	 * DERICA
	 */
	
	// Motors
	public static int kDericaLeftDriveMotorFrontDeviceID  = 3;
	public static int kDericaLeftDriveMotorBackDeviceID = 4;
	//TODO FIND PDP slots THROUGH TESTING, THESE ARE NOT CORRECT
	public static int kDericaLeftDriveMotor1PDP = 12;
	public static int kDericaLeftDriveMotor2PDP = 13;

	public static int kDericaRightDriveMotorFrontDeviceID = 1;
	public static int kDericaRightDriveMotorBackDeviceID = 2;
	public static int kDericaRightDriveMotor1PDP = 14;
	public static int kDericaRightDriveMotor2PDP = 15;

	public static int kDericaLeftDriveEncoderDIOA = 2;
	public static int kDericaLeftDriveEncoderDIOB = 3;
	public static int kDericaRightDriveEncoderDIOA = 1;
	public static int kDericaRightDriveEncoderDIOB = 2;
	
	// INTAKE
	public static int kDericaIntakeMotorPWM;
	public static int kDericaIntakeMotorPDP;
	public static int kDericaArmIntakeMotorPWM;
	public static int kDericaArmIntakeMotorPDP;
	
	// Analog I/O

	// Compressor Ports DON'T WORK
	public static int kCompressorRelayPort = 0;
	public static int kPressureSwitchDIO = 1;

	// !!! Physical constants

	// !!! Program constants
	public static double kControlLoopsDt = 0.005;

	// !!! Control loop constants

	// Drive parameters
	public static double kDriveEncoderCountsPerRev = 250.0;
	public static double kDriveWheelSizeInches = 8; //pneumatic wheels

	@Override
	public String getFileLocation() {
		return "~/constants.txt";
	}

	static {
		new Constants().loadFromFile();
	}
}
