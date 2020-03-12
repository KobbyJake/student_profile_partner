package model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "programme")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Programme.findAll", query = "SELECT p FROM Programme p"),
	@NamedQuery(name = "Programme.findByName", query = "SELECT p FROM Programme p WHERE p.name = :name"),
	@NamedQuery(name = "Programme.findByCampus", query = "SELECT p FROM Programme p WHERE p.campus = :campus"),
	@NamedQuery(name = "Programme.findByDuration", query = "SELECT p FROM Programme p WHERE p.duration = :duration")})
public class Programme implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
        @Basic(optional = false)
        @Column(name = "name")
	private String name;
	@Column(name = "campus")
	private String campus;
	@Column(name = "duration")
	private String duration;
	@OneToMany(mappedBy = "program")
	private Collection<Student> studentCollection;

	public Programme() {
	}

	public Programme(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCampus() {
		return campus;
	}

	public void setCampus(String campus) {
		this.campus = campus;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	@XmlTransient
	public Collection<Student> getStudentCollection() {
		return studentCollection;
	}

	public void setStudentCollection(Collection<Student> studentCollection) {
		this.studentCollection = studentCollection;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (name != null ? name.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Programme)) {
			return false;
		}
		Programme other = (Programme) object;
		if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "model.Programme[ name=" + name + " ]";
	}
	
}
