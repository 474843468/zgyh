package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.CollectionSubmitViewModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Contract:主动收款
 * Created by zhx on 2016/7/5
 */
public class CollectionSubmitContract {
    public interface View extends BaseView<Presenter> {

        /**
         * 成功回调：
         * 主动收款
         */
        void collectionSubmitSuccess(CollectionSubmitViewModel collectionSubmitViewModel);

        /**
         * 失败回调：
         * 主动收款
         */
        void collectionSubmitFail(BiiResultErrorException biiResultErrorException);
        // 安全因子返回
        void securityFactorReturned();
        // 获取UImodel
        CollectionSubmitViewModel getUiModel();
    }
    public interface Presenter extends BasePresenter {
        /**
         * 主动收款
         */
        void collectionSubmit(CollectionSubmitViewModel collectionSubmitViewModel);

        // 获取安全因子
        void psnGetSecurityFactor();
    }
}
