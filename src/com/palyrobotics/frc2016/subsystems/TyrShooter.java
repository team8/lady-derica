package com.palyrobotics.frc2016.subsystems;

import com.palyrobotics.frc2016.input.Commands;
import com.palyrobotics.frc2016.input.RobotState;
import com.palyrobotics.frc2016.subsystems.controllers.ConstantVoltageController;
import com.palyrobotics.frc2016.subsystems.controllers.StrongHoldController;
import com.palyrobotics.frc2016.util.Dashboard;
import com.palyrobotics.frc2016.util.Subsystem;
import com.team254.lib.util.CheesySpeedController;
import com.team254.lib.util.Controller;
import com.team254.lib.util.Loop;
import com.team254.lib.util.StateHolder;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

/**
 * Subsystem to control Tyr's Shooter
 * Shooter cycle is retract, lock, then extend, then unlock
 * @author Robbie, Nihar
 *
 */
public class TyrShooter extends Subsystem implements Loop {
	// Store motor output and solenoid output
	private double motorOutput = 0.0;
	private DoubleSolenoid.Value[] solenoidOutput = new DoubleSolenoid.Value[3];
	private DoubleSolenoid.Value latchOutput;
	private DoubleSolenoid.Value pistonOutput;
	private DoubleSolenoid.Value grabberOutput;

	// Tuning constants for hold in place controller
	final double kDeadzone = 0.1; // Range to ignore joystick output and hold position instead
	final double kJoystickScaleFactor = 0.5; // Scale down joystick input for precision (if setting speed directly)
	final double kP = 0;
	final double kI = 0;
	final double kD = 0;
	final double kTolerance = 1; // Tolerance for the hold arm controller
	// For raising and lowering the shooters at constant voltages
	private static final double kLoweringVoltage = -0.1;
	private static final double kRaisingVoltage = 0.5;
	// Shooter motor controller for holding position, or raising/lowering shooter
	private Controller m_controller = null;
	
	// Used mainly for autonomous raising and lowering of the shooter
	public enum WantedShooterState {
		RAISED, LOWERED, NONE
	}
	public WantedShooterState mWantedState = WantedShooterState.NONE;



	/**
	 * Returns the PWM signal for the shooter motor
	 */
	public double getMotorOutput() {
		return motorOutput;
	}
	/**
	 * Returns the solenoid values for the shooter (latch and grabber)
	 * 0 is latch
	 * 1 is piston
	 * 2 is grabber
	 */
	public DoubleSolenoid.Value[] getSolenoidOutput() {
		return solenoidOutput;
	}

	@Override
	public void onStart() {
	}

	/**
	 * TODO: lol
	 */
	@Override
	public void update(Commands commands, RobotState robotState) {
		if (commands.latch_request == Commands.LatchRequest.LOCK) {
			lock();
		} else if (commands.latch_request == Commands.LatchRequest.UNLOCK) {
			unlock();
		}
		if (commands.grabber_request == Commands.GrabberRequest.GRAB) {
			grab();
		} else if (commands.grabber_request == Commands.GrabberRequest.RELEASE) {
			release();
		}
		if (commands.shooter_request == Commands.ShooterRequest.EXTEND) {
			extend();
		} else if (commands.shooter_request == Commands.ShooterRequest.RETRACT) {
			retract();
		}
		motorOutput = commands.operatorStickInput.leftY *kJoystickScaleFactor;
	}

	@Override
	public void onStop() {
	}

	/**
	 * Resets the shooter's controller
	 * Should be done in teleop init to clear out autonomous
	 */
	public void reset() {
		// Reset controller
		if(m_controller instanceof ConstantVoltageController) {
			m_controller = new StrongHoldController(kP, kI, kD, kTolerance, m_potentiometer);
			((StrongHoldController) m_controller).disable();
		}
		mWantedState = WantedShooterState.NONE;
	}
	
	/**
	 * Used for autonomous
	 * Directs the shooter to a desired position
	 */
	public void setWantedState(WantedShooterState wantedState) {
		mWantedState = wantedState;
		switch(mWantedState) {
		case NONE:
			if(m_controller instanceof ConstantVoltageController) {
				m_controller = null;
			}
			break;
		case RAISED:
			m_controller = new ConstantVoltageController(kRaisingVoltage);
			break;
		case LOWERED:
			m_controller = new ConstantVoltageController(kLoweringVoltage);
			break;
		}
	}
	
	/**
	 * Tells the shooter to hold position at the target angle
	 */
	public void holdPosition() {
		// Continue using current hold postion controller if possible
		if(m_controller instanceof StrongHoldController) {
			((StrongHoldController) m_controller).enable();
			((StrongHoldController) m_controller).setPositionSetpoint(m_potentiometer.get());			
			return;
		} 
		// No potentiometer -> no shooter hold
		else if(m_potentiometer == null) {
			System.err.println("No shooter controller!");			
		} 
		// Start new hold position controller
		else {
			m_controller = new StrongHoldController(kP, kI, kD, kTolerance, m_potentiometer);
			((StrongHoldController) m_controller).enable();
			((StrongHoldController) m_controller).setPositionSetpoint(m_potentiometer.get());			
		}
	}
	
	/**
	 * Cancels the hold position controller
	 */
	public void cancelHoldPosition() {
		if(m_controller instanceof StrongHoldController) {
			((StrongHoldController) m_controller).disable();
			return;
		}
		else System.err.println("No shooter controller!");
	}
	
	/**
	 * Locks the shooter (latch triggered)
	 */
	public void lock() {
		latchOutput = Value.kReverse;
	}
	
	/**
	 * Unlocks the shooter (latch) allowing it to fire
	 */
	public void unlock() {
		latchOutput = Value.kForward;
	}
	
	/**
	 * Extends the piston solenoid (frees the spring)
	 */
	public void extend() {
		pistonOutput = Value.kForward;
	}
	
	/**
	 * Retracts the shooter solenoid (spring starts charging)
	 */
	public void retract() {
		pistonOutput = Value.kReverse;
	}
	
	/**
	 * Toggles grabber down
	 */
	public void grab() {
		mDashboard.getTable().putString("grabbermicrostate", "Grabbing");
		grabberOutput = Value.kForward;
	}
	
	/**
	 * Releases the grabber
	 */
	public void release() {
		mDashboard.getTable().putString("grabbermicrostate", "Raised");
		grabberOutput = Value.kReverse;
	}

	/**
	 * TODO:
	 */
	public TyrShooter() {
		super("TyrShooter");
		solenoidOutput[0] = latchOutput;
		solenoidOutput[1] = pistonOutput;
		solenoidOutput[2] = grabberOutput;
	}
}