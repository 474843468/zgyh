package com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DateTimePicker;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.model.AccountListItemViewModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.SlipMenu.SlipDrawerLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccountButton;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttimerangenew.SelectTimeRangeNew;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.PinnedSectionListView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.ShowListConst;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.adapter.ShowListAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.showlistview.bean.ShowListBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.Content;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.SelectGridView;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.DetailModelUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.model.AccountDetailViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.model.FinanceICTransferViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.model.InquiryRangeQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.model.MedicalTransferDetailQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.model.TransDetailViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.presenter.TransDetailPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Period;

import java.util.ArrayList;
import java.util.List;

/**
 * 交易明细
 * Created by wangf on 2016/6/3.
 */
public class TransDetailFragment extends MvpBussFragment<TransDetailContract.Presenter> implements TransDetailContract.View {

    /**
     * 根布局
     */
    private View mRootView;

    /**
     * fragment的页面
     */
    //详情的ListView
    PinnedSectionListView transListView;
    ShowListAdapter mAdapter;
    //详情的上拉刷新
    PullToRefreshLayout transRefresh;
    //侧滑菜单
    SelectTimeRangeNew rightDrawer;
    //无数据的TextView
    private TextView tvNoData;


    /**
     * listView的Head
     */
    private View headView;
    private TitleBarView queryTitleView;
    //账户选择
    private SelectAccountButton selectAccountButton;
    //头部的筛选Layout
    private RelativeLayout rlHeadSelectLayout;
    //筛选处额外添加的layout
    private LinearLayout llAddToLayout;
    //筛选处的范围
    private TextView tvTransDetailRange;
    //筛选按钮的LinearLayout
    private LinearLayout llTransdetailSelect;
    //筛选按钮的文字
    private TextView tvTransdetailSelect;
    //筛选按钮的imageView
    private ImageView ivTransdetailSelect;

    /**
     * 币种 钞汇的筛选
     */
    //币种筛选的Layout
    private LinearLayout llSelectTransCurrency;
    //钞汇筛选的Layout
    private LinearLayout llSelectTransCashRemit;
    //币种的筛选
    private SelectGridView selectTransCurrencyView;
    //钞汇的筛选
    private SelectGridView selectTransCashRemitView;
    //币种筛选的数据
    private List<Content> selectCurrencyList;
    //钞汇筛选的数据
    private List<Content> selectCashRemitList;
    //筛选出的当前币种信息
    private String currentCurrency;
    private String lastCurrency;
    //筛选出的当前钞汇信息
    private String currentCashRemit;
    private String lastCashRemit;

    /**
     * 侧滑菜单
     */
    private SlipDrawerLayout mDrawerLayout;

//    //交易明细service通信处理类
//    private TransDetailPresenter mTransDetailPresenter;
    //交易明细UI层model -- 活期
    private TransDetailViewModel mTransDetailViewModel;
    //交易明细UI层model -- 医保账户
    private MedicalTransferDetailQueryViewModel mMedicalTransferDetailQueryViewModel;
    //交易明细UI层model -- 电子现金账户
    private FinanceICTransferViewModel mFinanceICTransferViewModel;

    //起始时间
    private LocalDate startLocalDate;
    //结束时间
    private LocalDate endLocalDate;

    //所选择的账户
    private AccountBean accountSelectBean;

    // 币种数据
    private List<AccountListItemViewModel.CardAmountViewModel> currencyList;

    /**
     * 加载相关
     */
    //当前加载页码
    private int pageCurrentIndex;
    //电子现金账户的加载页码
    private int pageCurrentIndexFinance;
    //每页大小
    private static int pageSize;
    private String _refresh = "false";

    /**
     * 查询范围相关
     */
    //默认查询范围（月）
    private static int DEFAULT_QUERY_RANGE = 3;
    //最大查询范围（月）
    private static int MAX_QUERY_RANGE = 6;
    //最长查询时间（月）
    private static int MAX_QUERY_DATE = 24;

    //ListView所需要的List
    private List<ShowListBean> listViewBeanList;
    //账户类型列表
    private ArrayList<String> accountTypeList;

    //判断是否是筛选
    private boolean isSelectData;
    //判断是否是上拉加载
    private boolean isPullToRefresh;


    /**
     * 页面跳转所需要的参数
     */
    public static final String DETAIL_TYPE = "DetailType";
    public static final String DETAIL_INFO = "DetailInfo";
    public static final String DETAIL_ACCOUNT_BEAN = "DetailAccountBean";
    public static final String DETAIL_START_DATE = "DetailStartDate";
    public static final String DETAIL_END_DATE = "DetailEndDate";

    //从大红头进入的交易明细
    public static final int DETAIL_ACCOUNT_TYPE_ALL = 0;
    //普通单账户明细
    public static final int DETAIL_ACCOUNT_TYPE_COMMON = 1;
    //医保账户明细
    public static final int DETAIL_ACCOUNT_TYPE_MEDICAL = 2;
    //电子现金账户明细
    public static final int DETAIL_ACCOUNT_TYPE_FINANCE = 3;

    //当前所查询账户详情类型
    private int currentDetailAccountType;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = View.inflate(mContext, R.layout.boc_fragment_transfer_query, null);

        currentDetailAccountType = getArguments().getInt(DETAIL_TYPE, DETAIL_ACCOUNT_TYPE_ALL);

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
        //fragment页面
        transListView = (PinnedSectionListView) mRootView.findViewById(R.id.lv_transfer_query);
        transRefresh = (PullToRefreshLayout) mRootView.findViewById(R.id.refresh_transfer_query);
        tvNoData = (TextView) mRootView.findViewById(R.id.no_data_transfer_query);

        headView = LayoutInflater.from(mContext).inflate(R.layout.boc_head_account_transdetail, null);

        //页面title初始化
        initQueryTitleView();

        //账户选择
        selectAccountButton = (SelectAccountButton) headView.findViewById(R.id.select_account_button);
        //头部的筛选Layout
        rlHeadSelectLayout = (RelativeLayout) headView.findViewById(R.id.select_head_select_rl);

        //筛选
        llTransdetailSelect = (LinearLayout) headView.findViewById(R.id.ll_transdetail_select);
        tvTransdetailSelect = (TextView) headView.findViewById(R.id.tv_transdetail_select);
        ivTransdetailSelect = (ImageView) headView.findViewById(R.id.iv_transdetail_select);
        tvTransDetailRange = (TextView) headView.findViewById(R.id.tv_transdetail_range);

        //侧滑菜单及筛选内容
        mDrawerLayout = (SlipDrawerLayout) mRootView.findViewById(R.id.drawer_layout_transfer_query);
        rightDrawer = (SelectTimeRangeNew) mRootView.findViewById(R.id.right_drawer_transfer_query);

        //筛选中的币种和钞汇相关
        View selectTransLayout = View.inflate(mContext, R.layout.boc_view_trans_select_currency_cashremit, null);
        llSelectTransCurrency = (LinearLayout) selectTransLayout.findViewById(R.id.select_currency_layout);
        llSelectTransCashRemit = (LinearLayout) selectTransLayout.findViewById(R.id.select_cashremit_layout);
        selectTransCurrencyView = (SelectGridView) selectTransLayout.findViewById(R.id.select_single_currency);
        selectTransCashRemitView = (SelectGridView) selectTransLayout.findViewById(R.id.select_single_cashremit);
        llAddToLayout = rightDrawer.getAddToLayout();
        llAddToLayout.addView(selectTransLayout);

        judgeAccountType2VisibleCurrency(false);

        transListView.addHeaderView(headView, null, false);
        mAdapter = new ShowListAdapter(mContext, -1);
        transListView.saveAdapter(mAdapter);
        transListView.setShadowVisible(false);
        transListView.setAdapter(mAdapter);

    }

    /**
     * 初始化Title
     */
    private void initQueryTitleView() {
        queryTitleView = (TitleBarView) headView.findViewById(R.id.select_title_view);
        queryTitleView.setStyle(R.style.titlebar_common_white);
        queryTitleView.setTitle(R.string.boc_account_transdetail_title);
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
        pageSize = ApplicationConst.PAGE_SIZE;
        pageCurrentIndex = 0;

        //初始化币种和钞汇数据
        currentCurrency = ApplicationConst.CURRENCY_CNY;
        currentCashRemit = "";

        //币种钞汇信息的筛选
        selectCurrencyList = new ArrayList<Content>();
        selectCashRemitList = new ArrayList<Content>();

        currencyList = new ArrayList<AccountListItemViewModel.CardAmountViewModel>();

//        mTransDetailPresenter = new TransDetailPresenter(this);
        accountSelectBean = new AccountBean();
        listViewBeanList = new ArrayList<ShowListBean>();//ListView所需要的数据集合

        //去掉筛选时的快捷日期选项
        rightDrawer.isShowDateBtn(false);

        //根据上一个页面传来数据判断所要查询的明细类型
        judgeDetailTypeAndInitData();

    }

    /**
     * 判断当前的查询类型并初始化数据
     */
    private void judgeDetailTypeAndInitData() {
        switch (currentDetailAccountType) {
            case DETAIL_ACCOUNT_TYPE_ALL://大红头进入的全部查询

                mTransDetailViewModel = new TransDetailViewModel();
                //初始化查询范围数据
                initDetailAllQueryRangeData();

                accountTypeList = new ArrayList<String>();
                accountTypeList.add(ApplicationConst.ACC_TYPE_ORD);//普通活期
                accountTypeList.add(ApplicationConst.ACC_TYPE_BRO);//借记卡
                accountTypeList.add(ApplicationConst.ACC_TYPE_RAN);//活一本
                accountTypeList.add(ApplicationConst.ACC_TYPE_BOCINVT);//网上专属理财
                accountTypeList.add(ApplicationConst.ACC_TYPE_CBQX);//存本取息
                accountTypeList.add(ApplicationConst.ACC_TYPE_ZOR);//零存整取
                accountTypeList.add(ApplicationConst.ACC_TYPE_EDU);//教育储蓄
                accountTypeList.add(ApplicationConst.ACC_TYPE_YOUHUITONG);//优汇通专户

                List<AccountBean> accountBeanList = ApplicationContext.getInstance().getChinaBankAccountList(accountTypeList);
                if (accountBeanList.size() > 0) {
                    accountSelectBean = accountBeanList.get(0);
                    selectAccountButton.setData(accountSelectBean);
                } else {
                    judgeNoAccountHeadVisible(false);
                    return;
                }

//                judgeAccountType2VisibleSelect(accountSelectBean);

                //查询该账户的币种信息
                showLoadingDialog();
                getPresenter().queryAccountQueryAccountDetail(accountSelectBean.getAccountId());

//                //调用接口，查询最长时间范围
//                queryInquiryRange();

                break;
            case DETAIL_ACCOUNT_TYPE_COMMON://普通单账户查询

                mTransDetailViewModel = new TransDetailViewModel();
                accountSelectBean = getArguments().getParcelable(DETAIL_ACCOUNT_BEAN);
                selectAccountButton.setData(accountSelectBean);
                selectAccountButton.setArrowVisible(false);
                selectAccountButton.setEnabled(false);

//                judgeAccountType2VisibleSelect(accountSelectBean);

                //初始化查询范围数据
                initDetailCommonQueryRangeData();

                //查询该账户的币种信息
                showLoadingDialog();
                getPresenter().queryAccountQueryAccountDetail(accountSelectBean.getAccountId());
//                //调用接口，查询交易明细
//                queryTransDetailList();

                break;
            case DETAIL_ACCOUNT_TYPE_MEDICAL://医保账户明细查询

                mMedicalTransferDetailQueryViewModel = new MedicalTransferDetailQueryViewModel();
                accountSelectBean = getArguments().getParcelable(DETAIL_ACCOUNT_BEAN);
                selectAccountButton.setData(accountSelectBean);
                selectAccountButton.setArrowVisible(false);
                selectAccountButton.setEnabled(false);

                //初始化查询范围数据
                initDetailCommonQueryRangeData();

                //查询该账户的币种信息
                showLoadingDialog();
                getPresenter().queryAccountQueryAccountDetail(accountSelectBean.getAccountId());

//                //调用接口，查询交易明细
//                queryMedicalInsurAcctTransferlList();

                break;
            case DETAIL_ACCOUNT_TYPE_FINANCE://电子现金账户明细查询

                MAX_QUERY_RANGE = 3;//电子现金账户的最大查询时间范围为3个月
                MAX_QUERY_DATE = 12;//最长查询时间为1年

                pageCurrentIndexFinance = 1;

                mFinanceICTransferViewModel = new FinanceICTransferViewModel();
                accountSelectBean = getArguments().getParcelable(DETAIL_ACCOUNT_BEAN);
                selectAccountButton.setData(accountSelectBean);
                selectAccountButton.setArrowVisible(false);
                selectAccountButton.setEnabled(false);

                //电子现金账户无币种和钞汇的筛选
                judgeAccountType2VisibleCurrency(false);

                //初始化查询范围数据
                startLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().plusMonths(-MAX_QUERY_RANGE).plusDays(1);
                endLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate();
                String rightDrawerStartDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().plusMonths(-MAX_QUERY_RANGE).plusDays(1).format(
                        DateFormatters.dateFormatter1);
                String rightDrawerEndDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().format(DateFormatters.dateFormatter1);
                rightDrawer.setDefaultDate(rightDrawerStartDate, rightDrawerEndDate);
                tvTransDetailRange.setText("近3个月明细");

                //调用接口，查询交易明细
                queryFinanceICTransferList();

                break;
        }
    }


    /**
     * 根据查询类型，查询交易明细
     */
    private void queryDetailListData() {
        switch (currentDetailAccountType) {
            case DETAIL_ACCOUNT_TYPE_ALL:
                //调用接口，查询最长时间范围
                queryInquiryRange();
                break;
            case DETAIL_ACCOUNT_TYPE_COMMON:
                //调用接口，查询交易明细
                queryTransDetailList();
                break;
            case DETAIL_ACCOUNT_TYPE_MEDICAL:
                //调用接口，查询交易明细
                queryMedicalInsurAcctTransferlList();
                break;
        }
    }


    /**
     * 初始化默认查询范围数据 -- 大红头进入的明细
     */
    private void initDetailAllQueryRangeData() {
        startLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().plusMonths(-MAX_QUERY_RANGE).plusDays(1);
        endLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate();
        String rightDrawerStartDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().plusMonths(-MAX_QUERY_RANGE).plusDays(1).format(DateFormatters.dateFormatter1);
        String rightDrawerEndDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().format(DateFormatters.dateFormatter1);
        rightDrawer.setDefaultDate(rightDrawerStartDate, rightDrawerEndDate);
        //去掉筛选中的近三月的勾选状态
        rightDrawer.setDefaultSelectStatus();
        tvTransDetailRange.setText("近6个月明细");
    }

    /**
     * 初始化默认查询范围数据 -- 单账户的明细
     */
    private void initDetailCommonQueryRangeData() {
        startLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().plusMonths(-MAX_QUERY_RANGE).plusDays(1);
        endLocalDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate();
        String rightDrawerStartDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().plusMonths(-MAX_QUERY_RANGE).plusDays(1).format(DateFormatters.dateFormatter1);
        String rightDrawerEndDate = ApplicationContext.getInstance().getCurrentSystemDate().toLocalDate().format(DateFormatters.dateFormatter1);
        rightDrawer.setDefaultDate(rightDrawerStartDate, rightDrawerEndDate);
        //去掉筛选中的近三月的勾选状态
        rightDrawer.setDefaultSelectStatus();
        tvTransDetailRange.setText("近6个月明细");

        String startDate = getArguments().getString(DETAIL_START_DATE);
        String endDate = getArguments().getString(DETAIL_END_DATE);
        if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
            startLocalDate = LocalDate.parse(startDate, DateFormatters.dateFormatter1);
            endLocalDate = LocalDate.parse(endDate, DateFormatters.dateFormatter1);
            //去掉筛选中的近三月的勾选状态
            rightDrawer.setDefaultSelectStatus();

            Period period = startLocalDate.until(endLocalDate);
            if (period.getMonths() == 6) {
                tvTransDetailRange.setText("近6个月明细");
            }
        }
    }

    /**
     * 初始化币种和钞汇的筛选数据
     */
    private void initSelectView() {
        //钞汇数据信息
        String[] selectCashRemitNameData = {"现钞", "现汇"};
        String[] selectCashRemitIdData = {"01", "02"};

        if (currencyList.size() != 0) {
            judgeAccountType2VisibleCurrency(true);
        }

        changeCurrencyList();

        //币种数据的初始化
        for (int i = 0; i < currencyList.size(); i++) {
            Content item = new Content();
            item.setName(PublicCodeUtils.getCurrency(mContext, currencyList.get(i).getCurrencyCode()));
            item.setContentNameID(currencyList.get(i).getCurrencyCode());
            if (i == 0) {
                currentCurrency = currencyList.get(i).getCurrencyCode();
                lastCurrency = currentCurrency;
                item.setSelected(true);
            }
            //若该条数据的币种信息已经在selectCurrencyList中存在，则不需要再次添加该条数据
            if (!judgeCurrencyListRepeat(item)) {
                selectCurrencyList.add(item);
            }
        }
        selectTransCurrencyView.setData(selectCurrencyList);
        llSelectTransCurrency.setVisibility(View.VISIBLE);

        //钞汇数据的初始化
        for (int i = 0; i < selectCashRemitNameData.length; i++) {
            Content item = new Content();
            item.setName(selectCashRemitNameData[i]);
            item.setContentNameID(selectCashRemitIdData[i]);
            if (i == 0) {
                currentCashRemit = selectCashRemitIdData[i];
                lastCashRemit = currentCashRemit;
                item.setSelected(true);
            }
            selectCashRemitList.add(item);
        }
        selectTransCashRemitView.setData(selectCashRemitList);


        //若币种为人民币,则不需要显示钞汇信息
        if (ApplicationConst.CURRENCY_CNY.equals(currentCurrency)) {
            currentCashRemit = "";
            llSelectTransCashRemit.setVisibility(View.GONE);
        } else {
            llSelectTransCashRemit.setVisibility(View.VISIBLE);
        }

        //普活账户 医保账户 电子现金账户 零存整取 教育储蓄  存本取息 无币种和钞汇的筛选
        if (currentDetailAccountType == DETAIL_ACCOUNT_TYPE_MEDICAL ||
                currentDetailAccountType == DETAIL_ACCOUNT_TYPE_FINANCE ||
                ApplicationConst.ACC_TYPE_ORD.equals(accountSelectBean.getAccountType()) ||
                ApplicationConst.ACC_TYPE_ZOR.equals(accountSelectBean.getAccountType()) ||
                ApplicationConst.ACC_TYPE_EDU.equals(accountSelectBean.getAccountType()) ||
                ApplicationConst.ACC_TYPE_CBQX.equals(accountSelectBean.getAccountType())) {
            judgeAccountType2VisibleCurrency(false);
        }

        //查询交易明细数据
        queryDetailListData();
    }


    /**
     * 判断币种数据是否重复
     *
     * @return
     */
    private boolean judgeCurrencyListRepeat(Content item) {
        boolean isRepeat = false;
        for (int i = 0; i < selectCurrencyList.size(); i++) {
            if (item.getContentNameID().equals(selectCurrencyList.get(i).getContentNameID())) {
                isRepeat = true;
                break;
            }
        }
        return isRepeat;
    }



    /**
     * 若该账户中存在人民币的币种，则需要把人民币放在币种的第一位
     */
    private void changeCurrencyList(){
        AccountListItemViewModel.CardAmountViewModel cardAmountViewModel;
        for (int i = 0; i < currencyList.size(); i++){
            if (ApplicationConst.CURRENCY_CNY.equals(currencyList.get(i).getCurrencyCode())){
                cardAmountViewModel = currencyList.get(i);
                currencyList.remove(i);
                currencyList.add(0, cardAmountViewModel);
                return;
            }
        }
    }


    @Override
    public void setListener() {
        //选择账户layout
        selectAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入选择账户页面
                startForResult(SelectAccoutFragment.newInstance(accountTypeList), SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT);
            }
        });

        //筛选layout的点击事件
        llTransdetailSelect.setOnClickListener(new View.OnClickListener() {
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
                handleRightClickData(haveSelected, content);
            }

        });
        //筛选 -- 重置按钮的监听
        rightDrawer.setResetListener(new SelectTimeRangeNew.ResetClickListener() {
            @Override
            public void resetClick() {
                // modify by wangf on 2016-12-4 10:39:24 组件控制重置的状态
//                rightDrawer.setClickSelectDefaultData();
                resetCurrencyAndCashRemitSelected();
            }
        });

        //listView的item点击事件
        transListView.setListener(new PinnedSectionListView.ClickListener() {
            @Override
            public void onItemClickListener(int position) {
                Bundle bundle = new Bundle();
                TransDetailInfoFragment detailInfoFragment = new TransDetailInfoFragment();
                switch (currentDetailAccountType) {
                    case DETAIL_ACCOUNT_TYPE_ALL:// 所有详情--活期交易明细
                        bundle.putInt(DETAIL_TYPE, DETAIL_ACCOUNT_TYPE_ALL);
                        bundle.putParcelable(DETAIL_INFO, mTransDetailViewModel.getList().get(position));
                        detailInfoFragment.setArguments(bundle);
                        start(detailInfoFragment);
                        break;
                    case DETAIL_ACCOUNT_TYPE_COMMON:// 单账户的活期
                        bundle.putInt(DETAIL_TYPE, DETAIL_ACCOUNT_TYPE_COMMON);
                        bundle.putParcelable(DETAIL_INFO, mTransDetailViewModel.getList().get(position));
                        detailInfoFragment.setArguments(bundle);
                        start(detailInfoFragment);
                        break;
                    case DETAIL_ACCOUNT_TYPE_MEDICAL:// 医保账户
                        bundle.putInt(DETAIL_TYPE, DETAIL_ACCOUNT_TYPE_MEDICAL);
                        bundle.putParcelable(DETAIL_INFO, mMedicalTransferDetailQueryViewModel.getList().get(position));
                        detailInfoFragment.setArguments(bundle);
                        start(detailInfoFragment);
                        break;
                    case DETAIL_ACCOUNT_TYPE_FINANCE:// 电子现金账户 -- 不需进入详情页面
//                        bundle.putInt(DETAIL_TYPE, DETAIL_ACCOUNT_TYPE_FINANCE);
//                        bundle.putParcelable(DETAIL_INFO, mFinanceICTransferViewModel.getListBeen().get(position));
//                        detailInfoFragment.setArguments(bundle);
//                        start(detailInfoFragment);
                        break;
                }
            }
        });

        //上拉加载
        transRefresh.setOnLoadListener(new PullToRefreshLayout.OnLoadListener()

        {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                startPresenter();
                handleOnLoadMoreData();
            }
        });

        //币种的筛选
        selectTransCurrencyView.setListener(new SelectGridView.ClickListener()

        {
            @Override
            public void setItemClick(AdapterView<?> parent, View view, int position, long id) {
                Content selectCurrency = (Content) parent.getAdapter().getItem(position);
                currentCurrency = selectCurrency.getContentNameID();
                if (ApplicationConst.CURRENCY_CNY.equals(selectCurrency.getContentNameID())) {
                    currentCashRemit = "";
                    llSelectTransCashRemit.setVisibility(View.GONE);
                } else {
                    llSelectTransCashRemit.setVisibility(View.VISIBLE);
                }
                cancelCashRemitSelected();
            }
        });
    }


    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (SelectAccoutFragment.REQUEST_CODE_SELECT_ACCOUNT == requestCode) {
            if (resultCode == SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT) {
                accountSelectBean = data.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
                selectAccountButton.setData(accountSelectBean);

                rlHeadSelectLayout.setVisibility(View.VISIBLE);
                transRefresh.setMove(true);
                tvNoData.setVisibility(View.GONE);
                tvNoData.setText("");

                //判断账户类型，是否需要显示筛选按钮
//                judgeAccountType2VisibleSelect(accountSelectBean);
                //初始化筛选为默认状态
                judgeAccountType2VisibleCurrency(false);

                listViewBeanList.clear();
                if (mTransDetailViewModel.getList() != null) {
                    mTransDetailViewModel.getList().clear();
                }
                pageCurrentIndex = 0;
                _refresh = "true";
                isSelectData = false;
                isPullToRefresh = false;

                selectCurrencyList.clear();
                selectCashRemitList.clear();

                //查询该账户的币种信息
                showLoadingDialog();
                getPresenter().queryAccountQueryAccountDetail(accountSelectBean.getAccountId());
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    /**
     * 判断账户类型，用于是否显示筛选按钮
     * 当账户类型为以下账户时，不显示筛选
     * ApplicationConst.ACC_TYPE_ZOR //零存整取
     * ApplicationConst.ACC_TYPE_EDU //教育储蓄
     * ApplicationConst.ACC_TYPE_CBQX // 存本取息
     *
     * @param accountBean
     */
    private void judgeAccountType2VisibleSelect(AccountBean accountBean) {
        if (ApplicationConst.ACC_TYPE_ZOR.equals(accountBean.getAccountType()) ||
                ApplicationConst.ACC_TYPE_EDU.equals(accountBean.getAccountType()) ||
                ApplicationConst.ACC_TYPE_CBQX.equals(accountBean.getAccountType())) {
            llTransdetailSelect.setVisibility(View.GONE);
        } else {
            llTransdetailSelect.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 判断账户类型，用于是否显示筛选组件中的币种和钞汇
     * 普活 医保账户 电子现金账户 由于账户的币种为单一币种 因此不需要筛选币种和钞汇
     */
    private void judgeAccountType2VisibleCurrency(boolean isVisible) {
        if (isVisible) {
            llSelectTransCurrency.setVisibility(View.VISIBLE);
            llSelectTransCashRemit.setVisibility(View.VISIBLE);
        } else {
            llSelectTransCurrency.setVisibility(View.GONE);
            llSelectTransCashRemit.setVisibility(View.GONE);
        }
    }


    /**
     * 没有关联账号时页面显示规则
     *
     * @param isVisible
     */
    private void judgeNoAccountHeadVisible(boolean isVisible) {
        if (isVisible) {
            rlHeadSelectLayout.setVisibility(View.VISIBLE);
            selectAccountButton.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.GONE);
        } else {
            rlHeadSelectLayout.setVisibility(View.GONE);
            selectAccountButton.setVisibility(View.GONE);
            transRefresh.setMove(false);
            tvNoData.setVisibility(View.VISIBLE);
            tvNoData.setText(getResources().getString(R.string.boc_account_transdetail_no_account));
        }
    }


    /**
     * 处理点击筛选中的确认后的数据
     */
    private void handleRightClickData(boolean haveSelected, String content) {
        //页面筛选范围的显示
//        if (haveSelected) {
//            tvTransDetailRange.setText(content + "查询结果");
//        } else {
//            tvTransDetailRange.setText("");
//        }
        tvTransDetailRange.setText("");

        //筛选时间的判断
        String startTime = rightDrawer.getStartDate();
        String endTime = rightDrawer.getEndDate();
        startLocalDate = LocalDate.parse(startTime, DateFormatters.dateFormatter1);
        endLocalDate = LocalDate.parse(endTime, DateFormatters.dateFormatter1);
        if (!PublicUtils.judgeChoiceDateRange(startLocalDate, endLocalDate, MAX_QUERY_RANGE, TransDetailFragment.this)) {
            return;
        }

        judgeSelectCurrency();
        judgeSelectCashRemit();
        lastCashRemit = currentCashRemit;
        lastCurrency = currentCurrency;

        //根据当前账户类型请求数据
        switch (currentDetailAccountType) {
            case DETAIL_ACCOUNT_TYPE_ALL:// 所有详情--活期交易明细

            case DETAIL_ACCOUNT_TYPE_COMMON:// 单账户的活期
                //筛选币种和钞汇的判断
                if (ApplicationConst.CURRENCY_CNY.equals(currentCurrency)) {
                    currentCashRemit = "";
                } else {
                    if (!judgeSelectCashRemit()) {
                        showErrorDialog("请选择钞汇");
                        return;
                    }
                }
                //筛选合法时，重置数据，调用接口
                mDrawerLayout.toggle();
                listViewBeanList.clear();
                if (mTransDetailViewModel.getList() != null) {
                    mTransDetailViewModel.getList().clear();
                }
                pageCurrentIndex = 0;
                _refresh = "true";
                isSelectData = true;
                isPullToRefresh = false;
                queryTransDetailList();

                break;
            case DETAIL_ACCOUNT_TYPE_MEDICAL:// 医保账户
                //筛选币种和钞汇的判断
                if (ApplicationConst.CURRENCY_CNY.equals(currentCurrency)) {
                    currentCashRemit = "";
                } else {
                    if (!judgeSelectCashRemit()) {
                        showErrorDialog("请选择钞汇");
                        return;
                    }
                }
                //筛选合法时，重置数据，调用接口
                mDrawerLayout.toggle();
                listViewBeanList.clear();
                if (mMedicalTransferDetailQueryViewModel.getList() != null) {
                    mMedicalTransferDetailQueryViewModel.getList().clear();
                }
                pageCurrentIndex = 0;
                _refresh = "true";
                isSelectData = true;
                isPullToRefresh = false;
                queryMedicalInsurAcctTransferlList();
                break;
            case DETAIL_ACCOUNT_TYPE_FINANCE:// 电子现金账户

                //筛选合法时，重置数据，调用接口
                mDrawerLayout.toggle();
                listViewBeanList.clear();
                if (mFinanceICTransferViewModel.getListBeen() != null) {
                    mFinanceICTransferViewModel.getListBeen().clear();
                }
                pageCurrentIndexFinance = 1;
                _refresh = "true";
                isSelectData = true;
                isPullToRefresh = false;
                queryFinanceICTransferList();
                break;
        }

    }


    /**
     * 处理上拉加载更多的数据
     */
    private void handleOnLoadMoreData() {
        //根据当前账户类型请求数据
        switch (currentDetailAccountType) {
            case DETAIL_ACCOUNT_TYPE_ALL:// 所有详情--活期交易明细

            case DETAIL_ACCOUNT_TYPE_COMMON:// 单账户的活期

                if (mTransDetailViewModel.getList() != null) {
                    if (mTransDetailViewModel.getList().size() < mTransDetailViewModel.getRecordNumber()) {
                        isPullToRefresh = true;
                        queryTransDetailList();
                    } else {
                        transRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                    }
                } else {
                    listViewBeanList.clear();
                    pageCurrentIndex = 0;
                    _refresh = "true";
                    isPullToRefresh = true;
                    queryTransDetailList();
                }

                break;
            case DETAIL_ACCOUNT_TYPE_MEDICAL:// 医保账户
                if (mMedicalTransferDetailQueryViewModel.getList() != null) {
                    if (mMedicalTransferDetailQueryViewModel.getList().size() < mMedicalTransferDetailQueryViewModel.getRecordNumber()) {
                        isPullToRefresh = true;
                        queryMedicalInsurAcctTransferlList();
                    } else {
                        transRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                    }
                } else {
                    listViewBeanList.clear();
                    pageCurrentIndex = 0;
                    _refresh = "true";
                    isPullToRefresh = true;
                    queryMedicalInsurAcctTransferlList();
                }
                break;
            case DETAIL_ACCOUNT_TYPE_FINANCE:// 电子现金账户
                if (mFinanceICTransferViewModel.getListBeen() != null) {
                    if (mFinanceICTransferViewModel.getListBeen().size() < mFinanceICTransferViewModel.getRecordNumber()) {
                        isPullToRefresh = true;
                        queryFinanceICTransferList();
                    } else {
                        transRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                    }
                } else {
                    listViewBeanList.clear();
                    pageCurrentIndexFinance = 1;
                    _refresh = "true";
                    isPullToRefresh = true;
                    queryFinanceICTransferList();
                }
                break;
        }
    }


    /**
     * 判断 钞汇 的筛选
     *
     * @return
     */
    private boolean judgeSelectCashRemit() {
        boolean haveSelected = false;
        for (Content item : selectCashRemitList) {
            if (item.getSelected()) {
                haveSelected = true;
                currentCashRemit = item.getContentNameID();
                break;
            }
        }
        return haveSelected;
    }

    /**
     * 判断 币种 的筛选
     *
     * @return
     */
    private boolean judgeSelectCurrency() {
        boolean haveSelected = false;
        for (Content item : selectCurrencyList) {
            if (item.getSelected()) {
                haveSelected = true;
                currentCurrency = item.getContentNameID();
                break;
            }
        }
        return haveSelected;
    }


    /**
     * 重置为上次筛选的结果
     */
    private void resetCurrencyAndCashRemitSelected() {
        //若为电子现金账户，则无币种和钞汇的的筛选
        if (DETAIL_ACCOUNT_TYPE_FINANCE == currentDetailAccountType) {
            return;
        }
        for (int i = 0; i < selectCurrencyList.size(); i++) {
            Content item = selectCurrencyList.get(i);
            if (item.getContentNameID().equals(lastCurrency)) {
                item.setSelected(true);
            } else {
                item.setSelected(false);
            }
        }
        selectTransCurrencyView.getAdapter().notifyDataSetChanged();

        for (int i = 0; i < selectCashRemitList.size(); i++) {
            Content item = selectCashRemitList.get(i);
            if (item.getContentNameID().equals(lastCashRemit)) {
                item.setSelected(true);
            } else {
                item.setSelected(false);
            }
        }
        selectTransCashRemitView.getAdapter().notifyDataSetChanged();


        if (ApplicationConst.CURRENCY_CNY.equals(lastCurrency)) {
            currentCashRemit = "";
            llSelectTransCashRemit.setVisibility(View.GONE);
        } else {
            llSelectTransCashRemit.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 重置钞汇的选中
     */
    private void cancelCashRemitSelected() {
        for (int i = 0; i < selectCashRemitList.size(); i++) {
            Content item = selectCashRemitList.get(i);
            if (i == 0) {
                item.setSelected(true);
            } else {
                item.setSelected(false);
            }
        }
        selectTransCashRemitView.getAdapter().notifyDataSetChanged();
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
        tvTransdetailSelect.setTextColor(getResources().getColor(R.color.boc_text_color_red));
        ivTransdetailSelect.setImageResource(R.drawable.boc_select_red);
    }

    /** ----------------------------------------查询最大跨度和最长时间范围 数据处理 开始------------------------------------- */

    /**
     * 调用接口，查询最大跨度和最长时间范围
     */
    private void queryInquiryRange() {
        showLoadingDialog();
        getPresenter().queryInquiryRange();
    }

    /**
     * 查询最大跨度和最长时间范围 成功的数据处理
     *
     * @param rangeQueryViewModel
     */
    private void handleInquiryRangeSuccessData(InquiryRangeQueryViewModel rangeQueryViewModel) {
        MAX_QUERY_DATE = rangeQueryViewModel.getMaxYears() * 12;
        MAX_QUERY_RANGE = rangeQueryViewModel.getMaxMonths();

        //查询时间范围成功后，调用接口查询默认时间范围内的数据
        queryTransDetailList();
    }

    /**
     * 查询最大跨度和最长时间范围 失败的数据处理
     *
     * @param biiResultErrorException
     */
    private void handleInquiryRangeFailData(BiiResultErrorException biiResultErrorException) {
        showErrorDialog(biiResultErrorException.getErrorMessage());
    }

    /** ----------------------------------------查询最大跨度和最长时间范围 数据处理 结束------------------------------------- */

    /** ----------------------------------------------活期查询 交易明细 数据处理 开始---------------------------------------------- */

    /**
     * 调用接口，查询 交易明细 数据 -- 活期
     */
    private void queryTransDetailList() {
        if (pageCurrentIndex == 0) {
            //开启加载对话框
            showLoadingDialog();
        }
        getPresenter().queryTransDetailList(buildTransDetailViewModel());
    }


    /**
     * 封装页面数据 -- 活期
     *
     * @return
     */
    private TransDetailViewModel buildTransDetailViewModel() {
        mTransDetailViewModel.setAccountId(accountSelectBean.getAccountId());//账户标识
        mTransDetailViewModel.setCurrency(currentCurrency);//币种
        mTransDetailViewModel.setCashRemit(currentCashRemit);//钞汇
        mTransDetailViewModel.setStartDate(startLocalDate.format(DateFormatters.dateFormatter1));//起始日期
        mTransDetailViewModel.setEndDate(endLocalDate.format(DateFormatters.dateFormatter1));//结束日期
        mTransDetailViewModel.set_refresh(_refresh);//是否重新查询结果
        mTransDetailViewModel.setPageSize(pageSize);//页面大小
        mTransDetailViewModel.setCurrentIndex(pageCurrentIndex);//当前页索引
        return mTransDetailViewModel;
    }


    /**
     * 将接口返回数据封装成ListView所需要的model -- 活期
     *
     * @param transDetailViewModel
     */
    private void copyTransResult2TransBean(TransDetailViewModel transDetailViewModel) {
        listViewBeanList.clear();
        for (int i = 0; i < transDetailViewModel.getList().size(); i++){
            LocalDate localDate = transDetailViewModel.getList().get(i).getPaymentDate();
            String formatTime = "";//当前时间 MM月/yyyy
            String tempTime = "";//上一次时间
            if (localDate != null){
                formatTime = localDate.format(DateFormatters.monthFormatter1);
            }
            if (i > 0){
                tempTime = transDetailViewModel.getList().get(i - 1).getPaymentDate().format(DateFormatters.monthFormatter1);
            }
            if (tempTime.equals(formatTime)){
                ShowListBean item = new ShowListBean();
                item.type = ShowListBean.CHILD;

                item.setTitleID(ShowListConst.TITLE_DATE_TYPE);
                item.setTime(localDate);
                item.setChangeColor(true);
                item.setContentLeftAbove(transDetailViewModel.getList().get(i).getBusinessDigest());
                item.setContentRightAbove(MoneyUtils.transMoneyFormat(transDetailViewModel.getList().get(i).getAmount(), transDetailViewModel.getList().get(i).getCurrency()));
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

                        itemFirst.setTitleID(ShowListConst.TITLE_DATE_TYPE);
                        itemFirst.setTime(localDate);
                        itemFirst.setChangeColor(true);
                        itemFirst.setContentLeftAbove(transDetailViewModel.getList().get(i).getBusinessDigest());
                        itemFirst.setContentRightAbove(MoneyUtils.transMoneyFormat(transDetailViewModel.getList().get(i).getAmount(), transDetailViewModel.getList().get(i).getCurrency()));
                    }
                    listViewBeanList.add(itemFirst);
                }
            }
        }

//
//
//        for (int i = 0; i < transDetailViewModel.getList().size(); i++) {
//            TransactionBean transactionBean = new TransactionBean();
//
//            LocalDate paymentDate = transDetailViewModel.getList().get(i).getPaymentDate();
//
//            transactionBean.setTitleID(TransactionView.TITLE_DATE_TYPE);
//            transactionBean.setChangeColor(true);
//            transactionBean.setTime(paymentDate);
//            transactionBean.setContentLeftAbove(transDetailViewModel.getList().get(i).getBusinessDigest());
//            transactionBean.setContentRightBelow(MoneyUtils.transMoneyFormat(transDetailViewModel.getList().get(i).getAmount(), transDetailViewModel.getList().get(i).getCurrency()));
//
//            listViewBeanList.add(transactionBean);
//        }
    }


    /**
     * 处理请求成功后的数据 -- 活期
     *
     * @param transDetailViewModel
     */
    private void handleTransDetailListSuccessData(TransDetailViewModel transDetailViewModel) {
        mTransDetailViewModel.setRecordNumber(transDetailViewModel.getRecordNumber());
        if (transDetailViewModel.getList().size() != 0) {
            List<TransDetailViewModel.ListBean> listBeen = new ArrayList<TransDetailViewModel.ListBean>();
            if (mTransDetailViewModel.getList() != null) {
                listBeen.addAll(mTransDetailViewModel.getList());
            }
            listBeen.addAll(transDetailViewModel.getList());
            mTransDetailViewModel.setList(listBeen);

            copyTransResult2TransBean(mTransDetailViewModel);


            if (pageCurrentIndex == 0) {
                //关闭加载对话框
                closeProgressDialog();
            }
            if (isPullToRefresh) {
                transRefresh.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
            }
//            haveDataSelectStatust(true);
            PublicUtils.haveDataSelectText(true, tvNoData, isSelectData);

        } else {
            if (pageCurrentIndex == 0) {
                //关闭加载对话框
                closeProgressDialog();
//                haveDataSelectStatust(false);
                PublicUtils.haveDataSelectText(false, tvNoData, isSelectData);
            } else {
//                haveDataSelectStatust(true);
                PublicUtils.haveDataSelectText(true, tvNoData, isSelectData);
            }
            if (isPullToRefresh) {
                transRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
            }
        }
        //将所有数据处理完后为ListView设置数据
        mAdapter.setData(listViewBeanList);
        pageCurrentIndex += pageSize;//此处的索引是按记录的索引来走
        _refresh = "false";
    }

    /**
     * 处理请求失败后的数据 -- 活期
     *
     * @param biiResultErrorException
     */
    private void handleTransDetailListFailData(BiiResultErrorException biiResultErrorException) {
        if (pageCurrentIndex == 0) {
            //关闭加载对话框
            closeProgressDialog();
        } else {
            transRefresh.loadmoreCompleted(PullToRefreshLayout.FAIL);
        }
        //将所有数据处理完后为ListView设置数据
        mAdapter.setData(listViewBeanList);
        if ("CTIS.0204".equals(biiResultErrorException.getErrorCode())) {
            PublicUtils.haveDataSelectText(false, tvNoData, isSelectData);
            transRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
        } else {
            showErrorDialog(biiResultErrorException.getErrorMessage());
        }
    }

    /** ----------------------------------------------活期查询 交易明细 数据处理 结束---------------------------------------------- */

    /** ----------------------------------------------医保账户 交易明细 数据处理 开始---------------------------------------------- */

    /**
     * 调用接口，查询 交易明细 数据 -- 医保账户
     */
    private void queryMedicalInsurAcctTransferlList() {
        if (pageCurrentIndex == 0) {
            //开启加载对话框
            showLoadingDialog();
        }
        getPresenter().queryMedicalTransferList(buildMedicalTransferViewModel());
    }

    /**
     * 封装页面数据 -- 医保账户
     *
     * @return
     */
    private MedicalTransferDetailQueryViewModel buildMedicalTransferViewModel() {
        mMedicalTransferDetailQueryViewModel.setAccountId(accountSelectBean.getAccountId());//账户标识
        mMedicalTransferDetailQueryViewModel.setCurrency(currentCurrency);//币种
        mMedicalTransferDetailQueryViewModel.setCashRemit(currentCashRemit);//钞汇
        mMedicalTransferDetailQueryViewModel.setStartDate(startLocalDate.format(DateFormatters.dateFormatter1));//起始日期
        mMedicalTransferDetailQueryViewModel.setEndDate(endLocalDate.format(DateFormatters.dateFormatter1));//结束日期
        mMedicalTransferDetailQueryViewModel.set_refresh(_refresh);//是否重新查询结果
        mMedicalTransferDetailQueryViewModel.setPageSize(pageSize);//页面大小
        mMedicalTransferDetailQueryViewModel.setCurrentIndex(pageCurrentIndex);//当前页索引
        return mMedicalTransferDetailQueryViewModel;
    }


    /**
     * 将接口返回数据封装成ListView所需要的model -- 医保账户
     *
     * @param mMedicalViewModel
     */
    private void copyMedicalResult2TransBean(MedicalTransferDetailQueryViewModel mMedicalViewModel) {
        listViewBeanList.clear();
        for (int i = 0; i < mMedicalViewModel.getList().size(); i++){
            LocalDate localDate = mMedicalViewModel.getList().get(i).getPaymentDate();
            String formatTime = "";//当前时间 MM月/yyyy
            String tempTime = "";//上一次时间
            if (localDate != null){
                formatTime = localDate.format(DateFormatters.monthFormatter1);
            }
            if (i > 0){
                tempTime = mMedicalViewModel.getList().get(i - 1).getPaymentDate().format(DateFormatters.monthFormatter1);
            }
            if (tempTime.equals(formatTime)){
                ShowListBean item = new ShowListBean();
                item.type = ShowListBean.CHILD;

                item.setTitleID(ShowListConst.TITLE_DATE_TYPE);
                item.setTime(localDate);
                item.setChangeColor(true);
                item.setContentLeftAbove(mMedicalViewModel.getList().get(i).getBusinessDigest());
                item.setContentRightAbove(MoneyUtils.transMoneyFormat(mMedicalViewModel.getList().get(i).getAmount(), mMedicalViewModel.getList().get(i).getCurrency()));
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

                        itemFirst.setTitleID(ShowListConst.TITLE_DATE_TYPE);
                        itemFirst.setTime(localDate);
                        itemFirst.setChangeColor(true);
                        itemFirst.setContentLeftAbove(mMedicalViewModel.getList().get(i).getBusinessDigest());
                        itemFirst.setContentRightAbove(MoneyUtils.transMoneyFormat(mMedicalViewModel.getList().get(i).getAmount(), mMedicalViewModel.getList().get(i).getCurrency()));
                    }
                    listViewBeanList.add(itemFirst);
                }
            }
        }


//        for (int i = 0; i < mMedicalViewModel.getList().size(); i++) {
//            TransactionBean transactionBean = new TransactionBean();
//
//            LocalDate paymentDate = mMedicalViewModel.getList().get(i).getPaymentDate();
//
//            transactionBean.setTitleID(TransactionView.TITLE_DATE_TYPE);
//            transactionBean.setChangeColor(true);
//            transactionBean.setTime(paymentDate);
//            transactionBean.setContentLeftAbove(mMedicalViewModel.getList().get(i).getBusinessDigest());
//            transactionBean.setContentRightBelow(MoneyUtils.transMoneyFormat(mMedicalViewModel.getList().get(i).getAmount(), mMedicalViewModel.getList().get(i).getCurrency()));
//
//            listViewBeanList.add(transactionBean);
//        }
    }


    /**
     * 处理请求成功后的数据 -- 医保账户
     *
     * @param transDetailViewModel
     */
    private void handleMedicalInsurAcctTransferlListSuccessData(MedicalTransferDetailQueryViewModel transDetailViewModel) {
        mMedicalTransferDetailQueryViewModel.setRecordNumber(transDetailViewModel.getRecordNumber());
        if (transDetailViewModel.getList().size() != 0) {
            List<MedicalTransferDetailQueryViewModel.ListBean> listBeen = new ArrayList<MedicalTransferDetailQueryViewModel.ListBean>();
            if (mMedicalTransferDetailQueryViewModel.getList() != null) {
                listBeen.addAll(mMedicalTransferDetailQueryViewModel.getList());
            }
            listBeen.addAll(transDetailViewModel.getList());
            mMedicalTransferDetailQueryViewModel.setList(listBeen);

            copyMedicalResult2TransBean(mMedicalTransferDetailQueryViewModel);


            if (pageCurrentIndex == 0) {
                //关闭加载对话框
                closeProgressDialog();
            }
            if (isPullToRefresh) {
                transRefresh.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
            }
//            haveDataSelectStatust(true);
            PublicUtils.haveDataSelectText(true, tvNoData, isSelectData);

        } else {
            if (pageCurrentIndex == 0) {
                //关闭加载对话框
                closeProgressDialog();
//                haveDataSelectStatust(false);
                PublicUtils.haveDataSelectText(false, tvNoData, isSelectData);
            } else {
//                haveDataSelectStatust(true);
                PublicUtils.haveDataSelectText(true, tvNoData, isSelectData);
            }
            if (isPullToRefresh) {
                transRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
            }
        }
        //将所有数据处理完后为ListView设置数据
        mAdapter.setData(listViewBeanList);
        pageCurrentIndex += pageSize;//此处的索引是按记录的索引来走
        _refresh = "false";
    }

    /**
     * 处理请求失败后的数据 -- 医保账户
     *
     * @param biiResultErrorException
     */
    private void handleMedicalInsurAcctTransferlListFailData(BiiResultErrorException biiResultErrorException) {
        if (pageCurrentIndex == 0) {
            //关闭加载对话框
            closeProgressDialog();
        } else {
            transRefresh.loadmoreCompleted(PullToRefreshLayout.FAIL);
        }
        //将所有数据处理完后为ListView设置数据
        mAdapter.setData(listViewBeanList);
        if ("CTIS.0204".equals(biiResultErrorException.getErrorCode())) {
            PublicUtils.haveDataSelectText(false, tvNoData, isSelectData);
            transRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
        } else {
            showErrorDialog(biiResultErrorException.getErrorMessage());
        }
    }

    /** ------------------------------------------医保账户 交易明细 数据处理 结束------------------------------------------ */

    /** -----------------------------------------电子现金账户 交易明细 数据处理 开始---------------------------------------- */

    /**
     * 调用接口，查询 交易明细 数据 -- 电子现金账户
     */
    private void queryFinanceICTransferList() {
        if (pageCurrentIndexFinance == 1) {
            //开启加载对话框
            showLoadingDialog();
        }
        getPresenter().queryFinanceICTransferList(buildFinanceICTransferViewModel());
    }

    /**
     * 封装页面数据 -- 电子现金账户
     *
     * @return
     */
    private FinanceICTransferViewModel buildFinanceICTransferViewModel() {
        mFinanceICTransferViewModel.setAccountId(accountSelectBean.getAccountId());//账户ID
        mFinanceICTransferViewModel.setStartDate(startLocalDate.format(DateFormatters.dateFormatter1));//起始日期
        mFinanceICTransferViewModel.setEndDate(endLocalDate.format(DateFormatters.dateFormatter1));//结束日期
        mFinanceICTransferViewModel.setPageSize(pageSize);//页面大小
        mFinanceICTransferViewModel.setCurrentIndex(pageCurrentIndexFinance);//当前页索引
        return mFinanceICTransferViewModel;
    }

    /**
     * 将接口返回数据封装成ListView所需要的model -- 电子现金账户
     *
     * @param mFinanceViewModel
     */
    private void copyFinanceResult2TransBean(FinanceICTransferViewModel mFinanceViewModel) {
        listViewBeanList.clear();
        for (int i = 0; i < mFinanceViewModel.getListBeen().size(); i++){
            LocalDate localDate = mFinanceViewModel.getListBeen().get(i).getReturnDate();
            String formatTime = "";//当前时间 MM月/yyyy
            String tempTime = "";//上一次时间
            if (localDate != null){
                formatTime = localDate.format(DateFormatters.monthFormatter1);
            }
            if (i > 0){
                tempTime = mFinanceViewModel.getListBeen().get(i - 1).getReturnDate().format(DateFormatters.monthFormatter1);
            }
            if (tempTime.equals(formatTime)){
                ShowListBean item = new ShowListBean();
                item.type = ShowListBean.CHILD;

                item.setTitleID(ShowListConst.TITLE_DATE_TYPE);
                item.setTime(localDate);
                item.setChangeColor(true);
                item.setContentLeftAbove(DetailModelUtils.getTransferTypeString(mFinanceViewModel.getListBeen().get(i).getTransferType()));
                String content = "";
                if (mFinanceViewModel.getListBeen().get(i).isAmountFlag())
                    content = "-";
                item.setContentRightAbove(content + MoneyUtils.transMoneyFormat(mFinanceViewModel.getListBeen().get(i).getAmount(), mFinanceViewModel.getListBeen().get(i).getCurrency()));
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

                        itemFirst.setTitleID(ShowListConst.TITLE_DATE_TYPE);
                        itemFirst.setTime(localDate);
                        itemFirst.setChangeColor(true);
                        itemFirst.setContentLeftAbove(DetailModelUtils.getTransferTypeString(mFinanceViewModel.getListBeen().get(i).getTransferType()));
                        String content = "";
                        if (mFinanceViewModel.getListBeen().get(i).isAmountFlag())
                            content = "-";
                        itemFirst.setContentRightAbove(content + MoneyUtils.transMoneyFormat(mFinanceViewModel.getListBeen().get(i).getAmount(), mFinanceViewModel.getListBeen().get(i).getCurrency()));
                    }
                    listViewBeanList.add(itemFirst);
                }
            }
        }


//        for (int i = 0; i < mFinanceViewModel.getListBeen().size(); i++) {
//            TransactionBean transactionBean = new TransactionBean();
//
//            LocalDate paymentDate = mFinanceViewModel.getListBeen().get(i).getReturnDate();
//
//            transactionBean.setTitleID(TransactionView.TITLE_DATE_TYPE);
//            transactionBean.setChangeColor(true);
//            transactionBean.setTime(paymentDate);
//            transactionBean.setContentLeftAbove(DetailModelUtils.getTransferTypeString(mFinanceViewModel.getListBeen().get(i).getTransferType()));
//            String content = "";
//            if (mFinanceViewModel.getListBeen().get(i).isAmountFlag())
//                content = "-";
//            transactionBean.setContentRightBelow(content + MoneyUtils.transMoneyFormat(mFinanceViewModel.getListBeen().get(i).getAmount(), mFinanceViewModel.getListBeen().get(i).getCurrency()));
////            transactionBean.setContentRightBelow(PublicCodeUtils.getCurrency(mContext, mFinanceViewModel.getListBeen().get(i).getCurrency()) + " " + content +
////                    MoneyUtils.transMoneyFormat(mFinanceViewModel.getListBeen().get(i).getAmount(), mFinanceViewModel.getListBeen().get(i).getCurrency()));
//
//            listViewBeanList.add(transactionBean);
//        }
    }

    /**
     * 处理请求成功后的数据 -- 电子现金账户
     *
     * @param transDetailViewModel
     */
    private void handleFinanceICTransferListSuccessData(FinanceICTransferViewModel transDetailViewModel) {
        mFinanceICTransferViewModel.setRecordNumber(transDetailViewModel.getRecordNumber());
        if (transDetailViewModel.getListBeen().size() != 0) {
            List<FinanceICTransferViewModel.ListBean> listBeen = new ArrayList<FinanceICTransferViewModel.ListBean>();
            if (mFinanceICTransferViewModel.getListBeen() != null) {
                listBeen.addAll(mFinanceICTransferViewModel.getListBeen());
            }
            listBeen.addAll(transDetailViewModel.getListBeen());
            mFinanceICTransferViewModel.setListBeen(listBeen);

            copyFinanceResult2TransBean(mFinanceICTransferViewModel);


            if (pageCurrentIndexFinance == 1) {
                //关闭加载对话框
                closeProgressDialog();
            }
            if (isPullToRefresh) {
                transRefresh.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
            }
//            haveDataSelectStatust(true);
            PublicUtils.haveDataSelectText(true, tvNoData, isSelectData);

        } else {
            if (pageCurrentIndexFinance == 1) {
                //关闭加载对话框
                closeProgressDialog();
//                haveDataSelectStatust(false);
                PublicUtils.haveDataSelectText(false, tvNoData, isSelectData);
            } else {
//                haveDataSelectStatust(true);
                PublicUtils.haveDataSelectText(true, tvNoData, isSelectData);
            }
            if (isPullToRefresh) {
                transRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
            }
        }
        //将所有数据处理完后为ListView设置数据
        mAdapter.setData(listViewBeanList);
        pageCurrentIndexFinance++;//
    }

    /**
     * 处理请求失败后的数据 -- 电子现金账户
     *
     * @param biiResultErrorException
     */
    private void handleFinanceICTransferListFailData(BiiResultErrorException biiResultErrorException) {
        if (pageCurrentIndexFinance == 1) {
            //关闭加载对话框
            closeProgressDialog();
        } else {
            transRefresh.loadmoreCompleted(PullToRefreshLayout.FAIL);
        }
        //将所有数据处理完后为ListView设置数据
        mAdapter.setData(listViewBeanList);
        if ("CTIS.0204".equals(biiResultErrorException.getErrorCode())) {
            PublicUtils.haveDataSelectText(false, tvNoData, isSelectData);
            transRefresh.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
        } else {
            showErrorDialog(biiResultErrorException.getErrorMessage());
        }
    }

    /** ---------------------------------------------电子现金账户 交易明细 数据处理 结束-------------------------------------------- */


    /**
     * 查询最大跨度和最长时间范围 成功
     *
     * @param rangeQueryViewModel
     */
    @Override
    public void queryInquiryRangeSuccess(InquiryRangeQueryViewModel rangeQueryViewModel) {
        handleInquiryRangeSuccessData(rangeQueryViewModel);
    }

    /**
     * 查询最大跨度和最长时间范围 失败
     *
     * @param biiResultErrorException
     */
    @Override
    public void queryInquiryRangeFail(BiiResultErrorException biiResultErrorException) {
        handleInquiryRangeFailData(biiResultErrorException);
    }


    /**
     * 查询账户详情成功
     *
     * @param accountDetailViewModel
     */
    @Override
    public void queryAccountQueryAccountDetailSuccess(AccountDetailViewModel accountDetailViewModel) {
        if (accountDetailViewModel != null) {
            List<AccountListItemViewModel.CardAmountViewModel> cardAmountViewModelList =
                    new ArrayList<AccountListItemViewModel.CardAmountViewModel>();
            //组合返回的币种和余额信息
            for (int j = 0; j < accountDetailViewModel.getAccountDetaiList().size(); j++) {
                AccountListItemViewModel.CardAmountViewModel cardAmountViewModel =
                        new AccountListItemViewModel.CardAmountViewModel();
                //币种
                cardAmountViewModel.setCurrencyCode(
                        accountDetailViewModel.getAccountDetaiList().get(j).getCurrencyCode());
                //钞汇类型，01=现钞，02=现汇
                cardAmountViewModel.setCashRemit(
                        accountDetailViewModel.getAccountDetaiList().get(j).getCashRemit());
                //可用余额
                cardAmountViewModel.setAmount(
                        accountDetailViewModel.getAccountDetaiList().get(j).getAvailableBalance()
                                + "");
                //当前余额
                cardAmountViewModel.setBookBalance(
                        accountDetailViewModel.getAccountDetaiList().get(j).getBookBalance()
                                + "");
                cardAmountViewModelList.add(cardAmountViewModel);
            }
            currencyList = cardAmountViewModelList;
            initSelectView();
        }
    }

    /**
     * 查询账户详情失败
     *
     * @param biiResultErrorException
     */
    @Override
    public void queryAccountQueryAccountDetailFail(BiiResultErrorException biiResultErrorException) {
//        queryDetailListData();

        //关闭加载对话框
        closeProgressDialog();
        if ("您的账户无存单信息".equals(biiResultErrorException.getErrorMessage())) {
            rlHeadSelectLayout.setVisibility(View.GONE);
            transRefresh.setMove(false);
            tvNoData.setVisibility(View.VISIBLE);
            tvNoData.setText("您的账户无存款信息");
        } else {
            showErrorDialog(biiResultErrorException.getErrorMessage());
        }
    }

    /**
     * 查询交易明细列表成功 -- 活期
     */
    @Override
    public void queryTransDetailListSuccess(TransDetailViewModel transDetailViewModel) {
        handleTransDetailListSuccessData(transDetailViewModel);
    }

    /**
     * 查询交易明细失败 -- 活期
     */
    @Override
    public void queryTransDetailListFail(BiiResultErrorException biiResultErrorException) {
        handleTransDetailListFailData(biiResultErrorException);
    }

    /**
     * 查询交易明细成功 -- 医保账户
     */
    @Override
    public void queryMedicalInsurAcctTransferlListSuccess(MedicalTransferDetailQueryViewModel
                                                                  transDetailViewModel) {
        handleMedicalInsurAcctTransferlListSuccessData(transDetailViewModel);
    }

    /**
     * 查询交易明细失败 -- 医保账户
     */
    @Override
    public void queryMedicalInsurAcctTransferListFail(BiiResultErrorException
                                                              biiResultErrorException) {
        handleMedicalInsurAcctTransferlListFailData(biiResultErrorException);
    }

    /**
     * 查询交易明细成功 -- 电子现金账户
     */
    @Override
    public void queryFinanceICTransferListSuccess(FinanceICTransferViewModel
                                                          transDetailViewModel) {
        handleFinanceICTransferListSuccessData(transDetailViewModel);
    }

    /**
     * 查询交易明细失败 -- 电子现金账户
     */
    @Override
    public void queryFinanceICTransferListFail(BiiResultErrorException biiResultErrorException) {
        handleFinanceICTransferListFailData(biiResultErrorException);
    }

    @Override
    protected TransDetailContract.Presenter initPresenter() {
        return new TransDetailPresenter(this);
    }


//    @Override
//    protected void titleLeftIconClick() {
//        setFramgentResult(100,null);
//        pop();
//    }
//
//    @Override
//    public void onBack() {
//        setFramgentResult(100,null);
//    }

}
