package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.BaseDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.webView.ContractWebView;

/**
 * Created by wangtong on 2016/10/11.
 */
public class CannotReceivePage extends Activity {

    protected ContractWebView userLimit;
    protected TextView btnKnow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boc_fragment_user_limit);
        initView();
        initData();
        setListener();
    }

    public void initView() {
        userLimit = (ContractWebView) findViewById(R.id.user_limit);
        btnKnow = (TextView) findViewById(R.id.btn_know);
    }

    public void initData() {
        //ContractWebView.Contract contract = new ContractWebView.Contract();
        userLimit.setDefaultLoadUrl("file:///android_asset/webviewcontent/securityverify/tipInfo.html");
        //userLimit.setData(contract);
    }

    public void setListener() {
        btnKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
