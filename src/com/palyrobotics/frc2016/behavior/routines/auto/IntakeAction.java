package com.palyrobotics.frc2016.behavior.routines.auto;

import com.palyrobotics.frc2016.behavior.Routine;
import com.palyrobotics.frc2016.config.Commands;
import com.palyrobotics.frc2016.util.Subsystem;

public class IntakeAction extends Routine {

	private double time;
	private double startTime;
	private Commands.IntakeRequest intakeRequest;
	private boolean mIsDone = false;
	
	public IntakeAction(double time, Commands.IntakeRequest intakeRequest) {
		this.time = time;
		
		this.intakeRequest = intakeRequest;
	}
	
	@Override
	public boolean isFinished() {
		return mIsDone;
	}

	@Override
	public Commands update(Commands commands) {
		commands.intakeRequest = this.intakeRequest;
		if(System.currentTimeMillis() >= startTime + 1000*time) {
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
	public void start() {
		System.out.println("Moving intake!");
		mIsDone = false;
		this.startTime = System.currentTimeMillis();
	}

	@Override
	public Subsystem[] getRequiredSubsystems() {
		return new Subsystem[]{intake};
	}

	@Override
	public String getName() {
		return "Intake";
	}
}
