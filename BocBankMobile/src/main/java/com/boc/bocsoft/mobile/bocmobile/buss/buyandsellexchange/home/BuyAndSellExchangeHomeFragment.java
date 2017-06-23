package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryQuotePrice.PsnFessQueryQuotePriceResult;
import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnGetExchangeOutlay.PsnGetExchangeOutlayResult;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.swipeRefreshLayout.SuperSwipeRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.model.BuyAndSellExcHomeModel;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.ui.BuyExchangeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.SellExchange.ui.CurrencyAccFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.TradeQuery.ui.TradeQueryListFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.common.BuySellCalculatorFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.home.model.PriceModel;

import java.util.List;

/**
 * 结构汇首页
 * Created by gwluo on 2016/11/29.
 */

public class BuyAndSellExchangeHomeFragment extends MvpBussFragment<BuyExchangeHomePresenter> implements View.OnClickListener, BuyExchangeHomeContract.View {
    BuyExchangeHomePresenter presenter;
    private CashRemitAdapter cashRemitAdapter;

    @Override
    protected BuyExchangeHomePresenter initPresenter() {
        presenter = new BuyExchangeHomePresenter(this);
        return presenter;
    }

    private View mRoot;
    private TextView tv_buy_exchange;//购汇按钮
    private TextView tv_sell_exchange;//结汇按钮
    private TextView tv_trans_query;//交易查询按钮
    private ListView lv_cast_remit;//牌价列表
    private SuperSwipeRefreshLayout cus_cast_remit_layout;//牌价列表外层

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRoot = mInflater.inflate(R.layout.boc_fragment_buy_sell_exchange_home, null);
        return mRoot;
    }

    @Override
    public void initView() {
        tv_buy_exchange = mViewFinder.find(R.id.tv_buy_exchange);
        tv_sell_exchange = mViewFinder.find(R.id.tv_sell_exchange);
        tv_trans_query = mViewFinder.find(R.id.tv_trans_query);
        cus_cast_remit_layout = mViewFinder.find(R.id.cus_cast_remit_layout);
        lv_cast_remit = mViewFinder.find(R.id.lv_cast_remit);
        mTitleBarView.setRightButton(getResources().getDrawable(R.drawable.boc_icon));
    }

    @Override
    public void initData() {
        cashRemitAdapter = new CashRemitAdapter(mContext);
        lv_cast_remit.setAdapter(cashRemitAdapter);
        boolean login = ApplicationContext.isLogin();
//        PsnInvestmentManageIsOpenParams params = new PsnInvestmentManageIsOpenParams();
        //登录和非登录调用接口不同
        if (login) {
            getPresenter().psnFessQueryQuotePrice();
        } else {
            getPresenter().psnGetExchangeOutlayParams();
        }
    }

    @Override
    public void setListener() {
        tv_buy_exchange.setOnClickListener(this);
        tv_sell_exchange.setOnClickListener(this);
        tv_trans_query.setOnClickListener(this);
        mTitleBarView.setRightButtonOnClickLinster(caculateClickLis);
        cus_cast_remit_layout.setOnPullRefreshListener(listPullListener);
        lv_cast_remit.setOnItemClickListener(onListItemClick);
        lv_cast_remit.setOnItemLongClickListener(onListItemLongClick);
    }

    /**
     * 点击
     */
    private AdapterView.OnItemClickListener onListItemClick = new AdapterView.OnItemClickListener() {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    };
    /**
     * 长按
     */
    private AdapterView.OnItemLongClickListener onListItemLongClick = new AdapterView.OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            return false;
        }
    };
    /**
     * 下拉监听
     */
    private SuperSwipeRefreshLayout.OnPullRefreshListener listPullListener = new SuperSwipeRefreshLayout.OnPullRefreshListener() {

        @Override
        public void onRefresh() {

        }

        @Override
        public void onPullDistance(int distance) {

        }

        @Override
        public void onPullEnable(boolean enable) {

        }
    };
    /**
     * 右上角计算器监听
     */
    private View.OnClickListener caculateClickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BuySellCalculatorFragment fragment = new BuySellCalculatorFragment();
            start(fragment);
        }
    };

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_buy_sell_exchange);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_buy_exchange) {//购汇
            jumpToBuyExchange(null);
        } else if (id == R.id.tv_sell_exchange) {//结汇
            jumpToSellExchange(null);
        } else if (id == R.id.tv_trans_query) {//查询详情
            TradeQueryListFragment mTradeQueryListFragment = new TradeQueryListFragment();
            start(mTradeQueryListFragment);
        }

    }

    @Override
    protected boolean isHaveTitleBarView() {
        return super.isHaveTitleBarView();
    }


    @Override
    public void getDataSucc(List<PriceModel> list) {

    }

    @Override
    public void getDataFail() {
        closeProgressDialog();
    }

    private void jumpToBuyExchange(BuyAndSellExcHomeModel model) {
        BuyExchangeFragment buyExchangeFragment = new BuyExchangeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("BuyAndSellExcHomeModel", model);
        buyExchangeFragment.setArguments(bundle);
        start(buyExchangeFragment);
    }

    private void jumpToSellExchange(BuyAndSellExcHomeModel model) {
        CurrencyAccFragment sellExchangeFragment = new CurrencyAccFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("BuyAndSellExcHomeModel", model);
        sellExchangeFragment.setArguments(bundle);
        start(sellExchangeFragment);
    }

    @Override
    public void setPresenter(BuyExchangeHomeContract.Presenter presenter) {

    }
}
