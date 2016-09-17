package Chi;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.NoHostAvailableException;

public class DatabaseReading extends DatabaseCassandra {
	
	public static boolean storeReading () {
		String ip=Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_IP_KEY);
		int port=Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PORT_KEY));
		Logger.log("DB Store Reading : "+Config.getConfig(Config.DATABASE_RECORD_SAVE_TO_DB_SQL_FILE_KEY));
		Cluster cluster=null;
		try {
			Logger.log("DB Store Reading - Connecting to database : "+ip+":"+port);
			cluster=getCluster();
			Session session=cluster.connect();
			Logger.log("DB Store Reading - Database connection OK!");
			
			BoundStatement [] sql=getBoundSQLStatementFromFile(session,Config.getConfig(Config.DATABASE_RECORD_SAVE_TO_DB_SQL_FILE_KEY));
			DataServerReadingToDatabase.Data d;
			while ((d=DataServerReadingToDatabase.queue.poll())!=null) {
				sql[0].setString(0, d.sname);
				sql[0].setInt(1,d.timestamp.getYear());
				sql[0].setInt(2,d.timestamp.getMonthValue());
				sql[0].setInt(3,d.timestamp.getDayOfMonth());
				sql[0].setInt(4,d.timestamp.getDayOfWeek().getValue());
				if (d.timestamp.getHour()>=12) sql[0].setBool(5,true);
				else sql[0].setBool(5, false);
				sql[0].setTimestamp(6,Timestamp.valueOf(d.timestamp));
				sql[0].setDouble(7,d.reading);
				executeSQL("DB Store Reading", session, sql[0]);
			}
			
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
	
	public static LinkedList<SensorReading> getReadingBetweenTime (String sn, LocalDateTime min, LocalDateTime max) {
		String ip=Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_IP_KEY);
		int port=Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PORT_KEY));
		Logger.log("DB Get Reading : "+Config.getConfig(Config. DATABASE_RECORD_QUERY_BETWEEN_TIME_FILE_KEY));
		Cluster cluster=null;
		LinkedList<SensorReading> list=new LinkedList<>();
		try {
			Logger.log("DB Get Reading - Connecting to database : "+ip+":"+port);
			cluster=getCluster();
			Session session=cluster.connect();
			Logger.log("DB Get Reading - Database connection OK!");
			
			BoundStatement [] sql=getBoundSQLStatementFromFile(session,Config.getConfig(Config. DATABASE_RECORD_QUERY_BETWEEN_TIME_FILE_KEY));
			sql[0].setString(0, sn);
			sql[0].setTimestamp(1,Utility.localDateTimeToSQLDate(min));
			sql[0].setTimestamp(2,Utility.localDateTimeToSQLDate(max));
			ResultSet rs=executeSQL("DB Get Reading", session, sql[0]);
			
			for (Row r : rs) {
			    if (rs.getAvailableWithoutFetching() == 100 && !rs.isFullyFetched()) rs.fetchMoreResults();
				SensorReading sr=new SensorReading(sn,Utility.dateToLocalDateTime(new Date(r.getTimestamp("TimeStp").getTime())),r.getDouble("Value"),Cache.Sensors.map.get(sn).denormalizeValue(r.getDouble("Value")));
				list.addLast(sr);
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
	
	public static LinkedList<SensorReading> getReadingMonthly (String sn, int year, int month) {
		String ip=Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_IP_KEY);
		int port=Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PORT_KEY));
		Logger.log("DB Get Reading : "+Config.getConfig(Config.DATABASE_RECORD_QUERY_MONTH_FILE_KEY));
		Cluster cluster=null;
		LinkedList<SensorReading> list=new LinkedList<>();
		try {
			Logger.log("DB Get Reading - Connecting to database : "+ip+":"+port);
			cluster=getCluster();
			Session session=cluster.connect();
			Logger.log("DB Get Reading - Database connection OK!");
			
			BoundStatement [] sql=getBoundSQLStatementFromFile(session,Config.getConfig(Config.DATABASE_RECORD_QUERY_MONTH_FILE_KEY));
			
			sql[0].setString(0, sn);
			sql[0].setInt(1,year);
			sql[0].setInt(2,month);
			ResultSet rs=executeSQL("DB Get Reading", session, sql[0]);
			for (Row r : rs) {
				if (rs.getAvailableWithoutFetching() == 100 && !rs.isFullyFetched()) rs.fetchMoreResults();
				list.addLast(new SensorReading(sn,Utility.dateToLocalDateTime(new Date(r.getTimestamp("TimeStp").getTime())),r.getDouble("Value"),Cache.Sensors.map.get(sn).denormalizeValue(r.getDouble("Value"))));
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
	
	public static void clearReading (String sn) {
		String ip=Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_IP_KEY);
		int port=Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PORT_KEY));
		Logger.log("DB Clear Reading : "+Config.getConfig(Config.DATABASE_RECORD_QUERY_MONTH_FILE_KEY));
		Cluster cluster=null;
		try {
			Logger.log("DB Clear Reading - Connecting to database : "+ip+":"+port);
			cluster=getCluster();
			Session session=cluster.connect();
			Logger.log("DB Clear Reading - Database connection OK!");
			
			session.execute("DELETE FROM Chi.SensorReading WHERE SensorName='"+sn+"';");
			
			session.close();
			cluster.close();
		} catch (NoHostAvailableException e) {
			Logger.log("DB Clear Reading - Database connection fail!");
		} catch (Exception e) {
			Logger.log("DB Clear Reading - Error - "+e.getMessage());
			e.printStackTrace();
		}
		if (cluster!=null) {
			cluster.close();
		}
	}
}
