package Chi;

import java.util.ArrayList;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DatabaseSensorActuatorResponse {

	public static interface OnCreateAction {
		public void run (int id, String an, String action, String expression, boolean en);
	}
	
	public static interface OnUpdateAction {
		public void run (int id, String an, String action, String expression, boolean en);
	}
	
	public static interface OnDeleteAction {
		public void run (int id);
	}
	
	private static ArrayList<OnCreateAction> OnCreateList=new ArrayList<>();
	private static ArrayList<OnUpdateAction> OnUpdateList=new ArrayList<>();
	private static ArrayList<OnDeleteAction> OnDeleteList=new ArrayList<>();
	
	public static void registerOnCreateAction (OnCreateAction a) {
		if (!OnCreateList.contains(a)) {
			Logger.log("DatabaseSensorActuatorResponse - Registered "+a.toString()+" to OnCreate callback");
			OnCreateList.add(a);
		}
	}
	
	public static void registerOnUpdateAction (OnUpdateAction a) {
		if (!OnUpdateList.contains(a)) {
			Logger.log("DatabaseSensorActuatorResponse - Registered "+a.toString()+" to OnUpdate callback");
			OnUpdateList.add(a);
		}
	}
	
	public static void registerOnDeleteAction (OnDeleteAction a) {
		if (!OnDeleteList.contains(a)) {
			Logger.log("DatabaseSensorActuatorResponse - Registered "+a.toString()+" to OnDelete callback");
			OnDeleteList.add(a);
		}
	}
	
	public static boolean createSensorActuatorResponse (String an, String action, String expression, boolean en) {
		Logger.log("DatabaseSensorActuatorResponse - Create");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			Sensoractuatorresponse s=new Sensoractuatorresponse(Cache.Actuators.map.get(an),action,expression,en,null);
			session.save(s);
			tx.commit();
			Cache.SensorActuatorResponses.map.put(String.valueOf(s.getId()),s);
	
			Logger.log("DatabaseSensorActuatorResponse - Create - Execute Callbacks");
			for (OnCreateAction a : OnCreateList) a.run(s.getId(), an, action, expression, en);
			flag = true;
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log("DatabaseSensorActuatorResponse - Create - Error" + e.getMessage());
		} finally {session.close();}
		return flag;
	}
	
	public static boolean updateSensorActuatorResponse (int id, String an, String action, String expression, boolean en) {
		Logger.log("DatabaseSensorActuatorResponse - Update");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			Sensoractuatorresponse s = session.get(Sensoractuatorresponse.class,id);
			if (s!=null) {
				s.setActuator(Cache.Actuators.map.get(an));
				s.setOntriggeraction(action);
				s.setExpression(expression);
				s.setEnabled(en);
				session.save(s);
				tx.commit();
	
				Logger.log("DatabaseSensorActuatorResponse - Update - Execute Callbacks");
				for (OnUpdateAction a : OnUpdateList) a.run(id,an,action,expression,en);
				flag = true;
			} else Logger.log("DB Update SensorActuatorResponse - Sensor doesn't exist");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log("DatabaseSensorActuatorResponse - Update- Error" + e.getMessage());
		} finally {session.close();}
		return flag;
	}
	
	public static boolean deleteSensorActuatorResponse (int id) {
		Logger.log("DatabaseSensorActuatorResponse - Delete");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			Sensoractuatorresponse s = session.get(Sensoractuatorresponse.class,id);
			if (s!=null) {
				session.delete(s);
				tx.commit();
				Cache.SensorActuatorResponses.map.remove(String.valueOf(id));
	
				Logger.log("DatabaseSensorActuatorResponse - Delete - Execute Callbacks");
				for (OnDeleteAction a : OnDeleteList) a.run(id);
				flag = true;
			} else Logger.log("DB Delete SensorActuatorResponse - Sensor doesn't exist");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log("DatabaseSensorActuatorResponse - Delete - Error" + e.getMessage());
		} finally {session.close();}
		return flag;

	}
	
}
