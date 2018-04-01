package com.kingkingyyk.chi;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import com.kingkingyyk.chi.config.Config;
import com.kingkingyyk.chi.connection.REST;
import com.kingkingyyk.chi.data.db.Database;

public class Chi {
	public static AtomicBoolean IsShuttingDown = new AtomicBoolean(false);
	
	public static void main (String [] args) throws Exception {
		Config.load();
		
		Database.initialize(Config.cassandra);
		REST.initialize(Config.rest);
	}
	
	public static boolean shutdown() {
		boolean flag = !IsShuttingDown.get();
		if (flag) {
			IsShuttingDown.set(true);
			Logger.getLogger(Chi.class.getName()).info("Shutting down in 5 seconds!");
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					Database.shutdown();
					System.exit(0);
				}}
			, 5000);
		}
		return flag;
	}
	
}
