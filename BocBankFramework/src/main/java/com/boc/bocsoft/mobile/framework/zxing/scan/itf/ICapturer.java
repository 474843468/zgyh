package com.boc.bocsoft.mobile.framework.zxing.scan.itf;

import android.app.Activity;
import android.os.Bundle;

import com.google.zxing.Result;

/**
 * Created by XieDu on 2016/6/15.
 */
public interface ICapturer {
    public Activity getActivity();

    public void finish();

    public void handleDecode(Result obj, Bundle bundle);

    public void onCamaraOpenError();
}
