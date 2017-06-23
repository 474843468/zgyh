package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
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
import com.boc.bocsoft.mobile.bocmobile.base.utils.DeviceInfoUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.EShieldVerify;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.payment.ui.QRPayCodeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.model.QRPayResetPwdViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.model.QRPaySetPwdViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.presenter.QRPayPwdPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.utils.QrCodeModelUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.cfca.mobile.log.CodeException;

import cfca.mobile.sip.CFCASipDelegator;
import cfca.mobile.sip.SipBox;

/**
 * 二维码支付 - 重置支付密码
 * Created by wangf on 2016/8/22.
 */
public class QRPayResetPayPwdFragment extends BussFragment implements QRPayPwdContract.QrPaySecurityFactorView, QRPayPwdContract.QrPaySetPwdView,
        SecurityVerity.VerifyCodeResultListener, CFCASipDelegator {

    private View mRootView;

    private TextView tvPayPwdTip;
    private TextView tvQrPayInputPwdTip;

    //支付密码
    private SipBox sipBoxPayPwd;
    //确认支付密码
    private SipBox sipBoxPayPwdAgain;
    //安全工具的选择
    private EditChoiceWidget ecwSelectSecurity;
    //确认按钮
    private Button btnConfirm;

    private QRPayPwdPresenter qrSecurityFactorPresenter;
    private QRPayPwdPresenter qrPayResetPwdPresenter;

    private QRPayResetPwdViewModel qrPayResetPwdViewModel;

    // 选择的安全因子
    private CombinListBean selectCombin;

    //页面数据传递
    public static final String RESET_PASS_FROM_KEY = "ResetPassFromKey";
    public static final int RESET_PASS_FROM_CHANGE = 0;
    public static final int RESET_PASS_FROM_PAY = 1;
    public static final int RESET_PASS_FROM_C2B_FANSAO = 2;
    private int currentFrom;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = View.inflate(mContext, R.layout.boc_fragment_qrpay_set_paypwd, null);
        currentFrom = getArguments().getInt(RESET_PASS_FROM_KEY, RESET_PASS_FROM_CHANGE);
        return mRootView;
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_qrpay_reset_pay_pwd);
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
        tvPayPwdTip = (TextView) mRootView.findViewById(R.id.qrcode_pay_tv_pay_pwd_tip);
        tvQrPayInputPwdTip = (TextView) mRootView.findViewById(R.id.tv_qrpay_pwd_tip);
        sipBoxPayPwd = (SipBox) mRootView.findViewById(R.id.qrcode_pay_sipbox_pay_pwd);
        sipBoxPayPwdAgain = (SipBox) mRootView.findViewById(R.id.qrcode_pay_sipbox_pay_pwd_again);
        ecwSelectSecurity = (EditChoiceWidget) mRootView.findViewById(R.id.qrcode_pay_select_security);
        btnConfirm = (Button) mRootView.findViewById(R.id.qrcode_pay_btn_pay_pwd_confirm);
        tvPayPwdTip.setVisibility(View.VISIBLE);

        setCFCAAttribute();
    }

    @Override
    public void initData() {
        String strTip = getResources().getString(R.string.boc_qrpay_pay_pwd_tip);
        SpannableStringBuilder style = new SpannableStringBuilder(strTip);
        style.setSpan(new StyleSpan(Typeface.BOLD), 6, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvQrPayInputPwdTip.setText(style);

        qrSecurityFactorPresenter = new QRPayPwdPresenter((QRPayPwdContract.QrPaySecurityFactorView)this);
        qrPayResetPwdPresenter = new QRPayPwdPresenter((QRPayPwdContract.QrPaySetPwdView)this);

        qrPayResetPwdViewModel = new QRPayResetPwdViewModel();

        showLoadingDialog();
        qrSecurityFactorPresenter.loadPwdSecurityFactor();
    }

    @Override
    public void setListener() {
        //安全因子的选择
        ecwSelectSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示安全认证选择对话框
                SecurityVerity.getInstance().selectSecurityType();
            }
        });

        //确认按钮
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkViewRegex()) {
                    showLoadingDialog();
                    qrPayResetPwdPresenter.loadQRPaySetPassPre(buildResetPassPreViewModel());
                }
            }
        });
    }


    /**
     * 页面校验
     * @return
     */
    private boolean checkViewRegex(){
        if (sipBoxPayPwd.getText().toString().isEmpty()) {
            showErrorDialog("支付密码不能为空");
            return false;
        } else if (sipBoxPayPwd.getText().toString().length() != 6) {
            showErrorDialog("支付密码：6位数字");
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
        SecurityVerity.getInstance(getActivity()).setSecurityVerifyListener(QRPayResetPayPwdFragment.this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    /**
     * 封装重置支付密码 预交易 数据
     */
    private QRPaySetPwdViewModel buildResetPassPreViewModel(){
        QRPaySetPwdViewModel pwdPreViewModel = new QRPaySetPwdViewModel();

        // 设置密码控件随机数
        sipBoxPayPwd.setRandomKey_S(QRPayPwdPresenter.randomID);
        sipBoxPayPwdAgain.setRandomKey_S(QRPayPwdPresenter.randomID);

        try {
            pwdPreViewModel.set_combinId(selectCombin.getId());
            pwdPreViewModel.setPassword(sipBoxPayPwd.getValue().getEncryptPassword());
            pwdPreViewModel.setPassword_RC(sipBoxPayPwd.getValue().getEncryptRandomNum());
            pwdPreViewModel.setPasswordConform(sipBoxPayPwdAgain.getValue().getEncryptPassword());
            pwdPreViewModel.setPasswordConform_RC(sipBoxPayPwdAgain.getValue().getEncryptRandomNum());
            pwdPreViewModel.setState(SecurityVerity.SECURITY_VERIFY_STATE);
            pwdPreViewModel.setActiv(SecurityVerity.getInstance().getCfcaVersion());
        } catch (CodeException e) {
            e.printStackTrace();
        }
        return pwdPreViewModel;
    }


    /**
     * 封装重置支付密码 提交交易 数据
     */
    private QRPaySetPwdViewModel buildResetPassViewModel(int factorId, String[] randomNums, String[] encryptPasswords){
        QRPaySetPwdViewModel params = new QRPaySetPwdViewModel();

        // 设置密码控件随机数
        sipBoxPayPwd.setRandomKey_S(QRPayPwdPresenter.randomID);
        sipBoxPayPwdAgain.setRandomKey_S(QRPayPwdPresenter.randomID);
        try {
            params.setPassword(sipBoxPayPwd.getValue().getEncryptPassword());
            params.setPassword_RC(sipBoxPayPwd.getValue().getEncryptRandomNum());
            params.setPasswordConform(sipBoxPayPwdAgain.getValue().getEncryptPassword());
            params.setPasswordConform_RC(sipBoxPayPwdAgain.getValue().getEncryptRandomNum());
            params.setState(SecurityVerity.SECURITY_VERIFY_STATE);
            params.setActiv(SecurityVerity.getInstance().getCfcaVersion());
            params.setPassType("01");
        } catch (CodeException e) {
            e.printStackTrace();
        }

        switch (factorId) {
            case SecurityVerity.SECURITY_VERIFY_TOKEN:// 动态口令
                params.setOtp(encryptPasswords[0]);
                params.setOtp_RC(randomNums[0]);
                break;
            case SecurityVerity.SECURITY_VERIFY_SMS:// 短信
                params.setSmc(encryptPasswords[0]);
                params.setSmc_RC(randomNums[0]);
                break;
            case SecurityVerity.SECURITY_VERIFY_SMS_AND_TOKEN:// 动态口令+短信
                params.setOtp(encryptPasswords[0]);
                params.setOtp_RC(randomNums[0]);
                params.setSmc(encryptPasswords[1]);
                params.setSmc_RC(randomNums[1]);
                break;
            case SecurityVerity.SECURITY_VERIFY_DEVICE:// 手机交易码+硬件绑定
            	params.setSmc(encryptPasswords[0]);
                params.setSmc_RC(randomNums[0]);
                DeviceInfoModel deviceInfoModel = DeviceInfoUtils.getDeviceInfo(mContext, QRPayPwdPresenter.randomID);
                params.setDeviceInfo(deviceInfoModel.getDeviceInfo());
                params.setDeviceInfo_RC(deviceInfoModel.getDeviceInfo_RC());
                break;
            case SecurityVerity.SECURITY_VERIFY_E_TOKEN:// 中银e盾
                params.set_signedData(randomNums[0]);
                break;
            default:
                break;
        }

        return params;
    }


    /**
     * 设置CFCA密码键盘属性
     */
    private void setCFCAAttribute() {
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


    /*** 查询安全因子成功 */
    @Override
    public void loadPwdSecurityFactorSuccess(SecurityViewModel securityViewModel) {
        closeProgressDialog();
        // 传递安全因子给组件
        selectCombin = SecurityVerity.getInstance().
                getDefaultSecurityFactorId(new SecurityFactorModel(PublicUtils.copyOfSecurityCombin(securityViewModel)));

        //设置页面安全因子数据
        ecwSelectSecurity.setChoiceTextContent(selectCombin.getName());
    }

    /*** 查询安全因子失败 */
    @Override
    public void loadPwdSecurityFactorFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }


//    /*** 重置支付密码预交易成功 */
//    @Override
//    public void loadQRPayResetPassPreSuccess(QRPayResetPwdViewModel resetPwdViewModel) {
//        closeProgressDialog();
//        // 显示安全认证对话框
//        if (SecurityVerity.getInstance().confirmFactor(QrCodeModelUtil.copyToFactorListBean(resetPwdViewModel.getFactorList()))) {
//            SecurityVerity.getInstance().setConversationId(QRPayPwdPresenter.conversationID);
//            SecurityVerity.getInstance().showSecurityDialog(QRPayPwdPresenter.randomID);
//        }
//    }
//
//    /*** 重置支付密码预交易失败 */
//    @Override
//    public void loadQRPayResetPassPreFail(BiiResultErrorException biiResultErrorException) {
//        closeProgressDialog();
//    }
//
//    /*** 重置支付密码成功 */
//    @Override
//    public void loadQRPayResetPassSuccess() {
//        closeProgressDialog();
//        startWithPop(QRPaySetResultFragment.newInstance(QRPaySetResultFragment.QRPAY_PWD_RESET_SUCCESS));
//    }
//
//    /*** 重置支付密码失败 */
//    @Override
//    public void loadQRPayResetPassFail(BiiResultErrorException biiResultErrorException) {
//        closeProgressDialog();
//    }



    @Override
    public void onSecurityTypeSelected(CombinListBean bean) {
        //用户选择的安全工具
        selectCombin = bean;
        ecwSelectSecurity.setChoiceTextContent(selectCombin.getName());
    }

    @Override
    public void onEncryptDataReturned(String factorId, String[] randomNums, String[] encryptPasswords) {
        showLoadingDialog();
        // 重置支付密码
        qrPayResetPwdPresenter.loadQRPaySetPass(buildResetPassViewModel(Integer.valueOf(factorId), randomNums, encryptPasswords));
    }

    @Override
    public void onSignedReturn(String signRetData) {
        showLoadingDialog();
        String[] randomNums = {signRetData};
        String[] encryptPasswords = {};
        // 重置支付密码
        qrPayResetPwdPresenter.loadQRPaySetPass(buildResetPassViewModel(SecurityVerity.SECURITY_VERIFY_E_TOKEN, randomNums, encryptPasswords));
    }

    /*** 重置支付密码预交易成功 */
    @Override
    public void loadQRPaySetPassPreSuccess(QRPaySetPwdViewModel payPwdPassPreViewModel) {
        closeProgressDialog();
        // 显示安全认证对话框
        EShieldVerify.getInstance(getActivity()).setmPlainData(payPwdPassPreViewModel.get_plainData());
        if (SecurityVerity.getInstance().confirmFactor(QrCodeModelUtil.copyToFactorListBean(payPwdPassPreViewModel.getFactorList()))) {
            SecurityVerity.getInstance().setConversationId(QRPayPwdPresenter.conversationID);
            SecurityVerity.getInstance().showSecurityDialog(QRPayPwdPresenter.randomID);
        }
    }

    /*** 重置支付密码预交易失败 */
    @Override
    public void loadQRPaySetPassPreFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /*** 重置支付密码成功 */
    @Override
    public void loadQRPaySetPassSuccess() {
        closeProgressDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(QRPaySetResultFragment.QRPAY_RESULT_KEY, QRPaySetResultFragment.QRPAY_PWD_RESET_SUCCESS);
        bundle.putInt(RESET_PASS_FROM_KEY, currentFrom);
        QRPaySetResultFragment fragment = new QRPaySetResultFragment();
        fragment.setArguments(bundle);
        startWithPop(fragment);
//        startWithPop(QRPaySetResultFragment.newInstance(QRPaySetResultFragment.QRPAY_PWD_RESET_SUCCESS));
    }

    /*** 重置支付密码成功 */
    @Override
    public void loadQRPaySetPassFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
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
    
    
    @Override
    public boolean onBack() {
        if (currentFrom == RESET_PASS_FROM_CHANGE){
            popToAndReInit(QRPayChangePayPwdFragment.class);
        }else if (currentFrom == RESET_PASS_FROM_PAY){
            pop();
        }else if (currentFrom == RESET_PASS_FROM_C2B_FANSAO){
            popToAndReInit(QRPayCodeFragment.class);
        }else{
            pop();
        }
        return false;
    }    
    
}
