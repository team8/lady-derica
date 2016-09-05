package com.palyrobotics.frc2016.auto.modes;

import com.palyrobotics.frc2016.auto.AutoMode;
import com.palyrobotics.frc2016.auto.AutoModeEndedException;
import com.palyrobotics.frc2016.auto.actions.EncoderDriveAction;
import com.palyrobotics.frc2016.auto.actions.TimerDriveAction;

public class WaitForwardBackwardAutoMode extends AutoMode{
	
	private int waitTime;
	private int driveTime;
	private int backDistance;
	
	/**
	 * 
	 * @param waitTime time to wait before starting
	 * @param driveTime time to drive forwards
	 * @param backDistance distance to back up
	 */
	public WaitForwardBackwardAutoMode(int waitTime, int driveTime, int backDistance) {
		this.waitTime = waitTime;
		this.driveTime = driveTime;
		this.backDistance = backDistance;
	}
	
	protected void routine() throws AutoModeEndedException {
		waitTime(waitTime);
		runAction(new TimerDriveAction(driveTime));
		runAction(new EncoderDriveAction(backDistance));
	}

	public void prestart() {
		System.out.println("Starting WaitForwardBackwardAutoMode");
	}
}