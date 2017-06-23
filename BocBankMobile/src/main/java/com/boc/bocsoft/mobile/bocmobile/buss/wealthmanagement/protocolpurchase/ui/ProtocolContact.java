package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.ui;

import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.ProtocolIntelligentDetailsBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.ProtocolModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.PsnXpadSignInitBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.PsnXpadSignResultBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.ArrayList;

/**
 * 投资协议申请--网络请求与接口回调
 * Created by wangtong on 2016/10/24.
 * Modified by liuweidong on 2016/11/4.
 */
public class ProtocolContact {

    public interface ProtocolView {
        /**
         * 智能协议详情查询成功
         */
        void queryTreatyDetailSuccess(ProtocolIntelligentDetailsBean details);

        /**
         * 周期性产品续约协议签约/签约初始化查询成功
         */
        void psnXpadSignInitSuccess(PsnXpadSignInitBean initBean);
    }

    public interface ProtocolIntelligentView{
        /**
         * 风险匹配成功
         */
        void queryRiskMatchSuccess();

        /**
         * 智能协议申请预交易成功
         */
        void confirmTreatySuccess();
    }

    public interface ProtocolSmartConfirmView {
        /**
         * 智能协议申请提交交易成功
         */
        void resultTreatySuccess();
        void queryProductListSuccess(ArrayList<WealthListBean> wealthListBeans);
    }

    /**
     * 定时定额投资
     */
    public interface ProtocolFixPurchaseView {
        void psnXpadQueryRiskMatchReturned();
    }

    public interface ProtocolFixConfirmView extends BaseView<Presenter> {
        public void psnXpadApplyAgreementResultReturned();
    }

    /**
     * 周期滚续投资 - 提交页
     */
    public interface ProtocolPeriodContinueView{
        void psnXpadQueryRiskMatchReturned();
    }

    /**
     * 周期滚续投资 - 确认页
     */
    public interface ProtocolPeriodContinueConfirmView{
        void psnXpadSignResultReturned(PsnXpadSignResultBean signResultBean);
        void queryProductListSuccess(ArrayList<WealthListBean> wealthListBeans);
    }

    public interface ProtocolPeriodConfirmView extends BaseView<Presenter> {
        public void psnXpadSignResultReturned();
    }

    public interface Presenter extends BasePresenter {
        /**
         * 4.74 074 智能协议详情查询
         */
        void queryTreatyDetail();

        /**
         * 4.24 024查询客户风险等级与产品风险等级是否匹配
         */
        void queryRiskMatch(ProtocolModel viewModel);

        /**
         * 4.24 024查询客户风险等级与产品风险等级是否匹配
         * 周期滚续专用
         */
        void queryRiskMatchByContinue(String serialCode, String accountKey);

        /**
         * 4.61 061 智能协议申请预交易
         */
        void confirmTreaty(ProtocolModel viewModel);

        /**
         * 4.62 062 智能协议申请提交交易
         */
        void resultTreaty(ProtocolModel viewModel);

        /**
         * 4.28 028投资协议申请（定额定投与余额理财）
         */
        void resultApplyAgreement();

        void psnXpadAptitudeTreatyApplyVerify();

        /**
         * 4.10 010周期性产品续约协议签约/签约初始化
         */
        void psnXpadSignInit();

        /**
         * 4.11 011周期性产品续约协议签约/签约结束
         */
        void psnXpadSignResult(ProtocolModel viewModel);

        void queryContinueProductList(String prodCode, String accountId, String curCode);

    }
}
