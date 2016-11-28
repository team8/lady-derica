package com.palyrobotics.frc2016.robot;

import com.palyrobotics.frc2016.input.RobotState;
import com.palyrobotics.frc2016.subsystems.Breacher;
import com.palyrobotics.frc2016.subsystems.Catapult;
import com.palyrobotics.frc2016.subsystems.Drive;
import com.palyrobotics.frc2016.subsystems.Intake;
import com.palyrobotics.frc2016.subsystems.LowGoalShooter;
import com.palyrobotics.frc2016.subsystems.TyrShooter;

public class HardwareUpdater {
	private Drive mDrive;
	private Catapult mCatapult;
	private Intake mIntake;
	private LowGoalShooter mLowGoalShooter;
	private TyrShooter mTyrShooter;
	private Breacher mBreacher;
	
	/**
	 * Hardware Updater for Derica
	 * Updates Drive, Catapult, Intake, LowGoalShooter
	 * @param mDrive
	 * @param mCatapult
	 * @param mIntake
	 * @param mLowGoalShooter
	 */
	public HardwareUpdater(Drive mDrive, Catapult mCatapult, Intake mIntake, LowGoalShooter mLowGoalShooter) {
		this.mDrive = mDrive;
		this.mCatapult = mCatapult;
		this.mIntake = mIntake;
		this.mLowGoalShooter = mLowGoalShooter;
	}
	
	public HardwareUpdater(Drive mDrive, TyrShooter mTyrShooter, Intake mIntake, Breacher mBreacher) {
		this.mDrive = mDrive;
		this.mTyrShooter = mTyrShooter;
		this.mIntake = mIntake;
		this.mBreacher = mBreacher;
	}
	
	public void updateSubsystems() {
		if(Robot.getRobotState().name == RobotState.RobotName.DERICA) {
			updateDrivetrain();
			updateCatapult();
			updateIntake();
		} else {
			updateDrivetrain();
			updateTyrShooter();
			updateBreacher();
			updateIntake();
		}
	}
	
	public void updateDrivetrain() {
		HardwareAdaptor.getInstance().getDrivetrain().kLeftDriveMotor.set(mDrive.getDriveSignal().leftMotor);
		HardwareAdaptor.getInstance().getDrivetrain().kRightDriveMotor.set(mDrive.getDriveSignal().rightMotor);
	}
	
	public void updateIntake() {
		if(Robot.getRobotState().name == RobotState.RobotName.DERICA) {
			HardwareAdaptor.getInstance().getIntake().kLeftIntakeMotor.set(mIntake.get()[0]);
			HardwareAdaptor.getInstance().getIntake().kIntakeArmMotor.set(mIntake.get()[1]);
		} else {
			HardwareAdaptor.getInstance().getIntake().kLeftIntakeMotor.set(mIntake.get()[0]);
			HardwareAdaptor.getInstance().getIntake().kRightIntakeMotor.set(mIntake.get()[1]);
		}
	}
	
	public void updateCatapult() {
	}
	
	public void updateLowGoalShooter() {
		HardwareAdaptor.getInstance().getLowGoalShooter().kLowGoalShooterMotor.set(mLowGoalShooter.get());
	}
	
	public void updateTyrShooter() {
		HardwareAdaptor.getInstance().getShooter().kShooterMotor.set(mTyrShooter.getMotorOutput());
		HardwareAdaptor.getInstance().getShooter().kLatchSolenoid.set(mTyrShooter.getSolenoidOutput()[0]);
		HardwareAdaptor.getInstance().getShooter().kPistonSolenoid.set(mTyrShooter.getSolenoidOutput()[1]);
		HardwareAdaptor.getInstance().getShooter().kGrabberSolenoid.set(mTyrShooter.getSolenoidOutput()[2]);
	}
	
	public void updateBreacher() {
		HardwareAdaptor.getInstance().getBreacher().kBreacherMotor.set(mBreacher.getMotorOutput());
	}

}
