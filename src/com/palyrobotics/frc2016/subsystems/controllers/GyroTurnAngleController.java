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
	
	private Drive kDrive = HardwareAdaptor.kDrive;
	
	private Pose mPriorSetpoint;
	
	public GyroTurnAngleController(Pose priorSetpoint, double heading, double maxVel) {
		this.maxVel = maxVel;
		mPriorSetpoint = priorSetpoint;
		targetAngle = heading;
		System.out.println("Target angle: "+targetAngle);
	}
	
	@Override
	public DriveSignal update(Pose pose) {
		P = targetAngle - (pose.getHeading()-mPriorSetpoint.getHeading());
		
		I = I + P * Constants.kLooperDt;
		
		D = -pose.getHeadingVelocity();
		
		double leftSpeed = Math.max(-maxVel, 
				Math.min(maxVel, Constants.kGyroTurnKp*P + Constants.kGyroTurnKi*I + Constants.kGyroTurnKd*D));
		double rightSpeed = -leftSpeed;
//		System.out.println("PID calc: " + Constants.kGyroTurnKp*P + Constants.kGyroTurnKi*I + Constants.kGyroTurnKd*D);
//		System.out.println("Left speed "+leftSpeed);
		return new DriveSignal(leftSpeed, rightSpeed);
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
		System.out.println("Heading: "+kDrive.getPhysicalPose().getHeading());
		System.out.println("Heading velocity: "+kDrive.getPhysicalPose().getHeadingVelocity());
		System.out.println(Math.abs(kDrive.getPhysicalPose().getHeading()-mPriorSetpoint.getHeading()-targetAngle));
		if(Math.abs(kDrive.getPhysicalPose().getHeading()-mPriorSetpoint.getHeading()-targetAngle) < Constants.kAcceptableGyroTurnError &&
				kDrive.getPhysicalPose().getHeadingVelocity() < Constants.kAcceptableGyroTurnStopSpeed) {
			System.out.println("Gyro turn on target");
			return true;
		} else return false;
	}

}
