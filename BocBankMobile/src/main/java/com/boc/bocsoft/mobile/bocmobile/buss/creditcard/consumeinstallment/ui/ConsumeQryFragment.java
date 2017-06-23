package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.PinnedSectionListView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.ShowListConst;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.adapter.ShowListAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.bean.ShowListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.model.ConsumeTransModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.model.CrcdConsumeQueryModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.consumeinstallment.presenter.ConSumeQryPresenter;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lq7090
 * 创建时间：2016/12/27.
 * 用途：用于显示可分期消费账单列表
 */
public class ConsumeQryFragment extends MvpBussFragment<ConSumeQryPresenter> implements View.OnClickListener, ConsumeQryContract.BaseView
        , PinnedSectionListView.ClickListener, PullToRefreshLayout.OnLoadListener {


    public static final String ACCOUNT_BEAN="account_bean";

    private View rootView;
    private PullToRefreshLayout flRefresh;
    private PinnedSectionListView transactionView;
    private TextView tv_no_result;
    private CrcdConsumeQueryModel billQueryModle;

    private int currentPage=0;
    private final int PAGE_SIZE=10;
    private List<ShowListBean> transactionBeans;
    private List<ConsumeTransModel.ConsumeBean> transBeanList;
    private ShowListAdapter mAdapter;
    private boolean isNoMoreData=false;
    private AccountBean accountBean;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView=mInflater.inflate(R.layout.boc_fragment_crcd_bill_consume,null);
        return rootView;
    }

    @Override
    public void beforeInitView() {
        super.beforeInitView();
        accountBean=getArguments().getParcelable(ACCOUNT_BEAN);
    }

    @Override
    public void initView() {
        super.initView();

        flRefresh = (PullToRefreshLayout) rootView.findViewById(R.id.fl_refresh);
        transactionView = (PinnedSectionListView) rootView.findViewById(R.id.tran_detail);
        tv_no_result=(TextView)rootView.findViewById(R.id.tv_no_result);

    }

    @Override
    public void initData() {
        super.initData();
        mAdapter = new ShowListAdapter(getContext(), -1);
        transactionView.saveAdapter(mAdapter);
        transactionView.setShadowVisible(false);
        transactionView.setAdapter(mAdapter);

        loadData();
    }

    /**
     *  开始加载数据
     */
    public void loadData(){

        showLoadingDialog();

        billQueryModle=new CrcdConsumeQueryModel();
        billQueryModle.setAccountId(accountBean.getAccountId());
        billQueryModle.setPageSize(PAGE_SIZE+"");
        billQueryModle.setCurrentIndex(currentPage+"");
        billQueryModle.setCurrencyCode(ApplicationConst.CURRENCY_CNY);
//        billQueryModle.set_refresh("true");

        getPresenter().crcdDividedPayConsumeQry(billQueryModle);


    }

    @Override
    public void setListener() {
        super.setListener();
        flRefresh.setOnLoadListener(this);
        transactionView.setListener(this);
    }



    /**
     * 根据交易明细生成TransactionView需要的TransactionBean
     * @return
     */
    private List<ShowListBean> generateTransactionBean(ConsumeTransModel crcdBillQueryModel) {
        if (transactionBeans==null)
            transactionBeans = new ArrayList<>();

        transBeanList=crcdBillQueryModel.getList();
        for (int i = 0; i < transBeanList.size(); i++) {
            LocalDate localDate = LocalDate.parse(transBeanList.get(i).getTransDate().replace("/","-"));
            String formatTime = "";// 当前时间 MM月/yyyy
            String tempTime = "";// 上一次时间
            if (localDate != null) {
                formatTime = localDate.format(DateFormatters.monthFormatter1);
            }
            if (i > 0) {
                tempTime = LocalDate.parse(transBeanList.get(i-1).getTransDate().replace("/","-")).format(DateFormatters.monthFormatter1);
            }
            if (tempTime.equals(formatTime)) {// child
                ShowListBean item = new ShowListBean();
                item.type = ShowListBean.CHILD;
                item.setTitleID(ShowListConst.TITLE_LEFT_ABOVE_RIGHT_BELOW);
                item.setTime(localDate);
                item.setContentLeftAbove(transBeanList.get(i).getTransDesc());
                item.setContentRightBelow(PublicCodeUtils.getCurrency(getContext(), transBeanList.get(i).getTranCurrency())+
                        MoneyUtils.transMoneyFormat(transBeanList.get(i).getTranAmount(),transBeanList.get(i).getTranCurrency()));

                transactionBeans.add(item);
            } else {// group
                for (int j = 0; j < 2; j++) {
                    ShowListBean itemFirst = new ShowListBean();
                    if (j == 0) {
                        itemFirst.type = ShowListBean.GROUP;
                        itemFirst.setGroupName(formatTime);
                        itemFirst.setTime(localDate);
                    } else {
                        itemFirst.type = ShowListBean.CHILD;
                        itemFirst.setTitleID(ShowListConst.TITLE_LEFT_ABOVE_RIGHT_BELOW);
                        itemFirst.setTime(localDate);
                        itemFirst.setContentLeftAbove(transBeanList.get(i).getTransDesc());
                        itemFirst.setContentRightBelow(PublicCodeUtils.getCurrency(getContext(), transBeanList.get(i).getTranCurrency())+
                                MoneyUtils.transMoneyFormat(transBeanList.get(i).getTranAmount(),transBeanList.get(i).getTranCurrency()));
                    }
                    transactionBeans.add(itemFirst);
                }
            }
        }
        return transactionBeans;
    }

    @Override
    public void crcdDividedPayConsumeQrySuccess(ConsumeTransModel result) {
        closeProgressDialog();
        currentPage++;

        if (result!=null&&result.getList().size()>0){
            mAdapter.setData(generateTransactionBean(result));
        }else {
            isNoMoreData=true;
            tv_no_result.setVisibility(View.VISIBLE);
            if (currentPage==1)
            flRefresh.setVisibility(View.GONE);
        }

        if (result.getRecordNumber()>currentPage*PAGE_SIZE){
            flRefresh.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
        }else{
            isNoMoreData=true;
            flRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
        }

    }

    @Override
    public void crcdDividedPayConsumeQryFailed(BiiResultErrorException exception) {

    }

    @Override
    public void onItemClickListener(int position) {
        gotoTransDetailFragment(transBeanList.get(position));
    }

    @Override
    public void onClick(View v) {

    }

    /**
     *  跳转详情页面
     */
    private void gotoTransDetailFragment( ConsumeTransModel.ConsumeBean transDetail) {
        ConsumeDetailsFragment detailsFragment=new ConsumeDetailsFragment();
        Bundle bundle=new Bundle();
        bundle.putParcelable(ConsumeDetailsFragment.DETAIL_INFO,transDetail);
        bundle.putParcelable(ACCOUNT_BEAN,accountBean);
        detailsFragment.setArguments(bundle);
        start(detailsFragment);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        if (!isNoMoreData){
//            billQueryModle.set_refresh("false");
            loadDetailListData();
        }
        else
            flRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
    }

    private void loadDetailListData() {
        showLoadingDialog();
        billQueryModle.setCurrentIndex(currentPage+"");
        getPresenter().crcdDividedPayConsumeQry(billQueryModle);
    }

    @Override
    protected ConSumeQryPresenter initPresenter() {
        return new ConSumeQryPresenter(this);
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
        return getResources().getString(R.string.boc_crcd_acc_more_consume_part);
    }
}
