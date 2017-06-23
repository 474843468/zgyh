package com.boc.bocsoft.mobile.bocmobile.buss.transfer.mobileremit.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.SecurityViewModel;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PhoneNumberFormat;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditClearWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.MenuFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.mobileremit.model.MobileRemitViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.mobileremit.presenter.MobileRemitPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.bocmobile.yun.BocCloudCenter;
import com.boc.bocsoft.mobile.bocmobile.yun.other.AccountType;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.utils.regex.RegexResult;
import com.boc.bocsoft.mobile.framework.utils.regex.RegexUtils;
import com.cfca.mobile.log.CodeException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cfca.mobile.sip.CFCASipDelegator;
import cfca.mobile.sip.SipBox;

/**
 * 汇往取款人
 * Created by liuweidong on 2016/7/12.
 */
public class MobileRemitFragment extends MvpBussFragment<MobileRemitContract.Presenter> implements MobileRemitContract.View,
        View.OnClickListener, CFCASipDelegator {
    private View rootView;
    private LinearLayout llParentView;
    private EditChoiceWidget editChoiceAccount;// 转出账户
    private LinearLayout llBalance;
    private EditMoneyInputWidget editMoneyInput;// 取款金额
    private TextView txtBalance;// 可用余额
    private SipBox sipBoxPassword;// 取款密码
    private SipBox sipBoxPasswordAgain;// 再次确认取款密码
    private EditClearWidget editClearName;// 收款人姓名
    private EditClearWidget editClearMobile;// 收款人手机号
    private EditClearWidget editClearComment;// 附言
    private Button btnConfirm;// 确定
    private TextView txtAgent;

    private static MobileRemitViewModel mViewModel;// View层数据
    private List<AccountBean> accountList;// 筛选后的账户列表
    private AccountBean curAccountBean;// 当前选中的账户数据
    private AccountBean againAccount;
    private CombinListBean defaultCombin;// 默认的安全因子
    private SelectAccoutFragment selectAccoutFragment;// 选择账户页面
    private Bundle accountBundle;// 当前选中的账户
    private LayoutInflater mInflater;
    private String phone;
    private boolean isAgain = false;// 是否再取一笔
    /**
     * 偏移量
     */
    private int offset = 0;
    private Handler handler = new Handler();

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        this.mInflater = mInflater;
        rootView = View.inflate(mContext, R.layout.boc_fragment_transfer_mobile_remit, null);
        return rootView;
    }

    @Override
    public void initView() {
        llParentView = (LinearLayout) rootView.findViewById(R.id.ll_parent_view);
        editChoiceAccount = (EditChoiceWidget) rootView.findViewById(R.id.choice_account);
        llBalance = (LinearLayout) rootView.findViewById(R.id.ll_balance);
        editMoneyInput = (EditMoneyInputWidget) rootView.findViewById(R.id.money_input);
        txtBalance = (TextView) rootView.findViewById(R.id.txt_balance);
        sipBoxPassword = (SipBox) rootView.findViewById(R.id.sipbox_password);
        sipBoxPasswordAgain = (SipBox) rootView.findViewById(R.id.sipbox_password_again);
        editClearName = (EditClearWidget) rootView.findViewById(R.id.edit_clear_name);
        editClearMobile = (EditClearWidget) rootView.findViewById(R.id.edit_clear_mobile);
        editClearComment = (EditClearWidget) rootView.findViewById(R.id.edit_clear_comment);
        btnConfirm = (Button) rootView.findViewById(R.id.btn_confirm);
        txtAgent = (TextView) rootView.findViewById(R.id.txt_agent_info);
        mTitleBarView.setRightButton(getResources().getDrawable(R.drawable.boc_overview_more));
        editClearMobile.showEditWidgetRightImage(true);
        editClearMobile.setClearEditRightImage(R.drawable.boc_callbook);
        /*代理点*/
        String str = getResources().getString(R.string.boc_transfer_mobile_remit_agent_info);
        int start = str.indexOf("代理点");
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        txtAgent.setMovementMethod(LinkMovementMethod.getInstance());
        style.setSpan(new TextViewClickable(), start, start + "代理点".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtAgent.setText(style);

        setCFCAAttribute();
        setWidgetAttribute();
    }

    @Override
    public void initData() {
        accountBundle = null;
        againAccount = curAccountBean;
        setDefaultFocus();
        cleanViewData();
        curAccountBean = new AccountBean();
        if (mViewModel == null) {
            mViewModel = new MobileRemitViewModel();
        }
        /*账户列表*/
        accountList = ApplicationContext.getInstance().getChinaBankAccountList(filterAccountType());
        if (accountList.size() == 0) {// 无可操作账户
            showErrorDialog(getResources().getString(R.string.boc_common_no_account));
            return;
        }
        if (isAgain) {// 再取一笔
            isAgain = false;
            curAccountBean = againAccount;
        } else {
            String accountId = BocCloudCenter.getInstance().getAccountId(AccountType.ACC_TYPE_PHONE_DRAW);
            if (!StringUtils.isEmptyOrNull(accountId)) {// 数据库存在最近操作账户
                boolean haveAccount = false;
                for (int i = 0; i < accountList.size(); i++) {
                    String sha256String = BocCloudCenter.getSha256String(accountList.get(i).getAccountId());
                    if (accountId.equals(sha256String)) {
                        haveAccount = true;
                        curAccountBean = accountList.get(i);
                        break;
                    }
                }
                if (!haveAccount) {
                    curAccountBean = accountList.get(0);
                }
            } else {
                curAccountBean = accountList.get(0);
            }
        }
        queryCurAccountDetails();// 查询当前账户详情
    }

    @Override
    public void setListener() {
        editChoiceAccount.setOnClickListener(this);
        txtAgent.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        editClearMobile.setRightImageCallBack(new EditClearWidget.EditRightImageOnClick() {
            @Override
            public void onClick() {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });
        editClearMobile.getContentEditText().addTextChangedListener(new Watcher());

        editMoneyInput.setOnKeyBoardListener(new EditMoneyInputWidget.KeyBoardDismissOrShowCallBack() {
            @Override
            public void onKeyBoardDismiss() {

            }

            @Override
            public void onKeyBoardShow() {
                sipBoxPassword.hideSecurityKeyBoard();
                sipBoxPasswordAgain.hideSecurityKeyBoard();
            }
        });
    }

    @Override
    public void reInit() {
        isAgain = true;
        mContentView.removeAllViews();
        View contentView = onCreateView(mInflater);
        if (contentView != null) {
            mContentView.addView(contentView);
        }
        initView();
        initData();
        setListener();
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_transfer_mobile_remit_title);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return true;
    }

    @Override
    protected void titleRightIconClick() {
        Bundle bundle = new Bundle();
        bundle.putInt("Menu", MenuFragment.MOBILE_REMIT);
        MenuFragment fragment = new MenuFragment();
        fragment.setArguments(bundle);
        start(fragment);
    }

    @Override
    protected MobileRemitContract.Presenter initPresenter() {
        return new MobileRemitPresenter(this, false);
    }

    /**
     * view层model
     *
     * @return
     */
    public static MobileRemitViewModel getViewModel() {
        return mViewModel;
    }

    /**
     * 设置页面的默认焦点
     */
    private void setDefaultFocus() {
        editChoiceAccount.getChoiceNameTextView().setFocusable(true);
        editChoiceAccount.getChoiceNameTextView().setFocusableInTouchMode(true);
        editChoiceAccount.getChoiceNameTextView().requestFocus();
    }

    /**
     * 过滤账户类型
     */
    private ArrayList<String> filterAccountType() {
        /*借记卡 119 普通活期 101 活期一本通 188*/
        ArrayList<String> accountTypeList = new ArrayList<String>();
        accountTypeList.add(ApplicationConst.ACC_TYPE_BRO);
        accountTypeList.add(ApplicationConst.ACC_TYPE_ORD);
        accountTypeList.add(ApplicationConst.ACC_TYPE_RAN);
        return accountTypeList;
    }

    /**
     * 设置CFCA密码键盘属性
     */
    private void setCFCAAttribute() {
        //设置为国密算法
        sipBoxPassword.setCipherType(ApplicationConst.CIPHERTYPE);
        //输入密码设置的加密类型
        sipBoxPassword.setOutputValueType(ApplicationConst.OUT_PUT_VALUE_TYPE_OTP);
        //设置为全键盘 0 全键盘  1 数字键盘
        sipBoxPassword.setKeyBoardType(SipBox.NUMBER_KEYBOARD);
        //设置最小输入长度
        sipBoxPassword.setPasswordMinLength(ApplicationConst.MIN_LENGTH - 1);
        //设置最大输入长度
        sipBoxPassword.setPasswordMaxLength(ApplicationConst.MIN_LENGTH);
        sipBoxPassword.setSipDelegator(this);

        //设置为国密算法
        sipBoxPasswordAgain.setCipherType(ApplicationConst.CIPHERTYPE);
        //输入密码设置的加密类型
        sipBoxPasswordAgain.setOutputValueType(ApplicationConst.OUT_PUT_VALUE_TYPE_OTP);
        //设置为全键盘 0 全键盘  1 数字键盘
        sipBoxPasswordAgain.setKeyBoardType(SipBox.NUMBER_KEYBOARD);
        //设置最小输入长度
        sipBoxPasswordAgain.setPasswordMinLength(ApplicationConst.MIN_LENGTH - 1);
        //设置最大输入长度
        sipBoxPasswordAgain.setPasswordMaxLength(ApplicationConst.MIN_LENGTH);
        sipBoxPasswordAgain.setSipDelegator(this);
    }

    /**
     * 设置其他组件属性
     */
    private void setWidgetAttribute() {
        // 设置输入金额的属性
        editMoneyInput.getContentMoneyEditText().setHint(getResources().getString(R.string.boc_transfer_hint_money));
        editMoneyInput.getContentMoneyEditText().setTextColor(getResources().getColor(R.color.boc_text_color_red));
        editMoneyInput.setMaxLeftNumber(11);
        editMoneyInput.setMaxRightNumber(2);
        // 设置输入手机号属性
        editClearMobile.getContentEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
        editClearMobile.getContentEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    /**
     * 清空页面数据
     */
    private void cleanViewData() {
        editChoiceAccount.setChoiceTextContent(getResources().getString(R.string.boc_common_select));// 设置默认（请选择）
        txtBalance.setText("");// 可用余额
        editMoneyInput.setmContentMoneyEditText("");// 汇款金额
        editClearName.setEditWidgetContent("");// 收款人名称
        editClearMobile.setEditWidgetContent("");// 收款人手机号
        sipBoxPassword.setText("");// 预留密码
        sipBoxPasswordAgain.setText("");// 确认密码
        editClearComment.setEditWidgetContent("");// 附言
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.choice_account) {// 选择账户
            if (accountList.size() == 0) {
                showErrorDialog(getResources().getString(R.string.boc_common_no_account));
                return;
            }
            selectAccoutFragment = SelectAccoutFragment.newInstance(filterAccountType());
            selectAccoutFragment.isRequestNet(true);
            startForResult(selectAccoutFragment, SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT);
            selectAccoutFragment.setOnItemListener(new SelectAccoutFragment.ItemListener() {
                @Override
                public void onItemClick(Bundle bundle) {
                    MobileRemitFragment.this.accountBundle = bundle;
                    AccountBean item = MobileRemitFragment.this.accountBundle.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
                    // 保存当前账户信息
                    curAccountBean = item;
                    // 查询当前账户详情
                    queryCurAccountDetails();
                }
            });
        } else if (i == R.id.btn_confirm) {// 确定
            if (checkViewRegex()) {
                showLoadingDialog(false);
                getPresenter().psnTransQuotaquery();
//                getPresenter().querySecurityFactor();// 查询安全因子
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            final List<String> phoneList = PhoneNumberFormat.insertPhonenumber1(mActivity, data);
            List<String> phoneNumList = new ArrayList<String>(); // 手机号码列表
            // 截取字符串,构建一个纯的号码列表
            for (String phone : phoneList) {
                phoneNumList.add(PhoneNumberFormat.getPhoneNum(phone));
            }

            phone = "";

            if (phoneList != null) {
                if (phoneList.size() == 1) {
                    phone = phoneList.get(0);
                    editClearMobile.setEditWidgetContent(PhoneNumberFormat.getPhoneNum(phone));

                    // 判断是否需要带回name
                    String name = editClearName.getEditWidgetContent();
                    if (TextUtils.isEmpty(name)) {
                        editClearName.setEditWidgetContent(PhoneNumberFormat.getPhoneName(phone));
                    }
                    return;
                }

                if (phoneList.size() > 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    int size = phoneList.size();
                    builder.setTitle("请选择一个号码").setItems(phoneNumList.toArray(new String[size]), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int position = which;
                            phone = phoneList.get(position);
                            editClearMobile.setEditWidgetContent(PhoneNumberFormat.getPhoneNum(phone));

                            // 判断是否需要带回name
                            String name = editClearName.getEditWidgetContent();
                            if (TextUtils.isEmpty(name)) {
                                editClearName.setEditWidgetContent(PhoneNumberFormat.getPhoneName(phone));
                            }
                        }
                    }).create().show();
                }
            }
        }
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (resultCode == SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT) {
            AccountBean item = data.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
            // 设置选中账户
            editChoiceAccount.setChoiceTextContent(NumberUtils.formatCardNumber(item.getAccountNumber()));
            // 保存当前账户信息
            curAccountBean = item;
        }
    }

    /**
     * 页面校验
     */
    private boolean checkViewRegex() {
        if (getResources().getString(R.string.boc_common_select).equals(editChoiceAccount.getChoiceTextContent())) {
            showErrorDialog(getResources().getString(R.string.boc_transfer_message_remit_account_empty));
            return false;
        }

        // 汇款金额
        RegexResult amountResult = RegexUtils.check(mContext, "mbdi_tr_mobilewith_amount",
                editMoneyInput.getContentMoney(), true);
        if (!amountResult.isAvailable) {
            showErrorDialog(amountResult.errorTips);
            return false;
        }
        if (BigDecimal.valueOf(Double.valueOf(editMoneyInput.getContentMoney())).compareTo(mViewModel.getAvailableBalance()) == 1) {
            showErrorDialog(getResources().getString(R.string.boc_transfer_message_remit_money_overtop));
            return false;
        }

        // 收款人名称
        RegexResult nameResult = RegexUtils.check(mContext, "mbdi_tr_mobilewith_payeeacctname",
                editClearName.getEditWidgetContent().trim(), true);
        if (!nameResult.isAvailable) {
            showErrorDialog(nameResult.errorTips);
            return false;
        }

        // 收款人手机号
        RegexResult mobileResult = RegexUtils.check(mContext, "phone_trans_no_mobile",
                editClearMobile.getEditWidgetContent().replaceAll("\\s*", ""), true);
        if (!mobileResult.isAvailable) {
            showErrorDialog(mobileResult.errorTips);
            return false;
        }

        if (sipBoxPassword.getText().toString().isEmpty()) {
            showErrorDialog("预留密码不能为空");
            return false;
        } else if (sipBoxPassword.getText().toString().length() != 6) {
            showErrorDialog("预留密码：6位数字");
            return false;
        }
        if (sipBoxPasswordAgain.getText().toString().isEmpty()) {
            showErrorDialog("确认密码不能为空");
            return false;
        } else if (sipBoxPasswordAgain.getText().toString().length() != 6) {
            showErrorDialog("确认密码：6位数字");
            return false;
        }

//        RegexResult passwordResult = RegexUtils.check(mContext, "mbdi_tr_mobilewith_password",
//                sipBoxPassword.getText().toString(), true);
//        if (!passwordResult.isAvailable) {
//            showErrorDialog(passwordResult.errorTips);
//            return false;
//        }
//
//        RegexResult passwordConfirmResult = RegexUtils.check(mContext, "mbdi_tr_mobilewith_password_confirm",
//                sipBoxPasswordAgain.getText().toString(), true);
//        if (!passwordConfirmResult.isAvailable) {
//            showErrorDialog(passwordConfirmResult.errorTips);
//            return false;
//        }

        // 附言
        RegexResult remarkResult = RegexUtils.check(mContext, "mbdi_common_remark",
                editClearComment.getEditWidgetContent().trim(), false);
        if (!remarkResult.isAvailable) {
            showErrorDialog(remarkResult.errorTips);
            return false;
        }
        return true;
    }

//    /**
//     * 保存当前账户信息
//     */
//    private void saveCurAccountInfo(AccountBean accountBean) {
//        curAccountBean.setAccountId(accountBean.getAccountId());
//        curAccountBean.setAccountNumber(accountBean.getAccountNumber());
//        curAccountBean.setAccountType(accountBean.getAccountType());
//    }

    /**
     * 封装页面数据
     */
    private void buildViewModel() {
        mViewModel.setMoney(BigDecimal.valueOf(Double.valueOf(editMoneyInput.getContentMoney())));// 汇款金额
        mViewModel.setPayeeName(editClearName.getEditWidgetContent());// 收款人名称
        mViewModel.setPayeeMobile(editClearMobile.getEditWidgetContent().replaceAll("\\s*", ""));// 收款人手机号
        mViewModel.setRemark(editClearComment.getEditWidgetContent());// 附言
        //设置密码控件随机数
        sipBoxPassword.setRandomKey_S(MobileRemitPresenter.randomID);
        sipBoxPasswordAgain.setRandomKey_S(MobileRemitPresenter.randomID);
        try {
            mViewModel.setWithDrawPwd(sipBoxPassword.getValue().getEncryptPassword());
            mViewModel.setWithDrawPwd_RC(sipBoxPassword.getValue().getEncryptRandomNum());
            mViewModel.setWithDrawPwdConf(sipBoxPasswordAgain.getValue().getEncryptPassword());
            mViewModel.setWithDrawPwdConf_RC(sipBoxPasswordAgain.getValue().getEncryptRandomNum());
        } catch (CodeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beforeKeyboardShow(SipBox sipBox, int keyboardHeigt) {
        if (offset < 0) {
            handler.removeCallbacksAndMessages(null);
            return;
        }
        int id = rootView.findFocus().getId();
        if (id == R.id.sipbox_password) {
            sipBox = sipBoxPassword;
        } else {
            sipBox = sipBoxPasswordAgain;
        }

        int[] locationSubmit = new int[2];
        sipBox.getLocationOnScreen(locationSubmit);
        int bottomSubmit = locationSubmit[1] + sipBox.getMeasuredHeight();
        int screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        int keyboardY = screenHeight - keyboardHeigt;
        offset = keyboardY - bottomSubmit;
        if (offset < 0) {
            llParentView.scrollBy(0, -offset);
        }
    }

    @Override
    public void afterKeyboardHidden(SipBox sipBox, int i) {
        if (offset < 0) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    llParentView.scrollBy(0, offset);
                    offset = 0;
                }
            }, 200);
        }
    }

    @Override
    public void afterClickDown(SipBox sipBox) {
        sipBox.hideSecurityKeyBoard();
    }

    /**
     * 文本改变监听
     */
    class Watcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // 输入的联系人进行格式化
            PhoneNumberFormat.onEditTextChanged(s, start, before, editClearMobile.getContentEditText());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    /**
     * 可点击TextView
     */
    class TextViewClickable extends ClickableSpan {

        @Override
        public void onClick(View widget) {
            ((TextView) widget).setHighlightColor(getResources().getColor(android.R.color.transparent));
            start(new QueryAgentFragment());
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
            ds.setColor(getResources().getColor(R.color.boc_main_button_color));
        }
    }

    /**
     * 查询当前账户详情
     */
    private void queryCurAccountDetails() {
        showLoadingDialog();
        getPresenter().queryAccountDetails(curAccountBean.getAccountId());
    }

    /**
     * 查询账户详情成功
     */
    @Override
    public void queryAccountDetailsSuccess(MobileRemitViewModel viewModel) {
        closeProgressDialog();
        if (!ApplicationConst.CURRENCY_CNY.equals(viewModel.getCurrencyCode())) {
            showErrorDialog(getResources().getString(R.string.boc_transfer_account_no_rmb));
            return;
        }
        if (accountBundle != null) {
            selectAccoutFragment.setFramgentResult(SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT, accountBundle);
            selectAccoutFragment.pop();
        } else {
            editChoiceAccount.setChoiceTextContent(NumberUtils.formatCardNumber(curAccountBean.getAccountNumber()));
        }
        llBalance.setVisibility(View.VISIBLE);
        txtBalance.setText(getResources().getString(R.string.boc_transfer_money_unit,
                MoneyUtils.transMoneyFormat(String.valueOf(viewModel.getAvailableBalance()), viewModel.getCurrencyCode())));
    }

    /**
     * 查询账户详情失败
     *
     * @param biiResultErrorException
     */
    @Override
    public void queryAccountDetailsFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        llBalance.setVisibility(View.GONE);
        editChoiceAccount.setChoiceTextContent(mContext.getString(R.string.boc_common_select));
    }

    /**
     * 查询安全因子失败
     *
     * @param biiResultErrorException
     */
    @Override
    public void querySecurityFactorFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /**
     * 查询安全因子成功
     *
     * @param securityViewModel
     */
    @Override
    public void querySecurityFactorSuccess(SecurityViewModel securityViewModel) {
        buildViewModel();// 封装页面数据

        // 传递安全因子给组件
        defaultCombin = SecurityVerity.getInstance(getActivity()).
                getDefaultSecurityFactorId(new SecurityFactorModel(PublicUtils.copyOfSecurityCombin(securityViewModel)));
        // 汇往取款人预交易
        getPresenter().mobileRemitConfirm(curAccountBean.getAccountId(), defaultCombin.getId());
    }

    /**
     * 预交易失败
     *
     * @param biiResultErrorException
     */
    @Override
    public void mobileRemitConfirmFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /**
     * 预交易成功
     */
    @Override
    public void mobileRemitConfirmSuccess() {
        closeProgressDialog();
        EShieldVerify.getInstance(getActivity()).setmPlainData(mViewModel.get_plainData());
        Bundle bundle = new Bundle();
        bundle.putString("DefaultCombinID", defaultCombin.getId());
        bundle.putString("DefaultCombinName", defaultCombin.getName());
        bundle.putParcelable("AccountBean", curAccountBean);
        bundle.putSerializable("MobileRemitViewModel", mViewModel);
        MobileRemitConfirmFragment mobileRemitConfirmFragment = new MobileRemitConfirmFragment();
        mobileRemitConfirmFragment.setArguments(bundle);
        start(mobileRemitConfirmFragment);
    }

    @Override
    public void psnTransQuotaquerySuccess(String quotaAmount) {
        BigDecimal input = new BigDecimal(editMoneyInput.getContentMoney());
        BigDecimal result = new BigDecimal(quotaAmount);
        BigDecimal limit = BigDecimal.valueOf(300000);
        if ((input.add(result)).compareTo(limit) == 1) {
            closeProgressDialog();
            final TitleAndBtnDialog dialog = new TitleAndBtnDialog(getContext());
            String[] btnNames = new String[]{"取消", "确认"};
            dialog.setBtnName(btnNames);
            dialog.setNoticeContent("您网银和手机银行累计转账金额已超过30万元，请确认是否继续转账");
            dialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
                @Override
                public void onLeftBtnClick(View view) {
                    dialog.dismiss();
                }

                @Override
                public void onRightBtnClick(View view) {
                    dialog.dismiss();
                    showLoadingDialog();
                    getPresenter().querySecurityFactor();
                }
            });
            dialog.show();
        } else {
            getPresenter().querySecurityFactor();// 查询安全因子
        }
    }

    @Override
    public void psnTransQuotaqueryFail() {
        getPresenter().querySecurityFactor();// 查询安全因子
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel = null;
    }
}
