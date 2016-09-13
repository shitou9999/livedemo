package tv.kuainiu.command.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import tv.kuainiu.command.db.AREAContract;
import tv.kuainiu.command.db.AreaDB;
import tv.kuainiu.modle.Area;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nanck on 2016/7/6.
 */
public class AreaDao {
    private static final String TAG = "AreaDao";
    private AreaDB mAreaDB = new AreaDB();
    private SQLiteDatabase db;
    private Context context;

    public AreaDao(Context context) {
        this.context = context;
    }

    public List<Area> selectProvince() {
        return select(AREAContract.AreaEntry.COLUMN_LEVEL + " = 1");
    }

    public List<Area> selectArea(String id) {
        return select(AREAContract.AreaEntry.COLUMN_FATHER_ID + " = " + id);
    }

    private List<Area> select(String where) {
        Area area;
        List<Area> areaList = new ArrayList<>();
        db = mAreaDB.openDatabase(context);
        if (db.isOpen()) {
            Cursor cursor = db.query("area", new String[]{AREAContract.AreaEntry.COLUMN_ID,
                            AREAContract.AreaEntry.COLUMN_NAME, AREAContract.AreaEntry.COLUMN_CODE,
                            AREAContract.AreaEntry.COLUMN_FATHER_ID, AREAContract.AreaEntry.COLUMN_LEVEL},
                    where, null, null, null,
                    null);
            if (cursor.moveToFirst()) {
                do {
                    area = new Area();
                    area.setId(cursor.getString(cursor
                            .getColumnIndex(AREAContract.AreaEntry.COLUMN_ID)));
                    area.setName(cursor.getString(cursor
                            .getColumnIndex(AREAContract.AreaEntry.COLUMN_NAME)));
                    area.setCode(cursor.getString(cursor
                            .getColumnIndex(AREAContract.AreaEntry.COLUMN_CODE)));
                    area.setFatherId(cursor.getString(cursor
                            .getColumnIndex(AREAContract.AreaEntry.COLUMN_FATHER_ID)));
                    area.setLevel(cursor.getString(cursor
                            .getColumnIndex(AREAContract.AreaEntry.COLUMN_LEVEL)));
                    areaList.add(area);
                } while (cursor.moveToNext());
            }
            if (null != cursor) {
                cursor.close();
            }
        }
        if (db != null) {
            db.close();
        }
        return areaList;
    }
}
