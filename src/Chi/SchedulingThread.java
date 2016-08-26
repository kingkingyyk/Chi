package Chi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;

public class SchedulingThread extends Thread {
	public boolean stopQueued=false;
	public HashMap<String,SchedulingData> data;
	
	public void requestStop () {
		this.stopQueued=true;
	}
	
	public class OnActuatorUpdate implements DatabaseActuator.OnUpdateAction {
		@Override
		public void run(String oldN, String n, String u) {
			for (SchedulingData d : data.values()) {
				if (d.getActuatorName().equals(oldN)) {
					d.setActuatorName(n);
				}
			}
			FrameOngoingSchedules.refresh();
		}
	}
	
	public class OnActuatorDelete implements DatabaseActuator.OnDeleteAction {
		public void run (String sn) {
			FrameOngoingSchedules.refresh();
		}
	}
	
	public class OnDayScheduleRuleUpdate implements DatabaseDayScheduleRule.OnUpdateAction {
		@Override
		public void run(String oldN, String n, int sh, int sm, int eh, int em) {
			for (SchedulingData d : data.values()) {
				if (!d.getTimeRule().equals(oldN)) {
					d.setTimeRule(n);
				} else {
					d.updateScheduler(true);
				}
			}
			FrameOngoingSchedules.refresh();
		}
	}
	
	public class OnRegularScheduleCreate implements DatabaseRegularSchedule.OnCreateAction {
		@Override
		public void run(String sn, String an, int day, String rn, boolean ao, int pr, boolean en) {
			SchedulingData d=new SchedulingDataRegular(sn,an,day,rn,ao,pr,en);
			if (en && day!=0) {
				data.put(sn,d);
				FrameOngoingSchedules.refresh();
			}
		}
	}
	
	public class OnRegularScheduleUpdate implements DatabaseRegularSchedule.OnUpdateAction {
		@Override
		public void run(String oldSN, String sn, String an, int day, String rn, boolean ao, int pr, boolean en) {
			SchedulingDataRegular d=(SchedulingDataRegular)data.get(oldSN);
			if (d==null) {
				d=new SchedulingDataRegular(sn,an,day,rn,ao,pr,en);
				if (d.enabled && day!=0) {
					data.put(sn,d);
					FrameOngoingSchedules.refresh();
				}
			} else {
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
				}
				if (en && day!=0) data.put(sn,d);
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
			boolean hasConflict=false;
			for (SchedulingData d : data.values()) {
				if (d.getActuatorName().equals(dat.getActuatorName()) && d.getNextEndTime().compareTo(dat.getNextStartTime())>0 && d.getPriority()>dat.getPriority()) {
					hasConflict=true;
					break;
				}
			}
			
			if (!hasConflict) {
				Logger.log("RegularSchedule ("+this.dat.name+") : Started -> Attempt to set "+this.dat.actuatorName+" to "+this.dat.actuatorFlag+". [SET]");
				Cache.updateActuator();
				TriggerActuator((String)(Cache.actuatorMap.get(dat.actuatorName)[1]),this.dat.actuatorName,this.dat.actuatorFlag);
			} else {
				Logger.log("RegularSchedule ("+this.dat.name+") : Started -> Attempt to set "+this.dat.actuatorName+" to "+this.dat.actuatorFlag+". [NOT SET DUE TO LOW PRIORITY]");
			}
		}
	}
	
	public class OnRegularScheduleEnd extends ScheduleOnEndAction {
		@Override
		public void run() {
			Logger.log("RegularSchedule ("+this.dat.name+") : Ended");
			FrameOngoingSchedules.refresh();
		}
	}
	
	
	public class OnSpecialScheduleCreate implements DatabaseSpecialSchedule.OnCreateAction {
		@Override
		public void run(String sn, String an, int year, int month, int day, String rn, boolean ao, int pr, boolean en) {
			SchedulingData d=new SchedulingDataSpecial(sn,an,year,month,day,rn,ao,pr,en);
			if (d.getNextEndTime().compareTo(LocalDateTime.now())>0 && en) {
				data.put(sn,d);
				FrameOngoingSchedules.refresh();
			}
		}
	}
	
	public class OnSpecialScheduleUpdate implements DatabaseSpecialSchedule.OnUpdateAction {
		@Override
		public void run(String oldSN, String sn, String an, int year, int month, int day, String rn, boolean ao, int pr, boolean en) {
			SchedulingDataSpecial d=(SchedulingDataSpecial)data.getOrDefault(oldSN,null);
			if (d==null) {
				d=new SchedulingDataSpecial(sn,an,year,month,day,rn,ao,pr,en);
				if (d.getNextEndTime().compareTo(LocalDateTime.now())>0 && d.enabled && day!=0) {
					data.put(sn,d);
					FrameOngoingSchedules.refresh();
				}
			} else {
				d.setYear(year);
				d.setMonth(month);
				d.setDay(day);
				if (d.getNextEndTime().compareTo(LocalDateTime.now())<0) data.remove(oldSN);
				else {
					if (!d.getName().equals(sn)) {
						d.setName(sn);
						data.remove(oldSN);
						data.put(sn,d);
					}
					d.setActuatorName(an);
	
					d.setTimeRule(rn);
					d.setActuatorFlag(ao);
					if (d.isEnabled()!=en) {
						d.setEnabled(en);
						if (en) data.put(sn,d);
						else data.remove(sn);
					}
				}
				FrameOngoingSchedules.refresh();
			}
		}
	}
	
	public class OnSpecialScheduleDelete implements DatabaseSpecialSchedule.OnDeleteAction {
		@Override
		public void run(String sn) {
			SchedulingData d=data.get(sn);
			if (d!=null) {
				data.remove(sn);
				FrameOngoingSchedules.refresh();
			}
		}
	}
	
	public class OnSpecialScheduleStart extends ScheduleOnStartAction {
		@Override
		public void run() {
			boolean hasConflict=false;
			for (SchedulingData d : data.values()) {
				if (d instanceof SchedulingDataSpecial && d.getActuatorName().equals(dat.getActuatorName()) && d.getNextEndTime().compareTo(dat.getNextStartTime())>0 && d.getPriority()>dat.getPriority()) {
					hasConflict=true;
					break;
				}
			}
			
			if (!hasConflict) {
				Logger.log("SpecialSchedule ("+this.dat.name+") : Started -> Attempt to set "+this.dat.actuatorName+" to "+this.dat.actuatorFlag+". [SET]");
				Cache.updateActuator();
				TriggerActuator((String)(Cache.actuatorMap.get(dat.actuatorName)[1]),this.dat.actuatorName,this.dat.actuatorFlag);
			} else {
				Logger.log("SpecialSchedule ("+this.dat.name+") : Started -> Attempt to set "+this.dat.actuatorName+" to "+this.dat.actuatorFlag+". [NOT SET DUE TO LOW PRIORITY]");
			}
		}
	}
	
	public class OnSpecialScheduleEnd extends ScheduleOnEndAction {
		@Override
		public void run() {
			Logger.log("SpecialSchedule ("+this.dat.name+") : Ended");
			FrameOngoingSchedules.refresh();
		}
	}
	
	private static void TriggerActuator (String cn, String sn, boolean flag) {
		String status;
		if (flag) status="ON";
		else status="OFF";
		SchedulingThreadActuatorTrigger trig=new SchedulingThreadActuatorTrigger(cn,sn,status);
		trig.trigger();
	}
	
	public void run () {
		OnDayScheduleRuleUpdate odsru=new OnDayScheduleRuleUpdate();
		DatabaseDayScheduleRule.registerOnUpdateAction(odsru);
		this.data=new HashMap<>();
		
		OnActuatorUpdate oau=new OnActuatorUpdate();
		OnActuatorDelete oae=new OnActuatorDelete();
		DatabaseActuator.registerOnUpdateAction(oau);
		DatabaseActuator.registerOnDeleteAction(oae);
		
		
		OnSpecialScheduleCreate ossc=new OnSpecialScheduleCreate();
		OnSpecialScheduleUpdate ossu=new OnSpecialScheduleUpdate();
		OnSpecialScheduleDelete ossd=new OnSpecialScheduleDelete();
		DatabaseSpecialSchedule.registerOnCreateAction(ossc);
		DatabaseSpecialSchedule.registerOnUpdateAction(ossu);
		DatabaseSpecialSchedule.registerOnDeleteAction(ossd);
		OnSpecialScheduleStart osss=new OnSpecialScheduleStart();
		OnSpecialScheduleEnd osse=new OnSpecialScheduleEnd();
		
		ResultSet rs=DatabaseSpecialSchedule.getSpecialSchedules();
		try {
			while (rs.next()) {
				SchedulingDataSpecial d=new SchedulingDataSpecial(rs.getString("ScheduleName"),rs.getString("ActuatorName"),rs.getInt("Year"),rs.getInt("Month"),rs.getInt("Day"),rs.getString("Rule"),rs.getBoolean("ActuatorOn"),rs.getInt("Priority"),rs.getBoolean("Enabled"));
				if (d.getNextEndTime().compareTo(LocalDateTime.now())>0 && d.isEnabled()) {
					d.registerOnStartFunc(osss);
					d.registerOnEndFunc(osse);
					data.put(d.getName(),d);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		OnRegularScheduleCreate orsc=new OnRegularScheduleCreate();
		OnRegularScheduleUpdate orsu=new OnRegularScheduleUpdate();
		OnRegularScheduleDelete orsd=new OnRegularScheduleDelete();
		
		DatabaseRegularSchedule.registerOnCreateAction(orsc);
		DatabaseRegularSchedule.registerOnUpdateAction(orsu);
		DatabaseRegularSchedule.registerOnDeleteAction(orsd);
		OnRegularScheduleStart orss=new OnRegularScheduleStart();
		OnRegularScheduleEnd orse=new OnRegularScheduleEnd();
		
		rs=DatabaseRegularSchedule.getRegularSchedules();
		try {
			while (rs.next()) {
				SchedulingDataRegular d=new SchedulingDataRegular(rs.getString("ScheduleName"),rs.getString("ActuatorName"),rs.getInt("DayMask"),rs.getString("Rule"),rs.getBoolean("ActuatorOn"),rs.getInt("Priority"),rs.getBoolean("Enabled"));
				if (d.getNextEndTime().compareTo(LocalDateTime.now())>0 && d.isEnabled() && d.getDay()!=0) {
					d.registerOnStartFunc(orss);
					d.registerOnEndFunc(orse);
					data.put(d.getName(),d);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		while (true) {
			if (stopQueued) {
				stopQueued=false;
				DatabaseDayScheduleRule.unregisterOnUpdateAction(odsru);
				
				DatabaseSpecialSchedule.unregisterOnCreateAction(ossc);
				DatabaseSpecialSchedule.unregisterOnUpdateAction(ossu);
				DatabaseSpecialSchedule.unregisterOnDeleteAction(ossd);
				
				DatabaseRegularSchedule.unregisterOnCreateAction(orsc);
				DatabaseRegularSchedule.unregisterOnUpdateAction(orsu);
				DatabaseRegularSchedule.unregisterOnDeleteAction(orsd);
				
				SchedulingServer.notifyStop();
			}
			try { Thread.sleep(1000);
			} catch (InterruptedException e) {}
		}
	}
}
