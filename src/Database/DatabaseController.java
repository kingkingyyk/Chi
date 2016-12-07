package Database;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import Chi.Logger;
import Chi.Utility;
import Entity.Actuator;
import Entity.Controller;
import Entity.Sensor;
import Entity.Site;

public class DatabaseController {

	public static interface OnCreateAction {
		public void run (String n, String s, double x, double y, int t);
	}
	
	public static interface OnUpdateAction {
		public void run (String oldN, String n, String s, double x, double y, int t);
	}
	
	public static interface OnReportAction {
		public void run (String n);
	}
	
	public static interface OnDeleteAction {
		public void run (String n);
	}
	
	private static ArrayList<OnCreateAction> OnCreateList=new ArrayList<>();
	private static ArrayList<OnUpdateAction> OnUpdateList=new ArrayList<>();
	private static ArrayList<OnReportAction> OnReportList=new ArrayList<>();
	private static ArrayList<OnDeleteAction> OnDeleteList=new ArrayList<>();
	
	public static void registerOnCreateAction (OnCreateAction a) {
		if (!OnCreateList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseController - Registered "+a.toString()+" to OnCreate callback");
			OnCreateList.add(a);
		}
	}
	
	public static void unregisterOnCreateAction (OnCreateAction a) {
		if (OnCreateList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseController - Unregistered "+a.toString()+" from OnCreate callback");
			OnCreateList.remove(a);
		}
	}
	
	public static void registerOnUpdateAction (OnUpdateAction a) {
		if (!OnUpdateList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseController - Registered "+a.toString()+" to OnUpdate callback");
			OnUpdateList.add(a);
		}
	}
	
	public static void unregisterOnUpdateAction (OnUpdateAction a) {
		if (OnUpdateList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseController - Unregistered "+a.toString()+" from OnUpdate callback");
			OnUpdateList.remove(a);
		}
	}
	
	public static void registerOnReportAction (OnReportAction a) {
		if (!OnReportList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseController - Registered "+a.toString()+" to OnReport callback");
			OnReportList.add(a);
		}
	}
	
	public static void unregisterOnReportAction (OnReportAction a) {
		if (OnReportList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseController - Unregistered "+a.toString()+" from OnReport callback");
			OnReportList.remove(a);
		}
	}
	
	public static void registerOnDeleteAction (OnDeleteAction a) {
		if (!OnDeleteList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseController - Registered "+a.toString()+" to OnDelete callback");
			OnDeleteList.add(a);
		}
	}
	
	public static void unregisterOnDeleteAction (OnDeleteAction a) {
		if (OnDeleteList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseController - Unregistered "+a.toString()+" from OnDelete callback");
			OnDeleteList.remove(a);
		}
	}
	
	public static boolean createController (String n, String s, double x, double y, int t) {
		Logger.log(Logger.LEVEL_INFO,"DatabaseController - Create");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			Controller ctrl = (Controller) session.get(Controller.class,n);
			if (ctrl==null) {
				ctrl=new Controller(n);
				ctrl.setSite((Site)session.get(Site.class,s));
				ctrl.setPositionx(x);
				ctrl.setPositiony(y);
				ctrl.setReporttimeout(t);
				ctrl.setIpaddress("255.255.255.255");
				ctrl.setLastreporttime(new Date(0));
				session.save(ctrl);
				tx.commit();
				Cache.Controllers.map.put(n,ctrl);
	
				Logger.log(Logger.LEVEL_INFO,"DatabaseController - Create - Execute Callbacks");
				for (OnCreateAction a : OnCreateList) a.run(n,s,x,y,t);
				flag = true;
			} else Logger.log(Logger.LEVEL_WARNING,"DB Create Controller - Controller already exists");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log(Logger.LEVEL_ERROR,"DatabaseController - Create - " + e.getMessage());
		} finally {session.close();}
		return flag;
	}
	
	public static boolean updateController (String oldN, String n, String s, double x, double y, int t) {
		Logger.log(Logger.LEVEL_INFO,"DatabaseController - Update");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			Controller ctrl = Cache.Controllers.map.get(oldN);
			if (!oldN.equals(n)) {
				session.createQuery("Update Controller set ControllerName='" + n + "' where ControllerName='" + oldN + "'").executeUpdate();
				Cache.Controllers.map.remove(oldN);
				Cache.Controllers.map.put(n,ctrl);
			}
			if (ctrl!=null) {
				ctrl.setControllername(n);
				ctrl.setSite((Site)session.get(Site.class,s));
				ctrl.setPositionx(x);
				ctrl.setPositiony(y);
				ctrl.setReporttimeout(t);
				ctrl.setLastreporttime(new Date(0));
				session.update(ctrl);
				tx.commit();
				Cache.Controllers.map.put(n,ctrl);
				for (Sensor se : Cache.Sensors.map.values())
					if (se.getController().getControllername().equals(oldN))
						se.setController(ctrl);
				for (Actuator act : Cache.Actuators.map.values())
					if (act.getController().getControllername().equals(oldN))
						act.setController(ctrl);
	
				Logger.log(Logger.LEVEL_INFO,"DatabaseController - Update - Execute Callbacks");
				for (OnUpdateAction a : OnUpdateList) a.run(oldN,n,s,x,y,t);
				flag = true;
			} else Logger.log(Logger.LEVEL_WARNING,"DB Update Controller - Controller doesn't exist");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log(Logger.LEVEL_ERROR,"DatabaseController - Update - " + e.getMessage());
		} finally {session.close();}
		return flag;
	}
	
	public static boolean updateControllerReport (String n, String ip, LocalDateTime dt) {
		Logger.log(Logger.LEVEL_INFO,"DatabaseController - Update Report");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			Controller ctrl = Cache.Controllers.map.get(n);
			if (ctrl!=null) {
				ctrl.setLastreporttime(Utility.localDateTimeToSQLDate(dt));
				ctrl.setIpaddress(ip);
				session.update(ctrl);
				tx.commit();
	
				Logger.log(Logger.LEVEL_INFO,"DatabaseController - Update Report - Execute Callbacks");
				for (OnReportAction a : OnReportList) a.run(n);
				flag = true;
			} else Logger.log(Logger.LEVEL_WARNING,"DB Update Controller Report - Controller doesn't exist");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log(Logger.LEVEL_ERROR,"DatabaseController - Update Report - " + e.getMessage());
		} finally {session.close();}
		return flag;
	}
	
	public static boolean deleteController (String n) {
		Logger.log(Logger.LEVEL_INFO,"DatabaseController - Delete");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			Controller ctrl = (Controller) session.get(Controller.class,n);
			if (ctrl!=null) {
				for (Sensor s : Cache.Sensors.map.values()) if (s.getController().getControllername().equals(n)) DatabaseSensor.deleteSensor(s.getSensorname());
				for (Actuator act : Cache.Actuators.map.values()) if (act.getController().getControllername().equals(n)) DatabaseActuator.deleteActuator(act.getName());
				
				session.delete(ctrl);
				tx.commit();
				Cache.Controllers.map.remove(n);
	
				Logger.log(Logger.LEVEL_INFO,"DatabaseController - Delete - Execute Callbacks");
				for (OnDeleteAction a : OnDeleteList) a.run(n);
				flag = true;
			} else Logger.log(Logger.LEVEL_WARNING,"DB Delete Controller - Controller doesn't exist");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log(Logger.LEVEL_ERROR,"DatabaseController - Delete - " + e.getMessage());
		} finally {session.close();}
		return flag;
	}

}
