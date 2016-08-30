package Chi;

import java.util.ArrayList;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DatabaseActuator {

	public static interface OnCreateAction {
		public void run(String n, String u);
	}

	public static interface OnUpdateAction {
		public void run(String oldN, String n, String u);
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
			Logger.log("DatabaseActuator - Registered " + a.toString() + " to OnCreate callback");
			OnCreateList.add(a);
		}
	}

	public static void registerOnUpdateAction(OnUpdateAction a) {
		if (!OnUpdateList.contains(a)) {
			Logger.log("DatabaseActuator - Registered " + a.toString() + " to OnUpdate callback");
			OnUpdateList.add(a);
		}
	}

	public static void registerOnUpdateStatusAction(OnUpdateStatusAction a) {
		if (!OnUpdateStatusList.contains(a)) {
			Logger.log("DatabaseActuator - Registered " + a.toString() + " to OnUpdateStatus callback");
			OnUpdateStatusList.add(a);
		}
	}

	public static void registerOnDeleteAction(OnDeleteAction a) {
		if (!OnDeleteList.contains(a)) {
			Logger.log("DatabaseActuator - Registered " + a.toString() + " to OnDelete callback");
			OnDeleteList.add(a);
		}
	}

	public static boolean createActuator(String n, String u) {
		Logger.log("DatabaseActuator - Create");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			Actuator act = session.get(Actuator.class,n);
			if (act==null) {
				act = new Actuator(n, (Controller) session.get(Controller.class, "DefaultController"),"Pending Update", null, null);
				session.save(act);
				tx.commit();
	
				Logger.log("DatabaseActuator - Create - Execute Callbacks");
				for (OnCreateAction a : OnCreateList) a.run(n, u);
				flag = true;
			} else Logger.log("DB Create Actuator - Actuator already exists");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log("DatabaseActuator - Create - Error" + e.getMessage());
		} finally {session.close();}
		return flag;
	}

	public static boolean updateActuator(String oldN, String n, String u) {
		Logger.log("DBActuator Update");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag = false;
		try {
			tx = session.beginTransaction();
			if (!oldN.equals(n)) session.createQuery("Update Actuator set Name='" + n + "' where Name='" + oldN + "'").executeUpdate();
			Actuator act = session.get(Actuator.class, n);
			if (act != null) {
				act.setController((Controller) session.get(Controller.class, u));
				session.update(act);
				tx.commit();
				
				Logger.log("DB Update Actuator - Execute Callbacks");
				for (OnUpdateAction a : OnUpdateList) a.run(oldN, n, u);
				flag = true;
			} else Logger.log("DB Update Actuator - Actuator doesn't exist");
		} catch (HibernateException e) {
			if (tx != null) tx.rollback();
			Logger.log("DB Update Actuator - Error" + e.getMessage());
		} finally { session.close();}
		return flag;
	}

	public static boolean updateActuatorStatus(String n, String st) {
		Logger.log("DBActuator Update Status");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			Actuator act = session.get(Actuator.class, n);
			if (act!=null) {
				act.setStatus(st);
				session.update(act);
				tx.commit();
				
				Logger.log("DB Update Actuator Status - Execute Callbacks");
				for (OnUpdateStatusAction a : OnUpdateStatusList) a.run(n, st);
				flag = true;
			} else Logger.log("DB Update Actuator Status - Actuator doesn't exist");
		} catch (HibernateException e) {
			if (tx != null) tx.rollback();
			Logger.log("DB Update Actuator Status - Error" + e.getMessage());
		} finally {
			session.close();
		}
		return flag;
	}

	public static boolean deleteActuator(String n) {
		Logger.log("DBActuator Delete ");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			Actuator act = session.get(Actuator.class, n);
			if (act!=null) {
				session.delete(act);
				tx.commit();
				
				Logger.log("DB Delete Actuator - Execute Callbacks");
				for (OnDeleteAction a : OnDeleteList) a.run(n);
				flag = true;
			} else Logger.log("DB Delete Actuator - Actuator doesn't exist");
		} catch (HibernateException e) {
			if (tx != null) tx.rollback();
			Logger.log("DB Delete Actuator - Error" + e.getMessage());
		} finally {
			session.close();
		}
		return flag;
	}

}
