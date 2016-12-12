package com.palyrobotics.frc2016.behavior.routines;

import com.palyrobotics.frc2016.config.Commands;
import com.palyrobotics.frc2016.robot.HardwareAdaptor;

/**
 * Abstract superclass for a routine, which specifies an autonomous series of actions in tele-op <br />
 * Each routine takes in Commands and returns modified Setpoints
 * @author Nihar, Team 254
 *
 */
public abstract class Routine {
    // Called to start a routine
    public abstract void start();

    public abstract Commands.Setpoints update(Commands commands);
    
    // Called to stop a routine
    public abstract void cancel();

    public abstract boolean isFinished();

    public abstract String getName();
}
