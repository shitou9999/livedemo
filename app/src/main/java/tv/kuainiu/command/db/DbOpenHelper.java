package tv.kuainiu.command.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import tv.kuainiu.modle.NewsItem;
import tv.kuainiu.utils.ClassRefUtil;
import tv.kuainiu.utils.DebugUtils;


/**
 * @ClassName DbOpenHelper
 * @Description 数据库操作帮助类
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    private static DbOpenHelper instance;
    private Context context;

    private DbOpenHelper(Context context) {
        super(context, getUserDatabaseName(), null, DBConstant.DB_VERSION);
        this.context = context;

    }

    public static synchronized DbOpenHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbOpenHelper(context.getApplicationContext());
        }
        return instance;
    }

    private static String getUserDatabaseName() {
        return DBConstant.DB_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ClassRefUtil.generateCreateTableSql(NewsItem.class, false));
        db.execSQL(VDLContract.SQL_CREATE_VIDEO_DOWNLOADS);
        db.execSQL(LiveSubscribeContract.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        DebugUtils.dd("=====================================================");
        DebugUtils.dd("onUpgrade oldVersion : " + oldVersion);
        DebugUtils.dd("onUpgrade newVersion : " + newVersion);
        DebugUtils.dd("=====================================================");

        if (newVersion == 4) {
            db.execSQL(LiveSubscribeContract.SQL_CREATE_TABLE);
        }
        if (newVersion == 3) {
            db.execSQL("ALTER TABLE " + DBConstant.TABLE_PLAYRECORD + " ADD COLUMN stopPosition INTEGER DEFAULT '0';");
        }
        db.execSQL(VDLContract.SQL_CREATE_VIDEO_DOWNLOADS);
    }

    public void closeDB() {
        if (instance != null) {
            try {
                SQLiteDatabase db = instance.getWritableDatabase();
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            instance = null;
        }
    }

}
