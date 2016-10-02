package com.palyrobotics.frc2016.subsystems.controllers;

import com.palyrobotics.frc2016.Constants;
import com.palyrobotics.frc2016.HardwareAdaptor;
import com.palyrobotics.frc2016.Robot;
import com.palyrobotics.frc2016.subsystems.Drive;
import com.team254.lib.util.DriveSignal;
import com.team254.lib.util.Pose;

/**
 * Turns drivetrain using the gyroscope
 * @author Nihar
 *
 */
public class GyroTurnAngleController implements Drive.DriveController {
	
	private double maxVel;
	private double targetAngle;
	
	private double P;
	private double I;
	private double D;
	
	private double headingVelocity;
	private final Drive kDrive;
	
	private Pose mPriorSetpoint;
	
	public GyroTurnAngleController(Pose priorSetpoint, double angle, double maxVel, final Drive drive) {
		this.maxVel = maxVel;
		mPriorSetpoint = priorSetpoint;
		targetAngle = priorSetpoint.getHeading();
		kDrive = drive;
	}
	
	@Override
	public DriveSignal update(Pose pose) {
		P = targetAngle - pose.getHeading();
		
		I = I + P * Constants.kLooperDt;
		
		D = -pose.getHeadingVelocity();
		
		double leftSpeed = Math.max(-maxVel, 
				Math.min(maxVel, Constants.kGyroTurnKp*P + Constants.kGyroTurnKi*I + Constants.kGyroTurnKd*D));
		double rightSpeed = -leftSpeed;
//		System.out.println("ERROR" + P);
		
		return new DriveSignal(leftSpeed, -rightSpeed);
	}

	@Override
	public Pose getCurrentSetpoint() {
		return new Pose(
				mPriorSetpoint.getLeftDistance(),
				mPriorSetpoint.getRightDistance(),
				mPriorSetpoint.getLeftVelocity(),
				mPriorSetpoint.getRightVelocity(),
				mPriorSetpoint.getHeading()+targetAngle,
				mPriorSetpoint.getHeadingVelocity());
	}

	@Override
	public boolean onTarget() {
		if(Math.abs(kDrive.getPhysicalPose().getHeading()-targetAngle) < Constants.kAcceptableGyroTurnError &&
				kDrive.getPhysicalPose().getHeadingVelocity() < Constants.kAcceptableGyroTurnStopSpeed) {
			return true;
		} else return false;
	}

}
