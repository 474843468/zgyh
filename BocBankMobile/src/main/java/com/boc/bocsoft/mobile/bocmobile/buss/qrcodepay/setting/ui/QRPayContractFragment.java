package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.webView.ContractWebView;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.scan.ui.QRPayScanFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.presenter.QRPayBasePresenter;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;


/**
 * 二维码支付 - 协议
 * Created by wangf on 2016/09/02.
 */
public class QRPayContractFragment extends BussFragment implements QRPayBaseContract.QrServiceOpenBaseView{

    private View mRootView;
    /**
     * 合同的webview
     */
    private ContractWebView mWebView;
    private Button btnDisagree, btnAgree;

    //合同内容
    private ContractWebView.Contract mContract;

    private QRPayBasePresenter qrServiceOpenBasePresenter;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_qrpay_contract, null);
        return mRootView;
    }


    @Override
    public void beforeInitView() {

    }


    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    public void initView() {
        mWebView = (ContractWebView) mRootView.findViewById(R.id.web_qrpay_contract);
        btnDisagree = (Button) mRootView.findViewById(R.id.btn_qrpay_contract_disagree);
        btnAgree = (Button) mRootView.findViewById(R.id.btn_qrpay_contract_agree);
    }

    @Override
    public void initData() {

        qrServiceOpenBasePresenter = new QRPayBasePresenter(this);

//        String type=getArguments().getString("type");
//        if ("合同".equals(type)){
//            String content=getArguments().getString("content");
//            String cbiCustName= getArguments().getString("cbiCustName");
//            String cbiCerNo=getArguments().getString("cbiCerNo");
//            String cbiCustAccount=getArguments().getString("cbiCustAccount");
//            contract = new ContractWebView.Contract(content,cbiCustName,cbiCerNo,cbiCustAccount);
//        }else if("协议".equals(type)){
//            String content=getArguments().getString("content");
//            contract=new ContractWebView.Contract(content);
//        }
//        contract=new ContractWebView.Contract(content);
        mWebView.setDefaultLoadUrl("file:///android_asset/webviewcontent/qrcodepay/erweimaPay.html");
//        mWebView.setData(contract);
    }

    @Override
    public void setListener() {
        //不同意
        btnDisagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getAppManager().finishActivity();
            }
        });
        //同意
        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog();
                qrServiceOpenBasePresenter.loadQRServiceOpen();
            }
        });
    }


    @Override
    public boolean onBack() {
        ActivityManager.getAppManager().finishActivity();
        return false;
    }


    /*** 开通二维码服务成功 */
    @Override
    public void loadQRServiceOpenSuccess() {
        closeProgressDialog();
        if (QRPayScanFragment.SCAN_FROM_SCAN == getArguments().getInt(QRPayScanFragment.SCAN_FROM_KEY, -1)){
            QRPaySetPayPwdFragment fragment = new QRPaySetPayPwdFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(QRPayScanFragment.SCAN_FROM_KEY, QRPayScanFragment.SCAN_FROM_SCAN);
            fragment.setArguments(bundle);
            startWithPop(fragment);
        }else{
            startWithPop(new QRPaySetPayPwdFragment());
        }
    }

    /*** 开通二维码服务失败 */
    @Override
    public void loadQRServiceOpenFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }
}
