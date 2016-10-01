package com.palyrobotics.frc2016.behavior.routines;

import java.util.Optional;

import com.palyrobotics.frc2016.Constants;
import com.palyrobotics.frc2016.HardwareAdaptor;
import com.palyrobotics.frc2016.Robot;
import com.palyrobotics.frc2016.behavior.Commands;
import com.palyrobotics.frc2016.behavior.RobotSetpoints;
import com.palyrobotics.frc2016.subsystems.Drive;
import com.team254.lib.util.DriveSignal;

public class EncoderTurnAngleRoutine extends Routine {

	private Drive drive = HardwareAdaptor.kDrive;
	
	private double angle;
	private double maxVel;
	private double leftTarget;
	private double rightTarget;
	private double kDegreeToDistance;
	
	private EncoderTurnAngleStates m_state = EncoderTurnAngleStates.START;
	private boolean m_is_new_state = true;
	
	private double leftP;
	private double leftI;
	private double leftD;
	private double previousLeftError;
	
	private double rightP;
	private double rightI;
	private double rightD;
	private double previousRightError;
	
	private enum EncoderTurnAngleStates {
		START, TURNING, DONE
	}
	
	public EncoderTurnAngleRoutine(double angle, double maxVel) {
		this.angle = angle;
		this.maxVel = maxVel;
		
		if(Robot.name == Robot.RobotName.DERICA) {
			kDegreeToDistance = Constants.kDericaDegreeToDistance;
		} else if(Robot.name == Robot.RobotName.TYR) {
			kDegreeToDistance = Constants.kTyrDegreeToDistance;
		}
	}
	
	@Override
	public void start() {
		drive.reset();
	}

	@Override
	public RobotSetpoints update(Commands commands, RobotSetpoints existing_setpoints) {
		RobotSetpoints setpoints = existing_setpoints;
		EncoderTurnAngleStates new_state = m_state;
		
		switch(m_state) {
		case START:
			leftTarget = drive.m_left_encoder.getDistance() + angle * kDegreeToDistance;
			rightTarget = drive.m_right_encoder.getDistance() - angle * kDegreeToDistance;
			
			setpoints.drive_routine_action = RobotSetpoints.DriveRoutineAction.ENCODER_TURN;
			new_state = EncoderTurnAngleStates.TURNING;
			break;
			
		case TURNING:
			if(m_is_new_state) {
				leftI = 0;
				leftD = 0;
				previousLeftError = leftTarget - drive.m_left_encoder.getDistance();
				
				rightI = 0;
				rightD = 0;
				previousRightError = rightTarget - drive.m_right_encoder.getDistance();
			}
			
			leftP = leftTarget - drive.m_left_encoder.getDistance();
			rightP = rightTarget - drive.m_right_encoder.getDistance();
			
			leftI = leftI + leftP * Constants.kLooperDt;
			rightI = rightI + rightP * Constants.kLooperDt;
			
			leftD = (leftP - previousLeftError)/Constants.kLooperDt;
			rightD = (rightP - previousRightError)/Constants.kLooperDt;
			
			double leftSpeed = Math.max(-maxVel, Math.min(
					maxVel, Constants.kEncoderTurnKp*leftP + Constants.kEncoderTurnKi*leftI + Constants.kEncoderTurnKd*leftD));
			double rightSpeed = Math.max(-maxVel, Math.min(
					maxVel, Constants.kEncoderTurnKp*rightP + Constants.kEncoderTurnKi*rightI + Constants.kEncoderTurnKd*rightD));
			
			setpoints.encoder_turn_setpoint_left = Optional.of(leftSpeed);
			setpoints.encoder_turn_setpoint_right = Optional.of(rightSpeed);
			
			previousLeftError = leftP;
			previousRightError = rightP;
			
			if(isOnTarget()) {
				new_state = EncoderTurnAngleStates.DONE;
			}
			break;
			
		case DONE:
			drive.reset();
			break;
		}
		
		m_is_new_state = false;
		if(new_state != m_state) {
			m_state = new_state;
			m_is_new_state = true;
		}
		return setpoints;
	}

	private boolean isOnTarget() {
		if(Math.abs(leftP/kDegreeToDistance) < Constants.kEncoderTurnError && Math.abs(rightP/kDegreeToDistance) < Constants.kEncoderTurnError
				&& leftD == 0 && rightD == 0) {
			return true;
		} else return false;
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