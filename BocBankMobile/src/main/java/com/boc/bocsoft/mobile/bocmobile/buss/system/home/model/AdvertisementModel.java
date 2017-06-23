package com.boc.bocsoft.mobile.bocmobile.buss.system.home.model;

/**
 * 广告Model
 * Created by lxw on 2016/5/25.
 */
public class AdvertisementModel {

  /**
   * 地区
   */
  private String region;

  /**
   * 广告名称
   */
  private String posterName;

  /**
   * 广告类型 广告类型 0:产品 1:活动
   */
  private String posterType;

  /**
   * 广告链接（活动时有）
   */
  private String posterUrl;

  /**
   * 产品码 （类型为产品时有）
   */
  private String productCode;

  /**
   * 产品性质 0：结构性 1：类基金
   */
  private String productNature;
  /**
   * 图片URL
   */
  private String imageUrl;

  private String posterOrder;//

  private int placeHolderImageRes;//占位图片

  public String getRegion() {
    return region;
  }

  public String getPosterName() {
    return posterName;
  }

  public String getPosterType() {
    return posterType;
  }

  public String getPosterUrl() {
    return posterUrl;
  }

  public String getProductCode() {
    return productCode;
  }

  public String getProductNature() {
    return productNature;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public String getPosterOrder() {
    return posterOrder;
  }

  public int getPlaceHolderImageRes() {
    return placeHolderImageRes;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public void setPosterName(String posterName) {
    this.posterName = posterName;
  }

  public void setPosterType(String posterType) {
    this.posterType = posterType;
  }

  public void setPosterUrl(String posterUrl) {
    this.posterUrl = posterUrl;
  }

  public void setProductCode(String productCode) {
    this.productCode = productCode;
  }

  public void setProductNature(String productNature) {
    this.productNature = productNature;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public void setPosterOrder(String posterOrder) {
    this.posterOrder = posterOrder;
  }

  public void setPlaceHolderImageRes(int placeHolderImageRes) {
    this.placeHolderImageRes = placeHolderImageRes;
  }
}
