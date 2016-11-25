package com.palyrobotics.frc2016.util;

import com.palyrobotics.frc2016.input.Commands;
import com.palyrobotics.frc2016.input.RobotState;
import com.team254.lib.util.SystemManager;
import com.team254.lib.util.Tappable;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public abstract class Subsystem {
	protected Dashboard mDashboard = Dashboard.getInstance();
	private String name;

	public Subsystem(String name) {
		this.name = name;
//		SystemManager.getInstance().add(this);
	}
	// Updates the subsystem with current commands and state
	public abstract void update(Commands commands, RobotState robotState);

	public String getName() {
		return name;
	}

//	public abstract void reloadConstants();
}
