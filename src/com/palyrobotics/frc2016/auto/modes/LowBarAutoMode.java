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
import com.palyrobotics.frc2016.auto.actions.ParallelAction;

public class LowBarAutoMode extends AutoMode {
	
	public static final double mCompressorWaitTime = 3;

	@Override
	protected void routine() throws AutoModeEndedException {
		waitTime(mCompressorWaitTime); //Waits for compressor
		ArrayList<Action> crossLowBar = new ArrayList<Action>(2);
		crossLowBar.add(new DriveDistanceAction(Constants.kLowBarDistance, Constants.kLowBarVelocity));
		if(Robot.name == RobotName.TYR) {
			crossLowBar.add(new GetLowAction());
		} else {
			// TODO: Does Derica have any restrictions or simultaneous actions to run
		}
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