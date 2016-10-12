package com.palyrobotics.frc2016.auto.actions;

import com.palyrobotics.frc2016.HardwareAdaptor;
import com.palyrobotics.frc2016.subsystems.Drive;
import com.team254.lib.util.DriveSignal;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 * Aligns to the goal
 * TODO: Everything
 * @author 
 *
 */
public class AutoAlignAction implements Action {

	
	private NetworkTable table = NetworkTable.getTable("visiondata");
	private Drive drive = HardwareAdaptor.kDrive;
	double skewAngle = table.getNumber("skewangle", 10000);
	
	@Override
	public boolean isFinished() {
		if(Math.abs(skewAngle) < 3 || skewAngle == 10000) {
			return true;
		} 
		else return false;
	}

	@Override
	public void update() {
		skewAngle = table.getNumber("skewangle", 10000);
		skewAngle = (skewAngle >=0) ? skewAngle-2:skewAngle+2;
		if(!drive.hasController()) {
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
