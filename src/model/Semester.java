package model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "semester")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Semester.findAll", query = "SELECT s FROM Semester s"),
	@NamedQuery(name = "Semester.findById", query = "SELECT s FROM Semester s WHERE s.id = :id"),
	@NamedQuery(name = "Semester.findByName", query = "SELECT s FROM Semester s WHERE s.name = :name"),
	@NamedQuery(name = "Semester.findByNumberOfCourses", query = "SELECT s FROM Semester s WHERE s.numberOfCourses = :numberOfCourses"),
	@NamedQuery(name = "Semester.findByTotalCredit", query = "SELECT s FROM Semester s WHERE s.totalCredit = :totalCredit"),
	@NamedQuery(name = "Semester.findByGpa", query = "SELECT s FROM Semester s WHERE s.gpa = :gpa"),
	@NamedQuery(name = "Semester.findByCgpa", query = "SELECT s FROM Semester s WHERE s.cgpa = :cgpa"),
	@NamedQuery(name = "Semester.findByStartDate", query = "SELECT s FROM Semester s WHERE s.startDate = :startDate"),
	@NamedQuery(name = "Semester.findByEndDate", query = "SELECT s FROM Semester s WHERE s.endDate = :endDate")})
public class Semester implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
        @Basic(optional = false)
        @Column(name = "id")
	private String id;
	@Column(name = "name")
	private String name;
	@Column(name = "numberOfCourses")
	private Integer numberOfCourses;
	@Column(name = "totalCredit")
	private Integer totalCredit;
	// @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
	@Column(name = "gpa")
	private Double gpa;
	@Column(name = "cgpa")
	private Double cgpa;
	@Column(name = "startDate")
	private String startDate;
	@Column(name = "endDate")
	private String endDate;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "semesterID")
	private Collection<Schedule> scheduleCollection;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "semesterID")
	private Collection<Document> documentCollection;
	@JoinColumn(name = "studentID", referencedColumnName = "id")
        @ManyToOne(optional = false)
	private Student studentID;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "semesterID")
	private Collection<Event> eventCollection;

	public Semester() {
	}

	public Semester(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNumberOfCourses() {
		return numberOfCourses;
	}

	public void setNumberOfCourses(Integer numberOfCourses) {
		this.numberOfCourses = numberOfCourses;
	}

	public Integer getTotalCredit() {
		return totalCredit;
	}

	public void setTotalCredit(Integer totalCredit) {
		this.totalCredit = totalCredit;
	}

	public Double getGpa() {
		return gpa;
	}

	public void setGpa(Double gpa) {
		this.gpa = gpa;
	}

	public Double getCgpa() {
		return cgpa;
	}

	public void setCgpa(Double cgpa) {
		this.cgpa = cgpa;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@XmlTransient
	public Collection<Schedule> getScheduleCollection() {
		return scheduleCollection;
	}

	public void setScheduleCollection(Collection<Schedule> scheduleCollection) {
		this.scheduleCollection = scheduleCollection;
	}

	@XmlTransient
	public Collection<Document> getDocumentCollection() {
		return documentCollection;
	}

	public void setDocumentCollection(Collection<Document> documentCollection) {
		this.documentCollection = documentCollection;
	}

	public Student getStudentID() {
		return studentID;
	}

	public void setStudentID(Student studentID) {
		this.studentID = studentID;
	}

	@XmlTransient
	public Collection<Event> getEventCollection() {
		return eventCollection;
	}

	public void setEventCollection(Collection<Event> eventCollection) {
		this.eventCollection = eventCollection;
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
		if (!(object instanceof Semester)) {
			return false;
		}
		Semester other = (Semester) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "model.Semester[ id=" + id + " ]";
	}
	
}
