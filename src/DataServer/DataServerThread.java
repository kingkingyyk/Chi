package DataServer;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.CharUtils;

import Chi.Config;
import Chi.Logger;
import Database.Cache;
import Database.DatabaseController;
import Database.DatabaseReading;

public class DataServerThread extends Thread {
	private boolean running;
	
	public void setFlag(boolean flag) {
		Logger.log(Logger.LEVEL_INFO,"Data Server - SetFlag - "+flag);
		this.running=flag;
	}
	
	public void run() {
		ServerSocket ssc=null;
		try {
			ssc=new ServerSocket(Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_INCOMING_PORT_KEY)));
			Logger.log(Logger.LEVEL_INFO,"Data Server - StartP2 - Opening port "+Config.getConfig(Config.CONFIG_SERVER_INCOMING_PORT_KEY));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Fail to start server : "+e.getMessage(),Config.APP_NAME,JOptionPane.ERROR_MESSAGE);
			Logger.log(Logger.LEVEL_ERROR,"Data Server - StartP2 - Error - "+e.getMessage());
		}
		if (ssc==null) {
			DataServer.notifyDataThreadFailure();
		} else {
			Logger.log(Logger.LEVEL_INFO,"Data Server - StartP2 - Start OK!");
			Logger.log(Logger.LEVEL_INFO,"Data Server - Run - Data");
			while (this.running) {
				try {
					Socket sc=ssc.accept();
					sc.setSoTimeout(5000);
					if (this.running) {
						DataInputStream br=new DataInputStream(sc.getInputStream());
						byte [] read_data=new byte[100];
						try { br.readFully(read_data); } catch (Exception zz) {}
						StringBuilder sb=new StringBuilder();
						for (byte c : read_data) if (CharUtils.isAsciiPrintable((char)c)) sb.append((char)c);
						String received=sb.toString();
						br.close();
						sc.close();
						Logger.log(Logger.LEVEL_INFO,"Data Server - Received packet from "+sc.getInetAddress().getHostAddress()+" - "+received);
						StringTokenizer st=new StringTokenizer(received,Config.PACKET_FIELD_DELIMITER);
						String id=st.nextToken();
						switch (id) {
							case "0" : {
								try {
									String cn=st.nextToken(); //controller name
									String sn=st.nextToken(); //sensor name
									double reading=Double.parseDouble(st.nextToken());
									if (Cache.Controllers.map.get(cn)!=null && reading>=0.0 && reading<=1.0) {
										if (!Cache.Controllers.map.get(cn).getIpaddress().equals(sc.getInetAddress().getHostAddress()) || System.currentTimeMillis()-Cache.Controllers.map.get(cn).getLastreporttime().getTime()>Config.CONTROLLER_UPDATE_REPORT_MAX_MS)
											DatabaseController.updateControllerReport(cn,sc.getInetAddress().getHostAddress(),LocalDateTime.now());
										DatabaseReading.updateLastReading(sn, reading);
										DataServerReadingToDatabase.queueData(sn,reading);
									}
								} catch (NumberFormatException e) {}
								break;
							}
							case "1" : {
								String cn=st.nextToken();
								if (Cache.Controllers.map.get(cn)!=null) {
									if (!Cache.Controllers.map.get(cn).getIpaddress().equals(sc.getInetAddress().getHostAddress()) || System.currentTimeMillis()-Cache.Controllers.map.get(cn).getLastreporttime().getTime()>Config.CONTROLLER_UPDATE_REPORT_MAX_MS)
										DatabaseController.updateControllerReport(cn,sc.getInetAddress().getHostAddress(),LocalDateTime.now());
									DataServer.fireOnReportReceived(cn);
								}

								break;
							}
							case "2" : {
								String cn=st.nextToken();
								if (Cache.Controllers.map.get(cn)!=null) {
									if (!Cache.Controllers.map.get(cn).getIpaddress().equals(sc.getInetAddress().getHostAddress()) || System.currentTimeMillis()-Cache.Controllers.map.get(cn).getLastreporttime().getTime()>Config.CONTROLLER_UPDATE_REPORT_MAX_MS)
										DatabaseController.updateControllerReport(cn,sc.getInetAddress().getHostAddress(),LocalDateTime.now());
									DataServerActuatorStatusToDatabase.queueData(st.nextToken(),st.nextToken());
								}
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
					if (!DataServer.started()) Logger.log(Logger.LEVEL_ERROR,"Data Server - "+e.getMessage());
				}
			}
			Logger.log(Logger.LEVEL_INFO,"Data Server - StopP2 - Stop listening");
			try { ssc.close(); } catch (Exception e) {};
		}
	}
}
