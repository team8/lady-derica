package com.palyrobotics.frc2016.subsystems.controllers;

import com.palyrobotics.frc2016.subsystems.Drive;
import com.palyrobotics.lib.util.DriveSignal;
import com.palyrobotics.lib.util.Pose;

public class EncoderTurnAngleController implements Drive.DriveController {

	private double angle;
	private double leftTarget;
	private double rightTarget;
	
	public EncoderTurnAngleController(Pose priorSetpoint, double angle, double maxVel, double kDegreeToDistance) {
		this.angle = angle;
		leftTarget = priorSetpoint.getLeftDistance() + angle * kDegreeToDistance;
		rightTarget = priorSetpoint.getRightDistance() - angle * kDegreeToDistance;
		
	}
	
	@Override
	public DriveSignal update(Pose pose) {
		return null;
	}

	@Override
	public Pose getCurrentSetpoint() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onTarget() {
		// TODO Auto-generated method stub
		return false;
	}

}
