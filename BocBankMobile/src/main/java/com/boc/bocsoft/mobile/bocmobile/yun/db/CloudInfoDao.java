package com.boc.bocsoft.mobile.bocmobile.yun.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.boc.bocsoft.mobile.bocmobile.base.db.BaseDao;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingeryue on 2016年10月24.
 */

public class CloudInfoDao extends BaseDao {

  private final static String TABLE_NAME = "cloudinfo";
  private final static String STATE_NEW = "new";
  public final static String STATE_UPDATED = "loaded";

  //userid:用户标示     trCode:交易code  state:状态0已查询,1未请求
  private static final String CREATE_TABLE =
      "create table if not exists "+TABLE_NAME+" (userid text, trCode text ," +
          "state text)" ;

  public static void createTable(SQLiteDatabase db){
    LogUtils.d("dding","----创建表:CloudInfoDao");
    db.execSQL(CREATE_TABLE);
  }

  public void clearData(String user){
    LogUtils.d("dding","----清除表:"+user);

  }
  public void clearData(){
    LogUtils.d("dding","----清除云信息表:");
    db.execSQL("delete from "+TABLE_NAME);
  }

  /**
   * 获取需要重新加载的交易ID
   * @return
   */
  public List<String> getNeedLoadTrCodes(String user){
    //过滤掉 state为0的
    String sql = "select * from "+TABLE_NAME +" where userid='"+user+"'";

    Cursor cursor = db.rawQuery(sql, null);
    if(cursor == null)return new ArrayList<>();

    ArrayList<String> results = new ArrayList<>();
    for(;;){
      boolean b = cursor.moveToNext();
      if (!b) break;
      String state = cursor.getString(cursor.getColumnIndex("state"));
      if(STATE_UPDATED.equals(state))continue;

      results.add(cursor.getString(cursor.getColumnIndex("trCode")));
    }
    return results;
  }

  /**
   * @param user
   * @param tradeids
   */
  public void updateTrades(String user,List<String> tradeids){
    String delSql = "delete from "+TABLE_NAME+" where userid = '"+user+"'";//先删除
    db.execSQL(delSql);
    //批量插入
    db.beginTransaction();
    for(String item:tradeids){
      execSaveSql(user,item,STATE_NEW);
    }
    execSaveSql(user,"isexist",STATE_UPDATED);
    db.setTransactionSuccessful();
    db.endTransaction();

  }

  /**
   * 更新交易状态
   * @param user
   * @param tradeId
   * @param state
   */
  public void updateState(String user,String tradeId,String state){
    ContentValues values = new ContentValues();
    values.put("state",state);
    //更新状态
    db.update(TABLE_NAME,values," userid = '"+user+"' and trCode = '"+tradeId+"'",null);
  }

  private void execSaveSql(String user,String trCode,String state){
    ContentValues values = new ContentValues();
    values.put("userid",user);
    values.put("trCode",trCode);
    values.put("state",STATE_NEW);
    db.insert(TABLE_NAME,"",values);
  }

  /**
   * 用户是否初始化过云数据 (更具用户数据是否存在判断)
   * @param user
   * @return
   */
  public boolean hasInit(String user) {
    String sql = "select count(*) from " + TABLE_NAME + " where userid='" + user + "'";
    Cursor cursor = db.rawQuery(sql, null);
    if (cursor == null) return false;
    try {

      if (cursor.moveToNext()) {
        int anInt = cursor.getInt(0);
        if (anInt > 0) {
          return true;
        } else {
          return false;
        }
      } else {
        return false;
      }
    } finally {
      if (cursor != null) cursor.close();
    }
  }


}
