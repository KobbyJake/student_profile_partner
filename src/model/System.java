
package model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "System")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "System.findAll", query = "SELECT s FROM System s"),
	@NamedQuery(name = "System.findById", query = "SELECT s FROM System s WHERE s.id = :id"),
	@NamedQuery(name = "System.findByName", query = "SELECT s FROM System s WHERE s.name = :name"),
	@NamedQuery(name = "System.findByFile", query = "SELECT s FROM System s WHERE s.file = :file"),
	@NamedQuery(name = "System.findByDescription", query = "SELECT s FROM System s WHERE s.description = :description")})
public class System implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
        @Basic(optional = false)
        @Column(name = "id")
	private String id;
	@Column(name = "name")
	private String name;
	@Lob
	@Column(name = "file")
	private byte[] file;
	@Column(name = "description")
	private String description;

	public System() {
	}

	public System(String id) {
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

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
		if (!(object instanceof System)) {
			return false;
		}
		System other = (System) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "model.System[ id=" + id + " ]";
	}
	
}
