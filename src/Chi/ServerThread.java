package Chi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class ServerThread extends Thread {
	private boolean running;
	
	public void setFlag(boolean flag) {
		Logger.log("Listening Server - SetFlag - "+flag);
		this.running=flag;
	}
	
	public void run() {
		ServerSocket ss=null;
		try {
			Logger.log("Listening Server - StartP2 - Opening port "+Config.getConfig(Config.CONFIG_SERVER_INCOMING_PORT_KEY));
			ss=new ServerSocket(Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_INCOMING_PORT_KEY)));
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
					Socket clientSc=ss.accept();
					if (this.running) {
						BufferedReader br=new BufferedReader(new InputStreamReader(clientSc.getInputStream()));
						StringTokenizer st=new StringTokenizer(br.readLine(),Config.SENSOR_DATA_DELIMITER);
						ServerToDatabase.queueData(st.nextToken(),st.nextToken(),Double.parseDouble(st.nextToken()));
						br.close();
					}
					clientSc.close();
				} catch (IOException e) {
					Logger.log("Listening Server - Error - "+e.getMessage());
				}
			}
			Logger.log("Listening Server - StopP2 - Stop listening");
		}
	}
}
