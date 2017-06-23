package com.boc.bocsoft.mobile.bocmobile.base.widget.wealthmanagemenadviertisementview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * 中银理财-广告view
 * Created by yx on 2016/9/17.
 */
public class WealthManagemenAdviertisementView extends LinearLayout {

    private Context mContext;
    private View rootview;
    /**
     * 立即购买按钮
     */
    private TextView tv4;

    public WealthManagemenAdviertisementView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public WealthManagemenAdviertisementView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.mContext = context;
        init();
    }

    @SuppressLint("NewApi")
    public WealthManagemenAdviertisementView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        this.mContext = context;
        init();
    }

    /*
     * 初始化 引用固定模板布局
     */
    public void init() {
        rootview = View
                .inflate(mContext, R.layout.view_wealth_management_rjyl, null);
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
        tv4 = (TextView) rootview.findViewById(R.id.tv4);
        tv4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdviertisementViewListener != null) {
                    mAdviertisementViewListener.adviertisementViewOnclickListener();
                }
            }
        });
    }


    /**
     * 监听接口
     */
    private AdviertisementViewOnclickListener mAdviertisementViewListener;

    /**
     * 设置立即购买按钮-回调监听
     *
     * @param selectListener
     */
    public void setTextOnclickListener(AdviertisementViewOnclickListener selectListener) {
        this.mAdviertisementViewListener = selectListener;
    }

    public static interface AdviertisementViewOnclickListener {
        void adviertisementViewOnclickListener();
    }
}
