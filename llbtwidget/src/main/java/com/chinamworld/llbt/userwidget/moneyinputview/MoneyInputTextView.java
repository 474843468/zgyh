package com.chinamworld.llbt.userwidget.moneyinputview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.chinamworld.llbtwidget.R;


/**
 * 金额输入TextView
 */
public class MoneyInputTextView extends TextView implements View.OnClickListener, MoneyInputKeyBoardView.OnKeyBoardChangedListener {

    private MoneyInputDialog moneyInputDialog;

    public MoneyInputTextView(Context context) {
        this(context, null);
    }

    public MoneyInputTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public MoneyInputTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        if (isInEditMode())
            return;
        moneyInputDialog = new MoneyInputDialog(context);
        setOnClickListener(this);
        moneyInputDialog.setOnKeyBoardChangedListener(this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.moneyInputTextView);
        int left = a.getInt(R.styleable.moneyInputTextView_maxLeftNum, 13);
        int right = a.getInt(R.styleable.moneyInputTextView_maxRightNum, 2);

        moneyInputDialog.setMaxLeftNumber(left);
        moneyInputDialog.setMaxRightNumber(right);

        a.recycle();

        setFocusable(true);
    }

    /**
     * 获取输入的金额
     */
    public String getInputMoney() {
        return moneyInputDialog.getInputMoney();
    }

    public void setInputMoney(String money) {
        moneyInputDialog.setInputMoney(money);
    }

    public void setMaxLeftNumber(int left) {
        moneyInputDialog.setMaxLeftNumber(left);
    }

    public void setMaxRightNumber(int right) {
        moneyInputDialog.setMaxRightNumber(right);
    }

    /**
     * 清除所有金额内容
     */
    public void clearText() {
        moneyInputDialog.clear();
    }

    /**
     * 设置键盘消失监听
     */
    public void setOnKeyBoardDismiss(MoneyInputDialog.KeyBoardDismiss listener) {
        moneyInputDialog.setOnKeyBoardDismiss(listener);
    }

    @Override
    public void onClick(View v) {
        requestFocus();
        moneyInputDialog.showDialog();
    }

    @Override
    public void onNumberChanged(String number) {
        setText(number);
    }

    @Override
    public void onKeyBoardCanceled(String formatNumber) {
        setText(formatNumber);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        moneyInputDialog.cancel();
    }
}
