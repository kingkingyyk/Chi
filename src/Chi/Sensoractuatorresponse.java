package Chi;

import java.util.HashSet;
import java.util.Set;

public class Sensoractuatorresponse implements java.io.Serializable {
	private static final long serialVersionUID = -3304571642140500719L;
	private Integer id;
	private Actuator actuator;
	private String ontriggeraction;
	private String expression;
	private Boolean enabled;
	private Set<Sensoractuatorresponseline> sensoractuatorresponselines = new HashSet<>(0);

	public Sensoractuatorresponse() {
	}

	public Sensoractuatorresponse(Actuator actuator, String ontriggeraction, String expression, Boolean enabled,
			Set<Sensoractuatorresponseline> sensoractuatorresponselines) {
		this.actuator = actuator;
		this.ontriggeraction = ontriggeraction;
		this.expression = expression;
		this.enabled = enabled;
		this.sensoractuatorresponselines = sensoractuatorresponselines;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Actuator getActuator() {
		return this.actuator;
	}

	public void setActuator(Actuator actuator) {
		this.actuator = actuator;
	}

	public String getOntriggeraction() {
		return this.ontriggeraction;
	}

	public void setOntriggeraction(String ontriggeraction) {
		this.ontriggeraction = ontriggeraction;
	}

	public String getExpression() {
		return this.expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public Boolean getEnabled() {
		return this.enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Sensoractuatorresponseline> getSensoractuatorresponselines() {
		return this.sensoractuatorresponselines;
	}

	public void setSensoractuatorresponselines(Set<Sensoractuatorresponseline> sensoractuatorresponselines) {
		this.sensoractuatorresponselines = sensoractuatorresponselines;
	}

}
