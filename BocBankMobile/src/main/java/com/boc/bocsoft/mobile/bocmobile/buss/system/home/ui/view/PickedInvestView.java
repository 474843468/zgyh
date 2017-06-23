package com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 用户选择的投产项目
 * Created by lxw on 2016/7/18 0018.
 */
public class PickedInvestView extends LinearLayout {
    public PickedInvestView(Context context) {
        super(context);
    }

    public PickedInvestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PickedInvestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PickedInvestView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


}
