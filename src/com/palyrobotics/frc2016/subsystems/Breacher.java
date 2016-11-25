package com.palyrobotics.frc2016.subsystems;

import com.palyrobotics.frc2016.input.Commands;
import com.palyrobotics.frc2016.input.RobotState;
import com.palyrobotics.frc2016.util.Subsystem;
import com.team254.lib.util.Loop;


public class Breacher extends Subsystem implements Loop {
	private double motorOutput = 0.0;

	public Breacher() {
		super("Breacher");
	}

	public void update(Commands commands, RobotState robotState) {
		motorOutput = commands.operatorStickInput.rightY;
	}

	public double getMotorOutput() {
		return motorOutput;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		
	}
}