package com.boc.bocsoft.mobile.bocmobile.base.widget.moneyinputview;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * 　金额键盘对话框
 */
public class MoneyInputDialog extends Dialog {

    public MoneyInputKeyBoardView keyBoardView;

    private KeyBoardListener keyBoardListener;

    private boolean tripZeroWhenInput = true;

    //键盘消失监听
    public interface KeyBoardListener {
        public void onKeyBoardDismiss();

        public void onKeyBoardShow();
    }

    public MoneyInputDialog(Context context) {
        super(context, R.style.dialog_no_dim);
        keyBoardView = new MoneyInputKeyBoardView(context);
        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                keyBoardView.setTripZero(false);
                String formatNumber = keyBoardView.getFormatNumber();
                keyBoardView.getOnKeyBoardChangedListener().onKeyBoardCanceled(formatNumber);
                if (keyBoardListener != null) {
                    keyBoardListener.onKeyBoardDismiss();
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

    public void setOnKeyBoardDismiss(KeyBoardListener listener) {
        keyBoardListener = listener;
    }

    public String getInputMoney() {
        return keyBoardView.getInputMoney();
    }

    public void setInputMoney(String money) {
        keyBoardView.setInputMoney(money);
    }

    public void setOnKeyBoardChangedListener(
            MoneyInputKeyBoardView.OnKeyBoardChangedListener listener) {
        keyBoardView.setOnKeyBoardChangedListener(listener);
    }

    public void showDialog() {
        show();
        keyBoardView.setTripZero(tripZeroWhenInput);
        if (keyBoardListener != null) {
            keyBoardListener.onKeyBoardShow();
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

    /**
     * 　获取对话框高度
     */
    public int getDialogHeight() {
        float dpi = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpi * 261);
    }

    public void setTripZeroWhenInput(boolean tripZeroWhenInput) {
        this.tripZeroWhenInput = tripZeroWhenInput;
    }
}
