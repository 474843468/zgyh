package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.scan.ui.QRPayScanFragment;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;

/**
 * 操作结果
 * Created by wangf on 2016/9/18.
 */
public class QRPaySetResultFragment extends BussFragment {

    private View mRootView;

    private BaseOperationResultView mOperationResultView;

    public static final String QRPAY_RESULT_KEY = "qrpay_result_key";
    public static final int QRPAY_PWD_SET_SUCCESS = 1;
    public static final int QRPAY_PWD_CHANGE_SUCCESS = 2;
    public static final int QRPAY_PWD_RESET_SUCCESS = 3;

    private int currentResultId = 0;


    public static QRPaySetResultFragment newInstance(int resultId) {
        Bundle bundle = new Bundle();
        bundle.putInt(QRPAY_RESULT_KEY, resultId);
        QRPaySetResultFragment fragment = new QRPaySetResultFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = View.inflate(mContext, R.layout.boc_fragment_qrpay_result, null);
        currentResultId = getArguments().getInt(QRPAY_RESULT_KEY, 0);
        return mRootView;
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_qrpay_result);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    public void initView() {
        mOperationResultView = (BaseOperationResultView) mRootView.findViewById(R.id.rv_qrpay_result);
        mOperationResultView.setDetailVisibility(View.GONE);
        mOperationResultView.isShowBottomInfo(false);
        mOperationResultView.updateButtonStyle();

    }

    @Override
    public void initData() {
        switch (currentResultId) {
            case QRPAY_PWD_SET_SUCCESS:
                mOperationResultView.updateHead(OperationResultHead.Status.SUCCESS, "密码设置成功");
                break;
            case QRPAY_PWD_CHANGE_SUCCESS:
                mOperationResultView.updateHead(OperationResultHead.Status.SUCCESS, "密码修改成功");
                break;
            case QRPAY_PWD_RESET_SUCCESS:
                mOperationResultView.updateHead(OperationResultHead.Status.SUCCESS, "密码重置成功");
                break;
            default:
                mOperationResultView.updateHead(OperationResultHead.Status.SUCCESS, "");
                break;
        }
    }

    @Override
    public void setListener() {
        // "返回首页" 的点击事件
        mOperationResultView.setgoHomeOnclick(new OperationResultBottom.HomeBtnCallback() {
            @Override
            public void onHomeBack() {
//                if (QRPayScanFragment.SCAN_FROM_SCAN == getArguments().getInt(QRPayScanFragment.SCAN_FROM_KEY, -1)){
//                    ActivityManager.getAppManager().finishActivity();
//                }else{
//                    popTo(QRPayMainFragment.class, false);
//                }
                if (QRPayScanFragment.SCAN_FROM_SCAN == getArguments().getInt(QRPayScanFragment.SCAN_FROM_KEY, -1)){
                    ActivityManager.getAppManager().finishActivity();//设置密码 - 来自 扫描页面的开通二维码服务、未设置密码
                }else if(QRPayResetPayPwdFragment.RESET_PASS_FROM_PAY == getArguments().getInt(QRPayResetPayPwdFragment.RESET_PASS_FROM_KEY, -1)){
                    ActivityManager.getAppManager().finishActivity();//重置密码 - 来自 C2B正扫支付页面的忘记密码
                }else{
                    popToAndReInit(QRPayMainFragment.class);
                }
            }
        });
    }
    
    
    @Override
    public boolean onBack() {
        if (QRPayScanFragment.SCAN_FROM_SCAN == getArguments().getInt(QRPayScanFragment.SCAN_FROM_KEY, -1)){
            ActivityManager.getAppManager().finishActivity();//设置密码 - 来自 扫描页面的开通二维码服务、未设置密码
        }else if(QRPayResetPayPwdFragment.RESET_PASS_FROM_PAY == getArguments().getInt(QRPayResetPayPwdFragment.RESET_PASS_FROM_KEY, -1)){
            ActivityManager.getAppManager().finishActivity();//重置密码 - 来自 C2B正扫支付页面的忘记密码
        }else{
            popToAndReInit(QRPayMainFragment.class);
        }
        return false;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}