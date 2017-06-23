package com.boc.bocsoft.mobile.bocmobile.base.widget.operation;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * (请使用最新结果页组件 BaseResultView,使用参考 TestResultFragment)操作结果头部组件
 * Created by Administrator on 2016/5/20.
 */
@Deprecated
public class OperationResultHead extends LinearLayout {

    private Context context;
    private View rootView;
    private ImageView img_result;
    private TextView tv_result, text_info,text_other;
    private LinearLayout llHeadInfo;

    public enum Status {
        SUCCESS, FAIL, INPROGRESS
    }

    @Deprecated
    public OperationResultHead(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    @Deprecated
    public OperationResultHead(Context context) {
        super(context);
        this.context = context;
        init();
    }

    @Deprecated
    private void init() {
        rootView = LayoutInflater.from(context).inflate(R.layout.boc_view_operation_result_head, this);
        img_result = (ImageView) rootView.findViewById(R.id.img_result);
        tv_result = (TextView) rootView.findViewById(R.id.tv_result);
        text_info = (TextView) rootView.findViewById(R.id.text_info);
        text_other = (TextView) rootView.findViewById(R.id.text_other);
        llHeadInfo = (LinearLayout) rootView.findViewById(R.id.ll_head_info);
        TextPaint tp = tv_result.getPaint();
        //设置粗体
        tp.setFakeBoldText(true);
    }




    /**
     * 操作结果头部信息，不同结果切换相应图片
     *
     * @param status :操作状态
     *               1.SUCCESS 成功
     *               2.FAIL 失败
     *               3.INPROGRESS 处理中
     * @param text   :操作结果提示信息
     */
    @Deprecated
    public void updateData(Status status, String text) {
        tv_result.setText(text);
        Drawable drawable;
        switch (status) {
            case SUCCESS:
                drawable = getResources().getDrawable(R.drawable.boc_operator_succeed);
                img_result.setImageDrawable(drawable);
                break;
            case FAIL:
                drawable = getResources().getDrawable(R.drawable.boc_operator_fail);
                img_result.setImageDrawable(drawable);
                break;
            case INPROGRESS:
                drawable = getResources().getDrawable(R.drawable.boc_operator_inprogress);
                img_result.setImageDrawable(drawable);
                break;
        }
    }

    /**
     * 更新操作结果
     * @param status 结果窗台
     * @param statusDesc 结果描述
     * @param statusDetail 结果详情
     */
    @Deprecated
    public void updateData(Status status, String statusDesc, String statusDetail) {
        updateData(status, statusDesc);
        tv_result.setGravity(Gravity.CENTER_HORIZONTAL); //如有多行文字，每行都水平居中显示

        //调整与下面一行文字的行间距（默认的间距有点大）
        tv_result.setPadding(0, 0, 0, (int)getResources().getDimension(R.dimen.boc_space_between_10px));

        TextView tvResultDetail = (TextView)rootView.findViewById(R.id.tv_result_detail);
        tvResultDetail.setVisibility(View.VISIBLE);
        tvResultDetail.setText(statusDetail);
    }

    /**
     * 是否显示操作详细提示信息
     *
     * @param isTrue
     */
    @Deprecated
    public void isShowInfo(boolean isTrue, String name) {
        if (isTrue) {
            text_info.setVisibility(View.VISIBLE);
        } else {
            text_info.setVisibility(View.GONE);
        }
        text_info.setText(name);
    }

    /**
     * 是否显示操作详细提示信息（信息居中显示）
     *
     * @param isTrue
     */
    @Deprecated
    public void isShowOther(boolean isTrue, String name) {
        if (isTrue) {
            text_other.setVisibility(View.VISIBLE);
        } else {
            text_other.setVisibility(View.GONE);
        }
        text_other.setText(name);
    }

    @Deprecated
    public void isShowHeadInfo(boolean isVisibled) {
        if (isVisibled) {
            llHeadInfo.setVisibility(View.VISIBLE);
        } else {
            llHeadInfo.setVisibility(View.GONE);
        }
    }

    @Deprecated
    public LinearLayout getHeadInfoParent() {
        return llHeadInfo;
    }

    /**
     * 605
     */
    @Deprecated
    public void setHeadTxtInfo() {
        text_info.setPadding(30, 40, 30, 0);
        text_info.setTextSize(TypedValue.COMPLEX_UNIT_PX,30);
        text_info.setTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
        TextPaint tp = text_info.getPaint();
        tp.setFakeBoldText(true);
    }
}
