package Chi;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import Database.Cache;
import Entity.Dayschedulerule;

public class SchedulingDataRegular extends SchedulingData {
	private int day;
	
	private static class FireOnEndTaskRegular extends SchedulingData.FireOnEndTask implements Runnable {
		@Override
		public void execute() {
			Logger.log(Logger.LEVEL_INFO,"SchedulingDataRegular.FireOnStartTaskRegular - Scheduled To Fire On Next End");
			this.d.fireOnEnd=new FireOnEndTaskRegular();
			this.d.fireOnEnd.d=this.d;
			this.d.onEndScheduler.schedule(this.d.fireOnEnd,Date.from(d.nextEndTime.atZone(ZoneId.systemDefault()).toInstant()));
		}
		
	}
	
	private static class FireOnStartTaskRegular extends SchedulingData.FireOnStartTask implements Runnable {

		@Override
		public void execute() {
			Logger.log(Logger.LEVEL_INFO,"SchedulingDataRegular.FireOnStartTaskRegular - Scheduled To Fire On Next Start");
			this.d.fireOnStart=new FireOnStartTaskRegular();
			this.d.fireOnStart.d=this.d;
			this.d.onStartScheduler.schedule(this.d.fireOnStart,Date.from(d.nextStartTime.atZone(ZoneId.systemDefault()).toInstant()));
		}
	}
	
	public SchedulingDataRegular (String sn, String an, int d, String rn, String startAct, String endAct, boolean lock, int pr, boolean en) {
		super(sn,an,rn,startAct,endAct,lock,pr,en,1);
		this.day=d;
		this.updateScheduler(true);
	}
	
	public void setDay(int d) {
		if (this.day!=d) {
			this.day=d;
			this.updateScheduler(true);
		}
	}
	
	public int getDay(){return this.day;}
	
	protected void updateScheduler(boolean updateScheduler) {
		Logger.log(Logger.LEVEL_INFO,"Scheduling Data Regular - Calculating start & end time for "+this.name);
		if (day!=0) {
			LocalDateTime now=LocalDateTime.now();
			Dayschedulerule r=Cache.DayScheduleRules.map.get(this.rule);
			LocalDateTime temp=LocalDateTime.of(now.getYear(),now.getMonthValue(),now.getDayOfMonth(),r.getEndhour(),r.getEndminute());
			if (now.compareTo(temp)>=0) {
				temp=temp.plusDays(1);
			}
			while ((day & (1 << temp.getDayOfWeek().getValue()))==0) {
				temp=temp.plusDays(1);
			}
			if (r!=null) {
				this.nextStartTime=LocalDateTime.of(temp.getYear(),temp.getMonthValue(),temp.getDayOfMonth(),r.getStarthour(),r.getStartminute());
				this.nextEndTime=LocalDateTime.of(temp.getYear(),temp.getMonthValue(),temp.getDayOfMonth(),r.getEndhour(),r.getEndminute());
				if (this.nextEndTime.compareTo(this.nextStartTime)<0) this.nextEndTime=this.nextEndTime.plusDays(1);
			}
			if (this.isEnabled() && updateScheduler) {
				this.onEndScheduler.purge();
				this.fireOnEnd=new FireOnEndTaskRegular();
				this.fireOnEnd.d=this;
				this.onEndScheduler.schedule(this.fireOnEnd,Date.from(this.nextEndTime.atZone(ZoneId.systemDefault()).toInstant()));
				
				this.onStartScheduler.purge();
				this.fireOnStart=new FireOnStartTaskRegular();
				this.fireOnStart.d=this;
				this.onStartScheduler.schedule(this.fireOnStart,Date.from(this.nextStartTime.atZone(ZoneId.systemDefault()).toInstant()));
			}
		} else if (this.isEnabled() && updateScheduler) {
			this.onEndScheduler.purge();
			this.onStartScheduler.purge();
			this.nextStartTime=LocalDateTime.MAX;
			this.nextEndTime=LocalDateTime.MAX;
		}
	}
	
}
