package com.fei.lettersidebar.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fei.lettersidebar.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @ClassName: DBManager
 * @Description: java类作用描述
 * @Author: Fei
 * @CreateDate: 2020-12-19 11:43
 * @UpdateUser: 更新者
 * @UpdateDate: 2020-12-19 11:43
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class DBManager {

    private static final String TAG = "DBManager";
    private final int BUFFER_SIZE = 1024;
    public static final String DB_NAME = "city_cn.db";
    public static final String PACKAGE_NAME = "com.fei.letter";
    private SQLiteDatabase mDataBase;
    private Context mContext;

    public DBManager(Context context) {
        mContext = context;
    }

    public SQLiteDatabase openDataBase() {
        String dbPath = mContext.getCacheDir() + File.separator + PACKAGE_NAME;
        if (!new File(dbPath).exists()) {
            new File(dbPath).mkdirs();
        }
        this.mDataBase = openDataBase(dbPath + "/" + DB_NAME);
        return mDataBase;
    }

    private SQLiteDatabase openDataBase(String dbPath) {
        try {
            File file = new File(dbPath);
            if (!file.exists()) {
                Log.e(TAG, "数据库不存在");
                //不存在
                InputStream inputStream = mContext.getResources().openRawResource(R.raw.ffu365);
                if (inputStream == null) {
                    Log.e(TAG, "读取raw失败");
                    return null;
                }
                FileOutputStream fileOutputStream = new FileOutputStream(dbPath);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = inputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer);
                    fileOutputStream.flush();
                }
                fileOutputStream.close();
                inputStream.close();
            }
            mDataBase = SQLiteDatabase.openOrCreateDatabase(dbPath, null);
            Log.e(TAG, "创建数据库成功");
            return mDataBase;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeDb() {
        if (mDataBase != null) {
            mDataBase.close();
        }
    }

}
