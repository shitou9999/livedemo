package com.iguxuan.iguxuan_friends.command.db;

import android.provider.BaseColumns;

/**
 * @author nanck on 2016/7/6.
 */
public class AREAContract {

    /**
     * id;
     * name;
     * code;
     * fatherId;
     * level;
     */
    public static abstract class AreaEntry implements BaseColumns {
        public static final String TABLE_NAME = "Area";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CODE = "code";
        public static final String COLUMN_FATHER_ID = "fatherId";
        public static final String COLUMN_LEVEL = "level";

    }
}
