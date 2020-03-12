package model;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import static view.Alpha.sfm;

@Entity
@Table(name = "event")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Event.findAll", query = "SELECT e FROM Event e"),
    @NamedQuery(name = "Event.findById", query = "SELECT e FROM Event e WHERE e.id = :id"),
    @NamedQuery(name = "Event.findByType", query = "SELECT e FROM Event e WHERE e.type = :type"),
    @NamedQuery(name = "Event.findByTitle", query = "SELECT e FROM Event e WHERE e.title = :title"),
    @NamedQuery(name = "Event.findByAmount", query = "SELECT e FROM Event e WHERE e.amount = :amount"),
    @NamedQuery(name = "Event.findByDate", query = "SELECT e FROM Event e WHERE e.date = :date"),
    @NamedQuery(name = "Event.findByTime", query = "SELECT e FROM Event e WHERE e.time = :time"),
    @NamedQuery(name = "Event.findByVenue", query = "SELECT e FROM Event e WHERE e.venue = :venue"),
    @NamedQuery(name = "Event.findByNote", query = "SELECT e FROM Event e WHERE e.note = :note")})
public class Event implements Serializable {
         

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Column(name = "type")
    private String type;
    @Column(name = "title")
    private String title;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "amount")
    private Double amount;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Column(name = "time")
    private String time;
    @Column(name = "venue")
    private String venue;
    @Column(name = "note")
    private String note;
    @JoinColumn(name = "semesterID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Semester semesterID;

    public Event() {
    }

    public Event(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getDate() throws ParseException {

        return sfm.parse(sfm.format(date));
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
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
        if (!(object instanceof Event)) {
            return false;
        }
        Event other = (Event) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Event[ id=" + id + " ]";
    }

    }
