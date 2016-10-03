package assignment1.eventplan.db.master;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import assignment1.eventplan.db.DatabaseEngine;
import assignment1.eventplan.entity.Contact;
import assignment1.eventplan.db.dao.EventContactDao;
import assignment1.eventplan.entity.EventPlan;
import assignment1.eventplan.db.dao.EventPlanDao;

import static assignment1.eventplan.utils.DateUtil.parseDateTime;

/**
 * Created by yumizhang on 2016/9/26.
 */
final class EventPlanDatabaseMaster {
    private EventPlanDatabaseMaster() {

    }

    /**
     * @param event insert or update
     */
    public static void insertOrUpdate(final EventPlan event) {
        final List<Contact> contacts = event.getContacts();
        DatabaseEngine.transaction(new DatabaseEngine.Callback() {
            @Override
            public void onTransaction(SQLiteDatabase db) {
                // 1. insert or update event content
                // 2. delete all relationship - if need
                // 3. re-add all relationship
                if (event.getId() > 0) {
                    final String[] whereArgs = new String[]{String.valueOf(event.getId())};
                    //1. update event
                    db.update(EventPlanDao.TABLE_NAME, EventPlanDao.buildContentValues(event), EventPlanDao.Field.ID + "=?", whereArgs);
                    //2. delete all relationship if exists
                    db.delete(EventContactDao.TABLE_NAME, EventContactDao.Field.ID_EVENT + "=?", whereArgs);
                } else {
                    //1. insert event
                    event.setId(db.insert(EventPlanDao.TABLE_NAME, null, EventPlanDao.buildContentValues(event)));
                }

                if (!contacts.isEmpty()) {
                    //3. now we need save all relationship
                    for (Contact contact : event.getContacts()) {
                        db.insert(EventContactDao.TABLE_NAME, null, EventContactDao.buildContentValues(event, contact));
                    }
                }

            }
        });
    }

    /**
     * @param event delete one event
     */
    public static void deleteEvent(final EventPlan event) {
        DatabaseEngine.transaction(new DatabaseEngine.Callback() {
            @Override
            public void onTransaction(SQLiteDatabase db) {
                final String[] whereArgs = new String[]{String.valueOf(event.getId())};
                // 1. delete event
                db.delete(EventPlanDao.TABLE_NAME, EventPlanDao.Field.ID + "=?", whereArgs);
                // 2. delete relationship if exists
                db.delete(EventContactDao.TABLE_NAME, EventContactDao.Field.ID_EVENT + "=?", whereArgs);

            }
        });
    }

    /**
     * get one event by id
     *
     * @param id event id
     * @return event
     */
    public static EventPlan getEventById(long id) {
        SQLiteDatabase database = DatabaseEngine.openDatabase();
        Cursor cursor = null;
        try {
            cursor = database.query(EventPlanDao.TABLE_NAME, null, EventPlanDao.Field.ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
            return cursor.moveToNext() ? EventPlanDao.createFromCursor(cursor) : null;
        } finally {
            if (null != cursor)
                cursor.close();
            DatabaseEngine.closeIfOpen();
        }
    }


    /**
     * @return all evnets
     */
    public static List<EventPlan> getAllEvent() {
        SQLiteDatabase database = DatabaseEngine.openDatabase();
        List<EventPlan> result = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = database.query(EventPlanDao.TABLE_NAME, null, null, null, null, null, EventPlanDao.Field.START_DATE_TIME + " asc");
            while (cursor.moveToNext()) {
                EventPlan plan = EventPlanDao.createFromCursor(cursor);
                if (null == plan)
                    continue;
                result.add(plan);
            }
            return result;
        } finally {
            if (null != cursor)
                cursor.close();
            DatabaseEngine.closeIfOpen();
        }
    }


    /**
     * // TODO
     *
     * @param dateStr {@link EventPlan#formatPattern}
     * @return 查询某天是否有活动
     */
    public static boolean hasPlan(@Nullable String dateStr) {
        final long[] dateTimes = parseDateTime(dateStr);
        if (null == dateTimes)
            return false;
        SQLiteDatabase database = DatabaseEngine.openDatabase();
        Cursor cursor = null;
        try {
            cursor = database.query(EventPlanDao.TABLE_NAME, null,
                    EventPlanDao.Field.START_DATE_TIME + ">=? and " + EventPlanDao.Field.START_DATE_TIME + "<?",
                    new String[]{String.valueOf(dateTimes[0]), String.valueOf(dateTimes[1])},
                    null, null, null, "1");
            return cursor.getCount() > 0;
        } finally {
            if (null != cursor)
                cursor.close();
            DatabaseEngine.closeIfOpen();
        }
    }


    /**
     * @param dateStr {@link EventPlan#formatPattern}
     * @return all events by date
     */
    @Nullable
    public static List<EventPlan> getEventByDate(@Nullable String dateStr) {
        final long[] dateTimes = parseDateTime(dateStr);
        if (null == dateTimes)
            return null;

        SQLiteDatabase database = DatabaseEngine.openDatabase();
        List<EventPlan> result = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = database.query(EventPlanDao.TABLE_NAME, null,
                    EventPlanDao.Field.START_DATE_TIME + ">=? and " + EventPlanDao.Field.START_DATE_TIME + "<?",
                    new String[]{String.valueOf(dateTimes[0]), String.valueOf(dateTimes[1])},
                    null, null, null);
            while (cursor.moveToNext()) {
                EventPlan plan = EventPlanDao.createFromCursor(cursor);
                if (null == plan)
                    continue;
                result.add(plan);
            }
            return result;
        } finally {
            if (null != cursor)
                cursor.close();
            DatabaseEngine.closeIfOpen();
        }
    }


}
