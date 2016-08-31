package Chi;
import java.util.HashSet;
import java.util.Set;

public class Sensorclass implements java.io.Serializable {
	private static final long serialVersionUID = -2094242804548905932L;
	private String classname;
	private Set<Sensor> sensors = new HashSet<Sensor>(0);

	public Sensorclass() {
	}

	public Sensorclass(String classname) {
		this.classname = classname;
	}

	public Sensorclass(String classname, Set<Sensor> sensors) {
		this.classname = classname;
		this.sensors = sensors;
	}

	public String getClassname() {
		return this.classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public Set<Sensor> getSensors() {
		return this.sensors;
	}

	public void setSensors(Set<Sensor> sensors) {
		this.sensors = sensors;
	}

	public Object [] toObj () {
		return new Object [] {this.getClassname()};
	}
}
