package com.palyrobotics.frc2016.auto.modes;

import java.util.ArrayList;

import com.palyrobotics.frc2016.auto.AutoMode;
import com.palyrobotics.frc2016.auto.AutoModeEndedException;
import com.palyrobotics.frc2016.behavior.actions.Action;
import com.palyrobotics.frc2016.behavior.actions.DriveDistanceAction;
import com.palyrobotics.frc2016.behavior.actions.GetLowAction;
import com.palyrobotics.frc2016.behavior.actions.IntakeAction;
import com.palyrobotics.frc2016.behavior.actions.ParallelAction;
import com.palyrobotics.frc2016.config.RobotState;
import com.palyrobotics.frc2016.robot.Robot;
import com.palyrobotics.frc2016.subsystems.Intake.WantedIntakeState;
import com.palyrobotics.frc2016.config.Constants;

public class LowBarAutoMode extends AutoMode {
	
	public static final double mCompressorWaitTime = 3;

	@Override
	protected void routine() throws AutoModeEndedException {
		//if tyr, wait for compressor
		if(Robot.getRobotState().name == RobotState.RobotName.TYR) {
			waitTime(mCompressorWaitTime);
		}
		
		//the arraylist of actions for crossing low bar
		ArrayList<Action> crossLowBar = new ArrayList<Action>(2);
		
		//add drive forward to crossing low bar
		crossLowBar.add(new DriveDistanceAction(Constants.kLowBarDistance, Constants.kLowBarVelocity));
		
		//if tyr, move shooter down
		if(Robot.getRobotState().name == RobotState.RobotName.TYR) {
			crossLowBar.add(new GetLowAction());
		} 
		
		//if derica, move intake down before crossing
		else {
			crossLowBar.add(new IntakeAction(0.25, WantedIntakeState.LOWERING));
		}
		
		//run the crosslowbar action
		runAction(new ParallelAction(crossLowBar));
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