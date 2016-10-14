package com.palyrobotics.frc2016;

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
			kLeftDriveMotor = new CheesySpeedController(
					new SpeedController[]{new CANTalon(Constants.kDericaLeftDriveFrontMotorDeviceID), 
							new CANTalon(Constants.kDericaLeftDriveBackMotorDeviceID)},
					new int[]{Constants.kDericaLeftDriveFrontMotorPDP, Constants.kDericaLeftDriveBackMotorPDP});
			kRightDriveMotor = new CheesySpeedController(
					new SpeedController[]{new CANTalon(Constants.kDericaRightDriveFrontMotorDeviceID), 
							new CANTalon(Constants.kDericaRightDriveBackMotorDeviceID)}, 
					new int[]{Constants.kDericaRightDriveBackMotorPDP, Constants.kDericaRightDriveBackMotorPDP});
			kLeftDriveEncoder = new Encoder(
					Constants.kDericaLeftDriveEncoderDIOA, Constants.kDericaLeftDriveEncoderDIOB, true);
			kRightDriveEncoder = new Encoder(
					Constants.kDericaRightDriveEncoderDIOA, Constants.kDericaRightDriveEncoderDIOB);
			kGyro = new ADXRS450_Gyro();
			// no shifter solenoid
		}
		System.out.println(kLeftDriveMotor);
		System.out.println(kRightDriveMotor);
		System.out.println(kLeftDriveEncoder);
		System.out.println(kRightDriveEncoder);
		kDrive = new Drive("drive", kLeftDriveMotor, kRightDriveMotor, kLeftDriveEncoder, kRightDriveEncoder, kGyro, kShifterSolenoid);
		System.out.println("DT Initialized");
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

	public static DericaShooter kCatapult = new DericaShooter("catapult", null, null, null, null, null);
	
	// Pneumatic solenoids, only instantiate if Tyr
	static DoubleSolenoid kShooterSolenoid = null;
	static DoubleSolenoid kLatchSolenoid = null;
	static DoubleSolenoid kGrabberSolenoid = null;
	static CheesySpeedController kShooterMotor = null;
	
	public static TyrShooter kTyrShooter = null;
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
		}
	}

	/*
	 * BREACHER
	 */
	public static Breacher kBreacher = null;
	public static CheesySpeedController kBreacherMotor = null;
	
	static {
		kBreacherMotor = new CheesySpeedController(new CANTalon(Constants.kBreacherMotorDeviceID), Constants.kBreacherMotorPDP);
		kBreacher = new Breacher("breacher", kBreacherMotor);
	}
	
	public static PowerDistributionPanel kPDP = new PowerDistributionPanel();

	// Compressor
	//    public static Relay kCompressorRelay = new Relay(Constants.kCompressorRelayPort);
	//    public static DigitalInput kCompressorSwitch = new DigitalInput(Constants.kPressureSwitchDIO);
	//    public static CheesyCompressor kCompressor = new CheesyCompressor(kCompressorRelay, kCompressorSwitch);
	
	// Operator Interface
	public static Joystick kLeftStick = new Joystick(0);
	public static Joystick kRightStick = new Joystick(1);
	public static XboxController kOperatorStick = new XboxController(2);
}