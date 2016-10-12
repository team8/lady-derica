package com.palyrobotics.frc2016.auto.actions;

import com.palyrobotics.frc2016.Robot;
import com.palyrobotics.frc2016.Robot.RobotName;

import edu.wpi.first.wpilibj.Timer;

/**
 * Shoot high goal using Tyr's Shooter
 * @author Nihar
 *
 */
public class ShootAction implements Action {
	Timer m_timer = new Timer();
	// Wait time in between extending and unlocking the shooter
	private final double waitTime = 1;
	private boolean mIsDone = false;
	
	@Override
	public void update() {
		// Extend shooter and release grabber
		tyrShooter.extend();
		tyrShooter.release();
		if(m_timer.get()>= waitTime) {
			// Wait for potential lag, then shoot
			tyrShooter.unlock();
			mIsDone = true;
		}
	}

	@Override
	public boolean isFinished() {
		return mIsDone;
	}
	
	@Override
	public void start() {
		mIsDone = false;
		if(Robot.name != RobotName.TYR) {
			System.err.println("Not Tyr shooter...");
			mIsDone = true;
			return;
		}
		System.out.println("Starting firing sequence");
		m_timer.reset();
		m_timer.start();
	}

	@Override
	public void done() {
		System.out.println("Shooting now");
	}

}
