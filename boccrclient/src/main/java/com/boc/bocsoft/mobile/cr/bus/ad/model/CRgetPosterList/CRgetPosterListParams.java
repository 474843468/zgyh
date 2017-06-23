package com.boc.bocsoft.mobile.cr.bus.ad.model.CRgetPosterList;

/**
 * 广告接口
 * Created by eyding on 16/8/13.
 */
public class CRgetPosterListParams {

 private String releasePosition;//发布位置 0:首页、1:生活、2:投资、
  private String region;//地区  首页和投资默认送00，生活送真实地区编码，参见地区编码

  public void setRegion(String region) {
    this.region = region;
  }

  public void setReleasePosition(String releasePosition) {
    this.releasePosition = releasePosition;
  }
}
