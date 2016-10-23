package Database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Chi.Config;
import Chi.Logger;

public class DatabaseHSQL {
	
	private static String getAddress () {
		StringBuilder sb=new StringBuilder();
		sb.append("jdbc:hsqldb:hsql://");
		sb.append(Config.getConfig(Config.CONFIG_SERVER_DATABASE_HSQL_IP_KEY));
		sb.append(':');
		sb.append(Config.getConfig(Config.CONFIG_SERVER_DATABASE_HSQL_PORT_KEY));
		sb.append("/Chi");
		return sb.toString();
	}
	
	protected static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(getAddress(), Config.getConfig(Config.CONFIG_SERVER_DATABASE_HSQL_USERNAME_KEY), Config.getConfig(Config.CONFIG_SERVER_DATABASE_HSQL_PASSWORD_KEY));
	}
	
	public static boolean testConnection() {
		try {
			boolean flag=(getConnection()!=null);
			Logger.log(Logger.LEVEL_INFO,"DB HSQL Test Connection - Result : "+flag);
			return flag;
		} catch (SQLException e) {}
		Logger.log(Logger.LEVEL_INFO,"DB HSQL Test Connection - Result : "+false);
		return false;
	}
	public static boolean createTables () {
		return runSQLFromFile("DB HSQL Create Tables",Config.getConfig(Config.DATABASE_CREATE_TABLES_SQL_HSQL_FILE_KEY));
	}
	
	public static boolean reset () {
		return runSQLFromFile("DB HSQL Reset",Config.getConfig(Config.DATABASE_RESET_SQL_HSQL_FILE_KEY));
	}
	
	public static boolean runSQL(String cmdName, String sql) {
		Logger.log(Logger.LEVEL_INFO,cmdName+" - Connecting to database  : "+getAddress());
		try {
			Connection c = getConnection();
			if (c!=null) {
				Logger.log(Logger.LEVEL_INFO,cmdName+" - Database connection OK!");
				c.createStatement().executeQuery(sql);
			}
			c.close();
			return true;
		} catch (SQLException e) {
			Logger.log(Logger.LEVEL_ERROR,cmdName+" - "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean runSQLFromFile(String cmdName, String filename) {
		Logger.log(Logger.LEVEL_INFO,"Database HSQL - Run SQL From File : "+filename);
		try {
			Connection c = getConnection();
			if (c!=null) {
				Logger.log(Logger.LEVEL_INFO,cmdName+" - Database connection OK!");
				if (filename!=null) {
					String [] sql=getSQLStatementFromFile(filename);
					for (int i=0;i<sql.length;i++) {
						if (!sql[i].equals("")) {
							executeSQL(cmdName,c,sql[i]);
						}
					}
				}
			}
			c.close();
			return true;
		} catch (Exception e) {
			Logger.log(Logger.LEVEL_ERROR,cmdName+" - "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	protected static ResultSet runSQLFromFileAndGetData(String cmdName, String filename) {
		Logger.log(Logger.LEVEL_INFO,"Database HSQL - Run SQL From File : "+filename);
		try {
			Connection c = getConnection();
			ResultSet rs=null;
			if (c!=null) {
				Logger.log(Logger.LEVEL_INFO,cmdName+" - Database connection OK!");
				if (filename!=null) {
					String [] sql=getSQLStatementFromFile(filename);
					for (int i=0;i<sql.length;i++) {
						rs=executeSQL(cmdName,c,sql[i]);
					}
				}
			}
			c.close();
			return rs;
		} catch (Exception e) {
			Logger.log(Logger.LEVEL_ERROR,cmdName+" - "+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	protected static ResultSet executeSQL(String cmdName, Connection c, String statement) throws SQLException {
		Logger.log(Logger.LEVEL_INFO,cmdName+" - Execute SQL : "+statement);
		ResultSet rs=c.createStatement().executeQuery(statement);
		Logger.log(Logger.LEVEL_INFO,cmdName+" - SQL Result : "+rs.toString());
		return rs;
	}
	
	protected static String [] getSQLStatementFromFile(String path) throws IOException {
		ArrayList<String> statements=new ArrayList<>();
		StringBuilder sqlStatement=new StringBuilder();
		BufferedReader br=new BufferedReader(new FileReader(path));
		String s;
		while ((s=br.readLine())!=null) {
			if (s.equals("@")) {
				statements.add(sqlStatement.toString());
				sqlStatement=new StringBuilder();
			} else {
				sqlStatement.append(s);
				sqlStatement.append(" ");
			}
		}
		statements.add(sqlStatement.toString());
		br.close();
		return statements.toArray(new String[statements.size()]);
	}
	
}