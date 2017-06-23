package com.boc.bocsoft.mobile.bocmobile.buss.login.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.login.model.CardBalanceLoginViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.login.model.CreditCardDetailViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.login.model.DebitCardDetailViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;


/**
 * Created by feib on 16/8/2.
 */
public class CardBalanceQueryContract {
    public interface View extends BaseView<Presenter> {
        /**查询登录前随机数成功回调*/
        CardBalanceLoginViewModel randomPreSuccess(String random);
        /**查询卡类型成功回调*/
        void cardTypeSuccess(String accountType);
        /**卡信息成功回调*/
        void loginInforSuccess(CardBalanceLoginViewModel mViewModel);
        /**卡信息失败回调*/
        void loginInforfail();
        /**获取信用卡信息成功回调*/
        void creditCardInforSuccess(CreditCardDetailViewModel creditCardDetailViewModel);
        /**获取借记卡信息成功回调*/
        void debitCardInforSuccess(DebitCardDetailViewModel debitCardDetailViewModel);
    }

    public interface Presenter extends BasePresenter {
        /**卡号登录*/
        void queryLogin();
        /**获取卡类型*/
        void queryCardType(String accountNum);
        /**获取信用卡信息*/
        void queryCreditCardInfor(String accountSeq);
        /**获取借记卡信息*/
        void queryDebitCardInfor(String accountSeq);
    }
}
