package assignment1.eventplan.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by zz on 2016/8/26.
 */

@Table(name = "EventEntity")
public class EventEntity {
    @Column(name = "id", isId = true)
    private int id;
    @Column(name = "title")
    private String title;
    @Column(name = "startDate")
    private String startDate;
    @Column(name = "startTime")
    private String startTime;
    @Column(name = "endDate")
    private String endDate;
    @Column(name = "endTime")
    private String endTime;
    @Column(name = "veune")
    private String veune;
    @Column(name = "locationX")
    private String locationX;
    @Column(name = "locationY")
    private String locationY;
    @Column(name = "attendees")
    private String attendees;
    @Column(name = "note")
    private String note;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getVeune() {
        return veune;
    }

    public void setVeune(String veune) {
        this.veune = veune;
    }

    public String getLocationX() {
        return locationX;
    }

    public void setLocationX(String locationX) {
        this.locationX = locationX;
    }

    public String getLocationY() {
        return locationY;
    }

    public void setLocationY(String locationY) {
        this.locationY = locationY;
    }

    public String getAttendees() {
        return attendees;
    }

    public void setAttendees(String attendees) {
        this.attendees = attendees;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
