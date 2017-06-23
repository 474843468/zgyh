package com.boc.bocsoft.mobile.bocmobile.yun.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.boc.bocsoft.mobile.bocmobile.base.db.BaseDao;
import com.boc.bocsoft.mobile.bocmobile.yun.model.DictVoBean;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dingeryue on 2016年10月25.
 * 码表dao , 和用户无关
 */

public class DictDao extends BaseDao {

  private final static String TABLE_NAME = "yundict";

  //   type:类型  dickKey:key  dictValue:value
  private static final String CREATE_TABLE =
      "create table if not exists "+TABLE_NAME+"(dictType text, dickKey text ,dictValue text)" ;

  public static void createTable(SQLiteDatabase db){
    db.execSQL(CREATE_TABLE);
  }

  /**
   * 更具key值获取value
   * @param dictKey
   * @return
   */
  public String getDictValue(String dictKey){

    String sql = "select dictValue from "+TABLE_NAME+" where dickKey='"+dictKey+"'";

    Cursor cursor = db.rawQuery(sql, null);
    if(cursor == null)return "";

    try {

      if(cursor.moveToNext()){
        String dictValue = cursor.getString(cursor.getColumnIndex("dictValue"));
        return dictValue;
      }

    }finally {
      cursor.close();
    }

    return "";
  }


  public HashMap<String,String> getDictValue(String ... keys){
    if(keys == null || keys.length == 0){
      return new HashMap<>();
    }

    StringBuilder sb = new StringBuilder();
    sb.append("(");
    for(String item : keys){
      sb.append("'").append(item).append("'").append(",");
    }
    String substring = sb.substring(0, sb.length() - 1)+")";

    String sql = "select  dickKey,dictValue from "+TABLE_NAME+" where dickKey in "+substring;

    Cursor cursor = db.rawQuery(sql, null);
    if(cursor == null)return new HashMap<>();

    HashMap<String,String> maps = new HashMap<>();

    try {

      String key;
      String value;
      for(;;){
        boolean b = cursor.moveToNext();
        if(!b)break;
       key = cursor.getString(cursor.getColumnIndex("dickKey"));
       value =
           cursor.getString(cursor.getColumnIndex("dictValue"));
        maps.put(key,value);
      }

    }finally {
      cursor.close();
    }
    return maps;
  }

  public void  updateDict(List<DictVoBean> list){
    if(list == null)return;

    try {
      db.execSQL("delete from "+TABLE_NAME);
    }catch ( Exception e){
    }


    db.beginTransaction();

    for(DictVoBean bean:list){

      ContentValues values = new ContentValues();

      values.put("dictType",bean.getType());
      values.put("dickKey",bean.getKey());
      values.put("dictValue",bean.getValue());

      db.insert(TABLE_NAME,"",values);
    }

    db.setTransactionSuccessful();
    db.endTransaction();
  }


  public boolean hasInit(){

    String sql = "select count(*) from "+TABLE_NAME;

    Cursor cursor = db.rawQuery(sql, null);
    if(cursor == null)return  false;

    try {
      if(cursor.moveToNext()){
        int anInt = cursor.getInt(0);
        return anInt>0;
      }else{
        return false;
      }
    }finally {
      cursor.close();
    }
  }

}
