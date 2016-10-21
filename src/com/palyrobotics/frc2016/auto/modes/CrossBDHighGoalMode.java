package com.palyrobotics.frc2016.auto.modes;

import com.palyrobotics.frc2016.Constants;
import com.palyrobotics.frc2016.Robot;
import com.palyrobotics.frc2016.Robot.RobotName;
import com.palyrobotics.frc2016.auto.AutoMode;
import com.palyrobotics.frc2016.auto.AutoModeEndedException;
import com.palyrobotics.frc2016.auto.actions.AutoAlignAction;
import com.palyrobotics.frc2016.auto.actions.DriveDistanceAction;
import com.palyrobotics.frc2016.auto.actions.ParallelAction;
import com.palyrobotics.frc2016.auto.actions.RaiseShooterAction;
import com.palyrobotics.frc2016.auto.actions.ShootAction;
import com.palyrobotics.frc2016.subsystems.Drive.DriveGear;

/**
 * Crosses a B/D class defense
 * Attempts a high goal shot when contstructor parameter is true
 *
 */
public class CrossBDHighGoalMode extends AutoMode {
	private boolean mAttemptShot = false;
	public static final double mCompressorWaitTime = 3;

	/**
	 * Cross a B/D class defense
	 * @param attemptShot true to auto-align and shoot, false to only cross
	 */
	public CrossBDHighGoalMode(boolean attemptShot) {
		mAttemptShot = attemptShot;
	}
	
	@Override
	protected void routine() throws AutoModeEndedException {
		if(Robot.name == RobotName.TYR) {
			waitTime(mCompressorWaitTime);
			drive.setGear(DriveGear.HIGH);
		}
		
		runAction(new DriveDistanceAction(-Constants.kBreachDistance));
		
		if(mAttemptShot && Robot.name==RobotName.TYR) {
			runAction(new AutoAlignAction());
			runAction(new RaiseShooterAction());
			runAction(new ShootAction());
		}
	}

	@Override
	public String toString() {
		return "Cross_BD" + ((mAttemptShot) ? "_HighGoal" : "");
	}

	@Override
	public void prestart() {
		System.out.println("Starting auto mode: " + toString());
	}
}
