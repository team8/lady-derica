package com.palyrobotics.frc2016.behavior.routines.auto;

import com.palyrobotics.frc2016.behavior.Routine;
import com.palyrobotics.frc2016.config.Commands;
import com.palyrobotics.frc2016.util.Subsystem;
import edu.wpi.first.wpilibj.Timer;

public class ExpelShooterAction extends Routine {

	private double time;
	private boolean mIsDone = false;
	private Timer mTimer = new Timer();
	
	public ExpelShooterAction(double time) {
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
		commands.low_request = Commands.LowGoalShooterRequest.SHOOT;
		if(mTimer.get() >= time) {
			mIsDone = true;
		}
		return commands;
	}

	@Override
	public Commands cancel(Commands commands) {
		commands.intakeRequest = Commands.IntakeRequest.STOP;
		commands.low_request = Commands.LowGoalShooterRequest.NONE;
		return commands;
	}


	@Override
	public boolean isFinished() {
		return mIsDone;
	}

	@Override
	public Subsystem[] getRequiredSubsystems() {
		return new Subsystem[]{intake, lowGoalShooter};
	}

	@Override
	public String getName() {
		return "ExpelLowGoalShooter";
	}
}
