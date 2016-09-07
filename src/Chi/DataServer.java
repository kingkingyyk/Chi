package Chi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;

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
			OnReportReceivedCallbacks.add(r);
		}
	}
	
	public static void unregisterOnReportReceived (OnReportReceived r) {
		if (OnReportReceivedCallbacks.contains(r)) {
			OnReportReceivedCallbacks.remove(r);
		}
	}
	
	public static void registerOnReadingReceived (OnReadingReceived r) {
		if (!OnReadingReceivedCallbacks.contains(r)) {
			OnReadingReceivedCallbacks.add(r);
		}
	}
	
	public static void unregisterOnReadingReceived (OnReadingReceived r) {
		if (OnReadingReceivedCallbacks.contains(r)) {
			OnReadingReceivedCallbacks.remove(r);
		}
	}
	
	public static void start() {
		Logger.log("Attempting to start listening server.");
		attempted=false;
		Thread t=new Thread() {
			public void run() {
				try {
					Logger.log("Data Server - StartP1 - Checking lock file");
					File lockFile=new File(Config.getConfig(Config.CONFIG_SERVER_LOCK_FILE_KEY));
					boolean unlocked=!lockFile.exists() || lockFile.delete();
					if (unlocked && lockFile.createNewFile()) {
						DataServer.fileLocker=new FileOutputStream(lockFile);
						lockFile.deleteOnExit();
						lockedFile=lockFile;
						Logger.log("Data Server - StartP1 - Start up queued.");
						isStarted=true;
					} else {
						Logger.log("Data Server - StartP1 - Start failed. Unable to get the lock. Please stop any other instances.");
						return;
					}
				} catch (IOException e) {
					Logger.log("Data Server - StartP1 - Start failed. "+e.getMessage());
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
			Logger.log("Data server - StopP1 - Unlocking lock file.");
			DataServer.fileLocker.close();
			Logger.log("Data server - StopP1 - Deleting lock file.");
			DataServer.lockedFile.delete();
			Logger.log("Data server - StopP1 - Stop queued.");
			isStarted=false;
			
			DataServer.listeningThread.setFlag(false);
			
			//Send a dummy packet to notify the server to shut down.
			DatagramPacket packet=new DatagramPacket(new byte [1],1,InetAddress.getByName("localhost"),Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_INCOMING_PORT_KEY)));
			DatagramSocket dsocket=new DatagramSocket();
			dsocket.send(packet);
			dsocket.close();
		} catch (IOException e) {
			Logger.log("Data server - StopP1 - "+e.getMessage());
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
