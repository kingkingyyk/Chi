package Chi;

public class ControllerPacketRequestAlive {
	private ControllerPacket p;
	
	public ControllerPacketRequestAlive(String n) {
		this.p=new ControllerPacket(ControllerPacket.Type.Hello,new String [] {n});
	}
	
	public boolean send() {
		return this.p.send();
	}
}