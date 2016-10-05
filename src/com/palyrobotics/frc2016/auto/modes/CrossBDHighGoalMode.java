package com.palyrobotics.frc2016.auto.modes;

import com.palyrobotics.frc2016.auto.AutoMode;
import com.palyrobotics.frc2016.auto.AutoModeEndedException;

/**
 * Crosses a B/D class defense
 * Attempts a high goal shot when contstructor parameter is true
 *
 */
public class CrossBDHighGoalMode extends AutoMode {
	private boolean mAttemptShot = false;
	
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
		
	}

	@Override
	public void prestart() {
		// TODO Auto-generated method stub
		
	}

}
