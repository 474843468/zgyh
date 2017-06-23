package com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdraw.ui;

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

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PhoneNumberFormat;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditClearWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdraw.model.MobileWithdrawViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdraw.presenter.MobileWithdrawPresenter;
import com.boc.bocsoft.mobile.framework.utils.regex.RegexResult;
import com.boc.bocsoft.mobile.framework.utils.regex.RegexUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 代理点取款
 * Created by liuweidong on 2016/7/12.
 */
public class MobileWithdrawFragment extends MvpBussFragment<MobileWithdrawContract.Presenter> implements View.OnClickListener, MobileWithdrawContract.BeforeView {
    private View rootView;
    private EditClearWidget editClearName;// 收款人姓名
    private EditClearWidget editClearMobile;// 收款人手机号
    private EditClearWidget editClearNum;// 汇款编码
    private Button btnConfirm;// 确定

    private static MobileWithdrawViewModel mViewModel;// View层数据
    private String phone;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = View.inflate(mContext, R.layout.boc_fragment_transfer_mobile_withdraw, null);
        return rootView;
    }

    @Override
    public void initView() {
        editClearName = (EditClearWidget) rootView.findViewById(R.id.edit_clear_name);
        editClearMobile = (EditClearWidget) rootView.findViewById(R.id.edit_clear_mobile);
        editClearNum = (EditClearWidget) rootView.findViewById(R.id.edit_clear_num);
        btnConfirm = (Button) rootView.findViewById(R.id.btn_confirm);
        editClearMobile.showEditWidgetRightImage(true);
        editClearMobile.setClearEditRightImage(R.drawable.boc_callbook);
        setWidgetAttribute();
    }

    @Override
    public void initData() {
        setDefaultFocus();
        cleanViewData();
        if (mViewModel == null) {
            mViewModel = new MobileWithdrawViewModel();
        }
    }

    @Override
    public void setListener() {
        btnConfirm.setOnClickListener(this);
        editClearMobile.setRightImageCallBack(new EditClearWidget.EditRightImageOnClick() {
            @Override
            public void onClick() {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });
        editClearMobile.getContentEditText().addTextChangedListener(new Watcher());
    }

    @Override
    public void reInit() {
        initData();
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_transfer_mobile_withdraw_title);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected void titleLeftIconClick() {
        hideSoftInput();
        super.titleLeftIconClick();
    }

    @Override
    protected MobileWithdrawContract.Presenter initPresenter() {
        return new MobileWithdrawPresenter(this);
    }

    /**
     * view层model
     *
     * @return
     */
    public static MobileWithdrawViewModel getViewModel() {
        return mViewModel;
    }

    /**
     * 设置页面的默认焦点
     */
    private void setDefaultFocus() {
        editClearName.getTitleTextView().setFocusable(true);
        editClearName.getTitleTextView().setFocusableInTouchMode(true);
        editClearName.getTitleTextView().requestFocus();
    }

    /**
     * 设置控件属性
     */
    private void setWidgetAttribute() {
        // 设置输入手机号属性
        editClearMobile.getContentEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
        editClearMobile.getContentEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    /**
     * 清空页面数据
     */
    private void cleanViewData() {
        editClearName.setEditWidgetContent("");
        editClearMobile.setEditWidgetContent("");
        editClearNum.setEditWidgetContent("");
    }

    /**
     * 封装页面数据
     */
    private void buildViewModel() {
        mViewModel.setRemitNo(editClearNum.getEditWidgetContent());// 汇款编号
        mViewModel.setPayeeName(editClearName.getEditWidgetContent());// 收款人名称
        mViewModel.setPayeeMobile(editClearMobile.getEditWidgetContent().replaceAll("\\s*", ""));// 收款人手机号
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_confirm) {
            if (checkViewRegex()) {
                buildViewModel();
                showLoadingDialog();
                getPresenter().queryMobileWithdrawDetails(mViewModel);
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

            if(phoneList != null) {
                if (phoneList.size() == 1) {
                    phone = phoneList.get(0);
                    editClearMobile.setEditWidgetContent(PhoneNumberFormat.getPhoneNum(phone));

                    // 判断是否需要带回name
                    String name = editClearName.getEditWidgetContent();
                    if(TextUtils.isEmpty(name)) {
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
                            if(TextUtils.isEmpty(name)) {
                                editClearName.setEditWidgetContent(PhoneNumberFormat.getPhoneName(phone));
                            }
                        }
                    }).create().show();
                }
            }
        }
    }

    /**
     * 页面校验
     */
    private boolean checkViewRegex() {
        // 收款人名称
        RegexResult nameResult = RegexUtils.check(mContext, "mbdi_tr_mobilewith_payeeacctname",
                editClearName.getEditWidgetContent().replaceAll("\\s*", ""), true);
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

        // 汇款编号
        if (editClearNum.getEditWidgetContent().isEmpty()) {
            showErrorDialog(getResources().getString(R.string.boc_transfer_message_remit_num));
            return false;
        }
        return true;
    }

    @Override
    public void queryRandomFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        showErrorDialog(biiResultErrorException.getErrorMessage());
    }

    @Override
    public void queryRandomSuccess() {
        closeProgressDialog();
        MobileWithdrawConfirmFragment mobileWithdrawConfirmFragment = new MobileWithdrawConfirmFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("MobileWithdrawViewModel", mViewModel);
        mobileWithdrawConfirmFragment.setArguments(bundle);
        start(mobileWithdrawConfirmFragment);
    }

    @Override
    public void queryMobileWithdrawDetailsFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        showErrorDialog(biiResultErrorException.getErrorMessage());
    }

    @Override
    public void queryMobileWithdrawDetailsSuccess() {
        getPresenter().queryRandom();
    }

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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
