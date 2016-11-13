package com.palyrobotics.frc2016.robot;

import com.palyrobotics.frc2016.auto.AutoMode;
import com.palyrobotics.frc2016.auto.AutoModeExecuter;
import com.palyrobotics.frc2016.auto.AutoModeSelector;
import com.palyrobotics.frc2016.behavior.BehaviorManager;
import com.palyrobotics.frc2016.input.Commands;
import com.palyrobotics.frc2016.input.RobotState;
import com.palyrobotics.frc2016.subsystems.Breacher;
import com.palyrobotics.frc2016.subsystems.Drive;
import com.palyrobotics.frc2016.subsystems.Intake;
import com.palyrobotics.frc2016.subsystems.LowGoalShooter;
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
	private static RobotState robotState;
	public static RobotState getRobotState() {return robotState;}
	Looper subsystem_looper = new Looper();

	AutoModeExecuter autoModeRunner = new AutoModeExecuter();
	// Subsystems
	Drive drive = HardwareAdaptor.kDrive;
	TyrShooter shooter = HardwareAdaptor.kTyrShooter;
	Intake intake = HardwareAdaptor.kIntake;
	Breacher breacher = HardwareAdaptor.kBreacher;
	PowerDistributionPanel pdp = HardwareAdaptor.kPDP;
	LowGoalShooter lowGoal = HardwareAdaptor.kLowGoalShooter;

	BehaviorManager behavior_manager = new BehaviorManager();
	OperatorInterface operator_interface = new OperatorInterface();

	CheesyDriveHelper cdh = new CheesyDriveHelper(drive);
	ProportionalDriveHelper pdh = new ProportionalDriveHelper(drive);

	Joystick leftStick = HardwareAdaptor.kLeftStick;
	Joystick rightStick = HardwareAdaptor.kRightStick;
	Joystick operatorStick = HardwareAdaptor.kOperatorStick;

	Dashboard mDashboard = Dashboard.getInstance();
	NetworkTable sensorTable;

	static {
		SystemManager.getInstance().add(new RobotData());
	}

	@Override
	public void robotInit() {
		System.out.println("Start robotInit()");
		subsystem_looper.register(drive);
		if(RobotState.name == RobotState.RobotName.TYR) {
			subsystem_looper.register(shooter);
			subsystem_looper.register(breacher);
		} else {
			subsystem_looper.register(intake);
			subsystem_looper.register(lowGoal);
		}
		//        SystemManager.getInstance().add(behavior_manager);
		sensorTable = NetworkTable.getTable("Sensor");
		mDashboard.init();
	}

	@Override
	public void autonomousInit() {
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
		System.out.println("Start teleopInit()");
		subsystem_looper.start();
		if(RobotState.name == RobotState.RobotName.TYR) {
			shooter.reset();
		}
	}

	@Override
	public void teleopPeriodic() {
		// Passes joystick control to subsystems for their processing
		Commands commands = operator_interface.getCommands();
		// Pick one or the other drive scheme
//		pdh.pDrive(commands);
		cdh.cheesyDrive(commands, robotState);
		
		// Runs routines
		behavior_manager.update(operator_interface.getCommands());

		// Update sensorTable with encoder distances
		sensorTable.putString("left", String.valueOf(HardwareAdaptor.kLeftDriveEncoder.getDistance()));
		sensorTable.putString("right", String.valueOf(HardwareAdaptor.kRightDriveEncoder.getDistance()));
		mDashboard.update();
	}

	@Override
	public void disabledInit() {
		System.out.println("Start disabledInit()");
		System.out.println("Current Auto Mode: "+AutoModeSelector.getInstance().getAutoMode().toString());
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
		if(Dashboard.getInstance().getSelectedAutoMode() != "-1") {
			AutoModeSelector.getInstance().setFromDashboard();
		}
	}
}