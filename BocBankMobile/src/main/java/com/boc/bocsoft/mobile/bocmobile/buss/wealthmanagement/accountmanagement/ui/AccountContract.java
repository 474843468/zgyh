package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.ui;

import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryResult;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.model.AccountModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * 理财——账户管理回调
 * Created by Wan mengxin on 2016/9/20.
 */
public class AccountContract {

    //主界面回调
    public interface MainView extends BaseView<Presenter> {

        AccountModel getModel();

        void psnXpadAccountQuerySuccess(List<PsnXpadAccountQueryResult.XPadAccountEntity> mlist);

        void psnXpadAccountQueryFailed();

        void psnOFAAccountStateQuerySuccess();

        void psnOFAAccountStateQueryFailed();
    }

    //登记界面回调
    public interface RegistView extends BaseView<Presenter> {

        AccountModel getModel();

        void psnXpadResetSuccess(List<PsnXpadAccountQueryResult.XPadAccountEntity> mlist);

        void psnXpadResetFailed();

        void psnXpadResultSuccess();

        void psnXpadResultFailed();

    }

    //详情页回调
    public interface DetailView extends BaseView<Presenter> {

        AccountModel getModel();

        void psnAccountQueryAccountDetailSuccess();

        void psnAccountQueryAccountDetailFailed();

        void psnXpadAccountCancelSuccess();

        void psnXpadAccountCancelFailed();

        void psnOFADisengageBindSuccess();

        void psnOFADisengageBindFailed();

    }

    public interface Presenter extends BasePresenter {

        void psnInvtEvaluationInit();

        void psnOFAAccountStateQuery();

        void psnXpadReset();

        void psnXpadResult(String id);

        void psnXpadAccountCancel(String id);

        void psnOFADisengageBind(String key);

        void psnAccountQueryAccountDetail(String id);

    }
}
