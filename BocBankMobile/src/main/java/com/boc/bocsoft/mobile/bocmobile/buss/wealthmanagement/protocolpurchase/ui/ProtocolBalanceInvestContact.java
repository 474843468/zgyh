package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.XpadQueryRiskMatchViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * 余额理财投资
 * Created by zhx on 2016/11/10
 */
public class ProtocolBalanceInvestContact {
    public interface View extends BaseView<Presenter> {
        /**
         * 成功回调：查询客户风险等级与产品风险等级是否匹配
         */
        void queryRiskMatchSuccess(XpadQueryRiskMatchViewModel viewModel);

        /**
         * 失败回调：查询客户风险等级与产品风险等级是否匹配
         */
        void queryRiskMatchFailed(BiiResultErrorException biiResultErrorException);
    }


    public interface Presenter extends BasePresenter {
        /**
         * 查询客户风险等级与产品风险等级是否匹配
         */
        void queryRiskMatch(XpadQueryRiskMatchViewModel viewModel);
    }
}
