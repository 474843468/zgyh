package com.boc.bocsoft.mobile.bocmobile.base.widget.financelist;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.MClickableSpan;

/**
 * 理财列表item
 * Created by liuweidong on 2016/9/10.
 */
public class FinanceListItemView extends RelativeLayout {
    private Context context;
    private View rootView;
    //左边头部内容
    private TextView txtHeadLeft;
    //右边头部内容容器
    private LinearLayout llHeadRight;
    private TextView txtHeadRight1;
    private TextView txtHeadRight;
    private View dividerLine;
    private TextView txtNameL, txtNameC, txtNameR;
    private TextView txtValueL, txtValueC, txtValueR;
    private TextView txtBottom;
    private ImageView imgSellOut;

    public FinanceListItemView(Context context) {
        this(context, null);
    }

    public FinanceListItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FinanceListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        rootView = View.inflate(context, R.layout.boc_widget_item_finance_list, this);
        txtHeadLeft = (TextView) rootView.findViewById(R.id.txt_head_left);
        llHeadRight = (LinearLayout) rootView.findViewById(R.id.ll_head_right);
        txtHeadRight1 = (TextView) rootView.findViewById(R.id.txt_head_right_1);
        txtHeadRight = (TextView) rootView.findViewById(R.id.txt_head_Right);
        dividerLine = rootView.findViewById(R.id.divider_line);
        txtNameL = (TextView) rootView.findViewById(R.id.txt_name_left);
        txtNameC = (TextView) rootView.findViewById(R.id.txt_name_center);
        txtNameR = (TextView) rootView.findViewById(R.id.txt_name_right);
        txtValueL = (TextView) rootView.findViewById(R.id.txt_value_left);
        txtValueC = (TextView) rootView.findViewById(R.id.txt_value_center);
        txtValueR = (TextView) rootView.findViewById(R.id.txt_value_right);
        txtBottom = (TextView) rootView.findViewById(R.id.txt_bottom);
        imgSellOut = (ImageView) rootView.findViewById(R.id.img_sell_out);
    }

    /**
     * 是否显示分隔线
     *
     * @param isVisible
     */
    public void isShowDividerLine(boolean isVisible) {
        if (isVisible) {
            dividerLine.setVisibility(View.VISIBLE);
        } else {
            dividerLine.setVisibility(View.GONE);
        }
    }

    public TextView getTxtNameL() {
        return txtNameL;
    }

    /**
     * 设置头部左边文本
     *
     * @param value
     */
    public void setTxtHeadLeft(String value) {
        txtHeadLeft.setText(value);
    }

    /**
     * 设置头部右边文本
     *
     * @param value
     */
    public void setTxtHeadRight(String value) {
        txtHeadRight.setText(value);
    }

    /**
     * 设置所有内容名称
     */
    public void setTxtCenterName(String nameL, String nameC, String nameR) {
        txtNameL.setText(nameL);
        txtNameC.setText(nameC);
        txtNameR.setText(nameR);
    }

    /**
     * 设置所有内容的值
     *
     * @param valueL
     * @param valueC
     * @param valueR
     */
    public void setTxtCenterValue(String valueL, String valueC, String valueR) {
        txtValueL.setText(valueL);
        txtValueC.setText(valueC);
        txtValueR.setText(valueR);
    }
    /**
     * 设置所有内容的值
     *
     * @param valueL
     * @param valueC
     * @param valueR
     */
    public void setTxtCenterValue(String valueL, String valueC, SpannableString valueR) {
        txtValueL.setText(valueL);
        txtValueC.setText(valueC);
        txtValueR.setText(valueR);
    }
    public void setTxtCenterValue(SpannableString valueL, String valueC, String valueR) {
        txtValueL.setText(valueL);
        txtValueC.setText(valueC);
        txtValueR.setText(valueR);
    }
    public void setTxtCenterValue(SpannableString[] valueL, String valueC, String valueR) {
        if(valueL==null){
            txtValueL.setText("");
        }else{
            txtValueL.setText("");
            for (int i =0;i<valueL.length;i++){
                txtValueL.append(valueL[i]);
            }
        }
        txtValueC.setText(valueC);
        txtValueR.setText(valueR);
    }
    public void setHeadRight(boolean isCollect, boolean isLimit) {
        txtHeadRight1.setVisibility(isCollect ? VISIBLE : GONE);
        if (isLimit) {
            txtHeadRight.setVisibility(View.VISIBLE);
            txtHeadRight.setTextColor(getResources().getColor(R.color.boc_text_money_color_red));
            txtHeadRight.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
            txtHeadRight.setBackgroundResource(R.drawable.boc_frame_red);
            txtHeadRight.setText("额度紧张");
        } else {
            txtHeadRight.setVisibility(View.GONE);
        }
    }

    public void setImgBottom(boolean isVisible) {
        imgSellOut.setVisibility(isVisible ? VISIBLE : GONE);
    }

    /**
     * 设置左边值的属性
     *
     * @param color
     * @param dpsize
     * @date 2016-09-23 20:20:29
     * @author yx
     */
    public void setValueLAttributeDp(int color, float dpsize) {
        txtValueL.setTextColor(color);
        txtValueL.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dpsize);
    }

    /**
     * 设置下边属性
     */
    public void setBottomAttribute(int color, String value) {
        txtBottom.setVisibility(View.VISIBLE);
        txtBottom.setText(value);
        txtBottom.setTextColor(color);
    }

    /**
     * 设置下边文本显示位置
     *
     * @param gravity
     */
    public void setBottomLocation(int gravity) {
        txtBottom.setGravity(gravity);
    }

    /**
     * 设置带颜色的文本
     *
     * @param color
     * @param index
     * @param tips
     */
    public void setAppendTextColor(int color, int index, String... tips) {
        String CREDIT_CONTRACT = "";
        txtHeadRight.setText("");
        for (int i = 0; i < tips.length; i++) {
            if (i == index) {
                CREDIT_CONTRACT = tips[i];
                SpannableString spannableString = new SpannableString(CREDIT_CONTRACT);
                MClickableSpan clickableSpan = new MClickableSpan(CREDIT_CONTRACT, getContext());
                clickableSpan.setColor(color);
                spannableString.setSpan(clickableSpan, 0, CREDIT_CONTRACT.length(),
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                txtHeadRight.append(spannableString);
                txtHeadRight.setLongClickable(false);
            } else {
                txtHeadRight.append(tips[i]);
                txtHeadRight.setLongClickable(false);
            }
        }
    }

    /**
     *设置左侧value 的文本颜色和内容
     * @param content
     * @param color
     */
    public void setValueLeft(String content,int color){
        txtValueL.setTextColor(context.getResources().getColor(color));
        txtValueL.setText(content);
    }

    /**
     *设置中间value 的文本颜色和内容
     * @param content
     * @param color
     */
    public void setValueCenter(String content,int color){
        txtValueC.setTextColor(context.getResources().getColor(color));
        txtValueC.setText(content);
    }

    /**
     * 设置右侧value 的文本颜色和内容
     * @param content
     * @param color
     */
    public void setValueRight(String content,int color){
        txtValueR.setTextColor(context.getResources().getColor(color));
        txtValueR.setText(content);
    }

    /**
     * 设置所有name 字体颜色
     * @param color
     */
    public void setTxtCenterColor(int color){
        txtNameL.setTextColor(context.getResources().getColor(color));
        txtNameC.setTextColor(context.getResources().getColor(color));
        txtNameR.setTextColor(context.getResources().getColor(color));
    }
}
