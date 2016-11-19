package ServerActuatorResponseServer;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import Chi.Logger;
import ControllerPacket.ControllerPacketActuatorTrigger;
import DataServer.DataServer;
import Database.Cache;
import Database.DatabaseActuator;
import Database.DatabaseEvent;
import Database.DatabaseSensorActuatorResponse;
import Entity.Sensoractuatorresponse;

public class SensorActuatorResponseServer {
	private static boolean isStarted=false;
	private static OnReadingReceived ReceivedCallback=new OnReadingReceived();
	private static OnCreate CreateCallback=new OnCreate();
	private static OnUpdate UpdateCallback=new OnUpdate();
	private static OnDelete DeleteCallback=new OnDelete();
	private static OnActuatorUpdate UpdateActCallback=new OnActuatorUpdate();
	private static HashMap<Sensoractuatorresponse,ReadingTrackData> dataMap=new HashMap<>();
	
	private static class OnReadingReceived implements DataServer.OnReadingReceived {
		@Override
		public void run(String sensorName, double value) {
			for (ReadingTrackData dat : dataMap.values()) {
				dat.checkAndExecute();
			}
		}
	}
	
	private static class OnCreate implements DatabaseSensorActuatorResponse.OnCreateAction {
		@Override
		public void run(int id, String an, String onTrigAct, String onNotTrigAct, String expression, boolean en,
				int timeout) {
			if (en && Cache.Actuators.map.get(an).getControltype().equals("Sensor Response")) {
				Sensoractuatorresponse res=Cache.SensorActuatorResponses.map.get(String.valueOf(id));
				ReadingTrackData dat=new ReadingTrackData(res);
				dataMap.put(res,dat);
				dat.checkAndExecute();
			}
		}
	}
	
	private static class OnUpdate implements DatabaseSensorActuatorResponse.OnUpdateAction {
		@Override
		public void run(int id, String an, String onTrigAct, String onNotTrigAct, String expression, boolean en,
				int timeout) {
			Sensoractuatorresponse res=Cache.SensorActuatorResponses.map.get(String.valueOf(id));
			if (en && !dataMap.containsKey(res) &&  Cache.Actuators.map.get(an).getControltype().equals("Sensor Response")) {
				ReadingTrackData dat=new ReadingTrackData(res);
				dataMap.put(res,dat);
				dat.checkAndExecute();
			} else if (!en && dataMap.containsKey(res)) {
				dataMap.get(res).cleanUp();
				dataMap.remove(res);
			}
		}
	}
	
	private static class OnDelete implements DatabaseSensorActuatorResponse.OnDeleteAction {
		@Override
		public void run(int id) {
			Sensoractuatorresponse res=Cache.SensorActuatorResponses.map.get(String.valueOf(id));
			if (dataMap.containsKey(res)) {
				dataMap.get(res).cleanUp();
				dataMap.remove(res);
			}
		}
	}
	
	public static class OnActuatorUpdate implements DatabaseActuator.OnUpdateAction {

		@Override
		public void run(String oldN, String n, String u, String slist, double px, double py, String ctrlType) {
			if (ctrlType.equals("Sensor Response")) {
				for (Sensoractuatorresponse res : Cache.SensorActuatorResponses.map.values()) {
					if (!dataMap.containsKey(res) && res.getActuator().getName().equals(n) && res.getEnabled()) {
						ReadingTrackData dat=new ReadingTrackData(res);
						dataMap.put(res,dat);
						dat.checkAndExecute();
					}
				}
			} else {
				ReadingTrackData toRemove=null;
				for (ReadingTrackData dat : dataMap.values()) {
					if (dat.res.getActuator().getName().equals(n)) {
						toRemove=dat;
						break;
					}
				}
				
				if (toRemove!=null) {
					toRemove.cleanUp();
					dataMap.remove(toRemove.res);
				}
			}
		}
		
	}
	
	private static class ReadingTrackData {
		Sensoractuatorresponse res;
		Timer t;
		boolean ready;
		String lastStatus;
		String lastResult;
		
		private static class ResetReady extends TimerTask {
			ReadingTrackData dat;
			
			@Override
			public void run() {
				dat.ready=true;
				dat.t=null;
			}
			
		}
		
		public ReadingTrackData (Sensoractuatorresponse s) {
			res=s;
			ready=true;
			lastStatus=s.getActuator().getStatus();
			lastResult="";
		}
		
		private void updateTimer() {
			t=new Timer();
			ResetReady tt=new ResetReady();
			tt.dat=this;
			t.schedule(tt,res.getTimeout()*1000);
		}
		
		public void checkAndExecute() {
			if (ready) {
				ready=false;
				updateTimer();
				boolean currResult=false;
				boolean parseFail=false;
				try { currResult=SensoractuatorresponseEvaluator.evaluateStatement(res.getExpression()); } catch (Exception e) {parseFail=true;}
				if (!parseFail) {
					if (!String.valueOf(currResult).equals(lastResult)) {
						if (currResult && !res.getActuator().getStatus().equals(res.getOntriggeraction())) {
							Logger.log(Logger.LEVEL_INFO,"SensorActuatorResponseServer - Attempt to set "+res.getActuator().getName()+" to "+res.getOntriggeraction());
							DatabaseEvent.logActuatorEvent(res.getActuator().getName(),"","SensorActuatorResponse server attempt set actuator "+
															 res.getActuator().getName()+" to "+res.getOntriggeraction()+" from expression : "+res.getExpression());
							lastStatus=res.getActuator().getStatus();
							ControllerPacketActuatorTrigger p=new ControllerPacketActuatorTrigger(res.getActuator().getController().getControllername(),res.getActuator().getName(),res.getOntriggeraction());
							p.trigger();
						} else if (!currResult) {
							if (res.getOnnottriggeraction().equals("RESTORE")) {
								Logger.log(Logger.LEVEL_INFO,"SensorActuatorResponseServer - Attempt to set "+res.getActuator().getName()+" to "+lastStatus);
								DatabaseEvent.logActuatorEvent(res.getActuator().getName(),"","SensorActuatorResponse server attempt set actuator "+
										 res.getActuator().getName()+" to "+lastStatus+" from expression : "+res.getExpression());
								ControllerPacketActuatorTrigger p=new ControllerPacketActuatorTrigger(res.getActuator().getController().getControllername(),res.getActuator().getName(),lastStatus);
								p.trigger();
							} else {
								Logger.log(Logger.LEVEL_INFO,"SensorActuatorResponseServer - Attempt to set "+res.getActuator().getName()+" to "+res.getOnnottriggeraction());
								DatabaseEvent.logActuatorEvent(res.getActuator().getName(),"","SensorActuatorResponse server attempt set actuator "+
										 res.getActuator().getName()+" to "+res.getOnnottriggeraction()+" from expression : "+res.getExpression());
								ControllerPacketActuatorTrigger p=new ControllerPacketActuatorTrigger(res.getActuator().getController().getControllername(),res.getActuator().getName(),res.getOnnottriggeraction());
								p.trigger();
							}
						}
						lastResult=String.valueOf(currResult);
					}
				} else {
					DatabaseSensorActuatorResponse.updateSensorActuatorResponse(res.getId(),res.getActuator().getName(),
																				res.getOntriggeraction(), res.getOnnottriggeraction(),
																				res.getExpression(), false, res.getTimeout());
				}
			}
		}
		
		public void cleanUp() {
			if (t!=null) {
				t.cancel();
				t=null;
			}
		}
	}
	
	public static void start() {
		Logger.log(Logger.LEVEL_INFO,"SensorActuatorResponseServer started.");
		isStarted=true;
		
		for (Sensoractuatorresponse res : Cache.SensorActuatorResponses.map.values()) {
			if (res.getEnabled() && res.getActuator().getControltype().equals("Sensor Response")) {
				ReadingTrackData dat=new ReadingTrackData(res);
				dataMap.put(res,dat);
				dat.checkAndExecute();
			}
		}
		
		DataServer.registerOnReadingReceived(ReceivedCallback);
		DatabaseSensorActuatorResponse.registerOnCreateAction(CreateCallback);
		DatabaseSensorActuatorResponse.registerOnUpdateAction(UpdateCallback);
		DatabaseSensorActuatorResponse.registerOnDeleteAction(DeleteCallback);
		DatabaseActuator.registerOnUpdateAction(UpdateActCallback);
		
	}
	
	public static void stop() {
		Logger.log(Logger.LEVEL_INFO,"SensorActuatorResponseServer stopped.");
		isStarted=false;
		
		DataServer.unregisterOnReadingReceived(ReceivedCallback);
		DatabaseSensorActuatorResponse.unregisterOnCreateAction(CreateCallback);
		DatabaseSensorActuatorResponse.unregisterOnUpdateAction(UpdateCallback);
		DatabaseSensorActuatorResponse.unregisterOnDeleteAction(DeleteCallback);
		DatabaseActuator.unregisterOnUpdateAction(UpdateActCallback);
		
		for (ReadingTrackData dat : dataMap.values()) {
			dat.cleanUp();
			dataMap.remove(dat.res);
		}
		
	}
	
	public static boolean isStarted() {
		return isStarted;
	}
}
