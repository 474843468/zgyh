package com.chinamworld.bocmbci.biz.finc.finc_p606.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * onMeasure简单方法 解决ListView与ScollView冲突问题！
 */
public class MeasureListView extends ListView {
    public MeasureListView(Context context) {
        super(context);
    }

    public MeasureListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressLint("NewApi")
    public MeasureListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
//    MeasureSpec.makeMeasureSpec(int size，int mode)中的两个参数含义如下
//    size:表示父布局提供给你的大小参考
//    mode:表示规格，有EXACTLY、AT_MOST、UNSPECIFIED三种。
//    Integer.MAX_VALUE >> 2：表示父布局给的参考的大小无限大。（listview无边界）
//    MeasureSpec.AT_MOST：表示根据布局的大小来确定listview最终的高度，也就是有多少内容就显示多高。
}
