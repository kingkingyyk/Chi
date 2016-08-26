package Chi;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ControllerPacket {
	public static enum Type {Hello,SetActuator,ChangeName,ChangeTimeout};
	private Type type;
	private String [] data;
	
	public ControllerPacket(Type t, String [] d) {
		this.type=t;
		this.data=d;
	}
	
	public boolean send() {
		StringBuilder sb=new StringBuilder();
		sb.append(this.type.ordinal());
		sb.append(Config.PACKET_FIELD_DELIMITER);
		for (String s : data) {
			sb.append(s);
			sb.append(Config.PACKET_FIELD_DELIMITER);
		}
		String toSend=sb.toString();
		toSend=toSend.substring(0,toSend.length()-1);
		if (toSend.length()>=Config.PACKET_MAX_BYTE) {
			Logger.log("Controller Packet Error - Attempt to send larger than max byte size. Content : "+toSend);
		} else {
			try {
				InetAddress address=InetAddress.getByName(Config.getConfig(Config.CONFIG_SERVER_CONTROLLER_IP_KEY));
				byte [] toSendByte=toSend.getBytes();
				DatagramPacket packet=new DatagramPacket(toSendByte,toSendByte.length,address,Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_CONTROLLER_PORT_KEY)));
				DatagramSocket dsocket=new DatagramSocket();
				dsocket.send(packet);
				dsocket.close();
				return true;
			} catch (Exception e) {
				Logger.log("Controller Packet Error - Attempting to send packet. "+e.getMessage());
			}
		}
		return false;
	}
}
