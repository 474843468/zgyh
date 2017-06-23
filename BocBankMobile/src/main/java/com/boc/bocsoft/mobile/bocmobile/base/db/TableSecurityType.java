package com.boc.bocsoft.mobile.bocmobile.base.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 *
 */
public class TableSecurityType implements DatabaseHelper.DatabaseListener {

    private Context context;

    public class SecurityType {
        public static final String TABLE_NAME = "security_type";
        public static final String ID = "_id";
        public static final String NAME = "name";
        public static final String INDEX = "index";
    }

    public TableSecurityType(Context context) {
        this.context = context;
    }

    @Override
    public void onDatabaseCreate(SQLiteDatabase db) {
        String sql = "create table " + SecurityType.TABLE_NAME + "(_id integer primary key, name text,index integer not null)";
        db.execSQL(sql);
    }

    @Override
    public void onDatabaseUpdate(SQLiteDatabase db) {

    }

    private void insert(String name, int index) {
        String sql = "insert into security_type(name,index) value(" + name + "," + index + ")";
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        DatabaseHelper.getInstance(context).execSql(db, sql);
        db.close();
    }

    public int getSecurityType() {
        int type = -1;
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        Cursor cursor = DatabaseHelper.getInstance(context).query(db, true, "security_type", new String[]{"name,index"},
                null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            type = cursor.getInt(2);
            cursor.close();
        }
        db.close();
        return type;
    }

    public void update(String name, int index) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        if (getSecurityType() != -1) {
            String sql = "update security_type set name = " + name + ", index = " + index;
            DatabaseHelper.getInstance(context).execSql(db, sql);
        } else {
            insert(name, index);
        }
        db.close();
    }
}
