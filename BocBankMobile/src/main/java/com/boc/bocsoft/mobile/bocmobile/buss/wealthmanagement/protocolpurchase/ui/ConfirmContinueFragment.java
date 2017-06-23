package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.ProtocolModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model.PsnXpadSignResultBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.presenter.ProtocolPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.utils.ProtocolConvertUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * 周期滚续投资--确认信息
 * Created by wangf on 2016-11-10 08:36:57.
 */
public class ConfirmContinueFragment extends MvpBussFragment<ProtocolPresenter> implements
        ProtocolContact.ProtocolPeriodContinueConfirmView {

    protected ConfirmInfoView confirmView;
    private View rootView;
    private ProtocolModel signResultViewModel;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_portfolio_confirm, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        confirmView = (ConfirmInfoView) rootView.findViewById(R.id.confirm_view);
    }

    @Override
    public void initData() {
        signResultViewModel = getArguments().getParcelable(ProtocolPeriodContinueFragment.CONTINUERESULT);

        confirmView.isShowSecurity(false);

        if ("0".equals(signResultViewModel.getAmountTypeCode())) {//定额
            confirmView.addData(initRegularView(), true, false);
        } else if ("1".equals(signResultViewModel.getAmountTypeCode())) {//不定额
            confirmView.addData(initNoRegularView(), true, false);
        }

        confirmView.isShowSecurity(false);
        String tipString = ProtocolConvertUtils.convertRiskMsg(mContext, signResultViewModel.getRiskMsg()) + getString(R.string.boc_protocol_smart_tips_added);
        confirmView.setHint(tipString);
    }

    @Override
    public void setListener() {
        super.setListener();
        confirmView.setListener(new ConfirmInfoView.OnClickListener() {
            @Override
            public void onClickConfirm() {
                getPresenter().psnXpadSignResult(signResultViewModel);
            }

            @Override
            public void onClickChange() {

            }
        });
    }

    @Override
    protected ProtocolPresenter initPresenter() {
        return new ProtocolPresenter(this);
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected void titleLeftIconClick() {
        pop();
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return "确认信息";
    }

    @Override
    public void psnXpadSignResultReturned(PsnXpadSignResultBean signResultBean) {
        signResultViewModel.setSignResultBean(signResultBean);
        getPresenter().queryContinueProductList(signResultViewModel.getSignInitBean().getProductCode(), signResultViewModel.getAccountId(), signResultViewModel.getCurCode());
    }

    @Override
    public void queryProductListSuccess(ArrayList<WealthListBean> wealthListBeans) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ResultContinueFragment.CONTINUE_RESULT, signResultViewModel);
        bundle.putSerializable(ResultContinueFragment.CONTINUE_WEALTHLIST, wealthListBeans);
        ResultContinueFragment fragment = new ResultContinueFragment();
        fragment.setArguments(bundle);
        startWithPop(fragment);
    }

    /**
     * 定额投资页面数据
     */
    private LinkedHashMap<String, String> initRegularView() {
        LinkedHashMap<String, String> confirmDetails = new LinkedHashMap<>();
        confirmDetails.put("当前产品名称", "[" + PublicCodeUtils.getCurrency(mContext, signResultViewModel.getSignInitBean().getCurCode()) + "]" + signResultViewModel.getSignInitBean().getProductName());
        confirmDetails.put("产品系列名称", signResultViewModel.getSignInitBean().getSerialName());
        if (!ApplicationConst.CURRENCY_CNY.equals(signResultViewModel.getSignInitBean().getCurCode())) {
            if ("01".equals(signResultViewModel.getXpadCashRemit())) {
                confirmDetails.put("钞/汇", "现钞");
            } else if ("02".equals(signResultViewModel.getXpadCashRemit())) {
                confirmDetails.put("钞/汇", "现汇");
            }
        }
        confirmDetails.put("购买期数", signResultViewModel.getTotalPeriod());
        confirmDetails.put("基础金额模式", "定额");
        confirmDetails.put("基础金额", MoneyUtils.transMoneyFormat(signResultViewModel.getBaseAmount(), signResultViewModel.getSignInitBean().getCurCode()));
        confirmDetails.put("理财交易账户", NumberUtils.formatCardNumberStrong(signResultViewModel.getAccountNum()));
        confirmDetails.put("产品风险级别", ProtocolConvertUtils.getProRisk(signResultViewModel.getProRisk()));
        confirmDetails.put("客户风险级别", ProtocolConvertUtils.getCustRisk(signResultViewModel.getCustRisk()));

        return confirmDetails;
    }

    /**
     * 不定额投资页面数据
     */
    private LinkedHashMap<String, String> initNoRegularView() {
        LinkedHashMap<String, String> confirmDetails = new LinkedHashMap<>();
        confirmDetails.put("当前产品名称", "[" + PublicCodeUtils.getCurrency(mContext, signResultViewModel.getSignInitBean().getCurCode()) + "]" + signResultViewModel.getSignInitBean().getProductName());
        confirmDetails.put("产品系列名称", signResultViewModel.getSignInitBean().getSerialName());
        if (!ApplicationConst.CURRENCY_CNY.equals(signResultViewModel.getSignInitBean().getCurCode())) {
            if ("01".equals(signResultViewModel.getXpadCashRemit())) {
                confirmDetails.put("钞/汇", "现钞");
            } else if ("02".equals(signResultViewModel.getXpadCashRemit())) {
                confirmDetails.put("钞/汇", "现汇");
            }
        }
        confirmDetails.put("购买期数", signResultViewModel.getTotalPeriod());
        confirmDetails.put("基础金额模式", "不定额");
        confirmDetails.put("最低预留金额", MoneyUtils.transMoneyFormat(signResultViewModel.getMinAmount(), signResultViewModel.getSignInitBean().getCurCode()));
        confirmDetails.put("最大扣款金额", MoneyUtils.transMoneyFormat(signResultViewModel.getMaxAmount(), signResultViewModel.getSignInitBean().getCurCode()));
        confirmDetails.put("理财交易账户", NumberUtils.formatCardNumberStrong(signResultViewModel.getAccountList().get(0).getAccountNo()));
        confirmDetails.put("产品风险级别", ProtocolConvertUtils.getProRisk(signResultViewModel.getProRisk()));
        confirmDetails.put("客户风险级别", ProtocolConvertUtils.getCustRisk(signResultViewModel.getCustRisk()));

        return confirmDetails;
    }


}
