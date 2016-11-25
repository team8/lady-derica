package com.palyrobotics.frc2016.robot;

import edu.wpi.first.wpilibj.Joystick;

import com.palyrobotics.frc2016.input.Commands;
import com.palyrobotics.frc2016.input.RobotState;
import com.palyrobotics.frc2016.input.Commands.*;
import com.palyrobotics.frc2016.input.Commands.JoystickInput.XboxInput;
import com.palyrobotics.frc2016.subsystems.Drive.DriveGear;
import com.palyrobotics.frc2016.util.XboxController;
import com.team254.lib.util.Latch;

/**
 * Used to produce Commands {@link Commands} from human input
 * Singleton
 * @author Nihar
 *
 */
public class OperatorInterface {
	private static OperatorInterface mInstance = new OperatorInterface();

	public static OperatorInterface getInstance() {
		return mInstance;
	}

	private OperatorInterface() {
	}
	
	private Commands m_commands = new Commands();

	private HardwareAdaptor.Joysticks joysticks = HardwareAdaptor.getInstance().getJoysticks();
	private Joystick leftStick = joysticks.kLeftStick;
	private Joystick rightStick = joysticks.kRightStick;
	private Joystick operatorStick = joysticks.kOperatorStick;

	private Latch driveForwardLatch = new Latch();

	public void reset() {
		m_commands = new Commands();
	}
	
	public Commands getCommands() {
		Commands commands;
		if(Robot.getRobotState().name == RobotState.RobotName.TYR) {
			commands = getTyrCommands();
			commands.operatorStickInput = new XboxInput(((XboxController) operatorStick).getLeftX(), ((XboxController) operatorStick).getLeftY(), ((XboxController) operatorStick).getRightX(), ((XboxController) operatorStick).getRightY());
		} else {
			commands = getDericaCommands();
			commands.operatorStickInput = new XboxInput(operatorStick.getX(), operatorStick.getY(), operatorStick.getX(), operatorStick.getY());
		}
		commands.leftStickInput = new JoystickInput(leftStick.getX(), leftStick.getY(), leftStick.getTrigger());
		commands.rightStickInput = new JoystickInput(rightStick.getX(), rightStick.getY(), rightStick.getTrigger());
		return commands;
	}
	
	public Commands getDericaCommands() {		
		// Operator Stick - Derica Intake Control
		if (operatorStick.getRawButton(5)) {
			m_commands.intakeRequest = Commands.IntakeRequest.EXHAUST;
			m_commands.low_request = Commands.LowGoalShooterRequest.SHOOT;
		} else if (operatorStick.getRawButton(3)) {
			m_commands.intakeRequest = Commands.IntakeRequest.INTAKE;
			m_commands.low_request = Commands.LowGoalShooterRequest.LOAD;
		} else {
			m_commands.intakeRequest = Commands.IntakeRequest.NONE;
			m_commands.low_request = Commands.LowGoalShooterRequest.NONE;
		}

		// Left Stick trigger cancels current routine
		m_commands.cancel_current_routine = leftStick.getTrigger(); // Cancels routine?

		return m_commands;
	}
	
	public Commands getTyrCommands() {
		// Operator Stick - Intake Control
		if (((XboxController) operatorStick).getRightTriggerPressed()) {
			m_commands.intakeRequest = Commands.IntakeRequest.INTAKE;
		} else if (((XboxController) operatorStick).getLeftTriggerPressed()) {
			m_commands.intakeRequest = Commands.IntakeRequest.EXHAUST;
		} else {
			m_commands.intakeRequest = Commands.IntakeRequest.NONE;
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
			m_commands.routine_request = Routines.AUTO_ALIGN;
		} else {
			m_commands.routine_request = Routines.NONE;
		}
		
		if(rightStick.getRawButton(4)) {
			m_commands.gear_request = DriveGear.LOW;
		} else if(rightStick.getRawButton(6)) {
			m_commands.gear_request = DriveGear.HIGH;
		}
		
		// Left Stick trigger cancels current routine
		m_commands.cancel_current_routine = leftStick.getTrigger(); // Cancels routine?

		return m_commands;
	}
}