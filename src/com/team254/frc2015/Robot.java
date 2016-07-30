package com.team254.frc2015;

import com.team254.frc2015.auto.AutoMode;
import com.team254.frc2015.auto.AutoModeExecuter;
import com.team254.frc2015.auto.AutoModeSelector;
import com.team254.frc2015.behavior.BehaviorManager;
import com.team254.frc2015.subsystems.Drive;
import com.team254.lib.util.DriveSignal;
import com.team254.lib.util.Looper;
import com.team254.lib.util.MultiLooper;
import com.team254.lib.util.SystemManager;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class Robot extends IterativeRobot {
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

    MultiLooper looper = new MultiLooper("Controllers", 1 / 200.0, true);
    MultiLooper slowLooper = new MultiLooper("SlowControllers", 1 / 100.0);
    Looper logUpdater = new Looper("Updater", new Updater(), 1 / 50.0);

    AutoModeExecuter autoModeRunner = new AutoModeExecuter();

    Drive drive = HardwareAdaptor.kDrive;
    PowerDistributionPanel pdp = HardwareAdaptor.kPDP;

    BehaviorManager behavior_manager = new BehaviorManager();
    OperatorInterface operator_interface = new OperatorInterface();

    CheesyDriveHelper cdh = new CheesyDriveHelper(drive);

    Joystick leftStick = HardwareAdaptor.kLeftStick;
    Joystick rightStick = HardwareAdaptor.kRightStick;
    Joystick operatorStick = HardwareAdaptor.kOperatorStick;

    static {
        SystemManager.getInstance().add(new RobotData());
    }

    @Override
    public void robotInit() {
        System.out.println("Start robotInit()");
        HardwareAdaptor.kGyroThread.start();
        slowLooper.addLoopable(drive);
        logUpdater.start();
        SystemManager.getInstance().add(behavior_manager);
    }

    @Override
    public void autonomousInit() {
        setState(RobotState.AUTONOMOUS);

        HardwareAdaptor.kGyroThread.rezero();
        HardwareAdaptor.kGyroThread.reset();

        HardwareAdaptor.kLeftDriveEncoder.reset();
        HardwareAdaptor.kRightDriveEncoder.reset();
        AutoMode mode = AutoModeSelector.getInstance().getAutoMode();
        autoModeRunner.setAutoMode(mode);
        // Prestart auto mode
        mode.prestart();

        // Start control loops
        autoModeRunner.start();
        looper.start();
        slowLooper.start();
    }

    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopInit() {
        setState(RobotState.TELEOP);

        System.out.println("Start teleopInit()");

        looper.start();
    }

    @Override
    public void teleopPeriodic() {
        cdh.cheesyDrive(-leftStick.getY(), rightStick.getX(), rightStick.getRawButton(1), true);
        behavior_manager.update(operator_interface.getCommands());
    }

    @Override
    public void disabledInit() {
        setState(RobotState.DISABLED);

        System.out.println("Start disabledInit()");

        // Stop auto mode
        autoModeRunner.stop();

        // Stop routines
        //behavior_manager.reset();

        // Stop control loops
        looper.stop();
        slowLooper.stop();

        // Stop controllers
        drive.setOpenLoop(DriveSignal.NEUTRAL);

        // Reload constants
        drive.reloadConstants();

        System.gc();

        System.out.println("end disable init!");
    }

    @Override
    public void disabledPeriodic() {

    }

}
