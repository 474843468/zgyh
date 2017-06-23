package com.boc.bocsoft.mobile.bocmobile.buss.login.ui;

import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cfca.mobile.sip.CFCASipDelegator;
import cfca.mobile.sip.SipBox;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConfig;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ImageLoaderUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PhoneNumberFormat;
import com.boc.bocsoft.mobile.bocmobile.base.utils.SpUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.login.model.LoginViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.login.presenter.LoginPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.system.common.utils.UserPortraitUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.utils.regex.RegexResult;
import com.boc.bocsoft.mobile.framework.utils.regex.RegexUtils;
import com.boc.bocsoft.mobile.framework.widget.imageLoader.itf.LoadListener;
import com.boc.bocsoft.remoteopenacc.buss.activity.RemoteOpenAccActivity;
import com.cfca.mobile.log.CodeException;

/**
 * Created by feib on 16/7/13.
 * 登录主页面fragment
 */
public class LoginMainFragment extends LoginBaseFragment implements LoginContract.View,
        CFCASipDelegator, View.OnClickListener {

    //是否需要验证码
    private String needValidationChars = "";
    //登录service通信处理类
    private LoginPresenter mLoginPresenter;
    private View rootView;
    /**
     * UI界面View定义
     */
    protected ImageView loginPasswordDeleteIv;
    protected ImageView bocLogoIv;
    protected EditText loginNameEt;
    protected SipBox passwordSipBox;
    protected Button loginSubmitBtn;
    protected ImageView loginCodeIv;
    protected RelativeLayout loginCodeRl;
    protected EditText loginCodeEt;
    protected TextView queryBalanceTv;
    protected TextView resetPasswordTv;
    protected TextView selfRegisterTv;
    protected TextView openElectricAccountTv;
    /**
     * 登录和强制改密的原密码
     */
    public static final String OLD_LOGIN_PWD = "^[\\s\\S]{6,20}$";
    /**
     * 登录用户名
     */
    protected String loginName = "";

    /**
     * 偏移量
     */
    private int offset = 0;
    //密码输入框是否聚焦
    private boolean passHasFoucs = false;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.boc_fragment_login_main,
                null);

        return rootView;
    }


    @Override
    public void initView() {
        loginNameEt = (EditText) rootView.findViewById(R.id.loginNameEt);
        bocLogoIv = (ImageView) rootView.findViewById(R.id.bocLogoIv);
        //获取保存的登录用户名
        if (SpUtils.getLNhoneSpString(mContext, "loginName", null) != null) {
            preferencePhone = SpUtils.getLNhoneSpString(mContext, "loginName", null);
            encryptPhone = PhoneNumberFormat.getThreeFourThreeString(preferencePhone);
            loginNameEt.setText(encryptPhone);
            loginNameEt.setSelection(loginNameEt.getText().toString().length());
            //设置头像
            bocLogoIv.setImageBitmap(UserPortraitUtils.loadProtrait(preferencePhone));

        } else {
            //设置默认头像
            bocLogoIv.setImageBitmap(UserPortraitUtils.getDefaultBitmap());
        }

        loginNameEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (loginNameEt.getText().toString().contains("*")) {
                    loginNameEt.setText("");
                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        loginNameEt.setLongClickable(false);

        queryBalanceTv = (TextView) rootView.findViewById(R.id.queryBalanceTv);
        resetPasswordTv = (TextView) rootView.findViewById(R.id.resetPasswordTv);
        selfRegisterTv = (TextView) rootView.findViewById(R.id.selfRegisterTv);
        openElectricAccountTv = (TextView) rootView.findViewById(R.id.openElectricAccountTv);
        passwordSipBox = (SipBox) rootView.findViewById(R.id.loginPasswordSipBox);
        loginPasswordDeleteIv = (ImageView) rootView.findViewById(R.id.loginPasswordDeleteIv);
        loginPasswordDeleteIv.setVisibility(View.INVISIBLE);
        loginSubmitBtn = (Button) rootView.findViewById(R.id.loginSubmitBtn);
        loginCodeIv = (ImageView) rootView.findViewById(R.id.loginCodeIv);
        loginCodeRl = (RelativeLayout) rootView.findViewById(R.id.loginCodeRl);
        loginCodeEt = (EditText) rootView.findViewById(R.id.loginCodeEt);
        loginCodeRl.setVisibility(View.GONE);
        /**
         * 设置CFCA密码键盘属性
         */
        //设置为国密算法
        passwordSipBox.setCipherType(ApplicationConst.CIPHERTYPE);
        //输入密码设置的加密类型
        passwordSipBox.setOutputValueType(ApplicationConst.OUT_PUT_VALUE_TYPE);
        //设置为全键盘 0 全键盘  1 数字键盘
        passwordSipBox.setKeyBoardType(SipBox.COMPLETE_KEYBOARD);
        //设置最小输入长度
        passwordSipBox.setPasswordMinLength(ApplicationConst.MIN_LENGTH);
        //设置最大输入长度
        passwordSipBox.setPasswordMaxLength(ApplicationConst.MAX_LENGTH);
        passwordSipBox.setPasswordRegularExpression(OLD_LOGIN_PWD);
        passwordSipBox.setSipDelegator(this);
        passwordSipBox.setDoneKeyTitle("完成");
    }

    /**
     * 数据初始化
     */
    @Override
    public void initData() {
        super.initData();
        mLoginPresenter = new LoginPresenter(this);
        ApplicationContext.getInstance().setActive(passwordSipBox.getVersion() + "");
        ApplicationContext.getInstance().setState("41943040");
//        ApplicationContext.getInstance().logout();
    }

    @Override
    public void setListener() {
        //快速查询卡余额监听
        queryBalanceTv.setOnClickListener(this);
        //重置密码监听
        resetPasswordTv.setOnClickListener(this);
        //自助注册监听
        selfRegisterTv.setOnClickListener(this);
        //电子账户开户监听
        openElectricAccountTv.setOnClickListener(this);
        //手机号登录监听
        loginSubmitBtn.setOnClickListener(this);
        //验证码图片监听
        loginCodeIv.setOnClickListener(this);
        //密码清空按钮
        loginPasswordDeleteIv.setOnClickListener(this);
        passwordSipBox.setOnFocusChangeListener(onFocusChangeListener);
        passwordSipBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (passHasFoucs) {
                    setClearIconVisible(s.length() > 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /**
     * 设置清除图标的显示与隐藏
     */
    public void setClearIconVisible(boolean visible) {
        if(visible){
            //显示删除按钮
            loginPasswordDeleteIv.setVisibility(View.VISIBLE);
        }else{
            loginPasswordDeleteIv.setVisibility(View.INVISIBLE);
        }
    }

    View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
                passHasFoucs = hasFocus;
            if(hasFocus){
                if(passwordSipBox.getText().toString().length() > 0){
                    //显示删除按钮
                    loginPasswordDeleteIv.setVisibility(View.VISIBLE);
                }else{
                    loginPasswordDeleteIv.setVisibility(View.INVISIBLE);
                }
            }else{
                loginPasswordDeleteIv.setVisibility(View.INVISIBLE);
                passwordSipBox.hideSecurityKeyBoard();
            }
        }
    };

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.queryBalanceTv) {
            //清除cookie
            ApplicationContext.getInstance().logout();
            //快速查询卡余额点击
            start(new CardBalanceQueryFragment());
        } else if (view.getId() == R.id.resetPasswordTv) {
            //清除cookie
            ApplicationContext.getInstance().logout();
            //忘记密码点击
            //跳转到联龙找回密码页面 调用联龙接口 测试案例覆盖
            LoginContext.instance.gotoForgetPasswordModule(getActivity());

        } else if (view.getId() == R.id.selfRegisterTv) {
            //清除cookie
            ApplicationContext.getInstance().logout();
            //自助注册点击
            //跳转到联龙自助注册页面 调用联龙接口 测试案例覆盖
            LoginContext.instance.gotoRegistModule(getActivity());
        } else if (view.getId() == R.id.openElectricAccountTv) {
            //电子账户开户点击
            // TODO: 16/8/17 跳转到远程开户
            Intent intent = new Intent();
            intent.setClass(getActivity(), RemoteOpenAccActivity.class);
            getActivity().startActivity(intent);

        } else if (view.getId() == R.id.loginSubmitBtn) {
            passwordSipBox.clearFocus();
            passwordSipBox.hideSecurityKeyBoard();
            //手机号登录点击
            /**验证登录用户名、密码、验证码*/
            if (checkLoginInputRegex()) {
                //刚进入登录页面，没有验证码的时候，清除cookies
                if(!(View.VISIBLE == loginCodeEt.getVisibility())){
                    //清除cookie
                    ApplicationContext.getInstance().logout();
                }
                requestPhoneNumberLogin();
            }

        } else if (view.getId() == R.id.loginCodeIv) {
            //验证码图片点击
            requestVerificationCode();
        }else if (view.getId() == R.id.loginPasswordDeleteIv) {
            //密码清除按钮
            passwordSipBox.clearText();
            passwordSipBox.hideSecurityKeyBoard();
            loginPasswordDeleteIv.setVisibility(View.INVISIBLE);
        }

    }



    /**
     * 登录输入校验
     */
    private Boolean checkLoginInputRegex() {
        loginName = loginNameEt.getText().toString().trim();
        if (!StringUtils.isEmpty(loginName)
                && loginName.equals(encryptPhone)) {
            loginName = preferencePhone;
        }
        RegexResult nameResult = RegexUtils.check(mContext,
                "login_phone_name", loginName, true);
        if (!nameResult.isAvailable) {
            // 弹出校验错误的提示框
            showErrorDialog(nameResult.errorTips);
            return false;
        }

        RegexResult passwordResult = RegexUtils.check(mContext,
                "login_phone_password", passwordSipBox.getText()
                        .toString(), true);
        Log.i("feib", "passwordSipBox--->" + passwordSipBox.getText()
                .toString());
        if (!passwordResult.isAvailable) {
            // 弹出校验错误的提示框
            showErrorDialog(passwordResult.errorTips);
            return false;
        }

        if (!StringUtils.isEmptyOrNull(needValidationChars) && View.VISIBLE == loginCodeEt.getVisibility()) {
            if (StringUtils.isEmpty(loginCodeEt.getText().toString())) {
                showErrorDialog("请输入验证码");
                return false;
            }
            if (loginCodeEt.getText().toString().length() != 4) {
                showErrorDialog("验证码无效");
                return false;
            }
        }
        return true;
    }

    /**
     * 是否显示标题右侧按钮
     *
     * @return
     */
    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    /**
     * 是否显示红色标题头
     *
     * @return
     */
    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    /**
     * 获取验证码
     */
    private void requestVerificationCode() {
        ImageLoaderUtils.getBIIImageLoader()
                .load(ApplicationConfig.getVerificationCodeUrl())
                .refresh(true)
                .into(loginCodeIv, new LoadListener() {
                    @Override
                    public void onSuccess() {
                        //验证码获取成功回调

                    }

                    @Override
                    public void onError() {
                        //验证码获取失败回调
//                        showErrorDialog("验证码获取失败");
                    }
                });

    }

    /**
     * 手机号登录
     */
    private void requestPhoneNumberLogin() {

        showLoadingDialog(false);
        //登录接口调用
        mLoginPresenter.queryLogin();
    }

    /**
     * 随机数获取成功回调
     */
    @Override
    public LoginViewModel randomPreSuccess(String random) {
        //设置密码控件随机数
        passwordSipBox.setRandomKey_S(random);
        //封装UI数据model

        mLoginViewModel.setLoginName(loginName);
        try {
            mLoginViewModel.setPassword(passwordSipBox.getValue().getEncryptPassword());
            mLoginViewModel.setPassword_RC(passwordSipBox.getValue().getEncryptRandomNum());
        } catch (CodeException e) {
            showErrorDialog("原密码格式不正确");
            //e.printStackTrace();
            return null;
        }
        if (!StringUtils.isEmptyOrNull(needValidationChars)) {
            mLoginViewModel.setNeedValidationChars(loginCodeEt.getText().toString());
        }
        mLoginViewModel.setActiv(passwordSipBox.getVersion() + "");
        mLoginViewModel.setState("");
        return mLoginViewModel;
    }


    /**
     * 登录失败回调
     */
    @Override
    public void loginFail(BiiResultErrorException biiResultErrorException) {
        if(biiResultErrorException!= null && "ValidationException".equals(biiResultErrorException.getErrorType())){
            //用户名密码错误
            needValidationChars = "1";
            loginCodeEt.setText("");
            loginCodeRl.setVisibility(View.VISIBLE);
            loginCodeEt.setFocusable(true);
            loginCodeEt.setFocusableInTouchMode(true);
            loginCodeEt.requestFocus();
            requestVerificationCode();
        }
    }

    /**
     * 登录成功回调
     */
    @Override
    public void loginSuccess(LoginViewModel loginModel) {
        mLoginViewModel = loginModel;
//        Toast.makeText(mContext, "登录接口返回成功", Toast.LENGTH_LONG).show();
        //判断是否需要请求验证码
        if (judgeIsNeedValidationChars()) {
            return;
        }
        //根据不同的客户注册类型处理  柜台注册：1  批量注册：3
        combinStatus = loginModel.getCombinStatus();
        regtype = loginModel.getRegtype();
        if ("3".equals(regtype) && StringUtils.isEmpty(combinStatus)) {
            closeProgressDialog();
            ProtocolFragment protocolFragment = new ProtocolFragment();
            start(protocolFragment);
            return;
        }

        if (!StringUtils.isEmpty(combinStatus)) {
            if (combinStatus.equals("11")) {// 主客户首次登陆
                closeProgressDialog();
                ProtocolFragment protocolFragment = new ProtocolFragment();
                start(protocolFragment);
                return;
            } else if (combinStatus.equals("10")) {// 从客户首次登陆
                //请求登录后获取客户合并后信息
                mLoginBasePresenter.queryPsnCustomerCombinInfo();
                return;
            }
        }

        //判断是否需要修改密码  0：正常1：柜台首次登录2：柜台重置密码
        judgeModifyPassword();

    }

    /**
     * 判断是否需要验证码
     */
    private Boolean judgeIsNeedValidationChars() {
        needValidationChars = mLoginViewModel.getNeedValidationChars();
        if (!StringUtils.isEmptyOrNull(needValidationChars)) {
            // 存在上次登陆状态字段,1代表上次登陆失败,需输入图形验证码登录。
            if ("1".equals(needValidationChars)) {
                closeProgressDialog();
                loginCodeRl.setVisibility(View.VISIBLE);
                loginCodeEt.setText("");
                loginCodeEt.setFocusable(true);
                loginCodeEt.setFocusableInTouchMode(true);
                loginCodeEt.requestFocus();
                requestVerificationCode();
                return true;
            }
        }
        return false;
    }


    private Handler handler = new Handler();

    @Override
    public void beforeKeyboardShow(SipBox sipBox, int keyboardHeigt) {
        if (offset < 0) {
            handler.removeCallbacksAndMessages(null);
            return;
        }

        int[] locationSubmit = new int[2];
        loginSubmitBtn.getLocationOnScreen(locationSubmit);
        //int[] locationLogo = new int[2];
        //bocLogoIv.getLocationOnScreen(locationLogo);
        int bottomSubmit = locationSubmit[1] + loginSubmitBtn.getMeasuredHeight();
        int screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        int keyboardY = screenHeight - keyboardHeigt;
        offset = keyboardY - bottomSubmit;
        if (offset < 0) {
            rootView.scrollBy(0, -offset);
            //bocLogoIv.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void afterKeyboardHidden(SipBox sipBox, int i) {
        if (offset < 0) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    rootView.scrollBy(0, offset);
                    //bocLogoIv.setVisibility(View.VISIBLE);
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
     * 处理activity onStop时，结束RXjava通信
     */

    @Override
    protected String getTitleValue() {
        return "";
    }

    @Override
    public boolean onBackPress() {
        LoginContext.instance.setCallback(null);
        return super.onBackPress();
    }

}
