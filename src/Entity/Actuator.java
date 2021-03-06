package Entity;

import java.util.HashSet;
import java.util.Set;

public class Actuator implements java.io.Serializable, Comparable<Actuator> {
	private static final long serialVersionUID = -8250978371812671883L;
	private String name;
	private Controller controller;
	private String status;
	private Double positionx;
	private Double positiony;
	private String controltype;
	private String statuslist;
	private Set<Regularschedule> regularschedules = new HashSet<>(0);
	private Set<Sensoractuatorresponse> sensoractuatorresponses = new HashSet<>(0);
	private Set<Specialschedule> specialschedules = new HashSet<>(0);

	public Actuator() {
	}

	public Actuator(String name) {
		this.name = name;
	}

	public Actuator(String name, Controller controller, String status, Double positionx, Double positiony,
			String controltype, String statuslist, Set<Regularschedule> regularschedules, Set<Sensoractuatorresponse> sensoractuatorresponses, Set<Specialschedule> specialschedules) {
		this.name = name;
		this.controller = controller;
		this.status = status;
		this.positionx = positionx;
		this.positiony = positiony;
		this.controltype = controltype;
		this.statuslist = statuslist;
		this.regularschedules = regularschedules;
		this.sensoractuatorresponses = sensoractuatorresponses;
		this.specialschedules = specialschedules;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Controller getController() {
		return this.controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getPositionx() {
		return this.positionx;
	}

	public void setPositionx(Double positionx) {
		this.positionx = positionx;
	}

	public Double getPositiony() {
		return this.positiony;
	}

	public void setPositiony(Double positiony) {
		this.positiony = positiony;
	}

	public String getControltype() {
		return this.controltype;
	}

	public String getStatuslist() {
		return this.statuslist;
	}

	public void setStatuslist(String statuslist) {
		this.statuslist = statuslist;
	}
	
	public void setControltype(String controltype) {
		this.controltype = controltype;
	}

	public Set<Regularschedule> getRegularschedules() {
		return this.regularschedules;
	}

	public void setRegularschedules(Set<Regularschedule> regularschedules) {
		this.regularschedules = regularschedules;
	}

	public Set<Sensoractuatorresponse> getSensoractuatorresponses() {
		return this.sensoractuatorresponses;
	}

	public void setSensoractuatorresponses(Set<Sensoractuatorresponse> sensoractuatorresponses) {
		this.sensoractuatorresponses = sensoractuatorresponses;
	}

	public Set<Specialschedule> getSpecialschedules() {
		return this.specialschedules;
	}

	public void setSpecialschedules(Set<Specialschedule> specialschedules) {
		this.specialschedules = specialschedules;
	}

	public Object [] toObj () {
		return new Object [] {this.getName(), this.getController().getControllername(), this.getStatus(),this.positionx,this.positiony,this.controltype,this.statuslist};
	}
	
	public int compareTo(Actuator act) {
		return this.getName().compareTo(act.getName());
	}
}
