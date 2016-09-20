package com.palyrobotics.frc2016.behavior;

import java.util.Optional;

import com.palyrobotics.frc2016.Constants;
import com.palyrobotics.frc2016.HardwareAdaptor;
import com.palyrobotics.frc2016.behavior.routines.*;
import com.palyrobotics.frc2016.subsystems.*;
import com.palyrobotics.lib.util.DriveSignal;
import com.palyrobotics.lib.util.StateHolder;
import com.palyrobotics.lib.util.Tappable;

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
		boolean needs_cancel = (new_routine != m_cur_routine) && (m_cur_routine != null);

		boolean needs_reset = (new_routine != m_cur_routine) && (new_routine != null);
		if (needs_cancel) {
			m_cur_routine.cancel();
		}
		m_cur_routine = new_routine;
		if (needs_reset) {
			m_cur_routine.reset();
		}
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
		//resets the state of the robot, the setpoints will be changed later by the current routine
		m_setpoints.reset();

		// If current routine exists and is finished, nullify it
		if (m_cur_routine != null && m_cur_routine.isFinished()) {
			setNewRoutine(null);
		}

		// Set TROUT routines
		if (commands.cancel_current_routine) {
			setNewRoutine(null);
		} else if(commands.encoder_drive_request == Commands.EncoderDriveRequest.ACTIVATE && !(m_cur_routine instanceof EncoderDriveRoutine)) {
			setNewRoutine(new EncoderDriveRoutine(1000));
		} else if (commands.timer_drive_request == Commands.TimerDriveRequest.ACTIVATE && !(m_cur_routine instanceof TimerDriveRoutine)) {
			setNewRoutine(new TimerDriveRoutine());
		}

		//changes the setpoints according to the current routine update
		if (m_cur_routine != null) {
			m_setpoints = m_cur_routine.update(commands, m_setpoints);
		}

		// Get manual m_setpoints
		//        m_setpoints = m_manual_routine.update(commands, m_setpoints);

		// Intake commands parsing
		if (commands.intake_request == Commands.IntakeRequest.INTAKE) {
			// Run intake inwards.
			intake.setLeftRight(Constants.kManualIntakeSpeed, -Constants.kManualExhaustSpeed);
		} else if (commands.intake_request == Commands.IntakeRequest.EXHAUST) {
			// Run intake outwards.
			intake.setLeftRight(-Constants.kManualExhaustSpeed, Constants.kManualExhaustSpeed);
		} else {
			// Stop intake.
			intake.setLeftRight(0.0, 0.0);
		}

		// Parse latch commands because this is only open loop
		if (commands.latch_request == Commands.LatchRequest.LOCK) {
			kShooter.lock();
		} else if (commands.latch_request == Commands.LatchRequest.UNLOCK) {
			kShooter.unlock();
		} else {
			;
		}

		// Parse grabbber commands because this is only open loop
		if (commands.grabber_request == Commands.GrabberRequest.GRAB) {
			;
		} else if (commands.grabber_request == Commands.GrabberRequest.RELEASE) {
			;
		} else {
			;
		}

		// Parse shooter commands because this is only open loop
		if (commands.shooter_request == Commands.ShooterRequest.EXTEND) {
			kShooter.extend();
		} else if (commands.shooter_request == Commands.ShooterRequest.RETRACT) {
			kShooter.retract();
		} else {
		}
		
		//Timer based routine
		if(m_setpoints.timer_drive_action == RobotSetpoints.TimerDriveAction.DRIVE_STRAIGHT) {
			drive.setOpenLoop(new DriveSignal(0.5, 0.5));
		}
		if(m_setpoints.timer_drive_action == RobotSetpoints.TimerDriveAction.WAITING) {
			drive.setOpenLoop(new DriveSignal(0, 0));
		}
		
		//Encoder based routine
		if(m_setpoints.encoder_drive_action == RobotSetpoints.EncoderDriveAction.DRIVE_STRAIGHT) {
			drive.setOpenLoop(new DriveSignal(0.3, 0.3));
		}
		if(m_setpoints.encoder_drive_action == RobotSetpoints.EncoderDriveAction.WAITING) {
			drive.setOpenLoop(new DriveSignal(0, 0));
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