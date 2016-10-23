package Chi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import Entity.Controller;

public class ControllerPacket {
	public static enum Type {Hello,SetActuator,GetReading,ChangeName,ChangeTimeout};
	public static ConcurrentHashMap<Controller,ConcurrentLinkedQueue<ControllerPacket>> packetQueue=new ConcurrentHashMap<>();
	private Controller ct;
	private Type type;
	private String [] data;
	
	public ControllerPacket(Controller ct, Type t, String [] d) {
		this.type=t;
		this.data=d;
		this.ct=ct;
		
		if (!packetQueue.containsKey(ct)) packetQueue.put(ct,new ConcurrentLinkedQueue<>());
	}
	
	public String send() {
		packetQueue.get(this.ct).offer(this);
		while (packetQueue.get(this.ct).peek()!=this) {
			try { Thread.sleep(500); } catch (InterruptedException e) {};
		}
		String status=null;
		int tryCount=0;
		while (packetQueue.get(this.ct).peek()==this && tryCount<Config.CONTROLLER_MAX_RETRY) {
			tryCount++;
			StringBuilder sb=new StringBuilder();
			//sb.append("!!!!!");
			sb.append(this.type.ordinal());
			sb.append(Config.PACKET_FIELD_DELIMITER);
			for (String s : data) {
				sb.append(s);
				sb.append(Config.PACKET_FIELD_DELIMITER);
			}
			Socket sc=null; DataInputStream dis=null; DataOutputStream pw=null;
			try {
				sc=new Socket(ct.getIpaddress(),Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_CONTROLLER_PORT_KEY)));
				Logger.log(Logger.LEVEL_INFO,"Controller Packet Info - Send content : "+sb.toString());
				while (sb.length()<=Config.PACKET_MAX_BYTE) sb.append('!');
				
				dis=new DataInputStream(sc.getInputStream());
				pw=new DataOutputStream(sc.getOutputStream());
				
				sc.setSoTimeout(5000);
				
				pw.write(sb.toString().getBytes());
				byte [] data=new byte [100];
				dis.read(data);
				sb=new StringBuilder();
				for (int i=0;i<data.length;i++) if (data[i]!=0) sb.append((char)data[i]); else break;
				status=sb.toString();
				if (!status.equals("ON") && !status.equals("OFF")) status=null;
				Logger.log(Logger.LEVEL_INFO,"Controller Packet Info - Received content : "+status);

				packetQueue.get(this.ct).poll();
				try { Thread.sleep(Config.CONTROLLER_READY_TIME_MS); } catch (InterruptedException e) {}
			} catch (Exception e) {
				Logger.log(Logger.LEVEL_ERROR,"Controller Packet Error - Attempting to send packet. "+e.getMessage());
				status=null;
			} finally {
				try {
					if (pw!=null) pw.close();
					if (dis!=null) dis.close();
					if (sc!=null) sc.close();
				} catch (Exception zz) {};
			}
		}
		return status;
	}
}
