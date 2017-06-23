package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.SecurityViewModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.model.QRPayChangePwdViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.presenter.QRPayPwdPresenter;
import com.cfca.mobile.log.CodeException;

import cfca.mobile.sip.CFCASipDelegator;
import cfca.mobile.sip.SipBox;

/**
 * 二维码支付 - 修改支付密码
 * Created by wangf on 2016/8/23.
 */
public class QRPayChangePayPwdFragment extends BussFragment implements SecurityVerity.VerifyCodeResultListener,
        QRPayPwdContract.QrPaySecurityFactorView, QRPayPwdContract.QrPayChangePwdView, CFCASipDelegator {

    private View mRootView;

    private TextView tvSetPwdTitle;
    //旧支付密码
    private SipBox sipBoxPayPwdLast;
    //支付密码
    private SipBox sipBoxPayPwd;
    //确认支付密码
    private SipBox sipBoxPayPwdAgain;
    //忘记密码
    private TextView tvForgetPwd;
    //确认按钮
    private Button btnConfirm;

    private QRPayPwdPresenter qrSecurityFactorPresenter;
    private QRPayPwdPresenter qrPayChangePwdPresenter;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = View.inflate(mContext, R.layout.boc_fragment_qrpay_change_paypwd, null);
        return mRootView;
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_qrpay_change_pay_pwd);
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
    public void beforeInitView() {
        super.beforeInitView();
    }

    @Override
    public void initView() {
    	tvSetPwdTitle = (TextView)mRootView.findViewById(R.id.tv_set_pwd_title);
        sipBoxPayPwdLast = (SipBox) mRootView.findViewById(R.id.qrcode_pay_sipbox_pay_pwd_last);
        sipBoxPayPwd = (SipBox) mRootView.findViewById(R.id.qrcode_pay_sipbox_pay_pwd_new);
        sipBoxPayPwdAgain = (SipBox) mRootView.findViewById(R.id.qrcode_pay_sipbox_pay_pwd_new_again);

        tvForgetPwd = (TextView) mRootView.findViewById(R.id.qrcode_pay_btn_pay_forget);
        btnConfirm = (Button) mRootView.findViewById(R.id.qrcode_pay_btn_pay_pwd_confirm);

        setCFCAAttribute();
    }

    @Override
    public void initData() {
    	setDefaultFocus();
        qrSecurityFactorPresenter = new QRPayPwdPresenter((QRPayPwdContract.QrPaySecurityFactorView)this);
        qrPayChangePwdPresenter = new QRPayPwdPresenter((QRPayPwdContract.QrPayChangePwdView)this);


        showLoadingDialog();
        qrSecurityFactorPresenter.loadPwdSecurityFactor();
    }
    
    
    /**
     * 设置页面的默认焦点
     */
    private void setDefaultFocus() {
    	tvSetPwdTitle.setFocusable(true);
    	tvSetPwdTitle.setFocusableInTouchMode(true);
    	tvSetPwdTitle.requestFocus();
    }
    
    
    
    @Override
    public void reInit() {
    	setDefaultFocus();
    }
    

    @Override
    public void setListener() {
        tvForgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(new QRPayResetPayPwdFragment());
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	if(checkViewRegex()){
            		showLoadingDialog();
            		qrPayChangePwdPresenter.loadQRPayChangePass(buildChangePassViewModel());
            	}
            }
        });
    }
    
    
    /**
     * 页面校验
     * @return
     */
    private boolean checkViewRegex(){
        if (sipBoxPayPwdLast.getText().toString().isEmpty()) {
            showErrorDialog("原密码不能为空");
            return false;
        } else if (sipBoxPayPwdLast.getText().toString().length() != 6) {
            showErrorDialog("原密码：6位数字");
            return false;
        }
        if (sipBoxPayPwd.getText().toString().isEmpty()) {
            showErrorDialog("新密码不能为空");
            return false;
        } else if (sipBoxPayPwd.getText().toString().length() != 6) {
            showErrorDialog("新密码：6位数字");
            return false;
        }
        if (sipBoxPayPwdAgain.getText().toString().isEmpty()) {
            showErrorDialog("确认密码不能为空");
            return false;
        } else if (sipBoxPayPwdAgain.getText().toString().length() != 6) {
            showErrorDialog("确认密码：6位数字");
            return false;
        }
        return true;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SecurityVerity.getInstance(getActivity()).setSecurityVerifyListener(QRPayChangePayPwdFragment.this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    /**
     * 封装修改支付密码  数据
     */
    private QRPayChangePwdViewModel buildChangePassViewModel(){
        QRPayChangePwdViewModel params = new QRPayChangePwdViewModel();

        // 设置密码控件随机数
        sipBoxPayPwdLast.setRandomKey_S(QRPayPwdPresenter.randomID);
        sipBoxPayPwd.setRandomKey_S(QRPayPwdPresenter.randomID);
        sipBoxPayPwdAgain.setRandomKey_S(QRPayPwdPresenter.randomID);
        try {
            params.setOldPass(sipBoxPayPwdLast.getValue().getEncryptPassword());
            params.setOldPass_RC(sipBoxPayPwdLast.getValue().getEncryptRandomNum());
            params.setNewPass(sipBoxPayPwd.getValue().getEncryptPassword());
            params.setNewPass_RC(sipBoxPayPwd.getValue().getEncryptRandomNum());
            params.setNewPass2(sipBoxPayPwdAgain.getValue().getEncryptPassword());
            params.setNewPass2_RC(sipBoxPayPwdAgain.getValue().getEncryptRandomNum());
            params.setPassType("01");
            params.setState(SecurityVerity.SECURITY_VERIFY_STATE);
            params.setActiv(SecurityVerity.getInstance().getCfcaVersion());
        } catch (CodeException e) {
            e.printStackTrace();
        }

        return params;
    }


    /**
     * 设置CFCA密码键盘属性
     */
    private void setCFCAAttribute() {
        //设置为国密算法
        sipBoxPayPwdLast.setCipherType(ApplicationConst.CIPHERTYPE);
        //输入密码设置的加密类型
        sipBoxPayPwdLast.setOutputValueType(ApplicationConst.OUT_PUT_VALUE_TYPE);
        //设置为全键盘 0 全键盘  1 数字键盘
        sipBoxPayPwdLast.setKeyBoardType(SipBox.NUMBER_KEYBOARD);
        //设置最小输入长度
        sipBoxPayPwdLast.setPasswordMinLength(ApplicationConst.MIN_LENGTH - 1);
        //设置最大输入长度
        sipBoxPayPwdLast.setPasswordMaxLength(ApplicationConst.MIN_LENGTH);
        sipBoxPayPwdLast.setSipDelegator(this);

        //设置为国密算法
        sipBoxPayPwd.setCipherType(ApplicationConst.CIPHERTYPE);
        //输入密码设置的加密类型
        sipBoxPayPwd.setOutputValueType(ApplicationConst.OUT_PUT_VALUE_TYPE);
        //设置为全键盘 0 全键盘  1 数字键盘
        sipBoxPayPwd.setKeyBoardType(SipBox.NUMBER_KEYBOARD);
        //设置最小输入长度
        sipBoxPayPwd.setPasswordMinLength(ApplicationConst.MIN_LENGTH - 1);
        //设置最大输入长度
        sipBoxPayPwd.setPasswordMaxLength(ApplicationConst.MIN_LENGTH);
        sipBoxPayPwd.setSipDelegator(this);

        //设置为国密算法
        sipBoxPayPwdAgain.setCipherType(ApplicationConst.CIPHERTYPE);
        //输入密码设置的加密类型
        sipBoxPayPwdAgain.setOutputValueType(ApplicationConst.OUT_PUT_VALUE_TYPE);
        //设置为全键盘 0 全键盘  1 数字键盘
        sipBoxPayPwdAgain.setKeyBoardType(SipBox.NUMBER_KEYBOARD);
        //设置最小输入长度
        sipBoxPayPwdAgain.setPasswordMinLength(ApplicationConst.MIN_LENGTH - 1);
        //设置最大输入长度
        sipBoxPayPwdAgain.setPasswordMaxLength(ApplicationConst.MIN_LENGTH);
        sipBoxPayPwdAgain.setSipDelegator(this);
    }


    /*** 修改支付密码成功 */
    @Override
    public void loadQRPayChangePassSuccess() {
        closeProgressDialog();
        startWithPop(QRPaySetResultFragment.newInstance(QRPaySetResultFragment.QRPAY_PWD_CHANGE_SUCCESS));
    }

    /*** 修改支付密码失败*/
    @Override
    public void loadQRPayChangePassFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }


    /*** 查询安全因子成功 */
    @Override
    public void loadPwdSecurityFactorSuccess(SecurityViewModel securityViewModel) {
        closeProgressDialog();
    }

    /*** 查询安全因子失败 */
    @Override
    public void loadPwdSecurityFactorFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }



    @Override
    public void onSecurityTypeSelected(CombinListBean bean) {

    }

    @Override
    public void onEncryptDataReturned(String factorId, String[] randomNums, String[] encryptPasswords) {

    }

    @Override
    public void onSignedReturn(String signRetData) {

    }

	@Override
	public void afterClickDown(SipBox sipBox) {
		// TODO Auto-generated method stub
		sipBox.hideSecurityKeyBoard();
	}

	@Override
	public void afterKeyboardHidden(SipBox arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeKeyboardShow(SipBox arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}
