package com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.ui;

import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQuotaQuery.PsnTransQuotaQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.model.QrcodeTransModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.math.BigDecimal;

/**
 * Created by wangtong on 2016/7/28.
 */
public class QrcodeTransContact {
    public interface View extends BaseView<Presenter> {

        /**
         * 付款人详情数据模型
         */
        QrcodeTransModel getModel();

        /**
         * 账户余额查询返回
         */
        void psnAccountQueryAccountDetailReturned();

        /**
         * 安全因子返回
         */
        void securityFactorReturned();

        /**
         * 中行内转账预交易返回
         */
        void psnTransBocTransferVerifyReturned();

        /**
         * 查询信用卡账户详情成功
         */
        void queryCreditAccountDetailReturned();

        /**查询限额
         * @param exception
         */
        void queryQuotaForTransFailed(BiiResultErrorException exception);

        /**查询限额
         * @param result
         */
        void queryQuotaForTransSuccess(PsnTransQuotaQueryResult result);

    }

    public interface Presenter extends BasePresenter {

        /**
         * 查询账户余额
         */
        void psnAccountQueryAccountDetail(String accId);

        /**
         * 获取安全因子
         */
        void psnGetSecurityFactor();

        /**
         * 中行内转账预交易
         */
        void psnTransBocTransferVerify();

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
