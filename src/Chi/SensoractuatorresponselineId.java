package Chi;
// Generated Oct 10, 2016 8:50:49 PM by Hibernate Tools 5.2.0.Beta1

/**
 * SensoractuatorresponselineId generated by hbm2java
 */
public class SensoractuatorresponselineId implements java.io.Serializable {

	private int id;
	private String sensorname;

	public SensoractuatorresponselineId() {
	}

	public SensoractuatorresponselineId(int id, String sensorname) {
		this.id = id;
		this.sensorname = sensorname;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSensorname() {
		return this.sensorname;
	}

	public void setSensorname(String sensorname) {
		this.sensorname = sensorname;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SensoractuatorresponselineId))
			return false;
		SensoractuatorresponselineId castOther = (SensoractuatorresponselineId) other;

		return (this.getId() == castOther.getId()) && ((this.getSensorname() == castOther.getSensorname())
				|| (this.getSensorname() != null && castOther.getSensorname() != null
						&& this.getSensorname().equals(castOther.getSensorname())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getId();
		result = 37 * result + (getSensorname() == null ? 0 : this.getSensorname().hashCode());
		return result;
	}

}
