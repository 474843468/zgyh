package com.boc.bocsoft.mobile.bii.bus.setting.service;

import com.boc.bocsoft.mobile.bii.bus.setting.model.NamePasswordModMobile.NamePasswordModMobileParams;
import com.boc.bocsoft.mobile.bii.bus.setting.model.NamePasswordModMobile.NamePasswordModMobileResponse;
import com.boc.bocsoft.mobile.bii.bus.setting.model.NamePasswordModMobile.NamePasswordModMobileResult;
import com.boc.bocsoft.mobile.bii.bus.setting.model.psnSvrGlobalMsgList.PsnSvrGlobalMsgListParams;
import com.boc.bocsoft.mobile.bii.bus.setting.model.psnSvrGlobalMsgList.PsnSvrGlobalMsgListResponse;
import com.boc.bocsoft.mobile.bii.bus.setting.model.psnSvrGlobalMsgList.PsnSvrGlobalMsgListResult;
import com.boc.bocsoft.mobile.bii.common.client.BIIClient;

import java.util.List;

import rx.Observable;

/**
 * Created by feibin on 2016/6/6.
 */
public class PsnSettingService {
    /**
     * 获取欢迎页面全局消息列表
     *
     * @param params
     * @return
     */
    public Observable<List<PsnSvrGlobalMsgListResult>> psnSvrGlobalMsgList (PsnSvrGlobalMsgListParams params) {

        return BIIClient.instance.post("psnSvrGlobalMsgList", params,  PsnSvrGlobalMsgListResponse.class);
    }

    /**
     * 重置密码
     *
     * @param params
     * @return
     */
    public Observable<NamePasswordModMobileResult> namePasswordModMobile (NamePasswordModMobileParams params) {

        return BIIClient.instance.post("NamePasswordModMobile", params,  NamePasswordModMobileResponse.class);
    }
}
