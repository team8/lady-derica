package com.palyrobotics.frc2016;

import com.palyrobotics.frc2016.Robot.RobotName;
import com.palyrobotics.frc2016.subsystems.*;
import com.palyrobotics.lib.util.CheesyCompressor;
import com.palyrobotics.lib.util.CheesySolenoid;
import com.palyrobotics.lib.util.CheesySpeedController;

//import com.team254.lib.util.gyro.GyroThread;
import edu.wpi.first.wpilibj.*;

public class HardwareAdaptor {
	/* 
	 * DRIVETRAIN
	 */
	public static Drive kDrive;

	static CheesySpeedController kLeftDriveMotor;
	static CheesySpeedController kRightDriveMotor;
	static Encoder kLeftDriveEncoder;
	static Encoder kRightDriveEncoder;

	// Instantiate drive motors
	static {
		if(Robot.name == RobotName.TYR) {
			kLeftDriveMotor = new CheesySpeedController(
					new SpeedController[]{new CANTalon(Constants.kTyrLeftDriveMotorFrontDeviceID), 
							new CANTalon(Constants.kTyrLeftDriveMotorBackDeviceID)},
					new int[]{Constants.kTyrLeftDriveMotor1PDP, Constants.kTyrLeftDriveMotor2PDP});
			kRightDriveMotor = new CheesySpeedController(
					new SpeedController[]{new CANTalon(Constants.kTyrRightDriveMotorFrontDeviceID), 
							new CANTalon(Constants.kTyrRightDriveMotorBackDeviceID)}, 
					new int[]{Constants.kTyrRightDriveMotor2PDP, Constants.kTyrRightDriveMotor2PDP});
			kLeftDriveEncoder = new Encoder(
					Constants.kTyrLeftDriveEncoderDIOA, Constants.kTyrLeftDriveEncoderDIOB);
			kRightDriveEncoder = new Encoder(
					Constants.kTyrRightDriveEncoderDIOA, Constants.kTyrRightDriveEncoderDIOB);
		}
		else if (Robot.name == RobotName.DERICA) {
			kLeftDriveMotor = new CheesySpeedController(
					new SpeedController[]{new CANTalon(Constants.kDericaLeftDriveMotorFrontDeviceID), 
							new CANTalon(Constants.kDericaLeftDriveMotorBackDeviceID)},
					new int[]{Constants.kDericaLeftDriveMotor1PDP, Constants.kDericaLeftDriveMotor2PDP});
			kRightDriveMotor = new CheesySpeedController(
					new SpeedController[]{new CANTalon(Constants.kDericaRightDriveMotorFrontDeviceID), 
							new CANTalon(Constants.kDericaRightDriveMotorBackDeviceID)}, 
					new int[]{Constants.kDericaRightDriveMotor2PDP, Constants.kDericaRightDriveMotor2PDP});
			kLeftDriveEncoder = new Encoder(
					Constants.kDericaLeftDriveEncoderDIOA, Constants.kDericaLeftDriveEncoderDIOB);
			kRightDriveEncoder = new Encoder(
					Constants.kDericaRightDriveEncoderDIOA, Constants.kDericaRightDriveEncoderDIOB);
		}
		kDrive = new Drive("drive", kLeftDriveMotor, kRightDriveMotor, kLeftDriveEncoder, kRightDriveEncoder);
	}

	/*
	 * INTAKE
	 */
	public static Intake intake;

	static {
		if(Robot.name == RobotName.TYR) {
			CheesySpeedController kLeftIntakeMotor = new CheesySpeedController(
					new VictorSP(Constants.kTyrLeftIntakeMotorPWM),
					Constants.kTyrLeftIntakeMotorPDP);
			CheesySpeedController kRightIntakeMotor = new CheesySpeedController(
					new VictorSP(Constants.kTyrIntakeMotorPWM),
					Constants.kTyrRightIntakeMotorPDP);
			intake = new Intake("intake", kLeftIntakeMotor, kRightIntakeMotor);
		} else if (Robot.name == RobotName.DERICA) {
			CheesySpeedController kIntakeMotor = new CheesySpeedController(
					new VictorSP(Constants.kDericaIntakeMotorPWM),
					Constants.kDericaIntakeMotorPDP);
			CheesySpeedController kIntakeArmMotor = new CheesySpeedController(
					new VictorSP(Constants.kDericaArmIntakeMotorPWM),
					Constants.kDericaArmIntakeMotorPDP);
			intake = new Intake("intake", kIntakeMotor, kIntakeArmMotor);
		}
	}

	// Solenoids
	DoubleSolenoid kShooterSolenoid = new DoubleSolenoid(
			Constants.kShooterSolenoidPortExtend, Constants.kShooterSolenoidPortRetract);
	DoubleSolenoid kLatchSolenoid = new DoubleSolenoid(
			Constants.kLatchSolenoidPortExtend, Constants.kLatchSolenoidPortRetract);
	DoubleSolenoid kGrabberSolenoid = new DoubleSolenoid(
			Constants.kGrabberSolenoidPortExtend, Constants.kGrabberSolenoidPortRetract);

	// Sensors
	//    public static GyroThread kGyroThread = null; //new GyroThread();

	// Subsystems
	public static Intake kIntake = new Intake("intake", null, null);
	public static PowerDistributionPanel kPDP = new PowerDistributionPanel();



	// Compressor
	//    public static Relay kCompressorRelay = new Relay(Constants.kCompressorRelayPort);
	//    public static DigitalInput kCompressorSwitch = new DigitalInput(Constants.kPressureSwitchDIO);
	//    public static CheesyCompressor kCompressor = new CheesyCompressor(kCompressorRelay, kCompressorSwitch);

	// Interface
	public static Joystick kLeftStick = new Joystick(0);
	public static Joystick kRightStick = new Joystick(1);
	public static Joystick kOperatorStick = new Joystick(2);

}
