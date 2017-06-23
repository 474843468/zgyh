package com.boc.bocsoft.mobile.framework.widget.imageLoader.itf;

import android.content.Context;

import com.boc.bocsoft.mobile.common.client.BaseHttpClient;

/**
 * Created by XieDu on 2016/5/31.
 */
public interface RequestProcessorProvider {

    public Context getContext();

    public RequestProcessor provideRequestProcessor();

    public BaseHttpClient getHttpClient();
}
