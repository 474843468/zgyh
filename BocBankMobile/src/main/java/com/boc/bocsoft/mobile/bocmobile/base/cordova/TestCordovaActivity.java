package com.boc.bocsoft.mobile.bocmobile.base.cordova;

import android.os.Bundle;

/**
 * 作者：XieDu
 * 创建时间：2016/12/13 19:19
 * 描述：
 */
public class TestCordovaActivity extends BaseCordovaActivity {

    private String testUrl = "http://22.11.26.74/bocphone/Framework/test.html?entrance=SouvenirCoin_test";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadUrl(launchUrl);
    }
}
