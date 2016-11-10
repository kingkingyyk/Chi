package Entity;

public class UsercontrollernotificationId implements java.io.Serializable {
	private static final long serialVersionUID = -7961158088610733388L;
	private User user;
	private Controller controller;

	public UsercontrollernotificationId() {
	}

	public UsercontrollernotificationId(User user, Controller controller) {
		this.user = user;
		this.controller = controller;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Controller getController() {
		return this.controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof UsercontrollernotificationId))
			return false;
		UsercontrollernotificationId castOther = (UsercontrollernotificationId) other;

		return ((this.getUser() == castOther.getUser()) || (this.getUser() != null && castOther.getUser() != null
				&& this.getUser().equals(castOther.getUser())))
				&& ((this.getController() == castOther.getController())
						|| (this.getController() != null && castOther.getController() != null
								&& this.getController().equals(castOther.getController())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getUser() == null ? 0 : this.getUser().hashCode());
		result = 37 * result + (getController() == null ? 0 : this.getController().hashCode());
		return result;
	}

}
