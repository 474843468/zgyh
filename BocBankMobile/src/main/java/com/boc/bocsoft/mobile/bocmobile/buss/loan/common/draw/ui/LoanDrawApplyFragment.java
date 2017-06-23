package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.draw.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANCycleLoanMinAmountQuery.PsnLOANCycleLoanMinAmountQueryParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery.PsnLOANListEQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayeeAcountCheck.PsnLOANPayeeAcountCheckParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayeeAcountCheck.PsnLOANPayeeAcountCheckResult;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.MClickableSpan;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.webView.ContractFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.draw.model.LoanDrawApplyContractContent;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.draw.model.LoanDrawApplyVerifyReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.draw.model.LoanDrawApplyVerifyRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.draw.presenter.LoanDrawApplayPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.LoanCosnt;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.QueryAllChinaBankAccountRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.ui.AccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.ui.AccoutFragment.AccountType;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.ReloanQryConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.utils.DataUtils;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * 贷款管理-循环类贷款-提款页面fragment
 * Created by liuzc on 2016/8/24.
 */
public class LoanDrawApplyFragment extends BussFragment implements LoanDrawApplyContract.DrawView {
    //获取会话ID成功后的下一步操作
    enum NEXT_STEP{
        getDefPayeeAccount,  //获取默认收款账户
        gotoSelectAccount   //进入账户选择页面
    }

    private static final String TAG = "LoanDrawApplyFragment";
    /**url*/
    private static final String ELOANCONTRACT_URL = "file:///android_asset/webviewcontent/cycleloandrawapply/license.html"; //用款合同内容

    private String _combinId;
    private String combin_name;

    //可用额度
    private TextView tvAvailableCredit;

    //提款金额
    private EditMoneyInputWidget edtDrawAmount;

    //收款账户
    private EditChoiceWidget edtReceiptAccount;

    //合同条款
    private ImageButton imgbSelect;
    private TextView tvAgreement;

    //下一步按钮
    private Button btnNext;
    //可点击的Span
    private MClickableSpan clickableSpanFir;

    private boolean bAvailable;

    private SecurityVerity securityVerity;

    private String conversationId = null;

    //页面根视图
    private View mRoot;
    private LoanDrawApplayPresenter drawPresenter;
    /**用款传递对象*/
    private PsnLOANListEQueryResult.PsnLOANListEQueryBean mQryResultBean = null;
    private String accountId;
    private String account;

    private String repayPeriodCode = null; //还款周期

    private String cardNumber = null;//还款账户卡号

    private NEXT_STEP nextStep = NEXT_STEP.getDefPayeeAccount;

    private String minUseMoneyAmount; //最小用款金额

    LoanDrawApplyVerifyReq drawApplyVerifyParams = null; //请求参数；

    private boolean bCheckAccountSuccess = true; //收款账户检查是否成功
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRoot = mInflater.inflate(R.layout.boc_fragment_loan_common_drawapply, null);
        return mRoot;
    }

    @Override
    protected void titleLeftIconClick() {
        hideSoftInput();
        super.titleLeftIconClick();
    }

    @Override
    protected void titleRightIconClick() {
        super.titleRightIconClick();
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_eloan_draw_pagename);
    }

    @Override
    public void beforeInitView() {
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    public void initView() {
        drawPresenter = new LoanDrawApplayPresenter(this);

        showLoadingDialog(false);
        drawPresenter.creatConversation();
        securityVerity = SecurityVerity.getInstance(getActivity());

        //组件初始化
        tvAvailableCredit = (TextView) mRoot.findViewById(R.id.availableCredit);

        edtDrawAmount = (EditMoneyInputWidget) mRoot.findViewById(R.id.drawAmount);
        edtReceiptAccount = (EditChoiceWidget) mRoot.findViewById(R.id.receiptAccount);

        imgbSelect = (ImageButton) mRoot.findViewById(R.id.chbSelect);
        tvAgreement = (TextView) mRoot.findViewById(R.id.tvAgreement);

        btnNext = (Button) mRoot.findViewById(R.id.next);

    }

    @Override
    public void initData() {
        mQryResultBean = (PsnLOANListEQueryResult.PsnLOANListEQueryBean)getArguments()
                .getSerializable(EloanConst.ELON_DRAW);
        repayPeriodCode = getArguments().getString(EloanConst.LOAN_REPAY_PERIOD);
        cardNumber = getArguments().getString(LoanCosnt.LOAN_REPYNUM);
        minUseMoneyAmount = getArguments().getString(ReloanQryConst.KEY_MIN_USEMONEY_AMOUNT);
        if(StringUtils.isEmptyOrNull(minUseMoneyAmount)){
            minUseMoneyAmount = "1000";
        }

        //第一次提款，页面初始值设置
        String sTotal = String.format(" %s%s", DataUtils.getCurrencyDescByLetter(mContext, mQryResultBean.getCurrencyCode()),
                MoneyUtils.transMoneyFormat(mQryResultBean.getLoanCycleAvaAmount(), mQryResultBean.getCurrencyCode()));
        SpannableString spannableStringFir1 = new SpannableString(sTotal);
        spannableStringFir1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.boc_text_color_money_count)), 0,
                sTotal.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvAvailableCredit.setText(String.format("%s:", getString(R.string.boc_facility_available_quota)));
        tvAvailableCredit.append(spannableStringFir1);


        edtDrawAmount.getContentMoneyEditText().setHint(getString(R.string.boc_eloan_draw_input));
        edtDrawAmount.getContentMoneyEditText().setTextColor(Color.RED);
        edtDrawAmount.setEditWidgetTitle(getString(R.string.boc_details_drawamount));
        edtDrawAmount.setMaxLeftNumber(11);
        edtDrawAmount.setMaxRightNumber(2);
        edtDrawAmount.setCurrency(mQryResultBean.getCurrencyCode());

        edtReceiptAccount.setChoiceTextContent(getString(R.string.boc_eloan_choice));

        String CONTRACT = getResources().getString(R.string.boc_loan_reloan_contract);
        SpannableString spannableStringFir = new SpannableString(CONTRACT);
        clickableSpanFir = new MClickableSpan(CONTRACT, getContext());
        clickableSpanFir.setColor(getResources().getColor(R.color.boc_text_color_red));
        spannableStringFir.setSpan(clickableSpanFir, 0,
                CONTRACT.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvAgreement.setText(getResources().getString(R.string.boc_agreement_part1));
        tvAgreement.append(spannableStringFir);
        tvAgreement.append(String.format("%s%s",
                ",",
                getResources().getString(R.string.boc_agreement_part2)));
        tvAgreement.setMovementMethod(LinkMovementMethod.getInstance());
        tvAgreement.setLongClickable(false);
    }

    @Override
    public void setListener() {
        edtReceiptAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StringUtils.isEmptyOrNull(conversationId)){
                    showLoadingDialog(false);
                    nextStep = NEXT_STEP.gotoSelectAccount;
                    drawPresenter.creatConversation();
                }
                else{
                    gotoSelectAccountPage();
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judge();
            }
        });

        clickableSpanFir.setListener(new MClickableSpan.OnClickSpanListener() {
            @Override
            public void onClickSpan() {
                //初始化合同展示页面
                openContract();

            }
        });
        imgbSelect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                imgbSelect.setSelected(!imgbSelect.isSelected());
            }
        });
    }

    private void openContract(){
        String contentMoney = edtDrawAmount.getContentMoney();
        //未输入提款金额
        if(StringUtils.isEmptyOrNull(contentMoney)){
            showErrorDialog(getResources().getString(R.string.boc_loan_draw_amount_no_input2));
            return;
        }

        //未选择收款账户
        if(StringUtils.isEmptyOrNull(edtReceiptAccount.getChoiceTextContent()) ||
                edtReceiptAccount.getChoiceTextContent().equals(getString(R.string.boc_common_select))){
            showErrorDialog(getResources().getString(R.string.boc_loan_select_accept_account2));
            return;
        }

        start(ContractFragment.newInstance(ELOANCONTRACT_URL, getContractModel()));
    }

    private Object getContractModel(){
        LoanDrawApplyContractContent model = new LoanDrawApplyContractContent();
        model.setBorrower(ApplicationContext.getInstance().getUser().getCustomerName());
        model.setCDCard(NumberUtils.formatCardFor6Eight4(ApplicationContext.getInstance().getUser().getIdentityNumber()));
        String cdType = ApplicationContext.getInstance().getUser().getIdentityType();
        if (ApplicationContext.getInstance().getUser().getIdentityType().equals("1")
                || ApplicationContext.getInstance().getUser().getIdentityType().equals("01")) {
            cdType = getString(R.string.boc_identity_type_id_card);
        }
        model.setCDType(cdType);

        model.setLender(getString(R.string.boc_corp_name));
        model.setLoanAccount(NumberUtils.formatCardNumber(mQryResultBean.getAccountNumber()));
        model.setLoanAmount(MoneyUtils.transMoneyFormat(edtDrawAmount.getContentMoney(), mQryResultBean.getCurrencyCode()));
        model.setLoanCurrency(DataUtils.getCurrencyDescByLetter(mContext, mQryResultBean.getCurrencyCode()));
        model.setLoanPeriod(mQryResultBean.getLoanCycleLifeTerm());
        model.setRecivePeriod(getString(R.string.boc_period_month));
        model.setReciver(model.getBorrower());
        model.setReciverAccount(NumberUtils.formatCardNumber(account));
        model.setRepayment(model.getBorrower());
        model.setRepaymentAccount(NumberUtils.formatCardNumber(mQryResultBean.getLoanCycleRepayAccount()));
        return model;
    }

    private void gotoSelectAccountPage(){
        AccoutFragment accoutFragment = new AccoutFragment();
        accoutFragment.setAccountType(AccountType.SELECT_COLLECTIONACCOUNT);
        accoutFragment.setConversationId(conversationId);
        accoutFragment.setIsBocELoan(false, mQryResultBean.getCurrencyCode());
        Bundle bundle = new Bundle();
        bundle.putString("type", "draw");
        bundle.putString("commonType", "reloan");
        if(!StringUtils.isEmptyOrNull(cardNumber) && bCheckAccountSuccess){
            bundle.putString("currentAccount", cardNumber);
        }
        accoutFragment.setArguments(bundle);

        startForResult(accoutFragment, AccoutFragment.RequestCode);
    }

    //    getContentMoney    getEditWidgetContent
    private void judge() {
        String contentMoney = edtDrawAmount.getContentMoney();
        //未输入提款金额
        if(StringUtils.isEmptyOrNull(contentMoney)){
            showErrorDialog(getResources().getString(R.string.boc_loan_draw_amount_no_input));
            return;
        }

        //额度是否充足
        Boolean bMoneyEnough = new BigDecimal(MoneyUtils.getNormalMoneyFormat(mQryResultBean.getLoanCycleAvaAmount())).compareTo(
                new BigDecimal(MoneyUtils.getNormalMoneyFormat(contentMoney))) >= 0;
        //用款金额大于额度
        if(!bMoneyEnough){
            showErrorDialog(getResources().getString(R.string.boc_loan_draw_amount_biggerthan_max));
            return;
        }

        //用款小于下限
        if(!StringUtils.isEmptyOrNull(minUseMoneyAmount)){
            if(contentMoney != null &&
                    (new BigDecimal(edtDrawAmount.getContentMoney())).compareTo(
                            new BigDecimal(MoneyUtils.getNormalMoneyFormat(minUseMoneyAmount))) < 0){
                showErrorDialog(getString(R.string.boc_loan_draw_amount_smallerthan_min,
                        MoneyUtils.transMoneyFormat(minUseMoneyAmount, mQryResultBean.getCurrencyCode())));
                return;
            }
        }

        //未选择收款账户
        if(edtReceiptAccount.getChoiceTextContent() == null ||
                edtReceiptAccount.getChoiceTextContent().equals(getString(R.string.boc_common_select))){
            showErrorDialog(getResources().getString(R.string.boc_loan_select_accept_account));
            return;
        }

        //未勾选条款
        if(!imgbSelect.isSelected()){
            showErrorDialog(getResources().getString(R.string.boc_loan_select_and_agree));
            return;

        }

        /**
         * 先完成PsnLOANCycleLoanMinAmountQuery（查询个人循环贷款最低放款金额），该接口并无实际作用，
         * 如果不请求不接口直接去请求用款，后台会报“您的操作页面已经超时”，后台设计如此...
         */

        PsnLOANCycleLoanMinAmountQueryParams params = new PsnLOANCycleLoanMinAmountQueryParams();
        params.setConversationId(conversationId);
        drawPresenter.queryLoanCycleMinAmount(params);
        showLoadingDialog(false);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        drawPresenter.unsubscribe();
    }

    @Override
    public void obtainConversationSuccess(String conversationId) {
        Log.i(TAG, "获取会话成功！");
        this.conversationId = conversationId;
        drawPresenter.setConversationId(conversationId);

        if(nextStep == NEXT_STEP.getDefPayeeAccount){
            //查询中行所有帐户列表
            List<String> list = new ArrayList<String>();
            list.add("119"); //借记卡
            list.add("101");//普活101
            list.add("188");//活一本188
            drawPresenter.setAccountType(list);
            drawPresenter.queryAllChinaBankAccount();
            showLoadingDialog(false);
        }
        else{
            gotoSelectAccountPage();
        }
    }

    @Override
    public void obtainConversationFail(ErrorException e) {
        Log.i(TAG, "获取会话失败！");
        //继续请求
        drawPresenter.creatConversation();
    }


    @Override
    public void obtainSecurityFactorSuccess(SecurityFactorModel result) {
        Log.i(TAG, "-------->获取安全因子成功！");
        _combinId = securityVerity.getDefaultSecurityFactorId(result).getId();
        combin_name = securityVerity.getDefaultSecurityFactorId(result).getName();

        closeProgressDialog();
        buildVerifyReq();
    }

    @Override
    public void obtainSecurityFactorFail(ErrorException e) {
        Log.i(TAG, "-------->获取安全因子失败！");
        closeProgressDialog();
    }

    @Override
    public void drawApplyVerifySuccess(LoanDrawApplyVerifyRes result) {
        Log.i(TAG, "提款预交易成功！");
        closeProgressDialog();
        bAvailable = securityVerity.confirmFactor(result.getFactorList());
        LoanDrawApplyConfirmInfoFragment drawConfirmInfoFragment = new LoanDrawApplyConfirmInfoFragment();
        drawConfirmInfoFragment.setbAvailable(bAvailable);
        drawConfirmInfoFragment.setConversationId(conversationId);
        drawConfirmInfoFragment.set_combinId(_combinId);
        drawConfirmInfoFragment.set_combinName(combin_name);
        drawConfirmInfoFragment.setApplyVerifyReq(drawApplyVerifyParams);
        drawConfirmInfoFragment.setRepayCardNumber(cardNumber);
        drawConfirmInfoFragment.setAccountID(accountId);
        drawConfirmInfoFragment.setRepayPeriod(repayPeriodCode);
        start(drawConfirmInfoFragment);
    }

    @Override
    public void drawApplyVerifyFail(ErrorException e) {
        Log.i(TAG, "提款预交易失败！");
        closeProgressDialog();
    }

    @Override
    public void obtainAllChinaBankAccountSuccess(List<QueryAllChinaBankAccountRes> res) {
        Log.i(TAG, "------------>获取账户列表成功!"+mQryResultBean.getLoanCycleRepayAccount());
        boolean bSuccess = false;
        if (res.size()>0){
            for (QueryAllChinaBankAccountRes  queryAllChinaBankAccountRes: res){
                Log.i(TAG, "------------>获取账户列表成功!---->没有匹配的"+queryAllChinaBankAccountRes.getAccountNumber());
                String accountNumer = queryAllChinaBankAccountRes.getAccountNumber();
                if (!StringUtils.isEmptyOrNull(accountNumer)
                    && (accountNumer.equals(mQryResultBean.getLoanCycleRepayAccount()) || accountNumer.equals(cardNumber))){
                    account = queryAllChinaBankAccountRes.getAccountNumber();
                    accountId = queryAllChinaBankAccountRes.getAccountId()+"";
                    bSuccess = true;
                    Log.i(TAG, "------------>获取账户列表成功!----->有匹配的");
                    break;
                }else{
                    Log.i(TAG, "------------>获取账户列表成功!---->没有匹配的");
                    edtReceiptAccount.setChoiceTextContent(getString(R.string.boc_eloan_choice));
                }
            }
        }else{
            Log.i(TAG, "------------>获取账户列表成功!------->为空");
            edtReceiptAccount.setChoiceTextContent(getString(R.string.boc_eloan_choice));
        }

        if(bSuccess){
            //进行收款账户检查
            PsnLOANPayeeAcountCheckParams checkParams = new PsnLOANPayeeAcountCheckParams();
            checkParams.setConversationId(conversationId);
            checkParams.setCurrencyCode(transforCodeFormat(mQryResultBean.getCurrencyCode()));
            checkParams.setAccountId(accountId);

            drawPresenter.checkPayeeAccount(checkParams);
        }
        else{
            closeProgressDialog();
            edtReceiptAccount.setChoiceTextContent(getString(R.string.boc_eloan_choice));
        }

    }

    /**
     * 转换币种格式，由CNY转成001格式
     * @param currencyCode
     * @return
     */
    private String transforCodeFormat(String currencyCode){
        String mCode = "";
        if (!StringUtils.isEmptyOrNull(currencyCode)) {
            if (Pattern.compile("(?i)[A-Z]").matcher(currencyCode).find()) {
                mCode = PublicCodeUtils.getCurrencyCode(getContext(), currencyCode);
            }
            else{
                mCode = currencyCode;
            }
        }
        return mCode;
    }

    @Override
    public void obtainAllChinaBankAccountFail(ErrorException e) {
        Log.i(TAG, "------------>获取账户列表失败!");
        closeProgressDialog();
        edtReceiptAccount.setChoiceTextContent(getString(R.string.boc_eloan_choice));
    }


    @Override
    public void queryLoanCycleMinAmountSuccess(String reuslt) {
        drawPresenter.getSecurityFactor();
    }

    @Override
    public void queryLoanCycleMinAmountFail(ErrorException e) {
        closeProgressDialog();
        showErrorDialog(e.getErrorMessage());
    }

    @Override
    public void doPayeeAccountCheckSuccess(PsnLOANPayeeAcountCheckResult res) {
        if(res != null && res.getCheckResult() != null && res.getCheckResult().size() > 0){
            if(res.getCheckResult().get(0) != null && (res.getCheckResult().get(0).equals("01"))){
                //检查成功
                edtReceiptAccount.setChoiceTextContent(NumberUtils.formatCardNumber(account));
                closeProgressDialog();
                bCheckAccountSuccess = true;
                return;
            }
        }

        //检查失败
        bCheckAccountSuccess = false;
        closeProgressDialog();
        edtReceiptAccount.setChoiceTextContent(getString(R.string.boc_eloan_choice));
    }

    @Override
    public void doPayeeAccountCheckFail(ErrorException e) {
        bCheckAccountSuccess = false;
        closeProgressDialog();
        edtReceiptAccount.setChoiceTextContent(getString(R.string.boc_eloan_choice));
    }

    @Override
    public void setPresenter(LoanDrawApplyContract.Presenter presenter) {
    }

    /**构造预交易上送参数
     *
     */
    private void buildVerifyReq(){
        if(drawApplyVerifyParams == null){
            drawApplyVerifyParams = new LoanDrawApplyVerifyReq();
        }

        drawApplyVerifyParams.setConversationId(conversationId);
        drawApplyVerifyParams.set_combinId(_combinId);

        drawApplyVerifyParams.setLoanType(mQryResultBean.getLoanType());
        drawApplyVerifyParams.setAvailableAmount(new BigDecimal(mQryResultBean.getLoanCycleAvaAmount()));
        drawApplyVerifyParams.setCurrencyCode(mQryResultBean.getCurrencyCode());
        drawApplyVerifyParams.setAmount(new BigDecimal(MoneyUtils.getNormalMoneyFormat(edtDrawAmount.getContentMoney())));
        drawApplyVerifyParams.setLoanRate(mQryResultBean.getLoanCycleRate());
        drawApplyVerifyParams.setLoanCycleDrawdownDate(mQryResultBean.getLoanCycleDrawdownDate());
        drawApplyVerifyParams.setToAccountId(accountId);
        drawApplyVerifyParams.setLoanCycleToActNum(account);
        drawApplyVerifyParams.setPayAccount(mQryResultBean.getLoanCycleRepayAccount());
        drawApplyVerifyParams.setLoanActNum(mQryResultBean.getAccountNumber());
        drawApplyVerifyParams.setLoanPeriod(mQryResultBean.getLoanCycleLifeTerm());
        drawApplyVerifyParams.setLoanCycleMatDate(mQryResultBean.getLoanCycleMatDate());
        drawApplyVerifyParams.setPayType(mQryResultBean.getInterestType());
        drawApplyVerifyParams.setPayCycle(repayPeriodCode);

        drawPresenter.setVerifyReq(drawApplyVerifyParams);
        //点击下一步按钮，跳转到提款确认页
        drawPresenter.drawApplyVerify();
        showLoadingDialog(false);
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == AccoutFragment.ResultCode) {
            if (data != null) {
                Log.i(TAG, "-------->account" + data.get("account").toString());
                edtReceiptAccount.setChoiceTextContent
                        (NumberUtils.formatCardNumber(data.get("account").toString()));
                account = data.get("account").toString();
                accountId = data.get("accountId").toString();
            }
        }
    }

}
