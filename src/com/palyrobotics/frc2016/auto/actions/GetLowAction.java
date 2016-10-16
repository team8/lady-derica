package com.palyrobotics.frc2016.auto.actions;

import com.palyrobotics.frc2016.subsystems.TyrShooter.WantedShooterState;

import edu.wpi.first.wpilibj.Timer;

/**
 * Used for Tyr to ensure shooter is down
 * @author Nihar
 *
 */
public class GetLowAction implements Action {
	private Timer m_timer = new Timer();
	private final double waitTime = 1; 
	
	@Override
	public boolean isFinished() {
		if(m_timer.get() <= waitTime) {
			return false;			
		}
		else return true;
	}

	@Override
	public void update() {
		tyrShooter.setWantedState(WantedShooterState.LOWERED);	
	}

	@Override
	public void done() {
		System.out.println("Brought shooter down");
		tyrShooter.setWantedState(WantedShooterState.NONE);
	}

	@Override
	public void start() {
		System.out.println("Bringing shooter down");
		m_timer.reset();
		m_timer.start();
	}

}
