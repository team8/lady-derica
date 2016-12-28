package com.palyrobotics.frc2016.behavior.routines.auto;

import com.palyrobotics.frc2016.behavior.Routine;
import com.palyrobotics.frc2016.config.Commands;
import com.palyrobotics.frc2016.config.Constants;
import com.palyrobotics.frc2016.config.RobotState;
import com.palyrobotics.frc2016.robot.Robot;
import com.palyrobotics.frc2016.util.Subsystem;

import edu.wpi.first.wpilibj.Timer;

/**
 * Shoot high goal using Tyr's Shooter
 * @author Nihar
 *
 */
public class ShootAction extends Routine {
	Timer m_timer = new Timer();
	// Wait time in between extending and unlocking the shooter
	private final double waitTime = 1;
	private boolean mIsDone = false;
	
	@Override
	public Commands update(Commands commands) {
		// Extend shooter and release grabber
		tyrShooter.extend();
		tyrShooter.release();
		if(m_timer.get()>= waitTime) {
			// Wait for potential lag, then shoot
			tyrShooter.unlock();
			mIsDone = true;
		}
		return commands;
	}

	@Override
	public boolean isFinished() {
		return mIsDone;
	}
	
	@Override
	public void start() {
		mIsDone = false;
		if(Constants.kRobotName != Constants.RobotName.TYR) {
			System.err.println("Not Tyr shooter...");
			mIsDone = true;
			return;
		}
		System.out.println("Starting firing sequence");
		m_timer.reset();
		m_timer.start();
	}

	@Override
	public Commands cancel(Commands commands) {
		System.out.println("Shots fired");
		return commands;
	}

	@Override
	public Subsystem[] getRequiredSubsystems() {
		return new Subsystem[]{tyrShooter};
	}

	@Override
	public String getName() {
		return "Shoot";
	}

}
