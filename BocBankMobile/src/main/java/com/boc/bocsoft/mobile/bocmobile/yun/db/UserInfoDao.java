package com.boc.bocsoft.mobile.bocmobile.yun.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.boc.bocsoft.mobile.bocmobile.base.db.BaseDao;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.yun.model.UserInfoBean;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingeryue on 2016年10月25.
 */

public class UserInfoDao extends BaseDao {

  private final static String TABLE_NAME = "yunuserinfo";
  public final static String UPLOAD_WAIT = "0";//等待上传
  public final  static String UPLOAD_ED = "1";//已经上传

  //   photoUrl 头像url, yunBase64 云图片base64  localBase64 本地头像 uploadstate:上传状态,当localBase64有值时 切状态为需要上传时上传头像
  private static final String CREATE_TABLE =
      "create table if not exists "+TABLE_NAME+"(mobilePh text, custno text ,custnetno text, photoUrl text, yunBase64 text,localBase64 text, uploadstate text)" ;

  public static void createTable(SQLiteDatabase db){
    db.execSQL(CREATE_TABLE);
  }



  public void saveUser(String phone,String custNo,String custNetNo){
    String sql = "select count(*) from "+TABLE_NAME+" where mobilePh='"+phone+"' and custno='"+custNo+"' and custnetno ='"+custNetNo+"'";
    LogUtils.d("dding","sql:"+sql);
    Cursor cursor = db.rawQuery(sql, null);
    boolean hasuser = false;

    try {
      if(cursor != null && cursor.moveToNext()){
        int anInt = cursor.getInt(0);
        LogUtils.d("dding","---user size:"+anInt);

        hasuser =  anInt>0;
      }
    }finally {
      if(cursor != null)cursor.close();
    }

    if(hasuser){
      //怎么可能会变化
    }else{
      ContentValues values = new ContentValues();
      values.put("mobilePh",phone);
      values.put("custno",custNo);
      values.put("custnetno",custNetNo);

      db.beginTransaction();
      db.insert(TABLE_NAME,"",values);

      db.setTransactionSuccessful();
      db.endTransaction();

    }
  }


  public void saveLocalImage(String custno,String base64Image){

    if(!hasUser(custno)){
      return;
    }

    ContentValues values = new ContentValues();
    values.put("localBase64",base64Image);
    values.put("uploadstate",UPLOAD_WAIT);
    LogUtils.d("dding","db 保存头像:"+custno+" :"+base64Image);
    db.beginTransaction();
    db.update(TABLE_NAME,values," custno='"+custno+"'",null);
    db.setTransactionSuccessful();
    db.endTransaction();
  }

  /**
   * 保存云端头像
   * @param custno
   * @param url
   * @param base64Image
   */
  public void saveYunImage(String custno,String url,String base64Image){

    if(!hasUser(custno)){
      return;
    }

    ContentValues values = new ContentValues();
    values.put("photoUrl",url);
    values.put("yunBase64",base64Image);
    db.beginTransaction();
    db.update(TABLE_NAME,values,"custno='"+custno+"'",null);
    db.setTransactionSuccessful();
    db.endTransaction();
  }


  /**
   * 删除用户
   * @param custno
   */
  public void delUser(String custno){
    db.delete(TABLE_NAME," custno = '"+custno+"'",null);
  }


  /**
   * 获取头像 (base64) 优先本地
   * @param loginName
   * @return
   */
  public String getBase64Image(String loginName){
    if(!hasUserByLoginName(loginName))return null;
     String sql = " select yunBase64,localBase64 from "+TABLE_NAME +" where mobilePh='"+loginName+"'";

    Cursor cursor = db.rawQuery(sql, null);
    if(cursor == null)return null;

    try {

      if(cursor.moveToNext()){
        String local = cursor.getString(cursor.getColumnIndex("localBase64"));
        String yun = cursor.getString(cursor.getColumnIndex("yunBase64"));


        if(local != null && local.length()>1)return local;

        if(yun != null && yun.length()>1)return yun;
      }

    }finally {
      cursor.close();
    }
    return null;
  }

  public String getImageUrl(String custNo){
    String sql = "select photoUrl from "+TABLE_NAME+" where custno='"+custNo+"'";

    Cursor cursor = db.rawQuery(sql, null);
    if(cursor == null)return "";
    try {

      if(cursor.moveToNext()){
        return cursor.getString(0);
      }

    }finally {
      cursor.close();
    }
    return "";
  }

  /**
   * 是否需要下载数据
   * @param custno
   * @return
   */
  public boolean isNeedDownImage(String custno){

    if(!hasUser(custno))return false;

    String sql = "select photoUrl,yunBase64 from "+TABLE_NAME+" where custno='"+custno+"'";

    Cursor cursor = db.rawQuery(sql, null);

    if(cursor == null) return false;
    try {
      if(cursor.moveToNext()){
        String url = cursor.getString(cursor.getColumnIndex("photoUrl"));
        String base64 = cursor.getString(cursor.getColumnIndex("yunBase64"));

        if((url!= null && url.length()>1) && (base64==null || base64.length()<1)){
          return true;
        }
      }

    }finally {
      cursor.close();
    }

    return false;
  }

  /**
   * 获取需要上传的头像
   * @return
   */
  public List<UserInfoBean> getNeedUploadUser(){

    String sql = "select * from "+TABLE_NAME +" where uploadstate='"+UPLOAD_WAIT+"'" ;
    Cursor cursor = db.rawQuery(sql, null);
    if(cursor == null)return new ArrayList<>();

    List<UserInfoBean> beanList = new ArrayList<>();
    try {

      for(;;){
        boolean b = cursor.moveToNext();
        if(!b)break;

        UserInfoBean userInfoBean = new UserInfoBean();

        userInfoBean.setMobilePh(cursor.getString(cursor.getColumnIndex("mobilePh")));
        userInfoBean.setCustno(cursor.getString(cursor.getColumnIndex("custno")));
        userInfoBean.setCustnetno(cursor.getString(cursor.getColumnIndex("custnetno")));
        userInfoBean.setLocalBase64(cursor.getString(cursor.getColumnIndex("localBase64")));

        LogUtils.d("dding","--获取待上传头像:"+userInfoBean.getCustno()+" :" +userInfoBean.getLocalBase64());

        beanList.add(userInfoBean);
      }

    }finally {
      cursor.close();
    }
    return beanList;
  }


  public void updateUploadState(String custno,String state){
    ContentValues values = new ContentValues();
    values.put("uploadstate",state);

    db.beginTransaction();
    db.update(TABLE_NAME,values," custno ='"+custno+"'",null);
    db.setTransactionSuccessful();
    db.endTransaction();
  }

  public boolean hasUser(String custno){

    String sql = "select count(*) from "+TABLE_NAME+" where custno='"+custno+"'";

    Cursor cursor = db.rawQuery(sql, null);
    if(cursor == null) return false;

    try {

      if(cursor.moveToNext()){
        int anInt = cursor.getInt(0);
        return  anInt >0;
      }
    }finally {
      cursor.close();
    }
    return false;
  }


  public boolean hasUserByLoginName(String mobilePh){

    String sql = "select count(*) from "+TABLE_NAME+" where mobilePh='"+mobilePh+"'";

    Cursor cursor = db.rawQuery(sql, null);
    if(cursor == null) return false;

    try {

      if(cursor.moveToNext()){
        int anInt = cursor.getInt(0);
        return  anInt >0;
      }
    }finally {
      cursor.close();
    }
    return false;
  }

  public void clearData() {
    LogUtils.d("dding","----清除用户信息表:");
    db.execSQL("delete from "+TABLE_NAME);
  }
}
