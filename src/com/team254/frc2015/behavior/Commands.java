package com.team254.frc2015.behavior;

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

    public PresetRequest preset_request;
    
    // Subsystem requests
    public IntakeRequest intake_request;
    public GrabberRequest grabber_request;
    public LatchRequest latch_request;
    public ShooterRequest shooter_request;
    
    // Allows you to cancel routine
    public boolean cancel_current_routine = false;
}
