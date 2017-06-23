package com.boc.bocsoft.mobile.mbcg.activityinfo;

import java.util.List;

/**
 * Created by dingeryue on 2016年10月26.
 */

public class ActivityInfoResult {


  private List<ActivityBean> activityList;

  public static class ActivityBean{

    private String activityUrl	;//活动链接	String
    private String startTime		;//起始时间	String	结束时间大于开始时间
    private String endTime			;//结束时间	String
    private String isUsed			;//是否开启	String	0：关闭 1：开启
    private String keyId				;//活动唯一标识	String	同一个产品的唯一标识，按产品分
    private String acdescribe	;//活动描述	String
    private String activityName;//活动名称	String

    public String getActivityUrl() {
      return activityUrl;
    }

    public String getStartTime() {
      return startTime;
    }

    public String getEndTime() {
      return endTime;
    }

    public String getIsUsed() {
      return isUsed;
    }

    public String getKeyId() {
      return keyId;
    }

    public String getAcdescribe() {
      return acdescribe;
    }

    public String getActivityName() {
      return activityName;
    }
  }

  public List<ActivityBean> getActivityList() {
    return activityList;
  }
}
