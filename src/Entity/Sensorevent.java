package Entity;

import java.util.Date;

public class Sensorevent implements java.io.Serializable {
	private static final long serialVersionUID = 1369866640258113608L;
	private Long id;
	private Sensor sensor;
	private Date timestp;
	private String eventtype;
	private String eventvalue;

	public Sensorevent() {
	}

	public Sensorevent(Sensor sensor, Date timestp, String eventtype, String eventvalue) {
		this.sensor = sensor;
		this.timestp = timestp;
		this.eventtype = eventtype;
		this.eventvalue = eventvalue;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Sensor getSensor() {
		return this.sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	public Date getTimestp() {
		return this.timestp;
	}

	public void setTimestp(Date timestp) {
		this.timestp = timestp;
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
