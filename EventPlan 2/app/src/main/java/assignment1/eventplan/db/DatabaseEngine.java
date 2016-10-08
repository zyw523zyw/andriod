package assignment1.eventplan.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.concurrent.atomic.AtomicInteger;

import assignment1.eventplan.application.MyApplication;
import assignment1.eventplan.db.dao.ContactDao;
import assignment1.eventplan.db.dao.EventContactDao;
import assignment1.eventplan.db.dao.EventPlanDao;

/**
 * Created by yumizhang on 2016/8/26.
 */
public final class DatabaseEngine {

    public interface Callback {
        void onTransaction(SQLiteDatabase db);
    }

    private DatabaseEngine() {
    }

    /**
     * {@link #closeIfOpen()} must be call
     *
     * @return SQLiteDatabase
     */
    public static SQLiteDatabase openDatabase() {
        return DatabasePorter.get().open();
    }

    /**
     * //Note: the operation of the implementation of the current call in the thread, if it is to avoid time-consuming UI Thread operation, to avoid ANR
     * @param callback onTransaction callback
     */
    public static void transaction(Callback callback) {
        if (null == callback)
            return;
        SQLiteDatabase db = openDatabase();
        db.beginTransaction();
        try {
            callback.onTransaction(db);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            closeIfOpen();
        }
    }


    public static void closeIfOpen() {
        DatabasePorter.get().close();
    }


    /**
     * database manager impl
     * {@link #open()}
     * {@link #close()}
     **/
    private static final class DatabasePorter {
        static class SingleInstanceHolder {
            static final DatabasePorter manager = new DatabasePorter();
        }

        private DatabaseProvider dbProvider;
        private AtomicInteger dbOpenCount;
        private SQLiteDatabase database;

        DatabasePorter() {
            dbProvider = new DatabaseProvider();
            dbOpenCount = new AtomicInteger();
        }

        SQLiteDatabase open() {
            SQLiteDatabase database = this.database;
            if (null == database && dbOpenCount.incrementAndGet() == 1) {
                database = dbProvider.getWritableDatabase();
                database.enableWriteAheadLogging();
                this.database = database;
            }
            return database;
        }

        void close() {
            SQLiteDatabase database = this.database;
            if (null != database && dbOpenCount.decrementAndGet() == 0) {
                database.close();
                this.database = null;
            }
        }

        static DatabasePorter get() {
            return SingleInstanceHolder.manager;
        }

    }

    // database provider
    private static final class DatabaseProvider extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "EventPlanDB";
        private static final int VERSION = 1;

        DatabaseProvider() {
            super(MyApplication.get(), DATABASE_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(EventPlanDao.createTableSql());
            db.execSQL(EventContactDao.createTableSql());
            db.execSQL(ContactDao.createTableSql());

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        }
    }
}
