package com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.cancelorder.ui;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnQueryInvtBindingInfo.PsnQueryInvtBindingInfoResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.cancelorder.model.InvestCldParamsModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.cancelorder.model.InvestModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.cancelorder.model.InvestRedeemParamsModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.cancelorder.model.RedeemModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by huixiaobo on 2016/11/30.
 * 定投撤单网络请求方法回调
 */
public class InvestCldContract {

    /**定投撤单*/
    public interface CancelorderView extends BaseView<Presenter> {
        /**014定投撤单请求失败*/
        void fundDdAbortFail(BiiResultErrorException biiResultErrorException);
        /**014定投撤单请求成功*/
        void fundDdAbortSuccess(InvestModel investModel);
        /**001基金账号请求失败*/
        void fundAccountFail(BiiResultErrorException biiResultErrorException);
        /**001基金账号请求成功*/
        void fundAccountSuccess(PsnQueryInvtBindingInfoResult result);
    }

    /**定赎撤单*/
    public interface RedeemView extends BaseView<Presenter> {
        /**040接口定赎撤单请求失败*/
        void fundScheduleSellCancelFail(BiiResultErrorException biiResultErrorException);
        /**040定赎撤单请求成功*/
        void fundScheduleSellCancel(RedeemModel redeemModel);
        /**001基金账号请求失败*/
        void fundAccountFail(BiiResultErrorException biiResultErrorException);
        /**001基金账号请求成功*/
        void fundAccountSuccess(PsnQueryInvtBindingInfoResult result);

    }


    public interface Presenter extends BasePresenter {
        /**014定投撤单请求*/
        void queryFundDdAbort(InvestCldParamsModel cldParamsModel);
        /**040定赎撤单请求*/
        void queryScheduleSellCancel(InvestRedeemParamsModel paramsModel);
        /**001基金账号请求*/
        void queryAccount(String invtType);
    }
}
