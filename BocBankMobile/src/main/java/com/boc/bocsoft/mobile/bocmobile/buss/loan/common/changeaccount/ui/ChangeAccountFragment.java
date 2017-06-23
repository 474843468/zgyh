package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.changeaccount.ui;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery.PsnLOANListEQueryResult;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.MClickableSpan;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.changeaccount.model.ChangeAccountSubmitReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.changeaccount.model.ChangeAccountVerifyReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.changeaccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.changeaccount.presenter.ChangeAccountPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.LoanCosnt;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.ui.ContractContentFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.ui.AccoutFragment;
import com.boc.bocsoft.mobile.common.utils.ButtonClickLock;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

/**
 * 贷款管理-循环类贷款-变更还款账户页面fragment
 * Created by liuzc on 2016/8/24.
 */
public class ChangeAccountFragment extends MvpBussFragment<ChangeAccountPresenter> implements ChangeAccountContract.ChangeAccountView{
    //获取会话ID成功后的下一步操作
    enum NEXT_STEP{
        getSecurityFactor,  //获取安全因子
        gotoSelectAccount //进入账户选择页面
    }
    private static final String TAG = "ChangeAccountFragment";
    private SecurityVerity securityVerity;
    private TextView tvRepaymentAccount;
    private EditChoiceWidget edtNewAccount;
    private TextView tvHint;
    private ImageButton imgbSelect;
    private TextView tvAgreement;
    private Button btnSummit;
    private View mRoot;
//    private DetailTableRowButton bocBtntv;
    private String conversationId = null;
    private String _combinId;
    private ChangeResultFragment resultsFragment;
    private static String CONTRACT;
    private String accountId;
    private String account;
    private boolean available;
    //安全方式布局
    protected LinearLayout ll_security_content;
    // 安全方式 选择的值
    private TextView tv_security_value;
    // 安全方式 更改按钮
    private Button btn_change;

    //可点击的Span
    private MClickableSpan clickableSpanFir;
    //SpannableString对象
    private SpannableString spannableStringFir;
    private ChangeAccountVerifyReq changeAccountVerifyReq;

    /**变更还款账户model*/
    private PsnLOANListEQueryResult.PsnLOANListEQueryBean mQryResultBean = null;

    private String checkAccontDetailResult = null; //账户详情检查返回结果

    private String cardNumber = null;//还款账户卡号
    /**
     * 提交按钮 防暴力点击
     */
    private String click_more = "click_more";

    private String random = null; //随机数

    private NEXT_STEP nextStep = NEXT_STEP.getSecurityFactor;
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRoot = mInflater.inflate(R.layout.fragment_common_changeaccount, null);
        return mRoot;
    }

    @Override
    protected void titleLeftIconClick() {
        super.titleLeftIconClick();
    }

    @Override
    protected void titleRightIconClick() {
        super.titleRightIconClick();
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_eloan_changerepaymentaccount_pagename);
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
        securityVerity = SecurityVerity.getInstance(getActivity());

        tvRepaymentAccount = (TextView) mRoot.findViewById(R.id.repaymentAccount);
        edtNewAccount = (EditChoiceWidget) mRoot.findViewById(R.id.newAccount);
        edtNewAccount.setChoiceTitleBold(true);

        tvHint = (TextView) mRoot.findViewById(R.id.tvHint);
        imgbSelect = (ImageButton) mRoot.findViewById(R.id.chbSelect);
        tvAgreement = (TextView) mRoot.findViewById(R.id.tvAgreement);
        btnSummit = (Button) mRoot.findViewById(R.id.summit);

//        bocBtntv = new DetailTableRowButton(mContext);
//        llyContent = (LinearLayout) mRoot.findViewById(R.id.content);
        ll_security_content = (LinearLayout) mRoot.findViewById(R.id.ll_security_content);
        tv_security_value = (TextView) mRoot.findViewById(R.id.tv_security_value);
        btn_change = (Button) mRoot.findViewById(R.id.btn_change);

        resultsFragment=new ChangeResultFragment();
    }

    @Override
    public void initData() {
        mQryResultBean = (PsnLOANListEQueryResult.PsnLOANListEQueryBean)getArguments()
                .getSerializable(EloanConst.ELON_ACCOUNT);
        cardNumber = getArguments().getString(LoanCosnt.LOAN_REPYNUM);

        showLoadingDialog(false);
        //创建会话
        getPresenter().creatConversation();

        tvRepaymentAccount.setText(getResources().getString(R.string.boc_loan_current_repayment_account)
                + ": " + NumberUtils.formatCardNumber(cardNumber));
        edtNewAccount.setChoiceTextContent(getString(R.string.boc_eloan_choice));
        edtNewAccount.setChoiceTextName(getString(R.string.boc_eloan_newRepaymentAccount));
        tvHint.setText(getResources().getString(R.string.boc_loan_change_account_tips));
        CONTRACT = getResources().getString(R.string.boc_loan_customer_auth);
        //为组件添加数据
        spannableStringFir = new SpannableString(CONTRACT);
        clickableSpanFir = new MClickableSpan(CONTRACT, getContext());
        clickableSpanFir.setColor(getResources().getColor(R.color.boc_text_color_red));
        spannableStringFir.setSpan(clickableSpanFir, 0,
                CONTRACT.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvAgreement.setText(getResources().getString(R.string.boc_loan_agree_sign));
        tvAgreement.append(spannableStringFir);
        tvAgreement.setMovementMethod(LinkMovementMethod.getInstance());
        tvAgreement.setLongClickable(false);
    }

    @Override
    public void setListener() {
        //安全组件监听
        securityVerity.setSecurityVerifyListener(new SecurityVerity.VerifyCodeResultListener() {
            //选择安全组件
            @Override
            public void onSecurityTypeSelected(CombinListBean bean) {
                _combinId = bean.getId();
                tv_security_value.setText(bean.getName()+"");
                ll_security_content.setVisibility(View.VISIBLE);
            }

            //安全组件点击确定触发
            @Override
            public void onEncryptDataReturned(String factorId, String[] randomNums, String[] encryptPasswords) {
                ChangeAccountSubmitReq changeAccountSubmitReq
                        = new ChangeAccountSubmitReq();

                if ("8".equals(factorId)) {
                    //动态口令
                    changeAccountSubmitReq.setOtp(encryptPasswords[0]);
                    changeAccountSubmitReq.setOtp_RC(randomNums[0]);
                    changeAccountSubmitReq.setFactorId(factorId);
                } else if ("32".equals(factorId)) {
                    //短信验证码
                    changeAccountSubmitReq.setSmc(encryptPasswords[0]);
                    changeAccountSubmitReq.setSmc_RC(randomNums[0]);
                    changeAccountSubmitReq.setFactorId(factorId);
                } else if ("40".equals(factorId)) {
                    //动态口令+短信验证码
                    changeAccountSubmitReq.setOtp(encryptPasswords[0]);
                    changeAccountSubmitReq.setOtp_RC(randomNums[0]);
                    changeAccountSubmitReq.setSmc(encryptPasswords[1]);
                    changeAccountSubmitReq.setSmc_RC(randomNums[1]);
                    changeAccountSubmitReq.setFactorId(factorId);
                } else if ("96".equals(factorId)) {
                    //短信验证码
                    changeAccountSubmitReq.setSmc(encryptPasswords[0]);
                    changeAccountSubmitReq.setSmc_RC(randomNums[0]);
                    changeAccountSubmitReq.setFactorId(factorId);
                    DeviceInfoModel infoModel = DeviceInfoUtils.getDeviceInfo(getActivity()
                            ,random, ApplicationContext.getInstance().getUser().getOperatorId());

                    changeAccountSubmitReq.setDeviceInfo(infoModel.getDeviceInfo());
                    changeAccountSubmitReq.setDeviceInfo_RC(infoModel.getDeviceInfo_RC());
                    changeAccountSubmitReq.setDevicePrint(DeviceInfoUtils.getDevicePrint(getActivity()));
                }
                doBuildParamsAndSubmit(changeAccountSubmitReq);
            }

            @Override
            public void onSignedReturn(String signRetData) {
                submitForVoiKey(signRetData);
            }
        });

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ButtonClickLock.isCanClick(click_more)) {
                    return;
                }

                if(StringUtils.isEmptyOrNull(conversationId)){
                    //尚未成功请求会话ID
                    showLoadingDialog(false);
                    nextStep = NEXT_STEP.getSecurityFactor;
                    getPresenter().creatConversation();
                }
                else if(StringUtils.isEmptyOrNull(tv_security_value.getText().toString())
                        || tv_security_value.getText().toString().equals(getString(R.string.boc_common_select))){
                    //尚未成功获取安全因子
                    showLoadingDialog(false);
                    getPresenter().getSecurityFactor();
                }
                else{
                    securityVerity.selectSecurityType();
                }
            }
        });
        clickableSpanFir.setListener(new MClickableSpan.OnClickSpanListener() {
            @Override
            public void onClickSpan() {
                openContract();
            }
        });
        edtNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StringUtils.isEmptyOrNull(conversationId)){
                    showLoadingDialog(false);
                    nextStep = NEXT_STEP.gotoSelectAccount;
                    getPresenter().creatConversation();
                }
                else{
                    gotoSelectAccountPage();
                }
            }
        });
        btnSummit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onClickSubmit();
            }
        });

        imgbSelect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                imgbSelect.setSelected(!imgbSelect.isSelected());
            }
        });
    }

    private void gotoSelectAccountPage(){
        AccoutFragment accoutFragment = new AccoutFragment();
        accoutFragment.setAccountType(AccoutFragment.AccountType.CHANGE_REPAYMENTACCOUNT);
        accoutFragment.setConversationId(conversationId);
        accoutFragment.setIsBocELoan(false, mQryResultBean.getCurrencyCode());
        LogUtil.e("............................gotoSelectAccountPage " + mQryResultBean.getCurrencyCode());
        Bundle bundle = new Bundle();
        bundle.putString("type", "change");
        bundle.putString("commonType", "reloan");
        if(!StringUtils.isEmptyOrNull(cardNumber)){
            bundle.putString("currentAccount", cardNumber);
        }

        accoutFragment.setArguments(bundle);
        startForResult(accoutFragment, AccoutFragment.RequestCode);
    }

    /**
     * 打开合同
     */
    private void openContract(){
        if(StringUtils.isEmptyOrNull(edtNewAccount.getChoiceTextContent()) ||
                edtNewAccount.getChoiceTextContent().equals(getString(R.string.boc_common_select))){
            showErrorDialog(getString(R.string.boc_eloan_changerepaymentaccount_pagename2));
            return;
        }
        ContractContentFragment contractContentFragment = new ContractContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", getString(R.string.boc_loan_change_account_contract));
        bundle.putString("loanType", PublicCodeUtils.getLoanTypeName(mContext, mQryResultBean.getLoanType()));
        bundle.putString("accNo",
                NumberUtils.formatCardNumberStrong(account));
        contractContentFragment.setArguments(bundle);
        start(contractContentFragment);
    }

    //点击提交按钮
    private void onClickSubmit(){
        //无新还款账户
        if (StringUtils.isEmptyOrNull(edtNewAccount.getChoiceTextContent()) ||
                getResources().getString(R.string.boc_common_select).equals(edtNewAccount.getChoiceTextContent())){
            showErrorDialog(String.format("%s%s",
                    getString(R.string.boc_common_select),
                    getString(R.string.boc_eloan_newRepaymentAccount)));
            return;
        }

        //新旧还款账户一致
        if(account != null && account.equals(mQryResultBean.getLoanCycleRepayAccount())){
            showErrorDialog(getString(R.string.boc_loan_change_account_same));
            return;
        }

        //还款账户检查接口返回02,贷款账户与还款账户币种不一致
        if(checkAccontDetailResult != null && checkAccontDetailResult.equals("02")){
            showErrorDialog(getString(R.string.boc_loan_change_account_currency_not_same));
            return;
        }

        //还款账户检查接口返回03,还款账户为钞户
        if(checkAccontDetailResult != null && checkAccontDetailResult.equals("03")){
            showErrorDialog(getString(R.string.boc_loan_change_account_cashremit_wrong));
            return;
        }

        //未勾选协议
        if(!imgbSelect.isSelected()){
            showErrorDialog(getResources().getString(R.string.boc_loan_select_and_agree));
            return;
        }

        showLoadingDialog(false);
        doVerifySubmit();
    }

    /**
     * 执行预交易
     */
    private void doVerifySubmit() {
        changeAccountVerifyReq
                = new ChangeAccountVerifyReq();
        changeAccountVerifyReq.setConversationId(conversationId);
        changeAccountVerifyReq.set_combinId(_combinId);
        changeAccountVerifyReq.setOldPayAccountNum(mQryResultBean.getLoanCycleRepayAccount());
        changeAccountVerifyReq.setNewPayAccountNum(account);
        changeAccountVerifyReq.setNewPayAccountId(accountId);
        changeAccountVerifyReq.setLoanType(mQryResultBean.getLoanType());
        String currencyCode = mQryResultBean.getCurrencyCode();
        changeAccountVerifyReq.setCurrencyCode(currencyCode);
        changeAccountVerifyReq.setLoanActNum(mQryResultBean.getAccountNumber());
        changeAccountVerifyReq.setOldPayCardNum(cardNumber);
        if(currencyCode != null && (currencyCode.equals(ApplicationConst.CURRENCY_CNY) ||
            currencyCode.equals("CNY"))){
            changeAccountVerifyReq.setCashRemit("00");
        }
        else{
            changeAccountVerifyReq.setCashRemit("02");
        }


        getPresenter().setChangeAccountVerifyReq(changeAccountVerifyReq);
        getPresenter().changeAccountOEVerify();
    }

    /**
     * 音频key请求
     * @param signRetData
     */
    private void submitForVoiKey(String signRetData){
        ChangeAccountSubmitReq changeAccountSubmitReq = new ChangeAccountSubmitReq();
        changeAccountSubmitReq.set_signedData(signRetData);
        changeAccountSubmitReq.setFactorId("4");
        doBuildParamsAndSubmit(changeAccountSubmitReq);
    }

    /**
     * 执行提交交易
     * @param changeAccountSubmitReq
     */
    private void doBuildParamsAndSubmit(ChangeAccountSubmitReq changeAccountSubmitReq){
        if(changeAccountSubmitReq == null){
            return;
        }
        showLoadingDialog(false);

        changeAccountSubmitReq.setActiv(SecurityVerity.getInstance(getActivity()).getCfcaVersion());
        changeAccountSubmitReq.setState(SecurityVerity.SECURITY_VERIFY_STATE);

        changeAccountSubmitReq.setOldPayAccountNum(changeAccountVerifyReq.getOldPayAccountNum());
        changeAccountSubmitReq.setOldPayCardNum(changeAccountVerifyReq.getOldPayCardNum());
        changeAccountSubmitReq.setNewPayAccountNum(changeAccountVerifyReq.getNewPayAccountNum());
        changeAccountSubmitReq.setNewPayAccountId(changeAccountVerifyReq.getNewPayAccountId());
        changeAccountSubmitReq.setLoanType(changeAccountVerifyReq.getLoanType());
        changeAccountSubmitReq.setLoanActNum(changeAccountVerifyReq.getLoanActNum());
        changeAccountSubmitReq.setCurrencyCode(changeAccountVerifyReq.getCurrencyCode());
        changeAccountSubmitReq.setCashRemit(changeAccountVerifyReq.getCashRemit());

        getPresenter().setChangeAccountSubmitReq(changeAccountSubmitReq);
        getPresenter().changeAccountOESubmit();
    }

    //获取回话成功
    @Override
    public void obtainConversationSuccess(String conversationId) {
        Log.i(TAG, "------------>获取会话成功!");
        this.conversationId = conversationId;
        securityVerity.setConversationId(conversationId);

        if(nextStep == NEXT_STEP.getSecurityFactor){
            getPresenter().getSecurityFactor();
        }
        else{
            gotoSelectAccountPage();
        }
    }

    @Override
    public void obtainConversationFail(com.boc.bocsoft.mobile.bocmobile.buss.loan.common.changeaccount.model.ErrorException e) {
        closeProgressDialog();
        tv_security_value.setText(getString(R.string.boc_common_select));
        ll_security_content.setVisibility(View.VISIBLE);
    }


    //获取安全因子成功
    @Override
    public void obtainSecurityFactorSuccess(SecurityFactorModel result) {
        Log.i(TAG, "------------>获取安全因子成功!");
        closeProgressDialog();
        _combinId = securityVerity.getDefaultSecurityFactorId(result).getId();
        tv_security_value.setText(securityVerity.getDefaultSecurityFactorId(result).getName() + "");
        ll_security_content.setVisibility(View.VISIBLE);
    }

    @Override
    public void obtainSecurityFactorFail(com.boc.bocsoft.mobile.bocmobile.buss.loan.common.changeaccount.model.ErrorException e) {
        closeProgressDialog();
        tv_security_value.setText(getString(R.string.boc_common_select));
        ll_security_content.setVisibility(View.VISIBLE);
    }


    //获取随机数成功
    @Override
    public void obtainRandomSuccess(String random) {
        Log.i(TAG, "------------>获取随机数成功!");
        this.random = random;
        closeProgressDialog();
        //弹出选择的安全认证工具
        securityVerity.showSecurityDialog(random);
    }

    @Override
    public void obtainRandomFail(com.boc.bocsoft.mobile.bocmobile.buss.loan.common.changeaccount.model.ErrorException e) {
        closeProgressDialog();
    }

    @Override
    public void changeAccountOEVerifySuccess(com.boc.bocsoft.mobile.bocmobile.buss.loan.common.changeaccount.model.ChangeAccountVerifyRes res) {
        Log.i(TAG, "------------>变更还款账户预交易成功!");
        closeProgressDialog();
        available = securityVerity.confirmFactor(res.getFactorList());
        if (available) {
            // 音频key加密
            EShieldVerify.getInstance(getActivity()).setmPlainData(
                    res.get_plainData());
            //获取随机数
            getPresenter().getRandom();
            showLoadingDialog(false);
        } else {
            //TODO 获取确定的安全因子失败
        }
    }

    @Override
    public void changeAccountOEVerifyFail(com.boc.bocsoft.mobile.bocmobile.buss.loan.common.changeaccount.model.ErrorException e) {
        Log.i(TAG, "------------>变更循环非循环还款账户预交易失败!");
        closeProgressDialog();
    }

    //变更还款账户交易成功!
    @Override
    public void changeAccountOESubmitSuccess() {
        closeProgressDialog();
        resultsFragment.setStatus(OperationResultHead.Status.SUCCESS);
        Bundle bundle=new Bundle();
//        bundle.putSerializable(EloanConst.ELON_ACCOUNT,changeAccountVerifyReq);
        bundle.putString("oldAccount",cardNumber);
        bundle.putString("newAccount",account);
        resultsFragment.setArguments(bundle);
        start(resultsFragment);
    }

    //变更还款账户交易失败!
    @Override
    public void changeAccountOESubmitFail(ErrorException e) {
        Log.i(TAG, "------------>变更还款账户交易失败!");
        closeProgressDialog();
    }

    @Override
    public void checkAccountDetailSuccess(PsnAccountQueryAccountDetailResult result) {
        closeProgressDialog();
        checkAccontDetailResult = result.getAccountStatus();
    }

    @Override
    public void checkAccountDetailFail(ErrorException e) {
        closeProgressDialog();
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == AccoutFragment.ResultCode) {
            if (data != null) {
                Log.i(TAG, "-------->返回account------>" + data.get("account").toString());
                tvHint.setVisibility(View.VISIBLE);
                account = data.get("account").toString();
                accountId = data.get("accountId").toString();
                edtNewAccount.setChoiceTextContent
                        (NumberUtils.formatCardNumber(data.get("account").toString()));
            }
        }
    }

    @Override
    public void setPresenter(ChangeAccountContract.Presenter presenter) {
    }

    @Override
    protected ChangeAccountPresenter initPresenter() {
        return new ChangeAccountPresenter(this);
    }
}
