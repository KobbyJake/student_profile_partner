package model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "honors")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Honors.findAll", query = "SELECT h FROM Honors h"),
	@NamedQuery(name = "Honors.findById", query = "SELECT h FROM Honors h WHERE h.id = :id"),
	@NamedQuery(name = "Honors.findByFloor", query = "SELECT h FROM Honors h WHERE h.floor = :floor"),
	@NamedQuery(name = "Honors.findByCeiling", query = "SELECT h FROM Honors h WHERE h.ceiling = :ceiling"),
	@NamedQuery(name = "Honors.findByQualification", query = "SELECT h FROM Honors h WHERE h.qualification = :qualification")})
public class Honors implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
        @Basic(optional = false)
        @Column(name = "id")
	private String id;
	@Column(name = "floor")
	private Double floor;
	@Column(name = "ceiling")
	private Double ceiling;
	@Column(name = "qualification")
	private String qualification;

	public Honors() {
	}

	public Honors(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getFloor() {
		return floor;
	}

	public void setFloor(Double floor) {
		this.floor = floor;
	}

	public Double getCeiling() {
		return ceiling;
	}

	public void setCeiling(Double ceiling) {
		this.ceiling = ceiling;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
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
		if (!(object instanceof Honors)) {
			return false;
		}
		Honors other = (Honors) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "model.Honors[ id=" + id + " ]";
	}
	
}
