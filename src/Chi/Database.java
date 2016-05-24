package Chi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.NoHostAvailableException;

public class Database {

	public static boolean testConnection(String ip, int port) {
		return runSQLFromFile("DB Test Connection",ip,port,null);
	}
	
	public static boolean freshStart (String ip, int port) {
		return runSQLFromFile("DB Init",ip,port,Config.getConfig(Config.DATABASE_INIT_SQL_FILE_KEY));
	}
	
	public static boolean createTables (String ip, int port) {
		return runSQLFromFile("DB Create Tables",ip,port,Config.getConfig(Config.DATABASE_CREATE_TABLES_SQL_FILE_KEY));
	}
	
	public static boolean reset (String ip, int port) {
		return runSQLFromFile("DB Reset",ip,port,Config.getConfig(Config.DATABASE_RESET_SQL_FILE_KEY));
	}
	
	public static ResultSet getSensorReading (String ip, int port) {
		return runSQLFromFileAndGetData("DB Get Sensor Reading",ip,port,Config.getConfig(Config.DATABASE_RECORD_GETTING_SQL_FILE_KEY));
	}
	
	public static boolean storeReading (String ip, int port, String cn, String sn, Date time, double v) {
		Logger.log("DB Store Reading : "+Config.getConfig(Config.DATABASE_RECORD_READING_SQL_FILE_KEY));
		Cluster cluster=null;
		try {
			Logger.log("DB Store Reading - Connecting to database : "+ip+":"+port);
			cluster=Cluster.builder().withCredentials(Config.getConfig(Config.CONFIG_SERVER_DATABASE_USERNAME_KEY),Config.getConfig(Config.CONFIG_SERVER_DATABASE_PASSWORD_KEY))/*
			*/.withPort(port)/*
			*/.addContactPoint(Config.getConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY)).build();
			Session session=cluster.connect();
			Logger.log("DB Store Reading - Database connection OK!");
			
			BoundStatement [] sql=getBoundSQLStatementFromFile(session,Config.getConfig(Config.DATABASE_RECORD_READING_SQL_FILE_KEY));
			sql[0].setString(0, cn);
			sql[0].setString(1, sn);
			sql[0].setTimestamp(2, time);
			sql[0].setDouble(3, v);
			Database.executeSQL("DB Store Reading", session, sql[0]);
			
			session.close();
			cluster.close();
			return true;
		} catch (NoHostAvailableException e) {
			Logger.log("DB Store Reading - Database connection fail!");
		} catch (Exception e) {
			Logger.log("DB Store Reading - Error - "+e.getMessage());
			e.printStackTrace();
		}
		if (cluster!=null) {
			cluster.close();
		}
		return false;
	}
	
	public static boolean runSQL(String cmdName, String ip, int port, String sql) {
		Cluster cluster=null;
		try {
			Logger.log(cmdName+" - Connecting to database : "+ip+":"+port);
			cluster=Cluster.builder().withCredentials(Config.getConfig(Config.CONFIG_SERVER_DATABASE_USERNAME_KEY),Config.getConfig(Config.CONFIG_SERVER_DATABASE_PASSWORD_KEY))/*
			*/.withPort(port)/*
			*/.addContactPoint(Config.getConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY)).build();
			Session session=cluster.connect();
			Logger.log(cmdName+" - Database connection OK!");
			System.out.println("Result : "+Database.executeSQL(cmdName,session,sql).toString());
			session.close();
			cluster.close();
			return true;
		} catch (NoHostAvailableException e) {
			Logger.log(cmdName+" - Database connection fail!");
		} catch (Exception e) {
			Logger.log(cmdName+" - Error - "+e.getMessage());
		}
		if (cluster!=null) {
			cluster.close();
		}
		return false;
	}
	
	public static boolean runSQLFromFile(String cmdName, String ip, int port, String filename) {
		Logger.log("Database - Run SQL From File : "+filename);
		Cluster cluster=null;
		try {
			Logger.log(cmdName+" - Connecting to database : "+ip+":"+port);
			cluster=Cluster.builder().withCredentials(Config.getConfig(Config.CONFIG_SERVER_DATABASE_USERNAME_KEY),Config.getConfig(Config.CONFIG_SERVER_DATABASE_PASSWORD_KEY))/*
			*/.withPort(port)/*
			*/.addContactPoint(Config.getConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY)).build();
			Session session=cluster.connect();
			Logger.log(cmdName+" - Database connection OK!");
			if (filename!=null) {
				String [] sql=getSQLStatementFromFile(filename);
				for (int i=0;i<sql.length;i++) {
					if (!sql[i].equals("")) {
						Database.executeSQL(cmdName,session,sql[i]);
					}
				}
			}
			session.close();
			cluster.close();
			return true;
		} catch (NoHostAvailableException e) {
			Logger.log(cmdName+" - Database connection fail!");
		} catch (Exception e) {
			Logger.log(cmdName+" - Error - "+e.getMessage());
		}
		if (cluster!=null) {
			cluster.close();
		}
		return false;
	}

	private static ResultSet runSQLFromFileAndGetData(String cmdName, String ip, int port, String filename) {
		Logger.log("Database - Run SQL From File : "+filename);
		Cluster cluster=null;
		try {
			Logger.log(cmdName+" - Connecting to database : "+ip+":"+port);
			cluster=Cluster.builder().withCredentials(Config.getConfig(Config.CONFIG_SERVER_DATABASE_USERNAME_KEY),Config.getConfig(Config.CONFIG_SERVER_DATABASE_PASSWORD_KEY))/*
			*/.withPort(port)/*
			*/.addContactPoint(Config.getConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY))/*
			*/.build();
			Session session=cluster.connect();
			session=cluster.connect("Chi");
			Logger.log(cmdName+" - Database connection OK!");
			ResultSet rs=null;
			if (filename!=null) {
				String [] sql=getSQLStatementFromFile(filename);
				for (int i=0;i<sql.length;i++) {
					rs=Database.executeSQL(cmdName,session,sql[i]);
				}
			}
			session.close();
			cluster.close();
			return rs;
		} catch (NoHostAvailableException e) {
			Logger.log(cmdName+" - Database connection fail!");
		} catch (Exception e) {
			Logger.log(cmdName+" - Error - "+e.getMessage());
		}
		if (cluster!=null) {
			cluster.close();
		}
		return null;
	}
	
	private static ResultSet executeSQL(String cmdName, Session session, String statement) {
		Logger.log(cmdName+" - Execute SQL : "+statement);
		ResultSet rs=session.execute(statement);
		Logger.log(cmdName+" - SQL Result : "+rs.toString());
		return rs;
	}
	
	private static ResultSet executeSQL(String cmdName, Session session, BoundStatement statement) {
		Logger.log(cmdName+" - Execute SQL : "+statement.toString());
		ResultSet rs=session.execute(statement);
		Logger.log(cmdName+" - SQL Result : "+rs.toString());
		return rs;
	}
	
	private static String [] getSQLStatementFromFile(String path) throws IOException {
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
	
	private static BoundStatement [] getBoundSQLStatementFromFile(Session ss, String path) throws IOException {
		String [] sql=getSQLStatementFromFile(path);
		BoundStatement [] psql=new BoundStatement[sql.length];
		for (int i=0;i<sql.length;i++) {
			psql[i]=ss.prepare(sql[i]).bind();
		}
		return psql;
	}
}
