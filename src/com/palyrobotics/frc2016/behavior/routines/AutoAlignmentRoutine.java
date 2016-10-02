package com.palyrobotics.frc2016.behavior.routines;

import java.util.Optional;

import com.palyrobotics.frc2016.behavior.Commands;
import com.palyrobotics.frc2016.behavior.RobotSetpoints;
import com.palyrobotics.frc2016.behavior.RobotSetpoints.DriveRoutineAction;
import com.team254.lib.util.DriveSignal;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class AutoAlignmentRoutine extends Routine {
	/* Start = Start of the routine
	 * Set_Angle = wait for vision, then set angle
	 * Aligning = waiting while robot turns
	 * Done = no goal spotted, or finished iterations
	 */
	private enum AutoAlignStates {
		START, SET_ANGLE, ALIGNING, DONE
	}

	public AutoAlignStates m_state = AutoAlignStates.START;
	private boolean m_is_new_state = true;
	private NetworkTable table = NetworkTable.getTable("visiondata");
	// Threshold angle for which we will turn
	private final double m_min_angle = 3;
	
	// Number of iterations for successive auto alignments
	private final int m_default_iterations = 2;
	private int m_iterations = m_default_iterations;

	// Timer used for waiting period for camera stabilization
	private Timer m_timer = new Timer();
	private final double m_wait_time = 1.5; 
	
	
	RobotSetpoints setpoints;

	/**
	 * Changes number of successive alignments
	 */
	public void setIterations(int iterations) {
		this.m_iterations = iterations;
	}

	@Override
	public RobotSetpoints update(Commands commands, RobotSetpoints existing_setpoints) {
		RobotSetpoints setpoints = existing_setpoints;
		AutoAlignStates new_state = m_state;
		switch(m_state) {
		case START:
			if(m_iterations > 0) {
				m_timer.reset();
				m_timer.start();
				drive.reset();
				System.out.println("Started auto align " + m_state);
				new_state = AutoAlignStates.SET_ANGLE;
			} else {
				new_state = AutoAlignStates.DONE;
			}
			setpoints.drive_routine_action = DriveRoutineAction.AUTO_ALIGN;
			break;
		case SET_ANGLE:
			// Wait for m_wait_time before reading vision data (latency)
			if(m_timer.get() < m_wait_time) {
				System.out.println("Waiting for vision data");
				break;
			}
			// If angle turnpoint has been set, then set this routine to waiting for alignment
			if(existing_setpoints.auto_align_setpoint.isPresent()) {
				System.out.println("Already set angle setpoint");
				new_state = AutoAlignStates.ALIGNING;
				break;
			}
//			if(table.getNumber("skewangle", 100000) > m_min_angle) {
//				setpoints.auto_align_setpoint = Optional.of(table.getNumber("skewangle", 100000));
			if(true) {
				System.out.println("Manually set auto align setpoint");
				int direction = (m_iterations%2 == 1) ? -1:1;
				setpoints.auto_align_setpoint = Optional.of(direction * 20.0);
				System.out.println("SETPOINT:" + direction * 20.0);
			} else {
				System.out.println("No goal detected");
				m_iterations = 0;
				new_state = AutoAlignStates.DONE;
			}
			break;
		case ALIGNING:
			System.out.println("aligning, waiting on controller");
			// If finished turning, start next sequence or finish
			if(drive.controllerOnTarget()) {
				System.out.println("Drive controller reached target");
				m_iterations--;
				if(m_iterations > 0) {
					System.out.println("Starting new iteration");
					new_state = AutoAlignStates.START;
				} else {
					System.out.println("Finished auto aligning");
					new_state = AutoAlignStates.DONE;
				}
			}
			break;
		case DONE:
			drive.reset();
			break;
		}
		m_is_new_state = false;
		if(m_state != new_state) {
			m_state = new_state;
			m_is_new_state = true;
		}
		return setpoints;
	}

	@Override
	public void cancel() {
		m_state = AutoAlignStates.DONE;
		drive.setOpenLoop(DriveSignal.NEUTRAL);
		drive.reset();
	}

	@Override
	public void start() {
		m_timer.reset();
		m_timer.start();
		if(m_iterations < 1) {
			m_iterations = m_default_iterations;
		}
	}

	@Override
	public boolean isFinished() {
		return m_state == AutoAlignStates.DONE;
	}

	@Override
	public String getName() {
		return "Auto Alignment Routine";
	}

}
