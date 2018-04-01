package com.kingkingyyk.chi.config;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;

import org.apache.commons.io.IOUtils;

import io.vertx.core.json.JsonObject;

public class REST {
	private static final String FILENAME = "rest";
	public int PORT;
	public long MAX_PACKET_SIZE;
	
	private static File getSavedConfig() {
		return new File(Config.CONFIG_FOLDER+File.separator+REST.FILENAME+".json");
	}
	
	private static File getDefaultSavedConfig() {
		return new File(Config.CONFIG_FOLDER+File.separator+REST.FILENAME+Config.DEFAULT_CONFIG_NAME+".json");
	}
	
	private static final String KEY_PORT = "port";
	private static final String KEY_MAX_PACKET_SIZE = "max-packet-size";
	
	public static REST load() throws Exception {
		File saved = getSavedConfig();
		File defaultSaved = getDefaultSavedConfig();
		if (saved.exists()) return load(saved);
		else if (defaultSaved.exists()) {
			REST r=load(defaultSaved);
			r.save();
			return r;
		}
		else throw new Exception(defaultSaved.getPath()+" is not found!");
	}
	
	private static REST load(File f) throws Exception {
		JsonObject obj = new JsonObject(new String(Files.readAllBytes(f.toPath())));
		REST r = new REST();
		r.PORT = obj.getInteger(KEY_PORT);
		r.MAX_PACKET_SIZE = obj.getLong(KEY_MAX_PACKET_SIZE);
		return r;
	}
	
	public void save() throws Exception {
		File saved=REST.getSavedConfig();
		JsonObject obj = new JsonObject();
		obj.put(KEY_PORT, this.PORT);
		obj.put(KEY_MAX_PACKET_SIZE, this.MAX_PACKET_SIZE);
		FileWriter fw=new FileWriter(saved);
		IOUtils.write(obj.encode(), fw);
		fw.close();
	}
}
