package com.palyrobotics.frc2016.behavior.routines.auto;

import com.palyrobotics.frc2016.behavior.Routine;

import com.palyrobotics.frc2016.config.Commands;
import com.palyrobotics.frc2016.subsystems.Intake;
import com.palyrobotics.frc2016.util.Subsystem;
import edu.wpi.first.wpilibj.Timer;

/**
 * Expels the intake for the time specified
 * @author Nihar
 */
public class ExpelIntake extends Routine {
	private double time;
	private boolean mIsDone = false;
	private Timer mTimer = new Timer();
	
	public ExpelIntake(double time) {
		this.time = time;
	}
	

	@Override
	public void start() {
		mIsDone = false;
		mTimer.reset();
		mTimer.start();
	}

	@Override
	public Commands update(Commands commands) {
		commands.intakeRequest = Commands.IntakeRequest.EXPEL;
		if(mTimer.get() >= time) {
			mIsDone = true;
		}
		return commands;
	}

	@Override
	public Commands cancel(Commands commands) {
		commands.intakeRequest = Commands.IntakeRequest.STOP;
		return commands;
	}

	@Override
	public boolean isFinished() {
		return mIsDone;
	}

	@Override
	public Subsystem[] getRequiredSubsystems() {
		return new Subsystem[]{intake};
	}
	@Override
	public String getName() {
		return "ExpelIntake";
	}
}