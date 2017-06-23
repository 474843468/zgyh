package com.boc.bocsoft.mobile.bii.bus.account.service;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdQueryVIRCardInfo.PsnCrcdQueryVIRCardInfoParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdQueryVIRCardInfo.PsnCrcdQueryVIRCardInfoResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdQueryVIRCardInfo.PsnCrcdQueryVIRCardInfoResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardApplyConfirm.PsnCrcdVirtualCardApplyConfirmParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardApplyConfirm.PsnCrcdVirtualCardApplyConfirmResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardApplyConfirm.PsnCrcdVirtualCardApplyConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardApplyInit.PsnCrcdVirtualCardApplyInitParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardApplyInit.PsnCrcdVirtualCardApplyInitResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardApplyInit.PsnCrcdVirtualCardApplyInitResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardApplySubmit.PsnCrcdVirtualCardApplySubmitParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardApplySubmit.PsnCrcdVirtualCardApplySubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardApplySubmit.PsnCrcdVirtualCardApplySubmitResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardCancel.PsnCrcdVirtualCardCancelParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardCancel.PsnCrcdVirtualCardCancelResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardCancel.PsnCrcdVirtualCardCancelResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardFunctionSetConfirm.PsnCrcdVirtualCardFunctionSetConfirmParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardFunctionSetConfirm.PsnCrcdVirtualCardFunctionSetConfirmResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardFunctionSetConfirm.PsnCrcdVirtualCardFunctionSetConfirmResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardFunctionSetSubmit.PsnCrcdVirtualCardFunctionSetSubmitParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardFunctionSetSubmit.PsnCrcdVirtualCardFunctionSetSubmitResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardFunctionSetSubmit.PsnCrcdVirtualCardFunctionSetSubmitResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardOpenOnline.PsnCrcdVirtualCardOpenOnlineParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardOpenOnline.PsnCrcdVirtualCardOpenOnlineResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardQuery.PsnCrcdVirtualCardQueryParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardQuery.PsnCrcdVirtualCardQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardQuery.PsnCrcdVirtualCardQueryResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardRelevance.PsnCrcdVirtualCardRelevanceParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardRelevance.PsnCrcdVirtualCardRelevanceResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardSendMessage.PsnCrcdVirtualCardSendMessageParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardSendMessage.PsnCrcdVirtualCardSendMessageResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardSetPayAcct.PsnCrcdVirtualCardSetPayAcctParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardSetPayAcct.PsnCrcdVirtualCardSetPayAcctResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardSettledbillQuery.PsnCrcdVirtualCardSettledbillQueryParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardSettledbillQuery.PsnCrcdVirtualCardSettledbillQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardSettledbillQuery.PsnCrcdVirtualCardSettledbillQueryResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardUnsettledbillQuery.PsnCrcdVirtualCardUnsettledbillQueryParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardUnsettledbillQuery.PsnCrcdVirtualCardUnsettledbillQueryResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardUnsettledbillQuery.PsnCrcdVirtualCardUnsettledbillQueryResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardUnsettledbillSum.PsnCrcdVirtualCardUnsettledbillSumParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardUnsettledbillSum.PsnCrcdVirtualCardUnsettledbillSumResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardUnsettledbillSum.PsnCrcdVirtualCardUnsettledbillSumResult;
import com.boc.bocsoft.mobile.bii.common.client.BIIClient;

import java.util.List;

import rx.Observable;

/**
 * @author wangyang
 *         16/7/12 16:22
 *         虚拟银行卡Service
 */
public class VirtualService {

    /**
     * 申请虚拟银行卡初始化
     * @param params
     * @return
     */
    public Observable<PsnCrcdVirtualCardApplyInitResult> psnCrcdVirtualCardApplyInit(PsnCrcdVirtualCardApplyInitParams params){
        return BIIClient.instance.post("PsnCrcdVirtualCardApplyInit",params, PsnCrcdVirtualCardApplyInitResponse.class);
    }

    /**
     * 申请虚拟银行卡预交易
     * @param params
     * @return
     */
    public Observable<PsnCrcdVirtualCardApplyConfirmResult> psnCrcdVirtualCardApplyConfirm(PsnCrcdVirtualCardApplyConfirmParams params){
        return BIIClient.instance.post("PsnCrcdVirtualCardApplyConfirm",params, PsnCrcdVirtualCardApplyConfirmResponse.class);
    }

    /**
     * 申请虚拟银行卡提交
     * @param params
     * @return
     */
    public Observable<PsnCrcdVirtualCardApplySubmitResult> psnCrcdVirtualCardApplySubmit(PsnCrcdVirtualCardApplySubmitParams params){
        return BIIClient.instance.post("PsnCrcdVirtualCardApplySubmit",params, PsnCrcdVirtualCardApplySubmitResponse.class);
    }

    /**
     * 虚拟银行卡发送短信
     * @param params
     * @return
     */
    public Observable<String> psnCrcdVirtualCardSendMessage(PsnCrcdVirtualCardSendMessageParams params){
        return BIIClient.instance.post("PsnCrcdVirtualCardSendMessage",params, PsnCrcdVirtualCardSendMessageResponse.class);
    }

    /**
     * 虚拟银行卡交易限额修改预交易
     * @param params
     * @return
     */
    public Observable<PsnCrcdVirtualCardFunctionSetConfirmResult> psnCrcdVirtualCardFunctionSetConfirm(PsnCrcdVirtualCardFunctionSetConfirmParams params){
        return BIIClient.instance.post("PsnCrcdVirtualCardFunctionSetConfirm",params, PsnCrcdVirtualCardFunctionSetConfirmResponse.class);
    }

    /**
     * 虚拟银行卡交易限额修改提交
     * @param params
     * @return
     */
    public Observable<PsnCrcdVirtualCardFunctionSetSubmitResult> psnCrcdVirtualCardFunctionSetSubmit(PsnCrcdVirtualCardFunctionSetSubmitParams params){
        return BIIClient.instance.post("PsnCrcdVirtualCardFunctionSetSubmit",params, PsnCrcdVirtualCardFunctionSetSubmitResponse.class);
    }

    /**
     * 虚拟银行卡注销
     * @param params
     * @return
     */
    public Observable<PsnCrcdVirtualCardCancelResult> psnCrcdVirtualCardCancel(PsnCrcdVirtualCardCancelParams params){
        return BIIClient.instance.post("PsnCrcdVirtualCardCancel",params, PsnCrcdVirtualCardCancelResponse.class);
    }

    /**
     * 虚拟银行卡查询
     * @param params
     * @return
     */
    public Observable<PsnCrcdVirtualCardQueryResult> psnCrcdVirtualCardQuery(PsnCrcdVirtualCardQueryParams params){
        return BIIClient.instance.post("PsnCrcdVirtualCardQuery",params, PsnCrcdVirtualCardQueryResponse.class);
    }

    /**
     * 虚拟银行卡关联网银
     * @param params
     * @return
     */
    public Observable<String> psnCrcdVirtualCardRelevance(PsnCrcdVirtualCardRelevanceParams params){
        return BIIClient.instance.post("PsnCrcdVirtualCardRelevance",params, PsnCrcdVirtualCardRelevanceResponse.class);
    }

    /**
     * 虚拟银行卡开通网上支付
     * @param params
     * @return
     */
    public Observable<String> psnCrcdVirtualCardOpenOnline(PsnCrcdVirtualCardOpenOnlineParams params){
        return BIIClient.instance.post("PsnCrcdVirtualCardOpenOnline",params, PsnCrcdVirtualCardOpenOnlineResponse.class);
    }

    /**
     * 虚拟银行卡设置支付账户
     * @param params
     * @return
     */
    public Observable<String> psnCrcdVirtualCardSetPayAcct(PsnCrcdVirtualCardSetPayAcctParams params){
        return BIIClient.instance.post("PsnCrcdVirtualCardSetPayAcct",params, PsnCrcdVirtualCardSetPayAcctResponse.class);
    }

    /**
     * 虚拟银行卡未出账单查询
     * @param params
     * @return
     */
    public Observable<PsnCrcdVirtualCardUnsettledbillQueryResult> psnCrcdVirtualCardUnsettledbillQuery(PsnCrcdVirtualCardUnsettledbillQueryParams params){
        return BIIClient.instance.post("PsnCrcdVirtualCardUnsettledbillQuery",params, PsnCrcdVirtualCardUnsettledbillQueryResponse.class);
    }

    /**
     * 虚拟银行卡未出账单合计查询
     * @param params
     * @return
     */
    public Observable<List<PsnCrcdVirtualCardUnsettledbillSumResult>> psnCrcdVirtualCardUnsettledbillSum(PsnCrcdVirtualCardUnsettledbillSumParams params){
        return BIIClient.instance.post("PsnCrcdVirtualCardUnsettledbillSum",params, PsnCrcdVirtualCardUnsettledbillSumResponse.class);
    }

    /**
     * 虚拟银行卡已出账单查询
     * @param params
     * @return
     */
    public Observable<List<PsnCrcdVirtualCardSettledbillQueryResult>> psnCrcdVirtualCardSettledbillQuery(PsnCrcdVirtualCardSettledbillQueryParams params){
        return BIIClient.instance.post("PsnCrcdVirtualCardSettledbillQuery",params, PsnCrcdVirtualCardSettledbillQueryResponse.class);
    }

    /**
     * 虚拟银行卡详情
     * @param params
     * @return
     */
    public Observable<PsnCrcdQueryVIRCardInfoResult> psnCrcdQueryVIRCardInfo(PsnCrcdQueryVIRCardInfoParams params){
        return BIIClient.instance.post("PsnCrcdQueryVIRCardInfo",params, PsnCrcdQueryVIRCardInfoResponse.class);
    }
}
