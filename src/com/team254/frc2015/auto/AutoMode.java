package com.team254.frc2015.auto;

import com.team254.frc2015.HardwareAdaptor;
import com.team254.frc2015.auto.actions.*;
import com.team254.frc2015.subsystems.*;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

public abstract class AutoMode extends AutoModeBase {

    protected Drive drive = HardwareAdaptor.kDrive;
    protected PowerDistributionPanel pdp = HardwareAdaptor.kPDP;
    protected Intake intake = HardwareAdaptor.kIntake;

    public void waitTime(double seconds) throws AutoModeEndedException {
        runAction(new TimeoutAction(seconds));
    }
}
