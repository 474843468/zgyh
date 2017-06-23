package com.boc.bocsoft.mobile.framework.widget.imageLoader.itf;

import android.graphics.Bitmap;

/**
 * Created by XieDu on 2016/5/31.
 */
public interface Transformation {
    Bitmap transform(Bitmap var1);

    String key();
}
