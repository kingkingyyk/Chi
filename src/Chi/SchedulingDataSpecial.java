package Chi;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class SchedulingDataSpecial extends SchedulingData {
	private int year;
	private int month;
	private int day;
	
	private static class UpdateStartEndTimeTaskSpecial extends SchedulingData.UpdateStartEndTimeTask implements Runnable {

		public void execute() {
			d.updateScheduler(false);
			if (this.d.onEndFunc!=null && d.enabled) {
				this.d.onEndFunc.dat=d;
				this.d.onEndFunc.run();
			}
		}
		
	}
	
	private static class FireSchedulerOnStartTaskSpecial extends SchedulingData.FireSchedulerOnStartTask implements Runnable {

		public void run() {
			if (this.d.onStartFunc!=null && d.enabled) {
				this.d.onStartFunc.dat=d;
				this.d.onStartFunc.run();
			}
		}
	}
	
	public SchedulingDataSpecial (String sn, String an, int y, int m, int d, String rn, boolean af, int pr, boolean en) {
		super(sn,an,rn,af,pr,en,1);
		this.year=y;
		this.month=m;
		this.day=d;
		this.updateScheduler(true);
	}
	
	public int getYear () {return this.year;}
	public int getMonth() {return this.month;}
	public int getDay() {return this.day;}
	
	public void setYear (int y) {
		if (this.year!=y) {
			this.year=y;
			this.updateScheduler(true);
		}
	}
	
	public void setMonth (int m) {
		if (this.month!=m) {
			this.month=m;
			this.updateScheduler(true);
		}
	}
	
	public void setDay (int d) {
		if (this.day!=d) {
			this.day=d;
			this.updateScheduler(true);
		}
	}
	
	protected void updateScheduler(boolean updateScheduler) {
		Logger.log("Scheduling Data Special - Calculating start & end time for "+this.name);
		Cache.updateDayScheduleRule();
		Object [] o=DatabaseDayScheduleRule.getDayScheduleRule(this.rule);
		this.nextStartTime=LocalDateTime.of(this.getYear(),this.getMonth(),this.getDay(),(int)o[1],(int)o[2]);
		this.nextEndTime=LocalDateTime.of(this.getYear(),this.getMonth(),this.getDay(),(int)o[3],(int)o[4]);
		
		if (this.isEnabled() && updateScheduler) {
			this.onEndScheduler.purge();
			this.startEndTask=new UpdateStartEndTimeTaskSpecial();
			this.startEndTask.d=this;
			this.onEndScheduler.schedule(this.startEndTask,Date.from(this.nextEndTime.atZone(ZoneId.systemDefault()).toInstant()));
					
			this.onStartScheduler.purge();
			this.startScheFireTask=new FireSchedulerOnStartTaskSpecial();
			this.startScheFireTask.d=this;
			this.onStartScheduler.schedule(this.startScheFireTask,Date.from(this.nextStartTime.atZone(ZoneId.systemDefault()).toInstant()));
		}
	}
	
}
