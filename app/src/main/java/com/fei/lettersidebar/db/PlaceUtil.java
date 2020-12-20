package com.fei.lettersidebar.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: PlaceUtil
 * @Description: java类作用描述
 * @Author: Fei
 * @CreateDate: 2020-12-19 12:10
 * @UpdateUser: 更新者
 * @UpdateDate: 2020-12-19 12:10
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class PlaceUtil {

    private static final String TAG = "PlaceUtil";

    public static List<String> getCitys(Context context) {
        DBManager dbManager = new DBManager(context);
        SQLiteDatabase database = dbManager.openDataBase();
        if (database != null) {
            List<String> list = new ArrayList<String>();
            try {
                String sql = "select * from ffu_area where parentid>0 and parentid<33";
                Cursor cursor = database.rawQuery(sql, null);
                while (cursor.moveToNext()) {
                    Log.e(TAG, "有数据");
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    list.add(name);
                }
                return list;
            } catch (Exception e) {
                Log.e(TAG, "sql失败");
                e.printStackTrace();
            } finally {
                dbManager.closeDb();
            }
        }
        return null;
    }

}
