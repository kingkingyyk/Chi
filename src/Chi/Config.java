package Chi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

	//Unchangeable via configuration file
	public static final String APP_NAME="Chi";
	public static final String VERSION="build 000";
	
	public static final String CONFIG_FOLDER_PATH="conf";
	public static final String CONFIG_FILE_NAME="chi_conf.sh";
	
	private static Properties prop;
	
	//Changeable via configuration file
	public static final String CONFIG_SERVER_INCOMING_PORT_KEY="server.incoming.port";
	public static final String CONFIG_SERVER_DATABASE_IP_KEY="server.database.ip";
	public static final String CONFIG_SERVER_DATABASE_PORT_KEY="server.database.port";
	public static final String CONFIG_SERVER_DATABASE_TEST_OK_KEY="server.database.test.success.text";
	public static final String CONFIG_SERVER_DATABASE_TEST_FAIL_KEY="server.database.test.fail.text";
	//Listening server config
	public static final String CONFIG_SERVER_LOCK_FILE_KEY="server.lock.file.name";
	
	//Default values.
	public static final String CONFIG_DEFAULT_KEY=".default";
	private static final String CONFIG_SERVER_INCOMING_PORT_DEFAULT="40000";
	private static final String CONFIG_SERVER_DATABASE_IP_DEFAULT="127.0.0.1";
	private static final String CONFIG_SERVER_DATABASE_PORT_DEFAULT="9042";
	private static final String CONFIG_SERVER_DATABASE_TEST_OK_DEFAULT="<html><font color=\"green\">Connection OK!</font></html>";
	private static final String CONFIG_SERVER_DATABASE_TEST_FAIL_DEFAULT="<html><font color=\"red\">Connection fail!</font></html>";
	
	public static final String CONFIG_SERVER_LOCK_FILE_DEFAULT="CHI_LOCK";
	
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
		Config.setConfig(CONFIG_SERVER_DATABASE_IP_KEY+CONFIG_DEFAULT_KEY, CONFIG_SERVER_DATABASE_IP_DEFAULT);
		Config.setConfig(CONFIG_SERVER_DATABASE_PORT_KEY+CONFIG_DEFAULT_KEY, CONFIG_SERVER_DATABASE_PORT_DEFAULT);
		Config.setConfig(CONFIG_SERVER_DATABASE_TEST_OK_KEY+CONFIG_DEFAULT_KEY, CONFIG_SERVER_DATABASE_TEST_OK_DEFAULT);
		Config.setConfig(CONFIG_SERVER_DATABASE_TEST_FAIL_KEY+CONFIG_DEFAULT_KEY, CONFIG_SERVER_DATABASE_TEST_FAIL_DEFAULT);
		Config.setConfig(CONFIG_SERVER_LOCK_FILE_KEY+CONFIG_DEFAULT_KEY, CONFIG_SERVER_LOCK_FILE_DEFAULT);

		//Real config
		Config.setConfig(CONFIG_SERVER_INCOMING_PORT_KEY, CONFIG_SERVER_INCOMING_PORT_DEFAULT);
		Config.setConfig(CONFIG_SERVER_DATABASE_IP_KEY, CONFIG_SERVER_DATABASE_IP_DEFAULT);
		Config.setConfig(CONFIG_SERVER_DATABASE_PORT_KEY, CONFIG_SERVER_DATABASE_PORT_DEFAULT);
		Config.setConfig(CONFIG_SERVER_DATABASE_TEST_OK_KEY, CONFIG_SERVER_DATABASE_TEST_OK_DEFAULT);
		Config.setConfig(CONFIG_SERVER_DATABASE_TEST_FAIL_KEY, CONFIG_SERVER_DATABASE_TEST_FAIL_DEFAULT);
		Config.setConfig(CONFIG_SERVER_LOCK_FILE_KEY, CONFIG_SERVER_LOCK_FILE_DEFAULT);
		
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
