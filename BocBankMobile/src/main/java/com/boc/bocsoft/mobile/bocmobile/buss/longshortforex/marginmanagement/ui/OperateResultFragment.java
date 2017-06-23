package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.ResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.ResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.MenuFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui.BailAccountDetailFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.balanceenquiry.ui.BalanceEnquiryFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.marginmanagement.presenter.OperateResultPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.LinkedHashMap;

/**
 * @author hty7062
 *         保证金转入转出--操作结果页面
 */
public class OperateResultFragment extends MvpBussFragment<OperateResultContract.Presenter> implements OperateResultContract.View {

    private View rootView;
    private OperateResultContract.Presenter mOperateResultPresenter;
    private BaseResultView paymentResult;
    //mAmount 存入/转出金额数
    private String mAmount;
    private String settlementCurrency;
    private String cashRemit;
    private String currencyCode;
    private String settleMarginAccount;
    private Boolean modeTransfer;
    private String stockBalance;
    //pageClass 从那个页面进入
    private Class<? extends BussFragment> pageClass;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_operate_result2, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        paymentResult = (BaseResultView) rootView.findViewById(R.id.base_operation_result_view);
    }

    @Override
    public void initData() {
        mAmount = getArguments().getString("MAMOUNT");
        modeTransfer = getArguments().getBoolean("MODE_TRANSFER");
        currencyCode = getArguments().getString("CURRENCY_CODE");
        cashRemit = getArguments().getString("CASH_REMIT");
        settleMarginAccount = getArguments().getString("SETTLE_MARGIN_ACCOUNT");
        settlementCurrency = getArguments().getString("SETTLEMENT_CURRENCY");
        stockBalance = getArguments().getString("STOCK_BALANCE");
        pageClass = (Class<? extends BussFragment>) getArguments().getSerializable("pageClass");

        if (modeTransfer) {
            paymentResult.addStatus(ResultHead.Status.SUCCESS, "保证金存入成功");
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            if ("001".equals(currencyCode)) {
                map.put("存入金额", "人民币元 " + MoneyUtils.transMoneyFormat(mAmount, currencyCode));
            } else {
                map.put("存入金额", PublicCodeUtils.getCurrency(mContext, currencyCode) + " " + settlementCurrency + " " + MoneyUtils.transMoneyFormat(mAmount, currencyCode));
            }
            map.put("保证金账户余额", MoneyUtils.transMoneyFormat(stockBalance, currencyCode));
            paymentResult.addTopDetail(map);

            //点击交易明细展开具体内容
            paymentResult.updateDetail("查看详情");
            LinkedHashMap<String, String> map1 = new LinkedHashMap<>();
            map1.put("资金账户", NumberUtils.formatCardNumberStrong(settleMarginAccount));
            map1.put("操作方式", "资金账户转保证金账户");
            map1.put("网银交易序号", getArguments().getString("TRANSACTION_NO"));
            paymentResult.addDetail(map1);
        } else {
            paymentResult.updateDetail("查看详情");
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            paymentResult.addStatus(ResultHead.Status.SUCCESS, "保证金转出成功");
            if ("001".equals(currencyCode)) {
                map.put("转出金额", "人民币元 " + MoneyUtils.transMoneyFormat(mAmount, currencyCode));
            } else {
                map.put("转出金额", PublicCodeUtils.getCurrency(mContext, currencyCode) + " " + settlementCurrency + " " + MoneyUtils.transMoneyFormat(mAmount, currencyCode));
            }
            map.put("保证金账户余额", MoneyUtils.transMoneyFormat(stockBalance, currencyCode));
            paymentResult.addTopDetail(map);

            //点击交易明细展开具体内容
            LinkedHashMap<String, String> map1 = new LinkedHashMap<>();
            map1.put("资金账户", NumberUtils.formatCardNumberStrong(settleMarginAccount));
            map1.put("操作方式", "保证金账户转资金账户");
            map1.put("网银交易序号", getArguments().getString("TRANSACTION_NO"));
            paymentResult.addDetail(map1);
        }

        //添加你可能需要中的东西
        paymentResult.addNeedItem("买卖交易", 1);
        paymentResult.addNeedItem("继续保证金存入/转出", 2);
    }

    @Override
    protected OperateResultContract.Presenter initPresenter() {
        return new OperateResultPresenter(this);
    }

    @Override
    public void setListener() {
        paymentResult.setNeedListener(new ResultBottom.OnClickListener() {
            @Override
            public void onClick(int id) {
                if (id == 1) {
                    //popToAndReInit(MarginManagementFragment.class);
                    mActivity.finish();
                }
                if (id == 2) {
                    popToAndReInit(MarginManagementFragment.class);
                }
            }
        });
        paymentResult.setOnHomeBackClick(new BaseResultView.HomeBackListener() {
            @Override
            public void onHomeBack() {
                mActivity.finish();
            }
        });
    }

    @Override
    public boolean onBackPress() {
        backEnterPage();
        return true;
    }

    @Override
    protected String getTitleValue() {
        return "操作结果";
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected void titleLeftIconClick() {
        backEnterPage();
        super.titleLeftIconClick();
    }

//    @Override
//    public boolean onBack() {
//        backEnterPage();
//        return true;
//    }

    /**
     * 结果页返回指定页面
     */
    public void backEnterPage(){
        //从更多进入
        if (MenuFragment.class.equals(pageClass)) {
            popToAndReInit(MarginManagementFragment.class);
        }

        //从余额查询进入
        if (BalanceEnquiryFragment.class.equals(pageClass)) {
            popToAndReInit(pageClass);
        }

        //账户管理明细进入
        if (BailAccountDetailFragment.class.equals(pageClass)) {
            popToAndReInit(pageClass);
        }
    }
}