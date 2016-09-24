package Chi;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ControllerPacket {
	public static enum Type {Hello,SetActuator,GetReading,ChangeName,ChangeTimeout};
	private Controller ct;
	private Type type;
	private String [] data;
	
	public ControllerPacket(Controller ct, Type t, String [] d) {
		this.type=t;
		this.data=d;
		this.ct=ct;
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
		if (toSend.length()>=Config.PACKET_MAX_BYTE) {
			Logger.log("Controller Packet Error - Attempt to send larger than max byte size. Content : "+toSend);
		} else {
			try {
				InetAddress address=InetAddress.getByName(ct.getIpaddress());
				byte [] toSendByte=toSend.getBytes();
				DatagramPacket packet=new DatagramPacket(toSendByte,toSendByte.length,address,Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_CONTROLLER_PORT_KEY)));
				DatagramSocket dsocket=new DatagramSocket();
				dsocket.send(packet);
				dsocket.close();
				Logger.log("Controller Packet Info - Sent content "+toSend);
				return true;
			} catch (Exception e) {
				Logger.log("Controller Packet Error - Attempting to send packet. "+e.getMessage());
			}
		}
		return false;
	}
}
