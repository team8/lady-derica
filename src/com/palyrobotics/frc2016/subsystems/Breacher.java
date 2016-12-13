package com.palyrobotics.frc2016.subsystems;

import com.palyrobotics.frc2016.config.Commands;
import com.palyrobotics.frc2016.config.RobotState;
import com.palyrobotics.frc2016.util.Subsystem;


public class Breacher extends Subsystem {
	private static Breacher mBreacher = new Breacher();
	public static Breacher getInstance() {
		return mBreacher;
	}
	private double motorOutput = 0.0;

	private Breacher() {
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