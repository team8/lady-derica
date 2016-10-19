package com.palyrobotics.frc2016.auto.actions;

/**
 * Waits for drivetrain controller to reach it's target, or time out
 * @author Team 254
 *
 */
public class WaitForDriveControllerAction extends TimeoutAction {
    public WaitForDriveControllerAction(double timeout) {
        super(timeout);
    }

    @Override
    public boolean isFinished() {
        return drive.controllerOnTarget() || super.isFinished();
    }

}
