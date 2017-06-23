package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.ui;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.XpadApplyAgreementResultViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.model.PurchaseModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * 余额理财投资确认
 * Created by zhx on 2016/11/10
 */
public class ProtocolBalanceInvestConfirmContact {
    public interface View extends BaseView<Presenter> {
        /**
         * 成功回调：投资协议申请
         */
        void applyAgreementResultSuccess(XpadApplyAgreementResultViewModel viewModel);

        /**
         * 失败回调：投资协议申请
         */
        void applyAgreementResultFailed(BiiResultErrorException biiResultErrorException);

        /**
         * 成功回调：查询产品列表（猜你喜欢）
         */
        void queryProductListSuccess(List<WealthListBean> wealthListBeans);

        /**
         * 失败回调：查询产品列表（猜你喜欢）
         */
        void queryProductListFailed(BiiResultErrorException biiResultErrorException);
    }


    public interface Presenter extends BasePresenter {
        /**
         * 投资协议申请
         */
        void applyAgreementResult(XpadApplyAgreementResultViewModel viewModel);

        /**
         * 查询产品列表（猜你喜欢）
         */
        void queryProductList(String productCode, String productRisk, final PurchaseModel purchaseModel);
    }
}
