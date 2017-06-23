package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hcelogoff;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.hcecardlist.HceCardListQueryViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Contact：* e 闪付-注销
 * Created by gengjunying on 2016/11/29
 */
public class HceCardCancelContact {
    public interface View extends BaseView<Presenter> {
        /**
         * 成功回调：
         * e 闪付-查询Hce卡列表
         */
        void HCEQuickPassCancelSuccess(String str);

        /**
         * 失败回调：
         * e 闪付-查询Hce卡列表
         */
        void  HCEQuickPassCancelFail(BiiResultErrorException biiResultErrorException);
    }

    public interface Presenter extends BasePresenter {
        /**
         * e 闪付-查询Hce卡列表
         */

        void HCEQuickPassCancel(String masterCardNo, String slaveCardNo);
    }
}


