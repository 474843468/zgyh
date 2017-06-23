package com.boc.bocsoft.mobile.bocmobile.yun.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.boc.bocsoft.mobile.bocmobile.base.db.BaseDao;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.yun.model.LoginfoBean;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingeryue on 2016年10月25.
 */

public class LoginInfoDao extends BaseDao {

  private final static String TABLE_NAME = "yunloginfo";

  /**
   * 网银客户id
   */
  public static final String FIELD_BOCNETCUSTNO = "bocnetCustNo";

  /**
   * 核心系统客户号
   */
  public static final String FIELD_BANCSCUSTNO = "bancsCustNo";

  /**
   * 客户注册手机号
   */
  public static final String FIELD_MOBILEPH = "mobilePh";

  /**
   * 登录时间
   */
  public static final String FIELD_LOGINTIME = "loginTime";

  /**
   * 正常退出时间
   */
  public static final String FIELD_LOGOUTTIME = "logoutTime";

  /**
   * 运行内存 GB
   */
  public static final String FIELD_RAM = "ram";
  /**
   * 存储空间 GB
   */
  public static final String FIELD_ROM = "rom";
  /**
   * 已使用的存储空间 GB
   */
  public static final String FIELD_USEDROM = "userdRom";
  /**
   * 分辨率
   */
  public static final String FIELD_RESOLUTION = "resolution";

  /**
   * 上传状态
   */
  private static final String FIELD_STATE = "uploadstate";

  /**
   * 记录id 自增
   */
  private static final String FIELD_ID = "loginid";

  /*
   * 创建时间
   */
  private static final String FIELD_CREATE = "createtime";

  /**
   * 已经上传
   */
  public static final String UPLOAD_ED = "uploaded";
  /**
   * 未上传,等待上传
   */
  public static final String UPLOAD_WAIT = "updaloadwait";

  //   type:类型  dickKey:key  dictValue:value
  private static final String CREATE_TABLE = "create table if not exists "
      + TABLE_NAME
      + "("
      +FIELD_ID +" integer primary key autoincrement,"
      + FIELD_BOCNETCUSTNO + " text,"
      + FIELD_BANCSCUSTNO + " text,"
      + FIELD_MOBILEPH + " text,"
      + FIELD_LOGINTIME + " text,"
      + FIELD_LOGOUTTIME + " text,"
      + FIELD_CREATE +" text,"

      + FIELD_RAM + " text,"
      + FIELD_ROM + " text,"
      + FIELD_USEDROM + " text,"
      + FIELD_RESOLUTION + " text,"
      + FIELD_STATE + " text"
      + ")";

  public static void createTable(SQLiteDatabase db) {
    LogUtils.d("dding","create LoginInfo table:"+CREATE_TABLE);
    db.execSQL(CREATE_TABLE);
  }

  public void insertRecord(ContentValues values){
    values.put(FIELD_STATE,UPLOAD_WAIT);
    values.put(FIELD_CREATE,System.currentTimeMillis());

    db.beginTransaction();
    db.insert(TABLE_NAME,"",values);
    db.setTransactionSuccessful();
    db.endTransaction();
  }

  public boolean updateLoginoutTime(String time){

    //查找最后一条数据
    String sql = "select "+FIELD_ID+" from "+TABLE_NAME+" order by "+FIELD_ID+" desc limit 0,1";
    Cursor cursor = db.rawQuery(sql, null);
    if(cursor == null)return false;

    int lastId = -1;
    try {
      if(cursor.moveToNext()){
        lastId = cursor.getInt(0);//最后id
      }
    }finally {
      cursor.close();
    }

    if(lastId == -1)return false;

    //存在  - 更新

    ContentValues values = new ContentValues();
    values.put(FIELD_LOGOUTTIME,time);
    db.beginTransaction();
    int update = db.update(TABLE_NAME, values, FIELD_ID + "='" + lastId + "'", null);
    db.setTransactionSuccessful();
    db.endTransaction();


    return update>0;
  }

  public boolean deleteRecord(int recordId){

    db.beginTransaction();

    db.delete(TABLE_NAME,FIELD_ID+"='"+recordId+"'",null);

    db.setTransactionSuccessful();
    db.endTransaction();

    return true;
  }

  public boolean deleterRecords(int ... records){

    if(records == null || records.length == 0)return true;

    db.beginTransaction();

    for(int item:records){
      db.delete(TABLE_NAME,FIELD_ID+"='"+item+"'",null);
    }

    db.setTransactionSuccessful();
    db.endTransaction();

    return true;
  }


  /**
   * 上传数据
   * @return
   */
  public List<LoginfoBean> getNeedUploadList() {
    //上传除去最后一条数据的所有数据,   最后一条数据判断logouttime是否为空,不为空也上传

    String sql = "select * from " + TABLE_NAME + " order by " + FIELD_ID + " desc ";

    Cursor cursor = db.rawQuery(sql, null);

    List<LoginfoBean> list = new ArrayList<>();

    if(cursor == null)return list;

    for(;;){
      if(!cursor.moveToNext()){
        break;
      }

      LoginfoBean bean = new LoginfoBean();


      bean.setLoginid(cursor.getInt(cursor.getColumnIndex(FIELD_ID)));
      bean.setBancsCustNo(cursor.getString(cursor.getColumnIndex(FIELD_BANCSCUSTNO)));
      bean.setBocnetCustNo(cursor.getString(cursor.getColumnIndex(FIELD_BOCNETCUSTNO)));

      bean.setLoginTime(cursor.getString(cursor.getColumnIndex(FIELD_LOGINTIME)));
      bean.setLogoutTime(cursor.getString(cursor.getColumnIndex(FIELD_LOGOUTTIME)));
      bean.setMobilePh(cursor.getString(cursor.getColumnIndex(FIELD_MOBILEPH)));

      bean.setRam(cursor.getString(cursor.getColumnIndex(FIELD_RAM)));
      bean.setRom(cursor.getString(cursor.getColumnIndex(FIELD_ROM)));
      bean.setUserdRom(cursor.getString(cursor.getColumnIndex(FIELD_USEDROM)));

      bean.setResolution(cursor.getString(cursor.getColumnIndex(FIELD_RESOLUTION)));
      bean.setCreatetime(cursor.getString(cursor.getColumnIndex(FIELD_CREATE)));
      bean.setUploadstate(cursor.getString(cursor.getColumnIndex(FIELD_STATE)));

      if(TextUtils.isEmpty(bean.getLoginTime()))continue;

      list.add(bean);
    }

   /* if(list.size()<2){
      list.clear();
    }else{
      list.remove(list.size()-1);
    }*/
    cursor.close();

    return list;
  }




}
