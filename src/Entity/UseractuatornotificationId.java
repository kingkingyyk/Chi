package Entity;
// Generated Nov 1, 2016 3:26:38 PM by Hibernate Tools 5.2.0.Beta1

public class UseractuatornotificationId implements java.io.Serializable {
	private static final long serialVersionUID = -7590278073255366909L;
	private String username;
	private String actuatorname;

	public UseractuatornotificationId() {
	}

	public UseractuatornotificationId(String username, String actuatorname) {
		this.username = username;
		this.actuatorname = actuatorname;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getActuatorname() {
		return this.actuatorname;
	}

	public void setActuatorname(String actuatorname) {
		this.actuatorname = actuatorname;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof UseractuatornotificationId))
			return false;
		UseractuatornotificationId castOther = (UseractuatornotificationId) other;

		return ((this.getUsername() == castOther.getUsername()) || (this.getUsername() != null
				&& castOther.getUsername() != null && this.getUsername().equals(castOther.getUsername())))
				&& ((this.getActuatorname() == castOther.getActuatorname())
						|| (this.getActuatorname() != null && castOther.getActuatorname() != null
								&& this.getActuatorname().equals(castOther.getActuatorname())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getUsername() == null ? 0 : this.getUsername().hashCode());
		result = 37 * result + (getActuatorname() == null ? 0 : this.getActuatorname().hashCode());
		return result;
	}

}
