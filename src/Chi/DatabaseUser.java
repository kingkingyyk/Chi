package Chi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DatabaseUser extends DatabaseHSQL {

	public static interface OnCreateAction {
		public void run (String user, int lvl, String status);
	}
	
	public static interface OnUpdateAction {
		public void run (String user, int lvl, String status);
	}
	
	public static interface OnDeleteAction {
		public void run (String user);
	}
	
	private static ArrayList<OnCreateAction> OnCreateList=new ArrayList<>();
	private static ArrayList<OnUpdateAction> OnUpdateList=new ArrayList<>();
	private static ArrayList<OnDeleteAction> OnDeleteList=new ArrayList<>();
	
	public static void registerOnCreateAction (OnCreateAction a) {
		if (!OnCreateList.contains(a)) {
			Logger.log("DatabaseUser - Registered "+a.toString()+" to OnCreate callback");
			OnCreateList.add(a);
		}
	}
	
	public static void registerOnUpdateAction (OnUpdateAction a) {
		if (!OnUpdateList.contains(a)) {
			Logger.log("DatabaseUser - Registered "+a.toString()+" to OnUpdate callback");
			OnUpdateList.add(a);
		}
	}
	
	public static void registerOnDeleteAction (OnDeleteAction a) {
		if (!OnDeleteList.contains(a)) {
			Logger.log("DatabaseUser - Registered "+a.toString()+" to OnDelete callback");
			OnDeleteList.add(a);
		}
	}
	
	public static ResultSet getUsernameList () {
		return runSQLFromFileAndGetData("DB Get Username List",Config.getConfig(Config.DATABASE_QUERY_USERNAME_ALL_SQL_FILE_KEY));
	}
	
	public static ResultSet getUsers () {
		return runSQLFromFileAndGetData("DB Get Users",Config.getConfig(Config.DATABASE_QUERY_USER_ALL_SQL_FILE_KEY));
	}
	
	public static boolean createUserCredential (String user, String pw, int lvl, String status) {
		Logger.log("DB Create User : "+Config.getConfig(Config.DATABASE_CREATE_USER_SQL_FILE_KEY));
		if (Cache.userMap.containsKey(user)) {
			Logger.log("DB Create User[Cache] - User already exists!");
			return false;
		} else {
			try {
				Connection c = getConnection();
				if (c!=null) {
					Logger.log("DB Create User - Database connection OK!");
					String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_CREATE_USER_SQL_FILE_KEY));
					PreparedStatement ps=c.prepareStatement(sql[0]);
					Logger.log("DB Create User - Execute "+ps.toString());
					ps.execute();
					
					ps=c.prepareStatement(sql[1]);
					ps.setString(1, user);
					ps.setString(2, pw);
					ps.setInt(3, lvl);
					ps.setString(4, status);
					ps.setTimestamp(5,Timestamp.valueOf(LocalDateTime.now()));
					Logger.log("DB Create User - Execute");
					ps.execute();
					
					ps=c.prepareStatement(sql[2]);
					Logger.log("DB Create User - Execute "+ps.toString());
					ps.execute();
					
					Logger.log("DB Create User - Execute Callbacks");
					for (OnCreateAction a : OnCreateList) {
						a.run(user, lvl, status);
					}
				}
				c.close();
				return true;
			} catch (Exception e) {
				Logger.log("DB Create User - Error - "+e.getMessage());
				e.printStackTrace();
			}
			return false;
		}
	}
	
	public static boolean updateUserCredentialPassword (String oldN, String user, String pw, int lvl, String status) {
		Logger.log("DB Update User : "+Config.getConfig(Config.DATABASE_UPDATE_USER_W_PASSWORD_SQL_FILE_KEY));
		if (!Cache.userMap.containsKey(oldN)) {
			Logger.log("DB Update User[Cache] - User doesn't exist!");
			return false;
		} else {
			Object [] o=Cache.userMap.get(oldN);
			if (o[0].equals(user) && o[1].equals(pw) && o[2].equals(lvl) && o[3].equals(status)) {
				Logger.log("DB Update User[Cache] - User information didn't change!");
				return true;
			} else {
				try {
					Connection c = getConnection();
					if (c!=null) {
						Logger.log("DB Update User - Database connection OK!");
						String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_UPDATE_USER_W_PASSWORD_SQL_FILE_KEY));
						PreparedStatement ps=c.prepareStatement(sql[0]);
						Logger.log("DB Update User - Execute "+ps.toString());
						ps.execute();
						
						ps=c.prepareStatement(sql[1]);
						ps.setString(1, user);
						ps.setString(2, pw);
						ps.setInt(3, lvl);
						ps.setString(4, status);
						ps.setString(5,oldN);
						Logger.log("DB Update User - Execute "+ps.toString());
						ps.execute();
						
						ps=c.prepareStatement(sql[2]);
						Logger.log("DB Update User - Execute "+ps.toString());
						ps.execute();
						
						Logger.log("DB Update User - Execute Callbacks");
						for (OnUpdateAction a : OnUpdateList) {
							a.run(user, lvl, status);
						}
					}
					c.close();
					return true;
				} catch (Exception e) {
					Logger.log("DB Update User - Error - "+e.getMessage());
					e.printStackTrace();
				}
				return false;
			}
		}
	}
	
	public static boolean updateUserCredentialNoPassword (String oldN, String user, int lvl, String status) {
		Logger.log("DB Update User Credential/2 : "+Config.getConfig(Config.DATABASE_UPDATE_USER_WO_PASSWORD_SQL_FILE_KEY));
		if (!Cache.userMap.containsKey(oldN)) {
			Logger.log("DB Update User/2[Cache] - User doesn't exists!");
			return false;
		} else {
			Object [] o=Cache.userMap.get(oldN);
			if (o[0].equals(user) && o[2].equals(lvl) && o[3].equals(status)) {
				Logger.log("DB Update User/2[Cache] - User information didn't change!");
				return true;
			} else {
				try {
					Connection c = getConnection();
					if (c!=null) {
						Logger.log("DB Update User/2 - Database connection OK!");
						String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_UPDATE_USER_WO_PASSWORD_SQL_FILE_KEY));
						PreparedStatement ps=c.prepareStatement(sql[0]);
						Logger.log("DB Update User/2 - Execute "+ps.toString());
						ps.execute();
						
						ps=c.prepareStatement(sql[1]);
						ps.setString(1,user);
						ps.setInt(2,lvl);
						ps.setString(3, status);
						ps.setString(4, oldN);
						Logger.log("DB Update User/2 - Execute "+ps.toString());
						ps.execute();
						
						ps=c.prepareStatement(sql[2]);
						Logger.log("DB Update User/2 - Execute "+ps.toString());
						ps.execute();
						
						Logger.log("DB Update User/2 - Execute Callbacks");
						for (OnUpdateAction a : OnUpdateList) {
							a.run(user, lvl, status);
						}
					}
					c.close();
					return true;
				} catch (Exception e) {
					Logger.log("DB Create User/2 - Error - "+e.getMessage());
					e.printStackTrace();
				}
				return false;
			}
		}
	}
	
	public static boolean deleteUser (String user) {
		Logger.log("DB Delete User : "+Config.getConfig(Config.DATABASE_DELETE_USER_SQL_FILE_KEY));
		if (!Cache.userMap.containsKey(user)) {
			Logger.log("DB Delete User[Cache] - User doesn't exists!");
			return false;
		} else {
			try {
				Connection c = getConnection();
				if (c!=null) {
					Logger.log("DB Delete User - Database connection OK!");
					String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_DELETE_USER_SQL_FILE_KEY));
					PreparedStatement ps=c.prepareStatement(sql[0]);
					Logger.log("DB Delete User - Execute "+ps.toString());
					ps.execute();
					
					ps=c.prepareStatement(sql[1]);
					ps.setString(1, user);
					Logger.log("DB Delete User - Execute "+ps.toString());
					ps.execute();
					
					ps=c.prepareStatement(sql[2]);
					Logger.log("DB Delete User - Execute "+ps.toString());
					ps.execute();
					
					Logger.log("DB Delete User - Execute Callbacks");
					for (OnDeleteAction a : OnDeleteList) {
						a.run(user);
					}
				}
				c.close();
				return true;
			} catch (Exception e) {
				Logger.log("DB Delete User - Error - "+e.getMessage());
				e.printStackTrace();
			}
		}
		return false;
	}

}
