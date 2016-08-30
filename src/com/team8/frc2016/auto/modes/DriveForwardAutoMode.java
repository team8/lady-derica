package com.team8.frc2016.auto.modes;

import com.team8.frc2016.auto.AutoMode;
import com.team8.frc2016.auto.AutoModeEndedException;
import com.team8.frc2016.auto.actions.DriveForwardAction;

public class DriveForwardAutoMode extends AutoMode{
	protected void routine() throws AutoModeEndedException{
		waitTime(3);
		runAction(new DriveForwardAction(100));
	}

	public void prestart() {
		System.out.println("Starting DriveForwardAutoMode");
	}
}
