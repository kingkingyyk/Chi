package SchedulingServer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public abstract class SchedulingData implements Comparable<SchedulingData> {
	protected String name;
	protected String actuatorName;
	protected String rule;
	protected String onStartAction;
	protected String onEndAction;
	protected boolean lock;
	protected int priority;
	protected boolean enabled;
	protected LocalDateTime nextStartTime;
	protected LocalDateTime nextEndTime;
	protected Timer onStartScheduler;
	protected Timer onEndScheduler;
	protected FireOnEndTask fireOnEnd;
	protected FireOnStartTask fireOnStart;
	protected ArrayList<ScheduleOnStartAction> onStartFunc;
	protected ArrayList<ScheduleOnEndAction> onEndFunc;
	private int classPriority;
	
	protected static class FireOnEndTask extends TimerTask implements Runnable {
		public SchedulingData d;
		@Override
		public void run() {
			//if (d.nextEndTime.compareTo(LocalDateTime.now())>=0) {
				d.updateScheduler(true);
				if (d.enabled) {
					for (ScheduleOnEndAction s : d.onEndFunc) {
						s.dat=d;
						s.run();
					}
				}
				this.execute();
			//}

		}
		public void execute() {};
	}
	
	protected static class FireOnStartTask extends TimerTask implements Runnable {
		public SchedulingData d;
		@Override
		public void run() {
			if (d.enabled) {
				for (ScheduleOnStartAction s : d.onStartFunc) {
					s.dat=d;
					s.run();
				}
			}
		}
		public void execute() {};
	}
	
	public SchedulingData (String sn, String an, String rn, String startAct, String endAct, boolean lock, int pr, boolean en, int cp) {
		this.name=sn;
		this.actuatorName=an;
		this.rule=rn;
		this.onStartAction=startAct;
		this.onEndAction=endAct;
		this.lock=lock;
		this.priority=pr;
		this.enabled=en;
		this.onStartScheduler=new Timer();
		this.onEndScheduler=new Timer();
		this.classPriority=cp;
		this.onStartFunc=new ArrayList<>();
		this.onEndFunc=new ArrayList<>();
	}
	
	public void setName (String n) {this.name=n;}
	public void setActuatorName(String an) {this.actuatorName=an;}
	public void setStartAction(String s) {this.onStartAction=s;}
	public void setEndAction(String s) {this.onEndAction=s;}
	public void setLock(boolean flag) {this.lock=flag;}
	
	protected void updateScheduler(boolean updateScheduler) {
	}
	
	public void setTimeRule(String r) {
		if (!this.rule.equals(r)) {
			this.rule=r;
			this.updateScheduler(true);
		}
	}

	public void setEnabled(boolean e) {
		if (this.enabled!=e) {
			this.enabled=e;
			this.updateScheduler(true);
		}
	}
	
	public void setPriority(int pr) {this.priority=pr;}

	public void registerOnStartFunc (ScheduleOnStartAction f) {
		if (!this.onStartFunc.contains(f)) {
			this.onStartFunc.add(f);
		}
	}
	public void registerOnEndFunc (ScheduleOnEndAction f) {
		if (!this.onEndFunc.contains(f)) {
			this.onEndFunc.add(f);
		}
	}
	
	public String getName(){ return this.name;}
	public String getActuatorName() {return this.actuatorName;}
	public String getTimeRule(){return this.rule; }
	public String getStartAction() {return this.onStartAction;}
	public String getEndAction() {return this.onEndAction;}
	public boolean getLock() {return this.lock;}
	public int getPriority(){return this.priority;}
	public boolean isEnabled(){return this.enabled;}
	public LocalDateTime getNextStartTime(){return this.nextStartTime;}
	public LocalDateTime getNextEndTime(){return this.nextEndTime;}
	
	public String toString() {
		return this.name;
	}
	
	public int compareTo(SchedulingData d) {
		if (this.nextStartTime.compareTo(d.nextStartTime)!=0) {
			return this.nextStartTime.compareTo(d.nextStartTime);
		}
		if (this.classPriority!=d.classPriority) {
			return d.classPriority-this.classPriority;
		}
		if (this.priority!=d.priority) {
			return d.priority-this.priority;
		}
		if (this.nextEndTime.compareTo(d.nextEndTime)!=0) {
			return d.nextEndTime.compareTo(this.nextEndTime);
		}
		return 0;
	}
}
