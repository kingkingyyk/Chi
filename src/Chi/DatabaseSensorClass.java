package Chi;

import java.util.ArrayList;

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
	
	private static ArrayList<OnCreateAction> OnCreateList=new ArrayList<>();
	private static ArrayList<OnUpdateAction> OnUpdateList=new ArrayList<>();
	private static ArrayList<OnDeleteAction> OnDeleteList=new ArrayList<>();
	
	public static void registerOnCreateAction (OnCreateAction a) {
		if (!OnCreateList.contains(a)) {
			Logger.log("DatabaseSensorClass - Registered "+a.toString()+" to OnCreate callback");
			OnCreateList.add(a);
		}
	}
	
	public static void registerOnUpdateAction (OnUpdateAction a) {
		if (!OnUpdateList.contains(a)) {
			Logger.log("DatabaseSensorClass - Registered "+a.toString()+" to OnUpdate callback");
			OnUpdateList.add(a);
		}
	}
	
	public static void registerOnDeleteAction (OnDeleteAction a) {
		if (!OnDeleteList.contains(a)) {
			Logger.log("DatabaseSensorClass - Registered "+a.toString()+" to OnDelete callback");
			OnDeleteList.add(a);
		}
	}
	
	public static boolean createSensorClass (String name) {
		Logger.log("DatabaseSensorClass - Create");
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
	
				Logger.log("DatabaseSensorClass - Create - Execute Callbacks");
				for (OnCreateAction a : OnCreateList) a.run(name);
				flag = true;
			} else Logger.log("DB Create SensorClass - Class already exists");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log("DatabaseSensorClass - Create - Error" + e.getMessage());
		} finally {session.close();}
		return flag;
	}
	
	public static boolean updateSensorClass (String oldN, String newN) {
		Logger.log("DatabaseSensorClass - Update");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			if (!oldN.equals(newN)) session.createQuery("update Sensorclass set ClassName='"+newN+"' where ClassName='"+oldN+"'").executeUpdate();
			Sensorclass s = session.get(Sensorclass.class,newN);
			if (s!=null) {
				session.update(s);
				tx.commit();
	
				Logger.log("DatabaseSensorClass - Update - Execute Callbacks");
				for (OnUpdateAction a : OnUpdateList) a.run(oldN,newN);
				flag = true;
			} else Logger.log("DB Update SensorClass - Class doesn't exist");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log("DatabaseSensorClass - Update - Error" + e.getMessage());
		} finally {session.close();}
		return flag;
	}
	
	public static boolean deleteSensorClass (String name) {
		Logger.log("DatabaseSensorClass - Delete");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			Sensorclass s = session.get(Sensorclass.class,name);
			if (s!=null) {
				session.delete(s);
				tx.commit();
	
				Logger.log("DatabaseSensorClass - Delete - Execute Callbacks");
				for (OnDeleteAction a : OnDeleteList) a.run(name);
				flag = true;
			} else Logger.log("DB Delete SensorClass - Class doesn't exist");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log("DatabaseSensorClass - Delete - Error" + e.getMessage());
		} finally {session.close();}
		return flag;
	}

}
