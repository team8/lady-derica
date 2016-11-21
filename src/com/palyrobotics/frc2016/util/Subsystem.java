package com.palyrobotics.frc2016.util;

import com.team254.lib.util.SystemManager;
import com.team254.lib.util.Tappable;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public abstract class Subsystem {
	public Dashboard mDashboard = Dashboard.getInstance();
	private String name;

	public Subsystem(String name) {
		this.name = name;
//		SystemManager.getInstance().add(this);
	}

	public String getName() {
		return name;
	}

	public abstract void reloadConstants();
}
