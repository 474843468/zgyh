package com.boc.bocsoft.mobile.bocmobile.base.widget.edittext;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * 限定字符个数的EditText，其中中文按两个字符算
 * Created by liuzc on 2016/8/15.
 */
public class LimitLengthEditText extends EditText {
    private Context mContext = null;

    private int maxLength = -1; //最多字符个数，默认为-1

    public LimitLengthEditText(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public LimitLengthEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public LimitLengthEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    /**
     * 设定最大输入字符长度
     * @param value
     */
    public void setInputMaxLength(int value){
        maxLength = value;
    }

    private void init(){
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(maxLength < 1){
                    return;
                }
                int mTextMaxlength = 0;
                Editable editable = LimitLengthEditText.this.getText();
                String str = editable.toString();
                //得到最初字符的长度大小，用于光标位置的判断
                int selEndIndex = Selection.getSelectionEnd(editable);
                //取出每个字符进行判断，如果是字母、数字和标点符号则为一个字符，若为汉字则为两个字符
                for(int i = 0; i < str.length(); i ++){
                    char charAt = str.charAt(i);
                    //32-122包含了空格、大小写字母、数字和一些常用的符号
                    if(charAt >= 32 && charAt <= 122){
                        mTextMaxlength ++;
                    }
                    else{
                        mTextMaxlength += 2;
                    }
                    if(mTextMaxlength > maxLength){
                        String newStr = str.substring(0, i);
                        LimitLengthEditText.this.setText(newStr);
                        //得到新字段的长度
                        editable = LimitLengthEditText.this.getText();
                        int newLen = editable.length();
                        if(selEndIndex > newLen){
                            selEndIndex = editable.length();
                        }
                        //设置新光标所在位置
                        Selection.setSelection(editable, selEndIndex);
                    }
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}
