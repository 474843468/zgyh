package com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DateTimePicker;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.SlipMenu.SlipDrawerLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccountButton;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttimerangenew.SelectTimeRangeNew;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.PinnedSectionListView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.ShowListConst;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.adapter.ShowListAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.bean.ShowListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.ReloanQryConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.model.ReloanUseRecordsViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.presenter.ReloanUseRecordsPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.utils.DataUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 个人循环贷款-用款记录 页面
 * Created by liuzc on 2016/8/20.
 */
public class ReloanUseRecordsFragment extends BussFragment implements
        ReloanUseRecordsContract.View{
    private View mRootView = null;
    /**
     * 侧滑菜单
     */
    private SlipDrawerLayout mDrawerLayout;

    private LinearLayout llyHeader = null; //头部布局
    private LinearLayout llyNodata = null; //空页面布局
    private PullToRefreshLayout pullToRefreshLayout = null; //上拉刷新页面
    private PinnedSectionListView mListView = null;  //listview
    private ShowListAdapter mAdapter = null;
    private TextView tvNodata = null; //无数据提示
    private List<ShowListBean> listViewBeanList;

    private SelectTimeRangeNew rightDrawer;//侧滑菜单
    private boolean isShowDetail = true;

    /**
     * listView的Head
     */
    private View headView;
    private TitleBarView queryTitleView;
    //筛选的LinearLayout
    private LinearLayout llTransferRecordSelect;
    //筛选处的范围
    private TextView tvTransferRecordRange;
    //筛选
    private TextView tvTransferRecordSelect;
    //筛选的imageView
    private ImageView ivTransferRecordSelect;


    private ReloanUseRecordsPresenter mPresenter = null;


    private String loanAccountNumber = ""; //贷款账号
    private String currencyCode = ""; //币种，CNY格式

    //起始时间
    private LocalDate startLocalDate;
    //结束时间
    private LocalDate endLocalDate;

    //最大查询范围为3个月
    private final static int MAX_QUERY_RANGE = 3;
    //最大查询起始日期为一年
    private final static int MAX_QUERY_DATE = 12;

    private ReloanUseRecordsViewModel mViewModel = null; //页面显示数据
    //当前加载页码
    private int pageCurrentIndex = 1;
    //每页大小
    private final static int pageSize = 10;
    //是否通过上拉刷新进行的查询请求
    private boolean isQueryByPullToRefresh = false;

    private int totalRecordCount = -1; //总的记录数，在服务端第一次成功返回数据后赋值

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = View.inflate(mContext, R.layout.boc_fragment_loan_reloan_use_record_list, null);
        return mRootView;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    public void initView() {
        //侧滑菜单
        mDrawerLayout = (SlipDrawerLayout) mRootView.findViewById(R.id.layoutSlipDrawer);
        rightDrawer = (SelectTimeRangeNew) mRootView.findViewById(R.id.viewSelectTime);

        llyHeader = (LinearLayout) mRootView.findViewById(R.id.llyHeader);
        llyNodata = (LinearLayout) mRootView.findViewById(R.id.llyNodata);

        //上拉刷新
        pullToRefreshLayout = (PullToRefreshLayout) mRootView.findViewById(R.id.other_loan_online_query_view);
        tvNodata = (TextView) mRootView.findViewById(R.id.tvNoData);

        mListView = (PinnedSectionListView) mRootView.findViewById(R.id.lvUseRecord);
        mListView.setDividerHeight(0);

        //筛选
        headView = LayoutInflater.from(mContext).inflate(R.layout.boc_head_account_transdetail, null);
        //页面title初始化
        initQueryTitleView();
        //账户选择
        SelectAccountButton selectAccountButton = (SelectAccountButton) headView.findViewById(R.id.select_account_button);
        selectAccountButton.setVisibility(View.GONE);

        //筛选
        llTransferRecordSelect = (LinearLayout) headView.findViewById(R.id.ll_transdetail_select);
        tvTransferRecordSelect = (TextView) headView.findViewById(R.id.tv_transdetail_select);
        ivTransferRecordSelect = (ImageView) headView.findViewById(R.id.iv_transdetail_select);
        tvTransferRecordRange = (TextView) headView.findViewById(R.id.tv_transdetail_range);

        llyHeader.addView(headView);
//        mListView.addHeaderView(headView, null, false);
//        if (mListViewAdaper == null) {
//            mListViewAdaper = new UseRecordAdapter(mContext);
//            mListView.setAdapter(mListViewAdaper);
//        }

        mAdapter = new ShowListAdapter(mContext, -1);
        mListView.saveAdapter(mAdapter);
        mListView.setShadowVisible(false);
        mListView.setAdapter(mAdapter);
    }

    /**
     * 初始化Title
     */
    private void initQueryTitleView() {
        queryTitleView = (TitleBarView) headView.findViewById(R.id.select_title_view);
        queryTitleView.setStyle(R.style.titlebar_common_white);
        queryTitleView.setTitle(getResources().getString(R.string.boc_repayment));
        queryTitleView.setRightImgBtnVisible(false);

        queryTitleView.setLeftButtonOnClickLinster(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleLeftIconClick();
            }
        });
    }

    @Override
    public void initData() {
        Bundle bundle = getArguments();
        loanAccountNumber = bundle.getString(ReloanQryConst.KEY_ACCOUNT_NUMBER);
        currencyCode = bundle.getString(ReloanQryConst.KEY_CURRENCY);
        initShowDetail(bundle);
        mPresenter = new ReloanUseRecordsPresenter(this);
        mViewModel = new ReloanUseRecordsViewModel();
        pageCurrentIndex = 1;

        startLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().plusMonths(-MAX_QUERY_RANGE).plusDays(1);
        endLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate();
        rightDrawer.setDefaultDate(startLocalDate.format(DateFormatters.dateFormatter1),
                endLocalDate.format(DateFormatters.dateFormatter1));
        tvTransferRecordRange.setText(getResources().getString(R.string.boc_common_query_month_3));
        listViewBeanList = new ArrayList<>();
        queryUseRecords();
    }

    private void initShowDetail(Bundle bundle){
        if(bundle != null && bundle.containsKey(ReloanQryConst.SHOW_DETAIL)){
            isShowDetail = bundle.getBoolean(ReloanQryConst.SHOW_DETAIL);
        }else {
            isShowDetail = true;
        }
    }

    //执行查询操作
    private void queryUseRecords() {
        showLoadingDialog();
        ReloanUseRecordsViewModel model = new ReloanUseRecordsViewModel();

        model.setPageSize(String.format("%s", pageSize));
        model.setCurrentIndex(String.format("%s", pageCurrentIndex));
        model.setLoanActNum(loanAccountNumber);
        model.setStartDate(startLocalDate.format(DateFormatters.dateFormatter1));
        model.setEndDate(endLocalDate.format(DateFormatters.dateFormatter1));

        mPresenter.queryUseRecords(model);
    }

    //刷新页面数据
    private void refreshViewData(ReloanUseRecordsViewModel model) {
        if (mViewModel.getResult() == null) {
            mViewModel.setResult(new LinkedList<ReloanUseRecordsViewModel.PsnLOANUseRecordsQueryBean>());
        }
        List<ReloanUseRecordsViewModel.PsnLOANUseRecordsQueryBean> list = model.getResult();
        for (int i = 0; i < list.size(); i++) {
            if (totalRecordCount < 0) {
                totalRecordCount = list.get(i).getTotnumq();
            }
            mViewModel.getResult().add(list.get(i));
        }
    }



    @Override
    public void setListener() {
        //筛选layout的点击事件
        llTransferRecordSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectStatustChange();
                rightDrawer.setClickSelectDefaultData();
                mDrawerLayout.toggle();
            }
        });

        //筛选--侧滑菜单
        rightDrawer.setListener(new SelectTimeRangeNew.ClickListener() {
            @Override
            public void startClick() {
                judgeStartTimeAndSet(LocalDate.parse(rightDrawer.getStartDate(), DateFormatters.dateFormatter1));
            }

            @Override
            public void endClick() {
                judgeEndTimeAndSet(LocalDate.parse(rightDrawer.getEndDate(), DateFormatters.dateFormatter1));
            }

            @Override
            public void cancelClick() {
                mDrawerLayout.toggle();
            }

            @Override
            public void rightClick(boolean haveSelected, String content) {
                if (haveSelected) {
                    tvTransferRecordRange.setText(content + "查询结果");
                } else {
                    tvTransferRecordRange.setText("");
                }


                String startTime = rightDrawer.getStartDate();
                String endTime = rightDrawer.getEndDate();
                startLocalDate = LocalDate.parse(startTime, DateFormatters.dateFormatter1);
                endLocalDate = LocalDate.parse(endTime, DateFormatters.dateFormatter1);
                if (PublicUtils.judgeChoiceDateRange(startLocalDate, endLocalDate, MAX_QUERY_RANGE, ReloanUseRecordsFragment.this)) {
                    mDrawerLayout.toggle();
                    if (mViewModel != null && mViewModel.getResult() != null) {
                        mViewModel.setResult(null);
                    }
                    listViewBeanList.clear();
                    pageCurrentIndex = 1;
                    isQueryByPullToRefresh = false;
                    totalRecordCount = -1;
                    queryUseRecords();
                }
            }
        });

//        //筛选重置按钮的监听
//        rightDrawer.setResetListener(new SelectTimeRangeNew.ResetClickListener() {
//            @Override
//            public void resetClick() {
//                rightDrawer.setDefaultDate(startLocalDate.format(DateFormatters.dateFormatter1),
//                        endLocalDate.format(DateFormatters.dateFormatter1));
//            }
//        });


        //上拉加载
        pullToRefreshLayout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                //当已经加载过数据，并且已加载数据数目大于等于总数时，不在请求数据
                boolean bCanLoadMore = !(mViewModel.getResult() != null
                        && mViewModel.getResult().size() >= totalRecordCount);

                if (totalRecordCount < 0) {
                    bCanLoadMore = true;
                }
                if (bCanLoadMore) {
                    isQueryByPullToRefresh = true;
                    queryUseRecords();
                } else {
                    pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                }
            }
        });
        if (isShowDetail) {
            mListView.setListener(new PinnedSectionListView.ClickListener(){

                @Override
                public void onItemClickListener(int position) {
                    startDetailFragment(position);
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        mPresenter.unsubscribe();
        super.onDestroyView();
        listViewBeanList.clear();
    }


    @Override
    public void queryUseRecordsSuccess(ReloanUseRecordsViewModel viewModel) {
        mDrawerLayout.setVisibility(View.VISIBLE);
        closeProgressDialog();

        if (pageCurrentIndex < 0) {
            pageCurrentIndex = 0;
        }

        //是否无数据
        boolean bNoData = (viewModel == null || viewModel.getResult() == null ||
                viewModel.getResult().size() <= 0);

        //无返回记录
        if (bNoData) {
            if (isQueryByPullToRefresh) {
                pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
            } else {
                //非下拉刷新加载，即第一次请求数据为空
                showNoData();
            }
        } else {//有记录返回
            refreshViewData(viewModel);
            pageCurrentIndex++;

            if (isQueryByPullToRefresh) {
                pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
            }
            //refreshListView();
            copyResult2ShowListBean(mViewModel);
            mAdapter.setData(listViewBeanList);
            pullToRefreshLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void queryUseRecordsFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        //已经返回过数据，本次是通过上拉刷新方式加载的数据
        if (isQueryByPullToRefresh) {
            pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
        } else {
            showNoData();
        }
    }

    /**
     * 展示无数据情况
     */
    private void showNoData() {
//        llyNodata.setVisibility(View.VISIBLE);
//        tvNodata.setVisibility(View.VISIBLE);
//        tvNodata.setText(getResources().getString(R.string.boc_loan_no_use_record));
//        llTransferRecordSelect.setVisibility(View.GONE); //筛选部分隐藏
//        tvTransferRecordRange.setVisibility(View.GONE);
        pullToRefreshLayout.setVisibility(View.GONE);
    }

    @Override
    public void setPresenter(ReloanUseRecordsContract.Presenter presenter) {

    }

    /**
     * 起始日期的选择
     */
    private void judgeStartTimeAndSet(LocalDate currentDate) {
        DateTimePicker.showDatePick(mContext, currentDate, DateFormatters.dateFormatter1, new DateTimePicker.DatePickCallBack() {
            @Override
            public void onChoiceDateSet(String strChoiceTime, LocalDate choiceDate) {
                if (ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().isBefore(choiceDate)) {
                    showErrorDialog(getResources().getString(R.string.boc_account_transdetail_start_before));
                    return;
                }
                if (PublicUtils.isCompareDateRange(choiceDate, ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate(), MAX_QUERY_DATE)) {
                    showErrorDialog(getResources().getString(R.string.boc_account_transdetail_start_year_change, PublicUtils.changeNumberToUpper(MAX_QUERY_DATE / 12)));
                    return;
                }
                rightDrawer.setStartDate(strChoiceTime);
            }
        });
    }

    /**
     * 结束日期的选择
     */
    private void judgeEndTimeAndSet(LocalDate currentDate) {
        DateTimePicker.showDatePick(mContext, currentDate, DateFormatters.dateFormatter1, new DateTimePicker.DatePickCallBack() {
            @Override
            public void onChoiceDateSet(String strChoiceTime, LocalDate choiceDate) {
                if (ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().isBefore(choiceDate)) {
                    showErrorDialog(getResources().getString(R.string.boc_account_transdetail_end_before));
                    return;
                }
                if (PublicUtils.isCompareDateRange(choiceDate, ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate(), MAX_QUERY_DATE)) {
                    showErrorDialog(getResources().getString(R.string.boc_account_transdetail_end_year_change, PublicUtils.changeNumberToUpper(MAX_QUERY_DATE / 12)));
                    return;
                }
                rightDrawer.setEndDate(strChoiceTime);
            }
        });
    }


    /**
     * 筛选按钮的颜色改变
     */
    private void selectStatustChange() {
        tvTransferRecordSelect.setTextColor(getResources().getColor(R.color.boc_text_color_red));
        ivTransferRecordSelect.setImageResource(R.drawable.boc_select_red);
    }


    /**
     * 将接口返回数据封装成ListView所需要的model - 新接口
     *
     * @param reloadUseRecordViewModel
     */

    private void copyResult2ShowListBean(ReloanUseRecordsViewModel reloadUseRecordViewModel) {
        listViewBeanList.clear();
        List<ReloanUseRecordsViewModel.PsnLOANUseRecordsQueryBean> viewModelList = reloadUseRecordViewModel.getResult();
        for (int i = 0; i < viewModelList.size(); i++){
            String loanApplyDate = viewModelList.get(i).getLoanApplyDate();//"2016/12/14"
            LocalDate localDate = LocalDate.parse(loanApplyDate,DateFormatters.dateFormatter1);//"2016/12/14"

            String formatTime = localDate.format(DateFormatters.monthFormatter1);//"MM月/yyyy"
            String tempFormatTime = "";//上一次时间
            if (i > 0){
                String tempTime = viewModelList.get(i - 1).getLoanApplyDate();
                LocalDate FormatTime = LocalDate.parse(tempTime,DateFormatters.dateFormatter1);
                tempFormatTime = FormatTime.format(DateFormatters.monthFormatter1);

            }

            //交易描述
            String transDetail = viewModelList.get(i).getMerchant();
            if(StringUtils.isEmptyOrNull(transDetail)){
                transDetail = "-";
            }
            if (formatTime.equals(tempFormatTime)){
                ShowListBean item = new ShowListBean();
                item.type = ShowListBean.CHILD;
                item.setTitleID(ShowListConst.TITLE_LEFT_1_RIGHT_2);
                item.setTime(localDate);

                item.setContentLeftAbove(transDetail);//交易详情
                item.setContentRightAbove(MoneyUtils.transMoneyFormat(viewModelList.get(i).getLoanApplyAmount(),currencyCode));//金额
                item.setContentRightBelow(DataUtils.getCurrencyDescByLetter(mContext,currencyCode));//币种
                listViewBeanList.add(item);
            } else{
                for (int j = 0; j < 2; j++){
                    ShowListBean itemFirst = new ShowListBean();
                    if (j == 0){
                        itemFirst.type = ShowListBean.GROUP;
                        itemFirst.setGroupName(formatTime);
                        itemFirst.setTime(localDate);
                    }else {
                        itemFirst.type = ShowListBean.CHILD;
                        itemFirst.setTitleID(ShowListConst.TITLE_LEFT_1_RIGHT_2);
                        itemFirst.setTime(localDate);
                        itemFirst.setContentLeftAbove(transDetail);//交易详情
                        itemFirst.setContentRightAbove(MoneyUtils.transMoneyFormat(viewModelList.get(i).getLoanApplyAmount(),currencyCode));//金额
                        itemFirst.setContentRightBelow(DataUtils.getCurrencyDescByLetter(mContext,currencyCode));//币种
                    }
                    listViewBeanList.add(itemFirst);
                }
            }
        }

    }



    private void startDetailFragment( int position) {
        if(mAdapter == null || mAdapter.getCount() <= position || mViewModel.getResult() == null || mViewModel.getResult().size() <= position){
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(ReloanQryConst.KEY_RELOAD_USER_RECORD_BEAN,
                mViewModel.getResult().get(position));
        bundle.putString(ReloanQryConst.KEY_CURRENCY,currencyCode);
        ReloadUseRecordDetailFragment detailFragment = new ReloadUseRecordDetailFragment();
        detailFragment.setArguments(bundle);
        start(detailFragment);
    }

}
