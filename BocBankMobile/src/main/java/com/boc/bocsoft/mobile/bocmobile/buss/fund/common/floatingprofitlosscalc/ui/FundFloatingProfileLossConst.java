package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.floatingprofitlosscalc.model.FundFloatingProfileLossModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by taoyongzhen on 2016/11/30.
 */

public class FundFloatingProfileLossConst {

    public interface queryFloatingProfitLossView extends BaseView<BasePresenter> {
        void queryFloatingProfitLossFail(BiiResultErrorException exception);
        void queryFloatingProfitLossSuccess(FundFloatingProfileLossModel model);
    }

    public interface Prrsenter extends BasePresenter{
        void queryFloatingProfitLoss(FundFloatingProfileLossModel model);
    }

}
