package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * Created by wangtong on 2016/6/16.
 */
public class SmsVerifyView extends CheckBox {

    //短信倒计时一分钟
    private static final int TOTAL_TIME = 60 * 1000;
    //短信倒计时一分钟
    private static final int INTERVAL = 1000;
    //短信发送和接受
    private SmsActionListener smsActionListener;

    public interface SmsActionListener {
        //发送短信
        public void sendSms();

        //收到短信验证码
        public void onSmsReceived(String code);
    }

    public SmsVerifyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        //obtainSmsVerifyCode = new ObtainSmsVerifyCode(getContext());
        //obtainSmsVerifyCode.setOnSmsCodeReceivedListener(this);

        setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    startCountDown();
                }
            }
        });
    }

    //这只短信收发监听
    public void setOnSmsActionListener(SmsActionListener lis) {
        smsActionListener = lis;
    }

    /**
     * 短信倒计时
     */
    private CountDownTimer countDown = new CountDownTimer(TOTAL_TIME, INTERVAL) {
        @Override
        public void onTick(long millisUntilFinished) {
            setText(getContext().getString(R.string.security_count_down, millisUntilFinished / INTERVAL));
        }

        @Override
        public void onFinish() {
            stopCountDown();
        }
    };

    /**
     * 开始短信倒计时
     */
    public void startCountDown() {
        setEnabled(false);
        setTextColor(getContext().getResources().getColor(R.color.boc_text_mobile_color));
        setText(getContext().getString(R.string.security_count_down, TOTAL_TIME / INTERVAL));
        countDown.start();

        if (smsActionListener != null) {
            smsActionListener.sendSms();
        }
    }

    protected String getButtonText() {
        return getContext().getString(R.string.security_sms_resend);
    }

    public void setButtonText() {
        setText(getContext().getString(R.string.security_obtain_code));
    }

    /**
     * 停止短信倒计时
     */
    public void stopCountDown() {
        setEnabled(true);
        setChecked(true);
        setTextColor(getContext().getResources().getColor(R.color.boc_main_button_color));
        setText(getButtonText());
        countDown.cancel();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopCountDown();
    }
}
