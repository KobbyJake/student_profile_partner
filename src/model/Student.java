package model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "student")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Student.findAll", query = "SELECT s FROM Student s"),
	@NamedQuery(name = "Student.findById", query = "SELECT s FROM Student s WHERE s.id = :id"),
	@NamedQuery(name = "Student.findByUserName", query = "SELECT s FROM Student s WHERE s.userName = :userName"),
	@NamedQuery(name = "Student.findByPassword", query = "SELECT s FROM Student s WHERE s.password = :password"),
	@NamedQuery(name = "Student.findByFistName", query = "SELECT s FROM Student s WHERE s.fistName = :fistName"),
	@NamedQuery(name = "Student.findByLastName", query = "SELECT s FROM Student s WHERE s.lastName = :lastName"),
	@NamedQuery(name = "Student.findByIndexNumber", query = "SELECT s FROM Student s WHERE s.indexNumber = :indexNumber"),
	@NamedQuery(name = "Student.findByTelephone", query = "SELECT s FROM Student s WHERE s.telephone = :telephone"),
	@NamedQuery(name = "Student.findByEmail", query = "SELECT s FROM Student s WHERE s.email = :email"),
	@NamedQuery(name = "Student.findByHall", query = "SELECT s FROM Student s WHERE s.hall = :hall"),
	@NamedQuery(name = "Student.findByCurrentSem", query = "SELECT s FROM Student s WHERE s.currentSem = :currentSem"),
	@NamedQuery(name = "Student.findByOutputDir", query = "SELECT s FROM Student s WHERE s.outputDir = :outputDir"),
	@NamedQuery(name = "Student.findByPhoto", query = "SELECT s FROM Student s WHERE s.photo = :photo"),
	@NamedQuery(name = "Student.findByComment", query = "SELECT s FROM Student s WHERE s.comment = :comment"),
	@NamedQuery(name = "Student.findByLastBackup", query = "SELECT s FROM Student s WHERE s.lastBackup = :lastBackup"),
	@NamedQuery(name = "Student.findByLastRetore", query = "SELECT s FROM Student s WHERE s.lastRetore = :lastRetore")})
public class Student implements Serializable {
    @Lob
    @Column(name = "photo")
    private byte[] photo;
    @Column(name = "audio")
    private String audio;

	private static final long serialVersionUID = 1L;
	@Id
        @Basic(optional = false)
        @Column(name = "id")
	private String id;
	@Basic(optional = false)
        @Column(name = "userName")
	private String userName;
	@Basic(optional = false)
        @Column(name = "password")
	private String password;
	@Column(name = "fistName")
	private String fistName;
	@Column(name = "lastName")
	private String lastName;
	@Column(name = "indexNumber")
	private String indexNumber;
	@Column(name = "telephone")
	private String telephone;
	@Column(name = "email")
	private String email;
	@Column(name = "hall")
	private String hall;
	@Column(name = "currentSem")
	private String currentSem;
	@Column(name = "outputDir")
	private String outputDir;
	@Column(name = "comment")
	private String comment;
	@Column(name = "lastBackup")
	private String lastBackup;
	@Column(name = "lastRetore")
	private String lastRetore;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "email")
	private Collection<PasswordResets> passwordResetsCollection;
	@JoinColumn(name = "program", referencedColumnName = "name")
        @ManyToOne
	private Programme program;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "student")
	private Collection<CourseRead> courseReadCollection;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "studentID")
	private Collection<Semester> semesterCollection;

	public Student() {
	}

	public Student(String id) {
		this.id = id;
	}

	public Student(String id, String userName, String password) {
		this.id = id;
		this.userName = userName;
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFistName() {
		return fistName;
	}

	public void setFistName(String fistName) {
		this.fistName = fistName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getIndexNumber() {
		return indexNumber;
	}

	public void setIndexNumber(String indexNumber) {
		this.indexNumber = indexNumber;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHall() {
		return hall;
	}

	public void setHall(String hall) {
		this.hall = hall;
	}

	public String getCurrentSem() {
		return currentSem;
	}

	public void setCurrentSem(String currentSem) {
		this.currentSem = currentSem;
	}

	public String getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}


	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getLastBackup() {
		return lastBackup;
	}

	public void setLastBackup(String lastBackup) {
		this.lastBackup = lastBackup;
	}

	public String getLastRetore() {
		return lastRetore;
	}

	public void setLastRetore(String lastRetore) {
		this.lastRetore = lastRetore;
	}

	@XmlTransient
	public Collection<PasswordResets> getPasswordResetsCollection() {
		return passwordResetsCollection;
	}

	public void setPasswordResetsCollection(Collection<PasswordResets> passwordResetsCollection) {
		this.passwordResetsCollection = passwordResetsCollection;
	}

	public Programme getProgram() {
		return program;
	}

	public void setProgram(Programme program) {
		this.program = program;
	}

	@XmlTransient
	public Collection<CourseRead> getCourseReadCollection() {
		return courseReadCollection;
	}

	public void setCourseReadCollection(Collection<CourseRead> courseReadCollection) {
		this.courseReadCollection = courseReadCollection;
	}

	@XmlTransient
	public Collection<Semester> getSemesterCollection() {
		return semesterCollection;
	}

	public void setSemesterCollection(Collection<Semester> semesterCollection) {
		this.semesterCollection = semesterCollection;
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
		if (!(object instanceof Student)) {
			return false;
		}
		Student other = (Student) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "model.Student[ id=" + id + " ]";
	}

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }
	
}
