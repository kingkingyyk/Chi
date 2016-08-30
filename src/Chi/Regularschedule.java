package Chi;


public class Regularschedule implements java.io.Serializable {
	private static final long serialVersionUID = -3385893166395992584L;
	private String schedulename;
	private Actuator actuator;
	private Dayschedulerule dayschedulerule;
	private Integer daymask;
	private Boolean actuatoron;
	private Integer priority;
	private Boolean enabled;

	public Regularschedule() {
	}

	public Regularschedule(String schedulename) {
		this.schedulename = schedulename;
	}

	public Regularschedule(String schedulename, Actuator actuator, Dayschedulerule dayschedulerule, Integer daymask,
			Boolean actuatoron, Integer priority, Boolean enabled) {
		this.schedulename = schedulename;
		this.actuator = actuator;
		this.dayschedulerule = dayschedulerule;
		this.daymask = daymask;
		this.actuatoron = actuatoron;
		this.priority = priority;
		this.enabled = enabled;
	}

	public String getSchedulename() {
		return this.schedulename;
	}

	public void setSchedulename(String schedulename) {
		this.schedulename = schedulename;
	}

	public Actuator getActuator() {
		return this.actuator;
	}

	public void setActuator(Actuator actuator) {
		this.actuator = actuator;
	}

	public Dayschedulerule getDayschedulerule() {
		return this.dayschedulerule;
	}

	public void setDayschedulerule(Dayschedulerule dayschedulerule) {
		this.dayschedulerule = dayschedulerule;
	}

	public Integer getDaymask() {
		return this.daymask;
	}

	public void setDaymask(Integer daymask) {
		this.daymask = daymask;
	}

	public Boolean getActuatoron() {
		return this.actuatoron;
	}

	public void setActuatoron(Boolean actuatoron) {
		this.actuatoron = actuatoron;
	}

	public Integer getPriority() {
		return this.priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Boolean getEnabled() {
		return this.enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

}
