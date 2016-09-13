package tv.kuainiu.command.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import tv.kuainiu.command.db.DBConstant;
import tv.kuainiu.command.db.DbOpenHelper;
import tv.kuainiu.modle.NewsItem;
import tv.kuainiu.util.ClassRefUtil;
import tv.kuainiu.util.LogUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 播放记录DAO
 */
public class PlayRecordDao {
    private DbOpenHelper dbHelper;

    public PlayRecordDao(Context context) {
        this.dbHelper = DbOpenHelper.getInstance(context);
    }

    /**
     * 清空全部播放记录
     */
    public void delPlayRecord() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.execSQL("DELETE FROM " + DBConstant.TABLE_PLAYRECORD + " ;UPDATE sqlite_sequence SET seq = 0 WHERE name = " + DBConstant.TABLE_PLAYRECORD + ";");
        }
    }

    /**
     * 根据视频id删除播放记录
     *
     * @param id
     */
    public boolean delPlayRecordById(String id) {
        int count = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            count = db.delete(DBConstant.TABLE_PLAYRECORD, "id=?",
                    new String[]{id});
        }
        return count > 0;
    }

    /**
     * （本地）插入播放历史记录
     */
    public void insertPlayRecordData(NewsItem NewsItem) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.beginTransaction();
            ContentValues cv = new ContentValues();
            ClassRefUtil.setContentValues(cv, NewsItem);
            db.insert(DBConstant.TABLE_PLAYRECORD, null, cv);
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }


    /**
     * 更新播放进度
     *
     * @param id
     * @param stopPosition
     * @return
     */
    public boolean updateStopPosition(String id, int stopPosition) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("stopPosition", stopPosition);

        String selection = "id = ?";

        String[] selectionArgs = {id};

        boolean flag = false;
        if (db.isOpen()) {
            int count = db.update(
                    DBConstant.TABLE_PLAYRECORD,
                    values,
                    selection,
                    selectionArgs);
            flag = count > 0;
        }
        return flag;
    }

    /**
     * 获取全部播放记录
     *
     * @return
     */
    public List<NewsItem> getPlayRecord() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<NewsItem> list = new ArrayList<>();
        if (db.isOpen()) {
            String select_sql = "select * from " + DBConstant.TABLE_PLAYRECORD + " ORDER BY updatetime DESC ";
            LogUtils.d("select_sql = ", select_sql);
            Cursor c = db.rawQuery(select_sql, null);
            Field[] fields = NewsItem.class.getDeclaredFields();
            while (c.moveToNext()) {
                Map<String, String> map = new HashMap<String, String>();
                for (Field field : fields) {
                    if (field.getName().endsWith("change")) {
                        continue;
                    }
                    map.put(field.getName(), c.getString(c.getColumnIndex(field.getName())));
                }
                NewsItem sc = new NewsItem();
                ClassRefUtil.setFieldValue(sc, map);
                if (sc != null) {
                    list.add(sc);
                }
            }
            if (c != null) {
                c.close();
            }
        }
        return list;
    }

    public NewsItem findPlayRecordById(String id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        NewsItem newsItem = new NewsItem();
        if (db.isOpen()) {
            String select_sql = "select * from " + DBConstant.TABLE_PLAYRECORD + " where id = ?";
            LogUtils.d("select_sql = ", select_sql);
            Cursor c = db.rawQuery(select_sql, new String[]{id});
            Field[] fields = NewsItem.class.getDeclaredFields();
            while (c.moveToNext()) {
                Map<String, String> map = new HashMap<String, String>();
                for (Field field : fields) {
                    if (!field.getName().endsWith("change")) {
                        map.put(field.getName(), c.getString(c.getColumnIndex(field.getName())));
                    }
                }
                ClassRefUtil.setFieldValue(newsItem, map);
            }
            if (!c.isClosed()) {
                c.close();
            }
        }
        return newsItem;
    }

    /**
     * @param id video id
     * @return boolean result
     * <p/>
     * 判断是否已经存在相同的播放记录，有则返回True，无则返回false
     */
    public boolean isNotExist(String id) {
        int count = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor c = db.rawQuery("select * from " + DBConstant.TABLE_PLAYRECORD + " where id=?", new String[]{id});
            count = c.getCount();
            if (!c.isClosed()) {
                c.close();
            }
        }
        return count > 0;
    }

}
