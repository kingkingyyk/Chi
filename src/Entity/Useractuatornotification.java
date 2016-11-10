package Entity;
// Generated Nov 9, 2016 5:05:17 PM by Hibernate Tools 5.2.0.Beta1

import java.util.Date;

import Chi.Utility;

public class Useractuatornotification implements java.io.Serializable {
	private static final long serialVersionUID = 3548118196456020786L;
	private UseractuatornotificationId id;
	private Date lastread;

	public Useractuatornotification() {
	}

	public Useractuatornotification(UseractuatornotificationId id) {
		this.id = id;
	}

	public Useractuatornotification(UseractuatornotificationId id, Date lastread) {
		this.id = id;
		this.lastread = lastread;
	}

	public UseractuatornotificationId getId() {
		return this.id;
	}

	public void setId(UseractuatornotificationId id) {
		this.id = id;
	}

	public Date getLastread() {
		return this.lastread;
	}

	public void setLastread(Date lastread) {
		this.lastread = lastread;
	}

	public Object[] toObj () {
		return new Object[] {this.id.getUser().getUsername(),this.id.getActuator().getName(),Utility.dateToLocalDateTime(this.lastread)};
	}
}
