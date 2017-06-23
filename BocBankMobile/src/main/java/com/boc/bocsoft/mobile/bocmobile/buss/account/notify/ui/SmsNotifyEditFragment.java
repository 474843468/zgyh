package com.boc.bocsoft.mobile.bocmobile.buss.account.notify.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PhoneNumberFormat;
import com.boc.bocsoft.mobile.bocmobile.base.widget.assignment.SelectAgreementView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SmsVerifyView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditClearWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.contact.SmsNotifyEditContact;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.model.SmsNotifyEditModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.notify.presenter.SmsNotifyEditPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.utils.regex.RegexResult;
import com.boc.bocsoft.mobile.framework.utils.regex.RegexUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 开通短信通知、添加手机号、修改短信通知页面
 * Created by wangtong on 2016/6/15.
 */
public class SmsNotifyEditFragment extends BussFragment implements SmsNotifyEditContact.View,
        SmsVerifyView.SmsActionListener {
    //是否为编辑模式key
    public static final String KEY_IS_EDIT = "is_edit";
    //数据模型key
    public static final String KEY_MODEL = "model";
    //是否首次开通
    public static final String KEY_IS_FIRST = "is_first_open";
    //旧数据模型
    public static final String KEY_OLD_MODEL = "old_model";
    //根布局
    protected View rootView;
    //接受验证码的手机号码
    protected EditClearWidget phoneNum;
    //输入手机验证码
    protected EditText verifyCode;
    // 获取手机验证码按钮
    protected SmsVerifyView btnVerifyCode;
    // 通知最小金额
    protected EditMoneyInputWidget minMoney;
    // 通知最大金额
    protected EditMoneyInputWidget maxMoney;
    // 缴费账号
    protected EditChoiceWidget feeAccount;
    //费率
    protected TextView feeRate;
    //下一步
    protected TextView btnNext;
    //    protected CheckBox acceptRule;
//    protected TextView userLimit;
    protected LinearLayout availableGroup;
    protected RelativeLayout smsGroup;
    protected LinearLayout groupFeeStand;
    protected SelectAgreementView agree_view;
    //业务逻辑处理
    private SmsNotifyEditPresenter presenter;
    //数据模型
    private SmsNotifyEditModel model;
    //旧编辑数据
    private SmsNotifyEditModel oldModel;
    //是否为修改模式
    private boolean isEdit = true;
    protected TextView fragmentTitle;
    protected ImageView btnBack;
    private boolean isFirstOpen = false;
    private boolean isSmsSended = false;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_sms_edit, null);
        return rootView;
    }

    @Override
    public void initView() {
        phoneNum = (EditClearWidget) rootView.findViewById(R.id.phone_num);
        phoneNum.getContentEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
        phoneNum.getContentEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        phoneNum.getContentEditText().addTextChangedListener(new Watcher());
        verifyCode = (EditText) rootView.findViewById(R.id.verify_code);
        btnVerifyCode = (SmsVerifyView) rootView.findViewById(R.id.btn_verify_code);
        minMoney = (EditMoneyInputWidget) rootView.findViewById(R.id.min_money);
        minMoney.setMaxLeftNumber(11);
        maxMoney = (EditMoneyInputWidget) rootView.findViewById(R.id.max_money);
        maxMoney.setMaxLeftNumber(11);
        feeAccount = (EditChoiceWidget) rootView.findViewById(R.id.fee_account);
        feeRate = (TextView) rootView.findViewById(R.id.fee_rate);
        btnNext = (TextView) rootView.findViewById(R.id.btn_next);
//        acceptRule = (CheckBox) rootView.findViewById(R.id.accept_rule);
//        userLimit = (TextView) rootView.findViewById(R.id.user_limit);
        availableGroup = (LinearLayout) rootView.findViewById(R.id.available_group);
        smsGroup = (RelativeLayout) rootView.findViewById(R.id.sms_group);
        fragmentTitle = (TextView) rootView.findViewById(R.id.fragment_title);
        btnBack = (ImageView) rootView.findViewById(R.id.btn_back);
        groupFeeStand = (LinearLayout) rootView.findViewById(R.id.group_fee_stand);
        agree_view = (SelectAgreementView) rootView.findViewById(R.id.agree_view);
    }

    @Override
    public void initData() {
        super.initData();
        initUserLimit();
        isEdit = getArguments().getBoolean(KEY_IS_EDIT, true);
        model = getArguments().getParcelable(KEY_MODEL);
        if (isEdit) {
            fragmentTitle.setText("修改短信通知");
            oldModel = new SmsNotifyEditModel(model);
            phoneNum.setEditWidgetContent(model.getPhoneNum());
            minMoney.setmContentMoneyEditText(model.getMinMoney());
            maxMoney.setmContentMoneyEditText(model.getMaxMoney());
            feeAccount.setChoiceTextContent(NumberUtils.formatCardNumber(model.getFeeAccount().getAccountNumber()));
            availableGroup.setVisibility(View.GONE);
            smsGroup.setVisibility(View.GONE);
            isFirstOpen = false;
            model.setServiceId("PB175C");
        } else {
            availableGroup.setVisibility(View.VISIBLE);
            isFirstOpen = getArguments().getBoolean("isFirstOpen");
            if (isFirstOpen) {
                fragmentTitle.setText("开通短信通知");
                ArrayList<String> accountType = new ArrayList<>();
                accountType.add("101");
                accountType.add("119");
                accountType.add("188");
                List<AccountBean> feeAccountList = ApplicationContext.getInstance().getChinaBankAccountList(accountType);
                //TODO   由于不显示缴费账户，先将if else 代码注销。lgw
//                if (model.isStatus()) {
                if (StringUtils.isEmpty(model.getFeeAccount().getAccountNumber())) {
                    model.setFeeAccount(feeAccountList.get(0));
                } else {
                    model.setFeeAccount(model.getFeeAccount().getAccountNumber());
                }
                // 首次开通缴费账号和收费标准才显示
                groupFeeStand.setVisibility(View.VISIBLE);
                feeRate.setText(getString(R.string.boc_account_fee_type, "2.00"));
                feeAccount.setVisibility(View.VISIBLE);
                feeAccount.setChoiceTextContent(NumberUtils.formatCardNumber(model.getFeeAccount().getAccountNumber()));
//                } else {
//                }
                model.setServiceId("PB172C");
            } else {
                feeAccount.setVisibility(View.GONE);
                groupFeeStand.setVisibility(View.GONE);
                fragmentTitle.setText("添加手机号");
                model.setServiceId("PB173C");
            }
// TODO 测试说要显示金额，注释if else lgw
//            if (model.getFeeRate().equals("0")) {
//                groupFeeStand.setVisibility(View.GONE);
//            } else {
//            feeRate.setText(getString(R.string.boc_account_fee_type, model.getFeeRate()));
//            }
        }
        updateViews();
        model.setConversitionId(AccSmsNotifyHomeFragment.conversationId);
        presenter = new SmsNotifyEditPresenter(this);
    }

    @Override
    public void setListener() {
        super.setListener();
        btnVerifyCode.setOnSmsActionListener(this);

        feeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> accountType = new ArrayList<>();
                accountType.add("101");
                accountType.add("119");
                accountType.add("188");

                SelectAccoutFragment fragment = new SelectAccoutFragment();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(SelectAccoutFragment.ACCOUNT_TYPE_LIST, accountType);
                fragment.setArguments(bundle);
                startForResult(fragment, SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkViews()) {
                    return;
                }

                if (!isEdit) {
                    presenter.psnSsmMessageAdd();
                } else {
                    startConfirmFragment();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput();
                pop();
            }
        });

    }


    private void updateViews() {
        phoneNum.setEditWidgetContent(NumberUtils.formatMobileNumber(model.getPhoneNum()));
        minMoney.setmContentMoneyEditText(MoneyUtils.transMoneyFormat(model.getMinMoney(), "001"));
        maxMoney.setmContentMoneyEditText(MoneyUtils.transMoneyFormat(model.getMaxMoney(), "001"));
        feeAccount.setChoiceTextContentHint("请选择");
    }

    private boolean checkViews() {

        String mobileNum = phoneNum.getEditWidgetContent().toString().trim().replace(" ", "");
        RegexResult phoneResult = RegexUtils.check(getContext(), "account_sms_notice_mobile", mobileNum, true);
        if (phoneResult.isAvailable) {
            model.setPhoneNum(mobileNum);
        } else {
            showErrorDialog(phoneResult.errorTips);
            return false;
        }

        if (!isEdit && findFragment(AccSmsNotifyHomeFragment.class).isMobileOpenned(model.getPhoneNum())) {
            showErrorDialog("该手机号已开通动户通知，请更换其他手机号接收短信");
            return false;
        }

        if (smsGroup.getVisibility() == View.VISIBLE) {
            if (isSmsSended) {
                String passCode = verifyCode.getText().toString().trim().replace(" ", "");
                RegexResult passResult = RegexUtils.check(getContext(), "account_sms_notice_password", passCode, true);
                if (passResult.isAvailable) {
                    model.setVerifyCode(verifyCode.getText().toString());
                } else {
                    showErrorDialog(passResult.errorTips);
                    return false;
                }
            } else {
                showErrorDialog("未获取手机验证码");
                return false;
            }

        }

        if (!TextUtils.isEmpty(maxMoney.getContentMoney())) {
            model.setMaxMoney(maxMoney.getContentMoney());
        } else {
            showErrorDialog("最大金额不能为空");
            return false;
        }

        if (!TextUtils.isEmpty(minMoney.getContentMoney())) {
            model.setMinMoney(minMoney.getContentMoney());
        } else {
            showErrorDialog("最小金额不能为空");
            return false;
        }

        Double min = Double.parseDouble(minMoney.getContentMoney());
        Double max = Double.parseDouble(maxMoney.getContentMoney());
        if (min >= max) {
            showErrorDialog("最大金额应大于最小金额");
            return false;
        }

        if (feeAccount.getVisibility() == View.VISIBLE && isFirstOpen && TextUtils.isEmpty(feeAccount.getChoiceTextContent())) {
            showErrorDialog("缴费账户不能为空");
            return false;
        }

        if (!agree_view.isSelected() && !isEdit) {
            showErrorDialog("您还没有同意并勾选服务协议书");
            return false;
        }

        return true;
    }

    private void initUserLimit() {
        String limitString = getString(R.string.boc_account_user_limit);
//        SpannableStringBuilder style = new SpannableStringBuilder(limitString);
//        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.boc_text_color_red)),
//                14, 39, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//        style.setSpan(new MyClickableSpan(), 14, 39, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        agree_view.setAgreement(limitString.substring(14, 39),
                limitString.substring(0, 14),
                limitString.substring(39, limitString.length()));
        agree_view.setOnClickContractListener(new SelectAgreementView.OnClickContractListener() {
            @Override
            public void onClickContract(int index) {
                startUserLimit();
            }
        });
//        userLimit.setMovementMethod(LinkMovementMethod.getInstance());
//        userLimit.setText(style);
    }

    private void startUserLimit() {
        UserLimitFragment fragment = new UserLimitFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userName", model.getUserName());
        fragment.setArguments(bundle);
        start(fragment);
    }

    @Override
    public void smsSendReturned() {

    }

    @Override
    public SmsNotifyEditModel getUiModel() {
        return model;
    }

    @Override
    public void psnSsmMessageAddReturned() {
        startConfirmFragment();
    }


    @Override
    public void setPresenter(SmsNotifyEditContact.Presenter presenter) {

    }

    private void startConfirmFragment() {
        ConfirmInfoFragment fragment = new ConfirmInfoFragment();
        Bundle bundle = new Bundle();
        if (isEdit) {
            bundle.putParcelable(SmsNotifyEditFragment.KEY_OLD_MODEL, oldModel);
        }
        bundle.putParcelable(SmsNotifyEditFragment.KEY_MODEL, model);
        bundle.putBoolean(SmsNotifyEditFragment.KEY_IS_EDIT, isEdit);
        bundle.putBoolean(SmsNotifyEditFragment.KEY_IS_FIRST, isFirstOpen);
        fragment.setArguments(bundle);
        start(fragment);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unsubscribe();
    }

    @Override
    public void sendSms() {
        String mobileNum = phoneNum.getEditWidgetContent().toString().trim().replace(" ", "");
        RegexResult phoneResult = RegexUtils.check(getContext(), "account_sms_notice_mobile", mobileNum, true);
        if (phoneResult.isAvailable) {
            model.setPhoneNum(mobileNum);
            presenter.psnSsmSend(!isEdit);
        } else {
            btnVerifyCode.stopCountDown();
            showErrorDialog(phoneResult.errorTips);
        }
        isSmsSended = true;
    }

    @Override
    public void onSmsReceived(String code) {
        verifyCode.setText(code);
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT == requestCode && data != null) {
            AccountBean accountBean = data.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
            model.setFeeAccount(accountBean);
            feeAccount.setChoiceTextContent(NumberUtils.formatCardNumber(model.getFeeAccount().getAccountNumber()));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        btnVerifyCode.stopCountDown();
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

//    class MyClickableSpan extends ClickableSpan {
//
//        @Override
//        public void onClick(View widget) {
//            startUserLimit();
//        }
//
//        @Override
//        public void updateDrawState(TextPaint ds) {
//            ds.setUnderlineText(false);
//        }
//    }

    class Watcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // 输入的联系人进行格式化
            PhoneNumberFormat.onEditTextChanged(s, start, before, phoneNum.getContentEditText());
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (isEdit) {
                String str = s.toString().trim().replace(" ", "");
                if (str.length() == 11 && !str.equals(model.getPhoneNum())) {
                    smsGroup.setVisibility(View.VISIBLE);
                } else {
                    smsGroup.setVisibility(View.GONE);
                    if (isSmsSended) {
                        btnVerifyCode.stopCountDown();
                    }
                }
            }
        }
    }

}
