package com.palyrobotics.frc2016.auto.actions;

import com.team254.lib.util.DriveSignal;

public class EncoderDriveAction extends Action {

	private int distance;
	private int startPoint;
	
	public EncoderDriveAction(int distance) {
		this.distance = distance;
		startPoint = drive.m_right_encoder.get();
	}
	
	@Override
	public boolean isFinished() {
		if(distance >= 0) {
			if(drive.m_right_encoder.get() < startPoint + distance) {
				return false;
			}
			
			else return true;
		}
		
		else {
			if(drive.m_right_encoder.get() > startPoint + distance) {
				return false;
			}
			else return true;
		}
	}

	@Override
	public void update() {
		System.out.println("encoder " + drive.m_right_encoder.get());
	}

	@Override
	public void done() {
		System.out.println("EncoderDriveAction done");
		drive.setOpenLoop(new DriveSignal(0, 0));
	}

	@Override
	public void start() {
		System.out.println("Starting EncoderDriveAction");
		//setDistanceSetpoint is relative
		drive.setDistanceSetpoint(distance);
	}
}