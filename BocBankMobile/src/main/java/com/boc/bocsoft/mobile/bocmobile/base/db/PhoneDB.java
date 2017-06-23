package com.boc.bocsoft.mobile.bocmobile.base.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * 移动手机数据库
 * Created by Administrator on 2016/5/21.
 */
public class PhoneDB {

    private static final String DATABASE_NAME = "phone_bacnk_db";

    // 数据库表名
    private static final String CREATE_TABLE_BLOGS = "create table if not exists accounts (id integer primary key autoincrement, "
            + "url text, blogName text, username text, password text, imagePlacement text, centerThumbnail boolean, fullSizeImage boolean, maxImageWidth text, maxImageWidthId integer);";
    private SQLiteDatabase db;
    private Context mContext;



    public PhoneDB(Context ctx) {
        this.mContext = ctx;
        db = ctx.openOrCreateDatabase(DATABASE_NAME, 0, null);

        // Create tables if they don't exist 新建数据库表
        // TODO
        db.execSQL(CREATE_TABLE_BLOGS);



    }

    public SQLiteDatabase getDatabase() {
        return db;
    }

    public static void deleteDatabase(Context ctx) {
        ctx.deleteDatabase(DATABASE_NAME);
    }
}
