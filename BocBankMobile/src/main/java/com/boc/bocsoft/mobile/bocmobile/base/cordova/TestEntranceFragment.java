package com.boc.bocsoft.mobile.bocmobile.base.cordova;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;

/**
 * 作者：XieDu
 * 创建时间：2016/12/13 19:15
 * 描述：
 */
public class TestEntranceFragment extends BussFragment {

    private String testUrl = "http://22.11.26.74/bocphone/Framework/test.html?entrance=SouvenirCoin_test";

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_contract, null);
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = new Intent();
        intent.putExtra(BaseCordovaActivity.URL, testUrl);
        intent.setClass(mActivity, TestCordovaActivity.class);
        startActivity(intent);
    }
}
