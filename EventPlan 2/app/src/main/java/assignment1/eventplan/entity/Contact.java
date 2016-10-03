package assignment1.eventplan.entity;

/**
 * Created by yumizhang on 16/8/25.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class Contact implements Parcelable {


    public long id;
    public long phoneId;
    public String name;
    public String phone;
    public String label;

    public Contact() {

    }

    public Contact(long id, String name, String phone, String label) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.label = label;
    }

    protected Contact(Parcel in) {
        id = in.readLong();
        phoneId = in.readLong();
        name = in.readString();
        phone = in.readString();
        label = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(phoneId);
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(label);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    @Override
    public String toString() {
        return name + " | " + label + " : " + phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public long getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(long phoneId) {
        this.phoneId = phoneId;
    }
}