package Chi;

import java.util.HashSet;
import java.util.Set;

public class Dayschedulerule implements java.io.Serializable {
	private static final long serialVersionUID = 3621702214681822732L;
	private String rulename;
	private Integer starthour;
	private Integer startminute;
	private Integer endhour;
	private Integer endminute;
	private Set<Regularschedule> regularschedules = new HashSet<Regularschedule>(0);
	private Set<Specialschedule> specialschedules = new HashSet<Specialschedule>(0);

	public Dayschedulerule() {
	}

	public Dayschedulerule(String rulename) {
		this.rulename = rulename;
	}

	public Dayschedulerule(String rulename, Integer starthour, Integer startminute, Integer endhour, Integer endminute,
			Set<Regularschedule> regularschedules, Set<Specialschedule> specialschedules) {
		this.rulename = rulename;
		this.starthour = starthour;
		this.startminute = startminute;
		this.endhour = endhour;
		this.endminute = endminute;
		this.regularschedules = regularschedules;
		this.specialschedules = specialschedules;
	}

	public String getRulename() {
		return this.rulename;
	}

	public void setRulename(String rulename) {
		this.rulename = rulename;
	}

	public Integer getStarthour() {
		return this.starthour;
	}

	public void setStarthour(Integer starthour) {
		this.starthour = starthour;
	}

	public Integer getStartminute() {
		return this.startminute;
	}

	public void setStartminute(Integer startminute) {
		this.startminute = startminute;
	}

	public Integer getEndhour() {
		return this.endhour;
	}

	public void setEndhour(Integer endhour) {
		this.endhour = endhour;
	}

	public Integer getEndminute() {
		return this.endminute;
	}

	public void setEndminute(Integer endminute) {
		this.endminute = endminute;
	}

	public Set<Regularschedule> getRegularschedules() {
		return this.regularschedules;
	}

	public void setRegularschedules(Set<Regularschedule> regularschedules) {
		this.regularschedules = regularschedules;
	}

	public Set<Specialschedule> getSpecialschedules() {
		return this.specialschedules;
	}

	public void setSpecialschedules(Set<Specialschedule> specialschedules) {
		this.specialschedules = specialschedules;
	}

}
