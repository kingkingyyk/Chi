package Database;

import java.util.LinkedList;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import Chi.Logger;
import Entity.Dayschedulerule;
import Entity.Regularschedule;
import Entity.Specialschedule;

public class DatabaseDayScheduleRule {

	public static interface OnCreateAction {
		public void run (String n, int sh, int sm, int eh, int em);
	}
	
	public static interface OnUpdateAction {
		public void run (String oldN, String n, int sh, int sm, int eh, int em);
	}
	
	public static interface OnDeleteAction {
		public void run (String n);
	}
	
	private static LinkedList<OnCreateAction> OnCreateList=new LinkedList<>();
	private static LinkedList<OnUpdateAction> OnUpdateList=new LinkedList<>();
	private static LinkedList<OnDeleteAction> OnDeleteList=new LinkedList<>();
	
	public static void registerOnCreateAction (OnCreateAction a) {
		if (!OnCreateList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseDayScheduleRule - Registered "+a.toString()+" to OnCreate callback");
			OnCreateList.add(a);
		}
	}
	
	public static void unregisterOnCreateAction (OnCreateAction a) {
		if (OnCreateList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseDayScheduleRule - Unregistered "+a.toString()+" to OnCreate callback");
			OnCreateList.remove(a);
		}
	}
	
	public static void registerOnUpdateAction (OnUpdateAction a) {
		if (!OnUpdateList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseDayScheduleRule - Registered "+a.toString()+" to OnUpdate callback");
			OnUpdateList.add(a);
		}
	}
	
	public static void unregisterOnUpdateAction (OnUpdateAction a) {
		if (OnUpdateList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseDayScheduleRule - Unregistered "+a.toString()+" to OnUpdate callback");
			OnUpdateList.remove(a);
		}
	}
	
	public static void registerOnDeleteAction (OnDeleteAction a) {
		if (!OnDeleteList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseDayScheduleRule - Registered "+a.toString()+" to OnDelete callback");
			OnDeleteList.add(a);
		}
	}
	
	public static void unregisterOnDeleteAction (OnDeleteAction a) {
		if (OnDeleteList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseDayScheduleRule - Unregistered "+a.toString()+" to OnDelete callback");
			OnDeleteList.remove(a);
		}
	}
	
	public static boolean createDayScheduleRule (String n, int sh, int sm, int eh, int em) {
		Logger.log(Logger.LEVEL_INFO,"DatabaseDayScheduleRule - Create");
		Session session = Cache.factory.openSession();
		Transaction tx = null;
		boolean flag=false;
		try {
			tx = session.beginTransaction();
			Dayschedulerule r = session.get(Dayschedulerule.class,n);
			if (r==null) {
				r=new Dayschedulerule(n); r.setStarthour(sh); r.setStartminute(sm); r.setEndhour(eh); r.setEndminute(em);
				session.save(r);
				tx.commit();
				Cache.DayScheduleRules.map.put(n,r);
	
				Logger.log(Logger.LEVEL_INFO,"DatabaseDayScheduleRule - Create - Execute Callbacks");
				for (OnCreateAction a : OnCreateList) a.run(n,sh,sm,eh,em);
				flag = true;
			} else Logger.log(Logger.LEVEL_WARNING,"DB Create DayScheduleRule - Rule already exists");
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			Logger.log(Logger.LEVEL_ERROR,"DatabaseDayScheduleRule - Create - " + e.getMessage());
		} finally {session.close();}
		return flag;
	}
	
	public static boolean updateDayScheduleRule  (String oldN, String n, int sh, int sm, int eh, int em) {
		Logger.log(Logger.LEVEL_INFO,"DB Update DayScheduleRule");
		Session session=Cache.factory.openSession();
		Transaction tx=null;
		boolean flag=false;
		try {
			tx=session.beginTransaction();
			if (!oldN.equals(n)) {
				session.createQuery("Update Dayschedulerule set RuleName='"+n+"' where RuleName='"+oldN+"'").executeUpdate();
				Cache.DayScheduleRules.map.remove(oldN);
			}
			Dayschedulerule r=session.get(Dayschedulerule.class,n);
			if (r!=null) {
				r.setStarthour(sh); r.setStartminute(sm); r.setEndhour(eh); r.setEndminute(em);
				session.update(r);
				tx.commit();
				Cache.DayScheduleRules.map.put(n,r);
				for (Regularschedule rs : Cache.RegularSchedules.map.values())
					if (rs.getDayschedulerule().getRulename().equals(oldN))
						rs.setDayschedulerule(r);
				for (Specialschedule ss : Cache.SpecialSchedules.map.values())
					if (ss.getDayschedulerule().getRulename().equals(oldN))
						ss.setDayschedulerule(r);
				
				Logger.log(Logger.LEVEL_INFO,"DB Update DayScheduleRule - Execute Callbacks");
				for (OnUpdateAction a : OnUpdateList) a.run(oldN,n,sh,sm,eh,em);
				flag=true;
			} else Logger.log(Logger.LEVEL_WARNING,"DB Update DayScheduleRule - Rule doesn't exist");
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			Logger.log(Logger.LEVEL_ERROR,"DB Update DayScheduleRule - "+e.getMessage());
			flag=false;
		} finally { session.close(); }
		return flag;
	}
	
	public static boolean deleteDayScheduleRule (String n) {
		Logger.log(Logger.LEVEL_INFO,"DB Delete DayScheduleRule");
		Session session=Cache.factory.openSession();
		Transaction tx=null;
		boolean flag=false;
		try {
			tx=session.beginTransaction();
			Dayschedulerule r=session.get(Dayschedulerule.class,n);
			if (r!=null) {
				for (Regularschedule rs : Cache.RegularSchedules.map.values()) if (rs.getDayschedulerule().getRulename().equals(n)) DatabaseRegularSchedule.deleteRegularSchedule(rs.getSchedulename());
				for (Specialschedule ss : Cache.SpecialSchedules.map.values()) if (ss.getDayschedulerule().getRulename().equals(n)) DatabaseSpecialSchedule.deleteSpecialSchedule(ss.getSchedulename());
				
				session.delete(r);
				tx.commit();
				Cache.DayScheduleRules.map.remove(n);
				
				Logger.log(Logger.LEVEL_INFO,"DB Delete DayScheduleRule - Execute Callbacks");
				for (OnDeleteAction a : OnDeleteList) a.run(n);
				flag=true;
			} else Logger.log(Logger.LEVEL_WARNING,"DB Delete DayScheduleRule - Rule doesn't exist");
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			Logger.log(Logger.LEVEL_ERROR,"DB Delete DayScheduleRule - "+e.getMessage());
			flag=false;
		} finally { session.close(); }
		return flag;
	}

}
