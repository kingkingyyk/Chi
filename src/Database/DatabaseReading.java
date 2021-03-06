package Database;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.exceptions.NoHostAvailableException;

import Chi.Config;
import Chi.Logger;
import Chi.Utility;
import DataServer.DataServerReadingToDatabase;
import DataServer.DataServerReadingToDatabase.Data;
import Entity.Sensor;
import Entity.SensorReading;

public class DatabaseReading extends DatabaseCassandra {
	public static HashMap<String,Double> SensorLastReading=new HashMap<>();
	
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
			Logger.log(Logger.LEVEL_INFO,"DatabaseReading - Registered "+a.toString()+" to OnReceived callback");
			OnReceivedList.add(a);
		}
	}
	
	public static void registerOnRecalculateAction (OnRecalculateAction a) {
		if (!OnRecalculateList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseReading - Registered "+a.toString()+" to OnRecalculate callback");
			OnRecalculateList.add(a);
		}
	}
	
	public static void unregisterOnReceivedAction (OnReceivedAction a) {
		if (OnReceivedList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseReading - Unregistered "+a.toString()+" to OnReceived callback");
			OnReceivedList.remove(a);
		}
	}
	
	public static void unregisterOnRecalculateAction (OnRecalculateAction a) {
		if (OnRecalculateList.contains(a)) {
			Logger.log(Logger.LEVEL_INFO,"DatabaseReading - Unregistered "+a.toString()+" to OnRecalculate callback");
			OnRecalculateList.remove(a);
		}
	}
	
	public static void initialize() {
		SensorLastReading.clear();
		for (Sensor s : Cache.Sensors.map.values()) {
			SensorLastReading.put(s.getSensorname(),getLatestReading(s.getSensorname()));
		}
		DatabaseSensorReadingBind.initialize();
	}
	
	public static void updateLastReading (String sn, double reading) {
		Sensor s=Cache.Sensors.map.getOrDefault(sn,null);
		if (s!=null) {
			SensorLastReading.put(s.getSensorname(),s.denormalizeValue(reading));
		}
	}
	
	public static void migrateReading (String oldSN, String newSN) {
		SensorLastReading.put(newSN,SensorLastReading.getOrDefault(oldSN,0.0));
		SensorLastReading.remove(oldSN);
	}
	
	public static void refreshReading (String sn) {
		if (Cache.Sensors.map.containsKey(sn)) {
			LinkedList<SensorReading> list =getReadingBetweenTime(sn,LocalDateTime.of(1990,01,01,0,0,0),LocalDateTime.now());
			if (list.size()>0) SensorLastReading.put(sn,list.get(0).getActualValue());
		}
	}
	
	public static boolean storeReading () {
		Logger.log(Logger.LEVEL_INFO,"DB Store Reading : "+Config.getConfig(Config.DATABASE_RECORD_SAVE_TO_DB_SQL_FILE_KEY));
		try {
			BoundStatement [] sql=getBoundSQLStatementFromFile(DatabaseCassandra.getSession(),Config.getConfig(Config.DATABASE_RECORD_SAVE_TO_DB_SQL_FILE_KEY));
			Data d;
			while ((d=DataServerReadingToDatabase.queue.peek())!=null) {
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
				DataServerReadingToDatabase.queue.poll();
			}
			return true;
		} catch (NoHostAvailableException e) {
			if (!Chi.Chi.IssuedStop) Logger.log(Logger.LEVEL_ERROR,"DB Store Reading - Database connection fail!");
		} catch (Exception e) {
			Logger.log(Logger.LEVEL_ERROR,"DB Store Reading - "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public static LinkedList<SensorReading> getReadingBetweenTime (String sn, LocalDateTime min, LocalDateTime max) {
		Logger.log(Logger.LEVEL_INFO,"DB Get Reading : "+Config.getConfig(Config. DATABASE_RECORD_QUERY_BETWEEN_TIME_FILE_KEY));
		LinkedList<SensorReading> list=new LinkedList<>();
		try {
			BoundStatement [] sql=getBoundSQLStatementFromFile(DatabaseCassandra.getSession(),Config.getConfig(Config. DATABASE_RECORD_QUERY_BETWEEN_TIME_FILE_KEY));
			sql[0].setString(0, sn);
			sql[0].setTimestamp(1,Utility.localDateTimeToSQLDate(min));
			sql[0].setTimestamp(2,Utility.localDateTimeToSQLDate(max));
			ResultSet rs=executeSQL("DB Get Reading", DatabaseCassandra.getSession(), sql[0]);
			
			for (Row r : rs) {
			    if (rs.getAvailableWithoutFetching() == 100 && !rs.isFullyFetched()) {
			    	rs.fetchMoreResults();
			    	try { Thread.sleep(100); } catch (InterruptedException e) {}
			    }
				SensorReading sr=new SensorReading(sn,Utility.dateToLocalDateTime(new Date(r.getTimestamp("TimeStp").getTime())),r.getDouble("Value"),Cache.Sensors.map.get(sn).denormalizeValue(r.getDouble("Value")));
				list.addLast(sr);
			}
			
			Collections.sort(list);
		} catch (NoHostAvailableException e) {
			if (!Chi.Chi.IssuedStop) Logger.log(Logger.LEVEL_ERROR,"DB Get Reading - Database connection fail!");
		} catch (Exception e) {
			Logger.log(Logger.LEVEL_ERROR,"DB Get Reading - "+e.getMessage());
			e.printStackTrace();
		}
		return list;
	}
	
	private static double getLatestReading (String sn) {
		Logger.log(Logger.LEVEL_INFO,"DB Get Latest Reading : "+Config.getConfig(Config.DATABASE_RECORD_QUERY_LATEST_FILE_KEY));
		try {
			BoundStatement [] sql=getBoundSQLStatementFromFile(DatabaseCassandra.getSession(),Config.getConfig(Config.DATABASE_RECORD_QUERY_LATEST_FILE_KEY));
			sql[0].setString(0, sn);
			ResultSet rs=executeSQL("DB Get Latest Reading", DatabaseCassandra.getSession(), sql[0]);
			
			Date latestDate=new Date(0);
			double d=0;
			for (Row r : rs) {
			    if (rs.getAvailableWithoutFetching() == 100 && !rs.isFullyFetched()) {
			    	rs.fetchMoreResults();
			    	try { Thread.sleep(100); } catch (InterruptedException e) {}
			    }
				if (r.getTimestamp("TimeStp").after(latestDate)) d=Cache.Sensors.map.get(sn).denormalizeValue(r.getDouble("Value"));
			}
			return d;
		} catch (NoHostAvailableException e) {
			if (!Chi.Chi.IssuedStop) Logger.log(Logger.LEVEL_ERROR,"DB Get Latest Reading - Database connection fail!");
		} catch (Exception e) {
			Logger.log(Logger.LEVEL_ERROR,"DB Get Latest Reading - "+e.getMessage());
			e.printStackTrace();
		}
		return 0.0;
	}
	
	public static LinkedList<SensorReading> getReadingMonthly (String sn, int year, int month) {
		Logger.log(Logger.LEVEL_INFO,"DB Get Reading : "+Config.getConfig(Config.DATABASE_RECORD_QUERY_MONTH_FILE_KEY));
		LinkedList<SensorReading> list=new LinkedList<>();
		try {
			BoundStatement [] sql=getBoundSQLStatementFromFile(DatabaseCassandra.getSession(),Config.getConfig(Config.DATABASE_RECORD_QUERY_MONTH_FILE_KEY));
			
			sql[0].setString(0, sn);
			sql[0].setInt(1,year);
			sql[0].setInt(2,month);
			ResultSet rs=executeSQL("DB Get Reading", DatabaseCassandra.getSession(), sql[0]);
			for (Row r : rs) {
			    if (rs.getAvailableWithoutFetching() == 100 && !rs.isFullyFetched()) {
			    	rs.fetchMoreResults();
			    	try { Thread.sleep(100); } catch (InterruptedException e) {}
			    }
				list.addLast(new SensorReading(sn,Utility.dateToLocalDateTime(new Date(r.getTimestamp("TimeStp").getTime())),r.getDouble("Value"),Cache.Sensors.map.get(sn).denormalizeValue(r.getDouble("Value"))));
			}
			
			Collections.sort(list);
		} catch (NoHostAvailableException e) {
			Logger.log(Logger.LEVEL_ERROR,"DB Get Reading - Database connection fail!");
		} catch (Exception e) {
			Logger.log(Logger.LEVEL_ERROR,"DB Get Reading - "+e.getMessage());
			e.printStackTrace();
		}
		return list;
	}
	
	public static void updateSensorName (String oldSN, String newSN) {;
		try {
			Logger.log(Logger.LEVEL_INFO,"DB updateSensorName");
			
			ArrayList<UUID> idToUpdate=new ArrayList<>(); ArrayList<java.util.Date> dateList=new ArrayList<>();
			ResultSet rs=executeSQL("DB updateSensorName", DatabaseCassandra.getSession(), "SELECT Id, SensorName, TimeStp From SensorReading");
			for (Row r : rs) {
			    if (rs.getAvailableWithoutFetching() == 100 && !rs.isFullyFetched()) {
			    	rs.fetchMoreResults();
			    	try { Thread.sleep(100); } catch (InterruptedException e) {}
			    }
				UUID uuid=r.getUUID("Id"); String sName=r.getString("SensorName"); java.util.Date dat=r.getTimestamp("TimeStp");
				if (sName.equals(oldSN)) {
					idToUpdate.add(uuid);
					dateList.add(dat);
				}
			}
			BoundStatement bs=DatabaseCassandra.getSession().prepare("UPDATE SensorReading SET SensorName=? WHERE Id=? AND TimeStp=?").bind();
			for (int i=0;i<idToUpdate.size();i++) {
				bs.setString("SensorName",newSN);
				bs.setUUID("Id",idToUpdate.get(i));
				bs.setTimestamp("TimeStp",dateList.get(i));
				DatabaseCassandra.getSession().execute(bs);
			}
			idToUpdate.clear(); dateList.clear();
		} catch (NoHostAvailableException e) {
			Logger.log(Logger.LEVEL_ERROR,"DB updateSensorName - Database connection fail!");
		} catch (Exception e) {
			Logger.log(Logger.LEVEL_ERROR,"DB updateSensorName - "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void clearReading (String sn) {
		Logger.log(Logger.LEVEL_INFO,"DB Clear Reading");
		try {
			ArrayList<UUID> idToUpdate=new ArrayList<>(); ArrayList<java.util.Date> dateList=new ArrayList<>();
			ResultSet rs=executeSQL("DB Clear Reading", DatabaseCassandra.getSession(), "SELECT Id, SensorName, TimeStp From SensorReading");
			for (Row r : rs) {
			    if (rs.getAvailableWithoutFetching() == 100 && !rs.isFullyFetched()) {
			    	rs.fetchMoreResults();
			    	try { Thread.sleep(100); } catch (InterruptedException e) {}
			    }
				UUID uuid=r.getUUID("Id"); String sName=r.getString("SensorName"); java.util.Date dat=r.getTimestamp("TimeStp");
				if (sName.equals(sn)) {
					idToUpdate.add(uuid);
					dateList.add(dat);
				}
			}
			BoundStatement bs=DatabaseCassandra.getSession().prepare("DELETE FROM SensorReading WHERE Id=? AND TimeStp=?;").bind();
			for (int i=0;i<idToUpdate.size();i++) {
				bs.setUUID("Id",idToUpdate.get(i));
				bs.setTimestamp("TimeStp",dateList.get(i));
				DatabaseCassandra.getSession().execute(bs);
			}
			idToUpdate.clear(); dateList.clear();
		} catch (NoHostAvailableException e) {
			Logger.log(Logger.LEVEL_ERROR,"DB Clear Reading - Database connection fail!");
		} catch (Exception e) {
			Logger.log(Logger.LEVEL_ERROR,"DB Clear Reading - "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	private static LinkedList<SensorReading> getAllReadingBySensor (String sn) {
		Logger.log(Logger.LEVEL_INFO,"DB Get All Reading By Sensor : "+Config.getConfig(Config.DATABASE_RECORD_QUERY_ALL_BY_SENSOR_FILE_KEY));
		LinkedList<SensorReading> list=new LinkedList<>();
		try {
			BoundStatement [] sql=getBoundSQLStatementFromFile(DatabaseCassandra.getSession(),Config.getConfig(Config.DATABASE_RECORD_QUERY_ALL_BY_SENSOR_FILE_KEY));
			sql[0].setString(0, sn);
			ResultSet rs=executeSQL("DB Get All Reading By Sensor : ", DatabaseCassandra.getSession(), sql[0]);
			
			for (Row r : rs) {
			    if (rs.getAvailableWithoutFetching() == 100 && !rs.isFullyFetched()) {
			    	rs.fetchMoreResults();
			    	try { Thread.sleep(100); } catch (InterruptedException e) {}
			    }
				SensorReading sr=new SensorReading(sn,Utility.dateToLocalDateTime(new Date(r.getTimestamp("TimeStp").getTime())),r.getDouble("Value"),Cache.Sensors.map.get(sn).denormalizeValue(r.getDouble("Value")));
				list.addLast(sr);
			}
			
			Collections.sort(list);
		} catch (NoHostAvailableException e) {
			Logger.log(Logger.LEVEL_ERROR,"DB Get All Reading By Sensor - Database connection fail!");
		} catch (Exception e) {
			Logger.log(Logger.LEVEL_ERROR,"DB Get All Reading By Sensor - "+e.getMessage());
			e.printStackTrace();
		}
		return list;
	}
	
	private static final int KEY_LEVEL_YEAR=1;
	private static final int KEY_LEVEL_MONTH=2;
	private static final int KEY_LEVEL_DAY=3;
	
	private static String localDateTimeToKey (LocalDateTime dt, int level) {
		StringBuilder sb=new StringBuilder();
		
		if (level>=KEY_LEVEL_YEAR) sb.append(dt.getYear());
		else sb.append(1);
		sb.append(";");
		
		if (level>=KEY_LEVEL_MONTH) sb.append(dt.getMonthValue());
		else sb.append(1);
		sb.append(";");
		
		if (level>=KEY_LEVEL_DAY) sb.append(dt.getDayOfMonth());
		else sb.append(1);
		
		return sb.toString();
	}
	
	private static ArrayList<Object []> getTotalReadingGroupBy (String sn, int keyLevel) {
		LinkedList<SensorReading> list=getAllReadingBySensor(sn);
		HashMap<String,Double> map=new HashMap<>();
		for (SensorReading sr : list) {
			StringBuilder sb=new StringBuilder();
			sb.append(localDateTimeToKey(sr.getTimestamp(),keyLevel));
			map.put(sb.toString(),map.getOrDefault(sb.toString(),0.0)+sr.getActualValue());
		}
		
		//HashMap keys might not be in sequence. need to sort it!
		ArrayList<LocalDateTime> dateList=new ArrayList<>();
		for (String s : map.keySet()) {
			String [] date=s.split(";");
			dateList.add(LocalDateTime.of(Integer.parseInt(date[0]),Integer.parseInt(date[1]),Integer.parseInt(date[2]),0,0));
		}
		Collections.sort(dateList);
		
		//add to return....
		ArrayList<Object []> toReturn=new ArrayList<>();
		for (LocalDateTime dt : dateList) {
			Object [] o=new Object [keyLevel+1];
			
			if (keyLevel>=KEY_LEVEL_YEAR) o[0]=dt.getYear();
			if (keyLevel>=KEY_LEVEL_MONTH) o[1]=dt.getMonthValue();
			if (keyLevel>=KEY_LEVEL_DAY) o[2]=dt.getDayOfMonth();
			
			o[o.length-1]=map.get(localDateTimeToKey(dt,KEY_LEVEL_DAY));
			
			toReturn.add(o);
		}
		
		return toReturn;
	}
	
	public static ArrayList<Object []> getTotalReadingGroupByDay (String sn) {
		return getTotalReadingGroupBy(sn,KEY_LEVEL_DAY);
	}
	
	public static ArrayList<Object []> getTotalReadingGroupByMonth (String sn) {
		return getTotalReadingGroupBy(sn,KEY_LEVEL_MONTH);
	}
	
	public static ArrayList<Object []> getTotalReadingGroupByYear (String sn) {
		return getTotalReadingGroupBy(sn,KEY_LEVEL_YEAR);
	}
	
	private static ArrayList<Object []> getTotalReadingGroupByBetweenTime (String sn, int keyLevel, LocalDateTime st, LocalDateTime et) {
		LinkedList<SensorReading> list=getReadingBetweenTime(sn,st,et);
		HashMap<String,Double> map=new HashMap<>();
		for (SensorReading sr : list) {
			StringBuilder sb=new StringBuilder();
			sb.append(localDateTimeToKey(sr.getTimestamp(),keyLevel));
			map.put(sb.toString(),map.getOrDefault(sb.toString(),0.0)+sr.getActualValue());
		}
		
		//HashMap keys might not be in sequence. need to sort it!
		ArrayList<LocalDateTime> dateList=new ArrayList<>();
		for (String s : map.keySet()) {
			String [] date=s.split(";");
			dateList.add(LocalDateTime.of(Integer.parseInt(date[0]),Integer.parseInt(date[1]),Integer.parseInt(date[2]),0,0));
		}
		Collections.sort(dateList);
		
		//add to return....
		ArrayList<Object []> toReturn=new ArrayList<>();
		for (LocalDateTime dt : dateList) {
			Object [] o=new Object [keyLevel+1];
			
			if (keyLevel>=KEY_LEVEL_YEAR) o[0]=dt.getYear();
			if (keyLevel>=KEY_LEVEL_MONTH) o[1]=dt.getMonthValue();
			if (keyLevel>=KEY_LEVEL_DAY) o[2]=dt.getDayOfMonth();
			
			o[o.length-1]=map.get(localDateTimeToKey(dt,KEY_LEVEL_DAY));
			
			toReturn.add(o);
		}
		
		return toReturn;
	}
	
	public static ArrayList<Object []> getTotalReadingGroupByDayBetweenTime (String sn, LocalDateTime st, LocalDateTime et) {
		return getTotalReadingGroupByBetweenTime(sn,KEY_LEVEL_DAY,st,et);
	}
	
	public static ArrayList<Object []> getTotalReadingGroupByMonthBetweenTime (String sn, LocalDateTime st, LocalDateTime et) {
		return getTotalReadingGroupByBetweenTime(sn,KEY_LEVEL_MONTH,st,et);
	}
	
	public static ArrayList<Object []> getTotalReadingGroupByYearBetweenTime (String sn, LocalDateTime st, LocalDateTime et) {
		return getTotalReadingGroupByBetweenTime(sn,KEY_LEVEL_YEAR,st,et);
	}
	
	private static ArrayList<Object []> getAverageReadingGroupBy (String sn, int keyLevel) {
		LinkedList<SensorReading> list=getAllReadingBySensor(sn);
		HashMap<String,Double> map=new HashMap<>();
		HashMap<String,Integer> valueCount=new HashMap<>();
		for (SensorReading sr : list) {
			StringBuilder sb=new StringBuilder();
			sb.append(localDateTimeToKey(sr.getTimestamp(),keyLevel));
			map.put(sb.toString(),map.getOrDefault(sb.toString(),0.0)+sr.getActualValue());
			valueCount.put(sb.toString(),valueCount.getOrDefault(sb.toString(),0)+1);
		}
		
		//HashMap keys might not be in sequence. need to sort it!
		ArrayList<LocalDateTime> dateList=new ArrayList<>();
		for (String s : map.keySet()) {
			String [] date=s.split(";");
			dateList.add(LocalDateTime.of(Integer.parseInt(date[0]),Integer.parseInt(date[1]),Integer.parseInt(date[2]),0,0));
		}
		Collections.sort(dateList);
		
		//add to return....
		ArrayList<Object []> toReturn=new ArrayList<>();
		for (LocalDateTime dt : dateList) {
			Object [] o=new Object [keyLevel+1];
			
			if (keyLevel>=KEY_LEVEL_YEAR) o[0]=dt.getYear();
			if (keyLevel>=KEY_LEVEL_MONTH) o[1]=dt.getMonthValue();
			if (keyLevel>=KEY_LEVEL_DAY) o[2]=dt.getDayOfMonth();
			
			o[o.length-1]=map.get(localDateTimeToKey(dt,KEY_LEVEL_DAY))/valueCount.get(localDateTimeToKey(dt,KEY_LEVEL_DAY));
			
			toReturn.add(o);
		}
		return toReturn;
	}
	
	public static ArrayList<Object []> getAverageReadingGroupByDay (String sn) {
		return getAverageReadingGroupBy(sn,KEY_LEVEL_DAY);
	}
	
	public static ArrayList<Object []> getAverageReadingGroupByMonth (String sn) {
		return getAverageReadingGroupBy(sn,KEY_LEVEL_MONTH);
	}
	
	public static ArrayList<Object []> getAverageReadingGroupByYear (String sn) {
		return getAverageReadingGroupBy(sn,KEY_LEVEL_YEAR);
	}
	
	private static ArrayList<Object []> getAverageReadingGroupByBetweenTime (String sn, int keyLevel, LocalDateTime st, LocalDateTime et) {
		LinkedList<SensorReading> list=getReadingBetweenTime(sn,st,et);
		HashMap<String,Double> map=new HashMap<>();
		HashMap<String,Integer> valueCount=new HashMap<>();
		for (SensorReading sr : list) {
			StringBuilder sb=new StringBuilder();
			sb.append(localDateTimeToKey(sr.getTimestamp(),keyLevel));
			map.put(sb.toString(),map.getOrDefault(sb.toString(),0.0)+sr.getActualValue());
			valueCount.put(sb.toString(),valueCount.getOrDefault(sb.toString(),0)+1);
		}
		
		//HashMap keys might not be in sequence. need to sort it!
		ArrayList<LocalDateTime> dateList=new ArrayList<>();
		for (String s : map.keySet()) {
			String [] date=s.split(";");
			dateList.add(LocalDateTime.of(Integer.parseInt(date[0]),Integer.parseInt(date[1]),Integer.parseInt(date[2]),0,0));
		}
		Collections.sort(dateList);
		
		//add to return....
		ArrayList<Object []> toReturn=new ArrayList<>();
		for (LocalDateTime dt : dateList) {
			Object [] o=new Object [keyLevel+1];
			
			if (keyLevel>=KEY_LEVEL_YEAR) o[0]=dt.getYear();
			if (keyLevel>=KEY_LEVEL_MONTH) o[1]=dt.getMonthValue();
			if (keyLevel>=KEY_LEVEL_DAY) o[2]=dt.getDayOfMonth();
			
			o[o.length-1]=map.get(localDateTimeToKey(dt,KEY_LEVEL_DAY))/valueCount.get(localDateTimeToKey(dt,KEY_LEVEL_DAY));
			
			toReturn.add(o);
		}
		return toReturn;
	}
	
	public static ArrayList<Object []> getAverageReadingGroupByDayBetweenTime (String sn, LocalDateTime st, LocalDateTime et) {
		return getAverageReadingGroupByBetweenTime(sn,KEY_LEVEL_DAY,st,et);
	}
	
	public static ArrayList<Object []> getAverageReadingGroupByMonthBetweenTime (String sn, LocalDateTime st, LocalDateTime et) {
		return getAverageReadingGroupByBetweenTime(sn,KEY_LEVEL_MONTH,st,et);
	}
	
	public static ArrayList<Object []> getAverageReadingGroupByYearBetweenTime (String sn, LocalDateTime st, LocalDateTime et) {
		return getAverageReadingGroupByBetweenTime(sn,KEY_LEVEL_YEAR,st,et);
	}
	
	
	private static ArrayList<Object []> getCumulativeReadingGroupBy (String sn, int keyLevel) {
		LinkedList<SensorReading> list=getAllReadingBySensor(sn);
		Collections.sort(list);
		
		ArrayList<Object []> toReturn=new ArrayList<>();
		double currSum=0;
		
		Object [] o=new Object [keyLevel+1];
		String key="";
		for (SensorReading sr : list) {
			String currKey="";
			if (keyLevel>=KEY_LEVEL_YEAR) currKey+=sr.getTimestamp().getYear()+"_";
			if (keyLevel>=KEY_LEVEL_MONTH) currKey+=sr.getTimestamp().getMonthValue()+"_";
			if (keyLevel>=KEY_LEVEL_DAY) currKey+=sr.getTimestamp().getDayOfMonth()+"_";
			
			if (!currKey.equals(key)) {
				o=new Object[keyLevel+1];
				toReturn.add(o);
				
				if (keyLevel>=KEY_LEVEL_YEAR) o[0]=sr.getTimestamp().getYear();
				if (keyLevel>=KEY_LEVEL_MONTH) o[1]=sr.getTimestamp().getMonthValue();
				if (keyLevel>=KEY_LEVEL_DAY) o[2]=sr.getTimestamp().getDayOfMonth();
				key=currKey;
			}
			
			currSum+=sr.getActualValue();
			o[o.length-1]=currSum;

		}
		return toReturn;
	}
	
	public static ArrayList<Object []> getCumulativeReadingGroupByDay (String sn) {
		return getCumulativeReadingGroupBy(sn,KEY_LEVEL_DAY);
	}
	
	public static ArrayList<Object []> getCumulativeReadingGroupByMonth (String sn) {
		return getCumulativeReadingGroupBy(sn,KEY_LEVEL_MONTH);
	}
	
	public static ArrayList<Object []> getCumulativeReadingGroupByYear (String sn) {
		return getCumulativeReadingGroupBy(sn,KEY_LEVEL_YEAR);
	}
	
	private static ArrayList<Object []> getCumulativeReadingBetweenTime (String sn, int keyLevel, LocalDateTime st, LocalDateTime et) {
		LinkedList<SensorReading> list=getReadingBetweenTime(sn,st,et);
		Collections.sort(list);
		
		ArrayList<Object []> toReturn=new ArrayList<>();
		double currSum=0;
		
		Object [] o=new Object [keyLevel+1];
		String key="";
		for (SensorReading sr : list) {
			String currKey="";
			if (keyLevel>=KEY_LEVEL_YEAR) currKey+=sr.getTimestamp().getYear()+"_";
			if (keyLevel>=KEY_LEVEL_MONTH) currKey+=sr.getTimestamp().getMonthValue()+"_";
			if (keyLevel>=KEY_LEVEL_DAY) currKey+=sr.getTimestamp().getDayOfMonth()+"_";
			
			if (!currKey.equals(key)) {
				o=new Object[keyLevel+1];
				toReturn.add(o);
				
				if (keyLevel>=KEY_LEVEL_YEAR) o[0]=sr.getTimestamp().getYear();
				if (keyLevel>=KEY_LEVEL_MONTH) o[1]=sr.getTimestamp().getMonthValue();
				if (keyLevel>=KEY_LEVEL_DAY) o[2]=sr.getTimestamp().getDayOfMonth();
				key=currKey;
			}
			
			currSum+=sr.getActualValue();
			o[o.length-1]=currSum;
		}
		return toReturn;
	}
	
	public static ArrayList<Object []> getCumulativeReadingGroupByDayBetweenTime (String sn, LocalDateTime st, LocalDateTime et) {
		return getCumulativeReadingBetweenTime(sn,KEY_LEVEL_DAY,st,et);
	}
	
	public static ArrayList<Object []> getCumulativeReadingGroupByMonthBetweenTime (String sn, LocalDateTime st, LocalDateTime et) {
		return getCumulativeReadingBetweenTime(sn,KEY_LEVEL_MONTH,st,et);
	}
	
	public static ArrayList<Object []> getCumulativeReadingGroupByYearBetweenTime (String sn, LocalDateTime st, LocalDateTime et) {
		return getCumulativeReadingBetweenTime(sn,KEY_LEVEL_YEAR,st,et);
	}
}
