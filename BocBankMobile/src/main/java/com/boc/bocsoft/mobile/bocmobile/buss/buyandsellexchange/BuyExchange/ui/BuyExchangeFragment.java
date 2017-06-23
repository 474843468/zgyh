package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.ui;

import android.os.Bundle;
import android.view.View;

import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessGetUpperLimitOfForeignCurr.PsnFessGetUpperLimitOfForeignCurrParams;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryExchangeRate.PsnFessQueryExchangeRateParams;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BaseMobileActivity;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.SimpleListViewDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.model.BuyAndSellExcHomeModel;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.presenter.BuyExchangeContract;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.presenter.BuyExchangePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.presenter.BuySellExchangePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.common.BuyExchangeCurrencyUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.common.BuySellBaseExchangeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.login.ui.LoginMainFragment;
import com.boc.bocsoft.mobile.bocmobile.yun.BocCloudCenter;
import com.boc.bocsoft.mobile.bocmobile.yun.other.AccountType;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.utils.ResUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 购汇委托页面
 * Created by gwluo on 2016/11/29.
 */

public class BuyExchangeFragment extends BuySellBaseExchangeFragment implements BuyExchangeContract.BuyTransView {


    @Override
    public void initData() {
        if (!ApplicationContext.getInstance().isLogin()) {
            start(new LoginMainFragment());
            return;
        }
        super.initData();
    }

    @Override
    public void onAccListSucc() {
        String identityType = model.getIdentityType();
        if (BuyExchangeCurrencyUtil.isSupportBuyExchange(identityType)) {
            if (model.getAccList().size() > 0) {
                //此处从存储的位置获取上次转款存储的账户id，如果该id为空，转账账户显示集合第一个，否则显示账户id对应的账户
                String accountId = BocCloudCenter.getInstance().getAccountId(AccountType.ACC_TYPE_FESS_BUY_EXCHANGE);
                AccountBean accountBean = null;
                if (!StringUtils.isEmpty(accountId) && isContainAcc(accountId)) {
                    accountBean = BuyExchangeCurrencyUtil.getAccFromList(model, accountId);
                } else {
                    accountBean = model.getAccList().get(0);
                }
                selectedAcc = accountBean;
                model.setAccountId(accountBean.getAccountId());
                model.setPayerAccount(accountBean.getAccountNumber());
                fee_account.setChoiceTextContent(NumberUtils.formatCardNumberStrong(accountBean.getAccountNumber()));
                ((BuyExchangePresenter) getPresenter()).getPsnFessQueryAccountBalanceLimit(accountBean.getAccountId(), "11");
            } else {
                showErrorDialog("无可用交易账户，本功能支持活期一本通账户或借记卡（主账户为活期一本通）");
                ((BaseMobileActivity) getActivity()).setErrorDialogClickListener(new BaseMobileActivity.ErrorDialogClickCallBack() {
                    @Override
                    public void onEnterBtnClick() {
                        pop();
                    }
                });
            }
        } else {
            if (BuyExchangeCurrencyUtil.isSupportSellExchange(identityType)) {
                showErrorDialog(getString(R.string.boc_not_support_buysell));
            } else {
                showErrorDialog(getString(R.string.boc_not_support_buy));
            }
            ((BaseMobileActivity) getActivity()).setErrorDialogClickListener(new BaseMobileActivity.ErrorDialogClickCallBack() {
                @Override
                public void onEnterBtnClick() {
                    pop();
                }
            });
        }
    }

    @Override
    protected void getData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            fessHomeModel = (BuyAndSellExcHomeModel) arguments.getSerializable("BuyAndSellExcHomeModel");
        }
        getPresenter().getAccountList();
    }

    @Override
    protected String getFessFlag() {
        return "11";
    }

    protected void onAccountClick() {
        if (selectAccFragment == null) {
            selectAccFragment = new SelectAccoutFragment();
            selectAccFragment.isRequestNet(true);
            selectAccFragment.setOnItemListener(new SelectAccoutFragment.ItemListener() {
                @Override
                public void onItemClick(Bundle bundle) {
                    selectAccDate = bundle;
                    selectedAcc = bundle.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
                    ((BuyExchangePresenter) getPresenter()).getPsnFessQueryAccountBalanceLimit(selectedAcc.getAccountId(), "11");
                }
            });
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(SelectAccoutFragment.ACCOUNT_LIST, (ArrayList<AccountBean>) model.getAccList());
            selectAccFragment.setArguments(bundle);
        }
        startForResult(selectAccFragment, SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT);
    }

    protected List<String> getBankrollUse() {
        return ResUtils.getOrderedPropertyList(mContext, "code/fess_bankroll_use", 0);
    }

    @Override
    protected BuySellExchangePresenter initPresenter() {
        return new BuyExchangePresenter(this);
    }

    @Override
    protected boolean isBuyExchange() {
        return true;
    }

    @Override
    protected void onCurrencyClick() {
        List<String> currencyList = BuyExchangeCurrencyUtil.
                getInstance(mContext).getCurrencyList();
        showChoiceDialog("选择币种", currencyList, cus_currency, 0);
    }

    /**
     * 币种选择
     *
     * @param str
     * @param performView
     */
    protected void onCurrencySelected(String str, EditChoiceWidget performView) {
        performView.setChoiceTextContent(str);
        cus_cash_spot.setChoiceTextContent("");
        model.setCurrency(PublicCodeUtils.getCurrency(mContext, cus_currency.getChoiceTextContent()));
        //设置输入的整数和小数位，日元、韩元无小数
        if (isHaveXS(model.getCurrency())) {
            if (!isHaveXS(lastSelectCurr)) {
                cus_foreign_currency_money.setmContentMoneyEditText("");
            }
//                        if (!cus_foreign_currency_money.getContentMoney().contains(".")
//                                && cus_foreign_currency_money.getContentMoney().length() > 12) {
//                            cus_foreign_currency_money.setmContentMoneyEditText("");
//                        }
            setInputMoneyType(12, 2);
        } else {
            if (isHaveXS(lastSelectCurr)) {
                cus_foreign_currency_money.setmContentMoneyEditText("");
            }
//                        if (cus_foreign_currency_money.getContentMoney().contains(".")) {
//                            cus_foreign_currency_money.setmContentMoneyEditText("");
//                        }
            setInputMoneyType(15, 0);
        }
        lastSelectCurr = model.getCurrency();
        cus_foreign_currency_money.setCurrency(model.getCurrency());
        caculateRMBCost(cus_foreign_currency_money.getContentMoney());
        caculateMaxAmount();
    }

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
            BigDecimal costRmb = money.multiply(cashRemitRate).divide(rate, 2, BigDecimal.ROUND_HALF_UP);
            tv_cost_rmb.setText(String.format(getString(R.string.boc_available_balance), "人民币", MoneyUtils.transMoneyFormat(costRmb.toPlainString(), ApplicationConst.CURRENCY_CNY)));
            model.setRMBCost(costRmb.toPlainString());
        }
    }

    /**
     * 牌价选择
     *
     * @param str
     * @param performView
     * @param dialog
     * @return
     */
    @Override
    protected boolean onCashSpotSelected(String str, EditChoiceWidget performView, SimpleListViewDialog dialog) {
        //对显示的结构汇加牌价进行截取后保存
        performView.setChoiceTextContent(str.substring(0, 2));
        model.setCashRemit(PublicCodeUtils.getCashSpot(mContext, str.substring(0, 2)));
        model.setCashRemitRate(str.substring(7, str.length() - 1));
        caculateRMBCost(cus_foreign_currency_money.getContentMoney());
        //美元不调用金额上限接口
        if (ApplicationConst.CURRENCY_USD.equals(model.getCurrency())) {
            BigDecimal rmbBalance;
            try {
                rmbBalance = new BigDecimal(model.getAvailableBalance());
            } catch (Exception e) {
                showErrorDialog("可用余额值不正确");
                dialog.dismiss();
                return true;
            }
            BigDecimal cashRemitRate;
            try {
                cashRemitRate = new BigDecimal(model.getCashRemitRate());//钞汇牌价
            } catch (Exception e) {
                showErrorDialog("牌价值不正确");
                dialog.dismiss();
                return true;
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
                params.setAvailableBalanceRMB(model.getAvailableBalance());
                params.setAnnRmeAmtUSD(model.getAnnRmeAmtUSD());
                params.setFessFlag("11");
                getPresenter().getPsnFessGetUpperLimitOfForeignCurr(params);
            }
        }
        return false;
    }

    protected PsnFessQueryExchangeRateParams getExchangeRateParams(String cashSpot) {
        PsnFessQueryExchangeRateParams params = new PsnFessQueryExchangeRateParams();
        params.setCashRemit(cashSpot);
        params.setCurrency(model.getCurrency());
        params.setAccountId(model.getAccountId());
        params.setFessFlag("B");
        return params;
    }

    /**
     * 显示最大参考金额
     */
    @Override
    public void caculateMaxAmount() {
        //如果年度余额为0最大参考显示0
        if (new BigDecimal(model.getAnnRmeAmtUSD()).compareTo(new BigDecimal("0")) == 0) {
            setRmeZeroMaxAmount();
            return;
        }
        if (!StringUtils.isEmpty(cus_currency.getChoiceTextContent())
                && !StringUtils.isEmpty(cus_cash_spot.getChoiceTextContent())) {
            //本年额度内剩余可/售结汇外币金额和人民币可购外币最大金额取小值，计算最大参考金额
            BigDecimal annRmeAmtCUR = new BigDecimal(model.getAnnRmeAmtCUR());//本年额度内剩余可/售结汇外币金额
            BigDecimal availableBalanceCUR = new BigDecimal(model.getAvailableBalanceCUR());//人民币可购外币最大金额
            BigDecimal minAmount = annRmeAmtCUR.compareTo(availableBalanceCUR) == 1 ? availableBalanceCUR : annRmeAmtCUR;
            mMmaxAmount = MoneyUtils.transMoneyFormat(minAmount.toPlainString(), model.getCurrency());
            cus_foreign_currency_money.setContentHint("最大参考" + mMmaxAmount);
        } else {
            cus_foreign_currency_money.setContentHint("请输入");
        }
    }

    /**
     * 付款人选中时设置数据
     *
     * @param accountBean
     */
    private void setSelectAccData(AccountBean accountBean) {
        fee_account.setChoiceTextContent(NumberUtils.formatCardNumberStrong(accountBean.getAccountNumber()));
        model.setPayerAccount(accountBean.getAccountNumber());
        model.setPayerName(accountBean.getAccountName());
        model.setAccountId(accountBean.getAccountId());
    }

    @Override
    public void updateAccBalanceLimit() {
        setSelectAccData(selectedAcc);
        upDateFessLimit(model.getAnnAmtUSD(), model.getAnnRmeAmtUSD());
        upDateAccBalance(model.getAvailableBalance(), ApplicationConst.CURRENCY_CNY);
        if (selectAccFragment != null && selectAccFragment.isVisible()) {
            selectAccFragment.setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, selectAccDate);
            selectAccFragment.pop();
        }
    }

    /**
     * 余额
     *
     * @param availableBalance
     */
    protected void upDateAccBalance(String availableBalance, String currency) {
        ll_balance.setVisibility(View.VISIBLE);
        tv_aviliable_balance.setText(String.format(getString(R.string.boc_available_balance),
                PublicCodeUtils.getCurrency(mContext, currency),
                MoneyUtils.transMoneyFormat(availableBalance, currency)));
        //余额为零显示去转账按钮
        if (new BigDecimal("0").compareTo(new BigDecimal(availableBalance)) == 0) {
            tv_goto_trans.setVisibility(View.VISIBLE);
        } else {
            tv_goto_trans.setVisibility(View.GONE);
        }
    }
}
