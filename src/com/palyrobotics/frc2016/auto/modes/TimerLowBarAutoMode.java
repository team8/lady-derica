package com.palyrobotics.frc2016.auto.modes;

import java.util.ArrayList;

import com.palyrobotics.frc2016.auto.AutoMode;
import com.palyrobotics.frc2016.auto.AutoModeEndedException;
import com.palyrobotics.frc2016.auto.actions.Action;
import com.palyrobotics.frc2016.behavior.ParallelRoutine;
import com.palyrobotics.frc2016.behavior.Routine;
import com.palyrobotics.frc2016.behavior.routines.auto.DriveTimeAction;
import com.palyrobotics.frc2016.behavior.routines.auto.GetLowAction;
import com.palyrobotics.frc2016.config.Constants;
import com.palyrobotics.frc2016.config.RobotState;
import com.palyrobotics.frc2016.robot.Robot;

public class TimerLowBarAutoMode extends AutoMode {
	
	public static final double mCompressorWaitTime = 3;
	private double leftSpeed = 0.5;
	private double rightSpeed = 0.5;
	private double crossTime = 3.5;
	
	@Override
	protected void routine() throws AutoModeEndedException {
		//if tyr, wait for compressor
		if(Constants.kRobotName == Constants.RobotName.TYR) {
			waitTime(mCompressorWaitTime);
		}
		
		ArrayList<Routine> crossLowBar = new ArrayList<Routine>();
		
		//move across low bar
		crossLowBar.add(new DriveTimeAction(crossTime, -leftSpeed, -rightSpeed));
		
		//if Tyr, move shooter down
		if(Constants.kRobotName == Constants.RobotName.TYR) {
			crossLowBar.add(new GetLowAction());
		} 
		//if derica, move intake down while crossing
		else {
//			crossLowBar.add(new IntakeAction(0.25, WantedIntakeState.LOWERING));
		}
		runRoutine(new ParallelRoutine(crossLowBar));
	}

	@Override
	public String toString() {
		return "TimerLowBarAutoMode";
	}

	@Override
	public void prestart() {
		drive.resetController();
		
	}

}