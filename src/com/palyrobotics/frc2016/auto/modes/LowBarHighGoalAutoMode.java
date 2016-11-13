package com.palyrobotics.frc2016.auto.modes;

import java.util.ArrayList;

import com.palyrobotics.frc2016.auto.AutoMode;
import com.palyrobotics.frc2016.auto.AutoModeEndedException;
import com.palyrobotics.frc2016.auto.actions.Action;
import com.palyrobotics.frc2016.auto.actions.AutoAlignAction;
import com.palyrobotics.frc2016.auto.actions.DriveDistanceAction;
import com.palyrobotics.frc2016.auto.actions.GetLowAction;
import com.palyrobotics.frc2016.auto.actions.IntakeAction;
import com.palyrobotics.frc2016.auto.actions.ParallelAction;
import com.palyrobotics.frc2016.auto.actions.RaiseShooterAction;
import com.palyrobotics.frc2016.auto.actions.ShootAction;
import com.palyrobotics.frc2016.input.RobotState;
import com.palyrobotics.frc2016.robot.Robot;
import com.palyrobotics.frc2016.subsystems.Intake.WantedIntakeState;
import com.palyrobotics.frc2016.util.Constants;

/**
 * Goes under the low bar then shoots a high goal
 */
public class LowBarHighGoalAutoMode extends AutoMode {
	public static final double mCompressorWaitTime = 3;
	
	@Override
	protected void routine() throws AutoModeEndedException {
		
		/* Low bar */
		waitTime(mCompressorWaitTime); //Waits for compressor
		ArrayList<Action> crossLowBar = new ArrayList<Action>(2);
		crossLowBar.add(new DriveDistanceAction(Constants.kLowBarDistance));
		ArrayList<Action> prepareGoal = new ArrayList<Action>(2);
		if(Robot.getRobotState().name == RobotState.RobotName.TYR) {
			crossLowBar.add(new GetLowAction());
			prepareGoal.add(new RaiseShooterAction());
			prepareGoal.add(new IntakeAction(1.0, WantedIntakeState.LOWERING));
		}
		runAction(new ParallelAction(crossLowBar));
		/* Auto Align then high goal */
		
		runAction(new AutoAlignAction());
		runAction(new ParallelAction(prepareGoal));
		runAction(new ShootAction());
		waitTime(0.5);
		/* Bring shooter down if extra time */
		runAction(new GetLowAction());
	}

	@Override
	public void prestart() {
		System.out.println("Starting low bar high goal auto!");
	}
	
	@Override
	public String toString() {
		return "LowBar_HighGoal";
	}
}
