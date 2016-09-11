package com.palyrobotics.frc2016;

import com.palyrobotics.frc2016.Robot.RobotName;
import com.palyrobotics.frc2016.subsystems.*;
import com.palyrobotics.lib.util.CheesyCompressor;
import com.palyrobotics.lib.util.CheesySolenoid;
import com.palyrobotics.lib.util.CheesySpeedController;
import com.palyrobotics.lib.util.XboxController;

//import com.team254.lib.util.gyro.GyroThread;
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
					Constants.kDericaLeftDriveEncoderDIOA, Constants.kDericaLeftDriveEncoderDIOB);
			kRightDriveEncoder = new Encoder(
					Constants.kDericaRightDriveEncoderDIOA, Constants.kDericaRightDriveEncoderDIOB);
		}
		System.out.println("DT about to init");
		kDrive = new Drive("drive", kLeftDriveMotor, kRightDriveMotor, kLeftDriveEncoder, kRightDriveEncoder);
		System.out.println("DT Initialized");
	}
    static CheesySpeedController kShooter = new CheesySpeedController(new CANTalon(Constants.kShooterTalonId), 
    		Constants.kShooterPDP);

	/*
	 * INTAKE
	 */
	public static Intake kIntake = null;
	static {
		if(Robot.name == RobotName.TYR) {
			CheesySpeedController kLeftIntakeMotor = new CheesySpeedController(
					new VictorSP(Constants.kTyrLeftIntakeMotorPWM),
					Constants.kTyrLeftIntakeMotorPDP);
			CheesySpeedController kRightIntakeMotor = new CheesySpeedController(
					new VictorSP(Constants.kTyrIntakeMotorPWM),
					Constants.kTyrRightIntakeMotorPDP);
			kIntake = new Intake("intake", kLeftIntakeMotor, kRightIntakeMotor);
		} else if (Robot.name == RobotName.DERICA) {
			System.out.println("Intake not initialized");
//			CheesySpeedController kIntakeMotor = new CheesySpeedController(
//					new VictorSP(Constants.kDericaIntakeMotorPWM),
//					Constants.kDericaIntakeMotorPDP);
//			CheesySpeedController kIntakeArmMotor = new CheesySpeedController(
//					new VictorSP(Constants.kDericaArmIntakeMotorPWM),
//					Constants.kDericaArmIntakeMotorPDP);
//			kIntake = new Intake("intake", kIntakeMotor, kIntakeArmMotor);
		}
	}
	// Pneumatic solenoids, only instantiate if Tyr
	static DoubleSolenoid kShooterSolenoid = null;
	static DoubleSolenoid kLatchSolenoid = null;
	static DoubleSolenoid kGrabberSolenoid = null;

	static {
		if(Robot.name == RobotName.TYR){
			kShooterSolenoid = new DoubleSolenoid(
					Constants.kShooterSolenoidPortExtend, Constants.kShooterSolenoidPortRetract);
			kLatchSolenoid = new DoubleSolenoid(
					Constants.kLatchSolenoidPortExtend, Constants.kLatchSolenoidPortRetract);
			kGrabberSolenoid = new DoubleSolenoid(
					Constants.kGrabberSolenoidPortExtend, Constants.kGrabberSolenoidPortRetract);
		}
	}
    // Subsystems
    public static TyrShooter kTyrShooter = new TyrShooter("shooter", kShooter, kShooterSolenoid, kLatchSolenoid);    
    
	// Sensors
	//    public static GyroThread kGyroThread = null; //new GyroThread();

	// Subsystems
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
