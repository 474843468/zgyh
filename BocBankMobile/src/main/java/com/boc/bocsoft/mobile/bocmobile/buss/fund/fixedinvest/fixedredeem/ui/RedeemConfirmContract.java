package com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedredeem.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedredeem.model.PsnFundScheduledSellModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by taoyongzhen on 2016/12/19.
 */

public class RedeemConfirmContract {

    public interface RedeemConfirmView extends BaseView<Presenter>{

        //上报定期定额赎回成功
        void psnFundScheduledSellSuccess(PsnFundScheduledSellModel model);

        //上报定期定失败额赎回
        void psnFundScheduledSellFail(BiiResultErrorException e);


    }

    public interface Presenter extends BasePresenter{
        //上报定期定额赎回
        void psnFundScheduledSell(PsnFundScheduledSellModel model);
    }
}
