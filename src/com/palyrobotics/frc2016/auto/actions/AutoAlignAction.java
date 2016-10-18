package com.palyrobotics.frc2016.auto.actions;

import com.team254.lib.util.DriveSignal;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 * Aligns to the goal
 * @author Eric 
 *
 */
public class AutoAlignAction implements Action {

	private NetworkTable table = NetworkTable.getTable("visiondata");
	double skewAngle = table.getNumber("skewangle", 10000);
	
	@Override
	public boolean isFinished() {
		// If no goal found, or vision data and drive controller are on target
		if((Math.abs(skewAngle) <= 1.5 && drive.controllerOnTarget()) || skewAngle == 10002 || skewAngle == 9998) {
			return true;
		} 
		else return false;
	}

	@Override
	public void update() {
		skewAngle = table.getNumber("skewangle", 10000);
		skewAngle /= 2;
		if(!drive.hasController() && (Math.abs(skewAngle) >= 3)) {
			// Offset the skew by 2
//			skewAngle = (skewAngle >=0) ? skewAngle-2:skewAngle+2;
			drive.setAutoAlignSetpoint(skewAngle);
		}
	}

	@Override
	public void done() {
		System.out.println("Finished auto align");
		drive.setOpenLoop(DriveSignal.NEUTRAL);
	}

	@Override
	public void start() {
		System.out.println("Starting auto align");
	}

}
