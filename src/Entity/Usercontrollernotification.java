package Entity;
import java.util.Date;

import Chi.Utility;

public class Usercontrollernotification implements java.io.Serializable {
	private static final long serialVersionUID = 8375986962682022001L;
	private UsercontrollernotificationId id;
	private Date lastread;

	public Usercontrollernotification() {
	}

	public Usercontrollernotification(UsercontrollernotificationId id) {
		this.id = id;
	}

	public Usercontrollernotification(UsercontrollernotificationId id, Date lastread) {
		this.id = id;
		this.lastread = lastread;
	}

	public UsercontrollernotificationId getId() {
		return this.id;
	}

	public void setId(UsercontrollernotificationId id) {
		this.id = id;
	}

	public Date getLastread() {
		return this.lastread;
	}

	public void setLastread(Date lastread) {
		this.lastread = lastread;
	}

	public Object[] toObj () {
		return new Object[] {this.id.getUser().getUsername(),this.id.getController().getControllername(),Utility.dateToLocalDateTime(this.lastread)};
	}
}
