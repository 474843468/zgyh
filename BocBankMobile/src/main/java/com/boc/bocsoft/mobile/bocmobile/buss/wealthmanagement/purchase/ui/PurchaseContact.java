package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.model.PurchaseModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * Created by wangtong on 2016/9/20.
 */
public class PurchaseContact {

    public interface PurchaseView extends BaseView<BasePresenter> {
        //查询账户详情返回
        void queryAccountDetail(PurchaseModel purchaseModel);

        //用户风险匹配结果
        void queryRiskMatch(PurchaseModel purchaseModel);

        //购买预交易返回
        void productBuyPre(PurchaseModel purchaseModel);
    }

    public interface PurchaseTransactionView extends BaseView<BasePresenter> {
        //购买交易提交结果
        void productBuySubmit(PurchaseModel purchaseModel,List<WealthListBean> likeBeans);
    }

    public interface PurchasePresenter extends BasePresenter {
        //查询账户详情
        void queryAccountDetail(PurchaseModel purchaseModel);

        //用户风险匹配
        void queryRiskMatch(PurchaseModel purchaseModel);

        //购买预交易
        void productBuyPre(PurchaseModel purchaseModel);

        //购买交易提交
        void productBuySubmit(PurchaseModel purchaseModel);
    }
}
