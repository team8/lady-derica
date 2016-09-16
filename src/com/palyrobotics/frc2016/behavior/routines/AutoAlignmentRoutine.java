package com.palyrobotics.frc2016.behavior.routines;

import com.palyrobotics.frc2016.behavior.Commands;
import com.palyrobotics.frc2016.behavior.RobotSetpoints;
import com.palyrobotics.lib.util.DriveSignal;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class AutoAlignmentRoutine extends Routine{
	
	public enum States {
		START, ALIGN, DONE
	}

	public States m_state = States.START;
	private NetworkTable table = NetworkTable.getTable("visiondata");
    RobotSetpoints setpoints;
    
	@Override
	public void reset() {
		m_state = States.START;
	}

	@Override
	public RobotSetpoints update(Commands commands, RobotSetpoints existing_setpoints) {
		RobotSetpoints setpoints = existing_setpoints;
		States new_state = m_state;
		switch(m_state) {
		case START: 
			new_state = States.ALIGN;
		case ALIGN:
			if(table.getNumber("skewangle", 100000) > 3) {
				existing_setpoints.auto_alignment_action = RobotSetpoints.AutoAlignmentAction.ALIGN;
			} else {
				new_state = States.DONE;
			}
			break;
		case DONE:
			drive.reset();
			existing_setpoints.auto_alignment_action = RobotSetpoints.AutoAlignmentAction.NONE;
		}
		if (new_state != m_state) {
            m_state = new_state;
        }
		return existing_setpoints;
	}

	@Override
	public void cancel() {
		m_state = States.START;
		drive.setOpenLoop(new DriveSignal(0, 0));
		drive.reset();
	}

	@Override
	public boolean isFinished() {
		return m_state == States.DONE;
	}

	@Override
	public String getName() {
		return "Auto Alignment Routine";
	}

}
