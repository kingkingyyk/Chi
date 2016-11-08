package Database;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import Chi.Logger;
import Chi.Utility;
import Entity.Usersensornotification;
import Entity.UsersensornotificationId;

public class DatabaseUserSensorNotification {

	public static boolean subscribeSensorNotification (String username, String sensorname) {
		Logger.log(Logger.LEVEL_INFO,"DB Subscribe User Sensor Notification");
		if (!Cache.Users.map.containsKey(username)) {
			Logger.log(Logger.LEVEL_INFO,"DB Subscribe User Sensor Notification - User doesn't exist!");
			return false;
		} else 	if (!Cache.Users.map.containsKey(username)) {
			Logger.log(Logger.LEVEL_INFO,"DB Subscribe User Sensor Notification - Sensor doesn't exist!");
			return false;
		} else {
			Session session=Cache.factory.openSession();
			Transaction tx=null;
			boolean flag;
			try {
				tx=session.beginTransaction();
				UsersensornotificationId uid=new UsersensornotificationId(username,sensorname);
				session.save(new Usersensornotification(uid,Cache.Sensors.map.get(sensorname),Cache.Users.map.get(username),new Date()));
				tx.commit();
				
				Logger.log(Logger.LEVEL_INFO,"DB Subscribe User Sensor Notification - Execute Callbacks");
				flag=true;
			} catch (HibernateException e) {
				if (tx!=null) tx.rollback();
				Logger.log(Logger.LEVEL_ERROR,"DB Subscribe User Sensor Notification - "+e.getMessage());
				flag=false;
			} finally { session.close(); }
			return flag;
		}
	}
	
	public static boolean updateSensorNotification (String username, String sensorname, Date time) {
		Logger.log(Logger.LEVEL_INFO,"DB Update User Sensor Notification");
		if (!Cache.Users.map.containsKey(username)) {
			Logger.log(Logger.LEVEL_INFO,"DB Update User Sensor Notification - User doesn't exist!");
			return false;
		} else 	if (!Cache.Users.map.containsKey(username)) {
			Logger.log(Logger.LEVEL_INFO,"DB Update User Sensor Notification - Sensor doesn't exist!");
			return false;
		} else {
			Session session=Cache.factory.openSession();
			Transaction tx=null;
			boolean flag;
			try {
				tx=session.beginTransaction();
				UsersensornotificationId uid=new UsersensornotificationId(username,sensorname);
				Usersensornotification un=session.get(Usersensornotification.class,uid);
				if (un!=null) {
					un.setLastread(time);
					session.update(un);
				}
				tx.commit();
				
				Logger.log(Logger.LEVEL_INFO,"DB Update User Sensor Notification - Execute Callbacks");
				flag=true;
			} catch (HibernateException e) {
				if (tx!=null) tx.rollback();
				Logger.log(Logger.LEVEL_ERROR,"DB Update User Sensor Notification - "+e.getMessage());
				flag=false;
			} finally { session.close(); }
			return flag;
		}
	}
	
	public static LocalDateTime getSensorNotificationTime (String username, String sensorname) {
		Logger.log(Logger.LEVEL_INFO,"DB Get User Sensor Notification Time");
		if (!Cache.Users.map.containsKey(username)) {
			Logger.log(Logger.LEVEL_INFO,"DB Get User Sensor Notification Time - User doesn't exist!");
			return null;
		} else 	if (!Cache.Users.map.containsKey(username)) {
			Logger.log(Logger.LEVEL_INFO,"DB Get User Sensor Notification Time - Sensor doesn't exist!");
			return null;
		} else {
			Session session=Cache.factory.openSession();
			LocalDateTime toReturn=null;
			try {
				UsersensornotificationId uid=new UsersensornotificationId(username,sensorname);
				Usersensornotification un=session.get(Usersensornotification.class,uid);
				if (un!=null) toReturn=Utility.dateToLocalDateTime(un.getLastread());
				else Logger.log(Logger.LEVEL_INFO,"DB Get User Sensor Notification Time - Subscription not found");
				
				Logger.log(Logger.LEVEL_INFO,"DB Get User Sensor Notification Time - Execute Callbacks");
			} catch (HibernateException e) {
				Logger.log(Logger.LEVEL_ERROR,"DB Get User Sensor Notification Time - "+e.getMessage());
			} finally { session.close(); }
			return toReturn;
		}
	}
	
	
	public static boolean unsubscribeSensorNotification (String username, String sensorname) {
		Logger.log(Logger.LEVEL_INFO,"DB Unsubscribe User Sensor Notification");
		if (!Cache.Users.map.containsKey(username)) {
			Logger.log(Logger.LEVEL_INFO,"DB Unsubscribe User Sensor Notification - User doesn't exist!");
			return false;
		} else 	if (!Cache.Users.map.containsKey(username)) {
			Logger.log(Logger.LEVEL_INFO,"DB Unsubscribe User Sensor Notification - Sensor doesn't exist!");
			return false;
		} else {
			Session session=Cache.factory.openSession();
			Transaction tx=null;
			boolean flag;
			try {
				tx=session.beginTransaction();
				UsersensornotificationId uid=new UsersensornotificationId(username,sensorname);
				Usersensornotification un=session.get(Usersensornotification.class,uid);
				session.delete(un);
				tx.commit();
				
				Logger.log(Logger.LEVEL_INFO,"DB Unsubscribe User Sensor Notification - Execute Callbacks");
				flag=true;
			} catch (HibernateException e) {
				if (tx!=null) tx.rollback();
				Logger.log(Logger.LEVEL_ERROR,"DB Unsubscribe User Sensor Notification - "+e.getMessage());
				flag=false;
			} finally { session.close(); }
			return flag;
		}
	}
	
	public static ArrayList<String> getSubscription (String username) {
		Logger.log(Logger.LEVEL_INFO,"DB Get User Sensor Notification Subscription");
		
		ArrayList<String> toReturn=new ArrayList<>();
		if (!Cache.Users.map.containsKey(username)) Logger.log(Logger.LEVEL_INFO,"DB Get User Sensor Notification Subscription - User doesn't exist!");
		else {
			Session session=Cache.factory.openSession();
			try {
				@SuppressWarnings("unchecked")
				List<Usersensornotification> l=session.createQuery("FROM Usersensornotification").getResultList();
				for (Usersensornotification n : l) if (n.getUser().equals(username)) toReturn.add(n.getSensor().getSensorname());

			} catch (HibernateException e) {
				Logger.log(Logger.LEVEL_ERROR,"DB Get User Sensor Notification Subscription - "+e.getMessage());
			} finally { session.close(); }
		}
		return toReturn;
	}
}
