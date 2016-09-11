package com.palyrobotics.frc2016;

import com.palyrobotics.frc2016.Robot.RobotName;
import com.palyrobotics.frc2016.subsystems.*;
import com.palyrobotics.lib.util.CheesyCompressor;
import com.palyrobotics.lib.util.CheesySolenoid;
import com.palyrobotics.lib.util.CheesySpeedController;

//import com.team254.lib.util.gyro.GyroThread;
import edu.wpi.first.wpilibj.*;

public class HardwareAdaptor {
    // Motors
	// Drivetrain motors
    static CheesySpeedController kLeftDriveMotor;
    static CheesySpeedController kRightDriveMotor;
    
//   // Intake motors
//    static CheesySpeedController kLeftIntakeMotor = new CheesySpeedController(
//            new VictorSP(Constants.kLeftIntakeMotorPWM),
//            Constants.kLeftIntakeMotorPDP);
//    static CheesySpeedController kRightIntakeMotor = new CheesySpeedController(
//            new VictorSP(Constants.kRightIntakeMotorPWM),
//            Constants.kRightIntakeMotorPDP);

    // DIO
    static Encoder kLeftDriveEncoder;
    static Encoder kRightDriveEncoder;
    
    // Solenoids
    static DoubleSolenoid kShooterSolenoid = new DoubleSolenoid(
    		Constants.kShooterSolenoidPortExtend, Constants.kShooterSolenoidPortRetract);
    static DoubleSolenoid kLatchSolenoid = new DoubleSolenoid(
    		Constants.kLatchSolenoidPortExtend, Constants.kLatchSolenoidPortRetract);
    static DoubleSolenoid kGrabberSolenoid = new DoubleSolenoid(
    		Constants.kGrabberSolenoidPortExtend, Constants.kGrabberSolenoidPortRetract);


    // Sensors
//    public static GyroThread kGyroThread = null; //new GyroThread();

    // Subsystems
    public static Drive kDrive;
    public static Intake kIntake;
    public static PowerDistributionPanel kPDP = new PowerDistributionPanel();
    
    static {
    	if(Robot.name == RobotName.TYR) {
    		kLeftDriveMotor = new CheesySpeedController(
    	            new SpeedController[]{new CANTalon(Constants.kLeftDriveMotorFrontDeviceID), 
    	            		new CANTalon(Constants.kLeftDriveMotorBackDeviceID)},
    	            new int[]{Constants.kLeftDriveMotor1PDP, Constants.kLeftDriveMotor2PDP});
    		kRightDriveMotor = new CheesySpeedController(
    	            new SpeedController[]{new CANTalon(Constants.kRightDriveMotorFrontDeviceID), 
    	            		new CANTalon(Constants.kRightDriveMotorBackDeviceID)}, 
    	            new int[]{Constants.kRightDriveMotor2PDP, Constants.kRightDriveMotor2PDP});
    		kLeftDriveEncoder = new Encoder(
    	            Constants.kLeftDriveEncoderDIOA, Constants.kLeftDriveEncoderDIOB);
    		kRightDriveEncoder = new Encoder(
    	            Constants.kRightDriveEncoderDIOA, Constants.kRightDriveEncoderDIOB);
    		kDrive = new Drive("drive", kLeftDriveMotor, kRightDriveMotor, kLeftDriveEncoder, kRightDriveEncoder);
    		
    		kIntake = new Intake("intake", null, null);
    	}
    	
    	if(Robot.name == RobotName.DERICA) {
    		kLeftDriveMotor = new CheesySpeedController(
    	            new SpeedController[]{new CANTalon(Constants.kLeftDriveMotorFrontDeviceID), 
    	            		new CANTalon(Constants.kLeftDriveMotorBackDeviceID)},
    	            new int[]{Constants.kLeftDriveMotor1PDP, Constants.kLeftDriveMotor2PDP});
    		kRightDriveMotor = new CheesySpeedController(
    	            new SpeedController[]{new CANTalon(Constants.kRightDriveMotorFrontDeviceID), 
    	            		new CANTalon(Constants.kRightDriveMotorBackDeviceID)}, 
    	            new int[]{Constants.kRightDriveMotor2PDP, Constants.kRightDriveMotor2PDP});
    		kLeftDriveEncoder = new Encoder(
    	            Constants.kLeftDriveEncoderDIOA, Constants.kLeftDriveEncoderDIOB);
    		kRightDriveEncoder = new Encoder(
    	            Constants.kRightDriveEncoderDIOA, Constants.kRightDriveEncoderDIOB);
    		kDrive = new Drive("drive", kLeftDriveMotor, kRightDriveMotor, kLeftDriveEncoder, kRightDriveEncoder);
    		
    		kIntake = new Intake("intake", null, null);
    	}
    }
    
    // Compressor
//    public static Relay kCompressorRelay = new Relay(Constants.kCompressorRelayPort);
//    public static DigitalInput kCompressorSwitch = new DigitalInput(Constants.kPressureSwitchDIO);
//    public static CheesyCompressor kCompressor = new CheesyCompressor(kCompressorRelay, kCompressorSwitch);

    // Interface
    public static Joystick kLeftStick = new Joystick(0);
    public static Joystick kRightStick = new Joystick(1);
    public static Joystick kOperatorStick = new Joystick(2);

}
