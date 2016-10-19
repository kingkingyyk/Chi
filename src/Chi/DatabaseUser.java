package Chi;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DatabaseUser {

	public static interface OnCreateAction {
		public void run (String user, int lvl, String status);
	}
	
	public static interface OnUpdateAction {
		public void run (String user, int lvl, String status);
	}
	
	public static interface OnDeleteAction {
		public void run (String user);
	}
	
	private static LinkedList<OnCreateAction> OnCreateList=new LinkedList<>();
	private static LinkedList<OnUpdateAction> OnUpdateList=new LinkedList<>();
	private static LinkedList<OnDeleteAction> OnDeleteList=new LinkedList<>();
	
	public static void registerOnCreateAction (OnCreateAction a) {
		if (!OnCreateList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseUser - Registered "+a.toString()+" to OnCreate callback");
			OnCreateList.add(a);
		}
	}
	
	public static void registerOnUpdateAction (OnUpdateAction a) {
		if (!OnUpdateList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseUser - Registered "+a.toString()+" to OnUpdate callback");
			OnUpdateList.add(a);
		}
	}
	
	public static void registerOnDeleteAction (OnDeleteAction a) {
		if (!OnDeleteList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseUser - Registered "+a.toString()+" to OnDelete callback");
			OnDeleteList.add(a);
		}
	}

	public static boolean createUserCredential (String user, String pw, int lvl, String status) {
		Logger.log(Logger.LEVEL_INFO,"DB Create User");
		if (Cache.Users.map.containsKey(user)) {
			Logger.log(Logger.LEVEL_INFO,"DB Create User[Cache] - User already exists!");
			return false;
		} else {
			Session session=Cache.factory.openSession();
			Transaction tx=null;
			boolean flag;
			try {
				tx=session.beginTransaction();
				User u=new User(user,pw,lvl,status,Timestamp.valueOf(LocalDateTime.now()));
				session.save(u);
				tx.commit();
				Cache.Users.map.put(user,u);
				
				Logger.log(Logger.LEVEL_INFO,"DB Create User - Execute Callbacks");
				for (OnCreateAction a : OnCreateList) {
					a.run(user, lvl, status);
				}
				flag=true;
			} catch (HibernateException e) {
				if (tx!=null) tx.rollback();
				Logger.log(Logger.LEVEL_ERROR,"DB Create User - "+e.getMessage());
				flag=false;
			} finally { session.close(); }
			return flag;
		}
	}
	
	public static boolean updateUserCredentialPassword (String oldN, String user, String pw, int lvl, String status) {
		Logger.log(Logger.LEVEL_INFO,"DB Update User");
		if (!Cache.Users.map.containsKey(oldN)) {
			Logger.log(Logger.LEVEL_INFO,"DB Update User[Cache] - User doesn't exist!");
			return false;
		} else {
			Session session=Cache.factory.openSession();
			Transaction tx=null;
			boolean flag;
			try {
				tx=session.beginTransaction();
				if (!oldN.equals(user)) {
					session.createQuery("update User set Username='"+user+"' where Username='"+oldN+"'").executeUpdate();
					Cache.Users.map.remove(oldN);
				}
				User u=(User)session.get(User.class, user);
				u.setPassword(pw); u.setLevel(lvl); u.setStatus(status);
				session.update(u);
				tx.commit();
				Cache.Users.map.put(user,u);
				Logger.log(Logger.LEVEL_INFO,"DB Update User - Execute Callbacks");
				for (OnUpdateAction a : OnUpdateList) {
					a.run(user, lvl, status);
				}
				flag=true;
			}catch (HibernateException e) {
				if (tx!=null) tx.rollback();
				Logger.log(Logger.LEVEL_ERROR,"DB Create User - "+e.getMessage());
				flag=false;
			} finally { session.close(); }
			return flag;
		}
	}
	
	public static boolean updateUserCredentialNoPassword (String oldN, String user, int lvl, String status) {
		Logger.log(Logger.LEVEL_INFO,"DB Update User/2");
		if (!Cache.Users.map.containsKey(oldN)) {
			Logger.log(Logger.LEVEL_INFO,"DB Update User/2[Cache] - User doesn't exists!");
			return false;
		} else {
			Session session=Cache.factory.openSession();
			Transaction tx=null;
			boolean flag;
			try {
				tx=session.beginTransaction();
				if (!oldN.equals(user)) {
					session.createQuery("update User set Username='"+user+"' where Username='"+oldN+"'").executeUpdate();
					Cache.Users.map.remove(oldN);
				}
				User u=(User)session.get(User.class,user);
				u.setLevel(lvl); u.setStatus(status);
				session.update(u);
				tx.commit();
				Cache.Users.map.put(user,u);
				Logger.log(Logger.LEVEL_INFO,"DB Update User/2 - Execute Callbacks");
				for (OnUpdateAction a : OnUpdateList) {
					a.run(user, lvl, status);
				}
				flag=true;
			}catch (HibernateException e) {
				if (tx!=null) tx.rollback();
				Logger.log(Logger.LEVEL_ERROR,"DB Create User/2 - "+e.getMessage());
				flag=false;
			} finally { session.close(); }
			return flag;
		}
	}
	
	public static boolean deleteUser (String user) {
		Logger.log(Logger.LEVEL_INFO,"DB Delete User");
		if (!Cache.Users.map.containsKey(user)) {
			Logger.log(Logger.LEVEL_INFO,"DB Delete User[Cache] - User doesn't exists!");
			return false;
		} else {
			Session session=Cache.factory.openSession();
			Transaction tx=null;
			boolean flag;
			try {
				tx=session.beginTransaction();
				User u=(User)session.get(User.class, user);
				session.delete(u);
				tx.commit();
				Cache.Users.map.remove(user);
				Logger.log(Logger.LEVEL_INFO,"DB Delete User - Execute Callbacks");
				for (OnDeleteAction a : OnDeleteList) {
					a.run(user);
				}
				flag=true;
			}catch (HibernateException e) {
				if (tx!=null) tx.rollback();
				Logger.log(Logger.LEVEL_ERROR,"DB Create User/2 - "+e.getMessage());
				flag=false;
			} finally { session.close(); }
			return flag;
		}
	}

}
