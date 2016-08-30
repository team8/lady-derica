package com.team8.frc2016.behavior.routines;

import com.team8.frc2016.HardwareAdaptor;
import com.team8.frc2016.behavior.Commands;
import com.team8.frc2016.behavior.RobotSetpoints;
import com.team8.frc2016.subsystems.Drive;
import com.team8.lib.util.DriveSignal;

import edu.wpi.first.wpilibj.Timer;

public class EncoderDriveRoutine extends Routine {
	
	public enum States {
		NONE, DRIVE_FORWARD, DONE
	}
	
	States m_state = States.NONE;
	private int distance;
    Timer m_state_timer = new Timer();
    
    private Drive drive = HardwareAdaptor.kDrive;
    
    public EncoderDriveRoutine(int distance) {
    	this.distance = distance;
    }
    
	@Override
	public void reset() {
	}

	//Routines just change the states of the robotsetpoints, which the behavior manager then moves the physical subsystems based on.
	@Override
	public RobotSetpoints update(Commands commands, RobotSetpoints existing_setpoints) {
		States new_state = m_state;
		
		switch (m_state) {
		case NONE:
			existing_setpoints.encoder_drive_action = RobotSetpoints.EncoderDriveAction.WAITING;
			new_state = States.DRIVE_FORWARD;
			break;
		case DRIVE_FORWARD:
			if(drive.m_right_encoder.getDistance() < distance) {
				existing_setpoints.encoder_drive_action = RobotSetpoints.EncoderDriveAction.DRIVE_STRAIGHT;
			}
			else {
				existing_setpoints.encoder_drive_action = RobotSetpoints.EncoderDriveAction.NONE;
				new_state = States.DONE;
			}
			break;
		case DONE:
			drive.reset();
			existing_setpoints.encoder_drive_action = RobotSetpoints.EncoderDriveAction.NONE;
			break;
		}
		
        if (new_state != m_state) {
            m_state = new_state;
        }
        
		return existing_setpoints;
	}

	@Override
	public void cancel() {
		m_state = States.NONE;
        drive.setOpenLoop(new DriveSignal(0, 0));
        drive.reset();
	}

	@Override
	public boolean isFinished() {
		return m_state == States.DONE;
	}

	@Override
	public String getName() {
		return "Encoder Drive Forward";
	}

}
