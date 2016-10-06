package assignment1.eventplan.services;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import assignment1.eventplan.db.master.ContactMemory;
import assignment1.eventplan.entity.Contact;


public class ContactBackupService extends IntentService {

    public ContactBackupService() {
        super("ContactBackupService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("Contact", "handleIntent:work->" + Thread.currentThread());
        //1. load all contacts from device
        //2. save to app database
        final ContentResolver contentResolver = getContentResolver();
        final Uri uri = ContactsContract.Contacts.CONTENT_URI;

        final String[] projection = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
        };

        Cursor cursor = null;
        try {
            cursor = contentResolver.query(
                    uri,
                    projection,
                    null,
                    null,
                    ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            );
            if (null == cursor)
                return;
            List<Contact> contacts = new ArrayList<>();
            while (cursor.moveToNext()) {
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) < 1)// skip none phone_number
                    continue;

                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Cursor phoneCursor = null;
                try {
                    phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                            new String[]{contactId},
                            null
                    );
                    if (phoneCursor != null && phoneCursor.getCount() > 0) {
                        while (phoneCursor.moveToNext()) {
                            long phoneId = phoneCursor.getLong(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));

                            String customLabel = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));

                            String phoneLabel = (String) ContactsContract.CommonDataKinds.Phone.getTypeLabel(getResources(),
                                    phoneCursor.getInt(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)),
                                    customLabel
                            );
                            String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            contacts.add(new Contact(phoneId, contactName, phoneNumber, phoneLabel));
                        }
                    }
                } finally {
                    if (null != phoneCursor)
                        phoneCursor.close();
                }
            }
            ContactMemory.insertOrUpdate(contacts);
        } finally {
            if (null != cursor)
                cursor.close();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Contact", "work done->" + Thread.currentThread());
    }
}
