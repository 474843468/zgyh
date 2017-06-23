package com.boc.bocsoft.mobile.bocmobile.base.widget.details;

import android.annotation.SuppressLint;
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
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.MClickableSpan;

/**
 * 中银理财-持仓详情条目,优先右侧显示{产品名称和产品代码}
 * Created by yx on 2016/11/9.
 */
public class DetailFinancialTabRowTextButton extends LinearLayout {

    protected View rootView;
    private Context mContext;

    protected LinearLayout layoutBody;
    protected TextView tvTitle;
    protected TextView tvValueButton;
    protected View vBody;
    // 可点击的Span code码 可以点击
    private MClickableSpan mClickableSpan;
    // SpannableString对象 用来存储可点击的文字
    private SpannableString spannableStringButton;
    // 右侧蓝色字体点击事件
    private DetailRightTvOnClickListener mListener;

    public DetailFinancialTabRowTextButton(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public DetailFinancialTabRowTextButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    @SuppressLint("NewApi")
    public DetailFinancialTabRowTextButton(Context context, AttributeSet attrs,
                                           int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
        setListener();

    }

    /**
     * 初始化View
     */
    private void init() {
        rootView = LayoutInflater.from(mContext).inflate(
                R.layout.boc_view_financial_detail_textbutton, this);
        tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        tvValueButton = (TextView) rootView.findViewById(R.id.tv_value_button);
        layoutBody = (LinearLayout) rootView.findViewById(R.id.layout_body);
        vBody = (View) rootView.findViewById(R.id.v_body);
    }

    /**
     * 初始化监听事件
     */
    private void setListener() {


    }

    /**
     * 为条目设置内容
     *
     * @param name
     * @param value
     */
    public void addTextAndValue(String name, String value) {
        tvTitle.setText(name);
        tvValueButton.setText(value);
    }

    /**
     * 添加内容，右侧点击按钮
     *
     * @param name
     * @param value
     * @param button
     */
    public void addTextAndValue(String name, String value, String button) {
        tvTitle.setText(name);
        restContent(tvValueButton, value, button);
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
                    setOnclickView(tv, nameValue, code);
                } else {
//                    tv.setText(name + code);
                    setOnclickView(tv, nameValue, code);
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
        tvValueButton.setLongClickable(false);
        tv.setHighlightColor(Color.TRANSPARENT);
        if (mClickableSpan != null) {
            // 为协议文字添加点击事件
            mClickableSpan.setListener(new MClickableSpan.OnClickSpanListener() {
                @Override
                public void onClickSpan() {
                    if (mListener != null) {
                        LogUtils.d("yx------------onClickSpan");
                        mListener.onClickRightTextView();
                    }
                }
            });
        }
    }

    /***
     * 右侧蓝色按钮 点击监听事件
     */
    public interface DetailRightTvOnClickListener {
        void onClickRightTextView();

    }

    /**
     * 设置监听
     *
     * @param onClickListener
     */
    public void setRightTvListener(DetailRightTvOnClickListener onClickListener) {
        this.mListener = onClickListener;
    }

    /**
     * 根据手机分辨率吧dp转换成px
     */
    private int getPXvalue(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale - 0.5f);
    }
}