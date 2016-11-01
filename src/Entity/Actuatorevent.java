package Entity;
// Generated Nov 1, 2016 5:10:37 PM by Hibernate Tools 5.2.0.Beta1

import java.util.Date;

public class Actuatorevent implements java.io.Serializable {
	private static final long serialVersionUID = -4945434828046244936L;
	private Long id;
	private Actuator actuator;
	private Date timestp;
	private String eventtype;
	private String eventvalue;

	public Actuatorevent() {
	}

	public Actuatorevent(Actuator actuator, Date timestp, String eventtype, String eventvalue) {
		this.actuator = actuator;
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

	public Actuator getActuator() {
		return this.actuator;
	}

	public void setActuator(Actuator actuator) {
		this.actuator = actuator;
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
