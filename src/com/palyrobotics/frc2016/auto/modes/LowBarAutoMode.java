package com.palyrobotics.frc2016.auto.modes;

import java.util.ArrayList;

import com.palyrobotics.frc2016.auto.AutoMode;
import com.palyrobotics.frc2016.auto.AutoModeEndedException;
import com.palyrobotics.frc2016.auto.actions.Action;
import com.palyrobotics.frc2016.behavior.ParallelRoutine;
import com.palyrobotics.frc2016.behavior.Routine;
import com.palyrobotics.frc2016.behavior.routines.auto.DriveDistanceAction;
import com.palyrobotics.frc2016.behavior.routines.auto.GetLowAction;
import com.palyrobotics.frc2016.config.RobotState;
import com.palyrobotics.frc2016.robot.Robot;
import com.palyrobotics.frc2016.config.Constants;

public class LowBarAutoMode extends AutoMode {
	
	public static final double mCompressorWaitTime = 3;

	@Override
	protected void routine() throws AutoModeEndedException {
		//if tyr, wait for compressor
		if(Constants.kRobotName == Constants.RobotName.TYR) {
			waitTime(mCompressorWaitTime);
		}
		
		//the arraylist of actions for crossing low bar
		ArrayList<Routine> crossLowBar = new ArrayList<Routine>(2);
		
		//add drive forward to crossing low bar
		crossLowBar.add(new DriveDistanceAction(Constants.kLowBarDistance, Constants.kLowBarVelocity));
		
		//if tyr, move shooter down
		if(Constants.kRobotName == Constants.RobotName.TYR) {
			crossLowBar.add(new GetLowAction());
		} 
		
		//run the crosslowbar action
		runRoutine(new ParallelRoutine(crossLowBar));
	}

	@Override
	public void prestart() {
		drive.resetController();
		//TODO: resetController all other subsystems
	}
	
	@Override
	public String toString() {
		return "LowBar";
	}
}