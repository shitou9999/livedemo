package com.iguxuan.iguxuan_friends.command.db;

import android.provider.BaseColumns;

/**
 * 直播预约提醒表字符串常量
 *
 * @author nanck on 2016/7/22.
 */
public class LiveSubscribeContract {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE if not exists " + Entry.TABLE_NAME + " (" +
            Entry.COLUMN_ID + TEXT_TYPE + COMMA_SEP +
            Entry.COLUMN_FLAG + INTEGER_TYPE + COMMA_SEP +
            "PRIMARY KEY (" + Entry.COLUMN_ID + "))";


    public static final String SQL_SELECT_BY_ID = "SELECT " + Entry.COLUMN_ID + COMMA_SEP +
            Entry.COLUMN_FLAG + " FROM " + Entry.TABLE_NAME +
            " WHERE " + Entry.COLUMN_ID + " =?";


    public static abstract class Entry implements BaseColumns {
        public static final String TABLE_NAME = "live_subscribes";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_FLAG = "isSubscribe";
        public static final String COLUMN_DATE = "date";
    }

}
