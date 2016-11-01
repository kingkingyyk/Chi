package Entity;
// Generated Nov 1, 2016 5:03:19 PM by Hibernate Tools 5.2.0.Beta1

import java.util.Date;

public class Usercontrollernotification implements java.io.Serializable {
	private static final long serialVersionUID = -3452029375705216990L;
	private UsercontrollernotificationId id;
	private Controller controller;
	private User user;
	private Date lastread;

	public Usercontrollernotification() {
	}

	public Usercontrollernotification(UsercontrollernotificationId id, Controller controller, User user) {
		this.id = id;
		this.controller = controller;
		this.user = user;
	}

	public Usercontrollernotification(UsercontrollernotificationId id, Controller controller, User user,
			Date lastread) {
		this.id = id;
		this.controller = controller;
		this.user = user;
		this.lastread = lastread;
	}

	public UsercontrollernotificationId getId() {
		return this.id;
	}

	public void setId(UsercontrollernotificationId id) {
		this.id = id;
	}

	public Controller getController() {
		return this.controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
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
