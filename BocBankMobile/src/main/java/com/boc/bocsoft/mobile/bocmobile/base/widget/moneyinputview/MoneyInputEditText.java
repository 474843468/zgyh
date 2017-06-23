package com.boc.bocsoft.mobile.bocmobile.base.widget.moneyinputview;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Scroller;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.framework.utils.ResUtils;

/**
 * 金额输入TextView
 */
public class MoneyInputEditText extends EditText
        implements View.OnClickListener, MoneyInputKeyBoardView.OnKeyBoardChangedListener,
        View.OnFocusChangeListener {

    private MoneyInputDialog moneyInputDialog;

    private View scrollView;

    private Scroller scroller;

    private int scrollDy;

    private String currency;

    private Dialog dialog;

    public MoneyInputEditText(Context context) {
        this(context, null);
    }

    public MoneyInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
        scroller = new Scroller(context);
    }

    public MoneyInputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }
        ResUtils.hideSoftInputMethod(this);
        ResUtils.hideUnderline(this);
        moneyInputDialog = new MoneyInputDialog(context);
        setOnClickListener(this);
        //setOnTouchListener(this);
        setOnFocusChangeListener(this);
        moneyInputDialog.setCanceledOnTouchOutside(false);
        moneyInputDialog.setOnKeyBoardChangedListener(this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.moneyInputTextView);
        int left = a.getInt(R.styleable.moneyInputTextView_maxLeftNum, 13);
        int right = a.getInt(R.styleable.moneyInputTextView_maxRightNum, 2);
        currency = a.getString(R.styleable.moneyInputTextView_currency);

        moneyInputDialog.setMaxLeftNumber(left);
        moneyInputDialog.setMaxRightNumber(right);
        moneyInputDialog.keyBoardView.setCurrency(currency);

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
     * 设置根布局
     */
    public void setScrollView(View view) {
        scrollView = view;
    }

    public void setScrollView(View view, Dialog dialog) {
        scrollView = view;
        this.dialog = dialog;
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
    public void setOnKeyBoardDismiss(MoneyInputDialog.KeyBoardListener listener) {
        moneyInputDialog.setOnKeyBoardDismiss(listener);
    }

    @Override
    public void onClick(View v) {
        requestFocus();
        moneyInputDialog.showDialog();
        setText(getInputMoney());
        setSelection(getInputMoney().length());
        if (isDialog()) {
            int keyBoardDialogHeight = moneyInputDialog.getDialogHeight();
            dialogScrollUp(getScrollerY(keyBoardDialogHeight));
        } else {
            scrollUp();
        }
    }

    private boolean isDialog() {
        return dialog instanceof Dialog;
    }

    @Override
    public void onNumberChanged(String number) {
        setText(number);
        setSelection(number.length());
    }

    @Override
    public void onKeyBoardCanceled(String formatNumber) {
        setText(formatNumber);
        setSelection(formatNumber.length());
        if (isDialog()) {
            dialogScrollDown();
        } else {
            scrollDown();
        }
    }

    //    @Override
    //    protected void onDetachedFromWindow() {
    //        super.onDetachedFromWindow();
    //        moneyInputDialog.cancel();
    //    }

    private void scrollUp() {
        if (scrollView != null) {
            int[] location = new int[2];
            int dialogHeight, inputViewHeight, screenHeight;

            getLocationOnScreen(location);
            dialogHeight = moneyInputDialog.getDialogHeight();
            inputViewHeight = location[1] + getHeight();
            screenHeight = getResources().getDisplayMetrics().heightPixels;

            scrollDy = (inputViewHeight + dialogHeight + 20) - screenHeight;
            if (scrollDy > 0) {
                scroller.startScroll(0, 0, 0, scrollDy, 500);
                postInvalidate();
            }
        }
    }

    private void scrollDown() {
        if (scrollView != null) {
            scroller.startScroll(0, scrollDy, 0, -scrollDy, 500);
            postInvalidate();
        }
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            int x = scroller.getCurrX();
            int y = scroller.getCurrY();
            scrollView.scrollTo(x, y);
            postInvalidate();
        }
        super.computeScroll();
    }

    public void setCurrency(String currency) {
        moneyInputDialog.keyBoardView.setCurrency(currency);
        this.currency = currency;
    }

    private int getScrollerY(int keyBoardHeight) {
        int scrollDy;
        int inputViewHeight, screenHeight;
        int[] location = new int[2];
        scrollView.getLocationOnScreen(location);

        inputViewHeight = location[1] + scrollView.getHeight();
        screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        scrollDy = (inputViewHeight + keyBoardHeight) - screenHeight;
        return scrollDy;
    }

    public void dialogScrollUp(int scrollerY) {
        if (scrollerY > 0) {
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.y = 0 - scrollerY;
            dialog.getWindow().setAttributes(params);
        }
    }

    public void dialogScrollDown() {
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.y = 0;
        dialog.getWindow().setAttributes(params);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            int i = 0;
        } else {
            int j = 0;
            //moneyInputDialog.cancel();
        }
    }
}
