package com.palyrobotics.frc2016.auto;

import com.palyrobotics.frc2016.auto.actions.*;
import com.palyrobotics.frc2016.subsystems.*;

public abstract class AutoMode extends AutoModeBase {
	/**
	 * Keeps access to all subsystems to modify their output and read their status like
	 * {@link Drive#controllerOnTarget()} {@link Drive#setGear(Drive.DriveGear)}
	 */
	protected final Drive drive = Drive.getInstance();
	protected final Intake intake = Intake.getInstance();
	protected final Breacher breacher = Breacher.getInstance();
	protected final Catapult catapult = Catapult.getInstance();
	protected final LowGoalShooter lowGoalShooter = LowGoalShooter.getInstance();
	protected final TyrShooter tyrShooter = TyrShooter.getInstance();

	public void waitTime(double seconds) throws AutoModeEndedException {
		runAction(new TimeoutAction(seconds));
	}
}
