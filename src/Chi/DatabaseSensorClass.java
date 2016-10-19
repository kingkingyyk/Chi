package Chi;

import java.util.LinkedList;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DatabaseSensorClass {
	
	public static interface OnCreateAction {
		public void run (String name);
	}
	
	public static interface OnUpdateAction {
		public void run (String oldName, String name);
	}
	
	public static interface OnDeleteAction {
		public void run (String name);
	}
	
	private static LinkedList<OnCreateAction> OnCreateList=new LinkedList<>();
	private static LinkedList<OnUpdateAction> OnUpdateList=new LinkedList<>();
	private static LinkedList<OnDeleteAction> OnDeleteList=new LinkedList<>();
	
	public static void registerOnCreateAction (OnCreateAction a) {
		if (!OnCreateList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseSensorClass - Registered "+a.toString()+" to OnCreate callback");
			OnCreateList.add(a);
		}
	}
	
	public static void registerOnUpdateAction (OnUpdateAction a) {
		if (!OnUpdateList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseSensorClass - Registered "+a.toString()+" to OnUpdate callback");
			OnUpdateList.add(a);
		}
	}
	
	public static void registerOnDeleteAction (OnDeleteAction a) {
		if (!OnDeleteList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseSensorClass - Registered "+a.toString()+" to OnDelete callback");
			OnDeleteList.add(a);
		}
	}
	
	public static boolean createSensorClass (String name) {
		Logger.log(Logger.LEVEL_INFO,"DatabaseSensorClass - Create");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			Sensorclass s = session.get(Sensorclass.class,name);
			if (s==null) {
				s=new Sensorclass(name);
				session.save(s);
				tx.commit();
				Cache.SensorClasses.map.put(name,s);
	
				Logger.log(Logger.LEVEL_INFO,"DatabaseSensorClass - Create - Execute Callbacks");
				for (OnCreateAction a : OnCreateList) a.run(name);
				flag = true;
			} else Logger.log(Logger.LEVEL_WARNING,"DB Create SensorClass - Class already exists");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log(Logger.LEVEL_ERROR,"DatabaseSensorClass - Create - " + e.getMessage());
		} finally {session.close();}
		return flag;
	}
	
	public static boolean updateSensorClass (String oldN, String newN) {
		Logger.log(Logger.LEVEL_INFO,"DatabaseSensorClass - Update");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			if (!oldN.equals(newN)) {
				session.createQuery("update Sensorclass set ClassName='"+newN+"' where ClassName='"+oldN+"'").executeUpdate();
				Cache.SensorClasses.map.remove(oldN);
			}
			Sensorclass s = session.get(Sensorclass.class,newN);
			if (s!=null) {
				session.update(s);
				tx.commit();
				Cache.SensorClasses.map.put(newN,s);
				for (Sensor se : Cache.Sensors.map.values())
					if (se.getSensorclass().getClassname().equals(oldN))
						se.setSensorclass(s);
				
				Logger.log(Logger.LEVEL_INFO,"DatabaseSensorClass - Update - Execute Callbacks");
				for (OnUpdateAction a : OnUpdateList) a.run(oldN,newN);
				flag = true;
			} else Logger.log(Logger.LEVEL_WARNING,"DB Update SensorClass - Class doesn't exist");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log(Logger.LEVEL_ERROR,"DatabaseSensorClass - Update - " + e.getMessage());
		} finally {session.close();}
		return flag;
	}
	
	public static boolean deleteSensorClass (String name) {
		Logger.log(Logger.LEVEL_INFO,"DatabaseSensorClass - Delete");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			Sensorclass s = session.get(Sensorclass.class,name);
			if (s!=null) {
				session.delete(s);
				tx.commit();
				Cache.SensorClasses.map.remove(name);
	
				Logger.log(Logger.LEVEL_INFO,"DatabaseSensorClass - Delete - Execute Callbacks");
				for (OnDeleteAction a : OnDeleteList) a.run(name);
				flag = true;
			} else Logger.log(Logger.LEVEL_WARNING,"DB Delete SensorClass - Class doesn't exist");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log(Logger.LEVEL_ERROR,"DatabaseSensorClass - Delete - " + e.getMessage());
		} finally {session.close();}
		return flag;
	}

}
