package Chi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

	//Unchangeable via configuration file
	public static final String APP_NAME="Chi";
	public static final String VERSION="v1.0";
	
	public static final String CONFIG_FOLDER_PATH="conf";
	public static final String CONFIG_FILE_NAME="chi_conf.sh";

	public static final String PACKET_FIELD_DELIMITER=";";
	public static final int PACKET_MAX_BYTE=512;
	public static final String DATABASE_SQL_PATH="sql";
	public static final String ICON_TEXTURE_PATH="/images";
	
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
	public static final String CONFIG_SERVER_CONTROLLER_IP_KEY="server.controller.ip";
	public static final String CONFIG_SERVER_CONTROLLER_PORT_KEY="server.controller.port";
	public static final String CONFIG_SERVER_GWT_PORT_KEY="server.gwt.com.port";
	public static final String CONFIG_SERVER_GWT_KEY_FILE_KEY="server.controller.keyfile";
	public static final String CONFIG_SERVER_GWT_PASSWORD_KEY="server.controller.password";
	//Listening server config
	public static final String CONFIG_SERVER_LOCK_FILE_KEY="server.lock.file.name";
	//Database
	public static final String DATABASE_INIT_SQL_CASSANDRA_FILE_KEY="server.database.sql.cassandra.init.file";
	public static final String DATABASE_CREATE_TABLES_SQL_CASSANDRA_FILE_KEY="server.database.sql.cassandra.createtables.file";
	public static final String DATABASE_RESET_SQL_CASSANDRA_FILE_KEY="server.database.cassandra.sql.reset.file";
	public static final String DATABASE_CREATE_TABLES_SQL_HSQL_FILE_KEY="server.database.sql.hsql.createtables.file";
	public static final String DATABASE_RESET_SQL_HSQL_FILE_KEY="server.database.cassandra.hsql.reset.file";
	
	public static final String DATABASE_RECORD_READING_SQL_FILE_KEY="server.database.sql.recordreading";
	public static final String DATABASE_RECORD_GETTING_SQL_FILE_KEY="server.database.sql.recordgetting";
	
	public static final String DATABASE_CREATE_USER_SQL_FILE_KEY="server.database.sql.user.create.file";
	public static final String DATABASE_DELETE_USER_SQL_FILE_KEY="server.database.sql.user.delete.file";
	public static final String DATABASE_UPDATE_USER_W_PASSWORD_SQL_FILE_KEY="server.database.sql.user.updateWpassword.file";
	public static final String DATABASE_UPDATE_USER_WO_PASSWORD_SQL_FILE_KEY="server.database.sql.user.updateWOpassword.file";
	public static final String DATABASE_QUERY_USERNAME_SQL_FILE_KEY="server.database.sql.user.query.file";
	public static final String DATABASE_QUERY_USERNAME_ALL_SQL_FILE_KEY="server.database.sql.user.queryallnames.file";
	public static final String DATABASE_QUERY_USER_ALL_SQL_FILE_KEY="server.database.sql.user.queryall.file";
	
	public static final String DATABASE_CREATE_SITE_SQL_FILE_KEY="server.database.sql.site.create.file";
	public static final String DATABASE_DELETE_SITE_SQL_FILE_KEY="server.database.sql.site.delete.file";
	public static final String DATABASE_UPDATE_SITE_SQL_FILE_KEY="server.database.sql.site.update.file";
	public static final String DATABASE_QUERY_SITE_ALL_NAME_SQL_FILE_KEY="server.database.sql.site.queryallnames.file";
	public static final String DATABASE_QUERY_SITE_ALL_SQL_FILE_KEY="server.database.sql.site.queryall.file";
	
	public static final String DATABASE_CREATE_CONTROLLER_SQL_FILE_KEY="server.database.sql.controller.create.file";
	public static final String DATABASE_DELETE_CONTROLLER_SQL_FILE_KEY="server.database.sql.controller.delete.file";
	public static final String DATABASE_UPDATE_CONTROLLER_SQL_FILE_KEY="server.database.sql.controller.update.file";
	public static final String DATABASE_UPDATE_CONTROLLER_REPORT_TIME_SQL_FILE_KEY="server.database.sql.controller.update.reporttime.file";
	public static final String DATABASE_QUERY_CONTROLLER_ALL_NAME_SQL_FILE_KEY="server.database.sql.controller.queryname.file";
	public static final String DATABASE_QUERY_CONTROLLER_ALL_SQL_FILE_KEY="server.database.sql.controller.queryall.file";
	
	public static final String DATABASE_CREATE_SENSOR_CLASS_SQL_FILE_KEY="server.database.sql.sensorclass.create.file";
	public static final String DATABASE_DELETE_SENSOR_CLASS_SQL_FILE_KEY="server.database.sql.sensorclass.delete.file";
	public static final String DATABASE_UPDATE_SENSOR_CLASS_SQL_FILE_KEY="server.database.sql.sensorclass.update.file";
	public static final String DATABASE_QUERY_SENSOR_CLASS_ALL_SQL_FILE_KEY="server.database.sql.sensorclass.queryall.file";
	
	public static final String DATABASE_CREATE_SENSOR_SQL_FILE_KEY="server.database.sql.sensor.create.file";
	public static final String DATABASE_DELETE_SENSOR_SQL_FILE_KEY="server.database.sql.sensor.delete.file";
	public static final String DATABASE_UPDATE_SENSOR_SQL_FILE_KEY="server.database.sql.sensor.update.file";
	public static final String DATABASE_QUERY_SENSOR_ALL_NAME_SQL_FILE_KEY="server.database.sql.sensor.queryname.file";
	public static final String DATABASE_QUERY_SENSOR_ALL_SQL_FILE_KEY="server.database.sql.sensor.queryall.file";
	
	public static final String DATABASE_CREATE_ACTUATOR_SQL_FILE_KEY="server.database.sql.actuator.create.file";
	public static final String DATABASE_DELETE_ACTUATOR_SQL_FILE_KEY="server.database.sql.actuator.delete.file";
	public static final String DATABASE_UPDATE_ACTUATOR_SQL_FILE_KEY="server.database.sql.actuator.update.file";
	public static final String DATABASE_UPDATE_ACTUATOR_STATUS_SQL_FILE_KEY="server.database.sql.actuator.updatestatus.file";
	public static final String DATABASE_QUERY_ACTUATOR_SQL_FILE_KEY="server.database.sql.actuator.query.file";
	public static final String DATABASE_QUERY_ACTUATOR_ALL_NAME_SQL_FILE_KEY="server.database.sql.actuator.queryallnames.file";
	public static final String DATABASE_QUERY_ACTUATOR_ALL_SQL_FILE_KEY="server.database.sql.actuator.queryall.file";
	
	public static final String DATABASE_CREATE_DAY_SCHEDULE_RULE_SQL_FILE_KEY="server.database.sql.schedule.dayrule.create.file";
	public static final String DATABASE_DELETE_DAY_SCHEDULE_RULE_SQL_FILE_KEY="server.database.sql.schedule.dayrule.delete.file";
	public static final String DATABASE_UPDATE_DAY_SCHEDULE_RULE_SQL_FILE_KEY="server.database.sql.schedule.dayrule.update.file";
	public static final String DATABASE_QUERY_DAY_SCHEDULE_RULE_SQL_FILE_KEY="server.database.sql.schedule.dayrule.query.file";
	public static final String DATABASE_QUERY_DAY_SCHEDULE_RULE_ALL_NAME_SQL_FILE_KEY="server.database.sql.schedule.dayrule.queryallnames.file";
	public static final String DATABASE_QUERY_DAY_SCHEDULE_RULE_ALL_SQL_FILE_KEY="server.database.sql.schedule.dayrule.queryall.file";
	
	public static final String DATABASE_CREATE_REGULAR_SCHEDULE_SQL_FILE_KEY="server.database.sql.schedule.regular.create.file";
	public static final String DATABASE_DELETE_REGULAR_SCHEDULE_SQL_FILE_KEY="server.database.sql.schedule.regular.delete.file";
	public static final String DATABASE_UPDATE_REGULAR_SCHEDULE_SQL_FILE_KEY="server.database.sql.schedule.regular.update.file";
	public static final String DATABASE_QUERY_REGULAR_SCHEDULE_SQL_FILE_KEY="server.database.sql.schedule.regular.query.file";
	public static final String DATABASE_QUERY_REGULAR_SCHEDULE_ALL_NAME_SQL_FILE_KEY="server.database.sql.schedule.regular.queryallnames.file";
	public static final String DATABASE_QUERY_REGULAR_SCHEDULE_ALL_SQL_FILE_KEY="server.database.sql.schedule.regular.queryall.file";
	
	public static final String DATABASE_CREATE_SPECIAL_SCHEDULE_SQL_FILE_KEY="server.database.sql.schedule.special.create.file";
	public static final String DATABASE_DELETE_SPECIAL_SCHEDULE_SQL_FILE_KEY="server.database.sql.schedule.special.delete.file";
	public static final String DATABASE_UPDATE_SPECIAL_SCHEDULE_SQL_FILE_KEY="server.database.sql.schedule.special.update.file";
	public static final String DATABASE_QUERY_SPECIAL_SCHEDULE_SQL_FILE_KEY="server.database.sql.schedule.special.query.file";
	public static final String DATABASE_QUERY_SPECIAL_SCHEDULE_ALL_NAME_SQL_FILE_KEY="server.database.sql.special.regular.queryallnames.file";
	public static final String DATABASE_QUERY_SPECIAL_SCHEDULE_ALL_SQL_FILE_KEY="server.database.sql.schedule.special.queryall.file";
	//Default values.
	public static final String CONFIG_DEFAULT_KEY=".default";
	private static final String CONFIG_SERVER_INCOMING_PORT_DEFAULT="40000";
	private static final String CONFIG_SERVER_CONTROLLER_IP_DEFAULT="224.0.0.180";
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
	private static final String CONFIG_SERVER_GWT_KEY_FILE_DEFAULT="chikey";
	private static final String CONFIG_SERVER_GWT_PASSWORD_DEFAULT="chi-admin";
	
	//Listening server
	public static final String CONFIG_SERVER_LOCK_FILE_DEFAULT="CHI_LOCK";
	//Database - Init
	public static final String DATABASE_INIT_SQL_CASSANDRA_FILE_DEFAULT="InitializationCassandra/InitDB.sql";
	public static final String DATABASE_CREATE_TABLES_SQL_CASSANDRA_FILE_DEFAULT="InitializationCassandra/CreateTables.sql";
	public static final String DATABASE_RESET_SQL_CASSANDRA_FILE_DEFAULT="InitializationCassandra/ResetDB.sql";
	
	public static final String DATABASE_CREATE_TABLES_SQL_HSQL_FILE_DEFAULT="InitializationHSQL/CreateTables.sql";
	public static final String DATABASE_RESET_SQL_HSQL_FILE_DEFAULT="InitializationHSQL/ResetDB.sql";
	//Database - User
	public static final String DATABASE_CREATE_USER_SQL_FILE_DEFAULT="User/InsertUser.sql";
	public static final String DATABASE_DELETE_USER_SQL_FILE_DEFAULT="User/DeleteUser.sql";
	public static final String DATABASE_UPDATE_USER_W_PASSWORD_SQL_FILE_DEFAULT="User/UpdateUserWithPassword.sql";
	public static final String DATABASE_UPDATE_USER_WO_PASSWORD_SQL_FILE_DEFAULT="User/UpdateUserWithoutPassword.sql";
	public static final String DATABASE_QUERY_USERNAME_SQL_FILE_DEFAULT="User/QueryUsername.sql";
	public static final String DATABASE_QUERY_USERNAME_ALL_SQL_FILE_DEFAULT="User/QueryAllUsernames.sql";
	public static final String DATABASE_QUERY_USER_ALL_SQL_FILE_DEFAULT="User/QueryAllUsers.sql";
	//Database - Sensor Class
	public static final String DATABASE_CREATE_SITE_SQL_FILE_DEFAULT="Site/InsertSite.sql";
	public static final String DATABASE_DELETE_SITE_SQL_FILE_DEFAULT="Site/DeleteSite.sql";
	public static final String DATABASE_UPDATE_SITE_SQL_FILE_DEFAULT="Site/UpdateSite.sql";
	public static final String DATABASE_QUERY_SITE_ALL_SQL_FILE_DEFAULT="Site/QueryAllSites.sql";
	public static final String DATABASE_QUERY_SITE_ALL_NAME_SQL_FILE_DEFAULT="Site/QueryAllSiteName.sql";
	//Database - Controller
	public static final String DATABASE_CREATE_CONTROLLER_SQL_FILE_DEFAULT="Controller/InsertController.sql";
	public static final String DATABASE_DELETE_CONTROLLER_SQL_FILE_DEFAULT="Controller/DeleteController.sql";
	public static final String DATABASE_UPDATE_CONTROLLER_SQL_FILE_DEFAULT="Controller/UpdateController.sql";
	public static final String DATABASE_UPDATE_CONTROLLER_REPORT_TIME_SQL_FILE_DEFAULT="Controller/UpdateControllerLastReportTime.sql";
	public static final String DATABASE_QUERY_CONTROLLER_SQL_FILE_DEFAULT="Controller/QueryController.sql";
	public static final String DATABASE_QUERY_CONTROLLER_ALL_NAME_SQL_FILE_DEFAULT="Controller/QueryControllerName.sql";
	public static final String DATABASE_QUERY_CONTROLLER_ALL_SQL_FILE_DEFAULT="Controller/QueryAllControllers.sql";
	//Database - Site
	public static final String DATABASE_CREATE_SENSOR_CLASS_SQL_FILE_DEFAULT="SensorClass/InsertSensorClass.sql";
	public static final String DATABASE_DELETE_SENSOR_CLASS_SQL_FILE_DEFAULT="SensorClass/DeleteSensorClass.sql";
	public static final String DATABASE_UPDATE_SENSOR_CLASS_SQL_FILE_DEFAULT="SensorClass/UpdateSensorClass.sql";
	public static final String DATABASE_QUERY_SENSOR_CLASS_ALL_SQL_FILE_DEFAULT="SensorClass/QueryAllClasses.sql";
	//Database - Sensor
	public static final String DATABASE_CREATE_SENSOR_SQL_FILE_DEFAULT="Sensor/InsertSensor.sql";
	public static final String DATABASE_DELETE_SENSOR_SQL_FILE_DEFAULT="Sensor/DeleteSensor.sql";
	public static final String DATABASE_UPDATE_SENSOR_SQL_FILE_DEFAULT="Sensor/UpdateSensor.sql";
	public static final String DATABASE_QUERY_SENSOR_ALL_NAME_SQL_FILE_DEFAULT="Sensor/QuerySensorName.sql";
	public static final String DATABASE_QUERY_SENSOR_ALL_SQL_FILE_DEFAULT="Sensor/QueryAllSensors.sql";
	//Database - Actuator
	public static final String DATABASE_CREATE_ACTUATOR_SQL_FILE_DEFAULT="Actuator/InsertActuator.sql";
	public static final String DATABASE_DELETE_ACTUATOR_SQL_FILE_DEFAULT="Actuator/DeleteActuator.sql";
	public static final String DATABASE_UPDATE_ACTUATOR_SQL_FILE_DEFAULT="Actuator/UpdateActuator.sql";
	public static final String DATABASE_UPDATE_ACTUATOR_STATUS_SQL_FILE_DEFAULT="Actuator/UpdateActuatorStatus.sql";
	public static final String DATABASE_QUERY_ACTUATOR_SQL_FILE_DEFAULT="Actuator/QueryActuator.sql";
	public static final String DATABASE_QUERY_ACTUATOR_ALL_SQL_FILE_DEFAULT="Actuator/QueryAllActuators.sql";
	public static final String DATABASE_QUERY_ACTUATOR_ALL_NAME_SQL_FILE_DEFAULT="Actuator/QueryAllActuatorName.sql";
	//Database - Day Schedule Rule
	public static final String DATABASE_CREATE_DAY_SCHEDULE_RULE_SQL_FILE_DEFAULT="DayScheduleRule/InsertDayScheduleRule.sql";
	public static final String DATABASE_DELETE_DAY_SCHEDULE_RULE_SQL_FILE_DEFAULT="DayScheduleRule/DeleteDayScheduleRule.sql";
	public static final String DATABASE_UPDATE_DAY_SCHEDULE_RULE_SQL_FILE_DEFAULT="DayScheduleRule/UpdateDayScheduleRule.sql";
	public static final String DATABASE_QUERY_DAY_SCHEDULE_RULE_SQL_FILE_DEFAULT="DayScheduleRule/QueryDayScheduleRule.sql";
	public static final String DATABASE_QUERY_DAY_SCHEDULE_RULE_ALL_SQL_FILE_DEFAULT="DayScheduleRule/QueryAllDayScheduleRules.sql";
	public static final String DATABASE_QUERY_DAY_SCHEDULE_RULE_ALL_NAME_SQL_FILE_DEFAULT="DayScheduleRule/QueryAllDayScheduleRuleName.sql";
	//Database - Regular Schedule
	public static final String DATABASE_CREATE_REGULAR_SCHEDULE_SQL_FILE_DEFAULT="RegularSchedule/InsertRegularSchedule.sql";
	public static final String DATABASE_DELETE_REGULAR_SCHEDULE_SQL_FILE_DEFAULT="RegularSchedule/DeleteRegularSchedule.sql";
	public static final String DATABASE_UPDATE_REGULAR_SCHEDULE_SQL_FILE_DEFAULT="RegularSchedule/UpdateRegularSchedule.sql";
	public static final String DATABASE_QUERY_REGULAR_SCHEDULE_SQL_FILE_DEFAULT="RegularSchedule/QueryRegularSchedule.sql";
	public static final String DATABASE_QUERY_REGULAR_SCHEDULE_ALL_SQL_FILE_DEFAULT="RegularSchedule/QueryAllRegularSchedules.sql";
	public static final String DATABASE_QUERY_REGULAR_SCHEDULE_ALL_NAME_SQL_FILE_DEFAULT="RegularSchedule/QueryAllRegularScheduleName.sql";
	//Database - Special Schedule
	public static final String DATABASE_CREATE_SPECIAL_SCHEDULE_SQL_FILE_DEFAULT="SpecialSchedule/InsertSpecialSchedule.sql";
	public static final String DATABASE_DELETE_SPECIAL_SCHEDULE_SQL_FILE_DEFAULT="SpecialSchedule/DeleteSpecialSchedule.sql";
	public static final String DATABASE_UPDATE_SPECIAL_SCHEDULE_SQL_FILE_DEFAULT="SpecialSchedule/UpdateSpecialSchedule.sql";
	public static final String DATABASE_QUERY_SPECIAL_SCHEDULE_SQL_FILE_DEFAULT="SpecialSchedule/QuerySpecialSchedule.sql";
	public static final String DATABASE_QUERY_SPECIAL_SCHEDULE_ALL_SQL_FILE_DEFAULT="SpecialSchedule/QueryAllSpecialSchedules.sql";
	public static final String DATABASE_QUERY_SPECIAL_SCHEDULE_ALL_NAME_SQL_FILE_DEFAULT="SpecialSchedule/QueryAllSpecialScheduleName.sql";
	//Database - Reading
	public static final String DATABASE_RECORD_READING_SQL_FILE_DEFAULT="Reading/RecordReading.sql";
	public static final String DATABASE_RECORD_GETTING_SQL_FILE_DEFAULT="Reading/QueryReading.sql";

	
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
			Logger.log("Configuration file write done!");
		} catch (IOException e) {
			Logger.log("Configuration file write failed! Error : "+e.getMessage());
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
		Config.setConfig(CONFIG_SERVER_CONTROLLER_IP_KEY+CONFIG_DEFAULT_KEY, CONFIG_SERVER_CONTROLLER_IP_DEFAULT);
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
		Config.setConfig(CONFIG_SERVER_GWT_KEY_FILE_KEY+CONFIG_DEFAULT_KEY, CONFIG_SERVER_GWT_KEY_FILE_DEFAULT);
		Config.setConfig(CONFIG_SERVER_GWT_PASSWORD_KEY+CONFIG_DEFAULT_KEY, CONFIG_SERVER_GWT_PASSWORD_DEFAULT);
		
		Config.setConfig(DATABASE_INIT_SQL_CASSANDRA_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_INIT_SQL_CASSANDRA_FILE_DEFAULT);
		Config.setConfig(DATABASE_CREATE_TABLES_SQL_CASSANDRA_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_CREATE_TABLES_SQL_CASSANDRA_FILE_DEFAULT);
		Config.setConfig(DATABASE_RESET_SQL_CASSANDRA_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_RESET_SQL_CASSANDRA_FILE_DEFAULT);
		Config.setConfig(DATABASE_CREATE_TABLES_SQL_HSQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_CREATE_TABLES_SQL_HSQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_RESET_SQL_HSQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_RESET_SQL_HSQL_FILE_DEFAULT);
		
		Config.setConfig(DATABASE_RECORD_READING_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_RECORD_READING_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_RECORD_GETTING_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_RECORD_GETTING_SQL_FILE_DEFAULT);
		
		Config.setConfig(DATABASE_CREATE_USER_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_CREATE_USER_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_DELETE_USER_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_DELETE_USER_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_UPDATE_USER_W_PASSWORD_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_UPDATE_USER_W_PASSWORD_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_UPDATE_USER_WO_PASSWORD_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_UPDATE_USER_WO_PASSWORD_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_QUERY_USERNAME_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_QUERY_USERNAME_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_QUERY_USERNAME_ALL_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_QUERY_USERNAME_ALL_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_QUERY_USER_ALL_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_QUERY_USER_ALL_SQL_FILE_DEFAULT);

		Config.setConfig(DATABASE_CREATE_SITE_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_CREATE_SITE_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_DELETE_SITE_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_DELETE_SITE_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_UPDATE_SITE_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_UPDATE_SITE_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_QUERY_SITE_ALL_NAME_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_QUERY_SITE_ALL_NAME_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_QUERY_SITE_ALL_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_QUERY_SITE_ALL_SQL_FILE_DEFAULT);
		
		Config.setConfig(DATABASE_CREATE_CONTROLLER_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_CREATE_CONTROLLER_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_DELETE_CONTROLLER_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_DELETE_CONTROLLER_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_UPDATE_CONTROLLER_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_UPDATE_CONTROLLER_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_UPDATE_CONTROLLER_REPORT_TIME_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_UPDATE_CONTROLLER_REPORT_TIME_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_QUERY_CONTROLLER_ALL_NAME_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_QUERY_CONTROLLER_ALL_NAME_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_QUERY_CONTROLLER_ALL_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_QUERY_CONTROLLER_ALL_SQL_FILE_DEFAULT);
		
		Config.setConfig(DATABASE_CREATE_SENSOR_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_CREATE_SENSOR_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_DELETE_SENSOR_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_DELETE_SENSOR_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_UPDATE_SENSOR_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_UPDATE_SENSOR_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_QUERY_SENSOR_ALL_NAME_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_QUERY_SENSOR_ALL_NAME_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_QUERY_SENSOR_ALL_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_QUERY_SENSOR_ALL_SQL_FILE_DEFAULT);
		
		Config.setConfig(DATABASE_CREATE_SENSOR_CLASS_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_CREATE_SENSOR_CLASS_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_DELETE_SENSOR_CLASS_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_DELETE_SENSOR_CLASS_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_UPDATE_SENSOR_CLASS_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_UPDATE_SENSOR_CLASS_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_QUERY_SENSOR_CLASS_ALL_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_QUERY_SENSOR_CLASS_ALL_SQL_FILE_DEFAULT);
		
		Config.setConfig(DATABASE_CREATE_ACTUATOR_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_CREATE_ACTUATOR_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_DELETE_ACTUATOR_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_DELETE_ACTUATOR_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_UPDATE_ACTUATOR_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_UPDATE_ACTUATOR_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_UPDATE_ACTUATOR_STATUS_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_UPDATE_ACTUATOR_STATUS_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_QUERY_ACTUATOR_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_QUERY_ACTUATOR_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_QUERY_ACTUATOR_ALL_NAME_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_QUERY_ACTUATOR_ALL_NAME_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_QUERY_ACTUATOR_ALL_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_QUERY_ACTUATOR_ALL_SQL_FILE_DEFAULT);
		
		Config.setConfig(DATABASE_CREATE_DAY_SCHEDULE_RULE_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_CREATE_DAY_SCHEDULE_RULE_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_DELETE_DAY_SCHEDULE_RULE_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_DELETE_DAY_SCHEDULE_RULE_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_UPDATE_DAY_SCHEDULE_RULE_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_UPDATE_DAY_SCHEDULE_RULE_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_QUERY_DAY_SCHEDULE_RULE_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_QUERY_DAY_SCHEDULE_RULE_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_QUERY_DAY_SCHEDULE_RULE_ALL_NAME_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_QUERY_DAY_SCHEDULE_RULE_ALL_NAME_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_QUERY_DAY_SCHEDULE_RULE_ALL_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_QUERY_DAY_SCHEDULE_RULE_ALL_SQL_FILE_DEFAULT);
		
		Config.setConfig(DATABASE_CREATE_REGULAR_SCHEDULE_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_CREATE_REGULAR_SCHEDULE_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_DELETE_REGULAR_SCHEDULE_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_DELETE_REGULAR_SCHEDULE_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_UPDATE_REGULAR_SCHEDULE_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_UPDATE_REGULAR_SCHEDULE_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_QUERY_REGULAR_SCHEDULE_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_QUERY_REGULAR_SCHEDULE_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_QUERY_REGULAR_SCHEDULE_ALL_NAME_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_QUERY_REGULAR_SCHEDULE_ALL_NAME_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_QUERY_REGULAR_SCHEDULE_ALL_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_QUERY_REGULAR_SCHEDULE_ALL_SQL_FILE_DEFAULT);
		
		Config.setConfig(DATABASE_CREATE_SPECIAL_SCHEDULE_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_CREATE_SPECIAL_SCHEDULE_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_DELETE_SPECIAL_SCHEDULE_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_DELETE_SPECIAL_SCHEDULE_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_UPDATE_SPECIAL_SCHEDULE_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_UPDATE_SPECIAL_SCHEDULE_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_QUERY_SPECIAL_SCHEDULE_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_QUERY_SPECIAL_SCHEDULE_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_QUERY_SPECIAL_SCHEDULE_ALL_NAME_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_QUERY_SPECIAL_SCHEDULE_ALL_NAME_SQL_FILE_DEFAULT);
		Config.setConfig(DATABASE_QUERY_SPECIAL_SCHEDULE_ALL_SQL_FILE_KEY+CONFIG_DEFAULT_KEY, DATABASE_SQL_PATH+"/"+DATABASE_QUERY_SPECIAL_SCHEDULE_ALL_SQL_FILE_DEFAULT);
		
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
			Logger.log("Configuration file is invalid! Using default configuration.");
		}
		
	}
	
}
