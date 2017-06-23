package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * 作者：xwg on 16/12/16 13:53
 * 账单信息
 */
public class CrcdBillInfoItemView extends LinearLayout{
    private final Context mContext;
    private LinearLayout rooView;
    private LinearLayout ll_content_right;
    private TextView tvLeftTop,tvLeftMiddle,tvLeftBottom;
    private TextView tvRightTop,tvRightMiddle,tvRightBottom;
    private View divLine;
    private TextView tvLeftMiddle1;
    private TextView tvRightMiddle1;
    private View divider;
    private TextView tv_no_result;
    private LinearLayout ll_container;

    public CrcdBillInfoItemView(Context context) {
        this(context,null);
    }

    public CrcdBillInfoItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CrcdBillInfoItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
        initView();
    }

    private void initView() {
        rooView=(LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.boc_fragment_crcd_bill_info_item,this);



        ll_container=(LinearLayout)rooView.findViewById(R.id.ll_container);
        ll_content_right=(LinearLayout)rooView.findViewById(R.id.ll_content_right);

        tvLeftTop=(TextView)rooView.findViewById(R.id.tv_leftTop);
        tvLeftMiddle=(TextView)rooView.findViewById(R.id.tv_leftMiddle);
        tvLeftMiddle1=(TextView)rooView.findViewById(R.id.tv_leftMiddle1);
        tvLeftBottom=(TextView)rooView.findViewById(R.id.tv_leftBottom);

        tvRightTop=(TextView)rooView.findViewById(R.id.tv_rightTop);
        tvRightMiddle=(TextView)rooView.findViewById(R.id.tv_rightMiddle);
        tvRightMiddle1=(TextView)rooView.findViewById(R.id.tv_rightMiddle1);
        tvRightBottom=(TextView)rooView.findViewById(R.id.tv_rightBottom);

        divLine=rooView.findViewById(R.id.divide_line);
        divider=rooView.findViewById(R.id.divider);
        tv_no_result=(TextView)rooView.findViewById(R.id.tv_no_result);

    }

    /**
     *  设置是否显示右边部分
     *
     *  false时，全部左对齐，隐藏分割线
     */
    public void showRightContent(boolean isShow){
        if (isShow){
            ll_content_right.setVisibility(VISIBLE);
            tvLeftMiddle.setGravity(Gravity.END);
            tvLeftBottom.setGravity(Gravity.END);
            divLine.setVisibility(VISIBLE);
        }else{
            ll_content_right.setVisibility(GONE);
            tvLeftMiddle.setGravity(Gravity.START);
            tvLeftBottom.setGravity(Gravity.START);
            divLine.setVisibility(GONE);
        }
    }

    /**
     * 设置左边文本内容
     *
     * @param topText   最上边文字
     * @param middleText 中间文字
     * @param bottomText    底部文字 为空时不显示
     */
    public void setLeftContentText(String topText,String middleText,String bottomText){
        tvLeftTop.setVisibility(VISIBLE);
        tvLeftTop.setText(topText);
        tvLeftMiddle.setText(middleText);

        tvLeftBottom.setVisibility(StringUtil.isNullOrEmpty(bottomText)?GONE:VISIBLE);
        tvLeftBottom.setText(bottomText);
    }
    /**
     * 设置左边文本内容
     *
     * @param topText   最上边文字
     * @param middleText 中间文字
     */
    public void setLeftContentText(String topText,String middleText){
        setLeftContentText(topText,middleText,"");
    }
    /**
     * 设置右边文本内容
     *
     * @param topText   最上边文字
     * @param middleText 中间文字
     */
    public void setRightContentText(String topText,String middleText){
        setRightContentText(topText,middleText,"");
    }
    /**
     * 设置右边文本内容
     *
     * @param topText   最上边文字
     * @param middleText 中间文字
     * @param bottomText    底部文字
     */
    public void setRightContentText(String topText,String middleText,String bottomText){
        tvRightTop.setVisibility(VISIBLE);
        tvRightTop.setText(topText);
        tvRightMiddle.setText(middleText);

        tvRightBottom.setVisibility(StringUtil.isNullOrEmpty(bottomText)?GONE:VISIBLE);
        tvRightBottom.setText(bottomText);
    }

    /**
     *  是否首行 两列展示
     *  支出收入可调用此方法
     * @param topLeft
     * @param topRight
     * @param bottom
     * @param isDoubleColumn  是否首行 两列展示
     */
    public void setLeftContentText(String topLeft,String topRight,String bottom,boolean isDoubleColumn){
        if (isDoubleColumn){
            tvLeftTop.setVisibility(GONE);
            tvLeftMiddle1.setText(topLeft);
            tvLeftMiddle.setText(topRight);
            tvLeftBottom.setText(bottom);
            tvLeftBottom.setVisibility(StringUtil.isNullOrEmpty(bottom)?GONE:VISIBLE);
        }else {
            setLeftContentText(topLeft,topRight,bottom);
        }

    }
    /**
     *  是否首行 两列展示
     *  支出收入可调用此方法
     * @param topLeft
     * @param topRight
     * @param isDoubleColumn  是否首行 两列展示
     */
    public void setLeftContentText(String topLeft,String topRight,boolean isDoubleColumn){
        setLeftContentText(topLeft,topRight,"",isDoubleColumn);
    }
    /**
     *  支出收入可调用此方法
     * @param topLeft
     * @param topRight
     * @param bottom
     * @param isDoubleColumn  是否首行 两列展示
     */
    public void setRightContentText(String topLeft,String topRight,String bottom,boolean isDoubleColumn){
        if (isDoubleColumn){
            tvRightTop.setVisibility(GONE);
            tvRightMiddle1.setText(topLeft);
            tvRightMiddle.setText(topRight);
            tvRightBottom.setText(bottom);
            tvRightBottom.setVisibility(StringUtil.isNullOrEmpty(bottom)?GONE:VISIBLE);
        }else {
            setRightContentText(topLeft,topRight,bottom);
        }

    }
    /**
     *  支出收入可调用此方法
     * @param topLeft
     * @param topRight
     * @param isDoubleColumn  是否首行 两列展示
     */
    public void setRightContentText(String topLeft,String topRight,boolean isDoubleColumn){
        setRightContentText(topLeft,topRight,"",isDoubleColumn);

    }

    /**
     * 设置左边文本文字颜色
     * @param topTextColor
     * @param middleTextColor
     * @param bottomTextColor
     */
    public void setLeftContentTextColor(int topTextColor,int middleTextColor,int bottomTextColor){
        tvLeftTop.setTextColor(topTextColor);
        tvLeftMiddle.setTextColor(middleTextColor);
        tvLeftBottom.setTextColor(bottomTextColor);
    }
    /**
     * 设置左边文本文字颜色
     * @param topLeftTextColor
     * @param rightTextColor
     * @param bottomTextColor
     */
    public void setLeftContentTextColor(int topLeftTextColor,int rightTextColor,int bottomTextColor,boolean isDoubleColumn){
        if (isDoubleColumn){
            tvLeftMiddle1.setTextColor(topLeftTextColor);
            tvLeftMiddle.setTextColor(rightTextColor);
            tvLeftBottom.setTextColor(bottomTextColor);
        }else {
            setLeftContentTextColor(topLeftTextColor,rightTextColor,bottomTextColor);
        }
    }
    /**
     * 设置右边文本文字颜色
     * @param topTextColor
     * @param middleTextColor
     * @param bottomTextColor
     */
    public void setRightContentTextColor(int topTextColor,int middleTextColor,int bottomTextColor){
        tvRightTop.setTextColor(topTextColor);
        tvRightMiddle.setTextColor(middleTextColor);
        tvRightBottom.setTextColor(bottomTextColor);
    }
    /**
     * 设置右边文本文字颜色
     * @param topLeftTextColor
     * @param rightTextColor
     * @param bottomTextColor
     */
    public void setRightContentTextColor(int topLeftTextColor,int rightTextColor,int bottomTextColor,boolean isDoubleColumn){
        if (isDoubleColumn){
            tvRightMiddle1.setTextColor(topLeftTextColor);
            tvRightMiddle.setTextColor(rightTextColor);
            tvRightBottom.setTextColor(bottomTextColor);
        }else{
            setRightContentTextColor(topLeftTextColor,rightTextColor,bottomTextColor);
        }

    }

    /**
    * 设置底部margin
    */
    public void setBottomDividerVisble(boolean visble){
        divider.setVisibility(visble?VISIBLE:GONE);
    }

    public void setNoResultText(String text){
        tv_no_result.setText(text);
    }

    /**
    *  设置无结果页面是否显示
     *  （用于当前view作用于listview headview的情况）
    */
    public void setNoResultVisble(boolean visble){
        tv_no_result.setVisibility(visble?VISIBLE:GONE);
    }

    /**
    *  设置账单信息部分是否可见
    */
    public void setContainerVisble(boolean visble){
        ll_container.setVisibility(visble?VISIBLE:GONE);
    }

    /**
    *  设置容积部分背景色
    */
    public void setContainerBackgroudResource(int resourceId){
        ll_container.setBackgroundResource(resourceId);
    }

    public TextView getTvLeftBottom() {
        return tvLeftBottom;
    }

    public TextView getTvLeftMiddle() {
        return tvLeftMiddle;
    }

    public TextView getTvLeftTop() {
        return tvLeftTop;
    }

    public TextView getTvRightBottom() {
        return tvRightBottom;
    }

    public TextView getTvRightMiddle() {
        return tvRightMiddle;
    }

    public TextView getTvRightTop() {
        return tvRightTop;
    }
}
