package assignment1.eventplan.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import assignment1.eventplan.db.master.ContactMemory;


/**
 * Created by yumizhang on 2016/8/26.
 */
public final class EventPlan implements Parcelable, Comparable<EventPlan> {
    public static final String formatPattern = "yyyy-MM-dd HH:mm:ss";


    private long id;// db id
    private String title;
    private long startDateTime;
    private long endDateTime;
    private String address;
    private String attendees;
    private String note;
    private long date;

    private LatLng latLng;
    private String addressName;
    private String addressAttributions;


    // relationship 1:∞
    private List<Contact> contacts;


    public EventPlan() {

    }


    protected EventPlan(Parcel in) {
        id = in.readLong();
        title = in.readString();
        startDateTime = in.readLong();
        endDateTime = in.readLong();
        address = in.readString();
        attendees = in.readString();
        note = in.readString();
        date = in.readLong();
        latLng = in.readParcelable(LatLng.class.getClassLoader());
        addressName = in.readString();
        addressAttributions = in.readString();
        contacts = in.createTypedArrayList(Contact.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeLong(startDateTime);
        dest.writeLong(endDateTime);
        dest.writeString(address);
        dest.writeString(attendees);
        dest.writeString(note);
        dest.writeLong(date);
        dest.writeParcelable(latLng, flags);
        dest.writeString(addressName);
        dest.writeString(addressAttributions);
        dest.writeTypedList(contacts);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EventPlan> CREATOR = new Creator<EventPlan>() {
        @Override
        public EventPlan createFromParcel(Parcel in) {
            return new EventPlan(in);
        }

        @Override
        public EventPlan[] newArray(int size) {
            return new EventPlan[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(long startDateTime) {
        this.startDateTime = startDateTime;
    }

    public long getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(long endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    /**
     * //1. When no associated contact for the return emptyList, can not add or delete operations, such as the need to cover the operation list .
     * //2. Non-thread-safe, in the call to note
     * @return contacts,
     */
    @NonNull
    public List<Contact> getContacts() {
        if (null == contacts) {
            contacts = ContactMemory.loadAllContactByEventPlan(this);
        }
        return contacts;
    }

    public void setContacts(@NonNull List<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public int compareTo(EventPlan o) {
        return (int) (startDateTime - o.startDateTime);
    }


    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getAddressAttributions() {
        return addressAttributions;
    }

    public void setAddressAttributions(String addressAttributions) {
        this.addressAttributions = addressAttributions;
    }
}
