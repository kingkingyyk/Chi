package SchedulingServer;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import Chi.Logger;
import Database.Cache;
import Entity.Dayschedulerule;

public class SchedulingDataSpecial extends SchedulingData {
	private int year;
	private int month;
	private int day;
	
	private static class FireOnEndTaskSpecial extends SchedulingData.FireOnEndTask implements Runnable {
		@Override
		public void execute() {}
	}
	
	private static class FireOnStartTaskSpecial extends SchedulingData.FireOnStartTask implements Runnable {
		@Override
		public void execute() {}
	}
	
	public SchedulingDataSpecial (String sn, String an, int y, int m, int d, String rn, String startAct, String endAct, boolean lock, int pr, boolean en) {
		super(sn,an,rn,startAct,endAct,lock,pr,en,1);
		this.year=y;
		this.month=m;
		this.day=d;
		this.updateScheduler(true);
	}
	
	public void setYear(int y) {
		if (this.year!=y) {
			this.year=y;
			this.updateScheduler(true);
		}
	}
	
	public void setMonth(int m) {
		if (this.month!=m) {
			this.month=m;
			this.updateScheduler(true);
		}
	}
	
	public void setDay(int d) {
		if (this.day!=d) {
			this.day=d;
			this.updateScheduler(true);
		}
	}
	
	public int getDay(){return this.day;}
	
	protected void updateScheduler(boolean updateScheduler) {
		Logger.log(Logger.LEVEL_INFO,"Scheduling Data Special - Calculating start & end time for "+this.name);
		Dayschedulerule r=Cache.DayScheduleRules.map.get(this.rule);
		if (r!=null) {
			this.nextStartTime=LocalDateTime.of(year,month,day,r.getStarthour(),r.getStartminute());
			this.nextEndTime=LocalDateTime.of(year,month,day,r.getEndhour(),r.getEndminute());
			if (this.nextEndTime.compareTo(this.nextStartTime)<0) this.nextEndTime=this.nextEndTime.plusDays(1);
		}
		if (this.isEnabled() && updateScheduler) {
			this.onEndScheduler.purge();
			this.fireOnEnd=new FireOnEndTaskSpecial();
			this.fireOnEnd.d=this;
			this.onEndScheduler.schedule(this.fireOnEnd,Date.from(this.nextEndTime.atZone(ZoneId.systemDefault()).toInstant()));
				
			this.onStartScheduler.purge();
			this.fireOnStart=new FireOnStartTaskSpecial();
			this.fireOnStart.d=this;
			this.onStartScheduler.schedule(this.fireOnStart,Date.from(this.nextStartTime.atZone(ZoneId.systemDefault()).toInstant()));
		}
	}
	
}
