package com.chinamworld.llbt.userwidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.llbtwidget.R;


/**
 * P606批次新版布局。左右文本显示显示控件
 * Created by yuht on 2016/8/22.
 */
public class NewLabelTextView extends LinearLayout {

    public enum TextColor {
        Red, Black,
    }


    final int RED = 0xffba001d;
    final int BLACK = 0xff000000;


    private View rootView;
    private Context context;
    private TextView labelTextView;
    private TextView valueTextView;
    private TextView valueTextTwoView;
    private LinearLayout valueLayout;

    public NewLabelTextView(Context context){
        super(context);
        initLayout(context,null);
    }

    public NewLabelTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context,attrs);
    }

    private void initLayout(Context context, AttributeSet attr) {
        this.context = context;
        LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(param);
        LayoutInflater inflater = LayoutInflater.from(context);
        rootView = inflater.inflate(R.layout.new_label_textview_layout, this, false);
        this.addView(rootView);
        labelTextView = (TextView) rootView.findViewById(R.id.labelTextView);
        valueTextView = (TextView) rootView.findViewById(R.id.valueTextView);
        valueTextTwoView = (TextView) rootView.findViewById(R.id.valueTextViewTwo);
        valueLayout =(LinearLayout)rootView.findViewById(R.id.valueLayout);
//            BasePopupWindowUtils.Instance.setOnShowAllTextListener(context,
//                    valueTextView);
        if (attr == null)
            return;
        TypedArray t = context.obtainStyledAttributes(attr,
               R.styleable.NewLabelTextView);
        int id;
        for (int i = 0; i < t.getIndexCount(); i++) {
            id = t.getIndex(i);
            if(id == R.styleable.NewLabelTextView_NL_labelText){
                setLabelText(t.getString(id));
            }
            else if(id == R.styleable.NewLabelTextView_NL_valueText){
                setValueText(t.getString(id));
            }
            else if(id == R.styleable.NewLabelTextView_NL_LableTextColor){
                setTextColor(t.getInt(id, 1), labelTextView);
            }
            else if(id == R.styleable.NewLabelTextView_NL_ValueTextColor){
                setTextColor(t.getInt(id, 1), valueTextView);
            }
            else if(id == R.styleable.NewLabelTextView_NL_weightShowRate){
                setWeightShowRate(t.getString(id));
            }
        }

        t.recycle();


    }



    /**
     * 设置省略号的位置
     * @param where
     */
    public void setEllipsize(TextUtils.TruncateAt where){
        labelTextView.setEllipsize(where);
        valueTextView.setEllipsize(where);
        valueTextTwoView.setEllipsize(where);
    }

    public void setWeightShowRate(String weightShowRate) {
        if (weightShowRate == null)
            return;
        String[] splid = weightShowRate.split(":");
        if (splid.length != 2)
            return;
        try {
            labelTextView.setLayoutParams(new LayoutParams(0,
                    LayoutParams.WRAP_CONTENT, Float.parseFloat(splid[0])));
            valueLayout.setLayoutParams(new LayoutParams(0,
                    LayoutParams.WRAP_CONTENT, Float.parseFloat(splid[1])));
        } catch (Exception e) {
        }
    }

    /**
     * 设置控件文本展示的颜色
     * */
    private void setTextColor(TextColor color, TextView tv) {
        if (color == TextColor.Red)
            tv.setTextColor(RED);
        else if (color == TextColor.Black) {
            tv.setTextColor(BLACK);
        }
    }

    private void setTextColor(int color, TextView tv) {
        TextColor textColor = TextColor.Black;
        if (color == 0)
            textColor = TextColor.Red;
        else if (color == 1)
            textColor = TextColor.Black;
        setTextColor(textColor, tv);
    }

    /**
     * 设置控件左边展示的值
     * */
    public void setLabelText(String labelText) {
        labelTextView.setText(labelText);
//        BasePopupWindowUtils.Instance.setOnShowAllTextListener(context,
//                labelTextView);
    }

    /**
     * 设置控件左边展示的值(参数类型 int)
     */
    public void setLabelText(int resid) {
        labelTextView.setText(resid);
//        BasePopupWindowUtils.Instance.setOnShowAllTextListener(context,
//                labelTextView);
    }

    /**
     * 设置控件右边展示的值
     * */
    public void setValueText(String valueText) {
        valueTextView.setText(valueText);
//        BasePopupWindowUtils.Instance.setOnShowAllTextListener(context,
//                labelTextView);
    }

    /**
     * 设置控辩左边文本展示的颜色
     * */
    public void setLabelTextColor(TextColor color) {
        setTextColor(color, labelTextView);
    }

    /**
     * 设置控件右边展示的值
     * */
    public void setValueTextTwo(String valueText) {
        valueTextTwoView.setText(valueText);
//        BasePopupWindowUtils.Instance.setOnShowAllTextListener(context,
//                valueTextTwoView);
    }

    /**
     * 设置控辩左边文本展示的颜色
     * */
    public void setValueTextTwoColor(TextColor color) {
        setTextColor(color, valueTextTwoView);
    }

    /**
     * 设置控件右边文本展示的颜色
     * */
    public void setValueTextColor(TextColor color) {
        setTextColor(color, valueTextView);

    }

    public void setValueTextViewSingleLine(boolean flag){
        valueTextView.setSingleLine(flag);
    }

    /**
     * 设置左边文本颜色
     * @param resid
     */
    public void setLabelTextColor(int resid) {
        this.labelTextView.setTextColor(resid);
    }

    /**
     * 设置右边文本颜色
     * @param resid
     */
    public void setValueTextColor(int resid) {
        this.valueTextView.setTextColor(resid);
    }

    /**
     * 设置左边文本字体大小
     * @param resid 具体大小，如13sp，resid传入13
     */
    public void setLabelTextSize(int resid) {
        this.labelTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,resid);
    }

    /**
     * 设置右边文本字体大小
     * @param resid
     */
    public void setValueTextSize(int resid) {
        this.valueTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,resid);
    }

    /**
     * 设置加粗
     * @param flag
     */
    public void setLabelTextBold(boolean flag) {
        this.labelTextView.getPaint().setFakeBoldText(flag);
    }

    /**
     * 设置加粗
     * @param flag
     */
    public void setValueTextBold(boolean flag) {
        this.valueTextView.getPaint().setFakeBoldText(flag);
    }


}
