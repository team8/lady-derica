package com.palyrobotics.frc2016.behavior;

import com.palyrobotics.frc2016.behavior.routines.*;
import com.palyrobotics.frc2016.input.Commands;
import com.palyrobotics.frc2016.input.RobotState;
import com.palyrobotics.frc2016.robot.HardwareAdaptor;
import com.palyrobotics.frc2016.robot.Robot;
import com.palyrobotics.frc2016.subsystems.*;
import com.palyrobotics.frc2016.subsystems.LowGoalShooter.WantedLowGoalState;
import com.palyrobotics.frc2016.util.Constants;
import com.team254.lib.util.DriveSignal;
import com.team254.lib.util.StateHolder;
import com.team254.lib.util.Tappable;

public class RoutineManager implements Tappable {

	public boolean isZero(double val) {
		return val == 0 || (val < 0.001 && val > -0.001);
	}

	private Routine m_cur_routine = null;
	private Commands.Setpoints m_setpoints;
	//    private ManualRoutine m_manual_routine = new ManualRoutine();
	
	private void setNewRoutine(Routine new_routine) {
		// Cancel if new routine diff from current routine
		boolean needs_cancel = (new_routine != m_cur_routine) && (m_cur_routine != null);

		boolean needs_start = (new_routine != m_cur_routine) && (new_routine != null);
		// Cancel old routine
		if (needs_cancel) {
			m_cur_routine.cancel();
			// Reset all setpoints
			m_setpoints.reset();
		}
		// Start next routine
		if (needs_start) {
			new_routine.start();
		}
		m_cur_routine = new_routine;

	}

	public Routine getCurrentRoutine() {
		return m_cur_routine;
	}
	
	public void reset() {
		if(m_cur_routine != null) {
			m_cur_routine.cancel();
		}
		
		setNewRoutine(null);
	}

	public RoutineManager() {
		m_setpoints = new Commands.Setpoints();
		m_setpoints.reset();
	}

	public void update(Commands commands) {

		// If current routine exists and is finished, nullify it
		if (m_cur_routine != null && m_cur_routine.isFinished()) {
			System.out.println("Routine cancel called");
			setNewRoutine(null);
		}

		// Set TROUT routine_request
		if (commands.cancel_current_routine) {
			System.out.println("Cancel routine button");
			setNewRoutine(null);
		} else if(commands.routine_request == Commands.Routines.ENCODER_DRIVE && !(m_cur_routine instanceof EncoderDriveRoutine)) {
			setNewRoutine(new EncoderDriveRoutine(500));
		} else if(commands.routine_request == Commands.Routines.TIMER_DRIVE && !(m_cur_routine instanceof DriveTimeRoutine)) {
			System.out.println("Setting routine");
			setNewRoutine(new DriveTimeRoutine(3, 0.5));
		} else if(commands.routine_request == Commands.Routines.AUTO_ALIGN && !(m_cur_routine instanceof AutoAlignmentRoutine)) {
//			System.out.println("Auto align activated");
			setNewRoutine(new AutoAlignmentRoutine());
		} else if(commands.routine_request == Commands.Routines.TURN_ANGLE && !(m_cur_routine instanceof TurnAngleRoutine)) {
			System.out.println("Turn angle activated");
			setNewRoutine(new TurnAngleRoutine(45, 0.3));
		}

		//changes the setpoints according to the current routine update
		if (m_cur_routine != null) {
			m_setpoints = m_cur_routine.update(commands);
		}

		// Get manual m_setpoints
		//        m_setpoints = m_manual_routine.update(commands, m_setpoints);
		
		//Encoder drive distance routine
		if(m_setpoints.encoder_drive_setpoint.isPresent()) {
			drive.setOpenLoop(new DriveSignal(m_setpoints.drive_velocity_setpoint.get(), m_setpoints.drive_velocity_setpoint.get()));
		}
		//Timer based routine
		else if(m_setpoints.timer_drive_time_setpoint.isPresent()) {
			drive.setOpenLoop(new DriveSignal(m_setpoints.drive_velocity_setpoint.get(), m_setpoints.drive_velocity_setpoint.get()));
		}
		// If auto-align has a setpoint to use, start turning angle
		else if(m_setpoints.auto_align_setpoint.isPresent()) {
			drive.setAutoAlignSetpoint(m_setpoints.auto_align_setpoint.get());
//			drive.setGyroTurnAngleSetpoint((m_setpoints.auto_align_setpoint.getDriveSignal()),0.5);
		} // Parse winch commands because this is only open loop
		
		
		if (commands.winch_request == Commands.WinchRequest.WIND) {
			// Temporary speed, replace with a constant later
			catapult.wind(1);
		} else if (commands.winch_request == Commands.WinchRequest.UNWIND) {
			// Temporary speed, replace with a constant later
			catapult.unwind(-1);
		}
		
		// Parse pin commands because this is only open loop
		if (commands.pin_request == Commands.PinRequest.LOCK) {
			catapult.lock();
		} else if (commands.pin_request == Commands.PinRequest.UNLOCK) {
			catapult.unlock();
		} else {
			;
		}
	}

	@Override
	public void getState(StateHolder states) {
		states.put("mode", m_cur_routine != null ? m_cur_routine.getName() : "---");
	}

	@Override
	public String getName() {
		return "behaviors";
	}
}