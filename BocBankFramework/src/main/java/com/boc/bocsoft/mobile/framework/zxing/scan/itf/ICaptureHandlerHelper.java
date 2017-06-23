package com.boc.bocsoft.mobile.framework.zxing.scan.itf;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;

import com.google.zxing.Result;

/**
 * Created by XieDu on 2016/6/15.
 */
public interface ICaptureHandlerHelper {
    public Handler getCaptureHandler();
    public Rect getCropRect();
    public void handleDecode(Result obj, Bundle bundle);
}
