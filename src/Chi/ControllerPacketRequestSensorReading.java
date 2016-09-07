package Chi;

public class ControllerPacketRequestSensorReading {
	private ControllerPacket p;
	
	public ControllerPacketRequestSensorReading(String cn, String sn) {
		this.p=new ControllerPacket(ControllerPacket.Type.GetReading,new String [] {cn,sn});
	}
	
	public boolean send() {
		return this.p.send();
	}

}
