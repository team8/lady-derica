package com.palyrobotics.frc2016.auto.modes;

import com.palyrobotics.frc2016.auto.AutoMode;
import com.palyrobotics.frc2016.auto.AutoModeEndedException;
import com.palyrobotics.frc2016.subsystems.Drive.DriveGear;

/**
 * Crosses a B/D class defense
 * Attempts a high goal shot when contstructor parameter is true
 *
 */
public class CrossBDHighGoalMode extends AutoMode {
	private boolean mAttemptShot = false;
	public static final double mCompressorWaitTime = 3;

	/**
	 * Cross a B/D class defense
	 * @param attemptShot true to auto-align and shoot, false to only cross
	 */
	public CrossBDHighGoalMode(boolean attemptShot) {
		mAttemptShot = attemptShot;
	}
	
	@Override
	protected void routine() throws AutoModeEndedException {
		// TODO Auto-generated method stub
		waitTime(mCompressorWaitTime);
		drive.setGear(DriveGear.HIGH);
	}

	@Override
	public void prestart() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String toString() {
		return "Cross_BD" + ((mAttemptShot) ? "_HighGoal" : "");
	}
}
