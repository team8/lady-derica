package com.palyrobotics.frc2016.subsystems;

import com.palyrobotics.frc2016.input.Commands;
import com.palyrobotics.frc2016.input.RobotState;
import com.palyrobotics.frc2016.util.Subsystem;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class Catapult extends Subsystem {
	// Stores output
	private double winchMotorOutput = 0.0;
	private DoubleSolenoid.Value lockSolenoidOutput;

	public Catapult() {
		super("Catapult");
	}

	/**
	 * TODO: this method and documentation
	 * @param commands
	 * @param robotState
	 */
	@Override
	public void update(Commands commands, RobotState robotState) {

	}

	// Hold the catapult in place
	public void lock() {
		lockSolenoidOutput = Value.kForward;
	}
	
	// Release the catapult
	public void unlock() {
		lockSolenoidOutput = Value.kReverse;
	}
	
	// Tighten the elastics
	public void wind(double d) {
		winchMotorOutput = d;
	}
	
	// Loosen the elastics
	public void unwind(double d) {
		winchMotorOutput = -d;
	}
}