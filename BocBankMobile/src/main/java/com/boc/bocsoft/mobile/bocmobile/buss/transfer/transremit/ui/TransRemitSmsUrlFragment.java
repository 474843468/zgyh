package com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.ui;

import android.view.LayoutInflater;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.webView.BaseWebView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by WYme on 2016/10/20.
 */
public class TransRemitSmsUrlFragment extends BussFragment{
    private BaseWebView smsView;
    private TextView  button;
    private View  mRootView;
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_trans_5wan_detail_fragment, null);
        return mRootView;
    }

    @Override
    public void initView() {
        smsView= (BaseWebView) mRootView.findViewById(R.id.sms_detail_info);
        button= (TextView) mRootView.findViewById(R.id.btn_I_know);
    }

    @Override
    public void initData() {
        smsView.setDefaultLoadUrl("http://www.boc.cn/ebanking/service/cs1/201609/t20160908_7648060.html");
    }

    @Override
    public void setListener() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }
}
