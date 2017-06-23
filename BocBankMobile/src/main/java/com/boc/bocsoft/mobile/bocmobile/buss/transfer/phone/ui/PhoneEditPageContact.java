package com.boc.bocsoft.mobile.bocmobile.buss.transfer.phone.ui;

import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQuotaQuery.PsnTransQuotaQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.phone.model.PhoneEditModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by wangtong on 2016/7/27.
 */
public class PhoneEditPageContact {
    public interface View extends BaseView<Presenter> {
        /**
         * 付款人详情数据模型
         */
        PhoneEditModel getModel();

        void psnGetSecurityFactorReturned();

        /**
         * 账户余额查询返回
         */
        void psnAccountQueryAccountDetailReturned();

        /**
         * 查询信用卡账户详情成功
         */
        void queryCreditAccountDetailReturned();

        /**
         * 查询限额
         *
         * @param exception
         */
        void queryQuotaForTransFailed(BiiResultErrorException exception);

        /**
         * 查询限额
         *
         * @param result
         */
        void queryQuotaForTransSuccess(PsnTransQuotaQueryResult result);

    }

    public interface Presenter extends BasePresenter {

        void psnGetSecurityFactor();

        /**
         * 查询账户余额
         */
        void psnAccountQueryAccountDetail(String accId);

        /***
         * 查询信用卡账户详情
         */
        void queryCreditAccountDetail(String accountId);

        /**
         * 查询限额
         */
        void psnTransQuotaquery();
    }
}
