package Entity;

import java.util.Date;

import Chi.Utility;

public class Usersensornotification implements java.io.Serializable {
	private static final long serialVersionUID = -2987423081288875871L;
	private UsersensornotificationId id;
	private Date lastread;

	public Usersensornotification() {
	}

	public Usersensornotification(UsersensornotificationId id) {
		this.id = id;
	}

	public Usersensornotification(UsersensornotificationId id, Date lastread) {
		this.id = id;
		this.lastread = lastread;
	}

	public UsersensornotificationId getId() {
		return this.id;
	}

	public void setId(UsersensornotificationId id) {
		this.id = id;
	}

	public Date getLastread() {
		return this.lastread;
	}

	public void setLastread(Date lastread) {
		this.lastread = lastread;
	}
	
	public Object[] toObj () {
		return new Object[] {this.id.getUser().getUsername(),this.id.getSensor().getSensorname(),Utility.dateToLocalDateTime(this.lastread)};
	}

}
