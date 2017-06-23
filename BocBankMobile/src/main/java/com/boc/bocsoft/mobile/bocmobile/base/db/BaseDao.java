package com.boc.bocsoft.mobile.bocmobile.base.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;

/**
 * 基础的Dao类
 * Created by lxw on 2016/6/13.
 */
public class BaseDao {

    protected SQLiteDatabase db;
    public BaseDao(){
        db = ApplicationContext.mobileDB.getDatabase();
    }
    /**
     * 数据表的增删改调用这个方法
     */
    public synchronized void execSQL(String sql) {
        db.execSQL(sql);
    }

    /**
     * 数据表的增删改调用这个方法
     */
    public synchronized void execSQL(String sql,  Object[] bindArgs) {
        db.execSQL(sql, bindArgs);
    }
    /**
     * 数据表查询调用这个方法
     */
    public synchronized Cursor query(String sql, String[] selectionArgs) {
        return db.rawQuery(sql, selectionArgs);
    }
}
