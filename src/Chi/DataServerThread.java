package Chi;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

public class DataServerThread extends Thread {
	private boolean running;
	
	public void setFlag(boolean flag) {
		Logger.log("Data Server - SetFlag - "+flag);
		this.running=flag;
	}
	
	public void run() {
		DatagramSocket ss=null;
		byte [] buffer=new byte [Config.PACKET_MAX_BYTE];
		DatagramPacket packet=new DatagramPacket(buffer,buffer.length);
		try {
			Logger.log("Data Server - StartP2 - Opening port "+Config.getConfig(Config.CONFIG_SERVER_INCOMING_PORT_KEY));
			ss=new DatagramSocket(Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_INCOMING_PORT_KEY)));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Fail to start server : "+e.getMessage(),Config.APP_NAME,JOptionPane.ERROR);
			Logger.log("Data Server - StartP2 - Error - "+e.getMessage());
		}
		if (ss==null) {
			DataServer.notifyDataThreadFailure();
		} else {
			Logger.log("Data Server - StartP2 - Start OK!");
			Logger.log("Data Server - Run - Data");
			while (this.running) {
				try {
					ss.receive(packet);
					if (this.running) {
						int packetLength=0;
						for (;buffer[packetLength]!=0 && packetLength<buffer.length;packetLength++) {
						}
						String received=new String(buffer,0,packetLength);
						Logger.log("Data Server - Received packet - "+received);
						StringTokenizer st=new StringTokenizer(received,Config.PACKET_FIELD_DELIMITER);
						String id=st.nextToken();
						switch (id) {
							case "0" : {
								try {
									st.nextToken(); //controller name
									DataServerReadingToDatabase.queueData(st.nextToken(),Double.parseDouble(st.nextToken()));
								} catch (NumberFormatException e) {}
							}
							case "1" : {
								DataServerControllerAliveToDatabase.queueData(st.nextToken());
							}
							case "2" : {
								try {
									DataServerActuatorStatusToDatabase.queueData(st.nextToken(),st.nextToken());
								} catch (NumberFormatException e) {}
							}
							case "3" : {
								
							}
							case "4" : {
								
							}
						}
					}
					packet.setLength(buffer.length);
				} catch (Exception e) {
					Logger.log("Data Server - Error - "+e.getMessage());
				}
			}
			Logger.log("Data Server - StopP2 - Stop listening");
			ss.close();
		}
	}
}
