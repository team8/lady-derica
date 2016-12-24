package com.palyrobotics.frc2016.subsystems;

import com.palyrobotics.frc2016.config.Commands;
import com.palyrobotics.frc2016.config.RobotState;
import com.palyrobotics.frc2016.config.Constants;
import com.palyrobotics.frc2016.util.Subsystem;
import com.palyrobotics.frc2016.robot.team254.lib.util.Controller;
import com.palyrobotics.frc2016.util.SubsystemLoop;

/** 
 * Tyr: Stationary accumulator with 2 motors
 * Derica: Mobile intake on pivot with 1 motor and potentiometer
 * @author Nihar
 */
public class Intake extends Subsystem implements SubsystemLoop {
	private static Intake instance_ = new Intake();
	public static Intake getInstance() {
		return instance_;
	}
	// Stores output voltage
	private double[] output = new double[2];

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
	public void start() {
		// TODO Auto-generated method stub
	}

	/**
	 * TODO: this
	 */
	@Override
	public void update(Commands commands, RobotState robotState) {
		// Intake commands parsing
		if (commands.intakeRequest == Commands.IntakeRequest.INTAKE) {
			// Run intake inwards (positive speed is intake)
			output[0] = Constants.kManualIntakeSpeed;
			if(Constants.kRobotName == Constants.RobotName.TYR) {
				output[1] = Constants.kManualIntakeSpeed;
			}
		} else if (commands.intakeRequest == Commands.IntakeRequest.EXPEL) {
			// Run intake outwards (negative speed is exhaust)
			output[0] = Constants.kManualExhaustSpeed;
			if(Constants.kRobotName == Constants.RobotName.TYR) {
				output[1] = Constants.kManualExhaustSpeed;
			}
		} else {
			// Stop intake.
			output[0] = 0.0;
			output[1] = 0.0; // if Derica's arm, will be overridden next
		}
		if(Constants.kRobotName == Constants.RobotName.DERICA) {
			output[1] = -commands.operatorStickInput.y * kJoystickScaleFactor;
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
	}

	/**
	 * TODO: Document
	 */
	private Intake() {
		super("Intake");
	}
}
