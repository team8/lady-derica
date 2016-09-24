package com.palyrobotics.frc2016.subsystems;

import com.palyrobotics.lib.util.CheesySpeedController;
import com.palyrobotics.lib.util.StateHolder;
import com.palyrobotics.lib.util.Subsystem;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class TyrShooter extends Subsystem {

	CheesySpeedController m_shooter_motor;
	DoubleSolenoid m_shooter_solenoid;
	DoubleSolenoid m_latch_solenoid;
	
	public TyrShooter(String name, CheesySpeedController shooter_motor, DoubleSolenoid shooter_solenoid,
			DoubleSolenoid latch_solenoid) {
		super(name);
		this.m_shooter_motor = shooter_motor;
		this.m_shooter_solenoid = shooter_solenoid;
		this.m_latch_solenoid = latch_solenoid;
	}
	
	public void teleopControlShooter(double d) {
		m_shooter_motor.set(d);
	}
	
	public void lock() {
		m_latch_solenoid.set(Value.kForward);
	}

	public void unlock() {
		m_latch_solenoid.set(Value.kReverse);
	}
	
	public void extend() {
		this.m_shooter_solenoid.set(Value.kForward);
	}
	
	public void retract() {
		this.m_shooter_solenoid.set(Value.kReverse);
	}
	
	public void fire() {
		extend();
		unlock();
	}
	
	@Override
	public void getState(StateHolder states) {
		// TODO Auto-generated method stub
		states.put("m_shooter", this.m_shooter_motor.get());
	}

	@Override
	public void reloadConstants() {
		// TODO Auto-generated method stub
		
	}

}