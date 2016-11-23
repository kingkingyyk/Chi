package NotificationServer;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import Chi.Utility;
import Database.Cache;
import Database.DatabaseController;
import Database.DatabaseEvent;
import Entity.Controller;

public class NotificationServerControllerTracker {

	private static class ControllerTracker {
		public static HashMap<String,ControllerTracker> trackObjs=new HashMap<>();
		private String ctrlN;
		private int reportTimeout;
		private Date nextExpectedReportTime;
		private Timer expireTimer;
		private int lastReportStatus=1;
		
		private static class OnTimerExpire extends TimerTask implements Runnable {
			public ControllerTracker ct;
			@Override
			public void run() {
				if (ct.lastReportStatus==1) {
					DatabaseEvent.logControllerEvent(ct.ctrlN,"Report Not Received","Didn't report itself in expected time!");
					ct.lastReportStatus=2;
				}
			}
		}
		
		public ControllerTracker (String ctrlName) {
			this.ctrlN=ctrlName;
			this.reportTimeout=Cache.Controllers.map.get(ctrlName).getReporttimeout();
			trackObjs.put(this.ctrlN,this);
		}
		
		public void refreshTimerTimeout(int t) {
			if (this.reportTimeout!=t) {
				this.reportTimeout=t;
				this.refreshTimer();
			}
		}
		
		public void refreshTimer() {
			lastReportStatus=1;
			nextExpectedReportTime=Utility.localDateTimeToSQLDate(LocalDateTime.now().plusSeconds(Cache.Controllers.map.get(this.ctrlN).getReporttimeout()));
			expireTimer.cancel();
			expireTimer.purge();
			expireTimer=new Timer();
			OnTimerExpire t=new OnTimerExpire();
			t.ct=this;
			expireTimer.schedule(t, nextExpectedReportTime);
		}
		
		public static void destroy (String s) {
			for (ControllerTracker ct : trackObjs.values()) {
				if (ct.ctrlN.equals(s)) {
					ct.expireTimer.purge();
					trackObjs.remove(ct);
					break;
				}
			}
		}
		
		public static void destroyAll () {
			for (ControllerTracker ct : trackObjs.values()) if (ct.expireTimer!=null) ct.expireTimer.cancel();
			trackObjs.clear();
		}
	}
	

	private static class OnControllerCreateAction implements DatabaseController.OnCreateAction {
		public void run (String n, String s, double x, double y, int t) {
			new ControllerTracker(n);
		}
	}
	
	private static class OnControllerUpdateAction implements DatabaseController.OnUpdateAction {
		public void run (String oldN, String n, String s, double x, double y, int t) {
			if (!oldN.equals(n)) {
				ControllerTracker ct=ControllerTracker.trackObjs.get(oldN);
				ct.ctrlN=n;
				ControllerTracker.trackObjs.remove(oldN);
				ControllerTracker.trackObjs.put(oldN,ct);
			}
			ControllerTracker.trackObjs.get(n).refreshTimerTimeout(t);
		}
	}
	
	private static class OnControllerReportAction implements DatabaseController.OnReportAction {
		public void run (String n) {
			ControllerTracker.trackObjs.get(n).refreshTimer();
		}
	}
	
	private static class OnControllerDeleteAction implements DatabaseController.OnDeleteAction {
		public void run (String n) {
			ControllerTracker.destroy(n);
		}
	}

	private static OnControllerCreateAction cCreateAction=new OnControllerCreateAction();
	private static OnControllerUpdateAction cUpdateAction=new OnControllerUpdateAction();
	private static OnControllerReportAction cReportAction=new OnControllerReportAction();
	private static OnControllerDeleteAction cDeleteAction=new OnControllerDeleteAction();
	
	
	public static void start () {
		DatabaseController.registerOnCreateAction(cCreateAction);
		DatabaseController.registerOnUpdateAction(cUpdateAction);
		DatabaseController.registerOnReportAction(cReportAction);
		DatabaseController.registerOnDeleteAction(cDeleteAction);
		
		for (Controller c : Cache.Controllers.map.values()) {
			new ControllerTracker(c.getControllername());
		}
	}
	
	public static void stop () {
		DatabaseController.unregisterOnCreateAction(cCreateAction);
		DatabaseController.unregisterOnUpdateAction(cUpdateAction);
		DatabaseController.unregisterOnReportAction(cReportAction);
		DatabaseController.unregisterOnDeleteAction(cDeleteAction);
		
		ControllerTracker.destroyAll();
	}
	
}
