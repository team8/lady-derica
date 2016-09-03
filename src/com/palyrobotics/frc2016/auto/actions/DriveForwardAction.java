package com.palyrobotics.frc2016.auto.actions;

import com.palyrobotics.lib.util.DriveSignal;

public class DriveForwardAction extends Action {

	private int distance;
	
	public DriveForwardAction(int distance) {
		this.distance = distance;
	}
	
	@Override
	public boolean isFinished() {
		if(drive.m_right_encoder.getDistance() < distance) {
			return false;
		}
		
		else {
			return true;
		}
	}

	@Override
	public void update() {
		System.out.println("encoder " + drive.m_right_encoder.get());
	}

	@Override
	public void done() {
		System.out.println("done");
		drive.setOpenLoop(new DriveSignal(0, 0));
	}

	@Override
	public void start() {
		System.out.println("start");
		
		drive.setDistanceSetpoint(distance);
		drive.update();
	}

}
