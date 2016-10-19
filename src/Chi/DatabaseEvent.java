package Chi;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DatabaseEvent {

	public static interface OnSensorEventLoggedAction {
		public void run(String name, String eventType, String eventValue);
	}

	public static interface OnControllerEventLoggedAction {
		public void run(String name, String eventType, String eventValue);
	}

	private static ArrayList<OnSensorEventLoggedAction> OnSensorEventLoggedList = new ArrayList<>();
	private static ArrayList<OnControllerEventLoggedAction> OnControllerEventLoggedList = new ArrayList<>();

	public static void registerOnSensorEventLoggedAction(OnSensorEventLoggedAction a) {
		if (!OnSensorEventLoggedList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseEvent - Registered " + a.toString() + " to OnSensorEventLogged callback");
			OnSensorEventLoggedList.add(a);
		}
	}
	
	public static void unregisterOnSensorEventLoggedAction(OnSensorEventLoggedAction a) {
		if (OnSensorEventLoggedList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseEvent - Unregistered " + a.toString() + " from OnSensorEventLogged callback");
			OnSensorEventLoggedList.remove(a);
		}
	}
	
	public static void registerOnControllerEventLoggedAction(OnControllerEventLoggedAction a) {
		if (!OnControllerEventLoggedList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseEvent - Registered " + a.toString() + " to OnControllerEventLogged callback");
			OnControllerEventLoggedList.add(a);
		}
	}
	
	public static void unregisterOnControllerEventLoggedAction(OnControllerEventLoggedAction a) {
		if (OnControllerEventLoggedList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseEvent - Unregistered " + a.toString() + " from OnControllerEventLogged callback");
			OnControllerEventLoggedList.remove(a);
		}
	}
	
	public static boolean logSensorEvent (String name, String eventType, String eventValue) {
		StringBuilder sbLog=new StringBuilder("DatabaseEvent - Log Sensor Event ");
		sbLog.append(name); sbLog.append("|"); sbLog.append(eventType); sbLog.append("|"); sbLog.append(eventValue);
		Logger.log(Logger.LEVEL_INFO,sbLog.toString());
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			Sensorevent se =new Sensorevent(Cache.Sensors.map.get(name),new Date(),eventType,eventValue);
			session.save(se);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log(Logger.LEVEL_ERROR,"DatabaseSensorEvent - Log controller event - " + e.getMessage());
		} finally {session.close();}
		return flag;
	}

	public static ArrayList<Sensorevent> getSensorEventByName (String name) {
		Logger.log(Logger.LEVEL_INFO,"DatabaseEvent - Get sensor event "+name);
		Session session=Cache.factory.openSession();
		Transaction tx=null;
		ArrayList<Sensorevent> list=new ArrayList<>();
		try {
			tx=session.beginTransaction();
			@SuppressWarnings("rawtypes")
			List l=session.createQuery("FROM Sensorevent WHERE SensorName="+name).getResultList();
			for (Object o : l) {
				list.add((Sensorevent) o);
			}
		} catch (HibernateException e) {
	        Logger.log(Logger.LEVEL_ERROR,"DatabaseEvent - Get sensor event - "+e.getMessage());
	        if (tx!=null) tx.rollback();
	    } finally {
	         session.close(); 
	    }
		return list;
	}
	
	public static ArrayList<Sensorevent> getSensorEventBetweenTime (LocalDateTime min, LocalDateTime max) {
		Logger.log(Logger.LEVEL_INFO,"DatabaseEvent - Get sensor event between time");
		Session session=Cache.factory.openSession();
		Transaction tx=null;
		ArrayList<Sensorevent> list=new ArrayList<>();
		try {
			tx=session.beginTransaction();
			@SuppressWarnings("rawtypes")
			
			List l=session.createQuery("FROM Sensorevent WHERE TimeStp>TIMESTAMP("+Utility.localDateTimeToLong(min)+") AND TimeStp<TIMESTAMP("+Utility.localDateTimeToLong(max)+")").getResultList();
			for (Object o : l) {
				list.add((Sensorevent) o);
			}
		} catch (HibernateException e) {
	        Logger.log(Logger.LEVEL_ERROR,"DatabaseEvent - Get sensor event - Error - "+e.getMessage());
	        if (tx!=null) tx.rollback();
	    } finally {
	         session.close(); 
	    }
		return list;
	}
	
	public static boolean logControllerEvent (String name, String eventType, String eventValue) {
		StringBuilder sbLog=new StringBuilder("DatabaseEvent - Log Controller Event ");
		sbLog.append(name); sbLog.append("|"); sbLog.append(eventType); sbLog.append("|"); sbLog.append(eventValue);
		Logger.log(Logger.LEVEL_INFO,sbLog.toString());
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			Controllerevent ce =new Controllerevent(Cache.Controllers.map.get(name),new Date(),eventType,eventValue);
			session.save(ce);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log(Logger.LEVEL_ERROR,"DatabaseSensorEvent - Log controller event - " + e.getMessage());
		} finally {session.close();}
		return flag;
	}
	
	public static ArrayList<Controllerevent> getControllerEventByName (String name) {
		Logger.log(Logger.LEVEL_INFO,"DatabaseEvent - Get controller event "+name);
		Session session=Cache.factory.openSession();
		Transaction tx=null;
		ArrayList<Controllerevent> list=new ArrayList<>();
		try {
			tx=session.beginTransaction();
			@SuppressWarnings("rawtypes")
			List l=session.createQuery("FROM Controllerevent WHERE SensorName="+name).getResultList();
			for (Object o : l) {
				list.add((Controllerevent) o);
			}
		} catch (HibernateException e) {
	        Logger.log(Logger.LEVEL_ERROR,"DatabaseEvent - Get controller event - "+e.getMessage());
	        if (tx!=null) tx.rollback();
	    } finally {
	         session.close(); 
	    }
		return list;
	}
	
	public static ArrayList<Controllerevent> getControllerEventBetweenTime (LocalDateTime min, LocalDateTime max) {
		Logger.log(Logger.LEVEL_INFO,"DatabaseEvent - Get controller event between time");
		Session session=Cache.factory.openSession();
		Transaction tx=null;
		ArrayList<Controllerevent> list=new ArrayList<>();
		try {
			tx=session.beginTransaction();
			@SuppressWarnings("rawtypes")
			
			List l=session.createQuery("FROM Controllerevent WHERE TimeStp>TIMESTAMP("+Utility.localDateTimeToLong(min)+") AND TimeStp<TIMESTAMP("+Utility.localDateTimeToLong(max)+")").getResultList();
			for (Object o : l) {
				list.add((Controllerevent) o);
			}
		} catch (HibernateException e) {
	        Logger.log(Logger.LEVEL_ERROR,"DatabaseEvent - Get controller event - Error - "+e.getMessage());
	        if (tx!=null) tx.rollback();
	    } finally {
	         session.close(); 
	    }
		return list;
	}
}
