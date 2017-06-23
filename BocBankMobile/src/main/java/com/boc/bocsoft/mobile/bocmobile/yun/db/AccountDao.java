package com.boc.bocsoft.mobile.bocmobile.yun.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.boc.bocsoft.mobile.bocmobile.base.db.BaseDao;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.yun.model.AccountServiceBean;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingeryue on 2016年10月25.
 */

public class AccountDao extends BaseDao {

  private final static String TABLE_NAME = "yunaccount2";
  public final static String STATE_OLD = "0";
  public final static String STATE_NEW = "1";


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


  //userid:用户标示   accountType:账号类型  account:账号  state:状态0未上传,1已经上传
  private static final String CREATE_TABLE =
      "create table if not exists "+TABLE_NAME+" ("
          + FIELD_BOCNETCUSTNO+" text,"
          + FIELD_BANCSCUSTNO+" text,"
          + FIELD_MOBILEPH+" text,"
          + " accountType text ,account text, " +
          "state text)" ;

  public static void createTable(SQLiteDatabase db){
    db.execSQL(CREATE_TABLE);
  }

  /**
   * 更具类型获取账号
   * @param usre 核心客户号
   * @param type
   * @return
   */
  public String getAccount(String usre,String type){
    String sql = "select * from "+TABLE_NAME+" where "+FIELD_BANCSCUSTNO+"='"+usre+"' and accountType='"+type+"'";

    Cursor cursor = db.rawQuery(sql, null);
    if(cursor == null)return "";

    try{
      if(cursor.moveToNext()){
        return cursor.getString(cursor.getColumnIndex("account"));
      }
    }finally {
      cursor.close();
    }
    return null;
  }

  /**
   * 查询需要上传的account
   * @return 可上传的账户列表
   */
  public List<AccountServiceBean> getNeedUpdateAccounts() {

    String sql = " select * from "
        + TABLE_NAME
        + " where state='"
        + STATE_NEW
        + "'";
    Cursor cursor = db.rawQuery(sql, null);

    if (cursor == null) return new ArrayList<>();

    List<AccountServiceBean> list = new ArrayList<>();
    try {

      for (; ; ) {
        boolean b = cursor.moveToNext();
        if(!b)break;
          AccountServiceBean bean = new AccountServiceBean();
          bean.setLastAccId(cursor.getString(cursor.getColumnIndex("account")));
          bean.setServiceCode(cursor.getString(cursor.getColumnIndex("accountType")));
          bean.setBocnetCustNo(cursor.getString(cursor.getColumnIndex(FIELD_BOCNETCUSTNO)));
          bean.setBancsCustNo(cursor.getString(cursor.getColumnIndex(FIELD_BANCSCUSTNO)));
          bean.setMobilePh(cursor.getString(cursor.getColumnIndex(FIELD_MOBILEPH)));

          if(TextUtils.isEmpty(bean.getBancsCustNo()) || TextUtils.isEmpty(bean.getBocnetCustNo()) || TextUtils.isEmpty(bean.getMobilePh())){
            continue;
          }

          list.add(bean);
      }
    } finally {
        cursor.close();
    }
    return list;
  }

  /**
   * 批量更新  用于批量上传后
   * @param beanList 账户列表
   * @return 是否更新成功
   */
  public boolean updateAccountList(List<AccountServiceBean> beanList){

    db.beginTransaction();

    for(AccountServiceBean bean:beanList){
      execSaveOrUpdate(bean.getBancsCustNo(),bean.getBocnetCustNo(),bean.getMobilePh(),bean.getServiceCode(),bean.getLastAccId(),bean.getState());
    }

    db.setTransactionSuccessful();
    db.endTransaction();

    return true;
  }

  /**
   * 更新 用于调用者保存账号 注意保存时state值
   * @param custNo no
   * @param custNetNo netno
   * @param mobile 手机号
   * @param type 类型
   * @param account 账号
   * @param state 状态
   * @return 是否更新成功
   */
  public boolean updateAccount(String custNo,String custNetNo,String mobile,String type,String account,String state){

    execSaveOrUpdate(custNo,custNetNo,mobile,type,account,state);

    return true;
  }

  private boolean execSaveOrUpdate(String custNo,String custNetNo,String mobile,String type,String account,String state){
    //判断是否存在 存在更新不存在插入
    String accountE = getAccount(custNo, type);

    ContentValues values = new ContentValues();

    values.put(FIELD_BANCSCUSTNO,custNo);
    values.put(FIELD_BOCNETCUSTNO,custNetNo);
    values.put(FIELD_MOBILEPH,mobile);

    values.put("accountType",type);
    values.put("account",account);
    values.put("state",state);


    if(accountE == null || accountE.length() == 0){
      //插入
      db.insert(TABLE_NAME,"",values);
    }else{

      //更新
      db.update(TABLE_NAME,values," "+FIELD_BANCSCUSTNO+"='"+custNo+"' and accountType ='"+type+"'",null);
    }

    return true;
  }

  public void clearData() {
    LogUtils.d("dding","----清除最后交易id表:");
    db.execSQL("delete from "+TABLE_NAME);
  }
}
