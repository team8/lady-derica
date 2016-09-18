package com.palyrobotics.frc2016.subsystems;

import com.palyrobotics.frc2016.HardwareAdaptor;
import com.palyrobotics.lib.util.CheesySpeedController;
import com.palyrobotics.lib.util.StateHolder;
import com.palyrobotics.lib.util.Subsystem;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class TyrShooter extends Subsystem {

	CheesySpeedController m_shooter_motor;
	DoubleSolenoid m_shooter_solenoid;
	DoubleSolenoid m_latch_solenoid;
	
	public TyrShooter(String name, CheesySpeedController shooter, DoubleSolenoid shootSolenoid,
			DoubleSolenoid latchSolenoid) {
		super(name);
		this.m_shooter_motor = shooter;
		this.m_shooter_solenoid = shootSolenoid;
		this.m_latch_solenoid = latchSolenoid;
	}
	
	public void teleopControlShooter(double d) {
		assert (d > 0);
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