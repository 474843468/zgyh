package com.boc.bocsoft.mobile.bocmobile.base.widget.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

import java.math.BigDecimal;

/**
 * 斩仓比例和报警比例组件（保证金详情页面用到）
 * Created by zhx on 2016/12/21
 */
public class LiquidationAndWarnRatio extends FrameLayout {
    private Context mContext;
    private View mRootView;
    private BigDecimal mTopRate = BigDecimal.valueOf(0.2); // 顶部比率
    private BigDecimal mBottomRate = BigDecimal.valueOf(0.5); // 底部比率
    private LinearLayout ll_top;
    private LinearLayout ll_bottom;
    private int measuredWidth;
    private int measuredHeight;
    private View view_level_2;
    private View view_level_3;
    private TextView tv_top;
    private TextView tv_bottom;
    private int topWidth;
    private int bottomWidth;


    public LiquidationAndWarnRatio(Context context) {
        this(context, null);

    }

    public LiquidationAndWarnRatio(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public LiquidationAndWarnRatio(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.layout_liquidation_and_warn_ratio, this);
        ll_top = (LinearLayout) mRootView.findViewById(R.id.ll_top);
        ll_bottom = (LinearLayout) mRootView.findViewById(R.id.ll_bottom);
        view_level_2 = mRootView.findViewById(R.id.view_level_2);
        view_level_3 = mRootView.findViewById(R.id.view_level_3);
        tv_top = (TextView) mRootView.findViewById(R.id.tv_top);
        tv_bottom = (TextView) mRootView.findViewById(R.id.tv_bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        measuredWidth = this.getMeasuredWidth();
        measuredHeight = this.getMeasuredHeight();
        topWidth = ll_top.getMeasuredWidth();
        bottomWidth = ll_bottom.getMeasuredWidth();
    }



    public void setRate(BigDecimal topRate, BigDecimal bottomRate) {
        int rateTop = (int) (topRate.doubleValue() * 100);
        tv_top.setText(rateTop + "%");
        int rateBottom = (int) (bottomRate.doubleValue() * 100);
        tv_bottom.setText(rateBottom + "%");

        // 设置顶部比率的位置
        int topMarginLeft = (int) (measuredWidth * topRate.doubleValue() - topWidth);
        FrameLayout.LayoutParams topLayoutParams = (FrameLayout.LayoutParams) ll_top.getLayoutParams();
        topLayoutParams.leftMargin = topMarginLeft;
        ll_top.setLayoutParams(topLayoutParams);

        // 设置底部比率的位置
        int bottomMarginLeft = (int) (measuredWidth * bottomRate.doubleValue() - bottomWidth);
        FrameLayout.LayoutParams bottomLayoutParams = (FrameLayout.LayoutParams) ll_bottom.getLayoutParams();
        bottomLayoutParams.leftMargin = bottomMarginLeft;
        ll_bottom.setLayoutParams(bottomLayoutParams);

        // 设置指示器2的位置
        int indicator2Width = (int) (measuredWidth * bottomRate.doubleValue());
        FrameLayout.LayoutParams indicator2LayoutParams = (FrameLayout.LayoutParams) view_level_2.getLayoutParams();
        indicator2LayoutParams.width = indicator2Width;
        view_level_2.setLayoutParams(indicator2LayoutParams);

        // 设置指示器3的位置
        int indicator3Width = (int) (measuredWidth * topRate.doubleValue());
        FrameLayout.LayoutParams indicator3LayoutParams = (FrameLayout.LayoutParams) view_level_3.getLayoutParams();
        indicator3LayoutParams.width = indicator3Width;
        view_level_3.setLayoutParams(indicator3LayoutParams);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
