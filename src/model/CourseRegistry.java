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
@Table(name = "courseRegistry")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CourseRegistry.findAll", query = "SELECT c FROM CourseRegistry c"),
    @NamedQuery(name = "CourseRegistry.findByCode", query = "SELECT c FROM CourseRegistry c WHERE c.code = :code"),
    @NamedQuery(name = "CourseRegistry.findByTitle", query = "SELECT c FROM CourseRegistry c WHERE c.title = :title"),
    @NamedQuery(name = "CourseRegistry.findByCredit", query = "SELECT c FROM CourseRegistry c WHERE c.credit = :credit")})
public class CourseRegistry implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "code")
    private String code;
    @Column(name = "title")
    private String title;
    @Column(name = "credit")
    private Integer credit;

    public CourseRegistry() {
    }

    public CourseRegistry(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (code != null ? code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CourseRegistry)) {
            return false;
        }
        CourseRegistry other = (CourseRegistry) object;
        if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.CourseRegistry[ code=" + code + " ]";
    }
    
}
