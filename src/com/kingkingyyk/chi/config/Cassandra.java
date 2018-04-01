package com.kingkingyyk.chi.config;

import java.io.File;
import java.io.FileWriter;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class Cassandra {
	public static final String FILENAME = "cassandra";
	public String clusterName;
	public List<InetSocketAddress> endpoints=new ArrayList<>();
	
	private static File getSavedConfig() {
		return new File(Config.CONFIG_FOLDER+File.separator+Cassandra.FILENAME+".json");
	}
	
	private static File getDefaultSavedConfig() {
		return new File(Config.CONFIG_FOLDER+File.separator+Cassandra.FILENAME+Config.DEFAULT_CONFIG_NAME+".json");
	}
	
	public static Cassandra load() throws Exception {
		File saved = Cassandra.getSavedConfig();
		File defaultSaved = Cassandra.getDefaultSavedConfig();
		if (saved.exists()) return load(saved);
		else if (defaultSaved.exists()) {
			Cassandra c=load(defaultSaved);
			c.save();
			return c;
		}
		else throw new Exception(defaultSaved.getPath()+" is not found!");
	}
	
	private static final String KEY_CLUSTERNAME = "cluster-name";
	private static final String KEY_ENDPOINTS = "endpoints";
	private static final String KEY_ENDPOINTS_HOST = "host";
	private static final String KEY_ENDPOINTS_PORT = "port";
	
	private static Cassandra load(File f) throws Exception {
		JsonObject obj = new JsonObject(new String(Files.readAllBytes(f.toPath())));
		Cassandra c = new Cassandra();
		c.clusterName = obj.getString(KEY_CLUSTERNAME);
		JsonArray endpoints = obj.getJsonArray(KEY_ENDPOINTS);
		for (int i=0;i<endpoints.size();i++) {
			JsonObject endpointObj = endpoints.getJsonObject(i);
			c.endpoints.add(InetSocketAddress.createUnresolved(endpointObj.getString(KEY_ENDPOINTS_HOST),endpointObj.getInteger(KEY_ENDPOINTS_PORT)));
		}
		return c;
	}
	
	public void save() throws Exception {
		File saved=Cassandra.getSavedConfig();
		JsonObject obj=new JsonObject();
		obj.put(KEY_CLUSTERNAME, this.clusterName);
		JsonArray endpoints=new JsonArray();
		obj.put(KEY_ENDPOINTS, endpoints);
		for (int i=0;i<this.endpoints.size();i++) {
			InetSocketAddress add = this.endpoints.get(i);
			JsonObject endpointObj = new JsonObject();
			endpointObj.put(KEY_ENDPOINTS_HOST, add.getHostName());
			endpointObj.put(KEY_ENDPOINTS_PORT, add.getPort());
			endpoints.add(endpointObj);
		}
		FileWriter fw=new FileWriter(saved);
		IOUtils.write(obj.encode(), fw);
		fw.close();
	}
}
