package com.boc.bocsoft.mobile.bocmobile.buss.common.transactionProcess;

import com.boc.bocsoft.mobile.bocmobile.base.model.BaseFillInfoBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.BaseSubmitBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * 作者：XieDu
 * 创建时间：2016/9/18 13:49
 * 描述：
 */
public class BaseConfirmContract {
    public interface View<D> {

        void onVerifySuccess(VerifyBean verifyBean, String mRandom, String tokenId);

        void onSubmitSuccess(D submitResult);
    }

    public interface Presenter<T extends BaseFillInfoBean> extends BasePresenter {
        /**
         * 预交易等操作
         *
         * @param securityTypeChanged 安全因子方式是否改变
         */
        void verify(boolean securityTypeChanged, T fillInfo);

        void submit(T fillInfo, BaseSubmitBean submitBean);
    }
}
