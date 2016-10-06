package assignment1.eventplan.db.dao;

import android.content.ContentValues;

import assignment1.eventplan.entity.Contact;
import assignment1.eventplan.entity.EventPlan;

/**
 * Created by yumizhang on 2016/9/28.
 */
public class EventContactDao {

    public static final String TABLE_NAME = "T_EventContact";

    public static ContentValues buildContentValues(EventPlan event, Contact contact) {
        ContentValues values = new ContentValues();
        values.put(Field.ID_CONTACT, contact.getId());
        values.put(Field.ID_EVENT, event.getId());
        return values;
    }

    public interface Field {
        String ID = "_id";
        String ID_CONTACT = "id_contact";
        String ID_EVENT = "id_event";
    }


    public static String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + Field.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Field.ID_CONTACT + " INTEGER,"
                + Field.ID_EVENT + " INTEGER"
                + ")";
    }

}
