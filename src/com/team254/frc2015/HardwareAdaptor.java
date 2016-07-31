package com.team254.frc2015;

import com.team254.frc2015.subsystems.*;
import com.team254.lib.util.CheesyCompressor;
import com.team254.lib.util.CheesySolenoid;
import com.team254.lib.util.CheesySpeedController;
import com.team254.lib.util.gyro.GyroThread;
import edu.wpi.first.wpilibj.*;

public class HardwareAdaptor {
    // Motors
	// Drivetrain motors
//    static CheesySpeedController kLeftDriveFrontMotor = new CheesySpeedController(
//            new CANTalon(Constants.kLeftDriveMotorFrontDeviceID), new int[]{
//            Constants.kLeftDriveMotor1PDP,
//            Constants.kLeftDriveMotor2PDP});
//    
//    static CheesySpeedController kRightDriveFrontMotor = new CheesySpeedController(
//            new CANTalon(Constants.kRightDriveMotorFrontDeviceID), new int[]{
//            Constants.kRightDriveMotor2PDP,
//            Constants.kRightDriveMotor2PDP});
//   // Intake motors
//    static CheesySpeedController kLeftIntakeMotor = new CheesySpeedController(
//            new VictorSP(Constants.kLeftIntakeMotorPWM),
//            Constants.kLeftIntakeMotorPDP);
//    static CheesySpeedController kRightIntakeMotor = new CheesySpeedController(
//            new VictorSP(Constants.kRightIntakeMotorPWM),
//            Constants.kRightIntakeMotorPDP);

    // DIO
    static Encoder kLeftDriveEncoder = new Encoder(
            Constants.kLeftDriveEncoderDIOA, Constants.kLeftDriveEncoderDIOB);
    static Encoder kRightDriveEncoder = new Encoder(
            Constants.kRightDriveEncoderDIOA, Constants.kRightDriveEncoderDIOB);

    // Solenoids
    static DoubleSolenoid kShooterSolenoid = new DoubleSolenoid(
    		Constants.kShooterSolenoidPortExtend, Constants.kShooterSolenoidPortRetract);
    static DoubleSolenoid kLatchSolenoid = new DoubleSolenoid(
    		Constants.kLatchSolenoidPortExtend, Constants.kLatchSolenoidPortRetract);
    static DoubleSolenoid kGrabberSolenoid = new DoubleSolenoid(
    		Constants.kGrabberSolenoidPortExtend, Constants.kGrabberSolenoidPortRetract);


    // Sensors
    public static GyroThread kGyroThread = new GyroThread();

    // Subsystems
    public static Drive kDrive = new Drive("drive", null, null, kLeftDriveEncoder, kRightDriveEncoder,
            kGyroThread);
    public static Intake kIntake = new Intake("intake",
            null, null);
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
