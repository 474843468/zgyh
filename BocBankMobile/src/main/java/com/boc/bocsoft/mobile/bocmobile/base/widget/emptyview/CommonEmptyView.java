package com.boc.bocsoft.mobile.bocmobile.base.widget.emptyview;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.MClickableSpan;


/**
 * 空界面时候显示EmyView {图片和文案，可以设置点击文本事件}
 *
 * @author yx
 * @date 2016-9-17 09:15:41
 */
public class CommonEmptyView extends LinearLayout {

    private Context mContext;
    private View rootview;
    /**
     * 提示图片
     */
    private ImageView iv_empty;
    /**
     * 提示文本
     */
    private TextView tv_empty;
    //可点击的Span
    private MClickableSpan clickableSpan;
    //SpannableString对象
    private SpannableString spannableString;

    public CommonEmptyView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public CommonEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public CommonEmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    /*
     * 初始化 引用固定模板布局
     */
    public void init() {
        rootview = View
                .inflate(mContext, R.layout.view_common_empty_view, null);
        // 添加模板
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        addView(rootview, layoutParams);
        initView();
    }

    /*
     * 初始化模板内部控件
     */
    public void initView() {
        iv_empty = (ImageView) rootview.findViewById(R.id.iv_empty);
        tv_empty = (TextView) rootview.findViewById(R.id.tv_empty);
    }

    /**
     * 设置页面数据为空时的提示
     *
     * @param tips       提示语
     * @param drawableId 提示图片
     */
    public void setEmptyTips(String tips, int drawableId) {
        tv_empty.setText(tips);
        iv_empty.setImageResource(drawableId);
        iv_empty.setVisibility(View.VISIBLE);
    }

    /**
     * 设置页面数据为空时的提示,没有图片显示
     *
     * @param tips 提示语
     */
    public void setEmptyTips(String tips) {
        tv_empty.setText(tips);
        iv_empty.setVisibility(View.GONE);
    }

    /**
     * @param drawableId 图片资源
     * @param index      设定第几个文本可点击，并且是蓝色字体
     * @param tips       设置多个文本提示
     */
    public void setEmptyTips(int drawableId, int index, String... tips) {
        String CREDIT_CONTRACT = "";
        tv_empty.setText("");
        for (int i = 0; i < tips.length; i++) {
            if (i == index) {
                CREDIT_CONTRACT = tips[i];
                spannableString = new SpannableString(CREDIT_CONTRACT);
                clickableSpan = new MClickableSpan(CREDIT_CONTRACT, getContext());
                clickableSpan.setColor(getResources().getColor(R.color.boc_main_button_color));
                spannableString.setSpan(clickableSpan, 0, CREDIT_CONTRACT.length(),
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                tv_empty.append(spannableString);
                clickableSpan.setListener(new MClickableSpan.OnClickSpanListener() {
                    @Override
                    public void onClickSpan() {
                        if (mTextOnclickListener != null) {
                            mTextOnclickListener.textOnclickListener();
                        }

                    }
                });
            } else {
                tv_empty.append(tips[i]);
            }
        }

        tv_empty.setMovementMethod(LinkMovementMethod.getInstance());
        tv_empty.setLongClickable(false);
        iv_empty.setImageResource(drawableId);
        iv_empty.setVisibility(View.VISIBLE);
    }

    /**
     * 监听接口，将蓝色文本的点击事件暴露到使用此View的Fragment中
     */
    private TextOnclickListener mTextOnclickListener;

    /**
     * 设置蓝色文本的点击事件-回调监听
     *
     * @param selectListener
     */
    public void setTextOnclickListener(TextOnclickListener selectListener) {
        this.mTextOnclickListener = selectListener;
    }

    public static interface TextOnclickListener {
        void textOnclickListener();
    }

}
