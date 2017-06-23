package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.model.InvestTreatyConfirmModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.model.InvestTreatyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.presenter.InvestTreatyContract;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.presenter.InvestTreatyPresenter;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

import java.util.LinkedHashMap;

/**
 * 确认信息
 * Created by guokai on 2016/9/17.
 */
@SuppressLint("ValidFragment")
public class InvestTreatyConfirmFragment extends MvpBussFragment<InvestTreatyPresenter> implements InvestTreatyContract.InvestTreatyConfirmView, ConfirmInfoView.OnClickListener {
    private ConfirmInfoView detail;
    private InvestTreatyConfirmModel infoModel;
    private InvestTreatyModel.CapacityQueryBean model;

    public InvestTreatyConfirmFragment(InvestTreatyConfirmModel infoModel, InvestTreatyModel.CapacityQueryBean model) {
        this.infoModel = infoModel;
        this.model = model;
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_invest_treaty_confirm_info, null);
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_sure_message);
    }

    /**
     * 是否显示右侧标题按钮
     */
    protected boolean isDisplayRightIcon() {
        return false;
    }

    /**
     * 是否显示左侧标题按钮
     */
    protected boolean isDisplayLeftIcon() {
        return true;
    }

    /**
     * 红色主题titleBar：true ；
     * 白色主题titleBar：false ；
     */
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    public void initView() {
        detail = (ConfirmInfoView) mContentView.findViewById(R.id.detail);
        detail.isShowSecurity(false);
    }

    /**
     * 协议周期数转换
     */
    public String setPeriodtotal(String str) {
        if ("-1".equals(str)) {
            return getString(R.string.boc_invest_treaty_no_period);
        } else {
            return str;
        }
    }

    /**
     * 投资模式转换
     */
    public String setAmountType() {
        if ("0".equals(infoModel.getAmountType())) {
            return  getString(R.string.boc_invest_treaty_money);
        } else if ("1".equals(infoModel.getAmountType())){
            return getString(R.string.boc_invest_treaty_no_money);
        }
        return "";
    }

    @Override
    public void initData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(getString(R.string.boc_invest_treaty_my_acc_num), model.getAccNo().replace(model.getAccNo().substring(4, model.getAccNo().length() - 4), "******"));
        map.put(getString(R.string.boc_invest_treaty_amount_type), setAmountType());
        switch (infoModel.getInstType()) {
            case "1"://周期连续协议
            case "2"://周期不连续协议
            case "3"://多次购买协议
                if ("0".equals(infoModel.getAmountType())){
                        map.put(getString(R.string.boc_invest_treaty_amount), MoneyUtils.transMoneyFormat(infoModel.getAmount(), infoModel.getProCur()));
                } else {
                        map.put(getString(R.string.boc_invest_treaty_my_min_amount), MoneyUtils.transMoneyFormat(infoModel.getMinAmount(), infoModel.getProCur()));
                        map.put(getString(R.string.boc_invest_treaty_my_max_amount), MoneyUtils.transMoneyFormat(infoModel.getMaxAmount(), infoModel.getProCur()));
                }
                map.put(getString(R.string.boc_invest_treaty_new_buy_period), setPeriodtotal(infoModel.getBuyPeriod()));
                break;
            case "4"://多次赎回协议
                map.put(getString(R.string.boc_invest_treaty_unit_precent), infoModel.getUnit());
                map.put(getString(R.string.boc_invest_treaty_new_buy_period), setPeriodtotal(infoModel.getBuyPeriod()));
                if ("0".equals(infoModel.getIsneedpur())) {
                    map.put(getString(R.string.boc_invest_treaty_buy_money_precent), MoneyUtils.transMoneyFormat(infoModel.getAmount(), infoModel.getProCur()));
                }
                break;
            case "5"://定时定额投资
                map.put(getString(R.string.boc_invest_treaty_new_buy_period), setPeriodtotal(infoModel.getBuyPeriod()));
                if ("0".equals(infoModel.getTradeCode())) {
                    map.put(getString(R.string.boc_invest_treaty_amount_new_fen_precent), MoneyUtils.transMoneyFormat(infoModel.getAmount(),infoModel.getProCur()));
                } else {
                    map.put(getString(R.string.boc_invest_treaty_amount_new_jin_precent), MoneyUtils.transMoneyFormat(infoModel.getAmount(),infoModel.getProCur()));
                }
                break;
            case "6"://余额理财投资
                map.put(getString(R.string.boc_invest_treaty_trade_code_new_min_amount), MoneyUtils.transMoneyFormat(infoModel.getMinAmount(),infoModel.getProCur()));
                map.put(getString(R.string.boc_invest_treaty_trade_code_new_max_amount), MoneyUtils.transMoneyFormat(infoModel.getMaxAmount(),infoModel.getProCur()));
                map.put(getString(R.string.boc_invest_treaty_buy_period), setPeriodtotal(infoModel.getBuyPeriod()));
                break;
            case "7"://周期滚续协议
                map.put(getString(R.string.boc_invest_treaty_new_buy_period), setPeriodtotal(infoModel.getBuyPeriod()));
                if ("1".equals(infoModel.getAmountType())) {
                    map.put(getString(R.string.boc_invest_treaty_trade_new_min_amount), MoneyUtils.transMoneyFormat(infoModel.getMinAmount(),infoModel.getProCur()));
                    map.put(getString(R.string.boc_invest_treaty_trade_new_max_amount), MoneyUtils.transMoneyFormat(infoModel.getMaxAmount(),infoModel.getProCur()));
                } else if ("0".equals(infoModel.getAmountType())) {
                    map.put(getString(R.string.boc_invest_treaty_new_amount), MoneyUtils.transMoneyFormat(infoModel.getAmount(),infoModel.getProCur()));
                }
                break;
            case "8"://业绩基准周期滚续
                map.put(getString(R.string.boc_invest_treaty_amount), MoneyUtils.transMoneyFormat(infoModel.getAmount(), infoModel.getProCur()));
                map.put(getString(R.string.boc_invest_treaty_new_buy_period), setPeriodtotal(infoModel.getBuyPeriod()));
                break;
        }
        map.put(getString(R.string.boc_invest_treaty_finish_period), infoModel.getFinishperiod());
        detail.addData(map, true, true);
    }

    @Override
    public void setListener() {
        detail.setListener(this);
    }

    /**
     * 确认按钮
     */
    @Override
    public void onClickConfirm() {
        switch (infoModel.getInstType()) {
            case "1"://周期连续协议
            case "2"://周期不连续协议
            case "3"://多次购买协议
            case "4"://多次赎回协议
                showLoadingDialog();
                getPresenter().pnsXpadInvestAgreementModifyVerify(infoModel, model);
                break;
            case "5"://定时定额投资
            case "6"://余额理财投资
                showLoadingDialog();
                getPresenter().psnXpadAutomaticAgreementMaintainResult(infoModel, model);
                break;
            case "7"://周期滚续协议
                showLoadingDialog();
                getPresenter().psnXpadAgreementModifyResult(infoModel, model);
                break;
            case "8"://业绩基准周期滚续
                showLoadingDialog();
                getPresenter().psnXpadBenchmarkMaintainResult(infoModel, model,"1");
                break;
        }
    }

    /**
     * 更改按钮
     */
    @Override
    public void onClickChange() {

    }


    @Override
    public void setPresenter(BasePresenter presenter) {

    }

    @Override
    protected InvestTreatyPresenter initPresenter() {
        return new InvestTreatyPresenter(this);
    }
    /**
     * 投资协议修改预交易
     */
    @Override
    public void pnsXpadInvestAgreementModifyVerify() {
        getPresenter().psnXpadInvestAgreementModifyCommit(infoModel, model);
    }

    /**
     * 投资协议修改提交
     */
    @Override
    public void psnXpadInvestAgreementModifyCommit(InvestTreatyConfirmModel infoModel) {
        this.infoModel = infoModel;
        if (!StringUtils.isEmptyOrNull(infoModel.getTransactionId())) {
            ToastUtils.show(getString(R.string.boc_invest_treaty_change_period_success));
            popToAndReInit(InvestTreatyFragment.class);
        }else {
            ToastUtils.show(getString(R.string.boc_invest_treaty_change_period_failed));
        }
    }

    /**
     * 协议修改结果
     */
    @Override
    public void psnXpadAgreementModifyResult(InvestTreatyConfirmModel infoModel) {
        this.infoModel = infoModel;
        if (!StringUtils.isEmptyOrNull(infoModel.getTransactionId())) {
            ToastUtils.show(getString(R.string.boc_invest_treaty_change_period_success));
            popToAndReInit(InvestTreatyFragment.class);
        }else{
            ToastUtils.show(getString(R.string.boc_invest_treaty_change_period_failed));
        }
    }

    /**
     * 协议维护
     */
    @Override
    public void psnXpadAutomaticAgreementMaintainResult(InvestTreatyConfirmModel infoModel) {
        this.infoModel = infoModel;
        if (!StringUtils.isEmptyOrNull(infoModel.getTransactionId())) {
            ToastUtils.show(getString(R.string.boc_invest_treaty_change_period_success));
            popToAndReInit(InvestTreatyFragment.class);
        }else{
            ToastUtils.show(getString(R.string.boc_invest_treaty_change_period_failed));
        }
    }

    /**
     * 业绩基准周期滚续协议修改
     */
    @Override
    public void psnXpadBenchmarkMaintainResult(InvestTreatyConfirmModel infoModel) {
        this.infoModel = infoModel;
        if (!StringUtils.isEmptyOrNull(infoModel.getTransactionId())) {
            ToastUtils.show(getString(R.string.boc_invest_treaty_change_period_success));
            popToAndReInit(InvestTreatyFragment.class);
        }else{
            ToastUtils.show(getString(R.string.boc_invest_treaty_change_period_failed));
        }
    }
}
