package model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "schedule")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Schedule.findAll", query = "SELECT s FROM Schedule s"),
	@NamedQuery(name = "Schedule.findById", query = "SELECT s FROM Schedule s WHERE s.id = :id"),
	@NamedQuery(name = "Schedule.findByDay", query = "SELECT s FROM Schedule s WHERE s.day = :day"),
	@NamedQuery(name = "Schedule.findByNight", query = "SELECT s FROM Schedule s WHERE s.night = :night"),
	@NamedQuery(name = "Schedule.findByEvening", query = "SELECT s FROM Schedule s WHERE s.evening = :evening"),
	@NamedQuery(name = "Schedule.findByAfternoon", query = "SELECT s FROM Schedule s WHERE s.afternoon = :afternoon"),
	@NamedQuery(name = "Schedule.findByMidDay", query = "SELECT s FROM Schedule s WHERE s.midDay = :midDay"),
	@NamedQuery(name = "Schedule.findByMorning", query = "SELECT s FROM Schedule s WHERE s.morning = :morning"),
	@NamedQuery(name = "Schedule.findByDawn", query = "SELECT s FROM Schedule s WHERE s.dawn = :dawn")})
public class Schedule implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
        @Basic(optional = false)
        @Column(name = "id")
	private Integer id;
	@Basic(optional = false)
        @Column(name = "Day")
	private String day;
	@Column(name = "Night")
	private String night;
	@Column(name = "Evening")
	private String evening;
	@Column(name = "Afternoon")
	private String afternoon;
	@Column(name = "MidDay")
	private String midDay;
	@Column(name = "Morning")
	private String morning;
	@Column(name = "Dawn")
	private String dawn;
	@JoinColumn(name = "semesterID", referencedColumnName = "id")
        @ManyToOne(optional = false)
	private Semester semesterID;

	public Schedule() {
	}

	public Schedule(Integer id) {
		this.id = id;
	}

	public Schedule(Integer id, String day) {
		this.id = id;
		this.day = day;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getNight() {
		return night;
	}

	public void setNight(String night) {
		this.night = night;
	}

	public String getEvening() {
		return evening;
	}

	public void setEvening(String evening) {
		this.evening = evening;
	}

	public String getAfternoon() {
		return afternoon;
	}

	public void setAfternoon(String afternoon) {
		this.afternoon = afternoon;
	}

	public String getMidDay() {
		return midDay;
	}

	public void setMidDay(String midDay) {
		this.midDay = midDay;
	}

	public String getMorning() {
		return morning;
	}

	public void setMorning(String morning) {
		this.morning = morning;
	}

	public String getDawn() {
		return dawn;
	}

	public void setDawn(String dawn) {
		this.dawn = dawn;
	}

	public Semester getSemesterID() {
		return semesterID;
	}

	public void setSemesterID(Semester semesterID) {
		this.semesterID = semesterID;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Schedule)) {
			return false;
		}
		Schedule other = (Schedule) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "model.Schedule[ id=" + id + " ]";
	}
	
}
