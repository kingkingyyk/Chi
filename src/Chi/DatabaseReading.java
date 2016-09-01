package Chi;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.NoHostAvailableException;

public class DatabaseReading extends DatabaseCassandra {
	
	public static boolean storeReading (String sn, LocalDateTime time, double v) {
		String ip=Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_IP_KEY);
		int port=Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PORT_KEY));
		Logger.log("DB Store Reading : "+Config.getConfig(Config.DATABASE_RECORD_SAVE_TO_DB_SQL_FILE_KEY));
		Cluster cluster=null;
		try {
			Logger.log("DB Store Reading - Connecting to database : "+ip+":"+port);
			cluster=Cluster.builder().withCredentials(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_USERNAME_KEY),Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PASSWORD_KEY))/*
			*/.withPort(port)/*
			*/.addContactPoint(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_IP_KEY)).build();
			Session session=cluster.connect();
			Logger.log("DB Store Reading - Database connection OK!");
			
			BoundStatement [] sql=getBoundSQLStatementFromFile(session,Config.getConfig(Config.DATABASE_RECORD_SAVE_TO_DB_SQL_FILE_KEY));
			sql[0].setString(0, sn);
			sql[0].setInt(1,time.getYear());
			sql[0].setInt(2,time.getMonthValue());
			sql[0].setInt(3,time.getDayOfMonth());
			sql[0].setInt(4,time.getDayOfWeek().getValue());
			if (time.getHour()>=12) sql[0].setBool(5,true);
			else sql[0].setBool(5, false);
			sql[0].setTimestamp(6,Timestamp.valueOf(time));
			sql[0].setDouble(7, v);
			executeSQL("DB Store Reading", session, sql[0]);
			
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
	
	public static ArrayList<SensorReading> getReadingBetweenTime (String sn, LocalDateTime min, LocalDateTime max, int limit) {
		String ip=Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_IP_KEY);
		int port=Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PORT_KEY));
		Logger.log("DB Get Reading : "+Config.getConfig(Config. DATABASE_RECORD_QUERY_BETWEEN_TIME_FILE_KEY));
		Cluster cluster=null;
		ArrayList<SensorReading> list=new ArrayList<>();
		try {
			Logger.log("DB Get Reading - Connecting to database : "+ip+":"+port);
			cluster=Cluster.builder().withCredentials(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_USERNAME_KEY),Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PASSWORD_KEY))/*
			*/.withPort(port)/*
			*/.addContactPoint(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_IP_KEY)).build();
			Session session=cluster.connect();
			Logger.log("DB Get Reading - Database connection OK!");
			
			BoundStatement [] sql=getBoundSQLStatementFromFile(session,Config.getConfig(Config. DATABASE_RECORD_QUERY_BETWEEN_TIME_FILE_KEY));
			sql[0].setString(0, sn);
			sql[0].setTimestamp(2,Utility.localDateTimeToDate(min));
			sql[0].setTimestamp(2,Utility.localDateTimeToDate(max));
			sql[0].setInt(3,limit);
			ResultSet rs=executeSQL("DB Get Reading", session, sql[0]);
			
			Cache.Sensors.update();
			Iterator<Row> it=rs.iterator();
			while (it.hasNext()) {
				Row r=it.next();
				SensorReading sr=new SensorReading(sn,Utility.dateToLocalDateTime(new Date(r.getTimestamp("TimeStp").getTime())),r.getDouble("Value"),Cache.Sensors.map.get(sn).denormalizeValue(r.getDouble("Value")));
				list.add(sr);
			}
			
			session.close();
			cluster.close();
		} catch (NoHostAvailableException e) {
			Logger.log("DB Get Reading - Database connection fail!");
		} catch (Exception e) {
			Logger.log("DB Get Reading - Error - "+e.getMessage());
			e.printStackTrace();
		}
		if (cluster!=null) {
			cluster.close();
		}
		return list;
	}
	
	public static ArrayList<SensorReading> getReadingMonthly (String sn, int year, int month) {
		String ip=Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_IP_KEY);
		int port=Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PORT_KEY));
		Logger.log("DB Get Reading : "+Config.getConfig(Config.DATABASE_RECORD_QUERY_MONTH_FILE_KEY));
		Cluster cluster=null;
		ArrayList<SensorReading> list=new ArrayList<>();
		try {
			Logger.log("DB Get Reading - Connecting to database : "+ip+":"+port);
			cluster=Cluster.builder().withCredentials(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_USERNAME_KEY),Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PASSWORD_KEY))/*
			*/.withPort(port)/*
			*/.addContactPoint(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_IP_KEY)).build();
			Session session=cluster.connect();
			Logger.log("DB Get Reading - Database connection OK!");
			
			BoundStatement [] sql=getBoundSQLStatementFromFile(session,Config.getConfig(Config.DATABASE_RECORD_QUERY_MONTH_FILE_KEY));
			
			LocalDateTime dt=LocalDateTime.of(year,month,1,0,0);
			Cache.Sensors.update();
			
			while (dt.getMonthValue()==month) {
				sql[0].setString(0, sn);
				sql[0].setInt(1,year);
				sql[0].setInt(2,month);
				sql[0].setInt(3,dt.getDayOfMonth());
				sql[0].setBool(4,true);
				sql[0].setInt(5,1);
				ResultSet rs=executeSQL("DB Get Reading", session, sql[0]);
				Iterator<Row> it=rs.iterator();
				if (it.hasNext()) {
					Row r=it.next();
					list.add(new SensorReading(sn,Utility.dateToLocalDateTime(new Date(r.getTimestamp("TimeStp").getTime())),r.getDouble("Value"),Cache.Sensors.map.get(sn).denormalizeValue(r.getDouble("Value"))));
				}
				
				sql[0].setBool(4,false);
				rs=executeSQL("DB Get Reading", session, sql[0]);
				it=rs.iterator();
				if (it.hasNext()) {
					Row r=it.next();
					list.add(new SensorReading(sn,Utility.dateToLocalDateTime(new Date(r.getTimestamp("TimeStp").getTime())),r.getDouble("Value"),Cache.Sensors.map.get(sn).denormalizeValue(r.getDouble("Value"))));
				}
				dt=dt.plusDays(1);
			}
			
			session.close();
			cluster.close();
		} catch (NoHostAvailableException e) {
			Logger.log("DB Get Reading - Database connection fail!");
		} catch (Exception e) {
			Logger.log("DB Get Reading - Error - "+e.getMessage());
			e.printStackTrace();
		}
		if (cluster!=null) {
			cluster.close();
		}
		return list;
	}
}
