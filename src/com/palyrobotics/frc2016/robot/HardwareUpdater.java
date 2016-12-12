package com.palyrobotics.frc2016.robot;

import com.palyrobotics.frc2016.config.RobotState;
import com.palyrobotics.frc2016.subsystems.Breacher;
import com.palyrobotics.frc2016.subsystems.Catapult;
import com.palyrobotics.frc2016.subsystems.Drive;
import com.palyrobotics.frc2016.subsystems.Intake;
import com.palyrobotics.frc2016.subsystems.LowGoalShooter;
import com.palyrobotics.frc2016.subsystems.TyrShooter;

/**
 * Should only be used in robot package.
 */
class HardwareUpdater {
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
	HardwareUpdater(Drive mDrive, Catapult mCatapult, Intake mIntake, LowGoalShooter mLowGoalShooter) {
		this.mDrive = mDrive;
		this.mCatapult = mCatapult;
		this.mIntake = mIntake;
		this.mLowGoalShooter = mLowGoalShooter;
	}

	/**
	 * Hardware Updater for Tyr
	 * @param mDrive
	 * @param mTyrShooter
	 * @param mIntake
	 * @param mBreacher
	 */
	HardwareUpdater(Drive mDrive, TyrShooter mTyrShooter, Intake mIntake, Breacher mBreacher) {
		this.mDrive = mDrive;
		this.mTyrShooter = mTyrShooter;
		this.mIntake = mIntake;
		this.mBreacher = mBreacher;
	}

	/**
	 * Updates all the sensor data taken from the hardware
	 */
	void updateSensors() {
		RobotState robotState = Robot.getRobotState();
		robotState.shooter_potentiometer = HardwareAdapter.ShooterHardware.getInstance().kShooterPotentiometer.get();
	}

	/**
	 * Sets the output from all subsystems for the respective hardware
	 */
	void updateSubsystems() {
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
	
	private void updateDrivetrain() {
		HardwareAdapter.getInstance().getDrivetrain().kLeftDriveMotor.set(mDrive.getDriveSignal().leftMotor);
		HardwareAdapter.getInstance().getDrivetrain().kRightDriveMotor.set(mDrive.getDriveSignal().rightMotor);
	}
	
	private void updateIntake() {
		if(Robot.getRobotState().name == RobotState.RobotName.DERICA) {
			HardwareAdapter.getInstance().getIntake().kLeftIntakeMotor.set(mIntake.get()[0]);
			HardwareAdapter.getInstance().getIntake().kIntakeArmMotor.set(mIntake.get()[1]);
		} else {
			HardwareAdapter.getInstance().getIntake().kLeftIntakeMotor.set(mIntake.get()[0]);
			HardwareAdapter.getInstance().getIntake().kRightIntakeMotor.set(mIntake.get()[1]);
		}
	}
	
	private void updateCatapult() {
	}
	
	private void updateLowGoalShooter() {
		HardwareAdapter.getInstance().getLowGoalShooter().kLowGoalShooterMotor.set(mLowGoalShooter.get());
	}
	
	private void updateTyrShooter() {
		HardwareAdapter.getInstance().getShooter().kShooterMotor.set(mTyrShooter.getMotorOutput());
		HardwareAdapter.getInstance().getShooter().kLatchSolenoid.set(mTyrShooter.getSolenoidOutput()[0]);
		HardwareAdapter.getInstance().getShooter().kPistonSolenoid.set(mTyrShooter.getSolenoidOutput()[1]);
		HardwareAdapter.getInstance().getShooter().kGrabberSolenoid.set(mTyrShooter.getSolenoidOutput()[2]);
	}
	
	private void updateBreacher() {
		HardwareAdapter.getInstance().getBreacher().kBreacherMotor.set(mBreacher.getMotorOutput());
	}

}
