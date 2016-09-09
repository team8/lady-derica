package com.palyrobotics.frc2016.auto;

import com.palyrobotics.frc2016.auto.actions.Action;

public class AutoSimultaneous extends AutoMode {

	Action[] actions;
	
	public AutoSimultaneous(Action... actions) {
		this.actions = actions;
	}
	
	@Override
	protected void routine() throws AutoModeEndedException {
		
		for (Action a : actions) {
			Thread t = new Thread() {
				@Override
				public void run() {
					try {
						runAction(a);
					} catch (AutoModeEndedException e) {	
						e.printStackTrace();
					}
				}
			};
			t.start();
		}
	}

	@Override
	public void prestart() {	
		System.out.println("Starting simultaneous auto");
	}

}
