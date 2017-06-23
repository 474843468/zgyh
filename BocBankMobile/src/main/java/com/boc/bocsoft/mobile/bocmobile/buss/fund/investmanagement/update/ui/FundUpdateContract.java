package com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.ui;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnQueryInvtBindingInfo.PsnQueryInvtBindingInfoResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model.FundBalanceModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model.FundCompanyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model.FundScheduelBuyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model.FundScheduledBuyPms;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model.FundScheduledSellModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model.FundScheduledSellPms;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by huixiaobo on 2016/12/2.
 * 网络请求管理类
 */
public class FundUpdateContract {

    public interface InvestUpdateView extends BaseView<Presenter> {

        /**015基金账号请求失败*/
        void fundBalanceFail(BiiResultErrorException biiResultErrorException);
        /**015基金账号请求成功*/
        void fundBalanceSuccess(FundBalanceModel balanceResult);

    }

    public interface UpdateInfoView extends BaseView<Presenter> {
        /**001基金账号请求失败*/
        void fundAccountFail(BiiResultErrorException biiResultErrorException);
        /**001基金账号请求成功*/
        void fundAccountSuccess(PsnQueryInvtBindingInfoResult result);

//        /**061失败方法*/
//        void fundCompanyFail(BiiResultErrorException biiResultErrorException);
//        /**061成功方法*/
//        void fundCompanySuccess(FundCompanyModel companyResult);

        /**038失败方法*/
        void scheduledBuyModifyFail(BiiResultErrorException biiResultErrorException);
        /**038成功方法*/
        void scheduledBuyModifySuccess(FundScheduelBuyModel fundBuyResult);

        /**039失败方法*/
        void scheduledSellModifyFail(BiiResultErrorException biiResultErrorException);
        /**039成功方法*/
        void scheduledSellModifySuccess(FundScheduledSellModel fundSellResult);
    }

    public interface Presenter extends BasePresenter {
//        /**015接口请求*/
//        void qureyfundBalance(String fundcode);
//        /**061查询基金公司请求*/
//        void queryFundCompany(String fundcode);
        /**038定申修改确认请求*/
        void queryScheduledBuyModify(FundScheduledBuyPms params);
        /**039定赎修改确认请求*/
        void queryScheduledSellModify(FundScheduledSellPms params);
        /**001查询基金账号*/
        void queryAccount();

    }

}
