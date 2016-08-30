package Chi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.StringTokenizer;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class GWTServer {
	private static GWTServerThread t;
	
	private static class GWTServerThread extends Thread {
		public SSLServerSocket serverSocket;
		boolean stopFlag=false;
		
		public void run () {
			System.setProperty("javax.net.ssl.keyStore",Config.getConfig(Config.CONFIG_SERVER_GWT_KEY_FILE_KEY));
			System.setProperty("javax.net.ssl.keyStorePassword",Config.getConfig(Config.CONFIG_SERVER_GWT_PASSWORD_KEY));
			while (!stopFlag) {
				try {
					try {
					    SSLServerSocketFactory socketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
					    serverSocket = (SSLServerSocket) socketFactory.createServerSocket(Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_GWT_PORT_KEY)));
					    serverSocket.setSoTimeout(5000);
					    SSLSocket sslsocket=(SSLSocket) serverSocket.accept();
					    InputStream is=sslsocket.getInputStream();
					    InputStreamReader isRead=new InputStreamReader(is);
					    BufferedReader br=new BufferedReader(isRead);
					    String s=br.readLine();
					    br.close(); isRead.close(); is.close();
					    
					    OutputStream os=sslsocket.getOutputStream();
					    ObjectOutputStream oos=new ObjectOutputStream(os);
					    
					    StringTokenizer st=new StringTokenizer(s,Config.PACKET_FIELD_DELIMITER);
					    String data1=st.nextToken();
					    String data2=st.nextToken();
					    switch (data1) {
						    case "1" : {
						    	Cache.Users.update();
						    	oos.writeBoolean(Cache.Users.map.containsKey(data2));
						    	break;
						    }
						    case "2" : {
						    	DatabaseUser.createUserCredential(data2,st.nextToken(),1,"PENDING APPROVAL");
						    	oos.writeBoolean(true);
						    	break;
						    }
						    case "3" : {
						    	Cache.Users.update();
						    	if (!Cache.Users.map.containsKey(data2)) {
						    		oos.writeObject("FAILCHECK");
						    	} else {
						    		oos.writeObject(Cache.Users.map.get(data2).getStatus());
						    	}
						    }
					    }
					    oos.close(); os.close();
					    sslsocket.close(); serverSocket.close();
					} catch (SocketTimeoutException te) {
						serverSocket.close();
					} 
				} catch (IOException e) {
					Logger.log("GWTServerThread Error - "+e.getMessage());
				}
			}
			try {
				serverSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			};
		}
	}
	
	
	public static boolean start() {
		if (t==null) {
			Logger.log("GWTServer - Starting");
			t=new GWTServerThread();
			t.start();
			return true;
		}
		return false;
	}
	
	public static boolean stop() {
		if (t!=null) {
			Logger.log("GWTServer - Requested to stop");
			t.stopFlag=true;
			t.interrupt();
			t=null;
			return true;
		}
		return false;
	}
}
