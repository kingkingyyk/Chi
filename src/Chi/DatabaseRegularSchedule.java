package Chi;

import java.util.LinkedList;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DatabaseRegularSchedule {

	public static interface OnCreateAction {
		public void run (String sn, String an, int day, String rn, boolean ao, int pr, boolean en);
	}
	
	public static interface OnUpdateAction {
		public void run (String oldSN, String sn, String an, int day, String rn, boolean ao, int pr, boolean en);
	}
	
	public static interface OnDeleteAction {
		public void run (String sn);
	}
	
	private static LinkedList<OnCreateAction> OnCreateList=new LinkedList<>();
	private static LinkedList<OnUpdateAction> OnUpdateList=new LinkedList<>();
	private static LinkedList<OnDeleteAction> OnDeleteList=new LinkedList<>();
	
	public static void registerOnCreateAction (OnCreateAction a) {
		if (!OnCreateList.contains(a)) {
			Logger.log("DatabaseRegularSchedule - Registered "+a.toString()+" to OnCreate callback");
			OnCreateList.add(a);
		}
	}
	
	public static void unregisterOnCreateAction (OnCreateAction a) {
		if (OnCreateList.contains(a)) {
			Logger.log("DatabaseRegularSchedule - Unregistered "+a.toString()+" to OnCreate callback");
			OnCreateList.remove(a);
		}
	}
	
	public static void registerOnUpdateAction (OnUpdateAction a) {
		if (!OnUpdateList.contains(a)) {
			Logger.log("DatabaseRegularSchedule - Registered "+a.toString()+" to OnUpdate callback");
			OnUpdateList.add(a);
		}
	}
	
	public static void unregisterOnUpdateAction (OnUpdateAction a) {
		if (OnUpdateList.contains(a)) {
			Logger.log("DatabaseRegularSchedule - Unregistered "+a.toString()+" to OnUpdate callback");
			OnUpdateList.remove(a);
		}
	}
	
	public static void registerOnDeleteAction (OnDeleteAction a) {
		if (!OnDeleteList.contains(a)) {
			Logger.log("DatabaseRegularSchedule - Registered "+a.toString()+" to OnDelete callback");
			OnDeleteList.add(a);
		}
	}
	
	public static void unregisterOnDeleteAction (OnDeleteAction a) {
		if (OnDeleteList.contains(a)) {
			Logger.log("DatabaseRegularSchedule - Unregistered "+a.toString()+" to OnDelete callback");
			OnDeleteList.remove(a);
		}
	}
	
	public static boolean createRegularSchedule (String sn, String an, int day, String rn, boolean ao, int pr, boolean en) {
		Logger.log("DatabaseRegularSchedule - Create");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			Regularschedule r = session.get(Regularschedule.class,sn);
			if (r==null) {
				r=new Regularschedule(sn,session.get(Actuator.class,an),session.get(Dayschedulerule.class,rn),day,ao,pr,en);
				session.save(r);
				tx.commit();
	
				Logger.log("DatabaseRegularSchedule - Create - Execute Callbacks");
				for (OnCreateAction a : OnCreateList) a.run(sn,an,day,rn,ao,pr,en);
				flag = true;
			} else Logger.log("DB Create RegularSchedule - Schedule already exists");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log("DatabaseRegularSchedule - Create - Error" + e.getMessage());
		} finally {session.close();}
		return flag;
	}
	
	public static boolean updateRegularSchedule (String oldSN, String sn, String an, int day, String rn, boolean ao, int pr, boolean en) {
		Logger.log("DatabaseRegularSchedule - Update");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			if (!oldSN.equals(sn)) session.createQuery("update Regularschedule set ScheduleName='"+sn+"' where ScheduleName='"+oldSN+"'").executeUpdate();
			Regularschedule r = session.get(Regularschedule.class,sn);
			if (r!=null) {
				r.setActuator(session.get(Actuator.class,an));
				r.setDaymask(day);
				r.setDayschedulerule(session.get(Dayschedulerule.class,rn));
				r.setActuatoron(ao);
				r.setPriority(pr);
				r.setEnabled(en);
				session.update(r);
				tx.commit();
	
				Logger.log("DatabaseRegularSchedule - Update - Execute Callbacks");
				for (OnUpdateAction a : OnUpdateList) a.run(oldSN,sn,an,day,rn,ao,pr,en);
				flag = true;
			} else Logger.log("DB Update RegularSchedule - Schedule doesn't exist");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log("DatabaseRegularSchedule - Update - Error" + e.getMessage());
		} finally {session.close();}
		return flag;
	}
	
	public static boolean deleteRegularSchedule (String sn) {
		Logger.log("DatabaseRegularSchedule - Delete");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			Regularschedule r = session.get(Regularschedule.class,sn);
			if (r!=null) {
				session.delete(r);
				tx.commit();
	
				Logger.log("DatabaseRegularSchedule - Delete - Execute Callbacks");
				for (OnDeleteAction a : OnDeleteList) a.run(sn);
				flag = true;
			} else Logger.log("DB Delete RegularSchedule - Schedule doesn't exist");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log("DatabaseRegularSchedule - Delete - Error" + e.getMessage());
		} finally {session.close();}
		return flag;
	}

}
