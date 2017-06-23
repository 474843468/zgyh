package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.data;

import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.model.WealthAccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.portfoliopurchase.model.PortfolioPurchaseInputModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.ProtocolModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.model.PurchaseInputMode;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.DetailsRequestBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthDetailsBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthProductFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.utils.ResultConvertUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 页面间跳转数据的传递
 * Created by liuweidong on 2016/10/22.
 */
public class WealthBundleData {

    /**
     * 封装详情页额外的数据
     */
    public static DetailsRequestBean buildDetailsExtraData(WealthViewModel viewModel,
                                                           int curPosition) {
        DetailsRequestBean requestBean = new DetailsRequestBean();
        requestBean.setList(viewModel.getAccountList());// 理财账户列表
        requestBean.setAccountBean(viewModel.getAccountBean());// 当前筛选账户
        requestBean.setIsBuy(viewModel.getProductList().get(curPosition).getIsBuy());// 是否购买
        requestBean.setGroupBuy(
                viewModel.getProductList().get(curPosition).getImpawnPermit());// 是否组合购买
        requestBean.setIsAgreement(
                viewModel.getProductList().get(curPosition).getIsAgreement());// 是否协议请求
        requestBean.setIsProfitTest(
                viewModel.getProductList().get(curPosition).getIsProfiTest());// 是否可以收益试算
        return requestBean;
    }

    /**
     * 封装组合购买数据
     */
    public static PortfolioPurchaseInputModel buildGroupBuyData(WealthDetailsBean detailsBean,
                                                                DetailsRequestBean requestBean) {
        PortfolioPurchaseInputModel model = new PortfolioPurchaseInputModel();
        model.setDetailsBean(detailsBean);
        model.setAccountBean(requestBean.getAccountBean() == null ? new WealthAccountBean()
                : requestBean.getAccountBean());
        return model;
    }

    /**
     * 封装购买数据
     */
    public static PurchaseInputMode buildBuyData(WealthDetailsBean detailsBean,
                                                 WealthAccountBean accountBean) {
        PurchaseInputMode model = new PurchaseInputMode();
        if (accountBean != null) {
            model.payAccountId = accountBean.getAccountId();
            model.payAccountNum = accountBean.getAccountNo();
            model.accountKey = accountBean.getAccountKey();
            model.payAccountBancID = accountBean.getBancID();
            model.payAccountStatus = accountBean.getXpadAccountSatus();
            model.payAccountType = accountBean.getAccountType();
        }
        model.productKind = detailsBean.getProductKind();// 产品性质
        model.prodCode = detailsBean.getProdCode();
        model.productType = detailsBean.getProductType();
        model.prodRiskType = detailsBean.getProdRiskType();
        model.productTermType = detailsBean.getProductTermType();
        model.productBegin = detailsBean.getProdBegin();
        model.productEnd = detailsBean.getProdEnd();
        if (!StringUtils.isEmptyOrNull(detailsBean.getMaxPeriod()))
            model.maxPeriodNumber = Integer.parseInt(detailsBean.getMaxPeriod());
        model.prodName = detailsBean.getProdName();
        model.curCode = detailsBean.getCurCode();
        model.isCanCancle = detailsBean.getIsCanCancle();// 认购/申购撤单设置
        model.transTypeCode = detailsBean.getTransTypeCode();// 购买交易类型
        model.subscribeFee = detailsBean.getSubscribeFee();
        model.purchFee = detailsBean.getPurchFee();// 申购手续费（净值）
        model.subAmount = detailsBean.getSubAmount();// 认购起点金额
        model.addAmount = detailsBean.getAddAmount();// 追加认申购起点金额
        model.baseAmount = detailsBean.getBaseAmount();
        model.appdatered = detailsBean.getAppdatered();// 是否允许指定日期赎回
        model.redEmptionStartDate = detailsBean.getRedEmptionStartDate();
        model.redEmptionEndDate = detailsBean.getRedEmptionEndDate();
        model.redEmptionHoliday = detailsBean.getRedEmptionHoliday();
        model.isLockPeriod = detailsBean.getIsLockPeriod();
        model.prodTimeLimit = detailsBean.getProdTimeLimit();
        model.periodical = ResultConvertUtils.convertPeriodical(detailsBean.getPeriodical());
        model.sellType = detailsBean.getSellType();
        model.redEmperiodfReq = detailsBean.getRedEmperiodfReq();
        model.redEmperiodStart = detailsBean.getRedEmperiodStart();
        model.redEmperiodEnd = detailsBean.getRedEmperiodEnd();
        model.couponpayFreq = detailsBean.getCouponpayFreq();
        model.interestDate = detailsBean.getInterestDate();
        model.periodPrice = detailsBean.getPrice();
        model.priceDate = detailsBean.getPriceDate();
        model.yearlyRR = detailsBean.getYearlyRR();
        model.rateDetail = detailsBean.getRateDetail();

        if (StringUtils.isEmptyOrNull(detailsBean.getAvailamt())) {
            model.creditBalance = new BigDecimal(0.00);
        } else {
            model.creditBalance = new BigDecimal(detailsBean.getAvailamt());
        }
        return model;
    }

    /**
     * 封装协议申请数据
     */
    public static ProtocolModel buildProtocolData(ProtocolModel viewModel,
                                                  WealthDetailsBean detailsBean, DetailsRequestBean requestBean) {
        if (requestBean.getAccountBean() != null) {
            List<WealthAccountBean> list = new ArrayList<>();
            list.add(requestBean.getAccountBean());
            viewModel.setAccountList(list);
        } else {
            viewModel.setAccountList(WealthProductFragment.getInstance().getViewModel().getAccountList());
        }
        viewModel.setProId(detailsBean.getProdCode());
        viewModel.setProductKind(detailsBean.getProductKind());
        viewModel.setProdName(detailsBean.getProdName());
        viewModel.setCurCode(detailsBean.getCurCode());
        viewModel.setSubAmount(detailsBean.getSubAmount());
        viewModel.setAddAmount(detailsBean.getAddAmount());
        viewModel.setLowLimitAmount(detailsBean.getLowLimitAmount());
        viewModel.setPeriodical(detailsBean.getPeriodical());
        viewModel.setPrice(detailsBean.getPrice());
        viewModel.setPriceDate(detailsBean.getPriceDate());
        viewModel.setYearlyRR(detailsBean.getYearlyRR());
        viewModel.setRateDetail(detailsBean.getRateDetail());
        viewModel.setProdTimeLimit(detailsBean.getProdTimeLimit());
        viewModel.setIsLockPeriod(detailsBean.getIsLockPeriod());
        viewModel.setProductTermType(detailsBean.getProductTermType());
        return viewModel;
    }
}
