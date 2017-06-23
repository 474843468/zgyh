package com.boc.bocsoft.mobile.bocmobile.buss.system.life.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dingeryue on 2016年08月24.
 *
 * 生活缴费的菜单， （db存储）
 */
public class LifeMenuModel implements Parcelable{

  public static final String TYPE_CITY= "city";
  public static final String TYPE_COUNTRY = "country";
  public static final String TYPE_RECORD  = "record";//缴费记录
  public static final String TYPE_PLACEHOLER = "placeholder";//类型 , 标示用户是全删除了 还是未初始化过
  private String typeId;// 用户常用时为loginName ,地区全部时为city 全国时为country

  private String menuId;
  private String catId;
  private String isAvalid;
  private String displayFlag = "1";//是否在终端展示，0：不展示， 1：展示
  private String catName; //名称
  //全部菜单仅有上面数据   (常用缴费也有上面的)
  private String prvcDispName;//吉林
  private String cityDispName;//四平
  private String prvcShortName;//JL
  private String cityDispNo;

  private String flowFileId	  ;//流程文件ID	String
  private String merchantName	;//商户名称	String


  //上面参数  全部菜单 也应该从请求参数获取设置进来
  private int orderIndex;

  private String resName;

  public LifeMenuModel(){}

  protected LifeMenuModel(Parcel in) {
    typeId = in.readString();
    menuId = in.readString();
    catId = in.readString();
    isAvalid = in.readString();
    catName = in.readString();
    prvcDispName = in.readString();
    cityDispName = in.readString();
    prvcShortName = in.readString();
    cityDispNo = in.readString();
    flowFileId = in.readString();
    merchantName = in.readString();
    orderIndex = in.readInt();
    resName = in.readString();
    displayFlag=in.readString();
  }

  public static final Creator<LifeMenuModel> CREATOR = new Creator<LifeMenuModel>() {
    @Override public LifeMenuModel createFromParcel(Parcel in) {
      return new LifeMenuModel(in);
    }

    @Override public LifeMenuModel[] newArray(int size) {
      return new LifeMenuModel[size];
    }
  };

  public String getMenuId() {
    return menuId;
  }

  public void setMenuId(String menuId) {
    this.menuId = menuId;
  }

  public String getCatId() {
    return catId;
  }

  public void setCatId(String catId) {
    this.catId = catId;
  }

  public String getIsAvalid() {
    return isAvalid;
  }

  public void setIsAvalid(String isAvalid) {
    this.isAvalid = isAvalid;
  }

  public String getCatName() {
    return catName;
  }

  public void setCatName(String catName) {
    this.catName = catName;
  }

  public String getPrvcDispName() {
    return prvcDispName;
  }

  public void setPrvcDispName(String prvcDispName) {
    this.prvcDispName = prvcDispName;
  }

  public String getCityDispName() {
    return cityDispName;
  }

  public void setCityDispName(String cityDispName) {
    this.cityDispName = cityDispName;
  }

  public String getPrvcShortName() {
    return prvcShortName;
  }

  public String getMerchantName() {
    return merchantName;
  }

  public void setMerchantName(String merchantName) {
    this.merchantName = merchantName;
  }

  public void setPrvcShortName(String prvcShortName) {
    this.prvcShortName = prvcShortName;
  }

  public String getCityDispNo() {
    return cityDispNo;
  }

  public void setCityDispNo(String cityDispNo) {
    this.cityDispNo = cityDispNo;
  }

  public void setTypeId(String typeId) {
    this.typeId = typeId;
  }

  public void setOrderIndex(int orderIndex) {
    this.orderIndex = orderIndex;
  }

  public String getFlowFileId() {
    return flowFileId;
  }

  public void setFlowFileId(String flowFileId) {
    this.flowFileId = flowFileId;
  }

  public String getTypeId() {
    return typeId;
  }

  public String getResName() {
    return resName;
  }

  public void setResName(String resName) {
    this.resName = resName;
  }

  public boolean isDisplay(){
    return !"0".equalsIgnoreCase(displayFlag);
  }

  public void setDisplayFlag(String displayFlag) {
    this.displayFlag = displayFlag;
  }

  // 菜单唯一性 ： type(用户常用，地区全部，全国) + catId
  public String generateId(){
    return  typeId+catId+menuId;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(typeId);
    dest.writeString(menuId);
    dest.writeString(catId);
    dest.writeString(isAvalid);
    dest.writeString(catName);
    dest.writeString(prvcDispName);
    dest.writeString(cityDispName);
    dest.writeString(prvcShortName);
    dest.writeString(cityDispNo);
    dest.writeString(flowFileId);
    dest.writeString(merchantName);
    dest.writeInt(orderIndex);
    dest.writeString(resName);
    dest.writeString(displayFlag);
  }

  @Override public boolean equals(Object o) {
    if(this == o)return true;


    if(!(o instanceof LifeMenuModel))return false;

    LifeMenuModel other = (LifeMenuModel) o;

    return  generateId().equals(other.generateId());
   //return other.getMenuId()!=null && other.getMenuId().equals(getMenuId()) && other.getCatId()!=null && other.getCatId().equals(getCatId());
  }

  @Override public String toString() {
    return "LifeMenuModel{" +
        "typeId='" + typeId + '\'' +
        ", menuId='" + menuId + '\'' +
        ", catId='" + catId + '\'' +
        ", isAvalid='" + isAvalid + '\'' +
        ", catName='" + catName + '\'' +
        ", prvcDispName='" + prvcDispName + '\'' +
        ", cityDispName='" + cityDispName + '\'' +
        ", prvcShortName='" + prvcShortName + '\'' +
        ", cityDispNo='" + cityDispNo + '\'' +
        ", flowFileId='" + flowFileId + '\'' +
        ", merchantName='" + merchantName + '\'' +
        ", orderIndex=" + orderIndex +
        ", resName='" + resName + '\'' +
        '}';
  }
}
