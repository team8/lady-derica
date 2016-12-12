package com.palyrobotics.frc2016.robot;

import com.palyrobotics.frc2016.auto.AutoMode;
import com.palyrobotics.frc2016.auto.AutoModeExecuter;
import com.palyrobotics.frc2016.auto.AutoModeSelector;
import com.palyrobotics.frc2016.behavior.RoutineManager;
import com.palyrobotics.frc2016.config.Commands;
import com.palyrobotics.frc2016.config.RobotState;
import com.palyrobotics.frc2016.subsystems.*;
import com.palyrobotics.frc2016.util.Dashboard;
import com.palyrobotics.frc2016.util.SubsystemLooper;
import com.palyrobotics.frc2016.robot.team254.lib.util.DriveSignal;
import com.palyrobotics.frc2016.robot.team254.lib.util.RobotData;
import com.palyrobotics.frc2016.robot.team254.lib.util.SystemManager;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Robot extends IterativeRobot {
	// Instantiate singleton classes
	private static RobotState mRobotState = new RobotState();

	static RobotState getRobotState() {
		return mRobotState;
	}

	private static OperatorInterface mOperatorInterface = OperatorInterface.getInstance();
	private static HardwareAdapter mHardwareAdapter = HardwareAdapter.getInstance();
	// Instantiate separate thread controls
	private SubsystemLooper subsystem_looper = new SubsystemLooper();
	private AutoModeExecuter autoModeRunner = new AutoModeExecuter();
	private RoutineManager routineManager = new RoutineManager();

	// Subsystem controllers
	private Drive mDrive = new Drive(mRobotState);
	private Intake mIntake = new Intake(mRobotState.name);
	private TyrShooter mShooter = new TyrShooter();
	private Breacher mBreacher = new Breacher();
	private LowGoalShooter mLowGoalShooter = new LowGoalShooter();
	private Catapult mCatapult = new Catapult();
	
	//hardware updater
	private HardwareUpdater mHardwareUpdater;

	private Dashboard mDashboard = Dashboard.getInstance();
	NetworkTable sensorTable;

	static {
		SystemManager.getInstance().add(new RobotData());
	}

	@Override
	public void robotInit() {
		System.out.println("Start robotInit()");
		subsystem_looper.register(mDrive);
		if(mRobotState.name == RobotState.RobotName.TYR) {
			subsystem_looper.register(mShooter);
			subsystem_looper.register(mBreacher);
			
			mHardwareUpdater = new HardwareUpdater(mDrive, mShooter, mIntake, mBreacher);
		} else {
			subsystem_looper.register(mIntake);
			subsystem_looper.register(mLowGoalShooter);
			
			mHardwareUpdater = new HardwareUpdater(mDrive, mCatapult, mIntake, mLowGoalShooter);
		}
		//        SystemManager.getInstance().add(routineManager);
		sensorTable = NetworkTable.getTable("Sensor");
		mDashboard.init();
		System.out.println("End robotInit()");
	}

	@Override
	public void autonomousInit() {
		System.out.println("Start autonomousInit()");
		mRobotState.gamePeriod = RobotState.GamePeriod.AUTO;

		mDrive.resetController();
		
		AutoMode mode = AutoModeSelector.getInstance().getAutoMode();
		autoModeRunner.setAutoMode(mode);
		// Prestart auto mode
		mode.prestart();
		autoModeRunner.start();
		// Start control loops
		subsystem_looper.start();
		System.out.println("End autonomousInit()");
	}

	@Override
	public void autonomousPeriodic() {
		mDashboard.update();
		mHardwareUpdater.updateSubsystems();
	}

	@Override
	public void teleopInit() {
		System.out.println("Start teleopInit()");
		mRobotState.gamePeriod = RobotState.GamePeriod.TELEOP;
		subsystem_looper.start();
		if(mRobotState.name == RobotState.RobotName.TYR) {
			mShooter.reset();
		}
		System.out.println("End teleopInit()");
	}

	@Override
	public void teleopPeriodic() {
		// Update RobotState

		// Gets joystick commands
		Commands commands = mOperatorInterface.getCommands();
		
		// Updates commands based on routines
		routineManager.update(commands);

		//Update the hardware
		mHardwareUpdater.updateSubsystems();
		
		// Update sensorTable with encoder distances
		sensorTable.putString("left", String.valueOf(mRobotState.left_encoder));
		sensorTable.putString("right", String.valueOf(mRobotState.right_encoder));
		mDashboard.update();
	}

	@Override
	public void disabledInit() {
		System.out.println("Start disabledInit()");
		System.out.println("Current Auto Mode: "+AutoModeSelector.getInstance().getAutoMode().toString());
		mRobotState.gamePeriod = RobotState.GamePeriod.DISABLED;
		// Stop auto mode
		autoModeRunner.stop();

		// Stop routine_request
		routineManager.reset();

		// Stop control loops
		subsystem_looper.stop();

		// Stop controllers
		mDrive.setOpenLoop(DriveSignal.NEUTRAL);

		// Manually run garbage collector
		System.gc();

		System.out.println("End disabledInit()");
	}

	@Override
	public void disabledPeriodic() {
		if(Dashboard.getInstance().getSelectedAutoMode() != "-1") {
			AutoModeSelector.getInstance().setFromDashboard();
		}
	}
}