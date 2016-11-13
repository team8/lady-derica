package com.palyrobotics.frc2016.auto.actions;

import com.palyrobotics.frc2016.robot.HardwareAdaptor;
import com.palyrobotics.frc2016.subsystems.Drive;
import com.palyrobotics.frc2016.subsystems.Intake;
import com.palyrobotics.frc2016.subsystems.LowGoalShooter;
import com.palyrobotics.frc2016.subsystems.TyrShooter;

/**
 * Action Interface, an interface that describes an iterative action. It is run
 * by an autonomous action, called by the method runAction in AutoModeBase (or
 * more commonly in autonomous modes that extend AutoModeBase)
 * @author Team 254
 * @see com.palyrobotics.frc2016.auto.AutoModeBase#runAction
 */
public interface Action {
	public final Drive drive = HardwareAdaptor.kDrive;
	public final TyrShooter tyrShooter = HardwareAdaptor.kTyrShooter;
	public final Intake intake = HardwareAdaptor.kIntake;
	public final LowGoalShooter lowShooter = HardwareAdaptor.kLowGoalShooter;

    /**
     * Returns whether or not the code has finished execution. When implementing
     * this interface, this method is used by the runAction method every cycle
     * to know when to stop running the action
     * 
     * @return boolean
     */
    public abstract boolean isFinished();

    /**
     * Called by runAction in AutoModeBase iteratively until isFinished returns
     * true. Iterative logic lives in this method
     */
    public abstract void update();

    /**
     * Run code once when the action finishes, usually for clean up
     */
    public abstract void done();

    /**
     * Run code once when the action is started, for set up
     */
    public abstract void start();
}
