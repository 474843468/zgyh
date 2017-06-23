package com.chinamworld.llbt.userwidget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.llbtwidget.R;

/**
 * 数字型 文本字体大小可设置 控件
 * Created by linyl on 2016/11/1.
 */
public class NumberStyleTextView extends LinearLayout {

    private Context context;
    private View rootView;
    TextView tv_left,tv_right;

    public NumberStyleTextView(Context context){
        super(context);
        initLayout(context);
    }

    public NumberStyleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
    }

    private void initLayout(Context context){
        this.context = context;
        LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(param);
        LayoutInflater inflater = LayoutInflater.from(context);
        rootView = inflater.inflate(R.layout.llbt_numberstyle_layout, this, false);
        this.addView(rootView);
        tv_left = (TextView) rootView.findViewById(R.id.textview_left);
        tv_right = (TextView) rootView.findViewById(R.id.textview_right);
    }

    /**
     * 设置控件字符串样式
     * @param str 控件显示数字信息
     * @param rightLength  右侧 size变化位数
     */
    public void setNumberText(String str,int rightLength){
        if(str.length() < rightLength){
            tv_right.setText(str);
            return;
        }
        tv_left.setText(str.substring(0,str.length()-rightLength));
        tv_right.setText(str.substring(str.length()-rightLength));
    }

    /**
     * 设置控件字符串样式
     * @param str 控件显示数字信息
     * @param rightLength  右侧 size变化位数
     * @param defalutStr  如果传入数据为空时，默认显示的值
     */
    public void setNumberText(String str,int rightLength,String defalutStr){
        if(str == null || "-".equals(str) || "".equals(str) || "null".equals(str)) {
            tv_left.setText(defalutStr);
            return;
        }
        setNumberText(str,rightLength);
    }

}
