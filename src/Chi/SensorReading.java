package Chi;

import java.time.LocalDateTime;

public class SensorReading implements Comparable<SensorReading> {
	private String sensorName;
	private LocalDateTime timeStp;
	private double value;
	private double actualValue;
	
	public SensorReading (String s, LocalDateTime dt, double v, double av) {
		this.sensorName=s;
		this.timeStp=dt;
		this.value=v;
		this.actualValue=av;
	}
	
	public String getSensorName() {
		return this.sensorName;
	}
	
	public LocalDateTime getTimestamp() {
		return this.timeStp;
	}
	
	public double getValue() {
		return this.value;
	}
	
	public double getActualValue() {
		return this.actualValue;
	}

	@Override
	public int compareTo(SensorReading arg0) {
		if (!this.sensorName.equals(arg0)) {
			return this.timeStp.compareTo(arg0.timeStp);
		}
		return this.sensorName.compareTo(arg0.sensorName);
	}
}
