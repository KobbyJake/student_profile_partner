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
@Table(name = "gradingScheme")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "GradingScheme.findAll", query = "SELECT g FROM GradingScheme g"),
	@NamedQuery(name = "GradingScheme.findByName", query = "SELECT g FROM GradingScheme g WHERE g.name = :name"),
	@NamedQuery(name = "GradingScheme.findByPoint", query = "SELECT g FROM GradingScheme g WHERE g.point = :point")})
public class GradingScheme implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
        @Basic(optional = false)
        @Column(name = "name")
	private String name;
	@Column(name = "point")
	private Double point;

	public GradingScheme() {
	}

	public GradingScheme(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPoint() {
		return point;
	}

	public void setPoint(Double point) {
		this.point = point;
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
		if (!(object instanceof GradingScheme)) {
			return false;
		}
		GradingScheme other = (GradingScheme) object;
		if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "model.GradingScheme[ name=" + name + " ]";
	}
	
}
