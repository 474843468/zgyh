package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTradeDetailQuery.PsnVFGTradeDetailQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.BaseDetailView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRow;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.TransQueryUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model.XpadPsnVFGCancelOrderModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model.XpadVFGTradeDetailQueryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model.XpadVFGTradeInfoQueryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.presenter.HisEntruDetailPresenter;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

/**
 * 双向宝交易查询--历史委托查询详情界面
 * Created by zc on 2016/11/17
 */
public class HisEntruDetailFragment extends BussFragment implements HisEntruDetailContract.View {

    private View rootView;
    private HisEntruDetailContract.Presenter mHisEntruDetailPresenter;
    private static final int NOTICE_TYPE_CONDITION_CANCLE = 2; // 2表示“撤单成功”通知
    private ScrollView s_detail_scrollview;
    private BaseDetailView detail_view;
    private TextView tt_cancle;
    private TextView tt_state;//状态
    private TextView pay_currency;//货币标识
    private TextView currency_unit;//金额单位
    private TextView tt_amount;//交易金额
    private DetailTableRow dt_currency_coupe;//货币对
    private DetailTableRow dt_detail_transeq;//委托序号
    private DetailTableRow dt_detail_currency;//结算币种
    private DetailTableRow dt_detail_buy_direction;//买卖方向
    private DetailTableRow dt_detail_slogan;//建仓标识
    private DetailTableRow dt_detail_entrust_rate;//委托汇率
    private DetailTableRow dt_detail_add_money;//追击点差
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
        rootView = mInflater.inflate(R.layout.boc_fragment_his_entru_detail, null);
        return rootView;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(mActivity, "撤单成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setAction("longshortQuery");
            intent.putExtra("noticeType", NOTICE_TYPE_CONDITION_CANCLE); // 0表示“页面选中”通知（共3种通知类型，页面选中通知，筛选条件选择通知，撤单成功通知。这3种都有可能进行重新查询操作）
            intent.putExtra("selectPagePosition", selectPagePosition);
            mActivity.sendBroadcast(intent);
            HisEntruDetailFragment.this.pop();
        }
    };



    @Override
    public void initView() {
        s_detail_scrollview = (ScrollView) rootView.findViewById(R.id.detail_scroll);
        detail_view = (BaseDetailView) rootView.findViewById(R.id.details_view);
        tt_cancle = (TextView) rootView.findViewById(R.id.bt_cancle);
        pay_currency = (TextView) rootView.findViewById(R.id.pay_currency);
        currency_unit = (TextView) rootView.findViewById(R.id.currency_unit);
        tt_state = (TextView) rootView.findViewById(R.id.pay_state);
        tt_amount = (TextView) rootView.findViewById(R.id.pay_amount);
        dt_currency_coupe = (DetailTableRow) rootView.findViewById(R.id.currency_coupe);
        dt_detail_transeq = (DetailTableRow) rootView.findViewById(R.id.detail_transeq);
        dt_detail_currency = (DetailTableRow) rootView.findViewById(R.id.detail_currency);
        dt_detail_buy_direction = (DetailTableRow) rootView.findViewById(R.id.detail_buy_direction);
        dt_detail_slogan = (DetailTableRow) rootView.findViewById(R.id.detail_slogan);
        dt_detail_entrust_rate = (DetailTableRow) rootView.findViewById(R.id.detail_entrust_rate);
        dt_detail_add_money = (DetailTableRow) rootView.findViewById(R.id.detail_add_money);
        dt_detail_entrust_type = (DetailTableRow) rootView.findViewById(R.id.detail_entrust_type);
        dt_detail_success_type = (DetailTableRow) rootView.findViewById(R.id.detail_success_type);
        dt_detail_entrust_date = (DetailTableRow) rootView.findViewById(R.id.detail_entrust_date);
        dt_detail_lose_date = (DetailTableRow) rootView.findViewById(R.id.detail_lose_date);
        s_detail_scrollview.setVisibility(View.INVISIBLE);
    }

    @Override
    public void initData() {
        mHisEntruDetailPresenter = new HisEntruDetailPresenter(this);
        XpadVFGTradeDetailQueryModel model = new XpadVFGTradeDetailQueryModel();
        mTransQueryUtils = new TransQueryUtils();
        mTradeInfoQueryResultEntity = getArguments().getParcelable("xpadTradeInfoQueryResultEntity");
        conversationId = getArguments().getString("conversationId");
        selectPagePosition = getArguments().getInt("selectPagePosition", -1);

        model.setConversationId(conversationId);
        model.setVfgTransactionId(mTradeInfoQueryResultEntity.getConsignNumber());
        model.setInternalSeq(mTradeInfoQueryResultEntity.getInternalSeq());
        mHisEntruDetailPresenter.psnXpadHisEntruDetailQuery(model);

        if ("X".equals(mTradeInfoQueryResultEntity.getOrderStatus())){
            tt_state.setBackgroundResource(R.drawable.boc_transaction_status_bg_red);
        }else if ("S".equals(mTradeInfoQueryResultEntity.getOrderStatus())||"R".equals(mTradeInfoQueryResultEntity.getOrderStatus())){
            tt_state.setBackgroundDrawable(getResources().getDrawable(R.drawable.boc_transaction_status_bg_green));
        }else{
            tt_state.setBackgroundDrawable(getResources().getDrawable(R.drawable.boc_transaction_status_bg_orange));
        }
        tt_state.setText(mTransQueryUtils.getOrderStatusDetail(mTradeInfoQueryResultEntity.getOrderStatus()));

        String currencyType = mTransQueryUtils.getCurrencyType(mTradeInfoQueryResultEntity.getCurrency1().getCode());
        pay_currency.setText(getResources().getString(R.string.boc_long_short_forex_pay_amount)+currencyType);

        tt_state.setText(mTransQueryUtils.getOrderStatusDetail(mTradeInfoQueryResultEntity.getOrderStatus()));
        String currencyUnit = mTransQueryUtils.getCurrencyUnit(mTradeInfoQueryResultEntity.getCurrency1().getCode(),mTradeInfoQueryResultEntity.getCurrency2().getCode());
        if (currencyUnit.equals(mTradeInfoQueryResultEntity.getCurrency1().getCode())){
            currencyUnit = "";
        }
        tt_amount.setText(MoneyUtils.transMoneyFormat(mTradeInfoQueryResultEntity.getAmount(),mTradeInfoQueryResultEntity.getSettleCurrecny()) );
        currency_unit.setText(currencyUnit);

        dt_currency_coupe.updateValue(PublicCodeUtils.getGoldCurrencyCode(mActivity,mTradeInfoQueryResultEntity.getCurrency1().getCode()) + "/" +PublicCodeUtils.getGoldCurrencyCode(mActivity,mTradeInfoQueryResultEntity.getCurrency2().getCode()));
        dt_detail_currency.updateValue(PublicCodeUtils.getCurrency(mActivity,mTradeInfoQueryResultEntity.getSettleCurrecny()));
        dt_detail_buy_direction.updateValue(mTransQueryUtils.getBuyDirection(mTradeInfoQueryResultEntity.getDirection()));
        dt_detail_entrust_rate.updateValue(mTradeInfoQueryResultEntity.getFirstCustomerRate());
        dt_detail_entrust_type.updateValue(mTransQueryUtils.getExchangeTranType(mTradeInfoQueryResultEntity.getExchangeTranType()));
        dt_detail_success_type.updateValue(mTransQueryUtils.getSuccessType(mTradeInfoQueryResultEntity.getFirstType()));
        dt_detail_entrust_date.updateValue(mTradeInfoQueryResultEntity.getPaymentDate().format(DateFormatters.dateAndTimeFormatter));
        dt_detail_lose_date.updateValue(mTradeInfoQueryResultEntity.getDueDate().format(DateFormatters.dateAndTimeFormatter));



        if("N".equals(mTradeInfoQueryResultEntity.getOrderStatus())){
            tt_cancle.setVisibility(View.VISIBLE);
        }

//        String amount = mTradeInfoQueryResultEntity.getAmount();
//        String status = mTransQueryUtils.getOrderStatus(mTradeInfoQueryResultEntity.getOrderStatus());
//        String currency_coupe = PublicCodeUtils.getCurrency(mActivity,mTradeInfoQueryResultEntity.getCurrency1().getCode()) + "/" +PublicCodeUtils.getCurrency(mActivity,mTradeInfoQueryResultEntity.getCurrency2().getCode());
//
//        detail_view.updateHeadData(getResources().getString(R.string.boc_long_short_forex_pay_amount),amount);
//        detail_view.updateHeadStatus(status,getResources().getColor(R.color.boc_text_color_money_count));
//        detail_view.updateHeadDetail(getResources().getString(R.string.boc_long_short_forex_currency_coupe),currency_coupe);
//
//        detail_view.addDetailRow(getResources().getString(R.string.boc_long_short_forex_entrust_seq),mTradeInfoQueryResultEntity.getConsignNumber());
//        detail_view.addDetailRow(getResources().getString(R.string.boc_long_short_forex_settle_currency),PublicCodeUtils.getCurrency(mActivity,mTradeInfoQueryResultEntity.getSettleCurrecny()));
//        detail_view.addDetailRow(getResources().getString(R.string.boc_long_short_forex_direction),mTransQueryUtils.getBuyDirection(mTradeInfoQueryResultEntity.getDirection()));
//        detail_view.addDetailRow(getResources().getString(R.string.boc_long_short_forex_slogan),mTradeInfoQueryResultEntity.getOpenPositionFlag());
//        detail_view.addDetailRow(getResources().getString(R.string.boc_long_short_forex_entrust_rate),mTradeInfoQueryResultEntity.getFirstCustomerRate());




    }

    @Override
    public void setListener() {
        tt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TitleAndBtnDialog dialog = new TitleAndBtnDialog(mActivity);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) dialog.getTvNotice().getLayoutParams();
                layoutParams.height = 260;
                dialog.setTitle("");
                dialog.setNoticeContent("请您再次确认，是否撤单此委托交易？");
                dialog.setRightBtnTextBgColor(getResources().getColor(R.color.boc_main_bg_color),getResources().getColor(R.color.boc_main_bg_color),getResources().getColor(R.color.boc_text_color_red),getResources().getColor(R.color.boc_text_color_red));
                dialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
                    @Override
                    public void onLeftBtnClick(View view) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onRightBtnClick(View view) {
                        dialog.dismiss();
                        // 撤单的逻辑
                        XpadPsnVFGCancelOrderModel xpadPsnVFGCancelOrderModel = new XpadPsnVFGCancelOrderModel();

                        xpadPsnVFGCancelOrderModel.setAmount(mTradeInfoQueryResultEntity.getAmount());
                        xpadPsnVFGCancelOrderModel.setConsignNumber(mTradeInfoQueryResultEntity.getConsignNumber());
                        xpadPsnVFGCancelOrderModel.setCurrencyCode(mTradeInfoQueryResultEntity.getSettleCurrecny());
                        xpadPsnVFGCancelOrderModel.setCurrencycode1(mTradeInfoQueryResultEntity.getCurrency1().getCode());
                        xpadPsnVFGCancelOrderModel.setCurrencycode2(mTradeInfoQueryResultEntity.getCurrency2().getCode());
                        xpadPsnVFGCancelOrderModel.setDirection(mTradeInfoQueryResultEntity.getDirection());
                        xpadPsnVFGCancelOrderModel.setDueDate(mTradeInfoQueryResultEntity.getDueDate().format(DateFormatters.dateFormatter1));
                        xpadPsnVFGCancelOrderModel.setExchangeTranType(mTradeInfoQueryResultEntity.getExchangeTranType());
                        xpadPsnVFGCancelOrderModel.setOpenPositionFlag(mTradeInfoQueryResultEntity.getOpenPositionFlag());
                        xpadPsnVFGCancelOrderModel.setPaymentDate(mTradeInfoQueryResultEntity.getPaymentDate().format(DateFormatters.dateFormatter1));
                        mHisEntruDetailPresenter.psnXpadCancelOrder(xpadPsnVFGCancelOrderModel);
                    }
                });
                dialog.show();
            }

        });

    }

    /**
     * 历史委托详情查询成功
     * @param psnVFGTradeDetailQueryResult
     */
    @Override
    public void psnXpadHisEntruDetailQuerySuccess(PsnVFGTradeDetailQueryResult psnVFGTradeDetailQueryResult) {

        closeProgressDialog();
        PsnVFGTradeDetailQueryResult result = psnVFGTradeDetailQueryResult;
        dt_detail_add_money.updateValue(String.valueOf(result.getFoSet()));
        dt_detail_transeq.updateValue(result.getVfgTransactionId());
        dt_detail_slogan.updateValue(mTransQueryUtils.getopenPositionFlag(result.getTradeBackground()));
//        detail_view.addDetailRow(getResources().getString(R.string.boc_long_short_forex_add_set),String.valueOf(result.getFoSet()));
//        detail_view.addDetailRow(getResources().getString(R.string.boc_long_short_forex_entrust_type),mTransQueryUtils.getExchangeTranType(mTradeInfoQueryResultEntity.getExchangeTranType()));
//        detail_view.addDetailRow(getResources().getString(R.string.boc_long_short_forex_success_type),mTransQueryUtils.getSuccessType(mTradeInfoQueryResultEntity.getFirstType()));
//        detail_view.addDetailRow(getResources().getString(R.string.boc_long_short_forex_entrust_date),mTradeInfoQueryResultEntity.getPaymentDate().format(DateFormatters.dateAndTimeFormatter));
//        detail_view.addDetailRow(getResources().getString(R.string.boc_long_short_forex_lose_date),mTradeInfoQueryResultEntity.getDueDate().format(DateFormatters.dateAndTimeFormatter));

        s_detail_scrollview.setVisibility(View.VISIBLE);

    }

    /**
     * 历史委托详情查询失败
     * @param biiResultErrorException
     */
    @Override
    public void psnXpadHisEntruDetailQueryFail(BiiResultErrorException biiResultErrorException) {

    }

    /**
     * 历史委托撤单成功
     * @param viewModel
     */
    @Override
    public void psnXpadCancelOrderSuccess(XpadPsnVFGCancelOrderModel viewModel) {
        handler.sendEmptyMessageDelayed(0, 0);
    }

    /**
     * 历史委托撤单失败
     * @param biiResultErrorException
     */
    @Override
    public void psnXpadCancelOrderFail(BiiResultErrorException biiResultErrorException) {
        Toast.makeText(mActivity, "撤单失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(HisEntruDetailContract.Presenter presenter) {

    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_long_short_forex_history_entrust);
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
        mHisEntruDetailPresenter.unsubscribe();
        super.onDestroy();
    }
}
