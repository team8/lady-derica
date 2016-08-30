package com.team8.frc2016;

import edu.wpi.first.wpilibj.Joystick;

import java.util.Optional;

import com.team8.frc2016.behavior.Commands;
import com.team8.lib.util.Latch;

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
        	m_commands.timer_drive_request = Commands.TimerDriveRequest.ACTIVATE;
        } else if(driveForwardLatch.update(leftStick.getRawButton(5))) {
        	m_commands.encoder_drive_request = Commands.EncoderDriveRequest.ACTIVATE;
        } else {
        	m_commands.timer_drive_request = Commands.TimerDriveRequest.NONE;
        	m_commands.encoder_drive_request = Commands.EncoderDriveRequest.NONE;
        }
        
        // Left Stick trigger cancels current routine
        m_commands.cancel_current_routine = leftStick.getTrigger(); // Cancels routine?

        return m_commands;
    }
}
