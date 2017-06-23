package com.boc.bocsoft.mobile.bocmobile.base.widget.divider;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * 水平方向分割线
 * Created by XieDu on 2016/7/27.
 */
public class VerticalDivider extends View {
    public VerticalDivider(Context context) {
        super(context);
        initDefault();
    }

    public VerticalDivider(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDefault();
    }

    public VerticalDivider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDefault();
    }

    private void initDefault() {
        setBackgroundColor(getResources().getColor(R.color.boc_divider_line_color));
        setLayoutParams(new ViewGroup.LayoutParams(
                getResources().getDimensionPixelSize(R.dimen.boc_divider_1px),
                ViewGroup.LayoutParams.MATCH_PARENT));
    }
}
