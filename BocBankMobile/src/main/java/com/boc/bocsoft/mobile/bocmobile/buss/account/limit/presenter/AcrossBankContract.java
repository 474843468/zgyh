package com.boc.bocsoft.mobile.bocmobile.buss.account.limit.presenter;

import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.model.LimitModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * 作者：xwg on 16/10/25 13:46
 */
public interface AcrossBankContract {

    interface AcrossBankView extends BaseView<BasePresenter>{
        /**
         * 获取开通跨行订购列表
         * @param openModels
         */
        void queryService(List<LimitModel> openModels,List<LimitModel> closeModels);
    }

    interface AcrossBankPresenter extends BasePresenter{
        /**
         * 获取服务开通状态
         */
        void queryService(List<AccountBean> accountBeans);
    }
}
