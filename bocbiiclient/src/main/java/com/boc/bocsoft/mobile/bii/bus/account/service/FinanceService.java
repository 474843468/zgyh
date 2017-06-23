package com.boc.bocsoft.mobile.bii.bus.account.service;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICAccount.PsnFinanceICAccountParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICAccount.PsnFinanceICAccountResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICAccount.PsnFinanceICAccountResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICAccountDetail.PsnFinanceICAccountDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICAccountDetail.PsnFinanceICAccountDetailResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICAccountDetail.PsnFinanceICAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICIsSign.PsnFinanceICIsSignParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICIsSign.PsnFinanceICIsSignResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICSignCancel.PsnFinanceICSignCancelParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICSignCancel.PsnFinanceICSignCancelResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICSignCreat.PsnFinanceICSignCreatParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICSignCreat.PsnFinanceICSignCreatResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICSignCreat.PsnFinanceICSignCreatResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICSignCreatRes.PsnFinanceICSignCreatResParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICSignCreatRes.PsnFinanceICSignCreatResResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICTransfer.PsnFinanceICTransferParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICTransfer.PsnFinanceICTransferResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICTransfer.PsnFinanceICTransferResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICTransferDetail.PsnFinanceICTransferDetailParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICTransferDetail.PsnFinanceICTransferDetailResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICTransferDetail.PsnFinanceICTransferDetailResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnICTransferNoRelevance.PsnICTransferNoRelevanceParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnICTransferNoRelevance.PsnICTransferNoRelevanceResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnICTransferNoRelevance.PsnICTransferNoRelevanceResult;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnICTransferNoRelevanceRes.PsnICTransferNoRelevanceResParams;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnICTransferNoRelevanceRes.PsnICTransferNoRelevanceResResponse;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnICTransferNoRelevanceRes.PsnICTransferNoRelevanceResResult;
import com.boc.bocsoft.mobile.bii.common.client.BIIClient;

import java.util.List;

import rx.Observable;

/**
 * @author wangyang
 *         16/6/17 15:35
 *         电子现金账户接口
 */
public class FinanceService {

    /**
     * 电子现金账户概览
     *
     * @param params
     * @return
     */
    public Observable<List<PsnFinanceICAccountResult>> psnFinanceICAccountView(PsnFinanceICAccountParams params) {
        return BIIClient.instance.post("PsnFinanceICAccountView", params, PsnFinanceICAccountResponse.class);
    }

    /**
     * 账户详情及余额
     *
     * @param params
     * @return
     */
    public Observable<PsnFinanceICAccountDetailResult> psnFinanceICAccountDetail(PsnFinanceICAccountDetailParams params) {
        return BIIClient.instance.post("PsnFinanceICAccountDetail", params, PsnFinanceICAccountDetailResponse.class);
    }

    /**
     * 账户交易明细
     *
     * @param params
     * @return
     */
    public Observable<PsnFinanceICTransferDetailResult> psnFinanceICTransferDetail(PsnFinanceICTransferDetailParams params) {
        return BIIClient.instance.post("PsnFinanceICTransferDetail", params, PsnFinanceICTransferDetailResponse.class);
    }

    /**
     * 删除绑定关系
     *
     * @param params
     * @return
     */
    public Observable<String> psnFinanceICSignCancel(PsnFinanceICSignCancelParams params) {
        return BIIClient.instance.post("PsnFinanceICSignCancle", params, PsnFinanceICSignCancelResponse.class);
    }

    /**
     * 新增绑定关系--预交易
     *
     * @param params
     * @return
     */
    public Observable<PsnFinanceICSignCreatResult> psnFinanceICSignCreat(PsnFinanceICSignCreatParams params) {
        return BIIClient.instance.post("PsnFinanceICSignCreat", params, PsnFinanceICSignCreatResponse.class);
    }

    /**
     * 新增绑定关系--提交交易
     *
     * @param params
     * @return
     */
    public Observable<String> psnFinanceICSignCreatRes(PsnFinanceICSignCreatResParams params) {
        return BIIClient.instance.post("PsnFinanceICSignCreatRes", params, PsnFinanceICSignCreatResResponse.class);
    }

    /**
     * 账户充值
     *
     * @param params
     * @return
     */
    public Observable<PsnFinanceICTransferResult> psnFinanceICTransfer(PsnFinanceICTransferParams params) {
        return BIIClient.instance.post("PsnFinanceICTransfer", params, PsnFinanceICTransferResponse.class);
    }

    /**
     * 账户充值--未关联进网银(给他人充值)--预交易
     *
     * @param params
     * @return
     */
    public Observable<PsnICTransferNoRelevanceResult> psnICTransferNoRelevance(PsnICTransferNoRelevanceParams params) {
        return BIIClient.instance.post("PsnICTransferNoRelevance", params, PsnICTransferNoRelevanceResponse.class);
    }

    /**
     * 账户充值--未关联进网银(给他人充值)--提交交易
     *
     * @param params
     * @return
     */
    public Observable<PsnICTransferNoRelevanceResResult> psnICTransferNoRelevanceRes(PsnICTransferNoRelevanceResParams params) {
        return BIIClient.instance.post("PsnICTransferNoRelevanceRes", params, PsnICTransferNoRelevanceResResponse.class);
    }

    /**
     * 查询IC卡是否绑定
     *
     * @param params
     * @return
     */
    public Observable<String> psnFinanceICIsSign(PsnFinanceICIsSignParams params) {
        return BIIClient.instance.post("PsnFinanceICIsSign", params, PsnFinanceICIsSignResponse.class);
    }
}
