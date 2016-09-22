package com.palyrobotics.frc2016;
import com.palyrobotics.frc2016.subsystems.Drive;
import com.palyrobotics.lib.util.DriveSignal;

import edu.wpi.first.wpilibj.DriverStation;

public class PorportionalDriveHelper {

    private Drive drive;
    private DriveSignal signal = DriveSignal.NEUTRAL;

    public PorportionalDriveHelper(Drive drive) {
        this.drive = drive;
    }

    public void pDrive(double throttle, double wheel) {
        if (DriverStation.getInstance().isAutonomous()) {
            return;
        }
        
        if (drive.hasController()) {
        	return;
        }

        double angularPower = Math.abs(throttle) * wheel;
        //double angularPower = wheel;
        double rightPwm = throttle;
        double leftPwm = throttle;
        leftPwm += angularPower;
        rightPwm -= angularPower;

        signal.leftMotor = leftPwm;
        signal.rightMotor = rightPwm;
        drive.setOpenLoop(signal);
    }

}