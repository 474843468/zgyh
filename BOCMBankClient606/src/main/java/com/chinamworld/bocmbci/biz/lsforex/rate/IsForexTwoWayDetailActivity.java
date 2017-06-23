
package com.chinamworld.bocmbci.biz.lsforex.rate;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.base.application.CommonApplication;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.bii.constant.Prms;
import com.chinamworld.bocmbci.biz.foreign.StringTools;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseNewActivity;
import com.chinamworld.bocmbci.biz.lsforex.trade.IsForexTradeSubmitActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.DictionaryData;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
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
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.llbt.userwidget.dialogview.MessageDialog;
import com.chinamworld.llbt.userwidget.tabview.TabView;
import com.chinamworld.llbt.utils.TimerRefreshTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxj on 2016/10/15.
 * 双向宝详情页
 */
public class IsForexTwoWayDetailActivity extends IsForexBaseNewActivity implements View.OnClickListener{
    //    private Button btn_back;
//    private Spinner sp_currency;
//    private Button btn_share;
//    private Button btn_QRcode;
    private Context context = this;


    private Button btn_buy;
    private Button btn_sale;

//    private TextView tv_onehour;
//    private TextView tv_fourhour;
//    private TextView tv_day;
//    private TextView tv_week;
//    private TextView tv_mounth;
//    private Button btn_KOrZ;

    //    private ImageView image_collect;
    private TextView image_collect;
    private TextView tv_saleRate;
    private TextView tv_buyRate;
    private TextView tv_referPrice;

    private TextView tv_currDiff;
    private TextView tv_currPercentDiff;

    /**单笔行情数据*/
    private Map<String, Object> singleDataMap;
    /**首次进入 点击的数据第几个*/
    private int sePosition;
    private String tendencyType;//折线的趋势类型
    private String kType;//K线图的趋势类型
    private boolean isK = true;//是否折线图
    /**货币对代码*/
    private String ccygrp;
    /**自选、外汇（贵金属）标识*/
    private String flag="";
    /**买入or 卖出*/
    private int buyOrSale;
    /** 买入 */
    private static final int BUY = 4;
    /** 卖出 */
    private static final int SALE = 1;
    /** 用户选择的货币对 */
    private String codeName = null;
    /** K线图*/
    private InverstKLineView queryKTendencyView;
    /** 查询时间*/
    private String timeZones;
    private TabView mTabView;
//    private ArrayAdapter<String> currencyPairsAdapter;
    /**买入、卖出标识*/
    private String buyOrSell = null;
    /**收藏币种标识 1为收藏 0为不收藏*/
    private String customFlag;
    /** 提交货币对 */
    private String selectedArr[] = null;
    /**多笔行情*/
    List<Map<String, Object>> currencyList;
    private String sourceCurrencyCodeStr = "";
    private String targetCurrencyStr = "";
    private Dialog dialog;
    /**二维码 币种*/
    private String sourceCode;
    private String targetCode;
    /**二维码币种字典翻译*/
    private String codeNameCode;
    /**收藏的货币对*/
    private List<Map<String, Object>> customerRateList;
    /** 标题货币对*/
    private TextView currencyLeft;
    private TextView currencyRight;
    private String currencyLeftName;
    private String currencyRigthName;
    private int numberDigits;
    //刷新开启标识
    private boolean isStart = false;
    //开盘价
    private String openPrice;
    //最高价
    private String maxPrice;
    //最低价
    private String minPrice;
    //牌价有效时间
    private String priceTime;
    //刷新之后的买入价、卖出价
    private String sellPrice;
    private String buyPrice;
    private String currDifft;//涨跌值
    /**二维码扫描直接进入详情页面*/
    private boolean isQrcode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.isforex_main_detail_layout);
        if(isFlagGoWay != 0){
            isFlagGoWay =1;
        }
        isQrcode = getIntent().getBooleanExtra("isQrcode",false);
        initBaseLayout();
        initUi();
        if(isQrcode){
            if(BaseDroidApp.getInstance().isLogin()){
                BaseHttpEngine.showProgressDialog();
                requestisQrcodeCommConversationId();
            }else {
                BaseHttpEngine.showProgressDialog();
                requestQuerySingelQuotation(sourceCode+targetCode, flag);
            }
        }else {
            initData();
            initListener();
        }

//        getQueryKTendencyData(); // 获取K线图数据
//        getQueryAverageTendency(); // 获取趋势图查询
    }

    @Override
    public void requestisQrcodeCommConversationIdCallBack(Object resultObj) {
        super.requestisQrcodeCommConversationIdCallBack(resultObj);
        commConversationId =  (String)CommonApplication.getInstance().getBizDataMap().get("conversationId");
        requestCustomerSetRate();
    }

    @Override
    public void requestCustomerSetRateCallback(Object resultObj) {
        super.requestCustomerSetRateCallback(resultObj);
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        // 得到response
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        customerRateList = new ArrayList<Map<String, Object>>();
        customerRateList = (List<Map<String, Object>>) biiResponseBody.getResult();
        /**单笔行情请求*/
        requestQuerySingelQuotation(sourceCode+targetCode, flag);
    }
    private void requestQuerySingelQuotation(String currencyPairs, String type) {
        QuerySingelQuotationRequestParams params = new QuerySingelQuotationRequestParams(currencyPairs, type, "M");
        params.setCcygrp(currencyPairs);
        params.setCardType(type);
        params.setCardClass("M");
        HttpHelp httpHelp = HttpHelp.getInstance();
        httpHelp.postHttpFromSF(this, params);
        httpHelp.setHttpErrorCallBack(new IHttpErrorCallBack() {
            @Override
            public boolean onError(String exceptionMessage, Object extendParams) {
                BaseHttpEngine.dissMissProgressDialog();
                singleDataMap = new HashMap<String, Object>();
                if(BaseDroidApp.getInstance().isLogin()){
                    singleDataMap = getCustomFlag();
                }
                initData();
                initListener();
                return true;
            }
        });
        httpHelp.setOkHttpErrorCode(new IOkHttpErrorCode() {
            @Override
            public boolean handlerErrorCode(BaseResponseData responseData, Object extendParams) {
                BaseHttpEngine.dissMissProgressDialog();
                singleDataMap = new HashMap<String, Object>();
                if(BaseDroidApp.getInstance().isLogin()){
                    singleDataMap = getCustomFlag();
                }
                initData();
                initListener();
                return true;
            }
        });
        httpHelp.setHttpResponseCallBack(new IHttpResponseCallBack() {
            @Override
            public boolean responseCallBack(String response, Object extendParams) {
                BaseHttpEngine.dissMissProgressDialog();
                QuerySingleQuotationResponseData data = GsonTools.fromJson(response, QuerySingleQuotationResponseData.class);


                QuerySingleQuotationResult item = data.getBody();
                singleDataMap = new HashMap<String, Object>();
                //单笔数据
                singleDataMap = (Map<String,Object>) getSingleTotal(item);
                if(BaseDroidApp.getInstance().isLogin()){
                    singleDataMap = getCustomFlag();
                }
                initData();
                initListener();
                return false;
            }
        });
    }
    private Map<String, Object> getCustomFlag(){
//        Map<String, Object> map = new HashMap<String, Object>();
        for(int j=0; j<customerRateList.size(); j++){
            String sourceCurrencyCode1 = (String)customerRateList.get(j).get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
            String targetCurrencyCode1 = (String)customerRateList.get(j).get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
            if(sourceCode.equals(sourceCurrencyCode1) && targetCode.equals(targetCurrencyCode1)){
                singleDataMap.put("customFlag", "1");
                break;
            }else {
                singleDataMap.put("customFlag", "0");
            }
        }
        return singleDataMap;
    }
    /**获取单笔行情数据*/
    private Map<String, Object> getSingleTotal(QuerySingleQuotationResult item){

        Map<String, Object> map = new HashMap<String, Object>();
        if(StringUtil.isNullOrEmpty(item)){
        }else {
            map.put("ccygrpNm", item.ccygrpNm);
            map.put("buyRate", item.buyRate);
            map.put("sellRate", item.sellRate);
            map.put("priceTime", item.priceTime);
            map.put("currPercentDiff", item.currPercentDiff);
            map.put("currDiff", item.currDiff);
            map.put("tranCode", item.tranCode);
            map.put("sortPriority", item.sortPriority);
            map.put("sourceCurrencyCode", item.sourceCurrencyCode);
            map.put("targetCurrencyCode", item.targetCurrencyCode);
            map.put("referPrice",item.referPrice);
            map.put("openPrice", item.openPrice);//开盘价格
            map.put("maxPrice", item.maxPrice);//最高值
            map.put("minPrice", item.minPrice); //最低值
        }


        return map;
    }

    /**
     * 初始化基类布局
     */
    private void initBaseLayout(){
        if(!isQrcode){
            currencyList = new ArrayList<Map<String, Object>>();
            //多笔行情数据
            currencyList = (List<Map<String, Object>>) BaseDroidApp.getInstanse().getBizDataMap().get("totalCurrency");
            sePosition = getIntent().getIntExtra("position", -1);
            sourceCode = (String)currencyList.get(sePosition).get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
            targetCode = (String)currencyList.get(sePosition).get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
            currencyLeftName = (String)LocalData.Currency.get(sourceCode);
            currencyRigthName = (String)LocalData.Currency.get(targetCode);
            if(LocalData.goldLists.contains(currencyLeftName)||LocalData.goldLists.contains(currencyRigthName)){
                currencyLeftName = (String)LocalData.code_Map_Left.get(sourceCode);
                currencyRigthName = (String)LocalData.code_Map_Right.get(targetCode);
            }
        }else {
            sourceCode = getIntent().getStringExtra("sourceCode");
            targetCode = getIntent().getStringExtra("targetCode");
            currencyLeftName = (String)LocalData.Currency.get(sourceCode);
            currencyRigthName = (String)LocalData.Currency.get(targetCode);
            flag = "F";
            if(LocalData.goldLists.contains(currencyLeftName)||LocalData.goldLists.contains(currencyRigthName)){
                flag = "G";
                currencyLeftName = (String)LocalData.code_Map_Left.get(sourceCode);
                currencyRigthName = (String)LocalData.code_Map_Right.get(targetCode);
            }
        }
        setLeftButtonPopupGone();
        getBackGroundLayout().setTitleBackground(R.color.btn_white);
        if(isQrcode){
            getBackGroundLayout().setTitleText(currencyLeftName+"/"+currencyRigthName);
        }else {
            View title = LayoutInflater.from(context).inflate(R.layout.foreign_title_layout,null);
            getBackGroundLayout().setTitleLayout(title);
            LinearLayout foreignTitle = (LinearLayout) title.findViewById(R.id.foreign_title);
            currencyLeft = (TextView) title.findViewById(R.id.tv_currency_left);
            currencyRight = (TextView) title.findViewById(R.id.tv_currency_right);

            currencyLeft.setText(currencyLeftName);
            currencyRight.setText(currencyRigthName);
            foreignTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mTimerRefreshToolsSingel.stopTimer();
                    BaseDroidApp.getInstanse()
                            .showSelectCurrencyPairDialog(context , currencyList, codeItemQuery);
                }
            });
        }
//        String title = getCodeName(currencyList , sePosition);
//        getBackGroundLayout().setTitleText(title);
//        getBackGroundLayout().setOnLeftButtonImage(getResources().getDrawable(R.drawable.base_btn_left_back_black));
        getBackGroundLayout().setLeftButtonVisibility(View.VISIBLE);
        getBackGroundLayout().setRightButtonVisibility(View.VISIBLE);
        getBackGroundLayout().setRightButtonImage(getResources().getDrawable(R.drawable.share_qr_code),1f,1f);
        getBackGroundLayout().setShareButtonVisibility(View.GONE);
        if(isQrcode){
            getBackGroundLayout().setOnLeftButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                mTimerRefreshToolsSingel.stopTimer();
                    goToMainActivity();
                }
            });
        }
        getBackGroundLayout().setOnRightButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isQrcode){
                    codeNameCode = (String)LocalData.code_Map.get(sourceCode+targetCode);
                    if(StringUtil.isNullOrEmpty(codeNameCode)){
                        codeNameCode= LocalData.Currency.get(sourceCode)+"/"+LocalData.Currency.get(targetCode);
                    }
                }else {
                    sourceCode = (String)currencyList.get(sePosition).get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
                    targetCode = (String)currencyList.get(sePosition).get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
                    codeNameCode = (String)LocalData.code_Map.get(sourceCode+targetCode);
                    if(StringUtil.isNullOrEmpty(codeNameCode)){
                        codeNameCode= LocalData.Currency.get(sourceCode)+"/"+LocalData.Currency.get(targetCode);
                    }
                }

                //双向宝type=4
                InvestQRCodeActivity.goToInvestQRCodeActivity(IsForexTwoWayDetailActivity.this,"双向宝二维码", String.format("boc://bocphone?type=4&lsforexRQCode=%s",sourceCode+targetCode),codeNameCode);
            }
        });


        getBackGroundLayout().setOnShareButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimerRefreshToolsSingel.stopTimer();
    }

    /** 显示底部弹出的货币对*/

//    }public void showDialog(){
//        View  contentView  = View.inflate(this, R.layout.foreign_details_dialog, null);
//        AlertDialog.Builder builder=new AlertDialog.Builder(this).setView(contentView);  //先得到构造器
//        ListView listView = (ListView) contentView.findViewById(R.id.listview_currency);
//        TextView itemTv = (TextView) contentView.findViewById(R.id.cancel_button);
//        ForeignDialogAdapter foreignDialogAdapter = new ForeignDialogAdapter(IsForexTwoWayDetailActivity.this, currencyList);
//        listView.setAdapter(foreignDialogAdapter);
//        foreignDialogAdapter.setItemClickListener(codeItemQuery);
//        dialog = builder.create();
//        Window dialogWindow = dialog.getWindow();
//        dialogWindow.setGravity(Gravity.BOTTOM);  //设置位置
//        dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); //设置dialog的宽高属性
//        dialog.show();
//
//        itemTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });

    /** 货币对点击事件*/
    private AdapterView.OnItemClickListener codeItemQuery = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            sePosition = position;
//            String currncy = sort_code_List.get(position);
            Map<String,Object> selectCcygrpsMap = currencyList.get(position);
            ccygrp = getCcygrps(selectCcygrpsMap);
            kType = "OK";
            tendencyType = "O";
            //对title设置
            sourceCode = (String)currencyList.get(sePosition).get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
            targetCode = (String)currencyList.get(sePosition).get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
            if(BaseDroidApp.getInstance().isLogin()){
                customFlag = (String)currencyList.get(sePosition).get("customFlag");
                if(("1").equals(customFlag)){
                    setDrawable(image_collect,getResources().getDrawable(R.drawable.share_already_collect));
                }else {
                    setDrawable(image_collect,getResources().getDrawable(R.drawable.share_not_collected));
                }
            }
            numberDigits = (int)DictionaryData.getParamByKeyAndValue(sourceCode,targetCode,DictionaryData.getInvestCurrencyCodeList());
            currencyLeftName = (String)LocalData.Currency.get(sourceCode);
            currencyRigthName = (String)LocalData.Currency.get(targetCode);
            if(LocalData.goldLists.contains(currencyLeftName)||LocalData.goldLists.contains(currencyRigthName)){
                currencyLeftName = (String)LocalData.code_Map_Left.get(sourceCode);
                currencyRigthName = (String)LocalData.code_Map_Right.get(targetCode);
            }
            currencyLeft.setText(currencyLeftName);
            currencyRight.setText(currencyRigthName);
            queryKTendencyView.setLandscapeCurCode(currencyLeftName + "/" + currencyRigthName);
            queryKTendencyView.resetData(); // 清空K线图数据
            getQueryKTendencyData(); // 获取K线图数据
            getQueryAverageTendency(); // 获取趋势图查询
            getQuerySingelQuotation(ccygrp);
            isStart = true;
            BaseDroidApp.getInstanse().dismissMessageDialog(); // 关闭弹框
        }
    };
    /** 获取到请求是的货币对信息*/
    public String getCcygrps(Map<String,Object> map){
        sourceCurrencyCodeStr = (String) map.get(Prms.PRELOGIN_QUERY_SOURCECURRENCYCODE);
//        if (LocalData.Currency.containsKey(sourceCurrencyCodeStr)) {
//            sourceDealCode = LocalData.Currency.get(sourceCurrencyCodeStr);// 得到源货币的代码
//
//        }
        targetCurrencyStr = (String) map.get(Prms.PRELOGIN_QUERY_TARGETCURRENCYCODE);
//        if (LocalData.Currency.containsKey(targetCurrencyStr)) {
//            targetDealCode = LocalData.Currency.get(targetCurrencyStr); //得到目标货币代码
//        }
        ccygrp = sourceCurrencyCodeStr + targetCurrencyStr;
        return ccygrp;
    }

    private void initUi() {
        btn_buy = (Button)findViewById(R.id.btn_buy);
        btn_sale = (Button)findViewById(R.id.btn_sale);
        tv_referPrice = (TextView) findViewById(R.id.tv_referPrice);
        tv_currDiff = (TextView)findViewById(R.id.tv_currDiff);
        tv_currPercentDiff = (TextView)findViewById(R.id.tv_currPercentDiff);
        if(!isQrcode){
            flag = (String)getIntent().getStringExtra("flag");
            String sourceCurrency = (String)currencyList.get(sePosition).get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
            String targetCurrency= (String)currencyList.get(sePosition).get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
            ccygrp = sourceCurrency+targetCurrency;
        }else {
            ccygrp = sourceCode+targetCode;
        }
        queryKTendencyView = (InverstKLineView)findViewById(R.id.query_KTendency_view); // K线图

//        mTabView = (TabView)findViewById(R.id.tabView);
//        mTabView.setContentText("1小时;4小时;日;周;月");
//        mTabView.setCurSelectedIndex(0);
        queryKTendencyView.setRrefreshKLineDataListener(queryKTendencyClick);
        queryKTendencyView.setContentText("1小时;4小时;日;周;月");
        queryKTendencyView.setCurSelectedIndex(2);
        queryKTendencyView.setLandscapeAllRateList(currencyList);
        queryKTendencyView.setLandscapeCurCodeItemClickListener(codeItemQuery);
        queryKTendencyView.setLandscapeCurCode(currencyLeftName + "/" + currencyRigthName);
        timeZones = queryKTendencyView.getNewTimeZone(); //获取更新时间
//        image_collect = (ImageView)findViewById(R.id.image_collect);
        image_collect = (TextView)findViewById(R.id.image_collect);
        if(BaseDroidApp.getInstance().isLogin()){
            image_collect.setVisibility(View.VISIBLE);
        }else {
            image_collect.setVisibility(View.GONE);
        }
        tv_saleRate = (TextView)findViewById(R.id.tv_saleRate);
        tv_buyRate = (TextView)findViewById(R.id.tv_buyRate);
    }

    private void initData(){
//        kType = "OK";
//        tendencyType = "O";
//        Intent intent = getIntent();
        if(isQrcode){
            if(BaseDroidApp.getInstance().isLogin()){
                customFlag = (String)singleDataMap.get("customFlag");
                if(("1").equals(customFlag)){
//                image_collect.setBackgroundDrawable(getResources().getDrawable(R.drawable.isforex_collected));
                    setDrawable(image_collect,getResources().getDrawable(R.drawable.share_already_collect));
                }else {
//                image_collect.setBackgroundDrawable(getResources().getDrawable(R.drawable.isforex_collection));
                    setDrawable(image_collect,getResources().getDrawable(R.drawable.share_not_collected));
                }
            }
        }else {
            singleDataMap = new HashMap<String, Object>();
            //单笔数据
            singleDataMap = (Map<String,Object>) BaseDroidApp.getInstanse().getBizDataMap().get("singleDataMap");
            customerRateList = new ArrayList<Map<String, Object>>();
            customerRateList = (List<Map<String, Object>>)BaseDroidApp.getInstance().getBizDataMap().get("customerRateList");
//        Map<String,Object> currentItem = totalDataList.get(0);
            if(BaseDroidApp.getInstance().isLogin()){
                customFlag = (String)currencyList.get(sePosition).get("customFlag");
                if(("1").equals(customFlag)){
//                image_collect.setBackgroundDrawable(getResources().getDrawable(R.drawable.isforex_collected));
                    setDrawable(image_collect,getResources().getDrawable(R.drawable.share_already_collect));
                }else {
//                image_collect.setBackgroundDrawable(getResources().getDrawable(R.drawable.isforex_collection));
                    setDrawable(image_collect,getResources().getDrawable(R.drawable.share_not_collected));
                }
            }
        }
        String referPrice = String.valueOf(singleDataMap.get("referPrice"));
        if(StringUtil.isNullOrEmpty(referPrice)){
            tv_referPrice.setText("--");
        }else {
            tv_referPrice.setText(referPrice);
        }
        if(isQrcode){
            numberDigits = (int)DictionaryData.getParamByKeyAndValue(sourceCode,targetCode,DictionaryData.getInvestCurrencyCodeList());
        }else {
            String sourceCurrency = (String)currencyList.get(sePosition).get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
            String targetCurrency= (String)currencyList.get(sePosition).get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
            numberDigits = (int)DictionaryData.getParamByKeyAndValue(sourceCurrency,targetCurrency,DictionaryData.getInvestCurrencyCodeList());
        }

        String buyRate = String.valueOf(singleDataMap
                .get(Prms.PRELOGIN_QUERY_BUYRATE));
        String sellRate = String.valueOf(singleDataMap
                .get(Prms.PRELOGIN_QUERY_SELLRATE));
        if(StringUtil.isNullOrEmpty(sellRate)){
            tv_saleRate.setText("卖出价"+"--");
        }else{
            sellRate = parseStringPattern(sellRate,numberDigits);
            tv_saleRate.setText("卖出价"+sellRate);
        }
        if(StringUtil.isNullOrEmpty(buyRate)){
            tv_buyRate.setText("买入价"+"--");
        }else {
            buyRate = parseStringPattern(buyRate, numberDigits);
            tv_buyRate.setText("买入价"+buyRate);
        }

        //今日涨跌幅
        String currPercentDiff = (String) singleDataMap.get("currPercentDiff");
        //涨跌值
        String currDiff = (String)singleDataMap.get("currDiff");

        if(StringUtil.isNullOrEmpty(currDiff)){
            tv_currDiff.setTextColor(getResources().getColor(R.color.share_gray_color));
            tv_currDiff.setText("--");
        }else {
            //先格式化，然后去掉无效尾0   格式化会把+去掉
            if(currDiff.contains("+")){
                currDiff = StringTools.parseStringPattern(currDiff,5);
                currDiff = StringTools.subZeroAndDot(currDiff);
                currDiff = "+"+currDiff;
                tv_currDiff.setTextColor(getResources().getColor(R.color.btn_pink));
            }else if(currDiff.contains("-")) {
                currDiff = StringTools.parseStringPattern(currDiff,5);
                currDiff = StringTools.subZeroAndDot(currDiff);
                tv_currDiff.setTextColor(getResources().getColor(R.color.fonts_green));
            }else {
                currDiff = StringTools.parseStringPattern(currDiff,5);
                currDiff = StringTools.subZeroAndDot(currDiff);
                tv_currDiff.setTextColor(getResources().getColor(R.color.lianlong_color_bdc7cb));
            }
//            if(currDiff.contains("+")){
//                tv_currDiff.setTextColor(getResources().getColor(R.color.btn_pink));
//            }else if(currDiff.contains("-")){
//                tv_currDiff.setTextColor(getResources().getColor(R.color.fonts_green));
//            }else {
//                tv_currDiff.setTextColor(getResources().getColor(R.color.lianlong_color_bdc7cb));
//            }
//            currDiff = parseStringPattern(currDiff,5);
            tv_currDiff.setText(currDiff);
        }
        if(StringUtil.isNullOrEmpty(currPercentDiff)){
            tv_currPercentDiff.setText("--");
            tv_currPercentDiff.setBackgroundResource(R.drawable.shape_llbt_round_gray);
            tv_referPrice.setTextColor(getResources().getColor(R.color.lianlong_color_bdc7cb));
            queryKTendencyView.setLandscapeTitleBackground(getResources().getColor(R.color.share_gray_color));
        }else {
            if(currPercentDiff.contains("+")){
                tv_referPrice.setTextColor(getResources().getColor(R.color.btn_pink));
                tv_currPercentDiff.setBackgroundResource(R.drawable.shape_llbt_round_red);
                queryKTendencyView.setLandscapeTitleBackground(getResources().getColor(R.color.boc_text_color_red));
            }else if(currPercentDiff.contains("-")){
                tv_referPrice.setTextColor(getResources().getColor(R.color.fonts_green));
                tv_currPercentDiff.setBackgroundResource(R.drawable.shape_llbt_round_green);
                queryKTendencyView.setLandscapeTitleBackground(getResources().getColor(R.color.share_green_color));
            }else {
                tv_referPrice.setTextColor(getResources().getColor(R.color.lianlong_color_bdc7cb));
                tv_currPercentDiff.setBackgroundResource(R.drawable.shape_llbt_round_gray);
                queryKTendencyView.setLandscapeTitleBackground(getResources().getColor(R.color.share_gray_color));
            }
            currPercentDiff = StringTools.parseStringPattern5(currPercentDiff);
            tv_currPercentDiff.setText(currPercentDiff);
        }
        queryKTendencyView.setRateTextRefresh(tv_referPrice.getText().toString(),
                tv_currDiff.getText().toString(),tv_currPercentDiff.getText().toString());
        //开盘价
        openPrice = (String)singleDataMap.get("openPrice");
        maxPrice = (String)singleDataMap.get("maxPrice");
        //最低价
        minPrice = (String)singleDataMap.get("minPrice");

        //单笔详情时间
        priceTime = (String)singleDataMap.get("priceTime");

        queryKTendencyView.setPriceAndTimeText(priceTime, openPrice, maxPrice, minPrice);
        //七秒刷新
//        mTimerRefreshToolsSingel.startTimer();
    }

    private void setDrawable(TextView tv, Drawable drawable){
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        tv.setCompoundDrawables(null,null,drawable,null);
    }

    public void getQuerySingelQuotation(final String ccygrp) {
        QuerySingelQuotationRequestParams querySingelQuotation = new QuerySingelQuotationRequestParams(ccygrp, flag, "M");
        HttpHelp h = HttpHelp.getInstance();
        h.postHttpFromSF(this, querySingelQuotation);
        h.setHttpErrorCallBack(new IHttpErrorCallBack() {
            @Override
            public boolean onError(String exceptionMessage, Object extendParams) {
                getRefreshData(null);
                return true;
            }
        });
        h.setOkHttpErrorCode(new IOkHttpErrorCode() {
            @Override
            public boolean handlerErrorCode(BaseResponseData responseData, Object extendParams) {
                getRefreshData(null);
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

//                getQueryAverageTendency();
//                getQueryKTendencyData();
                return false;
            }
        });
    }

    TimerRefreshTools mTimerRefreshToolsSingel = new TimerRefreshTools(7000, new TimerRefreshTools.ITimerRefreshListener() {
        @Override
        public void onRefresh() {
            QuerySingelQuotationRequestParams querySingelQuotation = new QuerySingelQuotationRequestParams(ccygrp,flag , "M");
            HttpHelp h = HttpHelp.getInstance();
            h.postHttpFromSF(IsForexTwoWayDetailActivity.this, querySingelQuotation);
            h.setHttpErrorCallBack(new IHttpErrorCallBack() {
                @Override
                public boolean onError(String exceptionMessage, Object extendParams) {
                    getRefreshData(null);
                    return true;
                }
            });
            h.setOkHttpErrorCode(new IOkHttpErrorCode() {
                @Override
                public boolean handlerErrorCode(BaseResponseData responseData, Object extendParams) {
                    getRefreshData(null);
                    return true;
                }
            });
            h.setHttpResponseCallBack(new IHttpResponseCallBack() {
                @Override
                public boolean responseCallBack(String response, Object extendParams) {
                    QuerySingleQuotationResponseData data = GsonTools.fromJson(response, QuerySingleQuotationResponseData.class);
                    QuerySingleQuotationResult item = data.getBody();

                    getRefreshData(item);

//                    getQueryAverageTendency();
//                    getQueryKTendencyData();
                    return false;
                }
            });
        }
    });

    @Override
    protected void onResume() {
        super.onResume();
        mTimerRefreshToolsSingel.startTimer();
        queryKTendencyView.startRefreshData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTimerRefreshToolsSingel.stopTimer();
        queryKTendencyView.stopRefreshData();
    }

    private void getRefreshData(QuerySingleQuotationResult singleItem) {
//        targetCurrencyStr = singleItem.targetCurrencyCode;
//        sourceCurrencyCodeStr = singleItem.sourceCurrencyCode;
//        if (ccygrp.equals(sourceCurrencyCodeStr + targetCurrencyStr)) {
        if(StringUtil.isNullOrEmpty(singleItem)){
            tv_saleRate.setText("卖出价" + "--");
            tv_buyRate.setText("买入价" + "--");
            tv_currDiff.setTextColor(getResources().getColor(R.color.share_gray_color));
            tv_currDiff.setText("--");
            tv_currPercentDiff.setBackgroundResource(R.drawable.shape_llbt_round_gray);
            tv_currPercentDiff.setText("--");
            tv_referPrice.setTextColor(getResources().getColor(R.color.lianlong_color_bdc7cb));
            queryKTendencyView.setLandscapeTitleBackground(getResources().getColor(R.color.share_gray_color));
            tv_referPrice.setText("--");
            queryKTendencyView.setRateTextRefresh("--","--","--");
            priceTime = "";
            openPrice = "";
            maxPrice = "";
            minPrice = "";
            queryKTendencyView.setPriceAndTimeText(priceTime, openPrice, maxPrice, minPrice);

        }else {
            if(!StringUtil.isNullOrEmpty(singleItem.buyRate)){
                buyPrice = singleItem.buyRate;
            }
            if(!StringUtil.isNullOrEmpty(singleItem.sellRate)){
                sellPrice = singleItem.sellRate;
            }

//        numberDigits = (int)DictionaryData.getParamByKeyAndValue(sourceCurrencyCodeStr,targetCurrencyStr,DictionaryData.getInvestCurrencyCodeList());
            if(StringUtil.isNullOrEmpty(sellPrice)){
                tv_saleRate.setText("卖出价" + "--");
            }else {
                sellPrice = parseStringPattern(sellPrice,numberDigits);
                tv_saleRate.setText("卖出价" + sellPrice);
            }
            if(StringUtil.isNullOrEmpty(buyPrice)){
                tv_buyRate.setText("买入价" + "--");
            }else {
                buyPrice = parseStringPattern(buyPrice, numberDigits);
                tv_buyRate.setText("买入价" + buyPrice);
            }
            if(StringUtil.isNullOrEmpty(singleItem.currDiff)){
                tv_currDiff.setTextColor(getResources().getColor(R.color.share_gray_color));
                tv_currDiff.setText("--");
            }else {
                if((singleItem.currDiff).contains("+")){
                    tv_currDiff.setTextColor(getResources().getColor(R.color.btn_pink));
                }else if((singleItem.currDiff).contains("-")){
                    tv_currDiff.setTextColor(getResources().getColor(R.color.fonts_green));
                }else {
                    tv_currDiff.setTextColor(getResources().getColor(R.color.share_gray_color));
                }
                //先格式化成5位，然后去掉无效尾0
                currDifft = StringTools.parseStringPattern(singleItem.currDiff,5);
                currDifft = StringTools.subZeroAndDot(currDifft);
                if(singleItem.currDiff.contains("+")){
                    tv_currDiff.setText("+"+currDifft);
                }else {
                    tv_currDiff.setText(currDifft);
                }

            }
            if(StringUtil.isNullOrEmpty(singleItem.currPercentDiff)){
                tv_currPercentDiff.setBackgroundResource(R.drawable.shape_llbt_round_gray);
                tv_referPrice.setTextColor(getResources().getColor(R.color.lianlong_color_bdc7cb));
                queryKTendencyView.setLandscapeTitleBackground(getResources().getColor(R.color.share_gray_color));
                tv_currPercentDiff.setText("--");
            }else {
                if(singleItem.currPercentDiff.contains("+")){
                    tv_currPercentDiff.setBackgroundResource(R.drawable.shape_llbt_round_red);
                    tv_referPrice.setTextColor(getResources().getColor(R.color.btn_pink));
                    queryKTendencyView.setLandscapeTitleBackground(getResources().getColor(R.color.boc_text_color_red));
                }else if(singleItem.currPercentDiff.contains("-")){
                    tv_currPercentDiff.setBackgroundResource(R.drawable.shape_llbt_round_green);
                    tv_referPrice.setTextColor(getResources().getColor(R.color.fonts_green));
                    queryKTendencyView.setLandscapeTitleBackground(getResources().getColor(R.color.share_green_color));
                }else {
                    tv_currPercentDiff.setBackgroundResource(R.drawable.shape_llbt_round_gray);
                    tv_referPrice.setTextColor(getResources().getColor(R.color.lianlong_color_bdc7cb));
                    queryKTendencyView.setLandscapeTitleBackground(getResources().getColor(R.color.share_gray_color));
                }
                tv_currPercentDiff.setText(StringTools.parseStringPattern5(singleItem.currPercentDiff));
            }
            if(StringUtil.isNullOrEmpty(singleItem.referPrice)){
                tv_referPrice.setText("--");
            }else {
                tv_referPrice.setText(singleItem.referPrice);
            }

            queryKTendencyView.setRateTextRefresh(tv_referPrice.getText().toString(),
                    tv_currDiff.getText().toString(),tv_currPercentDiff.getText().toString());
            //开盘价
            openPrice = (String)singleItem.openPrice;
            maxPrice = (String)singleItem.maxPrice;
            //最低价
            minPrice = (String)singleItem.minPrice;

            //单笔详情时间
            priceTime = (String)singleItem.priceTime;


            //单笔详情时间
//            priceTime = (String)singleDataMap.get("priceTime");
            queryKTendencyView.setPriceAndTimeText(priceTime, openPrice, maxPrice, minPrice);

        }

        if(isStart){
            isStart = false;
            mTimerRefreshToolsSingel.startTimer();
        }
//        sourceCode = (String)currencyList.get(sePosition).get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
//        targetCode = (String)currencyList.get(sePosition).get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
//        currencyLeftName = (String)LocalData.Currency.get(sourceCode);
//        currencyRigthName = (String)LocalData.Currency.get(targetCode);
//        if(LocalData.goldLists.contains(currencyLeftName)||LocalData.goldLists.contains(currencyRigthName)){
//            currencyLeftName = (String)LocalData.code_Map_Left.get(sourceCode);
//            currencyRigthName = (String)LocalData.code_Map_Right.get(targetCode);
//        }
//        currencyLeft.setText(currencyLeftName);
//        currencyRight.setText(currencyRigthName);
//        String title = LocalData.code_Map.get(ccygrp);
//        if(StringUtil.isNullOrEmpty(title)){
//            title = (String)(LocalData.Currency.get(sourceCurrencyCodeStr)+"/"+LocalData.Currency.get(targetCurrencyStr));
//        }
//        getBackGroundLayout().setTitleText(title);
    }

    /** K线点击事件*/
    private IRefreshKLineDataListener queryKTendencyClick = new IRefreshKLineDataListener() {
        @Override
        public void onRefreshKLineDataCallBack(int nIndex, int nShowType, boolean isBackgroundRefresh) {
            timeZones = queryKTendencyView.getNewTimeZone(); //获取更新时间
            if (nShowType == 1) { //刷新K线图
                if (nIndex == 0){ // 一小时K线图
                    kType = "OK";
                } else if(nIndex == 1) { // 四小时K线图
                    kType = "FK";
                } else if(nIndex == 2) { // 日K线图
                    kType = "DK";
                } else if(nIndex == 3) { // 周K线图
                    kType = "WK";
                }else if(nIndex == 4) { // 月K线图
                    kType = "MK";
                }
                getQueryKTendencyData(); // 刷新K线图数据
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
                getQueryAverageTendency(); // 刷新趋势图查询
            }
        }
    };

    public void getQueryKTendencyData(){
        QuerykTendencyRequestParams queryKTendency= new QuerykTendencyRequestParams(ccygrp, kType, flag, "M", timeZones);
        HttpHelp help = HttpHelp.getInstance();
        help.postHttpFromSF(this,queryKTendency);
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
                QueryKTendencyResponseData data= GsonTools.fromJson(response, QueryKTendencyResponseData.class);
                QueryKTendencyResult body = data.getBody();
                queryKTendencyView.setKLineData(body);
                queryKTendencyView.startRefreshData();
                return false;
            }
        });
    }

    /** 查询趋势图    2.4 贵金属趋势图查询 -API*/
//    map.put("cardType","G");
//    map.put("cardClass", "R");
    public void getQueryAverageTendency(){
        QueryAverageTendencyRequestParams queryAverageTendency = new QueryAverageTendencyRequestParams(ccygrp,flag,"M",tendencyType);
        HttpHelp help = HttpHelp.getInstance();
        help.postHttpFromSF(this,queryAverageTendency);
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
                QueryAverageTendencyResponseData result = GsonTools.fromJson(response,QueryAverageTendencyResponseData.class);
                QueryAverageTendencyResult body =  result.getBody();
                //该方法已在控件中实现  linyl
//                queryKTendencyView.setQuShiData("yyyy\nMM-dd","yyyy-MM-dd","参考价格：","时间：","参考价格",2,"yyyy-MM-dd HH:mm:ss","暂无数据");
                queryKTendencyView.setECharsViewData(body);
                queryKTendencyView.startRefreshData();
                return false;
            }
        });
    }

    private void initListener() {

        btn_buy.setOnClickListener(this);
        btn_sale.setOnClickListener(this);
        image_collect.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.image_collect:
                //在未收藏的情况下点击（即 收藏），先判断个数
                if("0".equals(customFlag)){
                    if(customerRateList.size()>5){
                        BaseDroidApp.getInstanse().showInfoMessageDialog(
                                IsForexTwoWayDetailActivity.this.getString(R.string.forex_rate_makecode));
                        return;
                    }else{
                        setDrawable(image_collect,getResources().getDrawable(R.drawable.share_already_collect));
                        customFlag = "1";
                        customerRateList =changeCustemFlag();
                        BaseHttpEngine.showProgressDialog();
                        getSelectedPosition();
                    }
                }else {//取消收藏
                    if(customerRateList.size()==1){
                        BaseDroidApp.getInstanse().showInfoMessageDialog(
                                IsForexTwoWayDetailActivity.this.getString(R.string.forex_the_last_pair));
                        return;
                    }
                    customFlag = "0";
                    setDrawable(image_collect,getResources().getDrawable(R.drawable.share_not_collected));
                    customerRateList =changeCustemFlag();
                    BaseHttpEngine.showProgressDialog();
                    getSelectedPosition();
                }
                break;
            case R.id.btn_buy:
                mTimerRefreshToolsSingel.stopTimer();
                buyOrSale = BUY;
                buyOrSell = "买入";
                if(isQrcode){
                    if (!StringUtil.isNull(sourceCode) && !StringUtil.isNull(targetCode)) {
                        String sourceCurrency = LocalData.Currency.get(sourceCode);
                        String targetCurrency = LocalData.Currency.get(targetCode);
                        codeName = sourceCurrency + "/" + targetCurrency;
//			人民币金、银、铂、钯、美元金、银、铂、钯   贵金属的币种
                        if(LocalData.goldLists.contains(sourceCode)||LocalData.goldLists.contains(targetCode)){
                            flag = "G";
                            codeName = LocalData.code_Map.get(sourceCode+targetCode);
                        }else {
                            flag = "F";
                        }
                    }
                }else {
                    codeName = getCodeName(currencyList, sePosition);
                }
                exeRatePsnForexActIsset(flag);
                break;
            case R.id.btn_sale:
                mTimerRefreshToolsSingel.stopTimer();
                buyOrSale = SALE;
                buyOrSell = "卖出";
                if(isQrcode){
                    if (!StringUtil.isNull(sourceCode) && !StringUtil.isNull(targetCode)) {
                        String sourceCurrency = LocalData.Currency.get(sourceCode);
                        String targetCurrency = LocalData.Currency.get(targetCode);
                        codeName = sourceCurrency + "/" + targetCurrency;
//			人民币金、银、铂、钯、美元金、银、铂、钯   贵金属的币种
                        if(LocalData.goldLists.contains(sourceCode)||LocalData.goldLists.contains(targetCode)){
                            flag = "G";
                            codeName = LocalData.code_Map.get(sourceCode+targetCode);
                        }else {
                            flag = "F";
                        }
                    }
                }else {
                    codeName = getCodeName(currencyList, sePosition);
                }
                exeRatePsnForexActIsset(flag);
                break;

            default:

                break;
        }
    }
    private List<Map<String, Object>> changeCustemFlag(){
        Map<String, Object> map = new HashMap<String, Object>();
        if(isQrcode){
            map.put("customFlag", customFlag);
            map.put("sourceCurrencyCode",sourceCode);
            map.put("targetCurrencyCode",targetCode);
        }else {
            map = currencyList.get(sePosition);
            map.put("customFlag", customFlag);
        }
        //取消收藏 说明之前自选已经有该币种
        if(customFlag.equals("0")){
            String customerSourceCode = null;
            String customerTargetCode = null;
            for(int i=0; i<customerRateList.size(); i++){
                customerSourceCode = (String)customerRateList.get(i).get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
                customerTargetCode = (String)customerRateList.get(i).get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
                if(sourceCode.equals(customerSourceCode)&&targetCode.equals(customerTargetCode)){
                    customerRateList.remove(i);
                    break;
                }
            }
        }else {
            //收藏币种
            customerRateList.add(map);
        }
        return customerRateList;
    }
    /**点击收藏 获取选择的币种*/
    private void getSelectedPosition(){
        int position = sePosition;
        /**需要把剩余货币对 组成数组上送给后台*/
        /** 存储用户选择的源货币对代码 */
        List<String> selectedCodeList = new ArrayList<String>();
        /** 存储用户选择的目标货币对代码 */
        List<String> selectedTargetCodeList = new ArrayList<String>();
        for(int i=0; i<customerRateList.size(); i++){
            Map<String, Object> choiseMap = customerRateList.get(i);
            String sourceCurCde = (String) choiseMap.get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
            String targetCurCde = (String) choiseMap.get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
            if (!StringUtil.isNull(sourceCurCde) && !StringUtil.isNull(targetCurCde)) {
                selectedCodeList.add(sourceCurCde);
                selectedTargetCodeList.add(targetCurCde);
            }
        }
        if (selectedCodeList == null || selectedCodeList.size() <= 0 || selectedTargetCodeList == null
                || selectedTargetCodeList.size() <= 0) {
            return;
        }else {
            int len1 = selectedCodeList.size();
            int len2 = selectedTargetCodeList.size();
            if (len1 == len2) {
//                    length = len1;
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
        }
        // 货币对提交
        requestPsnVFGRateSetting(ConstantGloble.ISFOREX_UPDATE, selectedArr);
    }
    /** 汇率定制 */
    private void requestPsnVFGRateSetting(String submitType, String[] list) {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGRATESETTING_API);
        biiRequestBody.setConversationId(commConversationId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(IsForex.ISFOREX_SUBMITTYPE_REQ, submitType);
        map.put(IsForex.ISFOREX_HIDDENCURRENCYPAIR_REQ, list);
        biiRequestBody.setParams(map);
        HttpManager.requestBii(biiRequestBody, this, "requestPsnVFGRateSettingCallback");
    }
    /** 汇率定制 -----回调 */
    public void requestPsnVFGRateSettingCallback(Object resultObj) {
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        List<Map<String, String>> result = (List<Map<String, String>>) biiResponseBody.getResult();
        BaseHttpEngine.dissMissProgressDialog();
        if("1".equals(customFlag)){
            CustomDialog.toastInCenter(this, "加入自选");
        }else {
            CustomDialog.toastInCenter(this, "取消自选");
        }

    }
    /** 得到用户选择的货币对名称 */
    private String getCodeName(List<Map<String, Object>> lists, int position) {
        String codeNames = null;
        Map<String, Object> map = lists.get(position);
        String sourceCurrencyCode = (String) map.get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
        String targetCurrencyCode = (String) map.get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
        // 货币对
        if (!StringUtil.isNull(sourceCurrencyCode) && !StringUtil.isNull(targetCurrencyCode)) {
            String sourceCurrency = LocalData.Currency.get(sourceCurrencyCode);
            String targetCurrency = LocalData.Currency.get(targetCurrencyCode);
            codeNames = sourceCurrency + "/" + targetCurrency;
//			人民币金、银、铂、钯、美元金、银、铂、钯   贵金属的币种
            if(LocalData.goldLists.contains(sourceCurrency)||LocalData.goldLists.contains(targetCurrency)){
                flag = "G";
                codeNames = LocalData.code_Map.get(sourceCurrencyCode+targetCurrencyCode);
            }else {
                flag = "F";
            }
        }
        return codeNames;
    }
    // ////////////////////////////////全部数据未登录///////////////////////////////////////
    private void exeRatePsnForexActIsset(final String vfgType) {
        // 执行登陆
        BaseActivity.getLoginUtils(IsForexTwoWayDetailActivity.this).exe(new LoginTask.LoginCallback() {
            @Override
            public void loginStatua(boolean isLogin) {
                if (isLogin) {
                    if(isOpen && isSettingAcc){
                        BaseHttpEngine.showProgressDialog();
                        requestAllRate(flag);
                    }else {
                        //外置登陆成功先判断 开通逻辑 在判断 是否有买入资格 如果有 则跳入买入页面
                        commConversationId = (String)BaseDroidApp.getInstance().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
                        BaseHttpEngine.showProgressDialog();
                        if(StringUtil.isNull(commConversationId)){
                            //请求登陆后的commConversationId
                            requestCommConversationId();
                        }else{
                            // 判断用户是否开通投资理财服务
                            requestPsnInvestmentManageIsOpen();
                        }
                    }
                }
            }
        });
    }
    @Override
    public void requestCommConversationIdCallBack(Object resultObj) {
        super.requestCommConversationIdCallBack(resultObj);
        commConversationId = (String)BaseDroidApp.getInstance().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
        if(StringUtil.isNullOrEmpty(commConversationId)){
            BaseHttpEngine.dissMissProgressDialog();
            return;
        }else {
            //判断用户是否开通投资理财
            requestPsnInvestmentManageIsOpen();
        }
    }
    /** 判断是否开通投资理财服务--二级菜单----回调 */
    @Override
    public void requestMenuIsOpenCallback(Object resultObj) {
        super.requestMenuIsOpenCallback(resultObj);
        if (isOpen) {
            // 查询是否签约保证金产品
            searchBaillAfterOpen = 1;
            requestPsnVFGBailListQueryCondition();
        } else {
            BaseHttpEngine.dissMissProgressDialog();
            searchBaillAfterOpen = 2;
            getPop();
        }
    }
    /** 查询是否签约保证金产品--------回调 */
    @Override
    public void requestPsnVFGBailListQueryConditionCallback(Object resultObj) {
        super.requestPsnVFGBailListQueryConditionCallback(resultObj);
        List<Map<String, String>> result = (List<Map<String, String>>) BaseDroidApp.getInstanse().getBizDataMap()
                .get(ConstantGloble.ISFOREX_RESULT_SIGN);
        if (result == null || result.size() <= 0) {
            isSign = false;
            isSettingAcc = false;
            BaseHttpEngine.dissMissProgressDialog();
            // 弹出任提示框
            getPop();
            return;
        } else {
            isSign = true;
            isCondition = true;
            // 是否设置双向宝账户
            requestPsnVFGGetBindAccount();
        }
    }
    /** 判断是否设置外汇双向宝账户 --------条件判断 */
    @Override
    public void requestConditionAccountCallback(Object resultObj) {
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        // 得到response
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        Map<String, String> accReaultMap = (Map<String, String>) biiResponseBody.getResult();
        if (StringUtil.isNullOrEmpty(accReaultMap)) {
            isSettingAcc = false;
        } else {
            String accountNumber = accReaultMap.get(IsForex.ISFOREX_ACCOUNTNUMBER_REQ);
            if (StringUtil.isNull(accountNumber)) {
                isSettingAcc = false;
            } else {
                isSettingAcc = true;
                BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.ISFOREX_ACCREAULTMAP, accReaultMap);
            }
        }
        if (isOpen && isSign && isSettingAcc) {
            // 都开通了 判断改用户是否有买入资格
            requestAllRate(flag);
        } else {
            BaseHttpEngine.dissMissProgressDialog();
            // 弹出任提示框
            getPop();
            return;
        }
    }

    @Override
    public void requestAllRateCallback(Object resultObj) {
        super.requestAllRateCallback(resultObj);
        if (codeResultList == null || codeResultList.size() <= 0) {
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(
                    IsForexTwoWayDetailActivity.this.getResources().getString(R.string.isForex_trade_code));
            return;
        }
        // 查询结算币种
        requestPsnVFGGetRegCurrency(flag);
    }
    // 查询结算币种----回调
    @Override
    public void requestPsnVFGGetRegCurrencyCallback(Object resultObj) {
        super.requestPsnVFGGetRegCurrencyCallback(resultObj);
        if (vfgRegCurrencyList == null || vfgRegCurrencyList.size() <= 0) {
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(
                    IsForexTwoWayDetailActivity.this.getResources().getString(R.string.isForex_trade_jcCode));
            return;
        }
        tradeSwitch();
    }
    /**跳转到买入页面*/
    private void tradeSwitch(){
        Intent intent = new Intent(IsForexTwoWayDetailActivity.this, IsForexTradeSubmitActivity.class);
        intent.putExtra(ConstantGloble.ISFOREX_CODECODENAME_KEY, codeName);
        intent.putExtra(ConstantGloble.ISFOREX_DIRECTION_KEY, buyOrSell);
        intent.putExtra(ConstantGloble.ISFOREX_REQUEXTTRADE_KEY, ConstantGloble.ISFOREX_MINE_RATE_ACTIVITY);// 401
        startActivityForResult(intent, ConstantGloble.ISFOREX_TRADE_CODE_ACTIVITY);
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

