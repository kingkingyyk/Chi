package Chi;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.exceptions.NoHostAvailableException;

public class DatabaseReading extends DatabaseCassandra {
	public static HashMap<Sensor,Double> SensorLastReading=new HashMap<>();
	
	public static interface OnReceivedAction {
		public void run (String sensorName,double value);
	}
	public static interface OnRecalculateAction {
		public void run (String sensorName,double value);
	}
	
	private static LinkedList<OnReceivedAction> OnReceivedList=new LinkedList<>();
	private static LinkedList<OnRecalculateAction> OnRecalculateList=new LinkedList<>();
	
	public static void registerOnReceivedAction (OnReceivedAction a) {
		if (!OnReceivedList.contains(a)) {
			Logger.log("DatabaseReading - Registered "+a.toString()+" to OnReceived callback");
			OnReceivedList.add(a);
		}
	}
	
	public static void registerOnRecalculateAction (OnRecalculateAction a) {
		if (!OnRecalculateList.contains(a)) {
			Logger.log("DatabaseReading - Registered "+a.toString()+" to OnRecalculate callback");
			OnRecalculateList.add(a);
		}
	}
	
	public static void unregisterOnReceivedAction (OnReceivedAction a) {
		if (OnReceivedList.contains(a)) {
			Logger.log("DatabaseReading - Unregistered "+a.toString()+" to OnReceived callback");
			OnReceivedList.remove(a);
		}
	}
	
	public static void unregisterOnRecalculateAction (OnRecalculateAction a) {
		if (OnRecalculateList.contains(a)) {
			Logger.log("DatabaseReading - Unregistered "+a.toString()+" to OnRecalculate callback");
			OnRecalculateList.remove(a);
		}
	}
	
	public static void initialize() {
		SensorLastReading.clear();
		for (Sensor s : Cache.Sensors.map.values()) {
			SensorLastReading.put(s,getLatestReading(s.getSensorname()));
		}
		DatabaseSensorReadingBind.initialize();
	}
	
	public static void updateLastReading (String sn, double reading) {
		Sensor s=Cache.Sensors.map.getOrDefault(sn,null);
		if (s!=null) {
			SensorLastReading.put(s,s.denormalizeValue(reading));
		}
	}
	
	public static void refreshReading (String sn) {
		Sensor s=Cache.Sensors.map.getOrDefault(sn,null);
		if (s!=null) {
			LinkedList<SensorReading> list =getReadingBetweenTime(s.getSensorname(),LocalDateTime.of(1990,01,01,0,0,0),LocalDateTime.now());
			if (list.size()>0) SensorLastReading.put(s,list.get(0).getActualValue());
		}
	}
	
	public static boolean storeReading () {
		String ip=Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_IP_KEY);
		int port=Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PORT_KEY));
		Logger.log("DB Store Reading : "+Config.getConfig(Config.DATABASE_RECORD_SAVE_TO_DB_SQL_FILE_KEY));
		try {
			Logger.log("DB Store Reading - Connecting to database : "+ip+":"+port);
			Logger.log("DB Store Reading - Database connection OK!");
			
			BoundStatement [] sql=getBoundSQLStatementFromFile(DatabaseCassandra.getSession(),Config.getConfig(Config.DATABASE_RECORD_SAVE_TO_DB_SQL_FILE_KEY));
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
				executeSQL("DB Store Reading", DatabaseCassandra.getSession(), sql[0]);
			}
			return true;
		} catch (NoHostAvailableException e) {
			Logger.log("DB Store Reading - Database connection fail!");
		} catch (Exception e) {
			Logger.log("DB Store Reading - Error - "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public static LinkedList<SensorReading> getReadingBetweenTime (String sn, LocalDateTime min, LocalDateTime max) {
		String ip=Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_IP_KEY);
		int port=Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PORT_KEY));
		Logger.log("DB Get Reading : "+Config.getConfig(Config. DATABASE_RECORD_QUERY_BETWEEN_TIME_FILE_KEY));
		LinkedList<SensorReading> list=new LinkedList<>();
		try {
			Logger.log("DB Get Reading - Connecting to database : "+ip+":"+port);
			Logger.log("DB Get Reading - Database connection OK!");
			
			BoundStatement [] sql=getBoundSQLStatementFromFile(DatabaseCassandra.getSession(),Config.getConfig(Config. DATABASE_RECORD_QUERY_BETWEEN_TIME_FILE_KEY));
			sql[0].setString(0, sn);
			sql[0].setTimestamp(1,Utility.localDateTimeToSQLDate(min));
			sql[0].setTimestamp(2,Utility.localDateTimeToSQLDate(max));
			ResultSet rs=executeSQL("DB Get Reading", DatabaseCassandra.getSession(), sql[0]);
			
			for (Row r : rs) {
			    if (rs.getAvailableWithoutFetching() == 100 && !rs.isFullyFetched()) rs.fetchMoreResults();
				SensorReading sr=new SensorReading(sn,Utility.dateToLocalDateTime(new Date(r.getTimestamp("TimeStp").getTime())),r.getDouble("Value"),Cache.Sensors.map.get(sn).denormalizeValue(r.getDouble("Value")));
				list.addLast(sr);
			}
			
			Collections.sort(list);
		} catch (NoHostAvailableException e) {
			Logger.log("DB Get Reading - Database connection fail!");
		} catch (Exception e) {
			Logger.log("DB Get Reading - Error - "+e.getMessage());
			e.printStackTrace();
		}
		return list;
	}
	
	public static double getLatestReading (String sn) {
		String ip=Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_IP_KEY);
		int port=Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PORT_KEY));
		Logger.log("DB Get Latest Reading : "+Config.getConfig(Config.DATABASE_RECORD_QUERY_LATEST_FILE_KEY));
		try {
			Logger.log("DB Get Latest Reading - Connecting to database : "+ip+":"+port);
			Logger.log("DB Get Latest Reading - Database connection OK!");
			
			BoundStatement [] sql=getBoundSQLStatementFromFile(DatabaseCassandra.getSession(),Config.getConfig(Config.DATABASE_RECORD_QUERY_LATEST_FILE_KEY));
			sql[0].setString(0, sn);
			ResultSet rs=executeSQL("DB Get Latest Reading", DatabaseCassandra.getSession(), sql[0]);
			
			double d=0;
			for (Row r : rs) {
				d=Cache.Sensors.map.get(sn).denormalizeValue(r.getDouble("Value"));
			}
			return d;
		} catch (NoHostAvailableException e) {
			Logger.log("DB Get Latest Reading - Database connection fail!");
		} catch (Exception e) {
			Logger.log("DB Get Latest Reading - Error - "+e.getMessage());
			e.printStackTrace();
		}
		return 0.0;
	}
	
	public static LinkedList<SensorReading> getReadingMonthly (String sn, int year, int month) {
		String ip=Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_IP_KEY);
		int port=Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PORT_KEY));
		Logger.log("DB Get Reading : "+Config.getConfig(Config.DATABASE_RECORD_QUERY_MONTH_FILE_KEY));
		LinkedList<SensorReading> list=new LinkedList<>();
		try {
			Logger.log("DB Get Reading - Connecting to database : "+ip+":"+port);
			Logger.log("DB Get Reading - Database connection OK!");
			
			BoundStatement [] sql=getBoundSQLStatementFromFile(DatabaseCassandra.getSession(),Config.getConfig(Config.DATABASE_RECORD_QUERY_MONTH_FILE_KEY));
			
			sql[0].setString(0, sn);
			sql[0].setInt(1,year);
			sql[0].setInt(2,month);
			ResultSet rs=executeSQL("DB Get Reading", DatabaseCassandra.getSession(), sql[0]);
			for (Row r : rs) {
				if (rs.getAvailableWithoutFetching() == 100 && !rs.isFullyFetched()) rs.fetchMoreResults();
				list.addLast(new SensorReading(sn,Utility.dateToLocalDateTime(new Date(r.getTimestamp("TimeStp").getTime())),r.getDouble("Value"),Cache.Sensors.map.get(sn).denormalizeValue(r.getDouble("Value"))));
			}
			
			Collections.sort(list);
		} catch (NoHostAvailableException e) {
			Logger.log("DB Get Reading - Database connection fail!");
		} catch (Exception e) {
			Logger.log("DB Get Reading - Error - "+e.getMessage());
			e.printStackTrace();
		}
		return list;
	}
	
	public static void updateSensorName (String oldSN, String newSN) {;
		try {
			Logger.log("DB updateSensorName");
			
			DatabaseCassandra.getSession().execute("UPDATE SensorReading SET SensorName='"+newSN+"' WHERE SensorName='"+oldSN+"';");
		} catch (NoHostAvailableException e) {
			Logger.log("DB updateSensorName - Database connection fail!");
		} catch (Exception e) {
			Logger.log("DB updateSensorName - Error - "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void clearReading (String sn) {
		String ip=Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_IP_KEY);
		int port=Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PORT_KEY));
		Logger.log("DB Clear Reading : "+Config.getConfig(Config.DATABASE_RECORD_QUERY_MONTH_FILE_KEY));
		try {
			Logger.log("DB Clear Reading - Connecting to database : "+ip+":"+port);
			Logger.log("DB Clear Reading - Database connection OK!");
			
			DatabaseCassandra.getSession().execute("DELETE FROM SensorReading WHERE SensorName='"+sn+"';");
		} catch (NoHostAvailableException e) {
			Logger.log("DB Clear Reading - Database connection fail!");
		} catch (Exception e) {
			Logger.log("DB Clear Reading - Error - "+e.getMessage());
			e.printStackTrace();
		}
	}
}
