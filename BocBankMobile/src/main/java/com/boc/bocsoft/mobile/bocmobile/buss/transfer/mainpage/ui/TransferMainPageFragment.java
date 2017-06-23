package com.boc.bocsoft.mobile.bocmobile.buss.transfer.mainpage.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullableListView;
import com.boc.bocsoft.mobile.framework.ui.BaseFragment;

/**
 * Created by WM on 2016/6/4.
 */
public class TransferMainPageFragment extends BaseFragment {
//    private Context mContext;
    private View mainPageView;
    private TextView tv_myNum;
    private TextView tv_moduleNum;
    private PullableListView list_payee;
    private TextView  tv_allrencentpayee;
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mainPageView=LayoutInflater.from(mContext).inflate(R.layout.boc_transfer_main,null);
        return mainPageView;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        tv_moduleNum= (TextView) mainPageView.findViewById(R.id.modulenum);
        tv_myNum= (TextView) mainPageView.findViewById(R.id.mynumber);
        list_payee= (PullableListView) mainPageView.findViewById(R.id.recentpayeelist);
        tv_allrencentpayee= (TextView) mainPageView.findViewById(R.id.allrencentpayee);
    }

    @Override
    public void initData() {
        // TODO: 2016/6/6

    }

    @Override
    public void setListener() {
        // TODO: 2016/6/6
        tv_allrencentpayee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
