package com.chinamworld.bocmbci.fidget;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BTCDBManager extends SQLiteOpenHelper {
	BTCDBManager(Context context, String name) {
		super(context, name, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// modify by xxh  2012-1-5  增加三个字段，强制更新，全国标识，下标
		db.execSQL("CREATE TABLE " + BTCConstant.FIDGET
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ BTCConstant.FIDGET_ID + " TEXT," + BTCConstant.FIDGET_CITY
				+ " TEXT," + BTCConstant.FIDGET_NAME + " TEXT,"
				+ BTCConstant.FIDGET_URL + " TEXT," + BTCConstant.FIDGET_VERSION
				+ " TEXT," + BTCConstant.FIDGET_MD5 + " TEXT,"
				+ BTCConstant.FIDGET_INDEXMD5 + " TEXT,"
				+ BTCConstant.FIDGET_Owned + " TEXT,"
				+ BTCConstant.FIDGET_Nationwide + " TEXT,"
				+ BTCConstant.FIDGET_Index_org + " TEXT)");
	   // end
	}

	public void execute(String sql){
		getWritableDatabase().execSQL(sql);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 更改数据库版本的操作
	}

	public Cursor query(String sql, String[] args) {
		return getReadableDatabase().rawQuery(sql, args);
	}

	public void close(){
		getReadableDatabase().close();
	}
}
