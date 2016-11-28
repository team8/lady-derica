package com.palyrobotics.frc2016.subsystems;

import com.palyrobotics.frc2016.input.Commands;
import com.palyrobotics.frc2016.input.RobotState;
import com.palyrobotics.frc2016.robot.Robot;
import com.palyrobotics.frc2016.subsystems.controllers.ConstantVoltageController;
import com.palyrobotics.frc2016.subsystems.controllers.StrongHoldController;
import com.palyrobotics.frc2016.util.Constants;
import com.palyrobotics.frc2016.util.Subsystem;
import com.team254.lib.util.CheesySpeedController;
import com.team254.lib.util.Controller;
import com.team254.lib.util.Loop;
import com.team254.lib.util.StateHolder;

import edu.wpi.first.wpilibj.AnalogPotentiometer;

/** 
 * Tyr: Stationary accumulator with 2 motors
 * Derica: Mobile intake on pivot with 1 motor and potentiometer
 * @author Nihar
 */
public class Intake extends Subsystem implements Loop {
	RobotState.RobotName name;
	// Stores output voltage
	private double[] output = new double[2];
	// Used mainly for autonomous raising and lowering of the shooter
	public enum WantedIntakeState {
		INTAKING, EXPELLING, RAISING, LOWERING, NONE
	}
	public WantedIntakeState mWantedState = WantedIntakeState.NONE;
	// Constants for trying to hold intake up or down all the way
	private static final double kIntakeUpVoltage = 1;
	private static final double kIntakeDownVoltage = -1;

	Controller m_controller = null;
	// Tuning constants for StrongHoldController
	final double kDeadzone = 0.1; // Range to ignore joystick output and hold position instead
	final double kJoystickScaleFactor = 0.5; // Scale down joystick input for precision (if setting speed directly)
	final double kP = 0;
	final double kI = 0;
	final double kD = 0;
	final double kTolerance = 1; // Tolerance for the hold arm controller

	/**
	 * Get the current intake PWM signals.
	 * @return array with 2 doubles.
	 * For Tyr, first left PWM, then right PWM.
	 * For Derica, first intake PWM, then arm PWM
	 */
	public double[] get() {
		return output;
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Used for autonomous
	 * TODO: Not safe for Tyr vs Derica
	 * Directs the shooter to a desired position
	 */
	public void setWantedState(WantedIntakeState wantedState) {
		mWantedState = wantedState;
		switch(mWantedState) {
		case NONE:
			m_controller = null;
			output[0] = 0.0;
			output[1] = 0.0;
			break;
		case RAISING:
//			m_controller = new ConstantVoltageController(kIntakeUpVoltage);
			output[1] = kIntakeUpVoltage;
			break;
		case LOWERING:
//			m_controller = new ConstantVoltageController(kIntakeDownVoltage);
			output[1] = kIntakeDownVoltage;
			break;
		case INTAKING:
			output[0] = Constants.kManualIntakeSpeed;
			break;
		case EXPELLING:
			output[0] = Constants.kManualExhaustSpeed;
			break;
		default:
			break;
		}
	}
	
	/**
	 * TODO: this
	 * Runs control loop to position intake if applicable
	 */
	@Override
	/**
	 * Runs the intake during teleop
	 */
	public void update(Commands commands, RobotState robotState) {
		// Intake commands parsing
		if (commands.intakeRequest == Commands.IntakeRequest.INTAKE) {
			// Run intake inwards (positive speed is intake)
			output[0] = Constants.kManualIntakeSpeed;
			if(name == RobotState.RobotName.TYR) {
				output[1] = Constants.kManualIntakeSpeed;
			}
		} else if (commands.intakeRequest == Commands.IntakeRequest.EXHAUST) {
			// Run intake outwards (negative speed is exhaust)
			output[0] = Constants.kManualExhaustSpeed;
			if(name == RobotState.RobotName.TYR) {
				output[1] = Constants.kManualExhaustSpeed;
			}
		} else {
			// Stop intake.
			output[0] = 0.0;
			output[1] = 0.0; // if Derica's arm, will be overridden next
		}
		if(robotState.name == RobotState.RobotName.DERICA) {
			output[1] = -commands.operatorStickInput.y * kJoystickScaleFactor;
		}
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
	}

	/**
	 * TODO: Document
	 */
	public Intake(RobotState.RobotName robotName) {
		super("Intake");
		name = robotName;
	}
}
