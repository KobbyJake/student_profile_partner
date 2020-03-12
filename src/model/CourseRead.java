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
@Table(name = "courseRead")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CourseRead.findAll", query = "SELECT c FROM CourseRead c"),
    @NamedQuery(name = "CourseRead.findById", query = "SELECT c FROM CourseRead c WHERE c.id = :id"),
    @NamedQuery(name = "CourseRead.findBySemester", query = "SELECT c FROM CourseRead c WHERE c.semester = :semester"),
    @NamedQuery(name = "CourseRead.findByCode", query = "SELECT c FROM CourseRead c WHERE c.code = :code"),
    @NamedQuery(name = "CourseRead.findByTitle", query = "SELECT c FROM CourseRead c WHERE c.title = :title"),
    @NamedQuery(name = "CourseRead.findByCredit", query = "SELECT c FROM CourseRead c WHERE c.credit = :credit"),
    @NamedQuery(name = "CourseRead.findByLecturer", query = "SELECT c FROM CourseRead c WHERE c.lecturer = :lecturer"),
    @NamedQuery(name = "CourseRead.findByGrade", query = "SELECT c FROM CourseRead c WHERE c.grade = :grade"),
    @NamedQuery(name = "CourseRead.findByStatus", query = "SELECT c FROM CourseRead c WHERE c.status = :status")})
public class CourseRead implements Serializable {

	@Column(name = "resitGrade")
	private String resitGrade;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @Column(name = "semester")
    private String semester;
    @Basic(optional = false)
    @Column(name = "code")
    private String code;
    @Column(name = "title")
    private String title;
    @Column(name = "credit")
    private Integer credit;
    @Column(name = "lecturer")
    private String lecturer;
    @Column(name = "grade")
    private String grade;
    @Column(name = "status")
    private String status;
    @JoinColumn(name = "student", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Student student;

    public CourseRead() {
    }

    public CourseRead(String id) {
        this.id = id;
    }

    public CourseRead(String id, String semester, String code) {
        this.id = id;
        this.semester = semester;
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
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

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CourseRead)) {
            return false;
        }
        CourseRead other = (CourseRead) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.CourseRead[ id=" + id + " ]";
    }

	public String getResitGrade() {
		return resitGrade;
	}

	public void setResitGrade(String resitGrade) {
		this.resitGrade = resitGrade;
	}

}
