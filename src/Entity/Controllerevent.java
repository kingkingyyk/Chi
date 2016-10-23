package Entity;

import java.util.Date;

public class Controllerevent implements java.io.Serializable {
	private static final long serialVersionUID = -5562597974086520955L;
	private Long id;
	private Controller controller;
	private Date timestp;
	private String eventtype;
	private String eventvalue;

	public Controllerevent() {
	}

	public Controllerevent(Controller controller, Date timestp, String eventtype, String eventvalue) {
		this.controller = controller;
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

	public Controller getController() {
		return this.controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
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
