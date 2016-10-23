package Entity;
// Generated Sep 7, 2016 8:37:47 PM by Hibernate Tools 5.1.0.Alpha1

import java.util.HashSet;
import java.util.Set;

public class Sensor implements java.io.Serializable {
	private static final long serialVersionUID = 738139606256186433L;
	private String sensorname;
	private Controller controller;
	private Sensorclass sensorclass;
	private Double minvalue;
	private Double maxvalue;
	private Double transformationfactor;
	private String unit;
	private Double minthreshold;
	private Double maxthreshold;
	private Double positionx;
	private Double positiony;
	private Set<Sensorevent> sensorevents = new HashSet<Sensorevent>(0);

	public Sensor() {
	}

	public Sensor(String sensorname) {
		this.sensorname = sensorname;
	}

	public Sensor(String sensorname, Controller controller, Sensorclass sensorclass, Double minvalue, Double maxvalue,
			Double transformationfactor, String unit, Double minthreshold, Double maxthreshold, Double positionx,
			Double positiony, Set<Sensorevent> sensorevents) {
		this.sensorname = sensorname;
		this.controller = controller;
		this.sensorclass = sensorclass;
		this.minvalue = minvalue;
		this.maxvalue = maxvalue;
		this.transformationfactor = transformationfactor;
		this.unit = unit;
		this.minthreshold = minthreshold;
		this.maxthreshold = maxthreshold;
		this.positionx = positionx;
		this.positiony = positiony;
		this.sensorevents = sensorevents;
	}

	public String getSensorname() {
		return this.sensorname;
	}

	public void setSensorname(String sensorname) {
		this.sensorname = sensorname;
	}

	public Controller getController() {
		return this.controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public Sensorclass getSensorclass() {
		return this.sensorclass;
	}

	public void setSensorclass(Sensorclass sensorclass) {
		this.sensorclass = sensorclass;
	}

	public Double getMinvalue() {
		return this.minvalue;
	}

	public void setMinvalue(Double minvalue) {
		this.minvalue = minvalue;
	}

	public Double getMaxvalue() {
		return this.maxvalue;
	}

	public void setMaxvalue(Double maxvalue) {
		this.maxvalue = maxvalue;
	}

	public Double getTransformationfactor() {
		return this.transformationfactor;
	}

	public void setTransformationfactor(Double transformationfactor) {
		this.transformationfactor = transformationfactor;
	}

	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Double getMinthreshold() {
		return this.minthreshold;
	}

	public void setMinthreshold(Double minthreshold) {
		this.minthreshold = minthreshold;
	}

	public Double getMaxthreshold() {
		return this.maxthreshold;
	}

	public void setMaxthreshold(Double maxthreshold) {
		this.maxthreshold = maxthreshold;
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
	
	public Set<Sensorevent> getSensorevents() {
		return this.sensorevents;
	}

	public void setSensorevents(Set<Sensorevent> sensorevents) {
		this.sensorevents = sensorevents;
	}
	
	public double denormalizeValue(double d) {
		return (d*(getMaxvalue()-getMinvalue()))+getMinvalue();
	}
	
	public Object [] toObj () {
		return new Object [] {this.getSensorname(), this.getSensorclass().getClassname(),
							  this.getMinvalue(), this.getMaxvalue(), this.getTransformationfactor(),
							  this.getUnit(), this.getController().getControllername(),this.getMinthreshold(),
							  this.getMaxthreshold(),this.getPositionx(),this.getPositiony()};
	}
}
