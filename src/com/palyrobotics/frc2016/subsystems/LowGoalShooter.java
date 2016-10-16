package com.palyrobotics.frc2016.subsystems;

import com.team254.lib.util.CheesySpeedController;
import com.team254.lib.util.Loop;
import com.team254.lib.util.StateHolder;
import com.team254.lib.util.Subsystem;

public class LowGoalShooter extends Subsystem implements Loop{
	
	private CheesySpeedController m_top_motor;
	
	public enum WantedLowGoalState {
		SHOOTING, INTAKING, NONE
	}
	public WantedLowGoalState mWantedState = WantedLowGoalState.NONE;
	
	public LowGoalShooter(String name, CheesySpeedController lowGoalShooterMotor) {
		super(name);
		m_top_motor = lowGoalShooterMotor;
	}
	
	public void shoot(double speed) {
		m_top_motor.set(speed);
	}
	
	public void load(double speed) {
		m_top_motor.set(-speed);
	}
	
	public void stopMotor() {
		m_top_motor.set(0);
	}
	
	public void setWantedState(WantedLowGoalState state) {
		this.mWantedState = state;
	}

	@Override
	public void getState(StateHolder states) {
		
	}

	@Override
	public void reloadConstants() {
		
	}

	@Override
	public void onStart() {
		
	}

	@Override
	public void onLoop() {
		switch (this.mWantedState) {
		case INTAKING:
			this.load(.7);
			break;
		case SHOOTING:
			this.shoot(1.0);
			break;
		case NONE:
			this.stopMotor();
			break;
		default:
			break;
		}
	}

	@Override
	public void onStop() {
		
	}

}
