package com.palyrobotics.frc2016.subsystems.controllers;

import com.palyrobotics.frc2016.Constants;
import com.palyrobotics.frc2016.HardwareAdaptor;
import com.palyrobotics.frc2016.Robot;
import com.palyrobotics.frc2016.subsystems.Drive;
import com.team254.lib.util.DriveSignal;
import com.team254.lib.util.Pose;

public class EncoderTurnAngleController implements Drive.DriveController {
	
	private double maxVel;
	private double leftTarget;
	private double rightTarget;
	private double leftSpeed;
	private double rightSpeed;
	private double kDegreeToDistance;
	
	private double leftP;
	private double leftI;
	private double leftD;
	
	private double rightP;
	private double rightI;
	private double rightD;
	
	public EncoderTurnAngleController(Pose priorSetpoint, double angle, double maxVel) {
		this.maxVel = maxVel;
		
		if(Robot.name == Robot.RobotName.DERICA) {
			kDegreeToDistance = Constants.kDericaDegreeToDistance;
		} else if(Robot.name == Robot.RobotName.TYR) {
			kDegreeToDistance = Constants.kTyrDegreeToDistance;
		}
		
		leftTarget = priorSetpoint.getLeftDistance() + angle * kDegreeToDistance;
		rightTarget = priorSetpoint.getRightDistance() - angle * kDegreeToDistance;
	}
	
	@Override
	public DriveSignal update(Pose pose) {
		leftP = leftTarget - pose.getLeftDistance();
		rightP = rightTarget - pose.getRightDistance();
		
		leftI = leftI + leftP * Constants.kLooperDt;
		rightI = rightI + rightP * Constants.kLooperDt;
		
		leftD = -pose.getLeftVelocity();
		rightD = -pose.getRightVelocity();
		
		leftSpeed = Math.max(-maxVel, 
				Math.min(maxVel, Constants.kEncoderTurnKp*leftP + Constants.kEncoderTurnKi*leftI + Constants.kEncoderTurnKd*leftD));
		rightSpeed = Math.max(-maxVel, 
				Math.min(maxVel, Constants.kEncoderTurnKp*rightP + Constants.kEncoderTurnKi*rightI + Constants.kEncoderTurnKd*rightD));
		
		System.out.println("left P" + Constants.kEncoderTurnKp*leftP);
		System.out.println("left I" + Constants.kEncoderTurnKi*leftI);
		System.out.println("left D" + Constants.kEncoderTurnKd*leftD);
		System.out.println("right P" + Constants.kEncoderTurnKp*rightP);
		System.out.println("right I" + Constants.kEncoderTurnKi*rightI);
		System.out.println("right D" + Constants.kEncoderTurnKd*rightD);
		System.out.println("angle error" + leftP/kDegreeToDistance);
		System.out.println("left speed:" + leftSpeed);
		System.out.println("right speed:" + rightSpeed);
		System.out.println("--------------------------------");
		
//		System.out.println("ERROR" + leftP);
		
		return new DriveSignal(leftSpeed, rightSpeed);
	}

	@Override
	public Pose getCurrentSetpoint() {
//		return new Pose(
//				HardwareAdaptor.kDrive.m_left_encoder.getDistance(),//leftTarget,
//				HardwareAdaptor.kDrive.m_right_encoder.getDistance(),//rightTarget,
//				HardwareAdaptor.kDrive.m_left_encoder.getRate(),//leftSpeed,
//				HardwareAdaptor.kDrive.m_right_encoder.getRate(),//rightSpeed,
//				Math.toRadians(HardwareAdaptor.kDrive.m_gyro.getAngle()),
//				HardwareAdaptor.kDrive.m_gyro.getRate());
		return new Pose(
				leftTarget,
				rightTarget,
				leftSpeed,
				rightSpeed,
				HardwareAdaptor.kDrive.m_gyro.getAngle(),
				HardwareAdaptor.kDrive.m_gyro.getRate());
	}

	@Override
	public boolean onTarget() {
		if(Math.abs(leftP/kDegreeToDistance) < Constants.kAcceptableEncoderTurnError && 
				Math.abs(rightP/kDegreeToDistance) < Constants.kAcceptableEncoderTurnError && leftD == 0 && rightD == 0) {
			return true;
		} else return false;
	}

}
