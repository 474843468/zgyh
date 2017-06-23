package com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.BaseDetailView;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.model.PsnFundQueryHistoryDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.presenter.HistoryTradeDetailPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

/**
 * Fragment：基金-交易记录-历史交易详情页面
 * Created by wy7105 on 2016/11/18
 */
public class HistoryTradeDetailFragment extends BussFragment implements HistoryTradeDetailContract.View {

    private View rootView;
    private BaseDetailView detailView;
    private HistoryTradeDetailContract.Presenter mHistoryTradeDetailPresenter;
    private PsnFundQueryHistoryDetailModel.ListEntity listEntity;

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_history_trade_detail, null);
        return rootView;
    }

    @Override
    public void initView() {
        detailView = (BaseDetailView) rootView.findViewById(R.id.details_view);
    }

    @Override
    public void initData() {
        mHistoryTradeDetailPresenter = new HistoryTradeDetailPresenter(this);
        listEntity = (PsnFundQueryHistoryDetailModel.ListEntity) getArguments().getSerializable("listEntity"); //获取列表页传过来的值
        String transData = listEntity.getTransDate().format(DateFormatters.dateFormatter1); //获取交易日期
        String headTitle = String.format(getString(R.string.boc_fund_amount),
                DataUtils.getCurrencyDescByLetter(mContext, "CNY"));
        String amount =
                MoneyUtils.transMoneyFormat(listEntity.getApplyAmount(), "CNY");
        String ramark = getRemark(listEntity);
        detailView.updateHeadData(headTitle, amount);
        detailView.updateHeadDetail(getString(R.string.boc_finance_account_transfer_date), transData);
        String tranTypeDes = DataUtils.getTransTypeDes(mContext, listEntity.getTransCode());
        String code = mContext.getString(R.string.boc_fund_record_code, listEntity.getFundCode());
        String transState = DataUtils.getTransStatusDes(mContext, listEntity.getTransStatus());
        detailView.addTextCntent(getString(R.string.boc_fund_name_notice), listEntity.getFundName(), code, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }); //基金名称+代码
        detailView.addDetailRow(getString(R.string.boc_fundstate_applystutas), transState);//交易状态
        detailView.addDetailRow(getString(R.string.boc_wealth_fail_reason), listEntity.getFailReason());//失败原因（动态显示）
        detailView.addDetailRow(getString(R.string.boc_fundstate_applyType), tranTypeDes);//交易类型
        detailView.addDetailRow(getString(R.string.boc_fundstate_applycunt), listEntity.getApplyCount());//交易份额
        detailView.addDetailRow(getString(R.string.boc_fundstate_dPrice), listEntity.getNetValue());//单位净值
        detailView.addDetailRow(getString(R.string.boc_fund_trans_fee), listEntity.getTransFee());//交易费用
        detailView.addDetailRow(getString(R.string.boc_fund_remark), ramark);//备注
    }

    private String getRemark(PsnFundQueryHistoryDetailModel.ListEntity listEntity) {
        String content = "";
        if ("2".equals(listEntity.getSpecialTransFlag()))
            content = getString(R.string.boc_fund_appoint_date) + listEntity.getAppointDate();
        if (DataUtils.TRANS_TYPE_SET_BONUS.contains(listEntity.getTransCode()))
            content = getString(R.string.boc_fund_modify_bonus_Type) + listEntity.getBonusType();
        if (DataUtils.TRANS_TYPE_FUND_CONVERT.contains(listEntity.getTransCode()))
            content = getString(R.string.boc_fund_inFnname) + listEntity.getInFnname() + "。" + getString(R.string.boc_fund_inFncount) + listEntity.getConfirmCount();
        if (DataUtils.TRANS_TYPE_FUND_ACCOUNT_MANAGEMENT.contains(listEntity.getTransCode()))
            content = getString(R.string.boc_fund_TA_account) + listEntity.getTaAccount();
        return content;
    }

    @Override
    public void setListener() {
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_fund_details);
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
    public void onDestroy() {
        mHistoryTradeDetailPresenter.unsubscribe();
        super.onDestroy();
    }

}