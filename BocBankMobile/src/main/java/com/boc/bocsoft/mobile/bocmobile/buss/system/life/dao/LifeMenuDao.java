package com.boc.bocsoft.mobile.bocmobile.buss.system.life.dao;

import android.content.ContentValues;
import android.database.Cursor;
import com.boc.bocsoft.mobile.bocmobile.base.db.BaseDao;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.model.LifeMenuModel;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingeryue on 2016年09月01.
 */
public class LifeMenuDao extends BaseDao {

  private final String TABLE = "life_menu3";

  /**
   * 保存地区菜单
   */
  public void saveCityMenu(List<LifeMenuModel> menuModels, String user, String cityCode) {
    if (menuModels == null || menuModels.size() == 0) return;

    boolean has = hsInit(user);
    //插入新数据
    db.beginTransaction();
    delCityMenus(user, cityCode);

    int index = 0;
    for (LifeMenuModel menuModel : menuModels) {

      ContentValues values = new ContentValues();

      values.put("bocuser", user);

      values.put("typeId", menuModel.getTypeId());
      values.put("menuId", menuModel.getMenuId());
      values.put("catId", menuModel.getCatId());
      values.put("isAvailable", menuModel.getIsAvalid());
      values.put("cityDispNo", cityCode);
      values.put("orderIndex", index);
      values.put("menuname", menuModel.getCatName());

      //values.put("flowFileId",menuModel.getFlowFileId());
      //values.put("merchantName",menuModel.getMerchantName());

      values.put("cityDispNo",menuModel.getCityDispNo());
      values.put("cityDispName",menuModel.getCityDispName());
      values.put("prvcDispName",menuModel.getPrvcDispName());
      values.put("prvcShortName",menuModel.getPrvcShortName());

      db.insert(TABLE, "", values);

      index++;
    }


    //插入占位
    if(!has){
      ContentValues values = new ContentValues();
      values.put("bocuser", user);
      values.put("typeId", LifeMenuModel.TYPE_PLACEHOLER);
      values.put("cityDispNo",cityCode);
      db.insert(TABLE, "", values);
    }

    db.setTransactionSuccessful();
    db.endTransaction();
  }

  private void savePlaceHolder(String user){

  }


  /**
   * 保存用户常用菜单
   */
  public void saveUserCommonMenu(List<LifeMenuModel> menuModels, String user) {
    if (menuModels == null || menuModels.size() == 0 || StringUtils.isEmptyOrNull(user)) return;

    db.beginTransaction();

    delUserCommonMenu(user);

    int index = 0;
    for (LifeMenuModel menuModel : menuModels) {

      ContentValues values = new ContentValues();

      values.put("bocuser", user);
      values.put("typeId", menuModel.getTypeId());
      values.put("menuId", menuModel.getMenuId());
      values.put("catId", menuModel.getCatId());
      values.put("isAvailable", menuModel.getIsAvalid());
      values.put("orderIndex", index);
      values.put("menuname", menuModel.getCatName());

      values.put("flowFileId",menuModel.getFlowFileId());
      values.put("merchantName",menuModel.getMerchantName());

      values.put("cityDispNo",menuModel.getCityDispNo());
      values.put("cityDispName",menuModel.getCityDispName());
      values.put("prvcDispName",menuModel.getPrvcDispName());
      values.put("prvcShortName",menuModel.getPrvcShortName());

      db.insert(TABLE, "", values);

      index++;
    }

    db.setTransactionSuccessful();
    db.endTransaction();
  }

  /**
   * 删除用户常用菜单
   */
  public void delUserCommonMenu(String user) {
    db.execSQL("delete from " + TABLE + " where bocuser='" + user + "' and typeId='" + user + "'");
  }

  /**
   * 删除地区菜单
   */
  public void delCityMenus(String user, String cityCode) {
    db.execSQL(
        "delete from " + TABLE + " where bocuser='" + user + "' and cityDispNo='" + cityCode + "' and typeId='"+LifeMenuModel.TYPE_CITY+"'");
  }

  /**
   * 保存用户菜单  (用户常用 && 地区菜单)
   */
  public void saveMenuSort(List<LifeMenuModel> menuModels, String user, String cityCode) {
    if (menuModels == null) return;

    // 分类
    List<LifeMenuModel> commonList = new ArrayList<>();
    List<LifeMenuModel> cityList = new ArrayList<>();

    for (LifeMenuModel menuModel : menuModels) {
      if (LifeMenuModel.TYPE_CITY.equals(menuModel.getTypeId())) {
        cityList.add(menuModel);
      } else if (user != null && user.equals(menuModel.getTypeId())) {
        commonList.add(menuModel);
      }
    }
    //进行 插入


    //地区菜单
    saveCityMenu(cityList, user, cityCode);
    //用户常用
    saveUserCommonMenu(commonList, user);
  }

  /**
   * 保存用户菜单
   * @param user
   */
 /* public void saveMenuSort2(List<LifeMenuModel> menuModels,String user,String cityCode){
    //if(menuModels == null && menuModels.size()>0)return;

    if(menuModels == null){
      menuModels = new ArrayList<>();
    }

    //保存之前先删除
    delMenuOrder(user,cityCode);

    db.beginTransaction();
    int index = 0;
    for(LifeMenuModel menuModel:menuModels){
      //if(menuModel.getCatId() == null)continue;//更多没有id
      execSaveSortSql(user,menuModel,cityCode,index);
      index++;
    }

    //插入占位
    LifeMenuModel menuModel = new LifeMenuModel();
    menuModel.setTypeId(LifeMenuModel.TYPE_PLACEHOLER);
    execSaveSortSql(user,menuModel,cityCode,index);

    db.setTransactionSuccessful();
    db.endTransaction();

  }*/


/*  private void execSaveSortSql(String user,LifeMenuModel menuModel,String cityCode,int sortIndex){

    ContentValues values = new ContentValues();

    values.put("bocuser",user);

    values.put("typeId",menuModel.getTypeId());
    values.put("menuId",menuModel.getMenuId());
    values.put("catId",menuModel.getCatId());
    values.put("isAvailable",menuModel.getIsAvalid());
    values.put("cityDispNo",cityCode);
    values.put("orderIndex",sortIndex);
    values.put("menuname",menuModel.getCatName());

    db.insert(TABLE,"",values);

  }*/

/*  public void delMenuOrder(String user,String cityCode){
    //删除旧顺序
    db.execSQL("delete from "+TABLE+" where bocuser='"+user+"' and cityDispNo='"+cityCode+"'");

  }*/

  public boolean hsInit(String user){
    String sql = "select count(*) from "+TABLE+" where bocuser='"+user+"'";
    Cursor cursor = db.rawQuery(sql, null);
    try {
      if(cursor !=null && cursor.moveToNext()){
        int anInt = cursor.getInt(0);
        if(anInt>0)return true;
      }
    }finally {
      if(cursor != null){
        cursor.close();
      }
    }
   return false;
  }

  /**
   * 查询是否 某个用户&& 某个地区  有初始化过(初始化 和 用户删除所有)
   */
  /*
  public boolean hasAOrder(String user, String cityCode) {
    String sql = "select count(*) from "
        + TABLE
        + " where bocuser='"
        + user
        + "' and cityDispNo='"
        + cityCode
        + "'";
    Cursor cursor = db.rawQuery(sql, null);
    if (cursor != null && cursor.moveToFirst()) {
      return cursor.getInt(0) > 0;
    }
    return false;
  }
  */

  /**
   * 获取用户某个城市的全菜单项
   */
  public List<LifeMenuModel> getMenus(String user, String cityCode) {
    List<LifeMenuModel> menuModels = new ArrayList<>();

    menuModels.addAll(getUserCommonUse(user, cityCode));
    menuModels.addAll(getCityMenus(cityCode));

    return menuModels;
  }

  /**
   * 获取用户常用
   */
  public List<LifeMenuModel> getUserCommonUse(String user, String cityCode) {
    String sql = "select * from " + TABLE + " where bocuser='" + user + "' and typeId = '"+user+"'order by orderIndex asc";

    Cursor cursor = db.rawQuery(sql, null);
    try {
      List<LifeMenuModel> menuModels = cursor2Model(cursor);
      if (menuModels != null && menuModels.size() > 0) {
        for (LifeMenuModel menuModel : menuModels) {
          menuModel.setCityDispNo(cityCode);
        }
      }

      return menuModels;
    } finally {
      if (cursor != null) cursor.close();
    }
  }

  /**
   * 获取地区菜单
   */
  public List<LifeMenuModel> getCityMenus(String cityCode) {
    String sql =
        "select * from " + TABLE + " where cityDispNo='" + cityCode + "' and typeId='"+LifeMenuModel.TYPE_CITY+"'   order by orderIndex asc";

    Cursor cursor = db.rawQuery(sql, null);
    try {
      return cursor2Model(cursor);
    } finally {
      if (cursor != null) cursor.close();
    }
  }

  private List<LifeMenuModel> cursor2Model(Cursor cursor) {

    if (cursor == null) return new ArrayList<>();

    List<LifeMenuModel> menuModelList = new ArrayList<>();

    for (; ; ) {
      boolean b = cursor.moveToNext();
      if (!b) break;
      LifeMenuModel menuModel = new LifeMenuModel();

      String typeId = cursor.getString(cursor.getColumnIndex("typeId"));
      if (LifeMenuModel.TYPE_PLACEHOLER.equals(typeId)) {
        continue;
      }
      menuModel.setTypeId(typeId);
      menuModel.setMenuId(cursor.getString(cursor.getColumnIndex("menuId")));
      menuModel.setCatId(cursor.getString(cursor.getColumnIndex("catId")));

      menuModel.setIsAvalid(cursor.getString(cursor.getColumnIndex("isAvailable")));
      menuModel.setCityDispNo(cursor.getString(cursor.getColumnIndex("cityDispNo")));
      menuModel.setOrderIndex(cursor.getInt(cursor.getColumnIndex("orderIndex")));
      menuModel.setCatName(cursor.getString(cursor.getColumnIndex("menuname")));

      menuModel.setFlowFileId(cursor.getString(cursor.getColumnIndex("flowFileId")));
      menuModel.setMerchantName(cursor.getString(cursor.getColumnIndex("merchantName")));

      menuModel.setCityDispName(cursor.getString(cursor.getColumnIndex("cityDispName")));
      menuModel.setPrvcDispName(cursor.getString(cursor.getColumnIndex("prvcDispName")));
      menuModel.setPrvcShortName(cursor.getString(cursor.getColumnIndex("prvcShortName")));


      menuModelList.add(menuModel);
    }

    return menuModelList;
  }

  /**
   * 查询某个用户某个地区的菜单
   * @param user
   * @param cityCode
   * @return
   */
 /* public List<LifeMenuModel> getUserLocationMenuSort2(String user,String cityCode){
    String sql = "select * from "+TABLE+" where bocuser='"+user+"' and cityDispNo='"+cityCode+"' order by orderIndex asc";

    LogUtils.d("dding","----查询用户菜单,用户:"+user+"  "+cityCode+"  sql:"+sql);

    Cursor cursor = db.rawQuery(sql,null);

    if(cursor == null)return new ArrayList<>();

    List<LifeMenuModel> menuModelList = new ArrayList<>();
    for (; ; ) {
      boolean b = cursor.moveToNext();
      if (!b) break;
      LifeMenuModel menuModel = new LifeMenuModel();

      String typeId = cursor.getString(cursor.getColumnIndex("typeId"));
      if(LifeMenuModel.TYPE_PLACEHOLER.equals(typeId)){
        continue;
      }
      menuModel.setTypeId(typeId);
      menuModel.setMenuId(cursor.getString(cursor.getColumnIndex("menuId")));
      menuModel.setCatId(cursor.getString(cursor.getColumnIndex("catId")));

      menuModel.setIsAvalid(cursor.getString(cursor.getColumnIndex("isAvailable")));
      menuModel.setCityDispNo(cursor.getString(cursor.getColumnIndex("cityDispNo")));
      menuModel.setOrderIndex(cursor.getInt(cursor.getColumnIndex("orderIndex")));
      menuModel.setCatName(cursor.getString(cursor.getColumnIndex("menuname")));

      menuModelList.add(menuModel);

    }

    return menuModelList;

  }
*/

  /*public void saveUserCommons(String user,List<LifeMenuModel> modeList){
    //TOOD save之前先清除
    LogUtils.d("dding","---保存用户 菜单:");
    execSQL("delete from life_menu where typeId = '"+user+"'");
    LogUtils.d("dding","---保存用户 菜单   旧菜单删除成功: 开始插入");

   execBatchInsert(modeList);
    LogUtils.d("dding","---保存地区 菜单  插入成功 --->");
  }

  public void saveCitys(List<LifeMenuModel> modeList,String cityNo){
    LogUtils.d("dding","---保存地区 菜单:");
    execSQL("delete from life_menu where typeId = '"+LifeMenuModel.TYPE_CITY+"' and cityDispNo='"+cityNo+"'");
    LogUtils.d("dding","---保存地区 菜单   旧菜单删除成功: 开始插入");
    //批量插入
    execBatchInsert(modeList);
    LogUtils.d("dding","---保存地区 菜单  插入成功 --->");
  }

  public void saveCountrys(List<LifeMenuModel> modeList){
    //TODO save之前先清除
    execSQL("delete from life_menu where typeId = '"+LifeMenuModel.TYPE_COUNTRY+"'");
    execBatchInsert(modeList);
  }
*/


  /*private void execBatchInsert(List<LifeMenuModel> modeList){
    db.beginTransaction();
    int index = 0;
    for(LifeMenuModel mode:modeList){
      execInsertSQL(mode, index);
      index++;
    }
    db.setTransactionSuccessful();
    db.endTransaction();
  }

  private void execInsertSQL(LifeMenuModel mode,int index){
    String sql =
        "insert into life_menu(typeId,menuId,catId,isAvalid,catName,prvcDispName,cityDispName,prvcShortName,cityDispNo,orderIndex)  values("
            + "'"+mode.getTypeId()
            + "','"
            + mode.getMenuId()
            + "','"
            + mode.getCatId()
            + "','"
            + mode.getIsAvalid()
            + "','"
            + mode.getCatName()
            + "','"
            + mode.getPrvcDispName()
            + "','"
            + mode.getCityDispName()
            + "','"
            + mode.getPrvcShortName()
            + "','"
            + mode.getCityDispNo()
            + "',"
            + index+")";
    execSQL(sql);
  }*/
}
