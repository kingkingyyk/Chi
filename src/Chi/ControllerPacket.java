package Chi;

import java.io.DataOutputStream;
import java.net.Socket;

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
		sb.append("!!!!!");
		sb.append(this.type.ordinal());
		sb.append(Config.PACKET_FIELD_DELIMITER);
		for (String s : data) {
			sb.append(s);
			sb.append(Config.PACKET_FIELD_DELIMITER);
		}
		try {
			Socket sc=new Socket(ct.getIpaddress(),Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_CONTROLLER_PORT_KEY)));
			Logger.log("Controller Packet Info - Send content : "+sb.toString());
			while (sb.length()<=Config.PACKET_MAX_BYTE) sb.append('!');
			sc.setSoTimeout(5000);
			DataOutputStream pw=new DataOutputStream(sc.getOutputStream());
			pw.write(sb.toString().getBytes());
			pw.close();
			sc.close();
			return true;
		} catch (Exception e) {
			Logger.log("Controller Packet Error - Attempting to send packet. "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
}
