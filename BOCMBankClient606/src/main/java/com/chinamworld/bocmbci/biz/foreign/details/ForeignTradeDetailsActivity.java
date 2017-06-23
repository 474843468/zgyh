package com.chinamworld.bocmbci.biz.foreign.details;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.biz.foreign.Foreign;
import com.chinamworld.bocmbci.biz.foreign.ForeignBaseActivity;
import com.chinamworld.bocmbci.biz.foreign.ForeignDataCenter;
import com.chinamworld.bocmbci.biz.foreign.StringTools;
import com.chinamworld.bocmbci.biz.forex.rate.ForexQuickCurrentSubmitActivity;
import com.chinamworld.bocmbci.biz.forex.rate.ForexQuickTradeSubmitActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
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
import com.chinamworld.bocmbci.net.GsonTools;
import com.chinamworld.bocmbci.net.HttpHelp;
import com.chinamworld.bocmbci.net.model.BaseResponseData;
import com.chinamworld.bocmbci.net.model.IHttpErrorCallBack;
import com.chinamworld.bocmbci.net.model.IHttpResponseCallBack;
import com.chinamworld.bocmbci.net.model.IOkHttpErrorCode;
import com.chinamworld.bocmbci.userwidget.qrcodeview.InvestQRCodeActivity;
import com.chinamworld.bocmbci.userwidget.sfkline.IRefreshKLineDataListener;
import com.chinamworld.bocmbci.userwidget.sfkline.InverstKLineView;
import com.chinamworld.bocmbci.utils.KeyAndValueItem;
import com.chinamworld.bocmbci.utils.ListUtils;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.llbt.userwidget.dialogview.MessageDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * 外汇买卖 行情页 &功能外置
 * @see ForeignTradeDetailsActivity
 * @author luqp 2016年9月22日
 */
public class ForeignTradeDetailsActivity extends ForeignBaseActivity implements View.OnClickListener{
    private static final String TAG = "ForeignTradeDetailsActivity";
    private Context context = this;
    /** 得到源货币的代码*/
    private String sourceDealCode = null;
    /** 得到目标货币代码*/
    private String targetDealCode = null;
    /** 买入价*/
    private TextView buyRate = null;
    /** 卖出价*/
    private TextView sellRate = null;
    /** 收藏图标*/
    private ImageView detailsCollectImg = null;
    /** 收藏图标 是否隐藏*/
    private LinearLayout detailsCollectLl = null;
    /** K线图*/
    private InverstKLineView queryKTendencyView;
    /** 卖出*/
    private Button detailsSellClick = null;
    /** 买入*/
    private Button detailsBuyClick = null;
    /** 得到用户选择的数据*/
    private Map<String, Object> selectPositionMap;
    /** 得到全部数据*/
    private List<Map<String,Object>> selectPositionList;
    /** 请求标志 0-快速交易 1-卖出价 2-买入价  3-外置时是否开通投资理财*/
    private int requestTag = -1;
    /** 0-全部货币对，1-用户定制的货币战对 */
    private int allOrCustomerReq = -1;
    /** 用于区别是定期还是活期请求查询买入币种列表信息 1-活期 2-定期 */
    private int fixOrCurrency = -1;
    /** 点击快速交易还是银行卖价，1-快速交易，2-银行卖价,3-银行买价 */
    private int quickOrRateSell = 1;
    /** 银行卖价 目标货币对 卖出币种代码 */
    private String targetCode = null;
    /** 银行买价 目标货币对 买入币种代码 */
    private String sourceCurCde = null;
    /** 处理前，买入币种代码 */
    private List<String> buyCodeList = null;
    /** 处理后，买入币种名称 */
    private List<String> buyCodeDealList = null;
    /** 区分 查询自选还是 全部 1:自选 2:全部*/
    private int queryTag;
    /** 货币对*/
    private String sourceCurrencyCode;
    private String targetCurrencyCode;
    /** 查询的货币对*/
    private String ccygrps;
    /** 查询外汇F*/
    /** 查询时间*/
    private String timeZones;
    /** 得到货币对数据*/
    private List<Map<String,Object>> allRateList;
    private Dialog dialog;
    /** 提交货币对 */
    private String selectedArr[] = null;
    /** 标题货币对*/
    private TextView currencyLeft;
    private TextView currencyRight;
    private int selectPosition;
    /** 中间价*/
    private  TextView middleRateTv;
    /** 张跌值*/
    private  TextView currDiffTv;
    /** 涨跌幅*/
    private  TextView currPercentDiffTv;
    /** 涨跌幅*/
    private  LinearLayout currPercentDiffLl;
    /** 买入卖出价*/
    private String selectBuyRate;
    private String selectSellRate;
    /** 自选全部数据*/
    private List<Map<String, Object>> customerRateList = null;
    /** 自选货币对数据*/
    private List<Map<String, Object>> optionalList = null;
    /** 涨跌值*/
    private String curRDiff;
    /** 涨跌幅*/
    private String currPercentDiff;
    /** 全部收藏货币对*/
    private List<String> allCcygrps = null;
    /** 如果日元格式化小数位2位*/
    private Boolean currency;
    /**二维码扫描直接进入详情页面*/
    private boolean isQrcode = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menuOrTrade = 1;
        taskMark = 1;
        isQrcode = getIntent().getBooleanExtra("isQrcode",false);
        initData(); // 获取用户选择的数据
        if (!isQrcode) {
            initViewData();
        }
    }

    public void initViewData(){
        initView(); // 初始化所有控件
        initTitleClick(); // Title点击事件
        initClick(); // 点击事件处理
        BaseHttpEngine.showProgressDialog();
        getQuerySingelQuotation(ccygrps);//获取单笔行情查询
    }


    /** 根据登录状态显示页面信息*/
    @Override
    protected void onResumeFromLogin(final boolean isLogin) {
        super.onResumeFromLogin(isLogin);
        if(!isQrcode) { // 不是二维码进入
            if (isLogin) { //已登录
                commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
                BaseHttpEngine.showProgressDialogCanGoBack();
                if (StringUtil.isNull(commConversationId)) {
                    requestCommConversationId();  // 请求登录后的CommConversationId
                } else {
                    requestPsnInvestmentManageIsOpen(); // 判断用户是否开通投资理财服务
                }
                detailsCollectLl.setVisibility(View.VISIBLE);
            } else { // 未登录 未登录不能收藏隐藏收藏按钮
                detailsCollectLl.setVisibility(View.INVISIBLE);
            }
        }
    }

    /** 查询K线图   2.3 外汇、贵金属K线图查询 --API*/
    public void getQueryKTendencyData(String ccygrp){
        QuerykTendencyRequestParams queryKTendency= new QuerykTendencyRequestParams(ccygrp, kTypes, cardTypes, cardClasss, timeZones);
        HttpHelp help = HttpHelp.getInstance();
        help.postHttpFromSF(this,queryKTendency);
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
                QueryKTendencyResponseData data= GsonTools.fromJson(response, QueryKTendencyResponseData.class);
                QueryKTendencyResult body = data.getBody();
                queryKTendencyView.setKLineData(body);
                return false;
            }
        });
    }

    /** 查询趋势图    2.4 外汇、贵金属趋势图查询 -API*/
    public void getQueryAverageTendency(String ccygrp){
        QueryAverageTendencyRequestParams queryAverageTendency = new QueryAverageTendencyRequestParams(ccygrp,cardTypes,cardClasss,tendencyType);
        HttpHelp help = HttpHelp.getInstance();
        help.postHttpFromSF(this,queryAverageTendency);
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
                QueryAverageTendencyResponseData result = GsonTools.fromJson(response,QueryAverageTendencyResponseData.class);
                QueryAverageTendencyResult body =  result.getBody();
                // //该方法已在控件中实现  linyl
//                queryKTendencyView.setQuShiData("yyyy\nMM-dd","yyyy-MM-dd HH:mm:ss","参考价格：","时间：","参考价格",2,"yyyy-MM-dd HH:mm:ss","暂无数据");
                queryKTendencyView.setECharsViewData(body);
                return false;
            }
        });
    }

    /**  单笔行情查询   2.4 外汇、贵金属趋势图查询 -API*/
    public void getQuerySingelQuotation(String ccygrp){
        QuerySingelQuotationRequestParams querySingelQuotation = new QuerySingelQuotationRequestParams(ccygrp,cardTypes,cardClasss);
        HttpHelp help = HttpHelp.getInstance();
        help.postHttpFromSF(this,querySingelQuotation);
        help.setHttpErrorCallBack(new IHttpErrorCallBack() {
            @Override
            public boolean onError(String exceptionMessage, Object extendParams) {
                BaseHttpEngine.dissMissProgressDialog();
                MessageDialog.closeDialog(); //关闭通讯框
                return true;
            }
        });
        help.setOkHttpErrorCode(new IOkHttpErrorCode() {
            @Override
            public boolean handlerErrorCode(BaseResponseData responseData, Object extendParams) {
                BaseHttpEngine.dissMissProgressDialog();
                MessageDialog.closeDialog(); //关闭通讯框
                return true;
            }
        });
        help.setHttpResponseCallBack(new IHttpResponseCallBack() {
            @Override
            public boolean responseCallBack(String response, Object extendParams) {
                BaseHttpEngine.dissMissProgressDialog();
                QuerySingleQuotationResponseData result = GsonTools.fromJson(response,QuerySingleQuotationResponseData.class);
                QuerySingleQuotationResult body =  result.getBody();
                String strReferPrice = body.referPrice; //中间价
                curRDiff = body.currDiff; // 涨跌值
                currPercentDiff = body.currPercentDiff; //涨跌幅
                String date = body.priceTime; // 单笔详情时间
                String openPrices = body.openPrice; //开盘价
                String maxPrices = body.maxPrice; //最高价
                String minPrices = body.minPrice; //最低价
                middleRateTv.setText(strReferPrice);
                curRDiff = StringTools.subZeroAndDot(curRDiff);
                int number = StringTools.splitStringwith2pointnew(curRDiff); // 判断小数位后面几位
                if (number>=5){
                    curRDiff = StringTools.parseStringPattern(curRDiff,5);
                }
                currDiffTv.setText(curRDiff);
                currPercentDiff = StringTools.parseStringPattern5(currPercentDiff);
                currPercentDiffTv.setText(currPercentDiff);
                queryKTendencyView.setRateTextRefresh(strReferPrice,curRDiff,currPercentDiff);

                if (!StringUtil.isNull(date) && !StringUtil.isNull(openPrices) && !StringUtil.isNull(maxPrices) && !StringUtil.isNull(minPrices)) {
                    queryKTendencyView.setPriceAndTimeText(date, openPrices, maxPrices, minPrices);
                }

                if (!StringUtil.isNull(curRDiff) && !StringUtil.isNull(currPercentDiff)) {
                    currPercentDiffLl.setVisibility(View.VISIBLE);
                    if (curRDiff.contains("+") || currPercentDiff.contains("+")) { // 涨
                        currDiffTv.setTextColor(getResources().getColor(R.color.boc_text_color_red));
                        currPercentDiffLl.setBackgroundColor(getResources().getColor(R.color.boc_text_color_red));
                        middleRateTv.setTextColor(getResources().getColor(R.color.boc_text_color_red));
                        queryKTendencyView.setLandscapeTitleBackground(getResources().getColor(R.color.boc_text_color_red));
                        return true;
                    }
                    if (curRDiff.contains("-") || currPercentDiff.contains("-")) { //跌
                        currDiffTv.setTextColor(getResources().getColor(R.color.share_green_color));
                        currPercentDiffLl.setBackgroundColor(getResources().getColor(R.color.share_green_color));
                        middleRateTv.setTextColor(getResources().getColor(R.color.share_green_color));
                        queryKTendencyView.setLandscapeTitleBackground(getResources().getColor(R.color.share_green_color));
                        return true;
                    } else { //持平
                        currDiffTv.setTextColor(getResources().getColor(R.color.share_gray_color));
                        currPercentDiffLl.setBackgroundColor(getResources().getColor(R.color.share_gray_color));
                        middleRateTv.setTextColor(getResources().getColor(R.color.share_gray_color));
                        queryKTendencyView.setLandscapeTitleBackground(getResources().getColor(R.color.share_gray_color));
                        return true;
                    }
                } else { // 如果涨跌幅张跌值为空 隐藏布局
                    currPercentDiffLl.setVisibility(View.GONE);
                    return true;
                }
            }
        });
    }

    /** 初始化所有数据*/
    public void initData(){
        if(isQrcode){ // 用户扫描二维码进入详情页面的数据
            if (BaseDroidApp.getInstanse().isLogin()) { // 已登陆请求登陆后的接口
                BaseHttpEngine.showProgressDialog();
                requestPsnAllRates();
            } else { // 未登陆请求登陆后的接口
                BaseHttpEngine.showProgressDialog();
                requestAllRatesOutlay(); //请求未登录全部外汇
            }
        } else { // 在外汇首页面跳转用首页面数据
            /** 得到用户选择货币对位置*/
            selectPosition = this.getIntent().getIntExtra(Foreign.SELECTPOSITION, 0);
            /** 得到全部货币对数据*/
            allRateList = ForeignDataCenter.getInstance().getAllRateListData();
            queryTag = this.getIntent().getIntExtra(ConstantGloble.POSITION, 0);
            selectBuyRate = (String) allRateList.get(selectPosition).get(Forex.FOREX_RATE_BUYRATE_RES); // 买入价
            selectSellRate = (String) allRateList.get(selectPosition).get(Forex.FOREX_RATE_SELLRATE_RES); // 卖出价
            ccygrps = getCcygrps(allRateList.get(selectPosition));
            currency = getCurrency(allRateList.get(selectPosition)); // 如果是日元返回true.
        }
    }

    /** 初始化所有控件*/
    public void initView(){
        setContentView(R.layout.foreign_price_details_main);
        getBackGroundLayout().setShareButtonVisibility(View.VISIBLE); //设置分享图标显示
        getBackGroundLayout().setRightButtonVisibility(View.VISIBLE); //设置右按钮显示
        sellRate = (TextView) findViewById(R.id.foreign_details_sell_price); //卖出价
        buyRate = (TextView) findViewById(R.id.foreign_details_buy_price); //买入价
        detailsCollectImg = (ImageView) findViewById(R.id.foreign_details_collect); //收藏图标
        detailsCollectLl = (LinearLayout) findViewById(R.id.ll_foreign_details_collect); //收藏图标 是否隐藏
        queryKTendencyView = (InverstKLineView)findViewById(R.id.query_KTendency_view); // K线图
        detailsSellClick = (Button) findViewById(R.id.foreign_details_button_sell); // 卖出
        detailsBuyClick = (Button)findViewById(R.id.foreign_details_button_buy); // 买入

        middleRateTv = (TextView) findViewById(R.id.foreign_middle_rate); // 中间价
        currDiffTv = (TextView) findViewById(R.id.foreign_currDiff); // 涨跌值
        currPercentDiffTv = (TextView) findViewById(R.id.foreign_currPercentDiff); // 涨跌幅
        currPercentDiffLl = (LinearLayout) findViewById(R.id.ll_foreign_currPercentDiff); // 涨跌幅
        queryKTendencyView.setRrefreshKLineDataListener(queryKTendencyClick);
        queryKTendencyView.setContentText("1小时;4小时;日;周;月");
        queryKTendencyView.setCurSelectedIndex(2); // 默认点击 1小时
        detailsCollectImg.setImageDrawable(getResources().getDrawable(R.drawable.share_not_collected));
        //设置横屏时的货币对数据
        queryKTendencyView.setLandscapeAllRateList(allRateList);
        timeZones = queryKTendencyView.getNewTimeZone(); //获取更新时间
        if (currency){
            selectSellRate = StringUtil.parseStringPattern(selectSellRate,2);
            selectBuyRate = StringUtil.parseStringPattern(selectBuyRate,2);
        } else {
            selectSellRate = StringUtil.parseStringPattern(selectSellRate,4);
            selectBuyRate = StringUtil.parseStringPattern(selectBuyRate,4);
        }
        buyRate.setText(selectSellRate); //卖出价
        sellRate.setText(selectBuyRate); //买入价
        if(isQrcode) {
            if(BaseDroidApp.getInstance().isLogin()){
                detailsCollectLl.setVisibility(View.VISIBLE);
            } else {
                detailsCollectLl.setVisibility(View.GONE);
            }
        }

        if(BaseDroidApp.getInstance().isLogin()){
            detailsSellClick.setVisibility(View.VISIBLE);
        } else {
            detailsSellClick.setVisibility(View.GONE);
        }
    }

    /** 初始化点击事件*/
    public void initClick(){
        detailsCollectImg.setOnClickListener(this); // 收藏图标
        detailsSellClick.setOnClickListener(this); // 卖出
        detailsBuyClick.setOnClickListener(this); // 买入
    }

    /** K线点击事件*/
    private IRefreshKLineDataListener queryKTendencyClick = new IRefreshKLineDataListener() {
        @Override
        public void onRefreshKLineDataCallBack(int nIndex, int nShowType, boolean isBackgroundRefresh) {
            timeZones = queryKTendencyView.getNewTimeZone(); //获取更新时间
            if (nShowType == 1) { //刷新K线图
                if (nIndex == 0){ // 一小时K线图
                    kTypes = "OK";
                } else if(nIndex == 1) { // 四小时K线图
                    kTypes = "FK";
                } else if(nIndex == 2) { // 日K线图
                    kTypes = "DK";
                } else if(nIndex == 3) { // 周K线图
                    kTypes = "WK";
                }else if(nIndex == 4) { // 月K线图
                    kTypes = "MK";
                }
                getQueryKTendencyData(ccygrps); // 刷新K线图数据
            } else if(nShowType == 2){ //刷新曲线图
                if (nIndex == 0){ // 一小时K线图
                    tendencyType = "O";
                } else if(nIndex == 1) { // 四小时K线图
                    tendencyType = "F";
                } else if(nIndex == 2) { // 日K线图
                    tendencyType = "D";
                } else if(nIndex == 3) { // 周K线图
                    tendencyType = "W";
                }else if(nIndex == 4) { // 月K线图
                    tendencyType = "M";
                }
                getQueryAverageTendency(ccygrps); // 刷新趋势图查询
            }
        }
    };

    /** 获取到请求是的货币对信息*/
    public String getCcygrps(Map<String,Object> map){
        sourceCurrencyCode = (String) map.get(Forex.FOREX_RATE_SOURCECODE_RES);
        if (LocalData.Currency.containsKey(sourceCurrencyCode)) {
            sourceDealCode = LocalData.Currency.get(sourceCurrencyCode);// 得到源货币的代码

        }
        targetCurrencyCode = (String) map.get(Forex.FOREX_RATE_TARGETCODE_RES);
        if (LocalData.Currency.containsKey(targetCurrencyCode)) {
            targetDealCode = LocalData.Currency.get(targetCurrencyCode); //得到目标货币代码
        }
        ccygrps = sourceCurrencyCode + targetCurrencyCode;
        return ccygrps;
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.foreign_details_collect: //收藏
                LogGloble.i(TAG,"点击收藏............");
                getSelectedCode();
                break;
            case R.id.foreign_details_button_sell: //卖出
                LogGloble.i(TAG,"点击卖出............");
                if (BaseDroidApp.getInstanse().isLogin()) { // 已登录显示
                    taskMark = 2;
                    requestTag = 1;
                    commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
                    BaseHttpEngine.showProgressDialogCanGoBack();
                    if (StringUtil.isNull(commConversationId)) {
                        requestCommConversationId();  // 请求登录后的CommConversationId
                    } else {
                        requestPsnInvestmentManageIsOpen(); // 判断用户是否开通投资理财服务
                    }
                } else {
                    BaseActivity.getLoginUtils(ForeignTradeDetailsActivity.this).exe(new LoginTask.LoginCallback() { // 登陆跳转
                        @Override
                        public void loginStatua(boolean b) {
                            taskMark = 2;
                            commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
                            if (StringUtil.isNull(commConversationId)) {
                                requestTag = 1;
                                BaseHttpEngine.showProgressDialogCanGoBack();
                                requestCommConversationId();  // 请求登录后的CommConversationId
                            }
                        }
                    });
                }
                break;
            case R.id.foreign_details_button_buy: //买入
                LogGloble.i(TAG,"点击买入............");
                if (BaseDroidApp.getInstanse().isLogin()) { // 已登录显示
                    taskMark = 2;
                    requestTag = 2;
                    commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
                    BaseHttpEngine.showProgressDialogCanGoBack();
                    if (StringUtil.isNull(commConversationId)) {
                        requestCommConversationId();  // 请求登录后的CommConversationId
                    } else {
                        requestPsnInvestmentManageIsOpen(); // 判断用户是否开通投资理财服务
                    }
                } else {
                    BaseActivity.getLoginUtils(ForeignTradeDetailsActivity.this).exe(new LoginTask.LoginCallback() { // 登陆跳转
                        @Override
                        public void loginStatua(boolean b) {
                            taskMark = 2;
                            commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
                            if (StringUtil.isNull(commConversationId)) {
                                requestTag = 2;
                                BaseHttpEngine.showProgressDialogCanGoBack();
                                requestCommConversationId();  // 请求登录后的CommConversationId
                            }
                        }
                    });
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
        BaseHttpEngine.dissMissProgressDialog();
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        Map<String, List<Map<String, Object>>> result = (Map<String, List<Map<String, Object>>>) biiResponseBody
                .getResult();
        if (StringUtil.isNullOrEmpty(result)) {
//            BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.acc_transferquery_null));
            return;
        } else {
            optionalList = result.get(Forex.FOREX_RATE_CUSTOMERRATELIST_RES);
            if (optionalList == null || optionalList.size() <= 0) {
                return;
            }
            optionalList = getCurrencyCodeList(optionalList); //获取支持货币对
            ListUtils.sortForInvest(optionalList, new ListUtils.ISynchronousList<Map<String, Object>,
                    KeyAndValueItem>() {
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
            detailsCollectLl.setVisibility(View.VISIBLE);
            allCcygrps = getCcygrpList(optionalList);
            if (queryTag== 2) { // 自选
                allRateList.clear();
                allRateList = optionalList; //用于更新下拉选数据
            }
            for (int i=0;i<allCcygrps.size();i++){
                String str = allCcygrps.get(i);
                if (ccygrps.contains(str)){
                    detailsCollectImg.setImageDrawable(getResources().getDrawable(R.drawable.share_already_collect));
                    isCollection = true;
                    continue;
                }
            }
        }
    }

    /**
     * 查询外汇账户类型---回调 活期或者是定期 外汇买卖交易使用 快速交易
     * @param resultObj
     *            :返回结果
     */
    @SuppressWarnings("unchecked")
    public void ratePsnForexActIssetCallback(Object resultObj) {
        super.ratePsnForexActIssetCallback(resultObj);
        Map<String, Object> result = getHttpTools().getResponseResult(resultObj);
        String accountType = null;
        if (StringUtil.isNullOrEmpty(result)) {
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(ForeignTradeDetailsActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
            return;
        } else {
            canTwoSided = (String) result.get(Forex.FOREX_RATE_CANTWOSIDED_RES);
            BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_RATE_CANTWOSIDED_RES, canTwoSided);
            // 不含有InvestBindingInfo键
            if (!result.containsKey(Forex.FOREX_RATE_INVESTBINDINGINFO_RES)) {
                BaseHttpEngine.dissMissProgressDialog();
                BaseDroidApp.getInstanse().showInfoMessageDialog(ForeignTradeDetailsActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
                return;
            }
            Map<String, String> investBindingInfo = (Map<String, String>) result.get(Forex.FOREX_RATE_INVESTBINDINGINFO_RES);
            if (StringUtil.isNullOrEmpty(investBindingInfo)) {
                BaseHttpEngine.dissMissProgressDialog();
                BaseDroidApp.getInstanse().showInfoMessageDialog(ForeignTradeDetailsActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
                return;
            } else {
                String investAccount = investBindingInfo.get(Forex.FOREX_RATE_INVESTACCOUNT_RES);
                // 存储投资账号
                BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_RATE_INVESTACCOUNT_RES, investAccount);
                accountType = investBindingInfo.get(Forex.FOREX_RATE_ACCOUNTTYPE_RES);
                if (StringUtil.isNull(accountType)) {
                    BaseHttpEngine.dissMissProgressDialog();
                    BaseDroidApp.getInstanse().showInfoMessageDialog(ForeignTradeDetailsActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
                    return;
                }
            }
            if (!accountType.equals(ConstantGloble.FOREX_ACCTYPE_DQYBT)) {
                accIsCurr();  // 跳转到活期交易页面
            } else {
                if (result.containsKey(Forex.FOREX_VOLUMENUMBERLIST_RES)) {
                    if (StringUtil.isNullOrEmpty(result.get(Forex.FOREX_VOLUMENUMBERLIST_RES))) {
                        BaseHttpEngine.dissMissProgressDialog();
                        BaseDroidApp.getInstanse().showInfoMessageDialog(ForeignTradeDetailsActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
                        return;
                    } else {
                        volumeNumberList = (List<String>) result.get(Forex.FOREX_VOLUMENUMBERLIST_RES);
                        if (volumeNumberList == null || volumeNumberList.size() <= 0) {
                            BaseHttpEngine.dissMissProgressDialog();
                            BaseDroidApp.getInstanse().showInfoMessageDialog(ForeignTradeDetailsActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
                            return;
                        } else {
                            BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_VOLUMENUMBERLIST_RES, volumeNumberList);
                        }
                    }
                }
                accIsFix();
            }
        }
    }

    /** 绑定的账户是活期 */
    private void accIsCurr() {
        tradeConditionFixOrCurr = 2;
        fixOrCurrency = 1;
        switch (requestTag) {
            case ConstantGloble.FOREX_QUICK_REQUEST:// 快速交易
                quickOrRateSell = 1;
                break;
            case ConstantGloble.FOREX_BUY_REQUEST:// 银行买价 目标货币对
                quickOrRateSell = 3;
                switch (allOrCustomerReq) {
                    case ConstantGloble.FOREX_ALL:// 0----所有的货币对 买入币种
                        getSelectPositionCode();  // 得到币种
                        break;
                    case ConstantGloble.FOREX_CUSTOMER:// 1---用户定制 买入币种
                        getSelectPositionCode(); // 得到币种
                        break;

                    default:
                        break;
                }
                break;
            case ConstantGloble.FOREX_SELL_REQUEST:// 银行卖价 目标货币对
                quickOrRateSell = 2;
                switch (allOrCustomerReq) {
                    case ConstantGloble.FOREX_ALL:// 0----所有的货币对 卖出币种
                        getSelectPositionCode(); // 得到币种
                        break;
                    case ConstantGloble.FOREX_CUSTOMER:// 1---用户定制 卖出币种
                        getSelectPositionCode(); // 得到币种
                        break;
                    default:
                        break;
                }
                break;

            default:
                break;
        }
        tradeConditionPsnForexQueryBlanceCucyList();   // 查询卖出币种是否存在
    }

    /** 绑定的账户是定期 */
    private void accIsFix() {
        // 定期 外汇行情交易
        tradeConditionFixOrCurr = 1;
        fixOrCurrency = 2;
        BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_VOLUMENUMBERLIST_RES, volumeNumberList);
        switch (requestTag) {
            case ConstantGloble.FOREX_QUICK_REQUEST:// 快速交易
                quickOrRateSell = 1;
                break;
            case ConstantGloble.FOREX_BUY_REQUEST:// 银行买价 目标货币对
                quickOrRateSell = 3;
                switch (allOrCustomerReq) {
                    case ConstantGloble.FOREX_ALL:// 0----所有的货币对
                        getSelectPositionCode(); // 得到币种
                        break;
                    case ConstantGloble.FOREX_CUSTOMER:// 1---用户定制
                        getSelectPositionCode(); // 得到币种
                        break;
                    default:
                        break;
                }
                break;
            case ConstantGloble.FOREX_SELL_REQUEST:// 银行卖价 目标货币对
                quickOrRateSell = 2;
                switch (allOrCustomerReq) {
                    case ConstantGloble.FOREX_ALL:// 0----所有的货币对
                        getSelectPositionCode(); // 得到币种
                        break;
                    case ConstantGloble.FOREX_CUSTOMER:// 1---用户定制
                        getSelectPositionCode(); // 得到币种
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        // 查询卖出币种是否存在
        tradeConditionPsnForexQueryBlanceCucyList();
    }


    /** 定期-----卖出币种 */
    @SuppressWarnings("unchecked")
    public void tradeConditionFixBlanceCucyListCallback(Object resultObj) {
        Map<String, Object> result = getHttpTools().getResponseResult(resultObj);
        if (StringUtil.isNullOrEmpty(result)) {
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(ForeignTradeDetailsActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
            return;
        }
        if (!result.containsKey(Forex.FORX_TERMSUBACCOUNT_RES)) {
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(ForeignTradeDetailsActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
            return;
        }
        if (StringUtil.isNullOrEmpty(result.get(Forex.FORX_TERMSUBACCOUNT_RES))) {
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(ForeignTradeDetailsActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
            return;
        }
        List<Map<String, Object>> termSubAccountList = (List<Map<String, Object>>) result.get(Forex.FORX_TERMSUBACCOUNT_RES);
        if (termSubAccountList == null || termSubAccountList.size() == 0) {
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(ForeignTradeDetailsActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
            return;
        } else {
            BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FORX_TERMSUBACCOUNT_RES, termSubAccountList);
            int len2 = termSubAccountList.size();
            for (int j = 0; j < len2; j++) {
                Map<String, Object> termSubAccountMap = termSubAccountList.get(j);
                String status = (String) termSubAccountMap.get(Forex.FOREX_STATUS_RES);
                String type = (String) termSubAccountMap.get(Forex.FOREX_TYPE_RES);  // 存单类型
                // 账户状态正常,存单类型为定活两遍，整存整取
                if (ConstantGloble.FOREX_ACCTYPE_NORMAL.equals(status) && (ConstantGloble.FOREX_ACCTYPE_ZCZQ.equals(type) || ConstantGloble.FOREX_ACCTYPE_DHLB.equals(type))) {
                    // 得到可用余额，并判断是否大于0
                    Map<String, Object> balanceMap = (Map<String, Object>) termSubAccountMap.get(Forex.FOREX_BALANCE_RES);
                    if (!StringUtil.isNullOrEmpty(balanceMap)) {
                        String balance = (String) balanceMap.get(Forex.FOREX_AVAILABLEBALANCE_RES);
                        double b = Double.valueOf(balance);
                        if (b > 0) {  // 可用余额大于0
                            tradeConditionFixSellTag = true;
                            break;
                        }
                    }
                }
            }
        }
        if (!tradeConditionFixSellTag) {  // 不存在卖出币种
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(ForeignTradeDetailsActivity.this.getString(R.string.forex_rateinfo_sell_codes));
            return;
        } else {
            switch (quickOrRateSell) {
                case 1:// 快速交易 跳转到定期交易页面 查询买入币种信息
                    requestPsnForexQueryBuyCucyList();
                    break;
                case 2:// 点击银行卖价，查询卖出币种信息
                    isInFixResult(targetCode);  // 卖出币种是否在卖出币种集合里面
                    break;
                case 3:// 点击银行买价，查询卖出币种信息
                    isInFixResult(sourceCurCde);  // 卖出币种是否在卖出币种集合里面
                    break;
                default:
                    break;
            }
        }
    }

    /** 活期------卖出币种 */
    @SuppressWarnings("unchecked")
    public void tradeConditionCurrencyBlanceCucyListCallback(Object resultObj) {
        Map<String, Object> result = getHttpTools().getResponseResult(resultObj);
        tradeSellCurrResultList = new ArrayList<Map<String, Object>>();
        if (StringUtil.isNullOrEmpty(result)) {
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(ForeignTradeDetailsActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
            return;
        }
        // 不包含sellList键
        if (!result.containsKey(Forex.FOREX_SELLLIST_RES)) {
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(ForeignTradeDetailsActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
            return;
        }
        // sellList值为空
        if (StringUtil.isNullOrEmpty(result.get(Forex.FOREX_SELLLIST_RES))) {
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(ForeignTradeDetailsActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
            return;
        }
        List<Map<String, Object>> sellList = (List<Map<String, Object>>) result.get(Forex.FOREX_SELLLIST_RES);
        if (sellList == null || sellList.size() == 0) {
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(ForeignTradeDetailsActivity.this.getResources().getString(R.string.forex_rateinfo_sell_codes));
            return;
        } else {
            // 存储卖出币种的sellList
            BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_SELLCODE_SELLLIST_KEY, sellList);
            int len = sellList.size();
            for (int i = 0; i < len; i++) {
                String status = (String) sellList.get(i).get(Forex.FOREX_STATUS_RES); // 账户状态
                String normal = ConstantGloble.FOREX_ACCTYPE_NORMAL;  // 子账户状态正常
                if (normal.equals(status)) {
                    Map<String, Object> balance = (Map<String, Object>) sellList.get(i).get(Forex.FOREX_BALANCE_RES);
                    if (!StringUtil.isNullOrEmpty(balance)) {
                        String availableBalance = (String) balance.get(Forex.FOREX_AVAILABLEBALANCE_RES);
                        double moneyBolance = Double.valueOf(availableBalance);
                        Map<String, String> currency = (Map<String, String>) balance.get(Forex.FOREX_CURRENCY_RES);
                        if (!StringUtil.isNullOrEmpty(currency)) {  // 币种存在
                            String code = currency.get(Forex.FOREX_CURRENCY_CODE_RES).trim();
                            // 币种不允许为人民币
                            if (!StringUtil.isNull(code) && (!code.equals(ConstantGloble.FOREX_RMB_TAG1) || !code.equalsIgnoreCase(ConstantGloble.FOREX_RMB_CNA_TAG2))) {
                                if (moneyBolance > 0) {  // 账户余额大于0
                                    tradeSellCurrResultList.add(sellList.get(i));
                                }
                            }
                        }
                    }
                }
            }
            if (tradeSellCurrResultList.size() <= 0 || tradeSellCurrResultList == null) {
                tradeConditionSellTag = false;
                BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.forex_rateinfo_sell_codes));
                return;
            } else {
                BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_CURR_TRADESELLCODERESULTLIST, tradeSellCurrResultList);
                tradeConditionSellTag = true;
            }

        }
        // super.tradeConditionCurrencyBlanceCucyListCallback(resultObj);
        if (!tradeConditionSellTag) {  // 活期卖出不币种存在
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(ForeignTradeDetailsActivity.this.getString(R.string.forex_rateinfo_sell_codes));
            return;
        } else {
            switch (quickOrRateSell) {
                case 1:// 快速交易 活期卖出币种存在
                    requestPsnForexQueryBuyCucyList(); // 查询买入币种信息
                    break;
                case 2:// 银行卖价，卖出币种必须存在，否则不再进入到外汇交易页面中
                    isInResult(targetCode);  // 判断卖出币种是否存在
                    break;
                case 3:// 银行买价，卖出币种必须存在，否则不再进入到外汇交易页面中
                    isInResult(sourceCurCde);  // 判断卖出币种是否存在
                    break;
                default:
                    break;
            }
        }
    }

    /** 活期账户 银行买价 查询买入币种列表信息 **/
    private void requestPsnForexQueryBuyCucyList() {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Forex.FOREX_PSNFOREXQUERYBUYCUCYLIST_API);
        biiRequestBody.setConversationId(commConversationId);
        biiRequestBody.setParams(null);
        HttpManager.requestBii(biiRequestBody, this, "requestPsnForexQueryBuyCucyListCallback");
    }

    /** 活期 查询买入币种列表信息---回调 */
    @SuppressWarnings("unchecked")
    public void requestPsnForexQueryBuyCucyListCallback(Object resultObj) {
        BaseHttpEngine.dissMissProgressDialog();
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        if (StringUtil.isNullOrEmpty(biiResponseBody.getResult())) { // 弹出不可以买的信息
            BaseDroidApp.getInstanse().showInfoMessageDialog(ForeignTradeDetailsActivity.this.getString(R.string.forex_rateinfo_buy_codes));
            return;
        }
        List<Map<String, String>> buyCodeResultList = (List<Map<String, String>>) biiResponseBody.getResult();
        if (buyCodeResultList == null || buyCodeResultList.size() == 0) { // 弹出不可以买的信息
            BaseDroidApp.getInstanse().showInfoMessageDialog(ForeignTradeDetailsActivity.this.getString(R.string.forex_rateinfo_buy_codes));
            return;
        } else { // 存储买入币种信息
            BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_BUYCODERESULTLIST, buyCodeResultList);
            getBuyCode(); // 处理买入币种数据
        }
    }

    /** 处理买入币种信息 */
    @SuppressWarnings("unchecked")
    private void getBuyCode() {
        List<Map<String, String>> buyCodeResultList = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.FOREX_BUYCODERESULTLIST);
        int len = buyCodeResultList.size();
        buyCodeList = new ArrayList<String>();
        buyCodeDealList = new ArrayList<String>();
        for (int i = 0; i < len; i++) {
            Map<String, String> buyCodeResulMap = buyCodeResultList.get(i);
            String buyCodeResul = buyCodeResulMap.get(Forex.FOREX_BUY_CODE_RES).trim();
            String code = null;
            if (LocalData.Currency.containsKey(buyCodeResul)) {
                code = LocalData.Currency.get(buyCodeResul);
                buyCodeList.add(buyCodeResul);
                buyCodeDealList.add(code);
            } else {
                continue;
            }
        }
        if (buyCodeDealList == null || buyCodeDealList.size() <= 0 || buyCodeList == null || buyCodeList.size() <= 0) {
            BaseHttpEngine.dissMissProgressDialog();
            return;
        } else {
            BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_BUYCODEDEALLIST_KEY, buyCodeDealList);
            BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_BUYCODELIST_KEY, buyCodeList);
            switchTag();
        }
    }

    private void switchTag() {
        if (!isFinishing()) {
            switch (quickOrRateSell) {
                case 1:// 快速交易
                    switch (fixOrCurrency) {
                        case 1:// 活期
                            Intent intent3 = new Intent();
                            intent3.setClass(ForeignTradeDetailsActivity.this, ForexQuickCurrentSubmitActivity.class);
                            intent3.putExtra(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG, ConstantGloble.FOREX_TRADE_QUICK);
                            startActivityForResult(intent3, ConstantGloble.FOREX_RATE_TRADE_TAG);
                            break;
                        case 2:// 定期
                            Intent intent2 = new Intent();
                            intent2.setClass(ForeignTradeDetailsActivity.this, ForexQuickTradeSubmitActivity.class);
                            intent2.putExtra(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG, ConstantGloble.FOREX_TRADE_QUICK);// -1
                            startActivityForResult(intent2, ConstantGloble.FOREX_RATE_TRADE_TAG);
                            break;
                        default:
                            break;
                    }
                    break;
                case 2:// 银行卖价
                    buy_isInBuyResultPosition(sourceCurCde);
                    switch (fixOrCurrency) {
                        case 1:// 活期
                            Intent intent3 = new Intent();
                            intent3.setClass(ForeignTradeDetailsActivity.this, ForexQuickCurrentSubmitActivity.class);
                            intent3.putExtra(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG, ConstantGloble.FOREX_TRADE_QUICK_SELL_CURR);
                            startActivityForResult(intent3, ConstantGloble.FOREX_RATE_TRADE_TAG);
                            break;
                        case 2:// 定期
                            Intent intent = new Intent();
                            intent.setClass(ForeignTradeDetailsActivity.this, ForexQuickTradeSubmitActivity.class);
                            intent.putExtra(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG, ConstantGloble.FOREX_TRADE_QUICK_SELL_FIX);// 601
                            startActivityForResult(intent, ConstantGloble.FOREX_RATE_TRADE_TAG);
                            break;
                    }
                    break;
                case 3:// 银行买价
                    buy_isInBuyResultPosition(targetCode); // 买入币种是否存在买入币种集合里面
                    switch (fixOrCurrency) {
                        case 1:// 活期
                            Intent intent31 = new Intent();
                            intent31.setClass(ForeignTradeDetailsActivity.this, ForexQuickCurrentSubmitActivity.class);
                            intent31.putExtra(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG, ConstantGloble.FOREX_TRADE_QUICK_RATEINFO_CURR);
                            startActivity(intent31);
                            break;
                        case 2:// 定期
                            Intent intent = new Intent();
                            intent.setClass(ForeignTradeDetailsActivity.this, ForexQuickTradeSubmitActivity.class);
                            intent.putExtra(ConstantGloble.FOREX_CUSTOMER_OR_QUICK_TAG, ConstantGloble.FOREX_TRADE_QUICK_RATEINFO);// 301
                            startActivity(intent);
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /** 银行买价-买入币种是否在买入币种列表里面以及位置 */
    private void buy_isInBuyResultPosition(String buyCodeCode) {
        int len = buyCodeList.size();
        boolean tt = false;
        for (int i = 0; i < len; i++) {
            String buyCodeResul = buyCodeList.get(i);
            if (buyCodeCode.equals(buyCodeResul)) {
                tt = true;
                BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_BUYINLIST_POSITION, i);
                break;
            }
            if (!tt) {
                BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_BUYINLIST_POSITION, -1);
            }
        }
    }

    /** 卖出币种是否在卖出币种集合里面 */
    @SuppressWarnings("unchecked")
    private void isInFixResult(String sellCodeCode) {
        List<Map<String, Object>> termSubAccountList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap().get(Forex.FORX_TERMSUBACCOUNT_RES);
        int len2 = termSubAccountList.size();
        List<Map<String, Object>> littleResult = new ArrayList<Map<String, Object>>();
        for (int j = 0; j < len2; j++) {
            Map<String, Object> termSubAccountMap = termSubAccountList.get(j);
            if (StringUtil.isNullOrEmpty(termSubAccountMap)) {
                continue;
            }
            String type = (String) termSubAccountMap.get(Forex.FOREX_TYPE_RES);
            String cashRemit = (String) termSubAccountMap.get(Forex.FOREX_CASHREMIT_RES);
            if (StringUtil.isNull(cashRemit)) {
                continue;
            }
            String cash = LocalData.CurrencyCashremit.get(cashRemit);
            Map<String, Object> balanceMap = (Map<String, Object>) termSubAccountMap.get(Forex.FOREX_BALANCE_RES);
            if (StringUtil.isNullOrEmpty(balanceMap)) {
                continue;
            }
            Map<String, String> currency = (Map<String, String>) balanceMap.get(Forex.FOREX_CURRENCY_RES);
            if (StringUtil.isNullOrEmpty(currency)) {
                continue;
            }
            String code = currency.get(Forex.FOREX_CURRENCY_CODE_RES);
            if (StringUtil.isNull(code)) {
                continue;
            }
            String codeName = null;
            String codeCodeName = null;
            if (LocalData.Currency.containsKey(code) && LocalData.Currency.containsKey(sellCodeCode)) {
                codeName = LocalData.Currency.get(code);
                codeCodeName = LocalData.Currency.get(sellCodeCode);
            }
            // 整存整取-----不判断钞汇
            // TODO-----------------------不判断钞汇
            if (!StringUtil.isNull(type) && ConstantGloble.FOREX_ACCTYPE_ZCZQ.equals(type)) {
                if (codeName.equals(codeCodeName)) {
                    littleResult.add(termSubAccountMap);
                }
            }
        }
        if (littleResult == null || littleResult.size() <= 0) {  // 卖出币种不存在
            String codeName = LocalData.Currency.get(sellCodeCode);
            String info1 = getResources().getString(R.string.forex_curr_acc_code_info1);
            String info2 = getResources().getString(R.string.forex_curr_acc_code_info2);
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(info1 + codeName + info2);
            return;
        }
        getReturnValue(littleResult); // 卖出币种存在
    }

    /** 卖出币种存在时，判断可用余额、账户状态 */
    @SuppressWarnings("unchecked")
    private void getReturnValue(List<Map<String, Object>> list) {
        int len = list.size();
        boolean t = false;
        boolean k = false;
        Map<String, String> currency = null;
        String codeName = null;
        for (int i = 0; i < len; i++) {
            Map<String, Object> termSubAccountMap = list.get(i);
            if (StringUtil.isNullOrEmpty(termSubAccountMap)) {
                continue;
            }
            String status = (String) termSubAccountMap.get(Forex.FOREX_STATUS_RES);
            Map<String, Object> balanceMap = (Map<String, Object>) termSubAccountMap.get(Forex.FOREX_BALANCE_RES);
            if (StringUtil.isNullOrEmpty(balanceMap)) {
                continue;
            }
            currency = (Map<String, String>) balanceMap.get(Forex.FOREX_CURRENCY_RES);
            if (StringUtil.isNullOrEmpty(currency)) {
                continue;
            }
            String code = currency.get(Forex.FOREX_CURRENCY_CODE_RES);
            codeName = LocalData.Currency.get(code);
            String availableBalance = (String) balanceMap.get(Forex.FOREX_AVAILABLEBALANCE_RES);
            if (StringUtil.isNull(availableBalance) || (Double.valueOf(availableBalance) <= 0)) {
                t = false;
                continue;
            } else {
                t = true;
                if (ConstantGloble.FOREX_ACCTYPE_NORMAL.equals(status)) {
                    String volumeNumber = (String) termSubAccountMap.get(Forex.FOREX_VOLUMENUMBER_RES);
                    String cdnumber = (String) termSubAccountMap.get(Forex.FOREX_CDNUMBER_RES);
                    k = true;
                    BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_VOLUMENUMBER_RES, volumeNumber);
                    BaseDroidApp.getInstanse().getBizDataMap().put(Forex.FOREX_CDNUMBER_RES, cdnumber);
                    break;
                } else {
                    k = false;
                }
            }
        }
        if (!t) {  // 账户余额
            String info1 = getResources().getString(R.string.forex_curr_acc_info1);
            String info2 = getResources().getString(R.string.forex_curr_acc_info2);
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(info1 + codeName + info2);
            return;
        }
        if (!k) {  // 账户状态
            String info = getResources().getString(R.string.forex_curr_acc_status);
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(codeName + info);
            return;
        }
        if (t && k) {  // 查询买入币种信息
            requestPsnForexQueryBuyCucyList();
        }
    }

    /** 判断卖出币种是否在卖出result里面 */
    @SuppressWarnings("unchecked")
    private void isInResult(String sellCodeCode) {
        List<Map<String, Object>> sellList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.FOREX_SELLCODE_SELLLIST_KEY);
        int lens = sellList.size();
        boolean tt = false;
        boolean k = false;
        boolean b = false;
        for (int i = 0; i < lens; i++) {
            Map<String, Object> map = sellList.get(i);
            if (StringUtil.isNullOrEmpty(map)) {
                continue;
            }
            String cashRemit = (String) map.get(Forex.FOREX_CASHREMIT_RES);
            String status = (String) map.get(Forex.FOREX_STATUS_RES);  // 账户状态
            if (StringUtil.isNullOrEmpty(status)) {
                continue;
            }
            Map<String, Object> balance = (Map<String, Object>) map.get(Forex.FOREX_BALANCE_RES);
            if (StringUtil.isNullOrEmpty(balance)) {
                continue;
            }
            String availableBalance = (String) balance.get(Forex.FOREX_AVAILABLEBALANCE_RES);
            Map<String, String> currency = (Map<String, String>) balance.get(Forex.FOREX_CURRENCY_RES);
            if (StringUtil.isNullOrEmpty(currency)) {
                continue;
            }
            String code = currency.get(Forex.FOREX_CURRENCY_CODE_RES).trim();
            String codeName = null;
            String codeCodeName = null;
            if (!StringUtil.isNull(code) && LocalData.Currency.containsKey(code) && LocalData.Currency.containsKey(sellCodeCode)) {
                codeName = LocalData.Currency.get(code);
                codeCodeName = LocalData.Currency.get(sellCodeCode);
            }
            // 币种不为空，code和sourceCurCde相同
            if (codeName.equals(codeCodeName)) {
                tt = true;  // 币种存在
                if (StringUtil.isNull(availableBalance) || (Double.valueOf(availableBalance) <= 0)) {
                    // 账户余额
                    String info1 = getResources().getString(R.string.forex_curr_acc_info1);
                    String info2 = getResources().getString(R.string.forex_curr_acc_info2);
                    BaseHttpEngine.dissMissProgressDialog();
                    BaseDroidApp.getInstanse().showInfoMessageDialog(info1 + codeName + info2);
                    b = false;
                    break;
                } else {
                    b = true;
                    String normal = ConstantGloble.FOREX_ACCTYPE_NORMAL;  // 子账户状态正常
                    if (normal.equals(status)) {
                        k = true;
                    } else {
                        String info = getResources().getString(R.string.forex_curr_acc_status);
                        BaseHttpEngine.dissMissProgressDialog();
                        BaseDroidApp.getInstanse().showInfoMessageDialog(codeName + info);
                        k = false;
                        break;
                    }
                }
            }
        }
        if (!tt) {
            String codeName = LocalData.Currency.get(sellCodeCode);
            String info1 = getResources().getString(R.string.forex_curr_acc_code_info1);
            String info2 = getResources().getString(R.string.forex_curr_acc_code_info2);
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(info1 + codeName + info2);
            return;
        } else {
            if (k && b) {
                switch (quickOrRateSell) {
                    case 2:// 银行卖价
                        int position = getCurrSellCodeInListPosition(targetCode);
                        if (position < 0) {
                            String info1 = getResources().getString(R.string.forex_curr_acc_code_info1);
                            String info2 = getResources().getString(R.string.forex_curr_acc_code_info2);
                            BaseHttpEngine.dissMissProgressDialog();
                            String code = LocalData.Currency.get(targetCode);
                            BaseDroidApp.getInstanse().showInfoMessageDialog(info1 + code + info2);
                            return;
                        } else {
                            requestPsnForexQueryBuyCucyList(); // 查询买入币种信息
                        }
                        break;
                    case 3:// 银行买价
                        int po = getCurrSellCodeInListPosition(sourceCurCde);
                        if (po < 0) {
                            String info1 = getResources().getString(R.string.forex_curr_acc_code_info1);
                            String info2 = getResources().getString(R.string.forex_curr_acc_code_info2);
                            BaseHttpEngine.dissMissProgressDialog();
                            String code = LocalData.Currency.get(sourceCurCde);
                            BaseDroidApp.getInstanse().showInfoMessageDialog(info1 + code + info2);
                        } else {
                            requestPsnForexQueryBuyCucyList(); // 查询买入币种信息
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /** 得到卖出币种在tradeSellCurrResultList中的position */
    @SuppressWarnings("unchecked")
    private int getCurrSellCodeInListPosition(String sellCodeCode) {
        int position = -1;
        int len = tradeSellCurrResultList.size();
        for (int i = 0; i < len; i++) {
            Map<String, Object> map = tradeSellCurrResultList.get(i);
            if (StringUtil.isNullOrEmpty(map)) {
                continue;
            }
            Map<String, String> currency = (Map<String, String>) map.get(Forex.FOREX_CURRENCY_RES);
            if (StringUtil.isNullOrEmpty(currency)) {
                continue;
            }
            String code = currency.get(Forex.FOREX_CURRENCY_CODE_RES).trim();
            if (StringUtil.isNull(code)) {
                continue;
            }
            String cashRemit = (String) map.get(Forex.FOREX_CASHREMIT_RES);
            String cash = null;
            if (!StringUtil.isNull(cashRemit) && LocalData.CurrencyCashremit.containsKey(cashRemit)) {
                cash = LocalData.CurrencyCashremit.get(cashRemit);
            }
            // 外汇行情页面，所有币种的钞汇标志都是现汇
            if (code.equals(sellCodeCode)) {
                position = i; // 卖出币种在用户账户中
                BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.FOREX_SELLINLIST_POSITION, i);
                break;
            }
        }
        return position;
    }

    // =====================================================================================================
    /** Title点击事件统一处理*/
    public void initTitleClick(){
        // 分享 取消 by luqp 2016-11-1
        getBackGroundLayout().setShareButtonVisibility(View.GONE); //设置分享隐藏
        getBackGroundLayout().setRightButtonVisibility(View.VISIBLE); //设置右按钮显示
        if(isQrcode) { // 用户扫描二维码进入详情页面的数据
            getBackGroundLayout().setTitleText(sourceDealCode+"/"+targetDealCode);
        } else {
            View title = LayoutInflater.from(context).inflate(R.layout.foreign_title_layout,null);
            getBackGroundLayout().setTitleLayout(title);
            LinearLayout foreignTitle = (LinearLayout) title.findViewById(R.id.foreign_title);
            currencyLeft = (TextView) title.findViewById(R.id.tv_currency_left);
            currencyRight = (TextView) title.findViewById(R.id.tv_currency_right);
            currencyLeft.setText(sourceDealCode);
            currencyRight.setText(targetDealCode);
            foreignTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BaseDroidApp.getInstanse()
                            .showSelectCurrencyPairDialog(context , allRateList, onCurrencyPairsItemClickListener);
//                showDialog();
                }
            });
        }
        getBackGroundLayout().setRightButtonImage(getResources().getDrawable(R.drawable.share_qr_code));

        queryKTendencyView.setLandscapeCurCode(sourceDealCode+"/"+targetDealCode);
        queryKTendencyView.setLandscapeCurCodeItemClickListener(onCurrencyPairsItemClickListener);
        /** 返回按钮点击事件 停止自动刷新*/
        getBackGroundLayout().setOnLeftButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isQrcode) { // 用户扫描二维码进入详情页面的数据
                    ActivityTaskManager.getInstance().removeAllActivity();
                    finish();
                } else {
                    if (queryTag != 3) {
                        setResult(ConstantGloble.FOREX_FOREXBASE_ACTIVITY);
                    }
                    finish();
                }
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
                InvestQRCodeActivity.goToInvestQRCodeActivity(ForeignTradeDetailsActivity.this,"外汇买卖二维码",String.format("boc://bocphone?type=3&forexRQCode=%s",sourceCurrencyCode+targetCurrencyCode),sourceDealCode+"/"+ targetDealCode);
            }
        });
    }

    /** 底部货币对item点击事件*/
    private AdapterView.OnItemClickListener onCurrencyPairsItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            LogGloble.i(TAG, "点击货币对的 position......" +"position");
            selectPositionMap = allRateList.get(position);
            selectPosition = position; //更新用户选择数据的位置
            selectBuyRate = (String) selectPositionMap.get(Forex.FOREX_RATE_BUYRATE_RES);
            selectSellRate = (String) selectPositionMap.get(Forex.FOREX_RATE_SELLRATE_RES);
            ccygrps = getCcygrps(selectPositionMap);
            currencyLeft.setText(sourceDealCode); //更新标题货币对
            currencyRight.setText(targetDealCode);
            queryKTendencyView.setLandscapeCurCode(sourceDealCode+"/"+targetDealCode);
            currency = getCurrency(selectPositionMap); // 如果是日元返回true.
            if (currency){
                selectSellRate = StringUtil.parseStringPattern(selectSellRate,2);
                selectBuyRate = StringUtil.parseStringPattern(selectBuyRate,2);
            } else {
                selectSellRate = StringUtil.parseStringPattern(selectSellRate,4);
                selectBuyRate = StringUtil.parseStringPattern(selectBuyRate,4);
            }
            buyRate.setText(selectBuyRate); // 更新买入价
            sellRate.setText(selectSellRate); // 更新卖出价
            queryKTendencyView.resetData(); // 清空K线图数据
            if (BaseDroidApp.getInstanse().isLogin()) { // 已登录显示
                allCcygrps = getCcygrpList(optionalList);
                if (allCcygrps.contains(ccygrps)){
                    isCollection = true;
                    detailsCollectImg.setImageDrawable(getResources().getDrawable(R.drawable.share_already_collect));
                    LogGloble.i(TAG,"已收藏...........");
                } else {
                    isCollection = false;
                    detailsCollectImg.setImageDrawable(getResources().getDrawable(R.drawable.share_not_collected));
                    LogGloble.i(TAG,"未收藏............");
                }
            }
            // 得到用户选的货币对请求对应的数据
            getQueryKTendencyData(ccygrps); // 获取K线图数据
            getQueryAverageTendency(ccygrps); // 获取趋势图查询
            getQuerySingelQuotation(ccygrps);//获取单笔行情查询
            BaseDroidApp.getInstanse().dismissMessageDialog(); // 关闭弹框
        }
    };

    /** 区分自选和全部*/
    public void tradeQueryTag(int tag){
        if(tag == 1){ // 全部
            allOrCustomerReq = 0;
            customerOrTrade = 1;
//            BaseHttpEngine.showProgressDialog();
            ratePsnForexActIsset();  // 开始交易
        }else if(tag == 2){ // 自选
            allOrCustomerReq = 1;
            customerOrTrade = 1;
//            BaseHttpEngine.showProgressDialog();
            ratePsnForexActIsset();  // 开始交易
        }else { // 快速交易
            allOrCustomerReq = 0;
            customerOrTrade = 1;
//            BaseHttpEngine.showProgressDialog();
            ratePsnForexActIsset();  // 开始交易
        }
    }

    public void getSelectPositionCode(){
        sourceCurCde = (String) allRateList.get(selectPosition).get(Forex.FOREX_RATE_SOURCECODE_RES);
        targetCode = (String) allRateList.get(selectPosition).get(Forex.FOREX_RATE_TARGETCODE_RES);
    }

    /** 全部未登录时汇率 外置请求全部汇率回调*/
    @SuppressWarnings("unchecked")
    public void requestAllRatesOutlayCallback(Object resultObj) {
        super.requestAllRatesOutlayCallback(resultObj);
        BaseHttpEngine.dissMissProgressDialog();
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        List<Map<String, Object>> result = (List<Map<String, Object>>) biiResponseBody.getResult();
        if (StringUtil.isNullOrEmpty(result) || StringUtil.isNullOrEmpty(result)) {
            return;
        } else {
            allRateList = getTrueDate(result);  // 将得到的数据进行处理
            String sourceCode = getIntent().getStringExtra("sourceCode");
            String targetCode = getIntent().getStringExtra("targetCode");
            selectPosition = isContains(allRateList,sourceCode,targetCode);
            if (selectPosition == -1){
                BaseDroidApp.getInstanse().showMessageDialog("您所在的地区暂时不支持此货币对", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BaseDroidApp.getInstanse().dismissMessageDialog();
                        finish();
                    }
                });
            }
            selectBuyRate = (String) allRateList.get(selectPosition).get(Forex.FOREX_RATE_BUYRATE_RES); // 买入价
            selectSellRate = (String) allRateList.get(selectPosition).get(Forex.FOREX_RATE_SELLRATE_RES); // 卖出价
            ccygrps = getCcygrps(allRateList.get(selectPosition));
            currency = getCurrency(allRateList.get(selectPosition)); // 如果是日元返回true.
        }
        initViewData();
    }

    /** 查询全部外汇行情---回调 rateTimes赋值*/
    @SuppressWarnings("unchecked")
    public void requestPsnAllRateCallback(Object resultObj) {
        super.requestPsnAllRateCallback(resultObj);
        BaseHttpEngine.dissMissProgressDialog();
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        Map<String, List<Map<String, Object>>> result = (Map<String, List<Map<String, Object>>>) biiResponseBody.getResult();
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
            List<Map<String, Object>> allList = result.get(Forex.FOREX_RATE_ALLRATELIST_RES);
            if (allList == null || allList.size() <= 0) {
                return;
            }
            allRateList = getTrueDate(allList);  // 将得到的数据进行处理
            String sourceCode = getIntent().getStringExtra("sourceCode");
            String targetCode = getIntent().getStringExtra("targetCode");
            selectPosition = isContains(allRateList,sourceCode,targetCode);
            if (selectPosition == -1){
                BaseDroidApp.getInstanse().showMessageDialog("您所在的地区暂时不支持此货币对", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BaseDroidApp.getInstanse().dismissMessageDialog();
                        finish();

                    }
                });
            }
            selectBuyRate = (String) allRateList.get(selectPosition).get(Forex.FOREX_RATE_BUYRATE_RES); // 买入价
            selectSellRate = (String) allRateList.get(selectPosition).get(Forex.FOREX_RATE_SELLRATE_RES); // 卖出价
            ccygrps = getCcygrps(allRateList.get(selectPosition));
            currency = getCurrency(allRateList.get(selectPosition)); // 如果是日元返回true.
            initViewData();
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
            if (taskMark == 2) { // 未登录时点击买入卖出判断是否开通投资理财
                requestPsnInvestmentManageIsOpen();
            } else {
                if(isQrcode) { // 用户扫描二维码进入详情页面的数据
                    requestPsnInvestmentManageIsOpen();
                } else {
                    requestPSNGetTokenId();
                }
            }
        }
    }

    /**
     * 判断是否开通投资理财服务---回调 快速交易
     *
     * @param resultObj
     */
    public void requestPsnInvestmentManageIsOpenCallback(Object resultObj) {
        super.requestPsnInvestmentManageIsOpenCallback(resultObj);
        String isOpenOr = getHttpTools().getResponseResult(resultObj);
        if ("false".equals(isOpenOr)) {
            isOpen = false;
        } else {
            isOpen = true;
        }
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
        switch (taskMark) {
            case 1: // 查询自选全部数据
                requestPsnCustomerRate();
                break;
            case 2: // 货币对买卖
                tradeQueryTag(queryTag); // 开始交易
                break;
            default:
                break;
        }
    }

    /** 得到用户选择的货币对 */
    public void getSelectedCode() {
        if (optionalList == null) {
            return;
        } else {
            if (isCollection) { //取消收藏
                if (optionalList.size() <= 1){  // 收藏货币对最后一条不能取消收藏
                    MessageDialog.showMessageDialog(this,getString(R.string.forex_the_last_pair));
                    return;
                }
            } else { // 收藏
                if (optionalList.size() >= 6){
                    MessageDialog.showMessageDialog(this,getString(R.string.forex_rate_makecode));
                    return;
                }
            }

            /** 存储用户选择的源货币对代码 */
            List<String> selectedCodeList = new ArrayList<String>();
            /** 存储用户选择的目标货币对代码 */
            List<String> selectedTargetCodeList = new ArrayList<String>();
            for (int i = 0; i < optionalList.size(); i++) {
                Map<String, Object> choiseMap = optionalList.get(i);
                String sourceCurCde = (String) choiseMap.get(Forex.FOREX_RATE_SOURCECODE_RES);
                String targetCurCde = (String) choiseMap.get(Forex.FOREX_RATE_TARGETCODE_RES);
                if (!StringUtil.isNull(sourceCurCde) && !StringUtil.isNull(targetCurCde)) {
                    selectedCodeList.add(sourceCurCde);
                    selectedTargetCodeList.add(targetCurCde);
                }
            }
            if (selectedCodeList == null ||  selectedTargetCodeList == null) {
                return;
            } else {
                int len1 = selectedCodeList.size();
                int len2 = selectedTargetCodeList.size();
                if (len1 == len2) {
                    selectedArr = new String[len1];
                    for (int i = 0; i < len1; i++) {
                        String source = selectedCodeList.get(i);
                        String target = selectedTargetCodeList.get(i);
                        StringBuilder sb = new StringBuilder(source);
                        sb.append(target);
                        selectedArr[i] = sb.toString();
                    }
                } else {
                    return;
                }
//                requestPSNGetTokenId();
                requestCommConversationId();
                BaseHttpEngine.showProgressDialogCanGoBack();
            }
        }
    }

    /** 获取tocken*/
    private void requestPSNGetTokenId() {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
        biiRequestBody.setConversationId(commConversationId);
        biiRequestBody.setParams(null);
        HttpManager.requestBii(biiRequestBody, this, "requestPSNGetTokenIdCallback");
    }

    /**
     * 获取tokenId----回调
     * @param resultObj :返回结果
     */
    public void requestPSNGetTokenIdCallback(Object resultObj) {
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        tokenId = (String) biiResponseBody.getResult();
        if (StringUtil.isNull(tokenId)) {
            BaseHttpEngine.dissMissProgressDialog();
            return;
        } else {
            if (isCollection){ //取消收藏
                List<String> list= Arrays.asList(selectedArr);//将数组转换为list集合
                if(list.contains(ccygrps)) {//加入集合中包含这个元素
                    List<String> arrayList=new ArrayList<String>(list);//转换为ArrayLsit调用相关的remove方法
                    arrayList.remove(ccygrps);
                    int size = arrayList.size();
                    selectedArr = arrayList.toArray(new String[size]);
                }
                isCollection = false;
            } else { //收藏
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < selectedArr.length; i++) {
                    list.add(selectedArr[i]);
                }
                list.add(ccygrps);
                selectedArr = list.toArray(new String[0]);
                isCollection = true;
            }
            requestPsnSetCustmerCrcyPair(selectedArr);
        }
    }

    /** 客户定制货币对提交 */
    private void requestPsnSetCustmerCrcyPair(String[] str) {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Forex.FOREX_PSNSETCUSTEMRCRCY_API);
        biiRequestBody.setConversationId(commConversationId);
        Map<String, Object> map = new Hashtable<String, Object>();
        map.put(Forex.FOREX_HIDDENCURRENCYPAIR_REQ, str);
        map.put(Forex.FOREX_TOKEN_CODE_REQ, tokenId);
        biiRequestBody.setParams(map);
        HttpManager.requestBii(biiRequestBody, this, "requestPsnSetCustmerCrcyPairCallback");
    }

    /**
     * 客户定制货币对提交---回调
     * @param resultObj
     */
    public void requestPsnSetCustmerCrcyPairCallback(Object resultObj) {
//        BaseHttpEngine.dissMissProgressDialog();
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        List<Map<String, String>> resultList = (List<Map<String, String>>) biiResponseBody.getResult();
        if (isCollection){ //收藏成功
            CustomDialog.toastInCenter(this, getString(R.string.foreign_cancel_successful));
        } else { //取消收藏
            CustomDialog.toastInCenter(this, getString(R.string.foreign_collection_success));
            detailsCollectImg.setImageDrawable(getResources().getDrawable(R.drawable.share_not_collected));
        }
        requestOptionalQuotes(); // 调用自选行情刷新涨跌幅
    }

    @Override
    public void onBackPressed() {
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){//横屏
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置竖屏
        }else{
            finish();
        }
    }

    /** 二维码进来查询全部数据是否包含次货币对 如果包含记录次货币对位置*/
    public int isContains(List<Map<String,Object>> list,String text1,String text2){
        for (int i = 0;i<list.size();i++){
            Map<String,Object> map = list.get(i);
            String sourceCurrencyCode = (String) map.get(Forex.FOREX_RATE_SOURCECODE_RES);
            String targetCurrencyCode = (String) map.get(Forex.FOREX_RATE_TARGETCODE_RES);
            if (!StringUtil.isNull(sourceCurrencyCode) && !StringUtil.isNull(targetCurrencyCode) ) {
                if (sourceCurrencyCode.equals(text1) && targetCurrencyCode.equals(text2)) {
                    selectPosition = i;
                    return selectPosition;
                }
            }
        }
        return selectPosition = -1;
    }
}
