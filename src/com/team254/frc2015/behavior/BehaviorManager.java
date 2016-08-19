package com.team254.frc2015.behavior;

import com.team254.frc2015.Constants;
import com.team254.frc2015.HardwareAdaptor;
import com.team254.frc2015.behavior.routines.*;
import com.team254.frc2015.subsystems.*;
import com.team254.lib.util.DriveSignal;
import com.team254.lib.util.StateHolder;
import com.team254.lib.util.Tappable;

import java.util.Optional;

public class BehaviorManager implements Tappable {

	public boolean isZero(double val) {
		return val == 0 || (val < 0.001 && val > -0.001);
	}

	protected Drive drive = HardwareAdaptor.kDrive;
//	protected Intake intake = HardwareAdaptor.kIntake;

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

		// Set "TROUT" Routine (we have none for now)
		if (commands.cancel_current_routine) {
			setNewRoutine(null);
		} else if (commands.drive_forward_request == Commands.DriveForwardRequest.ACTIVATE && !(m_cur_routine instanceof DriveForwardRoutine)) {
			setNewRoutine(new DriveForwardRoutine());
		}

		//changes the setpoints according to the current routine update
		if (m_cur_routine != null) {
			m_setpoints = m_cur_routine.update(commands, m_setpoints);
		}

		// Get manual m_setpoints
		//        m_setpoints = m_manual_routine.update(commands, m_setpoints);

		// Intake commands parsing
//		if (commands.intake_request == Commands.IntakeRequest.INTAKE) {
//			// Run intake inwards.
//			intake.setSpeed(-Constants.kManualIntakeSpeed);
//		} else if (commands.intake_request == Commands.IntakeRequest.EXHAUST) {
//			// Run intake outwards.
//			intake.setSpeed(Constants.kManualExhaustSpeed);
//		} else {
//			// Stop intake.
//			intake.setSpeed(0.0);
//		}

		// Parse latch commands because this is only open loop
		if (commands.latch_request == Commands.LatchRequest.LOCK) {
			;
		} else if (commands.latch_request == Commands.LatchRequest.UNLOCK) {
			;
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
			;
		} else if (commands.shooter_request == Commands.ShooterRequest.RETRACT) {
			;
		} else {
			;
		}
		
		if(m_setpoints.drive_action == RobotSetpoints.DriveAction.DRIVE_STRAIGHT) {
			drive.setOpenLoop(new DriveSignal(0.5, 0.5));
		}
		if(m_setpoints.drive_action == RobotSetpoints.DriveAction.WAITING) {
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
