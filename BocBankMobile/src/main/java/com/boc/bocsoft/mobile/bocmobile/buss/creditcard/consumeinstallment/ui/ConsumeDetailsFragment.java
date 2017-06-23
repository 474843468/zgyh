package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableHead;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.model.ConsumeInstallmentModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.model.ConsumeTransModel;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

/**
 * 消费分期——明细
 */

public class ConsumeDetailsFragment extends BussFragment{

    public static final String DETAIL_INFO="tans_detail_info_bean";

    private View rootView;
    private DetailTableHead detailTableHead;
    private DetailContentView detailContentView;
    private ConsumeTransModel.ConsumeBean transDetail;
    private Button btnNext;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_crcd_billdetail_info, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();

        detailTableHead = (DetailTableHead) rootView.findViewById(R.id.head_view);
        detailContentView = (DetailContentView) rootView.findViewById(R.id.body_view);

        btnNext=(Button)rootView.findViewById(R.id.btn_next);

    }

    @Override
    public void initData() {
        super.initData();

        transDetail=getArguments().getParcelable(DETAIL_INFO);
        if (transDetail==null)
            return;

        detailTableHead.updateData("交易金额" +
                        "（" + PublicCodeUtils.getCurrency(mContext, transDetail.getTranCurrency()) + "）",
                MoneyUtils.transMoneyFormat(transDetail.getTranAmount(), transDetail.getTranCurrency()));

        detailTableHead.addDetail("交易日期",transDetail.getTransDate());


        detailContentView.addDetailRow("信用卡号", NumberUtils.formatCardNumber(transDetail.getAcctNum()));
        detailContentView.addDetailRow("交易描述",transDetail.getTransDesc());
        detailContentView.addDetailRow("交易金额",MoneyUtils.transMoneyFormat(transDetail.getTranAmount(),transDetail.getTranCurrency()));


    }

    @Override
    public void setListener() {
        super.setListener();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConsumeInstallmentModel consumeInstallmentModel = new ConsumeInstallmentModel();
                consumeInstallmentModel.setAmount(transDetail.getTranAmount());
                consumeInstallmentModel.setCurrencyCode(transDetail.getTranCurrency());//应该从明细页面获得
                consumeInstallmentModel.setMainAcctId(transDetail.getMainAcctId());//账户id
                consumeInstallmentModel.setTransId(transDetail.getTransId());//交易序号
                consumeInstallmentModel.setSequence(transDetail.getSequence());//周期号
                start(ConsumeInstallmentFragment.getInstance(consumeInstallmentModel,(AccountBean) getArguments().getParcelable(ConsumeQryFragment.ACCOUNT_BEAN)));
            }
        });
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return true;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_transfer_details_title);
    }
}
