package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdAppertainTranQuery.PsnCrcdAppertainTranQueryResult;
import com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdCurrencyQuery.PsnCrcdCurrencyQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DateTimePicker;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.SlipMenu.SlipDrawerLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectStringListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttimerangenew.SelectTimeRangeNew;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.PinnedSectionListView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.ShowListConst;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.adapter.ShowListAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.bean.ShowListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.model.AttCardModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.model.AttCardSetUpModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.model.AttCardTradeDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.presenter.AttCardTradeDetailContract;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.attachedcardmanagement.presenter.AttCardTradeDetailPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Name: liukai
 * Time：2016/12/5 9:13.
 * Created by lk7066 on 2016/12/5.
 * It's used to 附属卡交易明细页面
 */

/**
 * 接口调用说明：该页面需要调用币种查询接口和明细查询接口
 * 并行查询，币种查询结果用于流量设置和短信设置，在此页面一次性查询，防止重复查询
 * 如果查询币种失败，那么流量设置和短信设置再次查询，如果成功，则直接传值使用
 * 该页面与币种查询接口结果无关
 * */

public class AttCardTranHistoryFragment extends MvpBussFragment<AttCardTradeDetailContract.AttCardTradeDetailPresenter> implements AttCardTradeDetailContract.AttCardTradeDetailView, View.OnClickListener {

    //在这里因为头部随着整体可以滑动，所以需要一个headView作为列表的头部
    private View mRootView = null;
    private View headView = null;
    private AttCardTradeDetailContract.AttCardTradeDetailPresenter mPresenter;

    //自定义标题
    private TitleBarView attCardTitle;

    private SelectStringListDialog setUp;//设置的选择弹框

    private ShowListAdapter mAdapter;//查询列表组件的适配器
    private List<ShowListBean> mList;// 查询列表组件的数据集合
    private int currentIndex = 0;// 当前页起始索引，初始化应为0？？？还是1？？？网银端是1，待查证
    private boolean isPullToRefresh = false;// 是否上拉加载

    //无数据的TextView
    private TextView tvNoData;

    // 侧滑
    private SlipDrawerLayout mAttCardDrawerLayout;
    //左侧时间范围提示语
    private TextView timeRange;
    //提示语是否显示标志
    private int flag = 1;
    // 时间筛选组件
    private SelectTimeRangeNew attCardSelectTimeRangeNew;
    // “筛选”按钮
    private TextView attCardSelect;
    // 起始时间
    private LocalDate startLocalDate;
    // 结束时间
    private LocalDate endLocalDate;

    //判断是否是筛选
    private boolean isSelectData;

    // 上拉刷新
    private PullToRefreshLayout attCardRefreshLayout;
    // 查询列表组件
    private PinnedSectionListView attCardPinnedSectionListView;

    private AttCardTradeDetailModel detailModel;
    private AttCardModel attCardModel;
    private AttCardSetUpModel setUpModel;

    //时间选择器的开始时间和结束时间，初始上送查询交易明细时间
    //下面时间为测试时间
    private String startTime = "2031/10/01";
    private String endTime = "2031/12/01";

    // 初始查询范围为1个月
    private final static int MAX_QUERY_RANGE = 1;
    // 最大查询起始日期为一年
    private final static int MAX_QUERY_DATE = 12;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        super.onCreateView(mInflater);
        attCardModel = new AttCardModel();
        attCardModel = getArguments().getParcelable("AttCard");
        attCardModel.toString();

        mRootView = mInflater.inflate(R.layout.fragment_attcard_tranflow_history, null);
        return mRootView;
    }

    @Override
    public void initView(){

        //头部
        headView = LayoutInflater.from(mContext).inflate(R.layout.fragment_attcard_tranflow_history_head, null);

        //无结果显示文字
        tvNoData = (TextView) mRootView.findViewById(R.id.no_data_attcard_query);
        //设置按钮
        attCardSelect = (TextView) headView.findViewById(R.id.tv_attcard_select);
        //标题
        attCardTitle = (TitleBarView) headView.findViewById(R.id.attcard_select_title_view);
        //筛选左侧提示语“近一个月交易”
        timeRange = (TextView) headView.findViewById(R.id.tv_attcard_detail_range);

        //滑动组件
        mAttCardDrawerLayout = (SlipDrawerLayout) mRootView.findViewById(R.id.drawer_layout_attcard_query);
        //时间筛选组件
        attCardSelectTimeRangeNew = (SelectTimeRangeNew) mRootView.findViewById(R.id.right_drawer_attcard_query);
        //时间筛选快捷选择按钮去除
        attCardSelectTimeRangeNew.isShowDateBtn(false);

        //上拉加载组件
        attCardRefreshLayout = (PullToRefreshLayout) mRootView.findViewById(R.id.refresh_attcard_query);
        //交易明细列表组件
        attCardPinnedSectionListView = (PinnedSectionListView) mRootView.findViewById(R.id.lv_attcard_query);
        attCardPinnedSectionListView.setShadowVisible(false);

        initTitleView();

        //给查询列表添加头部，包括标题和筛选一栏
        attCardPinnedSectionListView.addHeaderView(headView, null, false);
        mAdapter = new ShowListAdapter(mContext, -1);
        attCardPinnedSectionListView.saveAdapter(mAdapter);
        attCardPinnedSectionListView.setShadowVisible(false);
        attCardPinnedSectionListView.setAdapter(mAdapter);

    }

    /**
     * 初始化Title
     */
    private void initTitleView() {

        attCardTitle.setStyle(R.style.titlebar_common_white);
        attCardTitle.setTitle(getResources().getString(R.string.boc_crcd_attcard_history_title));

        //103信用卡只有一项设置
        if(attCardModel.getMasterCrcdType().equals("103")){
            attCardTitle.setRightButton(getResources().getString(R.string.boc_crcd_attcard_history_setup2));
        } else {
            attCardTitle.setRightButton(getResources().getString(R.string.boc_crcd_attcard_history_setup1));
        }

        attCardTitle.setRightButtonOnClickLinster(this);

        //左上角返回按钮
        attCardTitle.setLeftButtonOnClickLinster(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                titleLeftIconClick();
            }

        });

    }

    @Override
    public void initData(){

        detailModel = new AttCardTradeDetailModel();
        setUpModel = new AttCardSetUpModel();
        setUpModel.setAccountId(attCardModel.getMasterCrcdId());
        setUpModel.setSubCrcdNo(attCardModel.getSubCrcdNum());
        setUpModel.setCrcdNo(attCardModel.getMasterCrcdNum());
        setUpModel.setAttCardName(attCardModel.getSubCrcdHolder());
        setUpModel.setMasterCrcdType(attCardModel.getMasterCrcdType());

        mPresenter = new AttCardTradeDetailPresenter(this);

        initTimePicker();

        showLoadingDialog();

        mList = new ArrayList<ShowListBean>();

        //初始化并行查询两支接口
        getPresenter().queryCrcdCurrency(attCardModel.getMasterCrcdNum());

        queryAppertainTranDetail();

    }

    @Override
    public void setListener() {

        /**
         * 跳转至详情页面
         * */
        attCardPinnedSectionListView.setListener(new PinnedSectionListView.ClickListener() {

            @Override
            public void onItemClickListener(int position) {

                Bundle bundle = new Bundle();
                bundle.putParcelable("detail", detailModel.getList().get(position));
                bundle.putString("crcdNum", attCardModel.getMasterCrcdNum());
                bundle.putString("subNum", attCardModel.getSubCrcdNum());
                AttCardTranDetailFragment fragment = new AttCardTranDetailFragment();
                fragment.setArguments(bundle);
                start(fragment);
            }

        });

        /**
         * 筛选按钮
         * */
        attCardSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                attCardSelectTimeRangeNew.setClickSelectDefaultData();
                mAttCardDrawerLayout.toggle();
                attCardSelect.setTextColor(getResources().getColor(R.color.boc_text_color_red));
                attCardSelect.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.boc_select_red), null);
            }

        });

        /**
         * 筛选重置按钮
         * */
        attCardSelectTimeRangeNew.setResetListener(new SelectTimeRangeNew.ResetClickListener() {

            @Override
            public void resetClick() {
                initTimePicker();
                flag = 1;
            }

        });

        /**
         * 时间选择器
         * */
        attCardSelectTimeRangeNew.setListener(new SelectTimeRangeNew.ClickListener() {

            @Override
            public void startClick() {
                judgeStartTimeAndSet(LocalDate.parse(attCardSelectTimeRangeNew.getStartDate(), DateFormatters.dateFormatter1));
                flag = 0;
            }

            @Override
            public void endClick() {
                judgeEndTimeAndSet(LocalDate.parse(attCardSelectTimeRangeNew.getEndDate(), DateFormatters.dateFormatter1));
                flag = 0;
            }

            @Override
            public void cancelClick() {
                mAttCardDrawerLayout.toggle();
                flag = 0;
            }

            @Override
            public void rightClick(boolean haveSelected, String content) {
                startTime = attCardSelectTimeRangeNew.getStartDate();
                endTime = attCardSelectTimeRangeNew.getEndDate();
                startLocalDate = LocalDate.parse(attCardSelectTimeRangeNew.getStartDate(), DateFormatters.dateFormatter1);
                endLocalDate = LocalDate.parse(attCardSelectTimeRangeNew.getEndDate(), DateFormatters.dateFormatter1);

                //此处为应该进行的操作，不过避免时间判断，先注释掉
                if (PublicUtils.judgeChoiceDateRange(startLocalDate, endLocalDate, 3, AttCardTranHistoryFragment.this)) { // 判断日期是否合法

                    mList.clear();
                    currentIndex = 0;
                    isSelectData = true;
                    detailModel = new AttCardTradeDetailModel();
                    mAttCardDrawerLayout.toggle();
                    showLoadingDialog();
                    queryAppertainTranDetail();

                }
                attCardSelect.setTextColor(getResources().getColor(R.color.boc_text_color_red));
                attCardSelect.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.boc_select_red), null);

                if(1 == flag){
                    timeRange.setVisibility(View.VISIBLE);
                }

            }

        });

        /**
         * 上拉加载
         * */
        attCardRefreshLayout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

                startPresenter();//这是？？？开启presenter去调接口

                if (detailModel.getList() != null) {
                    if (detailModel.getList().size() < detailModel.getRecordNumber()) {
                        isPullToRefresh = true;
                        queryAppertainTranDetail();
                    } else {
                        attCardRefreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                    }
                } else {
                    isPullToRefresh = true;
                    mList.clear();
                    currentIndex = 0;
                    queryAppertainTranDetail();
                }

            }

        });

    }

    /**
     * 初始化时间选择器
     * */
    private void initTimePicker(){
        startLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().plusMonths(-MAX_QUERY_RANGE);
        endLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate();

        /**
         * 调整初始化时间为系统当前时间
         * */
        //startTime = startLocalDate.format(DateFormatters.dateFormatter1);
        //endTime = endLocalDate.format(DateFormatters.dateFormatter1);
        attCardSelectTimeRangeNew.setDefaultDate(startLocalDate.format(DateFormatters.dateFormatter1), endLocalDate.format(DateFormatters.dateFormatter1));
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
                attCardSelectTimeRangeNew.setStartDate(strChoiceTime);
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
                attCardSelectTimeRangeNew.setEndDate(strChoiceTime);
            }
        });
    }

    @Override
    protected String getTitleValue() {
        return "";
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
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    public boolean onBack() {
        if (mAttCardDrawerLayout.isOpen()) {
            mAttCardDrawerLayout.toggle();
            return false; // 返回false，表示当前方法处理回退事件
        } else {
            return true; // 返回true，表示当前方法没有处理回退事件
        }
    }

    @Override
    public void onDestroy() {
        mPresenter.unsubscribe();
        super.onDestroy();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * 设置按钮
     * */
    @Override
    public void onClick(View v) {

        if(attCardModel.getMasterCrcdType().equals("103")){
            Bundle bundle = new Bundle();
            bundle.putParcelable("History", setUpModel);
            AttCardMessageSetupFragment messageSetupFragment = new AttCardMessageSetupFragment();
            messageSetupFragment.setArguments(bundle);
            start(messageSetupFragment);
            return;
        }

        setUp = new SelectStringListDialog(mContext);
        List<String> condition = new ArrayList<String>();
        condition.add(getResources().getString(R.string.boc_crcd_attcard_history_setup3));
        condition.add(getResources().getString(R.string.boc_crcd_attcard_history_setup2));
        setUp.setListData(condition);
        setUp.setOnSelectListener(new SelectListDialog.OnSelectListener<String>(){

            @Override
            public void onSelect(int position, String model) {
                setUp.dismiss();
                //跳转至不同的页面
                JumpToFragment(position);
            }

        });
        setUp.show();
    }

    /**
     * 设置跳转不同fragment
     * */
    public void JumpToFragment(int position){

        Bundle bundle = new Bundle();

        if(0 == position){
            bundle.putParcelable("History", setUpModel);
            AttCardTranFlowChooseFragment chooseFragment = new AttCardTranFlowChooseFragment();
            chooseFragment.setArguments(bundle);
            start(chooseFragment);
        } else {
            bundle.putParcelable("History", setUpModel);
            AttCardMessageSetupFragment messageSetupFragment = new AttCardMessageSetupFragment();
            messageSetupFragment.setArguments(bundle);
            start(messageSetupFragment);
        }

    }

    @Override
    protected AttCardTradeDetailContract.AttCardTradeDetailPresenter initPresenter() {
        return new AttCardTradeDetailPresenter(this);
    }

    /**
     * 交易查询明细
     * 成功回调
     * */
    @Override
    public void appertainTranDetailSuccess(PsnCrcdAppertainTranQueryResult psnCrcdAppertainTranQueryResult) {

        AttCardTradeDetailModel resultModel = new AttCardTradeDetailModel();

        closeProgressDialog();
        //currentIndex += ApplicationConst.PAGE_SIZE;//索引增加50，查询完毕自增50
        //测试用10条数据
        currentIndex += 10;
        isSelectData = false;//非筛选状态
        resultModel = resultModelToViewModel(psnCrcdAppertainTranQueryResult);
        updateData(resultModel);
        if (isPullToRefresh) {
            attCardRefreshLayout.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
            isPullToRefresh = false;
        }

    }

    /**
     * 交易明细查询
     * 失败回调
     * */
    @Override
    public void appertainTranDetailFailed(BiiResultErrorException exception) {

        closeProgressDialog();
        isPullToRefresh = false;
        attCardRefreshLayout.loadmoreCompleted(PullToRefreshLayout.FAIL);

    }

    /**
     * 查询币种
     * */
    @Override
    public void crcdCurrencyQuerySuccess(PsnCrcdCurrencyQueryResult mCurrencyQuery) {

        if(mCurrencyQuery.getCurrency2() != null){
            setUpModel.setCurrency2(mCurrencyQuery.getCurrency2().getCode());
            setUpModel.setmCurrency(2);
        } else if(mCurrencyQuery.getCurrency1().getCode().equals("001")) {
            setUpModel.setmCurrency(1);
        } else {
            setUpModel.setmCurrency(0);
        }
        setUpModel.setCurrency1(mCurrencyQuery.getCurrency1().getCode());

    }

    @Override
    public void crcdCurrencyQueryFailed(BiiResultErrorException exception) {
    }

    @Override
    public void setPresenter(AttCardTradeDetailContract.AttCardTradeDetailPresenter presenter) {

    }

    /**
     * 查询交易明细
     * 此处设置上送参数
     * */
    public void queryAppertainTranDetail(){

        detailModel.setSubCrcdNo(attCardModel.getSubCrcdNum());
        detailModel.setStartData(startTime);
        detailModel.setEndData(endTime);
        detailModel.setCurrentIndex(String.valueOf(currentIndex));
        detailModel.setPageSize("10");

        String refresh = "true";
        if (currentIndex == 0) {//表示从头开始查询
            refresh = "true";
            detailModel.set_refresh(refresh);
            getPresenter().queryAppertainFirstTranDetail(detailModel);
        } else {//所以不是0，说明是继续加载，继续添加内容
            refresh = "false";
            detailModel.set_refresh(refresh);
            getPresenter().queryAppertainLoadTranDetail(detailModel);
        }



    }

    /**
     * 更新UI数据
     */
    public void updateData(AttCardTradeDetailModel mResult){

        if (mResult.getRecordNumber() == 0) {
            PublicUtils.haveDataSelectText(false, tvNoData, isSelectData);
            return;
        }

        PublicUtils.haveDataSelectText(true, tvNoData, isSelectData);

        /*复制响应数据*/
        detailModel.setRecordNumber(mResult.getRecordNumber());
        List<AttCardTradeDetailModel.ListBean> newList = mResult.getList();// 新加载的数据
        List<AttCardTradeDetailModel.ListBean> oldList = detailModel.getList();

        if (oldList == null) {
            oldList = new ArrayList<AttCardTradeDetailModel.ListBean>();
        }

        oldList.addAll(newList);
        detailModel.setList(oldList);
        mList.clear();

        for(int i = 0; i < detailModel.getList().size(); i++){

            String formatTime = "";// 当前时间 MM月/yyyy
            String tempTime = "";// 上一次时间

            LocalDate localDate = LocalDate.parse(detailModel.getList().get(i).getBookDate(), DateFormatters.dateFormatter1);

            if (localDate != null) {
                formatTime = localDate.format(DateFormatters.monthFormatter1);
            }

            if(i > 0){
                LocalDate lastDate = LocalDate.parse(detailModel.getList().get(i - 1).getBookDate(), DateFormatters.dateFormatter1);
                tempTime = lastDate.format(DateFormatters.monthFormatter1);
            }

            if (tempTime.equals(formatTime)) {// child

                ShowListBean item = new ShowListBean();
                item.type = ShowListBean.CHILD;
                item.setTitleID(ShowListConst.TITLE_LEFT_ABOVE_RIGHT_BELOW);
                item.setTime(localDate);
                item.setContentLeftAbove(detailModel.getList().get(i).getRemark());

                if(detailModel.getList().get(i).getDebitCreditFlag().equals("CRED")){
                    item.setContentRightBelow("[存入]" + " " + PublicCodeUtils.getCurrency(mContext, detailModel.getList().get(i).getBookCurrency()) + " " + MoneyUtils.transMoneyFormat(detailModel.getList().get(i).getBookAmount(), detailModel.getList().get(i).getBookCurrency()));
                } else {
                    item.setContentRightBelow(PublicCodeUtils.getCurrency(mContext, detailModel.getList().get(i).getBookCurrency()) + " " + MoneyUtils.transMoneyFormat(detailModel.getList().get(i).getBookAmount(), detailModel.getList().get(i).getBookCurrency()));
                }

                mList.add(item);

            } else {// group

                for (int j = 0; j < 2; j++) {

                    ShowListBean itemFirst = new ShowListBean();

                    if (0 == j) {
                        itemFirst.type = ShowListBean.GROUP;
                        itemFirst.setGroupName(formatTime);
                        itemFirst.setTime(localDate);
                    } else {

                        itemFirst.type = ShowListBean.CHILD;
                        itemFirst.setTitleID(ShowListConst.TITLE_LEFT_ABOVE_RIGHT_BELOW);
                        itemFirst.setTime(localDate);
                        itemFirst.setContentLeftAbove(detailModel.getList().get(i).getRemark());

                        if(detailModel.getList().get(i).getDebitCreditFlag().equals("CRED")){
                            itemFirst.setContentRightBelow("[存入]" + " " + PublicCodeUtils.getCurrency(mContext, detailModel.getList().get(i).getBookCurrency()) + " " + MoneyUtils.transMoneyFormat(detailModel.getList().get(i).getBookAmount(), detailModel.getList().get(i).getBookCurrency()));
                        } else {
                            itemFirst.setContentRightBelow(PublicCodeUtils.getCurrency(mContext, detailModel.getList().get(i).getBookCurrency()) + " " + MoneyUtils.transMoneyFormat(detailModel.getList().get(i).getBookAmount(), detailModel.getList().get(i).getBookCurrency()));
                        }

                    }

                    mList.add(itemFirst);

                }

            }

        }

        mAdapter.setData(mList);

    }

    public AttCardTradeDetailModel resultModelToViewModel(PsnCrcdAppertainTranQueryResult result){

        AttCardTradeDetailModel model = new AttCardTradeDetailModel();
        List<AttCardTradeDetailModel.ListBean> list = new ArrayList<>();
        model.setRecordNumber(result.getRecordNumber());

        for(int i = 0; i < result.getList().size(); i++){

            PsnCrcdAppertainTranQueryResult.ListBean item = result.getList().get(i);
            AttCardTradeDetailModel.ListBean listBean = new AttCardTradeDetailModel.ListBean();
            listBean.setBookAmount(item.getBookAmount());
            listBean.setBookCurrency(item.getBookCurrency());
            listBean.setBookDate(item.getBookDate());
            listBean.setDebitCreditFlag(item.getDebitCreditFlag());
            listBean.setRemark(item.getRemark());
            listBean.setTranAmount(item.getTranAmount());
            listBean.setTranCurrency(item.getTranCurrency());
            listBean.setTransDate(item.getTransDate());
            listBean.setTransCode(item.getTransCode());
            list.add(listBean);

        }
        model.setList(list);

        return model;
    }

}
