package com.chinamworld.llbt.userwidget.refreshliseview;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.chinamworld.llbtwidget.R;

/**
 * Created by wang-pc on 2016/11/22.
 */
public class BaseFrameLayout extends FrameLayout {

    public BaseFrameLayout(Context context) {
        super(context);
    }

    public BaseFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }
    public void getReslutImageView(ImageView imageView){
        imageView.setBackgroundResource(R.drawable.load_success_new);
    }
}
