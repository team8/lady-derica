package com.palyrobotics.frc2016;

import com.team254.lib.util.ConstantsBase;

public class Constants extends ConstantsBase {
	// Chezy Drive tuning
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

	//Encoder Turn in Place Controller gains
	public static double kEncoderTurnKp = 0;
	public static double kEncoderTurnKi = 0;
	public static double kEncoderTurnKd = 0;
	public static double kTyrDegreeToDistance = 0;
	public static double kDericaDegreeToDistance = 0;
	public static double kEncoderTurnError = 5;
	
	// TurnInPlaceController gains
	public static double kTurnMaxSpeedRadsPerSec = 4.5;
	public static double kTurnMaxAccelRadsPerSec2 = 4.5;
	public static double kTurnKp = 3.0;
	public static double kTurnKi = 0.18;
	public static double kTurnKd = 0.23;
	public static double kTurnKv = 0.085;
	public static double kTurnKa = 0.075;
	public static double kTurnOnTargetError = 0.1;

	// !!! End of editable Constants! !!!
	public static int kEndEditableArea = 0;

	// !!! Electrical constants (do not change at runtime, lol)
	/*
	 * TYR
	 */
	// DRIVETRAIN
	// Motors
	public static int kTyrLeftDriveFrontMotorDeviceID  = 1;
	public static int kTyrLeftDriveBackMotorDeviceID = 3;
	public static int kTyrLeftDriveFrontMotorPDP = 12; // Found using tyr code
	public static int kTyrLeftDriveBackMotorPDP = 13;
	public static int kTyrRightDriveFrontMotorDeviceID = 2;
	public static int kTyrRightDriveBackMotorDeviceID = 4;
	public static int kTyrRightDriveFrontMotorPDP = 14; // Found using tyr code
	public static int kTyrRightDriveBackMotorPDP = 15;
	// DIO Encoders
	public static int kTyrLeftDriveEncoderDIOA = 1;
	public static int kTyrLeftDriveEncoderDIOB = 0;
	public static int kTyrRightDriveEncoderDIOA = 2;
	public static int kTyrRightDriveEncoderDIOB = 3;

	// INTAKE
	public static int kTyrLeftIntakeMotorPWM = 1;
	public static int kTyrLeftIntakeMotorPDP = 7;
	public static int kTyrRightIntakeMotorPWM = 0;
	public static int kTyrRightIntakeMotorPDP = 8;

	// SHOOTER
	public static int kTyrShooterMotorDeviceID = 8;
	public static int kTyrShooterMotorPDP = 3; 
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
	// DRIVETRAIN
	// Motors
	public static int kDericaLeftDriveFrontMotorDeviceID  = 3;
	public static int kDericaLeftDriveBackMotorDeviceID = 4;
	public static int kDericaLeftDriveFrontMotorPDP = 14;
	public static int kDericaLeftDriveBackMotorPDP = 3;
	public static int kDericaRightDriveFrontMotorDeviceID = 1;
	public static int kDericaRightDriveBackMotorDeviceID = 2;
	public static int kDericaRightDriveFrontMotorPDP = 15;
	public static int kDericaRightDriveBackMotorPDP = 2;
	// DIO Encoders
	public static int kDericaLeftDriveEncoderDIOA = 2;
	public static int kDericaLeftDriveEncoderDIOB = 3;
	public static int kDericaRightDriveEncoderDIOA = 0;
	public static int kDericaRightDriveEncoderDIOB = 1;

	// INTAKE
	public static int kDericaIntakeMotorPWM = 7;
	public static int kDericaIntakeMotorPDP = 1;
	public static int kDericaArmIntakeMotorPWM = 8;
	public static int kDericaArmIntakeMotorPDP = 0;


	// Compressor Ports DON'T WORK
	public static int kCompressorRelayPort = 0;
	public static int kPressureSwitchDIO = 1;

	// !!! Physical constants

	// !!! Program constants
	public static double kControlLoopsDt = 0.005;

	// !!! Control loop constants
	public static double kLooperDt = 0.001;
	
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