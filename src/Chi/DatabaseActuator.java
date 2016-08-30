package Chi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DatabaseActuator extends DatabaseHSQL {

	public static interface OnCreateAction {
		public void run (String n, String u);
	}
	
	public static interface OnUpdateAction {
		public void run (String oldN, String n, String u);
	}
	
	public static interface OnUpdateStatusAction {
		public void run (String n, String status);
	}
	
	public static interface OnDeleteAction {
		public void run (String n);
	}
	
	private static ArrayList<OnCreateAction> OnCreateList=new ArrayList<>();
	private static ArrayList<OnUpdateAction> OnUpdateList=new ArrayList<>();
	private static ArrayList<OnUpdateStatusAction> OnUpdateStatusList=new ArrayList<>();
	private static ArrayList<OnDeleteAction> OnDeleteList=new ArrayList<>();
	
	public static void registerOnCreateAction (OnCreateAction a) {
		if (!OnCreateList.contains(a)) {
			Logger.log("DatabaseActuator - Registered "+a.toString()+" to OnCreate callback");
			OnCreateList.add(a);
		}
	}
	
	public static void registerOnUpdateAction (OnUpdateAction a) {
		if (!OnUpdateList.contains(a)) {
			Logger.log("DatabaseActuator - Registered "+a.toString()+" to OnUpdate callback");
			OnUpdateList.add(a);
		}
	}
	
	public static void registerOnUpdateStatusAction (OnUpdateStatusAction a) {
		if (!OnUpdateStatusList.contains(a)) {
			Logger.log("DatabaseActuator - Registered "+a.toString()+" to OnUpdateStatus callback");
			OnUpdateStatusList.add(a);
		}
	}
	
	public static void registerOnDeleteAction (OnDeleteAction a) {
		if (!OnDeleteList.contains(a)) {
			Logger.log("DatabaseActuator - Registered "+a.toString()+" to OnDelete callback");
			OnDeleteList.add(a);
		}
	}
	
	public static boolean createActuator (String n, String u) {
		Logger.log("DatabaseActuator - Create");
		if (Cache.Actuators.map.containsKey(n)) {
			Logger.log("DatabaseActuator - Create[Cache] - User already exists!");
			return false;
		} else {
			Session session=Cache.factory.openSession();
			Transaction tx=null;
			boolean flag;
			try {
				tx=session.beginTransaction();
				Actuator ac=new Actuator(n,(Controller)session.get(Controller.class,"DefaultController"),"Pending Update",null,null);
				session.save(ac);
				tx.commit();
				
				Logger.log("DatabaseActuator - Create - Execute Callbacks");
				for (OnCreateAction a : OnCreateList) {
					a.run(n,u);
				}
				flag=true;
			} catch (HibernateException e) {
				if (tx!=null) tx.rollback();
				Logger.log("DatabaseActuator - Create - Error"+e.getMessage());
				flag=false;
			} finally { session.close(); }
			return flag;
		}
	}
	
	public static boolean updateActuator (String oldN, String n, String u) {
		Logger.log("DB Update Actuator : "+Config.getConfig(Config.DATABASE_UPDATE_ACTUATOR_SQL_FILE_KEY));
		if (!Cache.Actuators.map.containsKey(oldN)) {
			Logger.log("DB Update Actuator - Actuator doesn't exists!");
			return false;
		} else {
			Actuator ac=Cache.Actuators.map.get(oldN);
			if (ac.getName().equals(n) && ac.getStatus().equals(u)) {
				Logger.log("DB Update Actuator - Information is the same!");
				return true;
			} else {
				try {
					Connection c = getConnection();
					if (c!=null) {
						Logger.log("DB Update Actuator - Database connection OK!");
						String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_UPDATE_ACTUATOR_SQL_FILE_KEY));
						PreparedStatement ps=c.prepareStatement(sql[0]);
						Logger.log("DB Update Actuator - Execute "+ps.toString());
						ps.execute();
						
						ps=c.prepareStatement(sql[1]);
						ps.setString(1, n);
						ps.setString(2, u);
						ps.setString(3, oldN);
						Logger.log("DB Update Actuator - Execute "+ps.toString());
						ps.execute();
						
						ps=c.prepareStatement(sql[2]);
						Logger.log("DB Update Actuator - Execute "+ps.toString());
						ps.execute();
						
						for (OnUpdateAction a : OnUpdateList) {
							a.run(oldN,n,u);
						}
					}
					c.close();
					return true;
				} catch (Exception e) {
					Logger.log("DB Update Actuator - Error - "+e.getMessage());
					e.printStackTrace();
				}
				return false;
			}
		}
	}
	
	public static boolean updateActuatorStatus (String n, String st) {
		Logger.log("DB Update Actuator Status : "+Config.getConfig(Config.DATABASE_UPDATE_ACTUATOR_STATUS_SQL_FILE_KEY));
		if (!Cache.Actuators.map.containsKey(n)) {
			Logger.log("DB Update Actuator Status - Actuator doesn't exists!");
			return false;
		} else {
			Actuator act=Cache.Actuators.map.get(n);
			if (act.getName().equals(n) && act.getStatus().equals(st)) {
				Logger.log("DB Update Actuator Status - Information is the same!");
				return true;
			} else {
				try {
					Connection c = getConnection();
					if (c!=null) {
						Logger.log("DB Update Actuator Status - Database connection OK!");
						String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_UPDATE_ACTUATOR_STATUS_SQL_FILE_KEY));
						PreparedStatement ps=c.prepareStatement(sql[0]);
						Logger.log("DB Update Actuator Status - Execute "+ps.toString());
						ps.execute();
						
						ps=c.prepareStatement(sql[1]);
						ps.setString(1, st);
						ps.setString(2, n);
						Logger.log("DB Update Actuator Status - Execute "+ps.toString());
						ps.execute();
						
						ps=c.prepareStatement(sql[2]);
						Logger.log("DB Update Actuator Status - Execute "+ps.toString());
						ps.execute();
						
						for (OnUpdateStatusAction a : OnUpdateStatusList) {
							a.run(n,st);
						}
					}
					c.close();
					return true;
				} catch (Exception e) {
					Logger.log("DB Update Actuator Status - Error - "+e.getMessage());
					e.printStackTrace();
				}
				return false;
			}
		}
	}
	
	public static boolean deleteActuator (String n) {
		Logger.log("DB Delete Actuator : "+Config.getConfig(Config.DATABASE_DELETE_ACTUATOR_SQL_FILE_KEY));
		if (!Cache.Actuators.map.containsKey(n)) {
			Logger.log("DB Update Actuator - Actuator doesn't exists!");
			return false;
		} else {
			try {
				Connection c = getConnection();
				if (c!=null) {
					Logger.log("DB Delete Actuator - Database connection OK!");
					String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_DELETE_ACTUATOR_SQL_FILE_KEY));
					PreparedStatement ps=c.prepareStatement(sql[0]);
					Logger.log("DB Update Actuator - Execute "+ps.toString());
					ps.execute();
					
					ps=c.prepareStatement(sql[1]);
					ps.setString(1, n);
					Logger.log("DB Delete Actuator - Execute - "+ps.toString());
					ps.execute();
					
					ps=c.prepareStatement(sql[2]);
					Logger.log("DB Delete Actuator - Execute "+ps.toString());
					ps.execute();
				}
				c.close();
				return true;
			} catch (Exception e) {
				Logger.log("DB Delete Actuator - Error - "+e.getMessage());
				e.printStackTrace();
			}
			return false;
		}
	}

}
