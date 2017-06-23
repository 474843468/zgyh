package com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.BaseDetailView;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.model.PsnFundQueryTransOntranModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.trademanagement.presenter.TransitTradeDetailPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

/**
 * Fragment：基金-交易记录-在途交易详情页面
 * Created by wy7105 on 2016/11/18
 */
public class TransitTradeDetailFragment extends BussFragment implements TransitTradeDetailContract.View {

    private View rootView;
    private BaseDetailView detailView;
    private Button btnfundcancel = null; //撤单按钮
    private TransitTradeDetailContract.Presenter mTransitTradeDetailPresenter;
    private PsnFundQueryTransOntranModel model;
    private PsnFundQueryTransOntranModel.ListBean listBean;


    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_transit_trade_detail, null);
        return rootView;
    }

    @Override
    public void initView() {
        detailView = (BaseDetailView) rootView.findViewById(R.id.details_view);
        btnfundcancel = (Button) rootView.findViewById(R.id.btn_fund_cancel);
        btnfundcancel.setVisibility(View.INVISIBLE);
    }

    @Override
    public void initData() {
        mTransitTradeDetailPresenter = new TransitTradeDetailPresenter(this);
        listBean = (PsnFundQueryTransOntranModel.ListBean) getArguments().getSerializable("listBean"); //获取列表页传过来的值
        String paymentData = listBean.getPaymentDate().format(DateFormatters.dateFormatter1); //获取委托日期
        String headTitle = String.format(getString(R.string.boc_fund_amount),
                DataUtils.getCurrencyDescByLetter(mContext, "CNY"));
        String amount =
                MoneyUtils.transMoneyFormat(listBean.getTransAmount(), "CNY");
        String tranTypeDes = DataUtils.getTransTypeDes(mContext, listBean.getFundTranType());
        String ramark = getRemark(listBean);
        String code = mContext.getString(R.string.boc_fund_record_code, listBean.getFundCode());
        detailView.updateHeadData(headTitle, amount);
        detailView.updateHeadDetail(getString(R.string.boc_fundstate_applystutas), listBean.getTransStatus());//交易状态
        detailView.addTextCntent(getString(R.string.boc_fund_name_notice), listBean.getFundName(), code, true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        detailView.addDetailRow(getString(R.string.boc_wealth_entrust_date), paymentData);//委托日期
        detailView.addDetailRow(getString(R.string.boc_fundstate_applyType), tranTypeDes);//交易类型
        detailView.addDetailRow(getString(R.string.boc_fund_remark), ramark);//备注
        if ("Y".equals(listBean.getCancleFlag())) btnfundcancel.setVisibility(View.VISIBLE);
    }

    @Override
    public void setListener() {

        btnfundcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("listBean", listBean);
                TransitTradeCancelConfirmFragment transitTradeCancelConfirmFragment = new TransitTradeCancelConfirmFragment(model);
                transitTradeCancelConfirmFragment.setArguments(bundle);
                start(transitTradeCancelConfirmFragment);
            }
        });

    }

    private String getRemark(PsnFundQueryTransOntranModel.ListBean bean) {
        String content = "";
        if ("2".equals(bean.getSpecialTransFlag()))
            content = getString(R.string.boc_fund_appoint_date) + bean.getAppointDate();
        if (DataUtils.TRANS_TYPE_SET_BONUS.contains(bean.getFundTranType()))
            content = getString(R.string.boc_fund_modify_bonus_Type) + bean.getBonusType();
        if (DataUtils.TRANS_TYPE_FUND_CONVERT.contains(bean.getFundTranType()))
            content = getString(R.string.boc_fund_inFnname) + bean.getInFundName();
        if (DataUtils.TRANS_TYPE_FUND_ACCOUNT_MANAGEMENT.contains(bean.getFundTranType()))
            content = getString(R.string.boc_fund_TA_account) + bean.getTaAccountNo();
        return content;
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
        mTransitTradeDetailPresenter.unsubscribe();
        super.onDestroy();
    }

}