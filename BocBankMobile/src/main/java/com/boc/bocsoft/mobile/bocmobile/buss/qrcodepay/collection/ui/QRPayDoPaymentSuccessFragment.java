package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.collection.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;

/**
 * Created by fanbin on 16/10/11.
 */
public class QRPayDoPaymentSuccessFragment extends BussFragment {
    private View mRootView;
    private LayoutInflater mInflater;
    private TextView tv_boc_fragment_qrcollection_success_tishi,tv_boc_fragment_qrcollection_success_xinxi
            ,tv_boc_fragment_qrcollection_success_transnum,tv_boc_fragment_qrcollection_success_transtime
            ,tv_boc_fragment_qrcollection_success_ordernum;
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        this.mInflater = mInflater;
        mRootView = View.inflate(mContext, R.layout.boc_fragment_paydoment_success, null);

        return mRootView;
    }

    @Override
    public void initView() {
        tv_boc_fragment_qrcollection_success_xinxi= (TextView) mRootView.findViewById(R.id.tv_boc_fragment_qrcollection_success_tishi);
        tv_boc_fragment_qrcollection_success_transnum= (TextView) mRootView.findViewById(R.id.tv_boc_fragment_qrcollection_success_transnum);
        tv_boc_fragment_qrcollection_success_transtime= (TextView) mRootView.findViewById(R.id.tv_boc_fragment_qrcollection_success_transtime);
        tv_boc_fragment_qrcollection_success_ordernum= (TextView) mRootView.findViewById(R.id.tv_boc_fragment_qrcollection_success_ordernum);

    }

    @Override
    public void initData() {
        String payeeName=getArguments().getString("payeeName");
        String amount=getArguments().getString("amount");
        String acount=getArguments().getString("acount");
        String orderTime=getArguments().getString("orderTime");
        tv_boc_fragment_qrcollection_success_xinxi.setText("向"+payeeName+"成功付款"+amount+"元");
        tv_boc_fragment_qrcollection_success_transnum.setText(acount);
        tv_boc_fragment_qrcollection_success_transtime.setText(orderTime);


    }
    @Override
    protected boolean getTitleBarRed() {
        return false;
    }
    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.trans_phone_operate_title);
    }

}
