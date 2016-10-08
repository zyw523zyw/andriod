package assignment1.eventplan.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import assignment1.eventplan.entity.Contact;

/**
 * Created by yumizhang on 2016/9/28.
 */
public class ContactDao {


    public static final String TABLE_NAME = "T_CONTACT";
    public static final String KEY = "Contact";


    public interface Field {
        String ID = "_id";
        String NAME = "name";
        String PHONE = "phone";
        String LABEL = "label";
    }


    public static String createTableSql() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + Field.ID + " INTEGER PRIMARY KEY, "
                + Field.NAME + " TEXT,"
                + Field.PHONE + " TEXT,"
                + Field.LABEL + " TEXT"
                + ")";
    }

    public static final ContentValues buildContentValues(Contact contact) {
        ContentValues values = new ContentValues();
        values.put(Field.ID, contact.getId());
        values.put(Field.NAME, contact.getName());
        values.put(Field.PHONE, contact.getPhone());
        values.put(Field.LABEL, contact.getLabel());
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
    public static Contact createFromCursor(@NonNull Cursor cursor) {
        Contact contact = new Contact();
        contact.id = cursor.getLong(cursor.getColumnIndex(Field.ID));
        contact.name = cursor.getString(cursor.getColumnIndex(Field.NAME));
        contact.phone = cursor.getString(cursor.getColumnIndex(Field.PHONE));
        contact.label = cursor.getString(cursor.getColumnIndex(Field.LABEL));
        return contact;
    }
}
