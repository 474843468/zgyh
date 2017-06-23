package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify;

/**
 * @Description: 监听短信验证码
 * @author: wangtong
 * @time: 2016/6/1
 */
public class ObtainSmsVerifyCode {

   /* private Uri SMS_INBOX = Uri.parse("content://sms/");
    private Context context = null;
    private Handler handler = new Handler();
    private SmsObserver smsObserver;
    private SmsCodeReceivedListener listener = null;
    private String receivePhone;

    *//**
     * @Description: 获取到短信验证码监听
     *//*
    public interface SmsCodeReceivedListener {
        public void onSmsCodeReceived(String code);
    }

    public ObtainSmsVerifyCode(Context context) {
        this.context = context;
        ContentResolver cr = context.getContentResolver();
        smsObserver = new SmsObserver(handler);
        cr.registerContentObserver(SMS_INBOX, true, smsObserver);
    }

    public void setOnSmsCodeReceivedListener(SmsCodeReceivedListener lis) {
        listener = lis;
    }

    public void unRegisterContentObserver() {
        ContentResolver cr = context.getContentResolver();
        cr.unregisterContentObserver(smsObserver);
    }

    public void setReceivePhone(String receivePhone) {
        this.receivePhone = receivePhone;
    }

    *//**
     * @Description: 获取验证码
     *//*
    private void getVerifyCodeFromSms() {

        String res = "";
        ContentResolver cr = context.getContentResolver();
        String[] projection = new String[]{"body"};
        String where = "address = ? and date > ?";
        String[] value = new String[]{receivePhone, (System.currentTimeMillis() - 60 * 1000) + ""};
        Cursor cursor = cr.query(SMS_INBOX, projection, where, value, "date desc");

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String number = cursor.getString(cursor.getColumnIndex("address"));//手机号
                String name = cursor.getString(cursor.getColumnIndex("person"));//姓名
                String body = cursor.getString(cursor.getColumnIndex("body"));//内容
                res = matchVerityCode(body);
            }
            cursor.close();
        }

        if (listener != null) {
            listener.onSmsCodeReceived(res);
        }
    }

    private String matchVerityCode(String body) {
        String verityCode = "";
        Pattern pattern = Pattern.compile("[0-9]{6}");
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            verityCode = matcher.group();
        }
        Log.d("matchVerityCode", "verityCode = " + verityCode);
        return verityCode;
    }

    *//**
     * @Description: 监听短信数据
     *//*
    class SmsObserver extends ContentObserver {

        public SmsObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            getVerifyCodeFromSms();
        }
    }*/
}
