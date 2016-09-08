package com.iguxuan.iguxuan_friends.command.db;

import android.provider.BaseColumns;

/**
 * 下载记录表字符串常量
 *
 * @author nanck on 2016/5/9.
 */
public final class VDLContract {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_VIDEO_DOWNLOADS = "CREATE TABLE if not exists " + VDLEntry.TABLE_NAME + " (" +
            VDLEntry._ID + INTEGER_TYPE + COMMA_SEP +
            VDLEntry.COLUMN_VID + TEXT_TYPE + COMMA_SEP +
            VDLEntry.COLUMN_NEWS_ID + TEXT_TYPE + COMMA_SEP +
            VDLEntry.COLUMN_CAT_ID + TEXT_TYPE + COMMA_SEP +
            VDLEntry.COLUMN_TEACHER_ID + TEXT_TYPE + COMMA_SEP +
            VDLEntry.COLUMN_TITLE + TEXT_TYPE + COMMA_SEP +
            VDLEntry.COLUMN_FIRST_IMAGE + TEXT_TYPE + COMMA_SEP +
            VDLEntry.COLUMN_STATUS + TEXT_TYPE + COMMA_SEP +
            VDLEntry.COLUMN_DURATION + TEXT_TYPE + COMMA_SEP +
            VDLEntry.COLUMN_BITRATE + INTEGER_TYPE + COMMA_SEP +
            VDLEntry.COLUMN_FILE_SIZE + INTEGER_TYPE + COMMA_SEP +
            VDLEntry.COLUMN_TIMESTAMP + INTEGER_TYPE + COMMA_SEP +
            VDLEntry.COLUMN_PERCENT + INTEGER_TYPE + COMMA_SEP +
            "PRIMARY KEY (" +
            VDLEntry.COLUMN_VID + COMMA_SEP + " " +
            VDLEntry.COLUMN_BITRATE + "))";


    public static final String SQL_DELETE_VIDEO_DOWNLOADS =
            "DROP TABLE IF EXISTS " + VDLEntry.TABLE_NAME;


    public static abstract class VDLEntry implements BaseColumns {
        public static final String TABLE_NAME = "video_downloads";
        public static final String COLUMN_VID = "vid";
        public static final String COLUMN_NEWS_ID = "news_id";
        public static final String COLUMN_CAT_ID = "cat_id";
        public static final String COLUMN_TEACHER_ID = "teacher_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_FIRST_IMAGE = "first_image";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_FILE_SIZE = "file_size";
        public static final String COLUMN_BITRATE = "bitrate";
        public static final String COLUMN_PERCENT = "percent";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_TIMESTAMP = "time_stamp";
    }
}
