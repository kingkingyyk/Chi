package Chi;


public class Specialschedule implements java.io.Serializable {
	private static final long serialVersionUID = 1369555311752202152L;
	private String schedulename;
	private Actuator actuator;
	private Dayschedulerule dayschedulerule;
	private Integer year;
	private Integer month;
	private Integer day;
	private Boolean actuatoron;
	private Integer priority;
	private Boolean enabled;

	public Specialschedule() {
	}

	public Specialschedule(String schedulename) {
		this.schedulename = schedulename;
	}

	public Specialschedule(String schedulename, Actuator actuator, Dayschedulerule dayschedulerule, Integer year,
			Integer month, Integer day, Boolean actuatoron, Integer priority, Boolean enabled) {
		this.schedulename = schedulename;
		this.actuator = actuator;
		this.dayschedulerule = dayschedulerule;
		this.year = year;
		this.month = month;
		this.day = day;
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

	public Integer getYear() {
		return this.year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return this.month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getDay() {
		return this.day;
	}

	public void setDay(Integer day) {
		this.day = day;
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

	public Object [] toObj () {
		return new Object [] {this.getSchedulename(), this.getActuator().getName(),
							  this.getYear(),this.getMonth(),this.getDay(),
							  this.getDayschedulerule().getRulename(),this.getActuatoron(),
							  this.getPriority(),this.getEnabled()};
	}
}
