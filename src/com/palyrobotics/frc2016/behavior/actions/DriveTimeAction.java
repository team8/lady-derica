package com.palyrobotics.frc2016.behavior.actions;

import com.palyrobotics.frc2016.robot.team254.lib.util.DriveSignal;

import edu.wpi.first.wpilibj.Timer;

/**
 * Drives for a specific period of time
 * @author Nihar, Eric
 *
 */
public class DriveTimeAction implements Action {

	private Timer timer = new Timer();
	
	private double runTime;
	private double leftSpeed = 0.5;
	private double rightSpeed = 0.5;
	
	/**
	 * Drives forward at (0.5, 0.5) for a specified number of seconds.
	 * 
	 * @param runTime how long this action runs
	 */
	public DriveTimeAction(double runTime) {
		this.runTime = runTime;
	}
	
	/**
	 * Drives forward at (leftSpeed, rightSpeed) for a specified number of seconds.
	 * 
	 * @param runTime how long this action runs
	 * @param leftSpeed the speed of the left motor
	 * @param rightSpeed the speed of the right motor
	 */
	public DriveTimeAction(double runTime, double leftSpeed, double rightSpeed) {
		this.runTime = runTime;
		this.leftSpeed = leftSpeed;
		this.rightSpeed = rightSpeed;
	}
	
	@Override
	public boolean isFinished() {
		if(timer.get() < runTime) {
			return false;
		}
		else return true;
	}

	@Override
	public void update() {
		System.out.println("Drive Time:" + timer.get());
	}

	@Override
	public void done() {
		System.out.println("TimerDriveForwardAction done");
		drive.setOpenLoop(DriveSignal.NEUTRAL);
	}

	@Override
	public void start() {
		System.out.println("Starting TimerDriveForwardAction");
		timer.reset();
		timer.start();
		drive.setOpenLoop(new DriveSignal(leftSpeed, rightSpeed*.95));
	}
}