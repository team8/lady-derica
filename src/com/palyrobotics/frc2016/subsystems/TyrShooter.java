package com.palyrobotics.frc2016.subsystems;

import com.palyrobotics.frc2016.subsystems.controllers.ConstantVoltageController;
import com.palyrobotics.frc2016.subsystems.controllers.StrongHoldController;
import com.palyrobotics.frc2016.util.Dashboard;
import com.team254.lib.util.CheesySpeedController;
import com.team254.lib.util.Controller;
import com.team254.lib.util.Loop;
import com.team254.lib.util.StateHolder;
import com.team254.lib.util.Subsystem;

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
	// For raising and lowering the shooters at constant voltages
	static final double kLoweringVoltage = -0.1;
	static final double kRaisingVoltage = 0.5;
	// Shooter motor controller for holding position, or raising/lowering shooter
	Controller m_controller = null;
	
	// Used mainly for autonomous raising and lowering of the shooter
	public enum WantedShooterState {
		RAISED, LOWERED, NONE
	}
	public WantedShooterState mWantedState = WantedShooterState.NONE;
	
	@Override
	public void onStart() {
	}

	/**
	 * Runs the current controller if enabled and not null
	 */
	@Override
	public void onLoop() {
		if(m_controller instanceof StrongHoldController) {
			if(((StrongHoldController) m_controller).isEnabled()) {
				m_shooter_motor.set(((StrongHoldController) m_controller).update());
			}
		}
		else if(m_controller instanceof ConstantVoltageController) {
			//System.out.println("Shooter voltage: "+((ConstantVoltageController) m_controller).get());
			m_shooter_motor.set(((ConstantVoltageController) m_controller).get());
		}
	}

	@Override
	public void onStop() {
	}

	/**
	 * Resets the shooter
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
	 * Updates the shooter's motor output based on joystick input
	 * Called in teleop
	 * If there is an enabled controller that loop is executed in onLoop
	 * @see TyrShooter#onLoop()
	 */
	public void update(double joystickInput) {		
		// If no potentiometer available, directly use joystick input scaled down
		if(m_potentiometer == null) {
			m_shooter_motor.set(joystickInput*kJoystickScaleFactor);
			return;
		}
		
		// If joystick is within deadzone, then hold position using potentiometer
		if(joystickInput < kDeadzone) {
			holdPosition();
		} else {
			cancelHoldPosition();
			m_shooter_motor.set(joystickInput*kJoystickScaleFactor);
		}
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
		m_latch_solenoid.set(Value.kReverse);
	}
	
	/**
	 * Unlocks the shooter (latch) allowing it to fire
	 */
	public void unlock() {
		m_latch_solenoid.set(Value.kForward);
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
		mDashboard.getTable().putString("grabbermicrostate", "Grabbing");
		this.m_grabber_solenoid.set(Value.kForward);
	}
	
	/**
	 * Releases the grabber
	 */
	public void release() {
		mDashboard.getTable().putString("grabbermicrostate", "Raised");
		this.m_grabber_solenoid.set(Value.kReverse);
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
		// Default controller for holding position
		this.m_controller = new StrongHoldController(kP, kI, kD, kTolerance, m_potentiometer);
		// for safety
		((StrongHoldController) m_controller).setPositionSetpoint(m_potentiometer.get());
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