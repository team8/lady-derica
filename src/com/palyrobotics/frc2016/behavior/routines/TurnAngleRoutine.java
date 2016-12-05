package com.palyrobotics.frc2016.behavior.routines;

import com.palyrobotics.frc2016.util.Commands;
import com.palyrobotics.frc2016.subsystems.Drive;
import com.palyrobotics.frc2016.robot.team254.lib.util.DriveSignal;

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
		drive.resetController();
		m_state = States.START;
	}

	@Override
	public Commands.Setpoints update(Commands commands) {
		Commands.Setpoints setpoints = commands.robotSetpoints;
		
		switch(m_state) {
		case START:
			System.out.println("Set setpoint: "+angle);
			drive.setGyroTurnAngleSetpoint(angle, maxVel);
			
			setpoints.currentRoutine = Commands.Routines.TURN_ANGLE;
			m_state = States.TURNING;
			break;
			
		case TURNING:
			if(drive.controllerOnTarget()) {
				m_state = States.DONE;
			}
			break;
			
		case DONE:
			drive.resetController();
			break;
		}
		
		return setpoints;
	}
	
	@Override
	public void cancel() {
		m_state = States.DONE;
		drive.setOpenLoop(DriveSignal.NEUTRAL);
		drive.resetController();
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