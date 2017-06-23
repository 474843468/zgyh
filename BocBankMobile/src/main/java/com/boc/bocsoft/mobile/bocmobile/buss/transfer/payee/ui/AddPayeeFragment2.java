package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui;

import android.app.Activity;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PhoneNumberFormat;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditClearWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditDialogWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.BankEntity;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnEbpsRealTimePaymentSavePayeeViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnQueryActTypeByActNumViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransBocAddPayeeViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransIsSamePayeeViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransNationalAddPayeeViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransPayeeListqueryForDimViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransQueryExternalBankInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.presenter.AddPayeePresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 新增收款人
 * Created by zhx on 2016/7/19
 */
public class AddPayeeFragment2 extends BussFragment implements AddPayeeContact.View {
    private static final int REQUEST_CODE_SELECT_MOBILE_CONTACT = 3434; // 请求码，选择手机联系人
    private static final int REQUEST_CODE_SELECT_BELONG_BANK = 3435; // 请求码，选择所属银行
    private static final int REQUEST_CODE_SCAN_CARD_NUM = 234; // 请求码，扫描银行卡
    private static final int REQUEST_CODE_SELECT_OPEN_ACOUNT_BANK = 3436; // 请求码，选择开户行

    public static final int RESULT_CODE_ADD_PAYEE_SUCCESS = 12; // 响应码，添加收款人成功

    private View mRootView;
    private ImageView iv_payee_name;
    private EditClearWidget et_payee_name;
    private EditClearWidget et_payee_mobile;
    private EditClearWidget et_payee_actno;
    private EditChoiceWidget ecw_choose_belong_bank;
    private EditChoiceWidget ecw_choose_open_account_bank;
    private Button btn_ok;
    private BankEntity mCurrentSelectBankEntity; // 当前用户选择的银行
    private PsnTransQueryExternalBankInfoViewModel.OpenBankBean mCurrentOpenBankBean;
    private AddPayeePresenter mAddPayeePresenter;

    private int mCurrentSaveType = -1; // 当前的保存类型：1表示中行；2表示跨行普通；3表示跨行实时

    private PsnQueryActTypeByActNumViewModel mPsnQueryActTypeByActNumViewModel;

    private PsnTransPayeeListqueryForDimViewModel.PayeeEntity mNewPayeeEntity; // 新收款人
    private String cardNumber; // 扫描出来的银行卡号
    private String phone;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_add_payee, null);
        return mRootView;
    }

    @Override
    public void initView() {
        iv_payee_name = (ImageView) mRootView.findViewById(R.id.iv_payee_name);
        et_payee_name = (EditClearWidget) mRootView.findViewById(R.id.et_payee_name);
        et_payee_mobile = (EditClearWidget) mRootView.findViewById(R.id.et_payee_mobile);
        et_payee_actno = (EditClearWidget) mRootView.findViewById(R.id.et_payee_actno);
        btn_ok = (Button) mRootView.findViewById(R.id.btn_ok);
        ecw_choose_belong_bank = (EditChoiceWidget) mRootView.findViewById(R.id.ecw_choose_belong_bank);
        ecw_choose_open_account_bank = (EditChoiceWidget) mRootView.findViewById(R.id.ecw_choose_open_account_bank);

        // -----------------
        et_payee_mobile.setEditWidgetHint("选填");
        //设置手机号输入框格式为数字
        et_payee_mobile.getContentEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        //设置手机号输入的位数为11位
        et_payee_mobile.getContentEditText().setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(13)
        });
        et_payee_mobile.setEditWidgetTitle("手机号");
        et_payee_mobile.getClearEditRightImageView().setVisibility(View.VISIBLE);
        et_payee_mobile.setClearEditRightImage(R.drawable.boc_callbook);
        // -----------------
        et_payee_actno.setEditWidgetHint("请输入");
        et_payee_actno.getContentEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        et_payee_actno.getContentEditText().setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(19)
        });
        et_payee_actno.setEditWidgetTitle("收款账号");
        et_payee_actno.getClearEditRightImageView().setVisibility(View.VISIBLE);
        et_payee_actno.setClearEditRightImage(R.drawable.boc_camera);
        // -----------------
        et_payee_name.setEditWidgetHint("请输入");
        et_payee_name.setEditWidgetTitle("收款人名称");
    }

    @Override
    public void initData() {
        updateOpenBankItemViewVisualState();
        mAddPayeePresenter = new AddPayeePresenter(this);
    }

    @Override
    public void setListener() {
        //为组件添加实时格式化监听
        et_payee_mobile.getContentEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //实时格式化手机号方法调用
                PhoneNumberFormat.onEditTextChanged(s, start, before, et_payee_mobile.getContentEditText());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        et_payee_actno.setRightImageCallBack(new EditClearWidget.EditRightImageOnClick() {
            @Override
            public void onClick() {
                //                DoubleScanFragment fragment = new DoubleScanFragment();
                hideSoftInput();
                ScanCardNumFragment fragment = new ScanCardNumFragment();
                startForResult(fragment, REQUEST_CODE_SCAN_CARD_NUM);
            }
        });

        et_payee_mobile.setRightImageCallBack(new EditClearWidget.EditRightImageOnClick() {
            @Override
            public void onClick() {
                chooseMobileContact();
            }
        });

        ecw_choose_belong_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput();
                ChooseBankFragment1 chooseBankFragment = new ChooseBankFragment1();
                Bundle bundle = new Bundle();
                bundle.putInt(ChooseBankFragment1.KEY_CHOOSE_BANK_TYPE, 0);
                chooseBankFragment.setArguments(bundle);
                startForResult(chooseBankFragment, REQUEST_CODE_SELECT_BELONG_BANK);
            }
        });

        ecw_choose_open_account_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseOpenAcountBankFragment chooseOpenAcountBankFragment = new ChooseOpenAcountBankFragment();
                Bundle args = new Bundle();
                args.putParcelable("bank", mCurrentSelectBankEntity);
                chooseOpenAcountBankFragment.setArguments(args);
                startForResult(chooseOpenAcountBankFragment, REQUEST_CODE_SELECT_OPEN_ACOUNT_BANK);
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 第1步，检查用户输入的数据
                boolean flag = checkInputData();
                if (!flag) {
                    return;
                }

                showLoadingDialog("请稍候...", false);

                // 第2步，根据不同的情况执行不同的逻辑（共有3中情况：添加中国银行收款人，添加他行实时收款人，添加他行普通收款人）
                if ("中国银行".equals(mCurrentSelectBankEntity.getBankName())) {
                    mCurrentSaveType = 1;
                    PsnQueryActTypeByActNumViewModel psnQueryActTypeByActNumViewModel = new PsnQueryActTypeByActNumViewModel();
                    if (!TextUtils.isEmpty(cardNumber)) {
                        psnQueryActTypeByActNumViewModel.setAccountNumber(cardNumber); // 账号
                    } else {
                        psnQueryActTypeByActNumViewModel.setAccountNumber(et_payee_actno.getEditWidgetContent().trim()); // 账号
                    }
                    psnQueryActTypeByActNumViewModel.setToName(et_payee_name.getEditWidgetContent().trim()); // 收款人姓名

                    mAddPayeePresenter.psnQueryActTypeByActNum(psnQueryActTypeByActNumViewModel);
                } else { // 他行保存收款人
                    PsnTransIsSamePayeeViewModel psnTransIsSamePayeeViewModel = new PsnTransIsSamePayeeViewModel();
                    if (!TextUtils.isEmpty(cardNumber)) {
                        psnTransIsSamePayeeViewModel.setPayeeActno(cardNumber);
                    } else {
                        psnTransIsSamePayeeViewModel.setPayeeActno(et_payee_actno.getEditWidgetContent().trim());
                    }

                    if (mCurrentOpenBankBean == null) { // 实时付款保存收款人
                        mCurrentSaveType = 3;

                        psnTransIsSamePayeeViewModel.setCnapsCode(mCurrentSelectBankEntity.getBankCode());
                        psnTransIsSamePayeeViewModel.setBocFlag("3"); // 2016-12-16此处的问题导致了相同收款人判断失败，实时BocFlag为3
                    } else { // 跨行普通
                        mCurrentSaveType = 2;
                        // 设置是否“判断是否存在相同收款人”的字段
                        psnTransIsSamePayeeViewModel.setCnapsCode(mCurrentOpenBankBean.getCnapsCode());
                        psnTransIsSamePayeeViewModel.setBocFlag("0"); // 收款账号标识
                    }
                    mAddPayeePresenter.psnTransIsSamePayee(psnTransIsSamePayeeViewModel);
                }
            }
        });
    }

    // 检查用户输入的数据
    private boolean checkInputData() {
        String payeeName = et_payee_name.getEditWidgetContent().toString().trim();
        if (TextUtils.isEmpty(payeeName)) {
            showErrorDialog("收款人名称不能为空");
            return false;
        }

        if (StringUtils.getStringLength(payeeName) > 60) {
            showErrorDialog("收款人名称：最长60个字符或30个中文字符");
            return false;
        }

        // TODO 此处是否进行非法字符的校验，it is a question

        String payeeActNo = et_payee_actno.getEditWidgetContent().toString().trim();
        if (TextUtils.isEmpty(payeeActNo)) {
            showErrorDialog("收款账号不能为空");
            return false;
        }

        if (payeeActNo.length() > 31) {
            showErrorDialog("收款账号最长31个字符");
            return false;
        }

        if (mCurrentSelectBankEntity == null) {
            showErrorDialog("请选择所属银行");
            return false;
        }

        // 如果是中国银行，加上这个判断
        if (mCurrentSelectBankEntity.getBankName().equals("中国银行")) {
            if (!NumberUtils.checkCardNumber(payeeActNo)) {
                showErrorDialog("收款账号由12-17或19位数字组成");
                return false;
            }
        }

        String mobile = et_payee_mobile.getEditWidgetContent().trim();
        if (!TextUtils.isEmpty(mobile)) { // 如果手机号码不为空，检查手机号码的合法性
            if (!NumberUtils.checkMobileNumber(mobile.replace(" ", ""))) {
                showErrorDialog("收款人手机号：11位数字");
                return false;
            }
        }

        // 开户行的判断
        String bankName = mCurrentSelectBankEntity.getBankName();
        if (bankName.contains("中国进出口银行") || bankName.contains("中国农业发展银行") || bankName.contains("其他银行")) { // 2016-10-19 新增逻辑
            if (mCurrentOpenBankBean == null) {
                showErrorDialog("请选择开户行");
                return false;
            }
        }

        return true;
    }

    // 选择手机联系人
    private void chooseMobileContact() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_SELECT_MOBILE_CONTACT);
        //        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI), REQUEST_CODE_SELECT_MOBILE_CONTACT);
    }

    @Override
    public boolean onBack() {
        hideSoftInput();
        return super.onBack();
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_SELECT_BELONG_BANK) { // 选择所属银行
                mCurrentOpenBankBean = null; // 将选择的开户行置为null
                mCurrentSelectBankEntity =
                        data.getParcelable(ChooseBankFragment1.KEY_BANK);
                String bankName = mCurrentSelectBankEntity.getBankName();
                if (bankName.contains("中国进出口银行") || bankName.contains("国家开发银行") || bankName.contains("中国农业发展银行") || bankName.contains("其他银行")) { // 2016-10-19 新增逻辑
                    ecw_choose_open_account_bank.setChoiceTextContent("请选择"); // 将界面显示置为"请选择"
                } else {
                    ecw_choose_open_account_bank.setChoiceTextContent("可不选"); // 将界面显示置为"可不选"
                }
                ecw_choose_belong_bank.setChoiceTextContent(mCurrentSelectBankEntity.isHot() ? mCurrentSelectBankEntity.getBankAlias() : bankName);

                updateOpenBankItemViewVisualState(); // 更新“开户行点击条目”的显示状态
            } else if (requestCode == REQUEST_CODE_SELECT_OPEN_ACOUNT_BANK) { // 选择开户行
                mCurrentOpenBankBean = data.getParcelable("openBank");
                ecw_choose_open_account_bank.setChoiceTextContent(mCurrentOpenBankBean.getBankName());
            } else if (requestCode == REQUEST_CODE_SCAN_CARD_NUM) { // 扫描银行卡
                // 修改收款人卡号
                final EditDialogWidget dialog = new EditDialogWidget(mContext, 100, true);
                dialog.getClearEditText().setInputType(InputType.TYPE_CLASS_PHONE);
                // 设置Dialog初始值
                final String cardNumber1 = data.getString("cardNumber");
                dialog.setClearEditTextContent(cardNumber1);
                dialog.getTextViewInput().setVisibility(View.INVISIBLE);
                dialog.getTextViewAll().setVisibility(View.INVISIBLE);
                dialog.getTextViewDivision().setVisibility(View.INVISIBLE);
                dialog.setEditDialogListener(new EditDialogWidget.EditDialogCallBack() {
                    @Override
                    public void onClick(String strEditTextContent) {
                        String num = dialog.getStringText().replace(" ", "");
                        if (num.length() > 31) {
                            ToastUtils.show("收款账号最长31个字符");
                            return;
                        }

                        AddPayeeFragment2.this.cardNumber = num;
                        et_payee_actno.setEditWidgetContent(AddPayeeFragment2.this.cardNumber);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_SELECT_MOBILE_CONTACT) {
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
                            et_payee_mobile.setEditWidgetContent(PhoneNumberFormat.getPhoneNum(phone));

                            // 判断是否需要带回name
                            String name = et_payee_name.getEditWidgetContent();
                            if (TextUtils.isEmpty(name)) {
                                et_payee_name.setEditWidgetContent(PhoneNumberFormat.getPhoneName(phone));
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
                                    et_payee_mobile.setEditWidgetContent(PhoneNumberFormat.getPhoneNum(phone));

                                    // 判断是否需要带回name
                                    String name = et_payee_name.getEditWidgetContent();
                                    if (TextUtils.isEmpty(name)) {
                                        et_payee_name.setEditWidgetContent(PhoneNumberFormat.getPhoneName(phone));
                                    }
                                }
                            }).create().show();
                        }
                    }
                }
            }
        }
    }

    @Override
    protected String getTitleValue() {
        return "新增收款人";
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    // 成功回调：判断是否存在相同收款人
    @Override
    public void psnTransIsSamePayeeSuccess(PsnTransIsSamePayeeViewModel viewModel) {
        Boolean isSame = viewModel.getFlag();
        if (isSame) {
            showErrorDialog("已有相同收款人");
            closeProgressDialog();
        } else {
            // TODO 进行保存收款人的操作
            switch (mCurrentSaveType) {
                case 1: // 中行内汇款：新增收款人
                    PsnTransBocAddPayeeViewModel psnTransBocAddPayeeViewModel = new PsnTransBocAddPayeeViewModel();
                    psnTransBocAddPayeeViewModel.setToAccountId(et_payee_actno.getEditWidgetContent().trim()); // 转入账号
                    psnTransBocAddPayeeViewModel.setPayeeName(et_payee_name.getEditWidgetContent().trim()); // 收款人姓名
                    psnTransBocAddPayeeViewModel.setPayeeBankNum(mPsnQueryActTypeByActNumViewModel.getIbkNumber()); // 收款行行号
                    psnTransBocAddPayeeViewModel.setToAccountType(mPsnQueryActTypeByActNumViewModel.getAccountType()); // 转入行类型
                    psnTransBocAddPayeeViewModel.setPayeeMobile(et_payee_mobile.getEditWidgetContent().trim()); // 收款人手机

                    mAddPayeePresenter.psnTransBocAddPayee(psnTransBocAddPayeeViewModel);
                    break;
                case 2: // 国内跨行汇款：新增收款人
                    PsnTransNationalAddPayeeViewModel nationalAddPayeeViewModel = new PsnTransNationalAddPayeeViewModel();

                    nationalAddPayeeViewModel.setToAccountId(et_payee_actno.getEditWidgetContent().trim()); // 转入账号
                    nationalAddPayeeViewModel.setPayeeName(et_payee_name.getEditWidgetContent().trim());
                    nationalAddPayeeViewModel.setCnapsCode(mCurrentOpenBankBean.getCnapsCode());
                    nationalAddPayeeViewModel.setBankName(mCurrentSelectBankEntity.getBankName());
                    nationalAddPayeeViewModel.setToOrgName(mCurrentOpenBankBean.getBankName());
                    nationalAddPayeeViewModel.setPayeeMobile(et_payee_mobile.getEditWidgetContent().trim());

                    mAddPayeePresenter.psnTransNationalAddPayee(nationalAddPayeeViewModel);
                    break;
                case 3: // 实时付款保存收款人
                    PsnEbpsRealTimePaymentSavePayeeViewModel realTimePaymentSavePayeeViewModel = new PsnEbpsRealTimePaymentSavePayeeViewModel();
                    realTimePaymentSavePayeeViewModel.setPayeeActno(et_payee_actno.getEditWidgetContent().trim()); // 收款人账号
                    realTimePaymentSavePayeeViewModel.setPayeeName(et_payee_name.getEditWidgetContent().trim()); // 收款人名称
                    realTimePaymentSavePayeeViewModel.setPayeeBankName(mCurrentSelectBankEntity.getBankName()); // 收款人账户所属银行
                    realTimePaymentSavePayeeViewModel.setPayeeOrgName(""); // 收款人账户开户行
                    realTimePaymentSavePayeeViewModel.setPayeeCnaps(mCurrentSelectBankEntity.getBankCode()); // 银行行号
                    realTimePaymentSavePayeeViewModel.setPayeeMobile(et_payee_mobile.getEditWidgetContent().trim()); // 收款人手机号

                    mAddPayeePresenter.psnEbpsRealTimePaymentSavePayee(realTimePaymentSavePayeeViewModel);
                    break;
            }
        }
    }

    // 失败回调：判断是否存在相同收款人
    @Override
    public void psnTransIsSamePayeeFailed(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    // 成功回调：国内跨行汇款-新增收款人
    @Override
    public void psnTransNationalAddPayeeSuccess(PsnTransNationalAddPayeeViewModel viewModel) {
        closeProgressDialog();
        Toast.makeText(mActivity, "添加收款人成功", Toast.LENGTH_SHORT).show();

        // TODO 此处生成一个mNewPayeeEntity
        mNewPayeeEntity = new PsnTransPayeeListqueryForDimViewModel.PayeeEntity();
        mNewPayeeEntity.setAccountName(viewModel.getPayeeName());
        mNewPayeeEntity.setAccountNumber(viewModel.getToAccountId());
        mNewPayeeEntity.setBankName(viewModel.getBankName());
        mNewPayeeEntity.setCnapsCode(viewModel.getCnapsCode());
        mNewPayeeEntity.setMobile(viewModel.getPayeeMobile());
        mNewPayeeEntity.setPinyin("");
        mNewPayeeEntity.setAddress(mCurrentOpenBankBean.getBankName());
        mNewPayeeEntity.setBocFlag("2");

        Bundle bundle = new Bundle();
        bundle.putParcelable("mNewPayeeEntity", mNewPayeeEntity);
        setFramgentResult(RESULT_CODE_ADD_PAYEE_SUCCESS, bundle);
        pop();
    }

    /**
     * 更新“开户行点击条目”的显示状态
     */
    private void updateOpenBankItemViewVisualState() {
        if (mCurrentSelectBankEntity == null) {
            ecw_choose_open_account_bank.setVisibility(View.GONE);
        } else {
            if (mCurrentSelectBankEntity.getBankName().endsWith("中国银行")) {
                ecw_choose_open_account_bank.setVisibility(View.GONE);
                mRootView.findViewById(R.id.view_open_account_bank_line).setVisibility(View.GONE);
            } else {
                ecw_choose_open_account_bank.setVisibility(View.VISIBLE);
                mRootView.findViewById(R.id.view_open_account_bank_line).setVisibility(View.VISIBLE);
            }
        }
    }

    // 失败回调：国内跨行汇款-新增收款人
    @Override
    public void psnTransNationalAddPayeeFailed(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    // 成功回调：实时付款保存收款人
    @Override
    public void psnEbpsRealTimePaymentSavePayeeSuccess(PsnEbpsRealTimePaymentSavePayeeViewModel viewModel) {
        closeProgressDialog();
        Toast.makeText(mActivity, "添加收款人成功", Toast.LENGTH_SHORT).show();

        // TODO 此处生成一个mNewPayeeEntity
        mNewPayeeEntity = new PsnTransPayeeListqueryForDimViewModel.PayeeEntity();
        mNewPayeeEntity.setAccountName(viewModel.getPayeeName());
        mNewPayeeEntity.setAccountNumber(viewModel.getPayeeActno());
        mNewPayeeEntity.setBankName(viewModel.getPayeeBankName());
        mNewPayeeEntity.setCnapsCode(viewModel.getPayeeCnaps());
        mNewPayeeEntity.setMobile(viewModel.getPayeeMobile());
        mNewPayeeEntity.setPinyin("");
        mNewPayeeEntity.setBocFlag("3");

        Bundle bundle = new Bundle();
        bundle.putParcelable("mNewPayeeEntity", mNewPayeeEntity);
        setFramgentResult(RESULT_CODE_ADD_PAYEE_SUCCESS, bundle);
        pop();
    }

    // 失败回调：实时付款保存收款人
    @Override
    public void psnEbpsRealTimePaymentSavePayeeFailed(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    // 成功回调：中行内汇款-新增收款人
    @Override
    public void psnTransBocAddPayeeSuccess(PsnTransBocAddPayeeViewModel viewModel) {
        closeProgressDialog();
        mPsnQueryActTypeByActNumViewModel = null;
        Toast.makeText(mActivity, "添加收款人成功", Toast.LENGTH_SHORT).show();

        // TODO 此处生成一个mNewPayeeEntity
        mNewPayeeEntity = new PsnTransPayeeListqueryForDimViewModel.PayeeEntity();
        mNewPayeeEntity.setAccountName(viewModel.getPayeeName());
        mNewPayeeEntity.setAccountNumber(viewModel.getToAccountId());
        mNewPayeeEntity.setBankName("中国银行");
        mNewPayeeEntity.setCnapsCode(viewModel.getPayeeBankNum());
        mNewPayeeEntity.setMobile(viewModel.getPayeeMobile());
        mNewPayeeEntity.setPinyin("");
        mNewPayeeEntity.setBocFlag("1");

        Bundle bundle = new Bundle();
        bundle.putParcelable("mNewPayeeEntity", mNewPayeeEntity);
        setFramgentResult(RESULT_CODE_ADD_PAYEE_SUCCESS, bundle);
        pop();

    }

    // 失败回调：中行内汇款-新增收款人
    @Override
    public void psnTransBocAddPayeeFailed(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        mPsnQueryActTypeByActNumViewModel = null;
    }

    // 成功回调：根据账号查询账户类型
    @Override
    public void psnQueryActTypeByActNumSuccess(PsnQueryActTypeByActNumViewModel viewModel) {
        mPsnQueryActTypeByActNumViewModel = viewModel;

        String payeeActno = et_payee_actno.getEditWidgetContent().toString().trim(); // 收款人账号

        PsnTransIsSamePayeeViewModel psnTransIsSamePayeeViewModel = new PsnTransIsSamePayeeViewModel();
        psnTransIsSamePayeeViewModel.setPayeeActno(payeeActno);
        psnTransIsSamePayeeViewModel.setCnapsCode(viewModel.getIbkNumber());
        psnTransIsSamePayeeViewModel.setBocFlag("1");

        mAddPayeePresenter.psnTransIsSamePayee(psnTransIsSamePayeeViewModel);
    }

    // 失败回调：根据账号查询账户类型
    @Override
    public void psnQueryActTypeByActNumFailed(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void setPresenter(AddPayeeContact.Presenter presenter) {
    }
}