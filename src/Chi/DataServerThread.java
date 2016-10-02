package Chi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.CharUtils;

public class DataServerThread extends Thread {
	private boolean running;
	
	public void setFlag(boolean flag) {
		Logger.log("Data Server - SetFlag - "+flag);
		this.running=flag;
	}
	
	public void run() {
		ServerSocket ssc=null;
		try {
			ssc=new ServerSocket(Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_INCOMING_PORT_KEY)));
			Logger.log("Data Server - StartP2 - Opening port "+Config.getConfig(Config.CONFIG_SERVER_INCOMING_PORT_KEY));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Fail to start server : "+e.getMessage(),Config.APP_NAME,JOptionPane.ERROR_MESSAGE);
			Logger.log("Data Server - StartP2 - Error - "+e.getMessage());
		}
		if (ssc==null) {
			DataServer.notifyDataThreadFailure();
		} else {
			Logger.log("Data Server - StartP2 - Start OK!");
			Logger.log("Data Server - Run - Data");
			while (this.running) {
				try {
					Socket sc=ssc.accept();
					if (this.running) {
						BufferedReader br=new BufferedReader(new InputStreamReader(sc.getInputStream()));
						StringBuilder sb=new StringBuilder();
						for (char c : br.readLine().toCharArray()) if (CharUtils.isAsciiPrintable(c)) sb.append(c);
						String received=sb.toString();
						br.close();
						sc.close();
						Logger.log("Data Server - Received packet from "+sc.getInetAddress().getHostAddress()+" - "+received);
						StringTokenizer st=new StringTokenizer(received,Config.PACKET_FIELD_DELIMITER);
						String id=st.nextToken();
						switch (id) {
							case "0" : {
								try {
									String cn=st.nextToken(); //controller name
									DatabaseController.updateControllerReport(cn,sc.getInetAddress().getHostAddress(),LocalDateTime.now());
									DataServerReadingToDatabase.queueData(st.nextToken(),Double.parseDouble(st.nextToken()));
								} catch (NumberFormatException e) {}
								break;
							}
							case "1" : {
								String cn=st.nextToken();
								DatabaseController.updateControllerReport(cn,sc.getInetAddress().getHostAddress(),LocalDateTime.now());
								DataServer.fireOnReportReceived(cn);
								break;
							}
							case "2" : {
								try {
									String cn=st.nextToken();
									DatabaseController.updateControllerReport(cn,sc.getInetAddress().getHostAddress(),LocalDateTime.now());
									if (FrameActuatorManagementFeedbackWait.getCurrent()!=null) {
										Thread t=new Thread() {
											public void run () {
												try { Thread.sleep(Config.CONTROLLER_READY_TIME_MS); } catch (InterruptedException e) {};
												if (FrameActuatorManagementFeedbackWait.getCurrent()!=null) FrameActuatorManagementFeedbackWait.getCurrent().setVisible(false);
											}
										};
										t.start();
									}
									DataServerActuatorStatusToDatabase.queueData(st.nextToken(),st.nextToken());
								} catch (NumberFormatException e) {}
								break;
							}
							case "3" : {
								break;
							}
							case "4" : {
								break;
							}
						}
					}
				} catch (Exception e) {
					Logger.log("Data Server - Error - "+e.getMessage());
				}
			}
			Logger.log("Data Server - StopP2 - Stop listening");
			try { ssc.close(); } catch (Exception e) {};
		}
	}
}
