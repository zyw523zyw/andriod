package assignment1.eventplan.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import assignment1.eventplan.entity.EventPlan;

/**
 * Created by yumizhang on 2016/9/28.
 */
public final class EventPlanDao {

    public static final String TABLE_NAME = "T_EVENT";
    public static final String KEY = "EventPlan";


    public interface Field {
        String ID = "_id";
        String TITLE = "title";
        String START_DATE_TIME = "startDateTime";
        String END_DATE_TIME = "endDateTime";
        String ADDRESS = "address";
        String ATTENDEES = "attendees";
        String NOTE = "note";
        String DATE = "date";
        String LATITUDE = "lat";
        String LONGITUDE = "lng";
        String ADDRESS_NAME = "address_name";
        String ADDRESS_ATTRIBUTIONS = "address_attributions";

    }

    public static String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + Field.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Field.TITLE + " TEXT,"
                + Field.START_DATE_TIME + " INTEGER,"
                + Field.END_DATE_TIME + " INTEGER,"
                + Field.ADDRESS + " TEXT,"
                + Field.ATTENDEES + " TEXT,"
                + Field.NOTE + " TEXT,"
                + Field.LATITUDE + " INTEGER,"
                + Field.LONGITUDE + " INTEGER,"
                + Field.ADDRESS_NAME + " TEXT,"
                + Field.ADDRESS_ATTRIBUTIONS + " TEXT,"
                + Field.DATE + " INTEGER"
                + ")";
    }

    public static ContentValues buildContentValues(EventPlan plan) {
        ContentValues values = new ContentValues();
        values.put(Field.TITLE, plan.getTitle());
        values.put(Field.START_DATE_TIME, plan.getStartDateTime());
        values.put(Field.END_DATE_TIME, plan.getEndDateTime());
        values.put(Field.ADDRESS, plan.getAddress());
        values.put(Field.ATTENDEES, plan.getAttendees());
        values.put(Field.NOTE, plan.getNote());
        LatLng latLng = plan.getLatLng();
        if (null != latLng) {
            values.put(Field.LATITUDE, latLng.latitude);
            values.put(Field.LONGITUDE, latLng.longitude);
        }
        values.put(Field.ADDRESS_NAME, plan.getAddressName());
        values.put(Field.ADDRESS_ATTRIBUTIONS, plan.getAddressAttributions());
        return values;
    }

    /**
     * 注意:
     * 1.note:
     * 1. Cursor will not be closed here, because the query may be a collection
     * 2. Need to confirm whether the cursor has been moved to the next line before the external confirmation call to ensure that the current call is a valid call
     * {@see EventPlanDatabaseMaster#getEventById(long)}
     *
     * @param cursor query result set
     * @return event
     */
    @Nullable
    public static EventPlan createFromCursor(@NonNull Cursor cursor) {
        EventPlan plan = new EventPlan();
        plan.setId(cursor.getLong(cursor.getColumnIndex(Field.ID)));
        plan.setStartDateTime(cursor.getLong(cursor.getColumnIndex(Field.START_DATE_TIME)));
        plan.setEndDateTime(cursor.getLong(cursor.getColumnIndex(Field.END_DATE_TIME)));
        plan.setTitle(cursor.getString(cursor.getColumnIndex(Field.TITLE)));
        plan.setAddress(cursor.getString(cursor.getColumnIndex(Field.ADDRESS)));
        plan.setAttendees(cursor.getString(cursor.getColumnIndex(Field.ATTENDEES)));
        plan.setNote(cursor.getString(cursor.getColumnIndex(Field.NOTE)));
        plan.setLatLng(new LatLng(cursor.getLong(cursor.getColumnIndex(Field.LATITUDE)), cursor.getLong(cursor.getColumnIndex(Field.LONGITUDE))));
        plan.setAddressName(cursor.getString(cursor.getColumnIndex(Field.ADDRESS_NAME)));
        plan.setAddressAttributions(cursor.getString(cursor.getColumnIndex(Field.ADDRESS_ATTRIBUTIONS)));
        return plan;
    }
}
