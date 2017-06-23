package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.framework.utils.ResUtils;
import com.boc.bocsoft.mobile.framework.zxing.encode.QRCodeEncoder;

/**
 * 理财明细--二维码
 * Created by liuweidong on 2016/9/12.
 */
public class WealthQrCodeFragment extends BussFragment {

    private View mRootView;
    private ImageView iv_wealth_qrcode;
    private QRCodeEncoder mQRCodeEncoder;

    private String prodCode;
    private String productKind;

    public static final String WEALTH_QRCODE_KEY_CODE = "WEALTH_QRCODE_KEY_CODE";
    public static final String WEALTH_QRCODE_KEY_KIND = "WEALTH_QRCODE_KEY_KIND";

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = View.inflate(mContext, R.layout.boc_fragment_wealth_qrcode, null);
        return mRootView;
    }


    @Override
    public void initView() {
        iv_wealth_qrcode = (ImageView) mRootView.findViewById(R.id.iv_wealth_qrcode);
    }

    @Override
    public void initData() {
        prodCode = getArguments().getString(WEALTH_QRCODE_KEY_CODE);
        productKind = getArguments().getString(WEALTH_QRCODE_KEY_KIND);
        initCodeBuilder();
        setQrCodeToView(buildQrcodeContent(prodCode, productKind));
    }

    @Override
    protected String getTitleValue() {
        return "二维码";
    }


    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }


    /**
     * 封装二维码数据
     * @param prodCode
     * @param productKind
     * @return boc://bocphone?type=1&prodCode=12345&productKind=12345
     */
    private String buildQrcodeContent(String prodCode, String productKind){
        StringBuffer sb = new StringBuffer();
        sb.append("boc://bocphone?type=1")
                .append("&prodCode=")
                .append(prodCode)
                .append("&productKind=")
                .append(productKind);
        return  sb.toString();
    }


    /**
     * 设置二维码
     */
    private void setQrCodeToView(String code) {
        Bitmap qrCodeBitmap = mQRCodeEncoder.encode(code);
        if (qrCodeBitmap == null) {
            iv_wealth_qrcode.setImageBitmap(null);
            Toast.makeText(mContext, "二维码生成错误，请重试", Toast.LENGTH_SHORT).show();
        } else {
            iv_wealth_qrcode.setImageBitmap(qrCodeBitmap);
        }
    }


    /**
     * 初始化生成二维码参数
     */
    private void initCodeBuilder() {
        mQRCodeEncoder = new QRCodeEncoder.Builder()
                .setOutputBitmapHeight(ResUtils.dip2px(mContext, 300))
                .setOutputBitmapWidth(ResUtils.dip2px(mContext, 300))
                .setLogoScale(8)
                .setLogoBitmap(BitmapFactory.decodeResource(getResources(),
                                R.drawable.boc_qrcode_logo))
                .build();
    }

}
