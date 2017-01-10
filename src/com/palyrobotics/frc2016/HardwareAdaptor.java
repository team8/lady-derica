package com.palyrobotics.frc2016;

import com.ctre.CANTalon;
import com.palyrobotics.frc2016.Robot.RobotName;
import com.palyrobotics.frc2016.subsystems.*;
import com.palyrobotics.frc2016.util.XboxController;
import com.team254.lib.util.CheesySpeedController;

import edu.wpi.first.wpilibj.*;

public class HardwareAdaptor {
	/* 
	 * DRIVETRAIN
	 */
	public static Drive kDrive = null;

	static CheesySpeedController kLeftDriveMotor = null;
	static CheesySpeedController kRightDriveMotor = null;
	static Encoder kLeftDriveEncoder = null;
	static Encoder kRightDriveEncoder = null;
	static ADXRS450_Gyro kGyro;
	static DoubleSolenoid kShifterSolenoid = null;

	// Instantiate drive motors
	static {
		if(Robot.name == RobotName.TYR) {
			kLeftDriveMotor = new CheesySpeedController(
					new SpeedController[]{new CANTalon(Constants.kTyrLeftDriveFrontMotorDeviceID),
							new CANTalon(Constants.kTyrLeftDriveBackMotorDeviceID)},
					new int[]{Constants.kTyrLeftDriveFrontMotorPDP, Constants.kTyrLeftDriveBackMotorPDP});
			kRightDriveMotor = new CheesySpeedController(
					new SpeedController[]{new CANTalon(Constants.kTyrRightDriveFrontMotorDeviceID), 
							new CANTalon(Constants.kTyrRightDriveBackMotorDeviceID)}, 
					new int[]{Constants.kTyrRightDriveBackMotorPDP, Constants.kTyrRightDriveBackMotorPDP});
			kLeftDriveEncoder = new Encoder(
					Constants.kTyrLeftDriveEncoderDIOA, Constants.kTyrLeftDriveEncoderDIOB);
			kRightDriveEncoder = new Encoder(
					Constants.kTyrRightDriveEncoderDIOA, Constants.kTyrRightDriveEncoderDIOB);
			kGyro = new ADXRS450_Gyro();
			kShifterSolenoid = new DoubleSolenoid(
					Constants.kTyrDriveSolenoidExtend, Constants.kTyrDriveSolenoidRetract);
		}
		else if (Robot.name == RobotName.DERICA) {
			CANTalon leftDriveBackMotor = new CANTalon(Constants.kDericaLeftDriveBackMotorDeviceID);
			leftDriveBackMotor.enableBrakeMode(true);
			CANTalon leftDriveFrontMotor = new CANTalon(Constants.kDericaLeftDriveFrontMotorDeviceID);
			leftDriveFrontMotor.enableBrakeMode(true);
			CANTalon rightDriveBackMotor = new CANTalon(Constants.kDericaRightDriveBackMotorDeviceID);
			rightDriveBackMotor.enableBrakeMode(true);
			CANTalon rightDriveFrontMotor = new CANTalon(Constants.kDericaRightDriveFrontMotorDeviceID);
			rightDriveFrontMotor.enableBrakeMode(true);
			kLeftDriveMotor = new CheesySpeedController(
					new SpeedController[]{leftDriveFrontMotor, leftDriveBackMotor},
					new int[]{Constants.kDericaLeftDriveFrontMotorPDP, Constants.kDericaLeftDriveBackMotorPDP});
			kRightDriveMotor = new CheesySpeedController(
					new SpeedController[]{rightDriveFrontMotor, rightDriveBackMotor}, 
					new int[]{Constants.kDericaRightDriveBackMotorPDP, Constants.kDericaRightDriveBackMotorPDP});
			kLeftDriveEncoder = new Encoder(
					Constants.kDericaLeftDriveEncoderDIOA, Constants.kDericaLeftDriveEncoderDIOB, true);
			kRightDriveEncoder = new Encoder(
					Constants.kDericaRightDriveEncoderDIOA, Constants.kDericaRightDriveEncoderDIOB);
			kGyro = new ADXRS450_Gyro();
			// no shifter solenoid
		}
		kDrive = new Drive("drive", kLeftDriveMotor, kRightDriveMotor, kLeftDriveEncoder, kRightDriveEncoder, kGyro, kShifterSolenoid);
	}

	/*
	 * INTAKE
	 */
	public static Intake kIntake = null;
	static {
		if(Robot.name == RobotName.TYR) {
			CheesySpeedController kLeftIntakeMotor = new CheesySpeedController(
					new VictorSP(Constants.kTyrLeftIntakeMotorDeviceID),
					Constants.kTyrLeftIntakeMotorPDP);
			CheesySpeedController kRightIntakeMotor = new CheesySpeedController(
					new VictorSP(Constants.kTyrRightIntakeMotorDeviceID),
					Constants.kTyrRightIntakeMotorPDP);
			// null for lack of a potentiometer
			kIntake = new Intake("intake", kLeftIntakeMotor, kRightIntakeMotor, null);
			System.out.println("Intake initialized");
		} else if (Robot.name == RobotName.DERICA) {
			CheesySpeedController kIntakeMotor = new CheesySpeedController(
					new CANTalon(Constants.kDericaIntakeMotorPWM),
					Constants.kDericaIntakeMotorPDP);
			CheesySpeedController kIntakeArmMotor = new CheesySpeedController(
					new CANTalon(Constants.kDericaArmIntakeMotorPWM),
					Constants.kDericaArmIntakeMotorPDP);
			AnalogPotentiometer kArmPotentiometer = null;
			kIntake = new Intake("intake", kIntakeMotor, kIntakeArmMotor, kArmPotentiometer);
		}
	}
	
	/*
	 * SHOOTER/CATAPULT
	 * TyrShooter comes with Grabber
	 */

	// Pneumatic solenoids, only instantiate if Tyr
	static DoubleSolenoid kShooterSolenoid = null;
	static DoubleSolenoid kLatchSolenoid = null;
	static DoubleSolenoid kGrabberSolenoid = null;
	static CheesySpeedController kShooterMotor = null;
	
	public static TyrShooter kTyrShooter = null;
    public static DericaShooter kCatapult = null;
    public static LowGoalShooter kLowGoalShooter = null;

	static {
		if(Robot.name == RobotName.TYR){
			kShooterSolenoid = new DoubleSolenoid(
					Constants.kShooterSolenoidPortExtend, Constants.kShooterSolenoidPortRetract);
			kLatchSolenoid = new DoubleSolenoid(
					Constants.kLatchSolenoidPortExtend, Constants.kLatchSolenoidPortRetract);
			kGrabberSolenoid = new DoubleSolenoid(
					Constants.kGrabberSolenoidPortExtend, Constants.kGrabberSolenoidPortRetract);
			kShooterMotor = new CheesySpeedController(new CANTalon(Constants.kTyrShooterMotorDeviceID), 
					Constants.kTyrShooterMotorPDP);
			kTyrShooter = new TyrShooter("shooter", kShooterMotor, kShooterSolenoid, kLatchSolenoid, kGrabberSolenoid);
		} else {
			kCatapult = new DericaShooter("catapult", null, null, null, null, null);
			kLowGoalShooter = new LowGoalShooter("low goal shooter", new CheesySpeedController(
		    		new Victor(Constants.kDericaLowGoalShooterPWM), Constants.kDericaLowGoalShooterPDP));
		}
	}

	/*
	 * BREACHER
	 */
	public static Breacher kBreacher = null;
	public static CheesySpeedController kBreacherMotor = null;
	
	static {
		if(Robot.name == RobotName.TYR) {
			kBreacherMotor = new CheesySpeedController(new CANTalon(Constants.kBreacherMotorDeviceID), Constants.kBreacherMotorPDP);
			kBreacher = new Breacher("breacher", kBreacherMotor);
		}
	}
	
	public static PowerDistributionPanel kPDP = new PowerDistributionPanel();

	// Compressor
	//    public static Relay kCompressorRelay = new Relay(Constants.kCompressorRelayPort);
	//    public static DigitalInput kCompressorSwitch = new DigitalInput(Constants.kPressureSwitchDIO);
	//    public static CheesyCompressor kCompressor = new CheesyCompressor(kCompressorRelay, kCompressorSwitch);
	
	// Operator Interface
	public static Joystick kLeftStick = new Joystick(0);
	public static Joystick kRightStick = new Joystick(1);
	public static Joystick kOperatorStick;
	static{
		if(Robot.name == RobotName.TYR) {
			kOperatorStick = new XboxController(2);
		}
		else {
			kOperatorStick = new Joystick(2);
		}
	}
}