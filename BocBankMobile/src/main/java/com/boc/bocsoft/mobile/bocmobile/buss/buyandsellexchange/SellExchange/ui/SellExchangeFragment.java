package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.SellExchange.ui;

import android.os.Bundle;
import android.view.View;

import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessGetUpperLimitOfForeignCurr.PsnFessGetUpperLimitOfForeignCurrParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryAccountBalance.PsnFessQueryAccountBalanceResult;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.model.BuyExchangeModel;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.presenter.BuyExchangeContract;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.presenter.BuySellExchangePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.common.BuySellBaseExchangeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.SellExchange.model.SellAccountBalanceModel;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.SellExchange.presenter.SellExchangePresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.utils.ResUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * 结汇委托页面
 * Created by gwluo on 2016/12/20.
 */

public class SellExchangeFragment extends BuySellBaseExchangeFragment implements BuyExchangeContract.SellTransView {
    private int position;
    private ArrayList<SellAccountBalanceModel> accAndBalance;
    private final int SELL_EXCHANGE_CODE = 1;

    @Override
    public void reInit() {
        initView();
        initData();
        setListener();
    }

    @Override
    public void initView() {
        super.initView();
        fee_account.setArrowImageGone(false);
        cus_money_use.setChoiceTextName("资金来源");
        fund_describe.setText("资金来源说明");
        tv_limit_notice.setText("您的年度剩余额度为0，无法结汇。");
    }

    @Override
    public void initData() {
        super.initData();
        cus_foreign_currency_money.showEditMoneyRightImageView(false);
        cus_foreign_currency_money.setRightTextViewText("全部结汇");
        cus_foreign_currency_money.setRightTextColor(R.color.boc_text_color_red);
        Bundle arguments = getArguments();
        if (null != arguments) {
            model = (BuyExchangeModel) arguments.getSerializable("globalModel");
            accAndBalance = (ArrayList) arguments.getSerializable("accAndBalance");
            position = arguments.getInt("position");
            SellAccountBalanceModel selectItem = accAndBalance.get(position);
            setPayerData(selectItem);
            onCurrencyItemSelected();
        }
    }

    @Override
    public void setListener() {
        super.setListener();
        BuyExchangeModel buyExchangeModel = filteBalanceMap(model);
        //单币种不可点击
        if (buyExchangeModel.getBalanceMap().size() == 1) {
            cus_currency.setArrowImageGone(false);
            cus_currency.setOnClickListener(null);
        }
        cus_foreign_currency_money.setRightTextViewOnClick(new EditMoneyInputWidget.RightTextClickListener() {
            @Override
            public void onRightClick(View v) {
                cus_foreign_currency_money.setmContentMoneyEditText(mMmaxAmount);
            }
        });
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (resultCode == CurrencyAccFragment.RESULT_CODE_CURRENCY) {
            setPayerData((SellAccountBalanceModel) data.getSerializable("item"));
            onCurrencyItemSelected();
        }
    }

    @Override
    protected boolean isBuyExchange() {
        return false;
    }

    protected void upDateAccBalance(String availableBalance, String cashSpot, String currency) {
        ll_balance.setVisibility(View.VISIBLE);
        tv_aviliable_balance.setText(String.format(getString(R.string.boc_available_balance_cash_spot),
                PublicCodeUtils.getCurrency(mContext, currency), PublicCodeUtils.getCashSpot(mContext, cashSpot),
                MoneyUtils.transMoneyFormat(availableBalance, currency)));
    }

    /**
     * 显示最大参考金额
     */
    @Override
    public void caculateMaxAmount() {
        upDateFessLimit(model.getAnnAmtUSD(), model.getAnnRmeAmtUSD());
        //如果年度余额为0最大参考显示0
        if (new BigDecimal(model.getAnnRmeAmtUSD()).compareTo(new BigDecimal("0")) == 0) {
            setRmeZeroMaxAmount();
            return;
        }
        if (!StringUtils.isEmpty(cus_currency.getChoiceTextContent())) {
            BigDecimal availableBalance = new BigDecimal(model.getAvailableBalance());//可用余额
            BigDecimal annRmeAmtCUR = new BigDecimal(model.getAnnRmeAmtCUR());//剩余额度
            BigDecimal minAmount = annRmeAmtCUR.compareTo(availableBalance) == 1 ? availableBalance : annRmeAmtCUR;
            mMmaxAmount = MoneyUtils.transMoneyFormat(minAmount.toPlainString(), model.getCurrency());
            cus_foreign_currency_money.setContentHint("最大参考" + mMmaxAmount);
        } else {
            cus_foreign_currency_money.setContentHint("请输入");
        }
    }

    @Override
    protected void caculateRMBCost(String inputMoney) {
        String noFormatMoney = inputMoney.replaceAll(",", "");
        if (StringUtils.isEmpty(noFormatMoney)
                || StringUtils.isEmpty(cus_currency.getChoiceTextContent())) {
            ll_cost.setVisibility(View.GONE);
        } else {
            ll_cost.setVisibility(View.VISIBLE);
            //计算人民币花费
            BigDecimal money = new BigDecimal(noFormatMoney);
            BigDecimal cashRemitRate = new BigDecimal(model.getCashRemitRate());
            BigDecimal rate = new BigDecimal("100");
            BigDecimal costRmb = money.multiply(cashRemitRate).divide(rate, 2, BigDecimal.ROUND_HALF_UP);
            tv_cost_rmb.setText(String.format(getString(R.string.boc_available_balance), "人民币", MoneyUtils.transMoneyFormat(costRmb.toPlainString(), ApplicationConst.CURRENCY_CNY)));
            model.setRMBCost(costRmb.toPlainString());
        }
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_sell_exchange);
    }

    @Override
    protected void onCurrencyClick() {
        CurrencyAccFragment sellExchangeFragment = new CurrencyAccFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("globalModel", filteBalanceMap(model));
        bundle.putBoolean("isSetResult", true);
        sellExchangeFragment.setArguments(bundle);
        startForResult(sellExchangeFragment, SELL_EXCHANGE_CODE);
    }

    /**
     * 筛选出选中账户对应的余额
     *
     * @param map
     * @return
     */
    private BuyExchangeModel filteBalanceMap(BuyExchangeModel map) {
        BuyExchangeModel model = null;
        try {
            model = map.clone();
        } catch (Exception e) {

        }
        LinkedHashMap<String, List<PsnFessQueryAccountBalanceResult>> balanceMap = model.getBalanceMap();
        Set<String> keys = balanceMap.keySet();
        Iterator<String> iterator = keys.iterator();
        String accountId = model.getAccountId();
        List<String> removeId = new ArrayList<>();
        while (iterator.hasNext()) {
            String accId = iterator.next();
            if (!accountId.equals(accId)) {
                removeId.add(accId);
            }
        }
        for (String accId : removeId) {
            balanceMap.remove(accId);
        }
        return model;
    }

    @Override
    protected List<String> getBankrollUse() {
        String identityType = model.getIdentityType();
        //区分境内和境外
        boolean isOverseas = ("01".equals(identityType) || "02".equals(identityType)) == true ? false : true;
        if (isOverseas) {
            return ResUtils.getOrderedPropertyList(mContext, "code/fess_overseas_sell_use", 0);
        } else {
            return ResUtils.getOrderedPropertyList(mContext, "code/fess_territory_sell_use", 0);
        }
    }

    private void setPayerData(SellAccountBalanceModel selectModel) {
        model.setAccountId(selectModel.getAccountId());
        model.setCurrency(selectModel.getCurrency());
        model.setCashRemit(selectModel.getCashRemit());
        model.setPayerAccount(selectModel.getAccountNum());
        model.setAvailableBalance(MoneyUtils.trimAmountZero(selectModel.getAvailableBalance()));
    }

    @Override
    public void onAccListSucc() {

    }

    @Override
    protected String getFessFlag() {
        return "01";
    }

//    @Override
//    protected void onCurrencySelected(String str, EditChoiceWidget performView) {
//        performView.setChoiceTextContent(str);
////        cus_cash_spot.setChoiceTextContent("");
//        model.setCurrency(PublicCodeUtils.getCurrency(mContext, cus_currency.getChoiceTextContent()));
//        //设置输入的整数和小数位，日元、韩元无小数
//        if (isHaveXS(model.getCurrency())) {
//            if (!isHaveXS(lastSelectCurr)) {
//                cus_foreign_currency_money.setmContentMoneyEditText("");
//            }
//            setInputMoneyType(12, 2);
//        } else {
//            if (isHaveXS(lastSelectCurr)) {
//                cus_foreign_currency_money.setmContentMoneyEditText("");
//            }
//            setInputMoneyType(15, 0);
//        }
//        lastSelectCurr = model.getCurrency();
//        cus_foreign_currency_money.setCurrency(model.getCurrency());
////        caculateRMBCost(cus_foreign_currency_money.getContentMoney());
////        caculateMaxAmount();
//    }

    /**
     * 币种选中
     */
    private void onCurrencyItemSelected() {
        fee_account.setChoiceTextContent(NumberUtils.formatCardNumberStrong(model.getPayerAccount()));
        upDateAccBalance(model.getAvailableBalance(), model.getCashRemit(), model.getCurrency());
        cus_currency.setChoiceTextContent(
                PublicCodeUtils.getCurrency(mContext, model.getCurrency()) + " " +
                        PublicCodeUtils.getCashSpot(mContext, model.getCashRemit()));
        //设置输入的整数和小数位，日元、韩元无小数
        if (isHaveXS(model.getCurrency())) {
            if (!isHaveXS(lastSelectCurr)) {
                cus_foreign_currency_money.setmContentMoneyEditText("");
            }
            setInputMoneyType(12, 2);
        } else {
            if (isHaveXS(lastSelectCurr)) {
                cus_foreign_currency_money.setmContentMoneyEditText("");
            }
            setInputMoneyType(15, 0);
        }
        cus_foreign_currency_money.setCurrency(model.getCurrency());
        lastSelectCurr = model.getCurrency();

        cus_foreign_currency_money.setContentHint("请输入");
        ll_fess_usd.setVisibility(View.GONE);//重新请求结售汇额度
        ((SellExchangePresenter) getPresenter()).getPsnFessQueryLimit(model.getAccountId(), "01");
    }

    @Override
    protected BuySellExchangePresenter initPresenter() {
        return new SellExchangePresenter(this);
    }

    @Override
    protected boolean checkDate() {
        if (cus_foreign_currency_money.getMoney().compareTo(new BigDecimal(model.getAvailableBalance())) == 1) {
            showErrorDialog("外币金额不能大于可用余额");
            return false;
        }
        return super.checkDate();
    }

    @Override
    public void onQueryLimit() {
        caculateRMBCost(cus_foreign_currency_money.getContentMoney());
        //美元不调用金额上限接口
        if (ApplicationConst.CURRENCY_USD.equals(model.getCurrency())) {
            closeProgressDialog();
            BigDecimal rmbBalance;
            try {
                rmbBalance = new BigDecimal(model.getAvailableBalance());
            } catch (Exception e) {
                showErrorDialog("可用余额值不正确");
                return;
            }
            BigDecimal cashRemitRate;
            try {
                cashRemitRate = new BigDecimal(model.getCashRemitRate());//钞汇牌价
            } catch (Exception e) {
                showErrorDialog("牌价值不正确");
                return;
            }
            BigDecimal rate = new BigDecimal("100");
            BigDecimal usdBalance = null;//人民币转为美元余额
            if (isHaveXS(model.getCurrency())) {
                usdBalance = rmbBalance.multiply(rate).divide(cashRemitRate, 2, BigDecimal.ROUND_DOWN);
            } else {
                usdBalance = rmbBalance.multiply(rate).divide(cashRemitRate, 0, BigDecimal.ROUND_DOWN);
            }
            model.setAvailableBalanceCUR(usdBalance.toPlainString());
            model.setAnnRmeAmtCUR(model.getAnnRmeAmtUSD());
            caculateMaxAmount();
        } else {
            //年度余额为0不请求上限
            if (new BigDecimal(model.getAnnRmeAmtUSD()).compareTo(new BigDecimal("0")) == 0) {
                setRmeZeroMaxAmount();
            } else {
                //选择钞汇时对上限进行计算，为了显示最大参考值
                PsnFessGetUpperLimitOfForeignCurrParams params = new PsnFessGetUpperLimitOfForeignCurrParams();
                params.setCurrencyCode(model.getCurrency());
                params.setCashRemit(model.getCashRemit());
//                params.setAvailableBalanceRMB(model.getAvailableBalance());
                params.setAnnRmeAmtUSD(model.getAnnRmeAmtUSD());
                params.setFessFlag("01");
                getPresenter().getPsnFessGetUpperLimitOfForeignCurr(params);
            }
        }
    }
}
