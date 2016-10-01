package com.palyrobotics.frc2016.behavior;

import com.palyrobotics.frc2016.Constants;
import com.palyrobotics.frc2016.HardwareAdaptor;
import com.palyrobotics.frc2016.Robot;
import com.palyrobotics.frc2016.behavior.routines.*;
import com.palyrobotics.frc2016.subsystems.*;
import com.team254.lib.util.DriveSignal;
import com.team254.lib.util.StateHolder;
import com.team254.lib.util.Tappable;

public class BehaviorManager implements Tappable {

	public boolean isZero(double val) {
		return val == 0 || (val < 0.001 && val > -0.001);
	}

	protected Drive drive = HardwareAdaptor.kDrive;

	protected TyrShooter kShooter = HardwareAdaptor.kTyrShooter;
	protected Intake intake = HardwareAdaptor.kIntake;

	private Routine m_cur_routine = null;
	private RobotSetpoints m_setpoints;
	//    private ManualRoutine m_manual_routine = new ManualRoutine();

	public RobotSetpoints getSetpoints() {
		return m_setpoints;
	}
	
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

	public BehaviorManager() {
		m_setpoints = new RobotSetpoints();
		m_setpoints.reset();
	}

	public void update(Commands commands) {

		// If current routine exists and is finished, nullify it
		if (m_cur_routine != null && m_cur_routine.isFinished()) {
			System.out.println("Routine cancel called");
			setNewRoutine(null);
		}

		// Set TROUT routines
		if (commands.cancel_current_routine) {
			System.out.println("Cancel routine button");
			setNewRoutine(null);
		} else if(commands.encoder_drive_request == Commands.EncoderDriveRequest.ACTIVATE && !(m_cur_routine instanceof EncoderDriveRoutine)) {
			setNewRoutine(new EncoderDriveRoutine(1000));
		} else if(commands.timer_drive_request == Commands.TimerDriveRequest.ACTIVATE && !(m_cur_routine instanceof TimerDriveRoutine)) {
			setNewRoutine(new TimerDriveRoutine(5));
		} else if(commands.auto_align_request == Commands.AutoAlignRequest.ACTIVATE && !(m_cur_routine instanceof AutoAlignmentRoutine)) {
			setNewRoutine(new AutoAlignmentRoutine());
		} else if(commands.encoder_turn_angle_request == Commands.EncoderTurnAngleRequest.ACTIVATE && !(m_cur_routine instanceof EncoderTurnAngleRoutine)) {
			setNewRoutine(new EncoderTurnAngleRoutine(90, 0.5));
		}

		//changes the setpoints according to the current routine update
		if (m_cur_routine != null) {
			m_setpoints = m_cur_routine.update(commands, m_setpoints);
		}

		// Get manual m_setpoints
		//        m_setpoints = m_manual_routine.update(commands, m_setpoints);

		// Intake commands parsing
		if (commands.intake_request == Commands.IntakeRequest.INTAKE) {
			// Run intake inwards (positive speed is intake)
			intake.setSpeed(Constants.kManualIntakeSpeed);
		} else if (commands.intake_request == Commands.IntakeRequest.EXHAUST) {
			// Run intake outwards (negative speed is exhaust)
			intake.setSpeed(-Constants.kManualExhaustSpeed);
		} else {
			// Stop intake.
//			intake.setLeftRight(0.0, 0.0);
		}

		// Parse latch commands because this is only open loop
		if (commands.latch_request == Commands.LatchRequest.LOCK) {
			kShooter.lock();
		} else if (commands.latch_request == Commands.LatchRequest.UNLOCK) {
			kShooter.unlock();
		}

		// Parse grabbber commands because this is only open loop
		if (commands.grabber_request == Commands.GrabberRequest.GRAB) {
			kShooter.grab();
		} else if (commands.grabber_request == Commands.GrabberRequest.RELEASE) {
			kShooter.release();
		}

		// Parse shooter commands because this is only open loop
		if (commands.shooter_request == Commands.ShooterRequest.EXTEND) {
			kShooter.extend();
		} else if (commands.shooter_request == Commands.ShooterRequest.RETRACT) {
			kShooter.retract();
		}
		
		//Encoder drive distance routine
		if(m_setpoints.encoder_drive_setpoint.isPresent()) {
//			drive.setOpenLoop(new DriveSignal(m_setpoints.drive_velocity_setpoint.get(), m_setpoints.drive_velocity_setpoint.get()));
		}
		//Timer based routine
		else if(m_setpoints.timer_drive_time_setpoint.isPresent()) {
			drive.setOpenLoop(new DriveSignal(m_setpoints.drive_velocity_setpoint.get(), m_setpoints.drive_velocity_setpoint.get()));
		}
		// If auto-align has a setpoint to use, start turning angle
		else if(m_setpoints.auto_align_setpoint.isPresent()) {
			drive.setTurnSetPoint(m_setpoints.auto_align_setpoint.get());
		}
		//Encoder Turn Angle
		else if(m_setpoints.encoder_turn_setpoint_left.isPresent()) {
			drive.setOpenLoop(new DriveSignal(m_setpoints.encoder_turn_setpoint_left.get(), -m_setpoints.encoder_turn_setpoint_left.get()));
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