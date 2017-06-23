package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.balanceenquiry.ui;

import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGGetBindAccount.PsnVFGGetBindAccountResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.balanceenquiry.model.BalanceEnquiryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.balanceenquiry.model.XpadPsnVFGGetBindAccount;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * *双向宝-余额查询
 * Created by wjk7118 on 2016/11/29.
 */

public class BalanceEnquiryContract {

    public interface View extends BaseView<Presenter> {
        /**
         * 双向宝交易账户成功回调
         */
        void queryPsnVFGGetBindAccountSuccess(PsnVFGGetBindAccountResult psnVFGGetBindAccountResult);
        /**
         * 双向宝交易账户失败回调
         */
        void queryPsnVFGGetBindAccountFail(BiiResultErrorException biiResultErrorException);
        /**
         * 余额查询成功回调
         */
        void queryBalanceEnquiryListSuccess(BalanceEnquiryModel balanceEnquiryModel);

        /**
         * 余额查询失败回调
         */
        void queryBalanceEnquiryListFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        /**
         * 双向宝交易账户查询列表
         */
        void queryPsnVFGGetBindAccount(XpadPsnVFGGetBindAccount xpadPsnVFGGetBindAccount);
        /**
         * 余额查询列表
         */
        void queryBalanceEnquiryList(BalanceEnquiryModel balanceEnquiryModel);
    }

}