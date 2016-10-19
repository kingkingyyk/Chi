package Chi;

import java.util.LinkedList;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DatabaseSpecialSchedule {

	public static interface OnCreateAction {
		public void run (String sn, String an, int year, int month, int day, String rn, String startAct, String endAct, boolean lock, int pr, boolean en);
	}
	
	public static interface OnUpdateAction {
		public void run (String oldSN, String sn, String an, int year, int month, int day, String rn, String startAct, String endAct, boolean lock, int pr, boolean en);
	}
	
	public static interface OnDeleteAction {
		public void run (String sn);
	}
	
	private static LinkedList<OnCreateAction> OnCreateList=new LinkedList<>();
	private static LinkedList<OnUpdateAction> OnUpdateList=new LinkedList<>();
	private static LinkedList<OnDeleteAction> OnDeleteList=new LinkedList<>();
	
	public static void registerOnCreateAction (OnCreateAction a) {
		if (!OnCreateList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseSpecialSchedule - Registered "+a.toString()+" to OnCreate callback");
			OnCreateList.add(a);
		}
	}
	
	public static void unregisterOnCreateAction (OnCreateAction a) {
		if (OnCreateList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseSpecialSchedule - Unregistered "+a.toString()+" to OnCreate callback");
			OnCreateList.remove(a);
		}
	}
	
	public static void registerOnUpdateAction (OnUpdateAction a) {
		if (!OnUpdateList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseSpecialSchedule - Registered "+a.toString()+" to OnUpdate callback");
			OnUpdateList.add(a);
		}
	}
	
	public static void unregisterOnUpdateAction (OnUpdateAction a) {
		if (OnUpdateList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseSpecialSchedule - Unregistered "+a.toString()+" to OnUpdate callback");
			OnUpdateList.remove(a);
		}
	}
	
	public static void registerOnDeleteAction (OnDeleteAction a) {
		if (!OnDeleteList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseSpecialSchedule - Registered "+a.toString()+" to OnDelete callback");
			OnDeleteList.add(a);
		}
	}
	
	public static void unregisterOnDeleteAction (OnDeleteAction a) {
		if (OnDeleteList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseSpecialSchedule - Unregistered "+a.toString()+" to OnDelete callback");
			OnDeleteList.remove(a);
		}
	}
	
	public static boolean createSpecialSchedule (String sn, String an, int year, int month, int day, String rn, String startAct, String endAct, boolean lock, int pr, boolean en) {
		Logger.log(Logger.LEVEL_INFO,"DatabaseSpecialSchedule - Create");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			Specialschedule ss = session.get(Specialschedule.class,sn);
			if (ss==null) {
				ss=new Specialschedule(sn,session.get(Actuator.class,an),session.get(Dayschedulerule.class,rn),year,month,day,startAct,endAct,lock,pr,en);
				session.save(ss);
				tx.commit();
				Cache.SpecialSchedules.map.put(sn, ss);
	
				Logger.log(Logger.LEVEL_INFO,"DatabaseSpecialSchedule - Create - Execute Callbacks");
				for (OnCreateAction a : OnCreateList) a.run(sn,an,year,month,day,rn,startAct,endAct,lock,pr,en);
				flag = true;
			} else Logger.log(Logger.LEVEL_WARNING,"DB Create SpecialSchedule - Schedule already exists");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log(Logger.LEVEL_ERROR,"DatabaseSpecialSchedule - Create - " + e.getMessage());
		} finally {session.close();}
		return flag;
	}
	
	public static boolean updateSpecialSchedule (String oldSN, String sn, String an, int year, int month, int day, String rn, String startAct, String endAct, boolean lock, int pr, boolean en) {
		Logger.log(Logger.LEVEL_INFO,"DatabaseSpecialSchedule - Update");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			if (!oldSN.equals(sn)) {
				session.createQuery("update Specialschedule set ScheduleName='"+sn+"' where ScheduleName='"+oldSN+"'").executeUpdate();
				Cache.SpecialSchedules.map.remove(oldSN);
			}
			Specialschedule ss = session.get(Specialschedule.class,sn);
			if (ss!=null) {
				ss.setActuator(session.get(Actuator.class,an));
				ss.setYear(year);
				ss.setMonth(month);
				ss.setDay(day);
				ss.setDayschedulerule(session.get(Dayschedulerule.class,rn));
				ss.setOnstartaction(startAct);
				ss.setOnendaction(endAct);
				ss.setLockmanual(lock);
				ss.setPriority(pr);
				ss.setEnabled(en);
				session.update(ss);
				tx.commit();
				Cache.SpecialSchedules.map.put(sn, ss);
	
				Logger.log(Logger.LEVEL_INFO,"DatabaseSpecialSchedule - Update - Execute Callbacks");
				for (OnUpdateAction a : OnUpdateList) a.run(oldSN,sn,an,year,month,day,rn,startAct,endAct,lock,pr,en);
				flag = true;
			} else Logger.log(Logger.LEVEL_WARNING,"DB Update SpecialSchedule - Schedule doesn't exist");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log(Logger.LEVEL_ERROR,"DatabaseSpecialSchedule - Update - Error" + e.getMessage());
		} finally {session.close();}
		return flag;
	}
	
	public static boolean deleteSpecialSchedule (String sn) {
		Logger.log(Logger.LEVEL_INFO,"DatabaseSpecialSchedule - Delete");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			Specialschedule ss = session.get(Specialschedule.class,sn);
			if (ss!=null) {
				session.delete(ss);
				tx.commit();
				Cache.SpecialSchedules.map.remove(sn);
	
				Logger.log(Logger.LEVEL_INFO,"DatabaseSpecialSchedule - Delete - Execute Callbacks");
				for (OnDeleteAction a : OnDeleteList) a.run(sn);
				flag = true;
			} else Logger.log(Logger.LEVEL_WARNING,"DB Delete SpecialSchedule - Schedule doesn't exist");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log(Logger.LEVEL_ERROR,"DatabaseSpecialSchedule - Delete - " + e.getMessage());
		} finally {session.close();}
		return flag;
	}

}
