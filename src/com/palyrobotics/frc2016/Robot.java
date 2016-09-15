package com.palyrobotics.frc2016;

import com.palyrobotics.frc2016.auto.AutoMode;
import com.palyrobotics.frc2016.auto.AutoModeExecuter;
import com.palyrobotics.frc2016.auto.AutoModeSelector;
import com.palyrobotics.frc2016.behavior.BehaviorManager;
import com.palyrobotics.frc2016.behavior.RobotSetpoints;
import com.palyrobotics.frc2016.subsystems.Drive;
import com.palyrobotics.lib.util.DriveSignal;
import com.palyrobotics.lib.util.Looper;
import com.palyrobotics.lib.util.MultiLooper;
import com.palyrobotics.lib.util.SystemManager;
import com.palyrobotics.lib.util.XboxController;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	public enum RobotName {
		TYR, DERICA
	}

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

	//    MultiLooper looper = new MultiLooper("Controllers", 1 / 200.0, true);
	//    MultiLooper slowLooper = new MultiLooper("SlowControllers", 1 / 100.0);

	public static RobotName name = RobotName.DERICA;

	AutoModeExecuter autoModeRunner = new AutoModeExecuter();

	Drive drive = HardwareAdaptor.kDrive;
	PowerDistributionPanel pdp = HardwareAdaptor.kPDP;

	BehaviorManager behavior_manager = new BehaviorManager();
	OperatorInterface operator_interface = new OperatorInterface();

	CheesyDriveHelper cdh = new CheesyDriveHelper(drive);

	Joystick leftStick = HardwareAdaptor.kLeftStick;
	Joystick rightStick = HardwareAdaptor.kRightStick;
	XboxController operatorStick = HardwareAdaptor.kOperatorStick;

	NetworkTable sensorTable;

	static {
		SystemManager.getInstance().add(new RobotData());
	}

	@Override
	public void robotInit() {
		System.out.println("Start robotInit()");
		//        HardwareAdaptor.kGyroThread.start();
		//        slowLooper.addLoopable(drive);
		//        SystemManager.getInstance().add(behavior_manager);
		sensorTable = NetworkTable.getTable("Sensor");
	}

	@Override
	public void autonomousInit() {
		setState(RobotState.AUTONOMOUS);

		//        HardwareAdaptor.kGyroThread.rezero();
		//        HardwareAdaptor.kGyroThread.reset();

		HardwareAdaptor.kLeftDriveEncoder.reset();
		HardwareAdaptor.kRightDriveEncoder.reset();
		AutoMode mode = AutoModeSelector.getInstance().getAutoMode(2);
		autoModeRunner.setAutoMode(mode);
		// Prestart auto mode
		mode.prestart();
		//
		//        // Start control loops
		autoModeRunner.start();
		//        looper.start();
		//        slowLooper.start();
	}

	@Override
	public void autonomousPeriodic() {
	}

	@Override
	public void teleopInit() {
		setState(RobotState.TELEOP);

		System.out.println("Start teleopInit()");

		//        looper.start();
	}

	@Override
	public void teleopPeriodic() {
		// Test XboxController output
		
		if(RobotSetpoints.TimerDriveAction.NONE == behavior_manager.getSetpoints().timer_drive_action) {
			cdh.cheesyDrive(-leftStick.getY(), rightStick.getX(), rightStick.getRawButton(1), true);
		}

		//the behavior manager updates based on the commands from the operator interface.
		//in the first part, various routines are called based on the requests from the operator interface(buttons)
		//these routines should change the states of the RobotSetpoints only - NO MOVEMENT CODE
		//the second part of the update method in behavior manager runs the physical subsystems based off the RobotSetpoints.
		behavior_manager.update(operator_interface.getCommands());

		// Update sensorTable with encoder distances
		sensorTable.putString("left", String.valueOf(HardwareAdaptor.kLeftDriveEncoder.getDistance()));
		sensorTable.putString("right", String.valueOf(HardwareAdaptor.kRightDriveEncoder.getDistance()));
		//    	System.out.println("Encoders "+ String.valueOf(HardwareAdaptor.kLeftDriveEncoder.getDistance()+" "+String.valueOf(HardwareAdaptor.kRightDriveEncoder.getDistance())));
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
		//        looper.stop();
		//        slowLooper.stop();
		//
		//        // Stop controllers
		//        drive.setOpenLoop(DriveSignal.NEUTRAL);
		//
		//        // Reload constants
		//        drive.reloadConstants();
		//
		System.gc();

		System.out.println("end disable init!");
	}

	@Override
	public void disabledPeriodic() {
	}

}