package Entity;
// Generated Nov 1, 2016 3:26:38 PM by Hibernate Tools 5.2.0.Beta1

import java.util.Date;

public class Usersensornotification implements java.io.Serializable {
	private static final long serialVersionUID = -4934809444455293121L;
	private UsersensornotificationId id;
	private Sensor sensor;
	private User user;
	private Date lastread;

	public Usersensornotification() {
	}

	public Usersensornotification(UsersensornotificationId id, Sensor sensor, User user) {
		this.id = id;
		this.sensor = sensor;
		this.user = user;
	}

	public Usersensornotification(UsersensornotificationId id, Sensor sensor, User user, Date lastread) {
		this.id = id;
		this.sensor = sensor;
		this.user = user;
		this.lastread = lastread;
	}

	public UsersensornotificationId getId() {
		return this.id;
	}

	public void setId(UsersensornotificationId id) {
		this.id = id;
	}

	public Sensor getSensor() {
		return this.sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getLastread() {
		return this.lastread;
	}

	public void setLastread(Date lastread) {
		this.lastread = lastread;
	}

}
