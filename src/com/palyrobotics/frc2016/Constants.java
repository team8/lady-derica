package com.palyrobotics.frc2016;

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
    // Motors
    public static int kLeftDriveMotorFrontDeviceID = 1;
    public static int kLeftDriveMotorBackDeviceID = 3;
    public static int kLeftDriveMotor1PDP = 12; // Tested using tyr code
    public static int kLeftDriveMotor2PDP = 13; // Unsure about ordering

    public static int kRightDriveMotorFrontDeviceID = 2;
    public static int kRightDriveMotorBackDeviceID = 4;
    public static int kRightDriveMotor1PDP = 14; // Tested using tyr
    public static int kRightDriveMotor2PDP = 15; // Unsure if swapped

    public static int kLeftPeacockMotorPWM = 0;
    public static int kLeftPeacockMotorPDP = 0;
    public static int kRightPeacockMotorPWM = 9;
    public static int kRightPeacockMotorPDP = 0;

    public static int kTopCarriageMotor1PWM = 2;
    public static int kTopCarriageMotor2PWM = 3;
    public static int kTopCarriageMotor1PDP = 12;
    public static int kTopCarriageMotor2PDP = 13;

    public static int kBottomCarriageMotor1PWM = 6;
    public static int kBottomCarriageMotor2PWM = 7;
    public static int kBottomCarriageMotor1PDP = 2;
    public static int kBottomCarriageMotor2PDP = 3;

    public static int kLeftIntakeMotorPWM = 1;
    public static int kLeftIntakeMotorPDP = 7;
    public static int kRightIntakeMotorPWM = 8;
    public static int kRightIntakeMotorPDP = 8;

    // DIO
    public static int kLeftDriveEncoderDIOA = 1;
    public static int kLeftDriveEncoderDIOB = 0;
    public static int kRightDriveEncoderDIOA = 2;
    public static int kRightDriveEncoderDIOB = 3;

    // Analog I/O
    
    
    // Solenoids (shooter, latch, grabber)
    public static int kShooterSolenoidPortExtend = 5;
    public static int kShooterSolenoidPortRetract = 2;
    public static int kLatchSolenoidPortExtend = 1;
    public static int kLatchSolenoidPortRetract = 6;
    public static int kGrabberSolenoidPortExtend = 0;
    public static int kGrabberSolenoidPortRetract = 7;

    // Compressor Ports DON'T WORK
    public static int kCompressorRelayPort = 0;
    public static int kPressureSwitchDIO = 1;

    // !!! Physical constants

    // !!! Program constants
    public static double kControlLoopsDt = 0.005;

    // !!! Control loop constants

    // Drive parameters
    public static double kDriveEncoderCountsPerRev = 250.0;
    public static double kDriveWheelSizeInches = 6; //pneumatic wheels

    @Override
    public String getFileLocation() {
        return "~/constants.txt";
    }

    static {
        new Constants().loadFromFile();
    }
}
