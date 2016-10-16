package com.palyrobotics.frc2016.auto.modes;

import java.util.ArrayList;

import com.palyrobotics.frc2016.Constants;
import com.palyrobotics.frc2016.Robot;
import com.palyrobotics.frc2016.Robot.RobotName;
import com.palyrobotics.frc2016.auto.AutoMode;
import com.palyrobotics.frc2016.auto.AutoModeEndedException;
import com.palyrobotics.frc2016.auto.actions.Action;
import com.palyrobotics.frc2016.auto.actions.AutoAlignAction;
import com.palyrobotics.frc2016.auto.actions.DriveDistanceAction;
import com.palyrobotics.frc2016.auto.actions.DrivePathAction;
import com.palyrobotics.frc2016.auto.actions.GetLowAction;
import com.palyrobotics.frc2016.auto.actions.IntakeAction;
import com.palyrobotics.frc2016.auto.actions.ParallelAction;
import com.palyrobotics.frc2016.auto.actions.RaiseShooterAction;
import com.palyrobotics.frc2016.auto.actions.ShootAction;
import com.team254.lib.trajectory.Path;
import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.Trajectory.Segment;

/**
 * Goes under the low bar then shoots a high goal
 */
public class LowBarHighGoalAutoMode extends AutoMode {
	public static final double mCompressorWaitTime = 3;

	@Override
	protected void routine() throws AutoModeEndedException {
		
		//segments are constructed with pos, vel, acc, jerk, heading, dt, x, y
		//trajectory is composed of an array of segments
		//a path is constructed with a name and a Trajectory.Pair of trajectories.
		//the trajectoryfollower takes a path and follows it.
		
		Segment[] segments = new Segment[2];
		
		//drive 60 inches straight ahead?
		segments[0] = new Segment(60, 0.7, 0.1, 0.1, 0, 200, 0, 0);
		
		//drive 10 inches with an angle of 30 degrees?
		segments[1] = new Segment(10, 0.5, 0.1, 0.1, 30, 200, 0, 0);
		Trajectory left = new Trajectory(segments);
		Trajectory right = new Trajectory(segments);
		Trajectory.Pair pair = new Trajectory.Pair(left, right);
		Path path = new Path("low bar", pair);
		
		/* Low bar */
		waitTime(mCompressorWaitTime); //Waits for compressor
		ArrayList<Action> crossLowBar = new ArrayList<Action>(2);
		crossLowBar.add(new DriveDistanceAction(Constants.kLowBarDistance));
		ArrayList<Action> prepareGoal = new ArrayList<Action>(2);
		
		if(Robot.name == RobotName.TYR) {
			crossLowBar.add(new GetLowAction());
			prepareGoal.add(new RaiseShooterAction());
			prepareGoal.add(new IntakeAction(1.0));
		} else {
			// TODO: Does Derica have any restrictions or simultaneous actions to run
		}
		runAction(new ParallelAction(crossLowBar));
		/* Auto Align then high goal */
//		prepareGoal.add(new AutoAlignAction());
		
//		runAction(new DrivePathAction(path));
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
