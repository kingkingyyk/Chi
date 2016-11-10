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
import Entity.Useractuatornotification;
import Entity.UseractuatornotificationId;

public class DatabaseUserActuatorNotification {

	public static boolean subscribeActuatorNotification (String username, String actuatorname) {
		Logger.log(Logger.LEVEL_INFO,"DB Subscribe User Actuator Notification");
		if (!Cache.Users.map.containsKey(username)) {
			Logger.log(Logger.LEVEL_INFO,"DB Subscribe User Actuator Notification - User doesn't exist!");
			return false;
		} else 	if (!Cache.Users.map.containsKey(username)) {
			Logger.log(Logger.LEVEL_INFO,"DB Subscribe User Actuator Notification - Actuator doesn't exist!");
			return false;
		} else {
			Session session=Cache.factory.openSession();
			Transaction tx=null;
			boolean flag;
			try {
				tx=session.beginTransaction();
				UseractuatornotificationId uid=new UseractuatornotificationId(Cache.Users.map.get(username),Cache.Actuators.map.get(actuatorname));
				session.save(new Useractuatornotification(uid,new Date()));
				tx.commit();
				
				Logger.log(Logger.LEVEL_INFO,"DB Subscribe User Actuator Notification - Execute Callbacks");
				flag=true;
			} catch (HibernateException e) {
				if (tx!=null) tx.rollback();
				Logger.log(Logger.LEVEL_ERROR,"DB Subscribe User Actuator Notification - "+e.getMessage());
				flag=false;
			} finally { session.close(); }
			return flag;
		}
	}
	
	public static boolean updateActuatorNotification (String username, String actuatorname, Date time) {
		Logger.log(Logger.LEVEL_INFO,"DB Update User Actuator Notification");
		if (!Cache.Users.map.containsKey(username)) {
			Logger.log(Logger.LEVEL_INFO,"DB Update User Actuator Notification - User doesn't exist!");
			return false;
		} else 	if (!Cache.Users.map.containsKey(username)) {
			Logger.log(Logger.LEVEL_INFO,"DB Update User Actuator Notification - Actuator doesn't exist!");
			return false;
		} else {
			Session session=Cache.factory.openSession();
			Transaction tx=null;
			boolean flag;
			try {
				tx=session.beginTransaction();
				UseractuatornotificationId uid=new UseractuatornotificationId(Cache.Users.map.get(username),Cache.Actuators.map.get(actuatorname));
				Useractuatornotification un=session.get(Useractuatornotification.class,uid);
				if (un!=null) {
					un.setLastread(time);
					session.update(un);
				}
				tx.commit();
				
				Logger.log(Logger.LEVEL_INFO,"DB Update User Actuator Notification - Execute Callbacks");
				flag=true;
			} catch (HibernateException e) {
				if (tx!=null) tx.rollback();
				Logger.log(Logger.LEVEL_ERROR,"DB Update User Actuator Notification - "+e.getMessage());
				flag=false;
			} finally { session.close(); }
			return flag;
		}
	}
	
	public static LocalDateTime getActuatorNotificationTime (String username, String actuatorname) {
		Logger.log(Logger.LEVEL_INFO,"DB Get User Actuator Notification Time");
		if (!Cache.Users.map.containsKey(username)) {
			Logger.log(Logger.LEVEL_INFO,"DB Get User Actuator Notification Time - User doesn't exist!");
			return null;
		} else 	if (!Cache.Users.map.containsKey(username)) {
			Logger.log(Logger.LEVEL_INFO,"DB Get User Actuator Notification Time - Actuator doesn't exist!");
			return null;
		} else {
			Session session=Cache.factory.openSession();
			LocalDateTime toReturn=null;
			try {
				UseractuatornotificationId uid=new UseractuatornotificationId(Cache.Users.map.get(username),Cache.Actuators.map.get(actuatorname));
				Useractuatornotification un=session.get(Useractuatornotification.class,uid);
				if (un!=null) toReturn=Utility.dateToLocalDateTime(un.getLastread());
				else Logger.log(Logger.LEVEL_INFO,"DB Get User Actuator Notification Time - Subscription not found");
				
				Logger.log(Logger.LEVEL_INFO,"DB Get User Actuator Notification Time - Execute Callbacks");
			} catch (HibernateException e) {
				Logger.log(Logger.LEVEL_ERROR,"DB Get User Actuator Notification Time - "+e.getMessage());
			} finally { session.close(); }
			return toReturn;
		}
	}
	
	public static boolean unsubscribeActuatorNotification (String username, String actuatorname) {
		Logger.log(Logger.LEVEL_INFO,"DB Unubscribe User Actuator Notification");
		if (!Cache.Users.map.containsKey(username)) {
			Logger.log(Logger.LEVEL_INFO,"DB Unubscribe User Actuator Notification - User doesn't exist!");
			return false;
		} else 	if (!Cache.Users.map.containsKey(username)) {
			Logger.log(Logger.LEVEL_INFO,"DB Unsubscribe User Actuator Notification - Actuator doesn't exist!");
			return false;
		} else {
			Session session=Cache.factory.openSession();
			Transaction tx=null;
			boolean flag;
			try {
				tx=session.beginTransaction();
				UseractuatornotificationId uid=new UseractuatornotificationId(Cache.Users.map.get(username),Cache.Actuators.map.get(actuatorname));
				Useractuatornotification un=session.get(Useractuatornotification.class,uid);
				session.delete(un);
				tx.commit();
				
				Logger.log(Logger.LEVEL_INFO,"DB Unsubscribe User Actuator Notification - Execute Callbacks");
				flag=true;
			} catch (HibernateException e) {
				if (tx!=null) tx.rollback();
				Logger.log(Logger.LEVEL_ERROR,"DB Unsubscribe User Actuator Notification - "+e.getMessage());
				flag=false;
			} finally { session.close(); }
			return flag;
		}
	}
	
	public static ArrayList<Object []> getSubscription (String username) {
		Logger.log(Logger.LEVEL_INFO,"DB Get User Actuator Notification Subscription");
		
		ArrayList<Object []> toReturn=new ArrayList<>();
		if (!Cache.Users.map.containsKey(username)) Logger.log(Logger.LEVEL_INFO,"DB Get User Actuator Notification Subscription - User doesn't exist!");
		else {
			Session session=Cache.factory.openSession();
			try {
				@SuppressWarnings("unchecked")
				List<Useractuatornotification> l=session.createQuery("FROM Useractuatornotification").getResultList();

				for (Useractuatornotification n : l) if (n.getId().getUser().getUsername().equals(username)) toReturn.add(n.toObj());

			} catch (HibernateException e) {
				Logger.log(Logger.LEVEL_ERROR,"DB Get User Actuator Notification Subscription - "+e.getMessage());
			} finally { session.close(); }
		}
		return toReturn;
	}
}
