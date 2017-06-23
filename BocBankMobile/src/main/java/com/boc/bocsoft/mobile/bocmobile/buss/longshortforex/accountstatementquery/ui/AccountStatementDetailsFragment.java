package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountstatementquery.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.BaseDetailView;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountstatementquery.model.AccountStatementQueryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.TransQueryUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

/**
 * Created by wjk7118 on 2016/11/29.
 * 双向宝--对账单详情
 */
public class AccountStatementDetailsFragment extends BussFragment  {
    private View rootView;
    private BaseDetailView detailView;
    private TransQueryUtils mTransQueryUtils;
    private String settleCurrency;


    private AccountStatementQueryModel.AccountStatementQueryBean accountStatementQueryBean;

    /**
     *
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_account_statement_details, null);
        return rootView;
    }
    @Override
    public void initView() {
        detailView = (BaseDetailView) rootView.findViewById(R.id.details_view);
    }
    @Override
    public void initData() {
        mTransQueryUtils = new TransQueryUtils();
        AccountStatementQueryModel model=new AccountStatementQueryModel();
        accountStatementQueryBean = getArguments().getParcelable("accountStatementQueryBean");
        settleCurrency=getArguments().getString("settleCurrency");

        String headTitle = String.format(getString(R.string.boc_long_short_forex_transfer_amount));
        String amount = MoneyUtils.transMoneyFormat(accountStatementQueryBean.getTransferAmount(),
                        PublicCodeUtils.getCurrency(mActivity, settleCurrency));
        detailView.updateHeadData(headTitle, amount);

        detailView.updateHeadDetail(getString(R.string.boc_long_short_forex_transfer_slogan),
                mTransQueryUtils.gettransferDir(accountStatementQueryBean.getTransferDir()));

        detailView.addDetailRow(getString(R.string.boc_long_short_forex_funds_move_number),accountStatementQueryBean.getFundSeq());//资金变动序号
        detailView.addDetailRow(getString(R.string.boc_long_short_forex_settle_currency),PublicCodeUtils.getCurrency(mActivity, settleCurrency));//结算币种
        if(!settleCurrency.equals("001")){
            detailView.addDetailRow(getString(R.string.boc_long_short_forex_money_remit),mTransQueryUtils.getcashRemit(accountStatementQueryBean.getNoteCashFlag()));//钞汇标识
            }
        detailView.addDetailRow(getString(R.string.boc_long_short_forex_transfer_type),mTransQueryUtils.getFundTransferType(accountStatementQueryBean.getFundTransferType()));//转账类型
        detailView.addDetailRow(getString(R.string.boc_long_short_forex_account_remian),accountStatementQueryBean.getBalance());//账户余额
        detailView.addDetailRow(getString(R.string.boc_long_short_forex_transfer_time),accountStatementQueryBean.getTransferDate().format(DateFormatters.dateFormatter1));//交易日期
    }


    @Override
    public void setListener() {
    }


    @Override
    protected void titleLeftIconClick() {
        super.titleLeftIconClick();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected String getTitleValue() {
        return "明细";
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }


    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

}
