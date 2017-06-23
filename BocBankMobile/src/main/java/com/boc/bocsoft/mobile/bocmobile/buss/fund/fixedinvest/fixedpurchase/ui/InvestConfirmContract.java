package com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedpurchase.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fixedinvest.fixedpurchase.model.PsnFundScheduleBuyModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by pactera on 2016/12/19.
 */

public class InvestConfirmContract {

    public interface InvestConfirmView extends BaseView<Presenter>{
        //定期定额申购成功
        void psnFundScheduleBuySuccess(PsnFundScheduleBuyModel model);
        //定期定额申购失败
        void psnFundScheduleBuyFail(BiiResultErrorException e);
    }
    //定期定额申购
    public interface Presenter extends BasePresenter{
        void psnFundScheduleBuy(PsnFundScheduleBuyModel model);
    }

}
