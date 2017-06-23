package com.boc.bocsoft.mobile.bii.bus.account.service;

import com.boc.bocsoft.mobile.bii.bus.account.model.FactorAndCaResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayClosePre.PsnNcpayClosePreParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayClosePre.PsnNcpayClosePreResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayCloseSubmit.PsnNcpayCloseSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayCloseSubmit.PsnNcpayCloseSubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayOpenPre.PsnNcpayOpenPreParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayOpenSubmit.PsnNcpayOpenSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayOpenSubmit.PsnNcpayOpenSubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayOpenSubmit.PsnNcpayOpenSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayQuotaModifyPre.PsnNcpayQuotaModifyPreParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayQuotaModifySubmit.PsnNcpayQuotaModifySubmitParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayQuotaModifySubmit.PsnNcpayQuotaModifySubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayQuotaModifySubmit.PsnNcpayQuotaModifySubmitResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayServiceChoose.PsnNcpayServiceChooseParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayServiceChoose.PsnNcpayServiceChooseResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayServiceChoose.PsnNcpayServiceChooseResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PublicSecurityResponse;
import com.boc.bocsoft.mobile.bii.common.client.BIIClient;

import rx.Observable;

/**
 * @author wangyang
 *         2016/10/27 19:24
 *         跨行订购/小额凭签名免密
 */
public class LimitService {

    /**
     * 获取服务开通状态
     * @author wangyang
     * @time 2016/10/10 20:55
     */
    public Observable<PsnNcpayServiceChooseResult> psnNcpayServiceChoose(PsnNcpayServiceChooseParams params) {
        return BIIClient.instance.post("PsnNcpayServiceChoose", params, PsnNcpayServiceChooseResponse.class);
    }

    /**
     * 限额服务开通预交易
     * @author wangyang
     * @time 2016/10/10 20:55
     */
    public Observable<FactorAndCaResult> psnNcpayOpenPre(PsnNcpayOpenPreParams params) {
        return BIIClient.instance.post("PsnNcpayOpenPre", params, PublicSecurityResponse.class);
    }

    /**
     * 限额服务开通交易
     * @author wangyang
     * @time 2016/10/10 20:55
     */
    public Observable<PsnNcpayOpenSubmitResult> psnNcpayOpenSubmit(PsnNcpayOpenSubmitParams params) {
        return BIIClient.instance.post("PsnNcpayOpenSubmit", params, PsnNcpayOpenSubmitResponse.class);
    }

    /**
     * 限额服务修改限额预交易
     * @author wangyang
     * @time 2016/10/10 20:55
     */
    public Observable<FactorAndCaResult> psnNcpayQuotaModifyPre(PsnNcpayQuotaModifyPreParams params) {
        return BIIClient.instance.post("PsnNcpayQuotaModifyPre", params, PublicSecurityResponse.class);
    }

    /**
     * 限额服务修改限额交易
     * @author wangyang
     * @time 2016/10/10 20:55
     */
    public Observable<PsnNcpayQuotaModifySubmitResult> psnNcpayQuotaModifySubmit(PsnNcpayQuotaModifySubmitParams params) {
        return BIIClient.instance.post("PsnNcpayQuotaModifySubmit", params, PsnNcpayQuotaModifySubmitResponse.class);
    }

    /**
     * 限额服务关闭开通预交易
     * @author wangyang
     * @time 2016/10/10 20:55
     */
    public Observable<String> psnNcpayClosePre(PsnNcpayClosePreParams params) {
        return BIIClient.instance.post("PsnNcpayClosePre", params, PsnNcpayClosePreResponse.class);
    }

    /**
     * 限额服务关闭开通交易
     * @author wangyang
     * @time 2016/10/10 20:55
     */
    public Observable<String> psnNcpayCloseSubmit(PsnNcpayCloseSubmitParams params) {
        return BIIClient.instance.post("PsnNcpayCloseSubmit", params, PsnNcpayCloseSubmitResponse.class);
    }
}
