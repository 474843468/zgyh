package com.boc.bocsoft.mobile.mbcg;

import com.boc.bocsoft.mobile.mbcg.activityinfo.ActivityInfoParams;
import com.boc.bocsoft.mobile.mbcg.activityinfo.ActivityInfoRespone;
import com.boc.bocsoft.mobile.mbcg.activityinfo.ActivityInfoResult;
import rx.Observable;

/**
 * Created by dingeryue on 2016年10月26.
 */

public class MBCGService {


  /**
   * 获取活动信息 getActivityAction
   * @param params
   * @return
   */
  public Observable<ActivityInfoResult> getActivityAction (ActivityInfoParams params,MBCGHeader header) {

    header.setMethod("GetActivityAction");

    return MBCGClient.instance.post(MBCGClient.url(),header,"GetActivityAction",params, ActivityInfoRespone.class);
  }

}
