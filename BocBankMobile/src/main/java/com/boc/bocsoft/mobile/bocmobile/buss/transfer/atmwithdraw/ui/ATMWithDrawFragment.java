package com.boc.bocsoft.mobile.bocmobile.buss.transfer.atmwithdraw.ui;

import android.os.Bundle;
import android.os.Handler;
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
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditClearWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.MenuFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.atmwithdraw.model.ATMWithDrawViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.atmwithdraw.presenter.ATMWithDrawPresenter;
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
 * ATM无卡取款
 * Created by liuweidong on 2016/6/21.
 */
public class ATMWithDrawFragment extends MvpBussFragment<ATMWithDrawContract.Presenter> implements View.OnClickListener,
        ATMWithDrawContract.BeforeView, CFCASipDelegator {
    private View rootView;
    private LinearLayout llParentView;
    private EditChoiceWidget editChoiceAccount;// 取款账户
    private LinearLayout llBalance;
    private TextView txtBalance;// 可用余额
    private EditMoneyInputWidget editMoneyInput;// 取款金额
    private SipBox sipBoxPassword;// 取款密码
    private SipBox sipBoxPasswordAgain;// 再次确认取款密码
    private TextView txtMobile;// 本人手机号
    private EditClearWidget editClearComment;// 附言
    private Button btnConfirm;

    private static ATMWithDrawViewModel mViewModel;// View层数据
    private ArrayList<String> accountTypeList;// 过滤的账户类型
    private List<AccountBean> accountList = new ArrayList<>();
    private AccountBean curAccountBean;// 当前选中的账户数据
    private AccountBean againAccount;
    private CombinListBean defaultCombin;// 默认的安全因子
    private SelectAccoutFragment selectAccoutFragment;// 选择账户页面
    private Bundle accountBundle;// 当前选中的账户
    private LayoutInflater mInflater;
    private boolean isAgain = false;
    /**
     * 偏移量
     */
    private int offset = 0;
    private Handler handler = new Handler();

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        this.mInflater = mInflater;
        rootView = View.inflate(mContext, R.layout.boc_fragment_transfer_atm_withdraw, null);
        return rootView;
    }

    @Override
    public void initView() {
        llParentView = (LinearLayout) rootView.findViewById(R.id.ll_parent_view);
        editChoiceAccount = (EditChoiceWidget) rootView.findViewById(R.id.choice_account);
        llBalance = (LinearLayout) rootView.findViewById(R.id.ll_balance);
        txtBalance = (TextView) rootView.findViewById(R.id.txt_balance);
        editMoneyInput = (EditMoneyInputWidget) rootView.findViewById(R.id.money_input);
        sipBoxPassword = (SipBox) rootView.findViewById(R.id.sipbox_password);
        sipBoxPasswordAgain = (SipBox) rootView.findViewById(R.id.sipbox_password_again);
        editClearComment = (EditClearWidget) rootView.findViewById(R.id.edit_comment);
        txtMobile = (TextView) rootView.findViewById(R.id.txt_mobile);
        btnConfirm = (Button) rootView.findViewById(R.id.btn_confirm);
        mTitleBarView.setRightButton(getResources().getDrawable(R.drawable.boc_overview_more));
        setCFCAAttribute();
        setWidgetAttribute();
        filterAccountType();
    }

    @Override
    public void initData() {
        accountBundle = null;
        againAccount = curAccountBean;
        setDefaultFocus();
        cleanViewData();
        curAccountBean = new AccountBean();
        if (mViewModel == null) {
            mViewModel = new ATMWithDrawViewModel();
        }

        accountList = ApplicationContext.getInstance().getChinaBankAccountList(accountTypeList);
        if (accountList.size() == 0) {// 无可操作账户
            showErrorDialog(getResources().getString(R.string.boc_common_no_account));
            return;
        }
        if (isAgain) {
            isAgain = false;
            curAccountBean = againAccount;
        } else {
            String accountId = BocCloudCenter.getInstance().getAccountId(AccountType.ACC_TYPE_NOCARD_DRAW);
            if (!StringUtils.isEmptyOrNull(accountId)) {
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
        btnConfirm.setOnClickListener(this);
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
        return getResources().getString(R.string.boc_transfer_atm_draw_title);
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
        bundle.putInt("Menu", MenuFragment.ATM_WITH_DRAW);
        MenuFragment fragment = new MenuFragment();
        fragment.setArguments(bundle);
        start(fragment);
    }

    @Override
    protected ATMWithDrawContract.Presenter initPresenter() {
        return new ATMWithDrawPresenter(this, false);
    }

    /**
     * view层model
     *
     * @return
     */
    public static ATMWithDrawViewModel getViewModel() {
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
    private void filterAccountType() {
        accountTypeList = new ArrayList<String>();
        // 借记卡 119
        accountTypeList.add(ApplicationConst.ACC_TYPE_BRO);
        // 普通活期 101
        accountTypeList.add(ApplicationConst.ACC_TYPE_ORD);
        // 活期一本通 188
        accountTypeList.add(ApplicationConst.ACC_TYPE_RAN);
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
    }

    /**
     * 清空页面数据
     */
    private void cleanViewData() {
        editChoiceAccount.setChoiceTextContent(getResources().getString(R.string.boc_common_select));// 设置默认（请选择）
        txtBalance.setText("");// 可用余额
        editMoneyInput.setmContentMoneyEditText("");// 取款金额
        sipBoxPassword.setText("");// 预留密码
        sipBoxPasswordAgain.setText("");// 确认密码
        txtMobile.setText(NumberUtils.formatMobileNumber(ApplicationContext.getInstance().getUser().getMobile()));// 设置取款手机号
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
            selectAccoutFragment = SelectAccoutFragment.newInstance(accountTypeList);
            selectAccoutFragment.isRequestNet(true);
            startForResult(selectAccoutFragment, SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT);
            selectAccoutFragment.setOnItemListener(new SelectAccoutFragment.ItemListener() {
                @Override
                public void onItemClick(Bundle bundle) {
                    ATMWithDrawFragment.this.accountBundle = bundle;
                    AccountBean item = ATMWithDrawFragment.this.accountBundle.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
                    // 保存当前账户信息
                    curAccountBean = item;
                    // 查询当前账户详情
                    queryCurAccountDetails();
                }
            });
        } else if (i == R.id.btn_confirm) {
            if (checkViewRegex()) {
                showLoadingDialog(false);
                getPresenter().psnTransQuotaquery();
//                getPresenter().querySecurityFactor();// 查询安全因子
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
            showErrorDialog(getResources().getString(R.string.boc_transfer_message_account_empty));
            return false;
        }

        // 取款金额
        RegexResult amountResult = RegexUtils.check(mContext, "mbdi_common_amount",
                editMoneyInput.getContentMoney(), true);
        if (!amountResult.isAvailable) {
            showErrorDialog(amountResult.errorTips);
            return false;
        }
        if (BigDecimal.valueOf(Double.valueOf(editMoneyInput.getContentMoney())).compareTo(mViewModel.getAvailableBalance()) == 1) {
            showErrorDialog(getResources().getString(R.string.boc_transfer_message_money_overtop));
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

        // 预留密码
//        RegexResult passwordResult = RegexUtils.check(mContext, "mbdi_tr_mobilewith_password",
//                sipBoxPassword.getText().toString(), true);
////        Log.e("ddddd----",""+sipBoxPassword.getText().toString().length());
//        if (!passwordResult.isAvailable) {
//            showErrorDialog(passwordResult.errorTips);
//            return false;
//        }

        // 确认密码
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

    /**
     * 封装页面数据
     */
    private void buildViewModel() {
        // 设置取款金额
        mViewModel.setMoney(BigDecimal.valueOf(Double.valueOf(editMoneyInput.getContentMoney())));
        // 设置附言
        mViewModel.setFurInf(editClearComment.getEditWidgetContent());
        // 设置密码控件随机数
        sipBoxPassword.setRandomKey_S(ATMWithDrawPresenter.randomID);
        sipBoxPasswordAgain.setRandomKey_S(ATMWithDrawPresenter.randomID);
        try {
            mViewModel.setObligatePassword(sipBoxPassword.getValue().getEncryptPassword());
            mViewModel.setObligatePassword_RC(sipBoxPassword.getValue().getEncryptRandomNum());
            mViewModel.setReObligatePassword(sipBoxPasswordAgain.getValue().getEncryptPassword());
            mViewModel.setReObligatePassword_RC(sipBoxPasswordAgain.getValue().getEncryptRandomNum());
        } catch (CodeException e) {
            e.printStackTrace();
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
     *
     * @param viewModel
     */
    @Override
    public void queryAccountDetailsSuccess(ATMWithDrawViewModel viewModel) {
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
     */
    @Override
    public void queryAccountDetailsFail() {
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
        // 传递安全因子给组件
        defaultCombin = SecurityVerity.getInstance(getActivity()).
                getDefaultSecurityFactorId(new SecurityFactorModel(PublicUtils.copyOfSecurityCombin(securityViewModel)));
        // ATM无卡取款预交易
        getPresenter().atmWithDrawConfirm(curAccountBean.getAccountId(), defaultCombin.getId(),
                editMoneyInput.getContentMoney());
    }

    /**
     * 预交易失败
     *
     * @param biiResultErrorException
     */
    @Override
    public void atmWithDrawConfirmFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /**
     * 预交易成功
     */
    @Override
    public void atmWithDrawConfirmSuccess() {
        closeProgressDialog();
        buildViewModel();// 封装页面数据
        EShieldVerify.getInstance(getActivity()).setmPlainData(mViewModel.get_plainData());

        Bundle bundle = new Bundle();
        bundle.putString("DefaultCombinID", defaultCombin.getId());
        bundle.putString("DefaultCombinName", defaultCombin.getName());
        bundle.putParcelable("AccountBean", curAccountBean);
        bundle.putSerializable("ATMWithDrawViewModel", mViewModel);
        ATMWithDrawConfirmFragment fragment = new ATMWithDrawConfirmFragment();
        fragment.setArguments(bundle);
        start(fragment);
    }

    @Override
    public void psnTransQuotaqueryFail() {
        getPresenter().querySecurityFactor();// 查询安全因子
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
    public void onDestroy() {
        super.onDestroy();
        mViewModel = null;
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
}
