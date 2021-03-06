package Database;

import java.util.ArrayList;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import Chi.Logger;
import Entity.Actuator;
import Entity.Controller;
import Entity.Regularschedule;
import Entity.Sensoractuatorresponse;
import Entity.Specialschedule;

public class DatabaseActuator {

	public static interface OnCreateAction {
		public void run(String n, String u, String slist, double px, double py);
	}

	public static interface OnUpdateAction {
		public void run(String oldN, String n, String u, String slist, double px, double py, String ctrlType);
	}

	public static interface OnUpdateStatusAction {
		public void run(String n, String status);
	}

	public static interface OnDeleteAction {
		public void run(String n);
	}

	private static ArrayList<OnCreateAction> OnCreateList = new ArrayList<>();
	private static ArrayList<OnUpdateAction> OnUpdateList = new ArrayList<>();
	private static ArrayList<OnUpdateStatusAction> OnUpdateStatusList = new ArrayList<>();
	private static ArrayList<OnDeleteAction> OnDeleteList = new ArrayList<>();

	public static void registerOnCreateAction(OnCreateAction a) {
		if (!OnCreateList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseActuator - Registered " + a.toString() + " to OnCreate callback");
			OnCreateList.add(a);
		}
	}

	public static void registerOnUpdateAction(OnUpdateAction a) {
		if (!OnUpdateList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseActuator - Registered " + a.toString() + " to OnUpdate callback");
			OnUpdateList.add(a);
		}
	}

	public static void registerOnUpdateStatusAction(OnUpdateStatusAction a) {
		if (!OnUpdateStatusList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseActuator - Registered " + a.toString() + " to OnUpdateStatus callback");
			OnUpdateStatusList.add(a);
		}
	}

	public static void registerOnDeleteAction(OnDeleteAction a) {
		if (!OnDeleteList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseActuator - Registered " + a.toString() + " to OnDelete callback");
			OnDeleteList.add(a);
		}
	}
	
	public static void unregisterOnCreateAction(OnCreateAction a) {
		if (OnCreateList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseActuator - Unregistered " + a.toString() + " to OnCreate callback");
			OnCreateList.remove(a);
		}
	}

	public static void unregisterOnUpdateAction(OnUpdateAction a) {
		if (OnUpdateList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseActuator - Unregistered " + a.toString() + " to OnUpdate callback");
			OnUpdateList.remove(a);
		}
	}

	public static void unregisterOnUpdateStatusAction(OnUpdateStatusAction a) {
		if (OnUpdateStatusList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseActuator - Unregistered " + a.toString() + " to OnUpdateStatus callback");
			OnUpdateStatusList.remove(a);
		}
	}

	public static void unregisterOnDeleteAction(OnDeleteAction a) {
		if (OnDeleteList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseActuator - Unregistered " + a.toString() + " to OnDelete callback");
			OnDeleteList.remove(a);
		}
	}

	public static boolean createActuator(String n, String u, String slist, double px, double py, String ctrlType) {
		Logger.log(Logger.LEVEL_INFO,"DatabaseActuator - Create");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			Actuator act = session.get(Actuator.class,n);
			if (act==null) {
				act = new Actuator(n, (Controller) session.get(Controller.class, u),"Pending Update", px, py, ctrlType, slist ,null, null, null);
				session.save(act);
				tx.commit();
				Cache.Actuators.map.put(n,act);
	
				Logger.log(Logger.LEVEL_INFO,"DatabaseActuator - Create - Execute Callbacks");
				for (OnCreateAction a : OnCreateList) a.run(n, u, slist, px, py);
				flag = true;
			} else Logger.log(Logger.LEVEL_WARNING,"DB Create Actuator - Actuator already exists");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log(Logger.LEVEL_ERROR,"DatabaseActuator - Create - Error" + e.getMessage());
		} finally {session.close();}
		return flag;
	}

	public static boolean updateActuator(String oldN, String n, String u, String slist, double px, double py, String ctrlType) {
		Logger.log(Logger.LEVEL_INFO,"DBActuator Update");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag = false;
		try {
			tx = session.beginTransaction();
			Actuator act=Cache.Actuators.map.get(oldN);
			if (!oldN.equals(n)) {
				session.createQuery("Update Actuator set Name='" + n + "' where Name='" + oldN + "'").executeUpdate();
				Cache.Actuators.map.remove(oldN);
			}
			if (act != null) {
				act.setName(n);
				act.setController((Controller) session.get(Controller.class, u));
				act.setPositionx(px);
				act.setPositiony(py);
				act.setControltype(ctrlType);
				act.setStatuslist(slist);
				session.update(act);
				tx.commit();
				Cache.Actuators.map.put(n,act);
				for (Regularschedule r : Cache.RegularSchedules.map.values())
					if (r.getActuator().getName().equals(oldN))
						r.setActuator(act);
				for (Specialschedule s : Cache.SpecialSchedules.map.values())
					if (s.getActuator().getName().equals(oldN))
						s.setActuator(act);
				
				Logger.log(Logger.LEVEL_INFO,"DB Update Actuator - Execute Callbacks");
				for (OnUpdateAction a : OnUpdateList) a.run(oldN, n, u, slist, px, py, ctrlType);
				flag = true;
			} else Logger.log(Logger.LEVEL_WARNING,"DB Update Actuator - Actuator doesn't exist");
		} catch (HibernateException e) {
			if (tx != null) tx.rollback();
			Logger.log(Logger.LEVEL_ERROR,"DB Update Actuator - Error" + e.getMessage());
		} finally { session.close();}
		return flag;
	}

	public static boolean updateActuatorStatus(String n, String st) {
		Logger.log(Logger.LEVEL_INFO,"DBActuator Update Status");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			Actuator act = session.get(Actuator.class, n);
			if (act!=null) {
				Cache.Actuators.map.get(n).setStatus(st);
				act.setStatus(st);
				session.update(act);
				tx.commit();
				
				Logger.log(Logger.LEVEL_INFO,"DB Update Actuator Status - Execute Callbacks");
				for (OnUpdateStatusAction a : OnUpdateStatusList) a.run(n, st);
				flag = true;
			} else Logger.log(Logger.LEVEL_WARNING,"DB Update Actuator Status - Actuator doesn't exist");
		} catch (HibernateException e) {
			if (tx != null) tx.rollback();
			Logger.log(Logger.LEVEL_ERROR,"DB Update Actuator Status - Error" + e.getMessage());
		} finally {
			session.close();
		}
		return flag;
	}

	public static boolean deleteActuator(String n) {
		Logger.log(Logger.LEVEL_INFO,"DBActuator Delete ");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			Actuator act = session.get(Actuator.class, n);
			if (act!=null) {
				for (Regularschedule r : Cache.RegularSchedules.map.values()) if (r.getActuator().getName().equals(n)) DatabaseRegularSchedule.deleteRegularSchedule(r.getSchedulename());
				for (Specialschedule s : Cache.SpecialSchedules.map.values()) if (s.getActuator().getName().equals(n)) DatabaseSpecialSchedule.deleteSpecialSchedule(s.getSchedulename());
				for (Sensoractuatorresponse sar : act.getSensoractuatorresponses()) DatabaseSensorActuatorResponse.deleteSensorActuatorResponse(sar.getId());
				
				session.delete(act);
				tx.commit();
				Cache.Actuators.map.remove(n);
				
				Logger.log(Logger.LEVEL_INFO,"DB Delete Actuator - Execute Callbacks");
				for (OnDeleteAction a : OnDeleteList) a.run(n);
				flag = true;
				
				for (Sensoractuatorresponse res : Cache.SensorActuatorResponses.map.values()) {
					if (res.getActuator().getName().equals(n)) {
						DatabaseSensorActuatorResponse.deleteSensorActuatorResponse(res.getId());
						Cache.SensorActuatorResponses.map.remove(String.valueOf(res.getId()));
					}
				}
			} else Logger.log(Logger.LEVEL_WARNING,"DB Delete Actuator - Actuator doesn't exist");
		} catch (HibernateException e) {
			if (tx != null) tx.rollback();
			Logger.log(Logger.LEVEL_ERROR,"DB Delete Actuator - Error" + e.getMessage());
		} finally {
			session.close();
		}
		return flag;
	}

}
