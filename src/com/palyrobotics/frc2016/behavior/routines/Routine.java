package com.palyrobotics.frc2016.behavior.routines;

import com.palyrobotics.frc2016.config.Commands;
import com.palyrobotics.frc2016.subsystems.*;
import com.palyrobotics.frc2016.util.Subsystem;

import java.util.ArrayList;

/**
 * Abstract superclass for a routine, which specifies an autonomous series of actions in tele-op <br />
 * Each routine takes in Commands and returns modified Setpoints
 * @author Nihar; Team 254
 *
 */
public abstract class Routine {
    /**
     * Keeps access to all subsystems to modify their output and read their status like
     * {@link Drive#controllerOnTarget()}
     */
    protected final Drive drive = Drive.getInstance();
    protected final Intake intake = Intake.getInstance();
    protected final Breacher breacher = Breacher.getInstance();
    protected final Catapult catapult = Catapult.getInstance();
    protected final LowGoalShooter lowGoalShooter = LowGoalShooter.getInstance();
    protected final TyrShooter tyrShooter = TyrShooter.getInstance();

    // Store subsystems which are required by this routine, preventing routines from overlapping
    public abstract Subsystem[] getRequiredSubsystems();
    // Called to start a routine
    public abstract void start();
    // Update method modifies the setpoints
    public abstract Commands.Setpoints update(Commands commands);
    // Called to stop a routine
    public abstract void cancel();
    // Notifies routine manager when routine is complete
    public abstract boolean isFinished();
    // Force override of getName()
    public abstract String getName();
}
