package Chi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class DatabaseSite extends DatabaseHSQL {

	public static interface OnCreateAction {
		public void run (String name, String url);
	}
	
	public static interface OnUpdateAction {
		public void run (String oldName, String name, String url);
	}
	
	public static interface OnDeleteAction {
		public void run (String name);
	}
	
	private static ArrayList<OnCreateAction> OnCreateList=new ArrayList<>();
	private static ArrayList<OnUpdateAction> OnUpdateList=new ArrayList<>();
	private static ArrayList<OnDeleteAction> OnDeleteList=new ArrayList<>();
	
	public static void registerOnCreateAction (OnCreateAction a) {
		if (!OnCreateList.contains(a)) {
			Logger.log("DatabaseSite - Registered "+a.toString()+" to OnCreate callback");
			OnCreateList.add(a);
		}
	}
	
	public static void registerOnUpdateAction (OnUpdateAction a) {
		if (!OnUpdateList.contains(a)) {
			Logger.log("DatabaseSite - Registered "+a.toString()+" to OnUpdate callback");
			OnUpdateList.add(a);
		}
	}
	
	public static void registerOnDeleteAction (OnDeleteAction a) {
		if (!OnDeleteList.contains(a)) {
			Logger.log("DatabaseSite - Registered "+a.toString()+" to OnDelete callback");
			OnDeleteList.add(a);
		}
	}

	public static boolean createSite (String n, String u) {
		Logger.log("DB Create Site : "+Config.getConfig(Config.DATABASE_CREATE_SITE_SQL_FILE_KEY));
		if (Cache.Sites.map.containsKey(n)) {
			Logger.log("DB Create Site[Cache] - Site already exists!");
			return false;
		} else {
			try {
				Connection c = getConnection();
				if (c!=null) {
					Logger.log("DB Create Site - Database connection OK!");
					String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_CREATE_SITE_SQL_FILE_KEY));
					PreparedStatement ps=c.prepareStatement(sql[0]);
					Logger.log("DB Create Site - Execute "+ps.toString());
					ps.execute();
					
					ps=c.prepareStatement(sql[1]);
					ps.setString(1, n);
					ps.setString(2, u);
					Logger.log("DB Create Site - Execute "+ps.toString());
					ps.execute();
					
					ps=c.prepareStatement(sql[2]);
					Logger.log("DB Create Site - Execute "+ps.toString());
					ps.execute();
					
					Logger.log("DB Create Site - Execute Callbacks");
					for (OnCreateAction a : OnCreateList) {
						a.run(n,u);
					}
				}
				c.close();
				return true;
			} catch (Exception e) {
				Logger.log("DB Create Site - Error - "+e.getMessage());
				e.printStackTrace();
			}
			return false;
		}
	}
	
	public static boolean updateSite (String oldN, String n, String u) {
		Logger.log("DB Update Site : "+Config.getConfig(Config.DATABASE_UPDATE_SITE_SQL_FILE_KEY));
		if (!Cache.Sites.map.containsKey(oldN)) {
			Logger.log("DB Create Site[Cache] - Site doesn't exist!");
			return false;
		} else {
				try {
					Connection c = getConnection();
					if (c!=null) {
						Logger.log("DB Update Site - Database connection OK!");
						String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_UPDATE_SITE_SQL_FILE_KEY));
						PreparedStatement ps=c.prepareStatement(sql[0]);
						Logger.log("DB Update Site - Execute "+ps.toString());
						ps.execute();
						
						ps=c.prepareStatement(sql[1]);
						ps.setString(1, n);
						ps.setString(2, u);
						ps.setString(3, oldN);
						Logger.log("DB Update Site - Execute "+ps.toString());
						ps.execute();
						
						ps=c.prepareStatement(sql[2]);
						Logger.log("DB Update Site - Execute "+ps.toString());
						ps.execute();
						
						Logger.log("DB Update Site - Execute Callbacks");
						for (OnUpdateAction a : OnUpdateList) {
							a.run(oldN,n,u);
						}
					}
					c.close();
					return true;
				} catch (Exception e) {
					Logger.log("DB Update Site - Error - "+e.getMessage());
					e.printStackTrace();
				}
				return false;
			}
	}
	
	public static boolean deleteSite (String n) {
		Logger.log("DB Delete Site : "+Config.getConfig(Config.DATABASE_DELETE_SITE_SQL_FILE_KEY));
		if (!Cache.Sites.map.containsKey(n)) {
			Logger.log("DB Delete Site[Cache] - Site doesn't exist!");
			return false;
		} else {
			try {
				Connection c = getConnection();
				if (c!=null) {
					Logger.log("DB Delete Site - Database connection OK!");
					String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_DELETE_SITE_SQL_FILE_KEY));
					PreparedStatement ps=c.prepareStatement(sql[0]);
					Logger.log("DB Delete Site - Execute "+ps.toString());
					ps.execute();
					
					ps=c.prepareStatement(sql[1]);
					ps.setString(1, n);
					Logger.log("DB Delete Site - Execute - "+ps.toString());
					ps.execute();
					
					ps=c.prepareStatement(sql[2]);
					Logger.log("DB Delete Site - Execute "+ps.toString());
					ps.execute();
					
					Logger.log("DB Delete Site - Execute Callbacks");
					for (OnDeleteAction a : OnDeleteList) {
						a.run(n);
					}
				}
				c.close();
				return true;
			} catch (Exception e) {
				Logger.log("DB Delete Site - Error - "+e.getMessage());
				e.printStackTrace();
			}
			return false;
		}
	}

}
