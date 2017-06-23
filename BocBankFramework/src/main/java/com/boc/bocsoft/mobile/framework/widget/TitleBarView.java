package com.boc.bocsoft.mobile.framework.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.framework.R;
import com.boc.bocsoft.mobile.framework.utils.ResUtils;

/**
 * TODO: document your custom view class.
 */
public class TitleBarView extends RelativeLayout {
    protected LinearLayout rightButtonContainer;
    protected View dividerBottom;
    private TextView tvTitle;
    private ImageView btnLeft, iConBtnRight;
    private View rootView;

    private Drawable mIconleft, mIconRight;//左右图标
    private int mBackground;
    private int mTitleColor; //标题颜色
    private String mTitleText;
    private TextView rightTextButton;

    public TitleBarView(Context context) {
        this(context, null);
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        initView();
        final TypedArray typedArray =
                getContext().obtainStyledAttributes(attrs, R.styleable.TitleBarView, defStyle, 0);
        getType(typedArray);
        setView();
    }

    private void getType(TypedArray typedArray) {
        mIconleft = typedArray.getDrawable(R.styleable.TitleBarView_iconLeft);
        mIconRight = typedArray.getDrawable(R.styleable.TitleBarView_iconRight);
        mTitleColor = typedArray.getColor(R.styleable.TitleBarView_titleColor, mTitleColor);
        mTitleText = typedArray.getString(R.styleable.TitleBarView_titleString);
        mBackground = typedArray.getColor(R.styleable.TitleBarView_barBackground, mBackground);
        typedArray.recycle();
    }

    private void initView() {
        rootView = inflate(getContext(), R.layout.boc_view_title_bar, this);
        tvTitle = (TextView) rootView.findViewById(R.id.titleValueTv);
        btnLeft = (ImageView) rootView.findViewById(R.id.leftIconIv);
        iConBtnRight = (ImageView) rootView.findViewById(R.id.rightIconIv);
        rightTextButton = (TextView) rootView.findViewById(R.id.rightTextButton);
        rightButtonContainer = (LinearLayout) rootView.findViewById(R.id.rightButtonContainer);
        dividerBottom = rootView.findViewById(R.id.divider_bottom);
    }

    private void setView() {
        if (mTitleColor != 0) {
            tvTitle.setTextColor(mTitleColor);
            rightTextButton.setTextColor(mTitleColor);
        }
        tvTitle.setText(mTitleText);
        if (mIconleft != null) {
            btnLeft.setImageDrawable(mIconleft);
        }
        if (mIconRight != null) {
            iConBtnRight.setImageDrawable(mIconRight);
        }
        if (mBackground != 0) {
            setBackgroundColor(mBackground);
        }
    }

    public TitleBarView setTitle(int resId) {
        return setTitle(getResources().getText(resId));
    }

    public TitleBarView setTitle(CharSequence title) {
        tvTitle.setText(title);
        return this;
    }

    public TitleBarView setTitleColor(int color) {
        tvTitle.setTextColor(color);
        return this;
    }

    private TitleBarView setLeftButtonIcon(Drawable icon) {
        btnLeft.setImageDrawable(icon);
        btnLeft.setVisibility(VISIBLE);
        return this;
    }

    public TitleBarView setRightButton(Drawable icon) {
        iConBtnRight.setImageDrawable(icon);
        iConBtnRight.setVisibility(VISIBLE);
        rightTextButton.setVisibility(View.GONE);
        return this;
    }

    /**
     * 设置右边按钮
     */
    public TitleBarView setRightButton(String message) {
        iConBtnRight.setVisibility(View.GONE);
        rightTextButton.setVisibility(View.VISIBLE);
        rightTextButton.setText(message);
        return this;
    }

    /**
     * 同事显示右侧的图标和文字
     */
    public TitleBarView setRightButton(Context context, Drawable icon, String message) {
        iConBtnRight.setImageDrawable(icon);
        iConBtnRight.setVisibility(VISIBLE);

        int paddingMid = ResUtils.dip2px(context, 4);
        int paddingEdge = ResUtils.dip2px(context, 17);
        iConBtnRight.setPadding(paddingEdge, 0, paddingMid, 0);

        rightTextButton.setText(message);
        rightTextButton.setVisibility(View.VISIBLE);
        rightTextButton.setPadding(0, 0, paddingEdge, 0);
        return this;
    }

    /**
     * 设置右边按钮
     */
    public TextView setRightButton(String message, OnClickListener listener) {
        iConBtnRight.setVisibility(View.GONE);
        rightTextButton.setVisibility(View.VISIBLE);
        rightTextButton.setText(message);
        if (listener != null) {
            rightTextButton.setOnClickListener(listener);
        }
        return rightTextButton;
    }

    //设置左侧按钮监听事件
    public TitleBarView setLeftButtonOnClickLinster(OnClickListener listener) {
        btnLeft.setOnClickListener(listener);
        return this;
    }

    //设置右侧按钮监听事件
    public TitleBarView setRightButtonOnClickLinster(OnClickListener listener) {
        iConBtnRight.setOnClickListener(listener);
        rightTextButton.setOnClickListener(listener);
        return this;
    }

    /**
     * 设置右侧图片按钮是否显示
     */
    public TitleBarView setRightImgBtnVisible(Boolean isVisible) {
        if (isVisible) {
            iConBtnRight.setVisibility(View.VISIBLE);
        } else {
            iConBtnRight.setVisibility(View.INVISIBLE);
        }
        return this;
    }

    /**
     * 设置左侧按钮是否显示
     */
    public TitleBarView setLeftBtnVisible(Boolean isVisible) {
        if (isVisible) {
            btnLeft.setVisibility(View.VISIBLE);
        } else {
            btnLeft.setVisibility(View.INVISIBLE);
        }
        return this;
    }

    public TitleBarView setStyle(int styleId) {
        final TypedArray typedArray =
                getContext().obtainStyledAttributes(styleId, R.styleable.TitleBarView);
        getType(typedArray);
        setView();
        return this;
    }

    public TextView getRightTextButton() {
        return rightTextButton;
    }

    public ImageView getRightImgButton() {
        return iConBtnRight;
    }

    public LinearLayout getRightContainer() {
        return rightButtonContainer;
    }

    public void setDividerBottomVisible(boolean visible){
        dividerBottom.setVisibility(visible?VISIBLE:GONE);
    }
}
