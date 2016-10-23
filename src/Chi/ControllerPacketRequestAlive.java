package Chi;

import Database.Cache;

public class ControllerPacketRequestAlive {
	private ControllerPacket p;
	
	public ControllerPacketRequestAlive(String n) {
		this.p=new ControllerPacket(Cache.Controllers.map.get(n),ControllerPacket.Type.Hello,new String [] {n});
	}
	
	public boolean send() {
		return this.p.send()!=null;
	}
}
