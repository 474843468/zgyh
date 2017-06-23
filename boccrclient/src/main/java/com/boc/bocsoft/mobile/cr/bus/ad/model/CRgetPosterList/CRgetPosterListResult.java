package com.boc.bocsoft.mobile.cr.bus.ad.model.CRgetPosterList;

import java.util.List;

/**
 * 广告
 */
public class CRgetPosterListResult {

  private List<PosterBean> rows;

  public List<PosterBean> getArrayList() {
    return rows;
  }

  public static class PosterBean {

    private String name;//广告名称
    private String type;//广告类型 0:产品 1:活动
    private String region;//地区
    private String releasePosition;  //发布位置
    private String posterUrl;//广告链接

    private String posterOrder;//轮播顺序	0．.4，升序
    private String imageUrl;//图片Url	Apache服务器地址

    private String productCode;//产品码	广告类型为产品时有产品码
    private String productNature;//产品性质	0：结构性，1类基金

    public String getReleasePosition() {
      return releasePosition;
    }

    public String getPosterOrder() {
      return posterOrder;
    }

    public String getRegion() {
      return region;
    }

    public String getPosterName() {
      return name;
    }

    public String getPosterType() {
      return type;
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
  }
}
