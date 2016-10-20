package com.palyrobotics.frc2016;

import edu.wpi.first.wpilibj.Joystick;

import com.palyrobotics.frc2016.Robot.RobotName;
import com.palyrobotics.frc2016.behavior.Commands;
import com.palyrobotics.frc2016.subsystems.Drive.DriveGear;
import com.palyrobotics.frc2016.util.XboxController;
import com.team254.lib.util.Latch;

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
		if(Robot.name == RobotName.TYR) {
			return getTyrCommands();
		} else {
			return getDericaCommands();
		}
	}
	
	public Commands getDericaCommands() {
		
		m_commands.resetRoutineRequests();
		// Operator Stick - Derica Intake Control
		if (operatorStick.getRawButton(5)) {
			m_commands.intake_request = Commands.IntakeRequest.EXHAUST;
			m_commands.low_request = Commands.LowGoalShooterRequest.SHOOT;
		} else if (operatorStick.getRawButton(3)) {
			m_commands.intake_request = Commands.IntakeRequest.INTAKE;
			m_commands.low_request = Commands.LowGoalShooterRequest.LOAD;
		} else {
			m_commands.intake_request = Commands.IntakeRequest.NONE;
			m_commands.low_request = Commands.LowGoalShooterRequest.NONE;
		}

		// Left Stick trigger cancels current routine
		m_commands.cancel_current_routine = leftStick.getTrigger(); // Cancels routine?

		return m_commands;
	}
	
	public Commands getTyrCommands() {
		// Operator Stick - Intake Control
		if (((XboxController) operatorStick).getRightTriggerPressed()) {
			m_commands.intake_request = Commands.IntakeRequest.INTAKE;
		} else if (((XboxController) operatorStick).getLeftTriggerPressed()) {
			m_commands.intake_request = Commands.IntakeRequest.EXHAUST;
		} else {
			m_commands.intake_request = Commands.IntakeRequest.NONE;
		}
		// Operator Stick - Shooter Control
		if (((XboxController) operatorStick).getButtonX()) {
			m_commands.shooter_request = Commands.ShooterRequest.EXTEND;
		} else if (((XboxController) operatorStick).getButtonB()) {
			m_commands.shooter_request = Commands.ShooterRequest.RETRACT;
		} else {
			m_commands.shooter_request = Commands.ShooterRequest.NONE;
		}
		// Operator Stick - Latch Control
		if (((XboxController) operatorStick).getButtonA()) {
			m_commands.latch_request = Commands.LatchRequest.LOCK;
		} else if (((XboxController) operatorStick).getButtonY()) {
			m_commands.latch_request = Commands.LatchRequest.UNLOCK;
		} else {
			m_commands.latch_request = Commands.LatchRequest.NONE;
		}
		// Operator Stick - Grabber Control
		if (((XboxController) operatorStick).getLeftBumper()) {
			m_commands.grabber_request = Commands.GrabberRequest.RELEASE;
		} else {
			m_commands.grabber_request = Commands.GrabberRequest.GRAB;
		}
		
		// Right Stick - Activate routine
		if(rightStick.getRawButton(2)) {
			m_commands.resetRoutineRequests();
			m_commands.auto_align_request = Commands.AutoAlignRequest.ACTIVATE;
		} else {
			m_commands.resetRoutineRequests();
		}
		
		if(rightStick.getRawButton(4)) {
			HardwareAdaptor.kDrive.setGear(DriveGear.LOW);
		} else if(rightStick.getRawButton(6)) {
			HardwareAdaptor.kDrive.setGear(DriveGear.HIGH);
		}
		
		// Left Stick trigger cancels current routine
		m_commands.cancel_current_routine = leftStick.getTrigger(); // Cancels routine?

		return m_commands;
	}
}