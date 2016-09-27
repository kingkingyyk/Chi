package Chi;

public class ControllerPacketActuatorTrigger extends Thread {
	private String controllerName;
	private String actuatorName;
	private String status;
	
	public ControllerPacketActuatorTrigger(String cn, String an, String s) {
		this.controllerName=cn;
		this.actuatorName=an;
		this.status=s;
	}
	
	public void trigger() {
		Logger.log("ControllerPacketActuatorTrigger - Actuator trigger packet queued");
		this.start();
	}
	
	public void run() {
		String [] data={controllerName,actuatorName,status};
		ControllerPacket p=new ControllerPacket(Cache.Controllers.map.get(controllerName),ControllerPacket.Type.SetActuator,data);
		Logger.log("ControllerPacketActuatorTrigger - Send packet");
		if (p.send()) Logger.log("ControllerPacketActuatorTrigger - Packet sent successfully");

	}
}
