package com.palyrobotics.frc2016.subsystems;

import com.palyrobotics.frc2016.input.RobotState;
import com.palyrobotics.frc2016.robot.Robot;
import com.palyrobotics.frc2016.subsystems.controllers.EncoderTurnAngleController;
import com.palyrobotics.frc2016.subsystems.controllers.GyroTurnAngleController;
import com.palyrobotics.frc2016.subsystems.controllers.team254.DriveFinishLineController;
import com.palyrobotics.frc2016.subsystems.controllers.team254.DrivePathController;
import com.palyrobotics.frc2016.subsystems.controllers.team254.DriveStraightController;
import com.palyrobotics.frc2016.subsystems.controllers.team254.TurnInPlaceController;
import com.palyrobotics.frc2016.util.Constants;
import com.palyrobotics.frc2016.util.Subsystem;
import com.team254.lib.trajectory.Path;
import com.team254.lib.util.*;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

/**
 * Represents the drivetrain
 * Uses controllers or cheesydrivehelper to calculate DriveSignal
 */
public class Drive extends Subsystem implements Loop {

	public interface DriveController {
		DriveSignal update(Pose pose);
		Pose getCurrentSetpoint();

		boolean onTarget();
	}
	private DriveController m_controller = null;
	// Derica is always considered high gear
	public enum DriveGear {HIGH, LOW}
	public DriveGear mGear;
	// Encoder DPP
	protected final double m_inches_per_tick;
	protected final double m_wheelbase_width; // Get from CAD
	protected final double m_turn_slip_factor; // Measure empirically
	// Cache poses to not allocated at 200Hz
	private Pose m_cached_pose = new Pose(0, 0, 0, 0, 0, 0);
	// Cached robot state, updated by looper
	private RobotState m_cached_robot_state;
	// Stores output
	private DriveSignal mSignal = DriveSignal.NEUTRAL;

	public Drive(RobotState robotState) {
		super("drive");
		m_cached_robot_state = robotState;
		if(Robot.getRobotState().name == RobotState.RobotName.TYR) {
			m_wheelbase_width = 26.0;
			m_turn_slip_factor = 1.2;
			m_inches_per_tick = 0.184;
		}
		else {
			m_wheelbase_width = 22.0;
			m_turn_slip_factor = 1.2;
			m_inches_per_tick = 0.07033622;
			mGear = DriveGear.HIGH;
		}
	}

	/**
	 * @return DriveSignal
	 */
	public DriveSignal get() {
		return mSignal;
	}

	@Override
	public void onStart() {
	}

	@Override
	public void onStop() {
	}

	@Override
	public void onLoop() {
		if (!hasController()) {
			return;
		}
		setDriveOutputs(m_controller.update(getPhysicalPose()));
	}

	private void setDriveOutputs(DriveSignal signal) {
		mSignal = signal;
	}

	/**
	 * Allows shifting of gear - Note that Derica cannot shift gears
	 *
	 * @param targetGear Desired gear to shift to
	 * @return What the shifterSolenoid should be set to
	 */
	public DoubleSolenoid.Value setGear(DriveGear targetGear) {
		if (Robot.getRobotState().name == RobotState.RobotName.DERICA) {
			System.err.println("No gear shifting on Derica");
			return null;
		}
		switch (targetGear) {
			case HIGH:
				return Value.kForward;
			case LOW:
				return Value.kReverse;
		}
		return null;
	}

	public boolean isHighGear() {
		return mGear == DriveGear.HIGH;
	}

	public void setOpenLoop(DriveSignal signal) {
		m_controller = null;
		setDriveOutputs(signal);
	}

	public void setDistanceSetpoint(double distance) {
		setDistanceSetpoint(distance, Constants.kDriveMaxSpeedInchesPerSec);
	}
	public void setDistanceSetpoint(double distance, double velocity) {
		// 0 < vel < max_vel
		double vel_to_use = Math.min(Constants.kDriveMaxSpeedInchesPerSec, Math.max(velocity, 0));
		m_controller = new DriveStraightController(
				getPoseToContinueFrom(false),
				distance,
				vel_to_use);
	}
	
	public void setAutoAlignSetpoint(double heading) {
		// Check if already turning to that setpoint
		if(m_controller instanceof GyroTurnAngleController) {
//			if(m_controller.getCurrentSetpoint().getHeading()-getPhysicalPose().getHeading() != heading) {
//				// New auto align iteration
//				System.out.println("New auto align setpoint");
//				setGyroTurnAngleSetpoint(heading);
//			}
		} else {
			System.out.println("Started auto align controller");
			setGyroTurnAngleSetpoint(heading, 0.45);
		}
	}
	
	public void setTurnSetpoint(double heading) {
		setTurnSetpoint(heading, Constants.kTurnMaxSpeedRadsPerSec);
	}
	public void setTurnSetpoint(double heading, double velocity) {
		velocity = Math.min(Constants.kTurnMaxSpeedRadsPerSec, Math.max(velocity, 0));
		m_controller = new TurnInPlaceController(getPoseToContinueFrom(true), heading, velocity);
	}

	public void setEncoderTurnAngleSetpoint(double heading) {
		setEncoderTurnAngleSetpoint(heading, 1);
	}
	public void setEncoderTurnAngleSetpoint(double heading, double maxVel) {
		m_controller = new EncoderTurnAngleController(getPoseToContinueFrom(true), heading, maxVel);
	}
	
	public void setGyroTurnAngleSetpoint(double heading) {
		setGyroTurnAngleSetpoint(heading, 0.7);
	}
	public void setGyroTurnAngleSetpoint(double heading, double maxVel) {
		m_controller = new GyroTurnAngleController(getPoseToContinueFrom(true), heading, maxVel);
	}

	// Wipes current controller
	public void resetController() {
		m_controller = null;
	}

	public void setPathSetpoint(Path path) {
		resetController();
		m_controller = new DrivePathController(path);
	}

	public void setFinishLineSetpoint(double distance, double heading) {
		resetController();
		m_controller = new DriveFinishLineController(distance, heading, 1.0);
	}

	private Pose getPoseToContinueFrom(boolean for_turn_controller) {
		if (!for_turn_controller && m_controller instanceof TurnInPlaceController) {
			Pose pose_to_use = getPhysicalPose();
			pose_to_use.m_heading = ((TurnInPlaceController) m_controller).getHeadingGoal();
			pose_to_use.m_heading_velocity = 0;
			return pose_to_use;
		} else if (m_controller == null || (m_controller instanceof DriveStraightController && for_turn_controller)) {
			return getPhysicalPose();
		} else if (m_controller instanceof DriveFinishLineController) {
			return getPhysicalPose();
		} else if (m_controller.onTarget()) {
			return m_controller.getCurrentSetpoint();
		} else {
			return getPhysicalPose();
		}
	}

	/**
	 * @return The pose according to the current sensor state
	 */
	public Pose getPhysicalPose() {
		RobotState robotState = Robot.getRobotState();
		m_cached_pose = robotState.getDrivePose();
		return m_cached_pose;
	}

	public Drive.DriveController getController() {
		return m_controller;
	}

	@Override
	public void reloadConstants() {
		// TODO Auto-generated method stub

	}

	public boolean controllerOnTarget() {
		return m_controller != null && m_controller.onTarget();
	}

	public boolean hasController() {
		return m_controller != null;
	}
}