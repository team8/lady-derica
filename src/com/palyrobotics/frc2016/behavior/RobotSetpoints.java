package com.palyrobotics.frc2016.behavior;

import java.util.Optional;

public class RobotSetpoints {

	public enum IntakeAction {
		NONE, OPEN, CLOSE, PREFER_OPEN, PREFER_CLOSE
	}
	
	public enum DriveRoutineAction {
		NONE, TIMER_DRIVE, ENCODER_DRIVE, ENCODER_TURN, GYRO_TURN, AUTO_ALIGN
	}

	public static final Optional<Double> m_nullopt = Optional.empty();

	// Actions for each subsystem
	public IntakeAction intake_action;
	public DriveRoutineAction drive_routine_action;

	// Desired setpoints
	public Optional<Double> auto_align_setpoint = m_nullopt;
	public Optional<Double> encoder_drive_setpoint = m_nullopt;
	public Optional<Double> timer_drive_time_setpoint = m_nullopt;
	public Optional<Double> drive_velocity_setpoint = m_nullopt;
	public Optional<Double> encoder_turn_setpoint_left = m_nullopt;
	public Optional<Double> encoder_turn_setpoint_right = m_nullopt;

	public void reset() {
		intake_action = IntakeAction.NONE;
		drive_routine_action = DriveRoutineAction.NONE;
		
		auto_align_setpoint = m_nullopt;
		encoder_drive_setpoint = m_nullopt;
		timer_drive_time_setpoint = m_nullopt;
		encoder_turn_setpoint_left = m_nullopt;
		encoder_turn_setpoint_right = m_nullopt;
	}
}
