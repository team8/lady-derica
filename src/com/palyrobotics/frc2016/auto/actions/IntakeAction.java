package com.palyrobotics.frc2016.auto.actions;

import com.palyrobotics.frc2016.subsystems.Intake.WantedIntakeState;

import edu.wpi.first.wpilibj.Timer;

public class IntakeAction implements Action {

	private double time;
	private WantedIntakeState state;
	private boolean mIsDone = false;
	private Timer mTimer = new Timer();
	
	public IntakeAction(double time, WantedIntakeState state) {
		this.time = time;
		this.state = state;
	}
	
	@Override
	public boolean isFinished() {
		return mIsDone;
	}

	@Override
	public void update() {
		intake.setWantedState(state);
		if(mTimer.get() >= time) {
			mIsDone = true;
		}
	}

	@Override
	public void done() {
		intake.setSpeed(0.0);
	}

	@Override
	public void start() {
		System.out.println("Moving intake!");
		mIsDone = false;
		mTimer.reset();
		mTimer.start();
	}
}