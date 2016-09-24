package com.palyrobotics.frc2016.behavior.routines;

import java.util.Optional;

import com.palyrobotics.frc2016.behavior.Commands;
import com.palyrobotics.frc2016.behavior.RobotSetpoints;
import com.palyrobotics.frc2016.behavior.RobotSetpoints.DriveRoutineState;
import com.palyrobotics.lib.util.DriveSignal;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class AutoAlignmentRoutine extends Routine {
	/* Start = Start of the routine
	 * Set_Angle = wait for vision, then set angle
	 * Aligning = waiting while robot turns
	 * Done = no goal spotted, or finished iterations
	 */
	public enum AutoAlignStates {
		START, SET_ANGLE, ALIGNING, DONE, IDLE
	}

	public AutoAlignStates m_state = AutoAlignStates.IDLE;
	private NetworkTable table = NetworkTable.getTable("visiondata");
	// Threshold angle for which we will turn
	private final double m_min_angle = 3;
	
	// Number of iterations for successive auto alignments
	private final int m_default_iterations = 2;
	private int m_iterations = m_default_iterations;

	// Timer used for waiting period for camera stabilization
	private Timer m_timer = new Timer();
	private final double m_wait_time = 1500; 
	
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
				new_state = AutoAlignStates.SET_ANGLE;
			} else {
				new_state = AutoAlignStates.DONE;
			}
			setpoints.drive_routine_action = DriveRoutineState.AUTO_ALIGN;
			break;
		case SET_ANGLE:
			// Wait for m_wait_time before reading vision data (latency)
			if(m_timer.get() < m_wait_time) {
				break;
			}
			// If angle turnpoint has been set, then set this routine to waiting for alignment
			if(existing_setpoints.auto_align_setpoint.isPresent()) {
				new_state = AutoAlignStates.ALIGNING;
				break;
			}
			if(table.getNumber("skewangle", 100000) > m_min_angle) {
				setpoints.auto_align_setpoint = Optional.of(table.getNumber("skewangle", 100000));
			} else {
				System.out.println("No goal detected");
				m_iterations = 0;
				new_state = AutoAlignStates.DONE;
			}
			break;
		case ALIGNING:
			// If finished turning, start next sequence or finish
			if(drive.controllerOnTarget()) {
				m_iterations--;
				if(m_iterations > 0) {
					new_state = AutoAlignStates.START;
				} else {
					new_state = AutoAlignStates.DONE;
				}
			}
			break;
		case DONE:
			drive.reset();
			setpoints.auto_align_setpoint = RobotSetpoints.m_nullopt;
			setpoints.drive_routine_action = DriveRoutineState.NONE;
			new_state = AutoAlignStates.IDLE;
			break;
		case IDLE:
			// Do nothing, let other routines be
			break;
		}
		m_state = new_state;
		return setpoints;
	}

	@Override
	public void cancel() {
		setpoints.auto_align_setpoint = RobotSetpoints.m_nullopt;
		m_state = AutoAlignStates.DONE;
		drive.setOpenLoop(DriveSignal.NEUTRAL);
		drive.reset();
	}

	@Override
	public void reset() {
		m_state = AutoAlignStates.DONE;
		m_timer.reset();
		m_timer.start();
		m_iterations = m_default_iterations;
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
