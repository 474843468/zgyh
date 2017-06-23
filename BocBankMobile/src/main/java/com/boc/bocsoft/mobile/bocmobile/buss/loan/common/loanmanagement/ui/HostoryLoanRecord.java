package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.LoanCosnt;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.model.LoanAccountListModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.model.LoanOverdueModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.presenter.LoanMangerPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.ui.hostoryrecordlistview.HostoryRecordBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.ui.hostoryrecordlistview.HostoryRecordListView;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.model.EloanQuoteViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.utils.DataUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by taoyongzhen on 2016/10/28.
 */

public class HostoryLoanRecord extends MvpBussFragment<LoanMangerPresenter> implements View.OnClickListener, LoanMangerContract.LoanManageView{

    private View mRootView = null;
    private PullToRefreshLayout pullToRefreshLayout = null; //上拉刷新页面
    private HostoryRecordListView mRepayPlanListView = null;  //listview
    private TextView tvNodata = null; //无数据提示
    private LoanMangerPresenter mPresenter = null;

    private LoanAccountListModel mViewModel = null; //页面显示数据
    //当前加载页码
    private int pageCurrentIndex = 0;
    //每页大小
    private final static int pageSize = ApplicationConst.PAGE_SIZE;
    //是否通过上拉刷新进行的查询请求
    private boolean isQueryByPullToRefresh = false;

    private boolean bIsRequesting = false; //是否正在请求数据，正在请求的时候不可同时再次请求

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_hostory_loan_record,null);
        return mRootView;
    }

    @Override
    public void initView() {
        super.initView();
        pullToRefreshLayout = (PullToRefreshLayout) mRootView.findViewById(R.id.remain_query_view);
        mRepayPlanListView = (HostoryRecordListView)mRootView.findViewById(R.id.lv_remain_query);
        tvNodata = (TextView)mRootView.findViewById(R.id.tvNoData);
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
    public void initData() {
        super.initData();
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(LoanCosnt.LOAN_ACCOUTLIST_MODEL)) {
            mViewModel = (LoanAccountListModel)bundle.get(LoanCosnt.LOAN_ACCOUTLIST_MODEL);
        }
        
        if(mViewModel == null || mViewModel.getLoanList() == null || mViewModel.getLoanList().size()<=0) {
            pageCurrentIndex = 0;
            mViewModel = new LoanAccountListModel();
            queryLoanData();

        }else{
            pageCurrentIndex = mViewModel.getLoanList().size();
            View headerView = View.inflate(mContext, R.layout.boc_view_loan_repay_history_title, null);
            mRepayPlanListView.addHeaderView(headerView);
            refreshListView();
        }

    }



    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_load_hostory_record_title);
    }


    //刷新页面数据数据
    private void refreshViewData(LoanAccountListModel repayHistoryViewModel){
        mViewModel.setRecordNumber(repayHistoryViewModel.getRecordNumber());
        if( mViewModel.getLoanList() == null){
            mViewModel.setLoanList(new LinkedList<LoanAccountListModel.PsnLOANListEQueryBean>());
        }
        List<LoanAccountListModel.PsnLOANListEQueryBean> list =  repayHistoryViewModel.getLoanList();
        for(int i = 0; i < list.size(); i ++){
            mViewModel.getLoanList().add(list.get(i));
        }
    }

    //刷新listView
    private void refreshListView(){
        if(mViewModel == null || mViewModel.getLoanList() == null){
            return;
        }
        List<HostoryRecordBean> listBean = new LinkedList<HostoryRecordBean>();

        List<LoanAccountListModel.PsnLOANListEQueryBean> viewModelList = mViewModel.getLoanList();
        for(int i = 0; i < viewModelList.size(); i ++){
            HostoryRecordBean bean = new HostoryRecordBean();
            LoanAccountListModel.PsnLOANListEQueryBean modelBean = viewModelList.get(i);
            //贷款品种,
            String firstUpInfo = PublicCodeUtils.getLoanTypeName(mContext,modelBean.getLoanType());
            bean.setFirstLineTopInfo(firstUpInfo);
            //到日期（放贷日期）
            String firstDownInfo = modelBean.getLoanDate();
            bean.setFirstLineBottomInfo(firstDownInfo);
            //币种 人民币元
            String secLeftInfo = DataUtils.getCurrencyDescByLetter(mContext,modelBean.getCurrencyCode());
            bean.setSecondLineLeftInfo(secLeftInfo);
            //金额：循环 核准金额；非循环 累计放款金额（贷款金额）
            String  secRightInfo = "";
            if ("R".equals(modelBean.getCycleType())){
                secRightInfo = MoneyUtils.transMoneyFormat(modelBean.getLoanCycleAppAmount(),modelBean.getCurrencyCode());
            } else{
                secRightInfo = MoneyUtils.transMoneyFormat(modelBean.getLoanCycleAdvVal(),modelBean.getCurrencyCode());
            }
            bean.setSecondLinerightInfo(secRightInfo);
            listBean.add(bean);
        }
        mRepayPlanListView.setData(listBean);
    }

    @Override
    public void setListener() {
        //上拉加载
        pullToRefreshLayout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                //当已经加载过数据，并且已加载数据数目大于等于总数时，不在请求数据
                boolean bCanLoadMore = !(mViewModel.getLoanList() != null
                        && mViewModel.getLoanList().size() >= mViewModel.getRecordNumber());
                if(bCanLoadMore){
                    isQueryByPullToRefresh = true;
                    queryLoanData();
                }
                else{
                    pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                }
            }
        });

        mRepayPlanListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gotoRecordDetail(position);
            }
        });

    }


    private void gotoRecordDetail(int position){
        if(mViewModel != null && position >=1 && position <= mViewModel.getLoanList().size()) {
            Bundle bundle = new Bundle();
            bundle.putString(LoanCosnt.LOAN_END_DATE,mViewModel.getEndDate());
            bundle.putSerializable(LoanCosnt.LOAN_DATA,mViewModel.getLoanList().get(position-1));
            HostoryLoanRecordDetail hostoryLoanRecordDetail = new HostoryLoanRecordDetail();
            hostoryLoanRecordDetail.setArguments(bundle);
            start(hostoryLoanRecordDetail);
        }

    }


    /***
     * 052接口请求数据
     */
    private void queryLoanData(){
        mViewModel.seteFlag("Y");
        mViewModel.seteLoanState("10");//非签约额度
        mViewModel.setPageSize(pageSize);
        mViewModel.setCurrentIndex(pageCurrentIndex);
//        if (StringUtil.isNullOrEmpty(mViewModel.getConversationId()) == false){
//            getPresenter().setmSconversationId(mViewModel.getSconversationId());
//        }
        if (pageCurrentIndex == 0) {
            mViewModel.set_refresh("true");
        } else {
            mViewModel.set_refresh("false");
        }
        getPresenter().queryLoanAccount(mViewModel);

    }

    @Override
    public void onDestroyView() {
        getPresenter().unsubscribe();
        super.onDestroyView();
    }

    @Override
    public void queryLoanAccountSuccess(LoanAccountListModel repayHistoryViewModel) {

    }

    @Override
    public void queryLoanSettledFail(BiiResultErrorException biiResultErrorException) {
        bIsRequesting = false;
        //已经返回过数据，本次是通过上拉刷新方式加载的数据
        if (biiResultErrorException.getErrorCode().equals("validation.no.relating.acc")) {
            if(isQueryByPullToRefresh){
                pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
            }
            else{
                showNoData();
            }
        } else {
            showErrorDialog(biiResultErrorException.getMessage());

        }
        closeProgressDialog();

    }

    @Override
    public void queryLoanSettledSuccess(LoanAccountListModel repayHistoryViewModel) {
        bIsRequesting = false;
        closeProgressDialog();

        if(pageCurrentIndex < 0){
            pageCurrentIndex = 0;
        }

        //是否无数据
        boolean bNoData = (repayHistoryViewModel == null || repayHistoryViewModel.getLoanList() == null ||
                repayHistoryViewModel.getLoanList().size() <= 0);

        //无返回记录
        if(bNoData){
            if(isQueryByPullToRefresh){
                pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
            }
            else{
                //非下拉刷新加载，即第一次请求数据为空
                showNoData();
            }
        }
        else{//有记录返回
            refreshViewData(repayHistoryViewModel);
            pageCurrentIndex = mViewModel.getLoanList().size();

            if(isQueryByPullToRefresh){
                pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
            }
            else{
                //第一次请求，添加listview头
                if (mRepayPlanListView.getHeaderViewsCount() <= 0) {
                    View headerView = View.inflate(mContext, R.layout.boc_view_loan_repay_history_title, null);
                    mRepayPlanListView.addHeaderView(headerView);
                }
            }
            refreshListView();
        }

    }

    @Override
    public void queryLoanAccountFail(BiiResultErrorException biiResultErrorException) {

    }



    /**
     * 展示无数据情况
     */
    private void showNoData(){
        tvNodata.setVisibility(View.VISIBLE);
        tvNodata.setText(getResources().getString(R.string.boc_load_hostory_no_record));
        pullToRefreshLayout.setVisibility(View.GONE);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected LoanMangerPresenter initPresenter() {
        return new LoanMangerPresenter(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void eloanQuoteFail(BiiResultErrorException biiResultErrorException) {

    }

    @Override
    public void eloanQuoteSuccess(List<EloanQuoteViewModel> equoteResult) {

    }


    @Override
    public void querOverdueFail(BiiResultErrorException biiResultErrorException) {

    }

    @Override
    public void queryOverdueSuccess(LoanOverdueModel loanOverdue) {

    }

    @Override
    public void queryCardNumByAcctNumFail(BiiResultErrorException biiResultErrorException) {

    }

    @Override
    public void queryCardNumByAcctNumSuccess(String cardNum) {

    }

    @Override
    public void setPresenter(LoanMangerContract.Presenter presenter) {

    }


}
