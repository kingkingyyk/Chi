package Chi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class SchedulingThread extends Thread {
	public boolean stopQueued=false;
	public HashMap<String,SchedulingData> data;
	
	public void requestStop () {
		this.stopQueued=true;
	}
	
	public class OnRegularScheduleCreate implements DatabaseRegularSchedule.OnCreateAction {
		@Override
		public void run(String sn, String an, int day, String rn, boolean ao, int pr, boolean en) {
			SchedulingData d=new SchedulingDataRegular(sn,an,day,rn,ao,pr,en);
			data.put(sn,d);
			FrameOngoingSchedules.refresh();
		}
	}
	
	public class OnRegularScheduleUpdate implements DatabaseRegularSchedule.OnUpdateAction {
		@Override
		public void run(String oldSN, String sn, String an, int day, String rn, boolean ao, int pr, boolean en) {
			SchedulingDataRegular d=(SchedulingDataRegular)data.get(oldSN);
			if (!d.getName().equals(sn)) {
				d.setName(sn);
				data.remove(oldSN);
				data.put(sn,d);
			}
			d.setActuatorName(an);
			d.setDay(day);
			d.setTimeRule(rn);
			d.setActuatorFlag(ao);
			if (d.isEnabled()!=en) {
				d.setEnabled(en);
				if (en) data.put(sn,d);
				else data.remove(sn);
			}
			FrameOngoingSchedules.refresh();
		}
	}
	
	public class OnRegularScheduleDelete implements DatabaseRegularSchedule.OnDeleteAction {
		@Override
		public void run(String sn) {
			SchedulingData d=data.get(sn);
			if (d!=null) {
				data.remove(sn);
				FrameOngoingSchedules.refresh();
			}
		}
	}
	
	public class OnRegularScheduleStart extends ScheduleOnStartAction {
		@Override
		public void run() {
			System.out.println("START : "+this.dat.actuatorName+" SET STATUS TO "+this.dat.actuatorFlag);
		}
	}
	
	public class OnRegularScheduleEnd extends ScheduleOnEndAction {
		@Override
		public void run() {
			System.out.println("END : "+this.dat.actuatorName+" SET STATUS TO "+!this.dat.actuatorFlag);
			FrameOngoingSchedules.refresh();
		}
	}
	
	public void run () {
		this.data=new HashMap<>();
		DatabaseRegularSchedule.registerOnCreateAction(new OnRegularScheduleCreate());
		DatabaseRegularSchedule.registerOnUpdateAction(new OnRegularScheduleUpdate());
		DatabaseRegularSchedule.registerOnDeleteAction(new OnRegularScheduleDelete());
		OnRegularScheduleStart orss=new OnRegularScheduleStart();
		OnRegularScheduleEnd orse=new OnRegularScheduleEnd();
		
		ResultSet rs=DatabaseRegularSchedule.getRegularSchedules();
		try {
			while (rs.next()) {
				SchedulingDataRegular d=new SchedulingDataRegular(rs.getString("ScheduleName"),rs.getString("ActuatorName"),rs.getInt("DayMask"),rs.getString("Rule"),rs.getBoolean("ActuatorOn"),rs.getInt("Priority"),rs.getBoolean("Enabled"));
				d.registerOnStartFunc(orss);
				d.registerOnEndFunc(orse);
				data.put(d.getName(),d);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		while (true) {
			if (stopQueued) {
				stopQueued=false;
				SchedulingServer.notifyStop();
			}
			try { Thread.sleep(1000);
			} catch (InterruptedException e) {}
		}
	}
}
