package com.palyrobotics.frc2016.auto.actions;

/**
 * Aligns to the goal
 * TODO: Everything
 * @author 
 *
 */
public class AutoAlignAction implements Action {

	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void done() {
		System.out.println("Finished auto align");
	}

	@Override
	public void start() {
		System.out.println("Starting auto align");
	}

}
