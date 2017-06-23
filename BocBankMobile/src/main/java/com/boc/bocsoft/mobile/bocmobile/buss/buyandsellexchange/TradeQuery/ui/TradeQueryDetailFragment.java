package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.TradeQuery.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryForErrormesg.PsnFessQueryForErrormesgResult;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryHibsExchangeTransDetail.PsnFessQueryHibsExchangeTransDetailResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.emptyview.CommonEmptyView;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.TradeQuery.model.TradeQueryListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.TradeQuery.model.TradeQueryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.TradeQuery.model.TradeQueryTransDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.TradeQuery.presenter.TradeQueryDetailPresenter;

/**
 * 交易查询明细界面
 * Created by wzn7074 on 2016/11/28.
 */

public class TradeQueryDetailFragment extends MvpBussFragment<TradeQueryDetailPresenter>
        implements TradeQueryDetailContract.View, View.OnClickListener{

    private String wznTag = "wzn7074-TradeQueryDetailFragment"; //test
    public static final String MODEL = "model";

    protected LinearLayout viewData;
    protected CommonEmptyView viewEmpty;

    private View mRootView;
    private DetailTableHead mDetailHead;
    private DetailContentView mDetailContent;

    private TradeQueryTransDetailModel mModel;
    private TradeQueryModel mTradeQueryModel;
    private TradeQueryDetailPresenter mTradeQueryDetailPresenter;

    private TradeQueryListModel.TradeQueryResultEntity mTradeQueryResultEntities;
    private String conversationId;
    private String mState;

    @Override
    protected TradeQueryDetailPresenter initPresenter() {
//        return new TradeQueryDetailPresenter(this);
        return mTradeQueryDetailPresenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        LogUtils.d(wznTag, "onCreate");//test
        mModel = new TradeQueryTransDetailModel();

        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(MODEL)) {
            mModel = bundle.getParcelable(MODEL);
        }
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_buyandsellexchange_tradequery_details,null);
        return mRootView;
    }

    @Override
    public void initView() {
        super.initView();
        mDetailHead = (DetailTableHead) mRootView.findViewById(R.id.tradequery_detail_header);
        mDetailContent = (DetailContentView) mRootView.findViewById(R.id.tradequery_detail_content);
    }

    @Override
    public void initData() {
        mTradeQueryDetailPresenter = new TradeQueryDetailPresenter(this);
        mModel = new TradeQueryTransDetailModel();

        /**
         *  从上个页面 TradeQueryTransDetailFragment获取所需数据
         * */
        mTradeQueryResultEntities = getArguments().getParcelable("tradeQueryResultEntity");
        conversationId = getArguments().getString("conversationId");
        mState = mTradeQueryResultEntities.getStatus();

        /**
         *  将上一个页面带入的数据填写入相应的视图位置
         * */
        putDataToDetailViewByBundle(mTradeQueryResultEntities);

        /**
         *  判断交易状态 并根据不同状态调用不同接口（或不调用接口）
         * */
        if(mState.equals("00")) {//成功

            mModel.setConversationId(conversationId);
            mModel.setBankSelfNum(mTradeQueryResultEntities.getBankSelfNum());
            mModel.setPaymentDate(mTradeQueryResultEntities.getPaymentDate());
            mModel.setRefNum(mTradeQueryResultEntities.getRefNum());
            //test
            LogUtils.d(wznTag,  "ConversationId " + mModel.getConversationId());

            //调用接口 019PsnFessQueryHibsExchangeTransDetail查询全渠道结购汇交易详情
            showLoadingDialog();
            mTradeQueryDetailPresenter.queryTransDetail(mModel);

        } else if (mState.equals("01")) {//失败
            mModel.setTranRetCode(mTradeQueryResultEntities.getTranRetCode());
            //test
            LogUtils.d(wznTag, "TranRetCode: " + mTradeQueryResultEntities.getTranRetCode());

            showLoadingDialog();
            mTradeQueryDetailPresenter.queryTranRetMesg(mModel);


        } else if (mState.equals("02")) {//未明
            //test
            LogUtils.d(wznTag,"状态未明");
        }



    }


    @Override
    public void psnFessQueryHibsExchangeTransDetailSuccess(PsnFessQueryHibsExchangeTransDetailResult result) {

        closeProgressDialog();

        putDataToDetailViewSuccess(result);

    }

    @Override
    public void psnFessQueryForErrormesgFail(BiiResultErrorException biiResultErrorException) {
        //test
        LogUtils.d(wznTag,"psnFessQueryForErrormesgFail" );

    }

    @Override
    public void psnFessQueryForErrormesgSuccess(PsnFessQueryForErrormesgResult result) {
        //test
        LogUtils.d(wznTag,"psnFessQueryForErrormesgSuccess" );

        closeProgressDialog();

        putDataToDetailViewFail(result);


    }


    @Override
    public void psnFessQueryHibsExchangeTransDetailFail(BiiResultErrorException biiResultErrorException) {
        //test
        LogUtils.d(wznTag,"psnFessQueryHibsExchangeTransDetailFail" );

    }

    @Override
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

    @Override
    public void showLoading() {
        super.showLoadingDialog();
    }


    @Override
    public void closeLoading() {
        super.closeProgressDialog();
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void setPresenter(TradeQueryDetailContract.Presenter presenter) {

    }

    public void showDataView() {
        showDataView(true, null);
    }

    public void hideDataView(String errorMessage) {
        showDataView(false, errorMessage);
    }

    private void showDataView(boolean hasData, String errorMessage) {
        if (hasData) {
            viewData.setVisibility(View.VISIBLE);
            viewEmpty.setVisibility(View.GONE);
        } else {
            viewData.setVisibility(View.GONE);
            viewEmpty.setVisibility(View.VISIBLE);
            viewEmpty.setEmptyTips(errorMessage, R.drawable.boc_load_failed);
        }
    }

    /**
     *   拼接 交易金额 （币种 钞汇）
     * */
    private String mkAmountStr(String CurrencyCode, String CashRemit  ) {

        StringBuilder mAmountStrBud = new StringBuilder();
//       Log.d(wznTag,"try to build mAmountStrBud");
        mAmountStrBud = mAmountStrBud.append("交易金额").append(" (")
                .append(CurrencyCode).append(" ").append(CashRemit)
                .append(")");
        String AmountStr = mAmountStrBud.toString();

        return AmountStr;
    }

    /**
     * 用上一个页面带入数据装载 视图下部分
     * */
    private void putDataToDetailViewByBundle(TradeQueryListModel.TradeQueryResultEntity entity) {

        String mCurrencyCode = PublicCodeUtils.getCurrency(mContext, entity.getCurrencyCode());//币种

        String mCashRemit = PublicCodeUtils.getCashSpot(mContext,entity.getCashRemit());//钞汇类型

        String mAmount =MoneyUtils.transMoneyFormat(entity.getAmount(),entity.getCurrencyCode());//外币交易金额 将传入金额保留小数点后两位，四舍五入，没有小数的加.00.如果是日元则没有小数

        String mPaymentDate = entity.getPaymentDate() + " " + entity.getPaymentTime();//交易日期

        String mFurInfo = PublicCodeUtils.getBankrollUse(mContext, entity.getFundTypeCode());//资金来源
        LogUtils.d(wznTag, "列表带入的FundTypeCode: " + entity.getFundTypeCode());//test
        String mChannel = PublicCodeUtils.getFessChannel(mContext, entity.getChannel());//交易渠道

        String mTransType = PublicCodeUtils.getFessTransType(mContext, entity.getTransType());//交易类型

        String mAmountChineseStr = mkAmountStr(mCurrencyCode,mCashRemit);

        mDetailHead.updateData(mAmountChineseStr,mAmount);//交易金额 （美元 现汇）从字符串拼接

        if(entity.getStatus().equals("00")) {
            mDetailHead.setHeadStatus("成功", R.drawable.boc_transaction_status_bg_green);
        } else if (entity.getStatus().equals("01")) {
            mDetailHead.setHeadStatus("失败", R.drawable.boc_transaction_status_bg_red);
        } else if (entity.getStatus().equals("02")) {
            mDetailHead.setHeadStatus("未明", R.drawable.boc_transaction_status_bg_orange);
        }


        mDetailContent.addDetail("交易类型", mTransType);
        mDetailContent.addDetail("资金账户", entity.getAccountNumber());
        mDetailContent.addDetail("交易时间", mPaymentDate);
        mDetailContent.addDetail("资金用途", mFurInfo);
        mDetailContent.addDetail("交易渠道", mChannel);

    }

    /**
     * 请求交易详情接口所得数据装载 成功视图
     * */
    private void putDataToDetailViewSuccess(PsnFessQueryHibsExchangeTransDetailResult Result) {

        String mReturnCnyAmt = MoneyUtils.getRoundNumber(Result.getReturnCnyAmt(), 2);//人民币金额

        String mTableRow = MoneyUtils.getRoundNumber(Result.getExchangeRate(), 2);//交易成交牌价（渠道优惠后牌价）

        mDetailHead.setTableRow("成交牌价", mTableRow);
        mDetailHead.setTableRowTwo("人民币金额", mReturnCnyAmt);
        mDetailHead.setHeadStatus("成功",R.drawable.boc_transaction_status_bg_green);

    }


    /**
     * 请求交易详情接口所得数据装载 失败视图
     * */
    private void putDataToDetailViewFail(PsnFessQueryForErrormesgResult Result) {

        String mErrorMesg = Result.getTranRetMesg();
        mDetailHead.setTableRow("失败原因", mErrorMesg);
    }


}
