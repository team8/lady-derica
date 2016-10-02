package com.palyrobotics.frc2016.behavior.routines;

import com.palyrobotics.frc2016.HardwareAdaptor;
import com.palyrobotics.frc2016.behavior.Commands;
import com.palyrobotics.frc2016.behavior.RobotSetpoints;
import com.palyrobotics.frc2016.subsystems.Drive;
import com.team254.lib.util.DriveSignal;

public class EncoderTurnAngleRoutine extends Routine {

	private Drive drive = HardwareAdaptor.kDrive;
	
	private double angle;
	private double maxVel;
	
	private EncoderTurnAngleStates m_state = EncoderTurnAngleStates.START;
	
	private enum EncoderTurnAngleStates {
		START, TURNING, DONE
	}
	
	public EncoderTurnAngleRoutine(double angle, double maxVel) {
		this.angle = angle;
		this.maxVel = maxVel;
	}
	
	@Override
	public void start() {
		drive.reset();
	}

	@Override
	public RobotSetpoints update(Commands commands, RobotSetpoints existing_setpoints) {
		RobotSetpoints setpoints = existing_setpoints;
		
		switch(m_state) {
		case START:
			drive.setEncoderTurnAngleSetpoint(angle, maxVel);
			
			setpoints.drive_routine_action = RobotSetpoints.DriveRoutineAction.ENCODER_TURN;
			m_state = EncoderTurnAngleStates.TURNING;
			break;
			
		case TURNING:
			if(drive.controllerOnTarget()) {
				m_state = EncoderTurnAngleStates.DONE;
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
		m_state = EncoderTurnAngleStates.DONE;
		drive.setOpenLoop(DriveSignal.NEUTRAL);
		drive.reset();
	}

	@Override
	public boolean isFinished() {
		return m_state == EncoderTurnAngleStates.DONE;
	}

	@Override
	public String getName() {
		return "EncoderTurnAngleRoutine";
	}
}