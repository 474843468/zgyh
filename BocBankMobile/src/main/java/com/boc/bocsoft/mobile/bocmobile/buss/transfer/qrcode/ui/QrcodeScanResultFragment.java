package com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.model.ScanResultAccountModel;

/**
 * Use the {@link QrcodeScanResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 * 二维码扫描结果页面
 */
public class QrcodeScanResultFragment extends BussFragment {

    private static final String SCAN_RESULT="scan_result";
    private ScanResultAccountModel mScanResultAccountModel;

    public static QrcodeScanResultFragment newInstance(ScanResultAccountModel scanResultAccountModel) {
        QrcodeScanResultFragment fragment = new QrcodeScanResultFragment();
        Bundle args = new Bundle();
        args.putParcelable(SCAN_RESULT,scanResultAccountModel);
        fragment.setArguments(args);
        LogUtils.i(scanResultAccountModel.toString());
        return fragment;
    }

    /**
     * 初始化布局
     *
     * @param mInflater
     * @return
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return View.inflate(mContext, R.layout.fragment_qrcode_scan_result, null);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        if (getArguments() != null) {
            mScanResultAccountModel = getArguments().getParcelable(SCAN_RESULT);
        }
    }


    @Override
    public void setListener() {
    }


    /**
     * 是否显示标题栏
     */
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mQrcodeScanPresenter.unsubscribe();
    }

}
