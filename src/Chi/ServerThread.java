package Chi;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.StringTokenizer;

public class ServerThread extends Thread {
	private boolean running;
	
	public void setFlag(boolean flag) {
		Logger.log("Listening Server - SetFlag - "+flag);
		this.running=flag;
	}
	
	public void run() {
		DatagramSocket ss=null;
		byte [] buffer=new byte [8192];
		DatagramPacket packet=new DatagramPacket(buffer,buffer.length);
		try {
			Logger.log("Listening Server - StartP2 - Opening port "+Config.getConfig(Config.CONFIG_SERVER_INCOMING_PORT_KEY));
			ss=new DatagramSocket(Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_INCOMING_PORT_KEY)));
		} catch (IOException e) {
			Logger.log("Listening Server - StartP2 - Error - "+e.getMessage());
		}
		if (ss==null) {
			Server.notifyListeningThreadFailure();
		} else {
			Logger.log("Listening Server - StartP2 - Start OK!");
			Logger.log("Listening Server - Run - Listening");
			while (this.running) {
				try {
					ss.receive(packet);
					if (this.running) {
						StringTokenizer st=new StringTokenizer(new String(buffer,0,buffer.length),Config.SENSOR_DATA_DELIMITER);
						ServerToDatabase.queueData(st.nextToken(),st.nextToken(),Double.parseDouble(st.nextToken()));
					}
					packet.setLength(buffer.length);
				} catch (IOException e) {
					Logger.log("Listening Server - Error - "+e.getMessage());
				}
			}
			Logger.log("Listening Server - StopP2 - Stop listening");
		}
	}
}
