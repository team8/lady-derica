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
    
    Latch driveForwardLatch = new Latch();

    public void reset() {
        m_commands = new Commands();
    }

    public Commands getCommands() {
        // Operator Stick - Intake Control
        if (operatorStick.getRawButton(1)) {
            m_commands.intake_request = Commands.IntakeRequest.INTAKE;
        } else if (operatorStick.getRawButton(2)) {
            m_commands.intake_request = Commands.IntakeRequest.EXHAUST;
        } else {
            m_commands.intake_request = Commands.IntakeRequest.NONE;
        }
        // Operator Stick - Shooter Control
        if (operatorStick.getRawButton(3)) {
        	m_commands.shooter_request = Commands.ShooterRequest.EXTEND;
        } else if (operatorStick.getRawButton(2)) {
        	m_commands.shooter_request = Commands.ShooterRequest.RETRACT;
        } else {
        	m_commands.shooter_request = Commands.ShooterRequest.NONE;
        }
        // Operator Stick - Latch Control
        if (operatorStick.getRawButton(5)) {
        	m_commands.latch_request = Commands.LatchRequest.LOCK;
        } else if (operatorStick.getRawButton(3)) {
        	m_commands.latch_request = Commands.LatchRequest.UNLOCK;
        } else {
        	m_commands.latch_request = Commands.LatchRequest.NONE;
        }
        // Operator Stick - Grabber Control
        if (operatorStick.getRawButton(4)) {
            m_commands.grabber_request = Commands.GrabberRequest.RELEASE;
        } else {
            m_commands.grabber_request = Commands.GrabberRequest.GRAB;
        }
        
        // Operator Stick - Activate routine
        if (driveForwardLatch.update(leftStick.getRawButton(6))) {
        	m_commands.drive_forward_request = Commands.DriveForwardRequest.ACTIVATE;
        } else {
        	m_commands.drive_forward_request = Commands.DriveForwardRequest.NONE;
        }
        
        // Left Stick trigger cancels current routine
        m_commands.cancel_current_routine = leftStick.getTrigger(); // Cancels routine?

        return m_commands;
    }
}
