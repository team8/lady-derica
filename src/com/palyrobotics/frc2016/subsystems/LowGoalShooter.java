package com.palyrobotics.frc2016.subsystems;

import com.team254.lib.util.CheesySpeedController;
import com.team254.lib.util.Loop;
import com.team254.lib.util.StateHolder;
import com.team254.lib.util.Subsystem;

public class LowGoalShooter extends Subsystem implements Loop {
	
	private CheesySpeedController m_top_motor;
	
	public enum WantedLowGoalState {
		SHOOTING, INTAKING, NONE
	}
	public WantedLowGoalState mWantedState = WantedLowGoalState.NONE;
	
	public LowGoalShooter(String name, CheesySpeedController lowGoalShooterMotor) {
		super(name);
		m_top_motor = lowGoalShooterMotor;
	}
	
	public void setWantedState(WantedLowGoalState state) {
		this.mWantedState = state;
	}
	
	public void stopMotor() {
		m_top_motor.set(0);
	}

	@Override
	public void getState(StateHolder states) {
		
	}

	@Override
	public void reloadConstants() {
		//TOOD: Empty stub
	}

	@Override
	public void onStart() {
		m_top_motor.set(0);
	}

	@Override
	public void onLoop() {
		switch (this.mWantedState) {
		case INTAKING:
			m_top_motor.set(-0.7);
			break;
		case SHOOTING:
			m_top_motor.set(1.0);
			break;
		case NONE:
			m_top_motor.set(0.0);
			break;
		default:
			break;
		}
	}

	@Override
	public void onStop() {
		m_top_motor.set(0);
	}
}
