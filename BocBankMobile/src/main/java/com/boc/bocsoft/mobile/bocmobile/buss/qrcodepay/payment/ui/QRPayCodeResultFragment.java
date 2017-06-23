package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.payment.ui;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.payment.model.QRPayGetQRCodeViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.payment.model.QRPayTransInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.setting.ui.QRPayMainFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.framework.utils.ResUtils;
import com.boc.bocsoft.mobile.framework.zxing.encode.BarCodeEncoder;

/**
 * 二维码支付 - 操作结果页 - 反扫支付结果
 * Created by wangf on 2016/8/24.
 */
public class QRPayCodeResultFragment extends BussFragment {

    private View mRootView;

    private TextView txtHeadAmount;
    private TextView txtHeadInfo;
    private BaseOperationResultView mOperationResultView;

    //条形码的构建
    private BarCodeEncoder mBarCodeEncoder;

    private static final int OPEN_FREE_PAY = 0;

    //支付结果数据
    private QRPayTransInfoViewModel infoViewModel;
    //二维码付款信息
    private QRPayGetQRCodeViewModel payGetQRCodeViewModel;
    private String accountNumber;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = View.inflate(mContext, R.layout.boc_fragment_qrpay_result, null);
        infoViewModel = getArguments().getParcelable("GetTransInfo");
        payGetQRCodeViewModel = getArguments().getParcelable("GetQRCode");
        accountNumber = getArguments().getString("AccountNumber", "");
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
        mOperationResultView.setDetailsTitleIsShow(false);

//        //头部 信息
        View headView = View.inflate(mContext, R.layout.boc_view_qrpay_result_head_info, null);
//        txtHeadAmount = (TextView) headView.findViewById(R.id.txt_head_amount);
//        txtHeadInfo = (TextView) headView.findViewById(R.id.txt_head_info);
//        mOperationResultView.getHeadInfoParent().setPadding(0,0,0,0);
//        mOperationResultView.getHeadInfoParent().addView(headView);

        mOperationResultView.getHeadInfoParent().setPadding(0,40,0,0);
        mOperationResultView.getHeadInfoParent().addView(headView);

        //可能需要
        mOperationResultView.isShowBottomInfo(false);
//        mOperationResultView.setTitleNeedColor(R.color.boc_text_color_gray);

        initCodeBuilder();
    }

    @Override
    public void initData() {
        //头部 信息
        if ("0".equals(infoViewModel.getTranStatus())){
            String strTitle = "向" + payGetQRCodeViewModel.getMerchantName() + "成功支付" + MoneyUtils.transMoneyFormat(payGetQRCodeViewModel.getAmount(), ApplicationConst.CURRENCY_CNY) + "元";
            mOperationResultView.updateHead(OperationResultHead.Status.SUCCESS, strTitle);
//            txtHeadAmount.setText(MoneyUtils.transMoneyFormat(payGetQRCodeViewModel.getAmount(), ApplicationConst.CURRENCY_CNY));
//            txtHeadInfo.setText("支付成功");

        }else if("1".equals(infoViewModel.getTranStatus())){
            mOperationResultView.updateHead(OperationResultHead.Status.FAIL,"支付失败");
//            txtHeadAmount.setText(MoneyUtils.transMoneyFormat(payGetQRCodeViewModel.getAmount(), ApplicationConst.CURRENCY_CNY));
//            txtHeadInfo.setText("支付失败");
        }else if("2".equals(infoViewModel.getTranStatus()) || "3".equals(infoViewModel.getTranStatus())){
            mOperationResultView.updateHead(OperationResultHead.Status.INPROGRESS,"银行处理中");
//            txtHeadAmount.setText(MoneyUtils.transMoneyFormat(payGetQRCodeViewModel.getAmount(), ApplicationConst.CURRENCY_CNY));
//            txtHeadInfo.setText("银行处理中");
        }

        mOperationResultView.addDetailRow("付款账户", NumberUtils.formatCardNumberStrong(accountNumber));
        mOperationResultView.addDetailRow("交易时间", infoViewModel.getTranTime());

        if ("0".equals(infoViewModel.getTranStatus())){
            mOperationResultView.addDetailRow("付款凭证", "可在支付商户扫码退款", false, false);

            //详情图片
            View detailView = View.inflate(mContext, R.layout.boc_view_qrpay_result_detail_info, null);
            ImageView imageView = (ImageView) detailView.findViewById(R.id.iv_qrpay_code_bar);
            setBarCodeToView(imageView, infoViewModel.getVoucherNum());
            mOperationResultView.getLayoutDetailParent().addView(detailView);
        }else if("1".equals(infoViewModel.getTranStatus())){
            mOperationResultView.addDetailRow("失败原因", infoViewModel.getErrMsg());
        }

//        //可能需要
//        mOperationResultView.addContentItem("开通小额免密",OPEN_FREE_PAY);
    }

    @Override
    public void setListener() {
//        // "您可能需要" 的点击事件
//        mOperationResultView.setOnclick(new BaseOperationResultView.BtnCallback() {
//            @Override
//            public void onClickListener(View v) {
//                switch (v.getId()){
//                    case OPEN_FREE_PAY:// 开通小额免密
//                        start(new QRPayFreePwdFragment());
//                        break;
//                }
//            }
//        });

        // "返回首页" 的点击事件
        mOperationResultView.setgoHomeOnclick(new OperationResultBottom.HomeBtnCallback() {
            @Override
            public void onHomeBack() {
                popToAndReInit(QRPayMainFragment.class);
            }
        });
    }

//    @Override
//    protected void titleLeftIconClick() {
//        popToAndReInit(QRPayCodeFragment.class);
//    }

    @Override
    public boolean onBack() {
//        popToAndReInit(QRPayCodeFragment.class);
        popToAndReInit(QRPayMainFragment.class);
        return false;
    }
       
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 设置条形码
     */
    private void setBarCodeToView(ImageView imageView, String code) {
        Bitmap barCodeBitmap = mBarCodeEncoder.creatBarcode(mContext, code);
        if (barCodeBitmap == null) {
            imageView.setImageBitmap(null);
            Toast.makeText(mContext, "条形码生成错误，请重试", Toast.LENGTH_SHORT).show();
        } else {
            imageView.setImageBitmap(barCodeBitmap);
        }

    }

    /**
     * 初始化条形码的参数
     */
    private void initCodeBuilder() {
        mBarCodeEncoder = new BarCodeEncoder.Builder()
                .setOutputBitmapWidth(ResUtils.dip2px(mContext, 300))
                .setOutputBitmapHeight(ResUtils.dip2px(mContext, 60))
                .setIsDisplayCodeText(true)
                .setDisplayCodeTextSize(14)
                .setIsDisplayCodeTextBold(true)
                .setIsFormatCodeNumber(true)
                .build();
    }
}