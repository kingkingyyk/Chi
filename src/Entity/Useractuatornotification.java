package Entity;
// Generated Nov 1, 2016 3:26:38 PM by Hibernate Tools 5.2.0.Beta1

import java.util.Date;

public class Useractuatornotification implements java.io.Serializable {
	private static final long serialVersionUID = -5302387381126012623L;
	private UseractuatornotificationId id;
	private Actuator actuator;
	private User user;
	private Date lastread;

	public Useractuatornotification() {
	}

	public Useractuatornotification(UseractuatornotificationId id, Actuator actuator, User user) {
		this.id = id;
		this.actuator = actuator;
		this.user = user;
	}

	public Useractuatornotification(UseractuatornotificationId id, Actuator actuator, User user, Date lastread) {
		this.id = id;
		this.actuator = actuator;
		this.user = user;
		this.lastread = lastread;
	}

	public UseractuatornotificationId getId() {
		return this.id;
	}

	public void setId(UseractuatornotificationId id) {
		this.id = id;
	}

	public Actuator getActuator() {
		return this.actuator;
	}

	public void setActuator(Actuator actuator) {
		this.actuator = actuator;
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
