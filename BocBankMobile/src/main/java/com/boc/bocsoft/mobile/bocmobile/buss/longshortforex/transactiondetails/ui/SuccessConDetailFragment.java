package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTradeDetailQuery.PsnVFGTradeDetailQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRow;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.TransQueryUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model.XpadVFGTradeDetailQueryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model.XpadVFGTradeInfoQueryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.presenter.SuccessConDetailPresenter;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

/**
 * 双向宝交易查询--成交状况查询详情界面
 * Created by zc on 2016/11/17
 */
public class SuccessConDetailFragment extends BussFragment implements SuccessConDetailContract.View {

    private View rootView;
    private SuccessConDetailContract.Presenter mSuccessConDetailPresenter;
    private ScrollView s_detail_scrollview;
    private TextView tt_state;//状态
    private TextView tt_amount;//交易金额
    private TextView pay_currency;//货币标识
    private TextView currency_unit;//金额单位
    private DetailTableRow dt_currency_coupe;//货币对
    private DetailTableRow dt_detail_transeq;//委托序号
    private DetailTableRow dt_detail_currency;//结算币种
    private DetailTableRow dt_detail_buy_direction;//买卖方向
    private DetailTableRow dt_detail_slogan;//建仓标识
    private DetailTableRow dt_detail_entrust_rate;//委托汇率
    private DetailTableRow dt_detail_success_rate;//成交汇率
    private DetailTableRow dt_detail_entrust_type;//委托类型
    private DetailTableRow dt_detail_success_type;//成交类型
    private DetailTableRow dt_detail_entrust_date;//委托日期
    private DetailTableRow dt_detail_lose_date;//失效时间

    //查询到的数据实体
    private XpadVFGTradeInfoQueryModel.XpadPsnVFGTradeInfoQueryResultEntity mTradeInfoQueryResultEntity;

    private String conversationId;
    private int selectPagePosition;
    TransQueryUtils mTransQueryUtils;

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_success_con_detail, null);
        return rootView;
    }

    @Override
    public void initView() {
        s_detail_scrollview = (ScrollView) rootView.findViewById(R.id.detail_scroll);
        pay_currency= (TextView) rootView.findViewById(R.id.pay_currency);
        currency_unit = (TextView) rootView.findViewById(R.id.currency_unit);
        tt_amount = (TextView) rootView.findViewById(R.id.pay_amount);
        tt_state = (TextView) rootView.findViewById(R.id.pay_state);
        dt_currency_coupe = (DetailTableRow) rootView.findViewById(R.id.currency_coupe);
        dt_detail_transeq = (DetailTableRow) rootView.findViewById(R.id.detail_transeq);
        dt_detail_currency = (DetailTableRow) rootView.findViewById(R.id.detail_currency);
        dt_detail_buy_direction = (DetailTableRow) rootView.findViewById(R.id.detail_buy_direction);
        dt_detail_slogan = (DetailTableRow) rootView.findViewById(R.id.detail_slogan);
        dt_detail_entrust_rate = (DetailTableRow) rootView.findViewById(R.id.detail_entrust_rate);
        dt_detail_success_rate = (DetailTableRow) rootView.findViewById(R.id.detail_success_rate);
        dt_detail_entrust_type = (DetailTableRow) rootView.findViewById(R.id.detail_entrust_type);
        dt_detail_success_type = (DetailTableRow) rootView.findViewById(R.id.detail_success_type);
        dt_detail_entrust_date = (DetailTableRow) rootView.findViewById(R.id.detail_entrust_date);
        dt_detail_lose_date = (DetailTableRow) rootView.findViewById(R.id.detail_lose_date);
        s_detail_scrollview.setVisibility(View.INVISIBLE);
    }

    @Override
    public void initData() {
        mSuccessConDetailPresenter = new SuccessConDetailPresenter(this);
        XpadVFGTradeDetailQueryModel model = new XpadVFGTradeDetailQueryModel();
        mTransQueryUtils = new TransQueryUtils();
        mTradeInfoQueryResultEntity = getArguments().getParcelable("xpadTradeInfoQueryResultEntity");
        conversationId = getArguments().getString("conversationId");
        selectPagePosition = getArguments().getInt("selectPagePosition", -1);

        model.setConversationId(conversationId);
        model.setVfgTransactionId(mTradeInfoQueryResultEntity.getConsignNumber());
        model.setInternalSeq(mTradeInfoQueryResultEntity.getInternalSeq());
        mSuccessConDetailPresenter.psnXpadSuccessConDetailQuery(model);

        String currencyType = mTransQueryUtils.getCurrencyType(mTradeInfoQueryResultEntity.getCurrency1().getCode());
        pay_currency.setText(getResources().getString(R.string.boc_long_short_forex_pay_amount)+currencyType);

        String currencyUnit = mTransQueryUtils.getCurrencyUnit(mTradeInfoQueryResultEntity.getCurrency1().getCode(),mTradeInfoQueryResultEntity.getCurrency2().getCode());
        if (currencyUnit.equals(mTradeInfoQueryResultEntity.getCurrency1().getCode())){
            currencyUnit = "";
        }
        tt_amount.setText(MoneyUtils.transMoneyFormat(mTradeInfoQueryResultEntity.getAmount(),mTradeInfoQueryResultEntity.getSettleCurrecny()));
        currency_unit.setText(currencyUnit);

        if("R".equals(mTradeInfoQueryResultEntity.getOrderStatus())||"S".equals(mTradeInfoQueryResultEntity.getOrderStatus())){
            tt_state.setText("成交");
            tt_state.setBackgroundDrawable(getResources().getDrawable(R.drawable.boc_transaction_status_bg_green));
        }else if ("X".equals(mTradeInfoQueryResultEntity.getOrderStatus())){
            tt_state.setText("失败");
            tt_state.setBackgroundDrawable(getResources().getDrawable(R.drawable.boc_transaction_status_bg_red));
        }

        dt_currency_coupe.updateValue(PublicCodeUtils.getGoldCurrencyCode(mActivity,mTradeInfoQueryResultEntity.getCurrency1().getCode()) + "/" +PublicCodeUtils.getGoldCurrencyCode(mActivity,mTradeInfoQueryResultEntity.getCurrency2().getCode()));
        dt_detail_currency.updateValue(PublicCodeUtils.getCurrency(mActivity,mTradeInfoQueryResultEntity.getSettleCurrecny()));
        dt_detail_buy_direction.updateValue(mTransQueryUtils.getBuyDirection(mTradeInfoQueryResultEntity.getDirection()));
        dt_detail_entrust_rate.updateValue(mTradeInfoQueryResultEntity.getFirstCustomerRate());
        dt_detail_entrust_type.updateValue(mTransQueryUtils.getExchangeTranType(mTradeInfoQueryResultEntity.getExchangeTranType()));
        dt_detail_success_type.updateValue(mTransQueryUtils.getSuccessType(mTradeInfoQueryResultEntity.getFirstType()));
        dt_detail_entrust_date.updateValue(mTradeInfoQueryResultEntity.getPaymentDate().format(DateFormatters.dateAndTimeFormatter));
        if (mTradeInfoQueryResultEntity.getDueDate()!=null){
            dt_detail_lose_date.updateValue(mTradeInfoQueryResultEntity.getDueDate().format(DateFormatters.dateAndTimeFormatter));
        } else{
            dt_detail_lose_date.updateData("成交时间",mTradeInfoQueryResultEntity.getExchangeTransDate().format(DateFormatters.dateAndTimeFormatter));
        }


    }

    @Override
    public void psnXpadSuccessConDetailQuerySuccess(PsnVFGTradeDetailQueryResult psnVFGTradeDetailQueryResult) {
        closeProgressDialog();
        PsnVFGTradeDetailQueryResult result = psnVFGTradeDetailQueryResult;
        dt_detail_transeq.updateValue(result.getVfgTransactionId());
        dt_detail_slogan.updateValue(mTransQueryUtils.getopenPositionFlag(result.getTradeBackground()));
        dt_detail_success_rate.updateValue(result.getTxRate());
        s_detail_scrollview.setVisibility(View.VISIBLE);
    }

    @Override
    public void psnXpadSuccessConDetailQueryFail(BiiResultErrorException biiResultErrorException) {

    }
    @Override
    public void setListener() {
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_long_short_forex_success_condition);
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
        mSuccessConDetailPresenter.unsubscribe();
        super.onDestroy();
    }

    @Override
    public void setPresenter(SuccessConDetailContract.Presenter presenter) {

    }
}
