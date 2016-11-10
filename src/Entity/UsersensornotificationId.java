package Entity;

public class UsersensornotificationId implements java.io.Serializable {
	private static final long serialVersionUID = 1532585572049050257L;
	private User user;
	private Sensor sensor;

	public UsersensornotificationId() {
	}

	public UsersensornotificationId(User user, Sensor sensor) {
		this.user = user;
		this.sensor = sensor;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Sensor getSensor() {
		return this.sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof UsersensornotificationId))
			return false;
		UsersensornotificationId castOther = (UsersensornotificationId) other;

		return ((this.getUser() == castOther.getUser()) || (this.getUser() != null && castOther.getUser() != null
				&& this.getUser().equals(castOther.getUser())))
				&& ((this.getSensor() == castOther.getSensor()) || (this.getSensor() != null
						&& castOther.getSensor() != null && this.getSensor().equals(castOther.getSensor())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getUser() == null ? 0 : this.getUser().hashCode());
		result = 37 * result + (getSensor() == null ? 0 : this.getSensor().hashCode());
		return result;
	}

}
