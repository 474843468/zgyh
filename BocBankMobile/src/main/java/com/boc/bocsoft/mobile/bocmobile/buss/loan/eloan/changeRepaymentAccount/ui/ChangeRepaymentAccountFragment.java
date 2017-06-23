package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.ui;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.MClickableSpan;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.ui.ContractContentFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ChangeAccountSubmitReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ChangeAccountVerifyReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ChangeAccountVerifyRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.presenter.ChangeAccountPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanStatusListModel;
import com.boc.bocsoft.mobile.common.utils.ButtonClickLock;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

/**
 * 变更还款账户
 *
 * @author cq
 * @date 2016-8-22 14:20:42
 */
public class ChangeRepaymentAccountFragment extends MvpBussFragment<ChangeAccountPresenter> implements
        ChangeAccountContract.changeAccountView, OnClickListener {
    private static String PAGENAME;
    private static final String TAG = "ChangeRAccountFragment";
    //安全方式布局
    protected LinearLayout ll_security_content;
    // 安全方式 选择的值
    private TextView tv_security_value;
    // 安全方式 更改按钮
    private Button btn_change;
    //当前还款账户
    private TextView repaymentAccount;
    //选择还款账户组件
    private EditChoiceWidget newAccount;
    //提示语：第2天生效
    private TextView hint;
    // private CheckBox checkbox;
    private ImageButton checkbox;
    //客户授权view
    private TextView agreement;
    private Button summit;
    private View mRoot;
    //更改安全认证方式组件
//	private DetailTableRowButton bocBtntv;
    //会话id
    private String conversationId;
    //获取安全因子成功后返回的默认安全因子组合id
    private String _combinId;
    //客户授权
    private static String CONTRACT;
    // 可点击的Span
    private MClickableSpan clickableSpanFir;
    // SpannableString对象
    private SpannableString spannableStringFir;
    //显示客户授权内容
    private ContractContentFragment contractContentFragment;
//    private ChangeAccountPresenter changeAccountPresenter;
    //安全组件
    private SecurityVerity securityVerity;
    //预交易后，再次确认安全因子  为true调用预交易接口
    private boolean available;
    //变更还款账户后的id
    private String accountId;
    //变更还款账户后的账户
    private String account;
    private ChangeAccountVerifyReq changeAccountVerifyReq;
    /**
     * 变更还款账户model
     */
    private EloanStatusListModel mEloanDrawModel;
    //结果页
    private ChangeAccountResultFragment resultsFragment;
    //随机数
    private String random;
    private ChangeAccountSubmitReq req;
    /**
     * 提交按钮 防暴力点击
     */
    private String click_more = "click_more";
    /**额度编号*/
    public static String mQuoteno;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRoot = mInflater.inflate(
                R.layout.fragment_eloan_changerepaymentaccount, null);
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
    public void beforeInitView() {

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

//        changeAccountPresenter = new ChangeAccountPresenter(this);
        securityVerity = SecurityVerity.getInstance(getActivity());
        showLoadingDialog();
        // 创建会话
        getPresenter().creatConversation();

        mEloanDrawModel = (EloanStatusListModel) getArguments()
                .getSerializable(EloanConst.ELON_ACCOUNT);
        repaymentAccount = (TextView) mRoot.findViewById(R.id.repaymentAccount);
        newAccount = (EditChoiceWidget) mRoot.findViewById(R.id.newAccount);

        hint = (TextView) mRoot.findViewById(R.id.hint);
        // checkbox = (CheckBox) mRoot.findViewById(R.id.checkbox);
        checkbox = (ImageButton) mRoot.findViewById(R.id.checkbox);
        // checkbox.setSelected(false);
        agreement = (TextView) mRoot.findViewById(R.id.agreement);
        summit = (Button) mRoot.findViewById(R.id.summit);

        // 初始化合同展示页面
//		contractContentFragment = new ContractContentFragment();
        ll_security_content = (LinearLayout) mRoot.findViewById(R.id.ll_security_content);
        tv_security_value = (TextView) mRoot.findViewById(R.id.tv_security_value);
        btn_change = (Button) mRoot.findViewById(R.id.btn_change);
//		bocBtntv = new DetailTableRowButton(mContext);

        resultsFragment = new ChangeAccountResultFragment();
    }

    @Override
    public void initData() {
//		ButtonClickLock.getLock(click_more).lockDuration = 1000;
        ButtonClickLock.getLock(click_more).lockDuration = EloanConst.CLICK_MORE_TIME;
        // repaymentAccount.setText("当前还款账户："+NumberUtils.formatCardNumber(mEloanDrawModel.getPay()));
//		repaymentAccount.setText("当前还款账户："
//				+ NumberUtils.formatCardNumberStrong(mEloanDrawModel.getPayAccount()));

        if (!TextUtils.isEmpty(mQuoteno)
                && mQuoteno.equals(mEloanDrawModel.getQuoteNo())
                &&!StringUtils.isEmptyOrNull(ChangeAccountResultFragment.newAccount)) {
            repaymentAccount.setText("当前还款账户："
                    + NumberUtils.formatCardNumberStrong(ChangeAccountResultFragment.newAccount));
        } else {
            repaymentAccount.setText("当前还款账户："
                    + NumberUtils.formatCardNumberStrong(mEloanDrawModel.getPayAccount()));
        }

        newAccount.setChoiceTextContent(getString(R.string.boc_eloan_choice));
        newAccount
                .setChoiceTextName(getString(R.string.boc_eloan_newRepaymentAccount));
        hint.setText("还款账户变更申请后，新的还款账户将在第二天正式生效，建议不要在还款日当天变更还款账户。");
        CONTRACT = "《客户授权》";
        // 为组件添加数据
        spannableStringFir = new SpannableString(CONTRACT);
        clickableSpanFir = new MClickableSpan(CONTRACT, getContext());
        clickableSpanFir.setColor(mContext.getResources().getColor(R.color.boc_text_color_red));
        spannableStringFir.setSpan(clickableSpanFir, 0, CONTRACT.length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        agreement.setText("本人同意签署");
        agreement.append(spannableStringFir);
        agreement.setMovementMethod(LinkMovementMethod.getInstance());
        agreement.setLongClickable(false);

    }

    @Override
    public void setListener() {
        checkbox.setOnClickListener(this);
        // 安全组件监听
        securityVerity
                .setSecurityVerifyListener(new SecurityVerity.VerifyCodeResultListener() {
                    // 选择安全组件
                    @Override
                    public void onSecurityTypeSelected(CombinListBean bean) {
                        _combinId = bean.getId();
//                        bocBtntv.addTextBtn("安全认证", bean.getName(),
//                                "更改", getResources().getColor(R.color.boc_main_button_color));
                        tv_security_value.setText(bean.getName()+"");
                        ll_security_content.setVisibility(View.VISIBLE);
                    }

                    // 安全组件点击确定触发
                    @Override
                    public void onEncryptDataReturned(String factorId,
                                                      String[] randomNums, String[] encryptPasswords) {
                        showLoadingDialog();
                        buildSubmitReq(factorId, randomNums, encryptPasswords);
                    }

                    @Override
                    public void onSignedReturn(String signRetData) {
                        showLoadingDialog();
                        buildSubmitReq(signRetData);
                    }
                });
        // 安全认证方式选择
//        bocBtntv.setOnclick(new DetailTableRowButton.BtnCallback() {
//            @Override
//            public void onClickListener() {
//                if (!ButtonClickLock.isCanClick(click_more)) {
//                    return;
//                }
//                securityVerity.selectSecurityType();
//            }
//        });
        btn_change.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ButtonClickLock.isCanClick(click_more)) {
                    return;
                }
                securityVerity.selectSecurityType();
            }
        });
        // 客户授权
        clickableSpanFir.setListener(new MClickableSpan.OnClickSpanListener() {
            @Override
            public void onClickSpan() {
                if (!"请选择".equals(newAccount.getChoiceTextContent())) {
                    contractContentFragment = new ContractContentFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "变更还款合同");
                    // bundle.putString("loanType",changeAccountVerifyReq.getLoanType());
                    bundle.putString("loanType", "中银E贷");
                    bundle.putString("accNo",
                            NumberUtils.formatCardNumberStrong(account));
                    contractContentFragment.setArguments(bundle);
                    start(contractContentFragment);
                } else {
                    showErrorDialog("请选择新还款账户");
                }
            }
        });
        // 还款账户选择
        newAccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AccoutFragment accoutFragment = new AccoutFragment();
                accoutFragment
                        .setAccountType(AccoutFragment.AccountType.CHANGE_REPAYMENTACCOUNT);
                accoutFragment.setConversationId(conversationId);
                Bundle bundle = new Bundle();
                bundle.putString("type", "change");
//				bundle.putString("currentAccount",
//						mEloanDrawModel.getPayAccount());

                if (!TextUtils.isEmpty(mQuoteno) && mQuoteno.equals(mEloanDrawModel.getQuoteNo())
                        &&!StringUtils.isEmptyOrNull(ChangeAccountResultFragment.newAccount)) {
                    bundle.putString("currentAccount", ChangeAccountResultFragment.newAccount);
                } else {
                    bundle.putString("currentAccount",
                            mEloanDrawModel.getPayAccount());
                }

                accoutFragment.setArguments(bundle);
                startForResult(accoutFragment, AccoutFragment.RequestCode);
            }
        });
        // 提交
        summit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ButtonClickLock.isCanClick(click_more)) {
                    return;
                }
                judge();
            }
        });
    }

    /**
     * 界面元素校验
     *
     * @author yx
     * @date 2016-8-22 14:28:15
     */
    private void judge() {
        // 新还款账户
        if ("请选择".equalsIgnoreCase(newAccount.getChoiceTextContent())
                || StringUtils.isEmpty(newAccount.getChoiceTextContent())) {
            showErrorDialog("请选择新还款账户");
            return;
        }
        // 同意条款勾选按钮
        if (!checkbox.isSelected()) {
            showErrorDialog("请查看并勾选相关条款");
            return;
        }
        doSummit();
    }

    private void buildSubmitReq(String factorId, String[] randomNums,
                                String[] encryptPasswords) {

        req = new ChangeAccountSubmitReq();

        if ("8".equals(factorId)) {
            // if(check(factorId,encryptPasswords)){
            // 动态口令
            req.setOtp(encryptPasswords[0]);
            req.setOtp_RC(randomNums[0]);
            req.setFactorId(factorId);
            buildSubmit();
            // }
        } else if ("32".equals(factorId)) {
            // if(check(factorId,encryptPasswords)){
            // 短信验证码
            req.setSmc(encryptPasswords[0]);
            req.setSmc_RC(randomNums[0]);
            req.setFactorId(factorId);
            buildSubmit();
            // }
        } else if ("40".equals(factorId)) {
            // if(check(factorId,encryptPasswords)){
            // 动态口令+短信验证码
            req.setOtp(encryptPasswords[0]);
            req.setOtp_RC(randomNums[0]);
            req.setSmc(encryptPasswords[1]);
            req.setSmc_RC(randomNums[1]);
            req.setFactorId(factorId);
            buildSubmit();
            // }
        } else if ("96".equals(factorId)) {
            // if(check(factorId,encryptPasswords)){
            // 短信验证码
            req.setSmc(encryptPasswords[0]);
            req.setSmc_RC(randomNums[0]);
            req.setFactorId(factorId);
//            DeviceInfoModel infoModel = DeviceInfoUtils.getDeviceInfo(
//                    getActivity(), random, SpUtils.getSpString(getContext(),
//                            EloanConst.OPETATOR_ID, null));
//
//            req.setDeviceInfo(infoModel.getDeviceInfo());
//            req.setDeviceInfo_RC(infoModel.getDeviceInfo_RC());
//            req.setDevicePrint(DeviceInfoUtils.getDevicePrint(getActivity()));
            DeviceInfoModel info = DeviceInfoUtils.getDeviceInfo(getActivity(), random);
            req.setDeviceInfo(info.getDeviceInfo());
            req.setDeviceInfo_RC(info.getDeviceInfo_RC());
            req.setDevicePrint(DeviceInfoUtils.getDevicePrint(getActivity()));
            buildSubmit();
            // }
        } else if ("4".equals(factorId)) {
            // TODO 音频key
            // req.set_signedData(req.get_signedData());
        }
    }

    private void buildSubmitReq(String signRetData) {
        // TODO 音频key
        req = new ChangeAccountSubmitReq();
        req.set_signedData(signRetData);
        req.setFactorId("4");
        buildSubmit();
    }

    private void buildSubmit() {

        req.setConversationId(conversationId);

        // cfn版本号和状态
        req.setActiv(SecurityVerity.getInstance(getActivity()).getCfcaVersion());
        req.setState(SecurityVerity.SECURITY_VERIFY_STATE);

        // req.set_signedData();
        req.setQuoteNo(changeAccountVerifyReq.getQuoteNo());
        req.setOldPayCardNum(changeAccountVerifyReq.getOldPayCardNum());
        req.setNewPayAccountNum(changeAccountVerifyReq.getNewPayAccountNum());
        req.setNewPayAccountId(changeAccountVerifyReq.getNewPayAccountId());
        req.setLoanType(changeAccountVerifyReq.getLoanType());
        req.setCurrency(changeAccountVerifyReq.getCurrency());
        Log.i("aaaaaaaaaaaaaaaaaaaa", changeAccountVerifyReq.toString());
        getPresenter().setChangeAccountSubmitReq(req);

        showLoadingDialog();
        getPresenter().changeAccountSubmit();

    }

    private void doSummit() {
        changeAccountVerifyReq = new ChangeAccountVerifyReq();
        changeAccountVerifyReq.setConversationId(conversationId);
        changeAccountVerifyReq.set_combinId(_combinId);
        changeAccountVerifyReq.setQuoteNo(mEloanDrawModel.getQuoteNo());
        changeAccountVerifyReq
                .setOldPayCardNum(mEloanDrawModel.getPayAccount());
        changeAccountVerifyReq.setNewPayAccountNum(account);
        changeAccountVerifyReq.setNewPayAccountId(accountId);
        changeAccountVerifyReq.setLoanType(mEloanDrawModel.getLoanType());
        changeAccountVerifyReq.setCurrency(mEloanDrawModel.getCurrencyCode());
        getPresenter()
                .setChangeAccountVerifyReq(changeAccountVerifyReq);

        showLoadingDialog();
        getPresenter().changeAccountVerify();
    }

    @Override
    public void obtainConversationSuccess(String conversationId) {
        Log.i(TAG, "------------>获取会话成功!");
        this.conversationId = conversationId;
        securityVerity.setConversationId(conversationId);
//		closeProgressDialog();
        getPresenter().getSecurityFactor();
//		showLoadingDialog();
    }

    @Override
    public void obtainConversationFail(ErrorException e) {
        Log.i(TAG, "------------>获取会话失败!");
        closeProgressDialog();
    }

    @Override
    public void obtainSecurityFactorSuccess(SecurityFactorModel result) {
        Log.i(TAG, "------------>获取安全因子成功!");
//		closeProgressDialog();
        _combinId = securityVerity.getDefaultSecurityFactorId(result).getId();
//        bocBtntv.addTextBtn("安全认证",
//                securityVerity.getDefaultSecurityFactorId(result).getName(),
//                "更改", getResources().getColor(R.color.boc_main_button_color));
//        ll_security_content.addView(bocBtntv);
        tv_security_value.setText(securityVerity.getDefaultSecurityFactorId(result).getName()+"");
        ll_security_content.setVisibility(View.VISIBLE);
        closeProgressDialog();
    }

    @Override
    public void obtainSecurityFactorFail(ErrorException e) {
        Log.i(TAG, "------------>获取安全因子失败!");
        closeProgressDialog();
    }

    @Override
    public void obtainRandomSuccess(String random) {
        this.random = random;
        Log.i(TAG, "------------>获取随机数成功!");
        closeProgressDialog();
        // 弹出选择的安全认证工具
        securityVerity.showSecurityDialog(random);
    }

    @Override
    public void obtainRandomFail(ErrorException e) {
        Log.i(TAG, "------------>获取随机数失败!");
        closeProgressDialog();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getPresenter().unsubscribe();

    }

    @Override
    public void setPresenter(ChangeAccountContract.Presenter presenter) {

    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == AccoutFragment.ResultCode) {
            if (data != null) {
                account = data.get("account").toString();
                accountId = data.get("accountId").toString();
                newAccount.setChoiceTextContent(NumberUtils
                        .formatCardNumber(data.get("account").toString()));

                Log.i(TAG, "-------->返回account------>"
                        + account + "----------accountId---------"
                        + accountId);
            }
        }
    }

    @Override
    public void changeAccountVerifySuccess(ChangeAccountVerifyRes result) {
        Log.i(TAG, "------------>变更还款账户预交易成功!");
//		closeProgressDialog();
        available = securityVerity.confirmFactor(result.getFactorList());
        if (available) {
            // 获取随机数
            getPresenter().getRandom();
            // 音频key加密
            EShieldVerify.getInstance(getActivity()).setmPlainData(
                    result.get_plainData());
//			showLoadingDialog();
        } else {
            // TODO 获取确定的安全因子失败
        }

    }

    @Override
    public void changeAccountVerifyFail(ErrorException e) {
        Log.i(TAG, "------------>变更还款账户预交易失败!");
        closeProgressDialog();
    }

    @Override
    public void changeAccountSubmitSuccess() {
        Log.i(TAG, "------------>变更还款账户交易成功!");
        closeProgressDialog();
        //添加额度编号判断还款账户是否更新

        resultsFragment.setStatus(OperationResultHead.Status.SUCCESS);
        Bundle bundle = new Bundle();
        // bundle.putSerializable(EloanConst.ELON_ACCOUNT,changeAccountVerifyReq);

        if (!TextUtils.isEmpty(mQuoteno) && mQuoteno.equals(mEloanDrawModel.getQuoteNo())
                &&!StringUtils.isEmptyOrNull(ChangeAccountResultFragment.newAccount)) {
            bundle.putString("oldAccount", ChangeAccountResultFragment.newAccount);
        } else {
            bundle.putString("oldAccount", mEloanDrawModel.getPayAccount());
        }

//		bundle.putString("oldAccount", mEloanDrawModel.getPayAccount());

        bundle.putString("newAccount", account);
        resultsFragment.setArguments(bundle);
        mQuoteno = mEloanDrawModel.getQuoteNo();
        start(resultsFragment);
    }

    @Override
    public void changeAccountSubmitFail(ErrorException e) {
        Log.i(TAG, "------------>变更还款账户交易失败!");
        closeProgressDialog();
        // resultsFragment.setStatus(OperationResultHead.Status.FAIL);
        // start(resultsFragment);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.checkbox) {
            checkbox.setSelected(!checkbox.isSelected());
        }
    }

    @Override
    protected ChangeAccountPresenter initPresenter() {
        return new ChangeAccountPresenter(this);
    }
}
