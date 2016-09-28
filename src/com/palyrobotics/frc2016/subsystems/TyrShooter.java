package com.palyrobotics.frc2016.subsystems;

import com.palyrobotics.frc2016.subsystems.controllers.StrongHoldController;
import com.palyrobotics.lib.util.CheesySpeedController;
import com.palyrobotics.lib.util.StateHolder;
import com.palyrobotics.lib.util.Subsystem;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

/**
 * Subsystem to control Tyr's Shooter
 * Shooter cycle is retract, lock, then extend, then unlock
 * @author Robbie, Nihar
 *
 */
public class TyrShooter extends Subsystem {
	// Hardware components
	CheesySpeedController m_shooter_motor;
	DoubleSolenoid m_shooter_solenoid;
	DoubleSolenoid m_latch_solenoid;
	DoubleSolenoid m_grabber_solenoid;
	// Optional, if null, then code will not attempt to hold position
	AnalogPotentiometer m_potentiometer = null;
	
	// Tuning constants
	final double kDeadzone = 0.1; // Range to ignore joystick output and hold position instead
	final double kJoystickScaleFactor = 0.5; // Scale down joystick input for precision (if setting speed directly)
	final double kP = 0;
	final double kI = 0;
	final double kD = 0;
	final double kTolerance = 1; // Tolerance for the hold arm controller
	// Shooter motor controller for holding position, null if no potentiometer available
	StrongHoldController m_controller = null;
	
	/**
	 * Updates the shooter's motor output based on joystick input
	 */
	public void update(double joystickInput) {
		// If no potentiometer available, directly use joystick input scaled down
		if(m_controller == null) {
			m_shooter_motor.set(joystickInput*kJoystickScaleFactor);
			return;
		}
		
		// If joystick is within deadzone, then hold position using potentiometer
		if(joystickInput < kDeadzone) {
			if(m_controller.isEnabled()) {
				m_shooter_motor.set(m_controller.update());
			}
			else {
				holdPosition();
				m_shooter_motor.set(m_controller.update());
			}
		} else {
			cancelHoldPosition();
			m_shooter_motor.set(joystickInput*kJoystickScaleFactor);
		}
	}
	
	/**
	 * Tells the shooter to hold position at the target angle
	 */
	public void holdPosition() {
		if(m_controller == null) {
			System.err.println("No shooter controller!");
			return;
		}
		m_controller.enable();
		m_controller.setPositionSetpoint(m_potentiometer.get());
	}
	
	/**
	 * 
	 */
	public void cancelHoldPosition() {
		if(m_controller == null) {
			System.err.println("No shooter controller!");
			return;
		}
		m_controller.disable();
	}
	
	/**
	 * Locks the shooter (latch triggered)
	 */
	public void lock() {
		m_latch_solenoid.set(Value.kForward);
	}
	
	/**
	 * Unlocks the shooter (latch) allowing it to fire
	 */
	public void unlock() {
		m_latch_solenoid.set(Value.kReverse);
	}
	
	/**
	 * Extends the shooter solenoid (frees the spring)
	 */
	public void extend() {
		this.m_shooter_solenoid.set(Value.kForward);
	}
	
	/**
	 * Retracts the shooter solenoid (spring starts charging)
	 */
	public void retract() {
		this.m_shooter_solenoid.set(Value.kReverse);
	}
	
	/**
	 * Toggles grabber down
	 */
	public void grab() {
		this.m_grabber_solenoid.set(Value.kForward);
	}
	
	/**
	 * Releases the grabber
	 */
	public void release() {
		this.m_grabber_solenoid.set(Value.kReverse);
	}
	
	/**
	 * Fires at once, by extending the shooter solenoid (freed spring)
	 * and then immediately unlocking the latch
	 */
	public void fire() {
		extend();
		unlock();
	}
	
	@Override
	public void getState(StateHolder states) {
		states.put("m_shooter", this.m_shooter_motor.get());
	}

	@Override
	public void reloadConstants() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Constructs a shooter and grabber with a potentiometer
	 * This enables usage of holdPosition
	 * @param name should be "shooter"
	 * @param shooter_motor motor that aims the shooter up/down
	 * @param shooter_solenoid solenoid that pulls the spring
	 * @param shooter_potentiometer potentiometer on the shooter motor
	 * @param latch_solenoid solenoid that locks the shooter in place
	 * @param grabber_solenoid solenoid that controls the grabber
	 */
	public TyrShooter(String name, CheesySpeedController shooter_motor, DoubleSolenoid shooter_solenoid,
			DoubleSolenoid latch_solenoid, DoubleSolenoid grabber_solenoid, AnalogPotentiometer shooter_potentiometer) {
		super(name);
		this.m_shooter_motor = shooter_motor;
		this.m_shooter_solenoid = shooter_solenoid;
		this.m_latch_solenoid = latch_solenoid;
		this.m_grabber_solenoid = grabber_solenoid;
		this.m_potentiometer = shooter_potentiometer;
		this.m_controller = new StrongHoldController(kP, kI, kD, kTolerance, m_potentiometer);
		// for safety
		m_controller.setPositionSetpoint(m_potentiometer.get());
	}
	
	/**
	 * Constructs a shooter and grabber without a potentiometer
	 * This disables usage of holdPosition
	 * @param name should be "shooter"
	 * @param shooter_motor motor that aims the shooter up/down
	 * @param shooter_solenoid solenoid that pulls the spring
	 * @param latch_solenoid solenoid that locks the shooter in place
	 * @param grabber_solenoid solenoid that controls the grabber
	 */
	public TyrShooter(String name, CheesySpeedController shooter_motor, DoubleSolenoid shooter_solenoid,
			DoubleSolenoid latch_solenoid, DoubleSolenoid grabber_solenoid) {
		super(name);
		this.m_shooter_motor = shooter_motor;
		this.m_shooter_solenoid = shooter_solenoid;
		this.m_latch_solenoid = latch_solenoid;
		this.m_grabber_solenoid = grabber_solenoid;
	}
}