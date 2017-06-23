package com.boc.bocsoft.mobile.bocmobile.base.widget.SpringPressageView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;


/**
 * 中银理财->持仓详情->收益累进->预期年华收益率详情
 * 动态增长型进度条，类似progressBar，按百分比显示
 * 在xml中添加本组件，然后调用类中addTextContent()方法
 * Created by zn on 2016/11/3.
 */
public class SpringRateDetailContent extends LinearLayout {
    //上下文
    private Context mContext;
    //容器
    private LinearLayout detail_content;
    //百分比
    private float progress_show;
    public SpringRateDetailContent(Context context) {
        this(context, null, 0);
    }

    public SpringRateDetailContent(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpringRateDetailContent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        init();
    }
    private void init() {
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        detail_content = new LinearLayout(mContext);
        detail_content.setLayoutParams(lp);
        detail_content.setOrientation(LinearLayout.VERTICAL);
        this.addView(detail_content);
    }
    /**
     *  此方法供外界调用，传入以下参数
     * @param Max   最大值
     * @param Min   当前数据（后面不用加 % ）
     * @param days  左边标题
     * @return
     */
    public SpringRateDetailContent addTextContent(String Max, String Min, String days) {
        SpringRateDetail textContent = new SpringRateDetail(mContext);
        Float intMax = Float.valueOf(Max);
        Float intMin = Float.valueOf(Min);
        if (intMax!=0 && intMin<=intMax){
            progress_show = (intMin / intMax) * 100f;
            Log.i("progress_show =",progress_show+"");
        }

        textContent.addSpringProgressView(intMax,intMin,progress_show,days);
        detail_content.addView(textContent);
        return this;
    }
}