package com.palyrobotics.frc2016.subsystems;

import com.team254.lib.util.CheesySpeedController;
import com.team254.lib.util.StateHolder;
import com.team254.lib.util.Subsystem;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class DericaShooter extends Subsystem {
	
	CheesySpeedController winch;
	DoubleSolenoid lock;

	public DericaShooter(String name) {
		super(name);
	}
	
	public DericaShooter(String name, CheesySpeedController winch, DoubleSolenoid lock) {
		super(name);
		this.winch = winch;
		this.lock = lock;
	}
	
	public void lock() {
		lock.set(Value.kForward);
	}
	
	public void unlock() {
		lock.set(Value.kReverse);
	}
	
	public void wind(double d) {
		winch.set(d);
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
