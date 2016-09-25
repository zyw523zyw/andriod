package assignment1.eventplan.utils;

import android.content.Context;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

import assignment1.eventplan.entity.EventEntity;

/**
 * Created by zz on 2016/8/26.
 */
public class DbUtil {
    private DbUtil() {
    }

    private static DbManager getDbManager() {
        return x.getDb(new DbManager.DaoConfig()
                .setDbName("eventplan.db")
                .setDbVersion(1)
                .setAllowTransaction(true));
    }

    /**
     * add one event
     *
     * @param eventEntity
     */
    public static void addEvent(EventEntity eventEntity) {
        DbManager dbManager = getDbManager();
        try {
            dbManager.save(eventEntity);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * delete one event
     *
     * @param eventEntity
     */
    public static void deleteEvent(EventEntity eventEntity) {
        DbManager dbManager = getDbManager();
        try {
            dbManager.delete(eventEntity);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * get one event by id
     *
     * @param id
     * @return
     */
    public static EventEntity getEventById(int id) {
        DbManager dbManager = getDbManager();
        try {
            return dbManager.findById(EventEntity.class, id);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * udpate one event
     *
     * @param eventEntity
     */
    public static void updateEvent(EventEntity eventEntity) {
        DbManager dbManager = getDbManager();
        try {
            dbManager.update(eventEntity, "title", "startDate", "endDate", "startTime", "endTime", "veune", "locationX", "locationY", "attendees", "note");
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * get all of the evnets
     *
     * @return
     */
    public static List<EventEntity> getAllEvent() {
        DbManager dbManager = getDbManager();
        try {
            return dbManager.selector(EventEntity.class).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean hasPlan(String dateStr) {
        DbManager dbManager = getDbManager();
        try {
            long count = dbManager.selector(EventEntity.class).where("startDate", "==", dateStr).count();
            return count > 0;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * get all events by date
     * @param dateStr
     * @return
     */
    public static List<EventEntity> getEventByDate(String dateStr) {
        DbManager dbManager = getDbManager();
        try {
            return dbManager.selector(EventEntity.class).where("startDate", "==", dateStr).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }
}
