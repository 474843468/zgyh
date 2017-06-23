package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.singleprepay.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANRepayCount.PsnELOANRepayCountParams;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANRepayCount.PsnELOANRepayCountResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery.PsnLOANListEQueryResult;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANPayerAcountCheck.PsnLOANPayerAcountCheckParams;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.Content;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.SelectGridView;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.model.PrepayAccountDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.QueryAllChinaBankAccountRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.RepaymentAccountCheckRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.ui.AccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanAccountListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.singleprepay.model.SinglePrepaySubmitReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.singleprepay.presenter.SinglePrepaySubmitPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.utils.DataUtils;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 贷款管理-中银E贷-单笔提前还款Fragment
 * 页面初始化后请求数据逻辑： 请求会话ID -> 查找默认账户 -> 账户检查 -> 获取账户详情
 * Created by liuzc on 2016/9/2.
 */
public class SinglePrepaySubmitFragment extends BussFragment implements SinglePrepaySubmitContract.View, EditMoneyInputWidget.MoneyChangeListenerInterface {
    //获取会话ID成功后的下一步操作
    enum NEXT_STEP{
        getDefRepayAccount,  //获取默认还款账户
        gotoSelectAccount   //进入账户选择页面
    }

    private static final String TAG = "SPrepaySubmitFrag";

    private View mRootView;

    private LinearLayout llyBackground; //布局背景
    private TextView tvPrepayRemain;//剩余未还本金
    private SelectGridView singleSelect;;//提前还款本金类型
    private EditMoneyInputWidget edtMPrepayAmount;//提前还款本金

    private TextView tvTotalAmount;  //还款总额
    private TextView tvIntAndCharge;  //利息 + 手续费
    private LinearLayout llyInclude; //还款金额layout
    private LinearLayout llyRemainAvl; //可用余额layout
    private TextView tvRemainAvl; //账户可用余额
    private EditChoiceWidget edtCRepaymentAccount;//还款账户
    private Button btnNext;//下一步按钮

    private SinglePrepaySubmitPresenter mPresenger;

    private  ArrayList<Content> singleSelectList;  //还款类型选择数据

    //还款账户
    private String accountId = null;
    private String account = null;

    private String remainCapital = null;
    private String totalCapInt = null; //本息合计（不含手续费）

    private String availableBalance = null; //卡内可用余额
    private String charges = null; //提前还款手续费

    private String conversationID = null; //会话ID

    //上层页面传递过来的数据
    private EloanAccountListModel.PsnLOANListEQueryBean mPrepayDetailModel;
    private String QuoteNo; //额度编号
    private String mPrepayAccount; //还款账户

    private boolean bGotoConfirmPageFormCalcCharges = false; //是否计算完手续费直接进入确认页

    private NEXT_STEP nextStep = NEXT_STEP.getDefRepayAccount;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_eloan_single_prepay_submit, null);
        return mRootView;
    }

    @Override
    protected void titleLeftIconClick() {
        hideSoftInput();
        super.titleLeftIconClick();
    }


    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_eloan_prepay_pagename);
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
    public void initView() {
        //初始化组件
        llyBackground = (LinearLayout) mRootView.findViewById(R.id.llyBackground);
        tvPrepayRemain = (TextView) mRootView.findViewById(R.id.prepayRemain);
        singleSelect = (SelectGridView) mRootView.findViewById(R.id.viewSelect);
        edtMPrepayAmount = (EditMoneyInputWidget) mRootView.findViewById(R.id.prepay);
        edtMPrepayAmount.setMaxLeftNumber(11);
        edtMPrepayAmount.setMoneyChangeListener(this);
        edtCRepaymentAccount = (EditChoiceWidget) mRootView.findViewById(R.id.repaymentAccount);
        btnNext = (Button) mRootView.findViewById(R.id.next);
        tvTotalAmount = (TextView) mRootView.findViewById(R.id.tvTotalAmount);
        tvIntAndCharge = (TextView) mRootView.findViewById(R.id.tvIntAndCharge);
        llyInclude = (LinearLayout) mRootView.findViewById(R.id.include);
        llyRemainAvl = (LinearLayout) mRootView.findViewById(R.id.llyRemainAvl);
        tvRemainAvl = (TextView) mRootView.findViewById(R.id.tvRemainAvl);
    }

    @Override
    public void initData() {
        mPrepayDetailModel = (EloanAccountListModel.PsnLOANListEQueryBean)getArguments()
                .getSerializable(EloanConst.ELON_PREPAY);

        //利息，四舍五入展示
        mPrepayDetailModel.setThisIssueRepayInterest(DataUtils.getFormatMoney(mPrepayDetailModel.getThisIssueRepayInterest()));
        QuoteNo = getArguments().getString(EloanConst.LOAN_QUOTENO);
        mPrepayAccount = getArguments().getString(EloanConst.PEPAY_ACCOUNT);

        mPresenger = new SinglePrepaySubmitPresenter(this);
        //初始化组件
        remainCapital=mPrepayDetailModel.getRemainCapital();
        String currencyCode = mPrepayDetailModel.getCurrencyCode();
        tvPrepayRemain.setText(String.format("%s %s",
                DataUtils.getCurrencyDescByLetter(mContext, currencyCode),
                MoneyUtils.transMoneyFormat(remainCapital, currencyCode)));

        initPrepayTypeView();

        edtMPrepayAmount.setmContentMoneyEditText(MoneyUtils.transMoneyFormat(remainCapital, currencyCode));
        updateCapitalAndInterest(mPrepayDetailModel.getRemainCapital(), mPrepayDetailModel.getThisIssueRepayInterest());
        edtCRepaymentAccount.setChoiceTextName(getString(R.string.boc_eloan_repaymentAccount));
        edtCRepaymentAccount.setChoiceTitleBold(true);
        edtCRepaymentAccount.setChoiceTextContent(getString(R.string.boc_eloan_choice));

        edtMPrepayAmount.setClearIconVisible(false);
        edtMPrepayAmount.setMoneyEditTextShowLocation(false); //文字默认居右反显
        edtMPrepayAmount.getContentMoneyEditText().setClickable(false);
        edtMPrepayAmount.setCurrency(mPrepayDetailModel.getCurrencyCode());
        edtMPrepayAmount.setMaxRightNumber(2);
        llyInclude.setVisibility(View.VISIBLE);
        edtMPrepayAmount.setEditWidgetTitle(getString(R.string.boc_eloan_prepay_capital));
        edtMPrepayAmount.setTitleTextBold(true);
        edtMPrepayAmount.setRightTextBold(true);
        edtMPrepayAmount.getContentMoneyEditText().setTextColor(getResources().getColor(R.color.boc_text_color_red));

        showLoadingDialog();
        mPresenger.creatConversation();
    }

    /**
     * 查询中行账户列表
     */
    private void queryAccount(){
        List<String> list = new ArrayList<String>();
        list.add("119");
        mPresenger.queryAllChinaBankAccount(list);
    }

    /**
     * 更新本金、利息描述
     * @param capital 本金
     * @param interest 利息
     */
    private void updateCapitalAndInterest(String capital, String interest){
        try{
            if(capital == null || interest == null){
                return;
            }

            String tempCharges = charges;
            if(StringUtils.isEmptyOrNull(charges)){
                tempCharges = "0";
            }
            //本息合计
            BigDecimal total = new BigDecimal(capital).add(new BigDecimal(interest)).add(new BigDecimal(tempCharges));
            String currencyCode = mPrepayDetailModel.getCurrencyCode(); //币种
            String sTotal = String.format(" %s %s", DataUtils.getCurrencyDescByLetter(mContext,
                    currencyCode), MoneyUtils.transMoneyFormat(total,currencyCode));


            //本息合计金额
            SpannableString spannableStringFir = new SpannableString(sTotal);
            spannableStringFir.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.boc_text_green_gray)), 0,
                    sTotal.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tvTotalAmount.setText(String.format("%s:", getString(R.string.boc_eloan_prepay_total_desc2)));
            tvTotalAmount.append(spannableStringFir);

            String sIntAndCharges = String.format("(%s%s, %s%s)",
                    getString(R.string.boc_eloan_contain_interest),
                    MoneyUtils.transMoneyFormat(interest, currencyCode),
                    getString(R.string.boc_eloan_fee),
                    DataUtils.getFormatCharges(MoneyUtils.transMoneyFormat(tempCharges, currencyCode)));
            tvIntAndCharge.setText(sIntAndCharges);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 初始化还款类型view
     */
    private void initPrepayTypeView(){
        String[] singleSelectData = {getString(R.string.boc_eloan_prepay_some), getString(R.string.boc_eloan_prepay_all)};
        singleSelectList = new ArrayList<Content>();
        // 初始化日期范围单选组件数据
        for (int i = 0; i < singleSelectData.length; i++) {
            Content item = new Content();
            item.setName(singleSelectData[i]);
            if (i == (singleSelectData.length - 1)) {
                item.setSelected(true);// 默认勾选最后一个选项
            }
            singleSelectList.add(item);
        }
        singleSelect.setData(singleSelectList);
    }

    @Override
    public void setListener() {
        singleSelect.setListener(new SelectGridView.ClickListener() {
            @Override
            public void setItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < singleSelectList.size(); i++) {
                    singleSelectList.get(i).setSelected(false);
                    if (i == position) {
                        singleSelectList.get(position).setSelected(true);
                    }
                }
                onClickItem(position);
            }
        });

        
        edtCRepaymentAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StringUtils.isEmptyOrNull(conversationID)){
                    showLoadingDialog(false);
                    nextStep = NEXT_STEP.gotoSelectAccount;
                    mPresenger.creatConversation();
                }
                else{
                    gotoSelectAccountPage();
                }
            }
        });
        
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(edtMPrepayAmount.getContentMoney())){
                    if (singleSelect.getSelectedItemPosition() == 1){ //全额提前还款
                        if (MoneyUtils.getNormalMoneyFormat(edtMPrepayAmount.getContentMoney())
                                .equals(MoneyUtils.getNormalMoneyFormat(MoneyUtils.transMoneyFormat(remainCapital, ApplicationConst.CURRENCY_CNY)))){
                            judge();
                        }else{
                            showErrorDialog(getResources().getString(R.string.boc_eloan_repay_capital_nochang));
                        }
                    }else {
                        if(new BigDecimal(MoneyUtils.getNormalMoneyFormat(edtMPrepayAmount.getContentMoney())).compareTo(new BigDecimal(0)) <= 0){
                            showErrorDialog(getResources().getString(R.string.boc_eloan_repay_non_zero));
                            return;
                        }
                        Boolean bRepayMore = (new BigDecimal(MoneyUtils.getNormalMoneyFormat(edtMPrepayAmount.getContentMoney())).compareTo(new BigDecimal(
                                MoneyUtils.getNormalMoneyFormat(remainCapital))) > 0);
                        if (!bRepayMore) {
                            judge();
                        }else{
                            showErrorDialog(getResources().getString(R.string.boc_eloan_repay_amount_bigger));
                        }
                    }
                }else{
                    showErrorDialog(getResources().getString(R.string.boc_eloan_input_repay_amount));
                }
            }
        });
    }

    private void gotoSelectAccountPage(){
        AccoutFragment accoutFragment = new AccoutFragment();
        accoutFragment.setAccountType(AccoutFragment.AccountType.SELECT_REPAYMENTACCOUNT);
        accoutFragment.setConversationId(conversationID);
        Bundle bundle = new Bundle();
        bundle.putString("type", "prepay");
        accoutFragment.setArguments(bundle);
        startForResult(accoutFragment, AccoutFragment.RequestCode);
    }

    private void judge() {
        //本息合计
        BigDecimal bdCapAndInt = new BigDecimal(MoneyUtils.getNormalMoneyFormat(edtMPrepayAmount.getContentMoney())).add(new BigDecimal(
                MoneyUtils.getNormalMoneyFormat(mPrepayDetailModel.getThisIssueRepayInterest())));
        totalCapInt = bdCapAndInt.toString();
        //总额： 本息合计 + 手续费
        BigDecimal bdTotalMoney = bdCapAndInt;
        if(!StringUtils.isEmptyOrNull(charges)){
            bdTotalMoney = bdTotalMoney.add(new BigDecimal(charges));
        }
        if (!getResources().getString(R.string.boc_common_select).equals(edtCRepaymentAccount.getChoiceTextContent())){
            if (!TextUtils.isEmpty(availableBalance)){
                Boolean bAccountMoneyEnough = bdTotalMoney.compareTo(new BigDecimal(availableBalance)) <= 0;
                if (bAccountMoneyEnough){
                    if(charges == null){
                        //没有手续费时，先计算手续费，结算完成后直接跳转至确认页
                        bGotoConfirmPageFormCalcCharges = true;
                        calcPrepayCharges(mPrepayDetailModel);
                    }
                    else{
                        gotoConfirmPage();
                    }
                }else{
                    showErrorDialog(getResources().getString(R.string.boc_eloan_no_enough_money));
                }
            }else{
                showErrorDialog(getResources().getString(R.string.boc_eloan_no_enough_money));
            }
        }else{
            showErrorDialog(getResources().getString(R.string.boc_loan_select_repay_account));
        }
    }

    //进入确认页
    private void gotoConfirmPage(){
        SinglePrepaySubmitConfirmFragment fragment = new SinglePrepaySubmitConfirmFragment();

        fragment.setConversationID(conversationID);
        SinglePrepaySubmitReq request = buildSubmitParams();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EloanConst.ELON_PREPAY_COMMIT, request);
        bundle.putString(EloanConst.LOAN_PREPAY_CHARGES, charges);
        bundle.putString(EloanConst.LOAN_PREPAY_AVLAMOUNT, availableBalance);

        fragment.setArguments(bundle);
        start(fragment);
    }

    //构造提前还款请求参数
    private SinglePrepaySubmitReq buildSubmitParams(){
        SinglePrepaySubmitReq result = new SinglePrepaySubmitReq();

        result.setQuoteNo(QuoteNo);
        result.setAccountId(accountId);
        result.setPayAccount(account);
        result.setLoanType(mPrepayDetailModel.getLoanType());
        result.setLoanAccount(mPrepayDetailModel.getAccountNumber());
        result.setCurrency(ApplicationConst.CURRENCY_CNY);
        result.setAdvanceRepayInterest(getStrWith2Decimal(mPrepayDetailModel.getThisIssueRepayInterest()));
        result.setAdvanceRepayCapital(edtMPrepayAmount.getContentMoney());
        result.setRepayAmount(getStrWith2Decimal(totalCapInt));

        return result;
    }



    @Override
    public void onDestroyView() {
        mPresenger.unsubscribe();
        super.onDestroyView();
    }

    /**
     * 选择类型
     * @param position 索引
     */
    private void onClickItem(int position) {
        if(position == 1){        //全额
            edtMPrepayAmount.setmContentMoneyEditText(MoneyUtils.transMoneyFormat(remainCapital, "021"));
            edtMPrepayAmount.setMoneyEditTextShowLocation(false); //文字居右
            edtMPrepayAmount.setClearIconVisible(false);
            edtMPrepayAmount.getContentMoneyEditText().setClickable(false);
        }else if (position == 0){     //部分
            llyInclude.setVisibility(View.GONE);
            edtMPrepayAmount.setMoneyEditTextShowLocation(true); //文字居左
            edtMPrepayAmount.setClearIconVisible(true);
            edtMPrepayAmount.getContentMoneyEditText().clearText();
            edtMPrepayAmount.getContentMoneyEditText().setClickable(true);
        }
    }


    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == AccoutFragment.ResultCode) {
            if (data != null) {
                Log.i(TAG, "-------->account" + data.get("account").toString());
                account = data.get("account").toString();
                accountId = data.get("accountId").toString();
                edtCRepaymentAccount.setChoiceTextContent(NumberUtils.formatCardNumber(account));

                showLoadingDialog();
                mPresenger.prepayCheckAccountDetail(accountId);
            }
        }
    }

    @Override
    public void obtainConversationSuccess(String conversationId) {
        this.conversationID = conversationId;

        if(nextStep == NEXT_STEP.getDefRepayAccount){
            queryAccount();
        }
        else{
            gotoSelectAccountPage();
        }
    }

    @Override
    public void obtainConversationFail(ErrorException e) {
        closeProgressDialog();
    }

    @Override
    public void prepayCheckAccountDetailSuccess(PrepayAccountDetailModel.AccountDetaiListBean result) {
        Log.i(TAG, "提前还款查询账户详情成功！");
        availableBalance=String.valueOf(result.getAvailableBalance());

        llyRemainAvl.setVisibility(View.VISIBLE);
        tvRemainAvl.setText(String.format("%s %s",
                DataUtils.getCurrencyDescByLetter(mContext, mPrepayDetailModel.getCurrencyCode()),
                MoneyUtils.transMoneyFormat(availableBalance, mPrepayDetailModel.getCurrencyCode())));

        calcPrepayCharges(mPrepayDetailModel);
    }

    /**
     * 计算单笔提前还款手续费
     * @param bean
     */
    private void calcPrepayCharges(PsnLOANListEQueryResult.PsnLOANListEQueryBean bean){
        try{
            PsnELOANRepayCountParams params = new PsnELOANRepayCountParams();

            params.setLoanType(bean.getLoanType());
            params.setLoanAccount(bean.getAccountNumber());
            params.setCurrency(ApplicationConst.CURRENCY_CNY);
            params.setAdvanceRepayCapital(bean.getRemainCapital());
            params.setAdvanceRepayInterest(getStrWith2Decimal(bean.getThisIssueRepayInterest()));

            String remainAmount = (new BigDecimal(bean.getRemainCapital()).add(new BigDecimal(bean.getThisIssueRepayInterest()))).toString();
            params.setRepayAmount(getStrWith2Decimal(remainAmount));
            params.setAccountId(accountId);
            params.setPayAccount(bean.getLoanCycleRepayAccount());

            mPresenger.calcCharges(params);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 数值四舍五入得到两位小数
     * @param value
     * @return
     */
    private String getStrWith2Decimal(String value){
        if(StringUtils.isEmptyOrNull(value)){
            return "";
        }

        BigDecimal bdValue = new BigDecimal(value);
        return bdValue.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }


    @Override
    public void prepayCheckAccountDetailFail(ErrorException e) {
        Log.i(TAG, "提前还款查询账户详情失败！");
        llyRemainAvl.setVisibility(View.GONE);
        doCheckAccountFailed();
        closeProgressDialog();
    }

    @Override
    public void calcChargesSuccess(PsnELOANRepayCountResult result) {
        closeProgressDialog();
        charges = result.getCharges();

        if(StringUtils.isEmptyOrNull(charges)){
            charges = "0";
        }

        if(bGotoConfirmPageFormCalcCharges){
            gotoConfirmPage();
        }
        else{
            llyBackground.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void calcChargesFail(ErrorException e) {
        closeProgressDialog();
        charges = null;
    }

    @Override
    public void obtainAllChinaBankAccountSuccess(List<QueryAllChinaBankAccountRes> res) {
        boolean  istrue = false;
        if (!PublicUtils.isEmpty(res) && mPrepayDetailModel != null) {
            //账户检查参数
            PsnLOANPayerAcountCheckParams params = new PsnLOANPayerAcountCheckParams();
            params.setConversationId(conversationID);

            for (QueryAllChinaBankAccountRes queryAllChinaBankAccountRes : res) {
                if (mPrepayAccount != null && mPrepayAccount.equals(
                        queryAllChinaBankAccountRes.getAccountNumber())) {
                    account = queryAllChinaBankAccountRes.getAccountNumber();
                    accountId = queryAllChinaBankAccountRes.getAccountId() + "";

                    params.setCurrencyCode(queryAllChinaBankAccountRes.getCurrencyCode());
                    params.setAccountId(accountId);

                    edtCRepaymentAccount.setChoiceTextContent(NumberUtils.formatCardNumber(account));
                    LogUtils.i(TAG, "------------>获取账户列表成功!----->有匹配的");
                    istrue =true;
                    break;
                } else {
                    LogUtils.i(TAG, "------------>获取账户列表成功!---->没有匹配的");
                    edtCRepaymentAccount.setChoiceTextContent(getString(R.string.boc_eloan_choice));
                    istrue =false;
                }
            }
            if(istrue && !StringUtils.isEmptyOrNull(accountId)){
                mPresenger.checkRepaymentAccount(params);
            }else{
                closeProgressDialog();
                doCheckAccountFailed();
            }
        } else {
            LogUtils.i(TAG, "------------>获取账户列表成功!------->为空");
            closeProgressDialog();
            doCheckAccountFailed();
        }
    }

    @Override
    public void obtainAllChinaBankAccountFail(ErrorException e) {
        closeProgressDialog();
        doCheckAccountFailed();
    }

    private void doCheckAccountFailed(){
        availableBalance = null;
        edtCRepaymentAccount.setChoiceTextContent(getString(R.string.boc_eloan_choice));
        charges = null;
    }

    @Override
    public void doRepaymentAccountCheckSuccess(RepaymentAccountCheckRes res) {
        if(res != null && res.getCheckResult() != null && res.getCheckResult().size() > 0){
            String status = (String)res.getCheckResult().get(0);
            if(!StringUtils.isEmptyOrNull(status) && status.equals("01") || status.equals("00")){
                //检查成功，继续请求账户余额信息
                mPresenger.prepayCheckAccountDetail(accountId);
                return;
            }
        }

        closeProgressDialog();
        doCheckAccountFailed();
    }

    @Override
    public void doRepaymentAccountCheckFail(ErrorException e) {
        closeProgressDialog();
        doCheckAccountFailed();
    }

    @Override
    public void setPresenter(SinglePrepaySubmitContract.Presenter presenter) {

    }

    @Override
    public void setOnMoneyChangeListener(String str) {
        if(!StringUtils.isEmptyOrNull(str)){
            llyInclude.setVisibility(View.VISIBLE);
            updateCapitalAndInterest(str, mPrepayDetailModel.getThisIssueRepayInterest());
        }else{
            llyInclude.setVisibility(View.GONE);
        }
    }
}
