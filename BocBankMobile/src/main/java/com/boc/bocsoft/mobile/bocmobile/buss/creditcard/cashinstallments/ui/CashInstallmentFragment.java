package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.cashinstallments.ui;

import android.app.Activity;
import android.os.Bundle;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.SpannableString;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailRow;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectStringListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.moneyinputview.MoneyInputTextView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.Content;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.SelectGridView;
import com.boc.bocsoft.mobile.bocmobile.buss.account.apply.model.ApplyAccountModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.cashinstallments.model.CashInstallmentViewModel;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.framework.utils.PublicConst;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.cashinstallments.presenter.CashInstallmentPresenter;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 信用卡-现金分期
 * create by cry7096 on 2016/12/03.
 */
public class CashInstallmentFragment extends MvpBussFragment<CashInstallmentContract.Presenter>
        implements CashInstallmentContract.View, View.OnClickListener {

    private static final String PARAM = "param";
    private View rootView;
    //    private CashInstallmentContract.Presenter mCashInstallmentFragmentPresenter;
    private String mParam;
    private List<Content> selectGridViewList;
    //提现金额
    private EditMoneyInputWidget editMoneyInputWidget;
    //手续费分期方式
    private SelectGridView mSelectChargePattern;// 单选多选组件
    //手续费详情
    private DetailRow divChargeRate, firstPayAmount, perPayAmount;
    //手续费模块
    private LinearLayout charge;
    // 过滤的账户类型
    private ArrayList<String> accountTypeList;
    // 还款期数
    private EditChoiceWidget mDivPeriod;
    // 信用卡账户列表选择组件
    private EditChoiceWidget mCreditChoiceAccount;
    // 借记卡账户列表选择组件
    private EditChoiceWidget mDebitChoiceAccount;
    //勾选的checkBox
    private CheckBox cbAgreement;
    //服务须知
    protected TextView userContract;
    // 下一步
    private Button btnNext;
    //分期期数对话框
    private SelectStringListDialog divPeriodDialog;
    //可调用接口的选择账户
    protected CashAccountSelectFragment mSelectAccoutFragment;
    //选择信用卡账户的请求码
    protected final int REQUEST_CODE_SELECT_CREDIT_ACCOUNT = 1;
    //选择借记卡账户的请求码
    protected final int REQUEST_CODE_SELECT_DEBIT_ACCOUNT = 2;
    //View Model
    protected CashInstallmentViewModel cashInstallmentViewModel;
    //conversation id
    static private String conversationId;
    //上一次选择的信用卡accountId
    private String accountIdPre;
    //上一次输入的提现金额
    private String inputMoneyTemp = "";
    //上一次选择的手续费支付方式
    private int chargeModePre;
    //上一次手续费试算是否成功
    private boolean isChargeSuccess = false;
    //分期手续费
    static private String stDivCharge;
    //首期应还
    static private String stFirstPayAmount;
    //每期应还
    static private String stPerPayAmount;
    //分期手续费率
    static private String stDivRate;
    //首期手续费
    static private String stFirstCharge;
    //每期手续费
    static private String stPerCharge;
    //存放临时卡列表bean
    List<AccountBean> accountBeanListTemp;

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_cash_installment, null);
        return rootView;
    }

    @Override
    public void initView() {
        mSelectChargePattern = (SelectGridView) rootView.findViewById(R.id.select_charge_pattern);
        divChargeRate = (DetailRow) rootView.findViewById(R.id.boc_crcd_div_charge_rate);
        firstPayAmount = (DetailRow) rootView.findViewById(R.id.boc_crcd_first_pay_amount);
        perPayAmount = (DetailRow) rootView.findViewById(R.id.boc_crcd_per_pay_amount);
        charge = (LinearLayout) rootView.findViewById(R.id.charge);
        mDivPeriod = (EditChoiceWidget) rootView.findViewById(R.id.div_period);
        mCreditChoiceAccount = (EditChoiceWidget) rootView.findViewById(R.id.choice_credit_account);
        mDebitChoiceAccount = (EditChoiceWidget) rootView.findViewById(R.id.choice_debit_account);
        cbAgreement = (CheckBox) rootView.findViewById(R.id.cb_agreement);
        userContract = (SpannableString) rootView.findViewById(R.id.tv_agreement);
        btnNext = (Button) rootView.findViewById(R.id.btn_confirm);
        editMoneyInputWidget = (EditMoneyInputWidget) rootView.findViewById(R.id.money_input);
        setNameWidth();
        //请选择
        mCreditChoiceAccount.setChoiceTextContent(getString(R.string.boc_crcd_please_select));
        mDebitChoiceAccount.setChoiceTextContent(getString(R.string.boc_crcd_please_select));
        mDivPeriod.setChoiceTextContent(getString(R.string.boc_crcd_please_select));
        editMoneyInputWidget.setContentHint(getString(R.string.boc_crcd_cash_money_lower_hint) +
            MoneyUtils.transMoneyFormat(getString(R.string.boc_crcd_div_amount_lower), "001"));
    }

    @Override
    public void initData() {
        if (getArguments() != null) {
            mParam = getArguments().getString(PARAM);
        }
        //账户选择类型列表
        accountTypeList = new ArrayList<String>();
        //信用卡号
        cashInstallmentViewModel = new CashInstallmentViewModel();
        //初始化分期支付方式单选框
        initDivPattern();
        //服务须知
        userContract();
    }

    /**
     * 初始化分期支付方式单选框
     */
    private void initDivPattern() {
        //分期方式
        selectGridViewList = new ArrayList<Content>();
        selectGridViewList.clear();
        Content content = new Content();
        content.setName(getString(R.string.boc_crcd_full_pay));
        content.setContentNameID("0");
        content.setSelected(false);
        selectGridViewList.add(content);

        content = new Content();
        content.setName(getString(R.string.boc_crcd_installment_pay));
        content.setContentNameID("1");
        content.setSelected(false);
        selectGridViewList.add(content);
        mSelectChargePattern.setData(selectGridViewList);
    }

    /**
     * 初始化用户协议
     */
    private void userContract() {
        String limitString = getString(R.string.boc_crcd_user_contract);
        SpannableStringBuilder style = new SpannableStringBuilder(limitString);
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.boc_text_color_red)),
                14, 33, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        style.setSpan(new MyClickableSpan(), 14, 33, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        userContract.setMovementMethod(LinkMovementMethod.getInstance());
        userContract.setText(style);
    }

    @Override
    protected CashInstallmentContract.Presenter initPresenter() {
        return new CashInstallmentPresenter(this);
    }

    @Override
    public void setListener() {
        mDivPeriod.setOnClickListener(CashInstallmentFragment.this);
        mCreditChoiceAccount.setOnClickListener(CashInstallmentFragment.this);
        mDebitChoiceAccount.setOnClickListener(CashInstallmentFragment.this);
        btnNext.setOnClickListener(CashInstallmentFragment.this);

        //输入金额变化时的监听，此监听会在键盘收起或点击清除图标时执行
        editMoneyInputWidget.setMoneyInputCompleteListener(new EditMoneyInputWidget.MoneyInputCompleteListener() {
            @Override
            public void InputComplete(String inputMoney) {
                if (!inputMoney.equals(inputMoneyTemp)) {
                    reinitDivCharge();
                    //只有当输入金额满足要求时，才将输入金额给temp,并传入Model
                    inputMoneyTemp = inputMoney;
                    cashInstallmentViewModel.setDivAmount(editMoneyInputWidget.getContentMoney());
                }
            }
        });

        //分期支付方式改变响应函数
        mSelectChargePattern.setListener(new SelectGridView.ClickListener() {
            @Override
            public void setItemClick(AdapterView<?> parent, View view, int position, long id) {
                //币种
                cashInstallmentViewModel.setCurrency("001");
                if (position != chargeModePre || ((position == chargeModePre) &&
                        (isChargeSuccess == false))) {
                    if (position == 0) {//一次性支付
                        cashInstallmentViewModel.setChargeMode("0");
                        chargeModePre = 0;
                    } else {// 分期支付
                        cashInstallmentViewModel.setChargeMode("1");
                        chargeModePre = 1;
                    }
                    //如果未选择信用卡
                    if (null == cashInstallmentViewModel.getAvailableBalance()) {
                        showErrorDialog(getString(R.string.boc_crcd_credit_card_empty));
                        return;
                    }
                    //如果没有输入提现金额
                    if (null == editMoneyInputWidget.getContentMoney() || editMoneyInputWidget.getContentMoney().equals("")) {
                        showErrorDialog(getString(R.string.boc_crcd_div_amount_empty));
                        return;
                    }
                    //如果输入提现金额大于上限
                    if (editMoneyInputWidget.getMoney().compareTo(
                            new BigDecimal(cashInstallmentViewModel.getAvailableBalance())) == 1) {
                        showErrorDialog(getString(R.string.boc_crcd_div_amount_upper_limit) +
                                cashInstallmentViewModel.getAvailableBalance());
                        return;
                    }
                    //如果输入提现金额小于下限
                    if (editMoneyInputWidget.getMoney().compareTo(
                            new BigDecimal(getString(R.string.boc_crcd_div_amount_lower))) == -1) {
                        showErrorDialog(getString(R.string.boc_crcd_div_amount_lower_limit) +
                            MoneyUtils.transMoneyFormat(getString(R.string.boc_crcd_div_amount_lower), "001"));
                        return;
                    }
                    //如果输入提现金额不是100的倍数
                    else if (editMoneyInputWidget.getMoney().divideAndRemainder(new BigDecimal(getString(
                            R.string.boc_crcd_div_amount_100)))[1].compareTo(BigDecimal.ZERO) != 0) {
                        showErrorDialog(getString(R.string.boc_crcd_div_amount_multiple_limit));
                        return;
                    }
                    //如果未选择还款期数
                    if (null == cashInstallmentViewModel.getDivPeriod()) {
                        showErrorDialog(getString(R.string.boc_crcd_div_period_empty));
                        return;
                    }
                    //校验通过
                    chargeModePre = position;
                    cashInstallmentViewModel.setDivAmount(editMoneyInputWidget.getContentMoney());
                    getPresenter().getCashDivCommissionCharge(cashInstallmentViewModel);
                    //false：不带叉号，不能取消的
                    showLoadingDialog(false);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        //选择信用卡
        if (view.getId() == R.id.choice_credit_account) {
            filterCrcdAccountType();
            //选择信用卡
            selectCreditCard();
        }
        //选择收款卡号
        else if (view.getId() == R.id.choice_debit_account) {
            filterDebitAccountType();
            //选择借记卡
            selectDebitCard();
        }
        //还款期数
        else if (view.getId() == R.id.div_period) {
            showDivPeriodDialog();
        }
        //点击下一步
        else if (view.getId() == R.id.btn_confirm) {
            //如果未选择信用卡
            if (null == cashInstallmentViewModel.getAvailableBalance()) {
                showErrorDialog(getString(R.string.boc_crcd_credit_card_empty));
                return;
            }
            //如果没有输入提现金额
            if (null == cashInstallmentViewModel.getDivAmount()) {
                showErrorDialog(getString(R.string.boc_crcd_div_amount_empty));
                return;
            }
            //如果输入提现金额大于上限
            else if (editMoneyInputWidget.getMoney().compareTo(
                    new BigDecimal(cashInstallmentViewModel.getAvailableBalance())) == 1) {
                showErrorDialog(getString(R.string.boc_crcd_div_amount_upper_limit) + MoneyUtils.transMoneyFormat(
                        cashInstallmentViewModel.getAvailableBalance(), "001"));
                return;
            }
            //如果输入提现金额小于下限
            else if (editMoneyInputWidget.getMoney().compareTo(
                    new BigDecimal(getString(R.string.boc_crcd_div_amount_lower))) == -1) {
                showErrorDialog(getString(R.string.boc_crcd_div_amount_lower_limit) +
                        MoneyUtils.transMoneyFormat(getString(R.string.boc_crcd_div_amount_lower), "001"));
                return;
            }
            //如果输入提现金额不是100的倍数
            else if (editMoneyInputWidget.getMoney().divideAndRemainder(new BigDecimal(getString(
                    R.string.boc_crcd_div_amount_100)))[1].compareTo(BigDecimal.ZERO) != 0) {
                showErrorDialog(getString(R.string.boc_crcd_div_amount_multiple_limit));
                return;
            }
            //如果未选择还款期数
            if (null == cashInstallmentViewModel.getDivPeriod()) {
                showErrorDialog(getString(R.string.boc_crcd_div_period_empty));
                return;
            }
            //如果分期方式没有选择
            if (!selectGridViewList.get(0).getSelected() && !selectGridViewList.get(1).getSelected()) {
                showErrorDialog(getString(R.string.boc_crcd_charge_mode_empty));
                return;
            }
            //如果未选择借记卡
            if (null == cashInstallmentViewModel.getToCardNo()) {
                showErrorDialog(getString(R.string.boc_crcd_debit_card_empty));
                return;
            }
            //检验是否勾选了须知
            if (!cbAgreement.isChecked()) {
                showErrorDialog(getString(R.string.boc_account_service_bureau_hint));
                return;
            }
            //请求安全因子列表
            getPresenter().qrySecurityFactor(getString(R.string.boc_crcd_cash_psnDividedsecurityId));
            showLoadingDialog();
        }
    }

    /**
     * 过滤信用卡账户类型
     */
    private void filterCrcdAccountType() {
        accountTypeList.clear();
        // 中银系列信用卡 103
        accountTypeList.add(ApplicationConst.ACC_TYPE_ZHONGYIN);
        // 长城信用卡 104
        accountTypeList.add(ApplicationConst.ACC_TYPE_GRE);
        // 单外币信用卡 107
        accountTypeList.add(ApplicationConst.ACC_TYPE_SINGLEWAIBI);
    }

    /**
     * 过滤借记卡账户类型
     */
    private void filterDebitAccountType() {
        // 借记卡 119
        accountTypeList.clear();
        accountTypeList.add(ApplicationConst.ACC_TYPE_BRO);
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_crcd_cash_installment_title);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    //用户协议跳转
    class MyClickableSpan extends ClickableSpan {
        @Override
        public void onClick(View widget) {
            start(new CrcdCashServiceBureauFragment(true, ApplyAccountModel.APPLY_ENTY));
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }
    }

    /**
     * 选择信用卡付款账户
     */
    private void selectCreditCard() {
        accountBeanListTemp = ApplicationContext.getInstance().getChinaBankAccountList(accountTypeList);
        //没有卡时，跳转无账户fragment
        if (null == accountBeanListTemp) {
            start(new CashNoAccountFragment());
            return;
        }
        if (null == mSelectAccoutFragment) {
            mSelectAccoutFragment = CashAccountSelectFragment.newInstance(accountTypeList);
        }
        startForResult(mSelectAccoutFragment, REQUEST_CODE_SELECT_CREDIT_ACCOUNT);
    }

    /**
     * 选择借记卡收款账户
     */
    private void selectDebitCard() {
        accountBeanListTemp = ApplicationContext.getInstance().getChinaBankAccountList(accountTypeList);
        //没有卡时，跳转无账户fragment
        if (accountBeanListTemp.isEmpty()) {
            start(new CashNoAccountFragment());
        } else {
            startForResult(SelectAccoutFragment.newInstance(accountTypeList), REQUEST_CODE_SELECT_DEBIT_ACCOUNT);
        }
    }

    /**
     * 从信用卡列表选择fragment的参数返回处理函数
     *
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data        从fragment返回的数据
     */
    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode != SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT || null == data) {
            return;
        }
        //信用卡账户选择回调函数
        if (requestCode == REQUEST_CODE_SELECT_CREDIT_ACCOUNT) {
            AccountBean creditAccountBean = data.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
            String moneyBalance = data.getString(CashAccountSelectFragment.ACCOUNT_SELECT_BALANCE);
            String creditAccountId = creditAccountBean != null ? creditAccountBean.getAccountId() : null;
            String creditAccountNumber = creditAccountBean != null ? creditAccountBean.getAccountNumber() : null;

            cashInstallmentViewModel.setAvailableBalance(moneyBalance);
            cashInstallmentViewModel.setFromAccountId(creditAccountId);
            cashInstallmentViewModel.setFromCardNo(creditAccountNumber);
            editMoneyInputWidget.setContentHint(getString(R.string.boc_crcd_input_money_hint1) + MoneyUtils.transMoneyFormat(moneyBalance, "001") +
                    getString(R.string.boc_crcd_input_money_hint2) + MoneyUtils.transMoneyFormat(getString(
                    R.string.boc_crcd_div_amount_lower), "001"));
            mCreditChoiceAccount.setChoiceTextContent(NumberUtils.formatCardNumber(creditAccountNumber));
            //当两次选择不同的信用卡账户时，需要将提现金额置空
            if (!creditAccountId.equals(accountIdPre)) {
                //将提款金额置空
                MoneyInputTextView temp;
                temp = editMoneyInputWidget.getContentMoneyEditText();
                temp.clearText();
                //手续费试算恢复到初始状态
                reinitDivCharge();
                cashInstallmentViewModel.setDivAmount(null);
                cashInstallmentViewModel.setDivRate(null);
                cashInstallmentViewModel.setDivCharge(null);
                cashInstallmentViewModel.setFirstPayAmount(null);
                cashInstallmentViewModel.setPerPayAmount(null);
                cashInstallmentViewModel.setFirstCharge(null);
                cashInstallmentViewModel.setPerCharge(null);
            }
            accountIdPre = creditAccountId;
        } else {  //借记卡账户选择回调函数
            AccountBean debitAccountBean = data.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
            String debitAccountId = debitAccountBean != null ? debitAccountBean.getAccountId() : null;
            String debitAccountNumber = debitAccountBean != null ? debitAccountBean.getAccountNumber() : null;
            cashInstallmentViewModel.setToAccountId(debitAccountId);
            cashInstallmentViewModel.setToCardNo(debitAccountNumber);
            mDebitChoiceAccount.setChoiceTextContent(NumberUtils.formatCardNumber(debitAccountNumber));
        }
    }

    /**
     * 分期期数选择框显示
     */
    private void showDivPeriodDialog() {
        List<String> yearTypeList = new ArrayList<String>() {};
        yearTypeList.add(getResources().getString(R.string.boc_crcd_div_period_3));
        yearTypeList.add(getResources().getString(R.string.boc_crcd_div_period_6));
        yearTypeList.add(getResources().getString(R.string.boc_crcd_div_period_9));
        yearTypeList.add(getResources().getString(R.string.boc_crcd_div_period_12));
        yearTypeList.add(getResources().getString(R.string.boc_crcd_div_period_18));
        yearTypeList.add(getResources().getString(R.string.boc_crcd_div_period_24));
        final String yearListShow[] = getResources().getStringArray(R.array.boc_crcd_div_period_show);
        final String yearListModel[] = getResources().getStringArray(R.array.boc_crcd_div_period_model);
        if (null == divPeriodDialog) {
            divPeriodDialog = new SelectStringListDialog(mContext);
            divPeriodDialog.setListData(yearTypeList, false);//false：居中显示
            divPeriodDialog.setOnSelectListener(new SelectListDialog.OnSelectListener<String>() {
                @Override
                public void onSelect(int position, String model) {
                    mDivPeriod.setChoiceTextContent(yearListShow[position]);
                    cashInstallmentViewModel.setDivPeriod(yearListModel[position]);
                    divPeriodDialog.dismiss();
                    reinitDivCharge();
                }
            });
        }
        divPeriodDialog.show();
    }

    /**
     * 设置标题为根据内容大小显示
     */
    public void setNameWidth() {
        ViewGroup.LayoutParams lpCredit = mCreditChoiceAccount.getLayoutParams();
        lpCredit.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        mCreditChoiceAccount.setLayoutParams(lpCredit);

        ViewGroup.LayoutParams lpDebit = mDebitChoiceAccount.getLayoutParams();
        lpDebit.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        mDebitChoiceAccount.setLayoutParams(lpDebit);
    }

    /**
     * 请求安全因子返回成功函数
     *
     * @param securityFactorModel
     */
    public void onQrySecurityFactorSuccess(SecurityFactorModel securityFactorModel) {
        //默认的安全工具
        CombinListBean combinBean = SecurityVerity.getInstance(getActivity()).
                getDefaultSecurityFactorId(securityFactorModel);
        cashInstallmentViewModel.set_combinId(combinBean.getId());
        cashInstallmentViewModel.setCombinName(combinBean.getName());
        getPresenter().qryCrcdApplyCashDivPre(cashInstallmentViewModel);
    }

    /**
     * 请求现金分期 费用试算 成功回调函数
     */
    public void onQueryCashDivCommissionChargeSuccess(CashInstallmentViewModel cashInstallmentViewModel) {
        closeProgressDialog();
        isChargeSuccess = true;
        charge.setVisibility(View.VISIBLE);
        BigDecimal firstPayAmountBD = new BigDecimal(cashInstallmentViewModel.getFirstPayAmount());
        BigDecimal perPayAmountBD = new BigDecimal(cashInstallmentViewModel.getPerPayAmount());
        BigDecimal divCharge = new BigDecimal(cashInstallmentViewModel.getDivCharge());
        //分期收取
        if (cashInstallmentViewModel.getChargeMode() == "1") {
            BigDecimal firstPayChargeBD = new BigDecimal(cashInstallmentViewModel.getFirstCharge());
            BigDecimal perPayChargeBD = new BigDecimal(cashInstallmentViewModel.getPerCharge());
            //手续费率
            divChargeRate.updateData(getResources().getString(R.string.boc_crcd_div_charge_rate),
                    getPercentValue(cashInstallmentViewModel.getDivRate()));
            //首期应还
            firstPayAmount.updateData(getResources().getString(R.string.boc_crcd_first_pay_amount),
                    MoneyUtils.transMoneyFormat(firstPayAmountBD.add(firstPayChargeBD).toString(), "001")
                            + getString(R.string.boc_crcd_div_charge_show1) +
                            MoneyUtils.transMoneyFormat(cashInstallmentViewModel.getFirstCharge(), "001")
                            + getString(R.string.boc_crcd_div_charge_show2));
            //其余每期应还
            perPayAmount.updateData(getResources().getString(R.string.boc_crcd_per_pay_amount),
                    MoneyUtils.transMoneyFormat(perPayAmountBD.add(perPayChargeBD).toString(), "001")
                            + getString(R.string.boc_crcd_div_charge_show1) +
                            MoneyUtils.transMoneyFormat(cashInstallmentViewModel.getPerCharge(), "001")
                            + getString(R.string.boc_crcd_div_charge_show2));
        } else {//一次性收取
            //手续费率
            divChargeRate.updateData(getResources().getString(R.string.boc_crcd_div_charge_rate),
                    getPercentValue(cashInstallmentViewModel.getDivRate()));
            //首期应还
            firstPayAmount.updateData(getResources().getString(R.string.boc_crcd_first_pay_amount),
                    MoneyUtils.transMoneyFormat(firstPayAmountBD.add(divCharge).toString(), "001")
                            + getString(R.string.boc_crcd_div_charge_show1) +
                            MoneyUtils.transMoneyFormat(cashInstallmentViewModel.getDivCharge(), "001")
                            + getString(R.string.boc_crcd_div_charge_show2));
            //其余每期应还
            perPayAmount.updateData(getResources().getString(R.string.boc_crcd_per_pay_amount),
                    MoneyUtils.transMoneyFormat(cashInstallmentViewModel.getPerPayAmount(), "001"));
        }
    }

    /**
     * 请求现金分期 费用试算 失败回调函数
     */
    public void onQueryCashDivCommissionChargeFail(CashInstallmentViewModel cashInstallmentViewModel) {
        closeProgressDialog();
        cashInstallmentViewModel.setFirstPayAmount(null);
        cashInstallmentViewModel.setPerPayAmount(null);
        cashInstallmentViewModel.setDivRate(null);
        cashInstallmentViewModel.setFirstCharge(null);
        cashInstallmentViewModel.setPerCharge(null);
        isChargeSuccess = false;
        charge.setVisibility(View.GONE);
    }

    /**
     * 预交易成功回调函数
     *
     * @param verifyBean
     * @param conversationId
     */
    public void onCrcdApplyCashDivPreSuccess(VerifyBean verifyBean, String conversationId) {
        closeProgressDialog();
        cashInstallmentViewModel.setConversationId(conversationId);
        start(CashInstallmentConfirmFragment.newInstance(cashInstallmentViewModel, verifyBean));
    }

    /**
     * 预交易失败回调函数
     */
    public void onCrcdApplyCashDivPreFail(){
        closeProgressDialog();
    }

    /**
     * 数值加百分号%
     *
     * @param value
     * @return
     */
    public static String getPercentValue(String value) {
        NumberFormat percentInstance = NumberFormat.getPercentInstance();
        percentInstance.setMaximumFractionDigits(3);
        try {
            return percentInstance.format(Float.parseFloat(value));
        } catch (Exception e) {
            return "--";
        }
    }

    /**
     * 重新进入界面调用次方法
     */
    @Override
    public void reInit() {
        super.reInit();
        MoneyInputTextView temp;
        temp = editMoneyInputWidget.getContentMoneyEditText();
        temp.clearText();
        editMoneyInputWidget.setmContentMoneyEditText("");
        editMoneyInputWidget.setContentHint(getString(R.string.boc_crcd_cash_money_lower_hint) +
          MoneyUtils.transMoneyFormat(getString(R.string.boc_crcd_div_amount_lower), "001"));
        mCreditChoiceAccount.setChoiceTextContent("");
        mDebitChoiceAccount.setChoiceTextContent("");

        reinitDivCharge();
        mDivPeriod.setChoiceTextContent("");
        cbAgreement.setChecked(false);
        cashInstallmentViewModel = new CashInstallmentViewModel();
        cashInstallmentViewModel.setCurrency("001");
        accountIdPre = null;
        inputMoneyTemp = "";
        chargeModePre = -1;
        isChargeSuccess = false;
        //请选择
        mCreditChoiceAccount.setChoiceTextContent(getString(R.string.boc_crcd_please_select));
        mDebitChoiceAccount.setChoiceTextContent(getString(R.string.boc_crcd_please_select));
        mDivPeriod.setChoiceTextContent(getString(R.string.boc_crcd_please_select));
    }

    /**
     * 手续费试算模块恢复到初始化状态
     */
    public void reinitDivCharge() {
        isChargeSuccess = false;
        selectGridViewList.get(0).setSelected(false);
        selectGridViewList.get(1).setSelected(false);
        mSelectChargePattern.getAdapter().notifyDataSetChanged();
        charge.setVisibility(View.GONE);
        cashInstallmentViewModel.setFirstPayAmount(null);
        cashInstallmentViewModel.setPerPayAmount(null);
        cashInstallmentViewModel.setDivRate(null);
        cashInstallmentViewModel.setFirstCharge(null);
        cashInstallmentViewModel.setPerCharge(null);
    }

    public void backWithResult() {
        Bundle bundle = new Bundle();
        //TODO 替换成自己需要返回的数据
        bundle.putParcelable(PublicConst.RESULT_DATA, null);
        setFramgentResult(Activity.RESULT_OK, bundle);
        pop();
    }

}