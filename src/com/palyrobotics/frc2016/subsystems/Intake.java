package com.palyrobotics.frc2016.subsystems;

import com.palyrobotics.lib.util.CheesySpeedController;
import com.palyrobotics.lib.util.StateHolder;
import com.palyrobotics.lib.util.Subsystem;

public class Intake extends Subsystem {
    CheesySpeedController m_left_motor;
    CheesySpeedController m_right_motor;

    public Intake(String name, CheesySpeedController left_motor, 
    		CheesySpeedController right_motor) {
        super(name);
        m_left_motor = left_motor;
        m_right_motor = right_motor;
    }

    public void setSpeed(double speed) {
        setLeftRight(speed, speed);
    }

    public void setLeftRight(double left_speed, double right_speed) {
        m_left_motor.set(-left_speed);
        m_right_motor.set(-right_speed);
    }

    @Override
    public void reloadConstants() {
    }

    @Override
    public void getState(StateHolder states) {
    }
}
