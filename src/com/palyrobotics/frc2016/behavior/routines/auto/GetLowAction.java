package com.palyrobotics.frc2016.behavior.routines.auto;

import com.palyrobotics.frc2016.behavior.Routine;
import com.palyrobotics.frc2016.config.Commands;
import com.palyrobotics.frc2016.subsystems.TyrShooter.WantedShooterState;
import com.palyrobotics.frc2016.util.Subsystem;

import edu.wpi.first.wpilibj.Timer;

/**
 * Used for Tyr to ensure shooter is down
 * @author Nihar
 *
 */
public class GetLowAction extends Routine {
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
	public Commands update(Commands commands) {
		tyrShooter.setWantedState(WantedShooterState.LOWERED);	
		return commands;
	}

	@Override
	public Commands cancel(Commands commands) {
		System.out.println("Brought shooter down");
		tyrShooter.setWantedState(WantedShooterState.NONE);
		return commands;
	}

	@Override
	public void start() {
		System.out.println("Bringing shooter down");
		m_timer.reset();
		m_timer.start();
	}

	@Override
	public Subsystem[] getRequiredSubsystems() {
		return new Subsystem[]{tyrShooter};
	}

	@Override
	public String getName() {
		return "GetLowAction";
	}

}
