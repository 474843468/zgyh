package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.widget;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.CFCAEditTextView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerifyDialog;

/**
 * 输入支付密码对话框
 * Created by wangf on 2016/9/6.
 */
public class QRPayPwdDialog extends SecurityVerifyDialog {

//    private Context mContext;
    private Activity activity;

    private ImageView btnQrPayDialogCancel;
    private TextView tvQrPayDialogName;
    private TextView tvQrPayDialogAmount;
    private CFCAEditTextView cfcaEditTextView;

    private QRPayPwdDialogCallBack dialogCallBack;

    public QRPayPwdDialog(Activity activity) {
        super(activity);
        this.activity = activity;
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected View onAddContentView() {
    	rootView = inflateView(R.layout.boc_dialog_qrpay_pay_pwd);
        return rootView;
    }

    @Override
    protected void initView() {
        btnQrPayDialogCancel = (ImageView) rootView.findViewById(R.id.btn_qrpay_dialog_cancel);
        tvQrPayDialogName = (TextView) rootView.findViewById(R.id.btn_qrpay_dialog_name);
        tvQrPayDialogAmount = (TextView) rootView.findViewById(R.id.btn_qrpay_dialog_amount);
        cfcaEditTextView = (CFCAEditTextView) rootView.findViewById(R.id.btn_qrpay_dialog_cfc);
        cfcaEditTextView.setOutputValueType(1);
        cfcaEditTextView.setSipBoxIsInDialogNeedReLocation(true);
        cfcaEditTextView.setSecurityKeyboardListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        //关闭对话框的点击事件
        btnQrPayDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogCallBack != null){
                    dialogCallBack.onCancel();
                    cancel();
                }
            }
        });
        //密码输入框的监听
        cfcaEditTextView.setSecurityEditCompleListener(new CFCAEditTextView.SecurityEditCompleteListener() {
            @Override
            public void onNumCompleted(String random, String num, String mVersion) {
                if (dialogCallBack != null){
                    dialogCallBack.onNumCompleted(random, num, mVersion);
                    cancel();
                }
            }

            @Override
            public void onErrorMessage(boolean isShow) {
                if (dialogCallBack != null){
                    cfcaEditTextView.hiddenCfcaKeybard();
                    dialogCallBack.onErrorMessage(isShow);
                }
            }

			@Override
			public void onCompleteClicked(final String inputString) {
				activity.runOnUiThread(new Runnable() {
		            @Override
		            public void run() {
		                if (TextUtils.isEmpty(inputString)) {
		                    Toast.makeText(activity, "支付密码不能为空", Toast.LENGTH_SHORT).show();
		                } else if (inputString.length() < 6) {
		                    Toast.makeText(activity, "支付密码：6位数字", Toast.LENGTH_SHORT).show();
		                }
		            }
		        });
				
				
				if (dialogCallBack != null){
                    cfcaEditTextView.hiddenCfcaKeybard();
                    dialogCallBack.onCompleteClicked(inputString);
                }
            }
        });
    }


    /**
     * 设置对话框数据
     * @param name
     * @param amount
     * @param random
     */
    public void setDialogData(String name, String amount, String random){
        tvQrPayDialogName.setText(name);
        tvQrPayDialogAmount.setText(MoneyUtils.transMoneyFormat(amount, ApplicationConst.CURRENCY_CNY));
        cfcaEditTextView.setCFCARandom(random);
    }


    /**
     * 设置对话框的回调
     * @param dialogListener
     */
    public void setQRPayDialogListener(QRPayPwdDialogCallBack dialogListener){
        this.dialogCallBack = dialogListener;
    }

    public interface QRPayPwdDialogCallBack{
        void onCancel();
        void onNumCompleted(String encryptRandomNum, String encryptPassword, String mVersion);
        void onErrorMessage(boolean isShow);
        void onCompleteClicked(String inputString);
    }

}
