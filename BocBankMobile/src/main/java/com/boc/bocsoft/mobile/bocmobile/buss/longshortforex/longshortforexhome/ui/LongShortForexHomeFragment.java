package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.swipeRefreshLayout.SuperSwipeRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.buss.MenuFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.common.LoginCallback;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.adapter.LongShortForexHomeListAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.model.psnGetAllExchangeRatesOutlay.PsnGetAllExchangeRatesOutlayResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.model.wfssQueryMultipleQuotation.WFSSQueryMultipleQuotationResModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.presenter.LongShortForexContract;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.presenter.LongShortForexPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.utils.LongShortForexHomeCodeModelUtil;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.widget.TitleBarView;
import java.util.ArrayList;
import java.util.List;

/**
 * 双向宝首页列表
 * Created by yx on 2016/12/14
 */
public class LongShortForexHomeFragment extends MvpBussFragment<LongShortForexPresenter> implements View.OnClickListener,
        AdapterView.OnItemClickListener, LongShortForexContract.LongShortForexHomeView {

    public static final String OTHER = "other";
    //================view 初始化=============
    private View rootView;
    private TitleBarView titleView;// 标题
    //    private PullAndPushLayout pull_push_refresh;// 上拉刷新
//    private PullableListView listView;// 列表
    private TextView tv_no_result;
    private SuperSwipeRefreshLayout pull_push_refresh;// 上拉刷新
    private ListView listView;// 列表

    //----子布局1-----登录前---------
    private RelativeLayout ll_long_short_forex_login_before;//登录前显示布局
    private TextView tv_lsf_login;// 登录|持仓
    //-----子布局1-----登录后---------
    private LinearLayout ll_long_short_forex_login_after;//登录后显示布局
    private ImageView imgHelp;// 帮助
    private TextView tv_market_value_title;// 参考市值标题
    private TextView tv_market_value;// 参考市值
    private ImageView img_open_eye;//图片眼睛
    private LinearLayout ll_tab_button;// 交易查询|保证金交易|委托交易查询
    private TextView tv_transaction_query;// 交易查询
    private TextView tv_margin_transaction;// 保证金交易
    private TextView tv_entrust_query;// 委托交易查询

    //----------子布局2{自选，贵金属，外汇}-----
    private LinearLayout ll_parent_query_button;// 滑到最上面固定不能滑动的布局{自选，贵金属，外汇}
    private LinearLayout ll_self_ly;//自选布局
    private Button btn_selfchose;//自选
    private View ly_selfchose;
    private Button btn_gold;//贵金属
    private View ly_gold;
    private Button btn_forex;//外汇
    private View ly_forex;
    private TextView tv_edit;//编辑
    private TextView tv_riseorfall;//涨幅跌/涨跌值
    //    -----------------悬停时候的view 初始化---------
    private LinearLayout ll_self_ly2;//自选布局
    private Button btn_selfchose2;//自选
    private View ly_selfchose2;
    private Button btn_gold2;//贵金属
    private View ly_gold2;
    private Button btn_forex2;//外汇
    private View ly_forex2;
    private TextView tv_edit2;//编辑
    private TextView tv_riseorfall2;//涨幅跌/涨跌值
    //-----------子布局3{底部显示数据更新时间}----
    private TextView tv_bottom_content;
    //==================变量定义=========
    private static LongShortForexHomeFragment instance;
    private LongShortForexHomeListAdapter mAdapter;// 双向宝列表适配器
    private int mCurPosition;// 当前点击的item
    private boolean mIsLoginBeforeI = false;// 是否为登录前接口
    private boolean mRequestStatus = true;
    private static boolean[] mOpenStatus = new boolean[3];
    private OpenStatusI openStatusI;
    private View headView;//
    private View headViewTwo;//
    private View footerView;//
    private boolean isSort = false;//是否按照涨跌幅排序
    /**
     * 涨跌幅升=1(UP)、降序=2(DN)、不排序=0(""or null) 标识
     */
    private int mQuoteChangeId = 0;
    /**
     * 当前操作状态-one 自选，two 贵金属，three 外汇
     */
    private LongShorForeStatus mStatus = LongShorForeStatus.TWO;
    //涨跌幅排序 UP-升序，DN-降序，为空按优先级排序
    private String mpSort = "";


    public enum LongShorForeStatus {//自选，贵金属，外汇
        ONE, TWO, THREE
    }

    private Handler mHandler;//定时刷新 handler
    private int mTitleBarHeight = 0;//标题栏高度
    private int mHeadViewHeight = 0;//headViewHeight 高度

    //--------------------登录前-----接口-----------start---
    private WFSSQueryMultipleQuotationResModel mWfssQueryMultipleQuotationResModelOld = null;//登录前-涨跌幅/值 model 旧数据
    private WFSSQueryMultipleQuotationResModel mWfssQueryMultipleQuotationResModel = null;//登录前-涨跌幅/值 model  新数据
    //登录前---买入卖出价 接口返回数据
    private List<PsnGetAllExchangeRatesOutlayResModel> mListPsnGetAllExchangeRatesOutlayResModel = new ArrayList<PsnGetAllExchangeRatesOutlayResModel>();
    //--------------------登录前-----接口-----------end---

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = View.inflate(mContext, R.layout.boc_fragment_long_short_forex, null);
        return rootView;
    }

    @Override
    public void initView() {
        //主布局
        titleView = (TitleBarView) rootView.findViewById(R.id.title_view);
//        pull_push_refresh = (PullAndPushLayout) rootView.findViewById(R.id.pull_push_refresh);
//        listView = (PullableListView) rootView.findViewById(R.id.listview);
        tv_no_result = (TextView) rootView.findViewById(R.id.tv_no_result);
        pull_push_refresh = (SuperSwipeRefreshLayout) rootView.findViewById(R.id.pull_push_refresh);
        listView = (ListView) rootView.findViewById(R.id.listview);


        ll_parent_query_button = (LinearLayout) rootView.findViewById(R.id.ll_parent_query_button);
        ll_self_ly2 = (LinearLayout) ll_parent_query_button.findViewById(R.id.ll_self_ly);//自选
        btn_selfchose2 = (Button) ll_parent_query_button.findViewById(R.id.btn_selfchose);//自选
        ly_selfchose2 = (View) ll_parent_query_button.findViewById(R.id.ly_selfchose);
        btn_gold2 = (Button) ll_parent_query_button.findViewById(R.id.btn_gold);//贵金属
        ly_gold2 = (View) ll_parent_query_button.findViewById(R.id.ly_gold);
        btn_forex2 = (Button) ll_parent_query_button.findViewById(R.id.btn_forex);//外汇
        ly_forex2 = (View) ll_parent_query_button.findViewById(R.id.ly_forex);
        tv_edit2 = (TextView) ll_parent_query_button.findViewById(R.id.tv_edit);//编辑
        tv_riseorfall2 = (TextView) ll_parent_query_button.findViewById(R.id.tv_riseorfall);//涨幅跌/涨跌值
        ll_parent_query_button.setVisibility(View.GONE);

        headView = View.inflate(mContext, R.layout.boc_fragment_long_short_forex_content, null);
//        foot
        headViewTwo = View.inflate(mContext, R.layout.boc_fragment_long_short_forex_among_title, null);
        footerView = View.inflate(mContext, R.layout.boc_fragment_long_short_forex_footer, null);
        listView.addHeaderView(headView, null, false);// ListView添加HeaderView
        listView.addHeaderView(headViewTwo);
        listView.addFooterView(footerView);
        footerView.setVisibility(View.INVISIBLE);
//        子布局1------
        ll_long_short_forex_login_before = (RelativeLayout) headView.findViewById(R.id.ll_long_short_forex_login_before);
        ll_long_short_forex_login_after = (LinearLayout) headView.findViewById(R.id.ll_long_short_forex_login_after);
        tv_lsf_login = (TextView) headView.findViewById(R.id.tv_lsf_login);
        ll_tab_button = (LinearLayout) headView.findViewById(R.id.ll_tab_button);
        tv_transaction_query = (TextView) headView.findViewById(R.id.tv_transaction_query);
        tv_margin_transaction = (TextView) headView.findViewById(R.id.tv_margin_transaction);
        tv_entrust_query = (TextView) headView.findViewById(R.id.tv_entrust_query);
        imgHelp = (ImageView) headView.findViewById(R.id.img_help);
        img_open_eye = (ImageView) headView.findViewById(R.id.img_open_eye);
        tv_market_value_title = (TextView) headView.findViewById(R.id.tv_market_value_title);
        tv_market_value = (TextView) headView.findViewById(R.id.tv_market_value);

//        子布局2------
        ll_self_ly = (LinearLayout) headViewTwo.findViewById(R.id.ll_self_ly);//自选
        btn_selfchose = (Button) headViewTwo.findViewById(R.id.btn_selfchose);//自选
        ly_selfchose = (View) headViewTwo.findViewById(R.id.ly_selfchose);
        btn_gold = (Button) headViewTwo.findViewById(R.id.btn_gold);//贵金属
        ly_gold = (View) headViewTwo.findViewById(R.id.ly_gold);
        btn_forex = (Button) headViewTwo.findViewById(R.id.btn_forex);//外汇
        ly_forex = (View) headViewTwo.findViewById(R.id.ly_forex);
        tv_edit = (TextView) headViewTwo.findViewById(R.id.tv_edit);//编辑
        tv_riseorfall = (TextView) headViewTwo.findViewById(R.id.tv_riseorfall);//涨幅跌/涨跌值
        //子布局3------底部显示更新时间
        tv_bottom_content = (TextView) footerView.findViewById(R.id.tv_bottom_content);
        headView.post(new Runnable() {
            @Override
            public void run() {
                // 获取屏幕的高度
                WindowManager windowManager = mActivity.getWindowManager();
                int screenHeight = windowManager.getDefaultDisplay().getHeight();

                // 获取状态栏的高度
                int statusBarHeight = 0;
                int resourceId = mActivity.getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    statusBarHeight = (int) mActivity.getResources().getDimension(resourceId);
                }
                // 获取titleBar的高度
                int titleBarHeight = (int) mActivity.getResources().getDimension(R.dimen.boc_titlebar_height);
                // 获取headView的高度
                int headViewHeight = headView.getHeight();

                // 获取tv_no_result的高度
                int tv_no_resultHeight = tv_no_result.getHeight();
                mTitleBarHeight = titleBarHeight;
                mHeadViewHeight = headViewHeight;
                // 计算tv_no_result的上边距
                int topMargin = (screenHeight - statusBarHeight - titleBarHeight - headViewHeight - tv_no_resultHeight) / 2 + headViewHeight;

                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) tv_no_result.getLayoutParams();
                layoutParams.topMargin = topMargin;
                tv_no_result.setLayoutParams(layoutParams);
            }
        });
    }

    @Override
    public void initData() {
        mHandler = new Handler();
        setCommonInfo();
        if (ApplicationContext.getInstance().isLogin()) {// 已登录
            showLoginView(true);
            callLoginInterface();
            mStatus = LongShorForeStatus.ONE;
        } else {// 未登录
            showLoginView(false);
            mStatus = LongShorForeStatus.TWO;
            mIsLoginBeforeI = true;
            setReqParams(mIsLoginBeforeI, mStatus, "");
        }
    }

    private SparseArray recordSp = new SparseArray(0);
    private int mCurrentfirstVisibleItem = 0;

    @Override
    public void setListener() {
        //主布局
        listView.setOnItemClickListener(this);
        imgHelp.setOnClickListener(this);
        //子布局1
        tv_lsf_login.setOnClickListener(this);
        tv_transaction_query.setOnClickListener(this);
        tv_margin_transaction.setOnClickListener(this);
        tv_entrust_query.setOnClickListener(this);
        //子布局2
        btn_selfchose.setOnClickListener(this);
        btn_gold.setOnClickListener(this);
        btn_forex.setOnClickListener(this);
        tv_edit.setOnClickListener(this);
        tv_riseorfall.setOnClickListener(this);
        //悬停布局---跟子布局2做同样操作
        btn_selfchose2.setOnClickListener(this);
        btn_gold2.setOnClickListener(this);
        btn_forex2.setOnClickListener(this);
        tv_edit2.setOnClickListener(this);
        tv_riseorfall2.setOnClickListener(this);

        titleView.setLeftButtonOnClickLinster(new View.OnClickListener() {// 标题返回键
            @Override
            public void onClick(View v) {
                titleLeftIconClick();
            }
        });


        pull_push_refresh.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                LogUtil.d("yx--------------下拉刷新");
                stopDelayedUpdateGold();
                setReqParams(mIsLoginBeforeI, mStatus, "");
                startDelayedUpdateGold();

//                pull_push_refresh.setRefreshing(false);
            }

            @Override
            public void onPullDistance(int distance) {


                LogUtil.d("yx--------------distance---" + distance);
            }

            @Override
            public void onPullEnable(boolean enable) {
                LogUtil.d("yx--------------onPullEnable---" + enable);
            }
        });
        mAdapter.setOnRightTextViewClickListener(new LongShortForexHomeListAdapter.OnRightTextViewClickListener() {
            @Override
            public void onRightTextViewClick(boolean isShowCurrPercentDiff) {
                mAdapter.setIscurrPercentDiff(!isShowCurrPercentDiff);
                if (isShowCurrPercentDiff) {
                    tv_riseorfall.setText("涨跌值");
                } else {
                    tv_riseorfall.setText("涨跌幅");
                }
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                mCurrentfirstVisibleItem = i;
                View firstView = absListView.getChildAt(0);
                if (null != firstView) {
                    ItemRecod itemRecord = (ItemRecod) recordSp.get(i);
                    if (null == itemRecord) {
                        itemRecord = new ItemRecod();
                    }
                    itemRecord.height = firstView.getHeight();
                    itemRecord.top = firstView.getTop();
                    recordSp.append(i, itemRecord);
                    int h = getScrollY();//滚动距离
                    LogUtil.d("yx----getScrollY---->" + getScrollY());

                    if (h < mTitleBarHeight * 3) {
                        if (h > 0) {
                            double alpha = (255.0 / (mTitleBarHeight * 3));
                            setTitleAlpha((int) (alpha * h));
                        } else
                            setTitleAlpha(0);
                    } else {
                        setTitleAlpha(255);
                    }

//                    if (h >= (mHeadViewHeight-mTitleBarHeight)) {
//                        ll_parent_query_button.setVisibility(View.VISIBLE);
//                    } else {
//                        ll_parent_query_button.setVisibility(View.GONE);
//                    }
                    if (mCurrentfirstVisibleItem >= 1) {
                        ll_parent_query_button.setVisibility(View.VISIBLE);
                    } else {
                        ll_parent_query_button.setVisibility(View.GONE);
                    }
                }

            }
        });
    }


    private int getScrollY() {
        int height = 0;
        for (int i = 0; i < mCurrentfirstVisibleItem; i++) {
            ItemRecod itemRecod = (ItemRecod) recordSp.get(i);
            height += itemRecod.height;
        }
        ItemRecod itemRecod = (ItemRecod) recordSp.get(mCurrentfirstVisibleItem);
        if (null == itemRecod) {
            itemRecod = new ItemRecod();
        }
        return height - itemRecod.top;
    }


    class ItemRecod {
        int height = 0;
        int top = 0;
    }


    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    public void reInit() {

    }

    /**
     * 设置页面公共信息
     */
    private void setCommonInfo() {
        instance = this;
        mAdapter = new LongShortForexHomeListAdapter(mContext);
        listView.setAdapter(mAdapter);
        /*标题*/
        titleView.setStyle(R.style.titlebar_common_red);

        if (ApplicationContext.getInstance().isLogin()) {
            titleView.setBackgroundResource(R.color.boc_text_color_light_blue);
        } else {
            titleView.getBackground().setAlpha(0);
        }
        titleView.setTitle(R.string.boc_long_short_forex_title);
        setTitleRightView();
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
                Toast.makeText(mContext, "待添加", Toast.LENGTH_LONG).show();
            }
        });
        imgR.setOnClickListener(new View.OnClickListener() {// 更多菜单
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("Menu", MenuFragment.LONG_SHORT_FOREX);
                MenuFragment fragment = new MenuFragment();
                fragment.setArguments(bundle);
                start(fragment);
            }
        });
    }


    /**
     * 当前实例
     *
     * @return
     */
    public static LongShortForexHomeFragment getInstance() {
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

    /**
     * 查询接口请求状态
     *
     * @return
     */
    public boolean getRequestStatus() {
        return mRequestStatus;
    }

    public void setRequestStatus(boolean requestStatus) {
        this.mRequestStatus = requestStatus;
    }


    public void setCall(OpenStatusI openStatusI) {
        this.openStatusI = openStatusI;
    }

    @Override
    protected LongShortForexPresenter initPresenter() {
        return new LongShortForexPresenter(this);
    }


    @Override
    public void setPresenter(BasePresenter presenter) {

    }

    //==================================接口处理段落==================start======

    //        -----------------------------登录前接口处理段落-------------------

    /**
     * WFSS-2.1 queryMultipleQuotation
     * 外汇、贵金属多笔行情查询-登录前-查询外汇实盘、外汇虚盘、贵金属实盘、贵金属虚盘行情、详情基本信息。
     * 涨跌幅/值
     *
     * @param viewModel
     */
    @Override
    public void obtainWfssQueryMultipleQuotationSuccess(WFSSQueryMultipleQuotationResModel viewModel) {
        stopPresenter();
        if (viewModel != null) {
            LogUtil.d("yx-------------登录前-----WFSS-2.1 成功");
            mAdapter.setOldWfssData(mWfssQueryMultipleQuotationResModelOld);
            mWfssQueryMultipleQuotationResModel = viewModel;
            if (mStatus == LongShorForeStatus.ONE) {

            } else if (mStatus == LongShorForeStatus.TWO) {
                mAdapter.setDatas(LongShortForexHomeCodeModelUtil.transverterCustomLoginBeforeModel(mContext, mListPsnGetAllExchangeRatesOutlayResModel, mWfssQueryMultipleQuotationResModel, isSort, true));
            } else if (mStatus == LongShorForeStatus.THREE) {
                mAdapter.setDatas(LongShortForexHomeCodeModelUtil.transverterCustomLoginBeforeModel(mContext, mListPsnGetAllExchangeRatesOutlayResModel, mWfssQueryMultipleQuotationResModel, isSort, false));
            }
            footerView.setVisibility(View.VISIBLE);
            mWfssQueryMultipleQuotationResModelOld = mWfssQueryMultipleQuotationResModel;
            startDelayedUpdateGold();
        }
    }

    /**
     * WFSS-2.1 queryMultipleQuotation
     * 外汇、贵金属多笔行情查询-登录前-查询外汇实盘、外汇虚盘、贵金属实盘、贵金属虚盘行情、详情基本信息。
     * 涨跌幅/值
     *
     * @param biiResultErrorException
     */
    @Override
    public void obtainWfssQueryMultipleQuotationFail(BiiResultErrorException biiResultErrorException) {
        LogUtil.d("yx-------------登录前-----WFSS-2.1失败 ");
        mAdapter.setOldWfssData(mWfssQueryMultipleQuotationResModelOld);
        if (mStatus == LongShorForeStatus.ONE) {

        } else if (mStatus == LongShorForeStatus.TWO) {
            mAdapter.setDatas(LongShortForexHomeCodeModelUtil.transverterCustomLoginBeforeModel(mContext, mListPsnGetAllExchangeRatesOutlayResModel, mWfssQueryMultipleQuotationResModelOld, isSort, true));
        } else if (mStatus == LongShorForeStatus.THREE) {
            mAdapter.setDatas(LongShortForexHomeCodeModelUtil.transverterCustomLoginBeforeModel(mContext, mListPsnGetAllExchangeRatesOutlayResModel, mWfssQueryMultipleQuotationResModelOld, isSort, false));
        }
        footerView.setVisibility(View.VISIBLE);
        stopPresenter();
        startDelayedUpdateGold();
    }

    /**
     * 4.18 018 PsnGetAllExchangeRatesOutlay 登录前贵金属、外汇、双向宝行情查询 成功响应
     *
     * @param viewListModel
     */
    @Override
    public void obtainPsnGetAllExchangeRatesOutlaySuccess(List<PsnGetAllExchangeRatesOutlayResModel> viewListModel) {
        stopPresenter();
        pull_push_refresh.setRefreshing(false);
        LogUtil.d("yx-------------登录前-----登录前贵金属、外汇、双向宝行情查询 成功响应");
        if (!PublicUtils.isEmpty(viewListModel)) {
            mListPsnGetAllExchangeRatesOutlayResModel = viewListModel;
            startPresenter();
            getPresenter().getWFSSQueryMultipleQuotation(LongShortForexHomeCodeModelUtil.buildWFSSQueryMultipleQuotationParams("" + mpSort, mStatus));
            tv_bottom_content.setText("数据更新于北京时间" + viewListModel.get(0).getUpdateDate() + "，具体价格以实际成交为准。");
        }
    }

    /**
     * 4.18 018 PsnGetAllExchangeRatesOutlay 登录前贵金属、外汇、双向宝行情查询-失败响应
     *
     * @param biiResultErrorException
     */
    @Override
    public void obtainPsnGetAllExchangeRatesOutlayFail(BiiResultErrorException biiResultErrorException) {
        stopPresenter();
        pull_push_refresh.setRefreshing(false);
        LogUtil.d("yx-------------登录前-----登录前贵金属、外汇、双向宝行情查询 失败响应");
    }

    //        -----------------------------登录后接口处理段落-------------------

    //==================================接口处理段落==================end======


    //=====================点击事件处理段落=================开始====
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_lsf_login) {// 登录|持仓
            if (mContext.getString(R.string.boc_common_login).equals(tv_lsf_login.getText().toString())) {
                ModuleActivityDispatcher.startToLogin(mActivity, new LoginCallbackImpl());
            }
        } else if (i == R.id.tv_transaction_query) {// 交易查询
//            boolean[] needs = {true, false, true};
//            judgeToFragment(needs, new TransInquireFragment());
        } else if (i == R.id.tv_margin_transaction) {// 账户管理
//            boolean[] needs = {true, false, true};// 开通服务与登记账户
//            judgeToFragment(needs, new InvestTreatyFragment());
        } else if (i == R.id.tv_entrust_query) {// 委托交易查询
//            boolean[] needs = {true, false, true};
//            judgeToFragment(needs, new RepealOrderFragment());
        } else if (i == R.id.img_help) {
            showErrorDialog(mContext.getString(R.string.boc_long_short_forex_help));
        } else if (i == R.id.img_open_eye) {
            //            img_open_eye
        } else if (i == R.id.btn_selfchose) {//自选按钮
            mStatus = LongShorForeStatus.ONE;
            stopDelayedUpdateGold();
            setButtonShow(mStatus);
        } else if (i == R.id.btn_gold) {//贵金属按钮
            isSort = false;
            tv_riseorfall.setText("涨跌幅");
            tv_riseorfall2.setText("涨跌幅");
            stopDelayedUpdateGold();
            mStatus = LongShorForeStatus.TWO;
            setButtonShow(mStatus);
            setReqParams(mIsLoginBeforeI, mStatus, "");
            startDelayedUpdateGold();
        } else if (i == R.id.btn_forex) {//外汇
            isSort = false;
            tv_riseorfall.setText("涨跌幅");
            tv_riseorfall2.setText("涨跌幅");
            stopDelayedUpdateGold();
            mStatus = LongShorForeStatus.THREE;
            setButtonShow(mStatus);
            setReqParams(mIsLoginBeforeI, mStatus, "");
            startDelayedUpdateGold();
        } else if (i == R.id.tv_riseorfall) {
            stopDelayedUpdateGold();
            mAdapter.setIscurrPercentDiff(true);
            /**第一次升序 第二次降序 第三次 不排序*/
            if (0 == mQuoteChangeId) {//第一次升序
                isSort = true;
                mQuoteChangeId = 1;
                Drawable drawablerise = getResources().getDrawable(R.drawable.forex_gold_rise_right_icon);
                drawablerise.setBounds(0, 0, drawablerise.getMinimumWidth() / 2, drawablerise.getMinimumHeight() / 2);
                tv_riseorfall.setCompoundDrawables(null, null, drawablerise, null);
                tv_riseorfall2.setCompoundDrawables(null, null, drawablerise, null);
                setReqParams(mIsLoginBeforeI, mStatus, "UP");
            } else if (1 == mQuoteChangeId) {//第二次降序
                isSort = true;
                mQuoteChangeId = 2;
                Drawable drawablefall = getResources().getDrawable(R.drawable.forex_gold_fall_right_icon);
                drawablefall.setBounds(0, 0, drawablefall.getMinimumWidth() / 2, drawablefall.getMinimumHeight() / 2);
                tv_riseorfall.setCompoundDrawables(null, null, drawablefall, null);
                tv_riseorfall2.setCompoundDrawables(null, null, drawablefall, null);
                setReqParams(mIsLoginBeforeI, mStatus, "DN");
            } else if (2 == mQuoteChangeId) {//第三次不排序
                isSort = false;
                mQuoteChangeId = 0;
                Drawable drawable = getResources().getDrawable(R.drawable.forex_default_right_icon);
                drawable.setBounds(0, 0, drawable.getMinimumWidth() / 2, drawable.getMinimumHeight() / 2);
                tv_riseorfall.setCompoundDrawables(null, null, drawable, null);
                tv_riseorfall2.setCompoundDrawables(null, null, drawable, null);
                setReqParams(mIsLoginBeforeI, mStatus, "");
            }
            startDelayedUpdateGold();
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
        LogUtil.d("yx-------itemClick---点击了第" + position + "条数据");

    }
    //=====================点击事件处理段落=================结束====

    //========================================自定义方法段落============开始======

    /**
     * 页面展示View（已登录和未登录）
     */
    private void showLoginView(boolean isLogin) {
        if (isLogin) {
            ll_long_short_forex_login_before.setVisibility(View.GONE);
            ll_long_short_forex_login_after.setVisibility(View.VISIBLE);
            ll_tab_button.setVisibility(View.VISIBLE);// 未登录隐藏（交易查询|保证金交易|委托交易查询）
            ll_self_ly.setVisibility(View.VISIBLE);
            btn_gold.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
            ly_gold.setVisibility(View.INVISIBLE);
            tv_riseorfall.setCompoundDrawables(null, null, null, null);
//            -------------------悬停view需要做同样处理------------

            ll_self_ly2.setVisibility(View.VISIBLE);
            btn_gold2.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
            ly_gold2.setVisibility(View.INVISIBLE);
            tv_riseorfall2.setCompoundDrawables(null, null, null, null);
            LogUtil.d("yx----------双向宝--已经登录");
        } else {
            ll_long_short_forex_login_before.setVisibility(View.VISIBLE);
            ll_long_short_forex_login_after.setVisibility(View.GONE);
            ll_tab_button.setVisibility(View.GONE);// 未登录隐藏（交易查询|保证金交易|委托交易查询）
            ll_self_ly.setVisibility(View.GONE);
            btn_gold.setTextColor(getResources().getColor(R.color.boc_text_color_red));
            ly_gold.setVisibility(View.VISIBLE);
            Drawable drawable = getResources().getDrawable(R.drawable.forex_default_right_icon);
            drawable.setBounds(0, 0, drawable.getMinimumWidth() / 2, drawable.getMinimumHeight() / 2);
            tv_riseorfall.setCompoundDrawables(null, null, drawable, null);
            ll_self_ly2.setVisibility(View.GONE);
            btn_gold2.setTextColor(getResources().getColor(R.color.boc_text_color_red));
            ly_gold2.setVisibility(View.VISIBLE);
            drawable.setBounds(0, 0, drawable.getMinimumWidth() / 2, drawable.getMinimumHeight() / 2);
            tv_riseorfall2.setCompoundDrawables(null, null, drawable, null);
            LogUtil.d("yx----------双向宝--未登录");
        }
    }

    /**
     * 调用的接口（已登录）
     */
    private void callLoginInterface() {
        mIsLoginBeforeI = false;
    }

    /**
     * 登录成功回调
     */
    class LoginCallbackImpl implements LoginCallback {

        @Override
        public void success() {
//            PublicUtils.resetSelectDefault(mSortList);
//            PublicUtils.resetSelectDefault(mSelectList);
            loginSuccess();
        }
    }


    /**
     * 登录成功后调用（View和接口）
     */
    private void loginSuccess() {
        showLoginView(true);
//        initRequestData();
        callLoginInterface();
    }

    /**
     * 是否开通 投资协议等 3个步骤
     */
    public interface OpenStatusI {
        void openSuccess();

        void openFail();
    }

    /**
     * 判断跳转到哪个页面
     *
     * @param needs
     */
    public void judgeToFragment(final boolean[] needs, final BussFragment toFragment) {
        if (mRequestStatus) {
            startToFragment(needs, toFragment);
        } else {
            setCall(new OpenStatusI() {
                @Override
                public void openSuccess() {
                    startToFragment(needs, toFragment);
                }

                @Override
                public void openFail() {

                }
            });
//            requestOpenStatus();
        }
    }

    /**
     * 开启哪一个fragment
     *
     * @param needs
     * @param toFragment
     */
    public void startToFragment(boolean[] needs, BussFragment toFragment) {
//        if (PublicUtils.isOpenAll(mOpenStatus)) {
//            start(toFragment);
//        } else {
//            InvesttreatyFragment fragment = new InvesttreatyFragment();
//            fragment.setDefaultInvestFragment(needs, toFragment);
//            start(fragment);
//        }
    }

    /**
     * 设置 自选、贵金属、外汇 三个按钮的点击效果
     *
     * @param status
     */
    private void setButtonShow(LongShorForeStatus status) {
        if (status == LongShorForeStatus.ONE) {//自选 button 按钮
            btn_selfchose.setTextColor(getResources().getColor(R.color.boc_text_color_red));
            ly_selfchose.setVisibility(View.VISIBLE);
            btn_gold.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
            ly_gold.setVisibility(View.INVISIBLE);
            btn_forex.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
            ly_forex.setVisibility(View.INVISIBLE);
            //-----------------------------悬停布局 同样操作----------------
            btn_selfchose2.setTextColor(getResources().getColor(R.color.boc_text_color_red));
            ly_selfchose2.setVisibility(View.VISIBLE);
            btn_gold2.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
            ly_gold2.setVisibility(View.INVISIBLE);
            btn_forex2.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
            ly_forex2.setVisibility(View.INVISIBLE);
        } else if (status == LongShorForeStatus.TWO) {//贵金属 button 按钮
            btn_gold.setTextColor(getResources().getColor(R.color.boc_text_color_red));
            ly_gold.setVisibility(View.VISIBLE);
            btn_selfchose.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
            ly_selfchose.setVisibility(View.INVISIBLE);
            btn_forex.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
            ly_forex.setVisibility(View.INVISIBLE);
            //-----------------------------悬停布局 同样操作----------------
            btn_gold2.setTextColor(getResources().getColor(R.color.boc_text_color_red));
            ly_gold2.setVisibility(View.VISIBLE);
            btn_selfchose2.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
            ly_selfchose2.setVisibility(View.INVISIBLE);
            btn_forex2.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
            ly_forex2.setVisibility(View.INVISIBLE);
        } else if (status == LongShorForeStatus.THREE) {//外汇 button 按钮
            btn_forex.setTextColor(getResources().getColor(R.color.boc_text_color_red));
            ly_forex.setVisibility(View.VISIBLE);
            btn_gold.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
            ly_gold.setVisibility(View.INVISIBLE);
            btn_selfchose.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
            ly_selfchose.setVisibility(View.INVISIBLE);
            //-----------------------------悬停布局 同样操作----------------
            btn_forex2.setTextColor(getResources().getColor(R.color.boc_text_color_red));
            ly_forex2.setVisibility(View.VISIBLE);
            btn_gold2.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
            ly_gold2.setVisibility(View.INVISIBLE);
            btn_selfchose2.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
            ly_selfchose2.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * @param isLoginBefor 是否登录
     * @param status       牌价类型
     * @param pSort        涨跌幅排序
     */
    private void setReqParams(boolean isLoginBefor, LongShorForeStatus status, String pSort) {
      //  List<String> s=new ArrayList<>();
      //  for (int i=0;i<40;i++) {
      //      s.add("商品"+i);
      //  }
      //mAdapter.setDatas(s);
      //      startDelayedUpdateGold();

        startPresenter();
        mpSort = pSort;
        if (isLoginBefor) {
            LogUtil.d("yx-------------登录前接口请求");
            //登录前 {贵金属（G）、外汇(F)}
            if (status == LongShorForeStatus.TWO) {
                //贵金属
                getPresenter().getPsnGetAllExchangeRatesOutlay(LongShortForexHomeCodeModelUtil.buildPsnGetAllExchangeRatesOutlayParams("40142", "G"));
            } else if (status == LongShorForeStatus.THREE) {
                //外汇
                getPresenter().getPsnGetAllExchangeRatesOutlay(LongShortForexHomeCodeModelUtil.buildPsnGetAllExchangeRatesOutlayParams("40142", "F"));
            }
        } else {
            LogUtil.d("yx-------------登录后接口请求");
            //登录后
        }
    }
    //-----------------------定时刷新操作--------------start----
    /**
     * 定时更新贵金属线程
     */
    Runnable updateGoldDataRunnable = new Runnable() {

        @Override
        public void run() {
            LogUtils.i("yx----------开始执行定时刷新双向宝");
            setReqParams(mIsLoginBeforeI, mStatus, mpSort);
            LogUtils.i("yx----------执行结束定时刷新双向宝");
        }
    };

    /**
     * 开始定时更新贵金属数据
     */
    private void startDelayedUpdateGold() {
        try {
            mHandler.postDelayed(updateGoldDataRunnable, 7000);

        } catch (Exception ex) {

        }
    }

    /**
     * 停止更新贵金属数据
     */
    private void stopDelayedUpdateGold() {
        mHandler.removeCallbacks(updateGoldDataRunnable);
    }

    //-----------------------定时刷新操作--------------end----
    private void setTitleAlpha(float titleAlpha) {
        titleView.getBackground().setAlpha((int) titleAlpha);
    }
    //========================================自定义方法段落============结束======

    @Override
    public boolean onBack() {
        return super.onBack();
    }
}
