package com.palyrobotics.frc2016.behavior.routines;

import java.util.Optional;

import com.palyrobotics.frc2016.HardwareAdaptor;
import com.palyrobotics.frc2016.behavior.Commands;
import com.palyrobotics.frc2016.behavior.RobotSetpoints;
import com.palyrobotics.frc2016.subsystems.Drive;
import com.palyrobotics.lib.util.DriveSignal;

import edu.wpi.first.wpilibj.Timer;

public class TimerDriveRoutine extends Routine {

	public enum TimerDriveRoutineStates {
		START, DRIVING, DONE
	}

	TimerDriveRoutineStates m_state = TimerDriveRoutineStates.START;
	Timer m_timer = new Timer();
	// Default values for time and velocity setpoints
	private double m_time_setpoint = 3;
	private double m_velocity_setpoint = 0.5;

	private Drive drive = HardwareAdaptor.kDrive;
	
	/**
	 * Sets the time setpoint that will be used
	 * @param time how long to drive forward in seconds
	 */
	public void setTimeSetpoint(double time) {
		this.m_time_setpoint = time;
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
		TimerDriveRoutineStates new_state = m_state;
		RobotSetpoints setpoints = existing_setpoints;
		switch (m_state) {
		case START:
			setpoints.timer_drive_time_setpoint = Optional.of(m_time_setpoint);
			setpoints.drive_routine_action = RobotSetpoints.DriveRoutineState.TIMER_DRIVE;
			setpoints.drive_velocity_setpoint = Optional.of(m_velocity_setpoint);
			m_timer.reset();
			m_timer.start();
			new_state = TimerDriveRoutineStates.DRIVING;
			break;
		case DRIVING:
			if(m_timer.get() > m_time_setpoint) {
				setpoints.timer_drive_time_setpoint = RobotSetpoints.m_nullopt;
				new_state = TimerDriveRoutineStates.DONE;
			}
			break;
		case DONE:
			setpoints.timer_drive_time_setpoint = RobotSetpoints.m_nullopt;
			setpoints.drive_velocity_setpoint = RobotSetpoints.m_nullopt;
			drive.reset();
			setpoints.drive_routine_action = RobotSetpoints.DriveRoutineState.NONE;
			break;
		}
		m_state = new_state;
		return setpoints;
	}

	@Override
	public void reset() {
		m_state = TimerDriveRoutineStates.DONE;
		m_timer.stop();
		m_timer.reset();
	}

	@Override
	public void cancel() {
		m_state = TimerDriveRoutineStates.DONE;
		m_timer.stop();
		drive.setOpenLoop(new DriveSignal(0, 0));
		drive.reset();
	}

	@Override
	public boolean isFinished() {
		return m_state == TimerDriveRoutineStates.DONE;
	}

	@Override
	public String getName() {
		return "Timer Drive Forward";
	}

}
