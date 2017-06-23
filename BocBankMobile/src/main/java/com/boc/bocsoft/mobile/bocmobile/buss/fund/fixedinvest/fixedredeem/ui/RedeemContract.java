package com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedredeem.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model.FundBalanceModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.model.FundCompanyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.update.ui.FundUpdateContract;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by huixiaobo on 2016/12/16.
 * 网络请求接口管理类
 */
public class RedeemContract {

    public interface RedeemView {
        /**015基金账号请求失败*/
        void fundBalanceFail(BiiResultErrorException biiResultErrorException);
        /**015基金账号请求成功*/
        void fundBalanceSuccess(FundBalanceModel balanceResult);
        /**061失败方法*/
        void fundCompanyFail(BiiResultErrorException biiResultErrorException);
        /**061成功方法*/
        void fundCompanySuccess(FundCompanyModel companyResult);
    }

    public interface Presenter extends BasePresenter {
        /**015接口请求*/
        void qureyfundBalance(String fundcode);
        /**061查询基金公司请求*/
        void queryFundCompany(String fundcode);
    }

}
