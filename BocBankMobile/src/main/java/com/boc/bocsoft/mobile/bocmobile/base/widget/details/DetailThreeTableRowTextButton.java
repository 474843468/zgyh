package com.boc.bocsoft.mobile.bocmobile.base.widget.details;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.MClickableSpan;

/**
 * Created by taoyongzhen on 2016/12/22.
 * 包括：最左侧基金提示，中间：基金名称+基金代码，注意基金名称过长时，优先显示基金代码；最右侧必须显示基金转换按钮
 */

public class DetailThreeTableRowTextButton extends LinearLayout{


    protected View rootView;
    private Context mContext;

    protected RelativeLayout layoutBody;
    protected TextView tvTitle;
    protected TextView tvValue;
    protected TextView tvButton;
    protected View vBody;
    protected View vDevide;
    // 可点击的Span code码 可以点击
    private MClickableSpan mClickableSpan;
    // SpannableString对象 用来存储可点击的文字
    private SpannableString spannableStringButton;
    // 右侧蓝色字体点击事件
    private DetailThreeTableRowTextButton.DetailTvOnClickListener mListener;

    private DetailThreeTableRowTextButton.DetailRightTvOnClickListener rightTvOnClickListener;
    public DetailThreeTableRowTextButton(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public DetailThreeTableRowTextButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public DetailThreeTableRowTextButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    /**
     * 初始化View
     */
    private void init() {
        rootView = LayoutInflater.from(mContext).inflate(
                R.layout.boc_view_detail_three_tabrowtextbutton, this);
        tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        tvValue = (TextView) rootView.findViewById(R.id.tv_value);
        tvButton = (TextView) rootView.findViewById(R.id.tv_value_button);
        layoutBody = (RelativeLayout) rootView.findViewById(R.id.layout_body);
        vBody = (View) rootView.findViewById(R.id.v_body);
        vDevide = (View)rootView.findViewById(R.id.v_devide);
        setListener();
    }
    /**
     * 初始化监听事件
     */
    private void setListener() {
        tvButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClickTextView();
                }
            }
        });
    }

    /**
     *
     * @param title 左侧标题文本
     * @param leftValue  左侧文本
     * @param rightValue 右侧文本
     * @param button 按钮文本
     */
    public void addTextandView(String title,String leftValue,String rightValue,String button){
        tvTitle.setText(title);
        tvButton.setText(button);
        restContent(tvValue,leftValue,rightValue);
    }

    /**
     * 重置内容 保证code值显示完全,name不完整显示。。。
     *
     * @param tv
     * @param name
     * @param code
     * @return
     */
    private void restContent(final TextView tv, final String name, final String code) {
        LogUtils.d("dding", "重置内容:" + name + "," + code);

        tv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                tv.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int viewWidth = tv.getMeasuredWidth();
                float nameWidth = tv.getPaint().measureText(name);
                float codeWidth = tv.getPaint().measureText(code);
                LogUtils.d("dding", "---view treeObserver --- " + viewWidth + "," + nameWidth + "," + codeWidth);
                String nameValue = name;

                int lastIndex = nameValue.length() - 1;
                if (nameWidth + codeWidth > viewWidth) {
                    float aviableWidth = viewWidth - codeWidth - tv.getPaint().measureText("...");
                    for (; ; ) {
                        if (lastIndex < 0) break;
                        if (tv.getPaint().measureText(nameValue, 0, lastIndex) < aviableWidth) {
                            break;
                        }
                        lastIndex--;
                    }
                    if (lastIndex > 0) {
                        nameValue = nameValue.substring(0, lastIndex) + "...";
                    } else {
                        nameValue = "...";
                    }
                    //截取字符串
//                    tv.setText(nameValue + code);
                    setOnclickView(tv,nameValue,code);
                } else {
//                    tv.setText(name + code);
                    setOnclickView(tv,nameValue,code);
                }
            }
        });
    }

    /**
     * 处理 字符过长截取方法
     *
     * @param tv
     * @param name
     * @param code
     */
    private void setOnclickView(final TextView tv, final String name, final String code) {
        spannableStringButton = new SpannableString(code);
        mClickableSpan = new MClickableSpan(code, mContext);
        mClickableSpan.setColor(mContext.getResources().getColor(R.color.boc_main_button_color));
        spannableStringButton.setSpan(mClickableSpan, 0, code.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        LogUtils.d("yx-----tt---------title-->" + name + "------code--->" + code);
        tv.setText(name);
        tv.append(spannableStringButton);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setLongClickable(false);
        tv.setHighlightColor(Color.TRANSPARENT);
        if (mClickableSpan != null) {
            // 为协议文字添加点击事件
            mClickableSpan.setListener(new MClickableSpan.OnClickSpanListener() {
                @Override
                public void onClickSpan() {
                    if (rightTvOnClickListener != null) {
                        LogUtils.d("yx------------onClickSpan");
                        rightTvOnClickListener.onClickRightTextView();
                    }
                }
            });
        }
    }
    /***
     * 右侧蓝色文本 点击监听事件
     */
    public interface DetailTvOnClickListener {
        void onClickTextView();

    }

    /**
     * 设置监听
     * @param onClickListener
     */
    public void setDetailTvListener(DetailThreeTableRowTextButton.DetailTvOnClickListener onClickListener) {
        this.mListener = onClickListener;
    }

    /**
     * 设置右侧文本监听
     * @param rightTvOnClickListener
     */
    public void setRightTvOnClickListener(DetailRightTvOnClickListener rightTvOnClickListener) {
        this.rightTvOnClickListener = rightTvOnClickListener;
    }

    /**
     * 最右侧按钮点击事件
     */
    public interface DetailRightTvOnClickListener{
        void onClickRightTextView();
    }

    public void isShowDevide(boolean isShow){
        if (isShow) {
            vDevide.setVisibility(VISIBLE);
        }else{
            vDevide.setVisibility(GONE);
        }
    }

    public void isShowTvButton(boolean isShow){
        if (isShow) {
            tvButton.setVisibility(VISIBLE);
        }else{
            tvButton.setVisibility(GONE);
        }
    }

}
