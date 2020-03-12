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
@Table(name = "passwordResets")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "PasswordResets.findAll", query = "SELECT p FROM PasswordResets p"),
	@NamedQuery(name = "PasswordResets.findByToken", query = "SELECT p FROM PasswordResets p WHERE p.token = :token"),
	@NamedQuery(name = "PasswordResets.findByCreatedAt", query = "SELECT p FROM PasswordResets p WHERE p.createdAt = :createdAt"),
	@NamedQuery(name = "PasswordResets.findByUpdatedAt", query = "SELECT p FROM PasswordResets p WHERE p.updatedAt = :updatedAt")})
public class PasswordResets implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
        @Basic(optional = false)
        @Column(name = "token")
	private String token;
	// @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
	@Column(name = "createdAt")
	private Double createdAt;
	@Column(name = "updatedAt")
	private Double updatedAt;
	@JoinColumn(name = "email", referencedColumnName = "email")
        @ManyToOne(optional = false)
	private Student email;

	public PasswordResets() {
	}

	public PasswordResets(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Double getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Double createdAt) {
		this.createdAt = createdAt;
	}

	public Double getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Double updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Student getEmail() {
		return email;
	}

	public void setEmail(Student email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (token != null ? token.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof PasswordResets)) {
			return false;
		}
		PasswordResets other = (PasswordResets) object;
		if ((this.token == null && other.token != null) || (this.token != null && !this.token.equals(other.token))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "model.PasswordResets[ token=" + token + " ]";
	}
	
}
