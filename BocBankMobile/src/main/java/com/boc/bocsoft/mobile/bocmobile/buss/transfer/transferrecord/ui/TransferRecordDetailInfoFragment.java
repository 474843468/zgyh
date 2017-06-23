package com.boc.bocsoft.mobile.bocmobile.buss.transfer.transferrecord.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.ResultStatusConst;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRowGroup;
import com.boc.bocsoft.mobile.bocmobile.base.widget.shareinfoview.ShareInfoFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transferrecord.model.RemitReturnInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transferrecord.model.TransferRecordDetailInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.ResultStatusUtils;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

/**
 * 转账记录查询 -- 详情页面
 * Created by wangf on 2016/6/28
 */
public class TransferRecordDetailInfoFragment extends BussFragment {

    private View rootView;
    private DetailTableHead detailTableHead;
    private DetailContentView detailContentView;
    private DetailTableRowGroup detailTableRowGroup;

    //底部按钮
    private TextView btnBottomShare;

    /**
     * 详情数据
     */
    private TransferRecordDetailInfoViewModel detailsInfoViewModel;
    private RemitReturnInfoViewModel returnInfoViewModel;

    //是否有退汇信息
    private boolean haveReturn;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_account_transdetail_info, null);
        Bundle bundle = getArguments();
        haveReturn = bundle.getBoolean("HaveReturn", false);
        if (haveReturn) {
            returnInfoViewModel = bundle.getParcelable("DetailsInfo");
            detailsInfoViewModel = returnInfoViewModel.getDetailInfoViewModel();
        } else {
            detailsInfoViewModel = bundle.getParcelable("DetailsInfo");
        }
        return rootView;
    }

    @Override
    public void initView() {
        detailTableHead = (DetailTableHead) rootView.findViewById(R.id.head_view);
        detailContentView = (DetailContentView) rootView.findViewById(R.id.body_view);
        detailTableRowGroup = (DetailTableRowGroup) rootView.findViewById(R.id.bottom_view);
        btnBottomShare = (TextView) rootView.findViewById(R.id.bottom_button_share);
//        detailTableRowGroup.setVisibility(View.GONE);
    }


    @Override
    public void initData() {

        // 当转账状态为 A 并且 转账类型不为 01 时，显示分享按钮
        if (ResultStatusConst.STATUS_A.equals(detailsInfoViewModel.getStatus()) && !"01".equals(detailsInfoViewModel.getTransferType())) {
            btnBottomShare.setVisibility(View.VISIBLE);
            btnBottomShare.setText(getResources().getString(R.string.boc_common_notify));
        }

        detailTableHead.updateData(getResources().getString(R.string.boc_transfer_trans_record_detail_amount) +
                        "(" + PublicCodeUtils.getCurrency(mContext, detailsInfoViewModel.getFeeCur()) +
                        ResultStatusUtils.getStrCashRemit(mContext, detailsInfoViewModel.getCashRemit()) + ")",
                MoneyUtils.transMoneyFormat(detailsInfoViewModel.getAmount(), detailsInfoViewModel.getFeeCur()));
        String status = ResultStatusUtils.getTransferStatus(mContext, false, detailsInfoViewModel.getStatus());
        detailTableHead.setHeadStatus(status, ResultStatusUtils.changeTextBackground(status));
        if (StringUtils.isEmptyOrNull(detailsInfoViewModel.getCommissionCharge())) {
            detailTableHead.addDetail(getResources().getString(R.string.boc_transfer_trans_record_detail_cost), "");
        } else {
            detailTableHead.addDetail(getResources().getString(R.string.boc_transfer_trans_record_detail_cost), PublicCodeUtils.getCurrency(mContext, detailsInfoViewModel.getFeeCur()) + " " + MoneyUtils.transMoneyFormat(detailsInfoViewModel.getCommissionCharge(), detailsInfoViewModel.getFeeCur()));
        }

        if (!StringUtils.isEmpty(detailsInfoViewModel.getPayeeAccountName())) {
            detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_trans_record_detail_payee_name), detailsInfoViewModel.getPayeeAccountName());
        }
        if (!StringUtils.isEmpty(detailsInfoViewModel.getPayeeAccountNumber())) {
            detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_trans_record_detail_payee_account), NumberUtils.formatCardNumberStrong(detailsInfoViewModel.getPayeeAccountNumber()));
        }

        if (!StringUtils.isEmpty(detailsInfoViewModel.getPayeeBankName())) {
            detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_trans_record_detail_payee_bank_name), detailsInfoViewModel.getPayeeBankName());
        }
        if (!StringUtils.isEmpty(detailsInfoViewModel.getFurInfo())) {
            detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_comment), detailsInfoViewModel.getFurInfo());
        }
        if (!StringUtils.isEmpty(detailsInfoViewModel.getReturnCode())) {
            if (ResultStatusConst.STATUS_B.equals(detailsInfoViewModel.getStatus()) || ResultStatusConst.STATUS_12.equals(detailsInfoViewModel.getStatus())) {
                detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_trans_record_detail_fail_cause), detailsInfoViewModel.getReturnCode());
            }
        }

        if (!StringUtils.isEmpty(detailsInfoViewModel.getPayerAccountNumber())) {
            detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_trans_record_detail_payer_account), detailsInfoViewModel.getPayerAccountName() + " " + NumberUtils.formatCardNumber(detailsInfoViewModel.getPayerAccountNumber()));
        }
        if (!StringUtils.isEmpty(detailsInfoViewModel.getChannel())) {
            detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_withdrawal_query_details_channel), ResultStatusUtils.getTransChannel(mContext, detailsInfoViewModel.getChannel()));
        }
        if (!StringUtils.isEmpty(detailsInfoViewModel.getTransactionId())) {
            detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_trans_record_detail_transaction_id), detailsInfoViewModel.getTransactionId());
        }
        if (detailsInfoViewModel.getPaymentDate() != null) {
            detailContentView.addDetailRowNotLine("交易日期", detailsInfoViewModel.getPaymentDate().format(DateFormatters.dateFormatter1));
        }

        //以下退汇信息在有退汇交易时出现
        if (haveReturn) {
            detailTableRowGroup.setVisibility(View.VISIBLE);
            detailTableRowGroup.setGroupTitle(getResources().getString(R.string.boc_transfer_trans_record_detail_return_info));
            if (!StringUtils.isEmpty(returnInfoViewModel.getReexchangeAmount())) {
                detailTableRowGroup.addDetailTabRow(getResources().getString(R.string.boc_transfer_trans_record_detail_return_amount), PublicCodeUtils.getCurrency(mContext, detailsInfoViewModel.getFeeCur()) + " "
                        + MoneyUtils.transMoneyFormat(returnInfoViewModel.getReexchangeAmount(), detailsInfoViewModel.getFeeCur()));
            }
            if (!StringUtils.isEmpty(returnInfoViewModel.getReexchangeInfo())) {
                detailTableRowGroup.addDetailTabRow(getResources().getString(R.string.boc_transfer_trans_record_detail_return_cause), returnInfoViewModel.getReexchangeInfo());
            }
            if (returnInfoViewModel.getReexchangeDate() != null) {
                detailTableRowGroup.addDetailTabRow(getResources().getString(R.string.boc_transfer_trans_record_detail_return_date), returnInfoViewModel.getReexchangeDate().format(
                        DateFormatters.dateFormatter1));
            }
            if (!StringUtils.isEmpty(returnInfoViewModel.getRemittanceInfo())) {
                detailTableRowGroup.addDetailTabRow(getResources().getString(R.string.boc_transfer_comment), returnInfoViewModel.getRemittanceInfo());
            }

//            detailTableRowGroup.addDetailTabRow("转出账号", NumberUtils.formatCardNumber(returnInfoViewModel.getPayerActno()));
//            detailTableRowGroup.addDetailRowNotLine(getResources().getString(R.string.boc_transfer_trans_record_detail_return_account), NumberUtils.formatCardNumber(returnInfoViewModel.getPayeeActno()));
            if (!StringUtils.isEmpty(returnInfoViewModel.getPayerActno())) {
                detailTableRowGroup.addDetailRowNotLine(getResources().getString(R.string.boc_transfer_trans_record_detail_return_account), NumberUtils.formatCardNumber(returnInfoViewModel.getPayerActno()));
            }
        } else {
            detailTableRowGroup.setVisibility(View.GONE);
        }
    }


    @Override
    public void setListener() {
        btnBottomShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] moneyInfo = {"转账金额", MoneyUtils.transMoneyFormat(detailsInfoViewModel.getAmount(), detailsInfoViewModel.getFeeCur()) + "元"};
                ShareInfoFragment fragment = ShareInfoFragment.newInstance("转账交易成功", collectionName(), collectionValue(), moneyInfo);
//                fragment.setOther(true);
                start(fragment);
            }
        });
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
     * 名称
     *
     * @return
     */
    private String[] collectionName() {
        String[] name = new String[8];
        int index = 0;
        name[index++] = "交易日期";
        name[index++] = "收款人名称";
        name[index++] = "收款账号";
        name[index++] = "收款银行";
        name[index++] = "付款人名称";
        name[index++] = "付款账号";
        name[index++] = "附言";
        name[index++] = "交易序号";
        return name;
    }

    /**
     * 数据
     *
     * @return
     */
    private String[] collectionValue() {
        String[] value = new String[8];
        int index = 0;
        value[index++] = detailsInfoViewModel.getPaymentDate().format(DateFormatters.dateFormatter1);
        value[index++] = detailsInfoViewModel.getPayeeAccountName();
        value[index++] = NumberUtils.formatCardNumber2(detailsInfoViewModel.getPayeeAccountNumber());
        value[index++] = detailsInfoViewModel.getPayeeBankName();
        value[index++] = detailsInfoViewModel.getPayerAccountName();
        value[index++] = NumberUtils.formatCardNumberStrong(detailsInfoViewModel.getPayerAccountNumber());
        value[index++] = detailsInfoViewModel.getFurInfo();
        value[index++] = detailsInfoViewModel.getTransactionId();
        return value;
    }

//    @Override
//    protected void titleLeftIconClick() {
//        popToAndReInit(TransferRecordFragment.class);
//    }

    @Override
    public boolean onBack() {
        popToAndReInit(TransferRecordFragment.class);
        return false;
    }

}
