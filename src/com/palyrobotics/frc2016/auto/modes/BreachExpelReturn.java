package com.palyrobotics.frc2016.auto.modes;

import java.util.ArrayList;

import com.palyrobotics.frc2016.Constants;
import com.palyrobotics.frc2016.Robot;
import com.palyrobotics.frc2016.Robot.RobotName;
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
import com.palyrobotics.frc2016.subsystems.Intake.WantedIntakeState;

/**
 * Crosses a B/D class defense, expels a ball, and returns over the midline
 *
 */
public class BreachExpelReturn extends AutoMode {
	
	public static final double mCompressorWaitTime = 3;
	private final boolean mUTurn;
	
	/**
	 * Auto mode where the robot crosses, expels a boulder, than turns around 
	 * to try and accumulate again
	 * @param uTurn: if this is true the robot will do the 180 degree
	 * turn before returning. Otherwise, it will do it after coming back
	 */
	public BreachExpelReturn(boolean uTurn) {
		this.mUTurn = uTurn;
	}
	
	@Override
	protected void routine() throws AutoModeEndedException {
		// breach
		if(Robot.name == RobotName.TYR) {
			runAction(new DriveDistanceAction(-Constants.kBreachDistance));
		} else {
			runAction(new DriveDistanceAction(Constants.kBreachDistance));
		}
		// expel the ball
		if(Robot.name == RobotName.DERICA) {
			runAction(new ExpelShooterAction(Constants.kAutoShooterExpelTime));
		}
		runAction(new ExpelIntake(Constants.kAutoShooterExpelTime));
		// turn around and stop if Tyr
		if(Robot.name == RobotName.TYR) {
			runAction(new TurnAngleAutoAction(180));
			runAction(new DriveDistanceAction(-Constants.kBreachDistance));
			return;
		}
		if (this.mUTurn) {
			// drive back
			runAction(new DriveDistanceAction(Constants.kBreachDistance)); 	
		} else {
			runAction(new DriveDistanceAction(-Constants.kBreachDistance)); 	
			runAction(new TurnAngleAutoAction(180));
		}

		// drive towards midline ball and accumulate at the same time
		ArrayList<Action> secondaryActions = new ArrayList<Action>();
		secondaryActions.add(new DriveDistanceAction(Constants.kDistanceToDriveToAccumulateExtra));
		secondaryActions.add(new IntakeAction(1.0, WantedIntakeState.INTAKING));
		
		runAction(new ParallelAction(secondaryActions));
	}

	@Override
	public void prestart() {
		System.out.println("Starting AutoMode: "+toString());
	}
	
	@Override
	public String toString() {
		return "BreachExpelReturn";
	}
}
