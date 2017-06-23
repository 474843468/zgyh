package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRow;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.TransQueryUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model.XpadVFGTradeInfoQueryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.presenter.TakeTradeDetailPresenter;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

/**
 * 双向宝交易查询--斩仓交易查询详情界面
 * Created by zc on 2016/11/17
 */
public class TakeTradeDetailFragment extends BussFragment implements TakeTradeDetailContract.View {

    private View rootView;
    private TakeTradeDetailContract.Presenter mTakeTradeDetailPresenter;

    private LinearLayout detail_layout;
    private TextView tt_amount;//斩仓金额
    private TextView pay_currency;//货币标识
    private TextView currency_unit;//金额单位
    private DetailTableRow dt_currency_coupe;//货币对
    private DetailTableRow dt_detail_transeq;//斩仓前委托序号
    private DetailTableRow dt_detail_take_transeq;//斩仓序号
    private DetailTableRow dt_detail_take_date;//斩仓时间
    private DetailTableRow dt_detail_take_first_rate;//斩仓前充足率
    private DetailTableRow dt_detail_take_last_rate;//斩仓后委托率
    //查询到的数据实体
    private XpadVFGTradeInfoQueryModel.XpadPsnVFGTradeInfoQueryResultEntity mTradeInfoQueryResultEntity;

    private TransQueryUtils mTransQueryUtils;
    private String conversationId;
    private int selectPagePosition;


    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_take_trade_detail, null);
        return rootView;
    }

    @Override
    public void initView() {
        detail_layout = (LinearLayout) rootView.findViewById(R.id.detail_linearlayout);
        pay_currency = (TextView) rootView.findViewById(R.id.pay_currency);
        currency_unit = (TextView) rootView.findViewById(R.id.currency_unit);
        tt_amount = (TextView) rootView.findViewById(R.id.pay_amount);
        dt_currency_coupe = (DetailTableRow) rootView.findViewById(R.id.currency_coupe);
        dt_detail_transeq = (DetailTableRow) rootView.findViewById(R.id.detail_transeq);
        dt_detail_take_transeq = (DetailTableRow) rootView.findViewById(R.id.detail_take_transeq);
        dt_detail_take_date = (DetailTableRow) rootView.findViewById(R.id.detail_take_date);
        dt_detail_take_first_rate = (DetailTableRow) rootView.findViewById(R.id.detail_take_first_rate);
        dt_detail_take_last_rate = (DetailTableRow) rootView.findViewById(R.id.detail_take_last_rate);

        detail_layout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void initData() {
        closeProgressDialog();
        mTakeTradeDetailPresenter = new TakeTradeDetailPresenter(this);
        mTransQueryUtils = new TransQueryUtils();
        mTradeInfoQueryResultEntity = getArguments().getParcelable("xpadTradeInfoQueryResultEntity");
        conversationId = getArguments().getString("conversationId");
        selectPagePosition = getArguments().getInt("selectPagePosition", -1);

        String currencyType = mTransQueryUtils.getCurrencyType(mTradeInfoQueryResultEntity.getCurrency1().getCode());
        pay_currency.setText(getResources().getString(R.string.boc_long_short_forex_take_amount)+currencyType);

        String currencyUnit = mTransQueryUtils.getCurrencyUnit(mTradeInfoQueryResultEntity.getCurrency1().getCode(),mTradeInfoQueryResultEntity.getCurrency2().getCode());
        if (currencyUnit.equals(mTradeInfoQueryResultEntity.getCurrency1().getCode())){
            currencyUnit = "";
        }
        tt_amount.setText(MoneyUtils.transMoneyFormat(mTradeInfoQueryResultEntity.getLiquidationAmount(),mTradeInfoQueryResultEntity.getSettleCurrecny()) );
        currency_unit.setText(currencyUnit);
        dt_currency_coupe.updateValue(PublicCodeUtils.getGoldCurrencyCode(mActivity,mTradeInfoQueryResultEntity.getCurrency1().getCode()) + "/" +PublicCodeUtils.getGoldCurrencyCode(mActivity,mTradeInfoQueryResultEntity.getCurrency2().getCode()));
        dt_detail_transeq.updateValue(mTradeInfoQueryResultEntity.getOldExchangeSeq());
        dt_detail_take_transeq.updateValue(mTradeInfoQueryResultEntity.getLiquidationNo());
        dt_detail_take_date.updateValue(mTradeInfoQueryResultEntity.getLiquidationDate().format(DateFormatters.dateAndTimeFormatter));

        dt_detail_take_first_rate.updateValue(mTradeInfoQueryResultEntity.getBeforeLiquidRatio()+" %");
        dt_detail_take_last_rate.updateValue(mTradeInfoQueryResultEntity.getAfterLiquidRatio()+" %");

        detail_layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void setListener() {
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_long_short_forex_take_trade);
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
        mTakeTradeDetailPresenter.unsubscribe();
        super.onDestroy();
    }

}
