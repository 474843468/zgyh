package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.collection.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;

/**
 * Created by fanbin on 16/10/11.
 */
public class QRCollectionSuccessFragment extends BussFragment {
    private View mRootView;
    private LayoutInflater mInflater;
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        this.mInflater = mInflater;
        mRootView = View.inflate(mContext, R.layout.boc_fragment_qrcollection_success, null);

        return mRootView;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

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
