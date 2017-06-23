package com.boc.bocsoft.mobile.bocmobile.yun.db;

import android.database.sqlite.SQLiteDatabase;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;

/**
 * Created by dingeryue on 2016年10月24.
 */

public class YunDB {

  /**
   * 云DB建立表
   * @param db
   */
  public static void createTable(SQLiteDatabase db){
    LogUtils.d("dding","-----yun 初始化");
    CloudInfoDao.createTable(db);
    AccountDao.createTable(db);
    DictDao.createTable(db);
    UserInfoDao.createTable(db);
    LoginInfoDao.createTable(db);
  }
}
