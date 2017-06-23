package com.boc.bocsoft.mobile.bocmobile.base.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库创建、更新、打开、操作管理
 * wangtong
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "boc_database.db";
    private static DatabaseHelper databaseHelper;
    private List<DatabaseListener> listeners;

    /**
     * 数据库创建和更新监听
     */
    public interface DatabaseListener {

        public void onDatabaseCreate(SQLiteDatabase db);

        public void onDatabaseUpdate(SQLiteDatabase db);
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 0);
        listeners = new ArrayList<DatabaseListener>();
    }

    private DatabaseHelper(Context context, int newVersion) {
        super(context, DATABASE_NAME, null, newVersion);
        listeners = new ArrayList<DatabaseListener>();
    }

    public synchronized static DatabaseHelper getInstance(Context context) {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context);
        }
        return databaseHelper;
    }

    public synchronized static DatabaseHelper getInstance(Context context, int newVersion) {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context, newVersion);
        }
        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateTables(db);
    }

    /**
     * 数据库创建的时候，调用这个方法来创建表。
     */
    protected synchronized void createTables(SQLiteDatabase db) {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onDatabaseCreate(db);
        }
    }

    /**
     * 数据库更新的时候，调用这个方法来创建表。
     */
    protected synchronized void updateTables(SQLiteDatabase db) {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onDatabaseUpdate(db);
        }
    }

    /**
     * 数据表的增删改调用这个方法
     */
    public synchronized void execSql(SQLiteDatabase db, String sql) {
        db.execSQL(sql);
    }

    /**
     * 数据表查询调用这个方法
     */
    public synchronized Cursor query(SQLiteDatabase db, boolean distinct, String table, String[] columns, String selection,
                                     String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return db.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    /**
     * 关闭数据库
     */
    public synchronized void close(SQLiteDatabase db) {
        db.close();
    }

    /**
     * 添加创建和更新监听
     */
    public void addDatabaseCreateListener(DatabaseListener lis) {
        listeners.add(lis);
    }
}
