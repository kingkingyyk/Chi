package Chi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Server {
	private static FileOutputStream fileLocker;
	private static Thread listeningThread;
	private static File lockedFile;
	private static boolean isStarted=false;
	private static boolean attempted=false;
	
	public static void start() {
		Logger.log("Attempting to start listening server.");
		attempted=false;
		Thread t=new Thread() {
			public void run() {
				try {
					Logger.log("Listening Server - Checking lock file");
					File lockFile=new File(Config.getConfig(Config.CONFIG_SERVER_LOCK_FILE_KEY));
					boolean unlocked=!lockFile.exists() || lockFile.delete();
					if (unlocked && lockFile.createNewFile()) {
						Server.fileLocker=new FileOutputStream(lockFile);
						lockFile.deleteOnExit();
						lockedFile=lockFile;
						Logger.log("Listening Server - Started successfully.");
						isStarted=true;
					} else {
						Logger.log("Listening Server - Start failed. Unable to get the lock. Please stop any other instances.");
						return;
					}
				} catch (IOException e) {
					Logger.log("Listening Server - Start failed. "+e.getMessage());
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
	}
	
	public static void stop() {
		attempted=false;
		try {
			Logger.log("Listening server - Unlocking lock file.");
			Server.fileLocker.close();
			Logger.log("Listening server - Deleting lock file.");
			Server.lockedFile.delete();
			Logger.log("Listening server - Stopped successfully.");
			isStarted=false;
		} catch (IOException e) {
			
		}
		attempted=true;
	}
	
	public static boolean started() {
		return Server.isStarted;
	}
}
