package com.team254.frc2015.behavior.routines;

import com.team254.frc2015.HardwareAdaptor;
import com.team254.frc2015.behavior.Commands;
import com.team254.frc2015.behavior.RobotSetpoints;
import com.team254.frc2015.subsystems.Drive;
import com.team254.frc2015.subsystems.Intake;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

public abstract class Routine {
    protected Drive drive = HardwareAdaptor.kDrive;
    protected Intake intake = HardwareAdaptor.kIntake;
    protected PowerDistributionPanel pdp = HardwareAdaptor.kPDP;
    
    // Called when the next routine isn't null, so reset for next to work
    public abstract void reset();

    public abstract RobotSetpoints update(Commands commands, RobotSetpoints existing_setpoints);
    
    // Called when this routine needs to end immediately (may not have a next routine)
    public abstract void cancel();

    public abstract boolean isFinished();

    public abstract String getName();
}
