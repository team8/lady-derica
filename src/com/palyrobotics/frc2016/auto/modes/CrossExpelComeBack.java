package com.palyrobotics.frc2016.auto.modes;

import java.util.ArrayList;

import com.palyrobotics.frc2016.Constants;
import com.palyrobotics.frc2016.auto.AutoMode;
import com.palyrobotics.frc2016.auto.AutoModeEndedException;
import com.palyrobotics.frc2016.auto.actions.Action;
import com.palyrobotics.frc2016.auto.actions.AutoAlignAction;
import com.palyrobotics.frc2016.auto.actions.DriveDistanceAction;
import com.palyrobotics.frc2016.auto.actions.ExpelIntake;
import com.palyrobotics.frc2016.auto.actions.ExpelShooterAction;
import com.palyrobotics.frc2016.auto.actions.IntakeAction;
import com.palyrobotics.frc2016.auto.actions.ParallelAction;
import com.palyrobotics.frc2016.auto.actions.RaiseShooterAction;
import com.palyrobotics.frc2016.auto.actions.ShootAction;
import com.palyrobotics.frc2016.auto.actions.TurnAngleAutoAction;
import com.palyrobotics.frc2016.subsystems.Drive.DriveGear;

/**
 * Crosses a B/D class defense
 * Attempts a high goal shot when contstructor parameter is true
 *
 */
public class CrossExpelComeBack extends AutoMode {
	
	public static final double mCompressorWaitTime = 3;
	private final boolean kTurnBeforeComingBack;
	
	/**
	 * Auto mode where the robot crosses, expels a boulder, than turns around 
	 * to try and accumulate again
	 * @param turnBeforeComingBack: if this is the robot will do the 180 degree
	 * turn before crossing back.  If this is false, it will do it after coming back
	 */
	public CrossExpelComeBack(boolean turnBeforeComingBack) {
		this.kTurnBeforeComingBack = turnBeforeComingBack;
	}
	
	@Override
	protected void routine() throws AutoModeEndedException {
		// drive forward
		runAction(new DriveDistanceAction(Constants.kBreachDistance));
		// expel the ball
		runAction(new ExpelShooterAction(Constants.kAutoShooterExpelTime));
		runAction(new ExpelIntake(Constants.kAutoShooterExpelTime));
		// turn around
		
		if (this.kTurnBeforeComingBack) {
			runAction(new TurnAngleAutoAction(180));
			// drive back
			runAction(new DriveDistanceAction(Constants.kBreachDistance)); 	
		}
		else {
			runAction(new DriveDistanceAction(-Constants.kBreachDistance)); 	
			runAction(new TurnAngleAutoAction(180));
		}

		// drive to the ball and accumulate at the same time
		ArrayList<Action> secondaryActions = new ArrayList<Action>();
		secondaryActions.add(new DriveDistanceAction(Constants.kDistanceToDriveToAccumulateExtra));
		secondaryActions.add(new IntakeAction(1.0));
		
		runAction(new ParallelAction(secondaryActions));
	}

	@Override
	public void prestart() {

	}
	
	@Override
	public String toString() {
		return "CrossExpelBack";
	}
}
