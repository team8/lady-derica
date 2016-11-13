package com.palyrobotics.frc2016.behavior.routines;

import com.palyrobotics.frc2016.behavior.RobotSetpoints;
import com.palyrobotics.frc2016.input.Commands;
import com.palyrobotics.frc2016.robot.HardwareAdaptor;
import com.palyrobotics.frc2016.subsystems.Drive;
import com.team254.lib.util.DriveSignal;

public class TurnAngleRoutine extends Routine {

	private Drive drive = HardwareAdaptor.kDrive;
	
	private double angle;
	private double maxVel;
	
	private States m_state = States.START;
	
	private enum States {
		START, TURNING, DONE
	}
	
	public TurnAngleRoutine(double angle, double maxVel) {
		this.angle = angle;
		this.maxVel = maxVel;
	}
	
	@Override
	public void start() {
		drive.reset();
		m_state = States.START;
	}

	@Override
	public RobotSetpoints update(Commands commands, RobotSetpoints existing_setpoints) {
		RobotSetpoints setpoints = existing_setpoints;
		
		switch(m_state) {
		case START:
			System.out.println("Set setpoint: "+angle);
			drive.setGyroTurnAngleSetpoint(angle, maxVel);
			
			setpoints.drive_routine_action = RobotSetpoints.DriveRoutineAction.ENCODER_TURN;
			m_state = States.TURNING;
			break;
			
		case TURNING:
			if(drive.controllerOnTarget()) {
				m_state = States.DONE;
			}
			break;
			
		case DONE:
			drive.reset();
			break;
		}
		
		return setpoints;
	}
	
	@Override
	public void cancel() {
		m_state = States.DONE;
		drive.setOpenLoop(DriveSignal.NEUTRAL);
		drive.reset();
	}

	@Override
	public boolean isFinished() {
		return m_state == States.DONE;
	}

	@Override
	public String getName() {
		return "EncoderTurnAngleRoutine";
	}
}