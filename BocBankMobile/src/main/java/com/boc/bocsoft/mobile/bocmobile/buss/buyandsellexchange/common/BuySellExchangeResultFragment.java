package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryAccountBalance.PsnFessQueryAccountBalanceResult;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BaseMobileActivity;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.ResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.ResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.home.BuyAndSellExchangeHomeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.model.BuyExchangeModel;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.presenter.BuyExchangeContract;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.ui.BuyExchangeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.SellExchange.presenter.SellExchangeCurrencyPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.SellExchange.ui.CurrencyAccFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.SellExchange.ui.SellExchangeFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 购汇、结汇结果页面
 * Created by gwluo on 2016/12/8.
 */

public class BuySellExchangeResultFragment extends MvpBussFragment<SellExchangeCurrencyPresenter> implements BuyExchangeContract.ResultView {
    private BaseResultView resultView;
    private final int GO_ON_BUY_EXC = 1;
    private final int QRY_BALANCE = 2;
    private final int CROSS_BORDER_TRANS = 3;
    private BuyExchangeModel model;//页面model
    private boolean isBuyExchange;
    private String moduleName;

    @Override

    protected SellExchangeCurrencyPresenter initPresenter() {
        return new SellExchangeCurrencyPresenter(this);
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
        isBuyExchange = arguments.getBoolean("isBuyExchange");
        moduleName = isBuyExchange ? "购汇" : "结汇";
        LinkedHashMap<String, CharSequence> data = new LinkedHashMap<>();
        LinkedHashMap<String, CharSequence> hideData = new LinkedHashMap<>();
        hideData.put("账户", NumberUtils.formatCardNumberStrong(model.getPayerAccount()));
        if ("A".equals(model.getTransStatus())) {//成功
            resultView.addStatus(ResultHead.Status.SUCCESS, moduleName + "成功");
            resultView.addTitle(moduleName + " " + MoneyUtils.transMoneyFormat(model.getTransAmount(), model.getCurrency()) + " " +
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
            if (isBuyExchange) {
                resultView.addNeedItem("继续购汇", GO_ON_BUY_EXC);
            } else {
                resultView.addNeedItem("继续结汇", GO_ON_BUY_EXC);
            }
            resultView.addNeedItem("余额查询", QRY_BALANCE);
            if (isBuyExchange) {
                resultView.addNeedItem("跨境汇款", CROSS_BORDER_TRANS);
            }
        } else if ("B".equals(model.getTransStatus())) {//失败
            resultView.addStatus(ResultHead.Status.FAIL, moduleName + "失败");
            hideData.put(moduleName + "金额", PublicCodeUtils.getCurrency(mContext, model.getCurrency()) + " "
                    + PublicCodeUtils.getCashSpot(mContext, model.getCashRemit()) + " "
                    + model.getTransAmount());
        } else if ("K".equals(model.getTransStatus())) {//银行处理中
            resultView.addStatus(ResultHead.Status.INPROGRESS, "您的交易银行正在处理中");
            hideData.put(moduleName + "金额", PublicCodeUtils.getCurrency(mContext, model.getCurrency()) + " "
                    + PublicCodeUtils.getCashSpot(mContext, model.getCashRemit()) + " "
                    + model.getTransAmount());
        }
        resultView.updateDetail("查看详情");
        if (isBuyExchange) {
            hideData.put("资金用途", model.getMoneyUse());
        } else {
            hideData.put("资金来源", model.getMoneyUse());
        }
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
            popToAndReInit(BuyAndSellExchangeHomeFragment.class);
        }
    };
    /**
     * 底部条目点击
     */
    ResultBottom.OnClickListener bottomItemClick = new ResultBottom.OnClickListener() {

        @Override
        public void onClick(int id) {
            if (id == GO_ON_BUY_EXC) {
                if (isBuyExchange) {
                    handBack();
                } else {
                    getPresenter().getAccountList();
                }
            } else if (id == QRY_BALANCE) {
                ToastUtils.show("查询余额");
            } else if (id == CROSS_BORDER_TRANS) {
                ToastUtils.show("跨境汇款");
            }
        }
    };

    private void handBack() {
        if (isBuyExchange) {
            popToAndReInit(BuyExchangeFragment.class);
        } else {
            popToAndReInit(BuyAndSellExchangeHomeFragment.class);
        }
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
    public void updateAccBalance(boolean isSingle) {
        accIndex++;
        if (accIndex + 1 <= model.getAccList().size()) {
            getPresenter().getPsnFessQueryAccountBalance(model.getAccList().get(accIndex).getAccountId(), false);
        } else {
            closeProgressDialog();
            handJump();
        }
    }

    private void handJump() {
        LinkedHashMap<String, List<PsnFessQueryAccountBalanceResult>> balanceMap = model.getBalanceMap();
        if (balanceMap.size() == 0) {
            showErrorDialog("无可结汇币种");
        } else if (balanceMap.size() == 1) {//判断单币种和多币种
            Iterator<String> iterator = balanceMap.keySet().iterator();
            if (iterator.hasNext()) {
                String key = iterator.next();
                if (balanceMap.get(key).size() == 1) {
                    popToAndReInit(SellExchangeFragment.class);
                } else {
                    popToAndReInit(CurrencyAccFragment.class);
                }
            }
        } else {
            popToAndReInit(CurrencyAccFragment.class);
        }
    }

    private int accIndex = 0;//账户索引

    @Override
    public void onAccListSucc() {
        if (model.getAccList().size() > 0) {
            AccountBean accountBean;
            accIndex = 0;
            accountBean = model.getAccList().get(accIndex);
            model.getBalanceMap().clear();
            getPresenter().getPsnFessQueryAccountBalance(accountBean.getAccountId(), false);
        } else {
            closeProgressDialog();
            showErrorDialog("无可用交易账户，本功能支持活期一本通账户或借记卡（主账户为活期一本通）");
            ((BaseMobileActivity) getActivity()).setErrorDialogClickListener(new BaseMobileActivity.ErrorDialogClickCallBack() {
                @Override
                public void onEnterBtnClick() {
                    pop();
                }
            });
        }
    }

    @Override
    public BuyExchangeModel getModel() {
        return model;
    }

    @Override
    public void setPresenter(BuyExchangeContract.Presenter presenter) {

    }
}
