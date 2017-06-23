package com.boc.bocsoft.mobile.bii.bus.equickpay.service;

import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnCardBrandQuery.PsnCardBrandQueryParams;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnCardBrandQuery.PsnCardBrandQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnCardBrandQuery.PsnCardBrandQueryResult;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassActivationSubmit.PsnHCEQuickPassActivationSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassActivationSubmit.PsnHCEQuickPassActivationSubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassActivationSubmit.PsnHCEQuickPassActivationSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassActivationVerify.PsnHCEQuickPassActivationVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassActivationVerify.PsnHCEQuickPassActivationVerifyResponse;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassActivationVerify.PsnHCEQuickPassActivationVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassApplication.PsnHCEQuickPassApplicationParams;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassApplication.PsnHCEQuickPassApplicationResponse;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassApplication.PsnHCEQuickPassApplicationResult;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassCancel.PsnHCEQuickPassCancelParams;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassCancel.PsnHCEQuickPassCancelResponse;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassCancel.PsnHCEQuickPassCancelResult;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassLiftLoseSubmit.PsnHCEQuickPassLiftLoseSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassLiftLoseSubmit.PsnHCEQuickPassLiftLoseSubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassLiftLoseSubmit.PsnHCEQuickPassLiftLoseSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassLiftLoseVerify.PsnHCEQuickPassLiftLoseVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassLiftLoseVerify.PsnHCEQuickPassLiftLoseVerifyResponse;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassLiftLoseVerify.PsnHCEQuickPassLiftLoseVerifyResult;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassListQuery.PsnHCEQuickPassListQueryParams;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassListQuery.PsnHCEQuickPassListQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassListQuery.PsnHCEQuickPassListQueryResult;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassListQueryOutlay.PsnHCEQuickPassListQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassListQueryOutlay.PsnHCEQuickPassListQueryOutlayResponse;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassListQueryOutlay.PsnHCEQuickPassListQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassLukLoad.PsnHCEQuickPassLukLoadParams;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassLukLoad.PsnHCEQuickPassLukLoadResponse;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassLukLoad.PsnHCEQuickPassLukLoadResult;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassQuotaSettingSubmit.PsnHCEQuickPassQuotaSettingSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassQuotaSettingSubmit.PsnHCEQuickPassQuotaSettingSubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassQuotaSettingSubmit.PsnHCEQuickPassQuotaSettingSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassQuotaSettingVerify.PsnHCEQuickPassQuotaSettingVerifyParams;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassQuotaSettingVerify.PsnHCEQuickPassQuotaSettingVerifyResponse;
import com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassQuotaSettingVerify.PsnHCEQuickPassQuotaSettingVerifyResult;
import com.boc.bocsoft.mobile.bii.common.client.BIIClient;
import com.boc.bocsoft.mobile.bii.common.client.BIIClientConfig;

import rx.Observable;

/**
 * Created by gengjunying on 2016/12/06.
 * <p/>
 * e闪付 service
 */
public class EquickpayService {

    /**
     *  PsnHCEQuickPassListQuery  查询HCE闪付卡列表
     *
     * @param params
     * @return
     * @author gengjunying
     */
    public Observable<PsnHCEQuickPassListQueryResult> PsnHCEQuickPassListQuery(PsnHCEQuickPassListQueryParams params) {
        return BIIClient.instance.post("PsnHCEQuickPassListQuery", params, PsnHCEQuickPassListQueryResponse.class);
    }

    /**
     *  PsnCardBrandQuery   查询卡支持的卡品牌
     *
     * @param params
     * @return
     * @author yangle
     */
    public Observable<PsnCardBrandQueryResult> psnCardBrandQuery(PsnCardBrandQueryParams params) {
        return BIIClient.instance.post("PsnCardBrandQuery", params, PsnCardBrandQueryResponse.class);
    }

    /**
     *  PsnHCEQuickPassApplication    HCE闪付卡申请
     *
     * @param params
     * @return
     * @author yangle
     */
    public Observable<PsnHCEQuickPassApplicationResult> psnHCEQuickPassApplication(PsnHCEQuickPassApplicationParams params) {
        return BIIClient.instance.post("PsnHCEQuickPassApplication", params, PsnHCEQuickPassApplicationResponse.class);
    }

   /**
     *  PsnHCEQuickPassActivationVerify  HCE闪付卡激活预交易
     *
     * @param params
     * @return
     * @author yangle
     */
    public Observable<PsnHCEQuickPassActivationVerifyResult> psnHCEQuickPassActivationVerify(PsnHCEQuickPassActivationVerifyParams params) {
        return BIIClient.instance.post("PsnHCEQuickPassActivationVerify", params, PsnHCEQuickPassActivationVerifyResponse.class);
    }

   /**
     *  PsnHCEQuickPassActivationSubmit  HCE闪付卡激活提交交易
     *
     * @param params
     * @return
     * @author yangle
     */
    public Observable<PsnHCEQuickPassActivationSubmitResult> psnHCEQuickPassActivationSubmit(PsnHCEQuickPassActivationSubmitParams params) {
        return BIIClient.instance.post("PsnHCEQuickPassActivationSubmit", params, PsnHCEQuickPassActivationSubmitResponse.class);
    }

   /**
     *  PsnHCEQuickPassQuotaSettingVerify  HCE闪付卡限额设置预
     *
     * @param params
     * @return
     * @author yangle
     */
    public Observable<PsnHCEQuickPassQuotaSettingVerifyResult> psnHCEQuickPassQuotaSettingVerify(PsnHCEQuickPassQuotaSettingVerifyParams params) {
        return BIIClient.instance.post("PsnHCEQuickPassQuotaSettingVerify", params, PsnHCEQuickPassQuotaSettingVerifyResponse.class);
    }

   /**
     *   PsnHCEQuickPassQuotaSettingSubmit  HCE闪付卡限额设置提交交易
     *
     * @param params
     * @return
     * @author yangle
     */
    public Observable<PsnHCEQuickPassQuotaSettingSubmitResult>  psnHCEQuickPassQuotaSettingSubmit( PsnHCEQuickPassQuotaSettingSubmitParams params) {
        return BIIClient.instance.post(" PsnHCEQuickPassQuotaSettingSubmit", params,  PsnHCEQuickPassQuotaSettingSubmitResponse.class);
    }


    /**
     *   PsnHCEQuickPassCancel  HCE闪付卡注销
     *
     * @param params
     * @return
     * @author gengjunying
     */
    public Observable<PsnHCEQuickPassCancelResult>  PsnHCEQuickPassCancel(PsnHCEQuickPassCancelParams params) {
        return BIIClient.instance.post("PsnHCEQuickPassCancel", params,  PsnHCEQuickPassCancelResponse.class);
    }


    /**
     *   PsnHCEQuickPassCancel  HCE闪付卡解挂 预
     *
     * @param params
     * @return
     * @author gengjunying
     */
    public Observable<PsnHCEQuickPassLiftLoseVerifyResult>  PsnHCEQuickPassLiftLoseVerify(PsnHCEQuickPassLiftLoseVerifyParams params) {
        return BIIClient.instance.post("PsnHCEQuickPassLiftLoseVerify", params,  PsnHCEQuickPassLiftLoseVerifyResponse.class);
    }


    /**
     *   PsnHCEQuickPassCancel  HCE闪付卡解挂 提交
     *
     * @param params
     * @return
     * @author gengjunying
     */
    public Observable<PsnHCEQuickPassLiftLoseSubmitResult>  PsnHCEQuickPassLiftLoseSubmit(PsnHCEQuickPassLiftLoseSubmitParams params) {
        return BIIClient.instance.post("PsnHCEQuickPassLiftLoseSubmit", params,  PsnHCEQuickPassLiftLoseSubmitResponse.class);
    }


    /**
     *  PsnHCEQuickPassListQuery  未登录查询HCE闪付卡列表
     *
     * @param params
     * @return
     * @author gengjunying
     */
    public Observable<PsnHCEQuickPassListQueryOutlayResult> PsnHCEQuickPassListQueryOutlay(PsnHCEQuickPassListQueryOutlayParams params) {
        return BIIClient.instance.post(BIIClientConfig.getBPMSUrl(),"PsnHCEQuickPassListQueryOutlay", params, PsnHCEQuickPassListQueryOutlayResponse.class);
    }



    /**
     *  PsnHCEQuickPassLukLoad  HCE闪付卡LUK加载
     *
     * @param params
     * @return
     * @author gengjunying
     */
    public Observable<PsnHCEQuickPassLukLoadResult> PsnHCEQuickPassLukLoad(PsnHCEQuickPassLukLoadParams params) {
        return BIIClient.instance.post(BIIClientConfig.getBPMSUrl(),"PsnHCEQuickPassLukLoad", params, PsnHCEQuickPassLukLoadResponse.class);
    }


}
