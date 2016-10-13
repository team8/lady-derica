package com.palyrobotics.frc2016;

import com.palyrobotics.frc2016.auto.AutoMode;
import com.palyrobotics.frc2016.auto.AutoModeExecuter;
import com.palyrobotics.frc2016.auto.AutoModeSelector;
import com.palyrobotics.frc2016.behavior.BehaviorManager;
import com.palyrobotics.frc2016.behavior.RobotSetpoints;
import com.palyrobotics.frc2016.subsystems.Drive;
import com.palyrobotics.frc2016.subsystems.Intake;
import com.palyrobotics.frc2016.subsystems.TyrShooter;
import com.palyrobotics.frc2016.util.CheesyDriveHelper;
import com.palyrobotics.frc2016.util.Dashboard;
import com.palyrobotics.frc2016.util.ProportionalDriveHelper;
import com.palyrobotics.frc2016.util.XboxController;
import com.team254.lib.util.DriveSignal;
import com.team254.lib.util.Looper;
import com.team254.lib.util.RobotData;
import com.team254.lib.util.SystemManager;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	public enum RobotName {
		TYR, DERICA
	}
	public static RobotName name = RobotName.TYR;

	public enum RobotState {
		DISABLED, AUTONOMOUS, TELEOP
	}
	public static RobotState s_robot_state = RobotState.DISABLED;
	public static RobotState getState() {
		return s_robot_state;
	}
	public static void setState(RobotState state) {
		s_robot_state = state;
	}

	Looper subsystem_looper = new Looper();

	AutoModeExecuter autoModeRunner = new AutoModeExecuter();
	// Subsystems
	Drive drive = HardwareAdaptor.kDrive;
	TyrShooter shooter = HardwareAdaptor.kTyrShooter;
	Intake intake = HardwareAdaptor.kIntake;
	PowerDistributionPanel pdp = HardwareAdaptor.kPDP;

	BehaviorManager behavior_manager = new BehaviorManager();
	OperatorInterface operator_interface = new OperatorInterface();

	CheesyDriveHelper cdh = new CheesyDriveHelper(drive);
	ProportionalDriveHelper pdh = new ProportionalDriveHelper(drive);

	Joystick leftStick = HardwareAdaptor.kLeftStick;
	Joystick rightStick = HardwareAdaptor.kRightStick;
	XboxController operatorStick = HardwareAdaptor.kOperatorStick;

	Dashboard mDashboard = Dashboard.getInstance();
	NetworkTable sensorTable;

	static {
		SystemManager.getInstance().add(new RobotData());
	}

	@Override
	public void robotInit() {
		System.out.println("Start robotInit()");
		subsystem_looper.register(drive);
		if(Robot.name == RobotName.TYR) {
			subsystem_looper.register(shooter);
		} else {
			subsystem_looper.register(intake);
		}
		//        SystemManager.getInstance().add(behavior_manager);
		sensorTable = NetworkTable.getTable("Sensor");
		mDashboard.init();
	}

	@Override
	public void autonomousInit() {
		setState(RobotState.AUTONOMOUS);
		drive.reset();
		AutoMode mode = AutoModeSelector.getInstance().getAutoMode();
		autoModeRunner.setAutoMode(mode);
		// Prestart auto mode
		mode.prestart();
		autoModeRunner.start();
		// Start control loops
		subsystem_looper.start();
	}

	@Override
	public void autonomousPeriodic() {
		mDashboard.update();
	}

	@Override
	public void teleopInit() {
		setState(RobotState.TELEOP);
		System.out.println("Start teleopInit()");
		subsystem_looper.start();
		if(Robot.name == RobotName.TYR) {
			shooter.reset();
		}
	}

	@Override
	public void teleopPeriodic() {
		// Passes joystick control to subsystems for their processing
		if(Robot.name == RobotName.TYR) {
			shooter.update(operatorStick.getRightY());
		} else if(Robot.name == RobotName.DERICA) {
			intake.update(operatorStick.getRightY());
		}
		// Pick one or the other drive scheme
//		pdh.pDrive(-leftStick.getY(), rightStick.getX(), behavior_manager.getSetpoints());
		cdh.cheesyDrive(-leftStick.getY(), rightStick.getX(), rightStick.getRawButton(1), drive.isHighGear(), behavior_manager.getSetpoints());
		
		// Runs routines
		behavior_manager.update(operator_interface.getCommands());

		// Update sensorTable with encoder distances
		sensorTable.putString("left", String.valueOf(HardwareAdaptor.kLeftDriveEncoder.getDistance()));
		sensorTable.putString("right", String.valueOf(HardwareAdaptor.kRightDriveEncoder.getDistance()));
		mDashboard.update();
	}

	@Override
	public void disabledInit() {
		setState(RobotState.DISABLED);

		System.out.println("Start disabledInit()");

		// Stop auto mode
		autoModeRunner.stop();

		// Stop routines
		behavior_manager.reset();

		// Stop control loops
		subsystem_looper.stop();

		// Stop controllers
		drive.setOpenLoop(DriveSignal.NEUTRAL);
		//
		// Reload constants
		drive.reloadConstants();
		//
		System.gc();

		System.out.println("end disable init!");
	}

	@Override
	public void disabledPeriodic() {
//		System.out.println("Gyro: " + drive.getPhysicalPose().getHeading());
//		System.out.println("Left: " + drive.getPhysicalPose().getLeftDistance());
//		System.out.println("Right: " + drive.getPhysicalPose().getRightDistance());
	}
}