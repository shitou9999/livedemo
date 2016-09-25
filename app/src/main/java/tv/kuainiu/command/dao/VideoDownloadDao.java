package tv.kuainiu.command.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import tv.kuainiu.command.db.DbOpenHelper;
import tv.kuainiu.command.db.VDLContract;
import tv.kuainiu.modle.DownloadInfo2;

import java.util.LinkedList;

/**
 * @author nanck on 2016/5/5.
 */
public class VideoDownloadDao {

    private DbOpenHelper dbHelper;

    public VideoDownloadDao(Context context) {
        this.dbHelper = DbOpenHelper.getInstance(context);
    }

    public boolean addDownloadFile(DownloadInfo2 info) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = ContentValueFactory.getContentValue(info);
        db.beginTransaction(); // 开始事务

        long lineCount = db.insert(VDLContract.VDLEntry.TABLE_NAME, null, contentValues);
        if (lineCount > 0) {
            db.setTransactionSuccessful();
        }
        db.endTransaction();
        db.close();
        return (lineCount > 0);
    }

    public boolean deleteDownloadFile(DownloadInfo2 info) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = VDLContract.VDLEntry.COLUMN_VID + " = ? AND " + VDLContract.VDLEntry.COLUMN_BITRATE + " = ?";
        String[] selectionArgs = {info.getVid(), String.valueOf(info.getBitrate())};
        long lineCount = -1;
        if (db.isOpen()) {
            lineCount = db.delete(VDLContract.VDLEntry.TABLE_NAME, selection, selectionArgs);
        }
        return lineCount > 0;
    }

    public void updatePercent(DownloadInfo2 info, int percent) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "UPDATE " + VDLContract.VDLEntry.TABLE_NAME + " SET percent=? WHERE vid=? AND bitrate=?";
        db.execSQL(
                sql,
                new Object[]{percent, info.getVid(), info.getBitrate()});
    }


    public boolean updateStatus(DownloadInfo2 info, String status) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VDLContract.VDLEntry.COLUMN_STATUS, status);

        String selection = VDLContract.VDLEntry.COLUMN_VID + " = ? AND " +
                VDLContract.VDLEntry.COLUMN_BITRATE + " = ?";

        String[] selectionArgs = {info.getVid(), String.valueOf(info.getBitrate())};

        boolean flag = false;
        if (db.isOpen()) {
            int count = db.update(
                    VDLContract.VDLEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
            flag = count > 0;
        }
        return flag;
    }

    public boolean has(DownloadInfo2 info) {
        if (info == null || TextUtils.isEmpty(info.getVid())) {
            return false;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT vid ,duration,file_size,bitrate FROM " + VDLContract.VDLEntry.TABLE_NAME + " WHERE vid=? AND bitrate=?";
        Cursor cursor = db.rawQuery(sql, new String[]{info.getVid(), String.valueOf(info.getBitrate())});
        boolean flag = cursor.getCount() == 1;

        if (!cursor.isClosed()) {
            cursor.close();
        }
        return flag;
    }


    public boolean has(String videoId) {
        if (TextUtils.isEmpty(videoId)) {
            return false;
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT vid ,duration,file_size,bitrate FROM " + VDLContract.VDLEntry.TABLE_NAME + " WHERE vid=?";
        Cursor cursor = db.rawQuery(sql, new String[]{videoId});
        boolean flag = cursor.getCount() == 1;

        if (!cursor.isClosed()) {
            cursor.close();
        }
        return flag;
    }


    public LinkedList<DownloadInfo2> getDownloadAllFiles(String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        LinkedList<DownloadInfo2> infoList = new LinkedList<>();

        String[] projection = {
                VDLContract.VDLEntry.COLUMN_VID,
                VDLContract.VDLEntry.COLUMN_NEWS_ID,
                VDLContract.VDLEntry.COLUMN_CAT_ID,
                VDLContract.VDLEntry.COLUMN_TEACHER_ID,
                VDLContract.VDLEntry.COLUMN_TITLE,
                VDLContract.VDLEntry.COLUMN_FIRST_IMAGE,
                VDLContract.VDLEntry.COLUMN_FILE_SIZE,
                VDLContract.VDLEntry.COLUMN_TIMESTAMP,
                VDLContract.VDLEntry.COLUMN_BITRATE,
                VDLContract.VDLEntry.COLUMN_PERCENT,
                VDLContract.VDLEntry.COLUMN_DURATION,
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                VDLContract.VDLEntry.COLUMN_TIMESTAMP + " DESC";

        Cursor cursor = db.query(
                VDLContract.VDLEntry.TABLE_NAME, // The table to query
                projection,                      // The columns to return
                selection,                       // The columns for the WHERE clause
                selectionArgs,                   // The values for the WHERE clause
                null,                            // don't group the rows
                null,                            // don't filter by row groups
                sortOrder                        // The sort order
        );

        if (db.isOpen()) {
            if (!cursor.isClosed()) {
                while (cursor.moveToNext()) {
                    String vid = cursor.getString(cursor.getColumnIndex(VDLContract.VDLEntry.COLUMN_VID));
                    String title = cursor.getString(cursor.getColumnIndex(VDLContract.VDLEntry.COLUMN_TITLE));
                    String duration = cursor.getString(cursor.getColumnIndex(VDLContract.VDLEntry.COLUMN_DURATION));
                    String newsId = cursor.getString(cursor.getColumnIndex(VDLContract.VDLEntry.COLUMN_NEWS_ID));
                    String catId = cursor.getString(cursor.getColumnIndex(VDLContract.VDLEntry.COLUMN_CAT_ID));
                    String teacherId = cursor.getString(cursor.getColumnIndex(VDLContract.VDLEntry.COLUMN_TEACHER_ID));
                    String imageUrl = cursor.getString(cursor.getColumnIndex(VDLContract.VDLEntry.COLUMN_FIRST_IMAGE));
                    int fileSize = cursor.getInt(cursor.getColumnIndex(VDLContract.VDLEntry.COLUMN_FILE_SIZE));
                    int bitrate = cursor.getInt(cursor.getColumnIndex(VDLContract.VDLEntry.COLUMN_BITRATE));
                    int percent = cursor.getInt(cursor.getColumnIndex(VDLContract.VDLEntry.COLUMN_PERCENT));
                    long timeStamp = cursor.getInt(cursor.getColumnIndex(VDLContract.VDLEntry.COLUMN_TIMESTAMP));

                    DownloadInfo2 info = new DownloadInfo2(vid, duration, fileSize, bitrate);
                    info.setPercent(percent);
                    info.setTitle(title);
                    info.setNewsId(newsId);
                    info.setCatId(catId);
                    info.setTeacherId(teacherId);
                    info.setFirstImage(imageUrl);
                    info.setTimeStamp(timeStamp);

                    infoList.addLast(info);
                }
                cursor.close();
            }
            //  db.close();
        }
        return infoList;
    }


    public LinkedList<DownloadInfo2> getDownloadAllEdFiles() {
        String selection = VDLContract.VDLEntry.COLUMN_PERCENT + " >= ?";
        String[] selectionArgs = {"100"};
        return getDownloadAllFiles(selection, selectionArgs);
    }


    public LinkedList<DownloadInfo2> getDownloadAllIngFiles() {
        String selection = VDLContract.VDLEntry.COLUMN_PERCENT + " < ?";
        String[] selectionArgs = {"100"};
        return getDownloadAllFiles(selection, selectionArgs);
    }


    /**
     * Convert DownloadInfo2 into a ContentValues
     *
     * @author nanck
     * @since 2.1.0
     */
    private static class ContentValueFactory {
        public static ContentValues getContentValue(DownloadInfo2 info) {
            ContentValues cv = new ContentValues();
            cv.put(VDLContract.VDLEntry.COLUMN_VID, info.getVid());
            cv.put(VDLContract.VDLEntry.COLUMN_NEWS_ID, info.getNewsId());
            cv.put(VDLContract.VDLEntry.COLUMN_CAT_ID, info.getCatId());
            cv.put(VDLContract.VDLEntry.COLUMN_TEACHER_ID, info.getTeacherId());
            cv.put(VDLContract.VDLEntry.COLUMN_TITLE, info.getTitle());
            cv.put(VDLContract.VDLEntry.COLUMN_FIRST_IMAGE, info.getFirstImage());
            cv.put(VDLContract.VDLEntry.COLUMN_FILE_SIZE, info.getFilesize());
            cv.put(VDLContract.VDLEntry.COLUMN_TIMESTAMP, info.getTimeStamp());
            cv.put(VDLContract.VDLEntry.COLUMN_BITRATE, info.getBitrate());
            cv.put(VDLContract.VDLEntry.COLUMN_PERCENT, info.getPercent());
            cv.put(VDLContract.VDLEntry.COLUMN_DURATION, info.getDuration());
            return cv;
        }
    }
}
