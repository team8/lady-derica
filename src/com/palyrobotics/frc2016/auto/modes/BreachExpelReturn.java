package com.palyrobotics.frc2016.auto.modes;

import java.util.ArrayList;

import com.palyrobotics.frc2016.auto.AutoMode;
import com.palyrobotics.frc2016.auto.AutoModeEndedException;
import com.palyrobotics.frc2016.behavior.actions.Action;
import com.palyrobotics.frc2016.behavior.actions.DriveDistanceAction;
import com.palyrobotics.frc2016.behavior.actions.ExpelIntake;
import com.palyrobotics.frc2016.behavior.actions.ExpelShooterAction;
import com.palyrobotics.frc2016.behavior.actions.IntakeAction;
import com.palyrobotics.frc2016.behavior.actions.ParallelAction;
import com.palyrobotics.frc2016.behavior.actions.TurnAngleAutoAction;
import com.palyrobotics.frc2016.config.RobotState;
import com.palyrobotics.frc2016.robot.Robot;
import com.palyrobotics.frc2016.subsystems.Intake.WantedIntakeState;
import com.palyrobotics.frc2016.config.Constants;

/**
 * Crosses a B/D class defense, expels a ball, and returns over the midline
 *
 */
public class BreachExpelReturn extends AutoMode {
	
	public static final double mCompressorWaitTime = 3;
	private final boolean mUTurn;
	private final boolean mMidlineAccumulate = false;
	
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
		if(Robot.getRobotState().name == RobotState.RobotName.TYR) {
			runAction(new DriveDistanceAction(-Constants.kBreachDistance));
		} else {
			runAction(new DriveDistanceAction(Constants.kBreachDistance));
		}
		// expel the ball
		if(Robot.getRobotState().name == RobotState.RobotName.DERICA) {
			runAction(new ExpelShooterAction(Constants.kAutoShooterExpelTime));
		}
		runAction(new ExpelIntake(Constants.kAutoShooterExpelTime));
		// turn around and stop if Tyr
		if(Robot.getRobotState().name == RobotState.RobotName.TYR) {
			runAction(new TurnAngleAutoAction(180));
			runAction(new DriveDistanceAction(-Constants.kBreachDistance));
			return;
		}
		if (!this.mUTurn) {
			// drive back
			runAction(new DriveDistanceAction(-Constants.kBreachDistance));
			runAction(new TurnAngleAutoAction(180));
		} else {
			runAction(new TurnAngleAutoAction(180));
			runAction(new DriveDistanceAction(Constants.kBreachDistance)); 	
			
		}

		// drive towards midline ball and accumulate at the same time
		ArrayList<Action> secondaryActions = new ArrayList<Action>();
		secondaryActions.add(new DriveDistanceAction(Constants.kDistanceToDriveToAccumulateExtra));
		secondaryActions.add(new IntakeAction(1.0, WantedIntakeState.INTAKING));
		if(mMidlineAccumulate) {
			runAction(new ParallelAction(secondaryActions));
		}
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
