package Entity;
import java.util.Date;

public class User implements java.io.Serializable {
	private static final long serialVersionUID = -5467220434792658970L;
	public static String [] USER_STATUS={"ACTIVATED","PENDING APPROVAL","DEACTIVATED"};
	private String username;
	private String password;
	private Integer level;
	private String status;
	private Date dateadded;

	public User() {
	}

	public User(String username) {
		this.username = username;
	}

	public User(String username, String password, Integer level, String status, Date dateadded) {
		this.username = username;
		this.password = password;
		this.level = level;
		this.status = status;
		this.dateadded = dateadded;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getLevel() {
		return this.level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDateadded() {
		return this.dateadded;
	}

	public void setDateadded(Date dateadded) {
		this.dateadded = dateadded;
	}

}
