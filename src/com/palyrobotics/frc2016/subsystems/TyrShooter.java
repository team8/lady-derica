package com.palyrobotics.frc2016.subsystems;

import com.palyrobotics.lib.util.CheesySpeedController;
import com.palyrobotics.lib.util.StateHolder;
import com.palyrobotics.lib.util.Subsystem;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

/**
 * Subsystem to control Tyr's Shooter
 * Shooter cycle is retract, lock, then extend, then unlock
 * @author Robbie, Nihar
 *
 */
public class TyrShooter extends Subsystem {

	CheesySpeedController m_shooter_motor;
	DoubleSolenoid m_shooter_solenoid;
	DoubleSolenoid m_latch_solenoid;
	DoubleSolenoid m_grabber_solenoid;
	
	/**
	 * Constructs a shooter with the grabber
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
	
	/**
	 * Moves the shooter motor
	 * @param d
	 */
	public void teleopControlShooter(double d) {
		m_shooter_motor.set(d);
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

}