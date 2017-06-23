package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.common;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryExchangeRate.PsnFessQueryExchangeRateParams;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.SimpleListViewDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.model.BuyAndSellExcHomeModel;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.model.BuyExchangeModel;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.presenter.BuyExchangeContract;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.presenter.BuySellExchangePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.ui.TransRemitBlankFragment;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 购汇、结汇委托基类
 * Created by gwluo on 2016/11/29.
 */

public abstract class BuySellBaseExchangeFragment extends MvpBussFragment<BuySellExchangePresenter>
        implements View.OnClickListener, BuyExchangeContract.BaseTransView {
    protected View mRoot;
    protected EditChoiceWidget fee_account;
    protected EditMoneyInputWidget cus_foreign_currency_money;
    protected TextView btn_next;
    protected TextView tv_remainder_fess;//剩余额度
    protected TextView tv_used_fess;//使用额度
    protected TextView tv_goto_trans;//去转账
    protected TextView tv_limit_notice;//额度为0提示
    protected TextView fund_describe;//资金说明
    protected TextView tv_aviliable_balance;
    protected LinearLayout ll_rme_amount;//剩余额度为0提示
    protected LinearLayout ll_balance;//余额
    protected LinearLayout ll_fess_usd;//底部额度布局
    protected TextView tv_cost_rmb;
    protected LinearLayout ll_cost;//花费人民币
    protected EditChoiceWidget cus_currency;
    protected EditChoiceWidget cus_cash_spot;
    protected EditChoiceWidget cus_money_use;
    protected BuyExchangeModel model = new BuyExchangeModel();//页面model
    protected BuyAndSellExcHomeModel fessHomeModel;
    protected List<String> cashSpotList;//钞汇集合
    protected AccountBean selectedAcc;//选中的账户

    @Override
    protected BuySellExchangePresenter initPresenter() {
        return null;
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRoot = mInflater.inflate(R.layout.boc_fragment_buy_exchange, null);
        return mRoot;
    }

    @Override
    public void initView() {
        fee_account = mViewFinder.find(R.id.fee_account);
        cus_foreign_currency_money = mViewFinder.find(R.id.cus_foreign_currency_money);
        btn_next = mViewFinder.find(R.id.btn_next);
        tv_aviliable_balance = mViewFinder.find(R.id.tv_aviliable_balance);
        cus_currency = mViewFinder.find(R.id.cus_currency);

        cus_cash_spot = mViewFinder.find(R.id.cus_chaohui);
        cus_money_use = mViewFinder.find(R.id.cus_money_use);
        tv_used_fess = mViewFinder.find(R.id.tv_used_fess);
        tv_remainder_fess = mViewFinder.find(R.id.tv_remainder_fess);
        ll_rme_amount = mViewFinder.find(R.id.ll_rme_amount);
        ll_cost = mViewFinder.find(R.id.ll_cost);
        tv_cost_rmb = mViewFinder.find(R.id.tv_cost_rmb);
        ll_balance = mViewFinder.find(R.id.ll_balance);
        ll_fess_usd = mViewFinder.find(R.id.ll_fess_usd);
        tv_goto_trans = mViewFinder.find(R.id.tv_goto_trans);
        fund_describe = mViewFinder.find(R.id.fund_describe);
        tv_limit_notice = mViewFinder.find(R.id.tv_limit_notice);
        if (isBuyExchange()) {
            cus_cash_spot.setVisibility(View.VISIBLE);
        } else {
            cus_cash_spot.setVisibility(View.GONE);
        }
        cus_currency.setChoiceTextContentHint("请选择");
        cus_cash_spot.setChoiceTextContentHint("请选择");
        cus_money_use.setChoiceTextContentHint("请选择");
        ll_cost.setVisibility(View.GONE);
        tv_goto_trans.setVisibility(View.GONE);
        ll_balance.setVisibility(View.GONE);
        ll_fess_usd.setVisibility(View.GONE);
        ll_rme_amount.setVisibility(View.GONE);
        mTitleBarView.setRightImgBtnVisible(false);
    }

    @Override
    public void initData() {
        cus_foreign_currency_money.showEditMoneyRightImageView(true);
        cus_foreign_currency_money.setEditMoneyRightImage(R.drawable.boc_icon);
        fee_account.setChoiceTextContent("");
        tv_aviliable_balance.setText("");
        cus_currency.setChoiceTextContent("");
        cus_cash_spot.setChoiceTextContent("");
        cus_foreign_currency_money.setContentHint("请输入");
        cus_foreign_currency_money.setmContentMoneyEditText("");
        setInputMoneyType(12, 2);
        cus_money_use.setChoiceTextContent("");
        getData();
    }

    /**
     * 请求数据
     */
    protected void getData() {
    }


    protected void setInputMoneyType(int left, int right) {
        cus_foreign_currency_money.setMaxLeftNumber(left);
        cus_foreign_currency_money.setMaxRightNumber(right);
    }

    /**
     * 设置已用、剩余额度
     *
     * @param annAmtUsd
     * @param annRmeAmtUsd
     */
    @Override
    public void upDateFessLimit(String annAmtUsd, String annRmeAmtUsd) {
        //本年剩余额度是否为0
        if (new BigDecimal(annRmeAmtUsd).compareTo(new BigDecimal("0")) == 0) {
            ll_rme_amount.setVisibility(View.VISIBLE);
            setRmeZeroMaxAmount();
        } else {
            ll_rme_amount.setVisibility(View.GONE);
        }
        ll_fess_usd.setVisibility(View.VISIBLE);
        tv_used_fess.setText(String.format(getString(R.string.boc_used_usd), MoneyUtils.transMoneyFormat(annAmtUsd, ApplicationConst.CURRENCY_USD)));
        tv_remainder_fess.setText(String.format(getString(R.string.boc_balance_usd), MoneyUtils.transMoneyFormat(annRmeAmtUsd, ApplicationConst.CURRENCY_USD)));
    }

    /**
     * 最大金额为0设置
     */
    protected void setRmeZeroMaxAmount() {
        if (isHaveXS(StringUtils.isEmpty(model.getCurrency()) ? lastSelectCurr : model.getCurrency())) {
            cus_foreign_currency_money.setContentHint("最大参考" + "0.00");
        } else {
            cus_foreign_currency_money.setContentHint("最大参考" + "0");
        }
        mMmaxAmount = cus_foreign_currency_money.getContentMoney();
    }

    @Override
    public void setListener() {
        tv_goto_trans.setOnClickListener(this);
        fee_account.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        cus_currency.setOnClickListener(this);
        if (isBuyExchange()) {
            cus_cash_spot.setOnClickListener(this);
        }
        cus_money_use.setOnClickListener(this);
        cus_foreign_currency_money.setRightImageViewOnClick(moneyCalculatorClickLis);
        cus_foreign_currency_money.setMoneyInputTextWatcherListener(moneyInputLis);
    }

    /**
     * 是否购汇，true购汇，false结汇
     *
     * @return
     */
    protected abstract boolean isBuyExchange();

    private EditMoneyInputWidget.MoneyInputTextWatcher moneyInputLis =
            new EditMoneyInputWidget.MoneyInputTextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    caculateRMBCost(s.toString());
                }
            };

    @Override
    protected void titleRightIconClick() {
        onCalculatorClick();
    }

    protected String mMmaxAmount;//最大可用余额

    /**
     * 显示最大参考金额
     */
    @Override
    public abstract void caculateMaxAmount();

    /**
     * 计算预计花费RMB
     */
    protected void caculateRMBCost(String inputMoney) {
        String noFormatMoney = inputMoney.replaceAll(",", "");
        if (StringUtils.isEmpty(noFormatMoney)
                || StringUtils.isEmpty(cus_currency.getChoiceTextContent())
                || StringUtils.isEmpty(cus_cash_spot.getChoiceTextContent())) {
            ll_cost.setVisibility(View.GONE);
        } else {
            ll_cost.setVisibility(View.VISIBLE);
            //计算人民币花费
            BigDecimal money = new BigDecimal(noFormatMoney);
            BigDecimal cashRemitRate = new BigDecimal(model.getCashRemitRate());
            BigDecimal rate = new BigDecimal("100");
            BigDecimal costRmb = money.multiply(cashRemitRate).divide(rate, 2, BigDecimal.ROUND_DOWN);
            tv_cost_rmb.setText(String.format(getString(R.string.boc_available_balance), "人民币", MoneyUtils.transMoneyFormat(costRmb.toPlainString(), ApplicationConst.CURRENCY_CNY)));
            model.setRMBCost(costRmb.toPlainString());
        }
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_buy_exchange);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    public BuyExchangeModel getModel() {
        return model;
    }

    /**
     * 金额输入view右侧按钮点击
     */
    private EditMoneyInputWidget.RightImageViewClickListener moneyCalculatorClickLis =
            new EditMoneyInputWidget.RightImageViewClickListener() {
                @Override
                public void onRightImageClick(View v) {
                    onCalculatorClick();
                }
            };

    /**
     * 计算器点击
     */
    private void onCalculatorClick() {
        BuySellCalculatorFragment fragment = new BuySellCalculatorFragment();
        start(fragment);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fee_account) {//账户
            onAccountClick();
        } else if (id == R.id.btn_next) {//下一步
            onNextClick();
        } else if (id == R.id.cus_currency) {//币种
            onCurrencyClick();
        } else if (id == R.id.cus_chaohui) {//钞汇
            if (StringUtils.isEmpty(cus_currency.getChoiceTextContent())) {
                showErrorDialog("请选择币种");
            } else {
                cashSpotList = BuyExchangeCurrencyUtil.
                        getInstance(mContext).getCashSpot(cus_currency.getChoiceTextContent());
                requreInterfaceNum = cashSpotList.size();
                if (cashSpotList.size() > 0) {
                    cashSpotRateList.clear();
                    PsnFessQueryExchangeRateParams params = getExchangeRateParams(PublicCodeUtils.getCashSpot(mContext, cashSpotList.get(0)));
                    getPresenter().getPsnFessQueryExchangeRate(params);
                }
            }
        } else if (id == R.id.cus_money_use) {//资金用途
            List<String> cashUseList = getBankrollUse();
            showChoiceDialog(isBuyExchange() ? "选择资金用途" : "选择资金来源", cashUseList, cus_money_use, 2);
        } else if (id == R.id.tv_goto_trans) {//去转账
            start(new TransRemitBlankFragment());
        }
    }

    protected abstract void onCurrencyClick();

    /**
     * 获取资金用途
     *
     * @return
     */
    protected abstract List<String> getBankrollUse();

    @Override
    public void reInit() {
        initView();
        initData();
        setListener();
    }

    protected PsnFessQueryExchangeRateParams getExchangeRateParams(String cashSpot) {
        return null;
    }

    private List<String> cashSpotRateList = new ArrayList<>();//钞汇对应牌价

    @Override
    public void onExchangeRateSucc(String rate) {
        requreInterfaceNum--;
        cashSpotRateList.add(MoneyUtils.trimAmountZero(rate));
        if (requreInterfaceNum > 0) {
            PsnFessQueryExchangeRateParams params = getExchangeRateParams(PublicCodeUtils.getCashSpot(mContext, cashSpotList.get(1)));
            getPresenter().getPsnFessQueryExchangeRate(params);
        } else {
            closeProgressDialog();
            List<String> cashSpotAndRateList = new ArrayList<>();
            for (int i = 0; i < cashSpotList.size(); i++) {
                String cashSpotAndRate = cashSpotList.get(i) + "（参考牌价" + cashSpotRateList.get(i) + "）";
                cashSpotAndRateList.add(cashSpotAndRate);
            }
            showChoiceDialog("选择钞汇", cashSpotAndRateList, cus_cash_spot, 1);
        }
    }

    @Override
    public void onAccListSucc() {
    }

    /**
     * 集合是否包含传入Id账户
     *
     * @param accId
     * @return
     */
    protected boolean isContainAcc(String accId) {
        AccountBean accFromList = BuyExchangeCurrencyUtil.getAccFromList(model, accId);
        if (accFromList == null) {
            return false;
        } else {
            return true;
        }
    }

    protected boolean checkDate() {
        if (StringUtils.isEmpty(cus_currency.getChoiceTextContent())) {
            showErrorDialog("请选择币种");
            return false;
        }
        if (isBuyExchange()) {
            if (StringUtils.isEmpty(cus_cash_spot.getChoiceTextContent())) {
                showErrorDialog("请选择钞汇");
                return false;
            }
        }
        if (StringUtils.isEmpty(cus_foreign_currency_money.getContentMoney())) {
            showErrorDialog("请输入外币金额");
            return false;
        }
        if (StringUtils.isEmpty(cus_money_use.getChoiceTextContent())) {
            if (isBuyExchange()) {
                showErrorDialog("请选择资金用途");
            } else {
                showErrorDialog("请选择资金来源");
            }
            return false;
        }
        if (new BigDecimal("0").compareTo(cus_foreign_currency_money.getMoney()) == 0) {
            showErrorDialog("外币金额不能为0");
            return false;
        }
        return true;
    }

    private void onNextClick() {
        if (!checkDate()) {
            return;
        }

        model.setMoneyUse(cus_money_use.getChoiceTextContent());
        model.setTransAmount(cus_foreign_currency_money.getContentMoney());

        //预关注对象
        if ("02".equals(model.getTypeStatus())) {
            showNoticeDialog("个人外汇业务风险提示函", model.getCustName() + "\n" +
                    "\n" +
                    "       根据《国家外汇管理局关于进一步完善个人结售汇业务管理的通知》（汇发[2009]56号）等相关规定，您办理的个人外汇业务，涉嫌出借本人额度协助他人规避额度及真实性管理，若再次出现上述行为，将被外汇管理局列入“关注名单”管理。\n" +
                    "       特此提示");

        } else if ("03".equals(model.getTypeStatus()) && "0".equals(model.getSignStatus())) {
            //关注名单用户且未告知的情况需要调用此接口
            showNoticeDialog("个人外汇业务“关注名单”告知书", model.getCustName() + "\n" +
                    "\n" +
                    "\n" +
                    "        根据《国家外汇管理局关于进一步完善个人结售汇业务管理的通知》（汇发[2009]56号）等相关规定，您办理的个人外汇业务，涉嫌再次出借本人额度协助他人或以借用他人额度等方式规避额度及真实性管理，已被外汇局列入“关注名单”管理，关注期限自2014年11月11日至2015年12月12日。在关注期限内，您可凭本人有效身份证件和有交易额的相关证明材料到银行办理个人结售汇业务。如您对被列入“关注名单”有异议，可联系当地外汇管理局。\n" +
                    "          特此告知");
        } else {
            getPresenter().preSubmit(true, getFessFlag());
        }
    }

    /**
     * 钞汇类型
     *
     * @return
     */
    protected abstract String getFessFlag();

    private void showNoticeDialog(String title, String content) {
        TitleAndBtnDialog dialog = new TitleAndBtnDialog(mContext)
                .isShowTitle(true)
                .isShowBottomBtn(true)
                .setTitle(title)
                .setNoticeContent(content);
        dialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
            @Override
            public void onLeftBtnClick(View view) {

            }

            @Override
            public void onRightBtnClick(View view) {
                getPresenter().getPsnFessSignConfirmation4FocusParams(getFessFlag());
            }
        });
    }

    protected String lastSelectCurr = ApplicationConst.CURRENCY_CNY;//上次选择的币种

    protected void showChoiceDialog(String title, List<String> performList, final EditChoiceWidget performView, final int flag) {
        final SimpleListViewDialog dialog = new SimpleListViewDialog(mContext);
        dialog.isShowBottomBtn(false).isShowTitle(true).setTitle(title);
        dialog.setData(performList);
        dialog.setOnSelectListener(new SimpleListViewDialog.OnSelectListener<String>() {
            @Override
            public void onItemClick(int position, String str) {
                if (flag == 0) {//币种
                    onCurrencySelected(str, performView);
                } else if (flag == 1) {//钞汇
                    if (onCashSpotSelected(str, performView, dialog)) return;
                } else if (flag == 2) {//资金用途
                    performView.setChoiceTextContent(str);
                    model.setMoneyUse(cus_money_use.getChoiceTextContent());
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 牌价选择
     *
     * @param str
     * @param performView
     * @param dialog
     * @return
     */
    protected boolean onCashSpotSelected(String str, EditChoiceWidget performView, SimpleListViewDialog dialog) {
        return false;
    }

    /**
     * 币种选择
     *
     * @param str
     * @param performView
     */
    protected void onCurrencySelected(String str, EditChoiceWidget performView) {

    }

    protected SelectAccoutFragment selectAccFragment;
    protected Bundle selectAccDate;

    /**
     * 账户点击监听
     */
    protected void onAccountClick() {
    }


    @Override
    public void setPresenter(BuyExchangeContract.Presenter presenter) {

    }

    /**
     * 金额格式是否有小数位
     * 日元、韩元无小数
     *
     * @param currencyCode
     * @return
     */
    protected boolean isHaveXS(String currencyCode) {
        if ("027".equals(currencyCode) || "088".equals(currencyCode)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onPreSubmit() {
        BussFragment fragment = new BuySellExchangeConfirmInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("model", model);
        bundle.putBoolean("isBuyExchange", isBuyExchange());
        fragment.setArguments(bundle);
        start(fragment);
    }

    private int requreInterfaceNum = 0;//请求牌价接口次数，同时有现钞现汇的请求两次


}
