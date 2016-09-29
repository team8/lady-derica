package com.palyrobotics.frc2016.auto.modes;

import com.palyrobotics.frc2016.Constants;
import com.palyrobotics.frc2016.auto.AutoMode;
import com.palyrobotics.frc2016.auto.AutoModeEndedException;

public class LowBarAutoMode extends AutoMode {
	
	public static final double m_wait_time = 5;
	public static final double m_drive_timeout = 5;

	@Override
	protected void routine() throws AutoModeEndedException {
		waitTime(m_wait_time); //Waits for compressor
		//TODO: action to move the shooter and grabber down
//		runAction(new WaitForDriveDistanceAction(m_drive_timeout, Constants.lowBarDistance));
	}

	@Override
	public void prestart() {
		drive.reset();
		//TODO: reset all other subsystems
	}

}