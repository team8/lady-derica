package com.palyrobotics.frc2016.subsystems;

import com.palyrobotics.frc2016.robot.Robot;
import com.palyrobotics.frc2016.subsystems.TyrShooter.WantedShooterState;
import com.palyrobotics.frc2016.subsystems.controllers.ConstantVoltageController;
import com.palyrobotics.frc2016.subsystems.controllers.StrongHoldController;
import com.palyrobotics.frc2016.util.Constants;
import com.team254.lib.util.CheesySpeedController;
import com.team254.lib.util.Controller;
import com.team254.lib.util.Loop;
import com.team254.lib.util.StateHolder;
import com.team254.lib.util.Subsystem;

import edu.wpi.first.wpilibj.AnalogPotentiometer;

/** 
 * Tyr: Stationary accumulator with 2 motors
 * Derica: Mobile intake on pivot with 1 motor and potentiometer
 * @author Nihar
 */
public class Intake extends Subsystem implements Loop {
	// Used mainly for autonomous raising and lowering of the shooter
	public enum WantedIntakeState {
		INTAKING, EXPELLING, RAISING, LOWERING, NONE
	}
	public WantedIntakeState mWantedState = WantedIntakeState.NONE;
	
	static final double kIntakeUpVoltage = 1;
	static final double kIntakeDownVoltage = -1;
	
	// One of the following will be null depending on the robot
	CheesySpeedController m_left_motor = null;
	CheesySpeedController m_right_motor = null;
	CheesySpeedController m_arm_motor = null;
	
	// Potentiometer may exist for Derica intake's arm, if null, disables holding position
	AnalogPotentiometer m_arm_potentiometer = null;
	Controller m_controller = null;
	
	// Tuning constants
	final double kDeadzone = 0.1; // Range to ignore joystick output and hold position instead
	final double kJoystickScaleFactor = 0.5; // Scale down joystick input for precision (if setting speed directly)
	final double kP = 0;
	final double kI = 0;
	final double kD = 0;
	final double kTolerance = 1; // Tolerance for the hold arm controller
	
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
	 * If the joystick input is within a deadzone, hands off
	 * to a control loop to hold position
	 * @param joystickInput should be directly passed from the stick controlling this
	 * @see Intake.onLoop()
	 */
	public void update(double joystickInput) {
		if(m_arm_motor == null) {
			System.err.println("Trying to move arm on Tyr!");
			return;
		} else if(m_controller == null) {
			setArmSpeed(-joystickInput*kJoystickScaleFactor);
		} else {
			if(joystickInput < kDeadzone) {
				// If already holding position use that
				if(!(m_controller instanceof StrongHoldController) && m_arm_potentiometer != null) {
					m_controller = new StrongHoldController(kP, kI, kD, kTolerance, m_arm_potentiometer);
					((StrongHoldController)m_controller).setPositionSetpoint(m_arm_potentiometer.get());
				}
			} else {
				m_controller = null;
				setArmSpeed(-joystickInput*kJoystickScaleFactor);
			}
		}
	}
	
	public void setArmSpeed(double speed) {
		m_arm_motor.set(speed);
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Used for autonomous
	 * Directs the shooter to a desired position
	 */
	public void setWantedState(WantedIntakeState wantedState) {
		mWantedState = wantedState;
		switch(mWantedState) {
		case NONE:
			if(m_controller instanceof ConstantVoltageController) {
				m_controller = null;
			}
			break;
		case RAISING:
//			m_controller = new ConstantVoltageController(kIntakeUpVoltage);
			m_arm_motor.set(kIntakeUpVoltage);
			break;
		case LOWERING:
//			m_controller = new ConstantVoltageController(kIntakeDownVoltage);
			m_arm_motor.set(kIntakeDownVoltage);
			break;
		case INTAKING:
			setSpeed(Constants.kManualIntakeSpeed);
			break;
		case EXPELLING:
			setSpeed(Constants.kManualExhaustSpeed);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Runs control loop to position intake if applicable
	 */
	@Override
	public void onLoop() {
		if(m_controller instanceof StrongHoldController) {
			if(((StrongHoldController) m_controller).isEnabled()) {
				m_arm_motor.set(((StrongHoldController) m_controller).update());
			}
		} else if(m_controller instanceof ConstantVoltageController) {
			//System.out.println("Shooter voltage: "+((ConstantVoltageController) m_controller).get());
			setSpeed(((ConstantVoltageController) m_controller).get());
		}
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		
	}

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
		switch(Robot.getRobotState().name) {
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
	
	@Override
	public void reloadConstants() {
	}

	@Override
	public void getState(StateHolder states) {
	}
}
