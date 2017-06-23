package com.chinamworld.bocmbci.biz.prms.price;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.biz.foreign.StringTools;
import com.chinamworld.bocmbci.biz.foreign.adapter.ForeignDialogAdapter;
import com.chinamworld.bocmbci.biz.prms.PrmsNewBaseActivity;
import com.chinamworld.bocmbci.biz.prms.trade.PrmsTradeBuyActivity;
import com.chinamworld.bocmbci.biz.prms.trade.PrmsTradeSaleActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.DictionaryData;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.model.httpmodel.sifang.queryAverageTendency.QueryAverageTendencyRequestParams;
import com.chinamworld.bocmbci.model.httpmodel.sifang.queryAverageTendency.QueryAverageTendencyResponseData;
import com.chinamworld.bocmbci.model.httpmodel.sifang.queryAverageTendency.QueryAverageTendencyResult;
import com.chinamworld.bocmbci.model.httpmodel.sifang.querySingelQuotation.QuerySingelQuotationRequestParams;
import com.chinamworld.bocmbci.model.httpmodel.sifang.querySingelQuotation.QuerySingleQuotationResponseData;
import com.chinamworld.bocmbci.model.httpmodel.sifang.querySingelQuotation.QuerySingleQuotationResult;
import com.chinamworld.bocmbci.model.httpmodel.sifang.queryktendency.QueryKTendencyResponseData;
import com.chinamworld.bocmbci.model.httpmodel.sifang.queryktendency.QueryKTendencyResult;
import com.chinamworld.bocmbci.model.httpmodel.sifang.queryktendency.QuerykTendencyRequestParams;
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
import com.chinamworld.bocmbci.userwidget.qrcodeview.InvestQRCodeActivity;
import com.chinamworld.bocmbci.userwidget.sfkline.IRefreshKLineDataListener;
import com.chinamworld.bocmbci.userwidget.sfkline.InverstKLineView;
import com.chinamworld.bocmbci.utils.ListUtils;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.llbt.userwidget.dialogview.MessageDialog;
import com.chinamworld.llbt.userwidget.tabview.TabView;
import com.chinamworld.llbt.utils.TimerRefreshTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wuhan on 2016/9/26.
 * 贵金属行情详情页
 */
public class PrmsNewPricesDetailActivity extends PrmsNewBaseActivity implements View.OnClickListener {


    //    private Button btn_back;
//    private Spinner sp_currency;
//    private Button btn_share;
//    private Button btn_QRcode;


    private Button btn_buy;
    private Button btn_sale;
    private TextView tv_referPrice;
    private TextView tv_currDiff;
    private TextView tv_currPercentDiff;

//    private TextView tv_onehour;
//    private TextView tv_fourhour;
//    private TextView tv_day;
//    private TextView tv_week;
//    private TextView tv_mounth;
//    private Button btn_KOrZ;

    private ImageView image_collect;
    private TextView tv_saleRate;
    private TextView tv_buyRate;
//    private TextView tv_rise;
    /**
     * K线图
     */
    private InverstKLineView queryKTendencyView;
    private TabView mTabView;

    /**
     * 查询时间
     */
    private String timeZones;
//    private WebView web_k;

    private String tendencyType;//折线的趋势类型
    private String kType;//K线图的趋势类型
    //    private boolean isK = true;//是否折线图
    private String ccygrp;
    private int position;
    private String sourceCurrencyCodeStr = "";
    private String targetCurrencyStr = "";
    private int flag;
    /**
     * 买入
     */
    private static final int BUY = 4;
    /**
     * 卖出
     */
    private static final int SALE = 1;
    private List<Map<String, Object>> totalDataList;//合并后的数据
//    private List<String> sort_code_List;

    //    ArrayAdapter<String> codeAdapter;
    private int sp_position;


    private Map<String, String> oldSingleMap;//单笔行情第一次请求的数据。
    private Dialog dialog;
    String sourceDealCode,targetDealCode;
    /** 标题货币对*/
    private TextView currencyLeft;
    private TextView currencyRight;
    private ImageView currency_right_img;
    //    View downView; // 下拉箭头
    private String currencyLeftName;
    private String currencyRigthName;

    private int enters = 1;
    LinearLayout foreignTitle;
    View title;
    private List<Map<String, Object>> commDataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.prms_main_detail_layout);
        initUi();

        initData();
        initBaseLayout();
        initListener();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimerRefreshToolsSingel.stopTimer();
        queryKTendencyView.stopRefreshData();
    }

    /**
     * 初始化基类布局
     */
    private void initBaseLayout() {
        setLeftButtonPopupGone();
        getBackGroundLayout().setTitleBackground(R.color.btn_white);
        getBackGroundLayout().setTitleText(LocalData.code_Map.get(ccygrp));
//        getBackGroundLayout().setTitleTextColor(R.color.fonts_black);
//        getBackGroundLayout().setTitleText(getResources().getString(R.string.prms_title_price_news));
        getBackGroundLayout().setLeftButtonVisibility(View.VISIBLE);
        getBackGroundLayout().setOnLeftButtonImage(getResources().getDrawable(R.drawable.llbt_new_back_icon));
        getBackGroundLayout().setRightButtonVisibility(View.VISIBLE);
        getBackGroundLayout().setRightButtonImage(getResources().getDrawable(R.drawable.btn_rqcode),0.5f,0.5f);
        getBackGroundLayout().setShareButtonVisibility(View.GONE);
        //getBackGroundLayout().setRightButtonImage(getResources().getDrawable(R.drawable.llbt_btn_share_black));
//        sp_currency = new Spinner(this);
//        getBackGroundLayout().setTitleLayout(sp_currency);

//        getBackgroundLayout().setRightShareButtonImage();
        //base_btn_share_black


        currencyLeftName = (String)LocalData.Currency.get(sourceCurrencyCodeStr);
        currencyRigthName = (String)LocalData.Currency.get(targetCurrencyStr);

        if(LocalData.goldLists.contains(currencyLeftName)||LocalData.goldLists.contains(currencyRigthName)){
            currencyLeftName = (String)LocalData.code_Map_Left.get(sourceCurrencyCodeStr);
            currencyRigthName = (String)LocalData.code_Map_Right.get(targetCurrencyStr);
        }

        currencyLeft.setText(currencyLeftName);
        currencyRight.setText(currencyRigthName);
        getBackGroundLayout().setOnLeftButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrmsNewPricesDetailActivity.this.finish();
            }
        });
        //二维码扫描
        getBackGroundLayout().setOnRightButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String  codeNameCode = (String)LocalData.code_Map.get(sourceCurrencyCodeStr+targetCurrencyStr);
                if(StringUtil.isNullOrEmpty(codeNameCode)){
                    PrmsNewPricesActivity.prmsFlagGoWay = 3;
                    BaseDroidApp.getInstance().getBizDataMap().put("sourceCurrencyCodeStr",sourceCurrencyCodeStr);
                    BaseDroidApp.getInstance().getBizDataMap().put("targetCurrencyStr",targetCurrencyStr);
                    codeNameCode= LocalData.Currency.get(sourceCurrencyCodeStr)+"/"+LocalData.Currency.get(targetCurrencyStr);
                }
                //5
                InvestQRCodeActivity.goToInvestQRCodeActivity(PrmsNewPricesDetailActivity.this,"账户贵金属二维码", String.format("boc://bocphone?type=5&prmsRQCode=%s",sourceCurrencyCodeStr+targetCurrencyStr),codeNameCode);
            }
        });

        //分享
//        getBackGroundLayout().setOnShareButtonClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        queryKTendencyView.setLandscapeCurCode(currencyLeftName+"/"+currencyRigthName);
        queryKTendencyView.setLandscapeCurCodeItemClickListener(codeItemQuery);

        foreignTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseDroidApp.getInstanse()
                        .showSelectCurrencyPairDialog(PrmsNewPricesDetailActivity.this , totalDataList, codeItemQuery);
//                showDialog();
            }
        });

        queryKTendencyView.setRrefreshKLineDataListener(queryKTendencyClick);

        timeZones = queryKTendencyView.getNewTimeZone(); //获取更新时间
        mTabView = (TabView) findViewById(R.id.tabView);
        mTabView.setContentText("1小时;4小时;日;周;月");
        mTabView.setCurSelectedIndex(2);
        queryKTendencyView.setLandscapeAllRateList(totalDataList);

    }


    /**扫描完成后 需要执行的动作*/
//    public boolean isForexQRHandler(Activity activity, String qrInfo){
//        int type = 5;
//        if(type != 5){
//            return false;
//        }
//
//        return false;
//    }

    private void initUi() {

//        btn_back = (Button)findViewById(R.id.btn_back);
//        sp_currency = (Spinner) findViewById(R.id.sp_currency);
//        btn_share = (Button) findViewById(R.id.btn_share);
//        btn_QRcode = (Button) findViewById(R.id.btn_QRcode);
        btn_buy = (Button) findViewById(R.id.btn_buy);
        btn_sale = (Button) findViewById(R.id.btn_sale);
        tv_referPrice = (TextView) findViewById(R.id.tv_referPrice);
        tv_currDiff = (TextView) findViewById(R.id.tv_currDiff);
        tv_currPercentDiff = (TextView) findViewById(R.id.tv_currPercentDiff);


//        tv_onehour = (TextView) findViewById(R.id.tv_onehour);
//        tv_fourhour = (TextView) findViewById(R.id.tv_fourhour);
//        tv_day = (TextView) findViewById(R.id.tv_day);
//        tv_week = (TextView) findViewById(R.id.tv_week);
//        tv_mounth = (TextView) findViewById(R.id.tv_mounth);
//        btn_KOrZ = (Button) findViewById(R.id.btn_KOrZ);

        image_collect = (ImageView) findViewById(R.id.image_collect);
        image_collect.setVisibility(View.GONE);
        tv_saleRate = (TextView) findViewById(R.id.tv_saleRate);
        tv_buyRate = (TextView) findViewById(R.id.tv_buyRate);
//        tv_rise = (TextView) findViewById(R.id.tv_rise);
        queryKTendencyView = (InverstKLineView) findViewById(R.id.query_KTendency_view); // K线图
//        web_k = (WebView)findViewById(R.id.web_k);
        title = LayoutInflater.from(PrmsNewPricesDetailActivity.this).inflate(R.layout.foreign_title_layout,null);
        getBackGroundLayout().setTitleLayout(title);
        foreignTitle   = (LinearLayout) title.findViewById(R.id.foreign_title);
        currencyLeft = (TextView) title.findViewById(R.id.tv_currency_left);
        currencyRight = (TextView) title.findViewById(R.id.tv_currency_right);
        currency_right_img  =(ImageView)title.findViewById(R.id.currency_right_img);
//        downView = title.findViewById(R.id.currency_right_img);
    }

    /**
     * K线点击事件
     */
    private IRefreshKLineDataListener queryKTendencyClick = new IRefreshKLineDataListener() {
        @Override
        public void onRefreshKLineDataCallBack(int nIndex, int nShowType, boolean isBackgroundRefresh) {
            timeZones = queryKTendencyView.getNewTimeZone(); //获取更新时间
            if (nShowType == 1) { //刷新K线图
                if (nIndex == 0) { // 一小时K线图
                    kType = "OK";
                } else if (nIndex == 1) { // 四小时K线图
                    kType = "FK";
                } else if (nIndex == 2) { // 日K线图
                    kType = "DK";
                } else if (nIndex == 3) { // 周K线图
                    kType = "WK";
                } else if (nIndex == 4) { // 月K线图
                    kType = "MK";
                }
                getQueryKTendencyData(ccygrp); // 刷新K线图数据
            } else if (nShowType == 2) { //刷新曲线图
                if (nIndex == 0) { // 一小时K线图
                    tendencyType = "O";
                } else if (nIndex == 1) { // 四小时K线图
                    tendencyType = "F";
                } else if (nIndex == 2) { // 日K线图
                    tendencyType = "D";
                } else if (nIndex == 3) { // 周K线图
                    tendencyType = "W";
                } else if (nIndex == 4) { // 月K线图
                    tendencyType = "M";
                }
                getQueryAverageTendency(); // 刷新趋势图查询
            }
        }
    };

    /**
     * 查询K线图  贵金属K线图查询 --API
     */
//    map.put("cardType","G");
//    map.put("cardClass", "R");
    public void getQueryKTendencyData(String ccygrp) {
        QuerykTendencyRequestParams queryKTendency = new QuerykTendencyRequestParams(ccygrp, kType, "G", "R", timeZones);
        HttpHelp help = HttpHelp.getInstance();
        help.postHttpFromSF(this, queryKTendency);
        help.setHttpErrorCallBack(new IHttpErrorCallBack() {
            @Override
            public boolean onError(String exceptionMessage, Object extendParams) {
                MessageDialog.closeDialog(); //关闭通讯框
                queryKTendencyView.startRefreshData();
                return true;
            }
        });
        help.setOkHttpErrorCode(new IOkHttpErrorCode() {
            @Override
            public boolean handlerErrorCode(BaseResponseData responseData, Object extendParams) {
                MessageDialog.closeDialog(); //关闭通讯框
                queryKTendencyView.startRefreshData();
                return true;
            }
        });
        help.setHttpResponseCallBack(new IHttpResponseCallBack() {
            @Override
            public boolean responseCallBack(String response, Object extendParams) {
                QueryKTendencyResponseData data = GsonTools.fromJson(response, QueryKTendencyResponseData.class);
                QueryKTendencyResult body = data.getBody();
                queryKTendencyView.setKLineData(body);
                queryKTendencyView.startRefreshData();
                return false;
            }
        });
    }

    /**
     * 查询趋势图    2.4 贵金属趋势图查询 -API
     */
//    map.put("cardType","G");
//    map.put("cardClass", "R");
    public void getQueryAverageTendency() {
        QueryAverageTendencyRequestParams queryAverageTendency = new QueryAverageTendencyRequestParams(ccygrp, "G", "R", tendencyType);
        HttpHelp help = HttpHelp.getInstance();
        help.postHttpFromSF(this, queryAverageTendency);
        help.setHttpErrorCallBack(new IHttpErrorCallBack() {
            @Override
            public boolean onError(String exceptionMessage, Object extendParams) {
                MessageDialog.closeDialog(); //关闭通讯框
                queryKTendencyView.startRefreshData();
                return true;
            }
        });
        help.setOkHttpErrorCode(new IOkHttpErrorCode() {
            @Override
            public boolean handlerErrorCode(BaseResponseData responseData, Object extendParams) {
                MessageDialog.closeDialog(); //关闭通讯框
                queryKTendencyView.startRefreshData();
                return true;
            }
        });
        help.setHttpResponseCallBack(new IHttpResponseCallBack() {
            @Override
            public boolean responseCallBack(String response, Object extendParams) {
                QueryAverageTendencyResponseData result = GsonTools.fromJson(response, QueryAverageTendencyResponseData.class);
                QueryAverageTendencyResult body = result.getBody();
                //该方法已在控件中实现  linyl
//                queryKTendencyView.setQuShiData("yyyy\nMM-dd","yyyy-MM-dd","参考价格：","时间：","参考价格",2,"yyyy-MM-dd HH:mm:ss","暂无数据");
                queryKTendencyView.setECharsViewData(body);
                queryKTendencyView.startRefreshData();
                return false;
            }
        });
    }

    private void initData() {
        isbuysale = false;
        commDataList = new ArrayList<Map<String, Object>>();
        Intent intent = getIntent();
        enters = intent.getIntExtra("enters",1);
//        intent.putExtra("enters",3);//1是中行跳进去,2 内部跳进去,3是主页面扫二维码进去
        if(BaseDroidApp.getInstanse().isLogin()){
            btn_sale.setVisibility(View.VISIBLE);
        }else{
            btn_sale.setVisibility(View.GONE);
        }
        if(enters ==1){
            currency_right_img.setVisibility(View.GONE);
            sourceCurrencyCodeStr = intent.getStringExtra("sourceCurrencyCodeStr");
            targetCurrencyStr = intent.getStringExtra("targetCurrencyStr");
            ccygrp = sourceCurrencyCodeStr+targetCurrencyStr;
            foreignTitle.setEnabled(false);
            foreignTitle.setClickable(false);
            isLogin();

        }else if(enters ==2){
            initDetailData(intent);
            foreignTitle.setEnabled(true);
            foreignTitle.setClickable(true);
            currency_right_img.setVisibility(View.VISIBLE);
//            downView.setVisibility(View.GONE);
        }else if(enters == 3){
            sourceCurrencyCodeStr = intent.getStringExtra("sourceCurrencyCodeStr");
            targetCurrencyStr = intent.getStringExtra("targetCurrencyStr");
            ccygrp = sourceCurrencyCodeStr+targetCurrencyStr;
            foreignTitle.setEnabled(false);
            foreignTitle.setClickable(false);
            currency_right_img.setVisibility(View.GONE);
//            downView.setVisibility(View.GONE);
            isLogin();
        }
        //七秒刷新
        mTimerRefreshToolsSingel.startTimer();
//        sort_code_List = new ArrayList<String>();

//        BaseDroidApp.getInstanse().getBizDataMap().put("sort_code_List", sort_code_List);
//        sort_code_List = (List<String>) BaseDroidApp.getInstanse().getBizDataMap().get("sort_code_List");
//        codeAdapter = new ArrayAdapter<String>(
//                this, R.layout.dept_spinner, sort_code_List);
//        codeAdapter
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        sp_currency.setBackgroundResource(R.drawable.bg_spinner);
//        sp_currency.setBackgroundColor(this.getResources().getColor(R.color.white));
//        sp_currency.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT));
//        sp_currency.setAdapter(codeAdapter);


//        //默认发送折线图请求
//        requestqueryAverageTendency(ccygrp, tendencyType);
        //
//        sp_currency.setOnItemSelectedListener(new OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                sp_position = i;
//                String currncy = sort_code_List.get(i);
//                if (Prms.code_Map.containsValue(currncy)) {
//                    for (Map.Entry<String, String> e : Prms.code_Map.entrySet()) {
//                        if (e.getValue().equals(currncy)) {
//                            ccygrp = e.getKey();
//
////                            临时
//                            getQuerySingelQuotation(ccygrp);
////                            mTimerRefreshToolsSingel.startTimer();
//
////                            getRefreshData();
//                            break;
//                        }
//                    }
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

    }

    private void isLogin() {
        if (BaseDroidApp.getInstanse().isLogin()) {
//            if (!PollingRequestThread.pollingFlag) {
//                BaseHttpEngine.showProgressDialogCanGoBack();
//                checkRequestPsnInvestmentManageIsOpen();
//            }
            BaseHttpEngine.showProgressDialogCanGoBack();
            checkRequestPsnInvestmentManageIsOpen();
        } else {
//            if (!PollingRequestThread.pollingFlag) {
//                //发送没有登录时黄金行情。
//                BaseHttpEngine.showProgressDialogCanGoBack();
//                queryPrmsPricePoliPreLogin();
//            }
            //发送没有登录时黄金行情。
            BaseHttpEngine.showProgressDialogCanGoBack();
            queryPrmsPricePoliPreLogin();
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

        } else {
            if( isbuysale){
                queryPrmsAccBalance();
            }else{
                queryPrmsPricePoling();
            }


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


    /**
     * 黄金行情查询登录之前发送
     */
    //wuhan
    protected void queryPrmsPricePoliPreLogin() {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Prms.QUERY_TRADERATE_PRELOGIN);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Prms.IBKNUM, LocalDataService.getInstance().getIbkNum(ConstantGloble.Prms));
        map.put(Prms.PARITIESTYPE, "G");
        map.put("offerType", "R");
        biiRequestBody.setParams(map);
        HttpManager.requestOutlayBii(biiRequestBody, this, "queryPrmsPreLoginCallBack");
    }




    public void queryPrmsPreLoginCallBack(Object resultObj) {
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

        if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
            BaseHttpEngine.dissMissProgressDialog();
//			BaseDroidApp.getInstanse().showInfoMessageDialog("请求失败，稍后再试！");
            return;
        }
        String preLoginDetail = (String) biiResponseBody.getResult().toString();
        LogGloble.i("info", "biiResponseBody.getResult()==="+preLoginDetail);
        totalDataList = new ArrayList<Map<String, Object>>();
        totalDataList = (List<Map<String,Object>>)biiResponseBody.getResult();

        // /add by fsm判断行情列表是否为空

        if (StringUtil.isNullOrEmpty(totalDataList)) {
            BaseDroidApp.getInstanse().showMessageDialog(
                    getString(R.string.prms_no_price),
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            PrmsNewPricesDetailActivity.this.finish();
                        }
                    });
        }


        getqueryMultipleQuotation();


    }

    protected void queryPrmsPricePoling() {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Prms.QUERY_TRADERATE);
        biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
                .getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
        biiRequestBody.setParams(null);
        HttpManager.requestBii(biiRequestBody, this, "queryPrmsPricePolingCallBack");
    }

    public void queryPrmsPricePolingCallBack(Object resultObj) {

        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);

        if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
            BaseHttpEngine.dissMissProgressDialog();
            return;
        }
        String LoginDetail = (String) biiResponseBody.getResult().toString();
        LogGloble.i("info", "biiResponseBody.getResult()==="+LoginDetail);
        totalDataList = new ArrayList<Map<String, Object>>();
        totalDataList = (List<Map<String,Object>>)biiResponseBody.getResult();

        // /add by fsm判断行情列表是否为空

        if (StringUtil.isNullOrEmpty(totalDataList)) {
            BaseDroidApp.getInstanse().showMessageDialog(
                    getString(R.string.prms_no_price),
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            PrmsNewPricesDetailActivity.this.finish();
                        }
                    });
        }



        getqueryMultipleQuotation();

    }

    public void getqueryMultipleQuotation() {
        String cardType = "G";
        String cardClass = "R";
//        String pSort = "UP";
        QueryMultipleQuotationRequestParams queryMultiple = new QueryMultipleQuotationRequestParams(cardType, cardClass, "");
        HttpHelp h = HttpHelp.getInstance();
        h.postHttpFromSF(this, queryMultiple);
        h.setHttpErrorCallBack(null);
        h.setHttpResponseCallBack(new IHttpResponseCallBack() {
            @Override
            public boolean responseCallBack(String response, Object extendParams) {
                BaseHttpEngine.dissMissProgressDialog();
                QueryMultipleQuotationResponseData data = GsonTools.fromJson(response, QueryMultipleQuotationResponseData.class);
                //wuhan 要改
                List<QueryMultipleQuotationResult.QueryMultipleQuotationItem> list = data.getBody().getItem();


//                List<Map<String, Object>>  list = data.getBody();
                if (StringUtil.isNullOrEmpty(list)) {
                    BaseDroidApp.getInstanse().showMessageDialog(
                            getString(R.string.prms_no_price),
                            new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    PrmsNewPricesDetailActivity.this.finish();
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
                    if(null != commDataList || commDataList.size()!=0){



                        ListUtils.synchronousList(commDataList,totalDataList , new ListUtils.ISynchronousList<Map<String, Object>, Map<String, Object>>() {


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

                                    return true;
                                }
                                return false;
                            }
                        });
                    }



                }else {

                    if(null != commDataList || commDataList.size()!=0){


                        ListUtils.synchronousList(commDataList,  totalDataList, new ListUtils.ISynchronousList<Map<String, Object>, Map<String, Object>>() {


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
                                    return true;
                                }
                                return false;
                            }
                        });
                    }

                }
                totalDataList.clear();
                totalDataList= commDataList;
                BaseDroidApp.getInstanse().getBizDataMap().put(Prms.PRMS_PRICE, totalDataList);
                return false;
            }
        });


        getQuerySingelQuotation(ccygrp);

    }

    private void initDetailData(Intent intent) {
        ccygrp = intent.getStringExtra("ccygrp");
        position = intent.getIntExtra("position", 0);
        sourceCurrencyCodeStr = intent.getStringExtra("sourceCurrencyCodeStr");
        targetCurrencyStr = intent.getStringExtra("targetCurrencyStr");

//        BaseDroidApp.getInstanse().getBizDataMap().put("map", map);
//        临时
        oldSingleMap = new HashMap<String, String>();
        oldSingleMap = (Map<String, String>) BaseDroidApp.getInstanse().getBizDataMap().get("map");
        if(null !=oldSingleMap.get("referPrice")&&!"null".equals(oldSingleMap.get("referPrice"))){
            tv_referPrice.setText(oldSingleMap.get("referPrice"));
            String currDiff =oldSingleMap.get("currDiff");
//            if(oldSingleMap.get("currDiff").contains("+")){
//                tv_currDiff.setText("+"+PrmsNewPricesActivity.parseStringPattern4(oldSingleMap.get("currDiff"),5));
//            }else if(oldSingleMap.get("currDiff").contains("-")){
//                tv_currDiff.setText(PrmsNewPricesActivity.parseStringPattern4(oldSingleMap.get("currDiff"),5));
//            }else{
//                tv_currDiff.setText(PrmsNewPricesActivity.parseStringPattern4(oldSingleMap.get("currDiff"),5));
//            }
//            currDiff = StringTools.parseStringPattern(currDiff,5);
//            currDiff = StringTools.subZeroAndDot(currDiff);
            if(currDiff.contains("+")){
                currDiff = StringTools.parseStringPattern(currDiff,5);
                currDiff = StringTools.subZeroAndDot(currDiff);
                currDiff = "+"+currDiff;
            }else{
                currDiff = StringTools.parseStringPattern(currDiff,5);
                currDiff = StringTools.subZeroAndDot(currDiff);
            }
            tv_currDiff.setText(currDiff);

//            tv_currDiff.setText(PrmsNewPricesActivity.parseStringPattern4(oldSingleMap.get("currDiff"),5));
//            String currDiff = oldSingleMap.get("currDiff");
            if(null !=currDiff &&!"null".equals(currDiff)&&!"".equals(currDiff)){

                if(currDiff.contains("+")){
                    tv_currDiff.setTextColor(getResources().getColor(R.color.btn_pink));
                }else if(currDiff.contains("-")){
                    tv_currDiff.setTextColor(getResources().getColor(R.color.fonts_green));
                }else{
                    tv_currDiff.setTextColor(getResources().getColor(R.color.lianlong_color_bdc7cb));
                }
            }else{
                tv_currDiff.setText("--");
                tv_currDiff.setTextColor(getResources().getColor(R.color.lianlong_color_bdc7cb));
            }
            String currPercentdiff = oldSingleMap.get("currPercentDiff");
//            tv_currPercentDiff.setText(PrmsNewPricesActivity.parseStringPattern4(currPercentdiff,3));

            tv_currPercentDiff.setText( StringTools.parseStringPattern5(currPercentdiff));
            queryKTendencyView.setRateTextRefresh(tv_referPrice.getText().toString(),
                    tv_currDiff.getText().toString(),tv_currPercentDiff.getText().toString());
            if(null !=currPercentdiff &&!"null".equals(currPercentdiff)&&!"".equals(currPercentdiff)) {
//                if(!oldSingleMap.get("currPercentDiff").contains("-")){
//                    tv_currPercentDiff.setBackgroundResource(R.color.btn_pink);
//                }else{
//                    tv_currPercentDiff.setBackgroundResource(R.color.fonts_green);
//                }
                if(oldSingleMap.get("currPercentDiff").contains("+")){
                    tv_referPrice.setTextColor(getResources().getColor(R.color.btn_pink));
                    tv_currPercentDiff.setBackgroundResource(R.color.btn_pink);
                    queryKTendencyView.setLandscapeTitleBackground(getResources().getColor(R.color.boc_text_color_red));
                }else if(oldSingleMap.get("currPercentDiff").contains("-")){
                    tv_currPercentDiff.setBackgroundResource(R.color.fonts_green);
                    tv_referPrice.setTextColor(getResources().getColor(R.color.fonts_green));
                    queryKTendencyView.setLandscapeTitleBackground(getResources().getColor(R.color.share_green_color));
                }else{
                    tv_referPrice.setTextColor(getResources().getColor(R.color.lianlong_color_bdc7cb));
                    tv_currPercentDiff.setBackgroundResource(R.drawable.shape_llbt_round_gray);
                    queryKTendencyView.setLandscapeTitleBackground(getResources().getColor(R.color.share_gray_color));
                }

            }else {
                tv_currPercentDiff.setText("--");
                tv_currPercentDiff.setBackgroundResource(R.drawable.shape_llbt_round_gray);
                tv_referPrice.setTextColor(getResources().getColor(R.color.lianlong_color_bdc7cb));
                queryKTendencyView.setLandscapeTitleBackground(getResources().getColor(R.color.share_gray_color));
            }
        }else{
            tv_referPrice.setText("--");
            tv_currDiff.setText("--");
            tv_currPercentDiff.setText("--");
            queryKTendencyView.setRateTextRefresh("--","--","--");
            tv_currPercentDiff.setBackgroundResource(R.drawable.shape_llbt_round_gray);
            tv_currDiff.setTextColor(getResources().getColor(R.color.lianlong_color_bdc7cb));
        }

//        OHLCItem em = new OHLCItem();
//        em.setTimeStamp(oldSingleMap.get("priceTime"));
//        Log.i("info","tiem ===" + oldSingleMap.get("priceTime"));
//        em.setOpen(Double.parseDouble(PrmsNewPricesActivity.parseStringPattern4(oldSingleMap.get("openPrice")+"" ,5)));
//        em.setHigh(Double.parseDouble(oldSingleMap.get("maxPrice")));
//        em.setLow(Double.parseDouble(oldSingleMap.get("minPrice")));
//        if(em != null){
//            queryKTendencyView.setPriceAndTimeText(em);
//        }


        if (!StringUtil.isNull(oldSingleMap.get("priceTime")) && !StringUtil.isNull(oldSingleMap.get("openPrice")) && !StringUtil.isNull(oldSingleMap.get("maxPrice")) && !StringUtil.isNull(oldSingleMap.get("minPrice"))) {
            queryKTendencyView.setPriceAndTimeText(oldSingleMap.get("priceTime"),oldSingleMap.get("openPrice"), oldSingleMap.get("maxPrice"), oldSingleMap.get("minPrice"));
        }

        totalDataList = new ArrayList<Map<String, Object>>();
        totalDataList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap().get(Prms.PRMS_PRICE);
//        Map<String, Object> currentItem = totalDataList.get(position);
//
//        String buyRate = String.valueOf(currentItem
//                .get(Prms.PRELOGIN_QUERY_BUYRATE));
//        String sellRate = String.valueOf(currentItem
//                .get(Prms.PRELOGIN_QUERY_SELLRATE));
//        Integer formatNum = (Integer)DictionaryData.getParamByKeyAndValue(sourceCurrencyCodeStr,targetCurrencyStr, DictionaryData.getInvestCurrencyCodeList());
//
//        if(!"null".equals(buyRate) && !"null".equals(sellRate)){
//            sellRate = PrmsNewPricesActivity.parseStringPattern4(sellRate,formatNum);
//            buyRate = PrmsNewPricesActivity.parseStringPattern4(buyRate,formatNum);
//            tv_saleRate.setText("卖出价" + sellRate);
//            tv_buyRate.setText("买入价" + buyRate );
//        }else{
//            tv_saleRate.setText("卖出价 --" );
//            tv_buyRate.setText("买入价 --");
//        }


        String buyRate = String.valueOf(oldSingleMap.get("buyRate"));
        String sellRate = String.valueOf(oldSingleMap.get("sellRate"));
        Integer formatNum = (Integer)DictionaryData.getParamByKeyAndValue(sourceCurrencyCodeStr,targetCurrencyStr, DictionaryData.getInvestCurrencyCodeList());

        if(!"null".equals(buyRate) && !"null".equals(sellRate)){
            sellRate = PrmsNewPricesActivity.parseStringPattern4(sellRate,formatNum);
            buyRate = PrmsNewPricesActivity.parseStringPattern4(buyRate,formatNum);
            tv_saleRate.setText("卖出价" +sellRate );
            tv_buyRate.setText("买入价" + buyRate );
        }else{
            tv_saleRate.setText("卖出价 --" );
            tv_buyRate.setText("买入价 --");
        }

    }


    private void getRefreshData(QuerySingleQuotationResult Item) {
        //发送单笔详情
//        Map<String, Object> currentItem = new HashMap<String, Object>();
//        for (int i = 0; i < totalDataList.size(); i++) {
//            currentItem = totalDataList.get(position);
//            targetCurrencyStr = (String) currentItem.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
//            sourceCurrencyCodeStr = (String) currentItem.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
//            if (ccygrp.equals(sourceCurrencyCodeStr + targetCurrencyStr)) {
//                String buyRate = String.valueOf(currentItem
//                        .get(Prms.PRELOGIN_QUERY_BUYRATE));
//                String sellRate = String.valueOf(currentItem
//                        .get(Prms.PRELOGIN_QUERY_SELLRATE));
//                tv_saleRate.setText("卖出价" + sellRate);
//                tv_buyRate.setText("买入价" + buyRate);
//                //今日涨跌幅
//                String currPercentDiff = (String) currentItem.get("currPercentDiff");
//                tv_rise.setText(currPercentDiff);
//                getQueryAverageTendency();
//                getQueryKTendencyData();
//                break;
//            }
//        }
//        targetCurrencyStr = (String) list.get(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE);
//        sourceCurrencyCodeStr = (String) list.get(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE);
        targetCurrencyStr = Item.targetCurrencyCode;
        sourceCurrencyCodeStr = Item.sourceCurrencyCode;
//        if (ccygrp.equals(sourceCurrencyCodeStr + targetCurrencyStr)) {
        String buyRate = String.valueOf(Item.buyRate);
        String sellRate = String.valueOf(Item.sellRate);
        Integer formatNum = (Integer)DictionaryData.getParamByKeyAndValue(sourceCurrencyCodeStr,targetCurrencyStr, DictionaryData.getInvestCurrencyCodeList());

        if(!"null".equals(buyRate) && !"null".equals(sellRate)){
            sellRate = PrmsNewPricesActivity.parseStringPattern4(sellRate,formatNum);
            buyRate = PrmsNewPricesActivity.parseStringPattern4(buyRate,formatNum);
            tv_saleRate.setText("卖出价" +sellRate );
            tv_buyRate.setText("买入价" + buyRate );
        }else{
            tv_saleRate.setText("卖出价 --" );
            tv_buyRate.setText("买入价 --");
        }
        if(null !=Item.referPrice){
            tv_referPrice.setText(Item.referPrice);
//            tv_currDiff.setText(Item.currDiff);
//            if(Item.currDiff.contains("+")){
//                tv_currDiff.setText("+"+PrmsNewPricesActivity.parseStringPattern4(Item.currDiff,5));
//            }else if(Item.currDiff.contains("-")){
//                tv_currDiff.setText(PrmsNewPricesActivity.parseStringPattern4(Item.currDiff,5));
//            }else{
//                tv_currDiff.setText(PrmsNewPricesActivity.parseStringPattern4(Item.currDiff,5));
//            }
//            tv_currDiff.setText(PrmsNewPricesActivity.parseStringPattern4(Item.currDiff,5));
//            tv_currPercentDiff.setText(Item.currPercentDiff);
            String currDiff =Item.currDiff;

            currDiff = StringTools.parseStringPattern(currDiff,5);
            currDiff = StringTools.subZeroAndDot(currDiff);
            tv_currDiff.setText(currDiff);
            tv_currPercentDiff.setText( StringTools.parseStringPattern5(Item.currPercentDiff));
//            tv_currPercentDiff.setText(PrmsNewPricesActivity.parseStringPattern4(Item.currPercentDiff,3));
//            String currDiff = Item.currDiff;
            if(null !=currDiff &&!"null".equals(currDiff)&&!"".equals(currDiff)){
                if(Item.currDiff.contains("+")){
                    tv_currDiff.setTextColor(getResources().getColor(R.color.btn_pink));
                }else if(Item.currDiff.contains("-")){
                    tv_currDiff.setTextColor(getResources().getColor(R.color.fonts_green));
                }else{
                    tv_currDiff.setTextColor(getResources().getColor(R.color.lianlong_color_bdc7cb));
                }
            }else{
                tv_currDiff.setText("--");
                tv_currDiff.setTextColor(getResources().getColor(R.color.lianlong_color_bdc7cb));
            }

            String  currPercentdiff =Item.currPercentDiff;
            if(null !=currPercentdiff &&!"null".equals(currPercentdiff)&&!"".equals(currPercentdiff)){
                if(Item.currPercentDiff.contains("+")){
                    tv_referPrice.setTextColor(getResources().getColor(R.color.btn_pink));
                    tv_currPercentDiff.setBackgroundResource(R.color.btn_pink);
                    queryKTendencyView.setLandscapeTitleBackground(getResources().getColor(R.color.boc_text_color_red));
                }else if(Item.currPercentDiff.contains("-")){
                    tv_referPrice.setTextColor(getResources().getColor(R.color.fonts_green));
                    tv_currPercentDiff.setBackgroundResource(R.color.fonts_green);
                    queryKTendencyView.setLandscapeTitleBackground(getResources().getColor(R.color.share_green_color));
                }else{
                    tv_referPrice.setTextColor(getResources().getColor(R.color.lianlong_color_bdc7cb));
                    tv_currPercentDiff.setBackgroundResource(R.drawable.shape_llbt_round_gray);
                    queryKTendencyView.setLandscapeTitleBackground(getResources().getColor(R.color.share_gray_color));
                }
            }else {
                tv_currPercentDiff.setText("--");
                tv_currPercentDiff.setBackgroundResource(R.drawable.shape_llbt_round_gray);
                tv_referPrice.setTextColor(getResources().getColor(R.color.lianlong_color_bdc7cb));
                queryKTendencyView.setLandscapeTitleBackground(getResources().getColor(R.color.share_gray_color));
            }
            queryKTendencyView.setRateTextRefresh(tv_referPrice.getText().toString(),
                    tv_currDiff.getText().toString(),tv_currPercentDiff.getText().toString());
        }else{
            tv_referPrice.setText("--");
            tv_currDiff.setText("--");
            tv_currPercentDiff.setText("--");
            queryKTendencyView.setRateTextRefresh("--","--","--");
            tv_currPercentDiff.setBackgroundResource(R.drawable.shape_llbt_round_gray);
            tv_currDiff.setTextColor(getResources().getColor(R.color.lianlong_color_bdc7cb));
        }

        if (!StringUtil.isNull(Item.priceTime) && !StringUtil.isNull(Item.openPrice) && !StringUtil.isNull(Item.maxPrice) && !StringUtil.isNull(Item.minPrice)) {
            queryKTendencyView.setPriceAndTimeText(Item.priceTime, Item.openPrice, Item.maxPrice, Item.minPrice);
        }


//        Log.i("info","走了。。。。。");

//        getBackGroundLayout().setTitleText(LocalData.code_Map.get(ccygrp));

    }

    /** 显示底部弹出的货币对*/
    public void showDialog(){
        View  contentView  = View.inflate(this, R.layout.foreign_details_dialog, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(this).setView(contentView);  //先得到构造器
        ListView listView = (ListView) contentView.findViewById(R.id.listview_currency);
        TextView itemTv = (TextView) contentView.findViewById(R.id.cancel_button);
        ForeignDialogAdapter foreignDialogAdapter = new ForeignDialogAdapter(PrmsNewPricesDetailActivity.this, totalDataList);
        listView.setAdapter(foreignDialogAdapter);
        foreignDialogAdapter.setItemClickListener(codeItemQuery);
        dialog = builder.create();
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);  //设置位置
        dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); //设置dialog的宽高属性
        dialog.show();

        itemTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    /** 货币对点击事件*/
    private AdapterView.OnItemClickListener codeItemQuery = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            sp_position = position;
//            String currncy = sort_code_List.get(position);
            Map<String,Object> selectCcygrpsMap = totalDataList.get(position);
            ccygrp = getCcygrps(selectCcygrpsMap);
            currencyLeftName = (String)LocalData.Currency.get(sourceCurrencyCodeStr);
            currencyRigthName = (String)LocalData.Currency.get(targetCurrencyStr);
            if(LocalData.goldLists.contains(currencyLeftName)||LocalData.goldLists.contains(currencyRigthName)){
                currencyLeftName = (String)LocalData.code_Map_Left.get(sourceCurrencyCodeStr);
                currencyRigthName = (String)LocalData.code_Map_Right.get(targetCurrencyStr);
            }
            currencyLeft.setText(currencyLeftName);
            currencyRight.setText(currencyRigthName);
            queryKTendencyView.setLandscapeCurCode(currencyLeftName+"/"+currencyRigthName);
            //刷新
//            kType = "OK";
//            tendencyType = "O";
            getQuerySingelQuotation(ccygrp);
//            dialog.dismiss();
            BaseDroidApp.getInstanse().dismissMessageDialog(); // 关闭弹框
        }
    };


    /** 获取到请求是的货币对信息*/
    public String getCcygrps(Map<String,Object> map){
        sourceCurrencyCodeStr = (String) map.get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE);
        if (LocalData.Currency.containsKey(sourceCurrencyCodeStr)) {
            sourceDealCode = LocalData.Currency.get(sourceCurrencyCodeStr);// 得到源货币的代码

        }
        targetCurrencyStr = (String) map.get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE);
        if (LocalData.Currency.containsKey(targetCurrencyStr)) {
            targetDealCode = LocalData.Currency.get(targetCurrencyStr); //得到目标货币代码
        }

        ccygrp = sourceCurrencyCodeStr + targetCurrencyStr;
        return ccygrp;
    }


    //单笔详情
    public void getQuerySingelQuotation(final String ccygrp) {
//        map.put("ccygrp",ccygrp);//货币对代码
//        map.put("cardType","G");
//        map.put("cardClass", "R");;
        QuerySingelQuotationRequestParams querySingelQuotation = new QuerySingelQuotationRequestParams(ccygrp, "G", "R");
        HttpHelp h = HttpHelp.getInstance();
        h.postHttpFromSF(this, querySingelQuotation);
        h.setHttpErrorCallBack(new IHttpErrorCallBack() {
            @Override
            public boolean onError(String exceptionMessage, Object extendParams) {
                MessageDialog.closeDialog(); //关闭通讯框
                return true;
            }
        });
        h.setOkHttpErrorCode(new IOkHttpErrorCode() {
            @Override
            public boolean handlerErrorCode(BaseResponseData responseData, Object extendParams) {
                MessageDialog.closeDialog(); //关闭通讯框

                return true;
            }
        });
        h.setHttpResponseCallBack(new IHttpResponseCallBack() {
            @Override
            public boolean responseCallBack(String response, Object extendParams) {
                QuerySingleQuotationResponseData data = GsonTools.fromJson(response, QuerySingleQuotationResponseData.class);
                //单笔行情 wuhan要改
//                Map<String,String> body = (Map<String ,String>)data.getBody();

                QuerySingleQuotationResult item = data.getBody();

                getRefreshData(item);
                queryKTendencyView.resetData();
//                getQueryAverageTendency();
                getQueryKTendencyData(ccygrp);

                return false;
            }
        });
    }

    TimerRefreshTools mTimerRefreshToolsSingel = new TimerRefreshTools(7000, new TimerRefreshTools.ITimerRefreshListener() {
        @Override
        public void onRefresh() {
            QuerySingelQuotationRequestParams querySingelQuotation = new QuerySingelQuotationRequestParams(ccygrp, "G", "R");
            HttpHelp h = HttpHelp.getInstance();
            h.postHttpFromSF(PrmsNewPricesDetailActivity.this, querySingelQuotation);
            h.setHttpErrorCallBack(new IHttpErrorCallBack() {
                @Override
                public boolean onError(String exceptionMessage, Object extendParams) {
                    MessageDialog.closeDialog(); //关闭通讯框
//                    queryKTendencyView.startRefreshData();
                    return true;
                }
            });
            h.setOkHttpErrorCode(new IOkHttpErrorCode() {
                @Override
                public boolean handlerErrorCode(BaseResponseData responseData, Object extendParams) {
                    MessageDialog.closeDialog(); //关闭通讯框
//                    queryKTendencyView.startRefreshData();
                    return true;
                }
            });

                    h.setHttpResponseCallBack(new IHttpResponseCallBack() {
                @Override
                public boolean responseCallBack(String response, Object extendParams) {
                    QuerySingleQuotationResponseData data = GsonTools.fromJson(response, QuerySingleQuotationResponseData.class);
                    //单笔行情 wuhan要改
//                Map<String,String> body = (Map<String ,String>)data.getBody();

                    QuerySingleQuotationResult item = data.getBody();
//                    Log.i("info","dddddddddddd==item ==" + item.buyRate);
                    getRefreshData(item);
//                    mTabView.setCurSelectedIndex(0);
//                    getQueryAverageTendency();
//                    getQueryKTendencyData();
                    return false;
                }
            });
        }
    });



    private void initListener() {
//        btn_back.setOnClickListener(this);
//        btn_share.setOnClickListener(this);
//        btn_QRcode.setOnClickListener(this);

        btn_buy.setOnClickListener(this);
        btn_sale.setOnClickListener(this);
//        sp_currency.setOnItemSelectedListener(this);

//        tv_onehour.setOnClickListener(this);
//        tv_fourhour.setOnClickListener(this);.
//        tv_day.setOnClickListener(this);
//        tv_week.setOnClickListener(this);
//        tv_mounth.setOnClickListener(this);
//        btn_KOrZ.setOnClickListener(this);

    }

    boolean isbuysale = false;
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                PrmsNewPricesDetailActivity.this.finish();
                break;
            case R.id.btn_share:

                break;

            case R.id.btn_QRcode:

                break;
//            case R.id.btn_KOrZ:
//                if (isK) {
//                    isK = false; //折线图
//                    //发送折线图请求String ccygrp,String tendencyType
//                    //货币对代码,趋势类型
//                    requestqueryAverageTendency(ccygrp, tendencyType);
//                } else {
//                    isK = true; //K线图
//                    //发送K线图请求String ccygrp,String kType,String timeZone
//                    //货币对代码,趋势类型,时间区间
//                    requestqueryKTendency(ccygrp, kType, "");
//                }
//
//                break;
//            case R.id.tv_onehour:
//                tendencyType = "O";
//                kType = "OK";
//                initDrawable(tendencyType, kType);
//                break;
//            case R.id.tv_fourhour:
//                kType = "FK";
//                tendencyType = "F";
//                initDrawable(tendencyType, kType);
//                break;
//            case R.id.tv_day:
//                kType = "DK";
//                tendencyType = "D";
//                initDrawable(tendencyType, kType);
//                break;
//            case R.id.tv_week:
//                kType = "WK";
//                tendencyType = "W";
//                initDrawable(tendencyType, kType);
//                break;
//            case R.id.tv_mounth:
//                kType = "MK";
//                tendencyType = "M";
//                initDrawable(tendencyType, kType);
//                break;
            case R.id.btn_buy:
                mTimerRefreshToolsSingel.stopTimer();
                queryKTendencyView.stopRefreshData();
                isbuysale = true;
                if (BaseDroidApp.getInstanse().isLogin()) {
                    flag = BUY;

//                    BaseHttpEngine.showProgressDialog();
//                    queryPrmsAccBalance();
                    if(enters !=1 && enters!= 3 ){
                        BaseHttpEngine.showProgressDialog();
//                                queryPrmsAccBalance();
                        checkRequestPsnInvestmentManageIsOpen();
                    }else{
                        BaseHttpEngine.showProgressDialog();
                        queryPrmsAccBalance();
                    }
                } else {
                    BaseActivity.getLoginUtils(PrmsNewPricesDetailActivity.this).exe(new LoginTask.LoginCallback() {

                        @Override
                        public void loginStatua(boolean isLogin) {
                            if (isLogin) {
                                flag = BUY;
                                btn_sale.setVisibility(View.VISIBLE);
                                BaseHttpEngine.showProgressDialog();
//                                queryPrmsAccBalance();
                                checkRequestPsnInvestmentManageIsOpen();
                            }
                        }
                    });
                }
                break;
            case R.id.btn_sale:
                mTimerRefreshToolsSingel.stopTimer();
                queryKTendencyView.stopRefreshData();
                isbuysale = true;
                if (BaseDroidApp.getInstanse().isLogin()) {
                    flag = SALE;
                    if(enters !=1 && enters!= 3 ){
                        BaseHttpEngine.showProgressDialog();
//                                queryPrmsAccBalance();
                        checkRequestPsnInvestmentManageIsOpen();
                    }else{
                        BaseHttpEngine.showProgressDialog();
                        queryPrmsAccBalance();
                    }
                } else {
                    BaseActivity.getLoginUtils(PrmsNewPricesDetailActivity.this).exe(new LoginTask.LoginCallback() {
                        @Override
                        public void loginStatua(boolean isLogin) {
                            if (isLogin) {
                                flag = SALE;

                                BaseHttpEngine.showProgressDialog();
//                                queryPrmsAccBalance();
                                checkRequestPsnInvestmentManageIsOpen();
                            }
                        }
                    });
                }
                break;

            default:

                break;
        }
    }




//    //发送折线图
//    @Override
//    public void requestqueryAverageTendencyCallBack(Object resultObj) {
//        super.requestqueryAverageTendencyCallBack(resultObj);
//        BiiResponse biiResponse = (BiiResponse) resultObj;
//
//        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//        if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
//            BaseHttpEngine.dissMissProgressDialog();
//            return;
//        }
//
//
//
//
//    }
//
//    //发送K线图
//    @Override
//    public void requestqueryKTendencyCallBack(Object resultObj) {
//        super.requestqueryKTendencyCallBack(resultObj);
//        BiiResponse biiResponse = (BiiResponse) resultObj;
//
//        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//        if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) {
//            BaseHttpEngine.dissMissProgressDialog();
//            return;
//        }
//
//    }
//
//    @Override
//    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> adapterView) {
//
//    }


    @Override
    public void queryPrmsAccBalanceCallBack(Object resultObj) {
        BaseHttpEngine.dissMissProgressDialog();
        super.queryPrmsAccBalanceCallBack(resultObj);
        if (isbalance)
            return;
        Intent intent;
        switch (flag) {
            case BUY:// 买入
                List<Map<String, Object>> buyList = prmsControl
                        .getBuyCurrencyList(prmsControl.accBalanceList);
                for (Map<String, Object> map : buyList) {
                    if (map.get(Prms.QUERY_PEMSACTBALANCE_CODE).equals(
                            targetCurrencyStr)) {// 买入的时候
                        // 用目标币种和
                        // 持仓比对
//                        BaseDroidApp.getInstanse().getBizDataMap()
//                                .put(Prms.PRMS_PRICE, dataList);
                        intent = new Intent();
                        intent.setClass(this, PrmsTradeBuyActivity.class);
                        intent.putExtra(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE,
                                targetCurrencyStr);
                        intent.putExtra(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE,
                                sourceCurrencyCodeStr);
                        intent.putExtra(Prms.PRMS_IFFROMPRICE, true);
                        isbuysale = false;
                        startActivity(intent);
                        return;
                    }
                }
                StringBuffer errorInfoSb = new StringBuffer();
                errorInfoSb
                        .append(getString(R.string.prms_balancesale_null_error1));
                errorInfoSb.append(LocalData.Currency.get(targetCurrencyStr));
                errorInfoSb
                        .append(getString(R.string.prms_balancesale_null_error2));
                BaseDroidApp.getInstanse().showInfoMessageDialog(
                        errorInfoSb.toString());
                break;
            case SALE:// 卖出
                List<Map<String, Object>> saleList = prmsControl
                        .getSaleCurrencyList(prmsControl.accBalanceList);
                if (saleList.size() < 1) {
                    BaseDroidApp.getInstanse().showInfoMessageDialog(
                            getString(R.string.prms_balanceOne_null_error));
                    return;
                }
                for (Map<String, Object> map : saleList) {// 卖出用 源币种比对
                    if (map.get(Prms.QUERY_PEMSACTBALANCE_CODE).equals(
                            sourceCurrencyCodeStr)) {
//                        BaseDroidApp.getInstanse().getBizDataMap()
//                                .put(Prms.PRMS_PRICE, dataList);
                        intent = new Intent();
                        intent.setClass(this, PrmsTradeSaleActivity.class);
                        intent.putExtra(Prms.QUERY_TRADERATE_SOURCECURRENCYCODE,
                                sourceCurrencyCodeStr);
                        intent.putExtra(Prms.QUERY_TRADERATE_TARGETCURRENCYCODE,
                                targetCurrencyStr);
                        intent.putExtra(Prms.PRMS_IFFROMPRICE, true);
                        isbuysale = false;
                        startActivity(intent);
                        return;
                    }
                }
                BaseDroidApp.getInstanse().showInfoMessageDialog(
                        getString(R.string.prms_balanceOne_null_error));
                break;
//            case FASTBUY:// 快速交易买入
//                prmsControl.getBuyCurrencyList(prmsControl.accBalanceList);
//                BaseDroidApp.getInstanse().getBizDataMap()
//                        .put(Prms.PRMS_PRICE, dataList);
//                intent = new Intent();
//                intent.setClass(this, PrmsTradeBuyActivity.class);
//                intent.putExtra(Prms.PRMS_IFFROMPRICE, true);
//                startActivity(intent);
//                break;
//            case FASTSALE:// 快速交易没有 持仓
//                if (prmsControl.getSaleCurrencyList(prmsControl.accBalanceList)
//                        .size() < 1) {
//                    BaseDroidApp.getInstanse().showInfoMessageDialog(
//                            getString(R.string.prms_balanceAll_null_error));
//                    return;
//                }
//                intent = new Intent();
//                intent.setClass(this, PrmsTradeSaleActivity.class);
//                BaseDroidApp.getInstanse().getBizDataMap()
//                        .put(Prms.PRMS_PRICE, dataList);
//                intent.putExtra(Prms.PRMS_IFFROMPRICE, true);
//                startActivity(intent);
//                break;

            default:
                break;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        queryKTendencyView.startRefreshData();
        mTimerRefreshToolsSingel.startTimer();

    }

    @Override
    public void onBackPressed() {
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){//横屏
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置竖屏
        }else{
            finish();
        }
    }


}
