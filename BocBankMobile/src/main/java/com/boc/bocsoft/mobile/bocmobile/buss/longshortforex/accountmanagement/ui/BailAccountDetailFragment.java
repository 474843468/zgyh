package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.progress.LiquidationAndWarnRatio;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailAccountInfoListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailDetailQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGBailListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGGetBindAccountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.presenter.BailAccountDetailPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.ui.MarginManagementFragment;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Fragment：双向宝-账户管理-保证金账户详情
 * Created by zhx on 2016/11/23
 */
public class BailAccountDetailFragment extends MvpBussFragment<BailAccountDetailPresenter> implements BailAccountDetailContact.View {
    private View mRootView;
    private LinearLayout ll_container;
    private TextView tv_current_profit_loss;
    private TextView tv_margin_net_balance;
    private TextView tv_renminbi;
    private TextView tv_remit_banlance;
    private TextView tv_cash_banlance;
    private TextView tv_alarm_flag;
    private TextView tv_account_num;
    private TextView tv_change_contract;
    private TextView tv_currency_and_pro;
    private TextView tv_margin_occupied;
    private TextView tv_margina_vailable;
    private TextView tv_max_trade_amount;
    private TextView tv_margin_rate;
    private TextView tv_cancel_contract;
    private TextView tv_need_margin_ratio;
    private TextView tv_open_rate;
    private TextView tv_bail_transfer;
    private LiquidationAndWarnRatio lwr_ratio;
    private VFGBailListQueryViewModel.BailItemEntity bailItem;
    private VFGBailDetailQueryViewModel viewModel; // 详情的ViewModel
    private VFGGetBindAccountViewModel vfgGetBindAccountViewModel; // 双向宝交易账户ViewModel
    private boolean isAccountNumberSizeBiggerThanZero;
    private boolean isReInit = false; // 是否被reinit

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.fragment_bail_account_detail, null);
        return mRootView;
    }

    @Override
    public void initView() {
        ll_container = (LinearLayout) mRootView.findViewById(R.id.ll_container); //
        tv_current_profit_loss = (TextView) mRootView.findViewById(R.id.tv_current_profit_loss); // 账户暂计盈亏
        tv_margin_net_balance = (TextView) mRootView.findViewById(R.id.tv_margin_net_balance); // 保证金净值
        tv_renminbi = (TextView) mRootView.findViewById(R.id.tv_renminbi); // 人民币
        tv_remit_banlance = (TextView) mRootView.findViewById(R.id.tv_remit_banlance); // 资金汇余额
        tv_cash_banlance = (TextView) mRootView.findViewById(R.id.tv_cash_banlance); // 资金钞余额
        tv_alarm_flag = (TextView) mRootView.findViewById(R.id.tv_alarm_flag); // "余额不足"的图标
        tv_account_num = (TextView) mRootView.findViewById(R.id.tv_account_num); // 资金账户
        tv_change_contract = (TextView) mRootView.findViewById(R.id.tv_change_contract); // 更改账户
        tv_currency_and_pro = (TextView) mRootView.findViewById(R.id.tv_currency_and_pro); // 资金账户
        tv_margin_occupied = (TextView) mRootView.findViewById(R.id.tv_margin_occupied); // 已占用保证金
        tv_margina_vailable = (TextView) mRootView.findViewById(R.id.tv_margina_vailable); // 可用保证金
        tv_max_trade_amount = (TextView) mRootView.findViewById(R.id.tv_max_trade_amount); // 最大可交易额
        tv_margin_rate = (TextView) mRootView.findViewById(R.id.tv_margin_rate); // 保证金充足率
        tv_need_margin_ratio = (TextView) mRootView.findViewById(R.id.tv_need_margin_ratio);
        tv_open_rate = (TextView) mRootView.findViewById(R.id.tv_open_rate); // 交易所需保证金比例
        tv_bail_transfer = (TextView) mRootView.findViewById(R.id.tv_bail_transfer); // 保证金存入/转出
        tv_cancel_contract = (TextView) mRootView.findViewById(R.id.tv_cancel_contract); // 开仓充足率
        lwr_ratio = (LiquidationAndWarnRatio) mRootView.findViewById(R.id.lwr_ratio);
        lwr_ratio.post(new Runnable() {
            @Override
            public void run() {
                lwr_ratio.setRate(BigDecimal.valueOf(0.2), BigDecimal.valueOf(0.5));
            }
        });
    }

    @Override
    public void reInit() {
        isReInit = true;
        tv_account_num.setText(NumberUtils.formatCardNumber(bailItem.getAccountNumber())); // 重新显示资金账户的账号

        // 双向宝保证金账户基本信息多笔查询
        showLoadingDialog("加载中...");
        VFGBailAccountInfoListQueryViewModel viewModel = new VFGBailAccountInfoListQueryViewModel();
        getPresenter().vFGBailAccountInfoListQuery(viewModel);
    }

    @Override
    public void initData() {
        bailItem = getArguments().getParcelable("bailItem");
        isAccountNumberSizeBiggerThanZero = getArguments().getBoolean("isAccountNumberSizeBiggerThanZero");
        vfgGetBindAccountViewModel = getArguments().getParcelable("vfgGetBindAccountViewModel");

        diaplayData();

        tv_bail_transfer.setVisibility(bailItem.isCurrentAccount() ? View.VISIBLE : View.GONE); // “保证金存入/转出”的隐藏和显示

        showLoadingDialog("加载中...");
        getPresenter().vFGBailDetailQuery(viewModel);
    }

    // 展示数据
    private void diaplayData() {
        // 账户暂计盈亏
        BigDecimal currentProfitLoss = bailItem.getCurrentProfitLoss();
        String profitLossFlag = bailItem.getProfitLossFlag();
        if (currentProfitLoss != null) {
            String currentProfitLossStr = MoneyUtils.transMoneyFormat(currentProfitLoss, bailItem.getSettleCurrency());
            if ("P".equals(profitLossFlag)) {
                tv_current_profit_loss.setText(currentProfitLossStr);
            } else {
                tv_current_profit_loss.setText("-" + currentProfitLossStr);
            }
        }

        // 保证金净值
        BigDecimal marginNetBalance = bailItem.getMarginNetBalance();
        if (marginNetBalance != null) {
            String marginNetBalanceStr = MoneyUtils.transMoneyFormat(marginNetBalance, bailItem.getSettleCurrency());
            tv_margin_net_balance.setText(marginNetBalanceStr);
        }

        // 资金汇余额/资金钞余额
        String settleCurrency = bailItem.getSettleCurrency();
        String currency = PublicCodeUtils.getCurrency(mActivity, settleCurrency);
        if ("001".equals(settleCurrency)) {
            tv_renminbi.setVisibility(View.VISIBLE);
            tv_remit_banlance.setVisibility(View.GONE);
            tv_cash_banlance.setVisibility(View.INVISIBLE);

            BigDecimal marginFund = bailItem.getMarginFund();
            if (marginFund != null) {
                String marginFundStr = MoneyUtils.transMoneyFormat(marginFund, settleCurrency);
                tv_renminbi.setText(currency + " " + marginFundStr);
            }
        } else {
            tv_renminbi.setVisibility(View.GONE);
            tv_remit_banlance.setVisibility(View.VISIBLE);
            tv_cash_banlance.setVisibility(View.VISIBLE);

            // 资金汇余额
            BigDecimal remitBanlance = bailItem.getRemitBanlance();
            if (remitBanlance != null) {
                String remitBanlanceStr = MoneyUtils.transMoneyFormat(remitBanlance, settleCurrency);
                tv_remit_banlance.setText(currency + "/现汇 " + remitBanlanceStr);
            }

            // 资金钞余额
            BigDecimal cashBanlance = bailItem.getCashBanlance();
            if (cashBanlance != null) {
                String cashBanlanceStr = MoneyUtils.transMoneyFormat(cashBanlance, settleCurrency);
                tv_cash_banlance.setText(currency + "/现钞 " + cashBanlanceStr);
            }
        }

        // 是否显示"余额不足"
        String alarmFlag = bailItem.getAlarmFlag();
        if (alarmFlag != null) {
            tv_alarm_flag.setVisibility("Y".equals(alarmFlag) ? View.VISIBLE : View.GONE);
        }

        tv_account_num.setText(NumberUtils.formatCardNumber(bailItem.getAccountNumber())); // 资金账户
        tv_change_contract.setVisibility(isAccountNumberSizeBiggerThanZero ? View.VISIBLE : View.GONE);

        String marginAccountNo = bailItem.getMarginAccountNo();
        int index = marginAccountNo.indexOf("00000");
        if (index == 0) {
            String substring = marginAccountNo.substring(5, marginAccountNo.length());
            tv_currency_and_pro.setText("[" + currency + "]" + "保证金 " + NumberUtils.formatCardNumber(substring));
        } else {
            tv_currency_and_pro.setText("[" + currency + "]" + "保证金 " + NumberUtils.formatCardNumber(marginAccountNo));
        }


        // 已占用保证金
        BigDecimal marginOccupied = bailItem.getMarginOccupied();
        if (marginOccupied != null) {
            String marginOccupiedStr = MoneyUtils.transMoneyFormat(marginOccupied, settleCurrency);
            tv_margin_occupied.setText(marginOccupiedStr);
        }

        // 可用保证金
        BigDecimal marginAvailable = bailItem.getMarginAvailable();
        if (marginAvailable != null) {
            String marginAvailableStr = MoneyUtils.transMoneyFormat(marginAvailable, settleCurrency);
            tv_margina_vailable.setText(marginAvailableStr);
        }

        // 最大可交易额
        BigDecimal maxTradeAmount = bailItem.getMaxTradeAmount();
        if (maxTradeAmount != null) {
            String maxTradeAmountStr = MoneyUtils.transMoneyFormat(maxTradeAmount, settleCurrency);
            tv_max_trade_amount.setText(maxTradeAmountStr);
        }

        // 保证金充足率
        BigDecimal marginRate = bailItem.getMarginRate();
        if (marginRate != null) {
            int value = (int) (Double.valueOf(marginRate.toString()) * 100);
            tv_margin_rate.setText(value + "%");
        }

        viewModel = new VFGBailDetailQueryViewModel();
        viewModel.setAccountNumber(bailItem.getAccountNumber()); // 借记卡卡号
        viewModel.setSettleCurrency(bailItem.getSettleCurrency()); // 结算货币
    }

    @Override
    public void setListener() {
        // 变更签约账户
        tv_change_contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeContractFragment toFragment = new ChangeContractFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("bailItem", bailItem);
                bundle.putParcelable("vFGBailDetailQueryViewModel", viewModel);
                bundle.putParcelable("vfgGetBindAccountViewModel", vfgGetBindAccountViewModel);
                toFragment.setArguments(bundle);
                start(toFragment);
            }
        });

        // 双向宝解约
        tv_cancel_contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmCancelContractFragment contractFragment = new ConfirmCancelContractFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("bailItem", bailItem);
                bundle.putParcelable("vFGBailDetailQueryViewModel", viewModel);
                contractFragment.setArguments(bundle);
                start(contractFragment);
            }
        });

        tv_bail_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarginManagementFragment.newInstance(mActivity, BailAccountDetailFragment.class, bailItem.getSettleCurrency());
            }
        });
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return "明细";
    }

    @Override
    protected BailAccountDetailPresenter initPresenter() {
        return new BailAccountDetailPresenter(this);
    }

    @Override
    public void vFGBailDetailQuerySuccess(VFGBailDetailQueryViewModel vfgBailDetailQueryViewModel) {
        closeProgressDialog();
        ll_container.setVisibility(View.VISIBLE);

        // 交易所需保证金比例
        BigDecimal needMarginRatio = vfgBailDetailQueryViewModel.getNeedMarginRatio();
        String needMarginRatioStr = MoneyUtils.transRateFormat(needMarginRatio.toString());
        int needMarginRatioValue = (int) (Double.valueOf(needMarginRatioStr) * 100);
        tv_need_margin_ratio.setText(needMarginRatioValue + "%");

        // 开仓充足率
        BigDecimal openRate = vfgBailDetailQueryViewModel.getOpenRate();
        String openRateStr = MoneyUtils.transRateFormat(openRate.toString());
        int openRateValue = (int) (Double.valueOf(needMarginRatioStr) * 100);
        tv_open_rate.setText(openRateValue + "%");

        //        lwr_ratio.setRate(vfgBailDetailQueryViewModel.getLiquidationRatio(), vfgBailDetailQueryViewModel.getWarnRatio());
    }

    @Override
    public void vFGBailDetailQueryFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /**
     * 成功回调：
     * 双向宝-账户管理-双向宝保证金账户基本信息多笔查询
     */
    @Override
    public void vFGBailAccountInfoListQuerySuccess(VFGBailAccountInfoListQueryViewModel viewModel) {
        closeProgressDialog();
        List<VFGBailAccountInfoListQueryViewModel.VFGBailAccountInfo> list = viewModel.getList();
        for (VFGBailAccountInfoListQueryViewModel.VFGBailAccountInfo vfgBailAccountInfo : list) {
            if (bailItem.getMarginAccountNo().equals(vfgBailAccountInfo.getMarginAccountNo())) {
//                bailItem.setCurrentProfitLoss(vfgBailAccountInfo.getCurrentProfitLoss()); // 暂计盈亏标志
//                bailItem.setMarginNetBalance(vfgBailAccountInfo.getMarginNetBalance()); // 账户净值
//                bailItem.setMarginFund(vfgBailAccountInfo.getMarginFund()); // 账户余额
//                bailItem.setMarginOccupied(vfgBailAccountInfo.getMarginOccupied()); // 已占用保证金
//                bailItem.setMarginAvailable(vfgBailAccountInfo.getMarginAvailable()); // 可用保证金
//                bailItem.setMaxTradeAmount(vfgBailAccountInfo.getMaxTradeAmount()); // 最大可交易额
//                bailItem.setMaxDrawAmount(vfgBailAccountInfo.getMaxDrawAmount()); // 最大可提取额
//                bailItem.setMarginRate(vfgBailAccountInfo.getMarginRate()); // 保证金充足率
//                bailItem.setAlarmFlag(vfgBailAccountInfo.getAlarmFlag()); // 是否已经进入报警区
//                bailItem.setCashBanlance(vfgBailAccountInfo.getCashBanlance()); // 资金钞余额
//                bailItem.setRemitBanlance(vfgBailAccountInfo.getRemitBanlance()); // 资金汇余额

                BeanConvertor.toBean(vfgBailAccountInfo, bailItem);

                diaplayData();

                break;
            }
        }
    }

    /**
     * 失败回调：
     * 双向宝-账户管理-双向宝保证金账户基本信息多笔查询
     */
    @Override
    public void vFGBailAccountInfoListQueryFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void setPresenter(BailAccountDetailContact.Presenter presenter) {
    }

    @Override
    public boolean onBackPress() {
        Log.e("ljljlj", "onBackPress()");
        if (isReInit) {
            popToAndReInit(AccountManagementFragment.class);
            return false;
        } else {
            return super.onBackPress();
        }
    }
}
