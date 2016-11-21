package com.palyrobotics.frc2016.subsystems;

import com.palyrobotics.frc2016.util.Subsystem;
import com.team254.lib.util.CheesySpeedController;
import com.team254.lib.util.Loop;
import com.team254.lib.util.StateHolder;

import edu.wpi.first.wpilibj.AnalogPotentiometer;

public class Breacher extends Subsystem implements Loop{

	private CheesySpeedController m_breacher_motor;
	private AnalogPotentiometer m_potentiometer;
	
	public Breacher(String name, CheesySpeedController motor) {
		super(name);
		m_breacher_motor = motor;
	}
	
	public Breacher(String name, CheesySpeedController motor, AnalogPotentiometer breacher_potentiometer) {
		super(name);
		m_breacher_motor = motor;
		m_potentiometer = breacher_potentiometer;
	}

	public void update(double joystickInput) {
		m_breacher_motor.set(joystickInput);
	}
	
	@Override
	public void getState(StateHolder states) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reloadConstants() {
		// TODO Auto-generated method stub
		
	}

}
