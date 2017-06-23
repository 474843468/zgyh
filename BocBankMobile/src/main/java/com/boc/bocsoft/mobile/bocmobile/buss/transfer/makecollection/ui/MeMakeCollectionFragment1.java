package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PhoneNumberFormat;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditClearWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.MenuFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.adapter.PayerAccountAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.CollectionViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.PsnTransActCollectionVerifyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.QueryPayerListViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.presenter.MeMakeCollectionPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payer.ui.PayerFragment;
import com.boc.bocsoft.mobile.bocmobile.yun.BocCloudCenter;
import com.boc.bocsoft.mobile.bocmobile.yun.other.AccountType;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;
import com.boc.bocsoft.mobile.framework.utils.regex.RegexUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 我要收款的首页（只有单人付款）
 * Created by zhx on 2016/9/6
 */
public class MeMakeCollectionFragment1 extends BussFragment implements MeMakeCollectionContact.View {
    private static final int REQUEST_CODE_SELECT_MOBILE_CONTACT = 234; // 请求码，选择手机联系人

    private AccountBean mPayeeAccount;
    private PayerAccountAdapter mPayerAccountAdapter;
    private ArrayList<QueryPayerListViewModel.ResultBean> mSelectedPayerList;

    private View mRootView;
    private EditChoiceWidget ecwChoosePayeeAccount; // 组件：选择收款账户控件
    private int softInputMode; // 软键盘的模式

    private EditClearWidget ecw_payee_phone_number; // 收款人手机号码
    private TextView tv_next_step;
    private static final int REQUEST_CODE_PAYEE_ACCOUNT = 212120; // 请求码：收款账户
    private static final int REQUEST_CODE_PAYER_ACCOUNT = 212121; // 请求码：付款账户
    private ScrollView sv_root;
    private LinearLayout ll_total_amount_container;
    private EditText et_payer_num;
    private EditClearWidget ecw_remark;
    private EditMoneyInputWidget emiw_amount;
    private EditClearWidget ecw_payer_mobile;
    private EditClearWidget ecw_payer_name;
    private LinearLayout ll_save_common_payer;
    private ImageView iv_is_save_payer;
    private PsnTransActCollectionVerifyViewModel psnTransActCollectionVerifyViewModel;
    private MeMakeCollectionPresenter meMakeCollectionPresenter;
    private String phone;
    private ArrayList<String> accountTypeList; // 过滤的账户类型
    private SelectAccoutFragment selectAccoutFragment;
    private boolean isSavePayer = false;
    private boolean isOnReturnResult = false;
    //无可操作账户的错误提示框
    private TitleAndBtnDialog mQrNoAccountErrorDialog;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_me_make_collection1, null);
        mTitleBarView.setRightButton(getResources().getDrawable(R.drawable.boc_overview_more));

        // windowSoftInputMode属性的获取
        softInputMode = mActivity.getWindow().getAttributes().softInputMode;
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return mRootView;
    }

    @Override
    public void initView() {
        ecwChoosePayeeAccount = (EditChoiceWidget) mRootView.findViewById(R.id.ecw_choose_payee_account);
        ecw_payer_mobile = (EditClearWidget) mRootView.findViewById(R.id.ecw_payer_mobile);
        ecw_payer_name = (EditClearWidget) mRootView.findViewById(R.id.ecw_payer_name);
        emiw_amount = (EditMoneyInputWidget) mRootView.findViewById(R.id.emiw_amount);
        tv_next_step = (TextView) mRootView.findViewById(R.id.tv_next_step);
        ecw_payee_phone_number = (EditClearWidget) mRootView.findViewById(R.id.ecw_payee_phone_number);
        sv_root = (ScrollView) mRootView.findViewById(R.id.sv_root);
        ll_total_amount_container = (LinearLayout) mRootView.findViewById(R.id.ll_total_amount_container);
        et_payer_num = (EditText) mRootView.findViewById(R.id.et_payer_num);
        ecw_remark = (EditClearWidget) mRootView.findViewById(R.id.ecw_remark);
        ll_save_common_payer = (LinearLayout) mRootView.findViewById(R.id.ll_save_common_payer);
        iv_is_save_payer = (ImageView) mRootView.findViewById(R.id.iv_is_save_payer);


        // --------“付款人名称”字段---------
        ecw_payer_name.setEditWidgetHint("请输入");
        ecw_payer_name.setEditWidgetTitle("付款人名称");
        ecw_payer_name.getClearEditRightImageView().setVisibility(View.VISIBLE);
        ecw_payer_name.setClearEditRightImage(R.drawable.boc_remit_payee);

        // --------“收款人手机号”字段---------
        ecw_payee_phone_number.setEditWidgetHint("请输入");
        //设置手机号输入框格式为数字
        ecw_payee_phone_number.getContentEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        //设置手机号输入的位数为11位
        ecw_payee_phone_number.getContentEditText().setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(13)
        });
        ecw_payee_phone_number.setEditWidgetTitle("收款人手机号");
        ecw_payee_phone_number.getClearEditRightImageView().setVisibility(View.VISIBLE);

        // --------“付款人手机号”字段---------
        ecw_payer_mobile.setEditWidgetHint("请输入");
        //设置手机号输入框格式为数字
        ecw_payer_mobile.getContentEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        //设置手机号输入的位数为11位
        ecw_payer_mobile.getContentEditText().setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(13)
        });
        ecw_payer_mobile.setEditWidgetTitle("付款人手机号");
        ecw_payer_mobile.getClearEditRightImageView().setVisibility(View.VISIBLE);
        ecw_payer_mobile.setClearEditRightImage(R.drawable.boc_callbook);

        // --------“附言”字段---------
        ecw_remark.setEditWidgetHint("选填");
        ecw_remark.setEditWidgetTitle("附言");

        // --------“收款金额”字段---------
        emiw_amount.setContentHint("请输入");
        emiw_amount.setEditWidgetTitle("收款金额");
        emiw_amount.setMaxLeftNumber(11);
        emiw_amount.setMaxRightNumber(2);
        emiw_amount.setCurrency("001");
        emiw_amount.getContentMoneyEditText().setTextColor(getResources().getColor(R.color.boc_text_color_red));
    }

    /**
     * 过滤账户类型
     */
    private void filterAccountType() {
        accountTypeList = new ArrayList<String>();
        // 借记卡 119
        accountTypeList.add(ApplicationConst.ACC_TYPE_BRO);
        // 中银系列信用卡 103
        //        accountTypeList.add(ApplicationConst.ACC_TYPE_ZHONGYIN);
        // 长城信用卡 104
        accountTypeList.add(ApplicationConst.ACC_TYPE_GRE);
    }

    /**
     * 设置页面的默认焦点
     */
    private void setDefaultFocus() {
        ecw_payee_phone_number.setFocusable(true);
        ecw_payee_phone_number.setFocusableInTouchMode(true);
        ecw_payee_phone_number.requestFocus();
    }

    @Override
    public void initData() {
        setDefaultFocus();
        filterAccountType();
        // 判断是否有可用账户
        List<AccountBean> accountList = ApplicationContext.getInstance().getChinaBankAccountList(accountTypeList);
        if (accountList == null || accountList.size() == 0) {
            showNoAccountErrorDialog();
            return;
        }

        // 显示默认账户
        String accountId = BocCloudCenter.getInstance().getAccountId(AccountType.ACC_TYPE_PAYEE); // 获取上次操作的账户Id,如果没有,那么取第一个账户
        if (!StringUtils.isEmptyOrNull(accountId)) {
            for (AccountBean accountBean : accountList) {
                String accountId1 = accountBean.getAccountId();
                String sha256String = BocCloudCenter.getSha256String(accountId1);
                if (sha256String.equals(accountId)) {
                    mPayeeAccount = accountBean;
                }
            }
        }

        if (mPayeeAccount == null) {
            mPayeeAccount = accountList.get(0);
        }
        ecwChoosePayeeAccount.setChoiceTextContent(NumberUtils.formatCardNumberStrong(mPayeeAccount != null ? mPayeeAccount.getAccountNumber() : null));

        ecw_payee_phone_number.getContentEditText().setText(NumberUtils.formatMobileNumber(((ApplicationContext) (mActivity.getApplicationContext())).getUser().getMobile()));
    }

    @Override
    public void setListener() {

        //为组件添加实时格式化监听
        ecw_payer_mobile.getContentEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //实时格式化手机号方法调用
                PhoneNumberFormat.onEditTextChanged(s, start, before, ecw_payer_mobile.getContentEditText());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        emiw_amount.setOnKeyBoardListener(new EditMoneyInputWidget.KeyBoardDismissOrShowCallBack() {
            @Override
            public void onKeyBoardDismiss() {

            }

            @Override
            public void onKeyBoardShow() {
                emiw_amount.getTitleTextView().setFocusable(true);
                emiw_amount.getTitleTextView().setFocusableInTouchMode(true);
                emiw_amount.getTitleTextView().requestFocus();
            }
        });

        //为组件添加实时格式化监听
        ecw_payee_phone_number.getContentEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //实时格式化手机号方法调用
                PhoneNumberFormat.onEditTextChanged(s, start, before, ecw_payee_phone_number.getContentEditText());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ecw_payer_name.setRightImageCallBack(new EditClearWidget.EditRightImageOnClick() {
            @Override
            public void onClick() {
                // 隐藏键盘
                hideSoftInput();
                // 打开选择付款账户页面
                PayerFragment toFragment = new PayerFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("isChoosePayerAccount", true);
                toFragment.setArguments(bundle);

                startForResult(toFragment, REQUEST_CODE_PAYER_ACCOUNT);
            }
        });

        ecwChoosePayeeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                // 打开选择收款账户页面
                //                ChoosePayeeAccountFragment toFragment = new ChoosePayeeAccountFragment();
                //                startForResult(toFragment, REQUEST_CODE_PAYEE_ACCOUNT);

                selectAccoutFragment = new SelectAccoutFragment();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(SelectAccoutFragment.ACCOUNT_TYPE_LIST,
                        (ArrayList<String>) accountTypeList);
                selectAccoutFragment.setArguments(bundle);
                startForResult(selectAccoutFragment, REQUEST_CODE_PAYEE_ACCOUNT);
            }
        });

        tv_next_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 第1步，检查数据
                boolean flag = checkData();
                if (!flag) {
                    return;
                }

                showLoadingDialog("加载中...", false);
                psnTransActCollectionVerifyViewModel = generateCollectionVerifyViewModel();

                meMakeCollectionPresenter = new MeMakeCollectionPresenter(MeMakeCollectionFragment1.this);
                meMakeCollectionPresenter.setActivity(mActivity);
                meMakeCollectionPresenter.collectionVerify(psnTransActCollectionVerifyViewModel);
            }
        });

        ecw_payer_mobile.setRightImageCallBack(new EditClearWidget.EditRightImageOnClick() {
            @Override
            public void onClick() {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

        iv_is_save_payer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSavePayer = !isSavePayer;
                iv_is_save_payer.setImageResource(isSavePayer ? R.drawable.checkbox_checked : R.drawable.checkbox_normal);
            }
        });

        ecw_payer_name.getContentEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("ljljlj", "afterTextChanged()..........................");
                if (!isOnReturnResult) {
                    ll_save_common_payer.setVisibility(View.VISIBLE);
                    iv_is_save_payer.setImageResource(isSavePayer ? R.drawable.checkbox_checked : R.drawable.checkbox_normal);
                }

                isOnReturnResult = false;
            }
        });
    }

    /**
     * 显示无可操作账户的错误对话框
     */
    private void showNoAccountErrorDialog() {
        if (mQrNoAccountErrorDialog != null && mQrNoAccountErrorDialog.isShowing()) {
            return;
        }
        mQrNoAccountErrorDialog = new TitleAndBtnDialog(mContext);
        mQrNoAccountErrorDialog.setBtnName(new String[]{getString(R.string.boc_common_sure)});
        mQrNoAccountErrorDialog.isShowTitle(false);
        mQrNoAccountErrorDialog.setCanceledOnTouchOutside(false);
        mQrNoAccountErrorDialog.setCancelable(false);
        mQrNoAccountErrorDialog.setNoticeContent("无可操作账户");
        mQrNoAccountErrorDialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
            @Override
            public void onLeftBtnClick(View view) {
            }

            @Override
            public void onRightBtnClick(View view) {
                mQrNoAccountErrorDialog.dismiss();
                ActivityManager.getAppManager().finishActivity();
            }
        });
        mQrNoAccountErrorDialog.show();
    }

    @Override
    public void collectionVerifySuccess(PsnTransActCollectionVerifyViewModel viewModel) {
        pop();
        closeProgressDialog();
        // 启动“确认信息”页面
        PayConfirmImformationFragment1 payConfirmImformationFragment = new PayConfirmImformationFragment1();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 0); // 0表示单人，1 表示多人
        bundle.putString("DefaultCombinID", meMakeCollectionPresenter.defaultCombin.getId());
        bundle.putString("DefaultCombinName", meMakeCollectionPresenter.defaultCombin.getName());
        bundle.putSerializable("psnTransActCollectionVerifyViewModel", psnTransActCollectionVerifyViewModel);
        bundle.putBoolean("isSavePayer", isSavePayer);
        payConfirmImformationFragment.setArguments(bundle);
        start(payConfirmImformationFragment);
    }

    @Override
    public void collectionVerifyFailed(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void setPresenter(MeMakeCollectionContact.Presenter presenter) {
    }

    // 生成预交易ViewModel(单人)
    private PsnTransActCollectionVerifyViewModel generateCollectionVerifyViewModel() {
        PsnTransActCollectionVerifyViewModel viewModel = new PsnTransActCollectionVerifyViewModel();

        viewModel.setToAccountId(mPayeeAccount.getAccountId()); // 收款人账ID
        viewModel.setPayeeName(mPayeeAccount.getAccountName()); // 收款人姓名
        viewModel.setCurrency("001"); // 币种，001表示人民币

        // 收款金额
        viewModel.setNotifyPayeeAmount(MoneyUtils.transMoneyFormat(emiw_amount.getContentMoney(), "001"));

        viewModel.setRemark(ecw_remark.getEditWidgetContent().trim()); // 备注
        viewModel.setPayerMobile(ecw_payer_mobile.getEditWidgetContent().trim().replace(" ", "")); // 付款人手机
        viewModel.setPayerName(ecw_payer_name.getEditWidgetContent().trim()); // 付款人姓名
        //        viewModel.setPayeeMobile(((ApplicationContext) (mActivity.getApplicationContext())).getUser().getMobile()); // 收款人手机
        viewModel.setPayeeMobile(ecw_payee_phone_number.getEditWidgetContent().trim().replace(" ", "")); // 收款人手机


        if (mSelectedPayerList != null) {
            viewModel.setPayerCustId(mSelectedPayerList.get(0).getPayerCustomerId()); // 付款人客户号
            viewModel.setPayerChannel(mSelectedPayerList.get(0).getIdentifyType()); // 付款人类型:1：WEB渠道、2：手机渠道
        } else {
            viewModel.setPayerChannel("2");
        }

        // 如果填写的手机号码和实际手机号码不一致，那么PayerChannel为“手机渠道”
        if (mSelectedPayerList != null && !ecw_payer_mobile.getEditWidgetContent().trim().replace(" ", "").equals(mSelectedPayerList.get(0).getPayerMobile().trim().replace(" ", ""))) {
            viewModel.setPayerChannel("2");
        }

        viewModel.setPayeeActno(mPayeeAccount.getAccountNumber());

        return viewModel;
    }

    @Override
    protected void titleRightIconClick() {
        hideSoftInput(); // 隐藏软键盘

        Bundle bundle = new Bundle();
        bundle.putInt("Menu", MenuFragment.MAKE_COLLECTION);
        MenuFragment fragment = new MenuFragment();
        fragment.setArguments(bundle);
        start(fragment);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.getWindow().setSoftInputMode(softInputMode);
    }

    private boolean checkData() {
        if (mPayeeAccount == null) {
            showErrorDialog("请选择收款账号");
            return false;
        }

        String payeeMobile = ecw_payee_phone_number.getEditWidgetContent().trim();
        if (TextUtils.isEmpty(payeeMobile)) {
            showErrorDialog("收款人手机号不能为空");
            return false;
        }

        if (!NumberUtils.checkMobileNumber(payeeMobile.replace(" ", ""))) {
            showErrorDialog("收款人手机号：11位数字");
            return false;
        }

        String money = emiw_amount.getContentMoney();
        if (TextUtils.isEmpty(money)) {
            showErrorDialog("收款金额不能为空");
            return false;
        }

        String payerName = ecw_payer_name.getEditWidgetContent().trim();
        if (TextUtils.isEmpty(payerName)) {
            showErrorDialog("付款人名称不能为空");
            return false;
        }

        if (StringUtils.getStringLength(payerName) > 60) {
            showErrorDialog("付款人名称：最长60个字符或30个中文字符\n");
            return false;
        }

        String payerMobile = ecw_payer_mobile.getEditWidgetContent().trim();
        if (TextUtils.isEmpty(payerMobile)) {
            showErrorDialog("付款人手机号不能为空");
            return false;
        }

        if (!NumberUtils.checkMobileNumber(payerMobile.replace(" ", ""))) {
            showErrorDialog("付款人手机号：11位数字");
            return false;
        }

        if (!RegexUtils.check(getContext(), "me_make_collection_remark", ecw_remark.getEditWidgetContent().trim(), false).isAvailable) {
            showErrorDialog("附言：最长20个字符，不可包含[]^$\\~@#%&<>{}:'\"及回车");
            return false;
        }

        return true;
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
                    String phoneNum = PhoneNumberFormat.getPhoneNum(phone);
                    ecw_payer_mobile.setEditWidgetContent(phoneNum.replace("-", ""));

                    // 判断是否需要带回name
                    String name = ecw_payer_name.getEditWidgetContent();
                    if (TextUtils.isEmpty(name)) {
                        ecw_payer_name.setEditWidgetContent(PhoneNumberFormat.getPhoneName(phone));
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
                            String phoneNum = PhoneNumberFormat.getPhoneNum(phone);
                            ecw_payer_mobile.setEditWidgetContent(phoneNum.replace("-", ""));

                            // 判断是否需要带回name
                            String name = ecw_payer_name.getEditWidgetContent();
                            if (TextUtils.isEmpty(name)) {
                                ecw_payer_name.setEditWidgetContent(PhoneNumberFormat.getPhoneName(phone));
                            }
                        }
                    }).create().show();
                }
            }
        }
    }


    public void prepareCollectionViewModel(CollectionViewModel collectionViewModel) {
        collectionViewModel.setToAccountId(mPayeeAccount.getAccountId()); // 收款人账ID
        collectionViewModel.setPayeeName(mPayeeAccount.getAccountName()); // 收款人姓名
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {

        if (resultCode == SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT) { // 选择收款账户
            mPayeeAccount = data.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);

            if (mPayeeAccount != null) {
                ecwChoosePayeeAccount.setChoiceTextContent(NumberUtils.formatCardNumberStrong(mPayeeAccount.getAccountNumber()));
            }
        } else if (resultCode == ChoosePayerAccountFragment.RESULT_CODE_SELECTED_PAYER_ACCOUNT) { // 选择付款人
            isOnReturnResult = true;
            ll_save_common_payer.setVisibility(View.GONE);
            isSavePayer = false;

            mSelectedPayerList = data.getParcelableArrayList(ChoosePayerAccountFragment.SELECTED_PAYER_ACCOUNT);
            ecw_payer_name.setEditWidgetContent(mSelectedPayerList.get(0).getPayerName());
            ecw_payer_mobile.setEditWidgetContent(mSelectedPayerList.get(0).getPayerMobile());
        }
    }

    @Override
    public void reInit() {
        emiw_amount.getContentMoneyEditText().clearText();
        ecw_payer_name.setEditWidgetContent("");
        ecw_payer_mobile.setEditWidgetContent("");
        ecw_remark.setEditWidgetContent("");
        ll_save_common_payer.setVisibility(View.GONE);
        isSavePayer = false;
    }

    @Override
    protected String getTitleValue() {
        return "我要收款";
    }


    protected boolean isDisplayRightIcon() {
        return true;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }
}