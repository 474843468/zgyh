package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.openwealthmanager.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.openwealthmanager.model.OpenWealthModel;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * 开通和关闭投资理财服务 接口定义
 * Created by wangtong on 2016/10/20.
 */
public class OpenWealthManagerContact {
    /**
     * 开通状态页面
     */
    public interface OpenStatusView{
        /**
         * 查询理财账户成功
         */
        void getAccountSuccess();

        /**
         * 安全因子调用成功
         *
         * @param viewModel
         */
        public void psnGetSecurityFactorReturned(OpenWealthModel viewModel);
    }

    /**
     * 开通理财服务状态
     */
    public interface OpenView {

        public void psnInvestmentManageOpenConfirmReturned();

        public void psnInvestmentManageOpenReturned();
    }

    /**
     * 关闭理财服务状态
     */
    public interface CloseView {
        public void psnInvestmentManageCancelReturned();
    }

    public interface Presenter extends BasePresenter {
        /**
         * 查询理财账户信息
         */
        void queryFinanceAccountInfo();

        /**
         * 获取安全因子
         */
        public void psnGetSecurityFactor();

        /**
         * 开通理财服务确认
         */
        public void psnInvestmentManageOpenConfirm();

        /**
         * 开通理财服务提交
         */
        public void psnInvestmentManageOpen();

        /**
         * 关闭理财服务
         */
        public void psnInvestmentManageCancel();
    }
}
