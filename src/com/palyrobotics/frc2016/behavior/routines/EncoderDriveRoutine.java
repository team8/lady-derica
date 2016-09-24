package com.palyrobotics.frc2016.behavior.routines;

import java.util.Optional;

import com.palyrobotics.frc2016.HardwareAdaptor;
import com.palyrobotics.frc2016.behavior.Commands;
import com.palyrobotics.frc2016.behavior.RobotSetpoints;
import com.palyrobotics.frc2016.subsystems.Drive;
import com.palyrobotics.lib.util.DriveSignal;

import edu.wpi.first.wpilibj.Timer;

/**
 * Drives forward a specified distance
 * Uses right encoder to determine if distance is reached
 * Times out after specified seconds, default m_default_timeout
 * @author Nihar
 */
public class EncoderDriveRoutine extends Routine {
	/*
	 * START = Set new drive setpoint
	 * DRIVING = Waiting to reach drive setpoint
	 * DONE = reached target or not operating
	 */
	public enum EncoderDriveRoutineStates {
		START, DRIVING, DONE, IDLE
	}

	EncoderDriveRoutineStates m_state = EncoderDriveRoutineStates.IDLE;
	private double m_distance;
	private double m_velocity_setpoint;
	
	// Timeout after x seconds
	private double m_timeout;
	private final double m_default_timeout = 5;
	Timer m_timer = new Timer();

	private Drive drive = HardwareAdaptor.kDrive;
	
	/**
	 * Constructs with target distance
	 * Uses default timeout
	 * @param distance Target distance to travel
	 */
	public EncoderDriveRoutine(double distance) {
		this.m_distance = distance;
		this.m_timeout = m_default_timeout;
	}
	
	/**
	 * Constructs with specified timeout
	 * @param distance Target distance to travel
	 * @param timeout Time (seconds) before timeout
	 */
	public EncoderDriveRoutine(double distance, int timeout) {
		this.m_distance = distance;
		this.m_timeout = timeout;
	}
	
	/**
	 * 
	 * @param distance Target distance to travel
	 * @param timeout Time (seconds) before timeout
	 * @param velocity Target velocity
	 */
	public EncoderDriveRoutine(double distance, double timeout, double velocity) {
		this.m_distance = distance;
		this.m_timeout = timeout;
		setVelocity(velocity);
	}
	
	/**
	 * Sets the velocity setpoint
	 * @param velocity target velocity to drive at (0 to 1)
	 * @return true if valid setspeed
	 */
	public boolean setVelocity(double velocity) {
		if(velocity > 0) {
			this.m_velocity_setpoint = velocity;
			return true;
		}
		return false;
	}

	//Routines just change the states of the robotsetpoints, which the behavior manager then moves the physical subsystems based on.
	@Override
	public RobotSetpoints update(Commands commands, RobotSetpoints existing_setpoints) {
		EncoderDriveRoutineStates new_state = m_state;
		RobotSetpoints setpoints = existing_setpoints;
		System.out.println("Encoder Drive Update + " + m_state);
		switch (m_state) {
		case START:
			m_timer.reset();
			m_timer.start();
			setpoints.encoder_drive_setpoint = Optional.of(m_distance);
			setpoints.drive_velocity_setpoint = Optional.of(m_velocity_setpoint);

			setpoints.drive_routine_action = RobotSetpoints.DriveRoutineState.ENCODER_DRIVE;
			new_state = EncoderDriveRoutineStates.DRIVING;
			break;
		case DRIVING:
			if(drive.m_right_encoder.getDistance() > m_distance) {
				new_state = EncoderDriveRoutineStates.DONE;
			}
			if(m_timer.get() > m_timeout) {
				new_state = EncoderDriveRoutineStates.DONE;
				System.out.println("Encoder drive dist timed out!");
			}
			break;
		case DONE:
			drive.reset();
			setpoints.drive_routine_action = RobotSetpoints.DriveRoutineState.NONE;
			setpoints.drive_velocity_setpoint = RobotSetpoints.m_nullopt;
			setpoints.encoder_drive_setpoint = RobotSetpoints.m_nullopt;
			new_state = EncoderDriveRoutineStates.IDLE;
			break;
		case IDLE:
			// Do nothing, don't interfere with other routines
			break;
		}
		m_state = new_state;
		return setpoints;
	}

	@Override
	public void cancel() {
		System.out.println("Encoder drive routine cancel");
		m_state = EncoderDriveRoutineStates.DONE;
		m_timer.stop();
		m_timer.reset();
		drive.setOpenLoop(DriveSignal.NEUTRAL);
		drive.reset();
	}

	@Override
	public void reset() {
		System.out.println("Encoder drive routine reset");
		m_state = EncoderDriveRoutineStates.DONE;
		drive.reset();
		m_timer.reset();
	}

	@Override
	public boolean isFinished() {
		return m_state == EncoderDriveRoutineStates.IDLE;
	}

	@Override
	public String getName() {
		return "Encoder Drive Forward";
	}

}
