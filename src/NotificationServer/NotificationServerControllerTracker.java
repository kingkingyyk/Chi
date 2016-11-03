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
		public static HashMap<Controller,ControllerTracker> trackObjs=new HashMap<>();
		private Controller ctrl;
		private int reportTimeout;
		private Date nextExpectedReportTime;
		private Timer expireTimer;
		
		private class OnTimerExpire extends TimerTask implements Runnable {
			public ControllerTracker ct;
			@Override
			public void run() {
				DatabaseEvent.logControllerEvent(ct.ctrl.getControllername(),"Report Not Received","Didn't report itself in expected time!");
			}
		}
		
		public ControllerTracker (Controller c) {
			this.ctrl=c;
			this.reportTimeout=c.getReporttimeout();
			trackObjs.put(c,this);
			
			expireTimer=new Timer();
		}
		
		public void refreshTimerTimeout(int t) {
			if (this.reportTimeout!=t) {
				this.reportTimeout=t;
				this.refreshTimer();
			}
		}
		
		public void refreshTimer() {
			nextExpectedReportTime=Utility.localDateTimeToSQLDate(LocalDateTime.now().plusSeconds(ctrl.getReporttimeout()));
			expireTimer.purge();
			OnTimerExpire t=new OnTimerExpire();
			t.ct=this;
			expireTimer.schedule(t, nextExpectedReportTime);
		}
		
		public static void destroy (String s) {
			for (ControllerTracker ct : trackObjs.values()) {
				if (ct.ctrl.getControllername().equals(s)) {
					ct.expireTimer.purge();
					trackObjs.remove(ct);
					break;
				}
			}
		}
		
		public static void destroyAll () {
			for (ControllerTracker ct : trackObjs.values()) ct.expireTimer.purge();
			trackObjs.clear();
		}
	}
	

	private static class OnControllerCreateAction implements DatabaseController.OnCreateAction {
		public void run (String n, String s, double x, double y, int t) {
			new ControllerTracker(Cache.Controllers.map.get(n));
		}
	}
	
	private static class OnControllerUpdateAction implements DatabaseController.OnUpdateAction {
		public void run (String oldN, String n, String s, double x, double y, int t) {
			ControllerTracker.trackObjs.get(Cache.Controllers.map.get(n)).refreshTimerTimeout(t);
		}
	}
	
	private static class OnControllerReportAction implements DatabaseController.OnReportAction {
		public void run (String n) {
			ControllerTracker.trackObjs.get(Cache.Controllers.map.get(n)).refreshTimer();
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
			new ControllerTracker(c);
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
