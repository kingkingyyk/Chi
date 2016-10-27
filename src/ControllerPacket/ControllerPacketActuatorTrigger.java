package ControllerPacket;

import Chi.Logger;
import Database.Cache;
import Database.DatabaseActuator;
import FrameEntityManagement.FrameActuatorManagementFeedbackWait;

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
		Logger.log(Logger.LEVEL_INFO,"ControllerPacketActuatorTrigger - Actuator trigger packet queued");
		this.start();
	}
	
	public void run() {
		String [] data={controllerName,actuatorName,status};
		ControllerPacket p=new ControllerPacket(Cache.Controllers.map.get(controllerName),ControllerPacket.Type.SetActuator,data);
		Logger.log(Logger.LEVEL_INFO,"ControllerPacketActuatorTrigger - Send packet");
		String status;
		if ((status=p.send())!=null) {
			Logger.log(Logger.LEVEL_INFO,"ControllerPacketActuatorTrigger - Packet sent successfully");
			DatabaseActuator.updateActuatorStatus(actuatorName,status);
		}
		if (FrameActuatorManagementFeedbackWait.getCurrent()!=null && FrameActuatorManagementFeedbackWait.getCurrent().isVisible()) {
			FrameActuatorManagementFeedbackWait.getCurrent().setVisible(false);
		}
	}
}
