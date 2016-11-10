package Entity;

public class UseractuatornotificationId implements java.io.Serializable {
	private static final long serialVersionUID = -4277534132638917737L;
	private User user;
	private Actuator actuator;

	public UseractuatornotificationId() {
	}

	public UseractuatornotificationId(User user, Actuator actuator) {
		this.user = user;
		this.actuator = actuator;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Actuator getActuator() {
		return this.actuator;
	}

	public void setActuator(Actuator actuator) {
		this.actuator = actuator;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof UseractuatornotificationId))
			return false;
		UseractuatornotificationId castOther = (UseractuatornotificationId) other;

		return ((this.getUser() == castOther.getUser()) || (this.getUser() != null && castOther.getUser() != null
				&& this.getUser().equals(castOther.getUser())))
				&& ((this.getActuator() == castOther.getActuator()) || (this.getActuator() != null
						&& castOther.getActuator() != null && this.getActuator().equals(castOther.getActuator())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getUser() == null ? 0 : this.getUser().hashCode());
		result = 37 * result + (getActuator() == null ? 0 : this.getActuator().hashCode());
		return result;
	}

}
