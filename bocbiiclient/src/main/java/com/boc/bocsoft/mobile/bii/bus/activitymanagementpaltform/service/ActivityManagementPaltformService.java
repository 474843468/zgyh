package com.boc.bocsoft.mobile.bii.bus.activitymanagementpaltform.service;

import com.boc.bocsoft.mobile.bii.bus.activitymanagementpaltform.model.PsnQueryTransActivityStatus.PsnQueryTransActivityStatusParams;
import com.boc.bocsoft.mobile.bii.bus.activitymanagementpaltform.model.PsnQueryTransActivityStatus.PsnQueryTransActivityStatusResponse;
import com.boc.bocsoft.mobile.bii.bus.activitymanagementpaltform.model.PsnQueryTransActivityStatus.PsnQueryTransActivityStatusResult;
import com.boc.bocsoft.mobile.bii.bus.activitymanagementpaltform.model.psnActivityInfoQuery.PsnActivityInfoQueryParams;
import com.boc.bocsoft.mobile.bii.bus.activitymanagementpaltform.model.psnActivityInfoQuery.PsnActivityInfoQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.activitymanagementpaltform.model.psnActivityInfoQuery.PsnActivityInfoQueryResult;
import com.boc.bocsoft.mobile.bii.common.client.BIIClient;

import rx.Observable;


/**
 * /**
 *
 * @author yx
 * @description 微信抽奖活动-service{活动管理平台}
 * @date 2016/12/19
 */

public class ActivityManagementPaltformService {

    /**
     * I00-公共接口-3.36 036 PsnActivityInfoQuery 活动管理平台取票
     *
     * @param params
     * @return
     * @author yx
     * @date 2016-12-19 19:59:16
     */
    public Observable<PsnActivityInfoQueryResult> psnActivityInfoQuery(PsnActivityInfoQueryParams params) {

        return BIIClient.instance.post("PsnActivityInfoQuery", params, PsnActivityInfoQueryResponse.class);
    }


    /**
     * I00-公共接口-3.37 037 PsnQueryTransActivityStatus 查询转账汇款交易可参与的活动并取票
     *
     * @param params
     * @return
     * @author yx
     * @date 2016-12-19 20:07:09
     */
    public Observable<PsnQueryTransActivityStatusResult> psnQueryTransActivityStatus(PsnQueryTransActivityStatusParams params) {

        return BIIClient.instance.post("PsnQueryTransActivityStatus", params, PsnQueryTransActivityStatusResponse.class);
    }
}