package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model.OnLineLoanProductBean;
import java.util.List;

/**
 * Created by XieDu on 2016/7/21.
 */
public class LoanApplyOtherSelectContract {
    public interface View {
        void onLoadProductsSuccess(List<OnLineLoanProductBean> products);
    }

    public interface Presenter {

        /**
         * 查询支持的贷款列表
         */
        void getOnlineLoanProducts(String cityCode);
    }
}
