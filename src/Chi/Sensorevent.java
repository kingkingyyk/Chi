package Chi;

public class Sensorevent implements java.io.Serializable {
	private static final long serialVersionUID = 2441565123914032264L;
	private SensoreventId id;
	private Sensor sensor;
	private String eventtype;
	private String eventvalue;

	public Sensorevent() {
	}

	public Sensorevent(SensoreventId id, Sensor sensor) {
		this.id = id;
		this.sensor = sensor;
	}

	public Sensorevent(SensoreventId id, Sensor sensor, String eventtype, String eventvalue) {
		this.id = id;
		this.sensor = sensor;
		this.eventtype = eventtype;
		this.eventvalue = eventvalue;
	}

	public SensoreventId getId() {
		return this.id;
	}

	public void setId(SensoreventId id) {
		this.id = id;
	}

	public Sensor getSensor() {
		return this.sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	public String getEventtype() {
		return this.eventtype;
	}

	public void setEventtype(String eventtype) {
		this.eventtype = eventtype;
	}

	public String getEventvalue() {
		return this.eventvalue;
	}

	public void setEventvalue(String eventvalue) {
		this.eventvalue = eventvalue;
	}

}
