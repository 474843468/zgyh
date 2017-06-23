package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.havebills.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableHead;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.havebills.model.CrcdBilledDetailModel;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

/**
 * 历史账单——明细
 * Created by liuweidong on 2016/12/14.
 */

public class CrcdBillHistoryDetailsFragment  extends BussFragment {

    public static final String DETAIL_INFO="tans_detail_info_bean";

    private View rootView;
    private DetailTableHead detailTableHead;
    private DetailContentView detailContentView;
    private CrcdBilledDetailModel.TransListBean transDetail;

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

        String loanSign;
        if ("DEBT".equals(transDetail.getLoanSign()))
            loanSign="消费金额";
        else if("CRED".equals(transDetail.getLoanSign()))
            loanSign="存入金额";
        else
            loanSign="支出金额";


        detailTableHead.updateData(loanSign +
                        "（" + PublicCodeUtils.getCurrency(mContext, transDetail.getDealCcy()) + "）",
                MoneyUtils.transMoneyFormat(transDetail.getBalCnt(), transDetail.getDealCcy()));

        detailTableHead.addDetail("交易日期",transDetail.getDealDt());


        detailContentView.addDetailRow("信用卡号", NumberUtils.formatCardNumber(transDetail.getDealCardId()));
        detailContentView.addDetailRow("交易描述",transDetail.getDealDesc());
        detailContentView.addDetailRow("记账日期",transDetail.getCheckDt());


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
