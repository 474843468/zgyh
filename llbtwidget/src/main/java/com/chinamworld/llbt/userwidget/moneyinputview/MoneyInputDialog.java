package com.chinamworld.llbt.userwidget.moneyinputview;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;

import com.chinamworld.llbtwidget.R;


/**
 * 　金额键盘对话框
 */
public class MoneyInputDialog extends Dialog {

    private MoneyInputKeyBoardView keyBoardView;

    private KeyBoardDismiss keyBoardDismiss;

    //键盘消失监听
    public interface KeyBoardDismiss {
        public void onKeyBoardDismiss();

        public void onKeyBoardShow();
    }

    public MoneyInputDialog(Context context) {
        super(context, R.style.dialog_no_dim);
        keyBoardView = new MoneyInputKeyBoardView(context);
        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                String formatNumber = keyBoardView.getFormatNumber();
                keyBoardView.getOnKeyBoardChangedListener().onKeyBoardCanceled(formatNumber);
                if (keyBoardDismiss != null) {
                    keyBoardDismiss.onKeyBoardDismiss();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(keyBoardView.getKeyBoardView());
        setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.BOTTOM;
    }

    public void setOnKeyBoardDismiss(KeyBoardDismiss listener) {
        keyBoardDismiss = listener;
    }

    public String getInputMoney() {
        return keyBoardView.getInputMoney();
    }

    public void setInputMoney(String money) {
        keyBoardView.setInputMoney(money);
    }

    public void setOnKeyBoardChangedListener(MoneyInputKeyBoardView.OnKeyBoardChangedListener listener) {
        keyBoardView.setOnKeyBoardChangedListener(listener);
    }

    public void showDialog() {
        show();
        if (keyBoardDismiss != null) {
            keyBoardDismiss.onKeyBoardShow();
        }
    }

    public void clear() {
        keyBoardView.clear();
    }

    /**
     * 　设置左边金额最大位数
     */
    public void setMaxLeftNumber(int maxLeftNumber) {
        keyBoardView.setMaxLeftNumber(maxLeftNumber);
    }

    /**
     * 　设置右边边金额最大位数
     */
    public void setMaxRightNumber(int maxRightNumber) {
        keyBoardView.setMaxRightNumber(maxRightNumber);
    }
}
