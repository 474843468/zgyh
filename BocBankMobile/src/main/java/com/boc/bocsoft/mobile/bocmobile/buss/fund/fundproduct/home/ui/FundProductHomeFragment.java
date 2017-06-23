package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bii.bus.account.model.PsnQueryInvtBindingInfo.PsnQueryInvtBindingInfoParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCompanyQueryOutlay.PsnFundCompanyQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryFundBalance.PsnFundQueryFundBalanceParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryFundBalance.PsnFundQueryFundBalanceResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryOutlay.PsnFundQueryOutlayParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryOutlay.PsnFundQueryOutlayResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundRiskEvaluationQuery.PsnFundRiskEvaluationQueryResult;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnGetFundCompanyList.PsnGetFundCompanyListParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryFundDetail.PsnQueryFundDetailParams;
import com.boc.bocsoft.mobile.bii.bus.fund.model.PsnQueryFundDetail.PsnQueryFundDetailResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnInvestmentManageIsOpen.PsnInvestmentManageIsOpenParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOcrmProductQuery.PsnOcrmProductQueryParams;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOcrmProductQuery.PsnOcrmProductQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PinYinUtil;
import com.boc.bocsoft.mobile.bocmobile.base.widget.ClickableSpan.MClickableSpan;
import com.boc.bocsoft.mobile.bocmobile.base.widget.SlipMenu.SlipDrawerLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullableListView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttype.SelectTypeView;
import com.boc.bocsoft.mobile.bocmobile.buss.common.LoginCallback;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.ui.FundProductDetailFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.adapter.FundProductListAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.model.BuildViewData;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.model.FundCompanyListViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.model.FundProductHomeData;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.model.FundProductShowModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.model.InvstBindingInfoViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.presenter.FundProductHomePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.more.ui.FundHomeMoreOptionFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.recommend.ui.FundRecommendFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.search.ui.FundProductSearchFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.utils.DataUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.querymultiplefund.WFSSQueryMultipleFundParams;
import com.boc.bocsoft.mobile.wfss.buss.funds.model.querymultiplefund.WFSSQueryMultipleFundResult;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * 基金产品首页列表
 * Created by liuzc on 2016/11/17.
 */
public class FundProductHomeFragment extends MvpBussFragment<FundProductHomeContract.Presenter> implements
        View.OnClickListener, FundProductHomeContract.HomeView{
    private View rootView;
    private FrameLayout flyBackground; //标题栏以下的布局
    private SlipDrawerLayout slipDrawerLayout;// 侧滑
    private FrameLayout flyHeaderBack; //标题背景
    private TitleBarView titleView;// 标题
    private ImageView imvTitleBack; //标题返回图标
    private ImageView imvTitleShare = null; //标题分享图标；
    private ImageView imvTitleMore = null; //标题更多
    private PullToRefreshLayout pullToRefreshLayout;// 上拉刷新
    private PullableListView listView;// 列表
    private SelectTypeView selectTypeView;// 筛选
    private FundCompanySelectView viewFundCompanySel; //基金公司选择view

    private View viewMainContent; //主要元素布局（登陆及市值部分、搜索部分、基金类型选择）
    private View viewContentHead; //登陆、市值
    private View viewSearch; //搜索部分view
    private FundProductTitleView viewFundProductTitle; //基金标题头
    private SyncHorizontalScrollView srvFundListTitle;  //基金列表标题头的scrollview

    private LinearLayout llyRecBack; //推荐条背景
    private ImageView imvRecCloseIcon; //推荐条上的关闭按钮
    private TextView tvFundRecDesc; //推荐条上的文字描述
    private MClickableSpan clkspnRecSearch; //推荐条上的“查看”

    private ImageView imgHelp;// 帮助
    private LinearLayout llParentOCRM;// 市值布局
    private TextView tvOCRM; //市值text
    private TextView tvLogin;// 登录|持仓
    private LinearLayout llTab;// 交易查询|账户管理|撤单
    private TextView tvQuery;// 交易查询
    private TextView tvAccountManage;// 账户管理
    private TextView tvCancel;// 撤单
    private ImageView imgSearch;// 搜索图片
    private TextView tvSearch;// 搜索
    private TextView tvFilter;// 筛选

    private TextView tvListHeader = null; //listview的headerview
    private TextView tvListFooter = null; //listview的footerview

    private static FundProductHomeFragment instance;

    private FundProductListAdapter mAdapter;// 基金列表适配器

    private int currentPageIndex = 1;// 当前页起始索引,默认从1开始

    private boolean isPullToRefresh = false;// 是否上拉加载

    //listview上一层主要布局的高度(不含标题栏)，listview会默认添加一个相同高度的header，以实现悬浮效果
    private int mainContentHeight = 0;
    //登陆、市值这个区域的高度（不含重叠的标题栏部分）
    private int contentHeadHeight = 0;

    private FundProductHomeData fundProductHomeData = null; //页面参数

    private int listviewScrllY = 0; //listview在纵向的偏移值
    /*
    是否应当调整listview的footer的高度，为保证顶部悬浮效果，有时footer的高度过高，这样会使得上拉刷新
    效果出现问题，所以在listview滑动过程中，动态调整footer的高度
     */
    private boolean bShouldAdjustFooterHeight = false;

    private Handler mListViewScrollHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //为listview添加滑动监听事件
            setListViewScrollListener();
            listView.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = View.inflate(mContext, R.layout.boc_fragment_fund_product, null);
        return rootView;
    }

    @Override
    public void initView() {
        flyBackground = (FrameLayout) rootView.findViewById(R.id.flyBackground);
        slipDrawerLayout = (SlipDrawerLayout) rootView.findViewById(R.id.drawer_layout);
        flyHeaderBack = (FrameLayout) rootView.findViewById(R.id.flyHeaderBack);
        titleView = (TitleBarView) rootView.findViewById(R.id.title_view);
        imvTitleBack = (ImageView) titleView.findViewById(R.id.leftIconIv);
        initHeaderView();

        pullToRefreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.pull_refresh);
        listView = (PullableListView) rootView.findViewById(R.id.listview);
        selectTypeView = (SelectTypeView) rootView.findViewById(R.id.select_type);
        selectTypeView.showSelectArea(getString(R.string.boc_fund_company), getString(R.string.boc_fund_type_all));
        viewFundCompanySel = (FundCompanySelectView) rootView.findViewById(R.id.viewFundCompanySel);
        viewMainContent = rootView.findViewById(R.id.llyContent);

        llyRecBack = (LinearLayout) rootView.findViewById(R.id.llyRecBack);
        imvRecCloseIcon = (ImageView) rootView.findViewById(R.id.imvRecCloseIcon);
        tvFundRecDesc = (TextView) rootView.findViewById(R.id.tvFundRecDesc);
        String CONTRACT = getResources().getString(R.string.boc_fund_search);
        SpannableString spnRecSearch = new SpannableString(CONTRACT);
        clkspnRecSearch = new MClickableSpan(CONTRACT, getContext());
        clkspnRecSearch.setColor(getResources().getColor(R.color.boc_text_color_red));
        spnRecSearch.setSpan(clkspnRecSearch, 0,
                CONTRACT.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvFundRecDesc.setText(String.format("%s  ",
                getResources().getString(R.string.boc_fund_rec_tips)));
        tvFundRecDesc.append(spnRecSearch);
        tvFundRecDesc.setMovementMethod(LinkMovementMethod.getInstance());
        tvFundRecDesc.setLongClickable(false);

        viewContentHead = rootView.findViewById(R.id.layoutProductContent);
        viewFundProductTitle = (FundProductTitleView) rootView.findViewById(R.id.viewFundProductTitle);

        viewSearch = rootView.findViewById(R.id.ll_parent_search);
        srvFundListTitle = (SyncHorizontalScrollView)rootView.findViewById(R.id.shsrvListTitle);

        FundTypeSelectView fundTypeSelectView = (FundTypeSelectView)rootView.findViewById(R.id.viewSelectFundType);
        fundTypeSelectView.setFundTypeSelListener(new FundTypeSelectView.IFundTypeSelectListener() {
            @Override
            public void onSelectItem(String fundTypeCode) {
                //选择新的基金类型，刷新数据
                if(fundProductHomeData == null){
                    fundProductHomeData = new FundProductHomeData();
                }

                fundProductHomeData.setFundTypeCode(fundTypeCode);

                onFundTypeChanged();
            }
        });

        imgHelp = (ImageView) viewContentHead.findViewById(R.id.img_help);
        llParentOCRM = (LinearLayout) viewContentHead.findViewById(R.id.ll_parent_ocrm);
        tvOCRM = (TextView) viewContentHead.findViewById(R.id.txt_ocrm);
        tvLogin = (TextView) viewContentHead.findViewById(R.id.tvLogin);
        llTab = (LinearLayout) viewContentHead.findViewById(R.id.ll_tab);
        tvQuery = (TextView) viewContentHead.findViewById(R.id.tvQuery);
        tvAccountManage = (TextView) viewContentHead.findViewById(R.id.tvAccountManage);
        tvCancel = (TextView) viewContentHead.findViewById(R.id.tvCancel);
        imgSearch = (ImageView) viewSearch.findViewById(R.id.img_search);
        tvSearch = (TextView) viewSearch.findViewById(R.id.tvSearch);
        tvFilter = (TextView) viewSearch.findViewById(R.id.txt_select);
    }

    /**
     * 更新页面元素高度，保证正确的listview上拉悬浮效果
     */
    private void updateViewHeight(){
        int headerHeight = getViewHeight(titleView);
        Log.e("qq", "................updateViewHeight ." + getViewHeight(viewMainContent) + ", "
                + viewMainContent.getHeight() + ", " + headerHeight);
        mainContentHeight = getViewHeight(viewMainContent) - headerHeight;
        contentHeadHeight =getViewHeight(viewContentHead) - headerHeight;

        if(tvListHeader != null){
            listView.removeHeaderView(tvListHeader);
        }

        tvListHeader = new TextView(getContext());
        tvListHeader.setHeight(mainContentHeight);
        listView.addHeaderView(tvListHeader);

        listView.setAdapter(mAdapter);
        setListViewScrollListener();
    }

    private void setListViewScrollListener(){
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                listviewScrllY = getListViewScrollY(view) - listView.getTop();

                if(bShouldAdjustFooterHeight){
                    resetListViewSize();
                }
                updateFloatingView(listviewScrllY);

                if(!isLoginAndBinding()){
                    updateHeaderAlpha(listviewScrllY);
                }
            }
        });
    }

    /**
     * 获取view的真实高度
     * @param view
     * @return
     */
    private int getViewHeight(View view){
        if(view == null){
            return 0;
        }

        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredHeight();
    }

    /**
     * 更新悬浮元素
     * @param lsvScrollY： listview的纵向偏移值
     */
    private void updateFloatingView(int lsvScrollY){
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)viewMainContent.getLayoutParams();
        int paramTop = 0;
        if(lsvScrollY <= 0){
            paramTop = 0;
        }
        else if(lsvScrollY <= contentHeadHeight){
            paramTop = lsvScrollY;
        }
        else{
            paramTop = contentHeadHeight;
        }

        params.setMargins(0, -paramTop, 0, 0);
        viewMainContent.setLayoutParams(params);
    }

    /**
     * 更新标题栏的透明度，达到滑动渐变的效果
     * @param lsvScrollY
     */
    private void updateHeaderAlpha(int lsvScrollY){
        float tempDist;
        if(lsvScrollY <= 0){
            tempDist = 0;
        }
        else if(lsvScrollY <= contentHeadHeight){
            tempDist = lsvScrollY;
        }
        else{
            tempDist = contentHeadHeight;
        }

        float alpha = tempDist / contentHeadHeight;
        if (alpha < 0.3){

            updateHeaderStyle(true);
        } else {
            updateHeaderStyle(false);
        }
        flyHeaderBack.setAlpha(alpha);
    }

    /**
     * 更新标题的样式：
     * @param isWhite true：图标字体为白色； false：图标字体为黑色
     */
    private void updateHeaderStyle(boolean isWhite){
        if(isWhite){
            titleView.setTitleColor(getResources().getColor(R.color.boc_titlebar_text_white));
            imvTitleBack.setImageDrawable(getResources().getDrawable(R.drawable.boc_back_white));
            imvTitleShare.setImageDrawable(getResources().getDrawable(R.drawable.icon_share_white));
            imvTitleMore.setImageDrawable(getResources().getDrawable(R.drawable.boc_more_white));
        }
        else{
            titleView.setTitleColor(getResources().getColor(R.color.boc_text_color_dark_gray));
            imvTitleBack.setImageDrawable(getResources().getDrawable(R.drawable.boc_back_black));
            imvTitleShare.setImageDrawable(getResources().getDrawable(R.drawable.icon_share_black));
            imvTitleMore.setImageDrawable(getResources().getDrawable(R.drawable.boc_more_black));
        }
    }

    /**
     * 获取listview的纵向偏移值，通过listview的getScrollY获取的值不对
     * @param listView
     * @return
     */
    private int getListViewScrollY(AbsListView listView){
        if(listView == null){
            return 0;
        }
        View viewFirstVisible = listView.getChildAt(0);
        if(viewFirstVisible == null){
            return 0;
        }

        //第一个可见元素的索引,从0开始
        int firstVisiblePosition = listView.getFirstVisiblePosition();

        if(firstVisiblePosition == 0){ //header可见，Y向偏移值为header的top的绝对值
            return - viewFirstVisible.getTop();
        }
        else{ //header不可见，Y向偏移值为header的高度 + 隐藏元素的高度
            return (mainContentHeight + (-viewFirstVisible.getTop()) + (firstVisiblePosition - 1) * viewFirstVisible.getHeight());
        }

    }

    @Override
    public void initData() {
        fundProductHomeData = new FundProductHomeData();
        setCommonInfo();
        if (ApplicationContext.getInstance().isLogin()) {// 已登录
            showLoginView();
            updateViewHeight();
            queryIsOpenInvest();
        } else {// 未登录
            llTab.setVisibility(View.GONE);// 未登录隐藏（交易查询、账户管理、撤单）
            updateViewHeight();
            queryFundsProductNLog();
        }
    }

    @Override
    public void setListener() {
        tvLogin.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
        tvQuery.setOnClickListener(this);
        tvAccountManage.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvFilter.setOnClickListener(this);
        imgHelp.setOnClickListener(this);
        imvRecCloseIcon.setOnClickListener(this);
        tvFundRecDesc.setOnClickListener(this);

        titleView.setLeftButtonOnClickLinster(new View.OnClickListener() {// 标题返回键
            @Override
            public void onClick(View v) {
                titleLeftIconClick();
            }
        });

        clkspnRecSearch.setListener(new MClickableSpan.OnClickSpanListener() {
            @Override
            public void onClickSpan() {
                //// TODO: 2016/12/26
            }
        });

        tvSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               gotoFundSearchPage();
            }
        });

        //基金列表的各个标题的点击监听
        viewFundProductTitle.setTitleClickListener(new FundProductTitleView.ITitleListener() {

            @Override
            public void onClickItem(String wfssSortField, String wfssSortType, String biSortField, String biSortType) {
                //修改排序方式，重新刷新数据
                if(fundProductHomeData == null){
                    fundProductHomeData = new FundProductHomeData();
                }

                fundProductHomeData.setWfssSortField(wfssSortField);
                fundProductHomeData.setWfssSortType(wfssSortType);

                fundProductHomeData.setBiSortField(biSortField);
                fundProductHomeData.setBiSortType(biSortType);

                onFundSortTypeChanged();
            }
        });

        //上拉刷新
        pullToRefreshLayout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {// 上拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                startPresenter();// 开启presenter
                if ((fundProductHomeData.getRecordNumber() > 0) &&
                        (fundProductHomeData.getRecordNumber() > fundProductHomeData.getmProductShowModelList().size())) {//有下一页
                    isPullToRefresh = true;
                    if(isLoginAndBinding()){
                        queryFundsProductListLog();
                    }
                    else{
                        queryFundsProductNLog();
                    }
                } else {
                    pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                }
            }
        });

        // 筛选页面回调
        selectTypeView.setClickListener(new SelectTypeView.SelectListener() {
            @Override
            public void onClick(List<String> selectIds) {
                updateFilterDatas(selectIds);
                initRequestData();
                showLoadingDialog();

                tvFilter.setTextColor(getResources().getColor(R.color.boc_text_color_red));
                if(isLoginAndBinding()){
                    queryFundsProductListLog();
                }
                else{
                    queryFundsProductNLog();
                }
            }

            @Override
            public void resetClick() {

            }
        });

        //筛选页面的选择条目
        EditChoiceWidget edtcwCompanySelect = (EditChoiceWidget)selectTypeView.findViewById(R.id.edtcwSelect);
        edtcwCompanySelect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //如果尚未请求基金公司列表，则请求，否则直接打开基金公司选择页面
                if(fundProductHomeData.getFundCompanyListViewModel() == null){
                    showLoadingDialog();

                    if(isLoginAndBinding()){
                        //请求登陆后基金列表
                        PsnGetFundCompanyListParams params = new PsnGetFundCompanyListParams();
                        getPresenter().getFundCompanyList(params);
                    }
                    else{
                        //登陆前基金公司查询
                        PsnFundCompanyQueryOutlayParams params = new PsnFundCompanyQueryOutlayParams();
                        getPresenter().getFundCompanyListNLog(params);
                    }

                }
                else{
                    showFundCompanyList();
                }
            }
        });

        //基金公司选择回调监听
        viewFundCompanySel.setiListener(new FundCompanySelectView.IFundCompSelListener() {
            @Override
            public void onClickBack() {
                //点击返回，显示筛选页
                viewFundCompanySel.setVisibility(View.GONE);
                selectTypeView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSelectCompany(String companyName, String companyCode) {
                //选中某条记录
                viewFundCompanySel.setVisibility(View.GONE);
                selectTypeView.setVisibility(View.VISIBLE);
                fundProductHomeData.setFilterFundCompCode(companyCode);
                selectTypeView.showSelectArea(getString(R.string.boc_fund_company), companyName);
            }
        });
    }

    /**
     * 进入基金搜索页
     */
    private void gotoFundSearchPage(){
        FundProductSearchFragment fragment = new FundProductSearchFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DataUtils.KEY_BINDING_INFO, fundProductHomeData.getBindingInfoModel());
        fragment.setArguments(bundle);
        start(fragment);
    }

    /**
     * 更新筛选页面数据
     * @param selectIds
     */
    private void updateFilterDatas(List<String> selectIds){
        if(selectIds == null || selectIds.size() < 3){
            return;
        }

        fundProductHomeData.setFilterCurrency(selectIds.get(0).equals("000") ? "" : selectIds.get(0));
        fundProductHomeData.setFilterRiskLevel(selectIds.get(1).equals("0") ? "" : selectIds.get(1));
        fundProductHomeData.setFilterFundStatus(selectIds.get(2).equals("00") ? "" : selectIds.get(2));
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    protected FundProductHomeContract.Presenter initPresenter() {
        return new FundProductHomePresenter(this);
    }


    /**
     * 设置页面公共信息
     */
    private void setCommonInfo() {
        instance = this;
        fundProductHomeData.setmProductShowModelList(new LinkedList<FundProductShowModel>());
        mAdapter = new FundProductListAdapter(mContext);
        mAdapter.setTitleScrollView(srvFundListTitle);

        mAdapter.setListener(new FundProductListAdapter.IFundProductItemListener(){
            @Override
            public void onClickItem(FundProductShowModel item) {
                FundProductDetailFragment fragment = new FundProductDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString(DataUtils.KEY_FUND_ID, item.getFundId());
                bundle.putSerializable(DataUtils.KEY_BINDING_INFO, fundProductHomeData.getBindingInfoModel());
                fragment.setArguments(bundle);
                start(fragment);
            }
        });

        /*初始化排序与筛选*/
        selectTypeView.setSelectView(true, 3);

        fundProductHomeData.setmFilterList(BuildViewData.buildSelectData());
        selectTypeView.setData(fundProductHomeData.getmFilterList());
    }

    /**
     * 初始化标题布局
     */
    private void initHeaderView(){
        //设置标题右边的分享、更多效果
        if(imvTitleShare == null || imvTitleMore == null){
            LinearLayout linearLayout = titleView.getRightContainer();
            linearLayout.removeAllViews();
            View view = View.inflate(mContext, R.layout.boc_fragment_title_right, linearLayout);
            imvTitleShare = (ImageView) view.findViewById(R.id.img_left);
            imvTitleMore = (ImageView) view.findViewById(R.id.img_right);
            imvTitleShare.setOnClickListener(new View.OnClickListener() {// 分享
                @Override
                public void onClick(View v) {
//                    Toast.makeText(mContext, "待添加", Toast.LENGTH_LONG).show();
                    FundH5Fragment fragment = new FundH5Fragment();
                    start(fragment);
                }
            });
            imvTitleMore.setOnClickListener(new View.OnClickListener() {// 更多菜单
                @Override
                public void onClick(View v) {
                    FundHomeMoreOptionFragment fragment = new FundHomeMoreOptionFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(DataUtils.KEY_INVESTMENT_OPEN_STATE, fundProductHomeData.getInvestmentOpenState());
                    bundle.putSerializable(DataUtils.KEY_BINDING_INFO, fundProductHomeData.getBindingInfoModel());
                    bundle.putInt(DataUtils.KEY_INVESTMENT_OPEN_STATE, fundProductHomeData.getInvestmentOpenState());
                    fragment.setArguments(bundle);
                    start(fragment);
                }
            });
        }

        /*标题*/
//        titleView.setStyle(R.style.titlebar_common_transparent);
        titleView.setTitle(R.string.boc_fund_title);
//        titleView.setBackgroundColor(Colo);

        //已登录并且绑定账号
        if(isLoginAndBinding()){
            flyHeaderBack.setBackgroundResource(R.color.boc_bg_color_dark_blue);
            flyHeaderBack.setAlpha(1.0f);
        }
        else{
            flyHeaderBack.setBackgroundResource(R.color.white);
            flyHeaderBack.setAlpha(0.0f);
        }

        updateHeaderStyle(true);
    }

    /**
     * 初始化网络请求理财列表的数据
     */
    private void initRequestData() {
        currentPageIndex = 1;
        fundProductHomeData.setRecordNumber(0);

        listView.setOnScrollListener(null);
        if (fundProductHomeData.getmProductShowModelList() != null) {
            fundProductHomeData.getmProductShowModelList().clear();
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 当前实例
     * @return
     */
    public static FundProductHomeFragment getInstance() {
        return instance;
    }



    /**
     * 页面展示View（已登录）
     */
    private void showLoginView() {
        llTab.setVisibility(View.VISIBLE);

        //隐藏未登录的广告
        ImageView imvFundBanner = (ImageView)viewContentHead.findViewById(R.id.imvFundBanner);
        viewContentHead.setBackgroundColor(getResources().getColor(R.color.boc_bg_color_dark_blue));
        imvFundBanner.setVisibility(View.INVISIBLE);

        //背景调整成蓝色
        FrameLayout flyBackground = (FrameLayout)viewContentHead.findViewById(R.id.flyBackground);
        flyBackground.setBackgroundColor(getResources().getColor(R.color.boc_bg_color_dark_blue));

        //显示帮助图标
        ImageView imvHelp = (ImageView) viewContentHead.findViewById(R.id.img_help);
        imvHelp.setVisibility(View.VISIBLE);

        tvLogin.setText(mContext.getString(R.string.boc_fund_sign));//签约
    }

    @Override
    public void queryFundsRecommendSuccess(PsnOcrmProductQueryResult result) {
        if(result != null && result.getResultList() != null && result.getResultList().size() > 0){
            llyRecBack.setVisibility(View.VISIBLE);
        }
        else{
            llyRecBack.setVisibility(View.GONE);
        }

        queryFundsProductListLog();
    }

    @Override
    public void queryFundsRecommendFail(BiiResultErrorException biiResultErrorException) {
        llyRecBack.setVisibility(View.INVISIBLE);
        queryFundsProductListLog();
    }

    @Override
    public void setPresenter(FundUserContract.Presenter presenter) {

    }

    /**
     * 登录成功回调
     */
    class LoginCallbackImpl implements LoginCallback {
        @Override
        public void success() {
            //登陆成功后，重置筛选数据为默认状态
            fundProductHomeData.setmFilterList(BuildViewData.buildSelectData());
            selectTypeView.getAdapter().notifyDataSetChanged();
            loginSuccess();
        }
    }

    /**
     * 登录成功后调用（View和接口）
     */
    private void loginSuccess() {
        initHeaderView(); //重新更新标题样式
        showLoginView(); //显示登陆成功后的页面
        updateViewHeight(); //重新计算显示布局的高度
        initRequestData();

        queryIsOpenInvest();
    }

    /**
     * 从WFSS返回的结果取出对应的字段写入到当前展示的基金列表中
     * @param result
     */
    private void writeWFSSDataToShowProduct(WFSSQueryMultipleFundResult result){
        if(result == null){
            return;
        }

        List<WFSSQueryMultipleFundResult.FundList> newList = result.getItems();// 响应数据
        for(WFSSQueryMultipleFundResult.FundList wfssItem : newList){
            for(FundProductShowModel showItem : fundProductHomeData.getmProductShowModelList()){
                if(!StringUtils.isEmptyOrNull(wfssItem.getFundId())
                        && wfssItem.getFundId().equals(showItem.getFundId())){
                    showItem.setFundBakCode(wfssItem.getFundBakCode());
                    showItem.setJzTime(wfssItem.getJzTime());
                    showItem.setCurrPercentDiff(wfssItem.getCurrPercentDiff());
                    showItem.setChangeOfMonth(wfssItem.getChangeOfMonth());
                    showItem.setChangeOfQuarter(wfssItem.getChangeOfQuarter());
                    showItem.setChangeOfHalfYear(wfssItem.getChangeOfHalfYear());
                    showItem.setChangeOfYear(wfssItem.getChangeOfYear());
                    showItem.setChangeOfWeek(wfssItem.getChangeOfWeek());
                    showItem.setYieldOfTenThousand(wfssItem.getYieldOfTenThousand());
                    continue;
                }
            }
        }
    }

    /**
     * WFSS产品列表返回后的操作：1、对基金数据进行拼接； 2、listview刷新，并调整到指定位置
     */
    private void doAfterWFSSProductReturned(WFSSQueryMultipleFundResult result) {
        writeWFSSDataToShowProduct(result);

        mAdapter.setData(fundProductHomeData.getmProductShowModelList());// 页面显示数据

        mAdapter.notifyDataSetChanged();
        mAdapter.updateScorllView();

        updateListView();


        if(isPullToRefresh){//如果是上拉刷新出的数据，则直接展现即可
            mListViewScrollHandler.sendEmptyMessage(0);
        }
        else{ //若非上拉刷新，需要计算出listview上次悬停位置，并强制移动至此位置
            listView.smoothScrollToPositionFromTop(0, -listviewScrllY, 0);
            listView.setVisibility(View.INVISIBLE);

            mListViewScrollHandler.sendEmptyMessageDelayed(0, 400);
        }
    }

    /**
     * 请求数据成功后重新构建listview，添加footer以保证顶部悬浮效果
     */
    private void updateListView(){
        if(tvListFooter != null){
            listView.removeFooterView(tvListFooter);
        }

        int listViewContentHeight = 0; //listview内容区的高度（不算头尾）
        if(fundProductHomeData.getmProductShowModelList() != null  &&
                fundProductHomeData.getmProductShowModelList().size() > 0){
            listViewContentHeight = fundProductHomeData.getmProductShowModelList().size() * mAdapter.getItemViewHeight();
        }

        int currentListViewHeight = mainContentHeight + listViewContentHeight; //当前listview的高度

        //头部的纵向偏移值
        int headerOffsetY = listviewScrllY > contentHeadHeight ? contentHeadHeight: listviewScrllY;
        //listview的目标高度（屏幕高度 + 偏移高度）
        int destListViewHeight = flyBackground.getHeight() + headerOffsetY;

        //如果目标高度大于真实高度，则差值用footerview补上
        if(destListViewHeight > currentListViewHeight){
            tvListFooter = new TextView(getContext());
            tvListFooter.setHeight(destListViewHeight - currentListViewHeight);
            listView.addFooterView(tvListFooter);
            bShouldAdjustFooterHeight = true;
        }
        else{
            bShouldAdjustFooterHeight = false;
        }
    }

    /**
     * 动态调整listview的尺寸，把屏幕下方隐藏的地方去掉，保证上拉刷新效果
     */
    private void resetListViewSize(){
        int listViewContentHeight = 0; //listview内容区的高度（不算头尾）
        if(fundProductHomeData.getmProductShowModelList() != null &&
                fundProductHomeData.getmProductShowModelList().size() > 0){
            listViewContentHeight = fundProductHomeData.getmProductShowModelList().size() *
                    mAdapter.getItemViewHeight();
        }

        //listview的header和content的高度
        int lsvHeadAndContentHeight = listViewContentHeight + mainContentHeight;
        //头部的纵向偏移值
        int headerOffsetY = listviewScrllY > contentHeadHeight ? contentHeadHeight: listviewScrllY;
        //目标高度：纵向偏移值 + 内容区背景的高度
        int destHeight = headerOffsetY + flyBackground.getHeight();

        int footerHeight = tvListFooter.getHeight();
        //footerView在屏幕下方有隐藏的区域
        if((lsvHeadAndContentHeight + footerHeight > destHeight) && (destHeight > lsvHeadAndContentHeight)){
            tvListFooter.setHeight(destHeight - lsvHeadAndContentHeight);
        }
        else{
            //当listview的尺寸恰好是一屏的尺寸时，停止调整
            if(lsvHeadAndContentHeight + footerHeight <= flyBackground.getHeight()){
                bShouldAdjustFooterHeight = false;
            }
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tvLogin) {
            if (getString(R.string.boc_common_login).equals(tvLogin.getText().toString())) {
                // 登录|持仓
                ModuleActivityDispatcher.startToLogin(mActivity, new LoginCallbackImpl());
            }
            else if(getString(R.string.boc_fund_sign).equals(tvLogin.getText().toString())){ //签约
                start(new FundH5Fragment());
            }
        } else if (i == R.id.tvQuery) {// 交易查询

        } else if (i == R.id.tvAccountManage) {// 账户管理

        } else if (i == R.id.tvCancel) {// 撤单

        } else if (i == R.id.txt_select) {// 筛选
            changeRed(tvFilter, R.drawable.boc_select_red);
            hideSoftInput();
            selectTypeView.getAdapter().notifyDataSetChanged();
            selectTypeView.setVisibility(View.VISIBLE);
            viewFundCompanySel.setVisibility(View.GONE);
            selectTypeView.setDrawerLayout(slipDrawerLayout);
            slipDrawerLayout.toggle();
        } else if (i == R.id.img_search) {// 搜索
            gotoFundSearchPage();
        } else if (i == R.id.img_help) { //帮助
            showErrorDialog(mContext.getString(R.string.boc_wealth_help));
        } else if (i == R.id.imvRecCloseIcon) { //推荐条的关闭
            llyRecBack.setVisibility(View.GONE);
        } else if (i == R.id.tvFundRecDesc) {  //推荐
            start(new FundRecommendFragment());
        }
    }

    /**
     * 显示基金公司选择页
     */
    private void showFundCompanyList(){
        selectTypeView.setVisibility(View.GONE);
        viewFundCompanySel.setVisibility(View.VISIBLE);
        viewFundCompanySel.updateViewModel(fundProductHomeData.getFundCompanyListViewModel());
    }

    /**
     * 更新基金公司列表的字母标题
     */
    private void updateFundCompanyListTitle(){
        FundCompanyListViewModel model = fundProductHomeData.getFundCompanyListViewModel();

        if(model == null || model.getList() == null){
            return;
        }

        String lastTitleLetters = ""; //上一次记录的首字母
        for(int i = 0; i < model.getList().size(); i ++){
            FundCompanyListViewModel.ListBean bean = model.getList().get(i);
            String name = bean.getFundCompanyName();
            String compLetters = PinYinUtil.getPinYin(name); //公司名称对应的拼音
            String firstLetters = ""; //首字母标识

            if(!StringUtils.isEmptyOrNull(compLetters) && compLetters.trim().length() > 1){
                firstLetters = compLetters.toUpperCase().substring(0, 1);
            }
            else{
                firstLetters = "";
            }

            if(!StringUtils.isEmptyOrNull(firstLetters)){
                if(!firstLetters.equals(lastTitleLetters)){
                    bean.setFundCompanyLetterTitle(firstLetters);
                    lastTitleLetters = firstLetters;
                }
            }
        }

        //在最上层增加“全部”
        FundCompanyListViewModel.ListBean firstBean = model.new ListBean();
        firstBean.setFundCompanyName(getString(R.string.boc_fund_trans_type_all));
        firstBean.setFundCompanyCode("");
        model.getList().add(0, firstBean);
    }

    private void changeRed(TextView textView, int id) {
        Drawable drawable = mContext.getResources().getDrawable(id);
        textView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        textView.setTextColor(mContext.getResources().getColor(R.color.boc_text_color_red));
    }

    /**
     * 是否已经登陆并且绑定了交易账户
     * @return
     */
    private boolean isLoginAndBinding(){
        return (ApplicationContext.getInstance().isLogin() &&
                fundProductHomeData != null &&
                fundProductHomeData.getBindingInfoModel() != null &&
                !StringUtils.isEmptyOrNull(fundProductHomeData.getBindingInfoModel().getAccount()));
    }

    /**
     * 请求数据区域
     */

    /**
     * 选择新的基金类型，更新数据
     */
    private void onFundTypeChanged(){
        //理财型或者货币型的处理
        if("01".equals(fundProductHomeData.getFundTypeCode()) ||
                "06".equals(fundProductHomeData.getFundTypeCode())){
            viewFundProductTitle.switchFundTypes(FundProductTitleView.TITLE_TYPE_CURRENCY_WEALTH);
            mAdapter.setFundTypeAll(false);
        }
        else if("10".equals(fundProductHomeData.getFundTypeCode())){ //自选型，不显示排序图标
            viewFundProductTitle.switchFundTypes(FundProductTitleView.TITLE_TYPE_SELF_SELECT);
            mAdapter.setFundTypeAll(true);
        }
        else{
            viewFundProductTitle.switchFundTypes(FundProductTitleView.TITLE_TYPE_ALL);
            mAdapter.setFundTypeAll(true);
        }
        initRequestProductList();
    }

    /**
     * 修改排序方式
     */
    private void onFundSortTypeChanged(){
        initRequestProductList();
    }

    /**
     * 初始请求基金列表
     */
    private void initRequestProductList(){
        initRequestData();
        showLoadingDialog();

        if(isLoginAndBinding()){
            queryFundsProductListLog();
        }
        else{
            queryFundsProductNLog();
        }
    }

    /**
     * 查询是否开通投资服务
     */
    private void queryIsOpenInvest(){
        showLoadingDialog();

        PsnInvestmentManageIsOpenParams params = new PsnInvestmentManageIsOpenParams();
        getPresenter().queryInvestmentManageIsOpen(params);
    }

    /**
     * 查询资金账户、交易账户的绑定信息
     */
    private void queryInvtBindingInfo(){
        showLoadingDialog();

        PsnQueryInvtBindingInfoParams params = new PsnQueryInvtBindingInfoParams();
        params.setInvtType("12");
        getPresenter().queryInvtBindingInfo(params);
    }

    /**
     * 登录前查询FUNDS系统的基金列表
     */
    private void queryFundsProductNLog(){
        showLoadingDialog();

        PsnFundQueryOutlayParams params = new PsnFundQueryOutlayParams();
        params.setFundType(fundProductHomeData.getFundTypeCode());
        params.setCurrentIndex(String.format("%d", currentPageIndex));
        params.setPageSize(String.valueOf(ApplicationConst.FUND_PAGE_SIZE));
        params.setCurrencyCode(fundProductHomeData.getFilterCurrency());
        params.setFundState(fundProductHomeData.getFilterFundStatus());
        params.setRiskGrade(fundProductHomeData.getFilterRiskLevel());
        params.setCompany(fundProductHomeData.getFilterFundCompCode());
        //// TODO: 2016/12/23
        params.setFundKind("00");

        //设置排序方式
        if(!StringUtils.isEmptyOrNull(fundProductHomeData.getWfssSortField())){
            params.setSortField(fundProductHomeData.getBiSortField());
            params.setSortFlag(fundProductHomeData.getBiSortType());
        }

        getPresenter().queryFundsProductListNLog(params);
    }

    /**
     * 查询四方公司的基金列表（登录前）
     */
    private void queryWFSSFundProductList(){
        showLoadingDialog();

        WFSSQueryMultipleFundParams params = new WFSSQueryMultipleFundParams();
        params.setFundType(fundProductHomeData.getFundTypeCode());
        params.setPageNo(String.format("%d", currentPageIndex));
        params.setPageSize(String.valueOf(ApplicationConst.FUND_PAGE_SIZE));
        params.setTransCurrency(fundProductHomeData.getFilterCurrency());
        params.setFundStatus(fundProductHomeData.getFilterFundStatus());
        params.setLevelOfRisk(fundProductHomeData.getFilterRiskLevel());
        params.setFundCompanyCode(fundProductHomeData.getFilterFundCompCode());

        //设置排序方式
        if(!StringUtils.isEmptyOrNull(fundProductHomeData.getWfssSortField())){
            params.setSortFile(fundProductHomeData.getWfssSortField());
            params.setSortType(fundProductHomeData.getWfssSortType());
        }


        getPresenter().queryWFSSProductList(params);
    }

    /**
     * 登陆且绑定账号之后，从FUNDS系统请求基金行情信息（请求到之后，需要用四方的数据进行拼接）
     */
    private void queryFundsProductListLog(){
        showLoadingDialog();

        PsnQueryFundDetailParams params = new PsnQueryFundDetailParams();

        params.setFundType(fundProductHomeData.getFundTypeCode());
        params.setCurrentIndex(String.format("%d", currentPageIndex));
        params.setPageSize(String.valueOf(ApplicationConst.FUND_PAGE_SIZE));
        params.setCurrencyCode(fundProductHomeData.getFilterCurrency());
        params.setFundState(fundProductHomeData.getFilterFundStatus());
        params.setRiskGrade(fundProductHomeData.getFilterRiskLevel());
        params.setCompany(fundProductHomeData.getFilterFundCompCode());

        //// TODO: 2016/12/23
        params.setFundKind("00");

        //设置排序方式
        if(!StringUtils.isEmptyOrNull(fundProductHomeData.getWfssSortField())){
            params.setSortField(fundProductHomeData.getBiSortField());
            params.setSortFlag(fundProductHomeData.getBiSortType());
        }

        getPresenter().queryFundsProductListLog(params);
    }

    /**
     * 登陆前查询四方的基金列表数据
     */
    private void queryWFSSFundListNLogin(){
        PsnFundQueryOutlayResult biFundListResult = fundProductHomeData.getFundProductListNLog();
        if(biFundListResult == null || biFundListResult.getList() == null){
            return;
        }

        String fundIDList = "";
        for(int i = 0; i < biFundListResult.getList().size(); i ++){
            if(i == 0){
                fundIDList = biFundListResult.getList().get(i).getFundCode();
            }
            else{
                fundIDList += String.format("&%s", biFundListResult.getList().get(i).getFundCode());
            }
        }

        //请求四方数据
        showLoadingDialog();
        WFSSQueryMultipleFundParams params = buildWFSSQueryMultipleFundParams(fundIDList);
        getPresenter().queryWFSSProductList(params);
    }

    /**
     * 登陆后查询四方的基金列表数据
     */
    private void queryWFSSFundListAfterLogin(){
        PsnQueryFundDetailResult biFundListResult = fundProductHomeData.getFundProductListLog();
        if(biFundListResult == null || biFundListResult.getList() == null){
            return;
        }

        String fundIDList = "";
        for(int i = 0; i < biFundListResult.getList().size(); i ++){
            if(i == 0){
                fundIDList = biFundListResult.getList().get(i).getFundCode();
            }
            else{
                fundIDList += String.format("&%s", biFundListResult.getList().get(i).getFundCode());
            }
        }

        //请求四方数据
        showLoadingDialog();
        WFSSQueryMultipleFundParams params = buildWFSSQueryMultipleFundParams(fundIDList);
        getPresenter().queryWFSSProductList(params);
    }

    /**
     * 查询基金推荐列表
     */
    private void queryFundsRecommend(){
        showLoadingDialog();
        PsnOcrmProductQueryParams params = new PsnOcrmProductQueryParams();
        params.setProtpye("01");
        params.setTradeType("");
        params.setCurrentIndex(String.valueOf("0"));
        params.setPageSize(String.valueOf(ApplicationConst.FUND_PAGE_SIZE));

        getPresenter().queryFundsRecommend(params);
    }

    /**
     * 构造查询四方的基金列表的参数
     * @param fundCodeList
     * @return
     */
    private WFSSQueryMultipleFundParams buildWFSSQueryMultipleFundParams(String fundCodeList){
        WFSSQueryMultipleFundParams params = new WFSSQueryMultipleFundParams();
        params.setFundType("00");
        params.setMultipleFundBakCode(fundCodeList);
        return params;
    }


    @Override
    public void queryFundsProductListNLogSuccess(PsnFundQueryOutlayResult result) {
        fundProductHomeData.setFundProductListNLog(result);
        
        if(result == null){
            closeProgressDialog();
            doAfterWFSSProductReturned(null);
            return;
        }
        fundProductHomeData.setRecordNumber(result.getRecordNumber());
        
        if(result.getList() != null){
            currentPageIndex += 1;

            for(PsnFundQueryOutlayResult.ListBean curBean : result.getList()){
                FundProductShowModel destModel = new FundProductShowModel();
                destModel.setFundId(curBean.getFundCode());
                destModel.setFundName(curBean.getFundName());
                destModel.setFundCompany(curBean.getFundCompanyName());
                destModel.setFundType(curBean.getFntype());
                //// TODO: 2017/1/3  单位净值字段确认
                destModel.setDwjz(curBean.getNetPrice() != null ? curBean.getNetPrice().toString() : "--");
                fundProductHomeData.getmProductShowModelList().add(destModel);
            }
            
            queryWFSSFundListNLogin();
        }
        else{
            closeProgressDialog();
            doAfterWFSSProductReturned(null);
        }
    }

    @Override
    public void queryFundsProductListNLogFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        doAfterWFSSProductReturned(null);
        return;
    }

    /**
     * 请求回调区域
     * @param biiResultErrorException
     */
    //请求列表失败
    @Override
    public void queryWFSSProductListFail(BiiResultErrorException biiResultErrorException) {
        doAfterWFSSProductReturned(null);

        if (isPullToRefresh) {// 如果上拉加载显示结果
            pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.FAIL);
            isPullToRefresh = false;
        }
    }

    @Override
    public void queryWFSSProductListSuccess(WFSSQueryMultipleFundResult result) {
        closeProgressDialog();

        doAfterWFSSProductReturned(result);

        if (isPullToRefresh) {// 如果上拉加载显示成功view
            pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
            isPullToRefresh = false;
        }
    }

    @Override
    public void queryInvestmentManageIsOpenSuccess(Boolean result) {
        closeProgressDialog();
        if(result){
            //已开通投资服务，判断是否有资金账户
            fundProductHomeData.setInvestmentOpenState(DataUtils.INVESTMENT_OPEN_STATE_OPEN);
            fundProductHomeData.setbInvestmentIsOpen(true);

            queryInvtBindingInfo();
        }
        else{
            fundProductHomeData.setInvestmentOpenState(DataUtils.INVESTMENT_OPEN_STATE_NOTOPEN);
        }
    }

    @Override
    public void queryInvestmentManageIsOpenFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        fundProductHomeData.setInvestmentOpenState(DataUtils.INVESTMENT_OPEN_STATE_UNCHECK);
    }

    @Override
    public void queryInvtBindingInfoSuccess(InvstBindingInfoViewModel result) {
        fundProductHomeData.setBindingInfoModel(result);

        tvLogin.setVisibility(View.GONE);// 隐藏登陆/签约按钮
        llParentOCRM.setVisibility(View.VISIBLE);// 显示市值
        initHeaderView(); //更新标题栏样式

        //清空基金公司列表，后续使用时调新的接口重新查询
        fundProductHomeData.setFundCompanyListViewModel(null);

        //查询持仓信息
        PsnFundQueryFundBalanceParams params = new PsnFundQueryFundBalanceParams();
        getPresenter().queryFundBalance(params);

    }

    @Override
    public void queryInvtBindingInfoFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        String code = biiResultErrorException == null ? "": biiResultErrorException.getErrorMessage();
    }

    @Override
    public void queryFundRiskEvaluationSuccess(PsnFundRiskEvaluationQueryResult result) {

    }

    @Override
    public void queryFundRiskEvaluationFail(BiiResultErrorException biiResultErrorException) {

    }

    @Override
    public void queryFundBalanceSuccess(PsnFundQueryFundBalanceResult result) {
        closeProgressDialog();
        //显示持仓信息
        try{
            if(result != null){
                BigDecimal bdTotalAmount = new BigDecimal(0); //总市值
                for(int i = 0; i < result.getFundBalance().size(); i ++){
                    PsnFundQueryFundBalanceResult.FundBalanceBean bean = result.getFundBalance().get(i);
                    BigDecimal bdCurAmount = new BigDecimal(bean.getCurrentCapitalisation());
                    bdTotalAmount = bdTotalAmount.add(bdCurAmount);
                }

                if(bdTotalAmount.compareTo(new BigDecimal(0)) > 0){
                    tvOCRM.setText(bdTotalAmount.toString());
                }
                else{
                    tvOCRM.setText("****");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        queryFundsRecommend();
    }

    @Override
    public void queryFundBalanceFail(BiiResultErrorException biiResultErrorException) {
        String code = biiResultErrorException == null ? "": biiResultErrorException.getErrorMessage();

        tvOCRM.setText("****");
        showLoadingDialog();

        queryFundsRecommend();
    }

    @Override
    public void getFundCompanyListSuccess(FundCompanyListViewModel viewModel) {
        closeProgressDialog();

        fundProductHomeData.setFundCompanyListViewModel(viewModel);
        updateFundCompanyListTitle();
        showFundCompanyList();
    }

    @Override
    public void getFundCompanyListFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void getFundCompanyListNLogSuccess(FundCompanyListViewModel result) {
        closeProgressDialog();

        fundProductHomeData.setFundCompanyListViewModel(result);
        updateFundCompanyListTitle();
        showFundCompanyList();
    }

    @Override
    public void getFundCompanyListNLogFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void queryFundsProductListLogSuccess(PsnQueryFundDetailResult result) {
        fundProductHomeData.setFundProductListLog(result);

        if(result == null){
            closeProgressDialog();
            doAfterWFSSProductReturned(null);
            if(isPullToRefresh){
                pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.FAIL);
                isPullToRefresh = false;
            }
            return;
        }

        if(!StringUtils.isEmptyOrNull(result.getRecordNumber())){
            try{
                fundProductHomeData.setRecordNumber(Integer.parseInt(result.getRecordNumber()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        if(result.getList() != null){
            currentPageIndex += 1;

            for(PsnQueryFundDetailResult.ListBean curBean : result.getList()){
                FundProductShowModel destModel = new FundProductShowModel();
                destModel.setFundId(curBean.getFundCode());
                destModel.setFundName(curBean.getFundName());
                destModel.setFundCompany(curBean.getFundCompanyName());
                destModel.setFundType(curBean.getFntype());
                //// TODO: 单位净值用哪个字段
                destModel.setDwjz(curBean.getNetPrice() != null ? curBean.getNetPrice().toString() : "--");
                fundProductHomeData.getmProductShowModelList().add(destModel);
            }

            queryWFSSFundListAfterLogin();
        }
        else{
            closeProgressDialog();
            doAfterWFSSProductReturned(null);
            if(isPullToRefresh){
                pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.FAIL);
                isPullToRefresh = false;
            }
        }
    }

    @Override
    public void queryFundsProductListLogFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();

        doAfterWFSSProductReturned(null);

        if(isPullToRefresh){
            pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.FAIL);
            isPullToRefresh = false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //返回处理
            if(slipDrawerLayout.isOpen()){
                if(viewFundCompanySel.getVisibility() == View.VISIBLE){
                    viewFundCompanySel.setVisibility(View.GONE);
                    selectTypeView.setVisibility(View.VISIBLE);
                    return true;
                }
                else if(selectTypeView.getVisibility() == View.VISIBLE){
                    slipDrawerLayout.toggle();
                    return true;
                }
            }
        }
        return false;
    }
}
