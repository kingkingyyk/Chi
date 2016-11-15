package SchedulingServer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import Chi.Logger;
import ControllerPacket.ControllerPacketActuatorTrigger;
import Database.Cache;
import Database.DatabaseActuator;
import Database.DatabaseDayScheduleRule;
import Database.DatabaseEvent;
import Database.DatabaseRegularSchedule;
import Database.DatabaseSpecialSchedule;
import Entity.Regularschedule;
import Entity.Specialschedule;

public class SchedulingThread extends Thread {
	public boolean stopQueued=false;
	public HashMap<String,SchedulingData> data;
	
	public void requestStop () {
		this.stopQueued=true;
	}
	
	public class OnActuatorUpdate implements DatabaseActuator.OnUpdateAction {
		@Override
		public void run(String oldN, String n, String u, String slist, double px, double py, String ctrlType) {
			for (SchedulingData d : data.values()) {
				if (d.getActuatorName().equals(oldN)) {
					d.setActuatorName(n);
				}
			}
			
			if (ctrlType.equals("Scheduled")) {
				for (Regularschedule r : Cache.RegularSchedules.map.values()) {
					if (!data.containsKey(r.getSchedulename()) && r.getActuator().getName().equals(n) &&
							r.getEnabled() && r.getDaymask()!=0) {
						SchedulingDataRegular d=new SchedulingDataRegular(r.getSchedulename(),n,r.getDaymask(),
															r.getDayschedulerule().getRulename(),
															r.getOnstartaction(),r.getOnendaction(),
															r.getLockmanual(),r.getPriority(),
															r.getEnabled());
						d.registerOnStartFunc(new OnRegularScheduleStart());
						d.registerOnEndFunc(new OnRegularScheduleEnd());
						data.put(r.getSchedulename(),d);
					}
				}
					
				for (Specialschedule ss : Cache.SpecialSchedules.map.values()) {
					if (!data.containsKey(ss.getSchedulename()) && ss.getActuator().getName().equals(n) &&
						ss.getEnabled()) {
						SchedulingDataSpecial d=new SchedulingDataSpecial(ss.getSchedulename(),n,ss.getYear(),
																	ss.getMonth(),ss.getDay(),
																	ss.getDayschedulerule().getRulename(),
																	ss.getOnstartaction(),ss.getOnendaction(),
																	ss.getLockmanual(),ss.getPriority(),ss.getEnabled());
						if (ScheduleUtility.compareScheduleTime(d.getNextEndTime(),LocalDateTime.now())>0) {
							d.registerOnStartFunc(new OnSpecialScheduleStart());
							d.registerOnEndFunc(new OnSpecialScheduleEnd());
							
							data.put(ss.getSchedulename(),d);
						}
					}
				}
			} else {
				ArrayList<SchedulingData> toRemove=new ArrayList<>();
				for (SchedulingData d : data.values()) if (d.getActuatorName().equals(n)) toRemove.add(d);
				
				for (SchedulingData d : toRemove) {
					d.purgeTimer();
					data.remove(d.getName());
				}
			}
			
			FrameOngoingSchedules.refresh();
			FrameGanttChart.refresh();
		}
	}
	
	public class OnActuatorDelete implements DatabaseActuator.OnDeleteAction {
		public void run (String an) {
			for (SchedulingData d : data.values()) {
				if (d.getActuatorName().equals(an)) {
					d.purgeTimer();
					data.remove(d.getName());
				}
			}
			FrameOngoingSchedules.refresh();
			FrameGanttChart.refresh();
		}
	}
	
	public class OnDayScheduleRuleUpdate implements DatabaseDayScheduleRule.OnUpdateAction {
		@Override
		public void run(String oldN, String n, int sh, int sm, int eh, int em) {
			ArrayList<SchedulingData> toRemove=new ArrayList<>();
			for (SchedulingData d : data.values()) {
				if (d.getTimeRule().equals(oldN)) {
					d.setTimeRule(n);
					d.updateScheduler(true);
					
					if (ScheduleUtility.compareScheduleTime(d.getNextEndTime(),LocalDateTime.now())<=0) toRemove.add(d);
				}
			}
			
			for (SchedulingData d : toRemove) {
				d.purgeTimer();
				data.remove(d.getName());
			}
		
			for (Regularschedule r : Cache.RegularSchedules.map.values()) {
				if (!data.containsKey(r.getSchedulename()) && r.getDayschedulerule().getRulename().equals(n) &&
						r.getEnabled() && r.getDaymask()!=0) {
					SchedulingDataRegular d=new SchedulingDataRegular(r.getSchedulename(),r.getActuator().getName(),r.getDaymask(),
														r.getDayschedulerule().getRulename(),
														r.getOnstartaction(),r.getOnendaction(),
														r.getLockmanual(),r.getPriority(),
														r.getEnabled());
					d.registerOnStartFunc(new OnRegularScheduleStart());
					d.registerOnEndFunc(new OnRegularScheduleEnd());
					data.put(r.getSchedulename(),d);
				}
			}
				
			for (Specialschedule ss : Cache.SpecialSchedules.map.values()) {
				if (!data.containsKey(ss.getSchedulename()) && ss.getDayschedulerule().getRulename().equals(n) &&
					ss.getEnabled()) {
					SchedulingDataSpecial d=new SchedulingDataSpecial(ss.getSchedulename(),ss.getActuator().getName(),ss.getYear(),
																ss.getMonth(),ss.getDay(),
																ss.getDayschedulerule().getRulename(),
																ss.getOnstartaction(),ss.getOnendaction(),
																ss.getLockmanual(),ss.getPriority(),ss.getEnabled());
					if (ScheduleUtility.compareScheduleTime(d.getNextEndTime(),LocalDateTime.now())>0) {
						d.registerOnStartFunc(new OnSpecialScheduleStart());
						d.registerOnEndFunc(new OnSpecialScheduleEnd());
						
						data.put(ss.getSchedulename(),d);
					}
				}
			}
			
			FrameOngoingSchedules.refresh();
			FrameGanttChart.refresh();
		}
	}
	
	public class OnRegularScheduleCreate implements DatabaseRegularSchedule.OnCreateAction {
		@Override
		public void run(String sn, String an, int day, String rn, String start, String end, boolean lock, int pr, boolean en) {
			SchedulingData d=new SchedulingDataRegular(sn,an,day,rn,start,end,lock,pr,en);
			if (en && day!=0 && Cache.Actuators.map.get(an).getControltype().equals("Scheduled")) {
				data.put(sn,d);
				FrameOngoingSchedules.refresh();
				FrameGanttChart.refresh();
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
					FrameGanttChart.refresh();
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
				d.setPriority(pr);
				if (d.isEnabled()!=en) {
					d.setEnabled(en);
				}
				if (en && day!=0) data.put(sn,d);
				else {
					d.purgeTimer();
					data.remove(sn);
				}

				FrameOngoingSchedules.refresh();
				FrameGanttChart.refresh();
			}
		}
	}
	
	public class OnRegularScheduleDelete implements DatabaseRegularSchedule.OnDeleteAction {
		@Override
		public void run(String sn) {
			SchedulingData d=data.get(sn);
			if (d!=null) {
				d.purgeTimer();
				data.remove(sn);
				FrameOngoingSchedules.refresh();
				FrameGanttChart.refresh();
			}
		}
	}
	
	public class OnRegularScheduleStart extends ScheduleOnStartAction {
		@Override
		public void run(SchedulingData dat) {
			boolean hasConflict=false;
			for (SchedulingData d : data.values()) {
				if (d!=dat && d.getActuatorName().equals(dat.getActuatorName()) && 
					ScheduleUtility.compareScheduleTime(d.getNextEndTime(),dat.getNextStartTime())>0 && d.enabled) {
					
					if (d instanceof SchedulingDataSpecial || (d instanceof SchedulingDataRegular && (d.getPriority()>dat.getPriority() || (d.getPriority()==dat.getPriority() && d.getName().compareTo(dat.getName())<0)))) {
						hasConflict=true;
						break;
					}
					
				}
			}
			
			if (!hasConflict) {
				Logger.log(Logger.LEVEL_INFO,"RegularSchedule ("+dat.name+") : Started -> Attempt to set "+dat.actuatorName+" to "+dat.getStartAction()+". [SET]");
				TriggerActuator(Cache.Actuators.map.get(dat.actuatorName).getController().getControllername(),dat.actuatorName,dat.getStartAction());
				DatabaseEvent.logActuatorEvent(dat.actuatorName, "Schedule", "Set to "+dat.getStartAction()+" by regular schedule "+dat.getName()+" on start.");
			} else {
				Logger.log(Logger.LEVEL_INFO,"RegularSchedule ("+dat.name+") : Started -> Attempt to set "+dat.actuatorName+" to "+dat.getStartAction()+". [NOT SET DUE TO LOW PRIORITY]");
			}
		}
	}
	
	public class OnRegularScheduleEnd extends ScheduleOnEndAction {
		@Override
		public void run(SchedulingData dat) {
			boolean hasConflict=false;
			for (SchedulingData d : data.values()) {
				if (d!=dat && d.getActuatorName().equals(dat.getActuatorName()) && 
					ScheduleUtility.compareScheduleTime(d.getNextEndTime(),dat.getNextEndTime())>=0 && d.enabled) {
					
					if (d instanceof SchedulingDataSpecial || (d instanceof SchedulingDataRegular && (d.getPriority()>dat.getPriority() || (d.getPriority()==dat.getPriority() && d.getName().compareTo(dat.getName())<0)))) { 
						hasConflict=true;
						break;
					}
					
				}
			}
			
			if (!hasConflict) {
				Logger.log(Logger.LEVEL_INFO,"RegularSchedule ("+dat.name+") : Ended -> Attempt to set "+dat.actuatorName+" to "+dat.getEndAction()+". [SET]");
				TriggerActuator(Cache.Actuators.map.get(dat.actuatorName).getController().getControllername(),dat.actuatorName,dat.getEndAction());
				DatabaseEvent.logActuatorEvent(dat.actuatorName, "Schedule", "Set to "+dat.getEndAction()+" by regular schedule "+dat.getName()+" on end.");
			} else {
				Logger.log(Logger.LEVEL_INFO,"RegularSchedule ("+dat.name+") : Ended -> Attempt to set "+dat.actuatorName+" to "+dat.getEndAction()+". [NOT SET DUE TO LOW PRIORITY]");
			}
			
			FrameOngoingSchedules.refresh();
			FrameGanttChart.refresh();
		}
	}
	
	
	public class OnSpecialScheduleCreate implements DatabaseSpecialSchedule.OnCreateAction {
		@Override
		public void run(String sn, String an, int year, int month, int day, String rn, String startAct, String endAct, boolean lock, int pr, boolean en) {
			SchedulingData d=new SchedulingDataSpecial(sn,an,year,month,day,rn,startAct,endAct,lock,pr,en);
			if (d.getNextEndTime().compareTo(LocalDateTime.now())>0 && d.enabled && Cache.Actuators.map.get(an).getControltype().equals("Scheduled")) {
				data.put(sn,d);
				FrameOngoingSchedules.refresh();
				FrameGanttChart.refresh();
			}
		}
	}
	
	public class OnSpecialScheduleUpdate implements DatabaseSpecialSchedule.OnUpdateAction {
		@Override
		public void run(String oldSN, String sn, String an, int year, int month, int day, String rn, String startAct, String endAct, boolean lock, int pr, boolean en) {
			SchedulingDataSpecial d=(SchedulingDataSpecial)data.getOrDefault(oldSN,null);
			if (d==null) {
				d=new SchedulingDataSpecial(sn,an,year,month,day,rn,startAct,endAct,lock,pr,en);
				if (d.getNextEndTime().compareTo(LocalDateTime.now())>0 && d.enabled && Cache.Actuators.map.get(an).getControltype().equals("Scheduled")) {
					data.put(sn,d);
					FrameOngoingSchedules.refresh();
					FrameGanttChart.refresh();
				}
			} else {
				d.setYear(year);
				d.setMonth(month);
				d.setDay(day);
				if (d.getNextEndTime().compareTo(LocalDateTime.now())<0) {
					d.purgeTimer();
					data.remove(oldSN);
				} else {
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
					d.setPriority(pr);
					if (d.isEnabled()!=en) {
						d.setEnabled(en);
						if (en) data.put(sn,d);
						else {
							d.purgeTimer();
							data.remove(sn);
						}
					}
				}
				FrameOngoingSchedules.refresh();
				FrameGanttChart.refresh();
			}
		}
	}
	
	public class OnSpecialScheduleDelete implements DatabaseSpecialSchedule.OnDeleteAction {
		@Override
		public void run(String sn) {
			SchedulingData d=data.get(sn);
			if (d!=null) {
				d.purgeTimer();
				data.remove(sn);
				FrameOngoingSchedules.refresh();
				FrameGanttChart.refresh();
			}
		}
	}
	
	public class OnSpecialScheduleStart extends ScheduleOnStartAction {
		@Override
		public void run(SchedulingData dat) {
			boolean hasConflict=false;
			for (SchedulingData d : data.values()) {
				if (d!=dat && d instanceof SchedulingDataSpecial && d.getActuatorName().equals(dat.getActuatorName()) &&
						ScheduleUtility.compareScheduleTime(d.getNextEndTime(),dat.getNextStartTime())>0  && 
						(d.getPriority()>dat.getPriority() || (d.getPriority()==dat.getPriority() && d.getName().compareTo(dat.getName())<0)) &&
						 d.enabled) {
					hasConflict=true;
					break;
				}
			}
			
			if (!hasConflict) {
				Logger.log(Logger.LEVEL_INFO,"SpecialSchedule ("+dat.name+") : Started -> Attempt to set "+dat.actuatorName+" to "+dat.getStartAction()+". [SET]");
				TriggerActuator(Cache.Actuators.map.get(dat.actuatorName).getController().getControllername(),dat.actuatorName,dat.getStartAction());
				DatabaseEvent.logActuatorEvent(dat.actuatorName, "Schedule", "Set to "+dat.getStartAction()+" by special schedule "+dat.getName()+" on start.");
			} else {
				Logger.log(Logger.LEVEL_INFO,"SpecialSchedule ("+dat.name+") : Started -> Attempt to set "+dat.actuatorName+" to "+dat.getStartAction()+". [NOT SET DUE TO LOW PRIORITY]");
			}
		}
	}
	
	public class OnSpecialScheduleEnd extends ScheduleOnEndAction {
		@Override
		public void run(SchedulingData dat) {
			boolean hasConflict=false;
			for (SchedulingData d : data.values()) {
				if (d!=dat && d instanceof SchedulingDataSpecial && d.getActuatorName().equals(dat.getActuatorName()) &&
					ScheduleUtility.compareScheduleTime(d.getNextEndTime(),dat.getNextEndTime())>=0 &&
							(d.getPriority()>dat.getPriority() || (d.getPriority()==dat.getPriority() && d.getName().compareTo(dat.getName())<0)) &&
							d.enabled) {
					hasConflict=true;
					break;
				}
			}
			
			if (!hasConflict) {
				Logger.log(Logger.LEVEL_INFO,"SpecialSchedule ("+dat.name+") : Ended -> Attempt to set "+dat.actuatorName+" to "+dat.getEndAction()+". [SET]");
				TriggerActuator(Cache.Actuators.map.get(dat.actuatorName).getController().getControllername(),dat.actuatorName,dat.getEndAction());
				DatabaseEvent.logActuatorEvent(dat.actuatorName, "Schedule", "Set to "+dat.getStartAction()+" by special schedule "+dat.getName()+" on end.");
			} else {
				Logger.log(Logger.LEVEL_INFO,"SpecialSchedule ("+dat.name+") : Ended -> Attempt to set "+dat.actuatorName+" to "+dat.getEndAction()+". [NOT SET DUE TO LOW PRIORITY]");
			}
			
			Thread t=new Thread() {
				public void run () {
					while (true) {
						boolean exists=false;
						for (SchedulingData d : data.values()) {
							if (d!=dat && d instanceof SchedulingDataRegular && d.getActuatorName().equals(dat.getActuatorName())
								&& ScheduleUtility.compareScheduleTime(d.getNextEndTime(),dat.getNextEndTime())==0 && d.enabled) {
								exists=true;
								break;
							}
						}
						
						if (!exists) {
							data.remove(dat.getName());
							FrameOngoingSchedules.refresh();
							FrameGanttChart.refresh();
							break;
						}
						
						try { Thread.sleep(500); } catch (InterruptedException e) {}
					}
				}
			}; t.start();
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
			if (s.getActuator().getControltype().equals("Scheduled") && d.getNextEndTime().compareTo(LocalDateTime.now())>0 && d.isEnabled()) {
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
			if (r.getActuator().getControltype().equals("Scheduled") && d.isEnabled() && d.getNextEndTime().compareTo(LocalDateTime.now())>0 && d.getDay()!=0) {
				d.registerOnStartFunc(orss);
				d.registerOnEndFunc(orse);
				data.put(d.getName(),d);
			}
			
		}
		
		SchedulingServer.isStarted=true;
		while (true) {
			if (stopQueued) {
				for (SchedulingData d : data.values()) d.purgeTimer();
				data.clear();
				stopQueued=false;
				DatabaseActuator.unregisterOnUpdateAction(oau);
				DatabaseActuator.unregisterOnDeleteAction(oae);
				
				DatabaseDayScheduleRule.unregisterOnUpdateAction(odsru);
				
				DatabaseSpecialSchedule.unregisterOnCreateAction(ossc);
				DatabaseSpecialSchedule.unregisterOnUpdateAction(ossu);
				DatabaseSpecialSchedule.unregisterOnDeleteAction(ossd);
				
				DatabaseRegularSchedule.unregisterOnCreateAction(orsc);
				DatabaseRegularSchedule.unregisterOnUpdateAction(orsu);
				DatabaseRegularSchedule.unregisterOnDeleteAction(orsd);
				
				SchedulingServer.isStarted=false;
				break;
			}
			try { Thread.sleep(1000);} catch (InterruptedException e) {}
		}
	}
}
