package com.palyrobotics.frc2016.auto.actions;

import com.team254.lib.trajectory.Path;
import com.team254.lib.util.DriveSignal;

public class DrivePathAction implements Action {

	private Path path;
	
	public DrivePathAction(Path path) {
		this.path = path;
	}
	
	@Override
	public boolean isFinished() {
		return drive.controllerOnTarget();
	}

	@Override
	public void update() {
	}

	@Override
	public void done() {
		System.out.println("DrivePathAction Done");
		drive.setOpenLoop(DriveSignal.NEUTRAL);
	}

	@Override
	public void start() {
		System.out.println("DrivePathAction Started");
		drive.setPathSetpoint(path);
	}

}
