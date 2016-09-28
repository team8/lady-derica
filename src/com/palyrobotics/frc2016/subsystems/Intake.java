package com.palyrobotics.frc2016.subsystems;

import com.palyrobotics.frc2016.Robot;
import com.palyrobotics.frc2016.subsystems.controllers.StrongHoldController;
import com.palyrobotics.lib.util.CheesySpeedController;
import com.palyrobotics.lib.util.StateHolder;
import com.palyrobotics.lib.util.Subsystem;

import edu.wpi.first.wpilibj.AnalogPotentiometer;

/** 
 * Tyr: Stationary accumulator with 2 motors
 * Derica: Mobile intake on pivot with 1 motor and potentiometer
 * @author Nihar
 */
public class Intake extends Subsystem {
	// One of the following will be null depending on the robot
	CheesySpeedController m_left_motor = null;
	CheesySpeedController m_right_motor = null;
	CheesySpeedController m_arm_motor = null;
	
	// Potentiometer may exist for Derica intake's arm, if null, disables holding position
	AnalogPotentiometer m_arm_potentiometer = null;
	StrongHoldController m_controller = null;
	
	// Tuning constants
	final double kDeadzone = 0.1; // Range to ignore joystick output and hold position instead
	final double kJoystickScaleFactor = 0.5; // Scale down joystick input for precision (if setting speed directly)
	final double kP = 0;
	final double kI = 0;
	final double kD = 0;
	final double kTolerance = 1; // Tolerance for the hold arm controller
	
	
	/**
	 * Tyr - pass left then right motors
	 * Derica - pass intake motor then arm motor
	 * Pass potentiometer if there is one, else pass null
	 * @param name name of subsystem
	 * @param motor1 Left motor, or intake motor
	 * @param motor2 Right motor, or arm motor
	 * @param arm_potentiometer Set null if none, otherwise Derica's arm potentiometer 
	 */
	public Intake(String name, CheesySpeedController motor1, 
			CheesySpeedController motor2, AnalogPotentiometer armPotentiometer) {
		super(name);
		switch(Robot.name) {
		case TYR:
			m_left_motor = motor1;
			m_right_motor = motor2;
			m_arm_motor = null;
			break;
		case DERICA:
			// switch case falls through
		default:
			m_left_motor = motor1;
			m_right_motor = null;
			m_arm_motor = motor2;
			m_arm_potentiometer = armPotentiometer;
			break;
		}
		// If no potentiometer, set the controller to null
		if(m_arm_potentiometer == null) {
			m_controller = null;
		} else {
			m_controller = new StrongHoldController(kP, kI, kD, kTolerance, m_arm_potentiometer);
		}
	}
	
	/**
	 * Set intake to a single speed (both motors if Tyr)
	 * Positive is to intake, negative is to exhaust
	 * @param speed target speed (negative is exhaust)
	 */
	public void setSpeed(double speed) {
		setLeftRight(speed, speed);
	}
	
	/**
	 * Positive to intake, negative to exhaust
	 * @param left_speed
	 * @param right_speed N/A for Derica
	 */
	public void setLeftRight(double left_speed, double right_speed) {
		if(m_right_motor != null) {
			m_left_motor.set(left_speed);
			m_right_motor.set(-right_speed);
		} else {
			m_left_motor.set(-left_speed);
		}
	}
	
	/**
	 * Moves the arm, if we are Derica
	 * Positive will move arm up
	 * Negative will move arm down
	 * Holds position if the joystick input is within a deadzone
	 * @param joystickInput should be directly passed from the stick controlling this
	 */
	public void update(double joystickInput) {
		if(m_arm_motor == null) {
			System.err.println("Trying to move arm on Tyr!");
			return;
		} else if(m_controller == null) {
			m_arm_motor.set(-joystickInput*kJoystickScaleFactor);
		} else {
			if(joystickInput < kDeadzone) {
				// If already holding position use that
				if(m_controller.isEnabled()) {
					m_arm_motor.set(m_controller.update());
				} else {
					m_controller.setPositionSetpoint(m_arm_potentiometer.get());
					m_arm_motor.set(m_controller.update());
				}
			} else {
				m_controller.disable();
				m_arm_motor.set(-joystickInput*kJoystickScaleFactor);
			}
		}
	}

	@Override
	public void reloadConstants() {
	}

	@Override
	public void getState(StateHolder states) {
	}
}
