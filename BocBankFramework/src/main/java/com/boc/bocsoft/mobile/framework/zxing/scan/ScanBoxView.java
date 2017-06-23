package com.boc.bocsoft.mobile.framework.zxing.scan;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.boc.bocsoft.mobile.framework.R;
import com.boc.bocsoft.mobile.framework.utils.ResUtils;

/**
 * 扫描控件，包括扫描框和扫描线。不包括扫描框四周的阴影
 * @author xdy4486
 */
public class ScanBoxView extends RelativeLayout {
    protected View rootView;
    protected ImageView ivScanLine;
    protected RelativeLayout layoutScanBox;
    private Drawable mBoxBackground;
    private Drawable mScanLine;//扫描线
    private int mBoxWidth; //扫描框的宽度
    private int mBoxHeight;//扫描框的高度
    private int mScanlineTopMargin;//扫描线的top偏移量

    public ScanBoxView(Context context) {
        super(context);
        init(null, 0);
    }

    public ScanBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ScanBoxView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void initDefault() {
        mBoxWidth = ResUtils.dip2px(getContext(), 600);
        mBoxHeight = ResUtils.dip2px(getContext(), 600);
    }


    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        initDefault();
        final TypedArray typedArray = getContext().obtainStyledAttributes(
                attrs, R.styleable.ScanBoxView, defStyle, 0);
        Drawable boxBackgroud = typedArray.getDrawable(R.styleable.ScanBoxView_boxBackground);
        if (boxBackgroud != null) mBoxBackground = boxBackgroud;
        Drawable scanLine = typedArray.getDrawable(R.styleable.ScanBoxView_scanLine);
        if (scanLine != null) mScanLine = scanLine;
        mBoxWidth = typedArray.getDimensionPixelSize(R.styleable.ScanBoxView_boxWidth, mBoxWidth);
        mBoxHeight = typedArray.getDimensionPixelSize(R.styleable.ScanBoxView_boxHeight, mBoxHeight);
        mScanlineTopMargin = typedArray.getDimensionPixelSize(R.styleable.ScanBoxView_scanLineTopMargin, mScanlineTopMargin);
        typedArray.recycle();
        initView();
    }


    private void initView() {
        if (isInEditMode())
            return;
        View rootView = inflate(getContext(), R.layout.boc_view_scan_box, this);
        ivScanLine = (ImageView) rootView.findViewById(R.id.iv_scan_line);
        layoutScanBox = (RelativeLayout) rootView.findViewById(R.id.layout_scan_box);
        ResUtils.setBackground(ivScanLine,mScanLine);
        //ivScanLine.setImageDrawable(mScanLine);
        LayoutParams lineLp = (LayoutParams) ivScanLine.getLayoutParams();
        lineLp.topMargin = mScanlineTopMargin;
        lineLp.width=mBoxWidth-ResUtils.dip2px(getContext(),20);
        ivScanLine.setLayoutParams(lineLp);
        LayoutParams boxLp = (LayoutParams) layoutScanBox.getLayoutParams();
        boxLp.width = mBoxWidth;
        boxLp.height = mBoxHeight;
        layoutScanBox.setLayoutParams(boxLp);
        ResUtils.setBackground(layoutScanBox, mBoxBackground);
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                1.0f);
        animation.setDuration(3000);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        ivScanLine.startAnimation(animation);
    }

}
