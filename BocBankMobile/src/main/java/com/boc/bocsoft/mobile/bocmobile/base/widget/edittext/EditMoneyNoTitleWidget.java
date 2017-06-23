package com.boc.bocsoft.mobile.bocmobile.base.widget.edittext;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.moneyinputview.MoneyInputDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.moneyinputview.MoneyInputTextView;

/**
 * 输入框组件 - 金额键盘 - 无左边标题、无右边TextView
 * Created by wangfan on 2016-11-7 17:02:08
 */
public class EditMoneyNoTitleWidget extends LinearLayout
        implements TextWatcher, View.OnClickListener {

    /***
     * 输入框组件的EditText
     */
    private MoneyInputTextView mContentMoneyEditText;
    /***
     * 删除图标
     */
    private TextView editMoneyDelImage;

    private EditWidgetListener mEditWidgetListener;
    private LinearLayout ll_container;

    public EditMoneyNoTitleWidget(Context context) {
        this(context, null);
    }

    public EditMoneyNoTitleWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditMoneyNoTitleWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    /**
     * 初始化页面控件
     */
    protected void initView(Context context, AttributeSet attrs) {

        View contentView =
                View.inflate(getContext(), R.layout.boc_clear_edit_money_no_title_item, this);
        mContentMoneyEditText = (MoneyInputTextView) contentView.findViewById(
                R.id.clear_edit_money_no_title_context);
        editMoneyDelImage =
                (TextView) contentView.findViewById(R.id.clear_edit_money_no_title_del_image);
        ll_container = (LinearLayout) contentView.findViewById(R.id.ll_container);

        Drawable mClearDrawable = getResources().getDrawable(R.drawable.boc_btn_delete);
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth() + 10,
                mClearDrawable.getIntrinsicHeight() + 10);
        editMoneyDelImage.setCompoundDrawables(mClearDrawable,
                editMoneyDelImage.getCompoundDrawables()[1],
                editMoneyDelImage.getCompoundDrawables()[2],
                editMoneyDelImage.getCompoundDrawables()[3]);

        mContentMoneyEditText.addTextChangedListener(this);
        editMoneyDelImage.setOnClickListener(this);

        initViewData();
    }

    /**
     * 初始化页面数据，数据从属性中来
     */
    private void initViewData() {
        mContentMoneyEditText.setHint(getResources().getString(R.string.boc_common_input_hint));
        if (isInEditMode())
            return;
        mContentMoneyEditText.setOnKeyBoardDismiss(new MoneyInputDialog.KeyBoardListener() {
            @Override
            public void onKeyBoardDismiss() {
                if (dismissOrShowCallBack != null) {
                    dismissOrShowCallBack.onKeyBoardDismiss();
                }
                if (mContentMoneyEditText.getInputMoney().length() > 0) {
                    setClearIconVisible(true);
                } else {
                    setClearIconVisible(false);
                }
            }

            @Override
            public void onKeyBoardShow() {
                setClearIconVisible(false);
                if (dismissOrShowCallBack != null) {
                    dismissOrShowCallBack.onKeyBoardShow();
                }
            }
        });
    }


    // -------------------- 输入框 可输入内容View  使用方法 开始 -------------------

    /**
     * 设置金额输入框组件的文字颜色
     */
    public void setContentMoneyEditTextColor(int colorId) {
        mContentMoneyEditText.setTextColor(colorId);
    }

    /**
     * 获取金额输入框组件内容的EditText
     */
    public MoneyInputTextView getContentMoneyEditText() {
        return mContentMoneyEditText;
    }

    /**
     * 获取输入的金额
     */
    public String getContentMoney() {
        return mContentMoneyEditText.getInputMoney();
    }

    /**
     * 设置输入框的初始数据
     */
    public void setmContentMoneyEditText(String money) {
        mContentMoneyEditText.setInputMoney(money);
    }

    /**
     * 设置输入框的提示信息
     */
    public void setContentHint(String str) {
        if (null == str || str.isEmpty()) {
            return;
        }
        mContentMoneyEditText.setHint(str);
    }

    /**
     * 　设置左边金额最大位数
     */
    public void setMaxLeftNumber(int maxLeftNumber) {
        mContentMoneyEditText.setMaxLeftNumber(maxLeftNumber);
    }

    /**
     * 　设置右边边金额最大位数
     */
    public void setMaxRightNumber(int maxRightNumber) {
        mContentMoneyEditText.setMaxRightNumber(maxRightNumber);
    }

    /**
     * 设置币种
     */
    public void setCurrency(String currency) {
        mContentMoneyEditText.setCurrency(currency);
    }

    // ----------------------- 输入框 可输入内容View  使用方法 结束 ----------------------

    // --------------------------------- 其他功能 --------------------------------

    /**
     * 设置清除图标的显示与隐藏
     */
    public void setClearIconVisible(boolean visible) {
        if (visible) {
            editMoneyDelImage.setVisibility(View.VISIBLE);
        } else {
            editMoneyDelImage.setVisibility(View.GONE);
        }
    }

    public void setScrollView(View scrollView) {
        mContentMoneyEditText.setScrollView(scrollView);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (moneyInputTextWatcher != null) {
            moneyInputTextWatcher.beforeTextChanged(s, start, count, after);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (moneyInputTextWatcher != null) {
            moneyInputTextWatcher.onTextChanged(s, start, before, count);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (moneyInputTextWatcher != null) {
            moneyInputTextWatcher.afterTextChanged(s);
        }
    }

    @Override
    public void onClick(View v) {
        mContentMoneyEditText.clearText();
        if (mEditWidgetListener != null) {
            mEditWidgetListener.onClear();
        }
    }

    private KeyBoardDismissOrShowCallBack dismissOrShowCallBack;

    /**
     * 设置键盘消失与显示的监听
     */
    public void setOnKeyBoardListener(KeyBoardDismissOrShowCallBack listener) {
        dismissOrShowCallBack = listener;
    }

    public interface KeyBoardDismissOrShowCallBack {
        void onKeyBoardDismiss();

        void onKeyBoardShow();
    }

    private MoneyInputTextWatcher moneyInputTextWatcher;

    public interface MoneyInputTextWatcher {
        void beforeTextChanged(CharSequence s, int start, int count, int after);

        void onTextChanged(CharSequence s, int start, int before, int count);

        void afterTextChanged(Editable s);
    }

    public void setMoneyInputTextWatcherListener(MoneyInputTextWatcher moneyInputTextWatcher) {
        this.moneyInputTextWatcher = moneyInputTextWatcher;
    }

    public interface EditWidgetListener {
        void onClear();
    }

    public void setEditWidgetListener(EditWidgetListener editWidgetListener) {
        mEditWidgetListener = editWidgetListener;
    }

    public void setBackgrouds(Drawable drawable) {
        ll_container.setBackgroundDrawable(drawable);
    }

    public void setBackgroudRes(int resId) {
        ll_container.setBackgroundResource(resId);
    }

    public void setHeight(int height) {
        mContentMoneyEditText.getLayoutParams().height = height;
    }

    public void setETPadding(int left, int top, int right, int bottom) {
        ll_container.setPadding(left, top, right, bottom);
    }

}
