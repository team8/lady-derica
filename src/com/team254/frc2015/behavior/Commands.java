package com.team254.frc2015.behavior;

import java.util.Optional;

public class Commands {
    public enum PresetRequest {
        NONE, MANUAL
    }
    
    // Possible commands for intake
    public enum IntakeRequest {
        NONE, INTAKE, EXHAUST
    }

    public PresetRequest preset_request;
    public Optional<Double> top_jog = Optional.empty();
    public Optional<Double> bottom_jog = Optional.empty();
    public IntakeRequest intake_request;
}
