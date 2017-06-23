package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.ResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.ResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.model.BuyExchangeModel;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.presenter.BuySellExchangePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.common.BuyExchangeCurrencyUtil;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.LinkedHashMap;

/**
 * 购汇结果页面
 * Created by gwluo on 2016/12/8.
 */

public class BuyExchangeResultFragment extends MvpBussFragment<BuySellExchangePresenter> {
    private BaseResultView resultView;
    private final int GO_ON_BUY_EXC = 1;
    private final int QRY_BALANCE = 2;
    private final int CROSS_BORDER_TRANS = 3;
    private BuyExchangeModel model;//页面model

    @Override

    protected BuySellExchangePresenter initPresenter() {
        return null;
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        View mRoot = mInflater.inflate(R.layout.fragment_buy_exchange_result, null);
        return mRoot;
    }

    @Override
    public void initView() {
        resultView = mViewFinder.find(R.id.cus_result);
        super.initView();
    }

    @Override
    public void initData() {
        Bundle arguments = getArguments();
        model = (BuyExchangeModel) arguments.get("model");
        LinkedHashMap<String, CharSequence> data = new LinkedHashMap<>();
        LinkedHashMap<String, CharSequence> hideData = new LinkedHashMap<>();
        hideData.put("账户", NumberUtils.formatCardNumberStrong(model.getPayerAccount()));
        if ("A".equals(model.getTransStatus())) {//成功
            resultView.addStatus(ResultHead.Status.SUCCESS, "购汇成功");
            resultView.addTitle("购汇 " + MoneyUtils.transMoneyFormat(model.getTransAmount(), model.getCurrency()) + " " +
                    PublicCodeUtils.getCurrency(mContext, model.getCurrency()) + " " +
                    PublicCodeUtils.getCashSpot(mContext, model.getCashRemit()) + "，" +
                    "支出" + MoneyUtils.transMoneyFormat(model.getReturnCnyAmt(), ApplicationConst.CURRENCY_CNY) + "人民币元");
            data.put("基准牌价", BuyExchangeCurrencyUtil.formatExchangeRate(model.getFinalReferenceRate()));
            data.put("优惠后牌价", BuyExchangeCurrencyUtil.formatExchangeRate(model.getFinalExchangeRate()));

            hideData.put("资金用途", model.getMoneyUse());
            hideData.put("本年额度", "已用 等值" +
                    MoneyUtils.transMoneyFormat(model.getFinalAnnAmtUSD(), ApplicationConst.CURRENCY_USD) + "美元\n剩余 等值" +
                    MoneyUtils.transMoneyFormat(model.getFinalAnnRmeAmtUSD(), ApplicationConst.CURRENCY_USD) + "美元");
            hideData.put("交易序号", model.getTransactionId());

            resultView.addNeedItem("继续购汇", GO_ON_BUY_EXC);
            resultView.addNeedItem("余额查询", QRY_BALANCE);
            resultView.addNeedItem("跨境汇款", CROSS_BORDER_TRANS);
        } else if ("B".equals(model.getTransStatus())) {//失败
            resultView.addStatus(ResultHead.Status.FAIL, "购汇失败");
            hideData.put("购汇金额", PublicCodeUtils.getCurrency(mContext, model.getCurrency()) + " "
                    + PublicCodeUtils.getCashSpot(mContext, model.getCashRemit()) + " "
                    + model.getTransAmount());
        } else if ("K".equals(model.getTransStatus())) {//银行处理中
            resultView.addStatus(ResultHead.Status.INPROGRESS, "您的交易银行正在处理中");
            hideData.put("购汇金额", PublicCodeUtils.getCurrency(mContext, model.getCurrency()) + " "
                    + PublicCodeUtils.getCashSpot(mContext, model.getCashRemit()) + " "
                    + model.getTransAmount());
        }
        hideData.put("资金用途", model.getMoneyUse());
        hideData.put("本年额度", "已用 等值" +
                MoneyUtils.transMoneyFormat(model.getFinalAnnAmtUSD(), ApplicationConst.CURRENCY_USD) + "美元\n剩余 等值" +
                MoneyUtils.transMoneyFormat(model.getFinalAnnRmeAmtUSD(), ApplicationConst.CURRENCY_USD) + "美元");
        hideData.put("交易序号", model.getTransactionId());
        if (data.size() != 0) {
            resultView.addTopDetail(data);
        }
        if (hideData.size() != 0) {
            resultView.addDetail(hideData);
        }
    }

    @Override
    public void setListener() {
        resultView.setOnHomeBackClick(homeBackListener);
        resultView.setNeedListener(bottomItemClick);
        super.setListener();
    }

    @Override
    protected void titleLeftIconClick() {
        handBack();
    }

    @Override
    public boolean onBack() {
        handBack();
        return false;
    }

    BaseResultView.HomeBackListener homeBackListener = new BaseResultView.HomeBackListener() {
        @Override
        public void onHomeBack() {
            ToastUtils.show("返回首页");
        }
    };
    /**
     * 底部条目点击
     */
    ResultBottom.OnClickListener bottomItemClick = new ResultBottom.OnClickListener() {

        @Override
        public void onClick(int id) {
            if (id == GO_ON_BUY_EXC) {
                handBack();
            } else if (id == QRY_BALANCE) {
                ToastUtils.show("查询余额");
            } else if (id == CROSS_BORDER_TRANS) {
                ToastUtils.show("跨境汇款");
            }
        }
    };

    private void handBack() {
        popToAndReInit(BuyExchangeFragment.class);
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
}
