package com.palyrobotics.frc2016.auto.actions;

/**
 * Used for Tyr to ensure shooter is down
 * @author Nihar
 *
 */
public class TurnAngleAutoAction implements Action {
	
	private double m_heading;
	
	public TurnAngleAutoAction(double heading) {
		this.m_heading = heading;
	}
	
	@Override
	public boolean isFinished() {
		return drive.controllerOnTarget();
	}

	@Override
	public void update() {
		
	}

	@Override
	public void done() {
	
	}

	@Override
	public void start() {
		drive.setTurnSetpoint(m_heading);
	}

}
