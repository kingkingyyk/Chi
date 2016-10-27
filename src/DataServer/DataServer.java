package DataServer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

import org.hsqldb.lib.DataOutputStream;

import Chi.Config;
import Chi.Logger;
import Database.Cache;
import Entity.Sensor;

public class DataServer {
	private static FileOutputStream fileLocker;
	private static DataServerThread listeningThread;
	private static File lockedFile;
	private static boolean isStarted=false;
	private static boolean attempted=false;
	
	public static interface OnReportReceived {
		public void run (String controllerName);
	}
	
	public static interface OnReadingReceived {
		public void run (String sensorName, double value);
	}
	
	private static LinkedList<OnReportReceived> OnReportReceivedCallbacks=new LinkedList<>();
	private static LinkedList<OnReadingReceived> OnReadingReceivedCallbacks=new LinkedList<>();
	
	public static void registerOnReportReceived (OnReportReceived r) {
		if (!OnReportReceivedCallbacks.contains(r)) {
			Logger.log(Logger.LEVEL_INFO,"DataServer - Registered "+r.toString()+" to OnReportReceived callback");
			OnReportReceivedCallbacks.add(r);
		}
	}
	
	public static void unregisterOnReportReceived (OnReportReceived r) {
		if (OnReportReceivedCallbacks.contains(r)) {
			Logger.log(Logger.LEVEL_INFO,"DataServer - Unregistered "+r.toString()+" from OnReportReceived callback");
			OnReportReceivedCallbacks.remove(r);
		}
	}
	
	public static void fireOnReportReceived (String s) {
		for (OnReportReceived r : OnReportReceivedCallbacks) {
			r.run(s);
		}
	}
	
	public static void registerOnReadingReceived (OnReadingReceived r) {
		if (!OnReadingReceivedCallbacks.contains(r)) {
			Logger.log(Logger.LEVEL_INFO,"DataServer - Registered "+r.toString()+" to OnReadingReceived callback");
			OnReadingReceivedCallbacks.add(r);
		}
	}
	
	public static void unregisterOnReadingReceived (OnReadingReceived r) {
		if (OnReadingReceivedCallbacks.contains(r)) {
			Logger.log(Logger.LEVEL_INFO,"DataServer - Unregistered "+r.toString()+" from OnReportReceived callback");
			OnReadingReceivedCallbacks.remove(r);
		}
	}
	
	public static void fireOnReadingReceived (String name, double value) {
		Sensor s=Cache.Sensors.map.getOrDefault(name,null);
		if (s!=null) {
			double denormalized=s.denormalizeValue(value);
			for (OnReadingReceived r : OnReadingReceivedCallbacks) {
				r.run(name,denormalized);
			}
		}
	}
	
	public static void start() {
		Logger.log(Logger.LEVEL_INFO,"Attempting to start listening server.");
		attempted=false;
		Thread t=new Thread() {
			public void run() {
				try {
					Logger.log(Logger.LEVEL_INFO,"Data Server - StartP1 - Checking lock file");
					File lockFile=new File(Config.getConfig(Config.CONFIG_SERVER_LOCK_FILE_KEY));
					boolean unlocked=!lockFile.exists() || lockFile.delete();
					if (unlocked && lockFile.createNewFile()) {
						DataServer.fileLocker=new FileOutputStream(lockFile);
						lockFile.deleteOnExit();
						lockedFile=lockFile;
						Logger.log(Logger.LEVEL_INFO,"Data Server - StartP1 - Start up queued.");
						isStarted=true;
					} else {
						Logger.log(Logger.LEVEL_ERROR,"Data Server - StartP1 - Start failed. Unable to get the lock. Please stop any other instances.");
						return;
					}
				} catch (IOException e) {
					Logger.log(Logger.LEVEL_ERROR,"Data Server - StartP1 - Start failed. "+e.getMessage());
				}
				attempted=true;
			}
		};
		t.start();
		while (!attempted) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {};
		}
		if (DataServer.isStarted) {
			listeningThread=new DataServerThread();
			listeningThread.setFlag(true);
			listeningThread.start();
		}
	}
	
	public static void stop() {
		attempted=false;
		try {
			Logger.log(Logger.LEVEL_INFO,"Data server - StopP1 - Unlocking lock file.");
			DataServer.fileLocker.close();
			Logger.log(Logger.LEVEL_INFO,"Data server - StopP1 - Deleting lock file.");
			DataServer.lockedFile.delete();
			Logger.log(Logger.LEVEL_INFO,"Data server - StopP1 - Stop queued.");
			isStarted=false;
			
			DataServer.listeningThread.setFlag(false);
			
			//Send a dummy packet to notify the server to shut down.
			Socket sc=new Socket("localhost",Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_INCOMING_PORT_KEY)));
			DataOutputStream dos=new DataOutputStream(sc.getOutputStream());
			dos.write(0);
			dos.close();
			sc.close();
		} catch (IOException e) {
			Logger.log(Logger.LEVEL_ERROR,"Data server - StopP1 - "+e.getMessage());
		}
		attempted=true;
	}
	
	public static boolean started() {
		return DataServer.isStarted;
	}
	
	public static void notifyDataThreadFailure() {
		DataServer.stop();
	}
}
