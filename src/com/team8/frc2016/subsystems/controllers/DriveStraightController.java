package com.team8.frc2016.subsystems.controllers;

import static com.team8.lib.trajectory.TrajectoryFollower.TrajectorySetpoint;

import com.team8.frc2016.Constants;
import com.team8.frc2016.subsystems.Drive;
import com.team8.lib.trajectory.TrajectoryFollower;
import com.team8.lib.util.DriveSignal;
import com.team8.lib.util.Pose;
import com.team8.lib.util.SynchronousPID;

public class DriveStraightController implements Drive.DriveController {

    private TrajectoryFollowingPositionController mDistanceController;
    private SynchronousPID mTurnPid;
    private Pose mSetpointRelativePose;

    public DriveStraightController(Pose priorSetpoint, double goalSetpoint, double maxVelocity) {
        TrajectoryFollower.TrajectoryConfig config = new TrajectoryFollower.TrajectoryConfig();
        config.dt = Constants.kControlLoopsDt;
        config.max_acc = Constants.kDriveMaxAccelInchesPerSec2;
        config.max_vel = maxVelocity;

        mDistanceController = new TrajectoryFollowingPositionController(
                Constants.kDrivePositionKp,
                Constants.kDrivePositionKi,
                Constants.kDrivePositionKd,
                Constants.kDrivePositionKv,
                Constants.kDrivePositionKa,
                Constants.kDriveOnTargetError,
                config);

        TrajectorySetpoint initialSetpoint = new TrajectorySetpoint();
        initialSetpoint.pos = encoderDistance(priorSetpoint);
        initialSetpoint.vel = encoderVelocity(priorSetpoint);
        mDistanceController.setGoal(initialSetpoint, goalSetpoint);

        mTurnPid = new SynchronousPID();
        mTurnPid.setPID(
                Constants.kDriveStraightKp,
                Constants.kDriveStraightKi,
                Constants.kDriveStraightKd);
        mTurnPid.setSetpoint(priorSetpoint.getHeading());
        mSetpointRelativePose = new Pose(
                priorSetpoint.getLeftDistance(),
                priorSetpoint.getRightDistance(),
                0,
                0,
                priorSetpoint.getHeading(),
                priorSetpoint.getHeadingVelocity());
    }

    @Override
    public DriveSignal update(Pose currentPose) {
        mDistanceController.update(
                (currentPose.getLeftDistance() + currentPose.getRightDistance()) / 2.0,
                (currentPose.getLeftVelocity() + currentPose.getRightVelocity()) / 2.0);
        double throttle = mDistanceController.get();
        double turn = mTurnPid.calculate(currentPose.getHeading());

        return new DriveSignal(throttle + turn, throttle - turn);
    }

    @Override
    public Pose getCurrentSetpoint() {
        TrajectorySetpoint trajectorySetpoint = mDistanceController.getSetpoint();
        double dist = trajectorySetpoint.pos;
        double velocity = trajectorySetpoint.vel;
        return new Pose(
                mSetpointRelativePose.getLeftDistance() + dist,
                mSetpointRelativePose.getRightDistance() + dist,
                mSetpointRelativePose.getLeftVelocity() + velocity,
                mSetpointRelativePose.getRightVelocity() + velocity,
                mSetpointRelativePose.getHeading(),
                mSetpointRelativePose.getHeadingVelocity());
    }

    public static double encoderVelocity(Pose pose) {
        return (pose.getLeftVelocity() + pose.getRightVelocity()) / 2.0;
    }

    public static double encoderDistance(Pose pose) {
        return (pose.getLeftDistance() + pose.getRightDistance()) / 2.0;
    }

    @Override
    public boolean onTarget() {
        return mDistanceController.isOnTarget();
    }

}
