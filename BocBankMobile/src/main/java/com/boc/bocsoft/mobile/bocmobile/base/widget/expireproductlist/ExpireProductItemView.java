package com.boc.bocsoft.mobile.bocmobile.base.widget.expireproductlist;

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
 * Created by zc7067 on 2016/11/11.
 *
 * 中银理财——已到期产品列表Item专用，其他慎用
 */
public class ExpireProductItemView extends RelativeLayout {
    private Context context;
    private View rootView;
    //左边头部内容
    private TextView txtHeadLeft;
    private TextView txtHeadname;
    private TextView txtHeadcode;

    //右边头部内容容器
    private LinearLayout llHeadRight;
    private TextView txtHeadRight1;
    private TextView txtHeadRight;
    private View dividerLine;
    private TextView txtNameL, txtNameC, txtNameR;
    private TextView txtValueL, txtValueC, txtValueR;
    private TextView txtBottom;
    private ImageView imgSellOut;

    public ExpireProductItemView(Context context) {
        this(context, null);
    }

    public ExpireProductItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpireProductItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        rootView = View.inflate(context, R.layout.boc_widget_item_expire_product_list, this);
        txtHeadLeft = (TextView) rootView.findViewById(R.id.txt_head_left);
        txtHeadname = (TextView) rootView.findViewById(R.id.txt_head_name);
        txtHeadcode = (TextView) rootView.findViewById(R.id.txt_head_code);
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
     * 设置头部币种
     *
     * @param value
     */
    public void setTxtHeadCurrency(String value) {
        txtHeadLeft.setText(value);
    }
    /**
     * 设置头部产品名称
     *
     * @param value
     */
    public void setTxtHeadName(String value) {
        txtHeadname.setText(value);
    }
    /**
     * 设置头部产品代码
     *
     * @param value
     */
    public void setTxtHeadCode(String value) {
        txtHeadcode.setText(value);
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

    public void setTxtCenterValue(SpannableString valueL, String valueC, String valueR) {
        txtValueL.setText(valueL);
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
}
