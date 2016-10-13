package Chi;

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
		public void run(String oldN, String n, String u, double px, double py, String ctrlType) {
			for (SchedulingData d : data.values()) {
				if (d.getActuatorName().equals(oldN)) {
					d.setActuatorName(n);
				}
			}
			
			if (ctrlType.equals("Scheduled")) {
				int count=0;
				
				for (SchedulingData d : data.values()) if (d.getActuatorName().equals(n)) count++;
				
				if (count==0) {
					for (Regularschedule r : Cache.RegularSchedules.map.values()) {
						if (r.getActuator().getName().equals(n) && r.getEnabled() && r.getDaymask()!=0) {
							SchedulingData d=new SchedulingDataRegular(r.getSchedulename(),n,r.getDaymask(),
																r.getDayschedulerule().getRulename(),
																r.getOnstartaction(),r.getOnendaction(),
																r.getLockmanual(),r.getPriority(),
																r.getEnabled());
							data.put(r.getSchedulename(),d);
							FrameOngoingSchedules.refresh();
						}
					}
					
					for (Specialschedule ss : Cache.SpecialSchedules.map.values()) {
						if (ss.getActuator().getName().equals(n) && ss.getEnabled()) {
							SchedulingData dat=new SchedulingDataSpecial(ss.getSchedulename(),n,ss.getYear(),
																		ss.getMonth(),ss.getDay(),
																		ss.getDayschedulerule().getRulename(),
																		ss.getOnstartaction(),ss.getOnendaction(),
																		ss.getLockmanual(),ss.getPriority(),ss.getEnabled());
							if (dat.getNextEndTime().compareTo(LocalDateTime.now())>0) {
								data.put(ss.getSchedulename(),dat);
								FrameOngoingSchedules.refresh();
							}
						}
					}
				}
			} else {
				for (SchedulingData d : data.values()) if (d.getActuatorName().equals(oldN)) data.remove(d.getName());
			}
			
			FrameOngoingSchedules.refresh();
		}
	}
	
	public class OnActuatorDelete implements DatabaseActuator.OnDeleteAction {
		public void run (String an) {
			for (SchedulingData d : data.values()) {
				if (d.getActuatorName().equals(an)) {
					data.remove(d.getName());
				}
			}
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
		public void run(String sn, String an, int day, String rn, String start, String end, boolean lock, int pr, boolean en) {
			SchedulingData d=new SchedulingDataRegular(sn,an,day,rn,start,end,lock,pr,en);
			if (en && day!=0 && Cache.Actuators.map.get(an).getControltype().equals("Scheduled")) {
				data.put(sn,d);
				FrameOngoingSchedules.refresh();
			}
		}
	}
	
	public class OnRegularScheduleUpdate implements DatabaseRegularSchedule.OnUpdateAction {
		@Override
		public void run(String oldSN, String sn, String an, int day, String rn, String start, String end, boolean lock, int pr, boolean en) {
			SchedulingDataRegular d=(SchedulingDataRegular)data.get(oldSN);
			if (d==null) {
				d=new SchedulingDataRegular(sn,an,day,rn,start,end,lock,pr,en);
				if (d.enabled && day!=0 && Cache.Actuators.map.get(an).getControltype().equals("Scheduled")) {
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
				d.setStartAction(start);
				d.setEndAction(end);
				d.setLock(lock);
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
				if (d!=dat && d.getActuatorName().equals(dat.getActuatorName()) && d.getNextEndTime().compareTo(dat.getNextStartTime())>0 && d.getPriority()>dat.getPriority()) {
					hasConflict=true;
					break;
				}
			}
			
			if (!hasConflict) {
				Logger.log("RegularSchedule ("+this.dat.name+") : Started -> Attempt to set "+this.dat.actuatorName+" to "+this.dat.getStartAction()+". [SET]");
				TriggerActuator(Cache.Actuators.map.get(dat.actuatorName).getController().getControllername(),this.dat.actuatorName,this.dat.getStartAction());
			} else {
				Logger.log("RegularSchedule ("+this.dat.name+") : Started -> Attempt to set "+this.dat.actuatorName+" to "+this.dat.getStartAction()+". [NOT SET DUE TO LOW PRIORITY]");
			}
		}
	}
	
	public class OnRegularScheduleEnd extends ScheduleOnEndAction {
		@Override
		public void run() {
			boolean hasConflict=false;
			for (SchedulingData d : data.values()) {
				if (d!=dat && d.getActuatorName().equals(dat.getActuatorName()) && d.getNextEndTime().compareTo(dat.getNextStartTime())>0 && d.getPriority()>dat.getPriority()) {
					hasConflict=true;
					break;
				}
			}
			
			if (!hasConflict) {
				Logger.log("RegularSchedule ("+this.dat.name+") : Ended -> Attempt to set "+this.dat.actuatorName+" to "+this.dat.getEndAction()+". [SET]");
				TriggerActuator(Cache.Actuators.map.get(dat.actuatorName).getController().getControllername(),this.dat.actuatorName,this.dat.getEndAction());
			} else {
				Logger.log("RegularSchedule ("+this.dat.name+") : Ended -> Attempt to set "+this.dat.actuatorName+" to "+this.dat.getEndAction()+". [NOT SET DUE TO LOW PRIORITY]");
			}
			
			FrameOngoingSchedules.refresh();
		}
	}
	
	
	public class OnSpecialScheduleCreate implements DatabaseSpecialSchedule.OnCreateAction {
		@Override
		public void run(String sn, String an, int year, int month, int day, String rn, String startAct, String endAct, boolean lock, int pr, boolean en) {
			SchedulingData d=new SchedulingDataSpecial(sn,an,year,month,day,rn,startAct,endAct,lock,pr,en);
			if (d.getNextEndTime().compareTo(LocalDateTime.now())>0 && en && Cache.Actuators.map.get(an).getControltype().equals("Scheduled")) {
				data.put(sn,d);
				FrameOngoingSchedules.refresh();
			}
		}
	}
	
	public class OnSpecialScheduleUpdate implements DatabaseSpecialSchedule.OnUpdateAction {
		@Override
		public void run(String oldSN, String sn, String an, int year, int month, int day, String rn, String startAct, String endAct, boolean lock, int pr, boolean en) {
			SchedulingDataSpecial d=(SchedulingDataSpecial)data.getOrDefault(oldSN,null);
			if (d==null) {
				d=new SchedulingDataSpecial(sn,an,year,month,day,rn,startAct,endAct,lock,pr,en);
				if (d.getNextEndTime().compareTo(LocalDateTime.now())>0 && d.enabled && day!=0 && Cache.Actuators.map.get(an).getControltype().equals("Scheduled")) {
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
					d.setStartAction(startAct);
					d.setEndAction(endAct);
					d.setLock(lock);
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
				if (d!=dat && d instanceof SchedulingDataSpecial && d.getActuatorName().equals(dat.getActuatorName()) && d.getNextEndTime().compareTo(dat.getNextStartTime())>0 && d.getPriority()>dat.getPriority()) {
					hasConflict=true;
					break;
				}
			}
			
			if (!hasConflict) {
				Logger.log("SpecialSchedule ("+this.dat.name+") : Started -> Attempt to set "+this.dat.actuatorName+" to "+this.dat.getStartAction()+". [SET]");
				TriggerActuator(Cache.Actuators.map.get(dat.actuatorName).getController().getControllername(),this.dat.actuatorName,this.dat.getStartAction());
			} else {
				Logger.log("SpecialSchedule ("+this.dat.name+") : Started -> Attempt to set "+this.dat.actuatorName+" to "+this.dat.getStartAction()+". [NOT SET DUE TO LOW PRIORITY]");
			}
		}
	}
	
	public class OnSpecialScheduleEnd extends ScheduleOnEndAction {
		@Override
		public void run() {
			boolean hasConflict=false;
			for (SchedulingData d : data.values()) {
				if (d!=dat && d instanceof SchedulingDataSpecial && d.getActuatorName().equals(dat.getActuatorName()) && d.getNextEndTime().compareTo(dat.getNextStartTime())>0 && d.getPriority()>dat.getPriority()) {
					hasConflict=true;
					break;
				}
			}
			
			if (!hasConflict) {
				Logger.log("SpecialSchedule ("+this.dat.name+") : Ended -> Attempt to set "+this.dat.actuatorName+" to "+this.dat.getEndAction()+". [SET]");
				TriggerActuator(Cache.Actuators.map.get(dat.actuatorName).getController().getControllername(),this.dat.actuatorName,this.dat.getEndAction());
			} else {
				Logger.log("SpecialSchedule ("+this.dat.name+") : Ended -> Attempt to set "+this.dat.actuatorName+" to "+this.dat.getEndAction()+". [NOT SET DUE TO LOW PRIORITY]");
			}
			
			FrameOngoingSchedules.refresh();
		}
	}
	
	private static void TriggerActuator (String cn, String sn, String status) {
		ControllerPacketActuatorTrigger trig=new ControllerPacketActuatorTrigger(cn,sn,status);
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
		
		for (Specialschedule s : Cache.SpecialSchedules.map.values()) {
			SchedulingDataSpecial d=new SchedulingDataSpecial(	s.getSchedulename(),s.getActuator().getName(),
																s.getYear(), s.getMonth(), s.getDay(), s.getDayschedulerule().getRulename(),
																s.getOnstartaction(),s.getOnendaction(),s.getLockmanual(),s.getPriority(),s.getEnabled() );
			if (d.getNextEndTime().compareTo(LocalDateTime.now())>0 && d.isEnabled()) {
				d.registerOnStartFunc(osss);
				d.registerOnEndFunc(osse);
				data.put(d.getName(),d);
			}
		}
		OnRegularScheduleCreate orsc=new OnRegularScheduleCreate();
		OnRegularScheduleUpdate orsu=new OnRegularScheduleUpdate();
		OnRegularScheduleDelete orsd=new OnRegularScheduleDelete();
		
		DatabaseRegularSchedule.registerOnCreateAction(orsc);
		DatabaseRegularSchedule.registerOnUpdateAction(orsu);
		DatabaseRegularSchedule.registerOnDeleteAction(orsd);
		OnRegularScheduleStart orss=new OnRegularScheduleStart();
		OnRegularScheduleEnd orse=new OnRegularScheduleEnd();
		
		for (Regularschedule r : Cache.RegularSchedules.map.values()) {
			SchedulingDataRegular d=new SchedulingDataRegular(	r.getSchedulename(), r.getActuator().getName(), r.getDaymask(),
																r.getDayschedulerule().getRulename(), r.getOnstartaction(),
																r.getOnendaction(), r.getLockmanual(), r.getPriority(),
																r.getEnabled());
			if (d.getNextEndTime().compareTo(LocalDateTime.now())>0 && d.isEnabled() && d.getDay()!=0) {
				d.registerOnStartFunc(orss);
				d.registerOnEndFunc(orse);
				data.put(d.getName(),d);
			}
			
		}
		
		SchedulingServer.isStarted=true;
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
