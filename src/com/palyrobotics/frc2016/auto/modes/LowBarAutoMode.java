package com.palyrobotics.frc2016.auto.modes;

import java.util.ArrayList;

import com.palyrobotics.frc2016.Constants;
import com.palyrobotics.frc2016.Robot;
import com.palyrobotics.frc2016.Robot.RobotName;
import com.palyrobotics.frc2016.auto.AutoMode;
import com.palyrobotics.frc2016.auto.AutoModeEndedException;
import com.palyrobotics.frc2016.auto.actions.Action;
import com.palyrobotics.frc2016.auto.actions.DriveDistanceAction;
import com.palyrobotics.frc2016.auto.actions.GetLowAction;
import com.palyrobotics.frc2016.auto.actions.IntakeAction;
import com.palyrobotics.frc2016.auto.actions.ParallelAction;
import com.palyrobotics.frc2016.subsystems.Intake.WantedIntakeState;

public class LowBarAutoMode extends AutoMode {
	
	public static final double mCompressorWaitTime = 3;

	@Override
	protected void routine() throws AutoModeEndedException {
		//if tyr, wait for compressor
		if(Robot.name == RobotName.TYR) {
			waitTime(mCompressorWaitTime);
		}
		
		//the arraylist of actions for crossing low bar
		ArrayList<Action> crossLowBar = new ArrayList<Action>(2);
		
		//add drive forward to crossing low bar
		crossLowBar.add(new DriveDistanceAction(Constants.kLowBarDistance, Constants.kLowBarVelocity));
		
		//if tyr, move shooter down
		if(Robot.name == RobotName.TYR) {
			crossLowBar.add(new GetLowAction());
		} 
		
		//if derica, move intake down before crossing
		else {
			crossLowBar.add(new IntakeAction(0.5, WantedIntakeState.INTAKING));
		}
		
		//run the crosslowbar action
		runAction(new ParallelAction(crossLowBar));
	}

	@Override
	public void prestart() {
		drive.reset();
		//TODO: reset all other subsystems
	}
	
	@Override
	public String toString() {
		return "LowBar";
	}
}