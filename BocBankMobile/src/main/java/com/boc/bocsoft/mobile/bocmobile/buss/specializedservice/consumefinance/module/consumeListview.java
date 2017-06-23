package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.consumefinance.module;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by zcy7065 on 2016/11/3.
 */
public class consumeListview extends ListView {

    public consumeListview(Context context){
        super(context);
    }

    public consumeListview(Context context, AttributeSet attrs){
        super(context,attrs);
    }
    public consumeListview(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs,defStyle);
    }


    @Override

    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec,expandSpec);

    }


}
