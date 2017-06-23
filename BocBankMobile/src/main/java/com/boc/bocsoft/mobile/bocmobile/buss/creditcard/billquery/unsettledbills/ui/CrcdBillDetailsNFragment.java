package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.unsettledbills.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableHead;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.unsettledbills.model.CrcdUnsettledBillDetailModel;

/**
 * 未出账单——明细
 * Created by liuweidong on 2016/12/14.
 */

public class CrcdBillDetailsNFragment extends BussFragment{

    public static final String DETAIL_INFO="tans_detail_info_bean";

    private View rootView;
    private DetailTableHead detailTableHead;
    private DetailContentView detailContentView;
    private CrcdUnsettledBillDetailModel.CrcdTransactionListBean transDetail;

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

    }

    @Override
    public void initData() {
        super.initData();

        transDetail=getArguments().getParcelable(DETAIL_INFO);
        if (transDetail==null)
            return;

        detailTableHead.updateData("记账金额" +
                        "（" + PublicCodeUtils.getCurrency(mContext, transDetail.getBookCurrency()) + "）",
                MoneyUtils.transMoneyFormat(transDetail.getBookAmount(), transDetail.getBookCurrency()));

        detailTableHead.addDetail("记账日期",transDetail.getBookDate());


        detailContentView.addDetailRow("信用卡号后4位", transDetail.getCardNumberTail());
        detailContentView.addDetailRow("交易类型","CRED".equals(transDetail.getDebitCreditFlag())?"存入":"支出");
        detailContentView.addDetailRow("交易描述",transDetail.getRemark());
        detailContentView.addDetailRow("交易金额",MoneyUtils.transMoneyFormat(transDetail.getTranAmount(),transDetail.getTranCurrency()));
        detailContentView.addDetailRow("交易日期",transDetail.getTransDate());


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
