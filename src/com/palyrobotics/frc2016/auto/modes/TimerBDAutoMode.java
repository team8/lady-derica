package com.palyrobotics.frc2016.auto.modes;

import java.util.ArrayList;

import com.palyrobotics.frc2016.auto.AutoMode;
import com.palyrobotics.frc2016.auto.AutoModeEndedException;
import com.palyrobotics.frc2016.auto.actions.Action;
import com.palyrobotics.frc2016.auto.actions.AutoAlignAction;
import com.palyrobotics.frc2016.auto.actions.DriveTimeAction;
import com.palyrobotics.frc2016.auto.actions.ExpelIntake;
import com.palyrobotics.frc2016.auto.actions.ExpelShooterAction;
import com.palyrobotics.frc2016.auto.actions.RaiseShooterAction;
import com.palyrobotics.frc2016.auto.actions.ShootAction;
import com.palyrobotics.frc2016.input.RobotState;
import com.palyrobotics.frc2016.robot.Robot;
import com.palyrobotics.frc2016.subsystems.Drive.DriveGear;
import com.palyrobotics.frc2016.util.Constants;

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
		if(Robot.getRobotState().name == RobotState.RobotName.TYR) {
			leftSpeed = -1.0;
			rightSpeed = -1.0;
		} else {
			leftSpeed = -1;
			rightSpeed = -1;
		}
	}

	@Override
	protected void routine() throws AutoModeEndedException {
		if(Robot.getRobotState().name == RobotState.RobotName.TYR) {
			waitTime(mCompressorWaitTime);
			drive.setGear(DriveGear.HIGH);
		}
		
		runAction(new DriveTimeAction(2.25, leftSpeed, rightSpeed));
		if(mUTurn && Robot.getRobotState().name == RobotState.RobotName.DERICA) {
			ArrayList<Action> expel = new ArrayList<Action>(2);
			expel.add(new ExpelShooterAction(Constants.kAutoShooterExpelTime));
			expel.add((new ExpelIntake(Constants.kAutoShooterExpelTime)));
			waitTime(0.2);
			runAction(new DriveTimeAction(2.25, -leftSpeed, -rightSpeed));
		}
		if(mAttemptShot && Robot.getRobotState().name == RobotState.RobotName.TYR) {
			runAction(new AutoAlignAction());
			runAction(new RaiseShooterAction());
			runAction(new ShootAction());
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
