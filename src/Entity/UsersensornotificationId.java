package Entity;
// Generated Nov 1, 2016 3:26:38 PM by Hibernate Tools 5.2.0.Beta1

/**
 * UsersensornotificationId generated by hbm2java
 */
public class UsersensornotificationId implements java.io.Serializable {
	private static final long serialVersionUID = -3212106832150989692L;
	private String username;
	private String sensorname;

	public UsersensornotificationId() {
	}

	public UsersensornotificationId(String username, String sensorname) {
		this.username = username;
		this.sensorname = sensorname;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSensorname() {
		return this.sensorname;
	}

	public void setSensorname(String sensorname) {
		this.sensorname = sensorname;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof UsersensornotificationId))
			return false;
		UsersensornotificationId castOther = (UsersensornotificationId) other;

		return ((this.getUsername() == castOther.getUsername()) || (this.getUsername() != null
				&& castOther.getUsername() != null && this.getUsername().equals(castOther.getUsername())))
				&& ((this.getSensorname() == castOther.getSensorname())
						|| (this.getSensorname() != null && castOther.getSensorname() != null
								&& this.getSensorname().equals(castOther.getSensorname())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getUsername() == null ? 0 : this.getUsername().hashCode());
		result = 37 * result + (getSensorname() == null ? 0 : this.getSensorname().hashCode());
		return result;
	}

}
