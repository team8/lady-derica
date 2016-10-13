package com.team254.lib.util;

import com.palyrobotics.frc2016.util.Dashboard;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public abstract class Subsystem implements Tappable {
	public Dashboard mDashboard = Dashboard.getInstance();
    String name;

    public Subsystem(String name) {
        this.name = name;
        SystemManager.getInstance().add(this);
    }

    public String getName() {
        return name;
    }

    public abstract void reloadConstants();
}
