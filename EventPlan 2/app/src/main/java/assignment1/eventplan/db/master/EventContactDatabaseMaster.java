package assignment1.eventplan.db.master;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import assignment1.eventplan.db.DatabaseEngine;
import assignment1.eventplan.db.dao.ContactDao;
import assignment1.eventplan.db.dao.EventContactDao;
import assignment1.eventplan.entity.Contact;
import assignment1.eventplan.entity.EventPlan;

final class EventContactDatabaseMaster {


    @NonNull
    public static List<Contact> loadAllContact() {
        SQLiteDatabase db = DatabaseEngine.openDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(ContactDao.TABLE_NAME, null, null, null, null, null, null);
            List<Contact> list = new ArrayList<>();
            while (cursor.moveToNext()) {
                list.add(ContactDao.createFromCursor(cursor));
            }
            return list;
        } finally {
            if (null != cursor)
                cursor.close();
            DatabaseEngine.closeIfOpen();
        }
    }


    @NonNull
    public static List<Contact> loadAllContactByEventPlan(@NonNull EventPlan plan) {
        List<Contact> list = new ArrayList<>();
        if (plan.getId() < 1)
            return list;
        SQLiteDatabase db = DatabaseEngine.openDatabase();
        Cursor cursor = null;
        try {
            String sql = "SELECT contact.* from "
                    + ContactDao.TABLE_NAME
                    + " contact left join on "
                    + EventContactDao.TABLE_NAME + " event"
                    + " where event." + EventContactDao.Field.ID_EVENT + "=" + plan.getId()
                    + " and event." + EventContactDao.Field.ID_CONTACT + "= contact." + ContactDao.Field.ID;
            cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                list.add(ContactDao.createFromCursor(cursor));
            }
            return list;
        } finally {
            if (null != cursor)
                cursor.close();
            DatabaseEngine.closeIfOpen();
        }
    }


    public static void insertOrUpdate(final List<Contact> contacts) {
        DatabaseEngine.transaction(new DatabaseEngine.Callback() {
            @Override
            public void onTransaction(SQLiteDatabase db) {
                Log.d("Contact", "insertOrUpdate:work->" + Thread.currentThread());
                for (Contact contact : contacts) {
                    long rowId = db.insertWithOnConflict(ContactDao.TABLE_NAME, null, ContactDao.buildContentValues(contact), SQLiteDatabase.CONFLICT_REPLACE);
                    contact.setId(rowId);
                }
            }
        });
    }
}
