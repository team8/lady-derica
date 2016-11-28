package com.team254.lib.util;

import com.palyrobotics.frc2016.input.Commands;
import com.palyrobotics.frc2016.input.RobotState;

public interface Loop {
    public void onStart();
	public void update(Commands commands, RobotState robotState);
	public void onStop();
}
