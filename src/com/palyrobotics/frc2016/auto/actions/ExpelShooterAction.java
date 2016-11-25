package com.palyrobotics.frc2016.auto.actions;

import com.palyrobotics.frc2016.subsystems.Intake.WantedIntakeState;
import com.palyrobotics.frc2016.subsystems.LowGoalShooter.WantedLowGoalState;

import edu.wpi.first.wpilibj.Timer;

public class ExpelShooterAction implements Action {

	private double time;
	private boolean mIsDone = false;
	private Timer mTimer = new Timer();
	
	public ExpelShooterAction(double time) {
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
		lowShooter.setWantedState(WantedLowGoalState.SHOOTING);
		
		
		if(mTimer.get() >= time) {
			mIsDone = true;
		}
	}

	@Override
	public void done() {
		intake.setWantedState(WantedIntakeState.NONE);
		lowShooter.setWantedState(WantedLowGoalState.STOP);
	}

	@Override
	public void start() {
		mIsDone = false;
		mTimer.reset();
		mTimer.start();
	}
	
}
