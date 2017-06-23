package com.boc.bocsoft.mobile.bocmobile.buss.account.notify.contact;

import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.model.EditFeeAccountModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by wangtong on 2016/8/11.
 */
public class EditFeeAccountContact {
    public interface View extends BaseView<Presenter> {
        //安全因子返回
        void securityFactorReturned();

        //获取UImodel
        EditFeeAccountModel getUiModel();

        //修改付费账户预交易成功
        void psnSsmAccountChangePreReturned();

        //修改付费账户成功
        void psnSsmAccountChangeReturned();

    }

    public interface Presenter extends BasePresenter {

        //修改付费账户预交易
        void psnSsmAccountChangePre();

        //修改付费账户
        void psnSsmAccountChange();

        //获取安全因子
        void psnGetSecurityFactor();

    }
}
