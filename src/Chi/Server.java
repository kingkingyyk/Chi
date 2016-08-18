package Chi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server {
	private static FileOutputStream fileLocker;
	private static ServerThread listeningThread;
	private static File lockedFile;
	private static boolean isStarted=false;
	private static boolean attempted=false;
	
	public static void start() {
		Logger.log("Attempting to start listening server.");
		attempted=false;
		Thread t=new Thread() {
			public void run() {
				try {
					Logger.log("Listening Server - StartP1 - Checking lock file");
					File lockFile=new File(Config.getConfig(Config.CONFIG_SERVER_LOCK_FILE_KEY));
					boolean unlocked=!lockFile.exists() || lockFile.delete();
					if (unlocked && lockFile.createNewFile()) {
						Server.fileLocker=new FileOutputStream(lockFile);
						lockFile.deleteOnExit();
						lockedFile=lockFile;
						Logger.log("Listening Server - StartP1 - Start up queued.");
						isStarted=true;
					} else {
						Logger.log("Listening Server - StartP1 - Start failed. Unable to get the lock. Please stop any other instances.");
						return;
					}
				} catch (IOException e) {
					Logger.log("Listening Server - StartP1 - Start failed. "+e.getMessage());
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
		if (Server.isStarted) {
			Server.listeningThread=new ServerThread();
			listeningThread.setFlag(true);
			listeningThread.start();
		}
	}
	
	public static void stop() {
		attempted=false;
		try {
			Logger.log("Listening server - StopP1 - Unlocking lock file.");
			Server.fileLocker.close();
			Logger.log("Listening server - StopP1 - Deleting lock file.");
			Server.lockedFile.delete();
			Logger.log("Listening server - StopP1 - Stop queued.");
			isStarted=false;
			
			Server.listeningThread.setFlag(false);
			
			//Send a dummy packet to notify the server to shut down.
			DatagramPacket packet=new DatagramPacket(new byte [1],1,InetAddress.getByName("localhost"),Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_INCOMING_PORT_KEY)));
			DatagramSocket dsocket=new DatagramSocket();
			dsocket.send(packet);
			dsocket.close();
		} catch (IOException e) {
			Logger.log("Listening server - StopP1 - "+e.getMessage());
		}
		attempted=true;
	}
	
	public static boolean started() {
		return Server.isStarted;
	}
	
	public static void notifyListeningThreadFailure() {
		Server.stop();
	}
}
