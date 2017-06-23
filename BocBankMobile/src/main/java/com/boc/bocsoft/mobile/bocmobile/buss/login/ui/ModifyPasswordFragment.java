package com.boc.bocsoft.mobile.bocmobile.buss.login.ui;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BaseMobileActivity;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.login.model.ModifyPasswordViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.login.presenter.ModifyPasswordPresenter;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;
import com.boc.bocsoft.mobile.framework.utils.regex.RegexResult;
import com.boc.bocsoft.mobile.framework.utils.regex.RegexUtils;
import com.cfca.mobile.log.CodeException;

import cfca.mobile.sip.CFCASipDelegator;
import cfca.mobile.sip.SipBox;

/**
 * Created by feibin on 2016/5/31.
 */
public class ModifyPasswordFragment extends BussFragment implements CFCASipDelegator,ModifyPasswordContract.View {
    protected View rootView;
    //原密码
    SipBox oldPasswordSipBox;
    //新密码
    SipBox newPasswordSipBox;
    //确认密码
    SipBox confirmPasswordSipBox;
    //关联账户后六位
    EditText accountEt;
    //确认按钮
    Button confirmBtn;
    /** 重置密码 密码输入正则*/
    private static final String RESETPWD = "^(?=(\\S*[0-9]\\S*[a-zA-Z]|\\S*[a-zA-Z]\\S*[0-9]))^\\S{8,20}$";
//    private static final String RESETPWD = "^(?=[\\s\\S]*[a-zA-Z0-9][\\s\\S]*$)(?![\\s\\S]*[^\\x00-\\xff]+[\\s\\S]*$)[\\S]{8,20}$";
    /** 登录和强制改密的原密码 */
    private static final String OLD_LOGIN_PWD = "^[\\s\\S]{6,20}$";

    private ModifyPasswordPresenter mModifyPasswordPresenter;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_modify_password, null);
        return rootView;
    }
    @Override
    public void initView() {
        oldPasswordSipBox = (SipBox) rootView.findViewById(R.id.oldPasswordSipBox);
        newPasswordSipBox = (SipBox) rootView.findViewById(R.id.newPasswordSipBox);
        confirmPasswordSipBox = (SipBox) rootView.findViewById(R.id.confirmPasswordSipBox);
        accountEt = (EditText) rootView.findViewById(R.id.accountEt);
        confirmBtn = (Button) rootView.findViewById(R.id.confirmBtn);
        /**
         * 设置CFCA密码键盘属性
         */
        //设置为国密算法
        oldPasswordSipBox.setCipherType(ApplicationConst.CIPHERTYPE);
        //输入密码设置的加密类型
        oldPasswordSipBox.setOutputValueType(ApplicationConst.OUT_PUT_VALUE_TYPE);
        //设置为全键盘 0 全键盘  1 数字键盘
        oldPasswordSipBox.setKeyBoardType(SipBox.COMPLETE_KEYBOARD);
        //设置最小输入长度
        oldPasswordSipBox.setPasswordMinLength(ApplicationConst.MIN_LENGTH);
        //设置最大输入长度
        oldPasswordSipBox.setPasswordMaxLength(ApplicationConst.MAX_LENGTH);
        oldPasswordSipBox.setPasswordRegularExpression(OLD_LOGIN_PWD);
        oldPasswordSipBox.setSipDelegator(this);

        //设置为国密算法
        newPasswordSipBox.setCipherType(ApplicationConst.CIPHERTYPE);
        //输入密码设置的加密类型
        newPasswordSipBox.setOutputValueType(ApplicationConst.OUT_PUT_VALUE_TYPE);
        //设置为全键盘 0 全键盘  1 数字键盘
        newPasswordSipBox.setKeyBoardType(SipBox.COMPLETE_KEYBOARD);
        //设置最小输入长度
        newPasswordSipBox.setPasswordMinLength(ApplicationConst.MIN_LENGTH_MODIFY);
        //设置最大输入长度
        newPasswordSipBox.setPasswordMaxLength(ApplicationConst.MAX_LENGTH);
        newPasswordSipBox.setPasswordRegularExpression(RESETPWD);
        newPasswordSipBox.setSipDelegator(this);

        //设置为国密算法
        confirmPasswordSipBox.setCipherType(ApplicationConst.CIPHERTYPE);
        //输入密码设置的加密类型
        confirmPasswordSipBox.setOutputValueType(ApplicationConst.OUT_PUT_VALUE_TYPE);
        //设置为全键盘 0 全键盘  1 数字键盘
        confirmPasswordSipBox.setKeyBoardType(SipBox.COMPLETE_KEYBOARD);
        //设置最小输入长度
        confirmPasswordSipBox.setPasswordMinLength(ApplicationConst.MIN_LENGTH_MODIFY);
        //设置最大输入长度
        confirmPasswordSipBox.setPasswordMaxLength(ApplicationConst.MAX_LENGTH);
        confirmPasswordSipBox.setPasswordRegularExpression(RESETPWD);
        confirmPasswordSipBox.setSipDelegator(this);

        //确认按钮点击事件
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**验证卡号、密码、验证码*/
//                if (checkModifyInputRegex()) {
//                    showLoadingDialog();
//                    mModifyPasswordPresenter.queryNamePasswordModMobile();
//                }
                //生产bug问题单修改
                showLoadingDialog(false);
                mModifyPasswordPresenter.queryNamePasswordModMobile();
            }

        });

    }


    /**
     * 校验
     * @return
     */
    private boolean checkModifyInputRegex() {

        RegexResult oldPasswordResult = RegexUtils.check(mContext,
                "login_modify_password_old", oldPasswordSipBox.getText().toString().trim(), true);
        if (!oldPasswordResult.isAvailable) {
            // 弹出校验错误的提示框
            showErrorDialog(oldPasswordResult.errorTips);
            return false;
        }
        RegexResult newPasswordResult = RegexUtils.check(mContext,
                "login_modify_password_new", newPasswordSipBox.getText().toString().trim(), true);
        if (!newPasswordResult.isAvailable) {
            // 弹出校验错误的提示框
            showErrorDialog(newPasswordResult.errorTips);
            return false;
        }
        RegexResult confirmPasswordResult = RegexUtils.check(mContext,
                "login_modify_password_confirm", confirmPasswordSipBox.getText().toString().trim(), true);
        if (!confirmPasswordResult.isAvailable) {
            // 弹出校验错误的提示框
            showErrorDialog(confirmPasswordResult.errorTips);
            return false;
        }
        if (accountEt.getText().toString().trim().length() < 6) {
            showErrorDialog("请输入六位数字");
            return false;
        }
        return true;
    }
    /**
     * 数据初始化
     */
    @Override
    public void initData() {
        mModifyPasswordPresenter = new ModifyPasswordPresenter(this);
    }


    /**
     * 获取随机数成功回调
     * @param random
     * @return
     */
    @Override
    public ModifyPasswordViewModel randomSuccess(String random) {
        //设置密码控件随机数
        oldPasswordSipBox.setRandomKey_S(random);
        newPasswordSipBox.setRandomKey_S(random);
        confirmPasswordSipBox.setRandomKey_S(random);

        ModifyPasswordViewModel modifyPasswordViewModel = new ModifyPasswordViewModel();
        modifyPasswordViewModel.setActiv(oldPasswordSipBox.getVersion()+"");

        if (StringUtils.isEmpty(oldPasswordSipBox.getText().toString().trim())) {
           showErrorDialog("请输入原密码");
            return null;
        }

        try{
            modifyPasswordViewModel.setOldPass(oldPasswordSipBox.getValue().getEncryptPassword());
            modifyPasswordViewModel.setOldPass_RC(oldPasswordSipBox.getValue().getEncryptRandomNum());
        }catch (CodeException e){
            showErrorDialog("原密码格式不正确");
            return null;
            //e.printStackTrace();
        }

        if (StringUtils.isEmpty(newPasswordSipBox.getText().toString().trim())) {
            showErrorDialog("请输入新密码");
            return null;
        }

        if ((newPasswordSipBox.getText().toString().trim().length()<8)
                ||(newPasswordSipBox.getText().toString().trim().length()>20)) {
            showErrorDialog("新密码长度必须为8-20位");
            return null;
        }
        try{
            modifyPasswordViewModel.setNewPass(newPasswordSipBox.getValue().getEncryptPassword());
            modifyPasswordViewModel.setNewPass_RC(newPasswordSipBox.getValue().getEncryptRandomNum());
        }catch (CodeException e){
            showErrorDialog("新密码必须包含数字和字母,请修改");
//            e.printStackTrace();
            return null;
        }

        if (StringUtils.isEmpty(confirmPasswordSipBox.getText().toString().trim())) {
            showErrorDialog("请输入确认密码");
            return null;
        }

        if ((confirmPasswordSipBox.getText().toString().trim().length()<8)
                ||(confirmPasswordSipBox.getText().toString().trim().length()>20)) {
            showErrorDialog("确认密码长度必须为8-20位");
            return null;
        }

        try{
            modifyPasswordViewModel.setNewPass2(confirmPasswordSipBox.getValue().getEncryptPassword());
            modifyPasswordViewModel.setNewPass2_RC(confirmPasswordSipBox.getValue().getEncryptRandomNum());
        }catch (CodeException e){
            showErrorDialog("确认密码必须包含数字和字母,请修改");
//            e.printStackTrace();
            return null;
        }

        if (accountEt.getText().toString().trim().length() < 6) {
            showErrorDialog("请输入六位数字");
            return null;
        }
        modifyPasswordViewModel.setLoginName("");
        modifyPasswordViewModel.setCardNoSixLast(accountEt.getText().toString().trim());
        modifyPasswordViewModel.setCombinId("8");
        modifyPasswordViewModel.setState("");
        return modifyPasswordViewModel;
    }

    /**
     * 修改密码成功回调
     */
    @Override
    public void namePasswordModMobileSuccess() {

        showErrorDialog("密码修改成功，请使用新密码重新登录");
        ((BaseMobileActivity)getActivity()).setErrorDialogClickListener(new BaseMobileActivity.ErrorDialogClickCallBack() {
            @Override
            public void onEnterBtnClick() {
                mModifyPasswordPresenter.queryLogout();
            }
        });
    }

    /**
     * 退出成功回调
     */
    @Override
    public void logoutSuccess() {
        ApplicationContext.getInstance().logout();
        ModuleActivityDispatcher.popToHomePage();
        Intent intent = new Intent();
        intent.setClass(mContext, LoginBaseActivity.class);
        Activity activity = ActivityManager.getAppManager().currentActivity();
        try {

            if (activity != null) {
                startActivity(intent);
            } else {
                startActivity(intent);
            }
        } catch (Exception e) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void setPresenter(ModifyPasswordContract.Presenter presenter) {

    }

    /**CFCA监听时间--------start*/
    @Override
    public void beforeKeyboardShow(SipBox sipBox, int i) {

    }

    @Override
    public void afterKeyboardHidden(SipBox sipBox, int i) {

    }

    @Override
    public void afterClickDown(SipBox sipBox) {

    }
    /**CFCA监听时间--------end*/
    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return "修改密码";
    }

    @Override
    protected boolean isDisplayLeftIcon() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    public boolean onBackPress() {
        return true;
    }
}
