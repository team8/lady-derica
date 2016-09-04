package com.palyrobotics.frc2016.behavior;

import java.util.Optional;

public class RobotSetpoints {

	public enum RoutineAction {
		NONE, RUNNING
	}
	
    public enum IntakeAction {
    	NONE, OPEN, CLOSE, PREFER_OPEN, PREFER_CLOSE
    }

    public enum TimerDriveAction {
    	NONE, DRIVE_STRAIGHT, WAITING
    }
    
    public enum EncoderDriveAction {
    	NONE, DRIVE_STRAIGHT, WAITING
    }
    
    public static final Optional<Double> m_nullopt = Optional.empty();

    public IntakeAction intake_action;
    public TimerDriveAction timer_drive_action;
    public EncoderDriveAction encoder_drive_action;
    public Optional<Double> top_open_loop_jog;
    public Optional<Double> bottom_open_loop_jog;
    public RoutineAction routine_status;

    public void reset() {
    	intake_action = IntakeAction.NONE;
    	timer_drive_action = TimerDriveAction.NONE;
    	encoder_drive_action = EncoderDriveAction.NONE;
    	routine_status = RoutineAction.NONE;
    	top_open_loop_jog = m_nullopt;
    	bottom_open_loop_jog = m_nullopt;
    }
}
