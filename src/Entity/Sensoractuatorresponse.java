package Entity;

public class Sensoractuatorresponse implements java.io.Serializable {
	private static final long serialVersionUID = 5413831594834406684L;
	private Integer id;
	private Actuator actuator;
	private String ontriggeraction;
	private String onnottriggeraction;
	private String expression;
	private Boolean enabled;
	private Integer timeout;

	public Sensoractuatorresponse() {
	}

	public Sensoractuatorresponse(Actuator actuator, String ontriggeraction, String onnottriggeraction,
			String expression, Boolean enabled, Integer timeout) {
		this.actuator = actuator;
		this.ontriggeraction = ontriggeraction;
		this.onnottriggeraction = onnottriggeraction;
		this.expression = expression;
		this.enabled = enabled;
		this.timeout = timeout;
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

	public String getOnnottriggeraction() {
		return this.onnottriggeraction;
	}

	public void setOnnottriggeraction(String onnottriggeraction) {
		this.onnottriggeraction = onnottriggeraction;
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

	public Integer getTimeout() {
		return this.timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

}
