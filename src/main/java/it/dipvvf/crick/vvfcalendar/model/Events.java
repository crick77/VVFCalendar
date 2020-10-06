/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.dipvvf.crick.vvfcalendar.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Crick
 */
@Entity
@Table(name = "events.events")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Events.findAll", query = "SELECT e FROM Events e")})
public class Events implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "event_name")
    private String eventName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "starts_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startsAt;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "start_tz")
    private String startTz;
    @Column(name = "ends_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endsAt;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "end_tz")
    private String endTz;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "recurrence")
    private String recurrence;
    @Size(max = 2147483647)
    @Column(name = "owner")
    private String owner;
    @Basic(optional = false)
    @NotNull
    @Column(name = "holiday_skip")
    private boolean holidaySkip;
    @Basic(optional = false)
    @NotNull
    @Column(name = "version")
    @Version
    private int version;

    public Events() {
    }

    public Events(Integer id) {
        this.id = id;
    }

    public Events(Integer id, String eventName, Date startsAt, String startTz, String endTz, String recurrence, boolean holidaySkip) {
        this.id = id;
        this.eventName = eventName;
        this.startsAt = startsAt;
        this.startTz = startTz;
        this.endTz = endTz;
        this.recurrence = recurrence;
        this.holidaySkip = holidaySkip;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Date getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(Date startsAt) {
        this.startsAt = startsAt;
    }

    public String getStartTz() {
        return startTz;
    }

    public void setStartTz(String startTz) {
        this.startTz = startTz;
    }

    public Date getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(Date endsAt) {
        this.endsAt = endsAt;
    }

    public String getEndTz() {
        return endTz;
    }

    public void setEndTz(String endTz) {
        this.endTz = endTz;
    }

    public String getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(String recurrence) {
        this.recurrence = recurrence;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean getHolidaySkip() {
        return holidaySkip;
    }

    public void setHolidaySkip(boolean holidaySkip) {
        this.holidaySkip = holidaySkip;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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
        if (!(object instanceof Events)) {
            return false;
        }
        Events other = (Events) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.dipvvf.crick.vvfcalendar.model.Events[ id=" + id + " ]";
    }
    
}
