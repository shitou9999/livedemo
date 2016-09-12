package com.kuainiutv.command.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.kuainiutv.command.db.DbOpenHelper;
import com.kuainiutv.command.db.LiveSubscribeContract;
import com.kuainiutv.modle.Remind;

import java.util.LinkedList;

/**
 * @author nanck on 2016/7/22.
 */
public class LiveSubscribeDao {

    private DbOpenHelper dbHelper;

    public LiveSubscribeDao(Context context) {
        this.dbHelper = DbOpenHelper.getInstance(context);
    }


    public boolean insert(String id, int flag) {
        if (TextUtils.isEmpty(id)) {
            return false;
        }
        if (has(id)) {
            return false;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LiveSubscribeContract.Entry.COLUMN_ID, id);
        contentValues.put(LiveSubscribeContract.Entry.COLUMN_FLAG, flag);

        db.beginTransaction(); // 开始事务

        long lineCount = db.insert(LiveSubscribeContract.Entry.TABLE_NAME, null, contentValues);
        if (lineCount > 0) {
            db.setTransactionSuccessful();
        }
        db.endTransaction();
        db.close();
        return (lineCount > 0);
    }

    public boolean delete(String id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = LiveSubscribeContract.Entry.COLUMN_ID + " = ?";
        String[] selectionArgs = {id};
        long lineCount = -1;
        if (db.isOpen()) {
            lineCount = db.delete(LiveSubscribeContract.Entry.TABLE_NAME, selection, selectionArgs);
        }
        return lineCount > 0;
    }

    public boolean has(String id) {
        if (TextUtils.isEmpty(id)) {
            return false;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = LiveSubscribeContract.SQL_SELECT_BY_ID;
        Cursor cursor = db.rawQuery(sql, new String[]{id});
        boolean flag = cursor.getCount() == 1;

        if (!cursor.isClosed()) {
            cursor.close();
        }
        return flag;
    }

    public boolean updateFlag(String id, int flag) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LiveSubscribeContract.Entry.COLUMN_FLAG, flag);

        String selection = LiveSubscribeContract.Entry.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        int count = db.update(
                LiveSubscribeContract.Entry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        return count > 0;
    }


    public LinkedList<Remind> fetchReminds() {
        return fetchReminds(null, null);
    }

    public LinkedList<Remind> fetchReminds(String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        LinkedList<Remind> linkedList = new LinkedList<>();

        String[] projection = {
                LiveSubscribeContract.Entry.COLUMN_ID,
                LiveSubscribeContract.Entry.COLUMN_FLAG
        };

        String sortOrder = LiveSubscribeContract.Entry.COLUMN_ID + " ASC";

        Cursor c = db.query(
                LiveSubscribeContract.Entry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );


        if (db.isOpen()) {
            if (!c.isClosed()) {
                while (c.moveToNext()) {
                    String id = c.getString(c.getColumnIndex(LiveSubscribeContract.Entry.COLUMN_ID));
                    int flag = c.getInt(c.getColumnIndex(LiveSubscribeContract.Entry.COLUMN_FLAG));
                    Remind remind = new Remind(id, flag);
                    linkedList.addLast(remind);
                }
                c.close();
            }
        }
        return linkedList;
    }
}
