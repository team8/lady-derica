package com.palyrobotics.frc2016.auto.modes;

import java.util.ArrayList;

import com.palyrobotics.frc2016.auto.AutoMode;
import com.palyrobotics.frc2016.auto.AutoModeEndedException;
import com.palyrobotics.frc2016.auto.actions.Action;
import com.palyrobotics.frc2016.behavior.Routine;
import com.palyrobotics.frc2016.behavior.routines.AutoAlignmentRoutine;
import com.palyrobotics.frc2016.behavior.routines.auto.DriveTimeAction;
import com.palyrobotics.frc2016.behavior.routines.auto.ExpelIntake;
import com.palyrobotics.frc2016.behavior.routines.auto.ExpelShooterAction;
import com.palyrobotics.frc2016.behavior.routines.auto.RaiseShooterAction;
import com.palyrobotics.frc2016.behavior.routines.auto.ShootAction;
import com.palyrobotics.frc2016.config.RobotState;
import com.palyrobotics.frc2016.robot.Robot;
import com.palyrobotics.frc2016.subsystems.Drive.DriveGear;
import com.palyrobotics.frc2016.config.Constants;

public class TimerBDAutoMode extends AutoMode {
	
	private boolean mAttemptShot = false;
	public static final double mCompressorWaitTime = 3;
	// Default, for Derica. Tyr will set to -1,-1 instead
	private double leftSpeed;
	private double rightSpeed;
	// Return with another breach
	private boolean mUTurn = false;
	
	/**
	 * Cross a B/D class defense
	 * @param attemptShot true to auto-align and shoot, false to only cross
	 */
	public TimerBDAutoMode(boolean attemptShot) {
		mAttemptShot = attemptShot;
		if(Constants.kRobotName == Constants.RobotName.TYR) {
			leftSpeed = -1.0;
			rightSpeed = -1.0;
		} else {
			leftSpeed = -1;
			rightSpeed = -1;
		}
	}

	@Override
	protected void routine() throws AutoModeEndedException {
		if(Constants.kRobotName == Constants.RobotName.TYR) {
			waitTime(mCompressorWaitTime);
			drive.setGear(DriveGear.HIGH);
		}
		
		runRoutine(new DriveTimeAction(2.25, leftSpeed, rightSpeed));
		if(mUTurn && Constants.kRobotName == Constants.RobotName.DERICA) {
			ArrayList<Routine> expel = new ArrayList<Routine>(2);
			expel.add(new ExpelShooterAction(Constants.kAutoShooterExpelTime));
			expel.add((new ExpelIntake(Constants.kAutoShooterExpelTime)));
			waitTime(0.2);
			runRoutine(new DriveTimeAction(2.25, -leftSpeed, -rightSpeed));
		}
		if(mAttemptShot && Constants.kRobotName == Constants.RobotName.TYR) {
			runRoutine(new AutoAlignmentRoutine());
			runRoutine(new RaiseShooterAction());
			runRoutine(new ShootAction());
		}
	}

	@Override
	public String toString() {
		return "TimerBDAutoMode" + ((mAttemptShot) ? "_HighGoal" : "");
	}

	@Override
	public void prestart() {
		System.out.println("Starting auto mode: " + toString());
		drive.resetController();
	}

}
