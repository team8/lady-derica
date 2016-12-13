package com.palyrobotics.frc2016.auto;

import com.palyrobotics.frc2016.behavior.actions.*;
import com.palyrobotics.frc2016.robot.HardwareAdaptor;
import com.palyrobotics.frc2016.subsystems.*;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

public abstract class AutoMode extends AutoModeBase {

    protected Drive drive = HardwareAdaptor.kDrive;
    protected PowerDistributionPanel pdp = HardwareAdaptor.kPDP;
    protected Intake intake = HardwareAdaptor.kIntake;

    public void waitTime(double seconds) throws AutoModeEndedException {
        runAction(new TimeoutAction(seconds));
    }    
}
