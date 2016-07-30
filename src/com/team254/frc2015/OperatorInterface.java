package com.team254.frc2015;

import com.team254.frc2015.behavior.Commands;
import com.team254.lib.util.Latch;
import edu.wpi.first.wpilibj.Joystick;

import java.util.Optional;

public class OperatorInterface {
    private Commands m_commands = new Commands();

    Joystick leftStick = HardwareAdaptor.kLeftStick;
    Joystick rightStick = HardwareAdaptor.kRightStick;
    Joystick operatorStick = HardwareAdaptor.kOperatorStick;

    public void reset() {
        m_commands = new Commands();
    }

    public Commands getCommands() {
        // Left joystick
        if (leftStick.getRawButton(1)) {
            m_commands.intake_request = Commands.IntakeRequest.INTAKE;
        } else if (leftStick.getRawButton(2)) {
            m_commands.intake_request = Commands.IntakeRequest.EXHAUST;
        } else {
            m_commands.intake_request = Commands.IntakeRequest.NONE;
        }

        // Right joystick
        if (rightStick.getRawButton(2)) {
            m_commands.intake_request = Commands.IntakeRequest.OPEN;
        } else {
            m_commands.intake_request = Commands.IntakeRequest.CLOSE;
        }
        
        m_commands.cancel_current_routine = operatorStick.getX() < 0; // Button 6

        return m_commands;
    }
}
