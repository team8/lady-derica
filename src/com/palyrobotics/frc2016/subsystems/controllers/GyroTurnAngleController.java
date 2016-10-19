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
	
	private double P;
	private double I;
	private double D;
	
	private Drive kDrive = HardwareAdaptor.kDrive;
	
	private double mPriorHeading;
	private Pose setpoint;
	
	public GyroTurnAngleController(Pose priorSetpoint, double heading, double maxVel) {
		this.maxVel = maxVel;
		mPriorHeading = priorSetpoint.getHeading();
		setpoint = priorSetpoint.copy();
		setpoint.m_heading+=heading;
		System.out.println("Target angle: "+(setpoint.getHeading()-mPriorHeading));
	}
	
	@Override
	public DriveSignal update(Pose pose) {
		P = setpoint.getHeading()-pose.getHeading();
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
		return setpoint;
	}

	@Override
	public boolean onTarget() {
		System.out.println("Gyro Turn angle error: " + String.valueOf(Math.abs(setpoint.getHeading()-kDrive.getPhysicalPose().getHeading())).substring(0, 4));
		if(Math.abs(setpoint.getHeading()-kDrive.getPhysicalPose().getHeading()) < Constants.kAcceptableGyroTurnError &&
				Math.abs(kDrive.getPhysicalPose().getHeadingVelocity()) < Constants.kAcceptableGyroTurnStopSpeed) {
			System.out.println("Gyro turn on target");
			return true;
		} else return false;
	}

}
