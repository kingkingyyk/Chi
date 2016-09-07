package Chi;

import java.util.Date;

public class SensoreventId implements java.io.Serializable {
	private static final long serialVersionUID = 7889778697909349157L;
	private String sensorname;
	private Date timestp;

	public SensoreventId() {
	}

	public SensoreventId(String sensorname, Date timestp) {
		this.sensorname = sensorname;
		this.timestp = timestp;
	}

	public String getSensorname() {
		return this.sensorname;
	}

	public void setSensorname(String sensorname) {
		this.sensorname = sensorname;
	}

	public Date getTimestp() {
		return this.timestp;
	}

	public void setTimestp(Date timestp) {
		this.timestp = timestp;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SensoreventId))
			return false;
		SensoreventId castOther = (SensoreventId) other;

		return ((this.getSensorname() == castOther.getSensorname()) || (this.getSensorname() != null
				&& castOther.getSensorname() != null && this.getSensorname().equals(castOther.getSensorname())))
				&& ((this.getTimestp() == castOther.getTimestp()) || (this.getTimestp() != null
						&& castOther.getTimestp() != null && this.getTimestp().equals(castOther.getTimestp())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getSensorname() == null ? 0 : this.getSensorname().hashCode());
		result = 37 * result + (getTimestp() == null ? 0 : this.getTimestp().hashCode());
		return result;
	}

}
