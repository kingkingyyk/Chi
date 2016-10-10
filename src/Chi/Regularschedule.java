package Chi;


public class Regularschedule implements java.io.Serializable {
	private static final long serialVersionUID = -3385893166395992584L;
	private String schedulename;
	private Actuator actuator;
	private Dayschedulerule dayschedulerule;
	private Integer daymask;
	private String onstartaction;
	private String onendaction;
	private Boolean lockmanual;
	private Integer priority;
	private Boolean enabled;

	public Regularschedule() {
	}

	public Regularschedule(String schedulename) {
		this.schedulename = schedulename;
	}

	public Regularschedule(String schedulename, Actuator actuator, Dayschedulerule dayschedulerule, Integer daymask,
			String onstartaction, String onendaction, Boolean lockmanual, Integer priority, Boolean enabled) {
		this.schedulename = schedulename;
		this.actuator = actuator;
		this.dayschedulerule = dayschedulerule;
		this.daymask = daymask;
		this.onstartaction = onstartaction;
		this.onendaction = onendaction;
		this.lockmanual = lockmanual;
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


	public String getOnstartaction() {
		return this.onstartaction;
	}

	public void setOnstartaction(String onstartaction) {
		this.onstartaction = onstartaction;
	}

	public String getOnendaction() {
		return this.onendaction;
	}

	public void setOnendaction(String onendaction) {
		this.onendaction = onendaction;
	}

	public Boolean getLockmanual() {
		return this.lockmanual;
	}

	public void setLockmanual(Boolean lockmanual) {
		this.lockmanual = lockmanual;
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
							  this.getDaymask(), this.getDayschedulerule().getRulename(),
							  this.getOnstartaction(), this.getOnendaction(), this.getLockmanual(),
							  this.getPriority(),this.getEnabled()};
	}
}
