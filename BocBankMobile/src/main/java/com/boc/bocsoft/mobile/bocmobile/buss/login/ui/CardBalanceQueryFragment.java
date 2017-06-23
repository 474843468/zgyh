package com.boc.bocsoft.mobile.bocmobile.buss.login.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConfig;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ImageLoaderUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MapUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.login.model.CardBalanceLoginViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.login.model.CreditCardDetailViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.login.model.DebitCardDetailViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.login.presenter.CardBalanceQueryPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui.ScanCardNumFragment;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;
import com.boc.bocsoft.mobile.framework.utils.regex.RegexResult;
import com.boc.bocsoft.mobile.framework.utils.regex.RegexUtils;
import com.boc.bocsoft.mobile.framework.widget.imageLoader.itf.LoadListener;
import com.cfca.mobile.log.CodeException;

import java.util.HashMap;
import java.util.Map;

import cfca.mobile.sip.CFCASipDelegator;
import cfca.mobile.sip.SipBox;

/**
 * Created by feib on 16/8/1.
 */
public class CardBalanceQueryFragment extends BussFragment implements
        CardBalanceQueryContract.View, CFCASipDelegator, View.OnClickListener {

    protected LinearLayout cardPasswordLl;
    protected EditText cardNumEt;
    protected ImageView cameraIv;
    protected SipBox cardPasswordSipBox;
    protected EditText checkCodeEt;
    protected ImageView checkCodeIv;
    protected Button querySubmitBtn;
    protected View rootView;
    protected ImageView cardPasswordDeleteIv;
    //卡号登录UI层model
    private CardBalanceLoginViewModel mCardBalanceLoginViewModel;
    private CardBalanceQueryPresenter cardBalanceQueryPresenter;
    //是否关闭当前activity
    private boolean isFinishActivity = false;
    private static final String REGULAR_EXPRESSION = "\\S*";
    /**
     * 卡类型   1-信用卡 ，2-借记卡
     **/
    private static final Map<String, String> cardType = new HashMap<String, String>() {
        {
            put("103", "1");
            put("104", "1");
            put("107", "1");
            put("119", "2");
        }
    };
    private static final int REQUEST_CODE_SCAN_CARD = 111; // 扫描银行卡
    //卡类型
    private String accountType;
    //卡类型标识
    private String cardTypeId;
    //卡查询成功
    private boolean isCardQuerySuccess = false;
    //是否查询卡类型
    private Boolean isQueryCard = true;
    //密码输入框是否聚焦
    private Boolean passHasFoucs;
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.boc_fragment_card_balance_query,
                null);

        return rootView;
    }


    @Override
    public void initView() {

        cardNumEt = (EditText) rootView.findViewById(R.id.cardNumEt);
        cameraIv = (ImageView) rootView.findViewById(R.id.cameraIv);
        cardPasswordLl = (LinearLayout) rootView.findViewById(R.id.cardPasswordLl);
        cardPasswordLl.setVisibility(View.GONE);
        cardPasswordSipBox = (SipBox) rootView.findViewById(R.id.cardPasswordSipBox);
        checkCodeEt = (EditText) rootView.findViewById(R.id.checkCodeEt);
        checkCodeIv = (ImageView) rootView.findViewById(R.id.checkCodeIv);
        querySubmitBtn = (Button) rootView.findViewById(R.id.querySubmitBtn);
        cardPasswordDeleteIv = (ImageView) rootView.findViewById(R.id.cardPasswordDeleteIv);
        cardPasswordDeleteIv.setVisibility(View.INVISIBLE);
        /**
         * 设置CFCA密码键盘属性
         */
        //设置为国密算法
        cardPasswordSipBox.setCipherType(ApplicationConst.CIPHERTYPE);
        //输入密码设置的加密类型
        cardPasswordSipBox.setOutputValueType(ApplicationConst.OUT_PUT_VALUE_TYPE_OTP);
        //设置为全键盘 0 全键盘  1 数字键盘
        cardPasswordSipBox.setKeyBoardType(SipBox.NUMBER_KEYBOARD);
        //设置最小输入长度
        cardPasswordSipBox.setPasswordMinLength(ApplicationConst.MIN_LENGTH);
        //设置最大输入长度
        cardPasswordSipBox.setPasswordMaxLength(ApplicationConst.MAX_LENGTH);
        cardPasswordSipBox.setPasswordRegularExpression(REGULAR_EXPRESSION);
        cardPasswordSipBox.setSipDelegator(this);

    }

    /**
     * 数据初始化
     */
    @Override
    public void initData() {
        cardBalanceQueryPresenter = new CardBalanceQueryPresenter(this);
        mCardBalanceLoginViewModel = new CardBalanceLoginViewModel();
        requestVerificationCode();
    }

    @Override
    public void setListener() {
        //卡号栏位提示监听
        cameraIv.setOnClickListener(CardBalanceQueryFragment.this);
        //验证码监听
        checkCodeIv.setOnClickListener(CardBalanceQueryFragment.this);
        //查询余额监听
        querySubmitBtn.setOnClickListener(CardBalanceQueryFragment.this);
        cardNumEt.setOnFocusChangeListener(onFocusChangeListener);
        cardPasswordSipBox.setOnFocusChangeListener(onPassChangeListener);
        cardPasswordSipBox.addTextChangedListener(new TextWatcher() {
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
            cardPasswordDeleteIv.setVisibility(View.VISIBLE);
        }else{
            cardPasswordDeleteIv.setVisibility(View.INVISIBLE);
        }
    }

    View.OnFocusChangeListener onPassChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            passHasFoucs = hasFocus;
            if(hasFocus){
                if(cardPasswordSipBox.getText().toString().length() > 0){
                    //显示删除按钮
                    cardPasswordDeleteIv.setVisibility(View.VISIBLE);
                }else{
                    cardPasswordDeleteIv.setVisibility(View.INVISIBLE);
                }
            }else{
                cardPasswordDeleteIv.setVisibility(View.INVISIBLE);
                cardPasswordSipBox.hideSecurityKeyBoard();
            }
        }
    };

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.cameraIv) {
            isQueryCard = false;
            //卡号栏位照相机打开
            hideSoftInput();
            ScanCardNumFragment fragment = new ScanCardNumFragment();
            fragment.setOnCloseListener(new ScanCardNumFragment.OnCloseListener() {
                @Override
                public void onClose() {
                    isQueryCard = true;
                }
            });
            try{
                startForResult(fragment, REQUEST_CODE_SCAN_CARD);
            }catch (Exception e){
                showErrorDialog(e.getMessage());
            }

        } else if (view.getId() == R.id.checkCodeIv) {
            //验证码
            requestVerificationCode();
        } else if (view.getId() == R.id.querySubmitBtn) {
            //卡号登录
            /**验证卡号、密码、验证码*/
            if (checkCardInputRegex()) {
                showLoadingDialog(false);
                cardBalanceQueryPresenter.queryLogin();
            }
        }
    }

    //扫描的卡号
    private String cardScanNumber;
    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (RESULT_OK ==resultCode&&REQUEST_CODE_SCAN_CARD == requestCode) { // 扫描银行卡
            isQueryCard = true;
            cardScanNumber = data.getString("cardNumber").replace(" ","").trim();
            RegexResult nameResult = RegexUtils.check(mContext,
                    "login_card_number", cardScanNumber, true);
            if (!nameResult.isAvailable) {
                // 弹出校验错误的提示框
                showErrorDialog(nameResult.errorTips);
                return;
            }else{
                cardNumEt.setText(cardScanNumber);
                cardNumEt.clearFocus();
                cardNumEt.setSelection(cardNumEt.getText().toString().length());
                cardBalanceQueryPresenter.queryCardType(cardScanNumber);
                requestVerificationCode();
            }
        }
    }

    @Override
    protected View getTitleBarView() {
        View titleView = LayoutInflater.from(mContext).inflate(R.layout.boc_card_query_title_bar,
                null);
        titleView.findViewById(R.id.leftIconIv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isQueryCard = false;
                hideSoftInput();
                CardBalanceQueryFragment.this.pop();
            }
        });
        titleView.findViewById(R.id.rightIconIv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //卡号栏位提示
                showTipDialog();
            }
        });
        return titleView;
    }

    /**
     * 校验卡查询输入
     *
     * @return
     */
    private boolean checkCardInputRegex() {
        RegexResult nameResult = RegexUtils.check(mContext,
                "login_card_number", cardNumEt.getText().toString().trim(), true);
        if (!nameResult.isAvailable) {
            // 弹出校验错误的提示框
            showErrorDialog(nameResult.errorTips);
            return false;
        }

        if(!isCardQuerySuccess){
            showErrorDialog("请准确输入您的银行卡号");
        }

        RegexResult passwordResult;
        if ("1".equals(cardTypeId)) {
            passwordResult = RegexUtils.check(mContext,
                    "login_credit_card_password", cardPasswordSipBox.getText()
                            .toString().trim(), true);
        } else if("2".equals(cardTypeId)){
            passwordResult = RegexUtils.check(mContext,
                    "login_debit_card_password", cardPasswordSipBox.getText()
                            .toString().trim(), true);
        }else{
            passwordResult = null;
        }

        if ((null!=passwordResult)&&(!passwordResult.isAvailable)) {
            // 弹出校验错误的提示框
            showErrorDialog(passwordResult.errorTips);
            return false;
        }

        if (StringUtils.isEmpty(checkCodeEt.getText().toString())) {
            showErrorDialog("请输入验证码");
            return false;
        }
        if (checkCodeEt.getText().toString().length() != 4) {
            showErrorDialog("验证码无效");
            return false;
        }
        return true;
    }

    View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(isQueryCard){
                String accountNum = cardNumEt.getText().toString();
                if (!hasFocus && accountNum.length() > 0) {
                    showLoadingDialog(false);
                    // 16/8/2 校验
                    RegexResult nameResult = RegexUtils.check(mContext,
                            "login_card_number", cardNumEt.getText().toString().trim(), true);
                    if (!nameResult.isAvailable) {
                        // 弹出校验错误的提示框
                        showErrorDialog(nameResult.errorTips);
                        return;
                    }
                    cardBalanceQueryPresenter.queryCardType(accountNum);
                    requestVerificationCode();
                }
            }
        }
    };

    /**
     * 显示提示框
     */
    private void showTipDialog() {
        showErrorDialog(mContext.getResources().getString(R.string.boc_card_balance_query_tip));
    }

    /**
     * 获取验证码
     */
    private void requestVerificationCode() {
        ImageLoaderUtils.getBIIImageLoader()
                .load(ApplicationConfig.getVerificationCodeUrl())
                .refresh(true)
                .into(checkCodeIv, new LoadListener() {
                    @Override
                    public void onSuccess() {
                        //验证码获取成功回调

                    }

                    @Override
                    public void onError() {
                        //验证码获取失败回调

                    }
                });

    }

    /**
     * 获取随机数回调
     *
     * @param random
     * @return
     */
    @Override
    public CardBalanceLoginViewModel randomPreSuccess(String random) {

        //设置密码控件随机数
        cardPasswordSipBox.setRandomKey_S(random);
        //封装UI数据model

        mCardBalanceLoginViewModel.setLoginName(cardNumEt.getText().toString());

        try {
            //信用卡
            if ("1".equals(cardTypeId)) {
                mCardBalanceLoginViewModel.setPhoneBankPassword(cardPasswordSipBox.getValue().getEncryptPassword());
                mCardBalanceLoginViewModel.setPhoneBankPassword_RC(cardPasswordSipBox.getValue().getEncryptRandomNum());
            } else if ("2".equals(cardTypeId)) {
                //借记卡
                mCardBalanceLoginViewModel.setAtmPassword(cardPasswordSipBox.getValue().getEncryptPassword());
                mCardBalanceLoginViewModel.setAtmPassword_RC(cardPasswordSipBox.getValue().getEncryptRandomNum());
            }

        } catch (CodeException e) {
            e.printStackTrace();
        }
        mCardBalanceLoginViewModel.setValidationChar(checkCodeEt.getText().toString());
        mCardBalanceLoginViewModel.setActiv(cardPasswordSipBox.getVersion() + "");
        mCardBalanceLoginViewModel.setState("");
        return mCardBalanceLoginViewModel;
    }

    /**
     * 获取卡类型回调
     *
     * @param result
     * @return
     */
    @Override
    public void cardTypeSuccess(String result) {
        closeProgressDialog();
        isCardQuerySuccess = true;
        accountType = result;
        mCardBalanceLoginViewModel.setAccountType(accountType);
        if (StringUtils.isEmptyOrNull(accountType)) {
            return;
        }
        cardTypeId = cardType.get(accountType);
        if ("1".equals(cardTypeId)) {
            cardPasswordLl.setVisibility(View.VISIBLE);
            cardPasswordSipBox.setHint("查询密码");
            cardPasswordSipBox.setFocusable(true);
            cardPasswordSipBox.setFocusableInTouchMode(true);
            cardPasswordSipBox.requestFocus();
        } else if ("2".equals(cardTypeId)) {
            cardPasswordLl.setVisibility(View.VISIBLE);
            cardPasswordSipBox.setHint("取款密码");
            cardPasswordSipBox.setFocusable(true);
            cardPasswordSipBox.setFocusableInTouchMode(true);
            cardPasswordSipBox.requestFocus();
        } else {
            cardPasswordLl.setVisibility(View.GONE);
        }
    }

    /**
     * 卡登录信息成功回调
     *
     * @param mViewModel
     */
    @Override
    public void loginInforSuccess(CardBalanceLoginViewModel mViewModel) {
        closeProgressDialog();
        mCardBalanceLoginViewModel = mViewModel;
        String accountSeq = mCardBalanceLoginViewModel.getAccountSeq();
        //调用联龙卡号登录成功接口
        //保存cookie到联龙
        LoginContext.instance.saveCookiesToLianLong();
        //信用卡
        if ("1".equals(cardTypeId)) {
            LoginContext.instance.gotoBocnetLoginModule(getActivity(),
                    MapUtils.clzzField2Map(mViewModel),
                    accountSeq,false);
        }
        //借记卡
        else if ("2".equals(cardTypeId)) {
            LoginContext.instance.gotoBocnetLoginModule(getActivity(),
                    MapUtils.clzzField2Map(mViewModel),
                    accountSeq,true);
        }

        isFinishActivity = true;


//        //信用卡
//        if ("1".equals(cardTypeId)) {
//            cardBalanceQueryPresenter.queryCreditCardInfor(mCardBalanceLoginViewModel.getAccountSeq());
//        }
//        //借记卡
//        else if ("2".equals(cardTypeId)) {
//            cardBalanceQueryPresenter.queryDebitCardInfor(mCardBalanceLoginViewModel.getAccountSeq());
//        }

    }

    @Override
    public void loginInforfail() {
        checkCodeEt.getText().clear();
        //验证码
        requestVerificationCode();
    }

    /**
     * 信用卡号信息成功回调
     *
     * @param creditCardDetailViewModel
     */
    @Override
    public void creditCardInforSuccess(CreditCardDetailViewModel creditCardDetailViewModel) {
        closeProgressDialog();
        // lianlong 存储 跳转到lianlong 已不需要 联龙自己请求
        Toast.makeText(mContext, "信用卡信息获取成功", Toast.LENGTH_SHORT).show();
    }

    /**
     * 借记卡号信息成功回调
     *
     * @param debitCardDetailViewModel
     */
    @Override
    public void debitCardInforSuccess(DebitCardDetailViewModel debitCardDetailViewModel) {
        closeProgressDialog();
        // lianlong 存储 跳转到lianlong 已不需要 联龙自己请求
        Toast.makeText(mContext, "借记卡信息获取成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void beforeKeyboardShow(SipBox sipBox, int i) {

    }

    @Override
    public void afterKeyboardHidden(SipBox sipBox, int i) {

    }

    @Override
    public void afterClickDown(SipBox sipBox) {

    }


    @Override
    public void setPresenter(CardBalanceQueryContract.Presenter presenter) {

    }

    @Override
    public void onStop() {
        super.onStop();
        if(isFinishActivity){
            ActivityManager.getAppManager().finishActivity(LoginBaseActivity.class);
        }
    }

    @Override
    public boolean onBackPress() {
        isQueryCard = false;
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isQueryCard = true;
    }
}

