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
		default:
			m_left_motor = motor1;
			m_right_motor = null;
			m_arm_motor = motor2;
			break;
		}
	}

	public void setSpeed(double speed) {
		switch(Robot.name) {
		case TYR:
			m_left_motor.set(-speed);
			break;
		case DERICA:
		default:
			setLeftRight(speed, speed);			
		}
	}

	public void setLeftRight(double left_speed, double right_speed) {
		//TODO Is there a better practice than null checking, is robot name checking better?
		if(m_right_motor != null) {
			m_left_motor.set(-left_speed);
			m_right_motor.set(-right_speed);
		} else {
			m_left_motor.set(-left_speed);
			System.out.println("No right motor");
		}
	}

	@Override
	public void reloadConstants() {
	}

	@Override
	public void getState(StateHolder states) {
	}
}
