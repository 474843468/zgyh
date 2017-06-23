package com.boc.bocsoft.mobile.bocmobile.buss.account.relation.presenter;

import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * @author wangyang
 *         16/7/7 10:46
 *         取消关联业务逻辑,界面回调
 */
public class AccountRelationContract {

    public interface AccountRelationView extends BaseView<BasePresenter> {

        void cancelAccountRelation();
    }

    public interface Presenter extends BasePresenter {
        /**
         * 取消关联
         */
        void cancelAccountRelation(AccountBean accountBean);
    }
}
