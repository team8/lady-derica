package com.palyrobotics.frc2016.auto.actions;

import com.palyrobotics.frc2016.subsystems.Intake.WantedIntakeState;

import edu.wpi.first.wpilibj.Timer;

public class ExpelIntake implements Action {

	private double time;
	private boolean mIsDone = false;
	private Timer mTimer = new Timer();
	
	public ExpelIntake(double time) {
		this.time = time;
	}
	
	@Override
	public boolean isFinished() {
		return mIsDone;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		intake.setWantedState(WantedIntakeState.EXPELLING);
		if(mTimer.get() >= time) {
			mIsDone = true;
		}
	}

	@Override
	public void done() {
		intake.setSpeed(0);
	}

	@Override
	public void start() {
		mIsDone = false;
		mTimer.reset();
		mTimer.start();
		
	}

}
