package com.boc.bocsoft.mobile.bocmobile.base.widget.details;

import android.content.Context;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.MClickableSpan;

/**
 * Created by taoyongzhen on 2016/12/28.
 * 该组件在DetailTabRowTextButton基础上下部添加textView，提供hint信息，在基金持仓中有所使用
 */

public class DetailTabRowTextButtonWithHint extends FrameLayout {


    protected View rootView;
    private Context mContext;

    protected RelativeLayout layoutBody;
    protected TextView tvTitle;
    protected TextView tvValue;
    protected TextView tvButton;
    protected TextView tvTitleHint;
    protected TextView tvValueHint;
    protected View vBody;
    protected View vDevide;
    // 可点击的Span code码 可以点击
    private MClickableSpan mClickableSpan;
    // SpannableString对象 用来存储可点击的文字
    private SpannableString spannableStringButton;
    private DetailTvButtonOnClickListener detailTvOnClickListener;
    public DetailTabRowTextButtonWithHint(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public DetailTabRowTextButtonWithHint(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public DetailTabRowTextButtonWithHint(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init(){
        rootView = LayoutInflater.from(mContext).inflate(
                R.layout.boc_view_detail_textbutton_hint, this);
        tvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        tvTitleHint = (TextView) rootView.findViewById(R.id.tv_title_hint);
        tvValue = (TextView) rootView.findViewById(R.id.tv_value);
        tvButton = (TextView) rootView.findViewById(R.id.tv_button);
        tvValueHint = (TextView) rootView.findViewById(R.id.tv_hint);
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
                if (detailTvOnClickListener != null) {
                    detailTvOnClickListener.onClickTextView();
                }
            }
        });
    }

    /**
     * 最右侧按钮点击事件
     */
    public interface DetailTvButtonOnClickListener {
        void onClickTextView();
    }


    public void setDetailTvOnClickListener(DetailTvButtonOnClickListener detailTvOnClickListener) {
        this.detailTvOnClickListener = detailTvOnClickListener;
    }

    /**
     * 添加信息
     * @param title
     * @param value
     * @param button
     * @param detailTvOnClickListener
     */
    public void addTextandView(String title,String value,String button,DetailTvButtonOnClickListener detailTvOnClickListener){
        tvTitle.setText(title);
        tvValue.setText(value);
        tvButton.setText(button);
        setDetailTvOnClickListener(detailTvOnClickListener);
    }

    public void setTilleHint(String titleHint){
        tvTitleHint.setText(titleHint);
    }

    public void setValueHint(String valueHint){
        tvValueHint.setText(valueHint);
    }

    public void setHintInfo(String titleHint,String valuehint){
        setTilleHint(titleHint);
        setValueHint(valuehint);
    }

    public void updateValueContent(String content){
        tvValue.setText(content);
    }

}
