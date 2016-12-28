package com.palyrobotics.frc2016.behavior.routines.auto;

import com.palyrobotics.frc2016.behavior.Routine;
import com.palyrobotics.frc2016.config.Commands;
import com.palyrobotics.frc2016.config.Constants;
import com.palyrobotics.frc2016.config.RobotState;
import com.palyrobotics.frc2016.robot.Robot;
import com.palyrobotics.frc2016.robot.team254.lib.util.ConstantsBase;
import com.palyrobotics.frc2016.subsystems.TyrShooter.WantedShooterState;
import com.palyrobotics.frc2016.util.Subsystem;

import edu.wpi.first.wpilibj.Timer;

public class RaiseShooterAction extends Routine {
	private boolean mIsDone = false;
	private Timer mTimer = new Timer();
	private double mWaitTime = 1;
	
	@Override
	public boolean isFinished() {
		return mIsDone;
	}

	@Override
	public Commands update(Commands commands) {
		if(Constants.kRobotName == Constants.RobotName.TYR) {
			tyrShooter.setWantedState(WantedShooterState.RAISED);
			if(mTimer.get() >= mWaitTime) {
				mIsDone = true;
			}
		}
		return commands;
	}

	@Override
	public Commands cancel(Commands commands) {
		System.out.println("Shooter is raised");
		return commands;
	}

	@Override
	public void start() {
		mIsDone = false;
		mTimer.reset();
		mTimer.start();
		if(Constants.kRobotName == Constants.RobotName.DERICA) {
			System.err.println("No Tyr shooter!");
			mIsDone = true;
		}
		System.out.println("Raising Tyr Shooter");
	}

	@Override
	public Subsystem[] getRequiredSubsystems() {
		return new Subsystem[]{tyrShooter};
	}

	@Override
	public String getName() {
		return "RaiseShooter";
	}

}
