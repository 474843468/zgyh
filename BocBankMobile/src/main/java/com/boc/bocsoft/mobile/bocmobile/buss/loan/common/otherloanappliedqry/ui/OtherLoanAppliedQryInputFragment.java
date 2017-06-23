package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanappliedqry.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanappliedqry.OtherLoanAppliedQryConst;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.utils.regex.RegexResult;
import com.boc.bocsoft.mobile.framework.utils.regex.RegexUtils;


/**
 * 其他类型贷款申请进度查询--信息填写页面
 * Created by liuzc on 2016/8/15.
 */
public class OtherLoanAppliedQryInputFragment extends BussFragment {
    protected View rootView;

    private EditText edtName = null; //姓名\企业名称
    private EditText edtPhone = null; //手机号\邮箱
    private Button btnOK = null; //提交按钮


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = View.inflate(mContext, R.layout.boc_fragment_loan_other_applied_qry_input, null);
        return rootView;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    protected boolean isHaveTitleBarView() {
        return true;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    public void initView() {
        edtName = (EditText) rootView.findViewById(R.id.edtName);
        edtPhone = (EditText) rootView.findViewById(R.id.edtPhone);

        btnOK = (Button) rootView.findViewById(R.id.btn_ok);

    }

    @Override
    protected void titleLeftIconClick() {
        hideSoftInput();
        super.titleLeftIconClick();
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_loan_applied_online_qry);
    }

    @Override
    public void initData() {

    }

    @Override
    public void setListener() {
        btnOK.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });
    }

    /**
     * 点击提交
     */
    private void onSubmit(){
        hideSoftInput();
        String sName = edtName.getText().toString();
        String sPhone = edtPhone.getText().toString();

        // 收款人姓名
        RegexResult nameResult = RegexUtils.check(mContext, "mbdi_loan_otherqry_name",
                sName.replaceAll("\\s*", ""), true);
        if (!nameResult.isAvailable) {
            showErrorDialog(nameResult.errorTips);
            return;
        }

        //联系电话或者Email
        if(StringUtils.isEmptyOrNull(sPhone) || sPhone.indexOf(" ") > -1){
            //手机号和邮箱都不对，提示
            showErrorDialog(getResources().getString(R.string.boc_loan_phone_or_email_error));
            return;
        }
        else if(sPhone.indexOf("@") > -1){
            RegexResult emailResult = RegexUtils.check(mContext, "mbdi_loan_otherqry_email",
                    sPhone.replaceAll("\\s*", ""), true);
            if(!emailResult.isAvailable){
                showErrorDialog(emailResult.errorTips);
                return;
            }
        }
        else{
            RegexResult phoneResult = RegexUtils.check(mContext, "mbdi_loan_otherqry_phone",
                    sPhone.replaceAll("\\s*", ""), true);
            if(!phoneResult.isAvailable){
                showErrorDialog(phoneResult.errorTips);
                return;
            }
        }

        Bundle bundle = new Bundle();
        bundle.putString(OtherLoanAppliedQryConst.KEY_QRY_NAME, sName);
        bundle.putString(OtherLoanAppliedQryConst.KEY_QRY_PHONE, sPhone);

        OtherLoanAppliedQryListFragment loanOnlineQryListFragment = new OtherLoanAppliedQryListFragment();
        loanOnlineQryListFragment.setArguments(bundle);
        start(loanOnlineQryListFragment);
    }
}
