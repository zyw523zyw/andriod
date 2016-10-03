package assignment1.eventplan.db.master;

import android.support.annotation.NonNull;
import android.support.v4.util.LongSparseArray;

import java.util.Collections;
import java.util.List;

import assignment1.eventplan.entity.Contact;
import assignment1.eventplan.entity.EventPlan;

public class ContactMemory {


    private static final class SingleInstance {
        static final ContactMemory holder = new ContactMemory();
    }

    public static ContactMemory get() {
        return SingleInstance.holder;
    }

    public static void insertOrUpdate(List<Contact> contacts) {
        get().allContacts = contacts;
        EventContactDatabaseMaster.insertOrUpdate(contacts);
    }

    private List<Contact> allContacts;

    private ContactMemory() {
        allContacts = EventContactDatabaseMaster.loadAllContact();
    }

    public List<Contact> getAllContacts() {
        return allContacts;
    }

    @NonNull
    public static List<Contact> loadAllContactByEventPlan(EventPlan plan) {
        if (plan.getId() > 0) {
            return EventContactDatabaseMaster.loadAllContactByEventPlan(plan);
        }
        return Collections.emptyList();
    }
}
