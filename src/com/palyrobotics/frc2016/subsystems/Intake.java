package com.palyrobotics.frc2016.subsystems;

import com.palyrobotics.frc2016.Robot;
import com.palyrobotics.lib.util.CheesySpeedController;
import com.palyrobotics.lib.util.StateHolder;
import com.palyrobotics.lib.util.Subsystem;

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
	
	/**
	 * Tyr - pass left then right motors
	 * Derica - pass intake motor then arm motor
	 * @param name name of subsystem
	 * @param motor1 Left motor, or intake motor
	 * @param motor2 Right motor, or arm motor
	 */
	public Intake(String name, CheesySpeedController motor1, 
			CheesySpeedController motor2) {
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
			break;
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
	 * @param speed
	 */
	public void setArmSpeed(double speed) {
		if(m_arm_motor != null) {
			m_arm_motor.set(-speed);
		} else {
			System.err.println("Trying to move arm on Tyr!");
		}
	}

	@Override
	public void reloadConstants() {
	}

	@Override
	public void getState(StateHolder states) {
	}
}
