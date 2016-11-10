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
import Entity.Usercontrollernotification;
import Entity.UsercontrollernotificationId;

public class DatabaseUserControllerNotification {

	public static boolean subscribeControllerNotification (String username, String controllername) {
		Logger.log(Logger.LEVEL_INFO,"DB Subscribe User Controller Notification");
		if (!Cache.Users.map.containsKey(username)) {
			Logger.log(Logger.LEVEL_INFO,"DB Subscribe User Controller Notification - User doesn't exist!");
			return false;
		} else 	if (!Cache.Users.map.containsKey(username)) {
			Logger.log(Logger.LEVEL_INFO,"DB Subscribe User Controller Notification - Controller doesn't exist!");
			return false;
		} else {
			Session session=Cache.factory.openSession();
			Transaction tx=null;
			boolean flag;
			try {
				tx=session.beginTransaction();
				UsercontrollernotificationId uid=new UsercontrollernotificationId(Cache.Users.map.get(username),Cache.Controllers.map.get(controllername));
				session.save(new Usercontrollernotification(uid,new Date()));
				tx.commit();
				
				Logger.log(Logger.LEVEL_INFO,"DB Subscribe User Controller Notification - Execute Callbacks");
				flag=true;
			} catch (HibernateException e) {
				if (tx!=null) tx.rollback();
				Logger.log(Logger.LEVEL_ERROR,"DB Subscribe User Controller Notification - "+e.getMessage());
				flag=false;
			} finally { session.close(); }
			return flag;
		}
	}
	
	public static boolean updateControllerNotification (String username, String controllername, Date time) {
		Logger.log(Logger.LEVEL_INFO,"DB Update User Controller Notification");
		if (!Cache.Users.map.containsKey(username)) {
			Logger.log(Logger.LEVEL_INFO,"DB Update User Controller Notification - User doesn't exist!");
			return false;
		} else 	if (!Cache.Users.map.containsKey(username)) {
			Logger.log(Logger.LEVEL_INFO,"DB Update User Controller Notification - Controller doesn't exist!");
			return false;
		} else {
			Session session=Cache.factory.openSession();
			Transaction tx=null;
			boolean flag;
			try {
				tx=session.beginTransaction();
				UsercontrollernotificationId uid=new UsercontrollernotificationId(Cache.Users.map.get(username),Cache.Controllers.map.get(controllername));
				Usercontrollernotification un=session.get(Usercontrollernotification.class,uid);
				if (un!=null) {
					un.setLastread(time);
					session.update(un);
				}
				tx.commit();
				
				Logger.log(Logger.LEVEL_INFO,"DB Update User Controller Notification - Execute Callbacks");
				flag=true;
			} catch (HibernateException e) {
				if (tx!=null) tx.rollback();
				Logger.log(Logger.LEVEL_ERROR,"DB Update User Controller Notification - "+e.getMessage());
				flag=false;
			} finally { session.close(); }
			return flag;
		}
	}
	
	public static LocalDateTime getControllerNotificationTime (String username, String controllername) {
		Logger.log(Logger.LEVEL_INFO,"DB Get User Controller Notification Time");
		if (!Cache.Users.map.containsKey(username)) {
			Logger.log(Logger.LEVEL_INFO,"DB Get User Controller Notification Time - User doesn't exist!");
			return null;
		} else 	if (!Cache.Users.map.containsKey(username)) {
			Logger.log(Logger.LEVEL_INFO,"DB Get User Controller Notification Time - Controller doesn't exist!");
			return null;
		} else {
			Session session=Cache.factory.openSession();
			LocalDateTime toReturn=null;
			try {
				UsercontrollernotificationId uid=new UsercontrollernotificationId(Cache.Users.map.get(username),Cache.Controllers.map.get(controllername));
				Usercontrollernotification un=session.get(Usercontrollernotification.class,uid);
				if (un!=null) toReturn=Utility.dateToLocalDateTime(un.getLastread());
				else Logger.log(Logger.LEVEL_INFO,"DB Get User Controller Notification Time - Subscription not found");
				
				Logger.log(Logger.LEVEL_INFO,"DB Get User Controller Notification Time - Execute Callbacks");
			} catch (HibernateException e) {
				Logger.log(Logger.LEVEL_ERROR,"DB Get User Controller Notification Time - "+e.getMessage());
			} finally { session.close(); }
			return toReturn;
		}
	}
	
	public static boolean unsubscribeControllerNotification (String username, String controllername) {
		Logger.log(Logger.LEVEL_INFO,"DB Unubscribe User Controller Notification");
		if (!Cache.Users.map.containsKey(username)) {
			Logger.log(Logger.LEVEL_INFO,"DB Unubscribe User Controller Notification - User doesn't exist!");
			return false;
		} else 	if (!Cache.Users.map.containsKey(username)) {
			Logger.log(Logger.LEVEL_INFO,"DB Unsubscribe User Controller Notification - Controller doesn't exist!");
			return false;
		} else {
			Session session=Cache.factory.openSession();
			Transaction tx=null;
			boolean flag;
			try {
				tx=session.beginTransaction();
				UsercontrollernotificationId uid=new UsercontrollernotificationId(Cache.Users.map.get(username),Cache.Controllers.map.get(controllername));
				Usercontrollernotification un=session.get(Usercontrollernotification.class,uid);
				session.delete(un);
				tx.commit();
				
				Logger.log(Logger.LEVEL_INFO,"DB Unsubscribe User Controller Notification - Execute Callbacks");
				flag=true;
			} catch (HibernateException e) {
				if (tx!=null) tx.rollback();
				Logger.log(Logger.LEVEL_ERROR,"DB Unsubscribe User Controller Notification - "+e.getMessage());
				flag=false;
			} finally { session.close(); }
			return flag;
		}
	}
	
	public static ArrayList<Object []> getSubscription (String username) {
		Logger.log(Logger.LEVEL_INFO,"DB Get User Controller Notification Subscription");
		
		ArrayList<Object []> toReturn=new ArrayList<>();
		if (!Cache.Users.map.containsKey(username)) Logger.log(Logger.LEVEL_INFO,"DB Get User Controller Notification Subscription - User doesn't exist!");
		else {
			Session session=Cache.factory.openSession();
			try {
				@SuppressWarnings("unchecked")
				List<Usercontrollernotification> l=session.createQuery("FROM Usercontrollernotification").getResultList();
				for (Usercontrollernotification n : l) if (n.getId().getUser().getUsername().equals(username)) toReturn.add(n.toObj());

			} catch (HibernateException e) {
				Logger.log(Logger.LEVEL_ERROR,"DB Get User Controller Notification Subscription - "+e.getMessage());
			} finally { session.close(); }
		}
		return toReturn;
	}
}
