package com.chinamworld.bocmbci.biz.foreign.details;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.base.application.CommonApplication;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.biz.foreign.Foreign;
import com.chinamworld.bocmbci.biz.foreign.ForeignBaseActivity;
import com.chinamworld.bocmbci.biz.foreign.ForeignDataCenter;
import com.chinamworld.bocmbci.biz.foreign.ForeignMoreActivity;
import com.chinamworld.bocmbci.biz.foreign.adapter.ForeignPriceExternalAdapter;
import com.chinamworld.bocmbci.biz.forex.customer.ForexCustomerRateInfoActivity;
import com.chinamworld.bocmbci.biz.forex.quash.ForexQuashQueryActivity;
import com.chinamworld.bocmbci.biz.forex.rate.ForexAccSettingActivity;
import com.chinamworld.bocmbci.biz.forex.strike.ForexStrikeQueryActivity;

import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.http.thread.PollingRequestThread;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.model.httpmodel.sifang.querymultiplequotation.QueryMultipleQuotationRequestParams;
import com.chinamworld.bocmbci.model.httpmodel.sifang.querymultiplequotation.QueryMultipleQuotationResponseData;
import com.chinamworld.bocmbci.model.httpmodel.sifang.querymultiplequotation.QueryMultipleQuotationResult;
import com.chinamworld.bocmbci.net.GsonTools;
import com.chinamworld.bocmbci.net.HttpHelp;
import com.chinamworld.bocmbci.net.model.BaseResponseData;
import com.chinamworld.bocmbci.net.model.IHttpErrorCallBack;
import com.chinamworld.bocmbci.net.model.IHttpResponseCallBack;
import com.chinamworld.bocmbci.net.model.IOkHttpErrorCode;
import com.chinamworld.bocmbci.server.LocalDataService;
import com.chinamworld.bocmbci.userwidget.investview.InvestPriceView;
import com.chinamworld.bocmbci.utils.KeyAndValueItem;
import com.chinamworld.bocmbci.utils.ListUtils;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.llbt.userwidget.NewBackGround.NewBackGroundLayout;
import com.chinamworld.llbt.userwidget.dialogview.MessageDialog;
import com.chinamworld.llbt.userwidget.refreshliseview.DownRefreshLayout;
import com.chinamworld.llbt.userwidget.refreshliseview.IRefreshLayoutListener;
import com.chinamworld.llbt.userwidget.refreshliseview.PullableListView;
import com.chinamworld.llbt.userwidget.refreshliseview.RefreshDataStatus;
import com.chinamworld.llbt.userwidget.scrollview.NewScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 外汇买卖  外置详情 -- 于登录行情
 * @see ForeignExchangeRateActivity
 * @author luqp 2016年9月22日
 */
public class ForeignExchangeRateActivity extends ForeignBaseActivity implements View.OnClickListener {
    private static final String TAG = "ForeignExchangeRateActivity";
    //报错信息
    private String message;
    /** 我的持仓布局*/
    private RelativeLayout myPositioned;
    /** 未登录广告位*/
    private RelativeLayout advertisementLoginLay;
    /** 登陆*/
    private Button landing;
    /** 交易查询*/
    private Button trade_query;
    /** 账户管理*/
    private Button account_management;
    /** 撤单*/
    private Button commissionDealQueryTv;
    /** 自选*/
    private LinearLayout free;
    /** 外汇*/
    private LinearLayout foreign_query;
    /** 自选颜色修改*/
    private TextView freeTvColor;
    /** 外汇颜色修改*/
    private TextView foreignTvColor;
    /** 自选按钮 下面图片*/
    private ImageView ima_foreign_free;
    /** 外汇按钮 下面图片*/
    private ImageView ima_foreign_query;
    /** 编辑*/
    private TextView foreignEditor;
    /** 涨跌幅点击事件*/
    private LinearLayout riseDropLl;
    /** 涨跌幅 设置文字*/
    private TextView riseDrop;
    /** 显示汇率的信息*/
    private PullableListView rateListView = null;
    /** 全部汇率是否有数据*/
    private boolean isHasAllList = false;
    /** 我的外汇汇率是否有数据*/
    private boolean isHasCustomerList = false;
    /** 判断是否是第一次加载适配器, flase不刷新动画 true刷新动画*/
    private boolean isFirstTime = false;
    /**
     * @customerRateList:自选详情list
     */
    private List<Map<String, Object>> customerRateList = null;
    /**
     * @allRateList:全部详情的list
     */
    private List<Map<String, Object>> allRateList = null;
    /** 全部汇率适配器*/
    private ForeignPriceExternalAdapter adapter = null;
    /** 区分用户选中的是那个按钮 1:allRateButton;2:CustomerRateButton进行刷新数据 3:未登录是全部汇率数据*/
    private int coinId = 2;
    /** 时间标签*/
    private TextView rateTimes;
    /** 隐藏外汇 自选按钮*/
    private LinearLayout isShowForeignFreeBut;
    /** 交易查询 账户管理 委托交易查询 登陆前不显示*/
    private LinearLayout iscomprehensiveButton;
    /** 判断用户是否设置外汇交易账户*/
    public Map<String, String> investBindingInfo = null;
    /** taskPopCloseButton:任务提示框右上角关闭按钮*/
    public ImageView taskPopCloseButton = null;
    /** 全部外汇汇率*/
    private Button allRateButton = null;
    /** 我的外汇汇率*/
    private Button CustomerRateButton = null;
    /** 涨跌幅 rank="":不排序 rank="UP":升序  rank="DN":降序*/
    private String rank;
    /** 四方接口回调数据*/
    List<QueryMultipleQuotationResult.QueryMultipleQuotationItem> QueryMultipleList;
    /**
     * @refreshList: 用于刷新数据于四方涨跌幅数据匹配的list
     */
    private List<Map<String, Object>> refreshList = null;
    /** 下拉刷新控件*/
    private DownRefreshLayout pull_down_layout;
    /** 用户选择的货币对数据标记*/
    private int selectPosition;
    /** 当前选中账户的位置*/
    private int mCurrentPosition = -1;
    /** 涨跌幅排序后数据*/
    private List<Map<String,Object>> allList;
    /** 汇率详情全部数据 7秒刷新数据*/
    private List<Map<String, Object>> allRateListResults;
    /** 汇率详情自选数据 7秒刷新数据*/
    private List<Map<String, Object>> customerRateListResults;
    /** 数据变化的list*/
    private List<Map<String, Object>> refreshListMap = null;
    /** 保存数据有变化位置*/
    private List<Integer> listItem;
    private TextView refreshTimeTv;
    private NewScrollView fund_newScrollView;//上滑控件
    // 公共组件广告区 ===========================================
    /** 公共组件广告区*/
    private InvestPriceView investPriceView;
    /** 登陆前布局*/
    private FrameLayout loginOutLayout;
    /** 登陆后布局*/
    private FrameLayout mypositionedLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menuOrTrade = 1;
        taskTag = 4;
        getBackGroundLayout().setTitleText(R.string.foreign_title); // 设置标题
        setContentView(R.layout.foreign_price_external_main);
        initView(); //设置布局
        initOnClick(); // 所有点击事件处理
        initTitleClick(); // 标题按钮点击事件
        /**
         * 功能外置修改,以登陆先判断是否开通理财，未登录获取未登录全部行情 用户点击标题栏登陆按钮:
         * ->登陆成功回调onActivityResult通过ACTIVITY_REQUEST_LOGIN_CODE判断是标题栏入口，
         * 然后判断是否开通理财进而进行页面数据刷新 ->登陆失败不做任何事情 用户点击买入或卖出后登陆 ->登陆成功先判断是否已经开通理财和设置账户
         * ->没有开通理财和设置账户，taskTag = 8调用判断理财是否开通 ->已经开通理财和设置账号直接进行交易 ->登陆失败不做任何事情
         * 启动轮询查询机制在 onActivityResult 通过FOREX_RATE_TRADE_TAG 判断是从上一个交易页面返回，
         * 如果全部汇率按钮和我的汇率按钮都没有被按钮则默认按下我的汇率
         */
        if (BaseDroidApp.getInstanse().isLogin()) {
            commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
            BaseHttpEngine.showProgressDialogCanGoBack();
            if (StringUtil.isNull(commConversationId)) {
                taskMark = 6;
                requestCommConversationId();  // 请求登录后的CommConversationId
            }
//            else {
//                requestPsnInvestmentManageIsOpen(); // 判断用户是否开通投资理财服务
//            }
        } else {
            isCloseDialog = true;  // 用来判断是否关闭通讯框
            coinId = 3;
            riseDropLl.setEnabled(true);
            requestAllRatesOutlay(); //请求未登录全部外汇
        }
    }

    /** 设置布局*/
    public void initView() {
//        setContentView(R.layout.foreign_price_external_main);
//        getBackGroundLayout().setTitleText(R.string.foreign_title); // 设置标题
        getBackGroundLayout().setShareButtonVisibility(View.VISIBLE); //设置分享图标显示
        getBackGroundLayout().setRightButtonVisibility(View.VISIBLE); //设置右按钮显示
        getBackGroundLayout().setTitleBackgroundAlpha(0.5f);
        myPositioned = (RelativeLayout) findViewById(R.id.my_positioned);
        advertisementLoginLay = (RelativeLayout) findViewById(R.id.advertisement_login);
        landing = (Button) findViewById(R.id.foreign_landing_clicks);
        trade_query = (Button) findViewById(R.id.foreign_trade_query_clicks);
        account_management = (Button) findViewById(R.id.foreign_account_manage_clicks);
        commissionDealQueryTv = (Button) findViewById(R.id.foreign_commission_deal_query);
        free = (LinearLayout) findViewById(R.id.foreign_free_clicks);
        foreign_query = (LinearLayout) findViewById(R.id.foreign_query_clicks);
        rateListView = (PullableListView) findViewById(R.id.all_exchange_rate_listView);
        rateTimes = (TextView) findViewById(R.id.forex_rate_times);
        foreignEditor = (TextView) findViewById(R.id.foreign_editor_clicks);
        riseDrop = (TextView) findViewById(R.id.foreign_rise_drop_clicks);
        riseDropLl = (LinearLayout) findViewById(R.id.ll_foreign_rise_drop_clicks);
        freeTvColor = (TextView) findViewById(R.id.foreign_free_tv_color);
        foreignTvColor = (TextView) findViewById(R.id.foreign_query_tv_color);
        ima_foreign_free = (ImageView) findViewById(R.id.ima_foreign_free);
        ima_foreign_query = (ImageView) findViewById(R.id.ima_foreign_query);
        isShowForeignFreeBut = (LinearLayout) findViewById(R.id.foreign_free_but);
        iscomprehensiveButton = (LinearLayout) findViewById(R.id.ll_foreign_comprehensive_button);
        customerRateList = new ArrayList<Map<String, Object>>(); //自选数据
        allRateList = new ArrayList<Map<String, Object>>(); //全部数据
        pull_down_layout = (DownRefreshLayout) findViewById(R.id.pull_down_layout);  //下拉刷新
        rateListView.setIsPullUp(false);
        riseDrop.setText("涨跌幅");
        // 公共组件广告布局 ===============================================================
        investPriceView = (InvestPriceView) findViewById(R.id.investPriceView);
        loginOutLayout = (FrameLayout) investPriceView.findViewById(R.id.loginOutLayout);
        mypositionedLayout = (FrameLayout) investPriceView.findViewById(R.id.fl_mypositioned);
        investPriceView.setLoginOutAdvertiseImage(R.drawable.forex_adv_img);
        // 市值标题与我的持仓颜色
//        investPriceView.setMyPositionBackground(R.color.boc_title_color);
//        investPriceView.setBackGoundWithColor(R.color.boc_title_color);

        /**登录后显示我的持仓链接**/
        investPriceView.setLoginLayoutType(InvestPriceView.LoginLayoutType.MyPositionLayout);
        investPriceView.setmMypositionClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                stopPollingFlag();
                if (BaseDroidApp.getInstanse().isLogin()) { // 已登录显示
                    if (isOpen && isSettingAcc) {
                        Intent intent4 = new Intent();
                        intent4.setClass(ForeignExchangeRateActivity.this, ForexCustomerRateInfoActivity.class);
                        startActivity(intent4);
                    } else {
                        taskTag = 2;
                        menuOrTrade = 2;
                        BaseHttpEngine.showProgressDialogCanGoBack();
                        requestPsnInvestmentManageIsOpen();
                    }
                } else {
                    BaseActivity.getLoginUtils(ForeignExchangeRateActivity.this).exe(new LoginTask.LoginCallback() { // 登陆跳转
                        @Override
                        public void loginStatua(boolean b) {
                            commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble
                                    .CONVERSATION_ID);
                            BaseHttpEngine.showProgressDialogCanGoBack();
                            if (StringUtil.isNull(commConversationId)) {
                                requestCommConversationId();  // 请求登录后的CommConversationId
                            } else {
                                requestPsnInvestmentManageIsOpen(); // 判断用户是否开通投资理财服务
                            }
                        }
                    });
                }
            }
        });

        investPriceView.setLoginSuccessCallBack(new LoginTask.LoginCallback() {
            @Override
            public void loginStatua(boolean b) {
                if (b) {
                    loginOutLayout.setVisibility(View.GONE);//登陆隐藏
                    mypositionedLayout.setVisibility(View.VISIBLE);  //显示登陆后数据布局
                    fund_newScrollView.resetAssignMargin(0);
                    getBackGroundLayout().setTitleStyle(NewBackGroundLayout.TitleStyle.White);
                    getBackGroundLayout().setTitleBackgroundAlpha(0.0f);
                    loginClicks();
                }
            }
        });

        // 设置时间布局 ====================================================================
        View timeLayout =LayoutInflater.from(this).inflate(R.layout.foreign_time_layout,null);
        refreshTimeTv = (TextView) timeLayout.findViewById(R.id.forex_rate_times_new);
        rateListView.addFooterView(timeLayout);
    }

    /** 所有点击事件处理*/
    public void initOnClick() {
        landing.setOnClickListener(this); // 登录
        myPositioned.setOnClickListener(this); //我的持仓
        trade_query.setOnClickListener(this); //交易查询
        account_management.setOnClickListener(this); //账户管理
        commissionDealQueryTv.setOnClickListener(this); //撤单
        free.setOnClickListener(this); //自选
        foreign_query.setOnClickListener(this); //外汇
        foreignEditor.setOnClickListener(this); //编辑货币对
        riseDropLl.setOnClickListener(this); //涨跌幅
    }

    /** 根据登录状态显示页面信息*/
    @Override
    protected void onResumeFromLogin(final boolean isLogin) {
        super.onResumeFromLogin(isLogin);
        if (isLogin) { //已登录
//            myPositioned.setVisibility(View.VISIBLE);
//            advertisementLoginLay.setVisibility(View.GONE);
            loginOutLayout.setVisibility(View.GONE);  //登陆隐藏
            mypositionedLayout.setVisibility(View.VISIBLE);  //显示登陆后数据布局
            isShowForeignFreeBut.setVisibility(View.VISIBLE);
            foreignEditor.setVisibility(View.VISIBLE);
            iscomprehensiveButton.setVisibility(View.VISIBLE);
            getBackGroundLayout().setLineDividerVisibility(View.GONE);
            if (coinId!=1){
                free.performClick(); //默认点击自选
            }
        } else { // 未登录
//            myPositioned.setVisibility(View.GONE);
//            advertisementLoginLay.setVisibility(View.VISIBLE);
            loginOutLayout.setVisibility(View.VISIBLE);  //登陆显示
            mypositionedLayout.setVisibility(View.GONE);  //隐藏登陆后数据布局
            isShowForeignFreeBut.setVisibility(View.GONE);
            foreignEditor.setVisibility(View.GONE);
            iscomprehensiveButton.setVisibility(View.GONE);
            getBackGroundLayout().setLineDividerVisibility(View.GONE);
        }
        // 下拉刷新
        pull_down_layout.setOnRefreshListener(new IRefreshLayoutListener() {
            @Override
            public void onLoadMore(View pullToRefreshLayout) {
                stopPollingFlag(); // 暂停7秒刷新
                rateListView.removeFooterView(refreshTimeTv);
                if (BaseDroidApp.getInstanse().isLogin()) {//已登录
                    if (coinId == 1) { // 刷新外汇全部行情
                        isCloseDialog = false; // 用来判断是否关闭通讯框
                        requestNoDialogPsnAllRates();
                    } else if (coinId == 2) { // 刷新自选行情
                        isCloseDialog = false;  // 用来判断是否关闭通讯框
                        requestNoDialogPsnCustomerRate();
                    }
                } else {//未登陆
                    coinId = 3;
                    isCloseDialog = false;  // 用来判断是否关闭通讯框
                    requestNoDialogAllRatesOutlay();
                }
            }
        });
    }

    /** 标题按钮点击事件*/
    public void initTitleClick() {
        // 分享 取消 by luqp 2016-11-1
        getBackGroundLayout().setShareButtonVisibility(View.GONE); //设置分享图标隐藏
        getBackGroundLayout().setRightButtonVisibility(View.VISIBLE); //设置右按钮显示
        getBackGroundLayout().setTitleBackground(R.color.boc_title_color); //设置标题背景颜色
        getBackGroundLayout().setOnLeftButtonImage(getResources().getDrawable(R.drawable.share_left_arrow));
        getBackGroundLayout().setRightButtonImage(getResources().getDrawable(R.drawable.share_more_small));
        getBackGroundLayout().setTitleTextColor(R.color.boc_common_cell_color); // 设置标题颜色

        getBackGroundLayout().setContentLayout(true);
        getBackGroundLayout().setTitleBackgroundAlpha(0.0f);
        //向上滑动
        fund_newScrollView = (NewScrollView) findViewById(R.id.fund_newScrollView);
        getBackGroundLayout().postDelayed(new Runnable() {
            @Override
            public void run() {
                fund_newScrollView.setPreScrollHeight(getBackGroundLayout().getTitleHeight());
            }
        },300);
        fund_newScrollView.setINewScrollViewListener(new NewScrollView.INewScrollViewListener() {
            @Override
            public boolean isToTop() {
                if ((rateListView.getChildAt(0) == null) ||
                        rateListView.getFirstVisiblePosition() == 0 && rateListView.getChildAt(0).getTop() >= 0) {
                    return true;
                }
                return false;
            }
        });
        /**设置滑动标题背景透明度变化**/
        fund_newScrollView.setIScrollChangedListener(new NewScrollView.IScrollChangedListener() {
            @Override
            public boolean onScrollChanged(int x, int y) {
                float alpha =  0.0f + (float)(1.0*y/fund_newScrollView.getMaxScrollHeight());
                if(y != 0 && alpha > 0.5f){
                    getBackGroundLayout().setTitleStyle(NewBackGroundLayout.TitleStyle.Black);
                }else{
                    getBackGroundLayout().setTitleStyle(NewBackGroundLayout.TitleStyle.White);
                }
                getBackGroundLayout().setTitleBackground(R.color.lianlong_common_cell_color);
                getBackGroundLayout().setTitleBackgroundAlpha(alpha);
                return true;
            }
        });

        /** 返回按钮点击事件 停止自动刷新*/
        getBackGroundLayout().setOnLeftButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPollingFlag(); //点击时先停止7秒刷新
                ActivityTaskManager.getInstance().removeAllActivity();  // 清除
                ForeignDataCenter.getInstance().clearForeignData();  // 清除 根据自己模块清除自己的 数据中心类
                finish();
            }
        });
        /** 右边按钮点击事件*/
        getBackGroundLayout().setOnShareButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "分享正在开发中......", Toast.LENGTH_LONG).show();
            }
        });

        /** 右边按钮点击事件*/
        getBackGroundLayout().setOnRightButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPollingFlag();
//                MoreMenuItem item = new MoreMenuItem();
//                MoreMenuActivity.gotoMoreMenuActivity();
                Intent intent = new Intent(); //跳转到更多
                intent.setClass(ForeignExchangeRateActivity.this, ForeignMoreActivity.class);
//                startActivityForResult(intent,ConstantGloble.FOREX_CUSTOMER_FIX_TAG );
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.foreign_landing_clicks: //登录
                LogGloble.i(TAG, "点击点击登录......");
                loginClicks();
                break;
            case R.id.my_positioned: //我的持仓
                stopPollingFlag();
                if (BaseDroidApp.getInstanse().isLogin()) { // 已登录显示
                    if (isOpen && isSettingAcc) {
                        Intent intent4 = new Intent();
                        intent4.setClass(this, ForexCustomerRateInfoActivity.class);
                        startActivity(intent4);
                    } else {
                        taskTag = 2;
                        menuOrTrade = 2;
                        BaseHttpEngine.showProgressDialogCanGoBack();
                        requestPsnInvestmentManageIsOpen();
                    }
                } else {
                    BaseActivity.getLoginUtils(ForeignExchangeRateActivity.this).exe(new LoginTask.LoginCallback() { // 登陆跳转
                        @Override
                        public void loginStatua(boolean b) {
                            commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble
                                    .CONVERSATION_ID);
                            BaseHttpEngine.showProgressDialogCanGoBack();
                            if (StringUtil.isNull(commConversationId)) {
                                requestCommConversationId();  // 请求登录后的CommConversationId
                            } else {
                                requestPsnInvestmentManageIsOpen(); // 判断用户是否开通投资理财服务
                            }
                        }
                    });
                }
                break;
            case R.id.foreign_trade_query_clicks: //交易查询
                stopPollingFlag();
                if (BaseDroidApp.getInstanse().isLogin()) { // 已登录显示
                    if (isOpen && isSettingAcc) {
                        Intent intent4 = new Intent();
                        intent4.setClass(this, ForexStrikeQueryActivity.class);
                        startActivity(intent4);
                    } else {
                        taskTag = 3;
                        menuOrTrade = 2;
                        BaseHttpEngine.showProgressDialogCanGoBack();
                        requestPsnInvestmentManageIsOpen();
                    }
                } else {
                    BaseActivity.getLoginUtils(ForeignExchangeRateActivity.this).exe(new LoginTask.LoginCallback() { // 登陆跳转
                        @Override
                        public void loginStatua(boolean b) {
                            taskMark = 1;
                            commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble
                                    .CONVERSATION_ID);
                            BaseHttpEngine.showProgressDialogCanGoBack();
                            if (StringUtil.isNull(commConversationId)) {
                                requestCommConversationId();  // 请求登录后的CommConversationId
                            } else {
                                requestPsnInvestmentManageIsOpen(); // 判断用户是否开通投资理财服务
                            }
                        }
                    });
                }
                break;
            case R.id.foreign_account_manage_clicks: //账户管理
                stopPollingFlag();
                if (BaseDroidApp.getInstanse().isLogin()) { // 已登录显示
                    if (isOpen && isSettingAcc) {
//                        Intent intent4 = new Intent();
//                        intent4.setClass(this, ForexAccSettingActivity.class);
//                        startActivity(intent4);
                        BaseHttpEngine.showProgressDialogCanGoBack();
                        isCustomer = true;
                        customerPsnForexActAvai();
                    } else {
                        taskTag = 2;
                        menuOrTrade = 2;
                        BaseHttpEngine.showProgressDialogCanGoBack();
                        requestPsnInvestmentManageIsOpen();
                    }
                } else {
                    BaseActivity.getLoginUtils(ForeignExchangeRateActivity.this).exe(new LoginTask.LoginCallback() { // 登陆跳转
                        @Override
                        public void loginStatua(boolean b) {
                            taskMark = 5;
                            commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble
                                    .CONVERSATION_ID);
                            BaseHttpEngine.showProgressDialogCanGoBack();
                            if (StringUtil.isNull(commConversationId)) {
                                requestCommConversationId();  // 请求登录后的CommConversationId
                            } else {
                                requestPsnInvestmentManageIsOpen(); // 判断用户是否开通投资理财服务
                            }
                        }
                    });
                }
                break;
            case R.id.foreign_commission_deal_query: //委托交易查询
                stopPollingFlag();
                if (BaseDroidApp.getInstanse().isLogin()) { // 已登录显示
                    ActivityTaskManager.getInstance().removeAllSecondActivity();
                    if (isOpen && isSettingAcc) {
                        Intent intent = new Intent();
                        intent.setClass(this, ForexQuashQueryActivity.class);
                        startActivity(intent);
                    } else {
                        taskTag = 7;
                        menuOrTrade = 2;
                        BaseHttpEngine.showProgressDialogCanGoBack();
                        requestPsnInvestmentManageIsOpen();
                    }
                } else {
                    BaseActivity.getLoginUtils(ForeignExchangeRateActivity.this).exe(new LoginTask.LoginCallback() { // 登陆跳转
                        @Override
                        public void loginStatua(boolean b) {
                            taskMark = 3;
                            commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble
                                    .CONVERSATION_ID);
                            BaseHttpEngine.showProgressDialogCanGoBack();
                            if (StringUtil.isNull(commConversationId)) {
                                requestCommConversationId();  // 请求登录后的CommConversationId
                            } else {
                                requestPsnInvestmentManageIsOpen(); // 判断用户是否开通投资理财服务
                            }
                        }
                    });
                }
                break;
            case R.id.foreign_free_clicks: //自选
                stopPollingFlag();
                riseDrop.setText("涨跌幅");
                coinId = 2;
                taskTag = 5;
                customerOrTrade = 2;
                allOrCustomerReq = 1;
                menuOrTrade = 1;
                riseDropLl.setEnabled(false);
                clearDate();
                pull_down_layout.reset();  // 重置刷新状态
                freeClickStyle(coinId);  // 点击按钮样式统一修改
                if (isOpen && isSettingAcc) {
                    isCloseDialog = true;  // 用来判断是否关闭通讯框
                    BaseHttpEngine.showProgressDialogCanGoBack();
                    requestPsnCustomerRate();
                } else {
                    BaseHttpEngine.showProgressDialogCanGoBack();
                    requestPsnInvestmentManageIsOpen();
                }
                break;
            case R.id.foreign_query_clicks: //外汇
                stopPollingFlag(); //点击时先停止7秒刷新
                riseDrop.setText("涨跌幅");
                coinId = 1;
                taskTag = 6;
                allOrCustomerReq = 0;
                customerOrTrade = 3;
                menuOrTrade = 1;
                riseDropLl.setEnabled(true);
                pull_down_layout.reset(); // 重置刷新状态
                clearDate();
                freeClickStyle(coinId);  // 点击按钮样式统一修改
                if (isOpen && isSettingAcc) {
                    isCloseDialog = true;  // 用来判断是否关闭通讯框
                    BaseHttpEngine.showProgressDialogCanGoBack();
                    requestPsnAllRates();
                } else {
                    BaseHttpEngine.showProgressDialogCanGoBack();
                    requestPsnInvestmentManageIsOpen();
                }
                break;
            case R.id.foreign_editor_clicks: //编辑|货币对
                exchangeRateCustomizationClick();
                break;
            case R.id.ll_foreign_rise_drop_clicks: //涨跌幅
                stopPollingFlag(); //点击时先停止7秒刷新
                Drawable sortUp = getResources().getDrawable(R.drawable.share_rise);
                Drawable ortDown = getResources().getDrawable(R.drawable.share_fall);
                Drawable sortUn = getResources().getDrawable(R.drawable.share_flat);
                sortUp.setBounds(0, 0, sortUp.getMinimumWidth(), sortUp.getMinimumHeight()); //设置边界
                ortDown.setBounds(0, 0, ortDown.getMinimumWidth(), ortDown.getMinimumHeight()); //设置边界
                sortUn.setBounds(0, 0, sortUn.getMinimumWidth(), sortUn.getMinimumHeight()); //设置边界

                if (quoteChangeId == 0) { // 点击
                    quoteChangeId = 1;
                } else if (quoteChangeId == 1) {
                    quoteChangeId = 2;
                } else if (quoteChangeId == 2) {
                    quoteChangeId = 0;
                }
                switch (quoteChangeId) {//自选没有排序
                    case 0: //不排序
                        riseDrop.setCompoundDrawables(null, null, sortUn, null); // 设置不排序图片
                        rank = "";
                        isSorting = false;
                        getqueryMultipleQuotation(rank);
                        break;
                    case 1://升序
                        riseDrop.setCompoundDrawables(null, null, sortUp, null); // 设置升序图片
                        rank = "UP";
                        isSorting = true;
                        getqueryMultipleQuotation(rank);
                        break;
                    case 2://降序
                        riseDrop.setCompoundDrawables(null, null, ortDown, null); //设置降序图片
                        rank = "DN";
                        isSorting = true;
                        getqueryMultipleQuotation(rank);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    /**
     * 我的外汇汇率按钮响应事件---回调13 未定制外汇汇率提示页面
     * @resultObj:返回结果
     */
    @SuppressWarnings("unchecked")
    public void requestPsnCustomerRateCallback(Object resultObj) {
        super.requestPsnCustomerRateCallback(resultObj);
        if (isCloseDialog){
            BaseHttpEngine.dissMissProgressDialog();
        }
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        Map<String, List<Map<String, Object>>> result = (Map<String, List<Map<String, Object>>>) biiResponseBody
                .getResult();
        refreshCustomerData();  // 7秒刷新
        if (StringUtil.isNullOrEmpty(result)) {
            BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
            return;
        } else {
            if (!result.containsKey(Forex.FOREX_RATE_CUSTOMERRATELIST_RES) ||
                    StringUtil.isNullOrEmpty(result.get(Forex.FOREX_RATE_CUSTOMERRATELIST_RES))) {
                BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
                return;
            } else {
                customerRateList = result.get(Forex.FOREX_RATE_CUSTOMERRATELIST_RES);
                if (customerRateList == null || customerRateList.size() <= 0) {
                    isHasCustomerList = false;
                    getCustomerAdapterListener(new ArrayList<Map<String, Object>>()); // 清空
                    return;
                }
                isHasCustomerList = true;
                customerRateList = getCurrencyCodeList(customerRateList); //获取支持货币对
//                ListUtils.sortForInvest(customerRateList, new ListUtils.ISynchronousList<Map<String, Object>,
//                        KeyAndValueItem>() {
//                    @Override
//                    public boolean doSomeThing(Map<String, Object> stringObjectMap, KeyAndValueItem keyAndValueItem) {
//                        String sourceCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_SOURCECODE_RES);
//                        String targetCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_TARGETCODE_RES);
//                        if (keyAndValueItem.getKey().equals(sourceCurrencyCode) && keyAndValueItem.getValue().equals
//                                (targetCurrencyCode)) {
//                            stringObjectMap.put("formatNumber", keyAndValueItem.getParam());
//                            return true;
//                        }
//                        return false;
//                    }
//                });
            }
            getCustomerDate();
            getqueryMultipleQuotation(rank);
            pull_down_layout.loadmoreCompleted(RefreshDataStatus.Successed);
        }
    }

    /** 查询全部外汇行情---回调 rateTimes赋值*/
    @SuppressWarnings("unchecked")
    public void requestPsnAllRateCallback(Object resultObj) {
        super.requestPsnAllRateCallback(resultObj);
        if (isCloseDialog){
            BaseHttpEngine.dissMissProgressDialog();
        }
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        Map<String, List<Map<String, Object>>> result = (Map<String, List<Map<String, Object>>>) biiResponseBody
                .getResult();
        refreshAllLogInData();  // 7秒刷新
        if (StringUtil.isNullOrEmpty(result)) {
            BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
            return;
        } else {
            if (!result.containsKey(Forex.FOREX_RATE_ALLRATELIST_RES)) {
                BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
                return;
            }
            if (StringUtil.isNullOrEmpty(result.get(Forex.FOREX_RATE_ALLRATELIST_RES))) {
                BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
                return;
            }
            allRateList = result.get(Forex.FOREX_RATE_ALLRATELIST_RES);
            if (allRateList == null || allRateList.size() <= 0) {
                return;
            }
            allRateList = getCurrencyCodeList(allRateList); //获取支持货币对
            ListUtils.sortForInvest(allRateList, new ListUtils.ISynchronousList<Map<String, Object>, KeyAndValueItem>() {
                @Override
                public boolean doSomeThing(Map<String, Object> stringObjectMap, KeyAndValueItem keyAndValueItem) {
                    String sourceCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_SOURCECODE_RES);
                    String targetCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_TARGETCODE_RES);
                    if (keyAndValueItem.getKey().equals(sourceCurrencyCode) && keyAndValueItem.getValue().equals
                            (targetCurrencyCode)) {
                        stringObjectMap.put("formatNumber", keyAndValueItem.getParam());
                        return true;
                    }
                    return false;
                }
            });
            selectedAllRates();
            getqueryMultipleQuotation(rank);
            pull_down_layout.loadmoreCompleted(RefreshDataStatus.Successed);
        }
    }

    /** 全部未登录时汇率 外置请求全部汇率回调*/
    @SuppressWarnings("unchecked")
    public void requestAllRatesOutlayCallback(Object resultObj) {
        if (isCloseDialog){
            BaseHttpEngine.dissMissProgressDialog();
        }
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        List<Map<String, Object>> result = (List<Map<String, Object>>) biiResponseBody.getResult();
        coinId = 3;
        freeClickStyle(coinId);  // 点击按钮样式统一修改
        refreshAllData(); //7秒刷新
        if (StringUtil.isNullOrEmpty(result) || StringUtil.isNullOrEmpty(result)) {
            isHasAllList = false;
            refreshAllAdapterListenerOutlay(new ArrayList<Map<String, Object>>()); // 清空
        } else {
            allRateList = getTrueDate(result);  // 将得到的数据进行处理
            isHasAllList = true;
            refreshOutlayTimes(allRateList);
            allRateList = getCurrencyCodeList(allRateList); //获取支持货币对
            ListUtils.sortForInvest(allRateList, new ListUtils.ISynchronousList<Map<String, Object>, KeyAndValueItem>() {
                @Override
                public boolean doSomeThing(Map<String, Object> stringObjectMap, KeyAndValueItem keyAndValueItem) {
                    String sourceCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_SOURCECODE_RES);
                    String targetCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_TARGETCODE_RES);
                    if (keyAndValueItem.getKey().equals(sourceCurrencyCode) && keyAndValueItem.getValue().equals
                            (targetCurrencyCode)) {
                        stringObjectMap.put("formatNumber", keyAndValueItem.getParam());
                        return true;
                    }
                    return false;
                }
            });
            refreshAllAdapterListenerOutlay(allRateList);
            getqueryMultipleQuotation(rank);
        }
        pull_down_layout.loadmoreCompleted(RefreshDataStatus.Successed);
    }

    /** 用户定制的货币对-7秒刷新*/
    private void refreshCustomerData() {
        if (!PollingRequestThread.pollingFlag) {
            requestCustomerRatesPoling(); // 7秒刷新 --定制货币对
        }
    }

    /** 全部汇率-7秒刷新*/
    private void refreshAllData() {
        if (!PollingRequestThread.pollingFlag) {
            requestAllPsnGetAllExchangeRatesOutlay(); // 7秒刷新 --未登录汇率
        }
    }

    /** 全部汇率-7秒刷新*/
    private void refreshAllLogInData() {
        if (!PollingRequestThread.pollingFlag) {
            requestAllRatesPoling();  // 7秒刷新 --已登录全部外汇汇率
        }
    }

    /** 7秒刷新 我的外汇汇率*/
    private void requestCustomerRatesPoling() {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Forex.FOREX_CUSTOMER_RATE);
        biiRequestBody.setConversationId(commConversationId);
        biiRequestBody.setParams(null);
        HttpManager.requestPollingBii(biiRequestBody, allHttpHandler, ConstantGloble.FOREX_REFRESH_TIMES);// 7秒刷新
    }

    /** 7秒刷新-全部汇率*/
    private void requestAllRatesPoling() {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Forex.FOREX_ALLRATE);
        biiRequestBody.setConversationId(commConversationId);
        biiRequestBody.setParams(null);
        HttpManager.requestPollingBii(biiRequestBody, allHttpHandler, ConstantGloble.FOREX_REFRESH_TIMES);// 7秒刷新
    }

    /** 全部未登录时汇率 --018*/
    private void requestAllPsnGetAllExchangeRatesOutlay() {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Forex.FOREX_PsnGetAllExchangeRatesOutlay);
        Map<String, String> params = new HashMap<String, String>();
        // 客户登记的交易账户的省行联行号
        params.put(Forex.FOREX_ibknum, LocalDataService.getInstance().getIbkNum(ConstantGloble.Forex));
        params.put(Forex.FOREX_paritiesType, "F");  // 牌价类型(外汇f： 黄金：G)
        params.put(Forex.FOREX_offerType, "R");
        biiRequestBody.setParams(params);
        HttpManager.requestPollingOutlay(biiRequestBody, allHttpHandler, ConstantGloble.FOREX_REFRESH_TIMES);
    }

    /** 轮训请求 汇率 1:全部已登录汇率 2:全部定制汇率 3:未登录汇率*/
    private Handler allHttpHandler = new Handler() {
        @SuppressWarnings("unchecked")
        public void handleMessage(android.os.Message msg) {
            BiiResponse biiResponse = (BiiResponse) ((Map<String, Object>) msg.obj).get(ConstantGloble.HTTP_RESULT_DATA);
            List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
            BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
            switch (coinId) {
                case 1:// 全部外汇
                    Map<String, List<Map<String, Object>>> result = (Map<String, List<Map<String, Object>>>)
                            biiResponseBody.getResult();
                    if (StringUtil.isNullOrEmpty(result) || !result.containsKey(Forex.FOREX_RATE_ALLRATELIST_RES)
                            || StringUtil.isNullOrEmpty(result.get(Forex.FOREX_RATE_ALLRATELIST_RES))) {
                        isHasAllList = false;
                        return;
                    } else {
                        allRateListResults = result.get(Forex.FOREX_RATE_ALLRATELIST_RES);
                        if (StringUtil.isNullOrEmpty(allRateListResults) || allRateListResults == null || allRateListResults.size() <= 0) {
                            isHasAllList = false;
                            return;
                        } else {
                            allRateListResults = getTrueDate(allRateListResults); // 将得到的数据进行处理
                            if (StringUtil.isNullOrEmpty(allRateList) || allRateList == null || allRateList.size() <= 0) {
                                isHasAllList = false;
                                return;
                            }
                            refreshTimes(allRateListResults);
                            allRateListResults = getCurrencyCodeList(allRateListResults); //获取支持货币对
                            Boolean isRefresh = isRefreshData(allRateList,allRateListResults);
                            if (isHasAllList) {
                                ListUtils.synchronousListWithSameStruct(allRateList, allRateListResults, new ListUtils
                                        .ISynchronousList<Map<String, Object>, Map<String, Object>>() {

                                    @Override
                                    public boolean doSomeThing(Map<String, Object> stringObjectMap, Map<String, Object>
                                            stringObjectMap2) {
                                        String currencyCode =  getCurrencyPair(stringObjectMap);
                                        String currencyCodeNew =  getCurrencyPair(stringObjectMap2);
                                        if (currencyCode.equals(currencyCodeNew)) {
                                            stringObjectMap.put("buyRate", stringObjectMap2.get(Forex.FOREX_RATE_BUYRATE_RES)); // 卖价
                                            stringObjectMap.put("sellRate", stringObjectMap2.get(Forex.FOREX_RATE_SELLRATE_RES)); // 买价
                                            return true;
                                        }
                                        return false;
                                    }
                                });
                                if (isRefresh){ //如果新数据有变化 开启动画
                                    adapter.refreshDetaChaged(allRateList,listItem,true); // 刷新数据并开启动画
                                }else{
                                    adapter.refreshDeta(allRateList,false); // 刷新数据,不开启动画
                                }
                                getqueryMultipleQuotation(rank);
                            } else {
                                isHasAllList = true;
                                refreshAllAdapterListenerOutlay(allRateList);
                            }
                        }
                    }
                    break;
                case 2:// 我的外汇汇率
                    Map<String, List<Map<String, Object>>> customerResult =
                            (Map<String, List<Map<String, Object>>>) biiResponseBody.getResult();
                    if (StringUtil.isNullOrEmpty(customerResult) || !customerResult.containsKey(Forex.FOREX_RATE_CUSTOMERRATELIST_RES) ||
                            StringUtil.isNullOrEmpty(customerResult.get(Forex.FOREX_RATE_CUSTOMERRATELIST_RES))) {
                        isHasCustomerList = false;
                        return;
                    } else {
                        customerRateListResults = customerResult.get(Forex.FOREX_RATE_CUSTOMERRATELIST_RES);
                        if (StringUtil.isNullOrEmpty(customerRateListResults) || customerRateListResults == null || customerRateListResults.size() <= 0) {
                            isHasCustomerList = false;
                            return;
                        } else {
                            customerRateListResults = getTrueDate(customerRateListResults);  // 将得到的数据进行处理
                            if (StringUtil.isNullOrEmpty(customerRateListResults) || customerRateListResults == null || customerRateListResults.size() <= 0) {
                                isHasCustomerList = false;
                                return;
                            }
                            refreshTimes(customerRateListResults);
                            customerRateListResults = getCurrencyCodeList(customerRateListResults); //获取支持货币对
                            Boolean isRefresh = isRefreshData(customerRateList,customerRateListResults);
                            if (isHasCustomerList) {
                                ListUtils.synchronousListWithSameStruct(customerRateList, customerRateListResults, new
                                        ListUtils.ISynchronousList<Map<String, Object>, Map<String, Object>>() {

                                    @Override
                                    public boolean doSomeThing(Map<String, Object> stringObjectMap, Map<String, Object> stringObjectMap2) {
                                        String currencyCode =  getCurrencyPair(stringObjectMap);
                                        String currencyCodeNew =  getCurrencyPair(stringObjectMap2);
                                        if (currencyCode.equals(currencyCodeNew)) {
                                            stringObjectMap.put("buyRate", stringObjectMap2.get(Forex.FOREX_RATE_BUYRATE_RES)); // 卖价
                                            stringObjectMap.put("sellRate", stringObjectMap2.get(Forex.FOREX_RATE_SELLRATE_RES)); // 买价
                                            return true;
                                        }
                                        return false;
                                    }
                                });
                                if (isRefresh){ //如果新数据有变化 开启动画
                                    adapter.refreshDetaChaged(customerRateList,listItem,true); // 刷新数据并开启动画
                                }else{
                                    adapter.refreshDeta(customerRateList,false); // 刷新数据,不开启动画
                                }
                                getqueryMultipleQuotation(rank);
                            } else {
                                isHasCustomerList = true;
                                getCustomerAdapterListener(customerRateList);
                            }
                            // 保存省联号
                            String ibkNum = (String) customerRateList.get(0).get(Forex.FOREX_ibkNum);
                            LocalDataService.getInstance().saveIbkNum(ConstantGloble.Forex, ibkNum);
                        }
                    }
                    break;
                case 3:// 全部外汇 外置数据
                    allRateListResults = (List<Map<String, Object>>) biiResponseBody.getResult();
                    if (StringUtil.isNullOrEmpty(allRateListResults) || StringUtil.isNullOrEmpty(allRateListResults)) {
                        isHasAllList = false;
                        refreshAllAdapterListenerOutlay(new ArrayList<Map<String, Object>>()); // 清空
                    } else {
                        allRateListResults = getTrueDate(allRateListResults);  // 将得到的数据进行处理
                        isHasAllList = true;
                        Boolean isRefresh = isRefreshData(allRateList,allRateListResults);
                        allRateListResults = getCurrencyCodeList(allRateListResults); //获取支持货币对
                        ListUtils.synchronousListWithSameStruct(allRateList, allRateListResults, new ListUtils.ISynchronousList<Map<String, Object>, Map<String, Object>>() {

                            @Override
                            public boolean doSomeThing(Map<String, Object> stringObjectMap, Map<String, Object> stringObjectMap2) {
                                String currencyCode =  getCurrencyPair(stringObjectMap);
                                String currencyCodeNew =  getCurrencyPair(stringObjectMap2);
                                if (currencyCode.equals(currencyCodeNew)) {
                                    stringObjectMap.put("buyRate", stringObjectMap2.get(Forex.FOREX_RATE_BUYRATE_RES));   // 卖价
                                    stringObjectMap.put("sellRate", stringObjectMap2.get(Forex.FOREX_RATE_SELLRATE_RES)); // 买价
                                    return true;
                                }
                                return false;
                            }
                        });
                        refreshOutlayTimes(allRateListResults);
                        if (isRefresh){ //如果新数据有变化 开启动画
                            adapter.refreshDetaChaged(allRateList,listItem,true); // 刷新数据并开启动画
                        }else{
                            adapter.refreshDeta(allRateList,false); // 刷新数据,不开启动画
                        }
                        getqueryMultipleQuotation(rank);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /** 我的外汇汇率数据*/
    private void getCustomerDate() {
        customerRateList = getTrueDate(customerRateList);
        if (customerRateList == null || customerRateList.size() <= 0) {
            return;
        }
        isHasCustomerList = true;
        getCustomerAdapterListener(customerRateList);
        refreshTimes(customerRateList); // 刷新时间
    }

    /** 我的外汇汇率----监听事件*/
    private void getCustomerAdapterListener(List<Map<String, Object>> customerRateList) {
        if (adapter == null) {
            adapter = new ForeignPriceExternalAdapter(ForeignExchangeRateActivity.this, customerRateList);
            rateListView.setAdapter(adapter);
            adapter.setRisebuySellClickListener(buySellClickListener);
            adapter.setRiseImgOnItemClickListener(riseImgOnItemClickListener);
        } else {
            adapter.dataChaged(customerRateList);
        }
    }

    /** 选择全部汇率按钮*/
    private void selectedAllRates() {
        allRateList = getTrueDate(allRateList);  // 处理数据
        if (allRateList == null || allRateList.size() <= 0) {
            return;
        }
        isHasAllList = true;
        refreshAllAdapterListenerOutlay(allRateList);
        refreshTimes(allRateList); // 刷新时间
    }

    /** 全部外汇汇率-----适配器监听事件*/
    private void refreshAllAdapterListenerOutlay(List<Map<String, Object>> allRateList) {
        if (adapter == null) {
            adapter = new ForeignPriceExternalAdapter(ForeignExchangeRateActivity.this, allRateList);
            rateListView.setAdapter(adapter);
            adapter.setRisebuySellClickListener(buySellClickListener);
            adapter.setRiseImgOnItemClickListener(riseImgOnItemClickListener);
        } else {
            adapter.dataChaged(allRateList);
        }
    }

    /** 货币对点击事件*/
    private AdapterView.OnItemClickListener buySellClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            // 保存用户选择货币对条目
            List<Map<String, Object>> list = adapter.getList(); // 获取适配器数据
            ForeignDataCenter.getInstance().setSelectCurrencyMap(list.get(position));
            ForeignDataCenter.getInstance().setAllRateListData(list);
//            ForeignDataCenter.getInstance().setUserCurrencyData(customerRateList); // 保存自选货币对
            selectPosition = position;
            businessButtonClicks();
        }
    };

    /** 涨跌幅点击事件*/
    private AdapterView.OnItemClickListener riseImgOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            LogGloble.i(TAG, "点击涨跌幅......");
            Boolean isOnClick = false;
            if (mCurrentPosition == position) {
                mCurrentPosition = -1;
                riseDrop.setText("涨跌幅");
                adapter.setSelectedPosition(isOnClick);
            } else {
                mCurrentPosition = position;
                riseDrop.setText("涨跌值");
                isOnClick = true;
                adapter.setSelectedPosition(isOnClick);
            }
        }
    };

    /** 请求涨跌幅 --2.1外汇、贵金属多笔行情查询*/
    public void getqueryMultipleQuotation(String pSort) {
        String cardType = "F";
        String cardClass = "R";
        QueryMultipleQuotationRequestParams queryMultiple = new QueryMultipleQuotationRequestParams(cardType, cardClass,
                pSort);
        HttpHelp help = HttpHelp.getInstance();
        help.postHttpFromSF(this, queryMultiple);
        help.setHttpErrorCallBack(new IHttpErrorCallBack() {
            @Override
            public boolean onError(String exceptionMessage, Object extendParams) {
                MessageDialog.closeDialog(); //关闭通讯框
                return true;
            }
        });
        help.setOkHttpErrorCode(new IOkHttpErrorCode() {
            @Override
            public boolean handlerErrorCode(BaseResponseData responseData, Object extendParams) {
                MessageDialog.closeDialog(); //关闭通讯框
                return true;
            }
        });
        help.setHttpResponseCallBack(new IHttpResponseCallBack() {
            @Override
            public boolean responseCallBack(String response, Object extendParams) {
                QueryMultipleQuotationResponseData data = GsonTools.fromJson(response, QueryMultipleQuotationResponseData
                        .class);
                QueryMultipleQuotationResult body = data.getBody();
                QueryMultipleList = body.getItem();
                if (coinId == 2) { //自选汇率
                    refreshList = customerRateList;
                } else { //全部汇率
                    refreshList = allRateList;
                }
                if (isSorting){ //点击涨跌幅排序后数据
                    allList = new ArrayList<Map<String, Object>>();
                    ListUtils.synchronousList(QueryMultipleList ,refreshList, new ListUtils.ISynchronousList<QueryMultipleQuotationResult.QueryMultipleQuotationItem,
                            Map<String,Object>>() {


                        @Override
                        public boolean doSomeThing(QueryMultipleQuotationResult
                                                           .QueryMultipleQuotationItem queryMultipleQuotationItem,Map<String, Object> stringObjectMap) {
                            String sourceCurrencyCodeNew = queryMultipleQuotationItem.getSourceCurrencyCode();
                            String targetCurrencyCodeNew = queryMultipleQuotationItem.getTargetCurrencyCode();
                            String currencyCodeNew = sourceCurrencyCodeNew + targetCurrencyCodeNew;
                            String currencyCode = getCurrencyPair(stringObjectMap);
                            if (currencyCode.equals(currencyCodeNew)) {
                                stringObjectMap.put("currPercentDiff", queryMultipleQuotationItem.getCurrPercentDiff()); // 今日涨跌幅 小数点后有效位数为3位
                                stringObjectMap.put("currDiff", queryMultipleQuotationItem.getCurrDiff()); // 今日涨跌值
                                allList.add(stringObjectMap);
                                return true;
                            }
                            return false;
                        }
                    });
                    if (coinId == 2) { //刷新自选汇率
                        adapter.dataChaged(allList);
                    } else { //刷新全部汇率
                        adapter.dataChaged(allList);
                    }
                    return false;
                } else {
                    ListUtils.synchronousList(refreshList, QueryMultipleList, new ListUtils.ISynchronousList<Map<String, Object>,

                            QueryMultipleQuotationResult.QueryMultipleQuotationItem>() {


                        @Override
                        public boolean doSomeThing(Map<String, Object> stringObjectMap, QueryMultipleQuotationResult
                                .QueryMultipleQuotationItem queryMultipleQuotationItem) {
                            String sourceCurrencyCodeNew = queryMultipleQuotationItem.getSourceCurrencyCode();
                            String targetCurrencyCodeNew = queryMultipleQuotationItem.getTargetCurrencyCode();
                            String currencyCodeNew = sourceCurrencyCodeNew + targetCurrencyCodeNew;
                            String currencyCode = getCurrencyPair(stringObjectMap);
                            if (currencyCode.equals(currencyCodeNew)) {
                                stringObjectMap.put("currPercentDiff", queryMultipleQuotationItem.getCurrPercentDiff());
                                // 今日涨跌幅 小数点后有效位数为3位
                                stringObjectMap.put("currDiff", queryMultipleQuotationItem.getCurrDiff()); // 今日涨跌值
                                return true;
                            }
                            return false;
                        }
                    });
                    if (coinId == 2) { //刷新自选汇率
                        adapter.dataChaged(customerRateList);
                    } else { //刷新全部汇率
                        adapter.dataChaged(allRateList);
                    }
                    return false;
                }
            }
        });
    }

    /** 买入卖出统一点击事件*/
    public void businessButtonClicks() {
        Intent intent = new Intent();
        intent.setClass(ForeignExchangeRateActivity.this, ForeignTradeDetailsActivity.class);
        intent.putExtra(ConstantGloble.POSITION, coinId);
        intent.putExtra(Foreign.SELECTPOSITION, selectPosition);
//        startActivity(intent);
        startActivityForResult(intent,ConstantGloble.FOREX_FOREXBASE_ACTIVITY);
    }

    /** 跳转汇率定制统一按钮*/
    private void exchangeRateCustomizationClick() {
        stopPollingFlag(); // 停止轮训
        Intent intent = new Intent(ForeignExchangeRateActivity.this, ForeignCustomizeCurrencyPairsActivity.class);
        startActivityForResult(intent, ConstantGloble.FOREX_RATE_MAKE_TAG);
    }

    /** 每次点击按钮前，清空数据*/
    private void clearDate() {
        if (allRateList != null && !allRateList.isEmpty()) {
            rateTimes.setText(""); //清空时间
            allRateList.clear();
            adapter.notifyDataSetChanged();
        }
        if (customerRateList != null && !customerRateList.isEmpty()) {
            rateTimes.setText(""); //清空时间
            customerRateList.clear();
            adapter.notifyDataSetChanged();
        }
    }

    // 第一次进入判断是否开通投资理财 是否设置资金账户 start================================================
    @Override
    public void requestCommConversationIdCallBack(Object resultObj) {
        super.requestCommConversationIdCallBack(resultObj);
        commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
        if (StringUtil.isNullOrEmpty(commConversationId)) {
            BaseHttpEngine.dissMissProgressDialog();
            return;
        } else {  // 判断用户是否开通投资理财服务
            requestPsnInvestmentManageIsOpen();
        }
    }

    /**
     * 判断是否开通投资理财服务---回调 快速交易
     *
     * @param resultObj
     */
    public void requestPsnInvestmentManageIsOpenCallback(Object resultObj) {
        super.requestPsnInvestmentManageIsOpenCallback(resultObj);
        requestPsnForexActIsset();  // 设定账户的请求
    }

    /**
     * 交易条件
     * @param resultObj :返回结果
     * @任务提示框----判断是否设置账户
     */
    @SuppressWarnings("unchecked")
    public void requestPsnForexActIssetCallback(Object resultObj) {
        Map<String, Object> result = getHttpTools().getResponseResult(resultObj);
        if (StringUtil.isNullOrEmpty(result)) {
            BaseHttpEngine.dissMissProgressDialog();
            isSettingAcc = false;
        } else {
            if (StringUtil.isNullOrEmpty(result.get(Forex.FOREX_RATE_INVESTBINDINGINFO_RES))) {
                isSettingAcc = false;
            } else {
                investBindingInfo = (Map<String, String>) result.get(Forex.FOREX_RATE_INVESTBINDINGINFO_RES);
                BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_RATE_INVESTBINDINGINFO_RES, investBindingInfo);

                if (StringUtil.isNullOrEmpty(investBindingInfo)) {
                    isSettingAcc = false;
                } else {
                    isSettingAcc = true;
                }
            }
        }
        if (!isOpen || !isSettingAcc) {
            BaseHttpEngine.dissMissProgressDialog();
            getPopup();
            return;
        }
        if (isOpen && isSettingAcc) {
            Intent intent = new Intent();
            switch (taskMark) {
                case 1:// 交易查询
                    intent.setClass(ForeignExchangeRateActivity.this, ForexStrikeQueryActivity.class);
                    startActivity(intent);
                    break;
                case 2:// 我的外汇汇率
                    intent.setClass(ForeignExchangeRateActivity.this, ForexCustomerRateInfoActivity.class);
                    startActivity(intent);
                    break;
                case 3:// 委托查询
                    intent.setClass(ForeignExchangeRateActivity.this, ForexQuashQueryActivity.class);
                    startActivity(intent);
                case 4:// 登陆
                    isCloseDialog = true;  // 用来判断是否关闭通讯框
                    free.performClick(); //默认点击自选
                    break;
                case 5:// 我的外汇汇率
                    BaseHttpEngine.showProgressDialog();
                    isCustomer = true;
                    customerPsnForexActAvai();
                case 6:// 第一次进入时默认选择自选
                   free.performClick(); //默认点击自选
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 置外汇账户 查询所有的账户
     */
    private void customerPsnForexActAvai() {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Forex.FOREX_PSNFOREXACTAVAI_API);
        biiRequestBody.setConversationId(commConversationId);
        biiRequestBody.setParams(null);
        HttpManager.requestBii(biiRequestBody, this, "customerPsnForexActAvaiCallback");
    }

    /** 重设外汇交易账户---- 查询所有的外汇交易账户---回调 */
    public void customerPsnForexActAvaiCallback(Object resultObj) {
        BaseHttpEngine.dissMissProgressDialog();
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        // 得到response
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        // 得到result
        if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
            BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.forex_set_no_acc));
            return;
        }
        List<Map<String, String>> resultList = (List<Map<String, String>>) biiResponseBody.getResult();
        if (resultList == null || resultList.size() <= 0) {
            BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.forex_set_no_acc));
            return;
        }
        BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_ACTAVI_RESULT_KEY, resultList);
        // 跳转到账户设置页面
        Intent intent = new Intent(BaseDroidApp.getInstanse().getCurrentAct(), ForexAccSettingActivity.class);
        startActivityForResult(intent, ConstantGloble.FOREX_CUSTOMER_RESETACC_ACTIVITY);// 401

    };

    /**
     * 第一次进入外汇行情页面 查询用户定制的外汇汇率信息-回调
     * @param resultObj
     */
    @SuppressWarnings("unchecked")
    public void requestInitPsnCustomerRateCallback(Object resultObj) {
        super.requestInitPsnCustomerRateCallback(resultObj);
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        Map<String, List<Map<String, Object>>> result = (Map<String, List<Map<String, Object>>>) biiResponseBody.getResult();
        if (StringUtil.isNullOrEmpty(result) || !result.containsKey(Forex.FOREX_RATE_CUSTOMERRATELIST_RES) || StringUtil
                .isNullOrEmpty(result.get(Forex.FOREX_RATE_CUSTOMERRATELIST_RES))) {
            requestPsnAllRates();  // 查询所有的货币对
        } else {
            customerRateList = result.get(Forex.FOREX_RATE_CUSTOMERRATELIST_RES);
            if (StringUtil.isNullOrEmpty(customerRateList) || customerRateList == null || customerRateList.size() <= 0) {
                requestPsnAllRates(); // 查询所有的货币对
            } else {
                BaseHttpEngine.dissMissProgressDialog();
                getCustomerDate();
                refreshCustomerData();
            }
        }
    }

    // 第一次进入判断是否开通投资理财 是否设置资金账户 end==================================================
    /** 刷新功能外置时间*/
    private void refreshOutlayTimes(List<Map<String, Object>> list) {
        if (StringUtil.isNullOrEmpty(list)){
            return;
        }
        Map<String, Object> map = list.get(0);
        String times = (String) map.get(Forex.FOREX_RATE_UPDATEDATE_RES);
        String foreignTimes = this.getResources().getString(R.string.foreign_times);
        String timesStr = foreignTimes.replace("XXX", times); //分割替换字符串
        refreshTimeTv.setText(timesStr);
//        rateTimes.setText(timesStr);
    }

    /** 刷新时间*/
    private void refreshTimes(List<Map<String, Object>> list) {
        if (StringUtil.isNullOrEmpty(list)){
            return;
        }
        Map<String, Object> map = list.get(0);
        String times = (String) map.get(Forex.FOREX_RATE_UPDATEDATE_RES);
        String foreignTimes = this.getResources().getString(R.string.foreign_times);
        String timesStr = foreignTimes.replace("XXX", times); //分割替换字符串
        refreshTimeTv.setText(timesStr);
//        rateTimes.setText(timesStr);
    }

    /** 点击登陆*/
    public void loginClicks() {
        stopPollingFlag(); //点击时先停止7秒刷新
        BaseActivity.getLoginUtils(ForeignExchangeRateActivity.this).exe(new LoginTask.LoginCallback() { // 登陆跳转
            @Override
            public void loginStatua(boolean log) {
                taskMark = 4;
                CommonApplication.getInstance().setCurrentAct(ForeignExchangeRateActivity.this);
                commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
                BaseHttpEngine.showProgressDialogCanGoBack();
                if (StringUtil.isNull(commConversationId)) {
                    requestCommConversationId();  // 请求登录后的CommConversationId
                } else {
                    requestPsnInvestmentManageIsOpen(); // 判断用户是否开通投资理财服务
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ConstantGloble.ACTIVITY_REQUEST_LOGIN_CODE) {
                // 功能外置添加，点击标题栏登陆按钮登陆成功
                taskTag = 4;
                requestCommConversationId();
            }
        }
        if (requestCode == ConstantGloble.FOREX_RATE_TRADE_TAG) {  // 交易返回页面
            if (!allRateButton.isSelected() && !CustomerRateButton.isSelected()) {
                CustomerRateButton.performClick();
            }
        }

        if (requestCode == ConstantGloble.FOREX_FOREXBASE_ACTIVITY) {
            if (coinId == 1){ //外汇
                foreign_query.performClick();
            } else if (coinId == 2){ //自选
                free.performClick(); //默认点击自选
            }
        }
    }

    /** 自选&全部按钮样式修改*/
    private void freeClickStyle(int id) {
        if (id == 1 || id == 3) { //全部
            foreignTvColor.setTextColor(getResources().getColor(R.color.boc_text_color_red));
            freeTvColor.setTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
            ima_foreign_free.setImageResource(R.color.boc_grey_color);
            ima_foreign_query.setImageResource(R.color.boc_text_color_red);
            Drawable sortUn = getResources().getDrawable(R.drawable.share_flat); // 设置涨跌幅默认图片
            sortUn.setBounds(0, 0, sortUn.getMinimumWidth(), sortUn.getMinimumHeight()); //设置边界
            riseDrop.setCompoundDrawables(null, null, sortUn, null); // 设置不排序图片
        } else if (id == 2) { //自选
            foreignTvColor.setTextColor(getResources().getColor(R.color.boc_text_color_dark_gray));
            freeTvColor.setTextColor(getResources().getColor(R.color.boc_text_color_red));
            ima_foreign_free.setImageResource(R.color.boc_text_color_red);
            ima_foreign_query.setImageResource(R.color.boc_grey_color);
            riseDrop.setCompoundDrawables(null, null, null, null); // 清空排序图片
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void finish() {
        super.finish();
        if (HttpManager.mPollingRequestThread != null && PollingRequestThread.pollingFlag) {
            LogGloble.e(TAG, "onPause() mPollingRequestThread  stopPolling()  ");
            HttpManager.stopPolling();
        }
    }

    // 下拉刷新网络异常时触发
    @Override
    public void commonHttpErrorCallBack(String requestMethod) {
        // 通知返回异常
        pull_down_layout.loadmoreCompleted(RefreshDataStatus.Failed);
        pull_down_layout.reset(); // 重置刷新状态
        super.commonHttpErrorCallBack(requestMethod);
    }

    /**
     * 是否刷新数据
     * @param oldDataList 旧数据
     * @param afterRefreshDataList 7秒刷新后数据
     * @return true 刷新新数据 false 刷新旧数据
     */
    public Boolean isRefreshData(List<Map<String,Object>> oldDataList ,List<Map<String,Object>> afterRefreshDataList){
        refreshListMap = new ArrayList<Map<String,Object>>();
        listItem = new ArrayList<Integer>();
        for (int i =0;i < oldDataList.size();i++){
            Map<String,Object>  map1 = oldDataList.get(i);
            String sourceCurrencyCode1 = (String) map1.get(Forex.FOREX_RATE_SOURCECODE_RES); // 得到源货币的代码
            String targetCurrencyCode1 = (String) map1.get(Forex.FOREX_RATE_TARGETCODE_RES);
            String code1 = sourceCurrencyCode1+targetCurrencyCode1;
            String buyRate1 = (String) map1.get(Forex.FOREX_RATE_BUYRATE_RES);
            String sellRate1 = (String) map1.get(Forex.FOREX_RATE_SELLRATE_RES);
            for (int j=0;j < afterRefreshDataList.size() ;j++){
                Map<String,Object>  map2 = afterRefreshDataList.get(j);
                String sourceCurrencyCode2 = (String) map2.get(Forex.FOREX_RATE_SOURCECODE_RES); // 得到源货币的代码
                String targetCurrencyCode2 = (String) map2.get(Forex.FOREX_RATE_TARGETCODE_RES);
                String code2 = sourceCurrencyCode2+targetCurrencyCode2;
                String buyRate2 = (String) map2.get(Forex.FOREX_RATE_BUYRATE_RES);
                String sellRate2 = (String) map2.get(Forex.FOREX_RATE_SELLRATE_RES);
                if (!StringUtil.isNull(code1) && !StringUtil.isNull(code2) && code1.equals(code2)){
                    if (!buyRate1.equals(buyRate2) || !sellRate1.equals(sellRate2)){
                        listItem.add(i);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPollingFlag(); //点击时先停止7秒刷新
    }

    //对报错信息进行处理
    @Override
    public boolean doBiihttpRequestCallBackPre(BiiResponse response) {
        BaseHttpEngine.dissMissProgressDialog();
        List<BiiResponseBody> biiResponseBodyList = response.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodyList.get(0);
        if (!StringUtil.isNullOrEmpty(biiResponseBodyList)) {
            for (BiiResponseBody body : biiResponseBodyList) {
                BiiHttpEngine.dissMissProgressDialog();
                BiiError biiError = biiResponseBody.getError();
                if (biiError != null && biiError.getCode() != null) { // 判断是否存在error
                    if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
                        return super.doBiihttpRequestCallBackPre(response);
                    }
                    return true;
                }
            }
        } else if(Forex.FOREX_ALLRATE.equals(biiResponseBody.getMethod())) { // 查询全部外汇行情
            BiiError biiError = biiResponseBody.getError();
            if (biiError != null && biiError.getCode() != null) {  // 判断是否存在error
                if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
                    return super.doBiihttpRequestCallBackPre(response);
                }
            }
        }else if(Forex.FOREX_CUSTOMER_RATE.equals(biiResponseBody.getMethod())) { // 查询自选外汇行情
            BiiError biiError = biiResponseBody.getError();
            if (biiError != null && biiError.getCode() != null) {  // 判断是否存在error
                if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
                    return super.doBiihttpRequestCallBackPre(response);
                }
            }
        }else if(Forex.FOREX_PsnGetAllExchangeRatesOutlay.equals(biiResponseBody.getMethod())) { // 查询外置全部外汇行情
            BiiError biiError = biiResponseBody.getError();
            if (biiError != null && biiError.getCode() != null) {  // 判断是否存在error
                if (LocalData.timeOutCode.contains(biiError.getCode())) {// 表示回话超时
                    return super.doBiihttpRequestCallBackPre(response);
                }
            }
        } else {
             return super.doBiihttpRequestCallBackPre(response);
        }
        return super.doBiihttpRequestCallBackPre(response);
    }


}
