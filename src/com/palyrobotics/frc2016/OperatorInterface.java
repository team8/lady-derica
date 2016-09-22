package com.palyrobotics.frc2016;

import edu.wpi.first.wpilibj.Joystick;

import com.palyrobotics.frc2016.Robot.RobotName;
import com.palyrobotics.frc2016.behavior.Commands;
import com.palyrobotics.lib.util.Latch;
import com.palyrobotics.lib.util.XboxController;

public class OperatorInterface {
	private Commands m_commands = new Commands();

	Joystick leftStick = HardwareAdaptor.kLeftStick;
	Joystick rightStick = HardwareAdaptor.kRightStick;
	XboxController operatorStick = HardwareAdaptor.kOperatorStick;

	Latch driveForwardLatch = new Latch();

	public void reset() {
		m_commands = new Commands();
	}
	
	public Commands getCommands() {
		if(Robot.name == RobotName.TYR) {
			return getTyrCommands();
		} else {
			return getDericaCommands();
		}
	}
	
	public Commands getDericaCommands() {
		// Operator Stick - Intake Control
		if (operatorStick.getLeftTriggerPressed()) {
			m_commands.intake_request = Commands.IntakeRequest.INTAKE;
		} else if (operatorStick.getButtonX()) {
			m_commands.intake_request = Commands.IntakeRequest.EXHAUST;
		} else {
			m_commands.intake_request = Commands.IntakeRequest.NONE;
		}
		// Operator Stick - Shooter Control
		if (operatorStick.getButtonA()) {
			m_commands.shooter_request = Commands.ShooterRequest.EXTEND;
		} else if (operatorStick.getButtonB()) {
			m_commands.shooter_request = Commands.ShooterRequest.RETRACT;
		} else {
			m_commands.shooter_request = Commands.ShooterRequest.NONE;
		}
		// Operator Stick - Latch Control
		if (operatorStick.getButtonY()) {
			m_commands.latch_request = Commands.LatchRequest.LOCK;
		} else if (operatorStick.getRightTriggerPressed()) {
			m_commands.latch_request = Commands.LatchRequest.UNLOCK;
		} else {
			m_commands.latch_request = Commands.LatchRequest.NONE;
		}
		// Operator Stick - Grabber Control
		if (operatorStick.getRightBumper()) {
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
	
	public Commands getTyrCommands() {
		// Operator Stick - Intake Control
		if (operatorStick.getLeftTriggerPressed()) {
			m_commands.intake_request = Commands.IntakeRequest.INTAKE;
		} else if (operatorStick.getButtonX()) {
			m_commands.intake_request = Commands.IntakeRequest.EXHAUST;
		} else {
			m_commands.intake_request = Commands.IntakeRequest.NONE;
		}
		// Operator Stick - Shooter Control
		if (operatorStick.getButtonA()) {
			m_commands.shooter_request = Commands.ShooterRequest.EXTEND;
		} else if (operatorStick.getButtonB()) {
			m_commands.shooter_request = Commands.ShooterRequest.RETRACT;
		} else {
			m_commands.shooter_request = Commands.ShooterRequest.NONE;
		}
		// Operator Stick - Latch Control
		if (operatorStick.getButtonY()) {
			m_commands.latch_request = Commands.LatchRequest.LOCK;
		} else if (operatorStick.getRightTriggerPressed()) {
			m_commands.latch_request = Commands.LatchRequest.UNLOCK;
		} else {
			m_commands.latch_request = Commands.LatchRequest.NONE;
		}
		// Operator Stick - Grabber Control
		if (operatorStick.getRightBumper()) {
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