package com.palyrobotics.frc2016.auto.modes;

import java.util.ArrayList;

import com.palyrobotics.frc2016.auto.AutoMode;
import com.palyrobotics.frc2016.auto.AutoModeEndedException;
import com.palyrobotics.frc2016.auto.actions.Action;
import com.palyrobotics.frc2016.behavior.ParallelRoutine;
import com.palyrobotics.frc2016.behavior.Routine;
import com.palyrobotics.frc2016.behavior.routines.AutoAlignmentRoutine;
import com.palyrobotics.frc2016.behavior.routines.auto.DriveDistanceAction;
import com.palyrobotics.frc2016.behavior.routines.auto.GetLowAction;
import com.palyrobotics.frc2016.behavior.routines.auto.RaiseShooterAction;
import com.palyrobotics.frc2016.behavior.routines.auto.ShootAction;
import com.palyrobotics.frc2016.config.RobotState;
import com.palyrobotics.frc2016.robot.Robot;
import com.palyrobotics.frc2016.config.Constants;

/**
 * Goes under the low bar then shoots a high goal
 */
public class LowBarHighGoalAutoMode extends AutoMode {
	public static final double mCompressorWaitTime = 3;
	
	@Override
	protected void routine() throws AutoModeEndedException {
		
		/* Low bar */
		waitTime(mCompressorWaitTime); //Waits for compressor
		ArrayList<Routine> crossLowBar = new ArrayList<Routine>(2);
		crossLowBar.add(new DriveDistanceAction(Constants.kLowBarDistance));
		ArrayList<Routine> prepareGoal = new ArrayList<Routine>(2);
		if(Constants.kRobotName == Constants.RobotName.TYR) {
			crossLowBar.add(new GetLowAction());
			prepareGoal.add(new RaiseShooterAction());
		}
		runRoutine(new ParallelRoutine(crossLowBar));
		/* Auto Align then high goal */
		
		runRoutine(new AutoAlignmentRoutine());
		runRoutine(new ParallelRoutine(prepareGoal));
		runRoutine(new ShootAction());
		waitTime(0.5);
		/* Bring shooter down if extra time */
		runRoutine(new GetLowAction());
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
