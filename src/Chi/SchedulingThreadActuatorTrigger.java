package Chi;

public class SchedulingThreadActuatorTrigger extends Thread {
	private String controllerName;
	private String actuatorName;
	private String status;
	
	public SchedulingThreadActuatorTrigger(String cn, String an, String s) {
		this.controllerName=cn;
		this.actuatorName=an;
		this.status=s;
	}
	
	public void trigger() {
		Logger.log("SchedulingThreadActuatorTrigger - Actuator trigger packet queued");
		this.run();
	}
	
	public void run() {
		String [] data={controllerName,actuatorName,status};
		ControllerPacket p=new ControllerPacket(ControllerPacket.Type.SetActuator,data);
		Logger.log("SchedulingThreadActuatorTrigger - Send packet");
		if (p.send()) Logger.log("SchedulingThreadActuatorTrigger - Packet sent successfully");

	}
}
