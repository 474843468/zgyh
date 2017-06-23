package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.ui;

import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQuotaQuery.PsnTransQuotaQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.model.ConfirmPaymentModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by wangtong on 2016/7/21.
 */
public class ConfirmPaymentContact {
    public interface View extends BaseView<Presenter> {

        /**
         * 付款人详情数据模型
         */
        ConfirmPaymentModel getModel();

        /**
         * 账户余额查询返回
         */
        void psnAccountQueryAccountDetailReturned();

        /**
         * 查询信用卡账户详情成功
         */
        void queryCreditAccountDetailReturned();

        /**限额查询
         * @param exception
         */
        void queryQuotaForTransFailed(BiiResultErrorException exception);

        /**限额查询
         * @param result
         */
        void queryQuotaForTransSuccess(PsnTransQuotaQueryResult result);

    }

    public interface Presenter extends BasePresenter {

        /**
         * 查询账户余额
         */
        void psnAccountQueryAccountDetail();

        /***
         * 查询信用卡账户详情
         */
        void queryCreditAccountDetail(String accountId);

        /**
         * 限额查询
         */
        void psnTransQuotaquery();
    }
}
