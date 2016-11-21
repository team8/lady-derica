package com.palyrobotics.frc2016.subsystems;

import com.palyrobotics.frc2016.util.Subsystem;
import com.team254.lib.util.CheesySpeedController;
import com.team254.lib.util.StateHolder;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Encoder;

public class DericaShooter extends Subsystem {
	
	CheesySpeedController m_winch_motor;
	DoubleSolenoid m_lock_solenoid;
	
	Encoder m_winch_encoder;
	DigitalInput m_limit_switch, m_hall_effect;
	
	public DericaShooter(String name, CheesySpeedController winch, DoubleSolenoid lock,
			Encoder winchEncoder, DigitalInput limitSwitch, DigitalInput hallEffect) {
		super(name);
		this.m_winch_motor = winch;
		this.m_lock_solenoid = lock;
		this.m_winch_encoder = winchEncoder;
		this.m_limit_switch = limitSwitch;
		this.m_hall_effect = hallEffect;
	}
	
	// Hold the catapult in place
	public void lock() {
		m_lock_solenoid.set(Value.kForward);
	}
	
	// Release the catapult
	public void unlock() {
		m_lock_solenoid.set(Value.kReverse);
	}
	
	// Tighten the elastics
	public void wind(double d) {
		m_winch_motor.set(d);
	}
	
	// Loosen the elastics
	public void unwind(double d) {
		m_winch_motor.set(-d);
	}
	
	// Read from the encoder on the winch
	public double readEncoder() {
		return m_winch_encoder.get();
	}
	
	// Is the catapult all the way down?
	public boolean catapultDown() {
		return !m_hall_effect.get();
	}
	
	// Does the catapult contain a boulder?
	public boolean hasBoulder() {
		return !m_limit_switch.get();
	}
	
	@Override
	public void getState(StateHolder states) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reloadConstants() {
		// TODO Auto-generated method stub

	}

}
