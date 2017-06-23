package com.boc.bocsoft.mobile.bocmobile.base.widget.edittext;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * 带有删除功能的EditText
 * Created by wangfan on 2016/5/21.
 */
public class ClearEditText extends EditText implements OnFocusChangeListener, TextWatcher {

    /**
     * EditText右侧的删除按钮
     */
    private Drawable mClearDrawable;
    /**
     * 是否获取焦点
     */
    private boolean hasFoucs;

//    private int maxNumber;//最大字符数
    private ClearEditTextFocusCallBack focusCallBack;
    private ClearEditTextWatcher clearEditTextWatcher;

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    /**
     * 初始化页面
     */
    private void init() {
//        setMaxEms(maxNumber);
        // 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片,获取图片的顺序是左上右下（0,1,2,3,）
        mClearDrawable = getCompoundDrawables()[2];
        //设置EditText的DrawableRight的边距，防止输入大量文字导致文字与图片之间没有间距
        setCompoundDrawablePadding(10);
        if (mClearDrawable == null) {
            mClearDrawable = getResources().getDrawable(R.drawable.boc_btn_delete);
        }

        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth() + 10 ,
                mClearDrawable.getIntrinsicHeight() + 10);
        // 默认设置隐藏图标
        setClearIconVisible(false);
        // 设置焦点改变的监听
        setOnFocusChangeListener(this);
        // 设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
    }

//    public void setMaxNumber(int maxNumber) {
//        this.maxNumber = maxNumber;
//    }

    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们记住按下的位置来模拟点击事件
     * 按下的位置 在 EditText的宽度 - 图标到控件右边的间距 - 图标的宽度 和 EditText的宽度 - 图标到控件右边的间距 之间我们就算点击了图标
     * 说明：isInnerWidth, isInnerHeight为ture，触摸点在删除图标之内，则视为点击了删除图标
     * event.getX()获取相对应自身左上角的X坐标
     * event.getY() 获取相对应自身左上角的Y坐标
     * getWidth() 获取控件的宽度
     * getHeight()获取控件的高度
     * getTotalPaddingRight() 获取删除图标左边缘到控件右边缘的距离
     * getPaddingRight()获取删除图标右边缘到控件右边缘的距离
     * isInnerWidth: getWidth() - getTotalPaddingRight()计算删除图标左边缘到控件左边缘的距离
     * getWidth() - getPaddingRight() 计算删除图标右边缘到控件左边缘的距离
     * isInnerHeight: distance 删除图标顶部边缘到控件顶部边缘的距离
     * distance + height 删除图标底部边缘到控件顶部边缘的距离
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                Rect rect = getCompoundDrawables()[2].getBounds();
                int height = rect.height();
                int distance = (getHeight() - height) / 2;
                boolean isInnerWidth = x > (getWidth() - getTotalPaddingRight())
                        && x < (getWidth() - getPaddingRight());
                boolean isInnerHeight = y > distance && y < (distance + height);
                if (isInnerWidth && isInnerHeight) {
                    this.setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 当ClearEditText焦点发生变化的时候， 输入长度为零，隐藏删除图标，否则，显示删除图标
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFoucs = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }

        if (focusCallBack != null){
            focusCallBack.onFocusChange(v, hasFocus);
        }
    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     */
    public void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before,
                              int count) {
        if (hasFoucs) {
            setClearIconVisible(s.length() > 0);
        }
        if (clearEditTextWatcher != null){
            clearEditTextWatcher.onTextChanged(s, start, before, count);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        if (clearEditTextWatcher != null){
            clearEditTextWatcher.beforeTextChanged(s, start, count, after);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (clearEditTextWatcher != null){
            clearEditTextWatcher.afterTextChanged(s);
        }
    }


    /**
     * 设置焦点的回调
     * @param listener
     */
    public void setClearEditTextFocusListener(ClearEditTextFocusCallBack listener){
        this.focusCallBack = listener;
    }


    /**
     * 设置TextWatcher的回调
     * @param watcherListener
     */
    public void setClearEditTextWatcherListener(ClearEditTextWatcher watcherListener){
        this.clearEditTextWatcher = watcherListener;
    }


    public interface ClearEditTextFocusCallBack{
        void onFocusChange(View v, boolean hasFocus);
    }


    public interface ClearEditTextWatcher{
        void onTextChanged(CharSequence s, int start, int before, int count);
        void beforeTextChanged(CharSequence s, int start, int count,int after);
        void afterTextChanged(Editable s);
    }

}
