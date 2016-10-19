package Chi;

import java.util.LinkedList;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DatabaseSite {

	public static interface OnCreateAction {
		public void run (String name, String url);
	}
	
	public static interface OnUpdateAction {
		public void run (String oldName, String name, String url);
	}
	
	public static interface OnDeleteAction {
		public void run (String name);
	}
	
	private static LinkedList<OnCreateAction> OnCreateList=new LinkedList<>();
	private static LinkedList<OnUpdateAction> OnUpdateList=new LinkedList<>();
	private static LinkedList<OnDeleteAction> OnDeleteList=new LinkedList<>();
	
	public static void registerOnCreateAction (OnCreateAction a) {
		if (!OnCreateList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseSite - Registered "+a.toString()+" to OnCreate callback");
			OnCreateList.add(a);
		}
	}
	
	public static void registerOnUpdateAction (OnUpdateAction a) {
		if (!OnUpdateList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseSite - Registered "+a.toString()+" to OnUpdate callback");
			OnUpdateList.add(a);
		}
	}
	
	public static void registerOnDeleteAction (OnDeleteAction a) {
		if (!OnDeleteList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseSite - Registered "+a.toString()+" to OnDelete callback");
			OnDeleteList.add(a);
		}
	}

	public static boolean createSite (String n, String u) {
		Logger.log(Logger.LEVEL_INFO,"DatabaseSite - Create");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			Site s = session.get(Site.class,n);
			if (s==null) {
				s=new Site(n,u,null);
				session.save(s);
				tx.commit();
				Cache.Sites.map.put(n,s);
	
				Logger.log(Logger.LEVEL_INFO,"DatabaseSite - Create - Execute Callbacks");
				for (OnCreateAction a : OnCreateList) a.run(n,u);
				flag = true;
			} else Logger.log(Logger.LEVEL_WARNING,"DB Create Site - Site already exists");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log(Logger.LEVEL_ERROR,"DatabaseSite - Create - " + e.getMessage());
		} finally {session.close();}
		return flag;
	}
	
	public static boolean updateSite (String oldN, String n, String u) {
		Logger.log(Logger.LEVEL_INFO,"DatabaseSite - Update");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			if (!oldN.equals(n)) {
				session.createQuery("update Site set SiteName='"+n+"' where SiteName='"+oldN+"'").executeUpdate();
				Cache.Sites.map.remove(oldN);
			}
			Site s = session.get(Site.class,n);
			if (s!=null) {
				s.setSitemapurl(u);
				session.update(s);
				tx.commit();
				Cache.Sites.map.put(n,s);
				for (Controller c : Cache.Controllers.map.values())
					if (c.getSite().getSitename().equals(oldN))
						c.setSite(s);

				Logger.log(Logger.LEVEL_INFO,"DatabaseSite - Update - Execute Callbacks");
				for (OnUpdateAction a : OnUpdateList) a.run(oldN,n,u);
				flag = true;
			} else Logger.log(Logger.LEVEL_WARNING,"DB Update Site - Site doesn't exist");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log(Logger.LEVEL_ERROR,"DatabaseSite - Update - " + e.getMessage());
		} finally {session.close();}
		return flag;
	}
	
	public static boolean deleteSite (String n) {
		Logger.log(Logger.LEVEL_INFO,"DatabaseSite - Delete");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			Site s = session.get(Site.class,n);
			if (s!=null) {
				session.delete(s);
				tx.commit();
				Cache.Sites.map.remove(n);
	
				Logger.log(Logger.LEVEL_INFO,"DatabaseSite - Delete - Execute Callbacks");
				for (OnDeleteAction a : OnDeleteList) a.run(n);
				flag = true;
			} else Logger.log(Logger.LEVEL_WARNING,"DB Update Site - Site doesn't exist");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log(Logger.LEVEL_ERROR,"DatabaseSite - Delete - " + e.getMessage());
		} finally {session.close();}
		return flag;
	}

}
