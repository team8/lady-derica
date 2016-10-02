package com.palyrobotics.frc2016.auto.actions;

import com.team254.lib.util.DriveSignal;

public class EncoderDriveAction extends Action {

	private double distance;
	private double startPoint;
	
	public EncoderDriveAction(double distance) {
		this.distance = distance;
	}
	
	@Override
	public boolean isFinished() {
		return drive.controllerOnTarget();
//		if(distance >= 0) {
//			if(drive.getPhysicalPose().getRightDistance() < startPoint + distance) {
//				return false;
//			}
//			
//			else return true;
//		}
//		
//		else {
//			if(drive.getPhysicalPose().getRightDistance() > startPoint + distance) {
//				return false;
//			}
//			else return true;
//		}
	}

	@Override
	public void update() {
		System.out.println("left encoder: " + drive.getPhysicalPose().getRightDistance());
		System.out.println("right encoder: " + drive.getPhysicalPose().getLeftDistance());
	}

	@Override
	public void done() {
		System.out.println("EncoderDriveAction done");
		drive.setOpenLoop(DriveSignal.NEUTRAL);
	}

	@Override
	public void start() {
		System.out.println("Starting EncoderDriveAction");
		drive.reset();
		startPoint = drive.getPhysicalPose().getRightDistance();
		System.out.println(startPoint);
		//setDistanceSetpoint is relative
		drive.setDistanceSetpoint(distance);
	}
}