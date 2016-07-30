package com.team254.frc2015.behavior;

import java.util.Optional;

public class RobotSetpoints {

    public enum IntakeAction {
        NONE, OPEN, CLOSE, PREFER_OPEN, PREFER_CLOSE
    }

    public static final Optional<Double> m_nullopt = Optional.empty();

    public IntakeAction intake_action;
    public Optional<Double> top_open_loop_jog;
    public Optional<Double> bottom_open_loop_jog;

    public void reset() {
        intake_action = IntakeAction.NONE;
        top_open_loop_jog = m_nullopt;
        bottom_open_loop_jog = m_nullopt;
    }
}
