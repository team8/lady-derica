package com.palyrobotics.frc2016.subsystems;

import com.palyrobotics.frc2016.util.Commands;
import com.palyrobotics.frc2016.util.RobotState;
import com.palyrobotics.frc2016.util.Subsystem;


public class Breacher extends Subsystem {
	private double motorOutput = 0.0;

	public Breacher() {
		super("Breacher");
	}

	public double getMotorOutput() {
		return motorOutput;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Commands commands, RobotState robotState) {
		motorOutput = commands.operatorStickInput.rightY;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
}