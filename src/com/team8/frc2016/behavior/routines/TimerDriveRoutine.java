package com.team8.frc2016.behavior.routines;

import com.team8.frc2016.HardwareAdaptor;
import com.team8.frc2016.behavior.Commands;
import com.team8.frc2016.behavior.RobotSetpoints;
import com.team8.frc2016.subsystems.Drive;
import com.team8.lib.util.DriveSignal;

import edu.wpi.first.wpilibj.Timer;

public class TimerDriveRoutine extends Routine {
	
	public enum States {
		WAIT, DRIVE_FORWARD, DONE
	}
	
	States m_state = States.WAIT;
	private boolean m_is_new_state = true;
    Timer m_state_timer = new Timer();
    
    private Drive drive = HardwareAdaptor.kDrive;
    
	@Override
	public void reset() {
		m_state = States.WAIT;
		m_is_new_state = true;
		m_state_timer.stop();
        m_state_timer.reset();
	}

	//Routines just change the states of the robotsetpoints, which the behavior manager then moves the physical subsystems based on.
	@Override
	public RobotSetpoints update(Commands commands, RobotSetpoints existing_setpoints) {
		States new_state = m_state;
		
		switch (m_state) {
		case WAIT:
			existing_setpoints.timer_drive_action = RobotSetpoints.TimerDriveAction.WAITING;
			if (m_is_new_state) {
				m_state_timer.start();
			}
			if (m_state_timer.get() > 3) {
				new_state = States.DRIVE_FORWARD;
				m_state_timer.stop();
				m_state_timer.reset();
				m_state_timer.start();
			}
			break;
		case DRIVE_FORWARD:
			existing_setpoints.timer_drive_action = RobotSetpoints.TimerDriveAction.DRIVE_STRAIGHT;
			if(m_state_timer.get() > 3) {
				existing_setpoints.timer_drive_action = RobotSetpoints.TimerDriveAction.NONE;
				new_state = States.DONE;
			}
			break;
		case DONE:
			drive.reset();
			existing_setpoints.timer_drive_action = RobotSetpoints.TimerDriveAction.NONE;
			break;
		}
		
		m_is_new_state = false;
        if (new_state != m_state) {
            m_state = new_state;
            m_state_timer.reset();
            m_is_new_state = true;
        }
        
		return existing_setpoints;
	}

	@Override
	public void cancel() {
		m_state = States.WAIT;
        m_state_timer.stop();
        drive.setOpenLoop(new DriveSignal(0, 0));
        drive.reset();
	}

	@Override
	public boolean isFinished() {
		return m_state == States.DONE;
	}

	@Override
	public String getName() {
		return "Timer Drive Forward";
	}

}
