package com.boc.bocsoft.mobile.bocmobile.base.widget.edittext;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
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
 * 输入框组件 - 金额键盘 - 注意：此金额键盘为郭凯使用，其他功能请使用EditMoneyInputWidget
 * Created by wangfan on 2016/5/24.
 */
public class EditMoneyInputDialogView extends LinearLayout
        implements TextWatcher, View.OnClickListener {

    /**
     * 输入框组件的EditText
     */
    private MoneyInputTextView mContentMoneyEditText;
    /**
     * 删除图标
     */
    private TextView editMoneyDelImage;


    /**
     * 属性中定义的item的内容
     */
    private String strChoiceContent;

    private KeyBoardDismissOrShowCallBack dismissOrShowCallBack;


    public EditMoneyInputDialogView(Context context) {
        this(context, null);
    }

    public EditMoneyInputDialogView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditMoneyInputDialogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public void setScrollView(View view, Dialog dialog) {
        mContentMoneyEditText.setScrollView(view,dialog);
    }
    /**
     * 初始化页面控件
     */
    private void initView(Context context, AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.editChoiceWidget);
        strChoiceContent = a.getString(R.styleable.editChoiceWidget_editChoiceContent);
        int left = a.getInt(R.styleable.editChoiceWidget_moneyInputMaxLeftNum, 13);
        int right = a.getInt(R.styleable.editChoiceWidget_moneyInputMaxRightNum, 2);
        a.recycle();

        View contentView =
                View.inflate(getContext(), R.layout.boc_clear_edit_money_input_dialog_item, this);
        mContentMoneyEditText = (MoneyInputTextView) contentView.findViewById(R.id.clear_edit_money_context);
        editMoneyDelImage = (TextView) contentView.findViewById(R.id.clear_edit_money_del_image);

        Drawable mClearDrawable = getResources().getDrawable(R.drawable.boc_btn_delete);
        mClearDrawable.setBounds(0, 0,
                mClearDrawable.getIntrinsicWidth() + 10,
                mClearDrawable.getIntrinsicHeight() + 10);
        editMoneyDelImage.setCompoundDrawables(mClearDrawable,
                editMoneyDelImage.getCompoundDrawables()[1],
                editMoneyDelImage.getCompoundDrawables()[2],
                editMoneyDelImage.getCompoundDrawables()[3]);

        mContentMoneyEditText.addTextChangedListener(this);
        if (!isInEditMode()) {
            mContentMoneyEditText.setMaxLeftNumber(left);
            mContentMoneyEditText.setMaxRightNumber(right);
        }
        editMoneyDelImage.setOnClickListener(this);

        initViewData();
    }

    /**
     * 初始化页面数据，数据从属性中来
     */
    private void initViewData() {
        mContentMoneyEditText.setText(strChoiceContent);
        if (isInEditMode())
            return;
        mContentMoneyEditText.setOnKeyBoardDismiss(new MoneyInputDialog.KeyBoardListener() {
            @Override
            public void onKeyBoardDismiss() {
                if (mContentMoneyEditText.getInputMoney().length() > 0) {
                    setClearIconVisible(true);
                } else {
                    setClearIconVisible(false);
                }
                if (dismissOrShowCallBack != null){
                    dismissOrShowCallBack.onKeyBoardDismiss();
                }
            }

            @Override
            public void onKeyBoardShow() {
                setClearIconVisible(false);
                if (dismissOrShowCallBack != null){
                    dismissOrShowCallBack.onKeyBoardShow();
                }
            }
        });
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
     * 设置金额输入组件的金额输入框的边距
     * @param haveMarginLeft
     */
    public void setMoneyEditTextHaveMarginLeft(boolean haveMarginLeft){
        if (!haveMarginLeft){
            mContentMoneyEditText.setPadding(0, 0 ,0 ,0);
        }
    }

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
     * @param currency
     */
    public void setCurrency(String currency) {
        mContentMoneyEditText.setCurrency(currency);
    }


    public void setScrollView(View scrollView) {
        mContentMoneyEditText.setScrollView(scrollView);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() == 0) {
            setClearIconVisible(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        mContentMoneyEditText.clearText();
    }

    /**
     * 设置键盘消失与显示的监听
     */
    public void setOnKeyBoardListener(KeyBoardDismissOrShowCallBack listener) {
        dismissOrShowCallBack = listener;
    }

    //键盘消失与显示的监听
    public interface KeyBoardDismissOrShowCallBack {
        void onKeyBoardDismiss();

        void onKeyBoardShow();
    }
}
