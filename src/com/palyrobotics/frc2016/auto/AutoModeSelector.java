package com.palyrobotics.frc2016.auto;

import org.json.simple.JSONArray;

import com.palyrobotics.frc2016.auto.modes.CrossBDHighGoalMode;
import com.palyrobotics.frc2016.auto.modes.DoNothingAutoMode;
import com.palyrobotics.frc2016.auto.modes.DriveForwardAutoMode;
import com.palyrobotics.frc2016.auto.modes.LowBarHighGoalAutoMode;
import com.palyrobotics.frc2016.auto.modes.TrajectoryAutoMode;
import com.palyrobotics.frc2016.auto.modes.WaitForwardBackwardAutoMode;

import java.util.ArrayList;

public class AutoModeSelector {
	private static AutoModeSelector instance = null;
	private ArrayList<AutoMode> autoModes = new ArrayList<AutoMode>();
	int selectedIndex = 3;
	public static AutoModeSelector getInstance() {
		if (instance == null) {
			instance = new AutoModeSelector();
		}
		return instance;
	}

	/**
	 * Add an AutoMode to list to choose from
	 * @param auto AutoMode to add
	 */
	public void registerAutonomous(AutoMode auto) {
		autoModes.add(auto);
	}

	public AutoModeSelector() {
		registerAutonomous(new DoNothingAutoMode());
		registerAutonomous(new DriveForwardAutoMode());
		registerAutonomous(new WaitForwardBackwardAutoMode(3.0, 3.0, -200));
		registerAutonomous(new TrajectoryAutoMode());
		registerAutonomous(new LowBarHighGoalAutoMode());
		registerAutonomous(new CrossBDHighGoalMode(false));
	}

	/**
	 * Get the currently selected AutoMode
	 * @return AutoMode currently selected
	 */
	public AutoMode getAutoMode() {
		return autoModes.get(selectedIndex);
	}

	/**
	 * Get the AutoMode at specified index
	 * @param index index of desired AutoMode
	 * @return AutoMode at specified index
	 */
	public AutoMode getAutoMode(int index) {
		return autoModes.get(index);
	}

	/**
	 * Gets the names of all registered AutoModes
	 * @return ArrayList of AutoModes string name
	 * @see AutoMode#toString()
	 */
	public ArrayList<String> getAutoModeList() {
		ArrayList<String> list = new ArrayList<String>();
		for (AutoMode autoMode : autoModes) {
			list.add(autoMode.toString());
		}
		return list;
	}

	public JSONArray getAutoModeJSONList() {
		JSONArray list = new JSONArray();
		list.addAll(getAutoModeList());
		return list;
	}

	/**
	 * Attempt to set
	 * @return false if unable to find appropriate AutoMode
	 * @see AutoMode#toString()
	 */
	public boolean setAutoModeByName(String name) {
		int numOccurrences = 0;
		int index = -1;
		for(int i=0; i<autoModes.size(); i++) {
			if(autoModes.get(i).toString() == name) {
				numOccurrences++;
				index = i;
			}
		}
		if(numOccurrences == 1) {
			setAutoModeByIndex(index);
			return true;
		} else if(numOccurrences == 0) {
			System.out.println("Couldn't find AutoMode "+name);
		} else {
			System.out.println("Found multiple AutoModes "+name);
		}
		System.err.println("Didn't select AutoMode");
		return false;
	}

	public void setFromDashboard(int index) {
		setAutoModeByIndex(index);
	}

	private void setAutoModeByIndex(int which) {
		if (which < 0 || which >= autoModes.size()) {
			which = 0;
		}
		selectedIndex = which;
		System.out.println("Selected AutoMode "+autoModes.get(selectedIndex).toString());
	}

}
