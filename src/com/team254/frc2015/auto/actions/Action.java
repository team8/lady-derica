package com.team254.frc2015.auto.actions;

import com.team254.frc2015.HardwareAdaptor;
import com.team254.frc2015.subsystems.Drive;
import com.team254.frc2015.subsystems.Intake;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

public abstract class Action {

    protected Drive drive = HardwareAdaptor.kDrive;
    protected PowerDistributionPanel pdp = HardwareAdaptor.kPDP;
    protected Intake intake = HardwareAdaptor.kIntake;

    public abstract boolean isFinished();

    public abstract void update();

    public abstract void done();

    public abstract void start();
}
