package com.palyrobotics.frc2016.auto.actions;

import com.palyrobotics.frc2016.Robot;
import com.palyrobotics.frc2016.Robot.RobotName;
import com.palyrobotics.frc2016.subsystems.TyrShooter.WantedShooterState;

import edu.wpi.first.wpilibj.Timer;

public class RaiseShooterAction implements Action {
	private boolean mIsDone = false;
	private Timer mTimer = new Timer();
	private double mWaitTime = 1;
	
	@Override
	public boolean isFinished() {
		return mIsDone;
	}

	@Override
	public void update() {
		if(Robot.name == RobotName.TYR) {
			tyrShooter.setWantedState(WantedShooterState.RAISED);
			if(mTimer.get() >= mWaitTime) {
				mIsDone = true;
			}
		}
	}

	@Override
	public void done() {
		System.out.println("Shooter is raised");
	}

	@Override
	public void start() {
		mIsDone = false;
		mTimer.reset();
		mTimer.start();
		if(Robot.name != RobotName.TYR) {
			System.err.println("No Tyr shooter!");
			mIsDone = true;
		}
		System.out.println("Raising Tyr Shooter");
	}

}
