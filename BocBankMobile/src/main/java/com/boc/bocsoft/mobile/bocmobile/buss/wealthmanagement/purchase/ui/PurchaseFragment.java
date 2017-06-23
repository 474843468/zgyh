package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.ui;

import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.pdf.PDFFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.AccountUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DateTimePicker;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.MClickableSpan;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.SpannableString;
import com.boc.bocsoft.mobile.bocmobile.base.widget.PortfolioProductInfoView.PortfolioProductInfoView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialog.MessageDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditClearWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.moneyruler.MoneyRulerWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selectdialog.SelectDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.webView.ContractFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.DateUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.ui.TransRemitBlankFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.model.WealthAccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.riskassess.ui.RiskAssessFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.ModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.model.PurchaseInputMode;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.model.PurchaseModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.presenter.PurchasePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.WealthConst;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.WealthProductFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangtong on 2016/9/18.
 */
public class PurchaseFragment extends BaseAccountFragment<PurchasePresenter> implements PurchaseContact.PurchaseView, MoneyRulerWidget.MoneyRulerScrollerListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener, MClickableSpan.OnClickSpanListener {

    public static final String PURCHASE = "purchase";
    public static final String PURCHASE_LIST = "purchaseList";

    /**
     * 申购手续费Panel,购买期数Panel,赎回日期Panel
     */
    private ViewGroup llFee, llPeriod, llRedeem;
    /**
     * 剩余额度,是否允许撤单,申购手续费
     */
    private TextView tvCredit, tvCancelable, tvFee;
    /**
     * 赎回日期提示,营销代码标题,营销代码
     */
    private TextView tvRedeem, tvMarket, tvMarketCode;
    /**
     * 产品名称
     */
    private PortfolioProductInfoView tvProduct;
    /**
     * 交易账户,钞汇选择(外币时选择),赎回日期
     */
    private EditChoiceWidget etAccount, etCashRemit, etRedeem;
    /**
     * 购买期数
     */
    private EditClearWidget etPeriod;
    /**
     * 余额提示,账户充值,用户协议
     */
    private SpannableString tvBalance, tvAgreement;
    /**
     * 金额输入标尺
     */
    private MoneyRulerWidget rulerAmount;
    /**
     * 金额输入框
     */
    private EditMoneyInputWidget etAmount;
    /**
     * 是否指定日期赎回,是否同意协议
     */
    private CheckBox cbRedeem, cbAgree;
    /**
     * 添加营销代码
     */
    private ImageView ivAdd;
    /**
     * 下一步按钮
     */
    private Button btnNext;

    /**
     * 交易购买Bean
     */
    private PurchaseModel model;
    /**
     * 账户列表
     */
    private ArrayList<WealthAccountBean> accountList;

    public static PurchaseFragment newInstance(PurchaseInputMode mode, ArrayList<WealthAccountBean> beans) {
        PurchaseFragment purchaseFragment = new PurchaseFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PURCHASE, mode);
        if (beans != null)
            bundle.putSerializable(PURCHASE_LIST, beans);
        purchaseFragment.setArguments(bundle);
        return purchaseFragment;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_wealth_buy);
    }

    @Override
    protected PurchasePresenter initPresenter() {
        return new PurchasePresenter(this);
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_wealthproduct_purchase, null);
    }

    @Override
    public void initView() {
        llFee = (ViewGroup) mContentView.findViewById(R.id.ll_fee);
        llPeriod = (ViewGroup) mContentView.findViewById(R.id.ll_period);
        llRedeem = (ViewGroup) mContentView.findViewById(R.id.ll_redeem);

        tvProduct = (PortfolioProductInfoView) mContentView.findViewById(R.id.tv_product);
        tvCredit = (TextView) mContentView.findViewById(R.id.tv_credit);
        tvCancelable = (TextView) mContentView.findViewById(R.id.tv_cancelable);
        tvFee = (TextView) mContentView.findViewById(R.id.tv_fee);
        tvRedeem = (TextView) mContentView.findViewById(R.id.tv_redeem);
        tvMarket = (TextView) mContentView.findViewById(R.id.tv_market);
        tvMarketCode = (TextView) mContentView.findViewById(R.id.tv_market_code);

        etAccount = (EditChoiceWidget) mContentView.findViewById(R.id.et_account);
        etCashRemit = (EditChoiceWidget) mContentView.findViewById(R.id.et_cash_remit);
        etPeriod = (EditClearWidget) mContentView.findViewById(R.id.et_period);
        etRedeem = (EditChoiceWidget) mContentView.findViewById(R.id.et_redeem);
        etRedeem.setChoiceTextContentHint(getString(R.string.boc_common_select));

        tvBalance = (SpannableString) mContentView.findViewById(R.id.tv_balance);
        tvAgreement = (SpannableString) mContentView.findViewById(R.id.tv_agreement);

        rulerAmount = (MoneyRulerWidget) mContentView.findViewById(R.id.ruler_amount);
        rulerAmount.getMoneyInputTextView().setScrollView(mContentView);
        etAmount = (EditMoneyInputWidget) mContentView.findViewById(R.id.et_amount);

        cbRedeem = (CheckBox) mContentView.findViewById(R.id.cb_redeem);
        cbAgree = (CheckBox) mContentView.findViewById(R.id.cb_agree);

        ivAdd = (ImageView) mContentView.findViewById(R.id.iv_add);
        btnNext = (Button) mContentView.findViewById(R.id.btn_next);
    }

    @Override
    public void setListener() {
        etAccount.setOnClickListener(this);
        etCashRemit.setOnClickListener(this);
        cbRedeem.setOnCheckedChangeListener(this);
        etRedeem.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        ivAdd.setOnClickListener(this);
        rulerAmount.setOnMoneyRulerScrollerListener(this);
    }

    @Override
    public void initData() {
        initBaseInfo();

        initAccountList();

        initMoneyInput();

        cbRedeem.setChecked(false);
        initRedeemDate(cbRedeem.isChecked());

        initAgreement();

        setMarketCode(null, getString(R.string.boc_purchase_product_market_code_title), View.VISIBLE, R.drawable.boc_detail_add);

        loadBalance();
    }

    @Override
    public void reInit() {
        loadBalance();
    }

    private void loadBalance() {
        showLoadingDialog();
        getPresenter().queryAccountDetail(model);
    }

    /**
     * 初始化基本信息
     */
    private void initBaseInfo() {
        model = null;
        PurchaseInputMode inputModel = getArguments().getParcelable(PURCHASE);
        if (inputModel != null)
            model = new PurchaseModel(inputModel);

        tvProduct.setText("[" + PublicCodeUtils.getCurrency(mContext, model.getCurCode()) + "]", model.getProdName(), "（" + model.getProdCode() + "）");
        tvCredit.setText(MoneyUtils.transMoneyFormat(model.getCreditBalance(), model.getCurCode()));

        switch (model.getIsCanCancel()) {
            case PurchaseModel.CANCEL_ZERO:
                tvCancelable.setText(getString(R.string.boc_purchase_product_cancel1));
                break;
            case PurchaseModel.CANCEL_ONE:
                tvCancelable.setText(getString(R.string.boc_purchase_product_cancel2));
                break;
            case PurchaseModel.CANCEL_TWO:
                tvCancelable.setText(getString(R.string.boc_purchase_product_cancel3));
                break;
        }

        if (model.isFundProKind())
            tvFee.setText(model.getFormatPurchFee());
        else
            llFee.setVisibility(View.GONE);

        if (ApplicationConst.CURRENCY_CNY.equals(model.getCurCode()))
            etCashRemit.setVisibility(View.GONE);
        else
            etCashRemit.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化账户信息
     */
    private void initAccountList() {
        accountList = (ArrayList<WealthAccountBean>) getArguments().getSerializable(PURCHASE_LIST);

        if (accountList == null)
            accountList = new ArrayList<>();

        WealthAccountBean wealthAccountBean;
        if (model == null || StringUtils.isEmptyOrNull(model.getPayAccountId()))
            wealthAccountBean = accountList.get(0);
        else
            wealthAccountBean = new WealthAccountBean(model.getPayAccountId(), model.getPayAccountNum(), model.getAccountKey());

        List<AccountBean> accountBeans = ApplicationContext.getInstance().getChinaBankAccountList(null);

        AccountBean accountBean = accountBeans.get(accountBeans.indexOf(wealthAccountBean));
        model.setPayAccountId(accountBean.getAccountId());
        model.setPayAccountNum(accountBean.getAccountNumber());
        model.setAccountKey(wealthAccountBean.getAccountKey());

        etAccount.setChoiceTextContent(NumberUtils.formatCardNumber(model.getPayAccountNum()));
        etAccount.setArrowImageGone(!accountList.isEmpty());
        etAccount.setClickable(!accountList.isEmpty());
        etAccount.setEnabled(!accountList.isEmpty());
    }

    /**
     * 初始化金额输入控件
     */
    private void initMoneyInput() {
        if (model.isPeriodProduct()) {
            llPeriod.setVisibility(View.VISIBLE);
            rulerAmount.setVisibility(View.GONE);
            etPeriod.setEditWidgetHint(getString(R.string.boc_purchase_product_period_hint_max, model.getMaxPeriod()));
            etPeriod.getContentEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
//            if (model.getBuyAmount().doubleValue() > 0)
//                etAmount.setmContentMoneyEditText(model.getBuyAmount().toPlainString());
        } else {
            rulerAmount.initMoneyRuler(model.getSubAmount().intValue(), model.getBaseAmount().intValue(), model.getCurCode());
            rulerAmount.setMoneyLabel(getString(R.string.boc_purchase_product_amount));
            rulerAmount.setMinTip(getString(R.string.boc_purchase_product_amount_hint_min));
            rulerAmount.setMaxTip(getString(R.string.boc_purchase_product_amount_hint_max));

            if (model.getBuyAmount().doubleValue() > 0)
                rulerAmount.setInitMoney(model.getBuyAmount().toPlainString());
        }
    }

    /**
     * 初始化回购日期
     *
     * @param checked
     */
    private void initRedeemDate(boolean checked) {
        etRedeem.setChoiceTextContent("");

        if (!model.isCanRedeem()) {
            llRedeem.setVisibility(View.GONE);
            return;
        }

        llRedeem.setVisibility(View.VISIBLE);

        if (!checked) {
            tvRedeem.setText(getString(R.string.boc_purchase_product_redeem_hint1));
            return;
        }

        if (!model.isCycleRedeem()) {
            tvRedeem.setText(getString(R.string.boc_purchase_product_redeem_hint, model.getRedeemStartDate().format(DateFormatters.dateFormatter2).toString(), model.getRedeemEndDate().format(DateFormatters.dateFormatter2).toString()));
            return;
        }

        switch (model.getRedEmperiodfReq()) {
            case PurchaseModel.REDEEM_DAY:
                tvRedeem.setText(getString(R.string.boc_purchase_product_redeem_hint_day, model.getRedEmperiodStart(), model.getRedEmperiodEnd()));
                break;
            case PurchaseModel.REDEEM_WEEK:
                tvRedeem.setText(getString(R.string.boc_purchase_product_redeem_hint_week, model.getRedEmperiodStart(), model.getRedEmperiodEnd()));
                break;
            case PurchaseModel.REDEEM_MONTH:
                tvRedeem.setText(getString(R.string.boc_purchase_product_redeem_hint_month, model.getRedEmperiodStart(), model.getRedEmperiodEnd()));
                break;
        }
    }

    private void initAgreement() {
        tvAgreement.setText("");
        cbAgree.setChecked(false);

        String startTitle1 = getString(R.string.boc_purchase_product_agreement_start_title1);
        String content1 = getString(R.string.boc_purchase_product_agreement_content1);
        String endTitle1 = getString(R.string.boc_purchase_product_agreement_end_title1);
        tvAgreement.setAppendContent(startTitle1, endTitle1, content1, new MClickableSpan.OnClickSpanListener() {
            @Override
            public void onClickSpan() {
                start(ContractFragment.newInstance("file:///android_asset/webviewcontent/wealthmanagement/portfoliopurchase/notice/notice.html"));
            }
        });

        String startTitle2 = getString(R.string.boc_purchase_product_agreement_start_title2);
        String content2 = getString(R.string.boc_purchase_product_agreement_content2);
        String endTitle2 = getString(R.string.boc_purchase_product_agreement_end_title2);
        tvAgreement.setAppendContent(startTitle2, endTitle2, content2, new MClickableSpan.OnClickSpanListener() {
            @Override
            public void onClickSpan() {
                String url = WealthConst.URL_INSTRUCTION + model.getProdCode();
                start(PDFFragment.newInstance(Uri.parse(url)));
            }
        });
    }

    private void initBalanceHint(BigDecimal money) {
        if (!isCurrentFragment())
            return;

        if (ApplicationConst.CURRENCY_CNY.equals(model.getCurCode()))
            etCashRemit.setVisibility(View.GONE);
        else
            etCashRemit.setVisibility(View.VISIBLE);

        tvBalance.setVisibility(View.VISIBLE);
        StringBuilder builder = new StringBuilder();

        String currency = PublicCodeUtils.getCurrency(mContext, model.getCurCode());
        String cashRemit = AccountUtils.getCashRemit(model.getCashRemitCode());

        etCashRemit.setChoiceTextContent(currency + "/" + cashRemit);

        builder.append(currency);
        if (!ApplicationConst.CURRENCY_CNY.equals(model.getCurCode()))
            builder.append("/" + cashRemit);


        builder.append(MoneyUtils.transMoneyFormat(model.getPayBalance(), model.getCurCode()));

        if(model.getPayBalance() == null)
            return;

        tvBalance.setContent(getString(R.string.boc_purchase_product_balance_title), builder.toString(), R.color.boc_text_color_money_count);

        if (model.getPayBalance().compareTo(money) >= 0)
            return;

        if (!ApplicationConst.CURRENCY_CNY.equals(model.getCurCode()))
            return;

        String rechargeTitle = getString(R.string.boc_purchase_product_recharge_title);
        String rechargeContent = getString(R.string.boc_purchase_product_recharge);
        tvBalance.setAppendContent(rechargeTitle, rechargeContent, R.color.boc_main_button_color, true, this);
    }

    @Override
    public void onClick(View v) {
        if (v == etAccount)
            goSelectAccount();
        else if (v == etCashRemit)
            showCashRemitDialog();
        else if (v == etRedeem)
            showRedeemDialog();
        else if (v == btnNext)
            productBuyPre();
        else if (v == ivAdd)
            showMarketCodeDialog();
    }

    /**
     * 跳转选择账户页面
     */
    private void goSelectAccount() {
        ArrayList<AccountBean> list = new ArrayList<>();

        List<AccountBean> accountBeans = ApplicationContext.getInstance().getChinaBankAccountList(null);

        if (accountList != null && !accountList.isEmpty()) {
            for (WealthAccountBean wealthAccountBean : accountList) {
                if (accountBeans.contains(wealthAccountBean))
                    list.add(accountBeans.get(accountBeans.indexOf(wealthAccountBean)));
            }
        }

        SelectAccoutFragment fragment = SelectAccoutFragment.newInstanceWithData(list);
        startForResult(fragment, SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT);
    }

    private void showCashRemitDialog() {
        final List<String> list = new ArrayList<>();
        list.add(PublicCodeUtils.getCurrency(mContext, model.getCurCode()) + "/钞");
        list.add(PublicCodeUtils.getCurrency(mContext, model.getCurCode()) + "/汇");
        SelectDialog dialog = new SelectDialog(getContext());
        dialog.setListener(new SelectDialog.OnItemSelectDialogClicked() {
            @Override
            public void onListItemClicked(int index) {
                etCashRemit.setChoiceTextContent(list.get(index));
                if (index == 0) {
                    model.setCashRemitCode(PurchaseModel.CODE_CASH);
                    model.setPayBalance(model.getPayBalanceCash());
                } else {
                    model.setCashRemitCode(PurchaseModel.CODE_REMIT);
                    model.setPayBalance(model.getPayBalanceRemit());
                }

                StringBuilder builder = new StringBuilder(
                        PublicCodeUtils.getCurrency(mContext, model.getCurCode()));
                String cashRemit = AccountUtils.getCashRemit(model.getCashRemitCode());
                if (!StringUtils.isEmpty(cashRemit))
                    builder.append("/").append(cashRemit);

                builder.append(" " + MoneyUtils.transMoneyFormat(model.getPayBalance(), model.getCurCode()));
                tvBalance.setContent(getString(R.string.boc_purchase_product_balance_title), builder.toString(), R.color.boc_text_color_money_count);
            }
        });
        dialog.showDialog(list);
    }

    private void showRedeemDialog() {
        //回购日期
        LocalDate current = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate();
        long minDate = DateUtil.parse(current.plusDays(1).format(DateFormatters.dateFormatter1));

        DateTimePicker.showRangeDatePick(getContext(), current, minDate, 0, DateFormatters.dateFormatter1, new DateTimePicker.DatePickCallBack() {
            @Override
            public void onChoiceDateSet(String strChoiceTime, LocalDate localDate) {
                etRedeem.setChoiceTextContent(strChoiceTime);
                model.setRedeemDate(localDate);
            }
        });
    }

    /**
     * 添加营销代码
     */
    private void showMarketCodeDialog() {
        final EditDialog dialog = new EditDialog(getContext());
        if (!StringUtils.isEmptyOrNull(model.getMartCode()))
            dialog.setInputText(model.getMartCode());

        dialog.setRightButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(dialog.getInputText()))
                    setMarketCode(dialog.getInputText(), getString(R.string.boc_purchase_product_market_code, dialog.getInputText()), View.GONE, R.drawable.boc_detail_revise);
                else
                    setMarketCode(null, getString(R.string.boc_purchase_product_market_code_title), View.VISIBLE, R.drawable.boc_detail_add);
                dialog.dismiss();
            }
        });
        dialog.showDialog();
    }

    /**
     * 设置营销代码
     *
     * @param marketCode
     * @param content
     * @param visibility
     * @param res
     */
    private void setMarketCode(String marketCode, String content, int visibility, int res) {
        model.setMartCode(marketCode);
        tvMarketCode.setText(content);
        tvMarket.setVisibility(visibility);
        ivAdd.setBackgroundResource(res);
    }

    /**
     * 预交易
     */
    private void productBuyPre() {
        if (model.isPeriodProduct()) {
            if (etAmount.getMoney().compareTo(new BigDecimal(0)) == 0) {
                showErrorDialog(getString(R.string.boc_purchase_product_amount_hint_empty));
                return;
            }

            if (StringUtils.isEmptyOrNull(etPeriod.getEditWidgetContent())) {
                showErrorDialog(getString(R.string.boc_purchase_product_period_hint_min));
                return;
            }

            int period = Integer.parseInt(etPeriod.getEditWidgetContent());
            if (period > model.getMaxPeriod()) {
                showErrorDialog(getString(R.string.boc_purchase_product_period_hint_min));
                return;
            }
            if (period <= 0) {
                showErrorDialog(getString(R.string.boc_purchase_product_period_hint_min));
                return;
            }

            model.setPeriodNumber(period);
            model.setBuyAmount(etAmount.getMoney());
        } else {
            if (rulerAmount.getMoney().compareTo(new BigDecimal(0)) == 0) {
                showErrorDialog(getString(R.string.boc_purchase_product_amount_hint_empty));
                return;
            }
            model.setBuyAmount(rulerAmount.getMoney());
        }

        if (!cbAgree.isChecked()) {
            showErrorDialog(getString(R.string.boc_purchase_product_agreement_hint));
            return;
        }

        showLoadingDialog(false);
        getPresenter().queryRiskMatch(model);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked)
            setVisibility(View.VISIBLE, etRedeem);
        else
            setVisibility(View.GONE, etRedeem);

        initRedeemDate(isChecked);
    }

    @Override
    public void onClickSpan() {
        TransRemitBlankFragment bussFragment = new TransRemitBlankFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TransRemitBlankFragment.ACCOUNT_FROM_PURCHASEFRAGMENT, model.getPayAccountId());
        bussFragment.setArguments(bundle);
        start(bussFragment);
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT != resultCode)
            return;

        if (SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT != requestCode)
            return;

        AccountBean accountBean = data.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
        WealthAccountBean entity = accountList.get(accountList.indexOf(accountBean));

        model = ModelUtil.generatePurchaseModel(model, accountBean, entity);
        etAccount.setChoiceTextContent(NumberUtils.formatCardNumber(model.getPayAccountNum()));

        loadBalance();
    }

    @Override
    public void onMoneyRulerScrollered(BigDecimal money) {
        initBalanceHint(money);
    }

    @Override
    public void queryAccountDetail(PurchaseModel purchaseModel) {
        closeProgressDialog();
        this.model = purchaseModel;
        initBalanceHint(model.getBuyAmount());
    }

    @Override
    public void queryRiskMatch(PurchaseModel purchaseModel) {
        closeProgressDialog();
        this.model = purchaseModel;

        switch (model.getRiskMatch()) {
            case PurchaseModel.RISK_SUCCESS:
                showLoadingDialog(false);
                getPresenter().productBuyPre(model);
                break;
            case PurchaseModel.RISK_WARN:
                String leftTitle = getString(R.string.boc_purchase_product_risk_cancel);
                String rightTitle = getString(R.string.boc_purchase_product_risk_ok);
                showMessageDialog(leftTitle, rightTitle, new MessageOnClickListener(MessageOnClickListener.TYPE_PRE_TRANSACTION), getString(R.string.boc_purchase_product_risk_warn_hint));
                break;
            case PurchaseModel.RISK_FAIL:
                leftTitle = getString(R.string.boc_purchase_product_risk_retry);
                if (findFragment(WealthProductFragment.class) != null)
                    rightTitle = getString(R.string.boc_purchase_product_risk_other);
                else
                    rightTitle = getString(R.string.boc_purchase_product_risk_other_close);
                showMessageDialog(leftTitle, new MessageOnClickListener(MessageOnClickListener.TYPE_RISK_RETRY), rightTitle, new MessageOnClickListener(MessageOnClickListener.TYPE_RISK_OTHER), getString(R.string.boc_purchase_product_risk_fail_hint));
                break;
        }
    }

    @Override
    public void productBuyPre(PurchaseModel purchaseModel) {
        closeProgressDialog();
        this.model = purchaseModel;

        String leftTitle = getString(R.string.boc_common_cancel);
        String rightTitle = getString(R.string.boc_common_sure);

        if (!model.isHasInvestXp()) {
            if (model.isOrderTime())
                showMessageDialog(leftTitle, rightTitle, new MessageOnClickListener(MessageOnClickListener.TYPE_ORDER_TIME), getString(R.string.boc_purchase_product_order_time_hint));
            else
                start(new PurchaseConfirmFragment(model));
            return;
        }

        if (model.isOrderTime())
            showMessageDialog(leftTitle, rightTitle, new MessageOnClickListener(MessageOnClickListener.TYPE_INVEST_AND_ORDER_TIME), getString(R.string.boc_purchase_product_invest_hint));
        else
            showMessageDialog(leftTitle, rightTitle, new MessageOnClickListener(MessageOnClickListener.TYPE_INVEST), getString(R.string.boc_purchase_product_invest_hint));
    }

    private MessageDialog messageDialog;

    private void showMessageDialog(String leftTitle, String rightTitle, View.OnClickListener rightListener, String content) {
        showMessageDialog(leftTitle, null, rightTitle, rightListener, content);
    }

    private void showMessageDialog(String leftTitle, View.OnClickListener leftListener, String rightTitle, View.OnClickListener rightListener, String content) {
        if (messageDialog == null)
            messageDialog = new MessageDialog(mContext);

        if (!StringUtils.isEmptyOrNull(leftTitle))
            messageDialog.setLeftButtonText(leftTitle);

        messageDialog.setLeftButtonClickListener(leftListener);

        if (!StringUtils.isEmptyOrNull(rightTitle))
            messageDialog.setRightButtonText(rightTitle);

        messageDialog.setRightButtonClickListener(rightListener);

        messageDialog.showDialog(content);
    }

    private class MessageOnClickListener implements View.OnClickListener {

        /**
         * 预交易
         */
        private static final int TYPE_PRE_TRANSACTION = 1;
        /**
         * 挂单弹框
         */
        private static final int TYPE_ORDER_TIME = 2;
        /**
         * 投资弹框
         */
        private static final int TYPE_INVEST = 3;
        /**
         * 先投资弹框,再挂单弹框
         */
        private static final int TYPE_INVEST_AND_ORDER_TIME = 4;
        /**
         * 重新风险评估
         */
        private static final int TYPE_RISK_RETRY = 5;
        /**
         * 查看其它理财产品
         */
        private static final int TYPE_RISK_OTHER = 6;

        private int type;

        public MessageOnClickListener(int type) {
            this.type = type;
        }

        @Override
        public void onClick(View v) {
            switch (type) {
                case TYPE_PRE_TRANSACTION:
                    showLoadingDialog(false);
                    getPresenter().productBuyPre(model);
                    messageDialog.dismiss();
                    break;
                case TYPE_ORDER_TIME:
                case TYPE_INVEST:
                    messageDialog.dismiss();
                    start(new PurchaseConfirmFragment(model));
                    break;
                case TYPE_INVEST_AND_ORDER_TIME:
                    showMessageDialog(getString(R.string.boc_common_cancel), getString(R.string.boc_common_sure), new MessageOnClickListener(MessageOnClickListener.TYPE_ORDER_TIME), getString(R.string.boc_purchase_product_order_time_hint));
                    break;
                case TYPE_RISK_RETRY:
                    messageDialog.dismiss();
                    start(new RiskAssessFragment(PurchaseFragment.class));
                    break;
                case TYPE_RISK_OTHER:
                    messageDialog.dismiss();
                    if (findFragment(WealthProductFragment.class) != null)
                        popToAndReInit(WealthProductFragment.class);
                    break;
            }
        }
    }
}
