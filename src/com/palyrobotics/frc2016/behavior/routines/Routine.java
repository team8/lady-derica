package com.palyrobotics.frc2016.behavior.routines;

import com.palyrobotics.frc2016.behavior.RobotSetpoints;
import com.palyrobotics.frc2016.input.Commands;
import com.palyrobotics.frc2016.robot.HardwareAdaptor;
import com.palyrobotics.frc2016.subsystems.Drive;
import com.palyrobotics.frc2016.subsystems.Intake;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

public abstract class Routine {
    protected Drive drive = HardwareAdaptor.kDrive;
    protected Intake intake = HardwareAdaptor.kIntake;
    protected PowerDistributionPanel pdp = HardwareAdaptor.kPDP;
    
    // Called to start a routine
    public abstract void start();

    public abstract RobotSetpoints update(Commands commands, RobotSetpoints existing_setpoints);
    
    // Called to stop a routine
    public abstract void cancel();

    public abstract boolean isFinished();

    public abstract String getName();
}
