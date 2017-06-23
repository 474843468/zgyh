package com.chinamworld.bocmbci.biz.prms.price;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.base.application.CommonApplication;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.biz.foreign.StringTools;
import com.chinamworld.bocmbci.biz.prms.PrmsNewBaseActivity;
import com.chinamworld.bocmbci.biz.prms.myaccount.PrmsAccMeneActivity;
import com.chinamworld.bocmbci.biz.prms.myaccount.PrmsAccPositionActivity;
import com.chinamworld.bocmbci.biz.prms.myaccount.PrmsAccSettingActivity;
import com.chinamworld.bocmbci.biz.prms.query.PrmsQueryActivity;
import com.chinamworld.bocmbci.biz.prms.query.PrmsQueryEntrustNowActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpObserver;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.thread.PollingRequestThread;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.model.httpmodel.sifang.querySingelQuotation.QuerySingelQuotationRequestParams;
import com.chinamworld.bocmbci.model.httpmodel.sifang.querySingelQuotation.QuerySingleQuotationResponseData;
import com.chinamworld.bocmbci.model.httpmodel.sifang.querySingelQuotation.QuerySingleQuotationResult;
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
import com.chinamworld.bocmbci.widget.adapter.CommonAdapter;
import com.chinamworld.bocmbci.widget.adapter.ICommonAdapter;
import com.chinamworld.llbt.userwidget.NewBackGround.NewBackGroundLayout;
import com.chinamworld.llbt.userwidget.NumberStyleTextView;
import com.chinamworld.llbt.userwidget.dialogview.MessageDialog;
import com.chinamworld.llbt.userwidget.refreshliseview.DownRefreshLayout;
import com.chinamworld.llbt.userwidget.refreshliseview.IRefreshLayoutListener;
import com.chinamworld.llbt.userwidget.refreshliseview.PullableListView;
import com.chinamworld.llbt.userwidget.refreshliseview.RefreshDataStatus;
import com.chinamworld.llbt.userwidget.scrollview.NewScrollView;
import com.chinamworld.llbt.utils.TimerRefreshTools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 贵金属行情页面
 *
 * @author wuhan
 */
public class PrmsNewPricesActivity extends PrmsNewBaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final String TAG = "PrmsNewPricesActivity";

    private FrameLayout loginOutLayout;//没有登录布局


    private FrameLayout loginLayout;//已登录布局
    private TextView tv_refvalue; //


    private InvestPriceView investPriceView;


    private Button btn_query;//交易查询
    private Button btn_accountmanager;//账户管理
    private Button btn_recall;//撤单

    private TextView tv_edit;//编辑
    private TextView tv_riseorfall;//涨幅跌
    private PullableListView lv_prms;
    private TextView tv_refishtime;//更新时间

    private List<Map<String, Object>> dataList =new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> commDataList; //贵金属多笔行情查询 登录前后共用的数据
    private List<Map<String, Object>> oldtotalLoginDataList;//合并后旧的数据
    private List<Map<String, Object>> oldtotalLogoutDataList;//合并后旧的数据

    /**
     * 目标币种
     */
    private String targetCurrencyStr;
    /**
     * 源币种
     */
    private String sourceCurrencyCodeStr;

    private CommonAdapter loginAdatper, preLoginAdapter;
    private List<Map<String, Object>> preLoginDataList = new ArrayList<Map<String, Object>>();

    //区分自选与贵金属
//    private boolean isGold = false;
    private boolean iscurrPercentDiffs = true;//涨跌幅
    private int index;
    private int iscurrPercentDiff =1;

    //    List<String> sort_code_List = new ArrayList<String>();
//    boolean isENTRUSTNOW = false;
    private DownRefreshLayout pull_down_layout;
    String pSort = "";
    private  static final int ACCPOSITIONINFO = 3;//持仓
    private static final int ENTRUSTNOW = 2;//撤单
    private static final int prms = 1;//是否开通理财
    private  int JUMPFLAG = 0;
    /**交易查询、账户管理、委托交易查询栏位  外置需隐藏**/
    private LinearLayout ll_btn;


//    Handler handler = new Handler(new Handler.Callback(){
//        @Override
//        public boolean handleMessage(Message msg) {
//            if(msg.what == 0 ){
//
//                refashePullDown();
//            }
//            return true;
//        }
//    });



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prms_main_layout);
        initBaseLayout();
        initUi();
        initData();
        initListener();


    }



    /**
     * 初始化基类布局
     */
    private void initBaseLayout() {
        setLeftButtonPopupGone();

        //getBackGroundLayout().setTitleBackground(R.color.share_button_normal_color);
//        getBackGroundLayout().setTitleBackground(R.color.lianlong_color_2797c1);

//        getBackGroundLayout().setTitleTextColor(R.color.bg_white);
        getBackGroundLayout().setTitleStyle(NewBackGroundLayout.TitleStyle.White);
        getBackGroundLayout().setTitleText(getResources().getString(R.string.prms_title_price_news));
        getBackGroundLayout().setLeftButtonVisibility(View.VISIBLE);

        //getBackGroundLayout().setOnLeftButtonImage(getResources().getDrawable(R.drawable.base_btn_left_back));
//        getBackGroundLayout().setOnLeftButtonImage(getResources().getDrawable(R.drawable.icon_back_white));
        getBackGroundLayout().setRightButtonVisibility(View.VISIBLE);
//        getBackGroundLayout().setRightButtonImage(getResources().getDrawable(R.drawable.base_btn_more));
        getBackGroundLayout().setShareButtonVisibility(View.GONE);
        //getBackGroundLayout().setShareButtonImage(getResources().getDrawable(R.drawable.base_btn_share));

        getBackGroundLayout().setOnLeftButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityTaskManager.getInstance().removeAllSecondActivity();
            }
        });
        //更多
        getBackGroundLayout().setOnRightButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BaseDroidApp.getInstanse().isLogin()) {
                    BaseDroidApp.getInstanse().getBizDataMap().put(Prms.PRMS_PRICE, dataList);
                } else {
                    BaseDroidApp.getInstanse().getBizDataMap().put(Prms.PRMS_PRICE, preLoginDataList);
                }
                Intent intent = new Intent(PrmsNewPricesActivity.this, PrmsMoreActivity.class);
                startActivity(intent);
            }
        });


//        getBackGroundLayout().setOnShareButtonClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

    }

    public void initUi() {
//        btn_base_back = (Button) findViewById(R.id.btn_base_back);
//        tv_base_title = (TextView) findViewById(R.id.tv_base_title); //标题
//        btn_share = (Button) findViewById(R.id.btn_share);//分享
//        btn_more = (Button) findViewById(R.id.btn_more);//更多菜单


//        lyt_logout_image = (RelativeLayout) findViewById(R.id.lyt_logout_image);//没有登录布局
//        btn_login = (Button)findViewById(R.id.btn_login);

//        lyt_login_title = (RelativeLayout) findViewById(R.id.lyt_login_title);//已登录布局
//        ima_help = (ImageView) findViewById(R.id.ima_help);//帮助
//        ima_openeye = (ImageView) findViewById(R.id.ima_openeye);//是否显示总金额
//        tv_refvalue = (TextView) findViewById(R.id.tv_refvalue); //
//        tv_totalnum = (TextView) findViewById(R.id.tv_totalnum);//总金额
        getBackGroundLayout().setContentLayout(true);
        getBackGroundLayout().setTitleBackgroundAlpha(0.0f);
        final NewScrollView sc =(NewScrollView)findViewById(R.id.sc);
        getBackGroundLayout().postDelayed(new Runnable() {
            @Override
            public void run() {
                sc.setPreScrollHeight(getBackGroundLayout().getTitleHeight());
            }
        },300);
        sc.setINewScrollViewListener(new NewScrollView.INewScrollViewListener() {
            @Override
            public boolean isToTop() {
                if((lv_prms.getChildAt(0) == null) ||
                        lv_prms.getFirstVisiblePosition() == 0 && lv_prms.getChildAt(0).getTop() >= 0) {
                    return true;
                }
                return false;
            }
        });
        /**设置滑动标题背景透明度变化**/
        sc.setIScrollChangedListener(new NewScrollView.IScrollChangedListener() {
            @Override
            public boolean onScrollChanged(int x, int y) {
                float alpha =  0.0f + (float)(1.0*y/sc.getMaxScrollHeight());
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

        pull_down_layout = (DownRefreshLayout)findViewById(R.id.pull_down_layout);
        investPriceView = (InvestPriceView) findViewById(R.id.investPriceView);
        loginOutLayout = (FrameLayout) investPriceView.findViewById(R.id.loginOutLayout);
//        btn_login = (Button) investPriceView.findViewById(R.id.login_bt);
        ll_btn = (LinearLayout) findViewById(R.id.ll_btn_link);
        loginLayout = (FrameLayout) investPriceView.findViewById(R.id.loginLayout);
        tv_refvalue = (TextView) investPriceView.findViewById(R.id.tv_refvalue);
//        img_open = (ImageView) investPriceView.findViewById(R.id.img_open);
//        ima_help = (FrameLayout) investPriceView.findViewById(R.id.ima_help);
//        amount_tv1 = (TextView) investPriceView.findViewById(R.id.amount_tv1);
//        amount_tv2 = (TextView) investPriceView.findViewById(R.id.amount_tv2);
//        tv_refvalue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                JUMPFLAG = ACCPOSITIONINFO;
//                BaseHttpEngine.showProgressDialog();
//                checkRequestPsnInvestmentManageIsOpen();
//            }
//        });
        /**登录后显示我的持仓链接**/
        investPriceView.setLoginLayoutType(InvestPriceView.LoginLayoutType.MyPositionLayout);
        investPriceView.setmMypositionClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                JUMPFLAG = ACCPOSITIONINFO;
                BaseHttpEngine.showProgressDialog();
                checkRequestPsnInvestmentManageIsOpen();
            }
        });
//        investPriceView.setLoginLayoutClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                JUMPFLAG = ACCPOSITIONINFO;
//                BaseHttpEngine.showProgressDialog();
//                checkRequestPsnInvestmentManageIsOpen();
//            }
//        });
//        investPriceView.onClick(btn_login);
//        investPriceView.onClick(img_open);
//        investPriceView.onClick(ima_help);
        investPriceView.setLoginSuccessCallBack(new LoginTask.LoginCallback() {
            @Override
            public void loginStatua(boolean b) {
                if (b) {
                    CommonApplication.getInstance().setCurrentAct(PrmsNewPricesActivity.this);
                    loginSuccess();
//                    sc.resetAssignMargin(0);
                    getBackGroundLayout().setTitleStyle(NewBackGroundLayout.TitleStyle.White);
                    getBackGroundLayout().setTitleBackgroundAlpha(0.0f);
                }
            }
        });


        //要改wuhan
        investPriceView.setHelpMessage(getString(R.string.prms_helpmessage));
        investPriceView.setLoginOutAdvertiseImage(R.drawable.prms_adv_img);

        btn_query = (Button) findViewById(R.id.btn_query);//交易查询
        btn_accountmanager = (Button) findViewById(R.id.btn_accountmanager);//账户管理
        btn_recall = (Button) findViewById(R.id.btn_recall);//撤单

//        lyt_modlename = (LinearLayout) findViewById(R.id.lyt_modlename);
//        btn_selfchose = (Button) findViewById(R.id.btn_selfchose);//自选
//        btn_gold = (Button) findViewById(R.id.btn_gold);//贵金属
//        lyt_modlename.setVisibility(View.GONE);
        tv_edit = (TextView) findViewById(R.id.tv_edit);//编辑
        tv_edit.setVisibility(View.GONE);
        tv_riseorfall = (TextView) findViewById(R.id.tv_riseorfall);//涨幅跌
        lv_prms = (PullableListView) findViewById(R.id.lv_prms);
        lv_prms.setIsPullUp(false);
//        tv_refishtime = (TextView) findViewById(R.id.tv_refishtime);//更新时间


        RelativeLayout loarMore = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.isforex_rate_main_footer, null);
        tv_refishtime = (TextView) loarMore.findViewById(R.id.tv_refishtime);//更新时间
        lv_prms.addFooterView(loarMore);

    }

    public void initData() {
        commDataList = new ArrayList<Map<String, Object>>();
        //第一次置空
        BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", dataList);
        BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLogoutDataList", preLoginDataList);
        JUMPFLAG = prms;
        Drawable drawable = getResources().getDrawable(R.drawable.share_flat);
        Drawable drawablerise = getResources().getDrawable(R.drawable.share_flat);
        drawablerise.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv_riseorfall.setCompoundDrawables(null, null, drawablerise, null);
//        BaseHttpEngine.showProgressDialog();
//        mTimerRefreshToolsMultiple.startTimer();
        isLogin();

    }

    private void isLogin() {
        isSort = false;
        stopPollingFlag();
        if (BaseDroidApp.getInstanse().isLogin()) {
            if (!PollingRequestThread.pollingFlag) {
                BaseHttpEngine.showProgressDialogCanGoBack();
//                requestPsnAssetBalanceQuery();
                checkRequestPsnInvestmentManageIsOpen();
            }
            loginOutLayout.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
            ll_btn.setVisibility(View.VISIBLE);
            getBackGroundLayout().setLineDividerVisibility(View.GONE);
        } else {
            if (!PollingRequestThread.pollingFlag) {
                //发送没有登录时黄金行情。
                BaseHttpEngine.showProgressDialogCanGoBack();
                queryPrmsPricePoliPreLogin();
            }
            loginOutLayout.setVisibility(View.VISIBLE);
            loginLayout.setVisibility(View.GONE);
            ll_btn.setVisibility(View.GONE);
            getBackGroundLayout().setLineDividerVisibility(View.GONE);
        }
    }


    private void onresumIsLogin() {
        isSort = false;
        stopPollingFlag();
        if (BaseDroidApp.getInstanse().isLogin()) {
            if (!PollingRequestThread.pollingFlag) {
//                requestPsnAssetBalanceQuery();
                checkRequestPsnInvestmentManageIsOpen();
            }
            loginOutLayout.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
            ll_btn.setVisibility(View.VISIBLE);
            getBackGroundLayout().setLineDividerVisibility(View.GONE);
        } else {
            if (!PollingRequestThread.pollingFlag) {
                //发送没有登录时黄金行情。
                queryPrmsPricePoliPreLogin();
            }
            loginOutLayout.setVisibility(View.VISIBLE);
            loginLayout.setVisibility(View.GONE);
            ll_btn.setVisibility(View.GONE);
            getBackGroundLayout().setLineDividerVisibility(View.GONE);
        }
    }
    private void refashePullDown(){
        stopPollingFlag();
        if (BaseDroidApp.getInstanse().isLogin()){
            if (!PollingRequestThread.pollingFlag) {
                queryPrmsPricePoling();
            }
        }else{
            if (!PollingRequestThread.pollingFlag) {
                queryPrmsPricePoliPreLogin();
            }
        }
    }

    /** 停止轮询 */
    private void stopPollingFlag() {
        if (HttpManager.mPollingRequestThread != null && PollingRequestThread.pollingFlag) {
            LogGloble.e(TAG, "onPause() mPollingRequestThread  stopPolling()  ");
            HttpManager.stopPolling();
//            mTimerRefreshToolsMultiple.stopTimer();
        }
    }
    public void initListener() {
//        btn_base_back.setOnClickListener(this);
//        btn_share.setOnClickListener(this);
//        btn_more.setOnClickListener(this);


//        ima_help.setOnClickListener(this);
//        ima_openeye.setOnClickListener(this);

        btn_query.setOnClickListener(this);
        btn_accountmanager.setOnClickListener(this);
        btn_recall.setOnClickListener(this);

//        btn_selfchose.setOnClickListener(this);
//        btn_gold.setOnClickListener(this);
//        tv_edit.setOnClickListener(this);
        tv_riseorfall.setOnClickListener(this);

        lv_prms.setOnItemClickListener(this);
        pull_down_layout.setOnRefreshListener(new IRefreshLayoutListener() {
            @Override
            public void onLoadMore(View pullToRefreshLayout) {
//                isrefeshflag=true;

//                isLogin();
                refashePullDown();
            }
        });

//        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Drawable drawable = getResources().getDrawable(R.drawable.prms_chose);
        switch (v.getId()) {
//            case R.id.ib_back:
//                ActivityTaskManager.getInstance().removeAllSecondActivity();
//                break;
//            case R.id.share_bt:
//
//
//                break;
//            case R.id.ib_top_right_btn:
//                Intent intent = new Intent(PrmsNewPricesActivity.this,PrmsMoreActivity.class);
//                startActivity(intent);
//
//                break;
//            case R.id.ima_help:
//
//                break;
//            case R.id.ima_openeye:
//
//                break;
            case R.id.btn_query://查询
                stopPollingFlag();
                if (BaseDroidApp.getInstanse().isLogin()) {
                    Intent prmsQueryintent = new Intent(PrmsNewPricesActivity.this, PrmsQueryActivity.class);
                    startActivity(prmsQueryintent);
                } else {

                    BaseActivity.getLoginUtils(PrmsNewPricesActivity.this).exe(new LoginTask.LoginCallback() {

                        @Override
                        public void loginStatua(boolean isLogin) {
                            if (isLogin) {
                                Intent prmsQueryintent = new Intent(PrmsNewPricesActivity.this, PrmsQueryActivity.class);
                                startActivity(prmsQueryintent);
                            }
                        }
                    });
                }


                break;
            case R.id.btn_accountmanager://我的账户贵金属
                stopPollingFlag();
                if (BaseDroidApp.getInstanse().isLogin()) {
                    Intent prmsAccMeneintent = new Intent(PrmsNewPricesActivity.this, PrmsAccMeneActivity.class);
                    startActivity(prmsAccMeneintent);
                } else {

                    BaseActivity.getLoginUtils(PrmsNewPricesActivity.this).exe(new LoginTask.LoginCallback() {

                        @Override
                        public void loginStatua(boolean isLogin) {
                            if (isLogin) {
                                Intent prmsAccMeneintent = new Intent(PrmsNewPricesActivity.this, PrmsAccMeneActivity.class);
                                startActivity(prmsAccMeneintent);
                            }
                        }
                    });
                }

                break;
            case R.id.btn_recall://当前有效委托查询
                stopPollingFlag();
                if (!BaseDroidApp.getInstanse().isLogin()) {// 没有登录就跳转到登录页面
//				Intent intent = new Intent(PrmsQueryActivity.this,
//						LoginActivity.class);
//				startActivityForResult(intent,
//						ConstantGloble.ACTIVITY_RESULT_CODE);
                    BaseActivity.getLoginUtils(PrmsNewPricesActivity.this).exe(new LoginTask.LoginCallback() {

                        @Override
                        public void loginStatua(boolean isLogin) {
                            //TODO yuht。登录成功，自动调用开通投资理财接口
                            JUMPFLAG = ENTRUSTNOW;
                            CommonApplication.getInstance().setCurrentAct(PrmsNewPricesActivity.this);
                            BaseHttpEngine.showProgressDialog();
                            checkRequestPsnInvestmentManageIsOpen();
                        }
                    });
                    return;
                }
//                flag = 2;
//                isENTRUSTNOW = true;
                JUMPFLAG = ENTRUSTNOW;
                BaseHttpEngine.showProgressDialog();
                checkRequestPsnInvestmentManageIsOpen();

                break;
//            case R.id.btn_selfchose:
//                isGold = false;
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                btn_selfchose.setCompoundDrawables(null, null, null, drawable);
//                btn_selfchose.setTextColor(PrmsNewPricesActivity.this.getResources().getColor(R.color.red));
//                btn_gold.setCompoundDrawables(null, null, null, null);
//                btn_gold.setTextColor(PrmsNewPricesActivity.this.getResources().getColor(R.color.black));
//                break;
//            case R.id.btn_gold:
//                isGold = true;
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                btn_gold .setCompoundDrawables(null, null, null, drawable);
//                btn_gold.setTextColor(PrmsNewPricesActivity.this.getResources().getColor(R.color.red));
//                btn_selfchose.setCompoundDrawables(null, null, null, null);
//                btn_selfchose.setTextColor(PrmsNewPricesActivity.this.getResources().getColor(R.color.black));
//                break;
//            case R.id.tv_edit:
//
//
//
//                break;
            case R.id.tv_riseorfall:
//                if (BaseDroidApp.getInstanse().isLogin()) {
                stopPollingFlag();
                getRaisOrfall();
//                } else {
//
//                    loginClicks();
//                }


                break;
//            case R.id.btn_login:
//        BaseActivity.getLoginUtils(PrmsNewPricesActivity.this).exe(new LoginTask.LoginCallback() {
//
//            @Override
//            public void loginStatua(boolean isLogin) {
//                // TODO Auto-geneonrated method stub
//                if (isLogin) {
//                    loginSuccess();
//                }
//            }
//        });
//                break;
            default:
                break;
        }
    }

    /**
     * 点击登陆
     */
//    public void loginClicks() {
//        BaseActivity.getLoginUtils(PrmsNewPricesActivity.this).exe(new LoginTask.LoginCallback() {
//
//            @Override
//            public void loginStatua(boolean isLogin) {
//                // TODO Auto-geneonrated method stub
//                if (isLogin) {
//                    getRaisOrfall();
//                }
//            }
//        });
//    }

//    private String pSort = "UP";//涨跌幅排序
//    private int iscurrPercentDiff = 1;//涨跌幅排序 1 : UP;2 :DN;3：无排序
    private boolean isSort = false;
    public void getRaisOrfall() {
        isSort = true;
        Drawable drawable = getResources().getDrawable(R.drawable.prms_gold_fall);
        tv_riseorfall.setText("涨跌幅");
        if (iscurrPercentDiff==1) {
            iscurrPercentDiff = 2;
            Drawable drawablefall = getResources().getDrawable(R.drawable.prms_gold_rise);
            drawablefall.setBounds(0, 0, drawable.getMinimumWidth()/2, drawable.getMinimumHeight()/2);
            tv_riseorfall.setCompoundDrawables(null, null, drawablefall, null);
            pSort = "UP";
        } else if (iscurrPercentDiff==2) {
            Drawable drawablerise = getResources().getDrawable(R.drawable.prms_gold_fall);
            drawablerise.setBounds(0, 0, drawable.getMinimumWidth()/2, drawable.getMinimumHeight()/2);
            tv_riseorfall.setCompoundDrawables(null, null, drawablerise, null);
            pSort = "DN";
            iscurrPercentDiff = 3;

        }else if(iscurrPercentDiff==3){
            Drawable drawablerise = getResources().getDrawable(R.drawable.share_flat);
            drawablerise.setBounds(0, 0, drawable.getMinimumWidth()/2, drawable.getMinimumHeight()/2);
            tv_riseorfall.setCompoundDrawables(null, null, drawablerise, null);
            pSort = "";
            iscurrPercentDiff = 1;
        }
        if (!PollingRequestThread.pollingFlag){
            BaseHttpEngine.showProgressDialogCanGoBack();
            getqueryMultipleQuotation(pSort);
        }



    }



    @Override
    public void checkRequestPsnInvestmentManageIsOpenCallback(Object resultObj) {
        super.checkRequestPsnInvestmentManageIsOpenCallback(resultObj);
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        String isOpenOr = (String) biiResponseBody.getResult();
        prmsControl.ifInvestMent = StringUtil.parseStrToBoolean(isOpenOr);

        queryPrmsAcc();
    }


    @Override
    public void queryPrmsAccCallBack(Object resultObj) {
        super.queryPrmsAccCallBack(resultObj);
        BaseHttpEngine.dissMissProgressDialog();
        BiiResponse biiResponse = (BiiResponse) resultObj;

        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

        if (biiResponseBody.getResult() == null) {
            prmsControl.ifhavPrmsAcc = false;
            prmsControl.accMessage = null;
            prmsControl.accId = null;
        } else {
            //wuhan保存一下省连号：

            prmsControl.accMessage = (Map<String, String>) biiResponseBodys
                    .get(0).getResult();
            String ibkNum = String.valueOf(prmsControl.accMessage.get(Prms.QUERY_PRMSACC_IBKNUM));
            LocalDataService.getInstance().saveIbkNum(ConstantGloble.Prms, ibkNum);
            prmsControl.accNum = String.valueOf(prmsControl.accMessage
                    .get(Prms.QUERY_PRMSACC_ACCOUNT));
            prmsControl.accId = String.valueOf(prmsControl.accMessage
                    .get(Prms.QUERY_PRMSACC_ACCOUNTID));
            prmsControl.ifhavPrmsAcc = true;
        }
        if (!prmsControl.ifhavPrmsAcc || !prmsControl.ifInvestMent) {
            getPopup();
//            要改，显示没有登录的界面???wuhanm

        } else {

            switch (JUMPFLAG) {
                case prms:
                    queryPrmsPricePoling();
                    break;
                case ENTRUSTNOW:
                    requestCommConversationId();
                    break;
                case ACCPOSITIONINFO:
                    JUMPFLAG = 1;
                    BaseHttpEngine.dissMissProgressDialog();
                    startActivity(new Intent(this, PrmsAccPositionActivity.class));
                    break;
                default:
                    break;
            }

        }
    }

    @Override
    public void queryPrmsAccsCallBack(Object resultObj) {
        super.queryPrmsAccsCallBack(resultObj);
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
            BaseHttpEngine.dissMissProgressDialog();
            // 如果没有可设定的账户
            BaseDroidApp.getInstanse().showInfoMessageDialog(
                    getString(R.string.prms_noprmsAcc_error));
            return;
        } else {
            prmsControl.prmsAccList = (List<Map<String, String>>) (biiResponseBody
                    .getResult());
            startActivityForResult(new Intent(this, PrmsAccSettingActivity.class), ConstantGloble.ACTIVITY_REQUEST_SETPRMSACC_CODE);
            BaseHttpEngine.dissMissProgressDialog();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        BaseDroidApp.getInstanse().setCurrentAct(this);
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ConstantGloble.ACTIVITY_REQUEST_SETPRMSACC_CODE:// 设置贵金属账户
                switch (resultCode) {
                    case RESULT_OK:
                        prmsControl.ifhavPrmsAcc = true;
                        BaseDroidApp.getInstanse().dismissMessageDialog();
//                        getqueryMultipleQuotation("");
                        queryPrmsPricePoling();
                        break;
                    default:
                        prmsControl.ifhavPrmsAcc = false;
                        getPopup();
                        break;
                }
                break;
            case ConstantGloble.ACTIVITY_REQUEST_MANAGE_CODE:// 开通理财服务
                switch (resultCode) {
                    case RESULT_OK:
                        prmsControl.ifInvestMent = true;
                        if (prmsControl.ifhavPrmsAcc) {
                            BaseDroidApp.getInstanse().dismissMessageDialog();
//                            getqueryMultipleQuotation("");
                            queryPrmsPricePoling();
                        } else {
                            getPopup();
                        }
                        break;
                    default:
                        prmsControl.ifInvestMent = false;
                        getPopup();
                        break;
                }
                break;
            default:
                break;
        }
    }


    protected void queryPrmsPricePoling() {
        isSort = false;
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Prms.QUERY_TRADERATE);
        biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
                .getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
        biiRequestBody.setParams(null);
        HttpManager.requestPollingBii(biiRequestBody, httpHandler, 7);// 7秒刷新
    }


    public Handler httpHandler = new Handler() {
        public void handleMessage(Message msg) {
            // http状态码
            String resultHttpCode = (String) ((Map<String, Object>) msg.obj)
                    .get(ConstantGloble.HTTP_RESULT_CODE);

            // 返回数据
            Object resultObj = ((Map<String, Object>) msg.obj)
                    .get(ConstantGloble.HTTP_RESULT_DATA);

            // 回调对象
            HttpObserver callbackObject = (HttpObserver) ((Map<String, Object>) msg.obj)
                    .get(ConstantGloble.HTTP_CALLBACK_OBJECT);

            // 回调方法
            String callBackMethod = (String) ((Map<String, Object>) msg.obj)
                    .get(ConstantGloble.HTTP_CALLBACK_METHOD);
            LogGloble.i("info", "resultHttpCode =" + resultHttpCode + "callbackObject ==" + callbackObject);
            switch (msg.what) {

                // 正常http请求数据返回
                case ConstantGloble.HTTP_STAGE_CONTENT:
                    BiiResponse biiResponse = (BiiResponse) ((Map<String, Object>) msg.obj)
                            .get(ConstantGloble.HTTP_RESULT_DATA);
                    List<BiiResponseBody> biiResponseBodys = biiResponse
                            .getResponse();
                    BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
                    if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
                        BaseHttpEngine.dissMissProgressDialog();
                        return;
                    }
                    BaseHttpEngine.dissMissProgressDialog();


                    dataList = (List<Map<String, Object>>) (biiResponseBody
                            .getResult());
                    //wuhan保存一下省连号：
//				String ibkNum = dataList.get("accountIbkNum");
//				LogGloble.i("info", "ibkNum=="+ibkNum);
//				LocalDataService.getInstance().saveIbkNum(ConstantGloble.Prms, ibkNum);
                    // /add by fsm判断行情列表是否为空
                    if (StringUtil.isNullOrEmpty(dataList)) {
                        BaseDroidApp.getInstanse().showMessageDialog(
                                getString(R.string.prms_no_price),
                                new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        PrmsNewPricesActivity.this.finish();
                                    }
                                });
                    }

                    getSortForInvest(dataList);



                    if(null != commDataList && commDataList.size()!=0){
                        ListUtils.synchronousList(dataList, commDataList, new ListUtils.ISynchronousList<Map<String, Object>, Map<String, Object>>() {


                            @Override
                            public boolean doSomeThing(Map<String, Object> stringObjectMap, Map<String, Object> stringObject) {
                                String sourceCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_SOURCECODE_RES);
                                String targetCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_TARGETCODE_RES);
                                String sourceCurrencyCodeNew = (String) stringObject.get("sourceCurrencyCode");
                                String targetCurrencyCodeNew =(String) stringObject.get("targetCurrencyCode");
                                String currencyCode = sourceCurrencyCode+targetCurrencyCode;
                                String currencyCodeNew = sourceCurrencyCodeNew+targetCurrencyCodeNew;
                                if(currencyCode.equals(currencyCodeNew)) {
                                    stringObjectMap.put("currPercentDiff",(String) stringObject.get("currPercentDiff") ); // 今日涨跌幅 小数点后有效位数为3位
                                    stringObjectMap.put("currDiff",(String) stringObject.get("currDiff")); // 今日涨跌值
                                    return true;
                                }
                                return false;
                            }
                        });
//                            LogGloble.i("info","preLoginDataList ==" + preLoginDataList.toString());
                    }
//                    ListUtils.synchronousList(dataList, commDataList, new ListUtils.ISynchronousList<Map<String, Object>, Map<String, Object>>() {
//
//
//                        @Override
//                        public boolean doSomeThing(Map<String, Object> stringObjectMap, Map<String, Object> stringObject) {
//                            String sourceCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_SOURCECODE_RES);
//                            String targetCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_TARGETCODE_RES);
//                            String sourceCurrencyCodeNew = (String) stringObject.get("sourceCurrencyCode");
//                            String targetCurrencyCodeNew =(String) stringObject.get("targetCurrencyCode");
//                            String currencyCode = sourceCurrencyCode+targetCurrencyCode;
//                            String currencyCodeNew = sourceCurrencyCodeNew+targetCurrencyCodeNew;
//                            if(currencyCode.equals(currencyCodeNew)) {
//                                stringObjectMap.put("currPercentDiff",(String) stringObject.get("currPercentDiff") ); // 今日涨跌幅 小数点后有效位数为3位
//                                stringObjectMap.put("currDiff",(String) stringObject.get("currDiff")); // 今日涨跌值
////                            allRateList.add(stringObjectMap);
//                                return true;
//                            }
//                            return false;
//                        }
//                    });

//                    if("".equals(pSort)){
//                        ListUtils.sortForInvest(dataList, new ListUtils.ISynchronousList<Map<String, Object>, KeyAndValueItem>() {
//                            @Override
//                            public boolean doSomeThing(Map<String, Object> stringObjectMap, KeyAndValueItem keyAndValueItem) {
//                                String sourceCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE);
//                                String targetCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE);
//                                if(keyAndValueItem.getKey().equals(sourceCurrencyCode) && keyAndValueItem.getValue().equals(targetCurrencyCode)){
//                                    stringObjectMap.put("formatNumber",keyAndValueItem.getParam());
//                                    return true;
//                                }
//                                return false;
//                            }
//                        });
//                    }


//
//                    for (int i = 0; i < commDataList.size(); i++) {
//                        Map<String, Object> map = commDataList.get(i);
//                        String sourceCurrencyCodes = (String) map
//                                .get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE);
//                        String targetCurrencyCodes = (String) map
//                                .get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE);
//                        for (int j = 0; j < dataList.size(); j++) {
//                            if (sourceCurrencyCodes.equals(dataList.get(j).get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE)) &&
//                                    targetCurrencyCodes.equals(dataList.get(j).get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE))) {
//                                map.putAll(dataList.get(j));
//                                totalLoginDataList.add(map);
//                                break;
//                            }
//                        }
//                    }


                    if (loginAdatper == null) {
                        loginAdatper = new CommonAdapter<Map<String, Object>>(PrmsNewPricesActivity.this, dataList, iCommonAdapter);
                        lv_prms.setAdapter(loginAdatper);

                    } else {
//                        loginAdatper.notifyDataSetChanged();
                        loginAdatper.setSourceList(dataList,0);
                    }

                    if (ffff && prmsControl.ifhavPrmsAcc && prmsControl.ifInvestMent) {
                        queryPrmsAccBalance();
                        ffff = false;
                    }
                    // 设置更新时间

                    int i = dataList.size();
                    Map<String, Object> lastMap = dataList.get(i - 1);
                    tv_refishtime.setText("数据更新于北京时间"
                            + lastMap.get(Prms.QUERY_TRADERATE_CREADTEDATE) + ", 具体价格以实际成交为准");
//                    showGuide();


                    pull_down_layout.loadmoreCompleted(RefreshDataStatus.Successed);
                    //四方接口
//                    mTimerRefreshToolsMultiple.startTimer();

                    requestQueryMultipleQuotation();
                    break;

                // 请求失败错误情况处理
                case ConstantGloble.HTTP_STAGE_CODE:

                    BaseHttpEngine.dissMissProgressDialog();
                    /**
                     * 执行code error 全局前拦截器
                     */
                    if (BaseDroidApp.getInstanse().httpCodeErrorCallBackPre(
                            resultHttpCode)) {
                        break;
                    }

                    /**
                     * 执行callbackObject error code 回调前拦截器
                     */
                    if (callbackObject.httpCodeErrorCallBackPre(resultHttpCode)) {
                        break;
                    }

                    Method httpCodeCallbackMethod = null;
                    try {
                        // 回调
                        httpCodeCallbackMethod = callbackObject.getClass()
                                .getMethod(callBackMethod, String.class);

                        httpCodeCallbackMethod.invoke(callbackObject,
                                resultHttpCode);
                    } catch (SecurityException e) {
                        LogGloble.e(TAG, "SecurityException ", e);
                    } catch (NoSuchMethodException e) {
                        LogGloble.e(TAG, "NoSuchMethodException ", e);
                    } catch (IllegalArgumentException e) {
                        LogGloble.e(TAG, "IllegalArgumentException ", e);
                    } catch (IllegalAccessException e) {
                        LogGloble.e(TAG, "IllegalAccessException ", e);
                    } catch (InvocationTargetException e) {
                        LogGloble.e(TAG, "InvocationTargetException ", e);
                    } catch (NullPointerException e) {
                        // add by wez 2012.11.06
                        LogGloble.e(TAG, "NullPointerException ", e);
                        throw e;
                    } catch (ClassCastException e) {
                        // add by wez 2012.11.06
                        LogGloble.e(TAG, "ClassCastException ", e);
                        throw e;
                    }

                    /**
                     * 执行code error 全局后拦截器
                     */
                    if (BaseDroidApp.getInstanse().httpCodeErrorCallBackAfter(
                            resultHttpCode)) {
                        break;
                    }

                    /**
                     * 执行callbackObject code error 后拦截器
                     */
                    if (callbackObject.httpCodeErrorCallBackAfter(resultHttpCode)) {
                        break;
                    }

                    break;

                default:
                    break;
            }

        }
    };

    boolean ffff = false;

    public void loginSuccess() {
        //要改
        loginOutLayout.setVisibility(View.GONE);
        loginLayout.setVisibility(View.VISIBLE);
        BaseHttpEngine.showProgressDialogCanGoBack();
        //再发一次获取贵金属信息的报文。//wuhan
        JUMPFLAG = prms;
//        requestPsnAssetBalanceQuery();
        checkRequestPsnInvestmentManageIsOpen();
    }

//    private void showGuide() {
//        getWindow().getDecorView().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                if (isshowguid) {
//                    View textView = lv_prms.findViewById(R.id.prms_price_listiterm1_buyprice);
//                    if (textView != null) {
//                        isshowguid = false;
//                        int[] location = new int[2];
//                        textView.getLocationInWindow(location);
//                        int diff = 10;
//                        RectF src = new RectF(location[0] - diff, location[1]
//                                - diff, location[0] + textView.getWidth()
//                                + diff, location[1] + textView.getHeight()
//                                + diff);
//                        CardWelcomGuideUtil.showCardPriceGuid(
//                                PrmsNewPricesActivity.this, src);
//                    } else {
//                        LogGloble.d(TAG, "testView is null");
//                    }
//                }
//            }
//        }, 500);
//    }


    /**
     * 黄金行情查询登录之前发送
     */
    //wuhan
    protected void queryPrmsPricePoliPreLogin() {
        isSort = false;
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Prms.QUERY_TRADERATE_PRELOGIN);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Prms.IBKNUM, LocalDataService.getInstance().getIbkNum(ConstantGloble.Prms));
        map.put(Prms.PARITIESTYPE, "G");
        map.put("offerType", "R");
        biiRequestBody.setParams(map);
//		HttpManager.requestOutlayBii(biiRequestBody, this, "queryPrmsPreLoginCallBack");
        HttpManager.requestPollingOutlay(biiRequestBody,httpHandlerPreLogin , 7);// 7秒刷新
    }

    public Handler httpHandlerPreLogin = new Handler() {

        public void handleMessage(Message msg) {
            // http状态码
            String resultHttpCode = (String) ((Map<String, Object>) msg.obj)
                    .get(ConstantGloble.HTTP_RESULT_CODE);

            // 返回数据
            Object resultObj = ((Map<String, Object>) msg.obj)
                    .get(ConstantGloble.HTTP_RESULT_DATA);

            // 回调对象
            HttpObserver callbackObject = (HttpObserver) ((Map<String, Object>) msg.obj)
                    .get(ConstantGloble.HTTP_CALLBACK_OBJECT);

            // 回调方法
            String callBackMethod = (String) ((Map<String, Object>) msg.obj)
                    .get(ConstantGloble.HTTP_CALLBACK_METHOD);
            LogGloble.i("info","走了。。。。");
            LogGloble.i("info", "resultHttpCode =" + resultHttpCode + "callbackObject ==" + callbackObject);
            switch (msg.what) {

                // 正常http请求数据返回
                case ConstantGloble.HTTP_STAGE_CONTENT:
                    BiiResponse biiResponse = (BiiResponse) ((Map<String, Object>) msg.obj)
                            .get(ConstantGloble.HTTP_RESULT_DATA);
                    List<BiiResponseBody> biiResponseBodys = biiResponse
                            .getResponse();
                    BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
                    if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
                        BaseHttpEngine.dissMissProgressDialog();
                        return;
                    }
                    preLoginDataList = (List<Map<String, Object>>) biiResponseBody.getResult();

                    // /add by fsm判断行情列表是否为空
                    BaseHttpEngine.dissMissProgressDialog();
                    if (StringUtil.isNullOrEmpty(preLoginDataList)) {
                        BaseDroidApp.getInstanse().showMessageDialog(
                                getString(R.string.prms_no_price),
                                new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        PrmsNewPricesActivity.this.finish();
                                    }
                                });
                    }

                    getSortForInvest(preLoginDataList);
//                        ListUtils.sortForInvest(preLoginDataList, new ListUtils.ISynchronousList<Map<String, Object>, KeyAndValueItem>() {
//                            @Override
//                            public boolean doSomeThing(Map<String, Object> stringObjectMap, KeyAndValueItem keyAndValueItem) {
//                                String sourceCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE);
//                                String targetCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE);
//                                if(keyAndValueItem.getKey().equals(sourceCurrencyCode) && keyAndValueItem.getValue().equals(targetCurrencyCode)){
//                                    stringObjectMap.put("formatNumber",keyAndValueItem.getParam());
//                                    return true;
//                                }
//                                return false;
//                            }
//                        });

                    if(null != commDataList && commDataList.size()!=0){
                        ListUtils.synchronousList(commDataList,preLoginDataList, new ListUtils.ISynchronousList<Map<String, Object>, Map<String, Object>>() {


                            @Override
                            public boolean doSomeThing(Map<String, Object> stringObjectMap, Map<String, Object> stringObject) {
                                String sourceCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_SOURCECODE_RES);
                                String targetCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_TARGETCODE_RES);
                                String sourceCurrencyCodeNew = (String) stringObject.get("sourceCurrencyCode");
                                String targetCurrencyCodeNew =(String) stringObject.get("targetCurrencyCode");
                                String currencyCode = sourceCurrencyCode+targetCurrencyCode;
                                String currencyCodeNew = sourceCurrencyCodeNew+targetCurrencyCodeNew;
                                if(currencyCode.equals(currencyCodeNew)) {
//                                    stringObjectMap.put("currPercentDiff",(String) stringObject.get("currPercentDiff") ); // 今日涨跌幅 小数点后有效位数为3位
//                                    stringObjectMap.put("currDiff",(String) stringObject.get("currDiff")); // 今日涨跌值
                                    stringObjectMap.put("buyRate",(String) stringObject.get("buyRate") );
                                    stringObjectMap.put("sellRate",(String) stringObject.get("sellRate") );
                                    stringObjectMap.put("ibkNum",(String) stringObject.get("ibkNum") );
                                    stringObjectMap.put("type",(String) stringObject.get("type") );
                                    stringObjectMap.put("rate",(String) stringObject.get("rate") );
                                    stringObjectMap.put("updateDate",(String) stringObject.get("updateDate") );
                                    stringObjectMap.put("buyNoteRate",(String) stringObject.get("buyNoteRate") );
                                    stringObjectMap.put("sellNoteRate",(String) stringObject.get("sellNoteRate") );
                                    stringObjectMap.put("state",(String) stringObject.get("state") );
                                    stringObjectMap.put("formatNumber",stringObject.get("formatNumber"));



                                    return true;
                                }
                                return false;
                            }
                        });
//                            LogGloble.i("info","preLoginDataList ==" + preLoginDataList.toString());

                        preLoginDataList.clear();
                        preLoginDataList = commDataList;

                    }
//        for (int i = 0; i < commDataList.size(); i++) {
//            Map<String, Object> map = commDataList.get(i);
//            String sourceCurrencyCodes = (String) map
//                    .get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE);
//            String targetCurrencyCodes = (String) map
//                    .get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE);
//            for (int j = 0; j < preLoginDataList.size(); j++) {
//                if (sourceCurrencyCodes.equals(preLoginDataList.get(j).get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE)) &&
//                        targetCurrencyCodes.equals(preLoginDataList.get(j).get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE))) {
//                    map.putAll(preLoginDataList.get(j));
//                    totalLogoutDataList.add(map);
//                    break;
//                }
//            }
//        }

                    if (preLoginAdapter == null) {
                        preLoginAdapter = new CommonAdapter<Map<String, Object>>(PrmsNewPricesActivity.this, preLoginDataList, preloginCommonAdapter);
                        lv_prms.setAdapter(preLoginAdapter);
                    } else {
//                        preLoginAdapter.notifyDataSetChanged();
                        preLoginAdapter.setSourceList(preLoginDataList,0);

                    }
                    // 设置更新时间
                    // wuhan
                    int i = preLoginDataList.size();
                    Map<String, Object> lastMap = preLoginDataList.get(i - 1);
                    tv_refishtime.setText("数据更新于北京时间"
                            + lastMap.get(Prms.PRELOGIN_QUERY_UPDATEDATE) + ", 具体价格以实际成交为准");
//                   保存本次所有请求回来的数据

                    pull_down_layout.loadmoreCompleted(RefreshDataStatus.Successed);


//                    mTimerRefreshToolsMultiple.startTimer();
                    requestQueryMultipleQuotation();
                    break;

                // 请求失败错误情况处理
                case ConstantGloble.HTTP_STAGE_CODE:

                    BaseHttpEngine.dissMissProgressDialog();
                    /**
                     * 执行code error 全局前拦截器
                     */
                    if (BaseDroidApp.getInstanse().httpCodeErrorCallBackPre(
                            resultHttpCode)) {
                        break;
                    }

                    /**
                     * 执行callbackObject error code 回调前拦截器
                     */
                    if (callbackObject.httpCodeErrorCallBackPre(resultHttpCode)) {
                        break;
                    }

                    Method httpCodeCallbackMethod = null;
                    try {
                        // 回调
                        httpCodeCallbackMethod = callbackObject.getClass()
                                .getMethod(callBackMethod, String.class);

                        httpCodeCallbackMethod.invoke(callbackObject,
                                resultHttpCode);
                    } catch (SecurityException e) {
                        LogGloble.e(TAG, "SecurityException ", e);
                    } catch (NoSuchMethodException e) {
                        LogGloble.e(TAG, "NoSuchMethodException ", e);
                    } catch (IllegalArgumentException e) {
                        LogGloble.e(TAG, "IllegalArgumentException ", e);
                    } catch (IllegalAccessException e) {
                        LogGloble.e(TAG, "IllegalAccessException ", e);
                    } catch (InvocationTargetException e) {
                        LogGloble.e(TAG, "InvocationTargetException ", e);
                    } catch (NullPointerException e) {
                        // add by wez 2012.11.06
                        LogGloble.e(TAG, "NullPointerException ", e);
                        throw e;
                    } catch (ClassCastException e) {
                        // add by wez 2012.11.06
                        LogGloble.e(TAG, "ClassCastException ", e);
                        throw e;
                    }

                    /**
                     * 执行code error 全局后拦截器
                     */
                    if (BaseDroidApp.getInstanse().httpCodeErrorCallBackAfter(
                            resultHttpCode)) {
                        break;
                    }

                    /**
                     * 执行callbackObject code error 后拦截器
                     */
                    if (callbackObject.httpCodeErrorCallBackAfter(resultHttpCode)) {
                        break;
                    }

                    break;

                default:
                    break;
            }

        }
    };

//    @Override
//    public void queryPrmsPreLoginCallBack(Object resultObj) {
//        super.queryPrmsPreLoginCallBack(resultObj);
//
//        BiiResponse biiResponse = (BiiResponse) resultObj;
//        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//
//        if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
//            BaseHttpEngine.dissMissProgressDialog();
//			BaseDroidApp.getInstanse().showInfoMessageDialog("请求失败，稍后再试！");
//            return;
//        }
//        String preLoginDetail = (String) biiResponseBody.getResult().toString();
//        LogGloble.i("info", "biiResponseBody.getResult()===" + preLoginDetail);
//        preLoginDataList = (List<Map<String, Object>>) biiResponseBody.getResult();
//
//
//
//
//
//
//        // /add by fsm判断行情列表是否为空
//        BaseHttpEngine.dissMissProgressDialog();
//        if (StringUtil.isNullOrEmpty(preLoginDataList)) {
//            BaseDroidApp.getInstanse().showMessageDialog(
//                    getString(R.string.prms_no_price),
//                    new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            PrmsNewPricesActivity.this.finish();
//                        }
//                    });
//        }
//
//
////        ListUtils.synchronousList(preLoginDataList, commDataList, new ListUtils.ISynchronousList<Map<String, Object>, Map<String, Object>>() {
////
////
////            @Override
////            public boolean doSomeThing(Map<String, Object> stringObjectMap, Map<String, Object> stringObject) {
////                String sourceCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_SOURCECODE_RES);
////                String targetCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_TARGETCODE_RES);
////                String sourceCurrencyCodeNew = (String) stringObject.get("sourceCurrencyCode");
////                String targetCurrencyCodeNew =(String) stringObject.get("targetCurrencyCode");
////                String currencyCode = sourceCurrencyCode+targetCurrencyCode;
////                String currencyCodeNew = sourceCurrencyCodeNew+targetCurrencyCodeNew;
////                if(currencyCode.equals(currencyCodeNew)) {
////                    stringObjectMap.put("currPercentDiff",(String) stringObject.get("currPercentDiff") ); // 今日涨跌幅 小数点后有效位数为3位
////                    stringObjectMap.put("currDiff",(String) stringObject.get("currDiff")); // 今日涨跌值
//////                            allRateList.add(stringObjectMap);
////                    return true;
////                }
////                return false;
////            }
////        });
//
//
////        if("".equals(pSort)){
////            ListUtils.sortForInvest(preLoginDataList, new ListUtils.ISynchronousList<Map<String, Object>, KeyAndValueItem>() {
////                @Override
////                public boolean doSomeThing(Map<String, Object> stringObjectMap, KeyAndValueItem keyAndValueItem) {
////                    String sourceCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE);
////                    String targetCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE);
////                    if(keyAndValueItem.getKey().equals(sourceCurrencyCode) && keyAndValueItem.getValue().equals(targetCurrencyCode)){
//////                        stringObjectMap.put("formatNumber",keyAndValueItem.getParam());
////                        return true;
////                    }
////                    return false;
////                }
////            });
////        }
//
////        totalLogoutDataList = new ArrayList<Map<String, Object>>();
//////        for (int i = 0; i < preLoginDataList.size(); i++) {
//////            Map<String, Object> map = preLoginDataList.get(i);
//////            map.putAll(commDataList.get(i));
//////            totalLogoutDataList.add(map);
//////        }
////        for (int i = 0; i < commDataList.size(); i++) {
////            Map<String, Object> map = commDataList.get(i);
////            String sourceCurrencyCodes = (String) map
////                    .get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE);
////            String targetCurrencyCodes = (String) map
////                    .get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE);
////            for (int j = 0; j < preLoginDataList.size(); j++) {
////                if (sourceCurrencyCodes.equals(preLoginDataList.get(j).get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE)) &&
////                        targetCurrencyCodes.equals(preLoginDataList.get(j).get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE))) {
////                    map.putAll(preLoginDataList.get(j));
////                    totalLogoutDataList.add(map);
////                    break;
////                }
////            }
////        }
//
//
//        //单存货币对  排序按金银铂钯排序，人民币在前，美元在后
////        sort_code_List.clear();
////        for (int m = 0; m < Prms.code_List.size(); m++) {
////            String ccygrp = Prms.code_List.get(m);
////            for (int k = 0; k < totalLogoutDataList.size(); k++) {
////                Map<String, Object> map = totalLogoutDataList.get(k);
////                final String sourceCurrencyCode = (String) map
////                        .get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE);//金，银，铂，钯
////                final String targetCurrencyCode = (String) map
////                        .get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE);
////                if (m == 0 && ccygrp.equals(sourceCurrencyCode + targetCurrencyCode)) {
////                    sort_code_List.add("人民币金/黄金(克)");
////                }
////                if (m == 1 && ccygrp.equals(sourceCurrencyCode + targetCurrencyCode)) {
////                    sort_code_List.add("人民币银/白银(克)");
////                }
////                if (m == 2 && ccygrp.equals(sourceCurrencyCode + targetCurrencyCode)) {
////                    sort_code_List.add("人民币铂/铂金(克)");
////                }
////                if (m == 3 && ccygrp.equals(sourceCurrencyCode + targetCurrencyCode)) {
////                    sort_code_List.add("人民币钯/钯金(克)");
////                }
////                if (m == 4 && ccygrp.equals(sourceCurrencyCode + targetCurrencyCode)) {
////                    sort_code_List.add("美元金/美元(盎司)");
////                }
////                if (m == 5 && ccygrp.equals(sourceCurrencyCode + targetCurrencyCode)) {
////                    sort_code_List.add("美元银/白银(盎司)");
////                }
////                if (m == 6 && ccygrp.equals(sourceCurrencyCode + targetCurrencyCode)) {
////                    sort_code_List.add("美元铂/铂金(盎司)");
////                }
////
////                if (m == 7 && ccygrp.equals(sourceCurrencyCode + targetCurrencyCode)) {
////                    sort_code_List.add("美元钯/钯金(盎司)");
////                }
////
////            }
////        }
//            if (preLoginAdapter == null) {
//                preLoginAdapter = new CommonAdapter<Map<String, Object>>(this, preLoginDataList, preloginCommonAdapter);
//                lv_prms.setAdapter(preLoginAdapter);
//            // wuhan
////            myAdapter.setsaleOnClickListener(saleonclick);
////            myAdapter.setbuyonclick(buyonclick);
////            myAdapter.notifyDataSetChanged();
////            listView.setAdapter(myAdapter);
//
//        } else {
////            myAdapter.setsaleOnClickListener(saleonclick);
////            myAdapter.setbuyonclick(buyonclick);
////            myAdapter.datachanged(preLoginDataList);
//            preLoginAdapter.notifyDataSetChanged();
//
//        }
//        // 设置更新时间
//        // wuhan
//        int i = preLoginDataList.size();
//        Map<String, Object> lastMap = preLoginDataList.get(i - 1);
//        tv_refishtime.setText("数据更新于北京时间"
//                + lastMap.get(Prms.PRELOGIN_QUERY_UPDATEDATE) + ", 具体价格以实际成交为准");
////        tvReNewTime.setText(lastMap.get(Prms.PRELOGIN_QUERY_UPDATEDATE).toString());
////        showGuide();
////                   保存本次所有请求回来的数据
//        BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLogoutDataList", preLoginDataList);
//
//
//
//        mTimerRefreshToolsMultiple.startTimer();
//    }


    /**
     * 登录之后行情适配器
     */
    private ICommonAdapter<Map<String, Object>> iCommonAdapter = new ICommonAdapter<Map<String, Object>>() {

        @Override
        public View getView(int arg0, Map<String, Object> currentItem, LayoutInflater inflater, View convertView,
                            ViewGroup viewGroup) {
            ViewHoldler holder = null;
            if (convertView == null) {
                holder = new ViewHoldler();
                convertView = inflater.inflate(R.layout.prms_main_item, lv_prms, false);
                holder.lty_item = (LinearLayout) convertView.findViewById(R.id.lty_item);
                holder.tv_currency = (TextView) convertView.findViewById(R.id.tv_currency);
                holder.tv_sale = (NumberStyleTextView) convertView.findViewById(R.id.tv_sale);
//                holder.tv_saledecimal = (TextView) convertView.findViewById(R.id.tv_saledecimal);
                holder.tv_buy = (NumberStyleTextView) convertView.findViewById(R.id.tv_buy);
//                holder.tv_buydecimal = (TextView) convertView.findViewById(R.id.tv_buydecimal);
                holder.tv_riseorfall = (TextView) convertView.findViewById(R.id.tv_riseorfall);


                convertView.setTag(holder);
            } else {
                holder = (ViewHoldler) convertView.getTag();
            }

            //
            final String upDownFlag = (String) currentItem.get(Prms.PRELOGIN_QUERY_FLAG);
            String buyRate = String.valueOf(currentItem
                    .get(Prms.PRELOGIN_QUERY_BUYRATE));
            String sellRate = String.valueOf(currentItem
                    .get(Prms.PRELOGIN_QUERY_SELLRATE));
            final String sourceCurrencyCode = (String) currentItem
                    .get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE);
            final String targetCurrencyCode = (String) currentItem
                    .get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE);

//            if(sellRate!=null && buyRate!=null){
//            if(!"null".equals(buyRate) && !"null".equals(sellRate)){
//                String[] buys = parseStringPattern4(buyRate, 4).split("\\.");
//                String num1 =buys[1].substring(0, 2);
//                String num2 = buys[1].substring(buys[1].length()-2, buys[1].length());
//                String[] sales = parseStringPattern4(sellRate, 4).split("\\.");
//                String salenum1 =buys[1].substring(0, 2);
//                String salenum2 = buys[1].substring(buys[1].length()-2, buys[1].length());
////                holder.tv_buy.setText(buys[0] + "."+num1);
////                holder.tv_buydecimal.setText(num2);
////                holder.tv_sale.setText(sales[0] + "."+salenum1);
////                holder.tv_saledecimal.setText(salenum2);
//
//                holder.tv_buy.setText( sales[0] + "."+salenum1);
//                holder.tv_buydecimal.setText(salenum2);
//                holder.tv_sale.setText(buys[0] + "."+num1 );
//                holder.tv_saledecimal.setText( num2 );
//            }else{
//                holder.tv_buy.setText("-");
//                holder.tv_buydecimal.setText("");
//                holder.tv_sale.setText("-");
//                holder.tv_saledecimal.setText("");
//            }

            Integer formatNumber = (Integer)currentItem.get("formatNumber");
            sellRate = parseStringPattern4(sellRate,formatNumber);
            buyRate = parseStringPattern4(buyRate,formatNumber);
            holder.tv_buy.setNumberText(sellRate,2,"--");
            holder.tv_sale.setNumberText(buyRate,2,"--");

            String temp = "";
            final String sourceCurrency = (String) currentItem
                    .get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);

//            if (sourceCurrency
//                    .equals(ConstantGloble.PRMS_CURRENCYCODE_RMBG)
//                    && targetCurrencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {// 人民币金
//                temp = PrmsNewPricesActivity.this.getResources().getString(R.string.new_prms_rembg);
//
//            } else if (sourceCurrencyCode
//                    .equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORG)
//                    && targetCurrencyCode
//                    .equals(ConstantGloble.PRMS_CODE_DOLOR)) {// 美元金
//                temp = PrmsNewPricesActivity.this.getResources().getString(R.string.new_prms_dolorg);
//
//            } else if (sourceCurrencyCode
//                    .equals(ConstantGloble.PRMS_CURRENCYCODE_RMBS)
//                    && targetCurrencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {// 人民币银
//                temp = PrmsNewPricesActivity.this.getResources().getString(R.string.new_prms_rembs);
//            } else if (sourceCurrencyCode
//                    .equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORS)
//                    && targetCurrencyCode
//                    .equals(ConstantGloble.PRMS_CODE_DOLOR)) {// 美元银
//                temp = PrmsNewPricesActivity.this.getResources().getString(R.string.new_prms_dolors);
//            } else if (sourceCurrencyCode
//                    .equals(ConstantGloble.PRMS_CURRENCYCODE_RMBBOG)
//                    && targetCurrencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {// 人民币铂金
//                temp = PrmsNewPricesActivity.this.getResources().getString(R.string.new_prms_rmbboG);
//            } else if (sourceCurrencyCode
//                    .equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORBOG)
//                    && targetCurrencyCode
//                    .equals(ConstantGloble.PRMS_CODE_DOLOR)) {// 美元bo金
//                temp = PrmsNewPricesActivity.this.getResources().getString(R.string.new_prms_dolorboG);
//            } else if (sourceCurrencyCode
//                    .equals(ConstantGloble.PRMS_CURRENCYCODE_RMBBAG)
//                    && targetCurrencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {// 人民币巴金
//                temp = PrmsNewPricesActivity.this.getResources().getString(R.string.new_prms_rmbbaG);
//            } else if (sourceCurrencyCode
//                    .equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORBAG)
//                    && targetCurrencyCode
//                    .equals(ConstantGloble.PRMS_CODE_DOLOR)) {// 美元钯金
//                temp = PrmsNewPricesActivity.this.getResources().getString(R.string.new_prms_dolorbaG);
//            }
            temp = LocalData.code_Map.get(sourceCurrencyCode+targetCurrencyCode);

            holder.tv_currency.setText(temp);
            //今日涨跌幅
            String currPercentDiff = (String) currentItem.get("currPercentDiff");
            //今日涨跌值
            String currDiff = (String) currentItem.get("currDiff");
            oldtotalLoginDataList = new ArrayList<Map<String, Object>>();
            oldtotalLoginDataList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap().get("oldTotalLoginDataList");
            /** 根据涨跌标志，设置买入牌价、卖出牌价的背景图片 图片需重新修改*/
            int black = PrmsNewPricesActivity.this.getResources().getColor(R.color.lianlong_color_bdc7cb);
            int green =  PrmsNewPricesActivity.this.getResources().getColor(R.color.lianlong_color_00c192);
            int red = PrmsNewPricesActivity.this.getResources().getColor(R.color.lianlong_text_money_color_red);


            String code1 = sourceCurrencyCode +targetCurrencyCode;
            if (null != oldtotalLoginDataList && oldtotalLoginDataList.size() != 0) {
//                LogGloble.i("info","oldtotalLogoutDataList ==" + oldtotalLoginDataList.toString());
//                LogGloble.i("info","dataList ==" + dataList.toString());
                for (int j=0;j < oldtotalLoginDataList.size() ;j++){
                    Map<String, Object> oldMap = oldtotalLoginDataList.get(j);
                    String oldBuyRate = (String) oldMap.get(Prms.PRELOGIN_QUERY_BUYRATE);
                    String oldSellRate = (String) oldMap.get(Prms.PRELOGIN_QUERY_SELLRATE);
                    oldSellRate = parseStringPattern4(oldSellRate,formatNumber);
                    oldBuyRate = parseStringPattern4(oldBuyRate,formatNumber);
                    String sourceCurrencyCode2 = (String) oldMap.get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE); // 得到源货币的代码
                    String targetCurrencyCode2 = (String) oldMap.get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE);
                    String code2 = sourceCurrencyCode2+targetCurrencyCode2;
                    if (!StringUtil.isNull(code1) && !StringUtil.isNull(code2) && code1.equals(code2)){
                        if (!sellRate.equals(oldSellRate) || !buyRate.equals(oldBuyRate)) {//发生变化
                            if(null !=currDiff && null !=currPercentDiff){
                                if (currDiff.contains("-")) {//进行绿色高亮显示
                                    startAnimation(convertView, R.color.green);
                                } else if(currDiff.contains("+")) {//进行红色高亮显示
                                    startAnimation(convertView, R.color.red); //加载红色动画
                                }else{
                                    startAnimation(convertView,R.color.share_gray_color); //加载灰色动画
                                }
                            }
                        }
                    }
                }

            }
            //默认显示涨跌幅
            if(null !=currDiff && null !=currPercentDiff&&!"".equals(currPercentDiff) &&!"".equals(currDiff)){
                if (iscurrPercentDiffs) {//涨跌幅

//                    holder.tv_riseorfall.setText(parseStringPattern4(currPercentDiff,3));
                    currPercentDiff =  StringTools.parseStringPattern5(currPercentDiff);
                    holder.tv_riseorfall.setText(currPercentDiff);
                } else {
//                    holder.tv_riseorfall.setText(parseStringPattern4(currDiff,5));
//                    if(currDiff.contains("+")){
//                        holder.tv_riseorfall.setText("+"+parseStringPattern4(currDiff,5));
//                    }else if(currDiff.contains("-")){
//                        holder.tv_riseorfall.setText(parseStringPattern4(currDiff,5));
//                    }else{
//                        holder.tv_riseorfall.setText(parseStringPattern4(currDiff,5));
//                    }
//                    int number = StringTools.splitStringwith2pointnew(currDiff); // 判断小数位后面几位
//                    String currParse ="";
//                    if (number>=5){
//                        currParse = StringTools.parseStringPattern(currDiff,5);
//                    }else {
//                        currParse = StringTools.subZeroAndDot(currDiff);
//                    }
                    if(currDiff.contains("+")){
                        currDiff = StringTools.parseStringPattern(currDiff,5);
                        currDiff = StringTools.subZeroAndDot(currDiff);
                        currDiff = "+"+currDiff;
                    }else{
                        currDiff = StringTools.parseStringPattern(currDiff,5);
                        currDiff = StringTools.subZeroAndDot(currDiff);
                    }
                    holder.tv_riseorfall.setText(currDiff);
                }
            }else{
                holder.tv_riseorfall.setText("--");
            }


            if(null !=currDiff && null !=currPercentDiff&&!"".equals(currPercentDiff) &&!"".equals(currDiff)){
                if (holder.tv_riseorfall.getText().toString().contains("-")&&!holder.tv_riseorfall.getText().toString().contains("--")) {//绿色背景
                    holder.tv_riseorfall.setBackgroundResource(R.drawable.shape_llbt_round_green);

                } else if(holder.tv_riseorfall.getText().toString().contains("+")){//红色背景
                    holder.tv_riseorfall.setBackgroundResource(R.drawable.shape_llbt_round_red);
                }else{
                    holder.tv_riseorfall.setBackgroundResource(R.drawable.shape_llbt_round_gray);
                }
            }else{
                holder.tv_riseorfall.setBackgroundResource(R.drawable.shape_llbt_round_gray);
            }

            holder.tv_riseorfall.setOnClickListener(l);
            return convertView;
        }

        class ViewHoldler {
            LinearLayout lty_item;
            TextView tv_currency;
            NumberStyleTextView tv_sale;
            //            TextView tv_saledecimal;
            NumberStyleTextView tv_buy;
            //            TextView tv_buydecimal;
            TextView tv_riseorfall;
        }


    };

    View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (iscurrPercentDiffs) {//涨跌值
                iscurrPercentDiffs = false;
                tv_riseorfall.setText("涨跌值");

            } else {
                iscurrPercentDiffs = true;
                tv_riseorfall.setText("涨跌幅");
            }
            loginAdatper. notifyDataSetChanged();
//            loginAdatper.setSourceList(dataList,0);
        }
    };



    View.OnClickListener lout = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (iscurrPercentDiffs) {//涨跌值
                iscurrPercentDiffs = false;
                tv_riseorfall.setText("涨跌值");

            } else {
                iscurrPercentDiffs = true;
                tv_riseorfall.setText("涨跌幅");
            }
            preLoginAdapter.notifyDataSetChanged();
//            preLoginAdapter.setSourceList(preLoginDataList,0);
        }
    };
    /**
     * 涨跌幅 跌时动画显示
     */
    public void startAnimation(final View view, final int colorID) {
        //创建动画,这里的关键就是使用ArgbEvaluator, 后面2个参数就是 开始的颜色,和结束的颜色.
        final ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), this.getResources().getColor(colorID), Color.WHITE);
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {


            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int color = (int) animation.getAnimatedValue();//之后就可以得到动画的颜色了
                view.setBackgroundColor(color);//设置一下, 就可以看到效果.
            }
        });
        colorAnimator.setDuration(500);
        colorAnimator.start();
    }


    //不带豆号，保留两位就数。
    public static String parseStringPattern4(String text, int scale) {
        if(text != null && !"".equals(text) && !"null".equals(text)) {
            if(!text.contains(",") && !text.contains("，")) {
                if(text.matches("^.*[.].*[.].*$")) {
                    return text;
                } else {
                    String temp = "#######################0";
                    if(scale > 0) {
                        temp = temp + ".";
                    }

                    for(int e = 0; e < scale; ++e) {
                        temp = temp + "0";
                    }

                    try {
                        DecimalFormat var6 = new DecimalFormat(temp);
                        BigDecimal d = new BigDecimal(text);
                        return var6.format(d).toString();
                    } catch (Exception var5) {
                        return text;
                    }
                }
            } else {
                return text;
            }
        } else {
            return "-";
        }
    }

    /**
     * 登录之前行情适配器
     */
    private ICommonAdapter<Map<String, Object>> preloginCommonAdapter = new ICommonAdapter<Map<String, Object>>() {

        @Override
        public View getView(int arg0, Map<String, Object> currentItem, LayoutInflater inflater, View convertView,
                            ViewGroup viewGroup) {
            ViewHoldler holder = null;
            if (convertView == null) {
                holder = new ViewHoldler();
                convertView = inflater.inflate(R.layout.prms_main_item, lv_prms, false);
                holder.tv_currency = (TextView) convertView.findViewById(R.id.tv_currency);
                holder.tv_sale = (NumberStyleTextView) convertView.findViewById(R.id.tv_sale);
//                holder.tv_saledecimal = (TextView) convertView.findViewById(R.id.tv_saledecimal);
                holder.tv_buy = (NumberStyleTextView) convertView.findViewById(R.id.tv_buy);
//                holder.tv_buydecimal = (TextView) convertView.findViewById(R.id.tv_buydecimal);
                holder.tv_riseorfall = (TextView) convertView.findViewById(R.id.tv_riseorfall);


                convertView.setTag(holder);
            } else {
                holder = (ViewHoldler) convertView.getTag();
            }

            final String upDownFlag = (String) currentItem.get(Prms.PRELOGIN_QUERY_FLAG);
            String buyRate = String.valueOf(currentItem
                    .get(Prms.PRELOGIN_QUERY_BUYRATE));
            String sellRate = String.valueOf(currentItem
                    .get(Prms.PRELOGIN_QUERY_SELLRATE));
            final String sourceCurrencyCode = (String) currentItem
                    .get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE);
            final String targetCurrencyCode = (String) currentItem
                    .get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE);


            Integer formatNumber = (Integer)currentItem.get("formatNumber");
            sellRate = parseStringPattern4(sellRate,formatNumber);
            buyRate = parseStringPattern4(buyRate,formatNumber);
            holder.tv_buy.setNumberText(sellRate,2,"--");
            holder.tv_sale.setNumberText(buyRate,2,"--");
//            if(buyRate!=null && sellRate!=null){
//                if(!"null".equals(buyRate) && !"null".equals(sellRate)){
//                String[] buys = parseStringPattern4(buyRate, 4).split("\\.");
//                String num1 =buys[1].substring(0, 2);
//                String num2 = buys[1].substring(buys[1].length()-2, buys[1].length());
//                String[] sales = parseStringPattern4(sellRate, 4).split("\\.");
//                String salenum1 =buys[1].substring(0, 2);
//                String salenum2 = buys[1].substring(buys[1].length()-2, buys[1].length());
//                holder.tv_buy.setText(buys[0] + "."+num1);
//                holder.tv_buydecimal.setText(num2);
//                holder.tv_sale.setText(sales[0] + "."+salenum1);
//                holder.tv_saledecimal.setText(salenum2);



//            }else{
//                holder.tv_buy.setText("--");
//                holder.tv_buydecimal.setText("");
//                holder.tv_sale.setText("--");
//                holder.tv_saledecimal.setText("");
//            }
            String temp = "";
            final String sourceCurrency = (String) currentItem
                    .get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);

//            if (sourceCurrency
//                    .equals(ConstantGloble.PRMS_CURRENCYCODE_RMBG)
//                    && targetCurrencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {// 人民币金
//                temp = PrmsNewPricesActivity.this.getResources().getString(R.string.new_prms_rembg);
//
//            } else if (sourceCurrencyCode
//                    .equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORG)
//                    && targetCurrencyCode
//                    .equals(ConstantGloble.PRMS_CODE_DOLOR)) {// 美元金
//                temp = PrmsNewPricesActivity.this.getResources().getString(R.string.new_prms_dolorg);
//
//            } else if (sourceCurrencyCode
//                    .equals(ConstantGloble.PRMS_CURRENCYCODE_RMBS)
//                    && targetCurrencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {// 人民币银
//                temp = PrmsNewPricesActivity.this.getResources().getString(R.string.new_prms_rembs);
//            } else if (sourceCurrencyCode
//                    .equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORS)
//                    && targetCurrencyCode
//                    .equals(ConstantGloble.PRMS_CODE_DOLOR)) {// 美元银
//                temp = PrmsNewPricesActivity.this.getResources().getString(R.string.new_prms_dolors);
//            } else if (sourceCurrencyCode
//                    .equals(ConstantGloble.PRMS_CURRENCYCODE_RMBBOG)
//                    && targetCurrencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {// 人民币铂金
//                temp = PrmsNewPricesActivity.this.getResources().getString(R.string.new_prms_rmbboG);
//            } else if (sourceCurrencyCode
//                    .equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORBOG)
//                    && targetCurrencyCode
//                    .equals(ConstantGloble.PRMS_CODE_DOLOR)) {// 美元bo金
//                temp = PrmsNewPricesActivity.this.getResources().getString(R.string.new_prms_dolorboG);
//            } else if (sourceCurrencyCode
//                    .equals(ConstantGloble.PRMS_CURRENCYCODE_RMBBAG)
//                    && targetCurrencyCode.equals(ConstantGloble.PRMS_CODE_RMB)) {// 人民币巴金
//                temp = PrmsNewPricesActivity.this.getResources().getString(R.string.new_prms_rmbbaG);
//            } else if (sourceCurrencyCode
//                    .equals(ConstantGloble.PRMS_CURRENCYCODE_DOLORBAG)
//                    && targetCurrencyCode
//                    .equals(ConstantGloble.PRMS_CODE_DOLOR)) {// 美元钯金
//                temp = PrmsNewPricesActivity.this.getResources().getString(R.string.new_prms_dolorbaG);
//            }
            temp = LocalData.code_Map.get(sourceCurrencyCode+targetCurrencyCode);

            holder.tv_currency.setText(temp);

            //今日涨跌幅
            String currPercentDiff = (String) currentItem.get("currPercentDiff");
            //今日涨跌值
            String currDiff = (String) currentItem.get("currDiff");

            oldtotalLogoutDataList = new ArrayList<Map<String, Object>>();
            oldtotalLogoutDataList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap().get("oldTotalLogoutDataList");
            /** 根据涨跌标志，设置买入牌价、卖出牌价的背景图片 图片需重新修改*/
            int black = PrmsNewPricesActivity.this.getResources().getColor(R.color.lianlong_color_bdc7cb);
            int green =  PrmsNewPricesActivity.this.getResources().getColor(R.color.lianlong_color_00c192);
            int red = PrmsNewPricesActivity.this.getResources().getColor(R.color.lianlong_text_money_color_red);
//            LogGloble.i("info","oldtotalLogoutDataList ==" + oldtotalLogoutDataList.toString());
//            LogGloble.i("info","preLoginDataList ==" + preLoginDataList.toString());
            String code1 = sourceCurrencyCode +targetCurrencyCode;
            if (null != oldtotalLogoutDataList && oldtotalLogoutDataList.size() != 0) {
                for (int j=0;j < oldtotalLogoutDataList.size() ;j++){
                    Map<String, Object> oldMap = oldtotalLogoutDataList.get(j);
                    String oldBuyRate = (String) oldMap.get(Prms.PRELOGIN_QUERY_BUYRATE);
                    String oldSellRate = (String) oldMap.get(Prms.PRELOGIN_QUERY_SELLRATE);
                    oldSellRate = parseStringPattern4(oldSellRate,formatNumber);
                    oldBuyRate = parseStringPattern4(oldBuyRate,formatNumber);
                    String sourceCurrencyCode2 = (String) oldMap.get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE); // 得到源货币的代码
                    String targetCurrencyCode2 = (String) oldMap.get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE);
                    String code2 = sourceCurrencyCode2+targetCurrencyCode2;
                    if (!StringUtil.isNull(code1) && !StringUtil.isNull(code2) && code1.equals(code2)){
                        if (!sellRate.equals(oldSellRate) || !buyRate.equals(oldBuyRate)) {//发生变化
                            if(null !=currDiff && null !=currPercentDiff){
                                if (currDiff.contains("-")) {//进行绿色高亮显示
                                    startAnimation(convertView, R.color.share_green_color);
                                } else if(currDiff.contains("+")) {//进行红色高亮显示
                                    startAnimation(convertView, R.color.share_red_color); //加载红色动画
                                }else{
                                    startAnimation(convertView,R.color.share_gray_color); //加载灰色动画
                                }
                            }
                        }
                    }
                }

            }

            //默认显示涨跌幅
            if(null !=currDiff && null !=currPercentDiff&&!"".equals(currPercentDiff) &&!"".equals(currDiff)){
                if (iscurrPercentDiffs) {//涨跌幅
                    currPercentDiff =  StringTools.parseStringPattern5(currPercentDiff);
//                    holder.tv_riseorfall.setText(parseStringPattern4(currPercentDiff,3));
                    holder.tv_riseorfall.setText(currPercentDiff);
                } else {
//                    holder.tv_riseorfall.setText(parseStringPattern4(currDiff,5));
//                    if(currDiff.contains("+")){
//                        holder.tv_riseorfall.setText("+"+parseStringPattern4(currDiff,5));
//                    }else if(currDiff.contains("-")){
//                        holder.tv_riseorfall.setText(parseStringPattern4(currDiff,5));
//                    }else{
//                        holder.tv_riseorfall.setText(parseStringPattern4(currDiff,5));
//                    }
//                    int number = StringTools.splitStringwith2pointnew(currDiff); // 判断小数位后面几位
//                    String currParse ="";
//                    if (number>=5){
//                        currParse = StringTools.parseStringPattern(currDiff,5);
//                    }else {
//                        currParse = StringTools.subZeroAndDot(currDiff);
//                    }
                    if(currDiff.contains("+")){
                        currDiff = StringTools.parseStringPattern(currDiff,5);
                        currDiff = StringTools.subZeroAndDot(currDiff);
                        currDiff = "+"+currDiff;
                    }else{
                        currDiff = StringTools.parseStringPattern(currDiff,5);
                        currDiff = StringTools.subZeroAndDot(currDiff);
                    }
                    holder.tv_riseorfall.setText(currDiff);
                }
            }else{
                holder.tv_riseorfall.setText("--");
            }


            if(null !=currDiff && null !=currPercentDiff&&!"".equals(currPercentDiff) &&!"".equals(currDiff)){
                if (holder.tv_riseorfall.getText().toString().contains("-")&&!holder.tv_riseorfall.getText().toString().contains("--")) {//绿色背景
                    holder.tv_riseorfall.setBackgroundResource(R.drawable.shape_llbt_round_green);

                } else if(holder.tv_riseorfall.getText().toString().contains("+")){//红色背景
                    holder.tv_riseorfall.setBackgroundResource(R.drawable.shape_llbt_round_red);
                }else{
                    holder.tv_riseorfall.setBackgroundResource(R.drawable.shape_llbt_round_gray);
                }
            }else{
                holder.tv_riseorfall.setBackgroundResource(R.drawable.shape_llbt_round_gray);
            }






            holder.tv_riseorfall.setOnClickListener(lout);

            return convertView;
        }

        class ViewHoldler {
            TextView tv_currency;
            NumberStyleTextView tv_sale;
            //            TextView tv_saledecimal;
            NumberStyleTextView tv_buy;
            //            TextView tv_buydecimal;
            TextView tv_riseorfall;
        }
    };

    String ccygrp = "";

    /**多笔行情查询*/
    protected  void requestQueryMultipleQuotation(){
        {
            String cardType = "G";
            String cardClass = "R";
            QueryMultipleQuotationRequestParams queryMultiple = new QueryMultipleQuotationRequestParams(cardType, cardClass, pSort);
            HttpHelp h = HttpHelp.getInstance();
            h.postHttpFromSF(PrmsNewPricesActivity.this, queryMultiple);
            h.setHttpErrorCallBack(new IHttpErrorCallBack() {
                @Override
                public boolean onError(String exceptionMessage, Object extendParams) {
                    MessageDialog.closeDialog(); //关闭通讯框
//                    if(!isSort){
//                        if(BaseDroidApp.getInstanse().isLogin()){
////                            保存本次所有请求回来的数据
//                            BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", dataList);
//                            if (loginAdatper == null) {
//                                loginAdatper = new CommonAdapter<Map<String, Object>>(PrmsNewPricesActivity.this, dataList, iCommonAdapter);
//                                lv_prms.setAdapter(loginAdatper);
//
//
//                            } else {
////                                loginAdatper.notifyDataSetChanged();
//                                loginAdatper.setSourceList(dataList,0);
//                            }
//                        }else{
//                            //                   保存本次所有请求回来的数据
//                            BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLogoutDataList", preLoginDataList);
//                            if (preLoginAdapter == null) {
//                                preLoginAdapter = new CommonAdapter<Map<String, Object>>(PrmsNewPricesActivity.this, preLoginDataList, preloginCommonAdapter);
//                                lv_prms.setAdapter(preLoginAdapter);
//
//                            } else {
////                                preLoginAdapter.notifyDataSetChanged();
//                                preLoginAdapter.setSourceList(preLoginDataList,0);
//                            }
//                        }
//                    }
                    return true;
                }
            });

            h.setOkHttpErrorCode(new IOkHttpErrorCode() {
                @Override
                public boolean handlerErrorCode(BaseResponseData responseData, Object extendParams) {
                    MessageDialog.closeDialog(); //关闭通讯框
//                        if(!isSort){
//                            if(BaseDroidApp.getInstanse().isLogin()){
////                            保存本次所有请求回来的数据
//                                BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", dataList);
//                                if (loginAdatper == null) {
//                                    loginAdatper = new CommonAdapter<Map<String, Object>>(PrmsNewPricesActivity.this, dataList, iCommonAdapter);
//                                    lv_prms.setAdapter(loginAdatper);
//
//
//                                } else {
////                                loginAdatper.notifyDataSetChanged();
//                                    loginAdatper.setSourceList(dataList,0);
//                                }
//                            }else{
//                                //                   保存本次所有请求回来的数据
//                                BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLogoutDataList", preLoginDataList);
//                                if (preLoginAdapter == null) {
//                                    preLoginAdapter = new CommonAdapter<Map<String, Object>>(PrmsNewPricesActivity.this, preLoginDataList, preloginCommonAdapter);
//                                    lv_prms.setAdapter(preLoginAdapter);
//
//                                } else {
////                                preLoginAdapter.notifyDataSetChanged();
//                                    preLoginAdapter.setSourceList(preLoginDataList,0);
//                                }
//                            }
//                        }

                    return true;
                }
            });


            h.setHttpResponseCallBack(new IHttpResponseCallBack() {
                @Override
                public boolean responseCallBack(String response, Object extendParams) {
                    QueryMultipleQuotationResponseData data = GsonTools.fromJson(response, QueryMultipleQuotationResponseData.class);
                    //wuhan 要改
                    List<QueryMultipleQuotationResult.QueryMultipleQuotationItem> list = data.getBody().getItem();


                    if (StringUtil.isNullOrEmpty(list)) {
                        BaseDroidApp.getInstanse().showMessageDialog(
                                getString(R.string.prms_no_price),
                                new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        PrmsNewPricesActivity.this.finish();
                                    }
                                });
                    }
                    commDataList.clear();
                    for (int i = 0; i < list.size(); i++) {
                        QueryMultipleQuotationResult.QueryMultipleQuotationItem item = list.get(i);
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("ccygrpNm", item.ccygrpNm);
                        map.put("buyRate", item.buyPrice);
                        map.put("sellRate", item.sellPrice);
                        map.put("currPercentDiff", item.currPercentDiff);
                        map.put("currDiff", item.currDiff);
                        map.put("priceTime", item.priceTime);
                        map.put("tranCode", item.tranCode);
                        map.put("sortPriority", item.sortPriority);
                        map.put("sourceCurrencyCode", item.sourceCurrencyCode);
                        map.put("targetCurrencyCode", item.targetCurrencyCode);
                        commDataList.add(map);
                    }

//                    isLogin();

                    if(BaseDroidApp.getInstanse().isLogin()){
                        getSortForInvest(dataList);
//                        ListUtils.sortForInvest(dataList, new ListUtils.ISynchronousList<Map<String, Object>, KeyAndValueItem>() {
//                            @Override
//                            public boolean doSomeThing(Map<String, Object> stringObjectMap, KeyAndValueItem keyAndValueItem) {
//                                String sourceCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE);
//                                String targetCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE);
//                                if(keyAndValueItem.getKey().equals(sourceCurrencyCode) && keyAndValueItem.getValue().equals(targetCurrencyCode)){
//                                    stringObjectMap.put("formatNumber",keyAndValueItem.getParam());
//                                    return true;
//                                }
//                                return false;
//                            }
//                        });
                        if(null != commDataList &&commDataList.size()!=0&&"".equals(pSort)){
                            ListUtils.synchronousList(dataList, commDataList, new ListUtils.ISynchronousList<Map<String, Object>, Map<String, Object>>() {


                                @Override
                                public boolean doSomeThing(Map<String, Object> stringObjectMap, Map<String, Object> stringObject) {
                                    String sourceCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_SOURCECODE_RES);
                                    String targetCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_TARGETCODE_RES);
                                    String sourceCurrencyCodeNew = (String) stringObject.get("sourceCurrencyCode");
                                    String targetCurrencyCodeNew =(String) stringObject.get("targetCurrencyCode");
                                    String currencyCode = sourceCurrencyCode+targetCurrencyCode;
                                    String currencyCodeNew = sourceCurrencyCodeNew+targetCurrencyCodeNew;
                                    if(currencyCode.equals(currencyCodeNew)) {
                                        stringObjectMap.put("currPercentDiff",(String) stringObject.get("currPercentDiff") ); // 今日涨跌幅 小数点后有效位数为3位
                                        stringObjectMap.put("currDiff",(String) stringObject.get("currDiff")); // 今日涨跌值
                                        return true;
                                    }
                                    return false;
                                }
                            });
                        }else if(null != commDataList && commDataList.size()!=0&&!"".equals(pSort)){
                            ListUtils.synchronousList(commDataList, dataList, new ListUtils.ISynchronousList<Map<String, Object>, Map<String, Object>>() {


                                @Override
                                public boolean doSomeThing(Map<String, Object> stringObjectMap, Map<String, Object> stringObject) {
                                    String sourceCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_SOURCECODE_RES);
                                    String targetCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_TARGETCODE_RES);
                                    String sourceCurrencyCodeNew = (String) stringObject.get("sourceCurrencyCode");
                                    String targetCurrencyCodeNew =(String) stringObject.get("targetCurrencyCode");
                                    String currencyCode = sourceCurrencyCode+targetCurrencyCode;
                                    String currencyCodeNew = sourceCurrencyCodeNew+targetCurrencyCodeNew;
                                    if(currencyCode.equals(currencyCodeNew)) {
                                        stringObjectMap.put("buyRate",(String) stringObject.get("buyRate") );
                                        stringObjectMap.put("sellRate",(String) stringObject.get("sellRate") );
                                        stringObjectMap.put("ibkNum",(String) stringObject.get("ibkNum") );
                                        stringObjectMap.put("type",(String) stringObject.get("type") );
                                        stringObjectMap.put("rate",(String) stringObject.get("rate") );
                                        stringObjectMap.put("updateDate",(String) stringObject.get("updateDate") );
                                        stringObjectMap.put("buyNoteRate",(String) stringObject.get("buyNoteRate") );
                                        stringObjectMap.put("sellNoteRate",(String) stringObject.get("sellNoteRate") );
                                        stringObjectMap.put("state",(String) stringObject.get("state") );
                                        stringObjectMap.put("formatNumber",stringObject.get("formatNumber"));
//                                        stringObjectMap.put("currPercentDiff",(String) stringObject.get("currPercentDiff") ); // 今日涨跌幅 小数点后有效位数为3位
//                                        stringObjectMap.put("currDiff",(String) stringObject.get("currDiff")); // 今日涨跌值
                                        return true;
                                    }
                                    return false;
                                }
                            });
                        }


                        if("".equals(pSort)){
                            getSortForInvest(dataList);
//                            ListUtils.sortForInvest(dataList, new ListUtils.ISynchronousList<Map<String, Object>, KeyAndValueItem>() {
//                                @Override
//                                public boolean doSomeThing(Map<String, Object> stringObjectMap, KeyAndValueItem keyAndValueItem) {
//                                    String sourceCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE);
//                                    String targetCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE);
//                                    if(keyAndValueItem.getKey().equals(sourceCurrencyCode) && keyAndValueItem.getValue().equals(targetCurrencyCode)){
//                                        stringObjectMap.put("formatNumber",keyAndValueItem.getParam());
//                                        return true;
//                                    }
//                                    return false;
//                                }
//                            });
                        }



                        if(null != commDataList && commDataList.size()!=0&&"".equals(pSort)){
                            //保存本次所有请求回来的数据
                            BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", loginAdatper.getSourceList());

                            if (loginAdatper == null) {
                                loginAdatper = new CommonAdapter<Map<String, Object>>(PrmsNewPricesActivity.this, dataList, iCommonAdapter);
                                lv_prms.setAdapter(loginAdatper);


                            } else {
                                loginAdatper.setSourceList(dataList,0);
//                                loginAdatper.notifyDataSetChanged();

                            }

//                            //保存本次所有请求回来的数据
//                            BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", dataList);
                        }else if(null != commDataList && commDataList.size()!=0&&!"".equals(pSort)){
                            //保存本次所有请求回来的数据
                            BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", loginAdatper.getSourceList());
                            dataList.clear();
                            dataList= commDataList;
                            if (loginAdatper == null) {
                                loginAdatper = new CommonAdapter<Map<String, Object>>(PrmsNewPricesActivity.this, commDataList, iCommonAdapter);
                                lv_prms.setAdapter(loginAdatper);


                            } else {
//                                loginAdatper.notifyDataSetChanged();
                                loginAdatper.setSourceList(commDataList,0);

                            }
                        }
////                        保存本次所有请求回来的数据
//                        BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", commDataList);

                    }else {
                        getSortForInvest(preLoginDataList);
//                        ListUtils.sortForInvest(preLoginDataList, new ListUtils.ISynchronousList<Map<String, Object>, KeyAndValueItem>() {
//                            @Override
//                            public boolean doSomeThing(Map<String, Object> stringObjectMap, KeyAndValueItem keyAndValueItem) {
//                                String sourceCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE);
//                                String targetCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE);
//                                if(keyAndValueItem.getKey().equals(sourceCurrencyCode) && keyAndValueItem.getValue().equals(targetCurrencyCode)){
//                                    stringObjectMap.put("formatNumber",keyAndValueItem.getParam());
//                                    return true;
//                                }
//                                return false;
//                            }
//                        });




                        if(null != commDataList && commDataList.size()!=0&&"".equals(pSort)){
                            ListUtils.synchronousList(preLoginDataList, commDataList, new ListUtils.ISynchronousList<Map<String, Object>, Map<String, Object>>() {


                                @Override
                                public boolean doSomeThing(Map<String, Object> stringObjectMap, Map<String, Object> stringObject) {
                                    String sourceCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_SOURCECODE_RES);
                                    String targetCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_TARGETCODE_RES);
                                    String sourceCurrencyCodeNew = (String) stringObject.get("sourceCurrencyCode");
                                    String targetCurrencyCodeNew =(String) stringObject.get("targetCurrencyCode");
                                    String currencyCode = sourceCurrencyCode+targetCurrencyCode;
                                    String currencyCodeNew = sourceCurrencyCodeNew+targetCurrencyCodeNew;
                                    if(currencyCode.equals(currencyCodeNew)) {
                                        stringObjectMap.put("currPercentDiff",(String) stringObject.get("currPercentDiff") ); // 今日涨跌幅 小数点后有效位数为3位
                                        stringObjectMap.put("currDiff",(String) stringObject.get("currDiff")); // 今日涨跌值
                                        return true;
                                    }
                                    return false;
                                }
                            });
//                            LogGloble.i("info","preLoginDataList ==" + preLoginDataList.toString());
                        }else if(null != commDataList && commDataList.size()!=0&&!"".equals(pSort)){
                            ListUtils.synchronousList(commDataList, preLoginDataList, new ListUtils.ISynchronousList<Map<String, Object>, Map<String, Object>>() {


                                @Override
                                public boolean doSomeThing(Map<String, Object> stringObjectMap, Map<String, Object> stringObject) {
                                    String sourceCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_SOURCECODE_RES);
                                    String targetCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_TARGETCODE_RES);
                                    String sourceCurrencyCodeNew = (String) stringObject.get("sourceCurrencyCode");
                                    String targetCurrencyCodeNew =(String) stringObject.get("targetCurrencyCode");
                                    String currencyCode = sourceCurrencyCode+targetCurrencyCode;
                                    String currencyCodeNew = sourceCurrencyCodeNew+targetCurrencyCodeNew;
                                    if(currencyCode.equals(currencyCodeNew)) {
                                        stringObjectMap.put("buyRate",(String) stringObject.get("buyRate") );
                                        stringObjectMap.put("sellRate",(String) stringObject.get("sellRate") );
                                        stringObjectMap.put("ibkNum",(String) stringObject.get("ibkNum") );
                                        stringObjectMap.put("type",(String) stringObject.get("type") );
                                        stringObjectMap.put("rate",(String) stringObject.get("rate") );
                                        stringObjectMap.put("updateDate",(String) stringObject.get("updateDate") );
                                        stringObjectMap.put("buyNoteRate",(String) stringObject.get("buyNoteRate") );
                                        stringObjectMap.put("sellNoteRate",(String) stringObject.get("sellNoteRate") );
                                        stringObjectMap.put("state",(String) stringObject.get("state") );
                                        stringObjectMap.put("formatNumber",stringObject.get("formatNumber"));
//                                        stringObjectMap.put("currPercentDiff",(String) stringObject.get("currPercentDiff") ); // 今日涨跌幅 小数点后有效位数为3位
//                                        stringObjectMap.put("currDiff",(String) stringObject.get("currDiff")); // 今日涨跌值
                                        return true;
                                    }
                                    return false;
                                }
                            });
                        }

                        if("".equals(pSort)){
                            getSortForInvest(preLoginDataList);
//                            ListUtils.sortForInvest(preLoginDataList, new ListUtils.ISynchronousList<Map<String, Object>, KeyAndValueItem>() {
//                                @Override
//                                public boolean doSomeThing(Map<String, Object> stringObjectMap, KeyAndValueItem keyAndValueItem) {
//                                    String sourceCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE);
//                                    String targetCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE);
//                                    if(keyAndValueItem.getKey().equals(sourceCurrencyCode) && keyAndValueItem.getValue().equals(targetCurrencyCode)){
//                                        stringObjectMap.put("formatNumber",keyAndValueItem.getParam());
//                                        return true;
//                                    }
//                                    return false;
//                                }
//                            });
//                            LogGloble.i("info","sort   preLoginDataList ==" + preLoginDataList.toString());
                        }
                        if(null != commDataList && commDataList.size()!=0&&"".equals(pSort)){

                            BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLogoutDataList", preLoginAdapter.getSourceList());

                            if (preLoginAdapter == null) {
                                preLoginAdapter = new CommonAdapter<Map<String, Object>>(PrmsNewPricesActivity.this, preLoginDataList, preloginCommonAdapter);
                                lv_prms.setAdapter(preLoginAdapter);

                            } else {
//                                preLoginAdapter.notifyDataSetChanged();
                                preLoginAdapter.setSourceList(preLoginDataList,0);
//                                LogGloble.i("info","Adapter   preLoginDataList ==" + preLoginDataList.toString());
                            }

                            //                   保存本次所有请求回来的数据
//                            BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLogoutDataList", preLoginDataList);
//                            BaseDroidApp.getInstanse().getBizDataMap().get("oldTotalLogoutDataList");
//                            LogGloble.i("info","prelogindatalist ===" +BaseDroidApp.getInstanse().getBizDataMap().get("oldTotalLogoutDataList").toString() );
                        }else if(null != commDataList && commDataList.size()!=0&&!"".equals(pSort)){
                            BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLogoutDataList", preLoginAdapter.getSourceList());
                            preLoginDataList.clear();
                            preLoginDataList= commDataList;
                            if (preLoginAdapter == null) {
                                preLoginAdapter = new CommonAdapter<Map<String, Object>>(PrmsNewPricesActivity.this, commDataList, preloginCommonAdapter);
                                lv_prms.setAdapter(preLoginAdapter);

                            } else {
//                                preLoginAdapter.notifyDataSetChanged();
                                preLoginAdapter.setSourceList(commDataList,0);

                            }

                            //
//                            BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLogoutDataList", commDataList);
                        }
                    }


                    return false;
                }
            });
//            pull_down_layout.loadmoreCompleted(RefreshDataStatus.Successed);
        }
    }



    TimerRefreshTools mTimerRefreshToolsMultiple = new TimerRefreshTools(7000, new TimerRefreshTools.ITimerRefreshListener() {
        @Override
        public void onRefresh() {
            String cardType = "G";
            String cardClass = "R";
            QueryMultipleQuotationRequestParams queryMultiple = new QueryMultipleQuotationRequestParams(cardType, cardClass, pSort);
            HttpHelp h = HttpHelp.getInstance();
            h.postHttpFromSF(PrmsNewPricesActivity.this, queryMultiple);
            h.setHttpErrorCallBack(new IHttpErrorCallBack() {
                @Override
                public boolean onError(String exceptionMessage, Object extendParams) {
                    MessageDialog.closeDialog(); //关闭通讯框
//                    if(!isSort){
//                        if(BaseDroidApp.getInstanse().isLogin()){
////                            保存本次所有请求回来的数据
//                            BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", dataList);
//                            if (loginAdatper == null) {
//                                loginAdatper = new CommonAdapter<Map<String, Object>>(PrmsNewPricesActivity.this, dataList, iCommonAdapter);
//                                lv_prms.setAdapter(loginAdatper);
//
//
//                            } else {
////                                loginAdatper.notifyDataSetChanged();
//                                loginAdatper.setSourceList(dataList,0);
//                            }
//                        }else{
//                            //                   保存本次所有请求回来的数据
//                            BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLogoutDataList", preLoginDataList);
//                            if (preLoginAdapter == null) {
//                                preLoginAdapter = new CommonAdapter<Map<String, Object>>(PrmsNewPricesActivity.this, preLoginDataList, preloginCommonAdapter);
//                                lv_prms.setAdapter(preLoginAdapter);
//
//                            } else {
////                                preLoginAdapter.notifyDataSetChanged();
//                                preLoginAdapter.setSourceList(preLoginDataList,0);
//                            }
//                        }
//                    }
                    return true;
                }
            });

            h.setOkHttpErrorCode(new IOkHttpErrorCode() {
                @Override
                public boolean handlerErrorCode(BaseResponseData responseData, Object extendParams) {
                    MessageDialog.closeDialog(); //关闭通讯框
//                        if(!isSort){
//                            if(BaseDroidApp.getInstanse().isLogin()){
////                            保存本次所有请求回来的数据
//                                BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", dataList);
//                                if (loginAdatper == null) {
//                                    loginAdatper = new CommonAdapter<Map<String, Object>>(PrmsNewPricesActivity.this, dataList, iCommonAdapter);
//                                    lv_prms.setAdapter(loginAdatper);
//
//
//                                } else {
////                                loginAdatper.notifyDataSetChanged();
//                                    loginAdatper.setSourceList(dataList,0);
//                                }
//                            }else{
//                                //                   保存本次所有请求回来的数据
//                                BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLogoutDataList", preLoginDataList);
//                                if (preLoginAdapter == null) {
//                                    preLoginAdapter = new CommonAdapter<Map<String, Object>>(PrmsNewPricesActivity.this, preLoginDataList, preloginCommonAdapter);
//                                    lv_prms.setAdapter(preLoginAdapter);
//
//                                } else {
////                                preLoginAdapter.notifyDataSetChanged();
//                                    preLoginAdapter.setSourceList(preLoginDataList,0);
//                                }
//                            }
//                        }

                    return true;
                }
            });


            h.setHttpResponseCallBack(new IHttpResponseCallBack() {
                @Override
                public boolean responseCallBack(String response, Object extendParams) {
                    QueryMultipleQuotationResponseData data = GsonTools.fromJson(response, QueryMultipleQuotationResponseData.class);
                    //wuhan 要改
                    List<QueryMultipleQuotationResult.QueryMultipleQuotationItem> list = data.getBody().getItem();


                    if (StringUtil.isNullOrEmpty(list)) {
                        BaseDroidApp.getInstanse().showMessageDialog(
                                getString(R.string.prms_no_price),
                                new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        PrmsNewPricesActivity.this.finish();
                                    }
                                });
                    }
                    commDataList.clear();
                    for (int i = 0; i < list.size(); i++) {
                        QueryMultipleQuotationResult.QueryMultipleQuotationItem item = list.get(i);
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("ccygrpNm", item.ccygrpNm);
                        map.put("buyRate", item.buyPrice);
                        map.put("sellRate", item.sellPrice);
                        map.put("currPercentDiff", item.currPercentDiff);
                        map.put("currDiff", item.currDiff);
                        map.put("priceTime", item.priceTime);
                        map.put("tranCode", item.tranCode);
                        map.put("sortPriority", item.sortPriority);
                        map.put("sourceCurrencyCode", item.sourceCurrencyCode);
                        map.put("targetCurrencyCode", item.targetCurrencyCode);
                        commDataList.add(map);
                    }

//                    isLogin();

                    if(BaseDroidApp.getInstanse().isLogin()){
                        getSortForInvest(dataList);
//                        ListUtils.sortForInvest(dataList, new ListUtils.ISynchronousList<Map<String, Object>, KeyAndValueItem>() {
//                            @Override
//                            public boolean doSomeThing(Map<String, Object> stringObjectMap, KeyAndValueItem keyAndValueItem) {
//                                String sourceCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE);
//                                String targetCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE);
//                                if(keyAndValueItem.getKey().equals(sourceCurrencyCode) && keyAndValueItem.getValue().equals(targetCurrencyCode)){
//                                    stringObjectMap.put("formatNumber",keyAndValueItem.getParam());
//                                    return true;
//                                }
//                                return false;
//                            }
//                        });
                        if(null != commDataList &&commDataList.size()!=0&&"".equals(pSort)){
                            ListUtils.synchronousList(dataList, commDataList, new ListUtils.ISynchronousList<Map<String, Object>, Map<String, Object>>() {


                                @Override
                                public boolean doSomeThing(Map<String, Object> stringObjectMap, Map<String, Object> stringObject) {
                                    String sourceCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_SOURCECODE_RES);
                                    String targetCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_TARGETCODE_RES);
                                    String sourceCurrencyCodeNew = (String) stringObject.get("sourceCurrencyCode");
                                    String targetCurrencyCodeNew =(String) stringObject.get("targetCurrencyCode");
                                    String currencyCode = sourceCurrencyCode+targetCurrencyCode;
                                    String currencyCodeNew = sourceCurrencyCodeNew+targetCurrencyCodeNew;
                                    if(currencyCode.equals(currencyCodeNew)) {
                                        stringObjectMap.put("currPercentDiff",(String) stringObject.get("currPercentDiff") ); // 今日涨跌幅 小数点后有效位数为3位
                                        stringObjectMap.put("currDiff",(String) stringObject.get("currDiff")); // 今日涨跌值
                                        return true;
                                    }
                                    return false;
                                }
                            });
                        }else if(null != commDataList && commDataList.size()!=0&&!"".equals(pSort)){
                            ListUtils.synchronousList(commDataList, dataList, new ListUtils.ISynchronousList<Map<String, Object>, Map<String, Object>>() {


                                @Override
                                public boolean doSomeThing(Map<String, Object> stringObjectMap, Map<String, Object> stringObject) {
                                    String sourceCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_SOURCECODE_RES);
                                    String targetCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_TARGETCODE_RES);
                                    String sourceCurrencyCodeNew = (String) stringObject.get("sourceCurrencyCode");
                                    String targetCurrencyCodeNew =(String) stringObject.get("targetCurrencyCode");
                                    String currencyCode = sourceCurrencyCode+targetCurrencyCode;
                                    String currencyCodeNew = sourceCurrencyCodeNew+targetCurrencyCodeNew;
                                    if(currencyCode.equals(currencyCodeNew)) {
                                        stringObjectMap.put("buyRate",(String) stringObject.get("buyRate") );
                                        stringObjectMap.put("sellRate",(String) stringObject.get("sellRate") );
                                        stringObjectMap.put("ibkNum",(String) stringObject.get("ibkNum") );
                                        stringObjectMap.put("type",(String) stringObject.get("type") );
                                        stringObjectMap.put("rate",(String) stringObject.get("rate") );
                                        stringObjectMap.put("updateDate",(String) stringObject.get("updateDate") );
                                        stringObjectMap.put("buyNoteRate",(String) stringObject.get("buyNoteRate") );
                                        stringObjectMap.put("sellNoteRate",(String) stringObject.get("sellNoteRate") );
                                        stringObjectMap.put("state",(String) stringObject.get("state") );
                                        stringObjectMap.put("formatNumber",stringObject.get("formatNumber"));
//                                        stringObjectMap.put("currPercentDiff",(String) stringObject.get("currPercentDiff") ); // 今日涨跌幅 小数点后有效位数为3位
//                                        stringObjectMap.put("currDiff",(String) stringObject.get("currDiff")); // 今日涨跌值
                                        return true;
                                    }
                                    return false;
                                }
                            });
                        }


                        if("".equals(pSort)){
                            getSortForInvest(dataList);
//                            ListUtils.sortForInvest(dataList, new ListUtils.ISynchronousList<Map<String, Object>, KeyAndValueItem>() {
//                                @Override
//                                public boolean doSomeThing(Map<String, Object> stringObjectMap, KeyAndValueItem keyAndValueItem) {
//                                    String sourceCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE);
//                                    String targetCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE);
//                                    if(keyAndValueItem.getKey().equals(sourceCurrencyCode) && keyAndValueItem.getValue().equals(targetCurrencyCode)){
//                                        stringObjectMap.put("formatNumber",keyAndValueItem.getParam());
//                                        return true;
//                                    }
//                                    return false;
//                                }
//                            });
                        }



                        if(null != commDataList && commDataList.size()!=0&&"".equals(pSort)){
                            //保存本次所有请求回来的数据
                            BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", loginAdatper.getSourceList());

                            if (loginAdatper == null) {
                                loginAdatper = new CommonAdapter<Map<String, Object>>(PrmsNewPricesActivity.this, dataList, iCommonAdapter);
                                lv_prms.setAdapter(loginAdatper);


                            } else {
                                loginAdatper.setSourceList(dataList,0);
//                                loginAdatper.notifyDataSetChanged();

                            }

//                            //保存本次所有请求回来的数据
//                            BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", dataList);
                        }else if(null != commDataList && commDataList.size()!=0&&!"".equals(pSort)){
                            //保存本次所有请求回来的数据
                            BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", loginAdatper.getSourceList());
                            dataList.clear();
                            dataList= commDataList;
                            if (loginAdatper == null) {
                                loginAdatper = new CommonAdapter<Map<String, Object>>(PrmsNewPricesActivity.this, commDataList, iCommonAdapter);
                                lv_prms.setAdapter(loginAdatper);


                            } else {
//                                loginAdatper.notifyDataSetChanged();
                                loginAdatper.setSourceList(commDataList,0);

                            }
                        }
////                        保存本次所有请求回来的数据
//                        BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLoginDataList", commDataList);

                    }else {
                        getSortForInvest(preLoginDataList);
//                        ListUtils.sortForInvest(preLoginDataList, new ListUtils.ISynchronousList<Map<String, Object>, KeyAndValueItem>() {
//                            @Override
//                            public boolean doSomeThing(Map<String, Object> stringObjectMap, KeyAndValueItem keyAndValueItem) {
//                                String sourceCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE);
//                                String targetCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE);
//                                if(keyAndValueItem.getKey().equals(sourceCurrencyCode) && keyAndValueItem.getValue().equals(targetCurrencyCode)){
//                                    stringObjectMap.put("formatNumber",keyAndValueItem.getParam());
//                                    return true;
//                                }
//                                return false;
//                            }
//                        });




                        if(null != commDataList && commDataList.size()!=0&&"".equals(pSort)){
                            ListUtils.synchronousList(preLoginDataList, commDataList, new ListUtils.ISynchronousList<Map<String, Object>, Map<String, Object>>() {


                                @Override
                                public boolean doSomeThing(Map<String, Object> stringObjectMap, Map<String, Object> stringObject) {
                                    String sourceCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_SOURCECODE_RES);
                                    String targetCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_TARGETCODE_RES);
                                    String sourceCurrencyCodeNew = (String) stringObject.get("sourceCurrencyCode");
                                    String targetCurrencyCodeNew =(String) stringObject.get("targetCurrencyCode");
                                    String currencyCode = sourceCurrencyCode+targetCurrencyCode;
                                    String currencyCodeNew = sourceCurrencyCodeNew+targetCurrencyCodeNew;
                                    if(currencyCode.equals(currencyCodeNew)) {
                                        stringObjectMap.put("currPercentDiff",(String) stringObject.get("currPercentDiff") ); // 今日涨跌幅 小数点后有效位数为3位
                                        stringObjectMap.put("currDiff",(String) stringObject.get("currDiff")); // 今日涨跌值
                                        return true;
                                    }
                                    return false;
                                }
                            });
//                            LogGloble.i("info","preLoginDataList ==" + preLoginDataList.toString());
                        }else if(null != commDataList && commDataList.size()!=0&&!"".equals(pSort)){
                            ListUtils.synchronousList(commDataList, preLoginDataList, new ListUtils.ISynchronousList<Map<String, Object>, Map<String, Object>>() {


                                @Override
                                public boolean doSomeThing(Map<String, Object> stringObjectMap, Map<String, Object> stringObject) {
                                    String sourceCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_SOURCECODE_RES);
                                    String targetCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_TARGETCODE_RES);
                                    String sourceCurrencyCodeNew = (String) stringObject.get("sourceCurrencyCode");
                                    String targetCurrencyCodeNew =(String) stringObject.get("targetCurrencyCode");
                                    String currencyCode = sourceCurrencyCode+targetCurrencyCode;
                                    String currencyCodeNew = sourceCurrencyCodeNew+targetCurrencyCodeNew;
                                    if(currencyCode.equals(currencyCodeNew)) {
                                        stringObjectMap.put("buyRate",(String) stringObject.get("buyRate") );
                                        stringObjectMap.put("sellRate",(String) stringObject.get("sellRate") );
                                        stringObjectMap.put("ibkNum",(String) stringObject.get("ibkNum") );
                                        stringObjectMap.put("type",(String) stringObject.get("type") );
                                        stringObjectMap.put("rate",(String) stringObject.get("rate") );
                                        stringObjectMap.put("updateDate",(String) stringObject.get("updateDate") );
                                        stringObjectMap.put("buyNoteRate",(String) stringObject.get("buyNoteRate") );
                                        stringObjectMap.put("sellNoteRate",(String) stringObject.get("sellNoteRate") );
                                        stringObjectMap.put("state",(String) stringObject.get("state") );
                                        stringObjectMap.put("formatNumber",stringObject.get("formatNumber"));
//                                        stringObjectMap.put("currPercentDiff",(String) stringObject.get("currPercentDiff") ); // 今日涨跌幅 小数点后有效位数为3位
//                                        stringObjectMap.put("currDiff",(String) stringObject.get("currDiff")); // 今日涨跌值
                                        return true;
                                    }
                                    return false;
                                }
                            });
                        }

                        if("".equals(pSort)){
                            getSortForInvest(preLoginDataList);
//                            ListUtils.sortForInvest(preLoginDataList, new ListUtils.ISynchronousList<Map<String, Object>, KeyAndValueItem>() {
//                                @Override
//                                public boolean doSomeThing(Map<String, Object> stringObjectMap, KeyAndValueItem keyAndValueItem) {
//                                    String sourceCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE);
//                                    String targetCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE);
//                                    if(keyAndValueItem.getKey().equals(sourceCurrencyCode) && keyAndValueItem.getValue().equals(targetCurrencyCode)){
//                                        stringObjectMap.put("formatNumber",keyAndValueItem.getParam());
//                                        return true;
//                                }
//                                return false;
//                                }
//                            });
//                            LogGloble.i("info","sort   preLoginDataList ==" + preLoginDataList.toString());
                        }
                        if(null != commDataList && commDataList.size()!=0&&"".equals(pSort)){

                            BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLogoutDataList", preLoginAdapter.getSourceList());

                            if (preLoginAdapter == null) {
                                preLoginAdapter = new CommonAdapter<Map<String, Object>>(PrmsNewPricesActivity.this, preLoginDataList, preloginCommonAdapter);
                                lv_prms.setAdapter(preLoginAdapter);

                            } else {
//                                preLoginAdapter.notifyDataSetChanged();
                                preLoginAdapter.setSourceList(preLoginDataList,0);
//                                LogGloble.i("info","Adapter   preLoginDataList ==" + preLoginDataList.toString());
                            }

                            //                   保存本次所有请求回来的数据
//                            BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLogoutDataList", preLoginDataList);
//                            BaseDroidApp.getInstanse().getBizDataMap().get("oldTotalLogoutDataList");
//                            LogGloble.i("info","prelogindatalist ===" +BaseDroidApp.getInstanse().getBizDataMap().get("oldTotalLogoutDataList").toString() );
                        }else if(null != commDataList && commDataList.size()!=0&&!"".equals(pSort)){
                            BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLogoutDataList",preLoginAdapter.getSourceList());
                            preLoginDataList.clear();
                            preLoginDataList= commDataList;
                            if (preLoginAdapter == null) {
                                preLoginAdapter = new CommonAdapter<Map<String, Object>>(PrmsNewPricesActivity.this, commDataList, preloginCommonAdapter);
                                lv_prms.setAdapter(preLoginAdapter);

                            } else {
//                                preLoginAdapter.notifyDataSetChanged();
                                preLoginAdapter.setSourceList(commDataList,0);

                            }

                            //
//                            BaseDroidApp.getInstanse().getBizDataMap().put("oldTotalLogoutDataList", commDataList);
                        }
                    }


                    return false;
                }
            });
//            pull_down_layout.loadmoreCompleted(RefreshDataStatus.Successed);
        }

    });


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        if (BaseDroidApp.getInstanse().isLogin()) {
            targetCurrencyStr = (String) dataList.get(position)
                    .get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
            sourceCurrencyCodeStr = (String) dataList.get(position)
                    .get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);


//            BaseHttpEngine.showProgressDialog();
//            queryPrmsAccBalance();
        } else {
            index = position;
            targetCurrencyStr = (String) preLoginDataList.get(position)
                    .get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
            sourceCurrencyCodeStr = (String) preLoginDataList.get(position)
                    .get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
//            LogGloble.i("info","item preLoginDataList=="+preLoginDataList);
//            getLoginUtils().exe(new LoginTask.LoginCallback() {
//
//                @Override
//                public void loginStatua(boolean isLogin) {
//                    // TODO Auto-generated method stub
//                    if (isLogin) {
//                        // 如果以登陆直接调用,如果没有登陆打开登陆页面
//                        // 登陆成功
//                        loginSuccess();
//                    }
//                }
//            });


        }

//           贵金属单笔行情查询 querySingelQuotation
        index = position;
//       ccygrp= sourceCurrencyCode+targetCurrencyCode
        ccygrp = sourceCurrencyCodeStr + targetCurrencyStr;
        stopPollingFlag();
        BaseHttpEngine.showProgressDialog();
        getQuerySingelQuotation(ccygrp);
    }

    public void getQuerySingelQuotation(final String ccygrp) {
//        map.put("ccygrp",ccygrp);//货币对代码
//        map.put("cardType","G");
//        map.put("cardClass", "R");;
        QuerySingelQuotationRequestParams querySingelQuotation = new QuerySingelQuotationRequestParams(ccygrp, "G", "R");
        HttpHelp h = HttpHelp.getInstance();
        h.postHttpFromSF(this, querySingelQuotation);
        h.setHttpErrorCallBack(null);
        h.setHttpResponseCallBack(new IHttpResponseCallBack() {
            @Override
            public boolean responseCallBack(String response, Object extendParams) {
                BaseHttpEngine.dissMissProgressDialog();
                QuerySingleQuotationResponseData data = GsonTools.fromJson(response, QuerySingleQuotationResponseData.class);
                //单笔行情 wuhan要改
//                Map<String,String> body = (Map<String ,String>)data.getBody();

                QuerySingleQuotationResult item= data.getBody();
//                if(item !=null){
                Map<String,String> map = new HashMap<String, String>();
                map.put("ccygrpNm",item.ccygrpNm);
                map.put("buyRate",item.buyRate);
                map.put("sellRate",item.sellRate);
                map.put("priceTime",item.priceTime);
                map.put("currPercentDiff",item.currPercentDiff);
                map.put("currDiff",item.currDiff);
                map.put("tranCode",item.tranCode);
                map.put("sortPriority",item.sortPriority);
                map.put("sourceCurrencyCode",item.sourceCurrencyCode);
                map.put("targetCurrencyCode",item.targetCurrencyCode);
                map.put("referPrice",item.referPrice);
                map.put("openPrice",item.openPrice);
                map.put("maxPrice",item.maxPrice);
                map.put("minPrice",item.minPrice);

                BaseDroidApp.getInstanse().getBizDataMap().put("map", map);
//                }
                if (BaseDroidApp.getInstanse().isLogin()) {
                    BaseDroidApp.getInstanse().getBizDataMap().put(Prms.PRMS_PRICE, dataList);
                } else {
                    BaseDroidApp.getInstanse().getBizDataMap().put(Prms.PRMS_PRICE, preLoginDataList);
                }
//                BaseDroidApp.getInstanse().getBizDataMap().put("sort_code_List", sort_code_List);


                //要跳到详情页面，要有改动的。wuhan
                JUMPFLAG = prms;
                PrmsNewPricesActivity.prmsFlagGoWay =2;
                Intent intent = new Intent(PrmsNewPricesActivity.this, PrmsNewPricesDetailActivity.class);
                intent.putExtra("targetCurrencyStr", targetCurrencyStr);
                intent.putExtra("sourceCurrencyCodeStr", sourceCurrencyCodeStr);
                intent.putExtra("enters",2);//1是中行优选投资跳进去,2 内部跳进去,3是主页面扫二维码进去
                intent.putExtra("position", index);
                intent.putExtra("ccygrp", ccygrp);
                startActivity(intent);
                return false;
            }
        });
    }



    @Override
    public void requestPsnAssetBalanceQueryCallBack(Object resultObj) {
        super.requestPsnAssetBalanceQueryCallBack(resultObj);
//        ShowDialogTools.Instance.dissMissProgressDialog();
        if (StringUtil.isNullOrEmpty(resultObj)) {
            return;
        }
        Map<String, Object> map_result = getHttpTools().getResponseResult(resultObj);
        //纸铂金+   账户贵金属
//        String pltmAmt = String.valueOf(map_result.get("pltmAmt"));
//        //+纸钯金
//        String padmAmt = String.valueOf(map_result.get("padmAmt"));
        String   actGoldAmt   = String.valueOf(map_result.get("actGoldAmt"));


        double totalSum = 0.00;
//        if (this.isShowInSheet(pltmAmt)) {
//            totalSum = Double.parseDouble(pltmAmt);
//        }
//
//        if (this.isShowInSheet(padmAmt)) {
//            totalSum += Double.parseDouble(padmAmt);
//        }


        if (this.isShowInSheet(actGoldAmt)) {
            totalSum = Double.parseDouble(actGoldAmt);
        }

        if (isShowInSheet(totalSum + "")) {
            investPriceView.setAmountText(totalSum + "");
        } else {
            investPriceView.setAmountText(0 + "");
        }

        checkRequestPsnInvestmentManageIsOpen();

    }

    private boolean isShowInSheet(String amount) {
        try {
            double e = Double.parseDouble(amount);
            return e > 0.0D;
        } catch (Exception var4) {
            return false;
        }
    }

    /**
     * 请求 --贵金属多笔行情查询(涨跌幅)
     */
    public void getqueryMultipleQuotation(final String pSort) {
//        map.put("cardType","G");
//        map.put("cardClass", "R");
//        map.put("pSort",pSort);
        String cardType = "G";
        String cardClass = "R";
//        String pSort = "UP";
        QueryMultipleQuotationRequestParams queryMultiple = new QueryMultipleQuotationRequestParams(cardType, cardClass, pSort);
        HttpHelp h = HttpHelp.getInstance();
        h.postHttpFromSF(this, queryMultiple);
        h.setHttpErrorCallBack(null);
//        if(isSort){
//            //屏蔽错误码
//            h.setOkHttpErrorCode(new IOkHttpErrorCode() {
//                @Override
//                public boolean handlerErrorCode(BaseResponseData responseData, Object extendParams) {
//                    return false;
//
//                }
//            });
//        }
        h.setHttpResponseCallBack(new IHttpResponseCallBack() {
            @Override
            public boolean responseCallBack(String response, Object extendParams) {
                BaseHttpEngine.dissMissProgressDialog();
                QueryMultipleQuotationResponseData data = GsonTools.fromJson(response, QueryMultipleQuotationResponseData.class);
//                Map<String,String> body = (Map<String ,String>)data.getBody();
                //wuhan 要改
//                commDataList = (List<Map<String, Object>>) (biiResponseBody.getResult());
                List<QueryMultipleQuotationResult.QueryMultipleQuotationItem> list = data.getBody().getItem();


//                List<Map<String, Object>>  list = data.getBody();
                if (StringUtil.isNullOrEmpty(list)) {
                    BaseDroidApp.getInstanse().showMessageDialog(
                            getString(R.string.prms_no_price),
                            new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    PrmsNewPricesActivity.this.finish();
                                }
                            });
                }
                commDataList.clear();
                for (int i = 0; i < list.size(); i++) {
                    QueryMultipleQuotationResult.QueryMultipleQuotationItem item = list.get(i);
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("ccygrpNm", item.ccygrpNm);
                    map.put("buyRate", item.buyPrice);
                    map.put("sellRate", item.sellPrice);
                    map.put("currPercentDiff", item.currPercentDiff);
                    map.put("currDiff", item.currDiff);
                    map.put("priceTime", item.priceTime);
                    map.put("tranCode", item.tranCode);
                    map.put("sortPriority", item.sortPriority);
                    map.put("sourceCurrencyCode", item.sourceCurrencyCode);
                    map.put("targetCurrencyCode", item.targetCurrencyCode);
                    commDataList.add(map);
                }

//                isLogin();

                if(BaseDroidApp.getInstanse().isLogin()){
                    if(null != commDataList&& commDataList.size()!=0){
                        getSortForInvest(dataList);
//                        ListUtils.sortForInvest(dataList, new ListUtils.ISynchronousList<Map<String, Object>, KeyAndValueItem>() {
//                            @Override
//                            public boolean doSomeThing(Map<String, Object> stringObjectMap, KeyAndValueItem keyAndValueItem) {
//                                String sourceCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE);
//                                String targetCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE);
//                                if(keyAndValueItem.getKey().equals(sourceCurrencyCode) && keyAndValueItem.getValue().equals(targetCurrencyCode)){
//                                    stringObjectMap.put("formatNumber",keyAndValueItem.getParam());
//                                    return true;
//                                }
//                                return false;
//                            }
//                        });


                        ListUtils.synchronousList(commDataList, dataList, new ListUtils.ISynchronousList<Map<String, Object>, Map<String, Object>>() {


                            @Override
                            public boolean doSomeThing(Map<String, Object> stringObjectMap, Map<String, Object> stringObject) {
                                String sourceCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_SOURCECODE_RES);
                                String targetCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_TARGETCODE_RES);
                                String sourceCurrencyCodeNew = (String) stringObject.get("sourceCurrencyCode");
                                String targetCurrencyCodeNew =(String) stringObject.get("targetCurrencyCode");
                                String currencyCode = sourceCurrencyCode+targetCurrencyCode;
                                String currencyCodeNew = sourceCurrencyCodeNew+targetCurrencyCodeNew;
                                if(currencyCode.equals(currencyCodeNew)) {
                                    stringObjectMap.put("buyRate",(String) stringObject.get("buyRate") );
                                    stringObjectMap.put("sellRate",(String) stringObject.get("sellRate") );
                                    stringObjectMap.put("ibkNum",(String) stringObject.get("ibkNum") );
                                    stringObjectMap.put("type",(String) stringObject.get("type") );
                                    stringObjectMap.put("rate",(String) stringObject.get("rate") );
                                    stringObjectMap.put("updateDate",(String) stringObject.get("updateDate") );
                                    stringObjectMap.put("buyNoteRate",(String) stringObject.get("buyNoteRate") );
                                    stringObjectMap.put("sellNoteRate",(String) stringObject.get("sellNoteRate") );
                                    stringObjectMap.put("state",(String) stringObject.get("state") );
                                    stringObjectMap.put("formatNumber", stringObject.get("formatNumber"));
                                    return true;
                                }
                                return false;
                            }
                        });
                    }


                    if("".equals(pSort)){
                        getSortForInvest(commDataList);
//                            ListUtils.sortForInvest(commDataList, new ListUtils.ISynchronousList<Map<String, Object>, KeyAndValueItem>() {
//                                @Override
//                                public boolean doSomeThing(Map<String, Object> stringObjectMap, KeyAndValueItem keyAndValueItem) {
//                                    String sourceCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE);
//                                    String targetCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE);
//                                    if(keyAndValueItem.getKey().equals(sourceCurrencyCode) && keyAndValueItem.getValue().equals(targetCurrencyCode)){
//                                        stringObjectMap.put("formatNumber",keyAndValueItem.getParam());
//                                        return true;
//                                    }
//                                    return false;
//                                }
//                            });
                    }

                    dataList.clear();
                    dataList = commDataList;
                    if (loginAdatper == null) {
                        loginAdatper = new CommonAdapter<Map<String, Object>>(PrmsNewPricesActivity.this, commDataList, iCommonAdapter);
                        lv_prms.setAdapter(loginAdatper);


                    } else {
//                        loginAdatper.notifyDataSetChanged();

                        loginAdatper.setSourceList(commDataList,0);
                    }

                }else {

                    if(null != commDataList && commDataList.size()!=0){

                        getSortForInvest(preLoginDataList);
//                        ListUtils.sortForInvest(preLoginDataList, new ListUtils.ISynchronousList<Map<String, Object>, KeyAndValueItem>() {
//                            @Override
//                            public boolean doSomeThing(Map<String, Object> stringObjectMap, KeyAndValueItem keyAndValueItem) {
//                                String sourceCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE);
//                                String targetCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE);
//                                if(keyAndValueItem.getKey().equals(sourceCurrencyCode) && keyAndValueItem.getValue().equals(targetCurrencyCode)){
//                                    stringObjectMap.put("formatNumber",keyAndValueItem.getParam());
//                                    return true;
//                                }
//                                return false;
//                            }
//                        });

                        ListUtils.synchronousList(commDataList,  preLoginDataList, new ListUtils.ISynchronousList<Map<String, Object>, Map<String, Object>>() {


                            @Override
                            public boolean doSomeThing(Map<String, Object> stringObjectMap, Map<String, Object> stringObject) {
                                String sourceCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_SOURCECODE_RES);
                                String targetCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_RATE_TARGETCODE_RES);
                                String sourceCurrencyCodeNew = (String) stringObject.get("sourceCurrencyCode");
                                String targetCurrencyCodeNew =(String) stringObject.get("targetCurrencyCode");
                                String currencyCode = sourceCurrencyCode+targetCurrencyCode;
                                String currencyCodeNew = sourceCurrencyCodeNew+targetCurrencyCodeNew;
                                if(currencyCode.equals(currencyCodeNew)) {
                                    stringObjectMap.put("buyRate",(String) stringObject.get("buyRate") );
                                    stringObjectMap.put("sellRate",(String) stringObject.get("sellRate") );
                                    stringObjectMap.put("ibkNum",(String) stringObject.get("ibkNum") );
                                    stringObjectMap.put("type",(String) stringObject.get("type") );
                                    stringObjectMap.put("rate",(String) stringObject.get("rate") );
                                    stringObjectMap.put("updateDate",(String) stringObject.get("updateDate") );
                                    stringObjectMap.put("buyNoteRate",(String) stringObject.get("buyNoteRate") );
                                    stringObjectMap.put("sellNoteRate",(String) stringObject.get("sellNoteRate") );
                                    stringObjectMap.put("state",(String) stringObject.get("state") );
                                    stringObjectMap.put("formatNumber", stringObject.get("formatNumber"));
                                    return true;
                                }
                                return false;
                            }
                        });
                    }
//                  LogGloble.i("info","commdatalist ==" + commDataList.toString());
                    if("".equals(pSort)){
                        getSortForInvest(commDataList);
//                            ListUtils.sortForInvest(commDataList, new ListUtils.ISynchronousList<Map<String, Object>, KeyAndValueItem>() {
//                                @Override
//                                public boolean doSomeThing(Map<String, Object> stringObjectMap, KeyAndValueItem keyAndValueItem) {
//                                    String sourceCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE);
//                                    String targetCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE);
//                                    if(keyAndValueItem.getKey().equals(sourceCurrencyCode) && keyAndValueItem.getValue().equals(targetCurrencyCode)){
//                                        stringObjectMap.put("formatNumber",keyAndValueItem.getParam());
//                                        return true;
//                                    }
//                                    return false;
//                                }
//                            });
                    }
                    preLoginDataList.clear();
                    preLoginDataList = commDataList;
                    if (preLoginAdapter == null) {
                        preLoginAdapter = new CommonAdapter<Map<String, Object>>(PrmsNewPricesActivity.this, commDataList, preloginCommonAdapter);
                        lv_prms.setAdapter(preLoginAdapter);

                    } else {
//                        preLoginAdapter.notifyDataSetChanged();
                        preLoginAdapter.setSourceList(commDataList,0);

                    }

                }

//                commDataList

                return false;
            }
        });


        refashePullDown();

//        handler.sendEmptyMessageDelayed(0,7000);

    }



    @Override
    public void requestCommConversationIdCallBack(Object resultObj) {
        super.requestCommConversationIdCallBack(resultObj);
        queryPrmsTradeEntrustNow(0, 10, true);
    }


    @Override
    public void queryPrmsTradeEntrustNowCallBack(Object resultObj) {
        BaseHttpEngine.dissMissProgressDialog();
        super.queryPrmsTradeEntrustNowCallBack(resultObj);
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        Map<String, Object> map = (Map<String, Object>) biiResponseBody
                .getResult();
        prmsControl.queryEntrustNowList = (List<Map<String, Object>>) map
                .get(Prms.PRMS_QUERY_DEAL_LIST);
        if (!StringUtil.isNullOrEmpty(prmsControl.queryEntrustNowList)) {// 如果不为空
            JUMPFLAG = prms;
            String totalNum = (String) map
                    .get(Prms.PRMS_QUERY_DEAL_RECORDNUMBER);
            Intent intent = new Intent();
            intent.putExtra(Prms.PRMS_QUERY_DEAL_RECORDNUMBER, totalNum);
            intent.setClass(this, PrmsQueryEntrustNowActivity.class);
            startActivity(intent);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        switch (JUMPFLAG){
            case prms:
                isLogin();
                break;
            case ENTRUSTNOW:
                CommonApplication.getInstance().setCurrentAct(PrmsNewPricesActivity.this);
                BaseHttpEngine.showProgressDialog();
                checkRequestPsnInvestmentManageIsOpen();
                break;
            default:
                JUMPFLAG = prms;
                isLogin();
                break;
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mTimerRefreshToolsMultiple.stopTimer();
        stopPollingFlag();

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopPollingFlag();
    }



    private List<Map<String, Object>>  getSortForInvest(List<Map<String, Object>> list){

//        List<Map<String, Object>> mapList = list;

        ListUtils.sortForInvest(list, new ListUtils.ISynchronousList<Map<String, Object>, KeyAndValueItem>() {
            @Override
            public boolean doSomeThing(Map<String, Object> stringObjectMap, KeyAndValueItem keyAndValueItem) {
                String sourceCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE);
                String targetCurrencyCode = (String) stringObjectMap.get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE);
                if(keyAndValueItem.getKey().equals(sourceCurrencyCode) && keyAndValueItem.getValue().equals(targetCurrencyCode)){
                    stringObjectMap.put("formatNumber",keyAndValueItem.getParam());
                    return true;
                }
                return false;
            }
        });

        return list;

    }
}
