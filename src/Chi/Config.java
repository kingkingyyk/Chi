package Chi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

	//Unchangeable via configuration file
	public static final String APP_NAME="Chi";
	public static final String VERSION="v1.2 Build 125";
	
	public static final String CONFIG_FOLDER_PATH="conf";
	public static final String CONFIG_FILE_NAME="chi_conf.sh";

	public static final String PACKET_FIELD_DELIMITER=";";
	public static final int PACKET_MAX_BYTE=512;
	public static final String DATABASE_SQL_PATH="sql";
	public static final String ICON_TEXTURE_PATH="/images";
	public static final int CONTROLLER_READY_TIME_MS=4000;
	public static final int CONTROLLER_REPLY_TIMEOUT_MS=15000;
	public static final int CONTROLLER_MAX_RETRY=3;
	
	private static Properties prop;
	
	//Changeable via configuration file
	public static final String CONFIG_SERVER_INCOMING_PORT_KEY="server.incoming.port";
	public static final String CONFIG_SERVER_DATABASE_CASSANDRA_IP_KEY="server.database.cassandra.ip";
	public static final String CONFIG_SERVER_DATABASE_CASSANDRA_PORT_KEY="server.database.cassandra.port";
	public static final String CONFIG_SERVER_DATABASE_HSQL_IP_KEY="server.hsql.ip";
	public static final String CONFIG_SERVER_DATABASE_HSQL_PORT_KEY="server.database.hsql.port";
	public static final String CONFIG_SERVER_DATABASE_CASSANDRA_USERNAME_KEY="server.database.cassandra.username";
	public static final String CONFIG_SERVER_DATABASE_CASSANDRA_PASSWORD_KEY="server.database.cassandra.password";
	public static final String CONFIG_SERVER_DATABASE_HSQL_USERNAME_KEY="server.database.hsql.username";
	public static final String CONFIG_SERVER_DATABASE_HSQL_PASSWORD_KEY="server.database.hsql.password";
	public static final String CONFIG_SERVER_DATABASE_TEST_OK_KEY="server.database.test.success.text";
	public static final String CONFIG_SERVER_DATABASE_TEST_FAIL_KEY="server.database.test.fail.text";
	public static final String CONFIG_SERVER_CONTROLLER_PORT_KEY="server.controller.port";
	public static final String CONFIG_SERVER_GWT_PORT_KEY="server.gwt.com.port";
	public static final String CONFIG_SERVER_GWT_ENCRYPTION_KEY="server.gwt.com.encryption";
	public static final String CONFIG_SERVER_GWT_SALT_KEY="server.gwt.salt";
	public static final String CONFIG_SERVER_GWT_PASSWORD_KEY="server.gwt.password";
	public static final String CONFIG_SERVER_LOGGING_LEVEL_KEY="server.logging.level";
	public static final String CONFIG_SERVER_LOGGING_TOFILE_KEY="server.logging.file";
	//Listening server config
	public static final String CONFIG_SERVER_LOCK_FILE_KEY="server.lock.file.name";
	//Database
	public static final String DATABASE_INIT_SQL_CASSANDRA_FILE_KEY="server.database.sql.cassandra.init.file";
	public static final String DATABASE_CREATE_TABLES_SQL_CASSANDRA_FILE_KEY="server.database.sql.cassandra.createtables.file";
	public static final String DATABASE_RESET_SQL_CASSANDRA_FILE_KEY="server.database.cassandra.sql.reset.file";
	public static final String DATABASE_CREATE_TABLES_SQL_HSQL_FILE_KEY="server.database.sql.hsql.createtables.file";
	public static final String DATABASE_RESET_SQL_HSQL_FILE_KEY="server.database.cassandra.hsql.reset.file";
	
	public static final String DATABASE_RECORD_SAVE_TO_DB_SQL_FILE_KEY="server.database.sql.record.save";
	public static final String DATABASE_RECORD_QUERY_BETWEEN_TIME_FILE_KEY="server.database.sql.record.query.betweentime";
	public static final String DATABASE_RECORD_QUERY_MONTH_FILE_KEY="server.database.sql.record.query.month";
	public static final String DATABASE_RECORD_QUERY_LATEST_FILE_KEY="server.database.sql.record.query.latest";
	//Default values.
	public static final String CONFIG_DEFAULT_KEY=".default";
	private static final String CONFIG_SERVER_INCOMING_PORT_DEFAULT="40000";
	private static final String CONFIG_SERVER_CONTROLLER_PORT_DEFAULT="40002";
	private static final String CONFIG_SERVER_DATABASE_CASSANDRA_IP_DEFAULT="127.0.0.1";
	private static final String CONFIG_SERVER_DATABASE_CASSANDRA_PORT_DEFAULT="9042";
	private static final String CONFIG_SERVER_DATABASE_HSQL_IP_DEFAULT="127.0.0.1";
	private static final String CONFIG_SERVER_DATABASE_HSQL_PORT_DEFAULT="9001";
	private static final String CONFIG_SERVER_DATABASE_CASSANDRA_USERNAME_DEFAULT="chi-admin";
	private static final String CONFIG_SERVER_DATABASE_CASSANDRA_PASSWORD_DEFAULT="chi-admin";
	private static final String CONFIG_SERVER_DATABASE_HSQL_USERNAME_DEFAULT="chi-admin";
	private static final String CONFIG_SERVER_DATABASE_HSQL_PASSWORD_DEFAULT="chi-admin";
	private static final String CONFIG_SERVER_DATABASE_TEST_OK_DEFAULT="<html><font color=\"green\">Connection OK!</font></html>";
	private static final String CONFIG_SERVER_DATABASE_TEST_FAIL_DEFAULT="<html><font color=\"red\">Connection fail!</font></html>";
	private static final String CONFIG_SERVER_GWT_PORT_DEFAULT="40001";
	private static final String CONFIG_SERVER_GWT_SALT_DEFAULT="chichilol";
	private static final String CONFIG_SERVER_GWT_PASSWORD_DEFAULT="chi-admin";
	private static final boolean CONFIG_SERVER_GWT_ENCRYPTION_DEFAULT=false;
	private static final String CONFIG_SERVER_LOGGING_LEVEL_DEFAULT=Logger.LEVEL_INFO+"";
	private static final boolean CONFIG_SERVER_LOGGING_TOFILE_DEFAULT=false;
	//Listening server
	public static final String CONFIG_SERVER_LOCK_FILE_DEFAULT="CHI_LOCK";
	//Database - Init
	public static final String DATABASE_INIT_SQL_CASSANDRA_FILE_DEFAULT="InitializationCassandra/InitDB.sql";
	public static final String DATABASE_CREATE_TABLES_SQL_CASSANDRA_FILE_DEFAULT="InitializationCassandra/CreateTables.sql";
	public static final String DATABASE_RESET_SQL_CASSANDRA_FILE_DEFAULT="InitializationCassandra/ResetDB.sql";
	//Database - Init
	public static final String DATABASE_CREATE_TABLES_SQL_HSQL_FILE_DEFAULT="InitializationHSQL/CreateTables.sql";
	public static final String DATABASE_RESET_SQL_HSQL_FILE_DEFAULT="InitializationHSQL/ResetDB.sql";
	//Database - Reading
	public static final String DATABASE_RECORD_READING_SQL_FILE_DEFAULT="";
	public static final String DATABASE_RECORD_GETTING_SQL_FILE_DEFAULT="";
	
	public static final String DATABASE_RECORD_SAVE_TO_DB_SQL_FILE_DEFAULT="Reading/RecordReading.sql";
	public static final String DATABASE_RECORD_QUERY_BETWEEN_TIME_FILE_DEFAULT="Reading/QueryReadingBetweenTime.sql";
	public static final String DATABASE_RECORD_QUERY_MONTH_FILE_DEFAULT="Reading/QueryReadingMonth.sql";
	public static final String DATABASE_RECORD_QUERY_LATEST_FILE_DEFAULT="Reading/QueryLatestReading.sql";
	
	public static void setConfig (String key, String value) {
		prop.setProperty(key,value);
	}
	
	public static void writeConfigFile() {
		try {
			//Creates config foldr if not exists.
			File configFolder=new File(Config.CONFIG_FOLDER_PATH);
			if (!configFolder.exists()) {
				configFolder.mkdir();
			}
			//Attempts to write the file.
			FileOutputStream fos=new FileOutputStream(Config.CONFIG_FOLDER_PATH+"/"+CONFIG_FILE_NAME);
			prop.store(fos,"");
			fos.close();
			Logger.log(Logger.LEVEL_INFO,"Configuration file write done!");
		} catch (IOException e) {
			Logger.log(Logger.LEVEL_ERROR,"Configuration file write failed! "+e.getMessage());
		}
	}
	
	public static String getConfig (String key) {
		return prop.getProperty(key);
	}
	
	public static void initialize() {
		//Load the configuration.
		prop=new Properties();
		
		//Default values
		Config.setConfig(CONFIG_SERVER_INCOMING_PORT_KEY+CONFIG_DEFAULT_KEY, CONFIG_SERVER_INCOMING_PORT_DEFAULT);
		Config.setConfig(CONFIG_SERVER_CONTROLLER_PORT_KEY+CONFIG_DEFAULT_KEY, CONFIG_SERVER_CONTROLLER_PORT_DEFAULT);
		Config.setConfig(CONFIG_SERVER_DATABASE_CASSANDRA_IP_KEY+CONFIG_DEFAULT_KEY, CONFIG_SERVER_DATABASE_CASSANDRA_IP_DEFAULT);
		Config.setConfig(CONFIG_SERVER_DATABASE_CASSANDRA_PORT_KEY+CONFIG_DEFAULT_KEY, CONFIG_SERVER_DATABASE_CASSANDRA_PORT_DEFAULT);
		Config.setConfig(CONFIG_SERVER_DATABASE_HSQL_IP_KEY+CONFIG_DEFAULT_KEY, CONFIG_SERVER_DATABASE_HSQL_IP_DEFAULT);
		Config.setConfig(CONFIG_SERVER_DATABASE_HSQL_PORT_KEY+CONFIG_DEFAULT_KEY, CONFIG_SERVER_DATABASE_HSQL_PORT_DEFAULT);
		Config.setConfig(CONFIG_SERVER_DATABASE_CASSANDRA_USERNAME_KEY+CONFIG_DEFAULT_KEY, CONFIG_SERVER_DATABASE_CASSANDRA_USERNAME_DEFAULT);
		Config.setConfig(CONFIG_SERVER_DATABASE_CASSANDRA_PASSWORD_KEY+CONFIG_DEFAULT_KEY, CONFIG_SERVER_DATABASE_CASSANDRA_PASSWORD_DEFAULT);
		Config.setConfig(CONFIG_SERVER_DATABASE_HSQL_USERNAME_KEY+CONFIG_DEFAULT_KEY, CONFIG_SERVER_DATABASE_HSQL_USERNAME_DEFAULT);
		Config.setConfig(CONFIG_SERVER_DATABASE_HSQL_PASSWORD_KEY+CONFIG_DEFAULT_KEY, CONFIG_SERVER_DATABASE_HSQL_PASSWORD_DEFAULT);
		Config.setConfig(CONFIG_SERVER_DATABASE_TEST_OK_KEY+CONFIG_DEFAULT_KEY, CONFIG_SERVER_DATABASE_TEST_OK_DEFAULT);
		Config.setConfig(CONFIG_SERVER_DATABASE_TEST_FAIL_KEY+CONFIG_DEFAULT_KEY, CONFIG_SERVER_DATABASE_TEST_FAIL_DEFAULT);
		Config.setConfig(CONFIG_SERVER_LOCK_FILE_KEY+CONFIG_DEFAULT_KEY, CONFIG_SERVER_LOCK_FILE_DEFAULT);
		Config.setConfig(CONFIG_SERVER_GWT_PORT_KEY+CONFIG_DEFAULT_KEY, CONFIG_SERVER_GWT_PORT_DEFAULT);
		Config.setConfig(CONFIG_SERVER_GWT_ENCRYPTION_KEY+CONFIG_DEFAULT_KEY, String.valueOf(CONFIG_SERVER_GWT_ENCRYPTION_DEFAULT));
		Config.setConfig(CONFIG_SERVER_GWT_SALT_KEY+CONFIG_DEFAULT_KEY, CONFIG_SERVER_GWT_SALT_DEFAULT);
		Config.setConfig(CONFIG_SERVER_GWT_PASSWORD_KEY+CONFIG_DEFAULT_KEY, CONFIG_SERVER_GWT_PASSWORD_DEFAULT);
		Config.setConfig(CONFIG_SERVER_LOGGING_LEVEL_KEY+CONFIG_DEFAULT_KEY, CONFIG_SERVER_LOGGING_LEVEL_DEFAULT);
		Config.setConfig(CONFIG_SERVER_LOGGING_TOFILE_KEY+CONFIG_DEFAULT_KEY, String.valueOf(CONFIG_SERVER_LOGGING_TOFILE_DEFAULT));
		
		Config.setConfig(DATABASE_INIT_SQL_CASSANDRA_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_INIT_SQL_CASSANDRA_FILE_DEFAULT);
		Config.setConfig(DATABASE_CREATE_TABLES_SQL_CASSANDRA_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_CREATE_TABLES_SQL_CASSANDRA_FILE_DEFAULT);
		Config.setConfig(DATABASE_RESET_SQL_CASSANDRA_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_RESET_SQL_CASSANDRA_FILE_DEFAULT);
		Config.setConfig(DATABASE_CREATE_TABLES_SQL_HSQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_CREATE_TABLES_SQL_HSQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_RESET_SQL_HSQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_RESET_SQL_HSQL_FILE_DEFAULT);
		
		Config.setConfig(DATABASE_RECORD_SAVE_TO_DB_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_RECORD_SAVE_TO_DB_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_RECORD_QUERY_BETWEEN_TIME_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_RECORD_QUERY_BETWEEN_TIME_FILE_DEFAULT);
		Config.setConfig(DATABASE_RECORD_QUERY_MONTH_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_RECORD_QUERY_MONTH_FILE_DEFAULT);
		Config.setConfig(DATABASE_RECORD_QUERY_LATEST_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_RECORD_QUERY_LATEST_FILE_DEFAULT);

		//Real config
		String [] keys=prop.keySet().toArray(new String[prop.keySet().size()]);
		for (int i=0;i<keys.length;i++) {
			String key=keys[i];
			key=key.substring(0, key.length()-CONFIG_DEFAULT_KEY.length());
			Config.setConfig(key, Config.getConfig(key+CONFIG_DEFAULT_KEY));
		}
		//To be replaced by configuration files
		try {
			FileInputStream fis=new FileInputStream(Config.CONFIG_FOLDER_PATH+"/"+CONFIG_FILE_NAME);
			prop.load(fis);
			fis.close();
		} catch (IOException e) {
			Logger.log(Logger.LEVEL_WARNING,"Configuration file is invalid! Using default configuration.");
		}
		
	}
	
}
