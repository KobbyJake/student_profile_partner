package model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "document")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Document.findAll", query = "SELECT d FROM Document d"),
    @NamedQuery(name = "Document.findById", query = "SELECT d FROM Document d WHERE d.id = :id"),
    @NamedQuery(name = "Document.findByTitle", query = "SELECT d FROM Document d WHERE d.title = :title"),
    @NamedQuery(name = "Document.findByReference", query = "SELECT d FROM Document d WHERE d.reference = :reference"),
    @NamedQuery(name = "Document.findByFile", query = "SELECT d FROM Document d WHERE d.file = :file"),
    @NamedQuery(name = "Document.findByNote", query = "SELECT d FROM Document d WHERE d.note = :note")})
public class Document implements Serializable {
    @Column(name = "fileName")
    private String fileName;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Column(name = "title")
    private String title;
    @Column(name = "reference")
    private String reference;
    @Lob
    @Column(name = "file")
    private byte[] file;
    @Column(name = "note")
    private String note;
    @JoinColumn(name = "semesterID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Semester semesterID;

    public Document() {
    }

    public Document(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
        if (!(object instanceof Document)) {
            return false;
        }
        Document other = (Document) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Document[ id=" + id + " ]";
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
}
