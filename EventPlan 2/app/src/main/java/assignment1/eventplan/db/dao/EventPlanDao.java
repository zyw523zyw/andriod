package assignment1.eventplan.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
        return values;
    }

    /**
     * 注意:
     * 1. 此处不会关闭 Cursor,因为查询出来的可能是个集合
     * 2. 需要在外部确认调用之前 cursor 是否已经移到 下一行,确保当前的调用是有效调用
     * {@link EventPlanDatabaseMaster#getEventById(long)}
     *
     * @param cursor 查询结果集
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
        return plan;
    }
}
