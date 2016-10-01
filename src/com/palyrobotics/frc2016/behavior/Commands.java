package com.palyrobotics.frc2016.behavior;

public class Commands {
    public enum PresetRequest {
        NONE, MANUAL
    }
    
    // Possible commands for intake
    public enum IntakeRequest {
        NONE, INTAKE, EXHAUST
    }
    
    // Commands for Grabber
    public enum GrabberRequest {
    	GRAB, RELEASE
    }
    
    // Commands for Latch
    public enum LatchRequest {
    	NONE, LOCK, UNLOCK
    }
    
    // Commands for Shooter
    public enum ShooterRequest {
    	NONE, EXTEND, RETRACT
    }
    
    // Commands for TimerDriveRoutine
    public enum TimerDriveRequest {
    	NONE, ACTIVATE
    }
    
    //Commands for EncoderDriveRoutine
    public enum EncoderDriveRequest {
    	NONE, ACTIVATE
    }
    
    //Commands for Encoder Turn Angle
    public enum EncoderTurnAngleRequest {
    	NONE, ACTIVATE
    }
    
    //Commands for AutoAlign
    public enum AutoAlignRequest {
    	NONE, ACTIVATE
    }

    public PresetRequest preset_request;
    
    // Subsystem requests
    public IntakeRequest intake_request;
    public GrabberRequest grabber_request;
    public LatchRequest latch_request;
    public ShooterRequest shooter_request;
    
    // Routine requests
    public TimerDriveRequest timer_drive_request;
    public EncoderDriveRequest encoder_drive_request;
    public AutoAlignRequest auto_align_request;
    public EncoderTurnAngleRequest encoder_turn_angle_request;
    
    // Allows you to cancel routine
    public boolean cancel_current_routine = false;
    
    // Reset all routine requests to NONE
    public void resetRoutineRequests() {
    	timer_drive_request = TimerDriveRequest.NONE;
    	encoder_drive_request = EncoderDriveRequest.NONE;
    	auto_align_request = AutoAlignRequest.NONE;
    	encoder_turn_angle_request = EncoderTurnAngleRequest.NONE;
    }
}