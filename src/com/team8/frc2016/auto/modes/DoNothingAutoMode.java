package com.team8.frc2016.auto.modes;

import com.team8.frc2016.auto.AutoMode;
import com.team8.frc2016.auto.AutoModeEndedException;

public class DoNothingAutoMode extends AutoMode {
    @Override
    protected void routine() throws AutoModeEndedException {

    }

    @Override
    public void prestart() {
    	System.out.println("Starting Do Nothing Auto Mode");
    }
}
