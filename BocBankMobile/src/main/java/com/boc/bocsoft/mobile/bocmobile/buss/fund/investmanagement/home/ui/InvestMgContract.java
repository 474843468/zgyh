package com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.InvalidinvestModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.InvestBuyDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.InvestParams;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.InvestParamsModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.InvestSellDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.InvestpsRsModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.RedeemParams;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.RedeempsRsModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.investmanagement.home.model.ValidinvestModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by huixiaobo on 2016/11/24.
 * 网络请求管理类
 */
public class InvestMgContract {

    /**有效定投*/
    public interface ValidinvestView extends BaseView<Presenter> {
        //011有效定投列表查询失败
        void fundValidinvestFail(BiiResultErrorException biiResultErrorException);
        //011有效定投列表查询成功
        void fundValidinvestSuccess(ValidinvestModel validinvestModel);
    }

    /**失效定投*/
    public interface InvestView extends BaseView<Presenter> {
        //058失效定投列表查询失败
        void fundInvalidFail(BiiResultErrorException biiResultErrorException);
        //058失效定投列表查询成功
        void fundInvalidSuccess(InvalidinvestModel invalidModel);
    }

    /**定投管理详情*/
    public interface InvestDetailView extends BaseView<Presenter> {
        /**054接口详情请求失败*/
        void fundInvestBuyDetailFail(BiiResultErrorException biiResultErrorException);
        /**054接口详情请求成功*/
        void fundInvestBuyDetailSuccess(InvestBuyDetailModel buyDetail);
        /**055接口详情请求失败*/
        void fundInvestSellDetailFail(BiiResultErrorException biiResultErrorException);
        /**055接口详情请求成功*/
        void fundInvestSellDetailSuccess(InvestSellDetailModel sellDetail);


    }

    /**暂停恢复*/
    public interface InvestPauseResumeView extends BaseView<Presenter> {
        //056定投暂停恢复失败
        void fundInvestPauseResumeFail(BiiResultErrorException biiResultErrorException);
        //056定投暂停恢复成功
        void fundInvestPauseResumeSuccess(InvestpsRsModel insRsModel);

        //057定赎暂停失败
        void fundRedeemPauseResumeFail(BiiResultErrorException biiResultErrorException);
        //057定赎暂停恢复成功
        void fundRedeemPauseResumeSuccess(RedeempsRsModel rsModel);
    }

    public interface Presenter extends BasePresenter {
        //011有效定投
        void queryValidinvest(InvestParamsModel investParams);
        //058失效定投
        void queryInvalid(InvestParamsModel investParams);
        //054详情
        void queryInvestBuyDetail(String date, String num);
        //055详情
        void queryInvestSellDetail(String date, String num);
        //056定投暂停恢复
        void queryInvestPauseResume(InvestParams params);
        //057 定赎暂停恢复
        void queryRedeemPauseResume(RedeemParams params);
    }
}
