package Chi;

public class Sensoractuatorresponseline implements java.io.Serializable {
	private static final long serialVersionUID = 5075965462599303705L;
	private SensoractuatorresponselineId id;
	private Sensor sensor;
	private Sensoractuatorresponse sensoractuatorresponse;
	private String triggervalue;
	private String triggerequality;

	public Sensoractuatorresponseline() {
	}

	public Sensoractuatorresponseline(SensoractuatorresponselineId id, Sensor sensor,
			Sensoractuatorresponse sensoractuatorresponse) {
		this.id = id;
		this.sensor = sensor;
		this.sensoractuatorresponse = sensoractuatorresponse;
	}

	public Sensoractuatorresponseline(SensoractuatorresponselineId id, Sensor sensor,
			Sensoractuatorresponse sensoractuatorresponse, String triggervalue, String triggerequality) {
		this.id = id;
		this.sensor = sensor;
		this.sensoractuatorresponse = sensoractuatorresponse;
		this.triggervalue = triggervalue;
		this.triggerequality = triggerequality;
	}

	public SensoractuatorresponselineId getId() {
		return this.id;
	}

	public void setId(SensoractuatorresponselineId id) {
		this.id = id;
	}

	public Sensor getSensor() {
		return this.sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	public Sensoractuatorresponse getSensoractuatorresponse() {
		return this.sensoractuatorresponse;
	}

	public void setSensoractuatorresponse(Sensoractuatorresponse sensoractuatorresponse) {
		this.sensoractuatorresponse = sensoractuatorresponse;
	}

	public String getTriggervalue() {
		return this.triggervalue;
	}

	public void setTriggervalue(String triggervalue) {
		this.triggervalue = triggervalue;
	}

	public String getTriggerequality() {
		return this.triggerequality;
	}

	public void setTriggerequality(String triggerequality) {
		this.triggerequality = triggerequality;
	}

}
