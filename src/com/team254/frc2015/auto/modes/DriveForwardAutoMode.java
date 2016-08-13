package com.team254.frc2015.auto.modes;

import com.team254.frc2015.auto.AutoMode;
import com.team254.frc2015.auto.AutoModeEndedException;
import com.team254.frc2015.auto.actions.DriveForwardAction;

public class DriveForwardAutoMode extends AutoMode{
	protected void routine() throws AutoModeEndedException{
		waitTime(3);
		runAction(new DriveForwardAction(100));
	}

	public void prestart() {
		System.out.println("Starting DriveForwardAutoMode");
	}
}
