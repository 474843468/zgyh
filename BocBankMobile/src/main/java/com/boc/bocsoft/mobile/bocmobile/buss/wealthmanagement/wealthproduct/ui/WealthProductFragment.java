package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.PartialLoadView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.SlipMenu.SlipDrawerLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.ErrorDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullableListView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttype.SelectTypeData;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttype.SelectTypeView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.shareinfoview.ShareUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.Content;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.SelectGridView;
import com.boc.bocsoft.mobile.bocmobile.buss.MenuFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.ui.OverviewFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.common.LoginCallback;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleCode;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.ui.FinancialPositionFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.openwealthmanager.ui.InvestTreatyFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.openwealthmanager.ui.OpenStatusI;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.repealorder.ui.RepealOrderFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.transinquire.ui.TransInquireFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.WealthConst;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.adapter.WealthProductAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.DetailsRequestBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthDetailsBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.presenter.WealthPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.data.WealthBundleData;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.ui.data.WealthViewData;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.utils.WealthPublicUtils;
import com.boc.bocsoft.mobile.common.utils.ButtonClickLock;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.utils.ViewUtils;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import rx.functions.Action1;

/**
 * 理财产品首页列表
 * Created by liuweidong on 2016/9/9.
 */
@SuppressLint("ValidFragment")
public class WealthProductFragment extends MvpBussFragment<WealthPresenter> implements View.OnClickListener,
        AdapterView.OnItemClickListener, WealthContract.HomeView {

    public static final String error_code = "customer.nonexist.counter.contract.only";

    private View rootView;
    private SlipDrawerLayout slipDrawerLayout;// 侧滑
    private TitleBarView titleView;// 标题
    private PullToRefreshLayout pullToRefreshLayout;// 上拉刷新
    private PullableListView listView;// 列表
    private SelectTypeView selectTypeSort;// 排序
    private SelectTypeView selectTypeView;// 筛选
    private SelectGridView selectGridView;// 账户
    private LinearLayout llParentSearch;// 悬停搜索
    private RelativeLayout rlSelect;

    private View headView;
    private View headView2;// 搜索
    private RelativeLayout rlHead;
    private ImageView imgHelp;// 帮助
    private LinearLayout llParentOCRM;// 市值
    private TextView txtOCRM;
    private TextView txtLogin;// 登录|持仓
    private LinearLayout llTab;// 交易查询|投资协议管理|撤单
    private TextView txtQuery;// 交易查询
    private TextView txtProtocol;// 投资协议管理
    private TextView txtCancel;// 撤单
    private ImageView imgSearch;// 搜索图片
    private EditText editSearch;// 搜索
    private TextView txtSort;// 排序
    private TextView txtSelect;// 筛选
    private ImageView imgSearch2;// 搜索图片
    private EditText editSearch2;// 搜索
    private TextView txtSort2;// 排序
    private TextView txtSelect2;// 筛选
    private RelativeLayout rlParentEmpty;
    private PartialLoadView partialLoadView;
    private TextView txtNoResult;// 无结果

    private SharedPreferences preferences;
    private WealthProductAdapter mAdapter;// 理财列表适配器
    private List<SelectTypeData> mSortList;// 排序
    private List<String> mCurSortIds;
    private List<SelectTypeData> mSelectList;// 筛选
    private List<String> mCurSelectIds;
    private List<Content> mSingleList;// 账户筛选
    private int mCurIndex = 0;// 当前页起始索引
    private int mCurPosition;// 当前点击的item
    private boolean mIsPullToRefresh = false;// 是否上拉加载
    private static WealthProductFragment instance;// 当前类实例
    private WealthViewModel mViewModel;// View层model
    private static boolean mRequestStatus = false;// 判断理财开通状态接口是否调用成功
    private static boolean[] mOpenStatus = new boolean[3];// 0开通理财服务1风险测评2登记账户
    private int riskRatingStatus = -1;// 0：未进行测评；1：测评过期
    private boolean[] isClick = {false, false};
    private boolean isHover = false;
    private OpenStatusI openStatusI;
    private boolean isLogined = false;
    private boolean isFromAccountManager;
    private String clickOprLock = "click_more";// 防重锁
    private boolean isFirstSelected = false;
    private boolean isFirstSorted = false;

    public WealthProductFragment(boolean isFromAccountManager) {
        this.isFromAccountManager = isFromAccountManager;
    }

    public WealthProductFragment() {
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = View.inflate(mContext, R.layout.boc_fragment_wealth_product, null);
        return rootView;
    }

    @Override
    public void initView() {
        slipDrawerLayout = (SlipDrawerLayout) rootView.findViewById(R.id.drawer_layout);
        titleView = (TitleBarView) rootView.findViewById(R.id.title_view);
        pullToRefreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.pull_refresh);
        listView = (PullableListView) rootView.findViewById(R.id.listview);
        selectTypeSort = (SelectTypeView) rootView.findViewById(R.id.select_type_sort);
        selectTypeView = (SelectTypeView) rootView.findViewById(R.id.select_type);
        llParentSearch = (LinearLayout) rootView.findViewById(R.id.ll_parent_search);
        imgSearch2 = (ImageView) llParentSearch.findViewById(R.id.img_search);
        editSearch2 = (EditText) llParentSearch.findViewById(R.id.edit_search);
        txtSort2 = (TextView) llParentSearch.findViewById(R.id.txt_sort);
        txtSelect2 = (TextView) llParentSearch.findViewById(R.id.txt_select);
        llParentSearch.setVisibility(View.GONE);// 隐藏悬停布局
        rlParentEmpty = (RelativeLayout) rootView.findViewById(R.id.rl_parent_empty);
        partialLoadView = (PartialLoadView) rootView.findViewById(R.id.part_load_view);
        txtNoResult = (TextView) rootView.findViewById(R.id.txt_no_result);

        headView = View.inflate(mContext, R.layout.boc_fragment_wealth_product_content, null);
        headView2 = View.inflate(mContext, R.layout.boc_fragment_wealth_search, null);
        listView.addHeaderView(headView, null, false);// ListView添加HeaderView
        listView.addHeaderView(headView2, null, false);
        rlHead = (RelativeLayout) headView.findViewById(R.id.rl_head);
        imgHelp = (ImageView) headView.findViewById(R.id.img_help);
        llParentOCRM = (LinearLayout) headView.findViewById(R.id.ll_parent_ocrm);
        txtOCRM = (TextView) headView.findViewById(R.id.txt_ocrm);
        txtLogin = (TextView) headView.findViewById(R.id.txt_login);
        llTab = (LinearLayout) headView.findViewById(R.id.ll_tab);
        txtQuery = (TextView) headView.findViewById(R.id.txt_query);
        txtProtocol = (TextView) headView.findViewById(R.id.txt_protocol);
        txtCancel = (TextView) headView.findViewById(R.id.txt_cancel);
        imgSearch = (ImageView) headView2.findViewById(R.id.img_search);
        editSearch = (EditText) headView2.findViewById(R.id.edit_search);
        txtSort = (TextView) headView2.findViewById(R.id.txt_sort);
        txtSelect = (TextView) headView2.findViewById(R.id.txt_select);
        rlSelect = (RelativeLayout) rootView.findViewById(R.id.rl_select);
        listView.setFastScrollAlwaysVisible(false);
        getNoResultHeight();
    }

    @Override
    public void initData() {
        setCommonInfo();
        if (ApplicationContext.getInstance().isLogin()) {// 已登录
            isLogined = true;
            llTab.setVisibility(View.VISIBLE);// 登录显示（交易查询、投资协议管理、撤单）
            txtLogin.setText(mContext.getString(R.string.boc_wealth_position));// 显示持仓按钮
            callLoginInterface();
        } else {// 未登录
            isLogined = false;
            titleView.setBackgroundResource(R.drawable.icon_wealth_home_title);
            rlHead.setBackgroundResource(R.drawable.icon_wealth_home);
            llTab.setVisibility(View.GONE);// 未登录隐藏（交易查询、投资协议管理、撤单）
            setPartStatusLoading();
            mViewModel.setFirst(true);
            getPresenter().queryProductListN(mCurIndex, "", false);// 查询全国发行的理财产品列表（登录前）
        }
    }

    @Override
    public void setListener() {
        txtLogin.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
        imgSearch2.setOnClickListener(this);
        txtQuery.setOnClickListener(this);
        txtProtocol.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
        txtSort.setOnClickListener(this);
        txtSelect.setOnClickListener(this);
        txtSort2.setOnClickListener(this);
        txtSelect2.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        imgHelp.setOnClickListener(this);
        llParentOCRM.setOnClickListener(this);

        setListViewScrollListener(false);

        titleView.setLeftButtonOnClickLinster(new View.OnClickListener() {// 标题返回键
            @Override
            public void onClick(View v) {
                titleLeftIconClick();
            }
        });

        ViewUtils.doubleClick(titleView, new Action1<View>() {
            @Override
            public void call(View view) {
                listView.setSelection(0);// 直接跳到顶部
//                listView.smoothScrollToPosition(0);
            }
        });

        slipDrawerLayout.setOnDrawerStateListener(new SlipDrawerLayout.OnDrawerStateListener() {
            @Override
            public void onDrawerStateChange(boolean isOpen) {
                editSearch.setCursorVisible(true);
            }
        });

        editSearch.setOnKeyListener(new View.OnKeyListener() {// 搜索
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    initRequestData();
                    setPartStatusLoading();
                    isCallWhichInterface(editSearch.getText().toString(), true);
                }
                return false;
            }
        });
        editSearch2.setOnKeyListener(new View.OnKeyListener() {// 搜索
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    initRequestData();
                    setPartStatusLoading();
                    isCallWhichInterface(editSearch2.getText().toString(), true);
                }
                return false;
            }
        });

        pullToRefreshLayout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {// 上拉加载
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                startPresenter();// 开启presenter
                if (mViewModel.getProductList() != null) {// 列表存在数据
                    if (mViewModel.getProductList().size() < Integer.valueOf(mViewModel.getRecordNumber())) {//数据小于记录数再次请求
                        mIsPullToRefresh = true;
                        isCallWhichInterface("", false);
                    } else {
                        pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
                    }
                } else {// 列表是空的
                    initRequestData();
                    mIsPullToRefresh = true;
                    isCallWhichInterface("", false);
                }
            }
        });

        selectTypeView.setClickListener(new SelectTypeView.SelectListener() {// 筛选
            @Override
            public void onClick(List<String> selectIds) {// 确认
                isFirstSelected = true;
                mCurSelectIds = selectIds;
                initRequestData();

                if (selectGridView != null) {
                    for (int j = 0; j < selectGridView.getList().size(); j++) {
                        if (selectGridView.getList().get(j).getSelected()) {
                            for (int k = 0; k < mViewModel.getAccountList().size(); k++) {
                                if (selectGridView.getList().get(j).getContentNameID().equals(mViewModel.getAccountList().get(k).getAccountId())) {
                                    mViewModel.setAccountBean(mViewModel.getAccountList().get(k));
                                }
                            }
                        }
                    }
                }
                mViewModel.setSelect(true);
                setPartStatusLoading();
                isCallWhichInterface("", false);

                txtSelect.setTextColor(getResources().getColor(R.color.boc_text_color_red));
            }

            @Override
            public void resetClick() {// 重置
                if (mSingleList == null) {
                    return;
                }
                for (int i = 0; i < mSingleList.size(); i++) {
                    if (i == 0) {
                        mSingleList.get(i).setSelected(true);
                    } else {
                        if (mSingleList.get(i).getSelected()) {
                            mSingleList.get(i).setSelected(false);
                        }
                    }
                }
                selectGridView.getAdapter().notifyDataSetChanged();
            }
        });

        selectTypeSort.setClickListener(new SelectTypeView.SelectListener() {// 排序
            @Override
            public void onClick(List<String> selectIds) {
                mViewModel.setFirst(false);
                isFirstSorted = true;
                mCurSortIds = selectIds;
                initRequestData();
                setPartStatusLoading();
                isCallWhichInterface("", false);
            }

            @Override
            public void resetClick() {

            }
        });
    }

    /**
     * 设置ListView的滚动监听
     *
     * @param isCancel
     */
    private void setListViewScrollListener(boolean isCancel) {
        if (isCancel) {
            listView.setOnScrollListener(null);
            return;
        }

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {// 滚动监听
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (!slipDrawerLayout.isOpen())// 侧滑关闭隐藏软键盘
                    hideSoftInput();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem >= 1 && editSearch.hasFocus())
                    editSearch.clearFocus();

              /* if (slipDrawerLayout.isOpen() || editSearch.hasFocus())
                    return;*/

                //Log.d("dding", "---first:" + firstVisibleItem);
                if (firstVisibleItem >= 1) {
                    isHover = true;
                    if (llParentSearch.getVisibility() != View.VISIBLE) {
                        llParentSearch.setVisibility(View.VISIBLE);
                    }
                    if (!StringUtils.isEmptyOrNull(editSearch.getText().toString())) {
                        editSearch2.setText(editSearch.getText().toString());
                        editSearch2.setSelection(editSearch2.getText().length());
                        editSearch.setText("");
                    }
                    if (isClick[0] && !txtSort2.isSelected() && isFirstSorted) {
                        changeRed(txtSort2, R.drawable.icon_sort_red);
                        txtSort2.setSelected(true);
                    }
                    if (isClick[1] && !txtSelect2.isSelected() && isFirstSelected) {
                        changeRed(txtSelect2, R.drawable.boc_select_red);
                        txtSelect2.setSelected(true);
                    }
                } else if (firstVisibleItem == 0) {
                    isHover = false;
                    editSearch.requestFocus();
                    if (llParentSearch.getVisibility() != View.GONE) {
                        llParentSearch.setVisibility(View.GONE);
                    }

                    if (!StringUtils.isEmptyOrNull(editSearch2.getText().toString())) {
                        editSearch.setText(editSearch2.getText().toString());
                        editSearch.setSelection(editSearch2.getText().length());
                        editSearch2.setText("");
                    }
                    if (isClick[0] && !txtSort.isSelected() && isFirstSorted) {
                        changeRed(txtSort, R.drawable.icon_sort_red);
                        txtSort.setSelected(true);
                    }
                    if (isClick[1] && !txtSelect.isSelected() && isFirstSelected) {
                        changeRed(txtSelect, R.drawable.boc_select_red);
                        txtSelect.setSelected(true);
                    }
                }
            }
        });
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    protected WealthPresenter initPresenter() {
        return new WealthPresenter(this, false);
    }

    @Override
    public void reInit() {
//        mViewModel.setProductList(WealthPublicUtils.sortList(mViewModel.getProductList(), preferences));
        mAdapter.notifyDataSetChanged();
        if (!isLogined && ApplicationContext.getInstance().isLogin()) {// 未登录改为登录
            titleView.setBackgroundResource(R.color.boc_text_color_money_count);
            isLogined = true;
            WealthPublicUtils.resetSelectDefault(mSortList);// 重置排序状态
            selectTypeSort.getAdapter().notifyDataSetChanged();
            WealthPublicUtils.resetSelectDefault(mSelectList);// 重置筛选状态
            selectTypeView.getAdapter().notifyDataSetChanged();

            rlHead.setBackgroundResource(R.color.boc_text_color_money_count);
            llTab.setVisibility(View.VISIBLE);// 登录显示（交易查询、投资协议管理、撤单）
            txtLogin.setText(mContext.getString(R.string.boc_wealth_position));// 显示持仓按钮
            initRequestData();
            callLoginInterface();
        }
    }

    /**
     * 设置页面公共信息
     */
    private void setCommonInfo() {
        hideSoftInput();
        preferences = mContext.getSharedPreferences(WealthDetailsFragment.FILE_NAME, Context.MODE_APPEND);
        instance = this;
        mViewModel = new WealthViewModel();
        mAdapter = new WealthProductAdapter(mContext);
        listView.setAdapter(mAdapter);
        /*标题*/
        titleView.setStyle(R.style.titlebar_common_red);
        titleView.setBackgroundResource(R.color.boc_text_color_money_count);
        titleView.setTitle(R.string.boc_wealth_title);
        setTitleRightView();
        /*初始化排序与筛选*/
        selectTypeSort.setSelectView(true, 2);
        selectTypeView.setSelectView(true, 2);
        mSortList = WealthViewData.buildSortData();
        mSelectList = WealthViewData.buildSelectData();
        mCurSortIds = new ArrayList<>();
        for (SelectTypeData item : mSortList) {
            mCurSortIds.add(item.getDefaultId());
        }
        mCurSelectIds = new ArrayList<>();
        for (SelectTypeData item : mSelectList) {
            mCurSelectIds.add(item.getDefaultId());
        }
        selectTypeSort.setData(mSortList);
        selectTypeView.setData(mSelectList);
    }

    @Override
    public void onResume() {
        super.onResume();
        setDefaultFocus();
//        hideSoftInput();
    }

    /**
     * 设置页面的默认焦点
     */
    private void setDefaultFocus() {
        rlSelect.setFocusable(true);
        rlSelect.setFocusableInTouchMode(true);
        rlSelect.requestFocus();
    }

    /**
     * 设置标题右边View
     */
    private void setTitleRightView() {
        LinearLayout linearLayout = titleView.getRightContainer();
        linearLayout.removeAllViews();
        View view = View.inflate(mContext, R.layout.boc_fragment_title_right, linearLayout);
        ImageView imgL = (ImageView) view.findViewById(R.id.img_left);
        ImageView imgR = (ImageView) view.findViewById(R.id.img_right);
        imgL.setOnClickListener(new View.OnClickListener() {// 分享
            @Override
            public void onClick(View v) {
                if (!ButtonClickLock.isCanClick(clickOprLock)) {// 防止暴力点击
                    return;
                }
                String title = getString(R.string.boc_wealth_title);
                String description = getString(R.string.boc_wealth_share_content);
                SendMessageToWX.Req req = ShareUtils.shareWebPage(0, WealthConst.getProductListShareUrl(), title, description);
                if (getApi() != null)
                    getApi().sendReq(req);
            }
        });
        imgR.setOnClickListener(new View.OnClickListener() {// 更多菜单
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(MenuFragment.MENU, MenuFragment.WEALTH);
                MenuFragment fragment = new MenuFragment();
                fragment.setArguments(bundle);
                start(fragment);
            }
        });
    }

    /**
     * 获取无结果显示的高度
     */
    private void getNoResultHeight() {
        headView.post(new Runnable() {
            @Override
            public void run() {
                int screenHeight = mActivity.getWindowManager().getDefaultDisplay().getHeight();// 获取屏幕的高度
                int statusBarHeight = getStatusBarHeight();// 状态栏的高度
                int titleBarHeight = mActivity.getResources().getDimensionPixelOffset(R.dimen.boc_titlebar_height);// 标题栏的高度
                int headViewHeight = headView.getHeight() + headView2.getHeight();
                int noResultHeight = rlParentEmpty.getHeight();
                int topMargin = (screenHeight - statusBarHeight - titleBarHeight - headViewHeight - noResultHeight) / 2 + headViewHeight;
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) rlParentEmpty.getLayoutParams();
                layoutParams.topMargin = topMargin;
                rlParentEmpty.setLayoutParams(layoutParams);
            }
        });
    }

    /**
     * 初始化网络请求理财列表的数据
     */
    private void initRequestData() {
        mCurIndex = 0;
        mViewModel.setRecordNumber("0");
        if (mViewModel.getProductList() != null) {
            mViewModel.getProductList().clear();
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 共用同一个model
     *
     * @return
     */
    public WealthViewModel getViewModel() {
        if (mViewModel == null)
            mViewModel = new WealthViewModel();
        return mViewModel;
    }

    /**
     * 当前实例
     *
     * @return
     */
    public static WealthProductFragment getInstance() {
        if (instance == null)
            instance = new WealthProductFragment();
        return instance;
    }

    /**
     * 获取开通投资理财的状态（缓存开通状态）
     *
     * @return
     */
    public boolean[] isOpenWealth() {
        return mOpenStatus;
    }

    /**
     * 设置投资理财的状态
     *
     * @param isOpenWealth
     */
    public void setOpenWealth(boolean[] isOpenWealth) {
        this.mOpenStatus = isOpenWealth;
    }

    public void setRiskStatus(int riskStatus) {
        this.riskRatingStatus = riskStatus;
    }

    public int getRiskStatus(){
        return riskRatingStatus;
    }

    /**
     * 查询接口请求状态（优先判断）
     *
     * @return
     */
    public boolean getRequestStatus() {
        return mRequestStatus;
    }

    public void setRequestStatus(boolean requestStatus) {
        this.mRequestStatus = requestStatus;
    }

    /**
     * 调用是否开通投资理财接口
     */
    public void requestOpenStatus() {
        getPresenter().queryOpenStatus(openStatusI);
    }

    /**
     * 登录成功需要调用的接口
     */
    private void callLoginInterface() {
        setRequestStatus(false);
        showLoadingDialog(false);
        getPresenter().queryAssetBalance();// 参考市值
        getPresenter().queryRiskEvaluation();// 是否风险评估
    }

    /**
     * 调用哪个查询理财列表接口
     */
    private void isCallWhichInterface(String prodCode, boolean isSearch) {
        /*搜索|排序|筛选|上拉加载*/
        mViewModel.setSortFlag(mCurSortIds.get(0));// 排序方式
        mViewModel.setSortType(mCurSortIds.get(1));// 排序类型
        int i = 0;
        mViewModel.setIssueType(mCurSelectIds.get(i++));// 产品类型
        mViewModel.setProductCurCode(mCurSelectIds.get(i++));// 产品币种
        mViewModel.setDayTerm(mCurSelectIds.get(i++));// 产品期限
        mViewModel.setProductRiskType(mCurSelectIds.get(i++));// 收益类型
        mViewModel.setProRisk(mCurSelectIds.get(i++));// 风险等级
        if (mViewModel.isLoginBeforeI()) {
            getPresenter().queryProductListN(mCurIndex, prodCode, isSearch);
        } else {
            getPresenter().queryProductListY(mCurIndex, prodCode, isSearch);
        }
    }

    /**
     * 更新数据（产品列表成功）
     *
     * @param viewModel
     */
    private void updateListData(WealthViewModel viewModel, boolean isLoginBeforeI) {
        List<WealthListBean> newList = viewModel.getProductList();// 响应数据
        List<WealthListBean> oldList = mViewModel.getProductList();// ui model存储总数据
        if (oldList == null) {
            oldList = new ArrayList<WealthListBean>();
        }
        oldList.addAll(newList);
        if (mCurIndex == ApplicationConst.WEALTH_PAGE_SIZE) {// 只对第一页数据进行排序
            mViewModel.setProductList(WealthPublicUtils.sortList(oldList, preferences));
        } else {
            mViewModel.setProductList(oldList);
        }
        mAdapter.setData(mViewModel.getProductList(), isLoginBeforeI);// 页面显示数据
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 添加筛选账户
     */
    private void addSelectAccount() {
        /*显示理财账户view*/
        mViewModel.setAccountBean(mViewModel.getAccountList().get(0));
        mSingleList = WealthViewData.buildSingleData(mViewModel.getAccountList());
        View view = View.inflate(mContext, R.layout.boc_fragment_wealth_select_account, null);
        selectGridView = (SelectGridView) view.findViewById(R.id.select_gridview);
        selectGridView.setData(mSingleList);
        selectTypeView.getParentView().addView(view);
    }

    /**
     * 设置局部正在加载
     */
    private void setPartStatusLoading() {
        partialLoadView.setLoadStatus(PartialLoadView.LoadStatus.LOADING);
        pullToRefreshLayout.setMove(false);
    }

    /**
     * 设置局部加载完成
     */
    private void setPartStatusSuccess() {
        partialLoadView.setLoadStatus(PartialLoadView.LoadStatus.SUCCESS);
        pullToRefreshLayout.setMove(true);
    }

    /**
     * 查询市值（成功）
     *
     * @param result
     */
    @Override
    public void queryAssetBalanceSuccess(String result) {
        /*市值大于等于0显示*/
        if (!StringUtils.isEmptyOrNull(result) && BigDecimal.valueOf(0.0).compareTo(new BigDecimal(result)) != 1) {
            txtLogin.setVisibility(View.GONE);// 隐藏持仓按钮
            llParentOCRM.setVisibility(View.VISIBLE);// 显示市值
            imgHelp.setVisibility(View.VISIBLE);// 显示帮助
            riseNumberTextView(txtOCRM, Float.valueOf(result), 500);
        }
    }

    /**
     * 是否开通投资理财服务（失败）
     */
    @Override
    public void isOpenInvestmentManageFail() {
        closeProgressDialog();
        pullToRefreshLayout.setMove(false);// 不允许上拉加载
        setRequestStatus(false);
    }

    /**
     * 是否开通投资理财服务（成功）
     *
     * @param result
     */
    @Override
    public void isOpenInvestmentManageSuccess(boolean result) {
        if (result) {// 已开通
            mOpenStatus[0] = true;
            getPresenter().queryFinanceAccountInfo();// 查询客户理财账户信息
        } else {// 未开通
            closeProgressDialog();
            setRequestStatus(false);
            mOpenStatus[0] = false;// 理财服务
            mViewModel.setFirst(true);
            setPartStatusLoading();
            getPresenter().queryProductListN(mCurIndex, "", false);// 理财列表（登录前）
        }
    }

    /**
     * 查询客户理财账户信息（失败）
     */
    @Override
    public void queryFinanceAccountInfoFail() {
        closeProgressDialog();
        pullToRefreshLayout.setMove(false);// 不允许上拉加载
        setRequestStatus(false);
    }

    /**
     * 查询客户理财账户信息（成功）
     */
    @Override
    public void queryFinanceAccountInfoSuccess() {
        closeProgressDialog();
        if (mViewModel.getAccountList().size() > 0) {// 已登记账户
            setRequestStatus(true);
            mOpenStatus[2] = true;
            addSelectAccount();// 添加理财账户列表
            getPresenter().queryBuyInit();// 产品查询与购买初始化
        } else {// 未登记账户
            setRequestStatus(false);
            mOpenStatus[2] = false;// 登记账户
            mViewModel.setFirst(true);
            setPartStatusLoading();
            getPresenter().queryProductListN(mCurIndex, "", false);// 理财列表（登录前）
        }
    }

    /**
     * 风险评估查询（失败）
     */
    @Override
    public void queryRiskEvaluationFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        setRequestStatus(false);
    }

    /**
     * 风险评估查询（成功）
     *
     * @param evaluatedBefore
     */
    @Override
    public void queryRiskEvaluationSuccess(String custExist, String evaluatedBefore, String evalExpired) {
        getPresenter().isOpenInvestmentManage();// 投资理财服务
        if ("0".equals(custExist)) {// 存在且总协议有效
            if ("true".equals(evaluatedBefore) && WealthConst.NO_1.equals(evalExpired)) {
                mOpenStatus[1] = true;
            } else {
                mOpenStatus[1] = false;
                if ("false".equals(evaluatedBefore)) {
                    riskRatingStatus = 0;
                }
                if (WealthConst.YES_0.equals(evalExpired)) {
                    riskRatingStatus = 1;
                }
            }
        } else {
            mOpenStatus[1] = false;
        }
        if (!mOpenStatus[1]) {
            setRequestStatus(false);
        }
    }

    /**
     * 查询购买初始化成功（登录后）
     */
    @Override
    public void queryBuyInitSuccess() {
        mViewModel.setFirst(true);
        setPartStatusLoading();
        getPresenter().queryProductListY(mCurIndex, "", false);// 理财产品列表接口（登录后）
    }

    /**
     * 查询产品列表失败（登录前）
     */
    @Override
    public void queryProductListNFail() {
        setPartStatusSuccess();
    }

    /**
     * 查询产品列表成功（登录前）
     */
    @Override
    public void queryProductListNSuccess(WealthViewModel viewModel) {
        setPartStatusSuccess();
        mViewModel.setRecordNumber(viewModel.getRecordNumber());
        if (Integer.valueOf(viewModel.getRecordNumber()) == 0) {// 无查询到数据
            txtNoResult.setVisibility(View.VISIBLE);// 显示无结果view
            if (mViewModel.isSearch()) {// 点击搜索后提示语
                txtNoResult.setText(getString(R.string.boc_wealth_no_data_search));
            } else if (mViewModel.isSelect()) {// 点击筛选确认按钮提示语
                txtNoResult.setText(getString(R.string.boc_wealth_no_data_select));
                mViewModel.setSelect(false);
            }
        } else {// 存在理财产品
            txtNoResult.setVisibility(View.GONE);
            mCurIndex += ApplicationConst.WEALTH_PAGE_SIZE;
            updateListData(viewModel, true);

            if (mIsPullToRefresh) {// 如果上拉加载显示成功view
                pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
                mIsPullToRefresh = false;
            }
        }
    }

    /**
     * 查询产品列表失败（登录后）
     */
    @Override
    public void queryProductListYFail() {
        setPartStatusSuccess();
        mIsPullToRefresh = false;
        pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.FAIL);
    }

    /**
     * 查询产品列表成功（登录后）
     *
     * @param viewModel
     */
    @Override
    public void queryProductListYSuccess(WealthViewModel viewModel) {
        setPartStatusSuccess();
        mViewModel.setRecordNumber(viewModel.getRecordNumber());
        if (Integer.valueOf(viewModel.getRecordNumber()) == 0) {// 无查询到数据
            txtNoResult.setVisibility(View.VISIBLE);// 显示无结果view
            if (mViewModel.isSearch()) {// 点击搜索后提示语
                txtNoResult.setText(getString(R.string.boc_wealth_no_data_search));
            } else if (mViewModel.isSelect()) {// 点击筛选确认按钮提示语
                txtNoResult.setText(getString(R.string.boc_wealth_no_data_select));
                mViewModel.setSelect(false);
            }
        } else {// 存在理财产品
            txtNoResult.setVisibility(View.GONE);
            mCurIndex += ApplicationConst.WEALTH_PAGE_SIZE;
            updateListData(viewModel, false);

            if (mIsPullToRefresh) {// 如果上拉加载显示成功view
                pullToRefreshLayout.loadmoreCompleted(PullToRefreshLayout.SUCCEED);
                mIsPullToRefresh = false;
            }
        }
    }

    /**
     * 查询产品详情失败（登录前）
     */
    @Override
    public void queryProductDetailNFail() {
        closeProgressDialog();
    }

    /**
     * 查询产品详情成功（登录前）
     */
    @Override
    public void queryProductDetailNSuccess(WealthDetailsBean detailsBean) {
        closeProgressDialog();
        Bundle bundle = new Bundle();
        detailsBean.setRemainCycleCount(mViewModel.getProductList().get(mCurPosition).getRemainCycleCount());// 剩余可购买最大期数.非周期性产品返回空
        bundle.putParcelable(WealthDetailsFragment.DETAILS_INFO, detailsBean);// 明细接口数据
        bundle.putParcelable(WealthDetailsFragment.DETAILS_REQUEST, WealthBundleData.buildDetailsExtraData(mViewModel, mCurPosition));
        WealthDetailsFragment fragment = new WealthDetailsFragment();
        fragment.setArguments(bundle);
        start(fragment);
    }

    /**
     * 查询产品详情失败（登录后）
     */
    @Override
    public void queryProductDetailYFail() {
        closeProgressDialog();
    }

    /**
     * 查询产品详情成功（登录后）
     */
    @Override
    public void queryProductDetailYSuccess(WealthDetailsBean detailsBean) {
        closeProgressDialog();
        Bundle bundle = new Bundle();
        detailsBean.setRemainCycleCount(mViewModel.getProductList().get(mCurPosition).getRemainCycleCount());// 剩余可购买最大期数.非周期性产品返回空
        bundle.putParcelable(WealthDetailsFragment.DETAILS_INFO, detailsBean);// 明细接口数据
        bundle.putParcelable(WealthDetailsFragment.DETAILS_REQUEST, WealthBundleData.buildDetailsExtraData(mViewModel, mCurPosition));
        WealthDetailsFragment fragment = new WealthDetailsFragment();
        fragment.setArguments(bundle);
        start(fragment);
    }

    /**
     * 设置开通理财状态的回调
     *
     * @param openStatusI
     */
    public void setCall(OpenStatusI openStatusI) {
        this.openStatusI = openStatusI;
    }

    /**
     * 判断跳转到哪个页面
     *
     * @param needs
     */
    public void judgeToFragment(final boolean[] needs, final BussFragment toFragment) {
        if (mRequestStatus) {// 接口调用成功
            startToFragment(needs, toFragment);
        } else {
            setCall(new OpenStatusI() {
                @Override
                public void openSuccess() {
                    closeProgressDialog();
                    startToFragment(needs, toFragment);
                }

                @Override
                public void openFail(ErrorDialog dialog) {
                    closeProgressDialog();
                }
            });
            showLoadingDialog(false);
            requestOpenStatus();
        }
    }

    /**
     * 开启哪一个fragment
     *
     * @param needs
     * @param toFragment
     */
    public void startToFragment(boolean[] needs, BussFragment toFragment) {
        if (WealthPublicUtils.isOpenAll(needs, mOpenStatus)) {// 开通所有
            start(toFragment);
        } else {
            InvestTreatyFragment fragment = new InvestTreatyFragment();
            fragment.setDefaultInvestFragment(needs, toFragment);
            start(fragment);
        }
    }

    /**
     * 点击登录按钮成功回调
     */
    class LoginCallbackImpl implements LoginCallback {

        @Override
        public void success() {
            titleView.setBackgroundResource(R.color.boc_text_color_money_count);
            isLogined = true;
            WealthPublicUtils.resetSelectDefault(mSortList);// 重置排序状态
            selectTypeSort.getAdapter().notifyDataSetChanged();
            WealthPublicUtils.resetSelectDefault(mSelectList);// 重置筛选状态
            selectTypeView.getAdapter().notifyDataSetChanged();

            rlHead.setBackgroundResource(R.color.boc_text_color_money_count);
            llTab.setVisibility(View.VISIBLE);// 登录显示（交易查询、投资协议管理、撤单）
            txtLogin.setText(mContext.getString(R.string.boc_wealth_position));// 显示持仓按钮
            initRequestData();
            callLoginInterface();
        }
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();

        int i = v.getId();
        if (i == R.id.txt_login) {
            if (getString(R.string.boc_common_login).equals(txtLogin.getText().toString())) {// 登录
                ModuleActivityDispatcher.startToLogin(mActivity, new LoginCallbackImpl());
            } else {// 持仓
                boolean[] needs = WealthViewData.needsStatus(ModuleCode.MODULE_BOCINVT_0100);
                judgeToFragment(needs, new FinancialPositionFragment());
            }
        } else if (i == R.id.txt_query) {// 交易查询
            boolean[] needs = WealthViewData.needsStatus(ModuleCode.MODULE_BOCINVT_0200);
            judgeToFragment(needs, TransInquireFragment.newinstance(mActivity, 0));
        } else if (i == R.id.txt_protocol) {// 投资协议管理
            boolean[] needs = WealthViewData.needsStatus(ModuleCode.MODULE_BOCINVT_0500);
            judgeToFragment(needs, new com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.investtreatymanage.investtreaty.ui.InvestTreatyFragment());
        } else if (i == R.id.txt_cancel) {// 撤单
            boolean[] needs = WealthViewData.needsStatus(ModuleCode.MODULE_BOCINVT_0400);
            judgeToFragment(needs, new RepealOrderFragment());
        } else if (i == R.id.txt_sort) {// 排序
            editSearch.setCursorVisible(false);
            isClick[0] = true;
            if (isFirstSorted) {
                changeRed(txtSort, R.drawable.icon_sort_red);
                changeRed(txtSort2, R.drawable.icon_sort_red);
            }
            hideSoftInput();
            for (int m = 0; m < mSortList.size(); m++) {
                for (int n = 0; n < mSortList.get(m).getList().size(); n++) {
                    Content item = mSortList.get(m).getList().get(n);
                    if (mCurSortIds.get(m).equals(item.getContentNameID())) {
                        item.setSelected(true);
                    } else {
                        item.setSelected(false);
                    }
                }
            }
            selectTypeSort.getAdapter().notifyDataSetChanged();
            selectTypeSort.setVisibility(View.VISIBLE);
            selectTypeView.setVisibility(View.GONE);
            selectTypeSort.setDrawerLayout(slipDrawerLayout);
            slipDrawerLayout.toggle();
        } else if (i == R.id.txt_select) {// 筛选
            editSearch.setCursorVisible(false);
            isClick[1] = true;
            if (isFirstSelected) {
                changeRed(txtSelect, R.drawable.boc_select_red);
                changeRed(txtSelect2, R.drawable.boc_select_red);
            }
            hideSoftInput();
            for (int m = 0; m < mSelectList.size(); m++) {
                for (int n = 0; n < mSelectList.get(m).getList().size(); n++) {
                    Content item = mSelectList.get(m).getList().get(n);
                    if (mCurSelectIds.get(m).equals(item.getContentNameID())) {
                        item.setSelected(true);
                    } else {
                        item.setSelected(false);
                    }
                }
            }
            selectTypeView.getAdapter().notifyDataSetChanged();
            if (selectGridView != null) {
                for (int j = 0; j < mSingleList.size(); j++) {
                    if (mSingleList.get(j).getContentNameID().equals(mViewModel.getAccountBean().getAccountId())) {
                        mSingleList.get(j).setSelected(true);
                    }
                }
                selectGridView.getAdapter().notifyDataSetChanged();
            }
            selectTypeSort.setVisibility(View.GONE);
            selectTypeView.setVisibility(View.VISIBLE);
            selectTypeView.setDrawerLayout(slipDrawerLayout);
            slipDrawerLayout.toggle();
        } else if (i == R.id.img_search) {// 搜索
            if (!ButtonClickLock.isCanClick(clickOprLock)) {// 防止暴力点击
                return;
            }
            initRequestData();
            setPartStatusLoading();
            if (isHover) {
                isCallWhichInterface(editSearch2.getText().toString(), true);
            } else {
                isCallWhichInterface(editSearch.getText().toString(), true);
            }
        } else if (i == R.id.img_help) {// 帮助
            showErrorDialog(mContext.getString(R.string.boc_wealth_help));
        } else if (i == R.id.ll_parent_ocrm) {// 市值
            boolean[] needs = WealthViewData.needsStatus(ModuleCode.MODULE_BOCINVT_0100);
            judgeToFragment(needs, new FinancialPositionFragment());
        }
    }

    /**
     * 详情监听
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showLoadingDialog(false);
        mCurPosition = position - 2;// ListView的Header占用position 0
        WealthListBean curWealthItem = mViewModel.getProductList().get(mCurPosition);
        if (mViewModel.isLoginBeforeI()) {// 登录前

            DetailsRequestBean requestBean = new DetailsRequestBean();
            requestBean.setProdCode(curWealthItem.getProdCode());
            /*产品性质*/
            if (WealthConst.PRODUCT_TYPE_2.equals(curWealthItem.getIssueType())) {
                requestBean.setProdKind(WealthConst.PRODUCT_KIND_1);// 净值开放类
            } else {
                requestBean.setProdKind(WealthConst.PRODUCT_KIND_0);// 结构性
            }
            getPresenter().queryProductDetailN(requestBean);
        } else {// 登录后
            DetailsRequestBean requestBean = new DetailsRequestBean();
            requestBean.setProdCode(curWealthItem.getProdCode());// 产品代码
            requestBean.setProdKind(curWealthItem.getProductKind());// 产品性质
            requestBean.setIbknum(mViewModel.getAccountBean().getIbkNumber());// 省行联行号
            getPresenter().queryProductDetailY(requestBean);
        }
    }

    private void changeRed(TextView textView, int id) {
        Drawable drawable = mContext.getResources().getDrawable(id);
        textView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        textView.setTextColor(mContext.getResources().getColor(R.color.boc_text_color_red));
    }

    /**
     * 金额带滚动效果
     *
     * @param textView
     * @param value
     * @param duration
     */
    private void riseNumberTextView(final TextView textView, float value, long duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, value);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                String str = animation.getAnimatedValue().toString();
                textView.setText(MoneyUtils.transMoneyFormat(str, ApplicationConst.CURRENCY_CNY));
            }
        });
        valueAnimator.start();
    }

    /**
     * 获取状态栏的高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        int height = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }

    @Override
    public boolean onBack() {
        if (isFromAccountManager) {
            popToAndReInit(OverviewFragment.class);
            return false;
        }
        return super.onBack();
    }

    @Override
    public boolean onBackPress() {
        if (isFromAccountManager) {
            popToAndReInit(OverviewFragment.class);
            return true;
        }
        return super.onBackPress();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButtonClickLock.removeLock(clickOprLock);
    }
}
