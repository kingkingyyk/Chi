package Chi;

import java.util.ArrayList;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DatabaseSensor {

	public static interface OnCreateAction {
		public void run (String sn, String cn, double min, double max, double trans, String unit, String con);
	}
	
	public static interface OnUpdateAction {
		public void run (String oldSN, String sn, String cn, double min, double max, double trans, String unit, String con);
	}
	
	public static interface OnDeleteAction {
		public void run (String sn);
	}
	
	private static ArrayList<OnCreateAction> OnCreateList=new ArrayList<>();
	private static ArrayList<OnUpdateAction> OnUpdateList=new ArrayList<>();
	private static ArrayList<OnDeleteAction> OnDeleteList=new ArrayList<>();
	
	public static void registerOnCreateAction (OnCreateAction a) {
		if (!OnCreateList.contains(a)) {
			Logger.log("DatabaseSensor - Registered "+a.toString()+" to OnCreate callback");
			OnCreateList.add(a);
		}
	}
	
	public static void registerOnUpdateAction (OnUpdateAction a) {
		if (!OnUpdateList.contains(a)) {
			Logger.log("DatabaseSensor - Registered "+a.toString()+" to OnUpdate callback");
			OnUpdateList.add(a);
		}
	}
	
	public static void registerOnDeleteAction (OnDeleteAction a) {
		if (!OnDeleteList.contains(a)) {
			Logger.log("DatabaseSensor - Registered "+a.toString()+" to OnDelete callback");
			OnDeleteList.add(a);
		}
	}
	
	public static boolean createSensor (String sn, String cn, double min, double max, double trans, String unit, String con) {
		Logger.log("DatabaseSensor - Create");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			Sensor s = session.get(Sensor.class,sn);
			if (s==null) {
				s=new Sensor(sn,session.get(Controller.class,con),session.get(Sensorclass.class,cn),min,max,trans,unit);
				session.save(s);
				tx.commit();
	
				Logger.log("DatabaseSensor - Create - Execute Callbacks");
				for (OnCreateAction a : OnCreateList) a.run(sn,cn,min,max,trans,unit,con);
				flag = true;
			} else Logger.log("DB Create Sensor - Sensor already exists");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log("DatabaseSensor - Create - Error" + e.getMessage());
		} finally {session.close();}
		return flag;
	}
	
	public static boolean updateSensor (String oldSN, String sn, String cn, double min, double max, double trans, String unit, String con) {
		Logger.log("DatabaseSensor - Update");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			if (!oldSN.equals(sn)) session.createQuery("update Sensor set SensorName='"+sn+"' where SensorName='"+oldSN+"'").executeUpdate();
			Sensor s = session.get(Sensor.class,sn);
			if (s!=null) {
				s.setSensorclass(session.get(Sensorclass.class,cn));
				s.setMinvalue(min);
				s.setMaxvalue(max);
				s.setTransformationfactor(trans);
				s.setUnit(unit);
				s.setController(session.get(Controller.class,con));
				session.save(s);
				tx.commit();
	
				Logger.log("DatabaseSensor - Update - Execute Callbacks");
				for (OnUpdateAction a : OnUpdateList) a.run(oldSN, sn,cn,min,max,trans,unit,con);
				flag = true;
			} else Logger.log("DB Update Sensor - Sensor doesn't exist");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log("DatabaseSensor - Update- Error" + e.getMessage());
		} finally {session.close();}
		return flag;
	}
	
	public static boolean deleteSensor (String sn) {
		Logger.log("DatabaseSensor - Delete");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			Sensor s = session.get(Sensor.class,sn);
			if (s!=null) {
				session.delete(s);
				tx.commit();
	
				Logger.log("DatabaseSensor - Delete - Execute Callbacks");
				for (OnDeleteAction a : OnDeleteList) a.run(sn);
				flag = true;
			} else Logger.log("DB Delete Sensor - Sensor doesn't exist");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log("DatabaseSensor - Delete - Error" + e.getMessage());
		} finally {session.close();}
		return flag;

	}

}
