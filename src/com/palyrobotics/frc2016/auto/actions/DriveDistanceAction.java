package com.palyrobotics.frc2016.auto.actions;

import com.palyrobotics.frc2016.robot.team254.lib.util.DriveSignal;

public class DriveDistanceAction implements Action {

	private double distance;
	private double maxVel;
	private double startPoint;
	
	public DriveDistanceAction(double distance) {
		this.distance = distance;
		this.maxVel = 1;
	}
	
	public DriveDistanceAction(double distance, double maxVel) {
		this.distance = distance;
		this.maxVel = maxVel;
	}
	
	@Override
	public boolean isFinished() {
		return drive.controllerOnTarget();
	}

	@Override
	public void update() {
//		System.out.println("left encoder: " + drive.getPhysicalPose().getRightDistance());
//		System.out.println("right encoder: " + drive.getPhysicalPose().getLeftDistance());
	}

	@Override
	public void done() {
		System.out.println("EncoderDriveAction done");
		drive.setOpenLoop(DriveSignal.NEUTRAL);
	}

	@Override
	public void start() {
		System.out.println("Starting EncoderDriveAction");
		drive.resetController();
		startPoint = drive.getPhysicalPose().getRightDistance();
		System.out.println(startPoint);
		//setDistanceSetpoint is relative
		drive.setDistanceSetpoint(distance, maxVel);
	}
}