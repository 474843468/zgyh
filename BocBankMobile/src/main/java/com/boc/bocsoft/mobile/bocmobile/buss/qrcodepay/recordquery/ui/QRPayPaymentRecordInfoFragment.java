package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.recordquery.ui;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRowGroup;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.recordquery.model.QRPayPaymentRecordViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.utils.QrCodeStatusUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.ResultStatusUtils;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.utils.ResUtils;
import com.boc.bocsoft.mobile.framework.zxing.encode.BarCodeEncoder;

/**
 * 二维码支付 - 支付记录 - 明细
 * Created by wangf on 2016/8/27
 */
public class QRPayPaymentRecordInfoFragment extends BussFragment {

    private View rootView;
    private DetailTableHead detailTableHead;
    private DetailContentView detailContentView;
    private DetailTableRowGroup detailTableRowGroup;
    private ImageView ivDetailInfoBarCode;

    //条形码的构建
    private BarCodeEncoder mBarCodeEncoder;

    //交易记录信息
    private QRPayPaymentRecordViewModel.ListBean recordListBean;
    //用户选择的账户
    private AccountBean selectAccountBean;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_account_transdetail_info, null);
        recordListBean = getArguments().getParcelable(QRPayPaymentRecordFragment.QRPAY_RECORD_INFO);
        selectAccountBean = getArguments().getParcelable(QRPayPaymentRecordFragment.QRPAY_RECORD_ACCOUNT);
        return rootView;
    }

    @Override
    public void initView() {
        detailTableHead = (DetailTableHead) rootView.findViewById(R.id.head_view);
        detailContentView = (DetailContentView) rootView.findViewById(R.id.body_view);
        detailTableRowGroup = (DetailTableRowGroup) rootView.findViewById(R.id.bottom_view);
        ivDetailInfoBarCode = (ImageView) rootView.findViewById(R.id.iv_detail_info);
        ivDetailInfoBarCode.setVisibility(View.GONE);
        ivDetailInfoBarCode.setPadding(0, 40, 0, 40);
        detailTableRowGroup.setVisibility(View.GONE);

        initCodeBuilder();
    }

    @Override
    public void initData() {
        if ("01".equals(recordListBean.getType())){//支付
            setC2BViewData();
        }else if("02".equals(recordListBean.getType())){//转账
            setC2CViewData();
        }
    }

    @Override

    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_transfer_details_title);
    }


    /**
     * 设置C2B页面数据
     */
    private void setC2BViewData(){
        detailTableHead.updateData("付款金额（人民币元）", MoneyUtils.transMoneyFormat(recordListBean.getTranAmount(), ApplicationConst.CURRENCY_CNY));
        String status = QrCodeStatusUtils.getResultStatus(recordListBean.getTranStatus());
        detailTableHead.setHeadStatus(status, ResultStatusUtils.changeTextBackground(status));
        detailTableHead.addDetail("商户名称", recordListBean.getMerchantName());

        detailContentView.addDetailRow("付款账户", NumberUtils.formatCardNumberStrong(selectAccountBean.getAccountNumber()));
        detailContentView.addDetailRow("交易时间", recordListBean.getTranTime().format(DateFormatters.dateAndTimeFormatter));

        if("01".equals(recordListBean.getTransferType())){//正扫
            // 交易成功时显示付款凭证号
            if("0".equals(recordListBean.getTranStatus())){
                detailContentView.addDetailRow("付款凭证", recordListBean.getVoucherNum());
                ivDetailInfoBarCode.setVisibility(View.GONE);
            }else{
                ivDetailInfoBarCode.setVisibility(View.GONE);
            }
        }else if("02".equals(recordListBean.getTransferType())){//反扫

            if ("1".equals(recordListBean.getTranRemark()) || "2".equals(recordListBean.getTranRemark())){//退款交易
                detailContentView.addDetailRow("交易说明", QrCodeStatusUtils.getTranRemark(recordListBean.getTranRemark()));
                if("0".equals(recordListBean.getTranStatus())){
                    detailContentView.addDetailRowNotAllLine("付款凭证", recordListBean.getVoucherNum());
                    ivDetailInfoBarCode.setVisibility(View.GONE);
                }
            }else{
                // 交易成功时显示付款凭证号
                if("0".equals(recordListBean.getTranStatus())){
                    detailContentView.addDetailRowNotAllLine("付款凭证", "可在支付商户扫码退款");
                    setBarCodeToView(recordListBean.getVoucherNum());
                    ivDetailInfoBarCode.setVisibility(View.VISIBLE);
                }else{
                    ivDetailInfoBarCode.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     *  设置C2C页面数据
     */
    private void setC2CViewData(){
        if ("02".equals(recordListBean.getTransferType())){//转出
            detailTableHead.updateData("付款金额（人民币元）", MoneyUtils.transMoneyFormat(recordListBean.getTranAmount(), ApplicationConst.CURRENCY_CNY));
            String status = QrCodeStatusUtils.getResultStatus(recordListBean.getTranStatus());
            detailTableHead.setHeadStatus(status, ResultStatusUtils.changeTextBackground(status));
            detailTableHead.addDetail("收款方", recordListBean.getPayeeName());

            detailContentView.addDetailRow("付款账户", NumberUtils.formatCardNumberStrong(selectAccountBean.getAccountNumber()));
//            detailContentView.addDetailRow("付款账户", NumberUtils.formatCardNumberStrong(recordListBean.getPayerAccNo()));
            detailContentView.addDetailRow("交易时间", recordListBean.getTranTime().format(DateFormatters.dateAndTimeFormatter));
            detailContentView.addDetailRow("交易流水号", recordListBean.getTranSeq());
        }else if("01".equals(recordListBean.getTransferType())){//转入
            detailTableHead.updateData("收款金额（人民币元）", MoneyUtils.transMoneyFormat(recordListBean.getTranAmount(), ApplicationConst.CURRENCY_CNY));
            String status = QrCodeStatusUtils.getResultStatus(recordListBean.getTranStatus());
            detailTableHead.setHeadStatus(status, ResultStatusUtils.changeTextBackground(status));
            detailTableHead.addDetail("付款方", recordListBean.getPayerName());

            detailContentView.addDetailRow("收款账户", NumberUtils.formatCardNumberStrong(selectAccountBean.getAccountNumber()));
//            detailContentView.addDetailRow("收款账户", NumberUtils.formatCardNumberStrong(recordListBean.getPayeeAccNo()));
            detailContentView.addDetailRow("交易时间", recordListBean.getTranTime().format(DateFormatters.dateAndTimeFormatter));
            detailContentView.addDetailRow("交易流水号", recordListBean.getTranSeq());
            if(!StringUtils.isEmptyOrNull(recordListBean.getPayeeComments())){
                detailContentView.addDetailRow("收款备注", recordListBean.getPayeeComments());
            }
        }
    }



    /**
     * 设置页面条形码
     */
    private void setBarCodeToView(String code) {
        Bitmap barCodeBitmap = mBarCodeEncoder.creatBarcode(mContext, code);
        if (barCodeBitmap == null) {
            ivDetailInfoBarCode.setImageBitmap(null);
            Toast.makeText(mContext, "条形码生成错误，请重试", Toast.LENGTH_SHORT).show();
        } else {
            ivDetailInfoBarCode.setImageBitmap(barCodeBitmap);
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
