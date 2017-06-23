package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PhoneNumberFormat;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.MClickableSpan;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditClearWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.model.CreditContractReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.model.CreditContractRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.model.DistrictRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.model.LoanContractReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.model.LoanContractRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.model.PreRegisterVerifyReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.model.PreRegisterVerifyRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.presenter.ApplyPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.ui.regex.ApplyRegexConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.ui.AccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.ui.AccoutFragment.AccountType;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.data.DataUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.draw.ui.DrawListAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanApplyModel;
import com.boc.bocsoft.mobile.common.utils.ButtonClickLock;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.utils.regex.RegexResult;
import com.boc.bocsoft.mobile.framework.utils.regex.RegexUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 激活界面--- 贷款管理-中银E贷-填写申请信息Fragment Created by xintong on 2016/6/3.
 */
public class ApplyFragment extends MvpBussFragment<ApplyPresenter>
        implements ApplyContract.ApplyView, ApplyContract.DistrictSelectView, OnClickListener {

    private static final String TAG = "ApplyFragment";
    // 页面名称
    private static String PAGENAME;
    // 地址信息
    protected TextView addInfo;
    //	private ApplyPresenter applyPresenter;
    // 地区网络请求
    //	private ApplyPresenter zonePresenter;
    // 获取安全因子成功后返回的默认安全因子组合id
    private String _combinId, combinId_name;
    // 安全组件
    private SecurityVerity securityVerity;
    // 合同id
    private String loanContractId = "";
    // 协议id
    private String creditContractId;
    // 预交易后，再次确认安全因子 为true调用预交易接口
    private boolean available;
    private PreRegisterVerifyReq req;
    // 国家行政区划代码,必须具体到县
    private String zoneCode;
    private ApplyConfirmInfoFragment applyConfirmInfoFragment;
    // 列表对话框
    private SelectListDialog selectListDialog;
    // 列表对话框
    private DrawListAdapter drawListAdapter;
    private View mRoot;
    private static final int RequestCode = 222;
    // 会话id
    private String conversationId;
    // 账户id
    private String accountId;
    // 账户
    private String account;
    // 姓名
    TextView name;
    // 手机号
    TextView tel;
    // 地区选择组件
    EditChoiceWidget district;
    // 街道信息
    EditClearWidget street;
    // 关系选择组件
    EditChoiceWidget relationship;
    // 联系人姓名输入框
    EditClearWidget nameEdit;
    // 联系人手机号输入框
    EditClearWidget telEdit;
    // 还款账户选择组件
    EditChoiceWidget repaymentAccount;
    // 是否同意选择框
    // CheckBox checkbox;
    private ImageButton checkbox;
    // 下一步按钮
    Button next;
    // 内容标签
    TextView label;
    // 同意条款合同文字
    TextView agreement;

    // 可点击的Span
    private MClickableSpan clickableSpanFir, clickableSpanSec;
    // SpannableString对象
    private SpannableString spannableStringFir, spannableStringSec;
    // 征信授权协议字段
    private static String CREDIT_CONTRACT;
    // 贷款合同字段
    private static String LOAN_CONTRACT;
    // 会话是否创建
    private boolean hasConv;
    /** 申请激活model */
    private EloanApplyModel mEloanApplyModel;

    // 合同内容
    private String loanContract;
    // 协议内容
    private String creditContrac;

    public static final String INIT_ZONE_CODE = "000000";
    // 地区选择控件
    private DistrictSelectFragment fragment;
    /** 提交按钮 防暴力点击 */
    private String click_more = "click_more";

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRoot = mInflater.inflate(R.layout.fragment_eloan_apply, null);
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
        return getString(R.string.boc_eloan_apply_pagename);
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
        fragment = new DistrictSelectFragment();
        // applyConfirmInfoFragment = new ApplyConfirmInfoFragment();
        securityVerity = SecurityVerity.getInstance(getActivity());
        //		applyPresenter = new ApplyPresenter((ApplyContract.ApplyView) this);
        //		zonePresenter = new ApplyPresenter(
        //				(ApplyContract.DistrictSelectView) this);
        showLoadingDialog(false);
        getPresenter().creatConversation();

        // 上个页面传递的参数
        mEloanApplyModel = (EloanApplyModel) getArguments().getSerializable(EloanConst.ELON_APPLY);
        if (mEloanApplyModel == null) {
            mEloanApplyModel = new EloanApplyModel();
        }
        // 初始化组件
        name = (TextView) mRoot.findViewById(R.id.name);
        tel = (TextView) mRoot.findViewById(R.id.tel);
        addInfo = (TextView) mRoot.findViewById(R.id.addInfo);
        district = (EditChoiceWidget) mRoot.findViewById(R.id.district);
        street = (EditClearWidget) mRoot.findViewById(R.id.street);

        label = (TextView) mRoot.findViewById(R.id.label);
        relationship = (EditChoiceWidget) mRoot.findViewById(R.id.relationship);
        nameEdit = (EditClearWidget) mRoot.findViewById(R.id.nameEdit);
        telEdit = (EditClearWidget) mRoot.findViewById(R.id.telEdit);
        repaymentAccount = (EditChoiceWidget) mRoot.findViewById(R.id.repaymentAccount);
        // checkbox = (CheckBox) mRoot.findViewById(R.id.checkbox);
        checkbox = (ImageButton) mRoot.findViewById(R.id.checkbox);
        agreement = (TextView) mRoot.findViewById(R.id.agreement);
        next = (Button) mRoot.findViewById(R.id.next);
    }

    @Override
    public void initData() {
        //		ButtonClickLock.getLock(click_more).lockDuration = 1000;
        ButtonClickLock.getLock(click_more).lockDuration = EloanConst.CLICK_MORE_TIME;
        CREDIT_CONTRACT = getString(R.string.boc_eloan_agreement_fir);
        LOAN_CONTRACT = getString(R.string.boc_eloan_agreement_sec);

        // 为组件添加数据
        spannableStringFir = new SpannableString(CREDIT_CONTRACT);
        spannableStringSec = new SpannableString(LOAN_CONTRACT);
        clickableSpanFir = new MClickableSpan(CREDIT_CONTRACT, getContext());
        clickableSpanSec = new MClickableSpan(LOAN_CONTRACT, getContext());
        clickableSpanFir.setColor(Color.RED);
        clickableSpanSec.setColor(Color.RED);
        spannableStringFir.setSpan(clickableSpanFir, 0, CREDIT_CONTRACT.length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringSec.setSpan(clickableSpanSec, 0, LOAN_CONTRACT.length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        agreement.setText(getString(R.string.boc_eloan_agreement_pre));
        agreement.append(spannableStringFir);
        agreement.append(getString(R.string.boc_eloan_agreement_and));
        agreement.append(spannableStringSec);
        agreement.append(getString(R.string.boc_eloan_agreement_end));
        agreement.setMovementMethod(LinkMovementMethod.getInstance());
        agreement.setLongClickable(false);

        // 初始化弹出框组件
        selectListDialog = new SelectListDialog(getContext());
        drawListAdapter = new DrawListAdapter(getContext());
        // 初始化弹出框组件列表适配器
        selectListDialog.setAdapter(drawListAdapter);

        name.setText(mEloanApplyModel.getCustomerName() + "（" + getString(R.string.boc_eloan_id)
                + NumberUtils.formatIDNumber(mEloanApplyModel.getIdentityNumber()) + "）");
        tel.setText(getString(R.string.boc_eloan_telEdit) + " "
                + NumberUtils.formatMobileNumberWithAsterrisk(mEloanApplyModel.getMobile()));
        addInfo.setText(getString(R.string.boc_eloan_addresslabel));
        district.setChoiceTextContent(getString(R.string.boc_eloan_choice));
        street.setEditWidgetHint(getString(R.string.boc_eloan_hint_street));
        label.setText(getString(R.string.boc_eloan_label));
        relationship.setChoiceTextContent(getString(R.string.boc_eloan_choice));
        // nameEdit.setEditWidgetHint(getString(R.string.boc_eloan_choice));
        nameEdit.setEditWidgetHint(getString(R.string.boc_eloan_apply_input));
        telEdit.setEditWidgetHint(getString(R.string.boc_eloan_apply_input));

        // 设置手机号输入框格式为数字
        telEdit.getContentEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        // 设置手机号输入的位数为11位
        telEdit.getContentEditText()
               .setFilters(new InputFilter[] { new InputFilter.LengthFilter(13) });

        repaymentAccount.setChoiceTextContent(getString(R.string.boc_eloan_choice));
        // checkbox.setChecked(true);
        hideSoftInput();
        district.setChoiceTitleBold(true);
        street.setChoiceTitleBold(true);
        relationship.setChoiceTitleBold(true);
        nameEdit.setChoiceTitleBold(true);
        telEdit.setChoiceTitleBold(true);
        repaymentAccount.setChoiceTitleBold(true);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    public void setListener() {
        checkbox.setOnClickListener(this);
        // 地区选择
        district.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("请选择".equals(district.getChoiceTextContent())) {
                    // 省市区联动查询
                    showLoadingDialog(false);
                    fragment = new DistrictSelectFragment();
                    getPresenter().queryDistrict(INIT_ZONE_CODE);
                } else {
                    // // TODO: 2016/8/25 如果不是第一次进入传递数据
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("First", false);
                    fragment.setArguments(bundle);
                    startForResult(fragment, DistrictSelectFragment.RequestCode);
                }
            }
        });
        // 关系选择
        relationship.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItem(DataUtils.getRelationShipList(), relationship);
            }
        });
        // 账户选择
        repaymentAccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 是否成功获取会话
                if (hasConv) {
                    AccoutFragment accoutFragment = new AccoutFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "apply");
                    accoutFragment.setArguments(bundle);
                    accoutFragment.setAccountType(AccountType.SELECT_REPAYMENTACCOUNT);
                    accoutFragment.setConversationId(conversationId);
                    startForResult(accoutFragment, AccoutFragment.RequestCode);
                }
            }
        });

        // 为组件添加实时格式化监听
        telEdit.getContentEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 实时格式化手机号方法调用
                PhoneNumberFormat.onEditTextChanged(s, start, before, telEdit.getContentEditText());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 为协议文字添加点击事件
        clickableSpanFir.setListener(new MClickableSpan.OnClickSpanListener() {
            @Override
            public void onClickSpan() {
                // 是否成功获取会话
                if (hasConv) {
                    ContractContentFragment contractContentFragment = new ContractContentFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "协议");
                    bundle.putString("content", creditContrac);
                    contractContentFragment.setArguments(bundle);
                    start(contractContentFragment);
                }
            }
        });

        // 为合同文字添加点击事件
        clickableSpanSec.setListener(new MClickableSpan.OnClickSpanListener() {
            @Override
            public void onClickSpan() {
                // 是否成功获取会话
                if (hasConv) {
                    if (!"请选择".equals(repaymentAccount.getChoiceTextContent())) {
                        ContractContentFragment contractContentFragment =
                                new ContractContentFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("type", "合同");
                        bundle.putString("content", loanContract);
                        bundle.putString("cbiCustName", mEloanApplyModel.getCustomerName());
                        bundle.putString("cbiCerNo",
                                NumberUtils.formatIDNumber(mEloanApplyModel.getIdentityNumber()));
                        // bundle.putString("cbiCerNo", NumberUtils
                        // .formatCardNumberStrong(mEloanApplyModel
                        // .getIdentityNumber()));
                        bundle.putString("cbiCustAccount",
                                NumberUtils.formatCardNumberStrong(account));
                        contractContentFragment.setArguments(bundle);
                        start(contractContentFragment);
                    } else {
                        showErrorDialog("请先选择还款账户");
                    }
                }
            }
        });
        // 下一步
        next.setOnClickListener(new OnClickListener() {
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
     * @date 2016-8-21 14:19:40
     */
    private void judge() {
        // 地区
        if ("请选择".equalsIgnoreCase(district.getChoiceTextContent()) || StringUtils.isEmpty(
                district.getChoiceTextContent())) {
            showErrorDialog("请选择地区");
            return;
        }
        // 街道信息
        if (StringUtils.isEmpty(street.getEditWidgetContent().trim())) {
            showErrorDialog("请输入街道信息");
            return;
        } else {
            // 正则校验
            RegexResult result1 = RegexUtils.check(mContext, ApplyRegexConst.MBDI_LM_ELOAN_STREET,
                    street.getEditWidgetContent().trim(), true);
            if (!result1.isAvailable) {
                showErrorDialog(result1.errorTips);
                return;
            }

            // 地区和街道信息
            // 正则校验
            RegexResult result2 = RegexUtils.check(mContext, ApplyRegexConst.MBDI_LM_ELOAN_ADDRESS,
                    street.getEditWidgetContent().trim() + district.getChoiceTextContent().trim(),
                    true);
            if (!result2.isAvailable) {
                showErrorDialog(result2.errorTips);
                return;
            }
        }
        // 关系
        if ("请选择".equals(relationship.getChoiceTextContent()) || StringUtils.isEmpty(
                relationship.getChoiceTextContent())) {
            showErrorDialog("请选择联系人关系");
            return;
        }
        // 姓名
        if (StringUtils.isEmpty(nameEdit.getEditWidgetContent())) {
            showErrorDialog("请输入联系人姓名");
            return;
        } else {
            // 正则校验
            RegexResult result1 =
                    RegexUtils.check(mContext, ApplyRegexConst.MBDI_LM_ELOAN_CONTACT_NAME,
                            nameEdit.getEditWidgetContent().trim(), true);
            if (!result1.isAvailable) {
                showErrorDialog(result1.errorTips);
                return;
            }
        }
        // 手机号
        if (StringUtils.isEmpty(telEdit.getEditWidgetContent())) {
            showErrorDialog("请输入联系人手机号");
            return;
        } else {
            // 正则校验
            // if (!NumberUtils.checkMobileNumber(telEdit.getEditWidgetContent()
            // .replace(" ", ""))) {
            // showErrorDialog("联系人手机号：11位数字");
            // return;
            // }
            // 正则校验
            RegexResult result1 = RegexUtils.check(mContext, ApplyRegexConst.MBDI_LM_ELOAN_MOBILE,
                    telEdit.getEditWidgetContent().replace(" ", ""), true);
            if (!result1.isAvailable) {
                showErrorDialog(result1.errorTips);
                return;
            }
        }
        // 还款账户
        if ("请选择".equals(repaymentAccount.getChoiceTextContent()) || StringUtils.isEmpty(
                repaymentAccount.getChoiceTextContent())) {
            showErrorDialog("请选择还款账户");
            return;
        }
        // 同意条款勾选按钮
        if (!checkbox.isSelected()) {
            showErrorDialog("请查看并勾选相关条款");
            return;
        }
        // 获取安全因子
        showLoadingDialog(false);
        getPresenter().getSecurityFactor();
        hideSoftInput();// yx add
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == AccoutFragment.ResultCode) {
            if (data != null) {
                Log.i(TAG, "-------->返回account------>" + data.get("account").toString() + data.get(
                        "accountId").toString());

                account = data.get("account").toString();
                accountId = data.get("accountId").toString();
                repaymentAccount.setChoiceTextContent(
                        NumberUtils.formatCardNumber(data.get("account").toString()));
            }
        }
        if (resultCode == DistrictSelectFragment.ResultCode) {
            if (data != null) {
                Log.i(TAG, "-------->返回districtInfo------>" + data.get("districtInfo").toString()
                        + data.get("zoneCode").toString());

                zoneCode = data.get("zoneCode").toString();
                district.setChoiceTextContent(data.get("districtInfo").toString());
            }
        }
    }

    @Override
    public void onDestroyView() {
        hideSoftInput();
        super.onDestroyView();
        getPresenter().unsubscribe();
    }

    /**
     * 为选项卡组件分配点击事件，点击弹出对话框公共组件
     *
     * @param list Dialog内的列表数据
     * @param view 点击的选项卡组件对象
     */
    private void onClickItem(List<String> list, final EditChoiceWidget view) {

        // selectListDialog.setTitle("选择期限");
        selectListDialog.setListData(list);
        selectListDialog.setOnSelectListener(new SelectListDialog.OnSelectListener() {
            @Override
            public void onSelect(int position, Object model) {
                if (null != model) {
                    view.setChoiceTextContent(model.toString());
                    selectListDialog.dismiss();
                }
            }
        });
        selectListDialog.show();
    }

    @Override
    public void obtainConversationSuccess(String conversationId) {
        Log.i(TAG, "-------->会话创建成功！");
        getPresenter().setConversationId(conversationId);

        hasConv = true;
        this.conversationId = conversationId;
        // applyConfirmInfoFragment.setConversationId(conversationId);

        // 协议接口调用
        CreditContractReq creditContractReq = new CreditContractReq();
        creditContractReq.setConversationId(conversationId);
        creditContractReq.setContractNo("");
        creditContractReq.seteLanguage("CHN");
        getPresenter().setCreditContractReq(creditContractReq);
        getPresenter().queryCreditContract();


    }

    @Override
    public void obtainConversationFail(ErrorException e) {
        Log.i(TAG, "-------->会话创建失败！");
        closeProgressDialog();
        hasConv = false;
    }

    @Override
    public void obtainSecurityFactorSuccess(SecurityFactorModel result) {
        Log.i(TAG, "-------->获取安全因子成功！");
        _combinId = securityVerity.getDefaultSecurityFactorId(result).getId();
        combinId_name = securityVerity.getDefaultSecurityFactorId(result).getName();
        LogUtils.i(TAG, "-------->获取安全因子成功！_combinId+combinId_name" + _combinId + combinId_name);
        // applyConfirmInfoFragment.set_combinId(_combinId);
        // //执行预交易接口
        buildVerifyReq();
    }

    @Override
    public void obtainSecurityFactorFail(ErrorException e) {
        Log.i(TAG, "-------->获取安全因子失败！");
        closeProgressDialog();
    }

    @Override
    public void obtainCreditContractSuccess(CreditContractRes result) {
        Log.i(TAG, "-------->获取征信协议成功！");
        creditContractId = result.getContractNo();
        // 征信合同内容
        creditContrac = result.getCreditContrac();
//        closeProgressDialog();
        // 合同接口调用
        LoanContractReq loanContractReq = new LoanContractReq();
        loanContractReq.setConversationId(conversationId);
        loanContractReq.setContractNo("");
        loanContractReq.seteLanguage("CHN");
        getPresenter().setLoanContractReq(loanContractReq);
        getPresenter().queryLoanContract();
        // contractContentFragment.setContent(result.getCreditContrac());
    }

    @Override
    public void obtainCreditContractFail(ErrorException e) {
        Log.i(TAG, "-------->获取征信协议失败！");
        closeProgressDialog();
    }

    @Override
    public void obtainLoanContractSuccess(LoanContractRes result) {
        Log.i(TAG, "-------->获取贷款合同成功！");
        loanContractId = result.getContractNo();
        closeProgressDialog();
        // 贷款合同内容
        loanContract = result.getLoanContract();
        // contractContentFragment.setContent(result.getLoanContract());
    }

    @Override
    public void obtainLoanContractFail(ErrorException e) {
        Log.i(TAG, "-------->获取贷款合同失败！");
        closeProgressDialog();
    }

    @Override
    public void preLoanRegisterVerifySuccess(PreRegisterVerifyRes result) {
        Log.i(TAG, "-------->预交易接口调用成功！");
        closeProgressDialog();
        // 音频key加密
        EShieldVerify.getInstance(getActivity()).setmPlainData(result.get_plainData());
        applyConfirmInfoFragment = new ApplyConfirmInfoFragment();

        applyConfirmInfoFragment.set_combinId(_combinId);
        applyConfirmInfoFragment.set_combinId_Name(combinId_name);
        applyConfirmInfoFragment.setConversationId(conversationId);
        available = securityVerity.confirmFactor(result.getFactorList());
        applyConfirmInfoFragment.setAvailable(available);

        Bundle bundle = new Bundle();
        bundle.putSerializable("PreRegisterVerifyReq", req);
        applyConfirmInfoFragment.setArguments(bundle);

        start(applyConfirmInfoFragment);
        LogUtils.i("cq---------->激活传过去req------" + req.toString());
        LogUtils.i("cq-------available-------->" + available);
    }

    @Override
    public void preLoanRegisterVerifyFail(ErrorException e) {
        Log.i(TAG,
                "-------->预交易接口调用失败！" + e.getErrorCode() + e.getErrorType() + e.getErrorMessage());
        closeProgressDialog();
    }

    @Override
    public void setPresenter(ApplyContract.Presenter presenter) {

    }

    /**
     * 构造预交易请求参数
     */
    private void buildVerifyReq() {
        req = new PreRegisterVerifyReq();
        req.setConversationId(conversationId);
        req.set_combinId(_combinId);
        req.setLoanPrdNo("OC-LOAN");
        req.setCustName(mEloanApplyModel.getCustomerName());
        req.setCerType(mEloanApplyModel.getIdentityType());
        req.setCerNo(mEloanApplyModel.getIdentityNumber());
        req.setZoneCode(zoneCode);
        req.setMobile(mEloanApplyModel.getMobile());
        req.setStreetInfo(street.getEditWidgetContent().trim());
        req.setLinkAddress(district.getChoiceTextContent() + street.getEditWidgetContent().trim());
        req.setLinkRelation(getLinkRelationN(relationship.getChoiceTextContent()));
        req.setLinkName(nameEdit.getEditWidgetContent());
        req.setLinkMobile(telEdit.getEditWidgetContent().replace(" ", ""));
        req.setLoanRepayAccountId(accountId);
        req.setLoanRepayAccount(account);
        req.setContractFormId(loanContractId);
        req.setApplyQuotet(BigDecimal.valueOf(Double.parseDouble(mEloanApplyModel.getQuote())));
        req.setCurrencyCode(mEloanApplyModel.getCurrency());
        req.setThreeContractNo(creditContractId);
        req.seteLanguage("CHN");

        // applyConfirmInfoFragment.setPreRegisterVerifyReq(preRegisterVerifyReq);

        getPresenter().setPreRegisterVerifyReq(req);
        showLoadingDialog(false);
        // 调用预交易接口
        getPresenter().preLoanRegisterVerify();
    }

    private String getLinkRelationN(String type) {
        // 联系关系01-父母 02-配偶 03-兄弟 04-姐妹
        if ("父母".equals(type)) {
            return "01";
        } else if ("配偶".equals(type)) {
            return "02";
        } else if ("兄弟".equals(type)) {
            return "03";
        } else if ("姐妹".equals(type)) {
            return "04";
        } else {
            return "02";
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.checkbox) {
            checkbox.setSelected(!checkbox.isSelected());
        }
    }

    /**
     * 省市区联动查询成功
     */
    @Override
    public void obtainDistrictSuccess(List<DistrictRes> result) {
        closeProgressDialog();
        ArrayList<DistrictRes> zoneList = (ArrayList<DistrictRes>) result;
        Bundle bundle = new Bundle();
        bundle.putBoolean("First", true);
        bundle.putParcelableArrayList("ZoneList", zoneList);
        // 跳转到选择地区页面

        fragment.setArguments(bundle);
        startForResult(fragment, DistrictSelectFragment.RequestCode);
    }

    /**
     * 省市区联动查询失败
     */
    @Override
    public void obtainDistrictFail(ErrorException e) {
        closeProgressDialog();
        showErrorDialog(e.getErrorMessage());
    }

    @Override
    protected ApplyPresenter initPresenter() {
        return new ApplyPresenter((ApplyContract.ApplyView) this).setDistrictSelectView(this);
    }
}
