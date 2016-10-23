package Database;

import java.util.LinkedList;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import Chi.Logger;
import Entity.Controller;
import Entity.Sensor;
import Entity.Sensorclass;
import Entity.Sensorevent;

public class DatabaseSensor {

	public static interface OnCreateAction {
		public void run (String sn, String cn, double min, double max, double trans, String unit, String con, double minT, double maxT, double px, double py);
	}
	
	public static interface OnUpdateAction {
		public void run (String oldSN, String sn, String cn, double min, double max, double trans, String unit, String con, double minT, double maxT, double px, double py);
	}
	
	public static interface OnDeleteAction {
		public void run (String sn);
	}
	
	private static LinkedList<OnCreateAction> OnCreateList=new LinkedList<>();
	private static LinkedList<OnUpdateAction> OnUpdateList=new LinkedList<>();
	private static LinkedList<OnDeleteAction> OnDeleteList=new LinkedList<>();
	
	public static void registerOnCreateAction (OnCreateAction a) {
		if (!OnCreateList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseSensor - Registered "+a.toString()+" to OnCreate callback");
			OnCreateList.add(a);
		}
	}
	
	public static void registerOnUpdateAction (OnUpdateAction a) {
		if (!OnUpdateList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseSensor - Registered "+a.toString()+" to OnUpdate callback");
			OnUpdateList.add(a);
		}
	}
	
	public static void registerOnDeleteAction (OnDeleteAction a) {
		if (!OnDeleteList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseSensor - Registered "+a.toString()+" to OnDelete callback");
			OnDeleteList.add(a);
		}
	}
	
	public static boolean createSensor (String sn, String cn, double min, double max, double trans, String unit, String con, double minT, double maxT, double px, double py) {
		Logger.log(Logger.LEVEL_INFO,"DatabaseSensor - Create");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			Sensor s = session.get(Sensor.class,sn);
			if (s==null) {
				s=new Sensor(sn,session.get(Controller.class,con),session.get(Sensorclass.class,cn),min,max,trans,unit,minT,maxT,px,py,null);
				session.save(s);
				tx.commit();
				Cache.Sensors.map.put(sn,s);
	
				Logger.log(Logger.LEVEL_INFO,"DatabaseSensor - Create - Execute Callbacks");
				for (OnCreateAction a : OnCreateList) a.run(sn,cn,min,max,trans,unit,con,minT,maxT,px,py);
				flag = true;
			} else Logger.log(Logger.LEVEL_WARNING,"DB Create Sensor - Sensor already exists");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log(Logger.LEVEL_ERROR,"DatabaseSensor - Create - " + e.getMessage());
		} finally {session.close();}
		return flag;
	}
	
	public static boolean updateSensor (String oldSN, String sn, String cn, double min, double max, double trans, String unit, String con, double minT, double maxT, double px, double py) {
		Logger.log(Logger.LEVEL_INFO,"DatabaseSensor - Update");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			if (!oldSN.equals(sn)) {
				session.createQuery("update Sensor set SensorName='"+sn+"' where SensorName='"+oldSN+"'").executeUpdate();
				Cache.Sensors.map.remove(oldSN);
			}
			Sensor s = session.get(Sensor.class,sn);
			if (s!=null) {
				s.setSensorclass(session.get(Sensorclass.class,cn));
				s.setMinvalue(min);
				s.setMaxvalue(max);
				s.setTransformationfactor(trans);
				s.setUnit(unit);
				s.setController(session.get(Controller.class,con));
				s.setMinthreshold(minT);
				s.setMaxthreshold(maxT);
				s.setPositionx(px);
				s.setPositiony(py);
				session.save(s);
				tx.commit();
				Cache.Sensors.map.put(sn,s);
	
				Logger.log(Logger.LEVEL_INFO,"DatabaseSensor - Update - Execute Callbacks");
				for (OnUpdateAction a : OnUpdateList) a.run(oldSN, sn,cn,min,max,trans,unit,con,minT,maxT,px,py);
				flag = true;
			} else Logger.log(Logger.LEVEL_WARNING,"DB Update Sensor - Sensor doesn't exist");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log(Logger.LEVEL_ERROR,"DatabaseSensor - Update - " + e.getMessage());
		} finally {session.close();}
		return flag;
	}
	
	public static boolean deleteSensor (String sn) {
		Logger.log(Logger.LEVEL_INFO,"DatabaseSensor - Delete");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			Sensor s = session.get(Sensor.class,sn);
			if (s!=null) {
				for (Sensorevent se : s.getSensorevents()) session.delete(se);
				
				session.delete(s);
				tx.commit();
				Cache.Sensors.map.remove(sn);
	
				Logger.log(Logger.LEVEL_INFO,"DatabaseSensor - Delete - Execute Callbacks");
				for (OnDeleteAction a : OnDeleteList) a.run(sn);
				flag = true;
			} else Logger.log(Logger.LEVEL_WARNING,"DB Delete Sensor - Sensor doesn't exist");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log(Logger.LEVEL_ERROR,"DatabaseSensor - Delete - " + e.getMessage());
		} finally {session.close();}
		return flag;

	}

}
