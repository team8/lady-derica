package com.palyrobotics.frc2016.subsystems;

import com.palyrobotics.frc2016.Constants;
import com.palyrobotics.frc2016.Robot;
import com.palyrobotics.frc2016.Robot.RobotName;
import com.palyrobotics.frc2016.subsystems.controllers.DriveFinishLineController;
import com.palyrobotics.frc2016.subsystems.controllers.DrivePathController;
import com.palyrobotics.frc2016.subsystems.controllers.DriveStraightController;
import com.palyrobotics.frc2016.subsystems.controllers.TurnInPlaceController;
import com.palyrobotics.lib.trajectory.Path;
import com.palyrobotics.lib.util.*;

//import com.team254.lib.util.gyro.GyroThread;
import edu.wpi.first.wpilibj.Encoder;

public class Drive extends Subsystem implements Loop {

	public interface DriveController {
		DriveSignal update(Pose pose);

		Pose getCurrentSetpoint();

		public boolean onTarget();

	}

	public CheesySpeedController m_left_motor;
	public CheesySpeedController m_right_motor;
	public Encoder m_left_encoder;
	public Encoder m_right_encoder;
	
	private DriveController m_controller = null;

	// Encoder DPP
	protected final double m_inches_per_tick;

	protected final double m_wheelbase_width; // Get from CAD
	protected final double m_turn_slip_factor; // Measure empirically
	private Pose m_cached_pose = new Pose(0, 0, 0, 0, 0, 0); // Don't allocate poses at 200Hz!

	public Drive(String name, CheesySpeedController left_drive,
			CheesySpeedController right_drive, Encoder left_encoder,
			Encoder right_encoder) {
		super(name);
		if(Robot.name == RobotName.TYR) {
			m_wheelbase_width = 26.0;
			m_turn_slip_factor = 1.2;
			// TODO: Encoder DPP's
			m_inches_per_tick = 1;
		}
		else {
			m_wheelbase_width = 22.0;
			m_turn_slip_factor = 1.2;
			// TODO: Encoder DPP's
			m_inches_per_tick = 1;
		}
		this.m_left_motor = left_drive;
		this.m_right_motor = right_drive;
		this.m_left_encoder = left_encoder;
		this.m_right_encoder = right_encoder;
		this.m_left_encoder.setDistancePerPulse(m_inches_per_tick);
		this.m_right_encoder.setDistancePerPulse(m_inches_per_tick);
		//        this.m_gyro = gyro;
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

	public void setTurnSetPoint(double heading) {
		setTurnSetPoint(heading, Constants.kTurnMaxSpeedRadsPerSec);
	}

	public void setTurnSetPoint(double heading, double velocity) {
		velocity = Math.min(Constants.kTurnMaxSpeedRadsPerSec, Math.max(velocity, 0));
		m_controller = new TurnInPlaceController(getPoseToContinueFrom(true), heading, velocity);
	}

	public void reset() {
		m_left_encoder.reset();
		m_right_encoder.reset();
		m_controller = null;
	}

	public void setPathSetpoint(Path path) {
		reset();
		m_controller = new DrivePathController(path);
	}

	public void setFinishLineSetpoint(double distance, double heading) {
		reset();
		m_controller = new DriveFinishLineController(distance, heading, 1.0);
	}

	@Override
	public void getState(StateHolder states) {
		//        states.put("gyro_angle", m_gyro.getAngle());
		states.put("left_encoder", m_left_encoder.getDistance());
		states.put("left_encoder_rate", m_left_encoder.getRate());
		states.put("right_encoder_rate", m_right_encoder.getRate());
		states.put("right_encoder", m_right_encoder.getDistance());

		Pose setPointPose = m_controller == null ? getPhysicalPose(): m_controller.getCurrentSetpoint();
				states.put("drive_set_point_pos",
					DriveStraightController.encoderDistance(setPointPose));
				states.put("turn_set_point_pos", setPointPose.getHeading());
				states.put("left_signal", m_left_motor.get());
				states.put("right_signal", m_right_motor.get());
				states.put("on_target", (m_controller != null && m_controller.onTarget()) ? 1.0 : 0.0);
	}
	
	@Override
	public void onLoop() {
		if (m_controller == null) {
			return;
		}
		setDriveOutputs(m_controller.update(getPhysicalPose()));
	}
	
	@Override
	public void onStart() {
	}
	
	@Override
	public void onStop() {
	}
	private void setDriveOutputs(DriveSignal signal) {
		m_left_motor.set(signal.leftMotor);
		m_right_motor.set(-signal.rightMotor);
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
		m_cached_pose.reset(
				m_left_encoder.getDistance(),
				m_right_encoder.getDistance(),
				m_left_encoder.getRate(),
				m_right_encoder.getRate(),
				0,
				0);
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
