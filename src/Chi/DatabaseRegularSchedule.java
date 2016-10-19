package Chi;

import java.util.LinkedList;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DatabaseRegularSchedule {

	public static interface OnCreateAction {
		public void run (String sn, String an, int day, String rn, String onstartaction, String onendaction, boolean lockmanual, int pr, boolean en);
	}
	
	public static interface OnUpdateAction {
		public void run (String oldSN, String sn, String an, int day, String rn, String onstartaction, String onendaction, boolean lockmanual,  int pr, boolean en);
	}
	
	public static interface OnDeleteAction {
		public void run (String sn);
	}
	
	private static LinkedList<OnCreateAction> OnCreateList=new LinkedList<>();
	private static LinkedList<OnUpdateAction> OnUpdateList=new LinkedList<>();
	private static LinkedList<OnDeleteAction> OnDeleteList=new LinkedList<>();
	
	public static void registerOnCreateAction (OnCreateAction a) {
		if (!OnCreateList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseRegularSchedule - Registered "+a.toString()+" to OnCreate callback");
			OnCreateList.add(a);
		}
	}
	
	public static void unregisterOnCreateAction (OnCreateAction a) {
		if (OnCreateList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseRegularSchedule - Unregistered "+a.toString()+" to OnCreate callback");
			OnCreateList.remove(a);
		}
	}
	
	public static void registerOnUpdateAction (OnUpdateAction a) {
		if (!OnUpdateList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseRegularSchedule - Registered "+a.toString()+" to OnUpdate callback");
			OnUpdateList.add(a);
		}
	}
	
	public static void unregisterOnUpdateAction (OnUpdateAction a) {
		if (OnUpdateList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseRegularSchedule - Unregistered "+a.toString()+" to OnUpdate callback");
			OnUpdateList.remove(a);
		}
	}
	
	public static void registerOnDeleteAction (OnDeleteAction a) {
		if (!OnDeleteList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseRegularSchedule - Registered "+a.toString()+" to OnDelete callback");
			OnDeleteList.add(a);
		}
	}
	
	public static void unregisterOnDeleteAction (OnDeleteAction a) {
		if (OnDeleteList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseRegularSchedule - Unregistered "+a.toString()+" to OnDelete callback");
			OnDeleteList.remove(a);
		}
	}
	
	public static boolean createRegularSchedule (String sn, String an, int day, String rn, String startAct, String endAct, boolean lock, int pr, boolean en) {
		Logger.log(Logger.LEVEL_INFO,"DatabaseRegularSchedule - Create");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			Regularschedule r = session.get(Regularschedule.class,sn);
			if (r==null) {
				r=new Regularschedule(sn,session.get(Actuator.class,an),session.get(Dayschedulerule.class,rn),day,startAct,endAct,lock,pr,en);
				session.save(r);
				tx.commit();
				Cache.RegularSchedules.map.put(sn,r);
	
				Logger.log(Logger.LEVEL_INFO,"DatabaseRegularSchedule - Create - Execute Callbacks");
				for (OnCreateAction a : OnCreateList) a.run(sn,an,day,rn,startAct,endAct,lock,pr,en);
				flag = true;
			} else Logger.log(Logger.LEVEL_WARNING,"DB Create RegularSchedule - Schedule already exists");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log(Logger.LEVEL_ERROR,"DatabaseRegularSchedule - Create - " + e.getMessage());
		} finally {session.close();}
		return flag;
	}
	
	public static boolean updateRegularSchedule (String oldSN, String sn, String an, int day, String rn, String startAct, String endAct, boolean lock, int pr, boolean en) {
		Logger.log(Logger.LEVEL_INFO,"DatabaseRegularSchedule - Update");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			if (!oldSN.equals(sn)) {
				session.createQuery("update Regularschedule set ScheduleName='"+sn+"' where ScheduleName='"+oldSN+"'").executeUpdate();
				Cache.RegularSchedules.map.remove(oldSN);
			}
			Regularschedule r = session.get(Regularschedule.class,sn);
			if (r!=null) {
				r.setActuator(session.get(Actuator.class,an));
				r.setDaymask(day);
				r.setDayschedulerule(session.get(Dayschedulerule.class,rn));
				r.setOnstartaction(startAct);
				r.setOnendaction(endAct);
				r.setLockmanual(lock);
				r.setPriority(pr);
				r.setEnabled(en);
				session.update(r);
				tx.commit();
				Cache.RegularSchedules.map.put(sn,r);
	
				Logger.log(Logger.LEVEL_INFO,"DatabaseRegularSchedule - Update - Execute Callbacks");
				for (OnUpdateAction a : OnUpdateList) a.run(oldSN,sn,an,day,rn,startAct,endAct,lock,pr,en);
				flag = true;
			} else Logger.log(Logger.LEVEL_WARNING,"DB Update RegularSchedule - Schedule doesn't exist");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log(Logger.LEVEL_ERROR,"DatabaseRegularSchedule - Update - " + e.getMessage());
		} finally {session.close();}
		return flag;
	}
	
	public static boolean deleteRegularSchedule (String sn) {
		Logger.log(Logger.LEVEL_INFO,"DatabaseRegularSchedule - Delete");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			Regularschedule r = session.get(Regularschedule.class,sn);
			if (r!=null) {
				session.delete(r);
				tx.commit();
				Cache.RegularSchedules.map.remove(sn);
	
				Logger.log(Logger.LEVEL_INFO,"DatabaseRegularSchedule - Delete - Execute Callbacks");
				for (OnDeleteAction a : OnDeleteList) a.run(sn);
				flag = true;
			} else Logger.log(Logger.LEVEL_WARNING,"DB Delete RegularSchedule - Schedule doesn't exist");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log(Logger.LEVEL_ERROR,"DatabaseRegularSchedule - Delete - " + e.getMessage());
		} finally {session.close();}
		return flag;
	}

}
